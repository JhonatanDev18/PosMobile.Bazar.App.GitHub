package com.crystal.bazarposmobile.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.common.Constantes;
import com.crystal.bazarposmobile.common.LogFile;
import com.crystal.bazarposmobile.common.SPM;
import com.crystal.bazarposmobile.common.Utilidades;
import com.crystal.bazarposmobile.db.BazarPosMovilDB;
import com.crystal.bazarposmobile.db.entity.DocHeaderEntity;
import com.crystal.bazarposmobile.db.entity.DocLineEntity;
import com.crystal.bazarposmobile.db.entity.DocPaymentEntity;
import com.crystal.bazarposmobile.db.entity.MediosPagoCajaEntity;
import com.crystal.bazarposmobile.db.entity.ProductoEntity;
import com.crystal.bazarposmobile.retrofit.ApiService;
import com.crystal.bazarposmobile.retrofit.AppCliente;
import com.crystal.bazarposmobile.retrofit.request.RequestHeaderList;
import com.crystal.bazarposmobile.retrofit.request.creardocumento.Payment;
import com.crystal.bazarposmobile.retrofit.request.suspension.SuspesionLinea;
import com.crystal.bazarposmobile.retrofit.response.cliente.Cliente;
import com.crystal.bazarposmobile.retrofit.response.cliente.ResponseCliente;
import com.crystal.bazarposmobile.retrofit.response.documentodetalle.DocumentoDetalle;
import com.crystal.bazarposmobile.retrofit.response.documentodetalle.HeaderDD;
import com.crystal.bazarposmobile.retrofit.response.documentodetalle.LineDD;
import com.crystal.bazarposmobile.retrofit.response.documentodetalle.PaymentsDD;
import com.crystal.bazarposmobile.retrofit.response.documentodetalle.ResponseDD;
import com.crystal.bazarposmobile.retrofit.response.documentodetalle.ResponseDDList;
import com.crystal.bazarposmobile.retrofit.response.eanes.Producto;
import com.crystal.bazarposmobile.retrofit.response.mediospagocaja.MediosCaja;
import com.crystal.bazarposmobile.ui.fragment.DocumentoDetalleGripFragment;
import com.crystal.bazarposmobile.ui.fragment.ProductoGripFragment;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocumentoImpresionActivity
        extends
            AppCompatActivity
        implements
            View.OnClickListener,
            DocumentoDetalleGripFragment.OnLFIDocDetallesGripListener{

    //Declaración del Cliente REST
    ApiService apiService;
    String usuario = SPM.getString(Constantes.USER_NAME) + " - " + SPM.getString(Constantes.CAJA_CODE);

    //Declaración de los objetos de la interfaz del activity
    EditText etrefencia, etfecha;
    TextView tvreferencia, tvfecha, tvmostrar;
    ImageView btnreferencia, btnfecha;
    MenuItem pbmenu;
    DocumentoDetalleGripFragment productoDDF;
    RequestHeaderList rHeaderList;

    //Declaración de la variables del activity
    String tienda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documento_impresion);
        this.setTitle(R.string.titulo_impresion_documento);

        tienda = SPM.getString(Constantes.TIENDA_CODE);

        retrofitInit();
        findViews();
        eventos();

        if(SPM.getBoolean(Constantes.MODO_AUTONOMO)){
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorModoAutonomo)));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Toolbar toolbar = this.findViewById(R.id.action_bar);
                if (toolbar!= null){
                    toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
            desabilitar_busqueda();
            cargarTRX();
        }else{
            cargarFechaActual();
        }
     }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.ivFechaDIA:
                consultar_documentos();
                break;
            case R.id.ivReferenciaInternaDIA:
                String ref = etrefencia.getText().toString();
                if(ref.length() > 0){
                    consultar_documento(ref);
                }else{
                    msjToast("Referencia interna inválida.");
                }
                break;
            case R.id.etFechaDIA:
                getDatePickerDialog();
                break;
            case R.id.tvMostrarMasDIA:
                cargar_documentos();
                break;
        }
    }

    private void desabilitar_busqueda() {
        btnreferencia.setVisibility(View.GONE);
        btnfecha.setVisibility(View.GONE);
        etrefencia.setVisibility(View.GONE);
        etfecha.setVisibility(View.GONE);
        tvreferencia.setVisibility(View.GONE);
        tvfecha.setVisibility(View.GONE);
        tvmostrar.setVisibility(View.GONE);
    }

    private void cargarTRX() {
        try {
            List<DocHeaderEntity> docHeaderEntityList = new DocHeaderTodosAsyncTask().execute().get();
            if(docHeaderEntityList.size() > 0){
                List<DocumentoDetalle> documentoDetList = new ArrayList<>();
                for(DocHeaderEntity docHE: docHeaderEntityList){
                    Integer total = docHE.getTotal().intValue();
                    HeaderDD headerDD = new HeaderDD(docHE.getActive(),docHE.getCurrencyId(),docHE.getCustomerId(),docHE.getDate(),docHE.getInternalReference(),"",docHE.getStoreId(),docHE.getInternalReference(),docHE.getTaxExcluded(),total.toString(),docHE.getCantidad().toString());

                    List<LineDD> lineList = new ArrayList<>();
                    List<DocLineEntity> lineDocList = new DocLineAsyncTask().execute(docHE.getInternalReference()).get();
                    for(DocLineEntity lineE: lineDocList){
                        LineDD lineDD = new LineDD("","","","",lineE.getReference(),"","",lineE.getQuantity(),lineE.getUnitPrice(),lineE.getUnitPrice(),lineE.getUnitPrice(),lineE.getUnitPrice(),"");
                        lineList.add(lineDD);
                    }

                    List<PaymentsDD> paymentList = new ArrayList<>();
                    List<DocPaymentEntity> payDocList = new DocPayAsyncTask().execute(docHE.getInternalReference()).get();
                    for(DocPaymentEntity paymentE: payDocList){
                        PaymentsDD paymentsDD = new PaymentsDD(paymentE.getAmount().toString(),paymentE.getMethodId(),paymentE.getCurrencyId(),paymentE.getDueDate());
                        paymentList.add(paymentsDD);
                    }

                    DocumentoDetalle dd = new DocumentoDetalle(null,headerDD,lineList,paymentList);
                    documentoDetList.add(dd);
                }
                productoDDF.setDocDetalles(documentoDetList);
            }else{
                msjToast("Sin documento disponibles.");
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    protected class DocPayAsyncTask extends AsyncTask<String,Void, List<DocPaymentEntity>> {

        @Override
        protected List<DocPaymentEntity> doInBackground(String... internalReference) {
            return BazarPosMovilDB.getBD(getApplication()).documentoDao().getPayments(internalReference[0]);
        }
    }

    protected class DocLineAsyncTask extends AsyncTask<String,Void, List<DocLineEntity>> {

        @Override
        protected List<DocLineEntity> doInBackground(String... internalReference) {
            return BazarPosMovilDB.getBD(getApplication()).documentoDao().getLines(internalReference[0]);
        }
    }

    protected class DocHeaderTodosAsyncTask extends AsyncTask<Void,Void, List<DocHeaderEntity>> {

        @Override
        protected List<DocHeaderEntity> doInBackground(Void... voids) {
            return BazarPosMovilDB.getBD(getApplication()).documentoDao().getHeaderAll();
        }
    }

    private void cargar_documentos() {
        iPB();
        rHeaderList.setPageIndex(rHeaderList.getPageIndex()+1);
        Call<ResponseDDList> call = apiService.doHeaderList(usuario,rHeaderList);
        call.enqueue(new Callback<ResponseDDList>() {
            @Override
            public void onResponse(Call<ResponseDDList> call, Response<ResponseDDList> response) {
                if(response.isSuccessful()){
                    assert response.body() != null;
                    if(response.body().getError()){
                        mensajeSimpleDialog(getResources().getString(R.string.error),response.body().getMensaje());
                    }else{
                        productoDDF.setDocDetalles(response.body().getDocumentoDetalleList());
                        if(response.body().getDocumentoDetalleList().size() == 15){
                            tvmostrar.setVisibility(View.VISIBLE);
                        }else{
                            tvmostrar.setVisibility(View.GONE);
                        }
                    }
                }else{
                    mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion_consultar_doc));
                }
                pPB();
            }

            @Override
            public void onFailure(Call<ResponseDDList> call, Throwable t) {
                LogFile.adjuntarLog("Error ResponseConsultarDocumento: " + call + t);
                pPB();
                mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion) + t.getMessage());
            }
        });
    }

    private void consultar_documento(String ref) {

        iPB();
        Call<ResponseDD> call = apiService.doConsultarDocumentoDetalle(usuario,ref);
        call.enqueue(new Callback<ResponseDD>() {
            @Override
            public void onResponse(Call<ResponseDD> call, Response<ResponseDD> response) {
                if(response.isSuccessful()){
                    assert response.body() != null;
                    if(response.body().getError()){
                        mensajeSimpleDialog(getResources().getString(R.string.error),response.body().getMensaje());
                    }else{
                        List<DocumentoDetalle> list = new ArrayList<>();
                        list.add(response.body().getDocumentoDetalle());
                        productoDDF.setDocDetalles(list);
                    }
                }else{
                    mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion_consultar_doc));
                }
                pPB();
            }

            @Override
            public void onFailure(Call<ResponseDD> call, Throwable t) {
                LogFile.adjuntarLog("Error ResponseConsultarDocumento: " + call + t);
                pPB();
                mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion) + t.getMessage());
            }
        });
    }

    private void consultar_documentos() {

        iPB();
        String fecha = etfecha.getText().toString();
        rHeaderList = new RequestHeaderList("Receipt", Collections.singletonList(tienda),null,fecha,fecha, 0, 15);
        cargar_documentos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflaterMenu = getMenuInflater();
        inflaterMenu.inflate(R.menu.menupb, menu);
        pbmenu = menu.findItem(R.id.progressbar_menu);
        pbmenu.setEnabled(false);
        return super.onCreateOptionsMenu(menu);
    }

    private void eventos() {
        btnreferencia.setOnClickListener(this);
        btnfecha.setOnClickListener(this);
        etfecha.setOnClickListener(this);
        tvmostrar.setOnClickListener(this);

        etrefencia.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    btnreferencia.callOnClick();
                    return true;
                }
                return false;
            }
        });
        etrefencia.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btnreferencia.callOnClick();
                }
                return handled;
            }
        });
        //Ocultar el teclado de pantalla
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void findViews() {
        btnreferencia = findViewById(R.id.ivReferenciaInternaDIA);
        btnfecha = findViewById(R.id.ivFechaDIA);

        etrefencia = findViewById(R.id.etReferenciaInternaDIA);
        etfecha = findViewById(R.id.etFechaDIA);

        tvreferencia = findViewById(R.id.tvReferenciaInternaDIA);
        tvfecha = findViewById(R.id.tvFechaDIA);
        tvmostrar = findViewById(R.id.tvMostrarMasDIA);
        tvmostrar.setVisibility(View.GONE);

        productoDDF = (DocumentoDetalleGripFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragmentDIA);
    }

    private void retrofitInit() {
        AppCliente appCliente = AppCliente.getInstance();
        apiService = appCliente.getApiService();
    }

    private void cargarFechaActual() {

        Calendar calendario = Calendar.getInstance();
        int dia = calendario.get(Calendar.DAY_OF_MONTH);
        int mes = calendario.get(Calendar.MONTH)+1;
        int year = calendario.get(Calendar.YEAR);

        String diaS = ""+dia;
        String mesS = ""+mes;
        if(diaS.length() == 1){
            diaS = "0" + diaS;
        }
        if(mesS.length() == 1){
            mesS = "0" + mesS;
        }
        etfecha.setText(year + "-" + mesS + "-" + diaS);
    }

    private void getDatePickerDialog() {
        Calendar calendario = Calendar.getInstance();
        int dia = calendario.get(Calendar.DAY_OF_MONTH);
        int mes = calendario.get(Calendar.MONTH);
        int year = calendario.get(Calendar.YEAR);

        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String diaS = ""+dayOfMonth;
                        String mesS = ""+(month+1);
                        if(diaS.length() == 1){
                            diaS = "0" + diaS;
                        }
                        if(mesS.length() == 1){
                            mesS = "0" + mesS;
                        }
                        etfecha.setText(year + "-" + mesS + "-" + diaS);
                    }
                },year,mes,dia);
        dpd.show();
    }

    private void msjToast(String msj) {
        Toast.makeText(this, msj, Toast.LENGTH_SHORT).show();
    }

    private void iPB() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        pbmenu.setActionView(R.layout.menu_item_porgressbar_layout);
    }

    private void pPB() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        pbmenu.setActionView(null);
    }

    public void mensajeSimpleDialog(String titulo, String msj) {

        int icon = R.drawable.msj_alert_30;
        if (titulo.equals(getResources().getString(R.string.error))) {
            icon = R.drawable.msj_error_30;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titulo)
                .setMessage(msj)
                .setIcon(icon)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void OnLFILDocDetalles(DocumentoDetalle mItem, int p) {
        //Alerta para confirmar el cierre de medios de pagos
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.transaccion_)+" "+mItem.getHeaderDD().getInternalReference())
                .setMessage(getResources().getString(R.string.re_imprimir_factura))
                .setCancelable(false)
                .setIcon(R.mipmap.prueba_impresion)
                .setPositiveButton(R.string.confirmar,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                validar_cliente(mItem);
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton(R.string.volver,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

        AlertDialog alert = builder.create();
        alert.show();
    }
    private void validar_cliente(DocumentoDetalle mItem) {
        if(mItem.getHeaderDD().getCustomerId().equals(SPM.getString(Constantes.CLIENTE_GENERICO))){
            clienteGenerico(mItem);
        }else{
            buscarCliente(mItem);
        }
    }

    //Asignacion de cliente generico en la compra
    private void clienteGenerico(DocumentoDetalle mItem) {

        String idGenerico = SPM.getString(Constantes.CLIENTE_GENERICO);

        SPM.setString(Constantes.FIRST_NAME_CLIENTE, getResources().getString(R.string._cliente));
        SPM.setString(Constantes.LAST_NAME_CLIENTE, getResources().getString(R.string._generico));
        SPM.setString(Constantes.CUSTOMER_ID, idGenerico);
        SPM.setString(Constantes.DOCUMENTO_CLIENTE, idGenerico);

        SPM.setString(Constantes.TIPO_CLIENTE, getResources().getString(R.string.ninguno));
        SPM.setString(Constantes.GEF_CLIENTE, "Sin segmento");
        SPM.setString(Constantes.PB_CLIENTE, "Sin segmento");
        SPM.setString(Constantes.BF_CLIENTE, "Sin segmento");
        SPM.setString(Constantes.GX_CLIENTE, "Sin segmento");
        SPM.setString(Constantes.TIPO_CLIENTE_LETRA, "N");
        SPM.setString(Constantes.EMPRESA_CLIENTE, "N");

        SPM.setString(Constantes.ESTADO31, "");
        SPM.setString(Constantes.MEDIO32, "");
        SPM.setString(Constantes.ADJUNTO33, "");
        SPM.setString(Constantes.EMAIL_CLIENTE, "");

        imprimir_factura(mItem);
    }

    private void buscarCliente(DocumentoDetalle mItem) {

        iPB();
        Call<ResponseCliente> call = apiService.doCliente(usuario,mItem.getHeaderDD().getCustomerId());
        call.enqueue(new Callback<ResponseCliente>() {
            @Override
            public void onResponse(Call<ResponseCliente> call, Response<ResponseCliente> response) {

                if(response.isSuccessful()){
                    assert response.body() != null;
                    if(response.body().isError()){
                        LogFile.adjuntarLog("Error ResponseCliente: " + response.body().getMensaje());
                        mensajeSimpleDialog(getResources().getString(R.string.error),response.body().getMensaje());
                        pPB();
                    }else{
                        cargarCliente(response.body().getCliente());
                        imprimir_factura(mItem);
                    }
                }else{
                    LogFile.adjuntarLog("Error ResponseCliente: " + response.message());
                    mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion_sb) + response.message());
                    pPB();
                }
            }

            @Override
            public void onFailure(Call<ResponseCliente> call, Throwable t) {
                LogFile.adjuntarLog("Error ResponseCliente: " + call + t);
                mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion) + t.getMessage());
                pPB();
            }
        });
    }

    private void imprimir_factura(DocumentoDetalle mItem) {

        List<Producto> productosList = new ArrayList<>();
        List<Payment> importesList = new ArrayList<>();
        String consecutivo, prefijo, numtrans, textoFiscal;
        double totalcompra, cambio = 0.0;

        List<String> list = Arrays.asList(mItem.getHeaderDD().getInternalReference().split("-"));
        consecutivo = list.get(1);
        prefijo = list.get(0);

        numtrans = mItem.getHeaderDD().getNumber();
        totalcompra = Double.parseDouble(mItem.getHeaderDD().getTaxIncludedTotalAmount());

        //inicia validaciones fiscales
        int numini = SPM.getInt(Constantes.CC_NUMERO_INICIAL);
        int numfin = SPM.getInt(Constantes.CC_NUMERO_FINAL);

        String fechaA = SPM.getString(Constantes.CC_FECHA_AUTORIZACION);
        String anoA = fechaA.substring(0,4);
        String mesA = fechaA.substring(5,7);
        String diaA = fechaA.substring(8,10);

        textoFiscal = "Factura : "+prefijo+" "+consecutivo+" Res. Dian "+SPM.getString(Constantes.CC_NUMERO_RESOLUCION)+" DE "+diaA+"/"+mesA+"/"+anoA+" Rango "+numini+" A "+numfin+" AUTORIZA";

        for(LineDD line: mItem.getLineDDS()){
            try {
                double cant = Double.parseDouble(line.getQuantity());
                Double precioP = Double.parseDouble(line.getTaxIncludedNetUnitPrice());
                ProductoEntity pe = new ProductoEanAsyncTask().execute(line.getItemReference()).get();
                Producto p = new Producto(pe.getEan(),precioP,pe.getNombre(),pe.getTalla(),pe.getColor(),pe.getTipoTarifa(),pe.getTienda(),
                        pe.getPeriodoTarifa(),pe.getIp(), pe.getComposicion(),pe.getPrecioUnitario(),pe.getArticulo(),pe.getCodigoTasaImpuesto(),
                        pe.getArticuloCerrado(),pe.getArticuloGratuito(),pe.getTasaImpuesto(),pe.getPrecioSinImpuesto(),pe.getImpuesto(),
                        pe.getValorTasa(),pe.getFechaTasa(),pe.getPrecioOriginal(),pe.getPeriodoActivo(),pe.getCodigoMarca(),
                        pe.getMarca(),pe.getSerialNumberId(),pe.getVendedorId(), (int) cant,pe.getDescontable(),
                        pe.getTipoPrendaCodigo(),pe.getTipoPrendaNombre(),pe.getGeneroCodigo(),pe.getGeneroNombre(),
                        pe.getCategoriaIvaCodigo(),pe.getCategoriaIvaNombre(),pe.getLine());
                productosList.add(p);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String mpEfectivo = SPM.getString(Constantes.MEDIO_PAGO_EFECTIVO_CAJA);

        for(PaymentsDD pay: mItem.getPaymentsDDS()){
            Double precioPay = Double.parseDouble(pay.getAmount());
            if(precioPay < 0 && pay.getCode().equals(mpEfectivo)){
                cambio = precioPay;
                importesList.add(new Payment("CAMBIO", precioPay, pay.getCurrency(), "", 0, 1, pay.getCode()));
            }else{
                try {
                    Log.e("logcat","getCode: "+pay.getCode());
                    MediosPagoCajaEntity medio = new MediosPagoCajaAsyncTask().execute(pay.getCode()).get();
                    importesList.add(new Payment(medio.getNombre(), precioPay, pay.getCurrency(), "", 0, 1, pay.getCode()));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    Log.e("logcat","getMessage: "+e.getMessage());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.e("logcat","getMessage: "+e.getMessage());
                }
            }
        }
        Log.e("logcat","size: "+importesList.size());
        
        Intent intent = Utilidades.activityImprimir(DocumentoImpresionActivity.this);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("lablePrint", (Serializable) "factura");
        intent.putExtra("prefijo", (Serializable) prefijo);
        intent.putExtra("consecutivo", (Serializable) consecutivo);
        intent.putExtra("numtrans", (Serializable) numtrans);
        intent.putExtra("textoFiscal", (Serializable) textoFiscal);
        intent.putExtra("cambio", cambio);
        intent.putExtra("totalcompra", totalcompra);
        intent.putExtra("listProductos", (Serializable) productosList);
        intent.putExtra("listImportes", (Serializable) importesList);
        intent.putExtra("vaucherText", (Serializable) "");
        intent.putExtra("vaucherPalabra", (Serializable) "");
        intent.putExtra("primeraimpresion", false);
        startActivity(intent);
        finish();
    }

    private class MediosPagoCajaAsyncTask extends AsyncTask<String,Void, MediosPagoCajaEntity> {

        @Override
        protected MediosPagoCajaEntity doInBackground(String... codigo) {
            return BazarPosMovilDB.getBD(getApplication()).mediosPagoCajaDao().getMedio(codigo[0]);
        }
    }

    private class ProductoEanAsyncTask extends AsyncTask<String,Void, ProductoEntity> {

        @Override
        protected ProductoEntity doInBackground(String... ean) {
            return BazarPosMovilDB.getBD(getApplication()).productoDao().getEAN(ean[0]);
        }
    }

    private void cargarCliente(Cliente cliente) {

        SPM.setString(Constantes.FIRST_NAME_CLIENTE, cliente.getFirstName());
        SPM.setString(Constantes.LAST_NAME_CLIENTE, cliente.getLastName());
        SPM.setString(Constantes.SEXO_CLIENTE, cliente.getSexo());
        SPM.setString(Constantes.DOCUMENTO_CLIENTE, cliente.getDocumento());
        SPM.setString(Constantes.TIPO_DOCUMENTO_CLIENTE, cliente.getTipoDocumento().trim());
        SPM.setString(Constantes.CUSTOMER_ID, cliente.getCustomerId());
        SPM.setString(Constantes.EMPRESA_CLIENTE, cliente.getEmpresa());
        SPM.setString(Constantes.EMAIL_CLIENTE, cliente.getEmail());
        SPM.setString(Constantes.CELULAR_CLIENTE, cliente.getCelular());
        SPM.setString(Constantes.ESTADO31, cliente.getEstado31());
        SPM.setString(Constantes.MEDIO32, cliente.getMedio32());
        SPM.setString(Constantes.ADJUNTO33, cliente.getAdjunto33());
        SPM.setString(Constantes.PORFOIS35, cliente.getParfois35());

        SPM.setInt(Constantes.NAC_DIA, cliente.getBirthDateDay());
        SPM.setInt(Constantes.NAC_MES, cliente.getBirthDateMonth());
        SPM.setInt(Constantes.NAC_ANO, cliente.getBirthDateYear());

        SPM.setString(Constantes.ADDRESS1, cliente.getAddressLine1());
        SPM.setString(Constantes.ADDRESS2, cliente.getAddressLine2());
        SPM.setString(Constantes.CITY, cliente.getCity());
        SPM.setString(Constantes.COUNTRYID, cliente.getCountryId());
        SPM.setString(Constantes.REGIONID, cliente.getRegionId());
        SPM.setString(Constantes.ZIPCODE, cliente.getZipCode());


        switch(cliente.getTipoDocumento().trim()){
            case "1":
                SPM.setString(Constantes.TIPO_DOCUMENTO_CLIENTE_DESC, getResources().getString(R.string.cedula_ciudadania));
                SPM.setString(Constantes.TIPO_DOCUMENTO_CLIENTE_DESC_L, getResources().getString(R.string.cedula_ciudadania_letra));
                break;
            case "2":
                SPM.setString(Constantes.TIPO_DOCUMENTO_CLIENTE_DESC, getResources().getString(R.string.nit));
                SPM.setString(Constantes.TIPO_DOCUMENTO_CLIENTE_DESC_L, getResources().getString(R.string.nit));
                break;
            case "8":
                SPM.setString(Constantes.TIPO_DOCUMENTO_CLIENTE_DESC, getResources().getString(R.string.pasaporte));
                SPM.setString(Constantes.TIPO_DOCUMENTO_CLIENTE_DESC_L, getResources().getString(R.string.pasaporte_letra));
                break;
            case "11":
                SPM.setString(Constantes.TIPO_DOCUMENTO_CLIENTE_DESC, getResources().getString(R.string.cedula_identidad_cr));
                SPM.setString(Constantes.TIPO_DOCUMENTO_CLIENTE_DESC_L, getResources().getString(R.string.cedula_ciudadania_letra));
                break;
            case "12":
                SPM.setString(Constantes.TIPO_DOCUMENTO_CLIENTE_DESC, getResources().getString(R.string.cedula_juridica));
                SPM.setString(Constantes.TIPO_DOCUMENTO_CLIENTE_DESC_L, getResources().getString(R.string.cedula_ciudadania_letra));
                break;
            case "13":
                SPM.setString(Constantes.TIPO_DOCUMENTO_CLIENTE_DESC, getResources().getString(R.string.dimex));
                SPM.setString(Constantes.TIPO_DOCUMENTO_CLIENTE_DESC_L, getResources().getString(R.string.dimex_letra));
                break;
            case "14":
                SPM.setString(Constantes.TIPO_DOCUMENTO_CLIENTE_DESC, getResources().getString(R.string.nite));
                SPM.setString(Constantes.TIPO_DOCUMENTO_CLIENTE_DESC_L, getResources().getString(R.string.nit));
                break;
            case "15":
                SPM.setString(Constantes.TIPO_DOCUMENTO_CLIENTE_DESC, getResources().getString(R.string.extranjero));
                SPM.setString(Constantes.TIPO_DOCUMENTO_CLIENTE_DESC_L, getResources().getString(R.string.dimex_letra));
                break;
        }

        switch(cliente.getSegmentoGX()) {
            case "1":
                SPM.setString(Constantes.GX_CLIENTE, "Bronce Activo");
                break;
            case "2":
                SPM.setString(Constantes.GX_CLIENTE, "Bronce Inactivo");
                break;
            case "3":
                SPM.setString(Constantes.GX_CLIENTE, "Bronce Perdido");
                break;
            case "4":
                SPM.setString(Constantes.GX_CLIENTE, "Plata Activo");
                break;
            case "5":
                SPM.setString(Constantes.GX_CLIENTE, "Plata Inactivo");
                break;
            case "6":
                SPM.setString(Constantes.GX_CLIENTE, "Plata Perdido");
                break;
            case "7":
                SPM.setString(Constantes.GX_CLIENTE, "Oro Activo");
                break;
            case "8":
                SPM.setString(Constantes.GX_CLIENTE, "Oro Inactivo");
                break;
            case "9":
                SPM.setString(Constantes.GX_CLIENTE, "Oro Perdido");
                break;
            case "10":
                SPM.setString(Constantes.GX_CLIENTE, "Platino Activo");
                break;
            case "11":
                SPM.setString(Constantes.GX_CLIENTE, "Platino Inactivo");
                break;
            case "12":
                SPM.setString(Constantes.GX_CLIENTE, "Platino Perdido");
                break;
            case "13":
                SPM.setString(Constantes.GX_CLIENTE, "Diamante Activo");
                break;
            case "14":
                SPM.setString(Constantes.GX_CLIENTE, "Diamante Inactivo");
                break;
            case "15":
                SPM.setString(Constantes.GX_CLIENTE, "Diamante Perdido");
                break;
            default:
                SPM.setString(Constantes.GX_CLIENTE, "Sin segmento");
                break;
        }

        switch(cliente.getSegmentoBF()) {
            case "1":
                SPM.setString(Constantes.BF_CLIENTE, "Bronce Activo");
                break;
            case "2":
                SPM.setString(Constantes.BF_CLIENTE, "Bronce Inactivo");
                break;
            case "3":
                SPM.setString(Constantes.BF_CLIENTE, "Bronce Perdido");
                break;
            case "4":
                SPM.setString(Constantes.BF_CLIENTE, "Plata Activo");
                break;
            case "5":
                SPM.setString(Constantes.BF_CLIENTE, "Plata Inactivo");
                break;
            case "6":
                SPM.setString(Constantes.BF_CLIENTE, "Plata Perdido");
                break;
            case "7":
                SPM.setString(Constantes.BF_CLIENTE, "Oro Activo");
                break;
            case "8":
                SPM.setString(Constantes.BF_CLIENTE, "Oro Inactivo");
                break;
            case "9":
                SPM.setString(Constantes.BF_CLIENTE, "Oro Perdido");
                break;
            case "10":
                SPM.setString(Constantes.BF_CLIENTE, "Platino Activo");
                break;
            case "11":
                SPM.setString(Constantes.BF_CLIENTE, "Platino Inactivo");
                break;
            case "12":
                SPM.setString(Constantes.BF_CLIENTE, "Platino Perdido");
                break;
            case "13":
                SPM.setString(Constantes.BF_CLIENTE, "Diamante Activo");
                break;
            case "14":
                SPM.setString(Constantes.BF_CLIENTE, "Diamante Inactivo");
                break;
            case "15":
                SPM.setString(Constantes.BF_CLIENTE, "Diamante Perdido");
                break;
            default:
                SPM.setString(Constantes.BF_CLIENTE, "Sin segmento");
                break;
        }

        switch(cliente.getSegmentoPB()) {
            case "1":
                SPM.setString(Constantes.PB_CLIENTE, "Bronce Activo");
                break;
            case "2":
                SPM.setString(Constantes.PB_CLIENTE, "Bronce Inactivo");
                break;
            case "3":
                SPM.setString(Constantes.PB_CLIENTE, "Bronce Perdido");
                break;
            case "4":
                SPM.setString(Constantes.PB_CLIENTE, "Plata Activo");
                break;
            case "5":
                SPM.setString(Constantes.PB_CLIENTE, "Plata Inactivo");
                break;
            case "6":
                SPM.setString(Constantes.PB_CLIENTE, "Plata Perdido");
                break;
            case "7":
                SPM.setString(Constantes.PB_CLIENTE, "Oro Activo");
                break;
            case "8":
                SPM.setString(Constantes.PB_CLIENTE, "Oro Inactivo");
                break;
            case "9":
                SPM.setString(Constantes.PB_CLIENTE, "Oro Perdido");
                break;
            case "10":
                SPM.setString(Constantes.PB_CLIENTE, "Platino Activo");
                break;
            case "11":
                SPM.setString(Constantes.PB_CLIENTE, "Platino Inactivo");
                break;
            case "12":
                SPM.setString(Constantes.PB_CLIENTE, "Platino Perdido");
                break;
            case "13":
                SPM.setString(Constantes.PB_CLIENTE, "Diamante Activo");
                break;
            case "14":
                SPM.setString(Constantes.PB_CLIENTE, "Diamante Inactivo");
                break;
            case "15":
                SPM.setString(Constantes.PB_CLIENTE, "Diamante Perdido");
                break;
            default:
                SPM.setString(Constantes.PB_CLIENTE, "Sin segmento");
                break;
        }

        switch(cliente.getSegmentoGEF()) {
            case "1":
                SPM.setString(Constantes.GEF_CLIENTE, "Bronce Activo");
                break;
            case "2":
                SPM.setString(Constantes.GEF_CLIENTE, "Bronce Inactivo");
                break;
            case "3":
                SPM.setString(Constantes.GEF_CLIENTE, "Bronce Perdido");
                break;
            case "4":
                SPM.setString(Constantes.GEF_CLIENTE, "Plata Activo");
                break;
            case "5":
                SPM.setString(Constantes.GEF_CLIENTE, "Plata Inactivo");
                break;
            case "6":
                SPM.setString(Constantes.GEF_CLIENTE, "Plata Perdido");
                break;
            case "7":
                SPM.setString(Constantes.GEF_CLIENTE, "Oro Activo");
                break;
            case "8":
                SPM.setString(Constantes.GEF_CLIENTE, "Oro Inactivo");
                break;
            case "9":
                SPM.setString(Constantes.GEF_CLIENTE, "Oro Perdido");
                break;
            case "10":
                SPM.setString(Constantes.GEF_CLIENTE, "Platino Activo");
                break;
            case "11":
                SPM.setString(Constantes.GEF_CLIENTE, "Platino Inactivo");
                break;
            case "12":
                SPM.setString(Constantes.GEF_CLIENTE, "Platino Perdido");
                break;
            case "13":
                SPM.setString(Constantes.GEF_CLIENTE, "Diamante Activo");
                break;
            case "14":
                SPM.setString(Constantes.GEF_CLIENTE, "Diamante Inactivo");
                break;
            case "15":
                SPM.setString(Constantes.GEF_CLIENTE, "Diamante Perdido");
                break;
            default:
                SPM.setString(Constantes.GEF_CLIENTE, "Sin segmento");
                break;
        }

        switch(cliente.getTipo()) {
            case "A":
                SPM.setString(Constantes.TIPO_CLIENTE, "Aprendiz");
                SPM.setString(Constantes.TIPO_CLIENTE_LETRA, "A");
                break;
            case "C":
                SPM.setString(Constantes.TIPO_CLIENTE, "Compañías");
                SPM.setString(Constantes.TIPO_CLIENTE_LETRA, "C");
                break;
            case "E":
                SPM.setString(Constantes.TIPO_CLIENTE, "Empleado");
                SPM.setString(Constantes.TIPO_CLIENTE_LETRA, "E");
                break;
            case "F":
                SPM.setString(Constantes.TIPO_CLIENTE, "Junta Directiva");
                SPM.setString(Constantes.TIPO_CLIENTE_LETRA, "F");
                break;
            case "J":
                SPM.setString(Constantes.TIPO_CLIENTE, "Jubilados");
                SPM.setString(Constantes.TIPO_CLIENTE_LETRA, "J");
                break;
            case "L":
                SPM.setString(Constantes.TIPO_CLIENTE, "Autorizados");
                SPM.setString(Constantes.TIPO_CLIENTE_LETRA, "L");
                break;
            case "M":
                SPM.setString(Constantes.TIPO_CLIENTE, "Mayorista");
                SPM.setString(Constantes.TIPO_CLIENTE_LETRA, "M");
                break;
            case "O":
                SPM.setString(Constantes.TIPO_CLIENTE, "Otros");
                SPM.setString(Constantes.TIPO_CLIENTE_LETRA, "O");
                break;
            case "S":
                SPM.setString(Constantes.TIPO_CLIENTE, "Socios");
                SPM.setString(Constantes.TIPO_CLIENTE_LETRA, "C");
                break;
            case "SC":
                SPM.setString(Constantes.TIPO_CLIENTE, "School Shopper");
                SPM.setString(Constantes.TIPO_CLIENTE_LETRA, "SC");
                break;
            default:
                SPM.setString(Constantes.TIPO_CLIENTE, "Ninguno");
                SPM.setString(Constantes.TIPO_CLIENTE_LETRA, "N");
                break;
        }
    }
}