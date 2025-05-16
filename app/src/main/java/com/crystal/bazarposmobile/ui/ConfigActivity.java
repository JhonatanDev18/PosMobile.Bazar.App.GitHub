package com.crystal.bazarposmobile.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.common.IConfigurableActivity;
import com.crystal.bazarposmobile.common.Constantes;
import com.crystal.bazarposmobile.common.SPM;
import com.crystal.bazarposmobile.common.TipoDatafono;
import com.crystal.bazarposmobile.common.Utilidades;
import com.crystal.bazarposmobile.retrofit.ApiService;
import com.crystal.bazarposmobile.retrofit.AppCliente;
import com.crystal.bazarposmobile.retrofit.request.RequestParametros;
import com.crystal.bazarposmobile.retrofit.request.RequestPerifericos;
import com.crystal.bazarposmobile.retrofit.response.consecutivofiscal.ResponseConsecutivoFiscal;
import com.crystal.bazarposmobile.retrofit.response.ResponseParametros;
import com.crystal.bazarposmobile.retrofit.response.caja.Caja;
import com.crystal.bazarposmobile.retrofit.response.perifericos.ResponseConsultarPerifericos;
import com.crystal.bazarposmobile.retrofit.response.tienda.ResponseTienda;
import com.crystal.bazarposmobile.ui.dialogfragmen.CajaTiendaSelectDialogFragment;
import com.crystal.bazarposmobile.ui.dialogfragmen.TipoTefDialogFragment;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfigActivity
        extends
            AppCompatActivity
        implements
            View.OnClickListener,
            CajaTiendaSelectDialogFragment.OnInputListener,
        IConfigurableActivity {

    //Declaración del Cliente REST
    ApiService apiService;
    String usuario = SPM.getString(Constantes.USER_NAME);
    Utilidades util;

    //Declaración de los objetos de la interfaz del activity
    Button btntienda, btncaja, btncerrar;
    TextView tvtienda, tvcaja;
    ProgressBar pb;

    //Declaración de la variables del activity
    List<Caja> cajasList = new ArrayList<>();
    CajaTiendaSelectDialogFragment cajaTiendaSelectDF;
    String caja,tienda;
    Context contexto;
    TipoTefDialogFragment tipoTefDialogFragment;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        this.setTitle(R.string.titulo_config);

        contexto = ConfigActivity.this;

        inicio();
    }

    @SuppressLint("SetTextI18n")
    private void inicio() {
        util = new Utilidades();
        retrofitInit();
        findViews();
        events();

        //Validar si tiene tienda asignada
        if ( SPM.getString(Constantes.TIENDA_CODE) == null){
            tvtienda.setText("Sin Tienda Asignada");
            btncaja.setEnabled(false);
            tvcaja.setEnabled(false);
        } else {
            tienda = SPM.getString(Constantes.TIENDA_CODE);
            tvtienda.setText(tienda);
            btncaja.setEnabled(true);
            tvcaja.setEnabled(true);
            configurarParametrosQRBancolombia();
        }

        //Validar si tiene caja asignada
        if ( SPM.getString(Constantes.CAJA_CODE) == null){
            tvcaja.setText("Sin Caja");
        } else {
            tvcaja.setText(SPM.getString(Constantes.CAJA_CODE));
        }
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflaterMenu = getMenuInflater();
        inflaterMenu.inflate(R.menu.menuconfigurador,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()){
            case R.id.mItemTestComunicacionConf:
                tipoTefDialogFragment = new TipoTefDialogFragment(this);
                tipoTefDialogFragment.show(getSupportFragmentManager(),"TipoTefDialogFragment");
                break;
            case R.id.mItemImpresionPruebaConf:
                //Pasar a la impresión de prueba
                i = Utilidades.activityImprimir(ConfigActivity.this);
                i.putExtra("lablePrint", (Serializable) "prueba");
                i.putExtra("desabilitar_configuracion", (Serializable) false);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Instanciar el Cliebte REST
    private void retrofitInit() {
        AppCliente appCliente = AppCliente.getInstance();
        apiService = appCliente.getApiService();
    }

    //Asignacion de Referencias
    private void findViews() {
        btntienda = findViewById(R.id.btnAsignarTiendaConfigA);
        btncaja = findViewById(R.id.btnAsignarCajaConfigA);
        btncerrar = findViewById(R.id.btnFinalizarConfigA);

        tvtienda = findViewById(R.id.tvTiendaConfigA);
        tvcaja = findViewById(R.id.tvCajaConfigA);

        pb = findViewById(R.id.pbConfigA);
        pb.setVisibility(View.GONE);

    }

    //Asignacion de eventos
    private void events() {
        btntienda.setOnClickListener(this);
        btncaja.setOnClickListener(this);
        btncerrar.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.btnAsignarTiendaConfigA:
                //Dialog Fragment para la gestion del cambio de la Tienda
                cajaTiendaSelectDF = new CajaTiendaSelectDialogFragment("Tienda", cajasList);
                cajaTiendaSelectDF.show(getSupportFragmentManager(),"CajaTiendaSelect");
                break;
            case R.id.btnAsignarCajaConfigA:
                //Dialog Fragment para la gestion del cambio de la Caja
                cajaTiendaSelectDF = new CajaTiendaSelectDialogFragment("Caja", cajasList);
                cajaTiendaSelectDF.show(getSupportFragmentManager(),"CajaTiendaSelect");
                break;
            case R.id.btnFinalizarConfigA:
                irLogin();
                break;
        }
    }

    private void irLogin() {
        //Pasar al activity de Principal
        Intent i = new Intent(ConfigActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void iniciarProgressBar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        pb.setVisibility(View.VISIBLE);
    }

    private void iPB(String mensaje) {
        if(util.estadoProgress()){
            util.mostrarDialogProgressBar(contexto, mensaje,
                    false);
        }
    }

    private void pPB(){
        util.ocultarDialogProgressBar();
    }

    private void pararProgressBar() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        pb.setVisibility(View.GONE);
    }

    //Recibe respuesta de Dialog Frame CajaTiendaSelectDialogFragment
    @Override
    public void sendInputItemCajaTiendaSelectDialogFragment(List<Caja> Cajas, String tiendaCaja, String code) {
        cajasList = Cajas;

        if ((code.equals("Tienda"))){
            iPB("Cargando parámetros de la tienda.");
            tvtienda.setText(tiendaCaja);
            tienda = tiendaCaja;
            btncaja.setEnabled(true);
            tvcaja.setEnabled(true);
            tvcaja.setText(R.string.sin_caja);
            configurarParametrosQRBancolombia();
        }else if (code.equals("Caja")){
            tvcaja.setText(R.string.sin_caja);
            iniciarProgressBar();
            //Consultar la Api de Consecutivo Fiscal
            Call<ResponseConsecutivoFiscal> call = apiService.doConsecutivoFiscal(usuario,tiendaCaja);
            call.enqueue(new Callback<ResponseConsecutivoFiscal>() {
                @Override
                public void onResponse(Call<ResponseConsecutivoFiscal> call, Response<ResponseConsecutivoFiscal> response) {

                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        if(response.body().getEsValida()){
                            for (String s : response.body().getMensaje()) {
                                if(!s.isEmpty()) {
                                    mensajeSimpleDialog(getResources().getString(R.string.alerta), s);
                                }
                            }
                            caja = response.body().getCaja();
                            SPM.setString(Constantes.CC_FECHA_FINAL, response.body().getCc().getFechaFinal());
                            SPM.setInt(Constantes.CC_NUMERO_INICIAL, response.body().getCc().getNumeroInicial());
                            SPM.setInt(Constantes.CC_NUMERO_FINAL, response.body().getCc().getNumeroFinal());
                            SPM.setInt(Constantes.CC_DIA_ALERTA, response.body().getCc().getDiaAlerta());
                            SPM.setString(Constantes.CC_ALERTA_LIMITE_FISCAL, response.body().getCc().getMensajeAlertaLimiteFiscal());
                            SPM.setString(Constantes.CC_ALERTA_LIMITE_CONSECUTIVO, response.body().getCc().getMensajeAlertaLimiteConsesecutivo());
                            SPM.setString(Constantes.CC_ALERTA_FECHA_RESOLUCION, response.body().getCc().getMensajeAlertaFechaResolucion());
                            SPM.setString(Constantes.CC_ALERTA_VENCIMIENTO_RESOLUCION, response.body().getCc().getMensajeAlertaVencimientoResolucion());
                            SPM.setInt(Constantes.CC_NUMERO_ALERTA, response.body().getCc().getNumeroAlerta());
                            SPM.setString(Constantes.CC_NUMERO_RESOLUCION, response.body().getCc().getNumeroResolucion());
                            SPM.setString(Constantes.CC_FECHA_AUTORIZACION, response.body().getCc().getFechaAutorizacion());
                            SPM.setString(Constantes.CC_PREFIJO, response.body().getCc().getPrefijo());
                            SPM.setInt(Constantes.CC_COMENZAR_EN, response.body().getCc().getComenzarEn());
                            SPM.setString(Constantes.CC_TIPO_RESOLUCION, response.body().getCc().getTipoResolucion());
                            SPM.setInt(Constantes.CC_CONSECUTIVO, response.body().getCc().getConsecutivo());

                            //Consultar la Api de parametros
                            RequestParametros requestParametros = new RequestParametros("bazar",caja,tienda);
                            Call<ResponseParametros> callRPC = apiService.doParametrosCaja(usuario,requestParametros);
                            callRPC.enqueue(new Callback<ResponseParametros>() {
                                @Override
                                public void onResponse(Call<ResponseParametros> call, Response<ResponseParametros> response) {

                                    if(response.isSuccessful()){
                                        assert response.body() != null;
                                        if(!response.body().getError()){
                                            if(response.body().getFechaHora() != null){
                                                String fechaHora = response.body().getFechaHora();
                                                if(fechaValida(fechaHora,5)){
                                                    SPM.setString(Constantes.CAJA_CODE, caja);
                                                    tvcaja.setText(caja);
                                                    configurarPerifericos();
                                                }else{
                                                    mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_config_caja_fecha));
                                                    SPM.setString(Constantes.CAJA_CODE, null);
                                                }
                                            }else{
                                                mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_act_caja));
                                                SPM.setString(Constantes.CAJA_CODE, null);
                                            }
                                            //Guardar variable de sesion de Textos Fijos para la factura, TEF, tirillas y usuarios
                                        }else{
                                            mensajeSimpleDialog(getResources().getString(R.string.error),response.body().getMensaje());
                                            SPM.setString(Constantes.CAJA_CODE, null);
                                        }
                                    }else{
                                        msjToast(getResources().getString(R.string.error_conexion_sb));
                                        SPM.setString(Constantes.CAJA_CODE, null);
                                    }
                                    pararProgressBar();
                                }

                                @Override
                                public void onFailure(Call<ResponseParametros> call, Throwable t) {
                                    Log.e("LOGCAT", "ResponseParametros: "+call + t);
                                    SPM.setString(Constantes.CAJA_CODE, null);
                                    pararProgressBar();
                                    msjToast(getResources().getString(R.string.error_conexion) + t.getMessage());
                                }
                            });
                        }else{
                            SPM.setString(Constantes.CAJA_CODE, null);
                            for (String s : response.body().getMensaje()) {
                                mensajeSimpleDialog(getResources().getString(R.string.error),s);
                            }
                            pararProgressBar();
                        }
                    } else {
                        SPM.setString(Constantes.CAJA_CODE, null);
                        msjToast(getResources().getString(R.string.error_conexion_sb));
                        pararProgressBar();
                    }
                }

                @Override
                public void onFailure(Call<ResponseConsecutivoFiscal> call, Throwable t) {
                    Log.e("LOGCAT", "ErrorResponseConsecutivoFiscal: "+call + t);
                    SPM.setString(Constantes.CAJA_CODE, null);
                    pararProgressBar();
                    msjToast(getResources().getString(R.string.error_conexion) + t.getMessage());
                }
            });
        }
    }

    private void configurarParametrosQRBancolombia(){
        Call<ResponseTienda> call = apiService.doTienda(SPM.getString(Constantes.USER_NAME)+" - "+getResources().getString(R.string.version_apk), tienda);
        call.enqueue(new Callback<ResponseTienda>() {
            @Override
            public void onResponse(Call<ResponseTienda> call, Response<ResponseTienda> response) {
                if(response.isSuccessful()){
                    assert response.body() != null;
                    if(!response.body().getError()){
                        SPM.setString(Constantes.TIENDA_MERCHANTID, response.body().getTienda().getMerchantID());
                        SPM.setString(Constantes.TIENDA_NOMBRE, response.body().getTienda().getNombre());
                        pPB();
                    }else{
                        mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion_sb) + response.message());
                        SPM.setString(Constantes.TIENDA_NOMBRE, null);
                        SPM.setString(Constantes.TIENDA_MERCHANTID, null);
                        pPB();
                    }
                }else{
                    SPM.setString(Constantes.TIENDA_NOMBRE, null);
                    SPM.setString(Constantes.TIENDA_MERCHANTID, null);
                    mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion_sb) + response.message());
                    pPB();
                }
            }

            @Override
            public void onFailure(Call<ResponseTienda> call, Throwable t) {
                Log.e("LOGCAT", "ErrorConsultaTienda: "+call + t);
                SPM.setString(Constantes.TIENDA_NOMBRE, null);
                SPM.setString(Constantes.TIENDA_MERCHANTID, null);
                pPB();
                mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion) + t.getMessage());
            }
        });
    }

    private void configurarPerifericos() {
        iPB("Consultando Perifericos...");

        String tienda = SPM.getString(Constantes.TIENDA_CODE);
        String caja = SPM.getString(Constantes.CAJA_CODE);
        RequestPerifericos requestPerifericos = new RequestPerifericos(tienda, caja);
        Call<ResponseConsultarPerifericos> call = Utilidades.servicioRetrofit().doConsultarPerifericos(
                SPM.getString(Constantes.USER_NAME)+" - "+getResources().getString(R.string.version_apk),requestPerifericos);
        call.enqueue(new Callback<ResponseConsultarPerifericos>() {
            @Override
            public void onResponse(@NonNull Call<ResponseConsultarPerifericos> call, @NonNull Response<ResponseConsultarPerifericos> response) {
                if(response.isSuccessful()) {
                    assert response.body() != null;
                    if(response.body().isEsValida()){
                        Utilidades.guardarPerifericos(response.body().getListPerifericos());
                        Utilidades.mjsToast("Parámetros de impresión y pago TEF cargados correctamente.",
                                Constantes.TOAST_TYPE_SUCCESS, Toast.LENGTH_LONG, contexto);
                        pPB();
                    }else{
                        pPB();
                        Utilidades.sweetAlertCustom(getResources().getString(R.string.mensaje), response.body().getMensaje(), contexto,
                                R.drawable.perifericos);
                    }
                }else{
                    pPB();
                    Utilidades.sweetAlert(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion_sb) + response.message(),
                            SweetAlertDialog.ERROR_TYPE, contexto);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseConsultarPerifericos> call, @NonNull Throwable t) {
                Log.e("LOGCAT", "ErrorResponseConsultarPerifericos: "+call + t);
                pPB();
                Utilidades.sweetAlert(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion) + t.getMessage(),
                        SweetAlertDialog.ERROR_TYPE, contexto);
            }
        });
    }

    private boolean fechaValida(String fechaHora, int rango) {

        Date dateServer = null, dateTelefonoMas10 = null, dateTelefonoMenos10 = null;
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, rango);
        String mas10 = df.format(cal.getTime());
        cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, (rango*-1));
        String menos10 = df.format(cal.getTime());

        try {
            dateTelefonoMas10 = df.parse(mas10);
            dateTelefonoMenos10 = df.parse(menos10);
            dateServer = df.parse(fechaHora);
        } catch (ParseException e) {
            e.printStackTrace();
            return true;
        }

        if(dateServer.after(dateTelefonoMas10)){
            return false;
        }

        if(dateServer.before(dateTelefonoMenos10)){
            return false;
        }

        return true;
    }

    private void msjToast(String msj) {
        Toast.makeText(ConfigActivity.this, msj, Toast.LENGTH_SHORT).show();
    }

    //Alert Dialog para mostrar mensaje de Error o alerta
    public void mensajeSimpleDialog(String titulo, String msj) {

        int icon = R.drawable.msj_alert_30;
        if(titulo.equals(getResources().getString(R.string.error))){
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
    public void onBackPressed(){
        moveTaskToBack(true);
    }

    @Override
    public void tipoComunicacionDatafono(TipoDatafono tipoDatafono) {
        tipoTefDialogFragment.dismiss();
        Intent i;

        if(tipoDatafono.getNombreDispositivo().equals(Constantes.DATAFONO_SMARTPOS_N6)){
            i = new Intent(ConfigActivity.this, DatafonoN6Activity.class);
        }else{
            i = new Intent(ConfigActivity.this, DatafonoActivity.class);
        }

        i.putExtra("tipoOperacion",  "testComunicacion");
        i.putExtra("tipoDatafono", tipoDatafono);
        startActivity(i);
    }
}
