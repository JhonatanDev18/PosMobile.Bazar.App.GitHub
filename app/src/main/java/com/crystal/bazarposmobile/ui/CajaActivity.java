package com.crystal.bazarposmobile.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.common.Constantes;
import com.crystal.bazarposmobile.common.LogFile;
import com.crystal.bazarposmobile.common.SPM;
import com.crystal.bazarposmobile.common.Utilidades;
import com.crystal.bazarposmobile.db.BazarPosMovilDB;
import com.crystal.bazarposmobile.db.entity.ProductoEntity;
import com.crystal.bazarposmobile.db.entity.SuspencionHeaderEntity;
import com.crystal.bazarposmobile.db.entity.SuspencionLineEntity;
import com.crystal.bazarposmobile.retrofit.AppCliente;
import com.crystal.bazarposmobile.retrofit.ApiService;
import com.crystal.bazarposmobile.retrofit.request.suspension.SuspesionLinea;
import com.crystal.bazarposmobile.retrofit.request.suspension.RequestSuspension;
import com.crystal.bazarposmobile.retrofit.response.ResponseBase;
import com.crystal.bazarposmobile.retrofit.response.ResponseCupoEmpleado;
import com.crystal.bazarposmobile.retrofit.response.eanes.Producto;
import com.crystal.bazarposmobile.ui.dialogfragmen.AutorizacionProductoDialogFragment;
import com.crystal.bazarposmobile.ui.dialogfragmen.ProductoGripDialogFragment;
import com.crystal.bazarposmobile.ui.fragment.ProductoGripFragment;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CajaActivity
        extends
            AppCompatActivity
        implements
            View.OnClickListener,
            ProductoGripFragment.OnListFragmentInteractionListener,
            AutorizacionProductoDialogFragment.OnInputListener,
            ProductoGripDialogFragment.OnInputListener{

    //Declaración del Cliente REST
    ApiService apiService;
    String usuario = SPM.getString(Constantes.USER_NAME) + " - " + SPM.getString(Constantes.CAJA_CODE);

    //Declaración de los objetos de la interfaz del activity
    ProgressBar pb;
    ImageView ivbuscar;
    Button btnpago;
    TextView tvtotal, tvvendedorcompra, tvarticulos;
    EditText tvean;
    ProductoGripFragment productoGF;

    //Declaración de la variables del activity
    public List<Producto> productosList = new ArrayList<>();
    List<String> medioscajalist;
    String pais, tipoCliente, idCliente, cajeroname, tiendaname, currencyId, customerId, tienda, date,
            empresa_cliente, medioscaja, caja, idGenerico, idclientetipo;
    SimpleDateFormat currentDate;
    Date todayDate;
    Double preciototal;
    DecimalFormat formatea = new DecimalFormat(Constantes.FORMATO_DECIMAL);
    boolean isClienteGenerico;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caja);

        if(SPM.getBoolean(Constantes.MODO_AUTONOMO)){
            Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorModoAutonomo)));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Toolbar toolbar = this.findViewById(R.id.action_bar);
                if (toolbar!= null){
                    toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        }

        //Obteniendo las variable a usar
        idGenerico = SPM.getString(Constantes.CLIENTE_GENERICO);
        idclientetipo = SPM.getString(Constantes.TIPO_DOCUMENTO_CLIENTE_DESC);
        tipoCliente = SPM.getString(Constantes.TIPO_CLIENTE_LETRA);
        empresa_cliente = SPM.getString(Constantes.EMPRESA_CLIENTE);
        idCliente = SPM.getString(Constantes.DOCUMENTO_CLIENTE);
        cajeroname = SPM.getString(Constantes.CAJERO_NAME);
        tiendaname = SPM.getString(Constantes.NOMBRE_TIENDA);
        currencyId = SPM.getString(Constantes.DIVISA);
        customerId = SPM.getString(Constantes.CUSTOMER_ID);
        pais = SPM.getString(Constantes.PAIS_CODE);
        tienda = SPM.getString(Constantes.TIENDA_CODE);
        medioscaja = SPM.getString(Constantes.MEDIOS_PAGO);
        caja = SPM.getString(Constantes.CAJA_CODE);
        currentDate = new SimpleDateFormat("yyyy-MM-dd");
        todayDate = new Date();
        date = currentDate.format(todayDate);

        //Lista de string de medio de pago en la caja
        medioscajalist = Arrays.asList(medioscaja.split(";"));

        //Titulo del activity
        String nombreApellido = SPM.getString(Constantes.FIRST_NAME_CLIENTE) + " " + SPM.getString(Constantes.LAST_NAME_CLIENTE);

        if (idCliente.equals(SPM.getString(Constantes.CLIENTE_GENERICO))) {
            this.setTitle("CONSUMIDOR FINAL");
            isClienteGenerico = true;
        }else{
            this.setTitle(nombreApellido);
        }

        if (!idCliente.equals(SPM.getString(Constantes.CLIENTE_GENERICO))) {
            Objects.requireNonNull(getSupportActionBar()).setSubtitle(idclientetipo+ ": " + idCliente);
        }

        retrofitInit();
        findViews();
        events();

        //Validar si se recibe desde Intent el String actualizarcaja
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("productosList")) {
                productosList = (ArrayList<Producto>) getIntent().getSerializableExtra("productosList");
                actualizarGrip();
            }
        }
    }

    private void iPB() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        pb.setVisibility(View.VISIBLE);
    }

    private void pPB() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        tvean.requestFocus();
        pb.setVisibility(View.GONE);
    }

    //Instanciar el Cliebte REST
    private void retrofitInit() {
        AppCliente appCliente = AppCliente.getInstance();
        apiService = appCliente.getApiService();
    }

    //Asignacion de Referencias
    @SuppressLint("SetTextI18n")
    private void findViews() {
        //Se inicializa el adapter aqui para evitar el error RecyclerView: No adapter attached; skipping layout
        productoGF = (ProductoGripFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragmentGridCajaA);

        tvean = findViewById(R.id.etCantArticuloCajaA);
        tvtotal = findViewById(R.id.tvTotalCajaA);
        tvarticulos = findViewById(R.id.tvArticulosCajaA);
        tvvendedorcompra = findViewById(R.id.tvVendedorCajaA);

        ivbuscar = findViewById(R.id.ivBuscarCajaA);
        btnpago = findViewById(R.id.btnMediosPagosCajaA);
        btnpago.setEnabled(false);

        pb = findViewById(R.id.pbCajaA);
        pb.setVisibility(View.GONE);

        preciototal = 0.0;
        tvtotal.setText(Double.toString(preciototal));

        tvvendedorcompra.setText(SPM.getString(Constantes.VENDEDOR_NAME) + " ("+caja+")");
    }

    //Asignacion de eventos
    @SuppressLint("ClickableViewAccessibility")
    private void events() {
        btnpago.setOnClickListener(this);
        ivbuscar.setOnClickListener(this);

        //Ocultar el teclado de pantalla
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        tvean.requestFocus();
        tvean.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    tvean.requestFocus();
                    ivbuscar.callOnClick();
                    return true;
                }
                return false;
            }
        });

        tvean.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    tvean.requestFocus();
                    ivbuscar.callOnClick();
                }
                return handled;
            }
        });

        tvean.setOnLongClickListener(
                new View.OnLongClickListener() {
                    public boolean onLongClick(View view) {
                        new IntentIntegrator(CajaActivity.this).setCaptureActivity(LectorCodigosCamaraActivity.class).initiateScan();
                        return false;
                    }
                }
        );
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.ivBuscarCajaA:
                //Obtener el EAN del producto
                String ean = tvean.getText().toString();
                tvean.setText("");
                if (ean.length() > 0) {
                    addProduct(ean, null);
                } else {
                    Intent i = new Intent(CajaActivity.this, ProductoSelectActivity.class);
                    startActivityForResult(i, Constantes.RESP_PRODUCTO_SELECT);
                }
                break;
            case R.id.btnMediosPagosCajaA:
                if (verficarPreciosPruductos()) {
                    pasarMedioPagos(medioscajalist);
                } else {
                    msjToast(getResources().getString(R.string.no_exentos_igual_0));
                }
                break;
        }
    }

    private boolean verficarPreciosPruductos() {
        for (Producto p : productosList)
            if (p.getCodigoTasaImpuesto().equals("EXO") && p.getPrecio().intValue() == 0)
                return false;
        return true;
    }

    //Actualizar el GripRecyclerViewAdapter
    private void actualizarGrip() {
        productoGF.setProducto(productosList);
        setTotal();
        productoGF.scrollButton(productosList);
    }

    //Actualizar el total para la caja
    @SuppressLint("SetTextI18n")
    private void setTotal() {
        double total;
        total = 0.0;
        int cantArt = 0;
        if (productosList.size() > 0) {
            for (int i = 0; i < productosList.size(); i++) {
                cantArt = cantArt + productosList.get(i).getQuantity();
                total = total + (Math.abs(productosList.get(i).getPrecio().floatValue())*productosList.get(i).getQuantity());
            }
            btnpago.setEnabled(true);
        }else{
            btnpago.setEnabled(false);
        }
        preciototal = (double) Math.round(total);
        tvtotal.setText(formatea.format(total));
        tvarticulos.setText(Integer.toString(cantArt));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constantes.RESP_PRODUCTO_SELECT && data != null) {
            Producto p = (Producto) data.getSerializableExtra("producto");
            productosList.add(p);
            tvean.requestFocus();
            actualizarGrip();
            onListFragmentInteraction(p,productosList.size()-1);
        }else if (resultCode == Constantes.RESP_DEV_CON_INFO && data != null) {
            List<Producto> listProductosDevolucion = (List<Producto>) data.getSerializableExtra("listProductosDevolucion");
            productosList.addAll(listProductosDevolucion);
            actualizarGrip();
        }else{
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            //Validar si la respuest es vacia
            if (result != null) {
                if (result.getContents() == null) {
                    msjToast(getResources().getString(R.string.escaneo_cancelado));
                } else {
                    tvean.setText(result.getContents());
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflaterMenu = getMenuInflater();

        if(isClienteGenerico){
            inflaterMenu.inflate(R.menu.menucajagenerico, menu);
        }else{
            inflaterMenu.inflate(R.menu.menucaja, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mItemCupoEmpleado:
                cupoEmplado();
                break;
            case R.id.mItemDevolucionConInfo:
                solicitar_autorizacion(null);
                break;
            case R.id.mItemSuspension:
                confirmar_suspension_documento();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void cupoEmplado() {
        iPB();

        //Consultar la Api de Cliente
        Call<ResponseCupoEmpleado> call = apiService.doCupoEmpleado(SPM.getString(Constantes.USER_NAME)+" - "+getResources().getString(R.string.version_apk),idCliente);
        call.enqueue(new Callback<ResponseCupoEmpleado>() {
            @Override
            public void onResponse(@NonNull Call<ResponseCupoEmpleado> call, @NonNull Response<ResponseCupoEmpleado> response) {

                if (response.isSuccessful()) {
                    pPB();
                    assert response.body() != null;
                    if (response.body().getEsValida()) {
                        //Pasar a la impresión del Cupo
                        Intent i = Utilidades.activityImprimir(CajaActivity.this);
                        i.putExtra("lablePrint", (Serializable) "cupoEmpleado");
                        i.putExtra("cupoEmpleadoBody", response.body());
                        startActivity(i);
                    } else {
                        mensajeSimpleDialog(getResources().getString(R.string.error), response.body().getMensaje());
                    }
                } else {
                    pPB();
                    mensajeSimpleDialog(getResources().getString(R.string.error), getResources().getString(R.string.error_conexion_sb) + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseCupoEmpleado> call, @NonNull Throwable t) {
                LogFile.adjuntarLog("Error ResponseCupoEmpleado: " + call + t);
                pPB();
                mensajeSimpleDialog(getResources().getString(R.string.error), getResources().getString(R.string.error_conexion) + t.getMessage());
            }
        });
    }

    private void confirmar_suspension_documento() {
        if (productosList.size() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CajaActivity.this);
            builder.setTitle(R.string.suspension_documento).setMessage(R.string.estas_seguro_suspension_documento);
            View viewInflated = LayoutInflater.from(CajaActivity.this).inflate(R.layout.alert_dialog_input, null, false);
            final EditText input = viewInflated.findViewById(R.id.etDescripcionADI);
            builder.setView(viewInflated);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String text = input.getText().toString();
                    if(text.length() > 0){
                        if(text.length() > 150){
                            text = text.substring(0,150);
                        }
                        if(SPM.getBoolean(Constantes.MODO_AUTONOMO)){
                            realizar_suspension_documento_modo_autonomo(text);
                        }else{
                            realizar_suspension_documento(text);
                        }
                    }else{
                        if(SPM.getBoolean(Constantes.MODO_AUTONOMO)){
                            realizar_suspension_documento_modo_autonomo("");
                        }else{
                            realizar_suspension_documento("");
                        }
                    }
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        } else {
            mensajeSimpleDialog(getResources().getString(R.string.error), getResources().getString(R.string.suspension_invalida));
        }
    }

    private void realizar_suspension_documento(String text) {
        iPB();
        List<SuspesionLinea> lineas = new ArrayList<>();
        for (int i = 0; i < productosList.size(); i++) {
            SuspesionLinea line = new SuspesionLinea(productosList.get(i).getEan(),productosList.get(i).getQuantity(),i+1);
            lineas.add(line);
        }

        SimpleDateFormat currentDate = new SimpleDateFormat("yyyyMMddHHmmss");
        Date todayDate = new Date();
        String reference = currentDate.format(todayDate);
        reference = caja.concat(reference);

        RequestSuspension rSus = new RequestSuspension(text,lineas,customerId, tienda, caja,reference);
        Call<ResponseBase> call = apiService.doSuspensionAgregar(usuario,rSus);
        call.enqueue(new Callback<ResponseBase>() {
            @Override
            public void onResponse(Call<ResponseBase> call, Response<ResponseBase> response) {

                if(response.isSuccessful()){
                    if(response.body().getError()){
                        LogFile.adjuntarLog("Error SuspensionAgregar: " + response.body().getMensaje());
                        mensajeSimpleDialog(getResources().getString(R.string.error),response.body().getMensaje());
                        pPB();
                    }else{
                        msjToast(getResources().getString(R.string.suspension_exitosa));
                        finish();
                    }
                }else{
                    LogFile.adjuntarLog("Error SuspensionAgregar: " + response.message());
                    mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion_sb) + response.message());
                    pPB();
                }
            }

            @Override
            public void onFailure(Call<ResponseBase> call, Throwable t) {
                LogFile.adjuntarLog("Error SuspensionAgregar: " + call + t);
                mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion) + t.getMessage());
                pPB();
            }
        });

    }

    private void realizar_suspension_documento_modo_autonomo(String text) {

        iPB();
        SimpleDateFormat currentDate = new SimpleDateFormat("yyyyMMddHHmmss");
        Date todayDate = new Date();
        String reference = currentDate.format(todayDate);
        reference = caja.concat(reference);

        int cantidad = 0;
        for (int i = 0; i < productosList.size(); i++) {
            SuspencionLineEntity line = new SuspencionLineEntity(reference,productosList.get(i).getEan(),productosList.get(i).getQuantity());
            new SusLineCrearAsyncTask().execute(line);
            cantidad = cantidad + productosList.get(i).getQuantity();
        }
        SuspencionHeaderEntity header = new SuspencionHeaderEntity(reference,text,cantidad);
        new SusHeaderCrearAsyncTask().execute(header);
    }

    private class SusHeaderCrearAsyncTask extends AsyncTask<SuspencionHeaderEntity,Void,Void>{

        @Override
        protected Void doInBackground(SuspencionHeaderEntity... susHeaderEntity) {
            try{
                BazarPosMovilDB.getBD(getApplication()).suspesionDao().insertHeader(susHeaderEntity[0]);
            }catch (Exception ex){
                msjToast("Error al crear el header: "+ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            finish();
        }
    }

    private class SusLineCrearAsyncTask extends AsyncTask<SuspencionLineEntity,Void,Void>{

        @Override
        protected Void doInBackground(SuspencionLineEntity... susLineEntity) {
            try{
                BazarPosMovilDB.getBD(getApplication()).suspesionDao().insertLine(susLineEntity[0]);
            }catch (Exception ex){
                msjToast("Error al crear la linea: "+ex.getMessage());
            }
            return null;
        }
    }

    private void solicitar_autorizacion(Producto product) {
        AutorizacionProductoDialogFragment autoDF = new AutorizacionProductoDialogFragment(product);
        autoDF.show(getSupportFragmentManager(), "AutorizacionDialogFragment");
        if(product == null){
            LogFile.adjuntarLog("Autorización de devolucion con información");
        }else{
            LogFile.adjuntarLog("Autorización de devolucion del producto: "+product.toString());
        }
    }

    @Override
    public void sendInputItemAutorizacion(Producto product) {
        if(product == null){
            Intent i = new Intent(this, DevolucionConInformacionActivity.class);
            startActivityForResult(i, Constantes.RESP_DEV_CON_INFO);
        }else{
            productosList.add(product);
            actualizarGrip();
        }
    }

    @Override
    public void onListFragmentInteraction(Producto item, Integer position) {
        ProductoGripDialogFragment dialogProducto = new ProductoGripDialogFragment(item, position);
        dialogProducto.show(getSupportFragmentManager(), "ProductoGrip");
    }

    //Añadir un nuevo producto a la compra
    public void addProduct(String ean, final Double precio) {
        String eanC = "";
        int line = 0;
        int cant = 1;
        boolean esValido = true;

        if(ean.contains("-")){
            List<String> datos = Arrays.asList(ean.split("-"));
            if(datos.size() == 2){
                if(datos.get(0).length() == 0 || datos.get(1).length() == 0){
                    esValido = false;
                }else{
                    line = Integer.parseInt(datos.get(1));
                    cant = Integer.parseInt(datos.get(0));
                }
            }else{
                esValido = false;
            }
        }else{
            eanC = ean;
        }

        if (esValido) {
            new ProductoAsyncTask(eanC,line,cant,precio).execute();
        } else {
            msjToast(getResources().getString(R.string.consulta_invalida));
        }
    }

    @Override
    public void sendInputItemProductoDialogFragment(Integer cant, Integer inputposition, boolean eliminar) {
        if(eliminar){
            productosList.remove(inputposition.intValue());
        }else{
            productosList.get(inputposition).setQuantity(cant);
            if(cant < 0){
                productosList.get(inputposition).setPrecio((productosList.get(inputposition).getPrecio()*-1));
                solicitar_autorizacion(productosList.get(inputposition));
                productosList.remove(inputposition.intValue());
            }else{
                productosList.get(inputposition).setPrecio((Math.abs(productosList.get(inputposition).getPrecio())));
            }
        }
        actualizarGrip();
    }

    private class ProductoAsyncTask extends AsyncTask<Void,Void, Producto> {

        String ean;
        Integer line;
        Integer cant;
        Double precio;
        String msjError = "";

        ProductoAsyncTask(String ean, Integer line, Integer cant,Double precio) {
            // list all the parameters like in normal class define
            this.ean = ean;
            this.line = line;
            this.cant = cant;
            this.precio = precio;
        }

        @Override
        protected void onPreExecute() {
            iPB();
            //Ocultar el teclado de pantalla
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(tvean.getWindowToken(), 0);
        }


        @Override
        protected Producto doInBackground(Void... voids) {
            Producto p = null;
            try{
                ProductoEntity pe;
                if(ean.length() == 0){
                    pe = BazarPosMovilDB.getBD(getApplication()).productoDao().getLine(line);
                }else{
                    pe = BazarPosMovilDB.getBD(getApplication()).productoDao().getEAN(ean);
                }
                Log.e("LOGCAT","Line: "+pe.getLine()+" - EAN: "+pe.getEan()+" - Precio: "+pe.getPrecio());
                p = new Producto(pe.getEan(),pe.getPrecio(),pe.getNombre(),pe.getTalla(),pe.getColor(),pe.getTipoTarifa(),pe.getTienda(),
                        pe.getPeriodoTarifa(),pe.getIp(), pe.getComposicion(),pe.getPrecioUnitario(),pe.getArticulo(),pe.getCodigoTasaImpuesto(),
                        pe.getArticuloCerrado(),pe.getArticuloGratuito(),pe.getTasaImpuesto(),pe.getPrecioSinImpuesto(),pe.getImpuesto(),
                        pe.getValorTasa(),pe.getFechaTasa(),pe.getPrecioOriginal(),pe.getPeriodoActivo(),pe.getCodigoMarca(),
                        pe.getMarca(),pe.getSerialNumberId(),pe.getVendedorId(),cant,pe.getDescontable(),
                        pe.getTipoPrendaCodigo(),pe.getTipoPrendaNombre(),pe.getGeneroCodigo(),pe.getGeneroNombre(),
                        pe.getCategoriaIvaCodigo(),pe.getCategoriaIvaNombre(),pe.getLine());
            }catch (Exception ex){
                msjError = ex.getMessage();
                Log.e("LOGCAT","Error: "+ex.getMessage());
            }
            return p;
        }

        @Override
        protected void onPostExecute(Producto producto) {
            if(producto != null){
                if(precio != null){
                    producto.setPrecio(precio);
                }

                productosList.add(producto);
                tvean.requestFocus();
                actualizarGrip();
            }else{
                msjToast(getResources().getString(R.string.producto_no_encontrado)+" "+msjError);
            }
            pPB();
        }
    }

    private void pasarMedioPagos(List<String> union) {
        //Pasar al activity de Medios de Pago
        Intent intent = new Intent(CajaActivity.this, MediosPagosActivity.class);
        intent.putExtra("listMediosPagoPermitidos", (Serializable) union);
        intent.putExtra("listProductos", (Serializable) productosList);
        intent.putExtra("total", (Serializable) preciototal);
        startActivity(intent);
    }

    private void msjToast(String msj) {
        Toast.makeText(CajaActivity.this, msj, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    public void onBackPressed() {

        if (productosList.size() > 0) {

            //Alerta para confirmar el cierre de medios de pagos
            AlertDialog.Builder builder = new AlertDialog.Builder(CajaActivity.this);
            builder.setTitle(getResources().getString(R.string.cierre_caja))
                .setMessage(getResources().getString(R.string.productos_realizados_salir))
                    .setPositiveButton(R.string.confirmar,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
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
        } else {
            finish();
        }
    }

    //Alert Dialog para mostrar mensaje de Error o alerta
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
}
