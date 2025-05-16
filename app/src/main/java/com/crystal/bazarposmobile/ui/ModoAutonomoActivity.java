package com.crystal.bazarposmobile.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.common.Constantes;
import com.crystal.bazarposmobile.common.LogFile;
import com.crystal.bazarposmobile.common.MyApp;
import com.crystal.bazarposmobile.common.SPM;
import com.crystal.bazarposmobile.db.BazarPosMovilDB;
import com.crystal.bazarposmobile.db.entity.DetalleDescuentoEntity;
import com.crystal.bazarposmobile.db.entity.DocHeaderEntity;
import com.crystal.bazarposmobile.db.entity.DocLineEntity;
import com.crystal.bazarposmobile.db.entity.DocPaymentEntity;
import com.crystal.bazarposmobile.db.entity.PagosTEFEntity;
import com.crystal.bazarposmobile.retrofit.ApiService;
import com.crystal.bazarposmobile.retrofit.AppCliente;
import com.crystal.bazarposmobile.retrofit.request.RequestPostDocumento;
import com.crystal.bazarposmobile.retrofit.request.creardocumento.HeaderSD;
import com.crystal.bazarposmobile.retrofit.request.creardocumento.LineSD;
import com.crystal.bazarposmobile.retrofit.request.creardocumento.LinesSD;
import com.crystal.bazarposmobile.retrofit.request.creardocumento.Payment;
import com.crystal.bazarposmobile.retrofit.request.creardocumento.Payments;
import com.crystal.bazarposmobile.retrofit.request.creardocumento.RequestSaleDocumentCreate;
import com.crystal.bazarposmobile.retrofit.request.detallesdescuento.Detalle;
import com.crystal.bazarposmobile.retrofit.request.detallesdescuento.RequestDetallesDescuento;
import com.crystal.bazarposmobile.retrofit.request.tef.PagosTEF;
import com.crystal.bazarposmobile.retrofit.request.tef.RequestTEF;
import com.crystal.bazarposmobile.retrofit.response.ResponseDomentoFiscal;
import com.crystal.bazarposmobile.retrofit.response.documentodetalle.ResponseDD;
import com.crystal.bazarposmobile.retrofit.response.postdocumento.ResponsePostDocumento;
import com.crystal.bazarposmobile.ui.fragment.DocHeaderFragment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Response;

public class ModoAutonomoActivity
        extends
            AppCompatActivity
        implements
            View.OnClickListener,
            DocHeaderFragment.OnListDocHeader {

    //Declaración del Cliente REST
    ApiService apiService;
    String usuario = SPM.getString(Constantes.USER_NAME) + " - " + SPM.getString(Constantes.CAJA_CODE);

    //Declaración de los objetos de la interfaz del activity
    Button btnmodoautonomo,btnsincronizar;
    ProgressBar pb;
    DocHeaderFragment docHeaderFG;
    TextView tvtotal;

    //Declaración de la variables del activity
    List<DocHeaderEntity> docHeaderList;
    String caja;
    boolean isProceso = false;
    int cont;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modo_autonomo);
        this.setTitle(R.string.modo_autonomo);

        findViews();
        events();
        retrofitInit();
        caja = SPM.getString(Constantes.CAJA_CODE);
        if(SPM.getBoolean(Constantes.MODO_AUTONOMO)){
            btnmodoautonomo.setText(R.string.desactivar_modo_autonomo);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorModoAutonomo)));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Toolbar toolbar = this.findViewById(R.id.action_bar);
                if (toolbar!= null){
                    toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        }else{
            btnmodoautonomo.setText(R.string.activar_modo_autonomo);
        }
        new DocHeaderTodosAsyncTask().execute();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mItemCrearLogTRX:
                if(!tvtotal.getText().equals("0")){
                    try {
                        crearLog();
                        mensajeSimpleDialog(getResources().getString(R.string.modo_autonomo),"Log creado satifactoriamente en el almacenamiento interno, ruta BazarTRX.");
                    } catch (ExecutionException | InterruptedException e) {
                        mensajeSimpleDialog(getResources().getString(R.string.error), "Error al crear el log: " + e.getMessage());
                    }
                }else{
                    msjToast("No tienen documentos para crear un log.");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected class DocHeaderTodosAsyncTask extends AsyncTask<Void,Void, List<DocHeaderEntity>> {

        @Override
        protected List<DocHeaderEntity> doInBackground(Void... voids) {
            return BazarPosMovilDB.getBD(getApplication()).documentoDao().getHeaderAll();
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(List<DocHeaderEntity> listDocHeaderEntity) {
            super.onPostExecute(listDocHeaderEntity);
            docHeaderFG.setDocHeader(listDocHeaderEntity);
            docHeaderList = listDocHeaderEntity;
            if(!listDocHeaderEntity.isEmpty()){
                btnmodoautonomo.setEnabled(false);
                btnsincronizar.setEnabled(true);
                Integer size = listDocHeaderEntity.size();
                tvtotal.setText(size.toString());
            }else{
                btnmodoautonomo.setEnabled(true);
                btnsincronizar.setEnabled(false);
                tvtotal.setText("0");
            }
        }
    }

    private void findViews() {
        docHeaderFG = (DocHeaderFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentMAA);
        btnmodoautonomo = findViewById(R.id.btnModoAutonomoMAA);
        btnsincronizar = findViewById(R.id.btnSincronizarMAA);
        btnsincronizar.setEnabled(false);
        tvtotal = findViewById(R.id.tvTotalMAA);
        pb = findViewById(R.id.pbMAA);
        pb.setVisibility(View.GONE);
    }

    private void events() {
        btnmodoautonomo.setOnClickListener(this);
        btnsincronizar.setOnClickListener(this);
    }

    //Instanciar el Cliebte REST
    private void retrofitInit() {
        AppCliente appCliente = AppCliente.getInstance();
        apiService = appCliente.getApiService();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btnModoAutonomoMAA:
                validarModoAutonomo();
                break;
            case R.id.btnSincronizarMAA:
                if(!isProceso){
                    iPB();
                    if(isNetDisponible()){
                        if(!docHeaderList.isEmpty()){
                            new Thread(new Runnable() {
                                public void run() {
                                    try {
                                        sincronizar();
                                    } catch (ExecutionException | InterruptedException e) {
                                        e.printStackTrace();
                                        progressDialog.cancel();
                                        mensajeSimpleDialog(getResources().getString(R.string.error), "Error al sincrozinar el documento: " + e.getMessage());
                                        new DocHeaderTodosAsyncTask().execute();
                                    }
                                }
                            }).start();
                        }else{
                            pPB();
                            mensajeSimpleDialog(getResources().getString(R.string.modo_autonomo),"Proceso finalizado exitosamente.");
                        }
                    }else{
                        mensajeSimpleDialog(getResources().getString(R.string.error),"No tienes conexión activa a la red.");
                        pPB();
                    }
                }
                break;
        }
    }

    private boolean isNetDisponible() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();
        return (actNetInfo != null && actNetInfo.isConnected());
    }

    public void sincronizar() throws ExecutionException, InterruptedException {
        runOnUiThread(new Runnable() {
            public void run() {
                progressDialog = new ProgressDialog(ModoAutonomoActivity.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setMessage("Procesando 1 de "+docHeaderList.size());
                progressDialog.show();
            }
        });

        cont = 1;
        for(DocHeaderEntity docH:docHeaderList) {
            if(docH.getCantidad() == 0){
                runOnUiThread(new Runnable() {
                    public void run() {
                        progressDialog.cancel();
                        cierreApertura();
                    }
                });
                new DelDocHeaderAsyncTask().execute(docH).get();
                new DocHeaderTodosAsyncTask().execute();
                return;
            }
            HeaderSD headersd = new HeaderSD(docH.getActive(),docH.getCurrencyId(),docH.getCustomerId(),
                    docH.getDate(),docH.getInternalReference(),docH.getSalesPersonId(),docH.getStoreId(),
                    docH.getTaxExcluded(),docH.getType(),docH.getWarehouseId(),docH.getFollowedReference(),"DGD");

            List<DocLineEntity> lineDocList = new DocLineAsyncTask(docH.getInternalReference()).execute().get();
            List<LineSD> listLineSD = new ArrayList<>();
            for(DocLineEntity docL:lineDocList) {
                listLineSD.add(new LineSD(docL.getNetUnitPrice(),docL.getQuantity(),docL.getReference(),docL.getSalesPersonId(),docL.getUnitPrice(),docL.getDiscountTypeId(),docL.getSerialNumberId(),docL.getMovementReasonId()));
            }

            List<DocPaymentEntity> payDocList = new DocPayAsyncTask(docH.getInternalReference()).execute().get();
            List<Payment> payList  = new ArrayList<>();
            for(DocPaymentEntity docP:payDocList) {
                payList.add(new Payment(docP.getName(),docP.getAmount(),docP.getCurrencyId(),docP.getDueDate(),docP.getId(),docP.getIsReceivedPayment(),docP.getMethodId()));
            }
            LinesSD linessd = new LinesSD(listLineSD);
            Payments payments = new Payments(payList);
            RequestSaleDocumentCreate requestSDC = new RequestSaleDocumentCreate(null, headersd, linessd, payments, caja);

            String numtrans;
            Response<ResponseDomentoFiscal> resSD = new EnviarDocumentoAsyncTask().execute(requestSDC).get();
            if(resSD != null){
                if (resSD.isSuccessful()) {
                    if (resSD.body().getEsValida()) {
                        numtrans = resSD.body().getNumber();
                    } else {
                        //Validar si existe el documento
                        Response<ResponseDD> resDD = new ConsoltarDocumentoAsyncTask().execute(docH.getInternalReference()).get();
                        if(resDD != null){
                            if (resDD.isSuccessful()) {
                                if(resDD.body().getError()){
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            progressDialog.cancel();
                                            mensajeSimpleDialog(getResources().getString(R.string.error), "Error DocumentoDetalle: "+resSD.body().getMensaje());
                                            pPB();
                                        }
                                    });
                                    new DocHeaderTodosAsyncTask().execute();
                                    return;
                                }else{

                                    String customeridDD = resDD.body().getDocumentoDetalle().getHeaderDD().getCustomerId();
                                    String fechaDD = resDD.body().getDocumentoDetalle().getHeaderDD().getDate();
                                    if(fechaDD.length() > 10){
                                        fechaDD = fechaDD.substring(0,10);
                                    }
                                    String tiendaDD = resDD.body().getDocumentoDetalle().getHeaderDD().getStoreId();
                                    Double cantDouble = Double.parseDouble(resDD.body().getDocumentoDetalle().getHeaderDD().getTotalQuantity());
                                    Integer cantDD = cantDouble.intValue();
                                    Double totalDD = Double.parseDouble(resDD.body().getDocumentoDetalle().getHeaderDD().getTaxIncludedTotalAmount());

                                    boolean esigual = true;
                                    if(!customeridDD.equals(docH.getCustomerId())){
                                        esigual = false;
                                    }
                                    if(!tiendaDD.equals(docH.getStoreId())){
                                        esigual = false;
                                    }
                                    if(!fechaDD.equals(docH.getDate())){
                                        esigual = false;
                                    }
                                    if(!totalDD.equals(docH.getTotal())){
                                        esigual = false;
                                    }
                                    if(!cantDD.equals(docH.getCantidad())){
                                        esigual = false;
                                    }
                                    if(listLineSD.size() != resDD.body().getDocumentoDetalle().getLineDDS().size()){
                                        esigual = false;
                                    }
                                    if(payList.size() != resDD.body().getDocumentoDetalle().getPaymentsDDS().size()){
                                        esigual = false;
                                    }
                                    if(esigual){
                                        numtrans = resDD.body().getDocumentoDetalle().getHeaderDD().getNumber();
                                        RequestPostDocumento requestPostDocumento = new RequestPostDocumento(Integer.parseInt(numtrans),caja);

                                        //PosDocumento
                                        Response<ResponsePostDocumento> resPD = new PosDocumentoAsyncTask().execute(requestPostDocumento).get();
                                        if(resPD != null) {
                                            if (resPD.isSuccessful()) {
                                                if (resPD.body().isError()) {
                                                    runOnUiThread(new Runnable() {
                                                        public void run() {
                                                            progressDialog.cancel();
                                                            mensajeSimpleDialog(getResources().getString(R.string.error), "Error PosDocumento: "+resSD.body().getMensaje());
                                                            pPB();
                                                        }
                                                    });
                                                    new DocHeaderTodosAsyncTask().execute();
                                                    return;
                                                }
                                            }else{
                                                runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        progressDialog.cancel();
                                                        mensajeSimpleDialog(getResources().getString(R.string.error), "Error PosDocumento: "+resSD.body().getMensaje());
                                                        pPB();
                                                    }
                                                });
                                                new DocHeaderTodosAsyncTask().execute();
                                                return;
                                            }
                                        }else{
                                            runOnUiThread(new Runnable() {
                                                public void run() {
                                                    progressDialog.cancel();
                                                    mensajeSimpleDialog(getResources().getString(R.string.error), "Error PosDocumento: "+resSD.body().getMensaje());
                                                    pPB();
                                                }
                                            });
                                            new DocHeaderTodosAsyncTask().execute();
                                            return;
                                        }
                                    }else{
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                progressDialog.cancel();
                                                mensajeSimpleDialog(getResources().getString(R.string.error), "La referencia interna: "+docH.getInternalReference()+" esta cosumida por otro documento, contacte inmediatamente a un administrador.");
                                                pPB();
                                            }
                                        });
                                        new DocHeaderTodosAsyncTask().execute();
                                        return;
                                    }
                                }
                            }else{
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        progressDialog.cancel();
                                        mensajeSimpleDialog(getResources().getString(R.string.error), "Error DocumentoDetalle: "+resSD.body().getMensaje());
                                        pPB();
                                    }
                                });
                                new DocHeaderTodosAsyncTask().execute();
                                return;
                            }
                        }else{
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    progressDialog.cancel();
                                    mensajeSimpleDialog(getResources().getString(R.string.error), "Error DocumentoDetalle: "+resSD.body().getMensaje());
                                    pPB();
                                }
                            });
                            new DocHeaderTodosAsyncTask().execute();
                            return;
                        }
                    }
                }else{
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.cancel();
                            mensajeSimpleDialog(getResources().getString(R.string.error), getResources().getString(R.string.error_conexion_crear_doc));
                            pPB();
                        }
                    });
                    new DocHeaderTodosAsyncTask().execute();
                    return;
                }
            }else{
                runOnUiThread(new Runnable() {
                    public void run() {
                        progressDialog.cancel();
                        mensajeSimpleDialog(getResources().getString(R.string.error), getResources().getString(R.string.error_conexion_crear_doc));
                        pPB();
                    }
                });
                new DocHeaderTodosAsyncTask().execute();
                return;
            }

            List<PagosTEFEntity> pagoTEFDocList = new PagosTEFAsyncTask(docH.getInternalReference()).execute().get();
            if(!pagoTEFDocList.isEmpty()){
                List<PagosTEF> pagosTEF = new ArrayList<>();
                for(PagosTEFEntity docPagoTEF:pagoTEFDocList) {
                    pagosTEF.add(new PagosTEF(docPagoTEF.getCodigoCaja(),docPagoTEF.getCodigoMedioPago(),docPagoTEF.getCuatroUltimosDigitosTarjeta(),docPagoTEF.getFechaVencimientoTarjeta(),docPagoTEF.getNumeroAutorizacion(),docPagoTEF.getNumeroCuotas(),docPagoTEF.getNumeroPago(),Integer.parseInt(numtrans),docPagoTEF.getNumeroTransaccionDatafono(),docPagoTEF.getRespuestaTEF(),docPagoTEF.getTipoTarjeta()));
                }
                RequestTEF requestTEF = new RequestTEF(pagosTEF);
                Response<String> resTEF = new EnviarPagosTEFAsyncTask().execute(requestTEF).get();
                if(resTEF != null) {
                    if (resTEF.isSuccessful()) {
                        if (!resTEF.body().equals("true")) {
                            LogFile.adjuntarLog("Error ResponseRequestTEF: " + resTEF.body());
                        }
                    } else {
                        LogFile.adjuntarLog("Error ResponseRequestTEF: " + resTEF.message());
                    }
                } else {
                    LogFile.adjuntarLog("Error ResponseRequestTEF");
                }
            }

            List<DetalleDescuentoEntity> ddList = new DetalleDescuentoAsyncTask(docH.getInternalReference()).execute().get();
            if(!ddList.isEmpty()){
                List<Detalle> detalleList = new ArrayList<>();
                for(DetalleDescuentoEntity docDD:ddList) {
                    detalleList.add(new Detalle(docDD.getMotivoDescuento(),docDD.getPorcentajeDescuento(),docDD.getValeUrrem(),docDD.getBaseDescuento(),docDD.getBaseDescuentoDivisa(),docDD.getImporteIngresoSinImpuestos(),docDD.getImporteIngresoSinImpuestosDivisa(),docDD.getImporteIngresoConImpuestos(),docDD.getImporteIngresoConImpuestosDivisas(),docDD.getDotacion(),docDD.getTipoDocumento(),docDD.getIndiceDocumento(),docDD.getNumeroLinea(),docDD.getNumeroOrden(),docDD.getOrdenDelDescuento(),docDD.getNumeroRango(),docDD.getEstablecimiento(),docDD.getFechaDocumento(),docDD.getDivisaSouche(),docDD.getDivisaEstablecimiento(),docDD.getTipoDescuento(),docDD.getCodigoCondicionComercial(),docDD.getCodigoEstat()));
                }
                RequestDetallesDescuento requestDetallesDescuento = new RequestDetallesDescuento(docH.getStoreId(), Integer.parseInt(numtrans), detalleList);
                Response<String> resDD = new EnviarDetallesDescuentoAsyncTask().execute(requestDetallesDescuento).get();
                if(resDD != null) {
                    if (!resDD.isSuccessful()) {
                        LogFile.adjuntarLog("Error RequestDetallesDescuento no insertadas para el documento: " + numtrans + "devido a fallos en el servicio no nativo");
                    }
                } else {
                    LogFile.adjuntarLog("Error ResponseDetallesDescuento");
                }
            }

            List<DocPaymentEntity> docPayList = new DocPayAsyncTask(docH.getInternalReference()).execute().get();
            if(!docPayList.isEmpty()){
            }

            new DelDocHeaderAsyncTask().execute(docH).get();
            new DelDocLineAsyncTask().execute(docH.getInternalReference()).get();
            new DelDocPayAsyncTask().execute(docH.getInternalReference()).get();
            if(!pagoTEFDocList.isEmpty()){
                new DelDocPagoTEF().execute(docH.getInternalReference()).get();
            }
            if(!ddList.isEmpty()){
                new DelDetallesDescuento().execute(docH.getInternalReference()).get();
            }
            cont++;
            runOnUiThread(new Runnable() {
                public void run() {
                    progressDialog.setMessage("Procesando "+cont+" de "+docHeaderList.size());
                    if(!progressDialog.isShowing()){
                        progressDialog.show();
                    }
                }
            });
        }
        runOnUiThread(new Runnable() {
            public void run() {
                progressDialog.cancel();
                pPB();
                mensajeSimpleDialog(getResources().getString(R.string.modo_autonomo),"Proceso finalizado exitosamente.");
            }
        });
        new DocHeaderTodosAsyncTask().execute();
    }

    private class DelDetallesDescuento extends AsyncTask<String,Void,Void> {

        @Override
        protected Void doInBackground(String... internalR) {
            BazarPosMovilDB.getBD(getApplication()).documentoDao().deleteDetalleDescuento(internalR[0]);
            return null;
        }
    }

    protected class EnviarDetallesDescuentoAsyncTask extends AsyncTask<RequestDetallesDescuento,Void, Response<String>> {

        @Override
        protected Response<String> doInBackground(RequestDetallesDescuento... rDetallesDescuento) {
            Call<String> callDetallesDescuento = apiService.doRequestDetallesDescuento(usuario,rDetallesDescuento[0]);
            try {
                return callDetallesDescuento.execute();
            } catch (IOException e) {
                e.printStackTrace();
                LogFile.adjuntarLog("Error ResponseDetallesDescuento: " + e.getMessage());
                return null;
            }
        }
    }

    protected class DetalleDescuentoAsyncTask extends AsyncTask<Void,Void, List<DetalleDescuentoEntity>> {

        String internalReference;
        DetalleDescuentoAsyncTask(String internalReference){
            this.internalReference = internalReference;
        }

        @Override
        protected List<DetalleDescuentoEntity> doInBackground(Void... voids) {
            return BazarPosMovilDB.getBD(getApplication()).documentoDao().getDetalleDescuento(internalReference);
        }
    }

    private void cierreApertura() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.modo_autonomo)
                .setMessage(R.string.cierre_apertura_confirmar)
                .setPositiveButton(R.string.confirmar,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                pPB();
                                btnsincronizar.callOnClick();
                            }
                        })
                .setNegativeButton(R.string.volver,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                pPB();
                            }
                        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflaterMenu = getMenuInflater();
        inflaterMenu.inflate(R.menu.menumodoautonomo,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void crearLog() throws ExecutionException, InterruptedException {
        cont = 1;
        for(DocHeaderEntity docH:docHeaderList) {
            String log = "TRX: "+docH.getInternalReference();
            log = log + "\n" + docH.toString();

            List<DocLineEntity> lineDocList = new DocLineAsyncTask(docH.getInternalReference()).execute().get();
            log = log + "\nLine: " + lineDocList.size();
            for(DocLineEntity docL:lineDocList) {
                log = log + "\n" + docL.toString();
            }

            List<DocPaymentEntity> payDocList = new DocPayAsyncTask(docH.getInternalReference()).execute().get();
            log = log + "\nPayment: " + payDocList.size();
            for(DocPaymentEntity docP:payDocList) {
                log = log + "\n" + docP.toString();
            }

            List<PagosTEFEntity> pagoTEFDocList = new PagosTEFAsyncTask(docH.getInternalReference()).execute().get();
            if(pagoTEFDocList.size() > 0){
                log = log + "\nPagosTEF: " + pagoTEFDocList.size();
                for(PagosTEFEntity docPagoTEF:pagoTEFDocList) {
                    log = log + "\n" + docPagoTEF.toString();
                }
            }
            log = log + "\n\n";
            adjuntarLog(log);
        }
    }

    private void adjuntarLog(String text) {
        if (ContextCompat.checkSelfPermission(MyApp.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) MyApp.getContext(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Constantes.PERMISO_WRITE_EXTERNAL_STORAGE);
        }else{
            //Obtener fecha y hora
            SimpleDateFormat currentDate = new SimpleDateFormat("yyyyMMddhhmmss");
            Date todayDate = new Date();
            //Crear ruta "Nombre a elegir (NOMBRE_CARPETA)" si no existe en el almacenamiento interno
            File dirPosMobile = new File(Environment.getExternalStorageDirectory(), "BazarTRX");
            if (!dirPosMobile.exists())
                if (!dirPosMobile.mkdirs())
                    android.util.Log.e("LogFile", Resources.getSystem().getString(R.string.no_directorio));
            //Creando archivo txt por dia de nombre yyyyMMddlog.txt
            String ruta = dirPosMobile.toString() + "/" + currentDate.format(todayDate) + "-" + caja +"TRX.txt";
            File logFile = new File(ruta);
            //Abriendo el archivo
            if (!logFile.exists()) {
                try {
                    logFile.createNewFile();
                }catch (IOException e){
                    e.printStackTrace();
                    android.util.Log.e("LogFile", Resources.getSystem().getString(R.string.log_no_creado)+" - "+e);
                }
            }
            //Escriendo en el archivo y despues cerrandolo
            try{
                //Fecha y hora de la escritura
                currentDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                todayDate = new Date();
                String fechahora = currentDate.format(todayDate);
                text = fechahora +": "+text;
                //BufferedWriter para establecer agregar al indicador de archivo
                BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
                buf.append(text);
                buf.newLine();
                buf.close();
            }catch (IOException e){
                e.printStackTrace();
                android.util.Log.e("LogFile", Resources.getSystem().getString(R.string.log_no_escrito)+" - "+e);
            }
        }
    }

    protected class PosDocumentoAsyncTask extends AsyncTask<RequestPostDocumento,Void, Response<ResponsePostDocumento>> {

        @Override
        protected Response<ResponsePostDocumento> doInBackground(RequestPostDocumento... requestPD) {
            Call<ResponsePostDocumento> callPD = apiService.doPostDocumento(usuario,requestPD[0]);
            try {
                return callPD.execute();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    protected class ConsoltarDocumentoAsyncTask extends AsyncTask<String,Void, Response<ResponseDD>> {

        @Override
        protected Response<ResponseDD> doInBackground(String... internalReference) {
            Call<ResponseDD> callDD = apiService.doConsultarDocumentoDetalle(usuario,internalReference[0]);
            try {
                return callDD.execute();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private class DelDocPagoTEF extends AsyncTask<String,Void,Void> {

        @Override
        protected Void doInBackground(String... internalR) {
            BazarPosMovilDB.getBD(getApplication()).documentoDao().deletePagosTEF(internalR[0]);
            return null;
        }
    }

    private class DelDocPayAsyncTask extends AsyncTask<String,Void,Void> {

        @Override
        protected Void doInBackground(String... internalR) {
            BazarPosMovilDB.getBD(getApplication()).documentoDao().deletePayments(internalR[0]);
            return null;
        }
    }

    private class DelDocLineAsyncTask extends AsyncTask<String,Void,Void> {

        @Override
        protected Void doInBackground(String... internalR) {
            BazarPosMovilDB.getBD(getApplication()).documentoDao().deleteLines(internalR[0]);
            return null;
        }
    }

    private class DelDocHeaderAsyncTask extends AsyncTask<DocHeaderEntity,Void,Void> {

        @Override
        protected Void doInBackground(DocHeaderEntity... docHeaderEntity) {
            BazarPosMovilDB.getBD(getApplication()).documentoDao().deleteHeader(docHeaderEntity[0]);
            return null;
        }
    }

    protected class EnviarPagosTEFAsyncTask extends AsyncTask<RequestTEF,Void, Response<String>> {

        @Override
        protected Response<String> doInBackground(RequestTEF... requestTEF) {
            Call<String> callTEF = apiService.doRequestTEF(usuario,requestTEF[0]);
            try {
                return callTEF.execute();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    protected class EnviarDocumentoAsyncTask extends AsyncTask<RequestSaleDocumentCreate,Void, Response<ResponseDomentoFiscal>> {

        @Override
        protected Response<ResponseDomentoFiscal> doInBackground(RequestSaleDocumentCreate... requestSDC) {
            Call<ResponseDomentoFiscal> callSD = apiService.doSaleDocument(usuario,requestSDC[0]);
            try {
                return callSD.execute();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    protected class PagosTEFAsyncTask extends AsyncTask<Void,Void, List<PagosTEFEntity>> {

        String internalReference;
        PagosTEFAsyncTask(String internalReference){
            this.internalReference = internalReference;
        }

        @Override
        protected List<PagosTEFEntity> doInBackground(Void... voids) {
            return BazarPosMovilDB.getBD(getApplication()).documentoDao().getPagosTEF(internalReference);
        }
    }

    protected class DocPayAsyncTask extends AsyncTask<Void,Void, List<DocPaymentEntity>> {

        String internalReference;
        DocPayAsyncTask(String internalReference){
            this.internalReference = internalReference;
        }

        @Override
        protected List<DocPaymentEntity> doInBackground(Void... voids) {
            return BazarPosMovilDB.getBD(getApplication()).documentoDao().getPayments(internalReference);
        }
    }

    protected class DocLineAsyncTask extends AsyncTask<Void,Void, List<DocLineEntity>> {

        String internalReference;
        DocLineAsyncTask(String internalReference){
            this.internalReference = internalReference;
        }

        @Override
        protected List<DocLineEntity> doInBackground(Void... voids) {
            return BazarPosMovilDB.getBD(getApplication()).documentoDao().getLines(internalReference);
        }
    }

    private void iPB() {
        isProceso = true;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        pb.setVisibility(View.VISIBLE);
    }

    private void pPB() {
        isProceso = false;
        pb.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void onListDocHeader(DocHeaderEntity item, Integer position) {

    }

    private void validarModoAutonomo() {
        if(SPM.getBoolean(Constantes.MODO_AUTONOMO)){
            vaciar_base_datos();
            SPM.setBoolean(Constantes.MODO_AUTONOMO,false);
            Intent intent = new Intent(this, ClienteConsultaActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("actualizarcaja",  true);
            startActivity(intent);
            finish();
        }else{
            //Alerta para confirmar el cierre de medios de pagos
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.modo_autonomo)
                    .setMessage(R.string.activa_modo_autonomo_confirmacion)
                    .setPositiveButton(R.string.confirmar,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    SPM.setBoolean(Constantes.MODO_AUTONOMO,true);
                                    Intent intent = new Intent(ModoAutonomoActivity.this, ClienteConsultaActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
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
        }
    }

    private void vaciar_base_datos() {
        new DelSusLineAllAsyncTask().execute();
        new DelSusHeaderAllAsyncTask().execute();
    }

    private class DelSusLineAllAsyncTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            BazarPosMovilDB.getBD(getApplication()).suspesionDao().deleteLinesAll();
            return null;
        }
    }

    private class DelSusHeaderAllAsyncTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            BazarPosMovilDB.getBD(getApplication()).suspesionDao().deleteHeaderAll();
            return null;
        }
    }

    //Alert Dialog para mostrar mensaje de Error o alerta
    public void mensajeSimpleDialog(String titulo, String msj) {
            int icon = R.drawable.ic_baseline_check_circle_30;
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

    private void msjToast(String msj) {
        Toast.makeText(this, msj, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if(isProceso){
            msjToast("Debes esperar que termine la sincronización de los documentos.");
        }else{
            finish();
        }
    }
}