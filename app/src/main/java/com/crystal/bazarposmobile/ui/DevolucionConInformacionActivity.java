package com.crystal.bazarposmobile.ui;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.common.Constantes;
import com.crystal.bazarposmobile.common.LogFile;
import com.crystal.bazarposmobile.common.SPM;
import com.crystal.bazarposmobile.db.BazarPosMovilDB;
import com.crystal.bazarposmobile.db.entity.ProductoEntity;
import com.crystal.bazarposmobile.retrofit.ApiService;
import com.crystal.bazarposmobile.retrofit.AppCliente;
import com.crystal.bazarposmobile.retrofit.response.ResponseConsultarDocumento;
import com.crystal.bazarposmobile.retrofit.response.eanes.Producto;
import com.crystal.bazarposmobile.ui.fragment.ProductoGripFragment;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DevolucionConInformacionActivity
        extends
            AppCompatActivity
        implements
            View.OnClickListener,
            ProductoGripFragment.OnListFragmentInteractionListener{

    //Declaración del Cliente REST
    ApiService apiService;
    String usuario = SPM.getString(Constantes.USER_NAME) + " - " + SPM.getString(Constantes.CAJA_CODE);

    //Declaración de los objetos de la interfaz del activity
    EditText etdoumento;
    Button btnbuscar,btndovolucion,btncerrar;
    ProgressBar pb;
    ProductoGripFragment productoGF;

    //Declaración de la variables del activity
    List<Producto> productosList = new ArrayList<>();
    List<Producto> productosCajaList = new ArrayList<>();
    String tienda,refEnVentaActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devolucion_con_informacion);
        this.setTitle(R.string.titulo_devolucion_con_informacion);

        tienda = SPM.getString(Constantes.TIENDA_CODE);
        refEnVentaActual = "";
        productosCajaList = (ArrayList<Producto>) getIntent().getSerializableExtra("listProductos");

        retrofitInit();
        findViews();
        eventos();
    }

    private void retrofitInit() {
        AppCliente appCliente = AppCliente.getInstance();
        apiService = appCliente.getApiService();
    }

    private void findViews() {
        etdoumento = findViewById(R.id.etReferenciaDCI);

        btnbuscar = findViewById(R.id.btnBuscarDCI);
        btndovolucion = findViewById(R.id.btnDevolcionDCI);
        btndovolucion.setVisibility(View.GONE);
        btncerrar = findViewById(R.id.btnCerrarDCI);

        pb = findViewById(R.id.progressBarDCI);
        pb.setVisibility(View.GONE);

        productoGF = (ProductoGripFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragmentGridDCI);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Obtendremos resultados de escaneo aquí
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        //Validar si la respuest es vacia
        if (result != null) {
            if (result.getContents() == null) {
                msjToast(getResources().getString(R.string.escaneo_cancelado));
            } else {
                etdoumento.setText(result.getContents());
            }
        }
    }

    private void eventos() {
        btnbuscar.setOnClickListener(this);
        btndovolucion.setOnClickListener(this);
        btncerrar.setOnClickListener(this);

        //Ocultar el teclado de pantalla
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        etdoumento.setImeActionLabel(getResources().getString(R.string._ir), KeyEvent.KEYCODE_ENTER);
        etdoumento.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    btnbuscar.callOnClick();
                    return true;
                }
                return false;
            }
        });

        etdoumento.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btnbuscar.callOnClick();
                }
                return handled;
            }
        });

        etdoumento.setOnLongClickListener(
                new View.OnLongClickListener() {
                    public boolean onLongClick(View view) {
                        new IntentIntegrator(DevolucionConInformacionActivity.this).setCaptureActivity(LectorCodigosCamaraActivity.class).initiateScan();
                        return false;
                    }
                }
        );
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.btnBuscarDCI:
                //Obtener el codigo del documento
                String documento = etdoumento.getText().toString();
                etdoumento.setText("");
                if(documento.length() > 0){
                    if(refEnVentaActual.contains(documento)){
                        msjToast(getResources().getString(R.string.numero_documento_repetido));
                    }else{
                        if(documento.length() > 13)
                            documento = documento.substring(0,13);
                        etdoumento.setText(documento);
                        addDocumento(documento);
                    }
                }else{
                    msjToast(getResources().getString(R.string.numero_documento_invalido));
                    etdoumento.requestFocus();
                }
                break;
            case R.id.btnDevolcionDCI:
                try {
                    realizarDevolucion();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btnCerrarDCI:
                finish();
                break;
        }
    }

    private void realizarDevolucion() throws ExecutionException, InterruptedException {

        iPB();
        btndovolucion.setVisibility(View.GONE);
        List<Producto> listProductosDevolucion = new ArrayList<Producto>();

        for(int i = 0; i < productosList.size(); i++){
            Log.e("logcat","getQuantity: "+productosList.get(i).getQuantity());
            if(productosList.get(i).getQuantity() < 0){
                Producto po = new GetProductoAsyncTask().execute(productosList.get(i).getEan()).get();
                productosList.get(i).setPrecioOriginal(po.getPrecioOriginal());
                productosList.get(i).setNombre(po.getNombre());
                productosList.get(i).setTalla(po.getTalla());
                productosList.get(i).setColor(po.getColor());
                productosList.get(i).setTipoTarifa(po.getTipoTarifa());
                productosList.get(i).setPeriodoTarifa(po.getPeriodoTarifa());
                productosList.get(i).setComposicion(po.getComposicion());
                productosList.get(i).setCodigoTasaImpuesto(po.getCodigoTasaImpuesto());
                productosList.get(i).setArticuloCerrado(po.getArticuloCerrado());
                productosList.get(i).setArticuloGratuito(po.getArticuloGratuito());
                productosList.get(i).setValorTasa(po.getValorTasa());
                productosList.get(i).setFechaTasa(po.getFechaTasa());
                productosList.get(i).setPeriodoActivo(po.getPeriodoActivo());
                productosList.get(i).setCodigoMarca(po.getCodigoMarca());
                productosList.get(i).setMarca(po.getMarca());
                productosList.get(i).setDescontable(po.getDescontable());
                productosList.get(i).setTipoPrendaCodigo(po.getTipoPrendaCodigo());
                productosList.get(i).setTipoPrendaNombre(po.getTipoPrendaNombre());
                productosList.get(i).setGeneroCodigo(po.getGeneroCodigo());
                productosList.get(i).setGeneroNombre(po.getGeneroCodigo());
                productosList.get(i).setCategoriaIvaCodigo(po.getCategoriaIvaCodigo());
                productosList.get(i).setCategoriaIvaNombre(po.getCategoriaIvaNombre());
                listProductosDevolucion.add(productosList.get(i));
            }
        }

        Intent i = new Intent(DevolucionConInformacionActivity.this,CajaActivity.class);
        i.putExtra("listProductosDevolucion", (Serializable) listProductosDevolucion);
        setResult(Constantes.RESP_DEV_CON_INFO, i);
        finish();
    }

    protected class GetProductoAsyncTask extends AsyncTask<String,Void, Producto> {

        @Override
        protected Producto doInBackground(String... ean) {
            try {
                ProductoEntity pe = BazarPosMovilDB.getBD(getApplication()).productoDao().getEAN(ean[0]);
                return new Producto(pe.getEan(),pe.getPrecio(),pe.getNombre(),pe.getTalla(),pe.getColor(),pe.getTipoTarifa(),pe.getTienda(),
                        pe.getPeriodoTarifa(),pe.getIp(), pe.getComposicion(),pe.getPrecioUnitario(),pe.getArticulo(),pe.getCodigoTasaImpuesto(),
                        pe.getArticuloCerrado(),pe.getArticuloGratuito(),pe.getTasaImpuesto(),pe.getPrecioSinImpuesto(),pe.getImpuesto(),
                        pe.getValorTasa(),pe.getFechaTasa(),pe.getPrecioOriginal(),pe.getPeriodoActivo(),pe.getCodigoMarca(),
                        pe.getMarca(),pe.getSerialNumberId(),pe.getVendedorId(),0,pe.getDescontable(),
                        pe.getTipoPrendaCodigo(),pe.getTipoPrendaNombre(),pe.getGeneroCodigo(),pe.getGeneroNombre(),
                        pe.getCategoriaIvaCodigo(),pe.getCategoriaIvaNombre(),pe.getLine());
            } catch (Exception e) {
                e.printStackTrace();
                msjToast("El EAN: "+ean[0]+", no pertence al bazar");
                return null;
            }
        }
    }

    private void addDocumento(final String documento) {
        //Consultar la SOAP para buscar documento
        iPB();
        Call<ResponseConsultarDocumento> call = apiService.doConsultarDocumento(usuario,documento);
        call.enqueue(new Callback<ResponseConsultarDocumento>() {
            @Override
            public void onResponse(Call<ResponseConsultarDocumento> call, Response<ResponseConsultarDocumento> response) {
                if(response.isSuccessful()){
                    if(response.body().getError()){
                        msjToast(response.body().getMensaje());
                        etdoumento.requestFocus();
                    }else{
                        boolean insertProduct = false;
                        int cont = 0;
                        for(Producto p:response.body().getProductos()){
                            if(p.getQuantity() > 0){
                                p.setMotivoDevolucionFactura(documento);
                                p.setMotivoDevolucionLinea(cont);
                                insertProduct = true;
                                productosList.add(p);
                            }
                            cont++;
                        }

                        if(insertProduct){
                            productoGF.setProducto(productosList);
                        }else{
                            msjToast(getResources().getString(R.string.devolucion_invalida));
                        }
                        refEnVentaActual = refEnVentaActual + ";" + documento;
                    }
                }else{
                    msjToast(getResources().getString(R.string.error_conexion_consultar_doc));
                    etdoumento.requestFocus();
                }
                pPB();
            }

            @Override
            public void onFailure(Call<ResponseConsultarDocumento> call, Throwable t) {
                LogFile.adjuntarLog("Error ResponseConsultarDocumento: " + call + t);
                etdoumento.requestFocus();
                pPB();
                msjToast(getResources().getString(R.string.error_conexion) + t.getMessage());
            }
        });
    }

    private void iPB() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        pb.setVisibility(View.VISIBLE);
    }

    private void pPB() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        pb.setVisibility(View.GONE);
    }

    @Override
    public void onListFragmentInteraction(Producto item, Integer position) {

        if(validarSiEstaEnCaja(item)){
            if(productosList.get(position).getPrecio().intValue() != 0){
                productosList.get(position).setPrecio(item.getPrecio()*-1);
            }
            productosList.get(position).setPrecioSinImpuesto(item.getPrecioSinImpuesto()*-1);
            productosList.get(position).setQuantity(item.getQuantity()*-1);
            productoGF.setProducto(productosList);
            btndovolucion.setVisibility(View.VISIBLE);
        }else{
            msjToast("Esta línea de venta ya está en uso");
        }
    }

    private boolean validarSiEstaEnCaja(Producto item) {
        if(productosCajaList != null)
            if(productosCajaList.size() > 0)
                for(int i = 0; i < productosCajaList.size(); i++)
                    if(item.getEan().equals(productosCajaList.get(i).getEan()))
                        if(item.getMotivoDevolucionFactura().equals(productosCajaList.get(i).getMotivoDevolucionFactura()))
                            if(item.getMotivoDevolucionLinea() == (productosCajaList.get(i).getMotivoDevolucionLinea()))
                                return false;
        return true;
    }

    private void msjToast(String msj) {
        Toast.makeText(DevolucionConInformacionActivity.this, msj, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
