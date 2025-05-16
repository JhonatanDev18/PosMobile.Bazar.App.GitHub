package com.crystal.bazarposmobile.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.common.Constantes;
import com.crystal.bazarposmobile.common.DatafonoTotales;
import com.crystal.bazarposmobile.common.LogFile;
import com.crystal.bazarposmobile.common.SPM;
import com.crystal.bazarposmobile.common.TipoDatafono;
import com.crystal.bazarposmobile.common.Utilidades;
import com.crystal.bazarposmobile.dataphono.CommonActivity;
import com.crystal.bazarposmobile.db.BazarPosMovilDB;
import com.crystal.bazarposmobile.db.entity.DatafonoEntity;
import com.crystal.bazarposmobile.retrofit.request.creardocumento.Payment;
import com.crystal.bazarposmobile.retrofit.response.mediospagocaja.MediosCaja;
import com.ingenico.pclservice.PclService;
import com.ingenico.pclservice.TransactionIn;
import com.ingenico.pclservice.TransactionOut;
import com.ingenico.pclutilities.PclUtilities;
import com.ingenico.pclutilities.PclUtilities.BluetoothCompanion;
import com.ingenico.pclutilities.PclUtilities.IpTerminal;
import com.ingenico.pclutilities.PclUtilities.SerialPortCompanion;
import com.ingenico.pclutilities.PclUtilities.UsbCompanion;
import com.ingenico.pclutilities.SslObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.MissingFormatArgumentException;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class DatafonoActivity extends CommonActivity implements OnClickListener, OnCheckedChangeListener {

    //Declaración de los objetos de la interfaz del activity
    private RadioGroup rg;
    private EditText etmonto,etnumerorecibo;
    private TextView tvestado, tvduracion, tvresultadoterminal, tvtitulo,tvdeuda,tvtotal,tvresultado,tvestadoconexion;
    private ProgressBar pb;
    private Button btnaccion,btndeuda,btnfinalizarcierre,btnimprtotales,bntimprdetallado,btnimprimir;
    private ImageView ivreiniciar;

    //Declaración de la variables del activity
    private static final String TAG = "PCLTESTAPP";
    private PclUtilities mPclUtil;
    private boolean mServiceStarted, sslActivated = false, mPermissionRequested = false, mPermissionGranted = false, mRestart;
    boolean esIniciarCierre = true;
    private int terminalCounter = 0;
    private Set<IpTerminal> terminals = new HashSet<IpTerminal>();
    private long mTestStartTime;
    private long mTestEndTime;
    CharSequence mCurrentDevice;
    UsbManager mUsbManager = null;
    PendingIntent mPermissionIntent = null;
    private static final String ACTION_USB_PERMISSION = "com.crystal.bazarposmobile.USB_PERMISSION";
    CheckBox mcbActivateSsl;

    MediosCaja mediopago;
    DatafonoEntity datafonoEntity;
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

    @SuppressLint("SetTextI18n")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datafono);

        //Recibir el tipo de operacion
        tipoDatafono = (TipoDatafono) getIntent().getSerializableExtra("tipoDatafono");
        tipoOperacion = getIntent().getExtras().getString("tipoOperacion");

        SPM.setObject(Constantes.OBJECT_TIPO_DATAFONO, tipoDatafono);

        //Variables generales
        caja = SPM.getString(Constantes.CAJA_CODE);
        cajero_code = SPM.getString(Constantes.CAJERO_CODE);
        nombreTiendadfff32 = SPM.getString(Constantes.NOMBRE_TIENDA);

        //variables de definicion de operacion
        String titulo;
        ConstraintLayout mainLayout;
        LayoutInflater inflater;
        View layout;

        findViews();

        etmonto = findViewById(R.id.etMontoDatafonoA);
        //Tipos de operaciones en el datafono
        switch (tipoOperacion) {
            case "compra":
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
                layout = inflater.inflate(R.layout.datafono_cierre_content, mainLayout, false);
                mainLayout.removeAllViews();
                mainLayout.addView(layout);

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
                layout = inflater.inflate(R.layout.datafono_ultimatransaccion_content, mainLayout, false);
                mainLayout.removeAllViews();
                mainLayout.addView(layout);

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
                layout = inflater.inflate(R.layout.datafono_anulacion_content, mainLayout, false);
                mainLayout.removeAllViews();
                mainLayout.addView(layout);

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
                layout = inflater.inflate(R.layout.datafono_bonoregalo_content, mainLayout, false);
                mainLayout.removeAllViews();
                mainLayout.addView(layout);

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
                //Titulo del activity
                tvtitulo.setText(getResources().getString(R.string.titulo_test_comunicacion_datafono));

                //Mostrando el layout para el cierre
                mainLayout = findViewById(R.id.includeDatafonoA);
                inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                layout = inflater.inflate(R.layout.datafono_testcomunicacion_content, mainLayout, false);
                mainLayout.removeAllViews();
                mainLayout.addView(layout);

                //Definicion de elementos para compras
                btnaccion = mainLayout.findViewById(R.id.btnTCDA);

                //Eventos usados en el cierre de datafono
                btnaccion.setOnClickListener(this);
                break;
        }

        eventos();

        final CharSequence data = (CharSequence) getLastNonConfigurationInstance();
        mCurrentDevice = data;
        Log.d(TAG, "mCurrentDevice = " + mCurrentDevice);
        mTestStartTime = System.currentTimeMillis();

        //Ocultar el teclado de pantalla
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mPclUtil = new PclUtilities(this, "com.crystal.bazarposmobile", "pairing_addr.txt");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
            if (mUsbManager != null) {
                mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
                IntentFilter usbFilter = new IntentFilter(ACTION_USB_PERMISSION);
                usbFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
                usbFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
                registerReceiver(mUsbReceiver,
                        usbFilter,
                        "com.crystal.bazarposmobile.permission.BROADCAST_USB_PERMISSION",
                        null);
            }
        }

        //Activar el bluetooth
        Intent enableBtIntent = new Intent(
                BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent,
                Constantes.RESP_ACT_BLUE_SELECT);
    }

    //definicion de elementos generales
    private void findViews() {
        tvtitulo = findViewById(R.id.tvTituloDatafonoA);

        pb = findViewById(R.id.pbDatafonoA);
        pb.setVisibility(View.GONE);

        tvestado = findViewById(R.id.tvEstatusDatafonoA);
        tvduracion = findViewById(R.id.tvDuracionDatafonoA);
        tvresultadoterminal = findViewById(R.id.tvRespuestaDatafonoA);
        tvestadoconexion = findViewById(R.id.tvEstadoDatafono);

        rg = findViewById(R.id.rgTerminalDatafonoA);
        ivreiniciar = findViewById(R.id.ivReiniciarDatafonoA);

        tvresultado = findViewById(R.id.tvTituloResultadoDatafonoA);
    }

    //eventos generales
    private void eventos() {
        ivreiniciar.setOnClickListener(this);

        etmonto.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if(btnaccion.isEnabled())
                        btnaccion.callOnClick();
                }
                return handled;
            }
        });
    }


    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @Override
    public void onClick(View v) {

        Intent i = Utilidades.activityImprimir(DatafonoActivity.this);

        switch (v.getId()) {
            //Importe igual a la deuda
            case R.id.btnDeudaDatafonoA:
                int d = deuda.intValue();
                etmonto.setText(Integer.toString(d));
                break;
            //Reiniciar Datafono
            case R.id.ivReiniciarDatafonoA:
                this.runReset();
                break;
            //Realizar pago TEF
            case R.id.btnPagoDatafonoA:
                if(!enproceso){
                    enproceso = true;
                    realizarCompra();
                }
                break;
            //Iniciar Cierre TEF
            case R.id.btnCierreDatafonoA:
                if(!enproceso){
                    enproceso = true;
                    iniciarCierre();
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
            //Realizar Cierre TEF
            case R.id.btnFinalizarCierreDatafonoA:
                if(!enproceso){
                    enproceso = true;
                    realizarCierre();
                }
                break;
            //Consulta la ultima trasaccion
            case R.id.btnConsultarUTDA:
                consultarUltimaTransaccion();
                break;
            //Anular trasaccion
            case R.id.btnAnularDatafonoA:
                if(!enproceso){
                    enproceso = true;
                    anularTransaccion();
                }
                break;
            //Bono Regalo
            case R.id.btnRegaloDatafonoA:
                if(!enproceso){
                    enproceso = true;
                    bonoRegalo();
                }
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
                if(labelUlt.length() > 0){
                    i.putExtra("lablePrint", (Serializable) labelUlt);
                    i.putExtra("respDatafono", (Serializable) datafonoEntity);
                    i.putExtra("primeraimpresion", false);
                    startActivity(i);
                    break;
                }else{
                    msjToast(getResources().getString(R.string.print_ultima_datafono));
                }
            case R.id.btnTCDA:
                echoTest();
                break;
        }

    }

    private void echoTest() {
        pb.setVisibility(View.VISIBLE);

        int appNumber;
        appNumber = 0;

        //FC03
        String code = "DC0114";
        int codeT = (code.length())/2;
        String codeThex = Integer.toHexString(codeT);
        if(codeThex.length() == 1)
            codeThex = "0"+codeThex;
        code = "FC"+codeThex+code;

        LogFile.adjuntarLog("TEF Cierre Manual: "+code);

        byte[] extDataIn = null;
        try {
            byte[] tmp = code.getBytes("ISO-8859-1");

            extDataIn = new byte[tmp.length/2];
            int j=0;
            for (int i =0; i<tmp.length; i++)
            {
                if (tmp[i] >= 'a' && tmp[i] <= 'f')
                    tmp[i] = (byte) (tmp[i] - 'a' + 10);
                else if (tmp[i] >= 'A' && tmp[i] <= 'F')
                    tmp[i] = (byte) (tmp[i] - 'A' + 10);
                else if (tmp[i] >= '0' && tmp[i] <= '9')
                    tmp[i] = (byte) (tmp[i] - '0');
            }
            for (int i=0; i<tmp.length; i+=2)
            {
                String str = String.format("%02x", tmp[i]*16 + tmp[i+1]);
                extDataIn[j++] = (byte) Integer.parseInt(str, 16);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] extDataOut = new byte[5000];

        TransactionIn transIn = new TransactionIn();
        TransactionOut transOut = new TransactionOut();

        new DoTransactionExTask(transIn, transOut, appNumber, extDataIn, extDataOut).execute();
    }

    private void bonoRegalo() {
        if(etmonto.getText().toString().length() == 0 || etmonto.getText().toString().length()> 9){
            msjToast(getResources().getString(R.string.importe_invalido));
            etmonto.setText("");
            enproceso = false;
        }else{
            String montoentrans = etmonto.getText().toString();

            int monto = Integer.parseInt(montoentrans);
            if(monto>0){

                pb.setVisibility(View.VISIBLE);
                btnaccion.setEnabled(false);

                int appNumber = 0;

                //DFFE4006000000000000
                String montoUsando = monto+"00";
                int fin = 12-montoUsando.length();
                for(int i=0; i<fin; i++){
                    montoUsando = "0"+montoUsando;
                }
                montoUsando = "DFFE4006"+montoUsando;

                //DFFF2B
                String cajeroUsando = ASCIIhex(cajero_code);
                int cajeroT = (cajeroUsando.length())/2;
                String cajeroThex = Integer.toHexString(cajeroT);
                if(cajeroThex.length() == 1)
                    cajeroThex = "0"+cajeroThex;
                cajeroUsando = "DFFF2B"+cajeroThex+cajeroUsando;

                //DFFF25
                String cajaUsanda = ASCIIhex(caja);
                int cajaT = (cajaUsanda.length())/2;
                String cajaThex = Integer.toHexString(cajaT);
                if(cajaThex.length() == 1)
                    cajaThex = "0"+cajaThex;
                cajaUsanda = "DFFF25"+cajaThex+cajaUsanda;

                //FC03
                String code = "DC011F"+montoUsando+cajeroUsando+cajaUsanda;
                int codeT = (code.length())/2;
                String codeThex = Integer.toHexString(codeT);
                if(codeThex.length() == 1)
                    codeThex = "0"+codeThex;
                code = "FC"+codeThex+code;

                LogFile.adjuntarLog("TEF Bono Regalo: "+code);

                byte[] extDataIn = null;
                try {
                    byte[] tmp = code.getBytes("ISO-8859-1");

                    extDataIn = new byte[tmp.length/2];
                    int j=0;
                    for (int i =0; i<tmp.length; i++)
                    {
                        if (tmp[i] >= 'a' && tmp[i] <= 'f')
                            tmp[i] = (byte) (tmp[i] - 'a' + 10);
                        else if (tmp[i] >= 'A' && tmp[i] <= 'F')
                            tmp[i] = (byte) (tmp[i] - 'A' + 10);
                        else if (tmp[i] >= '0' && tmp[i] <= '9')
                            tmp[i] = (byte) (tmp[i] - '0');
                    }
                    for (int i=0; i<tmp.length; i+=2)
                    {
                        String str = String.format("%02x", tmp[i]*16 + tmp[i+1]);
                        extDataIn[j++] = (byte) Integer.parseInt(str, 16);
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                byte[] extDataOut = new byte[5000];

                TransactionIn transIn = new TransactionIn();
                TransactionOut transOut = new TransactionOut();

                new DoTransactionExTask(transIn, transOut, appNumber, extDataIn, extDataOut).execute();
            }else{
                msjToast(getResources().getString(R.string.importe_no_0));
                etmonto.setText("");
                enproceso = false;
            }
        }
    }

    private void anularTransaccion() {
        if(etnumerorecibo.getText().toString().length() == 0){
            msjToast(getResources().getString(R.string.numero_recibo_invalido));
            enproceso = false;
        }else{
            String numeroRecibo = etnumerorecibo.getText().toString();
            if(Integer.parseInt(numeroRecibo) < 0){
                msjToast(getResources().getString(R.string.numero_recibo_0));
                enproceso = false;
            }else{

                btnaccion.setEnabled(false);
                pb.setVisibility(View.VISIBLE);

                int appNumber = 0;

                //xDFFF29
                int fin = 6-numeroRecibo.length();
                for(int i=0; i<fin; i++){
                    numeroRecibo = "0"+numeroRecibo;
                }
                numeroRecibo = ASCIIhex(numeroRecibo);
                int numeroReciboT = (numeroRecibo.length())/2;
                String numeroReciboThex = Integer.toHexString(numeroReciboT);
                if(numeroReciboThex.length() == 1)
                    numeroReciboThex = "0"+numeroReciboThex;
                numeroRecibo = "DFFF29"+numeroReciboThex+numeroRecibo;

                //DFFF2B
                String cajeroUsando = ASCIIhex(cajero_code);
                int cajeroT = (cajeroUsando.length())/2;
                String cajeroThex = Integer.toHexString(cajeroT);
                if(cajeroThex.length() == 1)
                    cajeroThex = "0"+cajeroThex;
                cajeroUsando = "DFFF2B"+cajeroThex+cajeroUsando;

                //DFFF25
                String cajaUsanda = ASCIIhex(caja);
                int cajaT = (cajaUsanda.length())/2;
                String cajaThex = Integer.toHexString(cajaT);
                if(cajaThex.length() == 1)
                    cajaThex = "0"+cajaThex;
                cajaUsanda = "DFFF25"+cajaThex+cajaUsanda;


                //FC03
                String code = "DC0102"+cajeroUsando+cajaUsanda+numeroRecibo;
                int codeT = (code.length())/2;
                String codeThex = Integer.toHexString(codeT);
                if(codeThex.length() == 1)
                    codeThex = "0"+codeThex;
                code = "FC"+codeThex+code;

                LogFile.adjuntarLog("TEF Anulación: "+code);

                byte[] extDataIn = null;
                try {
                    byte[] tmp = code.getBytes("ISO-8859-1");

                    extDataIn = new byte[tmp.length/2];
                    int j=0;
                    for (int i =0; i<tmp.length; i++)
                    {
                        if (tmp[i] >= 'a' && tmp[i] <= 'f')
                            tmp[i] = (byte) (tmp[i] - 'a' + 10);
                        else if (tmp[i] >= 'A' && tmp[i] <= 'F')
                            tmp[i] = (byte) (tmp[i] - 'A' + 10);
                        else if (tmp[i] >= '0' && tmp[i] <= '9')
                            tmp[i] = (byte) (tmp[i] - '0');
                    }
                    for (int i=0; i<tmp.length; i+=2)
                    {
                        String str = String.format("%02x", tmp[i]*16 + tmp[i+1]);
                        extDataIn[j++] = (byte) Integer.parseInt(str, 16);
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                byte[] extDataOut = new byte[5000];

                TransactionIn transIn = new TransactionIn();
                TransactionOut transOut = new TransactionOut();

                new DoTransactionExTask(transIn, transOut, appNumber, extDataIn, extDataOut).execute();
            }
        }
    }

    private void consultarUltimaTransaccion() {
        pb.setVisibility(View.VISIBLE);

        int appNumber;
        appNumber = 0;

        //DFFF2B
        String cajeroUsando = ASCIIhex(cajero_code);
        int cajeroT = (cajeroUsando.length())/2;
        String cajeroThex = Integer.toHexString(cajeroT);
        if(cajeroThex.length() == 1)
            cajeroThex = "0"+cajeroThex;
        cajeroUsando = "DFFF2B"+cajeroThex+cajeroUsando;

        //DFFF25
        String cajaUsanda = ASCIIhex(caja);
        int cajaT = (cajaUsanda.length())/2;
        String cajaThex = Integer.toHexString(cajaT);
        if(cajaThex.length() == 1)
            cajaThex = "0"+cajaThex;
        cajaUsanda = "DFFF25"+cajaThex+cajaUsanda;

        //FC03
        String code = "DC0110"+cajeroUsando+cajaUsanda;
        int codeT = (code.length())/2;
        String codeThex = Integer.toHexString(codeT);
        if(codeThex.length() == 1)
            codeThex = "0"+codeThex;
        code = "FC"+codeThex+code;

        LogFile.adjuntarLog("TEF Recuperación ultima Tx: "+code);

        byte[] extDataIn = null;
        try {
            byte[] tmp = code.getBytes("ISO-8859-1");

            extDataIn = new byte[tmp.length/2];
            int j=0;
            for (int i =0; i<tmp.length; i++)
            {
                if (tmp[i] >= 'a' && tmp[i] <= 'f')
                    tmp[i] = (byte) (tmp[i] - 'a' + 10);
                else if (tmp[i] >= 'A' && tmp[i] <= 'F')
                    tmp[i] = (byte) (tmp[i] - 'A' + 10);
                else if (tmp[i] >= '0' && tmp[i] <= '9')
                    tmp[i] = (byte) (tmp[i] - '0');
            }
            for (int i=0; i<tmp.length; i+=2)
            {
                String str = String.format("%02x", tmp[i]*16 + tmp[i+1]);
                extDataIn[j++] = (byte) Integer.parseInt(str, 16);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] extDataOut = new byte[5000];

        TransactionIn transIn = new TransactionIn();
        TransactionOut transOut = new TransactionOut();

        new DoTransactionExTask(transIn, transOut, appNumber, extDataIn, extDataOut).execute();
    }

    private void realizarCierre() {
        pb.setVisibility(View.VISIBLE);

        int appNumber;
        appNumber = 0;

        //DFFF2B
        String cajeroUsando = ASCIIhex(cajero_code);
        int cajeroT = (cajeroUsando.length())/2;
        String cajeroThex = Integer.toHexString(cajeroT);
        if(cajeroThex.length() == 1)
            cajeroThex = "0"+cajeroThex;
        cajeroUsando = "DFFF2B"+cajeroThex+cajeroUsando;

        //DFFF25
        String cajaUsanda = ASCIIhex(caja);
        int cajaT = (cajaUsanda.length())/2;
        String cajaThex = Integer.toHexString(cajaT);
        if(cajaThex.length() == 1)
            cajaThex = "0"+cajaThex;
        cajaUsanda = "DFFF25"+cajaThex+cajaUsanda;

        //FC03
        String code = "DC010F"+cajeroUsando+cajaUsanda;
        int codeT = (code.length())/2;
        String codeThex = Integer.toHexString(codeT);
        if(codeThex.length() == 1)
            codeThex = "0"+codeThex;
        code = "FC"+codeThex+code;

        LogFile.adjuntarLog("TEF Cierre Manual: "+code);

        byte[] extDataIn = null;
        try {
            byte[] tmp = code.getBytes("ISO-8859-1");

            extDataIn = new byte[tmp.length/2];
            int j=0;
            for (int i =0; i<tmp.length; i++)
            {
                if (tmp[i] >= 'a' && tmp[i] <= 'f')
                    tmp[i] = (byte) (tmp[i] - 'a' + 10);
                else if (tmp[i] >= 'A' && tmp[i] <= 'F')
                    tmp[i] = (byte) (tmp[i] - 'A' + 10);
                else if (tmp[i] >= '0' && tmp[i] <= '9')
                    tmp[i] = (byte) (tmp[i] - '0');
            }
            for (int i=0; i<tmp.length; i+=2)
            {
                String str = String.format("%02x", tmp[i]*16 + tmp[i+1]);
                extDataIn[j++] = (byte) Integer.parseInt(str, 16);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] extDataOut = new byte[5000];

        TransactionIn transIn = new TransactionIn();
        TransactionOut transOut = new TransactionOut();
        new DoTransactionExTask(transIn, transOut, appNumber, extDataIn, extDataOut).execute();
    }

    private void iniciarCierre() {
        pb.setVisibility(View.VISIBLE);

        int appNumber;
        appNumber = 0;

        //DFFF2B
        String cajeroUsando = ASCIIhex(cajero_code);
        int cajeroT = (cajeroUsando.length())/2;
        String cajeroThex = Integer.toHexString(cajeroT);
        if(cajeroThex.length() == 1)
            cajeroThex = "0"+cajeroThex;
        cajeroUsando = "DFFF2B"+cajeroThex+cajeroUsando;

        //DFFF25
        String cajaUsanda = ASCIIhex(caja);
        int cajaT = (cajaUsanda.length())/2;
        String cajaThex = Integer.toHexString(cajaT);
        if(cajaThex.length() == 1)
            cajaThex = "0"+cajaThex;
        cajaUsanda = "DFFF25"+cajaThex+cajaUsanda;

        //FC03
        String code = "DC0125"+cajeroUsando+cajaUsanda;
        int codeT = (code.length())/2;
        String codeThex = Integer.toHexString(codeT);
        if(codeThex.length() == 1)
            codeThex = "0"+codeThex;
        code = "FC"+codeThex+code;

        LogFile.adjuntarLog("TEF Cierre integrado: "+code);

        byte[] extDataIn = null;
        try {
            byte[] tmp = code.getBytes("ISO-8859-1");

            extDataIn = new byte[tmp.length/2];
            int j=0;
            for (int i =0; i<tmp.length; i++)
            {
                if (tmp[i] >= 'a' && tmp[i] <= 'f')
                    tmp[i] = (byte) (tmp[i] - 'a' + 10);
                else if (tmp[i] >= 'A' && tmp[i] <= 'F')
                    tmp[i] = (byte) (tmp[i] - 'A' + 10);
                else if (tmp[i] >= '0' && tmp[i] <= '9')
                    tmp[i] = (byte) (tmp[i] - '0');
            }
            for (int i=0; i<tmp.length; i+=2)
            {
                String str = String.format("%02x", tmp[i]*16 + tmp[i+1]);
                extDataIn[j++] = (byte) Integer.parseInt(str, 16);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] extDataOut = new byte[5000];

        TransactionIn transIn = new TransactionIn();
        TransactionOut transOut = new TransactionOut();
        new DoTransactionExTask(transIn, transOut, appNumber, extDataIn, extDataOut).execute();
    }

    private void realizarCompra() {
        if(etmonto.getText().toString().length() ==0 || etmonto.getText().toString().length()> 9){
            msjToast(getResources().getString(R.string.importe_invalido));
            enproceso = false;
        }else{
            String montoentrans = etmonto.getText().toString();
            if(Integer.parseInt(montoentrans) > deuda.intValue()){
                msjToast(getResources().getString(R.string.importe_no_mayor_deuda));
                etmonto.setText("");
                enproceso = false;
            }else{
                int monto = Integer.parseInt(montoentrans);
                if(monto>0){

                    pb.setVisibility(View.VISIBLE);
                    btnaccion.setEnabled(false);

                    int appNumber = 0;
                    int ivacompra = iva.intValue();
                    int total = totalCompra.intValue();

                    if(ivacompra > 0){
                        if(total != monto){
                            ivacompra = Math.round((float)(iva*monto)/total);
                        }
                    }

                    //DFFE4006000000000000
                    String montoUsando = monto+"00";
                    int fin = 12-montoUsando.length();
                    for(int i=0; i<fin; i++){
                        montoUsando = "0"+montoUsando;
                    }
                    montoUsando = "DFFE4006"+montoUsando;

                    //DFFF2206000000000000c
                    String ivaUsando = ivacompra+"00";
                    fin = 12-ivaUsando.length();
                    for(int i=0; i<fin; i++){
                        ivaUsando = "0"+ivaUsando;
                    }
                    ivaUsando = "DFFF2206"+ivaUsando;

                    //DFFF2B
                    String cajeroUsando = ASCIIhex(cajero_code);
                    int cajeroT = (cajeroUsando.length())/2;
                    String cajeroThex = Integer.toHexString(cajeroT);
                    if(cajeroThex.length() == 1)
                        cajeroThex = "0"+cajeroThex;
                    cajeroUsando = "DFFF2B"+cajeroThex+cajeroUsando;

                    //DFFF25
                    String cajaUsanda = ASCIIhex(caja);
                    int cajaT = (cajaUsanda.length())/2;
                    String cajaThex = Integer.toHexString(cajaT);
                    if(cajaThex.length() == 1)
                        cajaThex = "0"+cajaThex;
                    cajaUsanda = "DFFF25"+cajaThex+cajaUsanda;

                    //FC03
                    String code = "DC010A"+montoUsando+ivaUsando+cajeroUsando+cajaUsanda+"dfff2306000000000000";
                    int codeT = (code.length())/2;
                    String codeThex = Integer.toHexString(codeT);
                    if(codeThex.length() == 1)
                        codeThex = "0"+codeThex;
                    code = "FC"+codeThex+code;

                    LogFile.adjuntarLog("TEF Compra: "+code);

                    byte[] extDataIn = null;
                    try {
                        byte[] tmp = code.getBytes("ISO-8859-1");

                        extDataIn = new byte[tmp.length/2];
                        int j=0;
                        for (int i =0; i<tmp.length; i++)
                        {
                            if (tmp[i] >= 'a' && tmp[i] <= 'f')
                                tmp[i] = (byte) (tmp[i] - 'a' + 10);
                            else if (tmp[i] >= 'A' && tmp[i] <= 'F')
                                tmp[i] = (byte) (tmp[i] - 'A' + 10);
                            else if (tmp[i] >= '0' && tmp[i] <= '9')
                                tmp[i] = (byte) (tmp[i] - '0');
                        }
                        for (int i=0; i<tmp.length; i+=2)
                        {
                            String str = String.format("%02x", tmp[i]*16 + tmp[i+1]);
                            extDataIn[j++] = (byte) Integer.parseInt(str, 16);
                        }

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    byte[] extDataOut = new byte[5000];

                    TransactionIn transIn = new TransactionIn();
                    TransactionOut transOut = new TransactionOut();

                    transIn.setOperation("C");
                    transIn.setTermNum("58");
                    transIn.setCurrencyCode("170");

                    mensajeSimpleDialog("Datafono","Inserte la tarjeta por favor");
                    new DoTransactionExTask(transIn, transOut, appNumber, extDataIn, extDataOut).execute();
                }else{
                    msjToast(getResources().getString(R.string.importe_no_0));
                    etmonto.setText("");
                    enproceso = false;
                }
            }
        }
    }

    @Override
    protected void onResume() {
        refreshTerminalsList("onResume");
        if (rg.getCheckedRadioButtonId() == -1) {
            mRestart = false;
        } else {
            mRestart = true;
        }
        // Start PclService even if no companion is selected - to test TPCL-750
        rg.setOnCheckedChangeListener(this);
        startPclService();
        initService();
        mReleaseService = 1;
        if (isCompanionConnected()) {
            new GetTermInfoTask().execute();
            tvestadoconexion.setText(R.string.str_connected);
            tvestadoconexion.setBackgroundColor(Color.GREEN);
            tvestadoconexion.setTextColor(Color.BLACK);
            pb.setVisibility(View.GONE);
            btnaccion.setEnabled(true);
        } else {
            tvestadoconexion.setText(R.string.str_not_connected);
            tvestadoconexion.setBackgroundColor(Color.RED);
            tvestadoconexion.setTextColor(Color.BLACK);
            pb.setVisibility(View.VISIBLE);
        }
        super.onResume();
    }

    @Override
    public void onBackPressed(){
        if(enproceso){
            msjToast("finalice la transacción");
        }else{
            finish();
        }
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        mReleaseService = 0;
        CharSequence cs;
        int id = rg.getCheckedRadioButtonId();
        if (id == -1)
            cs = "";
        else
            cs = ((RadioButton)(rg.getChildAt(id))).getText();
        return cs;
    }

    class DoTransactionExTask extends AsyncTask<Void, Void, Boolean> {
        private TransactionIn transIn;
        private TransactionOut transOut;
        private byte[] extDataIn;
        private byte[] extDataOut;
        private long[] extDataOutSize;
        private int appNumber;
        public DoTransactionExTask(TransactionIn transIn, TransactionOut transOut, int appNumber, byte[] extDataIn, byte[] extDataOut) {
            this.transIn = transIn;
            this.transOut = transOut;
            this.extDataIn = extDataIn;
            this.extDataOut = extDataOut;
            this.appNumber = appNumber;
            this.extDataOutSize = new long[1];
            this.extDataOutSize[0] = extDataOut.length;
        }

        protected Boolean doInBackground(Void... tmp) {
            return doTransactionEx(transIn, transOut, appNumber, extDataIn, extDataIn.length, extDataOut, extDataOutSize);
        }

        protected void onPostExecute(Boolean result) {
            enproceso = false;
            tvestado.setText("Estado: "+result);
            mTestEndTime = System.currentTimeMillis();
            tvduracion.setText("Duración:" + String.format("%d.%03ds", (mTestEndTime-mTestStartTime)/1000, (mTestEndTime-mTestStartTime)%1000));
            btnaccion.setEnabled(true);
            String strResult = "N/A";
            if (result == true) {
                String strExtDataOut;
                // Transform extDataOut in hex
                StringBuffer hex = new StringBuffer();
                String str;
                for(int i = 0; i < extDataOutSize[0]; i++){
                    str = Integer.toHexString((int)extDataOut[i]);
                    if (str.length() > 2)
                        str = str.substring(str.length()-2, str.length());
                    else if (str.length() == 1)
                        str = "0" + str;
                    hex.append(str);
                }
                strExtDataOut = hex.toString();
                LogFile.adjuntarLog("TEF Respuesta: "+strExtDataOut);

                //Acciones dependiendo del tipo de opracion
                switch (tipoOperacion) {
                    case "compra":
                        procesarPago(transOut,strExtDataOut);
                        break;
                    case "cierre":
                        procesarCierre(transOut,strExtDataOut);
                        break;
                    case "ultimaTransaccion":
                        procesarUltimaTransaccion(transOut,strExtDataOut);
                        break;
                    case "anular":
                        procesarAnulacionTransaccion(transOut,strExtDataOut);
                        break;
                    case "bonoRegalo":
                        procesarBonoRegalo(transOut,strExtDataOut);
                        break;
                }

                pb.setVisibility(View.GONE);
            }
            else {
                tvresultadoterminal.setText(strResult);
                tvresultado.setText(getResources().getString(R.string.sin_resultado));
            }
        }
    }

    private void procesarBonoRegalo(TransactionOut transOut, String strExtDataOut) {

        if (transOut.getC3Error().equals(TransactionOut.ErrorCode.SUCCESS.toString())) {

            //Obtener informacion de la transacción
            extraerDatos(strExtDataOut);

            tvresultado.setText(getResources().getString(R.string.trans_aprobada));
            tvresultado.setTextColor(Color.rgb(0,168,65));
            tvresultadoterminal.setText("Tipo de Operación: " + idDC +
                    "\nMonto: " + montodffe40 +
                    "\nIVA: " + ivadfff22 +
                    "\nIAC: " + iacdfff23 +
                    "\nFecha: " + fecha9a +
                    "\nHora: " + hora9f21 +
                    "\nNumero de Recibo: " + numRecibodfff29 +
                    "\nBIN :" + bin42 +
                    "\nCódigo de Aprobación: " + codAprobaciondfff2d +
                    "\nCódigo de Respuesta: " + codRtadfff02 +
                    "\nDir. Establecimiento: " + dirEstabledffe78 +
                    "\nFecha Ven. Tarjeta: " + fechaVenTarjdffe76 +
                    "\nFranquicia: " + franquiciadffe49 +
                    "\nPropina: " + propinadfff21 +
                    "\nCriptograma: " + criptograma9f26 +
                    "\nTVR: " + tvr95 +
                    "\nTSI: " + tsi9b +
                    "\nAID: " + criptograma9f26 +
                    "\nHash: " + hora9f21 +
                    "\nIDCliente: " + idClientedffe02 +
                    "\nCuotas: " + cuotasdfff26 +
                    "\nCódigo Terminal: " + codTerminaldffe45 +
                    "\nRRN: " + rrndfff14 +
                    "\nCodigo del Comercio: " + codigoComerciodffe77 +
                    "\nUltm. Digitos Tarjeta: " + ultDigitoTarjdffe54 +
                    "\nTipo deCuenta: " + tipoCuentadffe50);

            //Habilitar la re-impresion del baucher de bono regalo
            btnimprimir.setEnabled(true);

            Intent i = new Intent(DatafonoActivity.this, ImpresionSumniActivity.class);
            i.putExtra("lablePrint", (Serializable) "bonoregalo");
            i.putExtra("respDatafono", (Serializable) datafonoEntity);
            startActivity(i);
        }
        else {
            tvresultado.setText(getResources().getString(R.string.trans_fallida));
            tvresultado.setTextColor(Color.RED);
            tvresultadoterminal.setText("Amount: " + transOut.getAmount() +
                    "\nCurrency: " + transOut.getCurrencyCode() +
                    "\nC3Error: " + transOut.getC3Error() +
                    "\nTermNum: " + transOut.getTerminalNumber() +
                    "\nUserData: " + transOut.getUserData() +
                    "\nFFU: " + transOut.getFFU() +
                    "\nLength: " + transOut.getLength());
        }
    }

    private void procesarAnulacionTransaccion(TransactionOut transOut, String strExtDataOut) {

        if (transOut.getC3Error().equals(TransactionOut.ErrorCode.SUCCESS.toString())) {

            //Obtener informacion de la transacción
            extraerDatos(strExtDataOut);

            tvresultado.setText(getResources().getString(R.string.trans_aprobada));
            tvresultado.setTextColor(Color.rgb(0,168,65));
            String strResult = getResources().getString(R.string.anulacion_realizada_datafono);
            tvresultadoterminal.setText(strResult +
                    "\nTipo de Operación: " + idDC +
                    "\nNumero de Recibo: " + numRecibodfff29 +
                    "\nMonto: " + montodffe40 +
                    "\nIVA: " + ivadfff22 +
                    "\nIAC: " + iacdfff23 +
                    "\nFecha:" + fecha9a +
                    "\nHora:" + hora9f21 +
                    "\nBIN :" + bin42 +
                    "\nCódigo de Aprobación: " + codAprobaciondfff2d +
                    "\nCódigo de Respuesta: " + codRtadfff02 +
                    "\nDir. Establecimiento: " + dirEstabledffe78 +
                    "\nFecha Ven. Tarjeta: " + fechaVenTarjdffe76 +
                    "\nFranquicia: " + franquiciadffe49 +
                    "\nPropina: " + propinadfff21 +
                    "\nCriptograma: " + criptograma9f26 +
                    "\nTVR: " + tvr95 +
                    "\nTSI: " + tsi9b +
                    "\nAID: " + criptograma9f26 +
                    "\nHash: " + hora9f21 +
                    "\nIDCliente: " + idClientedffe02 +
                    "\nCuotas: " + cuotasdfff26 +
                    "\nCódigo Terminal: " + codTerminaldffe45 +
                    "\nRRN: " + rrndfff14 +
                    "\nCodigo del Comercio: " + codigoComerciodffe77 +
                    "\nUltm. Digitos Tarjeta: " + ultDigitoTarjdffe54 +
                    "\nTipo deCuenta: " + tipoCuentadffe50);

            //Habilitar la re-impresion del baucher de Anulacion
            btnimprimir.setEnabled(true);

            Intent i = new Intent(DatafonoActivity.this, ImpresionSumniActivity.class);
            i.putExtra("lablePrint", (Serializable) "anulacion");
            i.putExtra("respDatafono", (Serializable) datafonoEntity);
            startActivity(i);

        }
        else {
            tvresultado.setText(getResources().getString(R.string.trans_fallida));
            tvresultado.setTextColor(Color.RED);
            tvresultadoterminal.setText("Amount: " + transOut.getAmount() +
                    "\nCurrency: " + transOut.getCurrencyCode() +
                    "\nC3Error: " + transOut.getC3Error() +
                    "\nTermNum: " + transOut.getTerminalNumber() +
                    "\nUserData: " + transOut.getUserData() +
                    "\nFFU: " + transOut.getFFU() +
                    "\nLength: " + transOut.getLength());
        }
    }

    private void procesarUltimaTransaccion(TransactionOut transOut, String strExtDataOut) {

        if (transOut.getC3Error().equals(TransactionOut.ErrorCode.SUCCESS.toString())) {

            //Obtener informacion de la transacción
            extraerDatos(strExtDataOut);

            String strResult = getResources().getString(R.string.titulo_utltrans_datafono);
            codRtadfff02 = codRtadfff02.trim();
            if(codRtadfff02.equals("00")){
                tvresultado.setText(getResources().getString(R.string.trans_aprobada));
                tvresultado.setTextColor(Color.rgb(0,168,65));
                //Habilitar la re-impresion de la ultima operacion
                btnimprimir.setEnabled(true);
            }else{
                if(codRtadfff02.equals("99")){
                    tvresultado.setText(getResources().getString(R.string.trans_sin_respuesta));
                    labelUlt = "";
                }else{
                    tvresultado.setText(getResources().getString(R.string.trans_fallida));
                    tvresultado.setTextColor(Color.RED);
                    labelUlt = "";
                }
            }
            tvresultadoterminal.setText(strResult +
                    "\nCódigo de Aprobación: " + codAprobaciondfff2d +
                    "\nTipo de Operación: " + idDC +
                    "\nMonto: " + montodffe40 +
                    "\nIVA: " + ivadfff22 +
                    "\nIAC: " + iacdfff23 +
                    "\nNumero de Recibo: " + numRecibodfff29 +
                    "\nFecha: " + fecha9a +
                    "\nHora: " + hora9f21 +
                    "\nBIN :" + bin42 +
                    "\nCódigo de Respuesta: " + codRtadfff02 +
                    "\nDir. Establecimiento: " + dirEstabledffe78 +
                    "\nFecha Ven. Tarjeta: " + fechaVenTarjdffe76 +
                    "\nFranquicia: " + franquiciadffe49 +
                    "\nPropina: " + propinadfff21 +
                    "\nCriptograma: " + criptograma9f26 +
                    "\nTVR: " + tvr95 +
                    "\nTSI: " + tsi9b +
                    "\nAID: " + criptograma9f26 +
                    "\nHash: " + hora9f21 +
                    "\nIDCliente: " + idClientedffe02 +
                    "\nCuotas: " + cuotasdfff26 +
                    "\nCódigo Terminal: " + codTerminaldffe45 +
                    "\nRRN: " + rrndfff14 +
                    "\nCodigo del Comercio: " + codigoComerciodffe77 +
                    "\nUltm. Digitos Tarjeta: " + ultDigitoTarjdffe54 +
                    "\nTipo deCuenta: " + tipoCuentadffe50);
        }
        else {
            tvresultado.setText(getResources().getString(R.string.trans_fallida));
            tvresultado.setTextColor(Color.RED);
            String strResult = getResources().getString(R.string.sin_transacciones_recibir);
            tvresultadoterminal.setText(strResult +
                    "\nAmount: " + transOut.getAmount() +
                    "\nCurrency: " + transOut.getCurrencyCode() +
                    "\nC3Error: " + transOut.getC3Error() +
                    "\nTermNum: " + transOut.getTerminalNumber() +
                    "\nUserData: " + transOut.getUserData() +
                    "\nFFU: " + transOut.getFFU() +
                    "\nLength: " + transOut.getLength());
        }
    }

    private void procesarPago(TransactionOut transOut, String strExtDataOut) {

        if (transOut.getC3Error().equals(TransactionOut.ErrorCode.SUCCESS.toString())) {

            //Obtener informacion de la transacción
            extraerDatos(strExtDataOut);

            tvresultado.setText(getResources().getString(R.string.trans_aprobada));
            tvresultado.setTextColor(Color.rgb(0,168,65));
            tvresultadoterminal.setText("Tipo de Operación: " + idDC +
                    "\nMonto: " + montodffe40 +
                    "\nIVA: " + ivadfff22 +
                    "\nIAC: " + iacdfff23 +
                    "\nFecha: " + fecha9a +
                    "\nHora: " + hora9f21 +
                    "\nNumero de Recibo: " + numRecibodfff29 +
                    "\nBIN :" + bin42 +
                    "\nCódigo de Aprobación: " + codAprobaciondfff2d +
                    "\nCódigo de Respuesta: " + codRtadfff02 +
                    "\nDir. Establecimiento: " + dirEstabledffe78 +
                    "\nFecha Ven. Tarjeta: " + fechaVenTarjdffe76 +
                    "\nFranquicia: " + franquiciadffe49 +
                    "\nPropina: " + propinadfff21 +
                    "\nCriptograma: " + criptograma9f26 +
                    "\nTVR: " + tvr95 +
                    "\nTSI: " + tsi9b +
                    "\nAID: " + criptograma9f26 +
                    "\nHash: " + hora9f21 +
                    "\nIDCliente: " + idClientedffe02 +
                    "\nCuotas: " + cuotasdfff26 +
                    "\nCódigo Terminal: " + codTerminaldffe45 +
                    "\nRRN: " + rrndfff14 +
                    "\nCodigo del Comercio: " + codigoComerciodffe77 +
                    "\nUltm. Digitos Tarjeta: " + ultDigitoTarjdffe54 +
                    "\nTipo deCuenta: " + tipoCuentadffe50);

            //crear el medio de pago tef para los importes con la respuesta del datafono
            Payment pagoDatafono = new Payment(mediopago.getNombre(), datafonoEntity.getMonto(), mediopago.getDivisa(), "", 0, 1, mediopago.getCodigo());
            pagoDatafono.setTEF(true);
            pagoDatafono.setDatafonoEntity(datafonoEntity);
            Intent intent= new Intent();
            intent.putExtra("pagoDatafono", (Serializable) pagoDatafono);
            //devolver el objeto pagoDatafono
            setResult(Constantes.RESP_TEF_VENTA,intent);
            finish();
        } else {
            tvresultado.setText(getResources().getString(R.string.trans_fallida));
            tvresultado.setTextColor(Color.RED);
            tvresultadoterminal.setText("Amount: " + transOut.getAmount() +
                    "\nCurrency: " + transOut.getCurrencyCode() +
                    "\nC3Error: " + transOut.getC3Error() +
                    "\nTermNum: " + transOut.getTerminalNumber() +
                    "\nUserData: " + transOut.getUserData() +
                    "\nFFU: " + transOut.getFFU() +
                    "\nLength: " + transOut.getLength());
        }
    }

    private void procesarCierre(TransactionOut transOut, String strExtDataOut) {
        String strResult;
        if (transOut.getC3Error().equals(TransactionOut.ErrorCode.SUCCESS.toString())) {
            if (strExtDataOut.length() > 50 && esIniciarCierre) {
                extraerDatos(strExtDataOut);
                tvresultado.setText(getResources().getString(R.string.resultado));
                strResult = getResources().getString(R.string.trans_aprobada);
                tvresultado.setTextColor(Color.rgb(0,168,65));
                tvresultadoterminal.setText(strResult +
                        "\nDFFE0D: " + finCierreDFFE0D +
                        "\nTipo de Operación: " + idDC +
                        "\nNumero de Recibo: " + numRecibodfff29 +
                        "\nFecha: " + fecha9a +
                        "\nHora: " + hora9f21 +
                        "\nMonto: " + montodffe40 +
                        "\nIVA: " + ivadfff22 +
                        "\nIAC: " + iacdfff23 +
                        "\nBIN :" + bin42 +
                        "\nCódigo de Aprobación: " + codAprobaciondfff2d +
                        "\nCódigo de Respuesta: " + codRtadfff02 +
                        "\nDir. Establecimiento: " + dirEstabledffe78 +
                        "\nFecha Ven. Tarjeta: " + fechaVenTarjdffe76 +
                        "\nFranquicia: " + franquiciadffe49 +
                        "\nPropina: " + propinadfff21 +
                        "\nCriptograma: " + criptograma9f26 +
                        "\nTVR: " + tvr95 +
                        "\nTSI: " + tsi9b +
                        "\nAID: " + criptograma9f26 +
                        "\nHash: " + hora9f21 +
                        "\nIDCliente: " + idClientedffe02 +
                        "\nCuotas: " + cuotasdfff26 +
                        "\nCódigo Terminal: " + codTerminaldffe45 +
                        "\nRRN: " + rrndfff14 +
                        "\nCodigo del Comercio: " + codigoComerciodffe77 +
                        "\nUltm. Digitos Tarjeta: " + ultDigitoTarjdffe54 +
                        "\nTipo deCuenta: " + tipoCuentadffe50);
            }else{
                if(esIniciarCierre){
                    tvresultado.setText(R.string.sin_transacciones_recibir);
                }else{
                    tvresultado.setText(R.string.cierre_datafono_finalizado);
                    try{
                        new TruncateAsyncTask().execute().get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Log.e("logcat",e.getMessage());
                    } catch (ExecutionException e) {
                        Log.e("logcat",e.getMessage());
                    }
                    finish();
                }
                tvresultado.setTextColor(Color.RED);
                tvresultadoterminal.setText("Amount: " + transOut.getAmount() +
                        "\nCurrency: " + transOut.getCurrencyCode() +
                        "\nC3Error: " + transOut.getC3Error() +
                        "\nTermNum: " + transOut.getTerminalNumber() +
                        "\nUserData: " + transOut.getUserData() +
                        "\nExtData: " + strExtDataOut);
            }
        }else {
            tvresultado.setText(getResources().getString(R.string.trans_fallida));
            tvresultado.setTextColor(Color.RED);
            tvresultadoterminal.setText("Amount: " + transOut.getAmount() +
                    "\nCurrency: " + transOut.getCurrencyCode() +
                    "\nC3Error: " + transOut.getC3Error() +
                    "\nTermNum: " + transOut.getTerminalNumber() +
                    "\nUserData: " + transOut.getUserData() +
                    "\nExtData: " + strExtDataOut);
        }
        if(esIniciarCierre)
            if (finCierreDFFE0D != null)
                if (Integer.parseInt(finCierreDFFE0D) > 0) {
                    btnaccion.callOnClick();
                }else{
                    btnaccion.setVisibility(View.GONE);
                    btnimprtotales.setEnabled(true);
                    bntimprdetallado.setEnabled(true);
                    esIniciarCierre = false;

                    try{
                        new DetalladoAsyncTask().execute().get();
                        new TotalesAsyncTask().execute().get();
                        new TotalesCanceladasAsyncTask().execute().get();
                        new EliminarAsyncTask().execute().get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Log.e("logcat",e.getMessage());
                    } catch (ExecutionException e) {
                        Log.e("logcat",e.getMessage());
                    }
                }
    }

    protected class EliminarAsyncTask extends AsyncTask<Void,Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            BazarPosMovilDB.getBD(getApplication()).datafonoDao().delete_cierre();
            return null;
        }
    }

    protected class DetalladoAsyncTask extends AsyncTask<Void,Void, List<DatafonoEntity>> {

        @Override
        protected List<DatafonoEntity> doInBackground(Void... voids) {
            return BazarPosMovilDB.getBD(getApplication()).datafonoDao().getDetallado();
        }

        @Override
        protected void onPostExecute(List<DatafonoEntity> datafonoEntityList) {
            Log.e("logcat",datafonoEntityList.toString());
            datafonoDetalladoList.addAll(datafonoEntityList);
        }
    }

    protected class TotalesAsyncTask extends AsyncTask<Void,Void, List<DatafonoTotales>> {

        @Override
        protected List<DatafonoTotales> doInBackground(Void... voids) {
            return BazarPosMovilDB.getBD(getApplication()).datafonoDao().getTotales();
        }

        @Override
        protected void onPostExecute(List<DatafonoTotales> datafonoTotalesList) {
            datafonoTotalesList.addAll(datafonoTotalesList);
        }
    }

    protected class TotalesCanceladasAsyncTask extends AsyncTask<Void,Void, List<DatafonoTotales>> {

        @Override
        protected List<DatafonoTotales> doInBackground(Void... voids) {
            return BazarPosMovilDB.getBD(getApplication()).datafonoDao().getTotalesCanceladas();
        }

        @Override
        protected void onPostExecute(List<DatafonoTotales> datafonoTotalesList) {
            datafonoTotalesCanList.addAll(datafonoTotalesList);
        }
    }

    protected class TruncateAsyncTask extends AsyncTask<Void,Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            BazarPosMovilDB.getBD(getApplication()).datafonoDao().trucate();
            return null;
        }
    }

    class GetTermInfoTask extends AsyncTask<Void, Void, Boolean> {
        protected Boolean doInBackground(Void... tmp) {
            Boolean bRet = getTermInfo();
            return bRet;
        }

        protected void onPostExecute(Boolean result) {
            if (result == true)
            {

            }
        }
    }

    class RestartServiceTask extends AsyncTask<Void, Void, Boolean> {
        protected Boolean doInBackground(Void... tmp) {
            releaseService();
            stopPclService();
            startPclService();
            initService();
            return true;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        Log.d(TAG, String.format("onCheckedChanged id=%d", checkedId));
        if (checkedId != -1) {
            RadioButton rb = (RadioButton) group.getChildAt(checkedId);
            if (rb != null) {

                tvestadoconexion.setBackgroundColor(Color.WHITE);
                tvestadoconexion.setTextColor(Color.WHITE);
                pb.setVisibility(View.VISIBLE);
                btnaccion.setEnabled(false);

                Log.d(TAG, String.format("onCheckedChanged id=%d text=%s tmpName=%s", checkedId, rb.getText(), mCurrentDevice));
                if (!rb.getText().equals(mCurrentDevice)) {
                    Log.d(TAG, String.format("current:%s saved:%s", rb.getText(), mCurrentDevice));

                    mCurrentDevice = rb.getText();
                    if (mCurrentDevice.toString().equals(getString(R.string.use_direct_connect))) {
                        IpTerminal terminal = mPclUtil.new IpTerminal("", "", "255.255.255.255", 0);

                        terminal.setSsl(0);

                        mPclUtil.activateIpTerminal(terminal);
                    } else if (rb.getTag() != null && rb.getTag().toString().equalsIgnoreCase("ip")) {
                        String[] terminalNameTab = mCurrentDevice.toString().split(" - ");
                        if (terminalNameTab.length > 0) {

                            for (IpTerminal terminal : terminals) {

                                if (terminal.getName().equalsIgnoreCase(terminalNameTab[1].trim())) {
                                    mPclUtil.activateIpTerminal(terminal);
                                } else {
                                    terminal.setActivated(false);
                                }
                            }

                        }
                    } else if (mCurrentDevice.charAt(2) == ':') {
                        mPclUtil.ActivateCompanion(((String) mCurrentDevice).substring(0, 17));
                    }
                    else if (((String) mCurrentDevice).startsWith("/dev/")) {
                        String[] terminalNameTab = mCurrentDevice.toString().split(" ");
                        if (terminalNameTab.length == 2) {
                            Log.d(TAG, "Activate SerialPortCompanion" + mCurrentDevice);
                            mPclUtil.activateSerialPortCompanion(terminalNameTab[0], terminalNameTab[1]);
                        }
                    } else {
                        mPclUtil.activateUsbCompanion((String) mCurrentDevice);
                    }

                    // Restart the service
                    if (mRestart) {
                        Log.d(TAG,"Restart PCL Service");
                        RestartServiceTask restartTask = new RestartServiceTask();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            restartTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } else {
                            restartTask.execute();
                        }
                    }
                } else {
                    Log.d(TAG, "onCheckedChanged Allready selected");
                }

            }
            mRestart = true;
        }
        Log.d(TAG, "onCheckedChanged EXIT");
    }

    @Override
    public void onStateChanged(String state) {
        if (state.equals("CONNECTED"))
        {
            new GetTermInfoTask().execute();
            tvestadoconexion.setText(R.string.str_connected);
            tvestadoconexion.setBackgroundColor(Color.GREEN);
            tvestadoconexion.setTextColor(Color.BLACK);
            pb.setVisibility(View.GONE);
            btnaccion.setEnabled(true);
        } else {
            tvestadoconexion.setText(R.string.str_not_connected);
            tvestadoconexion.setBackgroundColor(Color.RED);
            tvestadoconexion.setTextColor(Color.BLACK);
            pb.setVisibility(View.VISIBLE);
            btnaccion.setEnabled(false);
        }
    }

    @Override
    public void onPclServiceConnected() {
        Log.d(TAG, "onPclServiceConnected");
        mPclService.addDynamicBridgeLocal(6000, 0);

        if (isCompanionConnected())
        {
            new GetTermInfoTask().execute();
            if (tvestadoconexion != null) {
                tvestadoconexion.setText(R.string.str_connected);
                tvestadoconexion.setBackgroundColor(Color.GREEN);
                tvestadoconexion.setTextColor(Color.BLACK);
                pb.setVisibility(View.GONE);
                btnaccion.setEnabled(true);
            }
        } else {
            if (tvestadoconexion != null) {
                tvestadoconexion.setText(R.string.str_not_connected);
                tvestadoconexion.setBackgroundColor(Color.RED);
                tvestadoconexion.setTextColor(Color.BLACK);
                pb.setVisibility(View.VISIBLE);
                btnaccion.setEnabled(false);
            }
        }

    }

    private void startPclService() {

        if (!mServiceStarted){
            SslObject sslObject = new SslObject("serverb.p12", "coucou");

            SharedPreferences settings = getSharedPreferences("PCLSERVICE", MODE_PRIVATE);
            boolean enableLog = settings.getBoolean("ENABLE_LOG", true);
            Intent i = new Intent(this, PclService.class);
            i.putExtra("PACKAGE_NAME", "com.crystal.bazarposmobile");
            i.putExtra("FILE_NAME", "pairing_addr.txt");
            i.putExtra("ENABLE_LOG", enableLog);
            i.putExtra("SSL_OBJECT", sslObject);
            if (getApplicationContext().startService(i) != null)
                mServiceStarted = true;
        }
    }

    protected void runReset() {
        tvestado.setText("");
        tvresultadoterminal.setText("");
        new ResetTerminalTask().execute(0);
    }

    class ResetTerminalTask extends AsyncTask<Integer, Void, Boolean> {
        protected Boolean doInBackground(Integer... info) {
            Boolean bRet = resetTerminal(info[0]);
            return bRet;
        }

        protected void onPostExecute(Boolean result) {
            tvestado.setText("Estado: "+result);
            mTestEndTime = System.currentTimeMillis();
            tvduracion.setText("Duración:" + String.format("%d.%03ds", (mTestEndTime-mTestStartTime)/1000, (mTestEndTime-mTestStartTime)%1000));

            if (result == true){

                tvresultadoterminal.setText(getResources().getString(R.string.ok));
                tvresultado.setText(getResources().getString(R.string.resultado));
            } else{
                tvresultadoterminal.setText(R.string.n_a);
                tvresultado.setText(getResources().getString(R.string.resultado));
            }
        }
    }

    private void stopPclService() {
        if (mServiceStarted){
            Intent i = new Intent(this, PclService.class);
            if (getApplicationContext().stopService(i))
                mServiceStarted = false;
        }
    }

    @Override
    public void onBarCodeReceived(String barCodeValue, int symbology) {
        // Do nothing
    }

    @Override
    public void onBarCodeClosed() {
        // Do nothing
    }

    void refreshTerminalsList(String string) {
        synchronized (rg) {
            Log.d(TAG, "refreshTerminalsList: " + string);
            rg.removeAllViewsInLayout();
            boolean bFound = false;

            terminalCounter = 0;
            Set<BluetoothCompanion> btComps = mPclUtil.GetPairedCompanions();
            if (btComps != null && (btComps.size() > 0)) {
                // Loop through paired devices
                for (BluetoothCompanion comp : btComps) {
                    Log.d(TAG, comp.getBluetoothDevice().getAddress());
                    RadioButton radioButton = new RadioButton(this);
                    radioButton.setText(comp.getBluetoothDevice().getAddress() + " - " + comp.getBluetoothDevice().getName());
                    radioButton.setId(terminalCounter);
                    if (comp.isActivated()) {
                        bFound = true;
                        radioButton.setChecked(true);
                        mCurrentDevice = comp.getBluetoothDevice().getAddress() + " - " + comp.getBluetoothDevice().getName();
                    } else {
                        radioButton.setChecked(false);
                    }
                    rg.addView(radioButton);
                    terminalCounter++;
                }
            }

            Set<UsbDevice> usbDevs = mPclUtil.getUsbDevices();
            if (usbDevs != null && (usbDevs.size() > 0)) {
                for (UsbDevice dev : usbDevs) {
                    Log.d(TAG, "refreshTerminalsList:" + dev.toString());
                    if (!mUsbManager.hasPermission(dev) && !mPermissionRequested && !mPermissionGranted) {
                        Log.d(TAG, "refreshTerminalsList: requestPermission");
                        mPermissionRequested = true;
                        mUsbManager.requestPermission(dev, mPermissionIntent);
                    } else {
                        UsbCompanion comp = mPclUtil.getUsbCompanion(dev);
                        if (comp != null) {
                            RadioButton radioButton = new RadioButton(this);
                            radioButton.setText(comp.getName());
                            radioButton.setId(terminalCounter);
                            if (comp.isActivated()) {
                                bFound = true;
                                radioButton.setChecked(true);
                                mCurrentDevice = comp.getName();
                            } else {
                                radioButton.setChecked(false);
                            }
                            rg.addView(radioButton);
                            terminalCounter++;
                        } else {
                            Log.d(TAG, "refreshTerminalsList: getUsbCompanion returns null");
                        }
                    }
                }
            }

            Set<String> serialDevices = mPclUtil.getSerialPortDevices();
            if (serialDevices != null && (serialDevices.size() > 0)) {
                for (String fileDev : serialDevices) {
                    // Our serial port terminal
                    if (fileDev.equals("/dev/ttyS3")) {
                        String tmpStr = "";
                        if (mCurrentDevice == null)
                            tmpStr = mPclUtil.getActivatedCompanion();
                        else
                            tmpStr = mCurrentDevice.toString();
                        // Test if already selected companion
                        if (tmpStr.startsWith("/dev/ttyS3")) {
                            RadioButton radioButton = new RadioButton(this);
                            radioButton.setText(tmpStr);
                            radioButton.setId(terminalCounter);
                            bFound = true;
                            radioButton.setChecked(true);
                            rg.addView(radioButton);
                            terminalCounter++;
                        } else {
                            SerialPortCompanion comp = mPclUtil.getSerialPortCompanion(fileDev);
                            if (comp != null) {

                                RadioButton radioButton = new RadioButton(this);
                                radioButton.setText(comp.toString());
                                radioButton.setId(terminalCounter);
                                if (comp.isActivated()) {
                                    bFound = true;
                                    radioButton.setChecked(true);
                                    mCurrentDevice = comp.toString();
                                } else {
                                    radioButton.setChecked(false);
                                }
                                rg.addView(radioButton);
                                terminalCounter++;
                                comp.getSerialPortDevice().close();
                            }
                        }
                        break;
                    }
                }
            } else {
                Log.d(TAG, "refreshTerminalsList: getSerialPortCompanions returns null or empty list");
            }
            terminals.clear();

            if (!bFound) {
                String act = mPclUtil.getActivatedCompanion();
                if ((act != null) && !act.isEmpty() && (act.charAt(2) != ':') && (!act.contains("_"))) {
                    RadioButton radioButton = new RadioButton(this);
                    radioButton.setText(act);
                    radioButton.setId(terminalCounter);
                    radioButton.setChecked(true);
                    mCurrentDevice = act;
                    rg.addView(radioButton);
                    terminalCounter++;
                } else if ((act != null) && !act.isEmpty() && act.contains("_")) {
                    String[] terminalIp = act.split("_");
                    if (!terminalIp[1].equals("255.255.255.255")) {
                        RadioButton radioButton = new RadioButton(this);
                        radioButton.setText(terminalIp[2] + " - " + terminalIp[0]);
                        radioButton.setId(terminalCounter);
                        radioButton.setTag("ip");
                        radioButton.setChecked(true);
                        sslActivated = terminalIp[3].equals("1");
                        mcbActivateSsl.setChecked(sslActivated);
                        mCurrentDevice = act;
                        rg.addView(radioButton);
                        IpTerminal term = mPclUtil.new IpTerminal(terminalIp[0], terminalIp[2], terminalIp[1], sslActivated ? 1 : 0);
                        term.activate();
                        terminals.add(term);
                        terminalCounter++;
                    } else {
                        ((RadioButton) rg.getChildAt(0)).setChecked(true);
                        mCurrentDevice = getString(R.string.use_direct_connect);
                    }
                }
            }

            GetIpTerminalsTask task = new GetIpTerminalsTask(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                task.execute();
            }
            Log.d(TAG, "END refreshTerminalsList: " + string);
        }
    }

    private class GetIpTerminalsTask extends AsyncTask<Object, IpTerminal, String> {

        Context context;

        public GetIpTerminalsTask(Context context) {
            super();
            this.context = context;
        }

        @Override
        protected String doInBackground(Object... params) {
            try {
                boolean bExist;
                for(int i = 0; i < 4; i++){
                    Set<IpTerminal> terminalsRetrived = mPclUtil.getIPTerminals();
                    for(IpTerminal term1: terminalsRetrived){
                        bExist = false;
                        for (IpTerminal term2: terminals){
                            if(term1.getMac().equals(term2.getMac())){
                                bExist = true;
                                break;
                            }
                        }
                        if (!bExist){
                            terminals.add(term1);
                            publishProgress(term1);
                        }
                    }
                }
                return null;
            } catch (MissingFormatArgumentException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(IpTerminal... values) {
            String activated = "";
            IpTerminal term = values[0];
            if(mCurrentDevice != null){
                String[] activatedtab = mCurrentDevice.toString().split("_");
                if(activatedtab.length > 2)
                    activated = activatedtab[2] + " - " + activatedtab[0];
            }
            Log.d(TAG, "GetIpTerminals: activated=" + activated);
            Log.d(TAG, "GetIpTerminals: term=" + term.getMac() + " - " + term.getName());
            if(!activated.equals(term.getMac() + " - " + term.getName())){
                RadioButton radioButton = new RadioButton(context);
                radioButton.setText(term.getMac() + " - " + term.getName());
                radioButton.setId(terminalCounter);
                radioButton.setTag("ip");
                if(term.isActivated()){
                    radioButton.setChecked(true);
                    mCurrentDevice = term.getMac() + " - " + term.getName();
                    String[] terminalIp = mPclUtil.getActivatedCompanion().split("_");
                    sslActivated = (terminalIp[3] == "1" ? true : false);
                    mcbActivateSsl.setChecked(sslActivated);
                }
                rg.addView(radioButton);
                terminalCounter++;
            }
        }
    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

        @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (device != null) {
                        if (PclUtilities.isIngenicoUsbDevice(device)) {
                            refreshTerminalsList("deviceAttached");
                        }
                    }
                }
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (device != null) {
                        if (PclUtilities.isIngenicoUsbDevice(device)) {
                            refreshTerminalsList("deviceDetached");
                        }
                    }
                }
            } else if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (PclUtilities.isIngenicoUsbDevice(device)) {
                        if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                            Log.i(TAG, "Permission granted for device\n" + device);
                            refreshTerminalsList("deviceGranted");
                            mPermissionGranted = true;
                        } else {
                            Log.w(TAG, "Permission refused for device\n" + device);
                        }
                        mPermissionRequested = false;
                    }

                }
            }
        }
    };

    private void extraerDatos(String extDataOut) {

        String infotrans,tvr95Text,tsi9bText,aid4fText,idDCinsert,
                iacdfff23Text,propinadfff21Text,montodffe40text,ivadfff22text;

        idDC = extraerTag(extDataOut,"dc");
        infotrans = cadenaSinTag(extDataOut,"dc");

        idDCinsert = idDC;
        labelUlt = "";

        switch (idDC) {
            case "0a":
                idDC = "Compra";
                idDCinsert = Constantes.COD_COMPRA;
                labelUlt = "CompTEF";
                break;
            case "02":
                idDC = "Anulación";
                idDCinsert = Constantes.COD_ANULACION;
                labelUlt = "anulacion";
                break;
            case "1f":
                idDC = "Bono Regalo";
                idDCinsert = Constantes.COD_BONO_REGALO;
                labelUlt = "bonoregalo";
                break;
        }

        bin42 = extraerTag("-"+infotrans,"42");
        infotrans = cadenaSinTag("-"+infotrans,"42");

        codAprobaciondfff2d = extraerTag("-"+infotrans,"dfff2d");
        infotrans = cadenaSinTag("-"+infotrans,"dfff2d");
        if(codAprobaciondfff2d.length()>0){
            codAprobaciondfff2d = hexASCII(codAprobaciondfff2d);
        }else{
            codAprobaciondfff2d = "";
        }

        codRtadfff02 = extraerTag("-"+infotrans,"dfff02");
        infotrans = cadenaSinTag("-"+infotrans,"dfff02");


        codigoComerciodffe77 = extraerTag("-"+infotrans,"dffe77");
        infotrans = cadenaSinTag("-"+infotrans,"dffe77");
        if(codigoComerciodffe77.length()>0){
            codigoComerciodffe77 = hexASCII(codigoComerciodffe77);
        }else{
            codigoComerciodffe77 = "";
        }

        dirEstabledffe78 = extraerTag("-"+infotrans,"dffe78");
        infotrans = cadenaSinTag("-"+infotrans,"dffe78");
        if(dirEstabledffe78.length()>0){
            dirEstabledffe78 = hexASCII(dirEstabledffe78);
        }else{
            dirEstabledffe78 = "";
        }

        fecha9a = extraerTag("-"+infotrans,"9a");
        infotrans = cadenaSinTag("-"+infotrans,"9a");

        fechaVenTarjdffe76 = extraerTag("-"+infotrans,"dffe76");
        infotrans = cadenaSinTag("-"+infotrans,"dffe76");

        franquiciadffe49 = extraerTag("-"+infotrans,"dffe49");
        infotrans = cadenaSinTag("-"+infotrans,"dffe49");
        if(franquiciadffe49.length()>0){
            franquiciadffe49 = hexASCII(franquiciadffe49);
        }else{
            franquiciadffe49 = "";
        }

        hora9f21 = extraerTag("-"+infotrans,"9f21");
        infotrans = cadenaSinTag("-"+infotrans,"9f21");

        montodffe40text = extraerTag("-"+infotrans,"dffe40");
        infotrans = cadenaSinTag("-"+infotrans,"dffe40");
        montodffe40 = Double.parseDouble(montodffe40text);
        if(montodffe40 > 0){
            montodffe40 = montodffe40/100;
        }

        ivadfff22text = extraerTag("-"+infotrans,"dfff22");
        infotrans = cadenaSinTag("-"+infotrans,"dfff22");
        ivadfff22 = Double.parseDouble(ivadfff22text);
        if(ivadfff22 > 0){
            ivadfff22 = ivadfff22/100;
        }

        iacdfff23Text = extraerTag("-"+infotrans,"dfff23");
        infotrans = cadenaSinTag("-"+infotrans,"dfff23");
        iacdfff23 = Double.parseDouble(iacdfff23Text);
        if(iacdfff23 > 0){
            iacdfff23 = iacdfff23/100;
        }

        propinadfff21Text = extraerTag("-"+infotrans,"dfff21");
        infotrans = cadenaSinTag("-"+infotrans,"dfff21");
        propinadfff21 = Double.parseDouble(propinadfff21Text);
        if(propinadfff21 > 0){
            propinadfff21 = propinadfff21/100;
        }

        criptograma9f26 = extraerTag("-"+infotrans,"9f26");
        infotrans = cadenaSinTag("-"+infotrans,"9f26");
        if(criptograma9f26.length()>0){
            BigInteger textBase = new BigInteger(criptograma9f26, 16);
            criptograma9f26 = textBase.toString();
        }else{
            criptograma9f26 = "";
        }


        tvr95Text = extraerTag("-"+infotrans,"95");
        infotrans = cadenaSinTag("-"+infotrans,"95");
        if(tvr95Text.length()>0){
            tvr95 = Long.parseLong(tvr95Text,16);
        }else{
            tvr95 = Long.valueOf(0);
        }

        tsi9bText = extraerTag("-"+infotrans,"9b");
        infotrans = cadenaSinTag("-"+infotrans,"9b");
        if(tsi9bText.length()>0){
            tsi9b = Long.parseLong(tsi9bText,16);
        }else{
            tsi9b = Long.valueOf(0);
        }


        aid4fText = extraerTag("-"+infotrans,"4f");
        infotrans = cadenaSinTag("-"+infotrans,"4f");
        if(aid4fText.length()>0){
            BigInteger aid4fBase = new BigInteger(aid4fText, 16);
            aid4f = aid4fBase.toString();
        }else{
            aid4f = "0";
        }

        hashdffe01 = extraerTag("-"+infotrans,"dffe01");
        infotrans = cadenaSinTag("-"+infotrans,"dffe01");
        if(hashdffe01.length()>0){
            hashdffe01 = hexASCII(hashdffe01);
        }else{
            hashdffe01 = "";
        }

        idClientedffe02 = extraerTag("-"+infotrans,"dffe02");
        infotrans = cadenaSinTag("-"+infotrans,"dffe02");
        if(idClientedffe02.length()>0){
            idClientedffe02 = hexASCII(idClientedffe02);
        }else{
            idClientedffe02 = "";
        }

        cuotasdfff26 = extraerTag("-"+infotrans,"dfff26");
        infotrans = cadenaSinTag("-"+infotrans,"dfff26");

        numRecibodfff29 = extraerTag("-"+infotrans,"dfff29");
        infotrans = cadenaSinTag("-"+infotrans,"dfff29");

        codTerminaldffe45 = extraerTag("-"+infotrans,"dffe45");
        infotrans = cadenaSinTag("-"+infotrans,"dffe45");
        if(codTerminaldffe45.length()>0){
            codTerminaldffe45 = hexASCII(codTerminaldffe45);
        }else{
            codTerminaldffe45 = "";
        }

        rrndfff14 = extraerTag("-"+infotrans,"dfff14");
        infotrans = cadenaSinTag("-"+infotrans,"dfff14");
        if(rrndfff14.length()>0){
            rrndfff14 = hexASCII(rrndfff14);
        }else{
            rrndfff14 = "";
        }

        tipoCuentadffe50 = extraerTag("-"+infotrans,"dffe50");
        infotrans = cadenaSinTag("-"+infotrans,"dffe50");
        if(tipoCuentadffe50.length()>0){
            tipoCuentadffe50 = hexASCII(tipoCuentadffe50);
        }else{
            tipoCuentadffe50 = "";
        }
        ultDigitoTarjdffe54 = extraerTag("-"+infotrans,"dffe54");

        boolean isTienda = extDataOut.indexOf("Android") !=-1? true: false;
        if(isTienda){
            nombreTiendadfff32 = extraerTag("-"+extDataOut,"dfff32");
            if(nombreTiendadfff32.length()>0){
                nombreTiendadfff32 = hexASCII(nombreTiendadfff32);
            }
        }

        datafonoEntity = new DatafonoEntity(idDCinsert,bin42,codAprobaciondfff2d,codRtadfff02,
                dirEstabledffe78,fechaVenTarjdffe76,franquiciadffe49,montodffe40,
                ivadfff22,criptograma9f26,tvr95,tsi9b,aid4f,idClientedffe02,cuotasdfff26,
                numRecibodfff29,codTerminaldffe45,rrndfff14,tipoCuentadffe50,ultDigitoTarjdffe54,
                codigoComerciodffe77,iacdfff23,fecha9a,hora9f21,hashdffe01,propinadfff21,nombreTiendadfff32, true);

        if(tipoOperacion.equals("cierre")){

            if(idDCinsert.equals(Constantes.COD_ANULACION)){
                datafonoEntity.setMonto(montodffe40*-1);
                datafonoEntity.setIva(ivadfff22*-1);
            }

            finCierreDFFE0D = extraerTag(extDataOut,"dffe0d");

            try{
                new DatafonoCrearAsyncTask().execute(datafonoEntity).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.e("logcat",e.getMessage());
            } catch (ExecutionException e) {
                Log.e("logcat",e.getMessage());
            }
        }else if(!tipoOperacion.equals("ultimaTransaccion")){

            if(idDCinsert.equals(Constantes.COD_ANULACION)){
                datafonoEntity.setMonto(montodffe40*-1);
                datafonoEntity.setIva(ivadfff22*-1);
            }
            datafonoEntity.setEsCierre(false);

            try{
                new DatafonoCrearAsyncTask().execute(datafonoEntity).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.e("logcat",e.getMessage());
            } catch (ExecutionException e) {
                Log.e("logcat",e.getMessage());
            }
        }
    }

    private class DatafonoCrearAsyncTask extends AsyncTask<DatafonoEntity,Void,Void>{

        @Override
        protected Void doInBackground(DatafonoEntity... datafonoEntity) {
            try{

                Log.e("logcat","-");
                Log.e("logcat",datafonoEntity.toString());
                Log.e("logcat","-");

                BazarPosMovilDB.getBD(getApplication()).datafonoDao().insert(datafonoEntity[0]);
            }catch (Exception ex){
                Log.e("logcat",ex.getMessage());
            }
            return null;
        }
    }

    private String hexASCII(String hex){
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < hex.length(); i+=2) {
            String str = hex.substring(i, i+2);
            output.append((char)Integer.parseInt(str, 16));
        }
        return output.toString();
    }

    private String ASCIIhex(String ascii){
        char[] ch = ascii.toCharArray();
        StringBuilder builder = new StringBuilder();

        for (char c : ch) {
            int i = (int) c;
            builder.append(Integer.toHexString(i).toUpperCase());
        }
        return builder.toString();
    }

    private String cadenaSinTag(String extDataOut, String tag){
        String retorno = null;
        String[] arraytags = extDataOut.split(tag);

        if(arraytags[0].length() > 0){

            int numlectura,fin,decimal;
            numlectura = arraytags[0].length();
            numlectura = numlectura+tag.length();
            fin = numlectura+2;
            String hex = extDataOut.substring(numlectura,fin);
            numlectura = fin;
            decimal = Integer.parseInt(hex,16);
            fin = numlectura+(decimal*2);
            numlectura = extDataOut.length();
            retorno = extDataOut.substring(fin,numlectura);

        }
        return retorno;
    }

    private String extraerTag(String extDataOut, String tag){

        String retorno = "";
        String[] arraytags = extDataOut.split(tag);

        if(arraytags[0].length() > 0){

            int numlectura,fin,decimal;

            numlectura = arraytags[0].length();
            numlectura = numlectura+tag.length();
            fin = numlectura+2;
            String hex = extDataOut.substring(numlectura,fin);
            numlectura = fin;

            decimal = Integer.parseInt(hex,16);
            if(decimal > 0){
                fin = numlectura+(decimal*2);
                retorno = extDataOut.substring(numlectura,fin);
            }else{
                retorno = "";
            }
        }
        return retorno;
    }


    private void msjToast(String msj) {
        Toast.makeText(DatafonoActivity.this, msj, Toast.LENGTH_SHORT).show();
    }

    public void mensajeSimpleDialog(String titulo, String msj) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titulo)
                .setMessage(msj)
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