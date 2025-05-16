package com.crystal.bazarposmobile.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.common.Constantes;
import com.crystal.bazarposmobile.common.DatafonoTotales;
import com.crystal.bazarposmobile.common.IConfigurableCarga;
import com.crystal.bazarposmobile.common.LogFile;
import com.crystal.bazarposmobile.common.NetworkUtil;
import com.crystal.bazarposmobile.common.PeticionN6;
import com.crystal.bazarposmobile.common.SPM;
import com.crystal.bazarposmobile.common.TipoDatafono;
import com.crystal.bazarposmobile.common.Utilidades;
import com.crystal.bazarposmobile.db.entity.DatafonoEntity;
import com.crystal.bazarposmobile.retrofit.request.RequestInsertarPerifericos;
import com.crystal.bazarposmobile.retrofit.request.creardocumento.Payment;
import com.crystal.bazarposmobile.retrofit.response.consecutivofiscal.ResponseConsecutivoFiscal;
import com.crystal.bazarposmobile.retrofit.response.datafonon6.ResponseBorrarCompraN6;
import com.crystal.bazarposmobile.retrofit.response.datafonon6.ResponseCompraN6;
import com.crystal.bazarposmobile.retrofit.response.datafonon6.ResponseConsultarCompraN6;
import com.crystal.bazarposmobile.retrofit.response.datafonon6.RespuestaConsultarCompraN6;
import com.crystal.bazarposmobile.retrofit.response.datafonon6.RespuestaTransaccion;
import com.crystal.bazarposmobile.retrofit.response.mediospagocaja.MediosCaja;
import com.crystal.bazarposmobile.retrofit.response.perifericos.ResponseGuardarPerifericos;
import com.crystal.bazarposmobile.ui.dialogfragmen.MostrarInfoDialogFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.ingenico.pclutilities.PclUtilities;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DatafonoN6Activity extends AppCompatActivity implements View.OnClickListener,
        IConfigurableCarga {
    //Declaración de los objetos de la interfaz del activity
    private RadioGroup rgterminal;
    private String estadoTransacion = "";
    private boolean testComunicacion;
    private long tiempoEsperaCierreDetafono;
    private CountDownTimer countDownTimer;
    private EditText etmonto,etnumerorecibo;
    private TextView tvestado, tvduracion, tvresultadoterminal, tvtitulo,tvdeuda,tvtotal,tvresultado,tvestadoconexion,
            tvSelecioneTerminalDatafonoA;
    private ProgressBar pb;
    private Button btnaccion,btndeuda,btnfinalizarcierre,btnimprtotales,bntimprdetallado,btnimprimir;
    private ImageView ivreiniciar;
    private int tiempoEsperaDatafono;
    ScrollView svResultadosDatafonoA, svTerminalesDatafonoA;

    //Declaración de la variables del activity
    private static final String TAG = "PCLTESTAPP";
    private PclUtilities mPclUtil;
    private boolean mServiceStarted, sslActivated = false, mPermissionRequested = false, mPermissionGranted = false, mRestart;
    boolean esIniciarCierre = true;
    private int terminalCounter = 0;
    private Set<PclUtilities.IpTerminal> terminals = new HashSet<PclUtilities.IpTerminal>();
    private boolean transaccionFinalizada;
    Utilidades util;
    private long mTestStartTime;
    private long mTestEndTime;
    CharSequence mCurrentDevice;
    UsbManager mUsbManager = null;
    PendingIntent mPermissionIntent = null;
    private static final String ACTION_USB_PERMISSION = "com.crystal.bazarposmobile.USB_PERMISSION";
    CheckBox mcbActivateSsl;

    MediosCaja mediopago;
    DatafonoEntity datafonoEntity;
    String titulo;
    ConstraintLayout mainLayout;
    LayoutInflater inflater;
    private Timer tiempo;
    private TimerTask tarea;
    private View rootView;
    String bin42,codAprobaciondfff2d,codRtadfff02,dirEstabledffe78,fechaVenTarjdffe76,
            franquiciadffe49,idClientedffe02,cuotasdfff26,numRecibodfff29,aid4f,
            codTerminaldffe45,rrndfff14, tipoCuentadffe50,ultDigitoTarjdffe54,
            codigoComerciodffe77,fecha9a,hora9f21,hashdffe01,nombreTiendadfff32,
            criptograma9f26, caja, cajero_code,tipoOperacion, labelUlt,
            finCierreDFFE0D,idDC;
    Long tvr95,tsi9b;
    Double iacdfff23,propinadfff21,montodffe40,ivadfff22;
    DecimalFormat formatea = new DecimalFormat(Constantes.FORMATO_DECIMAL);
    Double iva,totalCompra,deuda;
    List<DatafonoEntity> datafonoDetalladoList = new ArrayList<>();
    List<DatafonoTotales> datafonoTotalesList = new ArrayList<>();
    List<DatafonoTotales> datafonoTotalesCanList = new ArrayList<>();
    boolean enproceso = false;
    TipoDatafono tipoDatafono;
    Context contexto;
    ConstraintSet constraintSet;
    ConstraintLayout clDatafonoA;
    private IConfigurableCarga configurableCarga;
    TextInputLayout tliUsuarioDatafono, tliCodigoUnicoDatafono, tliCodigoDatafono, tliPassDatafono;
    EditText etUsuarioDatafono, etPassDatafono, etCodigoDatafono, etCodigoUnicoDatafono;
    private TextView tvErrorConexionN6;
    private Button btnComprobarPagoN6;
    private PeticionN6 factura;

    @SuppressLint("SetTextI18n")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datafono_n6);
        Objects.requireNonNull(getSupportActionBar()).hide();

        //Ocultar el teclado de pantalla
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Recibir el tipo de operacion
        tipoDatafono = (TipoDatafono) getIntent().getSerializableExtra("tipoDatafono");
        tipoOperacion = getIntent().getExtras().getString("tipoOperacion");

        SPM.setObject(Constantes.OBJECT_TIPO_DATAFONO, tipoDatafono);

        //Variables generales
        caja = SPM.getString(Constantes.CAJA_CODE);
        cajero_code = SPM.getString(Constantes.CAJERO_CODE);
        nombreTiendadfff32 = SPM.getString(Constantes.NOMBRE_TIENDA);

        findViews();
        tipoOperacion();
        eventos();

        ivreiniciar.setVisibility(View.INVISIBLE);

        if(isRedeban()){
            llenarDatosRedeban();
        }else{
            llenarDatosCredibanco();
        }
    }

    @SuppressLint("SetTextI18n")
    private void tipoOperacion() {
        //Tipos de operaciones en el datafono
        switch (tipoOperacion) {
            case "compra":
                if(tipoDatafono.getNombreDispositivo().equals(Constantes.DATAFONO_SMARTPOS_N6)){
                    tvSelecioneTerminalDatafonoA.setText(String.format(getResources().getString(R.string.titulo_compra_en_datafono_n6)
                            , tipoDatafono.getNombreDispositivo()));
                }

                //Definicion de elementos para compras
                tvdeuda = findViewById(R.id.tvDeudaDatafonoA);
                tvtotal = findViewById(R.id.tvTotalDatafonoA);
                etmonto = findViewById(R.id.etMontoDatafonoA);

                btnaccion = findViewById(R.id.btnPagoDatafonoA);
                btndeuda = findViewById(R.id.btnDeudaDatafonoA);

                //variables a utilizar para una compra desde el intent
                totalCompra = getIntent().getExtras().getDouble("total");
                deuda = getIntent().getExtras().getDouble("deuda");
                iva = getIntent().getExtras().getDouble("iva");
                mediopago = (MediosCaja)getIntent().getSerializableExtra("itemMedio");

                //Mostrando la deuda y el total
                tvdeuda.setText("Deuda: "+formatea.format(deuda));
                tvtotal.setText("Total: "+formatea.format(totalCompra));

                //Titulo del activity
                titulo = getResources().getString(R.string.pago)+" "+mediopago.getNombre();
                tvtitulo.setText(titulo);

                //Eventos
                btnaccion.setOnClickListener(this);
                btndeuda.setOnClickListener(this);
                break;
            case "cierre":
                //Titulo del activity
                tvtitulo.setText(getResources().getString(R.string.titulo_cierre_datafono));

                //Mostrando el layout para el cierre
                mainLayout = findViewById(R.id.includeDatafonoA);
                inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rootView = inflater.inflate(R.layout.datafono_cierre_content, mainLayout, false);
                mainLayout.removeAllViews();
                mainLayout.addView(rootView);

                //Definicion de elementos para compras
                btnaccion = mainLayout.findViewById(R.id.btnCierreDatafonoA);
                btnfinalizarcierre = mainLayout.findViewById(R.id.btnFinalizarCierreDatafonoA);
                btnimprtotales = mainLayout.findViewById(R.id.btnImprTotalesCierreDatafonoA);
                bntimprdetallado = mainLayout.findViewById(R.id.btnImprDetalladoCierreDatafonoA);

                //Eventos usados en el cierre de datafono
                btnaccion.setOnClickListener(this);
                btnfinalizarcierre.setOnClickListener(this);
                btnimprtotales.setOnClickListener(this);
                bntimprdetallado.setOnClickListener(this);

                //Desabilitar Bonones
                btnfinalizarcierre.setEnabled(false);
                btnimprtotales.setEnabled(false);
                bntimprdetallado.setEnabled(false);
                break;
            case "ultimaTransaccion":
                //Titulo del activity
                tvtitulo.setText(getResources().getString(R.string.titulo_utltrans_datafono));

                //Mostrando el layout para el cierre
                mainLayout = findViewById(R.id.includeDatafonoA);
                inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rootView = inflater.inflate(R.layout.datafono_ultimatransaccion_content, mainLayout, false);
                mainLayout.removeAllViews();
                mainLayout.addView(rootView);

                //Definicion de elementos para compras
                btnaccion = mainLayout.findViewById(R.id.btnConsultarUTDA);
                btnimprimir = mainLayout.findViewById(R.id.btnImpUTDA);

                //Eventos usados en el cierre de datafono
                btnaccion.setOnClickListener(this);
                btnimprimir.setOnClickListener(this);

                //Dasabilitar
                btnimprimir.setEnabled(false);
                break;
            case "anular":
                //Titulo del activity
                tvtitulo.setText(getResources().getString(R.string.titulo_anular_datafono));

                //Mostrando el layout para el cierre
                mainLayout = findViewById(R.id.includeDatafonoA);
                inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rootView = inflater.inflate(R.layout.datafono_anulacion_content, mainLayout, false);
                mainLayout.removeAllViews();
                mainLayout.addView(rootView);

                //Definicion de elementos para compras
                btnaccion = mainLayout.findViewById(R.id.btnAnularDatafonoA);
                btnimprimir = mainLayout.findViewById(R.id.btnImpresionAnularDatafonoA);
                etnumerorecibo = mainLayout.findViewById(R.id.etNumReciboDatafonoA);

                //Eventos usados en el cierre de datafono
                btnaccion.setOnClickListener(this);
                btnimprimir.setOnClickListener(this);

                //Dasabilitar
                btnimprimir.setEnabled(false);
                break;
            case "bonoRegalo":
                //Titulo del activity
                tvtitulo.setText(getResources().getString(R.string.titulo_bono_regalo_datafono));

                //Mostrando el layout para el cierre
                mainLayout = findViewById(R.id.includeDatafonoA);
                inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rootView = inflater.inflate(R.layout.datafono_bonoregalo_content, mainLayout, false);
                mainLayout.removeAllViews();
                mainLayout.addView(rootView);

                //Definicion de elementos para compras
                btnaccion = mainLayout.findViewById(R.id.btnRegaloDatafonoA);
                btnimprimir = mainLayout.findViewById(R.id.btnImprimirRegaloDatafonoA);
                etmonto = mainLayout.findViewById(R.id.etMontoRegaloDatafonoA);

                //Eventos usados en el cierre de datafono
                btnaccion.setOnClickListener(this);
                btnimprimir.setOnClickListener(this);

                //Dasabilitar
                btnimprimir.setEnabled(false);
                break;
            case "testComunicacion":
                if(tipoDatafono.getNombreDispositivo().equals(Constantes.DATAFONO_SMARTPOS_N6)){
                    tvSelecioneTerminalDatafonoA.setText(String.format(getResources().getString(R.string.titulo_configure_el_datafono)
                            , tipoDatafono.getNombreDispositivo()));
                    tvtitulo.setText(getResources().getString(R.string.titulo_test_comunicacion_datafono));
                    //Mostrando el layout para el cierre
                    mainLayout = findViewById(R.id.includeDatafonoA);
                    inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    rootView = inflater.inflate(R.layout.datafono_testcomunicacion_content, mainLayout, false);
                    mainLayout.removeAllViews();
                    mainLayout.addView(rootView);
                    //Definicion de elementos para compras
                    btnaccion = mainLayout.findViewById(R.id.btnTCDA);

                    mostrarConfiguracion();

                    constraintSet = new ConstraintSet();
                    constraintSet.clone(clDatafonoA);

                    constraintSet.connect(
                            mainLayout.getId(),
                            ConstraintSet.TOP,
                            tliCodigoUnicoDatafono.getId(),
                            ConstraintSet.BOTTOM
                    );

                    constraintSet.applyTo(clDatafonoA);

                    btnaccion.setOnClickListener(this);
                }else{
                    //Titulo del activity
                    tvtitulo.setText(getResources().getString(R.string.titulo_test_comunicacion_datafono));

                    //Mostrando el layout para el cierre
                    mainLayout = findViewById(R.id.includeDatafonoA);
                    inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    rootView = inflater.inflate(R.layout.datafono_testcomunicacion_content, mainLayout, false);
                    mainLayout.removeAllViews();
                    mainLayout.addView(rootView);

                    //Definicion de elementos para compras
                    btnaccion = mainLayout.findViewById(R.id.btnTCDA);

                    //Eventos usados en el cierre de datafono
                    btnaccion.setOnClickListener(this);
                    break;
                }
                break;
        }
    }

    private void iPB() {
        if(tipoDatafono.getNombreDispositivo().equals(Constantes.DATAFONO_SMARTPOS_N6)){
            if(util.estadoProgressCustom()){
                util.mostrarPantallaCargaCustom(getSupportFragmentManager(), "Cargando...",
                        false, null);
            }
        }else{
            pb.setVisibility(View.VISIBLE);
        }
    }

    private void pPB() {
        if(tipoDatafono.getNombreDispositivo().equals(Constantes.DATAFONO_SMARTPOS_N6)){
            util.ocultarPantallaCargaCustom();
        }else{
            pb.setVisibility(View.GONE);
        }
    }

    private void mostrarConfiguracion() {
        tliUsuarioDatafono.setVisibility(View.VISIBLE);
        tliPassDatafono.setVisibility(View.VISIBLE);
        tliCodigoDatafono.setVisibility(View.VISIBLE);
        tliCodigoUnicoDatafono.setVisibility(View.VISIBLE);
    }

    private boolean isRedeban() {
        SPM.setBoolean(Constantes.IS_REDEBAN,tipoDatafono.isRedeban());
        return tipoDatafono.isRedeban();
    }

    private void llenarDatosRedeban(){
        if(SPM.getString(Constantes.USUARIO_DATAFONO_RB) != null){
            etUsuarioDatafono.setText(SPM.getString(Constantes.USUARIO_DATAFONO_RB));
        }else{
            etUsuarioDatafono.setText("");
        }

        if(SPM.getString(Constantes.PASS_DATAFONO_RB) != null){
            etPassDatafono.setText(SPM.getString(Constantes.PASS_DATAFONO_RB));
        }else{
            etPassDatafono.setText("");
        }

        if(SPM.getString(Constantes.CODIGO_DATAFONO_RB) != null){
            etCodigoDatafono.setText(SPM.getString(Constantes.CODIGO_DATAFONO_RB));
        }else{
            etCodigoDatafono.setText("");
        }

        if(SPM.getString(Constantes.CODIGO_UNICO_DATAFONO_RB) != null){
            etCodigoUnicoDatafono.setText(SPM.getString(Constantes.CODIGO_UNICO_DATAFONO_RB));
        }else{
            etCodigoUnicoDatafono.setText("");
        }
    }

    private void llenarDatosCredibanco(){
        if(SPM.getString(Constantes.USUARIO_DATAFONO_CD) != null){
            etUsuarioDatafono.setText(SPM.getString(Constantes.USUARIO_DATAFONO_CD));
        }else{
            etUsuarioDatafono.setText("");
        }

        if(SPM.getString(Constantes.PASS_DATAFONO_CD) != null){
            etPassDatafono.setText(SPM.getString(Constantes.PASS_DATAFONO_CD));
        }else{
            etPassDatafono.setText("");
        }

        if(SPM.getString(Constantes.CODIGO_DATAFONO_CD) != null){
            etCodigoDatafono.setText(SPM.getString(Constantes.CODIGO_DATAFONO_CD));
        }else{
            etCodigoDatafono.setText("");
        }

        if(SPM.getString(Constantes.CODIGO_UNICO_DATAFONO_CD) != null){
            etCodigoUnicoDatafono.setText(SPM.getString(Constantes.CODIGO_UNICO_DATAFONO_CD));
        }else{
            etCodigoUnicoDatafono.setText("");
        }
    }

    //definicion de elementos generales
    private void findViews() {
        contexto = DatafonoN6Activity.this;
        util = new Utilidades();
        factura = new PeticionN6(SPM.getString(Constantes.CAJERO_CODE), SPM.getString(Constantes.CAJA_CODE),
                SPM.getString(Constantes.NOMBRE_TIENDA));
        configurableCarga = this;
        tvtitulo = findViewById(R.id.tvTituloDatafonoA);
        rootView = ((Activity) contexto).getWindow().getDecorView().findViewById(android.R.id.content);

        pb = findViewById(R.id.pbDatafonoA);
        pb.setVisibility(View.GONE);

        tvestado = findViewById(R.id.tvEstatusDatafonoA);
        tvduracion = findViewById(R.id.tvDuracionDatafonoA);
        tvresultadoterminal = findViewById(R.id.tvRespuestaDatafonoA);
        tvestadoconexion = findViewById(R.id.tvEstadoDatafono);

        rgterminal = findViewById(R.id.rgTerminalDatafonoA);
        ivreiniciar = findViewById(R.id.ivReiniciarDatafonoA);

        tvresultado = findViewById(R.id.tvResultadoDatafonoA);
        etmonto = findViewById(R.id.etMontoDatafonoA);
        tvSelecioneTerminalDatafonoA = findViewById(R.id.tvSelecioneTerminalDatafonoA);
        svResultadosDatafonoA = findViewById(R.id.svResultadosDatafonoA);
        svTerminalesDatafonoA = findViewById(R.id.svTerminalesDatafonoA);

        tliUsuarioDatafono = findViewById(R.id.tliUsuarioDatafono);
        tliPassDatafono = findViewById(R.id.tliPassDatafono);
        tliCodigoDatafono = findViewById(R.id.tliCodigoDatafono);
        tliCodigoUnicoDatafono = findViewById(R.id.tliCodigoUnicoDatafono);

        etUsuarioDatafono = findViewById(R.id.etUsuarioDatafono);
        etPassDatafono = findViewById(R.id.etPassDatafono);
        etCodigoDatafono = findViewById(R.id.etCodigoDatafono);
        etCodigoUnicoDatafono = findViewById(R.id.etCodigoUnicoDatafono);

        clDatafonoA = findViewById(R.id.clDatafonoA);
        tvErrorConexionN6 = findViewById(R.id.tvErrorConexionN6);
        btnComprobarPagoN6 = findViewById(R.id.btnComprobarPagoN6);
    }

    //eventos generales
    private void eventos() {
        ivreiniciar.setOnClickListener(this);
        btnComprobarPagoN6.setOnClickListener(this::comprobarPagoN6);

        etmonto.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if(btnaccion.isEnabled())
                    btnaccion.callOnClick();
            }
            return handled;
        });

        etUsuarioDatafono.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(isRedeban()){
                    SPM.setString(Constantes.USUARIO_DATAFONO_RB,etUsuarioDatafono.getText().toString());
                }else{
                    SPM.setString(Constantes.USUARIO_DATAFONO_CD,etUsuarioDatafono.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etPassDatafono.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(isRedeban()){
                    SPM.setString(Constantes.PASS_DATAFONO_RB,etPassDatafono.getText().toString());
                }else{
                    SPM.setString(Constantes.PASS_DATAFONO_CD,etPassDatafono.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etCodigoDatafono.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(isRedeban()){
                    SPM.setString(Constantes.CODIGO_DATAFONO_RB,etCodigoDatafono.getText().toString());
                }else{
                    SPM.setString(Constantes.CODIGO_DATAFONO_CD,etCodigoDatafono.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etCodigoUnicoDatafono.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(isRedeban()){
                    SPM.setString(Constantes.CODIGO_UNICO_DATAFONO_RB,etCodigoUnicoDatafono.getText().toString());
                }else{
                    SPM.setString(Constantes.CODIGO_UNICO_DATAFONO_CD,etCodigoUnicoDatafono.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @Override
    public void onClick(View v) {

        Intent i = Utilidades.activityImprimir(DatafonoN6Activity.this);

        switch (v.getId()) {
            //Importe igual a la deuda
            case R.id.btnDeudaDatafonoA:
                int d = deuda.intValue();
                etmonto.setText(Integer.toString(d));
                break;
            //Realizar pago TEF
            case R.id.btnPagoDatafonoA:
                if(SPM.getString(Constantes.ID_TRANSACCION_DATAFONO) != null){
                    if(validarMonto()){
                        comprobarRespuestaDatafono(false);
                    }
                }else{
                    realizarCompraN6(false);
                }
                break;
            case R.id.btnImprTotalesCierreDatafonoA:
                //Pasar a la impresión del cierre de totales
                i.putExtra("lablePrint", (Serializable) "TEFTotales");
                i.putExtra("listRDTotales", (Serializable) datafonoTotalesList);
                i.putExtra("listRDTotalesCanceladas", (Serializable) datafonoTotalesCanList);
                i.putExtra("codigoComercio", (Serializable) codigoComerciodffe77);
                i.putExtra("dirEstable", (Serializable) dirEstabledffe78);
                i.putExtra("codTerminal", (Serializable) codTerminaldffe45);
                startActivity(i);
                btnfinalizarcierre.setEnabled(true);
                break;

            case R.id.btnImprDetalladoCierreDatafonoA:
                //Pasar a la impresión del cierre detallado
                i.putExtra("lablePrint", (Serializable) "TEFDetallado");
                i.putExtra("listRDDetallado", (Serializable) datafonoDetalladoList);
                i.putExtra("codigoComercio", (Serializable) codigoComerciodffe77);
                i.putExtra("dirEstable", (Serializable) dirEstabledffe78);
                i.putExtra("codTerminal", (Serializable) codTerminaldffe45);
                startActivity(i);
                btnfinalizarcierre.setEnabled(true);
                break;
            //Imprimir Anulación
            case R.id.btnImpresionAnularDatafonoA:
                i.putExtra("lablePrint", (Serializable) "anulacion");
                i.putExtra("respDatafono", (Serializable) datafonoEntity);
                i.putExtra("primeraimpresion", false);
                startActivity(i);
                break;
            //Imprimir Bono Regalo
            case R.id.btnImprimirRegaloDatafonoA:
                i.putExtra("lablePrint", (Serializable) "bonoregalo");
                i.putExtra("respDatafono", (Serializable) datafonoEntity);
                i.putExtra("primeraimpresion", false);
                startActivity(i);
                break;
            //Ultima Trans
            case R.id.btnImpUTDA:
                if(!labelUlt.isEmpty()){
                    i.putExtra("lablePrint", (Serializable) labelUlt);
                    i.putExtra("respDatafono", (Serializable) datafonoEntity);
                    i.putExtra("primeraimpresion", false);
                    startActivity(i);
                    break;
                }else{
                    msjToast(getResources().getString(R.string.print_ultima_datafono));
                }
            case R.id.btnTCDA:
                if(SPM.getString(Constantes.ID_TRANSACCION_DATAFONO) != null){
                    comprobarRespuestaDatafono(true);
                }else{
                    pruebaDatafono(v);
                }
                break;
        }

    }

    @SuppressLint("StringFormatMatches")
    private void comprobarRespuestaDatafono(boolean isTestComunicacion) {
        util.mostrarPantallaCargaCustom(getSupportFragmentManager(), "Comprobando transacciones...", false,
                null);
        Call<ResponseConsultarCompraN6> call = Utilidades.servicioRetrofit().doConsultarCompraN6(SPM.getString(Constantes.USER_NAME)+" - "+getResources().getString(R.string.version_apk),
                Utilidades.peticionBaseN6());
        call.enqueue(new Callback<ResponseConsultarCompraN6>() {

            @Override
            public void onResponse(@NonNull Call<ResponseConsultarCompraN6> call, @NonNull Response<ResponseConsultarCompraN6> response) {
                if(response.isSuccessful()){
                    assert response.body() != null;
                    if(response.body().isEsValida()){
                        estadoTransacion = response.body().getRespuestaCompra().getEstadoDeTransaccion();
                        util.ocultarPantallaCargaCustom();
                        switch (estadoTransacion){
                            case Constantes.ESTADO_TRASACCION_COMPLETA:
                                if(isTestComunicacion){
                                    pruebaDatafono(null);
                                }else{
                                    realizarCompraN6(false);
                                }
                                break;
                            case Constantes.ESTADO_TRASACCION_CREADA:
                                if(isTestComunicacion){
                                    apiSobreescribirCompraN6(true);
                                }else{
                                    realizarCompraN6(true);
                                }
                                break;
                            case Constantes.ESTADO_TRASACCION_PENDIENTE:
                                if(!etmonto.getText().toString().isEmpty()){
                                    sweetAlertSobreescribir(getResources().getString(R.string.trasacion_pendiente),
                                            String.format(getResources().getString(R.string.msj_alerta_cancelar_tef_2),
                                                    Utilidades.formatearPrecio(response.body().getRespuestaCompra().getValorTotal()),
                                                    isTestComunicacion ? "10" : Utilidades.formatearPrecio(Double.parseDouble(etmonto.getText().toString()))),
                                            contexto, R.drawable.advertencia, isTestComunicacion);
                                }else{
                                    sweetAlertSobreescribir(getResources().getString(R.string.trasacion_pendiente),
                                            String.format(getResources().getString(R.string.msj_alerta_cancelar_tef_4),
                                                    Utilidades.formatearPrecio(response.body().getRespuestaCompra().getValorTotal())),
                                            contexto, R.drawable.advertencia, isTestComunicacion);
                                }
                                break;
                        }
                    }else{
                        util.ocultarPantallaCargaCustom();
                        if(isTestComunicacion){
                            pruebaDatafono(null);
                        }else{
                            realizarCompraN6(false);
                        }
                    }
                }else{
                    LogFile.adjuntarLog("Error ResponseConsultarCompraN6: " + response.message());
                    util.ocultarPantallaCargaCustom();
                    Utilidades.sweetAlert(getResources().getString(R.string.error), getResources().getString(R.string.error_conexion_sb) + response.message(),
                            SweetAlertDialog.ERROR_TYPE, contexto);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseConsultarCompraN6> call, @NonNull Throwable t) {
                LogFile.adjuntarLog("Error ResponseConsultarCompraN6: " + call + t);
                util.ocultarPantallaCargaCustom();
                Utilidades.sweetAlert(getResources().getString(R.string.error), getResources().getString(R.string.error_conexion) + t.getMessage(),
                        SweetAlertDialog.ERROR_TYPE, contexto);
            }
        });
    }

    private void apiSobreescribirCompraN6(boolean isTestComunicacion) {
        btnaccion.setEnabled(false);
        testComunicacion = isTestComunicacion;
        contadorDatafono();

        Random rand = new Random();
        int numeroFactura = rand.nextInt(900) + 100;
        Call<ResponseCompraN6> call;
        if(isTestComunicacion){
            call = Utilidades.servicioRetrofit().doSobreescribirCompraN6(SPM.getString(Constantes.USER_NAME)+" - "+getResources().getString(R.string.version_apk),
                    Utilidades.peticionSobreescribirCompraDatafonoN6(numeroFactura));
        }else{
            call = Utilidades.servicioRetrofit().doSobreescribirCompraN6(SPM.getString(Constantes.USER_NAME)+" - "+getResources().getString(R.string.version_apk),
                    Utilidades.peticionSobreescribirCompraDatafonoN6(factura));
        }

        call.enqueue(new Callback<ResponseCompraN6>() {
            @SuppressLint("StringFormatMatches")
            @Override
            public void onResponse(@NonNull Call<ResponseCompraN6> call, @NonNull Response<ResponseCompraN6> response) {
                if(response.isSuccessful()){
                    assert response.body() != null;
                    if(response.body().isEsValida()){
                        util.ocultarPantallaCargaCustom();

                        if(isTestComunicacion){
                            util.mostrarPantallaCargaCustom(getSupportFragmentManager(),String.format(getResources()
                                            .getString(R.string.progress_seguir_pasos_datafono_n6_prueba), numeroFactura),
                                    true, configurableCarga);
                        }else{
                            util.mostrarPantallaCargaCustom(getSupportFragmentManager(),getResources()
                                            .getString(R.string.progress_seguir_pasos_datafono_n6),
                                    true, configurableCarga);
                        }

                        countDownTimer.start();
                        SPM.setString(Constantes.ID_TRANSACCION_DATAFONO, Integer.toString(response.body().getRespuestaCompra().getIdTransaccion()));
                        iniciarTareaRespuestaDatafono(isTestComunicacion);
                    }else{
                        transaccionFinalizada = false;
                        btnaccion.setEnabled(true);
                        LogFile.adjuntarLog("Error ResponseCompraN6: " + response.body().getMensaje());
                        util.ocultarPantallaCargaCustom();
                        Utilidades.sweetAlert(getResources().getString(R.string.mensaje), response.body().getMensaje(),
                                SweetAlertDialog.WARNING_TYPE, contexto);
                    }
                }else{
                    transaccionFinalizada = false;
                    btnaccion.setEnabled(true);
                    LogFile.adjuntarLog("Error ResponseCompraN6: " + response.message());
                    util.ocultarPantallaCargaCustom();
                    Utilidades.sweetAlert(getResources().getString(R.string.error), getResources().getString(R.string.error_conexion_sb) + response.message(),
                            SweetAlertDialog.ERROR_TYPE, contexto);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseCompraN6> call, @NonNull Throwable t) {
                transaccionFinalizada = false;
                btnaccion.setEnabled(true);
                LogFile.adjuntarLog("Error ResponseCompraN6: " + call + t);
                util.ocultarPantallaCargaCustom();
                Utilidades.sweetAlert(getResources().getString(R.string.error), getResources().getString(R.string.error_conexion) + t.getMessage(),
                        SweetAlertDialog.ERROR_TYPE, contexto);
            }
        });
    }

    public void contadorDatafono(){
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
                    if(estadoTransacion.equals(Constantes.ESTADO_TRASACCION_PENDIENTE)){
                        util.ocultarDialogProgressBar();
                        sweetAlert(getResources().getString(R.string.trasacion_pendiente),
                                getResources().getString(R.string.msj_alerta_cancelar_tef), contexto, R.drawable.advertencia);
                    }else{
                        borrarCompraN6(false);
                    }
                }else{
                    util.ocultarPantallaCargaCustom();
                }
            }
        };
    }

    private void realizarCompraN6(boolean sobreescribir){
        if(validarMonto()){
            String montoentrans = etmonto.getText().toString();
            int monto = Integer.parseInt(montoentrans);

            int ivacompra = iva.intValue();
            int total = totalCompra.intValue();

            if(ivacompra > 0){
                if(total != monto){
                    ivacompra = Math.round((float)(iva*monto)/total);
                }
            }

            factura.setValorIvaN6(ivacompra);
            factura.setValorVentaN6(monto-ivacompra);

            util.mostrarPantallaCargaCustom(getSupportFragmentManager(),"Consultando consecutivo fiscal...",
                    false, configurableCarga);
            consecutivoFiscal(sobreescribir);
        }
    }

    private void consecutivoFiscal(boolean sobreescribir) {
        //Consultar la Api de Consecutivo Fiscal
        Call<ResponseConsecutivoFiscal> callConsecutivoFiscal =
                Utilidades.servicioRetrofit()
                        .doConsecutivoFiscal(SPM.getString(Constantes.USER_NAME)+" - "+getResources().getString(R.string.version_apk),
                                factura.getCaja());
        callConsecutivoFiscal.enqueue(new Callback<ResponseConsecutivoFiscal>() {
            @Override
            public void onResponse(@NonNull Call<ResponseConsecutivoFiscal> call, @NonNull Response<ResponseConsecutivoFiscal> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getEsValida()) {
                        util.ocultarPantallaCargaCustom();
                        factura.setConsecutivo(response.body().getConsecutivo().toString());
                        factura.setPrefijo(response.body().getPrefijo());
                        factura.construirReferenciaInterna();

                        util.mostrarPantallaCargaCustom(getSupportFragmentManager(),"Generando petición...",
                                false, configurableCarga);
                        transaccionFinalizada = false;
                        if(sobreescribir){
                            apiSobreescribirCompraN6(false);
                        }else{
                            compraN6(false);
                        }
                    } else {
                        for (String s : response.body().getMensaje()) {
                            LogFile.adjuntarLog("Error ErrorResponseConsecutivoFiscal: " + s);
                            mensajeSimpleDialog(getResources().getString(R.string.error), s,
                                    Constantes.SERVICIO_CONSECUTIVO_FISCAL);
                        }
                        util.ocultarPantallaCargaCustom();
                    }
                } else {
                    LogFile.adjuntarLog("Error ErrorResponseConsecutivoFiscal: " + response.message());
                    mensajeSimpleDialog(getResources().getString(R.string.error), getResources().getString(R.string.error_conexion_sb) + response.message(),
                            Constantes.SERVICIO_CONSECUTIVO_FISCAL);
                    util.ocultarPantallaCargaCustom();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseConsecutivoFiscal> call, @NonNull Throwable t) {
                LogFile.adjuntarLog("Error ErrorResponseConsecutivoFiscal: " + call + t);
                mensajeSimpleDialog(getResources().getString(R.string.error), getResources().getString(R.string.error_conexion) + t.getMessage(), Constantes.SERVICIO_CONSECUTIVO_FISCAL);
                util.ocultarPantallaCargaCustom();
            }
        });
    }

    private void pruebaDatafono(View view){
        try{
            if(etUsuarioDatafono.getText().toString().isEmpty()){
                Utilidades.mjsToast("Ingrese el usuario para el datáfono", Constantes.TOAST_TYPE_INFO,
                        Toast.LENGTH_LONG, contexto);
            }else if(etPassDatafono.getText().toString().isEmpty()){
                Utilidades.mjsToast("Ingrese la contraseña para el datáfono", Constantes.TOAST_TYPE_INFO,
                        Toast.LENGTH_LONG, contexto);
            }else if(etCodigoDatafono.getText().toString().isEmpty()){
                Utilidades.mjsToast("Ingrese el código del datáfono", Constantes.TOAST_TYPE_INFO,
                        Toast.LENGTH_LONG, contexto);
            }else if(etCodigoUnicoDatafono.getText().toString().isEmpty()) {
                Utilidades.mjsToast("Ingrese el código unico del datáfono", Constantes.TOAST_TYPE_INFO,
                        Toast.LENGTH_LONG, contexto);
            }else{
                util.mostrarPantallaCargaCustom(getSupportFragmentManager(),"Generando petición...",
                        false, this);
                transaccionFinalizada = false;
                compraN6(true);
            }
        }catch (Exception e){
            Utilidades.sweetAlert(getResources().getString(R.string.error), e.getMessage(), SweetAlertDialog.ERROR_TYPE, contexto);
        }
    }

    @SuppressLint("StringFormatMatches")
    private void compraN6(boolean isTestComunicacion){
        btnaccion.setEnabled(false);
        testComunicacion = isTestComunicacion;

        contadorDatafono();

        Random rand = new Random();
        int numeroFactura = rand.nextInt(900) + 100;
        Call<ResponseCompraN6> call;
        if(isTestComunicacion){
            call = Utilidades.servicioRetrofit().doCompraN6(SPM.getString(Constantes.USER_NAME)+" - "+getResources().getString(R.string.version_apk),
                    Utilidades.peticionCompraDatafonoN6(numeroFactura));
        }else{
            call = Utilidades.servicioRetrofit().doCompraN6(SPM.getString(Constantes.USER_NAME)+" - "+getResources().getString(R.string.version_apk),
                    Utilidades.peticionCompraDatafonoN6(factura));
        }

        call.enqueue(new Callback<ResponseCompraN6>() {
            @Override
            public void onResponse(@NonNull Call<ResponseCompraN6> call, @NonNull Response<ResponseCompraN6> response) {
                if(response.isSuccessful()){
                    assert response.body() != null;
                    if(response.body().isEsValida()){
                        util.ocultarPantallaCargaCustom();

                        if(isTestComunicacion){
                            util.mostrarPantallaCargaCustom(getSupportFragmentManager(),String.format(getResources()
                                            .getString(R.string.progress_seguir_pasos_datafono_n6_prueba), numeroFactura),
                                    true, configurableCarga);
                        }else{
                            util.mostrarPantallaCargaCustom(getSupportFragmentManager(),getResources()
                                            .getString(R.string.progress_seguir_pasos_datafono_n6),
                                    true, configurableCarga);
                        }

                        countDownTimer.start();
                        SPM.setString(Constantes.ID_TRANSACCION_DATAFONO, Integer.toString(response.body().getRespuestaCompra().getIdTransaccion()));
                        iniciarTareaRespuestaDatafono(isTestComunicacion);
                    }else{
                        transaccionFinalizada = false;
                        btnaccion.setEnabled(true);
                        LogFile.adjuntarLog("Error ResponseCompraN6: " + response.body().getMensaje());
                        util.ocultarPantallaCargaCustom();
                        Utilidades.sweetAlert(getResources().getString(R.string.mensaje), "Revisa la configuración del datafono, la información no es correcta o hay datos trocados.",
                                SweetAlertDialog.WARNING_TYPE, contexto);
                    }
                }else{
                    transaccionFinalizada = false;
                    btnaccion.setEnabled(true);
                    LogFile.adjuntarLog("Error ResponseCompraN6: " + response.message());
                    util.ocultarPantallaCargaCustom();
                    Utilidades.sweetAlert(getResources().getString(R.string.error), getResources().getString(R.string.error_conexion_sb) + response.message(),
                            SweetAlertDialog.ERROR_TYPE, contexto);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseCompraN6> call, @NonNull Throwable t) {
                transaccionFinalizada = false;
                btnaccion.setEnabled(true);
                LogFile.adjuntarLog("Error ResponseCompraN6: " + call + t);
                util.ocultarPantallaCargaCustom();
                Utilidades.sweetAlert(getResources().getString(R.string.error), getResources().getString(R.string.error_conexion) + t.getMessage(),
                        SweetAlertDialog.ERROR_TYPE, contexto);
            }
        });
    }

    public void sweetAlert(String titulo, String mensaje, Context contexto, int imagen){
        SweetAlertDialog alertDialog = new SweetAlertDialog(contexto, SweetAlertDialog.CUSTOM_IMAGE_TYPE);

        alertDialog.setTitleText(titulo)
                .setCustomImage(imagen)
                .setContentText(mensaje)
                .setConfirmButton(contexto.getResources().getString(R.string.entiendo), sweetAlertDialog -> {
                    util.mostrarPantallaCargaCustom(getSupportFragmentManager(),getResources()
                                    .getString(R.string.progress_seguir_pasos_datafono_n6),
                            true, configurableCarga);
                    countDownTimer.start();
                    iniciarTareaRespuestaDatafono(testComunicacion);
                    sweetAlertDialog.dismissWithAnimation();
                })
                .setCancelable(false);

        alertDialog.show();
    }

    public void sweetAlertSobreescribir(String titulo, String mensaje, Context contexto, int imagen, boolean isTestComunicacion){
        SweetAlertDialog alertDialog = new SweetAlertDialog(contexto, SweetAlertDialog.CUSTOM_IMAGE_TYPE);

        alertDialog.setTitleText(titulo)
                .setCustomImage(imagen)
                .setContentText(mensaje)
                .setConfirmButton(contexto.getResources().getString(R.string.continuar), sweetAlertDialog -> {
                    util.mostrarPantallaCargaCustom(getSupportFragmentManager(),getResources()
                                    .getString(R.string.progress_seguir_pasos_datafono_n6),
                            true, configurableCarga);
                    contadorDatafono();
                    countDownTimer.start();
                    iniciarTareaRespuestaDatafono(testComunicacion);
                    sweetAlertDialog.dismissWithAnimation();
                })
                .setCancelButton(contexto.getResources().getString(R.string.cambiar), sweetAlertDialog -> {
                    sweetAlertCambiar(getResources().getString(R.string.sobreescribir_proceso_titulo),
                            getResources().getString(R.string.msj_alerta_cancelar_tef_3),
                            contexto, R.drawable.advertencia, isTestComunicacion);
                    sweetAlertDialog.dismissWithAnimation();
                })
                .setCancelable(false);

        alertDialog.show();
    }

    public void sweetAlertCambiar(String titulo, String mensaje, Context contexto, int imagen, boolean isTestComunicacion){
        SweetAlertDialog alertDialog = new SweetAlertDialog(contexto, SweetAlertDialog.CUSTOM_IMAGE_TYPE);

        alertDialog.setTitleText(titulo)
                .setCustomImage(imagen)
                .setContentText(mensaje)
                .setConfirmButton(contexto.getResources().getString(R.string.listo), sweetAlertDialog -> {
                    comprobarRespuestaDatafono(isTestComunicacion);
                    sweetAlertDialog.dismissWithAnimation();
                })
                .setCancelButton(contexto.getResources().getString(R.string.salir), SweetAlertDialog::dismissWithAnimation)
                .setCancelable(false);

        alertDialog.show();
    }

    private void comprobarPagoN6(View view){
        if(NetworkUtil.isConnected(contexto)){
            tvErrorConexionN6.setVisibility(View.GONE);
            btnComprobarPagoN6.setVisibility(View.GONE);

            util.mostrarPantallaCargaCustom(getSupportFragmentManager(),getResources()
                            .getString(R.string.progress_seguir_pasos_datafono_n6_comprobar_pago),
                    true, configurableCarga);
            countDownTimer.start();
            iniciarTareaRespuestaDatafono(testComunicacion);
        }else{
            Utilidades.mjsToast("Todavía no hay conexión a internet", Constantes.TOAST_TYPE_INFO,
                    Toast.LENGTH_LONG, contexto);
        }
    }

    private void apiRespuestaDatafono(boolean isTestComunicacion) {
        Call<ResponseConsultarCompraN6> call = Utilidades.servicioRetrofit().doConsultarCompraN6(SPM.getString(Constantes.USER_NAME)+" - "+getResources().getString(R.string.version_apk),
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
                                btnaccion.setEnabled(true);
                                transaccionFinalizada = true;
                                btnComprobarPagoN6.setVisibility(View.GONE);
                                util.ocultarPantallaCargaCustom();
                                RespuestaTransaccion respuestaTransaccion = response.body().getRespuestaCompra().getRespuestaTransaccion();
                                RespuestaConsultarCompraN6 respuestaCompra = response.body().getRespuestaCompra();

                                if(isTestComunicacion){
                                    MostrarInfoDialogFragment vistaDialogFragmentMostrarInfo =
                                            new MostrarInfoDialogFragment(R.drawable.anular_tef_dos, "Información Tef",
                                                    respuestaTransaccion.toStringClaveValor(), Constantes.ACCION_TEF_COMUNICACION);
                                    vistaDialogFragmentMostrarInfo.show(getSupportFragmentManager(), "MostrarInfoClaveValor");
                                }else{
                                    datafonoEntity = new DatafonoEntity("COM",respuestaTransaccion.getBinDeTarjeta(),
                                            respuestaTransaccion.getCodigoDeAprobacion(),respuestaTransaccion.getCodigoDeRespuesta(),
                                            respuestaTransaccion.getDireccionComercio(),"",respuestaTransaccion.getFranquicia(),
                                            respuestaTransaccion.getValorTotal(), respuestaTransaccion.getValorIVA(),"",0L,0L,
                                            "0","", Integer.toString(respuestaTransaccion.getNumeroDeCuotas()),
                                            Integer.toString(respuestaTransaccion.getNumeroDeRecibo()),
                                            respuestaTransaccion.getCodigoDeTerminal(),respuestaTransaccion.getRrn(),
                                            respuestaTransaccion.getTipoCuenta(),respuestaTransaccion.getUltimosDigitosTarjeta(),
                                            respuestaCompra.getCodigoDeComercio(),0.0,respuestaTransaccion.getFechaTransaccion(),
                                            respuestaTransaccion.getHoraTransaccion(),"",0.0,factura.getNombreTienda(), false);

                                    finalizarPagoCompra();
                                }
                                break;
                            case Constantes.ESTADO_TRASACCION_CREADA:
                            case Constantes.ESTADO_TRASACCION_PENDIENTE:
                                iniciarTareaRespuestaDatafono(isTestComunicacion);
                                break;
                        }
                    }else{
                        btnaccion.setEnabled(true);
                        countDownTimer.cancel();
                        transaccionFinalizada = false;
                        LogFile.adjuntarLog("Error ResponseConsultarCompraN6: " + response.body().getMensaje());
                        util.ocultarPantallaCargaCustom();
                        if(response.body().getMensaje().equals("Error al consultar la compra")){
                            Utilidades.sweetAlert(getResources().getString(R.string.error), "Error en la transacción, genera otra petición al datafono.",
                                    SweetAlertDialog.ERROR_TYPE, contexto);
                        }else{
                            Utilidades.sweetAlert(getResources().getString(R.string.error), response.body().getMensaje(),
                                    SweetAlertDialog.ERROR_TYPE, contexto);
                        }
                    }
                }else{
                    if(!NetworkUtil.isConnected(contexto)){
                        if(!isTestComunicacion){
                            tvErrorConexionN6.setVisibility(View.VISIBLE);
                        }else{
                            Utilidades.sweetAlertCustom(getResources().getString(R.string.informacion),
                                    getResources().getString(R.string.tv_msj_error_conexion_n6),
                                    contexto, R.drawable.offline);
                        }

                        countDownTimer.cancel();
                        btnComprobarPagoN6.setVisibility(View.VISIBLE);
                    }else{
                        btnaccion.setEnabled(true);
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
                    if(!isTestComunicacion){
                        tvErrorConexionN6.setVisibility(View.VISIBLE);
                    }else{
                        Utilidades.sweetAlertCustom(getResources().getString(R.string.informacion),
                                getResources().getString(R.string.tv_msj_error_conexion_n6),
                                contexto, R.drawable.offline);
                    }

                    countDownTimer.cancel();
                    btnComprobarPagoN6.setVisibility(View.VISIBLE);
                }else{
                    btnaccion.setEnabled(true);
                }

                transaccionFinalizada = false;
                LogFile.adjuntarLog("Error ResponseConsultarCompraN6: " + call + t);
                util.ocultarPantallaCargaCustom();
                Utilidades.sweetAlert(getResources().getString(R.string.error), getResources().getString(R.string.error_conexion) + t.getMessage(),
                        SweetAlertDialog.ERROR_TYPE, contexto);
            }
        });
    }

    private void iniciarTareaRespuestaDatafono(boolean isTestComunicacion) {
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
                    apiRespuestaDatafono(isTestComunicacion);
                }
            }
        };

        tiempo = new Timer();
        tiempo.schedule(tarea, tiempoEsperaDatafono);
    }

    @Override
    public void cerrarFragmentCarga() {
        if(!estadoTransacion.isEmpty()){
            switch (estadoTransacion){
                case Constantes.ESTADO_TRASACCION_PENDIENTE:
                    Utilidades.sweetAlertCustom(getResources().getString(R.string.trasacion_pendiente),
                            getResources().getString(R.string.msj_alerta_cancelar_tef), contexto, R.drawable.advertencia);
                    break;
                case Constantes.ESTADO_TRASACCION_CREADA:
                    util.ocultarPantallaCargaCustom();
                    borrarCompraN6(false);
                    break;
            }
        }
    }

    private void borrarCompraN6(boolean salir) {
        Call<ResponseBorrarCompraN6> call = Utilidades.servicioRetrofit().doBorrarCompraN6(SPM.getString(Constantes.USER_NAME)+" - "+getResources().getString(R.string.version_apk),
                Utilidades.peticionBaseN6());
        call.enqueue(new Callback<ResponseBorrarCompraN6>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBorrarCompraN6> call, @NonNull Response<ResponseBorrarCompraN6> response) {
                if(response.isSuccessful()){
                    assert response.body() != null;
                    if(response.body().isEsValida()){
                        util.ocultarPantallaCargaCustom();
                        btnaccion.setEnabled(true);

                        if(tarea != null){
                            tarea.cancel();
                        }

                        if(tiempo != null){
                            tiempo.cancel();
                        }

                        countDownTimer.cancel();
                        Utilidades.mjsToast(response.body().getRespuesta().getMensajeDeRespuesta(),
                                Constantes.TOAST_TYPE_INFO, Toast.LENGTH_LONG, contexto);

                        if(salir){
                            finish();
                        }
                    }else{
                        LogFile.adjuntarLog("Error ResponseBorrarCompraN6: " + response.body().getMensaje());
                        util.ocultarPantallaCargaCustom();
                        if(salir){
                            finish();
                        }else{
                            btnaccion.setEnabled(true);
                            Utilidades.sweetAlert(getResources().getString(R.string.error), response.body().getMensaje(),
                                    SweetAlertDialog.ERROR_TYPE, contexto);
                        }
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

    @Override
    public void onBackPressed(){
        if(enproceso){
            msjToast("finalice la transacción");
        }else{
            if(tipoOperacion.equals("testComunicacion")){
                guardarPerifericos();
            }else{
                finish();
            }
        }
    }

    private void guardarPerifericos() {
        util.mostrarPantallaCargaCustom(getSupportFragmentManager(), "Guardando perifericos...",false,
                null);
        List<RequestInsertarPerifericos> perifericos = new ArrayList<>();

        if(tipoDatafono.getNombreDispositivo().equals(Constantes.DATAFONO_SMARTPOS_N6)){
            perifericos.add(new RequestInsertarPerifericos(factura.getCaja(), SPM.getString(Constantes.TIENDA_CODE),
                    "", "", Constantes.CONST_DATAFONO_CREDIBANCO_N6, "",
                    SPM.getString(Constantes.USUARIO_DATAFONO_CD), SPM.getString(Constantes.PASS_DATAFONO_CD),
                    SPM.getString(Constantes.CODIGO_DATAFONO_CD), SPM.getString(Constantes.CODIGO_UNICO_DATAFONO_CD),
                    SPM.getString(Constantes.USER_NAME)));
        }else{
            perifericos.add(new RequestInsertarPerifericos(factura.getCaja(), SPM.getString(Constantes.TIENDA_CODE),
                    "", SPM.getString(Constantes.NAME_DATAFONO_ROMPEFILAS), Constantes.CONST_DATAFONO_ROMPEFILAS,
                    SPM.getString(Constantes.MAC_DATAFONO_ROMPEFILAS), "", "", "",
                    "", SPM.getString(Constantes.USER_NAME)));
        }

        Call<ResponseGuardarPerifericos> callGuardarPerifericos = Utilidades.servicioRetrofit().doGuardarPerifericos(
                SPM.getString(Constantes.USER_NAME) + " - " + getResources().getString(R.string.version_apk),perifericos);

        callGuardarPerifericos.enqueue(new Callback<ResponseGuardarPerifericos>() {
            @Override
            public void onResponse(@NonNull Call<ResponseGuardarPerifericos> call, @NonNull Response<ResponseGuardarPerifericos> response) {
                if(response.isSuccessful()){
                    assert response.body() != null;
                    if(response.body().isEsValida()){
                        util.ocultarPantallaCargaCustom();
                        finish();
                    }else{
                        util.ocultarPantallaCargaCustom();
                        Utilidades.mjsToast(response.body().getMensaje(), Constantes.TOAST_TYPE_ERROR,
                                Toast.LENGTH_LONG, contexto);
                        finish();
                    }
                }else{
                    LogFile.adjuntarLog("Error ResponseGuardarPerifericos: " + response.message());
                    util.ocultarPantallaCargaCustom();
                    Utilidades.sweetAlert(getResources().getString(R.string.error), getResources().getString(R.string.error_conexion_sb) + response.message(),
                            SweetAlertDialog.ERROR_TYPE, contexto);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseGuardarPerifericos> call, @NonNull Throwable t) {
                LogFile.adjuntarLog("Error ResponseGuardarPerifericos: " + call + t);
                util.ocultarPantallaCargaCustom();
                Utilidades.sweetAlert(getResources().getString(R.string.error), getResources().getString(R.string.error_conexion) + t.getMessage(),
                        SweetAlertDialog.ERROR_TYPE, contexto);
            }
        });
    }

    private void finalizarPagoCompra(){
        //crear el medio de pago tef para los importes con la respuesta del datafono
        Payment pagoDatafono = new Payment(mediopago.getNombre(), datafonoEntity.getMonto(), mediopago.getDivisa(), "", 0, 1, mediopago.getCodigo());
        pagoDatafono.setTEF(true);
        pagoDatafono.setDatafonoEntity(datafonoEntity);
        Intent intent= new Intent();
        intent.putExtra("pagoDatafono", pagoDatafono);
        //devolver el objeto pagoDatafono
        setResult(Constantes.RESP_TEF_VENTA,intent);
        finish();
    }

    private void msjToast(String msj) {
        Toast.makeText(DatafonoN6Activity.this, msj, Toast.LENGTH_SHORT).show();
    }

    public void mensajeSimpleDialog(String titulo, String msj, int servicio) {
        int icon = SweetAlertDialog.WARNING_TYPE;
        if (titulo.equals(getResources().getString(R.string.error))) {
            icon = SweetAlertDialog.ERROR_TYPE;
        }

        Utilidades.sweetAlert(titulo, msj + " Cod ("+servicio+")", icon, DatafonoN6Activity.this);
    }

    public boolean validarMonto(){
        if(etmonto.getText().toString().isEmpty() || etmonto.getText().toString().length()> 9){
            msjToast(getResources().getString(R.string.importe_invalido));
            enproceso = false;
            return false;
        }else {
            String montoentrans = etmonto.getText().toString();
            if (Integer.parseInt(montoentrans) > deuda.intValue()) {
                msjToast(getResources().getString(R.string.importe_no_mayor_deuda));
                etmonto.setText("");
                enproceso = false;
                return false;
            }else{
                int monto = Integer.parseInt(montoentrans);
                if(monto<=0){
                    msjToast(getResources().getString(R.string.importe_no_0));
                    etmonto.setText("");
                    enproceso = false;
                    return false;
                }

                return true;
            }
        }
    }
}