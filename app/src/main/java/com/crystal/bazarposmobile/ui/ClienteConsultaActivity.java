package com.crystal.bazarposmobile.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.common.Constantes;
import com.crystal.bazarposmobile.common.IConfigurableActivity;
import com.crystal.bazarposmobile.common.IConfigurableCarga;
import com.crystal.bazarposmobile.common.LogFile;
import com.crystal.bazarposmobile.common.NetworkUtil;
import com.crystal.bazarposmobile.common.SPM;
import com.crystal.bazarposmobile.common.TipoDatafono;
import com.crystal.bazarposmobile.common.Utilidades;
import com.crystal.bazarposmobile.db.BazarPosMovilDB;
import com.crystal.bazarposmobile.db.entity.DocHeaderEntity;
import com.crystal.bazarposmobile.db.entity.MediosPagoCajaEntity;
import com.crystal.bazarposmobile.db.entity.ProductoEntity;
import com.crystal.bazarposmobile.db.entity.TarjetaEntity;
import com.crystal.bazarposmobile.retrofit.AppCliente;
import com.crystal.bazarposmobile.retrofit.ApiService;
import com.crystal.bazarposmobile.retrofit.request.RequestEanes;
import com.crystal.bazarposmobile.retrofit.request.RequestParametros;
import com.crystal.bazarposmobile.retrofit.request.datafonon6.RequestActualizarTransaccion;
import com.crystal.bazarposmobile.retrofit.response.datafonon6.CuerpoRespuestaDatafonoN6;
import com.crystal.bazarposmobile.retrofit.response.datafonon6.ResponseActualizarTransaccion;
import com.crystal.bazarposmobile.retrofit.response.datafonon6.ResponseAnularCompraN6;
import com.crystal.bazarposmobile.retrofit.response.datafonon6.ResponseBorrarCompraN6;
import com.crystal.bazarposmobile.retrofit.response.datafonon6.ResponseConsultarCompraN6;
import com.crystal.bazarposmobile.retrofit.response.datafonon6.ResponseRespuestasDatafono;
import com.crystal.bazarposmobile.retrofit.response.datafonon6.RespuestaCompletaTef;
import com.crystal.bazarposmobile.retrofit.response.eanes.ResponseEanes;
import com.crystal.bazarposmobile.retrofit.response.ResponseValidarAperturaCaja;
import com.crystal.bazarposmobile.retrofit.response.cliente.Cliente;
import com.crystal.bazarposmobile.retrofit.response.cliente.ResponseClienteLista;
import com.crystal.bazarposmobile.retrofit.response.consecutivofiscal.ResponseConsecutivoFiscal;
import com.crystal.bazarposmobile.retrofit.response.ResponseParametros;
import com.crystal.bazarposmobile.retrofit.response.caja.Caja;
import com.crystal.bazarposmobile.retrofit.response.caja.ResponseCajas;
import com.crystal.bazarposmobile.retrofit.response.eanes.Producto;
import com.crystal.bazarposmobile.retrofit.response.mediospagocaja.MediosCaja;
import com.crystal.bazarposmobile.retrofit.response.mediospagocaja.ResponseMediosCaja;
import com.crystal.bazarposmobile.retrofit.response.tarjetasbancarias.ResponseTarjetasBancarias;
import com.crystal.bazarposmobile.retrofit.response.tarjetasbancarias.Tarjeta;
import com.crystal.bazarposmobile.ui.dialogfragmen.AnularTefDialogFragment;
import com.crystal.bazarposmobile.ui.dialogfragmen.AutorizacionDialogFragment;
import com.crystal.bazarposmobile.ui.dialogfragmen.AutorizacionProductoDialogFragment;
import com.crystal.bazarposmobile.ui.dialogfragmen.ClienteSelectDialogFragment;
import com.crystal.bazarposmobile.ui.dialogfragmen.MostrarInfoDialogFragment;
import com.crystal.bazarposmobile.ui.dialogfragmen.MsjCustomDosAccionesDialogFragment;
import com.crystal.bazarposmobile.ui.dialogfragmen.MsjCustomUnaAccionDialogFragment;
import com.crystal.bazarposmobile.ui.dialogfragmen.TipoTefDialogFragment;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClienteConsultaActivity
        extends
            AppCompatActivity
        implements
            View.OnClickListener,
            ClienteSelectDialogFragment.OnInputListener,
            IConfigurableActivity,
            AnularTefDialogFragment.OnInputListener,
            AutorizacionDialogFragment.OnInputListener,
            IConfigurableCarga{

    //Declaración del Cliente REST
    ApiService apiService;
    String usuario = SPM.getString(Constantes.USER_NAME) + " - " + SPM.getString(Constantes.CAJA_CODE);

    //Declaración de los objetos de la interfaz del activity
    MenuItem pbmenu;
    Button btnconsultar, btngenerico;
    EditText etclienteID;
    TextView tvcaja, tvcajero;
    ImageButton btnconsultaF;
    private IConfigurableCarga configurableCarga;
    AnularTefDialogFragment anularTefDialogFragment;

    //Declaración de la variables del activity
    String idcliente,tiposclientes;
    String caja = SPM.getString(Constantes.CAJA_CODE);
    String tienda = SPM.getString(Constantes.TIENDA_CODE);
    String pais = SPM.getString(Constantes.PAIS_CODE);
    boolean cajaActualizada,noproceso;
    Context contexto;
    Utilidades util;
    private String estadoTransacion = "";
    private RespuestaCompletaTef respuestaCompletaTef;
    TipoTefDialogFragment tipoTefDialogFragment;
    MsjCustomDosAccionesDialogFragment msjCustomDosAccionesDialogFragment;
    private int tiempoEsperaDatafono;
    private boolean transaccionFinalizada;
    private CountDownTimer countDownTimer;
    private Timer tiempo;
    private TimerTask tarea;
    private long tiempoEsperaCierreDetafono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_consulta);
        this.setTitle(R.string.titulo_clente);

        if(SPM.getBoolean(Constantes.MODO_AUTONOMO)){
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorModoAutonomo)));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Toolbar toolbar = this.findViewById(R.id.action_bar);
                if (toolbar!= null){
                    toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        }

        cajaActualizada = true;
        noproceso = true;

        retrofitInit();
        findViews();
        events();

        //Asignar como vendedor a el cajero
        SPM.setString(Constantes.VENDEDOR_CODE, SPM.getString(Constantes.CAJERO_CODE));
        SPM.setString(Constantes.VENDEDOR_NAME, SPM.getString(Constantes.CAJERO_NAME));

        if(!SPM.getBoolean(Constantes.MODO_AUTONOMO)){
            //Validar si se recibe desde Intent el String actualizarcaja
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                if (extras.containsKey("actualizarcaja")) {
                    actualizarCaja();
                    //Controlador para iPB
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            iPB();
                        }
                    }, 50);
                }
            }
        }else{
            etclienteID.setVisibility(View.INVISIBLE);
            btnconsultar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflaterMenu = getMenuInflater();
        inflaterMenu.inflate(R.menu.menucliente,menu);
        pbmenu = menu.findItem(R.id.progressbar_menu);
        pbmenu.setEnabled(false);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AutorizacionDialogFragment autoDF;
        Intent i;
        TipoDatafono tipoDatafono = (TipoDatafono) SPM.getObject(Constantes.OBJECT_TIPO_DATAFONO, TipoDatafono.class);
        switch (item.getItemId()){
            case R.id.mItemTEFCierre:
                if(tipoDatafono != null){
                    if(tipoDatafono.getNombreDispositivo().equals(Constantes.DATAFONO_SMARTPOS_N6)){
                        Utilidades.sweetAlert(getResources().getString(R.string.mensaje),
                                "El datafono "+tipoDatafono.getNombreDispositivo()+" no cuenta con esta opción",
                                SweetAlertDialog.NORMAL_TYPE, ClienteConsultaActivity.this);
                    }else{
                        i = new Intent(this, DatafonoActivity.class);
                        i.putExtra("tipoOperacion", "cierre");
                        i.putExtra("tipoDatafono", tipoDatafono);
                        startActivity(i);
                    }
                }else{
                    Utilidades.sweetAlert(getResources().getString(R.string.alerta),
                            "Debe configurar el tipo de Datafono a utilizar",
                            SweetAlertDialog.WARNING_TYPE, ClienteConsultaActivity.this);
                }
                break;
            case R.id.mItemTEFUltima:
                if(tipoDatafono != null){
                    if(tipoDatafono.getNombreDispositivo().equals(Constantes.DATAFONO_SMARTPOS_N6)){
                        if(SPM.getString(Constantes.ID_TRANSACCION_DATAFONO) != null){
                            consultarUltimaTefN6();
                        }else{
                            Utilidades.sweetAlert(getResources().getString(R.string.mensaje),
                                    "No se encontro una ultima Transacción para el datafono",
                                    SweetAlertDialog.NORMAL_TYPE, ClienteConsultaActivity.this);
                        }
                    }else{
                        i = new Intent(this, DatafonoActivity.class);
                        i.putExtra("tipoOperacion", "ultimaTransaccion");
                        i.putExtra("tipoDatafono", tipoDatafono);
                        startActivity(i);
                    }
                }else{
                    Utilidades.sweetAlert(getResources().getString(R.string.alerta),
                            "Debe configurar el tipo de Datafono a utilizar",
                            SweetAlertDialog.WARNING_TYPE, ClienteConsultaActivity.this);
                }
                break;
            case R.id.mItemTEFAnulacion:
                if(tipoDatafono != null){
                    if(tipoDatafono.getNombreDispositivo().equals(Constantes.DATAFONO_SMARTPOS_N6)){
                        ConsultarRespuestasTef();
                    }else{
                        i = new Intent(this, DatafonoActivity.class);
                        i.putExtra("tipoOperacion", "anular");
                        i.putExtra("tipoDatafono", tipoDatafono);
                        startActivity(i);
                    }
                }else{
                    Utilidades.sweetAlert(getResources().getString(R.string.alerta),
                            "Debe configurar el tipo de Datafono a utilizar",
                            SweetAlertDialog.WARNING_TYPE, ClienteConsultaActivity.this);
                }
                break;
            case R.id.mItemCerrarSesion:
                apagar();
                break;
            case R.id.mItemActualizarCaja:
                actualizarCaja();
                break;
            case R.id.mItemTestComunicacionCaja:
                tipoTefDialogFragment = new TipoTefDialogFragment(this);
                tipoTefDialogFragment.show(getSupportFragmentManager(),"TipoTefDialogFragment");
                break;
            case R.id.mItemConsultarCliente:
                if(SPM.getBoolean(Constantes.MODO_AUTONOMO)){
                    msjToast("El modo autonómo esta activo, no puedes consultar los clientes.");
                }else{
                    etclienteID.setVisibility(View.VISIBLE);
                    btnconsultar.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.mItemTEFContinguencia:
                i = new Intent(ClienteConsultaActivity.this, TEFContingenciaActivity.class);
                startActivity(i);
                break;
            case R.id.mItemGestionMA:
                i = new Intent(ClienteConsultaActivity.this, ModoAutonomoActivity.class);
                startActivity(i);
                break;
            case R.id.mItemAperturaCierreMA:
                if(SPM.getBoolean(Constantes.MODO_AUTONOMO)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ClienteConsultaActivity.this);
                    builder.setTitle(R.string.modo_autonomo)
                            .setMessage(R.string.confirme_realizar_cierre_apertura)
                            .setPositiveButton(R.string.confirmar,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            try {
                                                realizarCierreApertura();
                                                mensajeSimpleDialog(getResources().getString(R.string.modo_autonomo), "Cierre y apertura realizada satifactoriamente.");
                                            } catch (ExecutionException | InterruptedException e) {
                                                e.printStackTrace();
                                                mensajeSimpleDialog(getResources().getString(R.string.error), "Error al realizar el cierre y la apertura de la caja: " + e.getMessage());
                                            }
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
                }else{
                    msjToast(getResources().getString(R.string.modo_autonomo_desactivado));
                }
                break;
            case R.id.mItemObtenerSuspenciones:
                i = new Intent(this, RecuperarTransaccionesSuspendidasActivity.class);
                startActivityForResult(i, Constantes.RESP_SUSPENSIONES);
                break;
            case R.id.mItemReimpresion:
                autoDF = new AutorizacionDialogFragment("ReImpDoc");
                autoDF.show(getSupportFragmentManager(), "AutorizacionDialogFragment");
                break;
            case R.id.mItemReImpresionTEF:
                autoDF = new AutorizacionDialogFragment("ReImpCompTEF");
                autoDF.show(getSupportFragmentManager(), "AutorizacionDialogFragment");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void consultarUltimaTefN6() {
        util.mostrarPantallaCargaCustom(getSupportFragmentManager(),getResources().getString(R.string.progress_consultando_ultima_tef),
                false, configurableCarga);

        Call<ResponseConsultarCompraN6> call = Utilidades.servicioRetrofit().doConsultarCompraN6(usuario+" - "+getResources().getString(R.string.version_apk),
                Utilidades.peticionBaseN6());
        call.enqueue(new Callback<ResponseConsultarCompraN6>() {
            @Override
            public void onResponse(@NonNull Call<ResponseConsultarCompraN6> call, @NonNull Response<ResponseConsultarCompraN6> response) {
                if(response.isSuccessful()) {
                    assert response.body() != null;
                    if(response.body().isEsValida()){
                        String estadoTransacion = response.body().getRespuestaCompra().getEstadoDeTransaccion();
                        if(estadoTransacion.equals(Constantes.ESTADO_TRASACCION_COMPLETA)){
                            util.ocultarPantallaCargaCustom();
                            MostrarInfoDialogFragment vistaDialogFragmentMostrarInfo =
                                    new MostrarInfoDialogFragment(R.drawable.ultima_tef_dos, "Consulta última Transacción TEF",
                                            response.body().getRespuestaCompra().getRespuestaTransaccion().toStringClaveValor(), Constantes.ACCION_TEF);
                            vistaDialogFragmentMostrarInfo.show(getSupportFragmentManager(), "MostrarInfoClaveValor");
                        }else{
                            Utilidades.sweetAlert(getResources().getString(R.string.mensaje),
                                    "No se encontro una ultima Transacción para el datafono",
                                    SweetAlertDialog.NORMAL_TYPE, ClienteConsultaActivity.this);
                        }
                    }else{
                        util.ocultarPantallaCargaCustom();
                        mensajeSimpleDialog(getResources().getString(R.string.error), response.body().getMensaje(),
                                Constantes.SERVICIO_CONSULTAR_ULTIMA_TEF_N6);
                    }
                }else{
                    LogFile.adjuntarLog("Error ResponseConsultarCompraN6: " + response.message());
                    util.ocultarPantallaCargaCustom();
                    mensajeSimpleDialog(getResources().getString(R.string.error), getResources().getString(R.string.error_conexion_sb) + response.message(),
                            Constantes.SERVICIO_CONSULTAR_ULTIMA_TEF_N6);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseConsultarCompraN6> call, @NonNull Throwable t) {
                LogFile.adjuntarLog("Error ResponseConsultarCompraN6: " + call + t);
                util.ocultarPantallaCargaCustom();
                mensajeSimpleDialog(getResources().getString(R.string.error), getResources().getString(R.string.error_conexion) + t.getMessage(),
                        Constantes.SERVICIO_CONSULTAR_ULTIMA_TEF_N6);
            }
        });
    }

    private void ConsultarRespuestasTef() {
        util.mostrarPantallaCargaCustom(getSupportFragmentManager(),"Consultando Respuestas TEF...",
                false, configurableCarga);
        //API consulta respuestas tef
        Call<ResponseRespuestasDatafono> datafonoCall = Utilidades.servicioRetrofit()
                .doRespuestasDatafono(util.getThisDateSimple(), tienda, caja);
        datafonoCall.enqueue(new Callback<ResponseRespuestasDatafono>() {
            @Override
            public void onResponse(@NonNull Call<ResponseRespuestasDatafono> call, @NonNull Response<ResponseRespuestasDatafono> response) {
                if(response.isSuccessful()) {
                    assert response.body() != null;
                    if(response.body().isEsValida()){
                        util.ocultarPantallaCargaCustom();
                        anularTefDialogFragment = new AnularTefDialogFragment(response.body().getListado());
                        anularTefDialogFragment.show(getSupportFragmentManager(), "AnularTefDialogFragment");
                    }else{
                        if(response.body().getMensaje().equals("No se encontraros datos con esos filtros")){
                            util.ocultarPantallaCargaCustom();
                            Utilidades.mjsToast("No se encontraron registros tef de hoy",
                                    Constantes.TOAST_TYPE_INFO, Toast.LENGTH_LONG, contexto);
                        }else{
                            util.ocultarPantallaCargaCustom();
                            mensajeSimpleDialog(contexto.getResources().getString(R.string.error), response.body().getMensaje(),
                                    Constantes.SERVICIO_RESPUESTAS_TEF);
                        }
                    }
                }else{
                    util.ocultarPantallaCargaCustom();
                    mensajeSimpleDialog(contexto.getResources().getString(R.string.error),
                            contexto.getResources().getString(R.string.error_conexion_sb) + response.message(),
                            Constantes.SERVICIO_RESPUESTAS_TEF);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseRespuestasDatafono> call, @NonNull Throwable t) {
                util.ocultarPantallaCargaCustom();
                mensajeSimpleDialog(contexto.getResources().getString(R.string.error),
                        contexto.getResources().getString(R.string.error_conexion) + t.getMessage(),
                        Constantes.SERVICIO_RESPUESTAS_TEF);
            }
        });
    }

    @Override
    public void sendInputItemAutorizacion(String code) {
        Intent i;
        switch (code) {
            case "ReImpDoc":
                i = new Intent(ClienteConsultaActivity.this, DocumentoImpresionActivity.class);
                startActivity(i);
                break;
            case "ReImpCompTEF":
                i = new Intent(ClienteConsultaActivity.this, ComprobanteTEFImprimirActivity.class);
                startActivity(i);
                break;
        }
    }

    private void realizarCierreApertura() throws ExecutionException, InterruptedException {
        Integer num = new HeaderCountAsyncTask().execute().get();
        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
        Date todayDate = new Date();
        String thisDate = currentDate.format(todayDate);
        DocHeaderEntity docHeader = new DocHeaderEntity("1", "divisa", "customerid", thisDate, "CA"+num, "", tienda, "0", "Receipt", tienda, "",0.0,0.0,0);
        new DocHeaderCrearAsyncTask().execute(docHeader).get();
    }

    @Override
    public void tipoComunicacionDatafono(TipoDatafono tipoDatafono) {
        tipoTefDialogFragment.dismiss();
        Intent i;

        if(tipoDatafono.getNombreDispositivo().equals(Constantes.DATAFONO_SMARTPOS_N6)){
            i = new Intent(ClienteConsultaActivity.this, DatafonoN6Activity.class);
        }else{
            i = new Intent(ClienteConsultaActivity.this, DatafonoActivity.class);
        }

        i.putExtra("tipoOperacion",  "testComunicacion");
        i.putExtra("tipoDatafono", tipoDatafono);
        startActivity(i);
    }

    @Override
    public void sendInputItemAnularSelectDialogFragment(RespuestaCompletaTef respuestaDatafono) {
        msjCustomDosAccionesDialogFragment = new MsjCustomDosAccionesDialogFragment(R.drawable.anular_tef_dos,
                String.format(getResources().getString(R.string.seleccione_opcion_tef), respuestaDatafono.getRespuestaTef().getRecibo()),
                getResources().getString(R.string.texto_seleccione_opcion_tef), "",
                getResources().getString(R.string.positivo_anulacion),
                getResources().getString(R.string.negativo_anulacion), Constantes.ACCION_ANULAR_TEF, true);
        msjCustomDosAccionesDialogFragment.show(getSupportFragmentManager(), "MsjCustomDosDialogFragment");
        msjCustomDosAccionesDialogFragment.setClienteConsulta(this);
        msjCustomDosAccionesDialogFragment.setCuerpoRespuestaDatafono(respuestaDatafono.getRespuestaTef());
        msjCustomDosAccionesDialogFragment.setRespuestaCompletaTef(respuestaDatafono);
        respuestaCompletaTef = respuestaDatafono;
    }

    public void mostrarInfoTef(CuerpoRespuestaDatafonoN6 respuestaDatafono){
        if(respuestaDatafono.toStringClaveValor() != null){
            MostrarInfoDialogFragment vistaDialogFragmentMostrarInfo =
                    new MostrarInfoDialogFragment(R.drawable.anular_tef_dos, "Información Tef",
                            respuestaDatafono.toStringClaveValor(), Constantes.ACCION_TEF);
            vistaDialogFragmentMostrarInfo.show(getSupportFragmentManager(), "MostrarInfoClaveValor");
        }else{
            Utilidades.mjsToast("Error al mostrar los datos", Constantes.TOAST_TYPE_INFO,
                    Toast.LENGTH_LONG, contexto);
        }
    }

    public void anularTransaccionTef(RespuestaCompletaTef respuestaDatafono){
        if(util.estadoProgressCustom()){
            util.mostrarPantallaCargaCustom(getSupportFragmentManager(),getResources().getString(R.string.progress_anulando),
                    false, configurableCarga);
            anulacionTransaccionTef(respuestaDatafono);
        }
    }

    private void anulacionTransaccionTef(RespuestaCompletaTef respuestaDatafono) {
        tiempoEsperaDatafono = Utilidades.tiempoDatafono();
        tiempoEsperaCierreDetafono = Utilidades.tiempoEsperaDatafono();

        countDownTimer = new CountDownTimer(tiempoEsperaCierreDetafono, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Actualizar el texto del temporizador en cada tick (cada segundo)
            }

            @Override
            public void onFinish() {
                // Acción a realizar al finalizar el tiempo.
                if(!transaccionFinalizada && NetworkUtil.isConnected(contexto)){
                    borrarAnulacionN6();
                }
                util.ocultarPantallaCargaCustom();
            }
        };

        Call<ResponseAnularCompraN6> call = Utilidades.servicioRetrofit().doAnularCompraN6(usuario+" - "+getResources().getString(R.string.version_apk),
                Utilidades.peticionAnularCompraDatafonoN6(Integer.parseInt(respuestaDatafono.getRespuestaTef().getRecibo())));
        call.enqueue(new Callback<ResponseAnularCompraN6>() {
            @Override
            public void onResponse(@NonNull Call<ResponseAnularCompraN6> call, @NonNull Response<ResponseAnularCompraN6> response) {
                if(response.isSuccessful()){
                    assert response.body() != null;
                    if(response.body().isEsValida()){
                        util.ocultarPantallaCargaCustom();
                        util.mostrarPantallaCargaCustom(getSupportFragmentManager(),getResources()
                                        .getString(R.string.progress_seguir_pasos_datafono_n6_anulacion),
                                true, configurableCarga);
                        countDownTimer.start();
                        SPM.setString(Constantes.ID_TRANSACCION_DATAFONO, Integer.toString(response.body().getRespuesta().getIdDeTransaccion()));
                        iniciarTareaRespuestaDatafonoAnulacion(respuestaDatafono);
                    }else{
                        transaccionFinalizada = false;
                        util.ocultarPantallaCargaCustom();
                        mensajeSimpleDialog(getResources().getString(R.string.error), response.body().getMensaje(),
                                Constantes.SERVICIO_ANULAR_TEF);
                    }
                }else{
                    transaccionFinalizada = false;
                    mensajeSimpleDialog(getResources().getString(R.string.error), getResources().getString(R.string.error_conexion_sb) + response.message(),
                            Constantes.SERVICIO_ANULAR_TEF);
                    util.ocultarPantallaCargaCustom();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseAnularCompraN6> call, @NonNull Throwable t) {
                util.ocultarPantallaCargaCustom();
                transaccionFinalizada = false;
                Log.e("LOGCAT", "Error doAnularCompraN6: " + call + t);
                mensajeSimpleDialog(getResources().getString(R.string.error), getResources().getString(R.string.error_conexion) + t.getMessage(), Constantes.SERVICIO_ANULAR_TEF);
            }
        });
    }

    private void iniciarTareaRespuestaDatafonoAnulacion(RespuestaCompletaTef respuestaDatafono) {
        tarea = new TimerTask() {
            @Override
            public void run() {
                if(util.estadoProgressCustom()){
                    if(tarea != null){
                        tarea.cancel();
                    }

                    if(tiempo != null){
                        tiempo.cancel();
                    }
                }else{
                    apiRespuestaDatafono(respuestaDatafono);
                }
            }
        };

        tiempo = new Timer();
        tiempo.schedule(tarea, tiempoEsperaDatafono);
    }

    private void apiRespuestaDatafono(RespuestaCompletaTef respuestaDatafono) {
        Call<ResponseConsultarCompraN6> call = Utilidades.servicioRetrofit().doConsultarAnulacionN6(usuario+" - "+getResources().getString(R.string.version_apk),
                Utilidades.peticionBaseN6());
        call.enqueue(new Callback<ResponseConsultarCompraN6>() {
            @Override
            public void onResponse(@NonNull Call<ResponseConsultarCompraN6> call, @NonNull Response<ResponseConsultarCompraN6> response) {
                if(response.isSuccessful()){
                    assert response.body() != null;
                    if(response.body().isEsValida()){
                        estadoTransacion = response.body().getRespuestaCompra().getEstadoDeTransaccion();
                        switch (estadoTransacion){
                            case Constantes.ESTADO_TRASACCION_COMPLETA:
                                transaccionFinalizada = true;
                                util.ocultarPantallaCargaCustom();
                                util.mostrarPantallaCargaCustom(getSupportFragmentManager(),getResources()
                                                .getString(R.string.progress_actualizando_tef),
                                        false, configurableCarga);
                                actualizarRespuestaTef(respuestaDatafono);
                                break;
                            case Constantes.ESTADO_TRASACCION_CREADA:
                            case Constantes.ESTADO_TRASACCION_PENDIENTE:
                                iniciarTareaRespuestaDatafonoAnulacion(respuestaDatafono);
                                break;
                        }
                    }else{
                        transaccionFinalizada = false;
                        LogFile.adjuntarLog("Error ResponseConsultarCompraN6: " + response.body().getMensaje());
                        util.ocultarPantallaCargaCustom();
                        Utilidades.sweetAlert(getResources().getString(R.string.error), response.body().getMensaje(),
                                SweetAlertDialog.ERROR_TYPE, contexto);
                    }
                }else{
                    if(!NetworkUtil.isConnected(contexto)){
                        sweetAlertCustomClienteAnulacion(getResources().getString(R.string.informacion),
                                getResources().getString(R.string.tv_msj_error_conexion_n6_anulacion),
                                contexto, R.drawable.offline);

                        countDownTimer.cancel();
                    }

                    transaccionFinalizada = false;
                    LogFile.adjuntarLog("Error ResponseConsultarCompraN6: " + response.message());
                    util.ocultarPantallaCargaCustom();
                    Utilidades.sweetAlert(getResources().getString(R.string.error), getResources().getString(R.string.error_conexion_sb) + response.message(),
                            SweetAlertDialog.ERROR_TYPE, contexto);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseConsultarCompraN6> call, @NonNull Throwable t) {
                if(!NetworkUtil.isConnected(contexto)){
                    sweetAlertCustomClienteAnulacion(getResources().getString(R.string.informacion),
                            getResources().getString(R.string.tv_msj_error_conexion_n6_anulacion),
                            contexto, R.drawable.offline);

                    countDownTimer.cancel();
                }

                transaccionFinalizada = false;
                LogFile.adjuntarLog("Error ResponseConsultarCompraN6: " + call + t);
                util.ocultarPantallaCargaCustom();
                Utilidades.sweetAlert(getResources().getString(R.string.error), getResources().getString(R.string.error_conexion) + t.getMessage(),
                        SweetAlertDialog.ERROR_TYPE, contexto);
            }
        });
    }

    public void sweetAlertCustomClienteAnulacion(String titulo, String mensaje, Context contexto, int imagen){
        SweetAlertDialog alertDialog = new SweetAlertDialog(contexto, SweetAlertDialog.CUSTOM_IMAGE_TYPE);

        alertDialog.setTitleText(titulo)
                .setCustomImage(imagen)
                .setContentText(mensaje)
                .setConfirmButton(contexto.getResources().getString(R.string.comprobar_anulacion), sweetAlertDialog -> {
                    if(NetworkUtil.isConnected(contexto)){
                        sweetAlertDialog.dismissWithAnimation();
                        util.mostrarPantallaCargaCustom(getSupportFragmentManager(),getResources()
                                        .getString(R.string.progress_seguir_pasos_datafono_n6_anulacion),
                                true, configurableCarga);
                        countDownTimer.start();
                        iniciarTareaRespuestaDatafonoAnulacion(respuestaCompletaTef);
                    }else{
                        Utilidades.mjsToast("Todavía no hay conexión a internet", Constantes.TOAST_TYPE_INFO,
                                Toast.LENGTH_LONG, contexto);
                    }
                })
                .setCancelable(false);

        alertDialog.show();
    }

    private void borrarAnulacionN6() {
        Call<ResponseBorrarCompraN6> call = Utilidades.servicioRetrofit().doBorrarAnulacionN6(usuario+" - "+getResources().getString(R.string.version_apk),
                Utilidades.peticionBaseN6());
        call.enqueue(new Callback<ResponseBorrarCompraN6>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBorrarCompraN6> call, @NonNull Response<ResponseBorrarCompraN6> response) {
                if(response.isSuccessful()){
                    assert response.body() != null;
                    if(response.body().isEsValida()){
                        if(tarea != null){
                            tarea.cancel();
                        }

                        if(tiempo != null){
                            tiempo.cancel();
                        }

                        countDownTimer.cancel();

                        Utilidades.mjsToast(response.body().getRespuesta().getMensajeDeRespuesta(),
                                Constantes.TOAST_TYPE_INFO, Toast.LENGTH_LONG, contexto);
                    }else{
                        LogFile.adjuntarLog("Error ResponseBorrarCompraN6: " + response.body().getMensaje());
                        util.ocultarPantallaCargaCustom();
                        Utilidades.sweetAlert(getResources().getString(R.string.error), response.body().getMensaje(),
                                SweetAlertDialog.ERROR_TYPE, contexto);
                    }
                }else{
                    LogFile.adjuntarLog("Error ResponseBorrarCompraN6: " + response.message());
                    util.ocultarPantallaCargaCustom();
                    Utilidades.sweetAlert(getResources().getString(R.string.error), getResources().getString(R.string.error_conexion_sb) + response.message(),
                            SweetAlertDialog.ERROR_TYPE, contexto);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBorrarCompraN6> call, @NonNull Throwable t) {
                LogFile.adjuntarLog("Error ResponseBorrarCompraN6: " + call + t);
                util.ocultarPantallaCargaCustom();
                Utilidades.sweetAlert(getResources().getString(R.string.error), getResources().getString(R.string.error_conexion) + t.getMessage(),
                        SweetAlertDialog.ERROR_TYPE, contexto);
            }
        });
    }

    private class DocHeaderCrearAsyncTask extends AsyncTask<DocHeaderEntity,Void,Void>{

        @Override
        protected Void doInBackground(DocHeaderEntity... docHeaderEntity) {
            try{
                BazarPosMovilDB.getBD(getApplication()).documentoDao().insertHeader(docHeaderEntity[0]);
            }catch (Exception ex){
                msjToast("Error al crear el documento: "+ex.getMessage());
            }
            return null;
        }
    }

    protected class HeaderCountAsyncTask extends AsyncTask<Void,Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            return BazarPosMovilDB.getBD(getApplication()).documentoDao().getHeaderCount();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Obtendremos resultados de escaneo aquí
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        //Validar si la respuest es vacia
         if(requestCode == Constantes.RESP_SUSPENSIONES && data != null) {
            List<Producto> productosList = (List<Producto>) data.getSerializableExtra("productosList");
            Cliente cliente = (Cliente) data.getSerializableExtra("cliente");
            if(cliente == null){
                clienteGenerico(productosList);
            }else{
                cargarCliente(cliente);
            }
        }else if (result != null) {
            if (result.getContents() == null) {
                msjToast(getResources().getString(R.string.escaneo_cancelado));
            } else {
                etclienteID.setText(result.getContents());
            }
        }
    }

    private void iPB() {
        noproceso = false;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        if(pbmenu != null)
            pbmenu.setActionView(R.layout.menu_item_porgressbar_layout);
    }

    private void pPB() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        if(pbmenu != null)
            pbmenu.setActionView(null);
        noproceso = true;
    }

    //Instanciar el Cliebte REST
    private void retrofitInit() {
        AppCliente appCliente = AppCliente.getInstance();
        apiService = appCliente.getApiService();
    }

    //Asignacion de Referencias
    private void findViews() {
        contexto = ClienteConsultaActivity.this;
        util = new Utilidades();
        configurableCarga = this;
        btnconsultar = findViewById(R.id.btnConsultarClienteA);
        btngenerico = findViewById(R.id.btnGenericoClienteA);
        etclienteID = findViewById(R.id.etIDClienteA);
        tvcaja = findViewById(R.id.tvCajaClienteA);
        tvcajero = findViewById(R.id.tvCajeroClienteA);
        btnconsultaF = findViewById(R.id.ibActualizarClienteA);
        btnconsultaF.setVisibility(View.GONE);

        tvcaja.setText(caja);
        tvcajero.setText(SPM.getString(Constantes.CAJERO_NAME));

        etclienteID.setVisibility(View.VISIBLE);

        btnconsultar.setVisibility(View.VISIBLE);
        btngenerico.setVisibility(View.VISIBLE);
    }

    //Asignacion de eventos
    private void events() {
        btngenerico.setOnClickListener(this);
        btnconsultar.setOnClickListener(this);
        btnconsultaF.setOnClickListener(this);

        //Evento sobre el EditText Texto ID
        etclienteID.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if(noproceso){
                        iPB();
                        consultarCliente();
                    }
                    return true;
                }
                return false;
            }
        });

        etclienteID.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if(noproceso){
                        iPB();
                        consultarCliente();
                    }
                }
                return handled;
            }
        });

        etclienteID.setOnLongClickListener(
                new View.OnLongClickListener() {
                    public boolean onLongClick(View view) {
                        new IntentIntegrator(ClienteConsultaActivity.this).setCaptureActivity(LectorCodigosCamaraActivity.class).initiateScan();
                        return false;
                    }
                }
        );
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.btnConsultarClienteA:
                if(noproceso){
                    iPB();
                    consultarCliente();
                }
                break;
            case R.id.btnGenericoClienteA:
                if(noproceso){
                    iPB();
                    clienteGenerico(null);
                }
                break;
            case R.id.ibActualizarClienteA:
                if(noproceso){
                    iPB();
                    consultaFiscal_Caja();
                }
                break;
        }
    }

    //Asignacion de cliente generico en la compra
    private void clienteGenerico(List<Producto> productosList) {

        String idGenerico = SPM.getString(Constantes.CLIENTE_GENERICO);

        SPM.setString(Constantes.FIRST_NAME_CLIENTE, getResources().getString(R.string._cliente));
        SPM.setString(Constantes.LAST_NAME_CLIENTE, getResources().getString(R.string._generico));
        SPM.setString(Constantes.CUSTOMER_ID, idGenerico);
        SPM.setString(Constantes.DOCUMENTO_CLIENTE, idGenerico);
        SPM.setString(Constantes.TIPO_DOCUMENTO_CLIENTE_DESC_L, getResources().getString(R.string.cedula_ciudadania_letra));

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

        Intent i = new Intent(ClienteConsultaActivity.this, CajaActivity.class);
        if(productosList != null){
            i.putExtra("productosList", (Serializable) productosList);
        }
        startActivity(i);
        pPB();
    }

    private void consultarCliente() {

        idcliente = etclienteID.getText().toString();
        if(idcliente.length() > 0){
            if(idcliente.length() < 6 || idcliente.length() > 10){
                msjToast(getResources().getString(R.string.numero_identificacion_invalido));
                etclienteID.setText("");
                etclienteID.requestFocus();
                pPB();
            }else{
                if(SPM.getString(Constantes.CAJERO_CODE).equals(idcliente)){
                    msjToast(getResources().getString(R.string.autoregistro_cliente));
                    etclienteID.setText("");
                    pPB();
                }else{
                    //Ocultar el teclado de pantalla
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etclienteID.getWindowToken(), 0);

                    //Consultar la Api de Cliente
                    Call<ResponseClienteLista> call = apiService.doClienteBuscar(usuario,idcliente);
                    call.enqueue(new Callback<ResponseClienteLista>() {
                        @Override
                        public void onResponse(Call<ResponseClienteLista> call, Response<ResponseClienteLista> response) {
                            if(response.isSuccessful()){
                                assert response.body() != null;
                                if(response.body().getError()){
                                    if(response.body().getMensaje().length() > 0){
                                        mensajeSimpleDialog(getResources().getString(R.string.error),response.body().getMensaje());
                                    }else{
                                        mensajeSimpleDialog(getResources().getString(R.string.alerta),getResources().getString(R.string.no_docuemnto_no_habeas));
                                    }
                                }else{
                                    List<Cliente> clientes = response.body().getClientes();
                                    tiposclientes = ";";
                                    for(Cliente c:clientes){
                                        tiposclientes = tiposclientes + c.getTipoDocumento() + ";";
                                    }
                                    if(clientes.size() == 1){
                                        tiposclientes = "";
                                        cargarCliente(clientes.get(0));
                                    }else{
                                        ClienteSelectDialogFragment dialogClienteSelect = new ClienteSelectDialogFragment(response.body().getClientes());
                                        dialogClienteSelect.show(getSupportFragmentManager(),"ClienteSelect");
                                    }
                                }
                            }else{
                                mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion_consultar_cliente));
                            }
                            pPB();
                            etclienteID.setText("");
                        }
                        @Override
                        public void onFailure(Call<ResponseClienteLista> call, Throwable t) {
                            Log.e("LOGCAT", "Error ResponseClienteLista: "+call + t);
                            pPB();
                            msjToast(getResources().getString(R.string.error_conexion) + t.getMessage());
                        }
                    });
                }
            }

        }else{
            msjToast(getResources().getString(R.string.ident_req_cliente));
            pPB();
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

        Intent i = new Intent(ClienteConsultaActivity.this, CajaActivity.class);
        i.putExtra("tiposclientes",  tiposclientes);
        startActivity(i);

    }

    //Cerrar sesion para el usuario cajero en la aplicacion
    private void apagar() {
        SweetAlertDialog alertDialog = new SweetAlertDialog(ClienteConsultaActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE);

        alertDialog.setCustomImage(R.mipmap.cerrar_sesion)
                .setTitleText(getResources().getString(R.string.cerrar_sesion))
                .setContentText(getResources().getString(R.string.cerrar_sesion_confirmar))
                .setConfirmButton(R.string.si, sweetAlertDialog -> {
                    sweetAlertDialog.dismissWithAnimation();
                    Intent intent = new Intent(ClienteConsultaActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                })
                .setCancelButton(R.string.no, SweetAlertDialog::dismissWithAnimation)
                .setCancelable(false);

        alertDialog.show();
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

        if(!(ClienteConsultaActivity.this.isFinishing())){
            alert.show();
        }
    }

    public void mensajeSimpleDialog(String titulo, String msj, int servicio) {
        if(servicio == Constantes.SERVICIO_ACTUALIZAR_TRANSACCION_TEF){
            @SuppressLint("StringFormatMatches") MsjCustomUnaAccionDialogFragment msjCustomUnaAccionDialogFragment = new MsjCustomUnaAccionDialogFragment(R.drawable.advertencia,
                    getResources().getString(R.string.ups_algo_mal),
                    String.format(getResources().getString(R.string.ups_algo_mal_msj_actualizar), servicio), msj,
                    getResources().getString(R.string.reintentar), Constantes.ACCION_ACTUALIZAR_TRANSACCION_TEF);
            msjCustomUnaAccionDialogFragment.show(getSupportFragmentManager(), "MsjCustomDialogFragment");
            msjCustomUnaAccionDialogFragment.setClienteConsultaActivity(this);
        }else{
            int icon = SweetAlertDialog.WARNING_TYPE;
            if (titulo.equals(getResources().getString(R.string.error))) {
                icon = SweetAlertDialog.ERROR_TYPE;
            }

            Utilidades.sweetAlert(titulo, msj + " Cod ("+servicio+")", icon, ClienteConsultaActivity.this);
        }
    }

    public void reintentarActualizacionTransaccion(){
        if(util.estadoProgressCustom()){
            util.mostrarPantallaCargaCustom(getSupportFragmentManager(),getResources().
                            getString(R.string.progress_cargando),
                    false, configurableCarga);
            actualizarRespuestaTef(respuestaCompletaTef);
        }
    }

    private void actualizarRespuestaTef(RespuestaCompletaTef respuestaCompletaTef) {
        RequestActualizarTransaccion requestActualizarTransaccion =
                new RequestActualizarTransaccion(respuestaCompletaTef.getHeader().getNumeroTransaccion(),
                        tienda, caja, respuestaCompletaTef.getHeader().getReferenciaInterna());

        Call<ResponseActualizarTransaccion> call = Utilidades.servicioRetrofit().doActualizarAnulacionN6(
                usuario+" - "+getResources().getString(R.string.version_apk),requestActualizarTransaccion);

        call.enqueue(new Callback<ResponseActualizarTransaccion>() {
            @Override
            public void onResponse(@NonNull Call<ResponseActualizarTransaccion> call, @NonNull Response<ResponseActualizarTransaccion> response) {
                if(response.isSuccessful()) {
                    assert response.body() != null;
                    LogFile.adjuntarLog(response.body().toString());
                    if(response.body().isEsValida()){
                        util.ocultarPantallaCargaCustom();
                        Utilidades.sweetAlert("Transacción Anulada ("+respuestaCompletaTef.getRespuestaTef().getRecibo()+")",
                                "La transacción se anulo correctamente",
                                SweetAlertDialog.SUCCESS_TYPE, contexto);
                        msjCustomDosAccionesDialogFragment.dismiss();
                        anularTefDialogFragment.dismiss();
                    }else{
                        util.ocultarPantallaCargaCustom();
                        mensajeSimpleDialog(contexto.getResources().getString(R.string.error), response.body().getMensaje(),
                                Constantes.SERVICIO_ACTUALIZAR_TRANSACCION_TEF);
                    }
                }else{
                    util.ocultarPantallaCargaCustom();
                    mensajeSimpleDialog(contexto.getResources().getString(R.string.error),
                            contexto.getResources().getString(R.string.error_conexion_sb) + response.message(),
                            Constantes.SERVICIO_ACTUALIZAR_TRANSACCION_TEF);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseActualizarTransaccion> call, @NonNull Throwable t) {
                util.ocultarPantallaCargaCustom();
                mensajeSimpleDialog(getResources().getString(R.string.error),
                        getResources().getString(R.string.error_conexion) + t.getMessage(),
                        Constantes.SERVICIO_ACTUALIZAR_TRANSACCION_TEF);
            }
        });
    }

    private void consultaFiscal_Caja(){
        btnconsultar.setEnabled(false);
        btngenerico.setEnabled(false);
        etclienteID.setEnabled(false);
        //Consultar la Api de Apertura de Caja
        Call<ResponseValidarAperturaCaja> call2 = apiService.doAperturaCaja(usuario,caja);
        call2.enqueue(new Callback<ResponseValidarAperturaCaja>() {
            @Override
            public void onResponse(Call<ResponseValidarAperturaCaja> call, Response<ResponseValidarAperturaCaja> response) {
                //Validar que la respuesta de Api sea correcta
                if (response.isSuccessful()) {
                    //Validar que la caja tiene apertura
                    assert response.body() != null;
                    if(response.body().getEsValida()){
                        //Validar que la caja este abierta
                        if(response.body().getEstadoCaja().equals("ABIERTA")) {

                            tvcajero.setText(response.body().getNombreVendedor());
                            SPM.setString(Constantes.VENDEDOR_CODE, response.body().getCajero());
                            SPM.setString(Constantes.VENDEDOR_NAME, response.body().getNombreVendedor());
                            SPM.setInt(Constantes.NUMERO_DIA_APERTURA, response.body().getNumeroDia());

                            //Crear mensaje de alerta si la caja fue abierta un dia anterior
                            String alertMSJ = "";
                            try {
                                SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
                                Date todayDate = new Date();
                                String thisDate = currentDate.format(todayDate);

                                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                Date date = null;
                                date = inputFormat.parse(response.body().getFechaApertura());
                                String fechaApertura = currentDate.format(date);

                                if (!fechaApertura.equals(thisDate))
                                    if (todayDate.after(date)) {
                                        String msjbase = getResources().getString(R.string.apertura_caja_fecha_no);
                                        alertMSJ = String.format(msjbase, fechaApertura,caja,thisDate);
                                        mensajeSimpleDialog(getResources().getString(R.string.alerta),alertMSJ);
                                    }
                            } catch (ParseException e) {
                                e.printStackTrace();
                                pPB();
                            }

                            //Consultar la Api de Consecutivo Fiscal
                            Call<ResponseConsecutivoFiscal> call2 = apiService.doConsecutivoFiscal(usuario,caja);
                            call2.enqueue(new Callback<ResponseConsecutivoFiscal>() {
                                @Override
                                public void onResponse(Call<ResponseConsecutivoFiscal> call, Response<ResponseConsecutivoFiscal> response) {

                                    if (response.isSuccessful()) {
                                        assert response.body() != null;
                                        if(response.body().getEsValida()){
                                            for (String s : response.body().getMensaje()) {
                                                mensajeSimpleDialog(getResources().getString(R.string.alerta),s);
                                            }
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

                                            if(cajaActualizada){
                                                btnconsultar.setEnabled(true);
                                                btngenerico.setEnabled(true);
                                                etclienteID.setEnabled(true);
                                            }else{
                                                mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.caja_desactualizada));
                                            }

                                        }else{
                                            for (String s : response.body().getMensaje()) {
                                                mensajeSimpleDialog(getResources().getString(R.string.error),s);
                                            }
                                            btnconsultaF.setVisibility(View.VISIBLE);
                                        }
                                    } else {
                                        msjToast(getResources().getString(R.string.error_conexion_sb));
                                        btnconsultaF.setVisibility(View.VISIBLE);
                                    }
                                    pPB();
                                }

                                @Override
                                public void onFailure(Call<ResponseConsecutivoFiscal> call, Throwable t) {
                                    Log.e("LOGCAT", "ErrorResponseConsecutivoFiscal: "+call + t);
                                    pPB();
                                    btnconsultaF.setVisibility(View.VISIBLE);
                                    msjToast(getResources().getString(R.string.error_conexion) + t.getMessage());
                                }
                            });

                        }else{
                            String msjbase = getResources().getString(R.string.caja_n_cerrada);
                            String msjbaseFormateada = String.format(msjbase, caja);
                            mensajeSimpleDialog(getResources().getString(R.string.error),msjbaseFormateada);
                            pPB();
                            btnconsultaF.setVisibility(View.VISIBLE);
                        }
                    }else{
                        msjToast(response.body().getMensaje());
                        pPB();
                        btnconsultaF.setVisibility(View.VISIBLE);
                    }
                } else {
                    msjToast(getResources().getString(R.string.error_conexion_sb));
                    pPB();
                    btnconsultaF.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ResponseValidarAperturaCaja> call, Throwable t) {
                Log.e("LOGCAT", "ResponseValidarAperturaCaja: "+call + t);
                pPB();
                btnconsultaF.setVisibility(View.VISIBLE);
                msjToast(getResources().getString(R.string.error_conexion) + t.getMessage());
            }
        });
    }

    private void actualizarCaja() {
        iPB();
        btnconsultar.setEnabled(false);
        btngenerico.setEnabled(false);
        etclienteID.setEnabled(false);
        cajaActualizada = false;
        //Consultar la API de Cajas
        Call<ResponseCajas> call = apiService.doCajas(usuario);
        call.enqueue(new Callback<ResponseCajas>() {
            @Override
            public void onResponse(Call<ResponseCajas> call, Response<ResponseCajas> response) {
                //for para elegir la caja actual
                assert response.body() != null;
                for (Caja str : response.body().getCajas()) {
                    if (tienda.equals(str.getCodigoTienda()) && caja.equals(str.getCodigoCaja())){
                        SPM.setString(Constantes.DIVISA, str.getDivisa());
                        SPM.setString(Constantes.MEDIOS_PAGO, str.getMediosPagoAutorizados());
                        SPM.setString(Constantes.PAIS_CODE, str.getPais());
                        SPM.setString(Constantes.NOMBRE_TIENDA, str.getNombreTienda());
                        SPM.setString(Constantes.IDENTIFICADOR_CAJA, str.getIdentificadorCaja());
                        SPM.setString(Constantes.PREFIJO_CAJA, str.getPrefijoCaja());
                        SPM.setString(Constantes.CAJA_MOVIL, str.getCajaMovil());
                        break;
                    }
                }

                //Consultar la Api de parametros
                RequestParametros requestParametros = new RequestParametros("bazar",caja,tienda);
                Call<ResponseParametros> callRPC = apiService.doParametrosCaja(usuario,requestParametros);
                callRPC.enqueue(new Callback<ResponseParametros>() {
                    @Override
                    public void onResponse(Call<ResponseParametros> call, Response<ResponseParametros> response) {

                        if(response.isSuccessful()){
                            assert response.body() != null;
                            if(!response.body().getError()){
                                String fechaHora = response.body().getFechaHora();
                                if(fechaValida(fechaHora,5)){
                                    boolean errorPametro = false;
                                    String nombreParametros = "";
                                    //Guardar variable de sesion de Textos Fijos para la factura, TEF, tirillas y usuarios

                                    if (response.body().getMediosPagosCreditos() != null) {
                                        SPM.setString(Constantes.MEDIOS_PAGOS_CREDITOS, response.body().getMediosPagosCreditos());
                                    } else {
                                        errorPametro = true;
                                        nombreParametros = nombreParametros + "ZPOSM:MEDIOSPAGO:CREDITOS,";
                                    }

                                    if (response.body().getEmpresaTemporales() != null) {
                                        SPM.setString(Constantes.EMPRESA_TEMPORALES, response.body().getEmpresaTemporales());
                                    } else {
                                        errorPametro = true;
                                        nombreParametros = nombreParametros + "WCUPO:TEMPORALES,";
                                    }

                                    if (response.body().getDatafonosCredibanCo() != null) {
                                        SPM.setString(Constantes.DATAFONOS_CREDIBANCO, response.body().getDatafonosCredibanCo());
                                    } else {
                                        errorPametro = true;
                                        nombreParametros = nombreParametros + "ZPOSM:BAZAR:DATAFONOS:CREDIBAN,";
                                    }

                                    if (response.body().getDatafonosRedeban() != null) {
                                        SPM.setString(Constantes.DATAFONOS_REDEBAN, response.body().getDatafonosRedeban());
                                    } else {
                                        errorPametro = true;
                                        nombreParametros = nombreParametros + "ZPOSM:DATAFONOS:REDEBAN,";
                                    }

                                    if(response.body().getAmbienteFacturaElectronica() != null){
                                        String ambiente = response.body().getAmbienteFacturaElectronica();

                                        if(ambiente.equalsIgnoreCase("productivo")){
                                            SPM.setString(Constantes.AMBIENTE_FE_QR, "1");
                                        }else{
                                            SPM.setString(Constantes.AMBIENTE_FE_QR, "2");
                                        }
                                    }else{
                                        errorPametro = true;
                                        nombreParametros = nombreParametros + "FACT:ELECTRO:AMBIENTE,";
                                    }

                                    if(response.body().getProveedorFacturaElectronica() != null){
                                        SPM.setString(Constantes.PROVEEDOR_TECNOLOGICO_FE, response.body().getProveedorFacturaElectronica());
                                    }else{
                                        errorPametro = true;
                                        nombreParametros = nombreParametros + "ZPOSM:FE:PROVEEDORTEC,";
                                    }

                                    if(response.body().getUrlBaseFacturaElectronicaQR() != null){
                                        SPM.setString(Constantes.URL_BASE_FE_QR, response.body().getUrlBaseFacturaElectronicaQR());
                                    }else{
                                        errorPametro = true;
                                        nombreParametros = nombreParametros + "ZPOSM:FE:URLBASE:QR,";
                                    }

                                    if(response.body().getMedioPagoQrBancolombia() != null){
                                        SPM.setString(Constantes.MEDIO_PAGO_QRBANCOLOMBIA, response.body().getMedioPagoQrBancolombia());
                                    }else{
                                        errorPametro = true;
                                        nombreParametros = nombreParametros + "ZPOSM:MEDIOPAGO:QRBANCOLOMBIA,";
                                    }

                                    if(response.body().getContribuyentesText() != null){
                                        SPM.setString(Constantes.TEXTOS_CONTRIBUYENTES, response.body().getContribuyentesText());
                                    }else{
                                        errorPametro = true;
                                        nombreParametros = nombreParametros + "ZPOSM:TIQUETE:CONTRIBUYENTE,";
                                    }
                                    if(response.body().getPlazosText() != null){
                                        SPM.setString(Constantes.TEXTOS_PLAZOS, response.body().getPlazosText());
                                    }else{
                                        errorPametro = true;
                                        nombreParametros = nombreParametros + "ZPOSM:TIQUETE:PLAZOCAMBIO,";
                                    }
                                    if(response.body().getPoliticasCambiosText() != null){
                                        SPM.setString(Constantes.TEXTOS_POLITICAS_CAMBIOS, response.body().getPoliticasCambiosText());
                                    }else{
                                        errorPametro = true;
                                        nombreParametros = nombreParametros + "ZPOSM:TIQUETE:POLITICASCAMBIO,";
                                    }
                                    if(response.body().getLineasAtencionText() != null){
                                        SPM.setString(Constantes.TEXTOS_LINEAS_ATENCION, response.body().getLineasAtencionText());
                                    }else{
                                        errorPametro = true;
                                        nombreParametros = nombreParametros + "ZPOSM:TIQUETE:LINEAATENCION,";
                                    }
                                    if(response.body().getPagareText() != null){
                                        SPM.setString(Constantes.TEXTOS_PAGARE_TEF, response.body().getPagareText());
                                    }else{
                                        errorPametro = true;
                                        nombreParametros = nombreParametros + "ZPOSM:TEF:CREDIBANCOPAGARE,";
                                    }
                                    if(response.body().getTarifaIVAtext() != null){
                                        SPM.setString(Constantes.TARIFA_IVA, response.body().getTarifaIVAtext());
                                    }else{
                                        errorPametro = true;
                                        nombreParametros = nombreParametros + "ZPOSM:TIQUETE:TEXTOIVA,";
                                    }

                                    if(response.body().getUsuarioCajero() != null){
                                        SPM.setString(Constantes.USUARIO_CAJERO, response.body().getUsuarioCajero());
                                    }else{
                                        errorPametro = true;
                                        nombreParametros = nombreParametros + "ZPOSM:GRUPOSROL:CAJERO,";
                                    }
                                    if(response.body().getUsuarioAdministrador() != null){
                                        SPM.setString(Constantes.USUARIO_ADMINISTRADOR, response.body().getUsuarioAdministrador());
                                    }else{
                                        errorPametro = true;
                                        nombreParametros = nombreParametros + "ZPOSM:GRUPOSROL:ADMINISTRADOR,";
                                    }
                                    if(response.body().getUsuarioConfigurador() != null){
                                        SPM.setString(Constantes.USUARIO_CONFIGURADOR, response.body().getUsuarioConfigurador());
                                    }else{
                                        errorPametro = true;
                                        nombreParametros = nombreParametros + "ZPOSM:GRUPOSROL:CONFIGURADOR,";
                                    }
                                    if(response.body().getClienteGenerico() != null){
                                        SPM.setString(Constantes.CLIENTE_GENERICO, response.body().getClienteGenerico());
                                    }else{
                                        errorPametro = true;
                                        nombreParametros = nombreParametros + "ZPOSM:CLIENTE:GENERICO,";
                                    }
                                    if(response.body().getPoliticaDatosText() != null){
                                        SPM.setString(Constantes.TIRILLA_POLITICA_DATOS, cleanString(response.body().getPoliticaDatosText()));
                                    }else{
                                        errorPametro = true;
                                        nombreParametros = nombreParametros + "WCONSENT:PoliticaDatos,";
                                    }
                                    if(response.body().getMedioPagoEfectivoCaja() != null){
                                        SPM.setString(Constantes.MEDIO_PAGO_EFECTIVO_CAJA, response.body().getMedioPagoEfectivoCaja());
                                    }else{
                                        errorPametro = true;
                                        nombreParametros = nombreParametros + "ZPOSM:MEDIOPAGOEFECTIVO:CAJA,";
                                    }
                                    if(response.body().getMedioPagoTefContinguencia() != null){
                                        SPM.setString(Constantes.MEDIO_PAGO_TEF_CONTIGUENCIA, response.body().getMedioPagoTefContinguencia());
                                    }else{
                                        errorPametro = true;
                                        nombreParametros = nombreParametros + "ZPOSM:MEDIOPAGOTEFCONTINGENCIA,";
                                    }
                                    if(response.body().getEanesBazar() != null){
                                        SPM.setString(Constantes.EANES_BAZAR, response.body().getEanesBazar());
                                    }else{
                                        errorPametro = true;
                                        nombreParametros = nombreParametros + "ZPOSM:BAZAR:EANES,";
                                    }
                                    if(response.body().getCodigoAutDevo() != null){
                                        SPM.setString(Constantes.AUT_DEVOLUCION, response.body().getCodigoAutDevo());
                                    }else{
                                        SPM.setString(Constantes.AUT_DEVOLUCION, "426490");
                                    }

                                    if(errorPametro) {
                                        String msjbase = getResources().getString(R.string.error_act_caja_msj);
                                        String msjbaseFormateada = String.format(msjbase, nombreParametros);
                                        msjbaseFormateada = msjbaseFormateada.substring(0, msjbaseFormateada.length() - 1);
                                        mensajeSimpleDialog(getResources().getString(R.string.error), msjbaseFormateada);
                                        pPB();
                                    }else{
                                        btnconsultar.setEnabled(true);
                                        btngenerico.setEnabled(true);
                                        etclienteID.setEnabled(true);
                                        cajaActualizada = true;

                                        //Cargar los productos
                                        String eanposicion = response.body().getEanesBazar();
                                        if(eanposicion.length() > 0){
                                            List<String> listPosEan = Arrays.asList(eanposicion.split(";"));

                                            List<String> eanes = new ArrayList<>();
                                            if(listPosEan.size() > 0){
                                                for (int i = 0; i < listPosEan.size(); i++) {
                                                    String ep = listPosEan.get(i);
                                                    List<String> epList = Arrays.asList(ep.split("\\|"));
                                                    eanes.add(epList.get(1));
                                                }
                                                if(eanes.size() > 0) {

                                                    RequestEanes rEanes = new RequestEanes(eanes, pais, tienda);
                                                    Call<ResponseEanes> callEanes = apiService.doEanes(usuario,rEanes);
                                                    callEanes.enqueue(new Callback<ResponseEanes>() {
                                                        @Override
                                                        public void onResponse(Call<ResponseEanes> callEanes, Response<ResponseEanes> response) {

                                                            if(response.isSuccessful()) {
                                                                assert response.body() != null;
                                                                if (response.body().getError()) {
                                                                    msjToast(response.body().getMensaje());
                                                                    pPB();
                                                                }else{
                                                                    if(response.body().getProductos().size() > 0){
                                                                        if(response.body().getProductos().size() == eanes.size()){
                                                                            try {
                                                                                new ProductoDeleteAsyncTask().execute().get();
                                                                                new MediosPagoCajaDeleteAsyncTask().execute().get();
                                                                                new TarjetaBDeleteAsyncTask().execute().get();
                                                                                insertarProductos(response.body().getProductos(),eanes);
                                                                                mediosPagoCaja();
                                                                                tarjetasBancarias();
                                                                            } catch (ExecutionException | InterruptedException e) {
                                                                                msjToast("Error al realiza la actualización de la base de datos: "+e.getMessage());
                                                                            }
                                                                        }else {
                                                                            mensajeSimpleDialog(getResources().getString(R.string.error), getResources().getString(R.string.eanes_invalidos));
                                                                            pPB();
                                                                        }
                                                                    }else {
                                                                        mensajeSimpleDialog(getResources().getString(R.string.error), getResources().getString(R.string.eanes_invalidos));
                                                                        pPB();
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<ResponseEanes> callEanes, Throwable t) {
                                                            Log.e("LOGCAT", "ResponseParametros: "+call + t);
                                                            pPB();
                                                            msjToast(getResources().getString(R.string.error_conexion) + t.getMessage());
                                                        }
                                                    });
                                                }else {
                                                    mensajeSimpleDialog(getResources().getString(R.string.error), getResources().getString(R.string.eanes_invalidos));
                                                    pPB();
                                                }
                                            }else {
                                                mensajeSimpleDialog(getResources().getString(R.string.error), getResources().getString(R.string.eanes_invalidos));
                                                pPB();
                                            }
                                        }else {
                                            mensajeSimpleDialog(getResources().getString(R.string.error), getResources().getString(R.string.eanes_invalidos));
                                            pPB();
                                        }
                                    }
                                }else{
                                    mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_fecha));
                                    pPB();
                                }
                            }else{
                                mensajeSimpleDialog(getResources().getString(R.string.error_act_caja),response.body().getMensaje());
                                pPB();
                            }
                        }else{
                            msjToast(getResources().getString(R.string.error_conexion_sb));
                            pPB();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseParametros> call, Throwable t) {
                        Log.e("LOGCAT", "ResponseParametros: "+call + t);
                        pPB();
                        msjToast(getResources().getString(R.string.error_conexion) + t.getMessage());
                    }
                });
            }

            @Override
            public void onFailure(Call<ResponseCajas> call, Throwable t) {
                Log.e("LOGCAT", "Error ResponseCajas: " + call + t);
                pPB();
                msjToast(getResources().getString(R.string.error_conexion) + t.getMessage());
            }
        });
    }

    private void insertarProductos(List<Producto> productos, List<String> eanes) {

        for (int i = 0; i < productos.size(); i++) {
            for (int x = 0; x < eanes.size(); x++) {
                if(eanes.get(x).equals(productos.get(i).getEan())){
                    Producto p = productos.get(i);
                    ProductoEntity pE = new ProductoEntity(p.getEan(),p.getPrecio(),p.getNombre(),p.getTalla(),p.getColor(),
                            p.getTipoTarifa(),p.getTienda(),p.getPeriodoTarifa(),p.getIp(),p.getComposicion(),p.getPrecioUnitario(),
                            p.getArticulo(),p.getCodigoTasaImpuesto(),p.getArticuloCerrado(),p.getArticuloGratuito(),p.getTasaImpuesto(),
                            p.getPrecioSinImpuesto(),p.getImpuesto(),p.getValorTasa(),p.getFechaTasa(),p.getPrecioOriginal(),
                            p.getPeriodoActivo(),p.getCodigoMarca(),p.getMarca(),p.getSerialNumberId(),p.getVendedorId(),
                            1,p.getDescontable(),p.getTipoPrendaCodigo(),p.getTipoPrendaNombre(),p.getGeneroCodigo(),
                            p.getGeneroNombre(),p.getCategoriaIvaCodigo(),p.getCategoriaIvaNombre(),x+1);
                    new ProductoCrearAsyncTask().execute(pE);
                    break;
                }
            }
        }
        //new ProductoTodosAsyncTask().execute();
    }

    private class ProductoCrearAsyncTask extends AsyncTask<ProductoEntity,Void,Void>{

        @Override
        protected Void doInBackground(ProductoEntity... productoEntities) {
            try{
                BazarPosMovilDB.getBD(getApplication()).productoDao().insert(productoEntities[0]);
            }catch (Exception ex){
                msjToast("Error al crear el producto: "+productoEntities[0].getEan()+ " "+ex.getMessage());
            }
            return null;
        }
    }

    private class ProductoDeleteAsyncTask extends AsyncTask<Producto,Void,Void> {

        @Override
        protected Void doInBackground(Producto... productos) {
            try{
                BazarPosMovilDB.getBD(getApplication()).productoDao().delete();
            }catch (Exception ex){
                msjToast("Error al eliminar los productos: "+ex.getMessage());
            }
            return null;
        }
    }

    protected class ProductoTodosAsyncTask extends AsyncTask<Void,Void, List<ProductoEntity>>{

        @Override
        protected List<ProductoEntity> doInBackground(Void... voids) {
            return BazarPosMovilDB.getBD(getApplication()).productoDao().getAll();
        }

        @Override
        protected void onPostExecute(List<ProductoEntity> productos) {
            super.onPostExecute(productos);
            Log.e("LOGCAT","ProductoTodos size: "+productos.size());
            for(ProductoEntity p:productos){
                Log.e("LOGCAT","Line: "+p.getLine()+" - EAN: "+p.getEan()+" - Precio: "+p.getPrecio());
            }
        }
    }

    //Cargar los medios de pago en la caja
    private void mediosPagoCaja() {

        Call<ResponseMediosCaja> call = apiService.doMediosCaja(usuario,caja);
        call.enqueue(new Callback<ResponseMediosCaja>() {
            @Override
            public void onResponse(Call<ResponseMediosCaja> call, Response<ResponseMediosCaja> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getMediosCaja().size() > 0) {
                        List<MediosCaja> listmediospagoscaja = response.body().getMediosCaja();
                        for (int i = 0; i < listmediospagoscaja.size(); i++) {
                            MediosCaja mpc = listmediospagoscaja.get(i);
                            MediosPagoCajaEntity mpcE = new MediosPagoCajaEntity(mpc.getCaja(),mpc.getCodigo()
                                    ,mpc.getDivisa(),mpc.getNombre(),mpc.getPais(),mpc.getTipo(),mpc.getTpe(),mpc.getTpeValue());
                            new MediosPagoCajaCrearAsyncTask().execute(mpcE);
                        }
                    } else {
                        msjToast(getResources().getString(R.string.no_medios_pago_caja));
                    }
                } else {
                    msjToast(getResources().getString(R.string.error_conexion_sb));
                }
                //new MediosPagoCajaTodosAsyncTask().execute();
            }

            @Override
            public void onFailure(Call<ResponseMediosCaja> call, Throwable t) {
                LogFile.adjuntarLog("Error ResponseMediosCaja: " + call + t);
                pPB();
                msjToast(getResources().getString(R.string.error_conexion) + t.getMessage());
            }
        });
    }

    private class MediosPagoCajaCrearAsyncTask extends AsyncTask<MediosPagoCajaEntity,Void,Void>{

        @Override
        protected Void doInBackground(MediosPagoCajaEntity... mediosPagoCajaEntities) {
            try{
                BazarPosMovilDB.getBD(getApplication()).mediosPagoCajaDao().insert(mediosPagoCajaEntities[0]);
            }catch (Exception ex){
                msjToast("Error al crear el producto: "+mediosPagoCajaEntities[0].getNombre()+ " "+ex.getMessage());
            }
            return null;
        }
    }

    private class MediosPagoCajaDeleteAsyncTask extends AsyncTask<MediosPagoCajaEntity,Void,Void> {

        @Override
        protected Void doInBackground(MediosPagoCajaEntity... mediosPagoCaja) {
            try{
                BazarPosMovilDB.getBD(getApplication()).mediosPagoCajaDao().delete();
            }catch (Exception ex){
                msjToast("Error al eliminar los medios de pagos: "+ex.getMessage());
            }
            return null;
        }
    }

    protected class MediosPagoCajaTodosAsyncTask extends AsyncTask<Void,Void, List<MediosPagoCajaEntity>>{

        @Override
        protected List<MediosPagoCajaEntity> doInBackground(Void... voids) {
            return BazarPosMovilDB.getBD(getApplication()).mediosPagoCajaDao().getAll();
        }

        @Override
        protected void onPostExecute(List<MediosPagoCajaEntity> mediosPagoCajaList) {
            super.onPostExecute(mediosPagoCajaList);
            Log.e("LOGCAT","ediosPagoTodos size: "+mediosPagoCajaList.size());
            for(MediosPagoCajaEntity mp:mediosPagoCajaList){
                Log.e("LOGCAT",mp.getCodigo()+" - "+mp.getNombre());
            }
        }
    }

    //Cargar las tarjetas bancarias para pago con tef contingencia
    private void tarjetasBancarias() {

        //Consultar la Api de tarjeta bancarias
        Call<ResponseTarjetasBancarias> call = apiService.doTarjetasBancarias(usuario,SPM.getString(Constantes.PAIS_CODE));
        call.enqueue(new Callback<ResponseTarjetasBancarias>() {
            @Override
            public void onResponse(Call<ResponseTarjetasBancarias> call, Response<ResponseTarjetasBancarias> response) {
                if(response.isSuccessful()){
                    assert response.body() != null;
                    List<Tarjeta> listtarjeta = response.body().getTarjetas();
                    for (int i = 0; i < listtarjeta.size(); i++) {
                        Tarjeta t = listtarjeta.get(i);
                        TarjetaEntity te = new TarjetaEntity(t.getCodigo(),t.getNombre(),t.getPais(),t.getTipo());
                        new TarjetaBCrearAsyncTask().execute(te);
                    }
                }else{
                    msjToast(getResources().getString(R.string.error_conexion_sb));
                }
               //new TarjetaBTodosAsyncTask().execute();
                consultaFiscal_Caja();
                msjToast(getResources().getString(R.string.caja_actualizada));
            }

            @Override
            public void onFailure(Call<ResponseTarjetasBancarias> call, Throwable t) {
                Log.e("LOGCAT", "Error ResponseTarjetasBancarias: " + call + t);
                pPB();
                msjToast(getResources().getString(R.string.error_conexion) + t.getMessage());
            }
        });
    }

    private class TarjetaBDeleteAsyncTask extends AsyncTask<TarjetaEntity,Void,Void> {

        @Override
        protected Void doInBackground(TarjetaEntity... tarjetaEntity) {
            try{
                BazarPosMovilDB.getBD(getApplication()).tarjetaDao().delete();
            }catch (Exception ex){
                msjToast("Error al eliminar las tarjetas bancarias: "+ex.getMessage());
            }
            return null;
        }
    }

    private class TarjetaBCrearAsyncTask extends AsyncTask<TarjetaEntity,Void,Void>{

        @Override
        protected Void doInBackground(TarjetaEntity... tarjetaEntity) {
            try{
                BazarPosMovilDB.getBD(getApplication()).tarjetaDao().insert(tarjetaEntity[0]);
            }catch (Exception ex){
                msjToast("Error al crear el producto: "+tarjetaEntity[0].getNombre()+ " "+ex.getMessage());
            }
            return null;
        }
    }

    protected class TarjetaBTodosAsyncTask extends AsyncTask<Void,Void, List<TarjetaEntity>>{

        @Override
        protected List<TarjetaEntity> doInBackground(Void... voids) {
            return BazarPosMovilDB.getBD(getApplication()).tarjetaDao().getAll();
        }

        @Override
        protected void onPostExecute(List<TarjetaEntity> tarjetaList) {
            super.onPostExecute(tarjetaList);
            Log.e("LOGCAT","TarjetaTodos size: "+tarjetaList.size());
            for(TarjetaEntity t:tarjetaList){
                Log.e("LOGCAT",t.getCodigo()+" - "+t.getNombre());
            }
        }
    }

    //Validar que la fecha del servidor y la fecha del dispositivo esten en un rango en minutos
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
        Toast.makeText(ClienteConsultaActivity.this, msj, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed(){
        apagar();
    }

    //Limpiar string de todos los acentos y caracteres especiales
    public static String cleanString(String texto) {
        texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
        texto = texto.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return texto;
    }

    @Override
    public void sendInputListClienteSelectDialogFragment(Boolean esNuevo, Cliente cliente) {
        cargarCliente(cliente);
    }

    @Override
    public void cerrarFragmentCarga() {
        if(!estadoTransacion.isEmpty()){
            switch (estadoTransacion){
                case Constantes.ESTADO_TRASACCION_PENDIENTE:
                    Utilidades.sweetAlertCustom(getResources().getString(R.string.trasacion_pendiente),
                            getResources().getString(R.string.msj_alerta_cancelar_tef_anulacion), contexto, R.drawable.advertencia);
                    break;
                case Constantes.ESTADO_TRASACCION_CREADA:
                    util.ocultarPantallaCargaCustom();
                    borrarAnulacionN6();
                    break;
            }
        }
    }
}
