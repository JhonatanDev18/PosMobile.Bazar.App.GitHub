package com.crystal.bazarposmobile.ui;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.common.BluetoothImpresora;
import com.crystal.bazarposmobile.common.ClaveValorTef;
import com.crystal.bazarposmobile.common.Constantes;
import com.crystal.bazarposmobile.common.DatafonoTotales;
import com.crystal.bazarposmobile.common.LogFile;
import com.crystal.bazarposmobile.common.NetworkUtil;
import com.crystal.bazarposmobile.common.SPM;
import com.crystal.bazarposmobile.common.Utilidades;
import com.crystal.bazarposmobile.db.entity.DatafonoEntity;
import com.crystal.bazarposmobile.db.entity.TEFContinguenciaEntity;
import com.crystal.bazarposmobile.impresora.ExcepcionImpresion;
import com.crystal.bazarposmobile.impresora.ManejadorImpresora;
import com.crystal.bazarposmobile.impresora.ManejadorImpresoraPredeterminado;
import com.crystal.bazarposmobile.impresora.SettingsHelper;
import com.crystal.bazarposmobile.retrofit.request.RequestInsertarPerifericos;
import com.crystal.bazarposmobile.retrofit.request.creardocumento.Payment;
import com.crystal.bazarposmobile.retrofit.response.ResponseCupoEmpleado;
import com.crystal.bazarposmobile.retrofit.response.mercadopago.MercadoPagoImporte;
import com.crystal.bazarposmobile.retrofit.response.eanes.Producto;
import com.crystal.bazarposmobile.retrofit.response.perifericos.ResponseGuardarPerifericos;
import com.crystal.bazarposmobile.ui.dialogfragmen.AutorizacionSimpleFragment;
import com.crystal.bazarposmobile.ui.dialogfragmen.DeviceBluetoothSelectDialogFragment;
import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.comm.TcpConnection;

import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImpresionActivity
        extends
        AppCompatActivity
        implements
        View.OnClickListener,
        DeviceBluetoothSelectDialogFragment.OnInputListener,
        AutorizacionSimpleFragment.OnInputListener{

    //Declaración de los objetos de la interfaz del activity
    EditText etMac, etPuerto, etIp;
    ResponseCupoEmpleado cupoEmpleado;
    String nombreEmpresa = "Crystal S.A.S";
    String nitEmpresa = "NIT: 890901672-5";
    Button btnImprimir, btncerrar, btnresumen;
    TextView tvEstatus, tvMacAddress, tvIpAddress, tvPuertoIP;
    RadioGroup rgImpresion;
    RadioButton rbBluetooth, rbIp;
    ManejadorImpresora manejadorImpresora;
    Utilidades util;
    boolean isClaveValorTef;
    String tituloClaveValor;
    List<ClaveValorTef> claveValorTefList = new ArrayList<>();

    //Declaración de la variables del activity
    BluetoothAdapter mBtAdapter;
    BluetoothImpresora coneccionGlobal;
    private static final String bluetoothAddressKey = "ZEBRA_DEMO_BLUETOOTH_ADDRESS";
    private static final String tcpAddressKey = "ZEBRA_DEMO_TCP_ADDRESS";
    private static final String tcpPortKey = "ZEBRA_DEMO_TCP_PORT";
    private static final String PREFS_NAME = "OurSavedAddress";
    String pais, tipoCliente, cajeroname, tiendaname, currencyId, vendedorcode, vendedorName,
            tipo_cliente_letra, empresa_cliente, medioscaja, caja, lablePrint, mac, ip, puerto,
            numtrans, textoFiscal, cpclConfigLabel, cpclDtoControl, textplazos, textlineastencion,
            clienteName, codigobarras, fechahora, consecutivo, prefijo, tienda, clienteid,cajeroid;
    Double cambio, base_lmp, totalcompra, ivacompra;
    boolean reimprimir, primeraimpresion, cambioImp, isBluetooth,
            tirilaM1gen, tirilaM2gen, esFactura;
    SimpleDateFormat currentDate;
    DecimalFormat formatea = new DecimalFormat(Constantes.FORMATO_DECIMAL);
    Date todayDate;
    List<Producto> productoList = new ArrayList<>();
    List<Payment> importesList = new ArrayList<>();
    List<String> plazoslist, lineastencionlist;
    String cufeHash;

    //Comandos de impresion
    private static final byte[] LINE_FEED = {0x0A};
    private static final byte[] PAPER_FEED = {27, 0x4A, (byte) 0xFF};
    private static final byte[] PAPER_CUT = {0x1D, 0x56, 0x1};
    private static final byte[] ALIGN_LEFT = {0x1B, 0x61, 0};
    private static final byte[] ALIGN_CENTER = {0x1B, 0x61, 1};
    private static final byte[] BOLD_ON = {0x1B, 0x45, 1};
    private static final byte[] BOLD_OFF = {0x1B, 0x45, 0};
    private static final byte[] INIT = {0x1B, 0x40};
    private static final byte[] FLUSH_COMMAND = {(byte) 0xFF, 0x0C};
    private static final byte[] reset = new byte[]{0x1B, 0x40};

    //Comandos de formato texto
    public static final byte[] TXT_SMALL = {0x1b,0x21,0x01};
    public static final byte[] TXT_NORMAL = {0x1b, 0x21, 0x00};
    public static final byte[] TXT_2HEIGHT = {0x1b, 0x21, 0x10};
    public static final byte[] TXT_2WIDTH = {0x1b, 0x21, 0x20};
    public static final byte[] TXT_4SQUARE = {0x1b, 0x21, 0x30};
    public static final byte[] TXT_FONT_B = {0x1b, 0x4d, 0x01};

    byte[] LINEA_PUN = "--------------------------------------------------------".getBytes();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impresion);
        //Titulo del activity
        this.setTitle(R.string.impresion);

        //Obtener las variables de Intent
        lablePrint = getIntent().getExtras().getString("lablePrint");
        util = new Utilidades();

        findViews();

        //Validar si estoy en factura
        if (lablePrint.equals("factura")) {
            getSupportActionBar().setSubtitle(R.string.factura);
            //Obtener las variables de Intent para la factura
            prefijo = getIntent().getExtras().getString("prefijo");
            consecutivo = getIntent().getExtras().getString("consecutivo");
            numtrans = getIntent().getExtras().getString("numtrans");
            textoFiscal = getIntent().getExtras().getString("textoFiscal");
            cufeHash = getIntent().getExtras().getString("cufeHash");
            cambio = (Double) getIntent().getSerializableExtra("cambio");
            totalcompra = (Double) getIntent().getSerializableExtra("totalcompra");
            productoList = (ArrayList<Producto>) getIntent().getSerializableExtra("listProductos");
            importesList = (ArrayList<Payment>) getIntent().getSerializableExtra("listImportes");
            primeraimpresion = getIntent().getExtras().getBoolean("primeraimpresion");

            base_lmp = 0.0;
            ivacompra = 0.0;
            for (Producto p : productoList) {
                if (p.getCodigoTasaImpuesto().equals("NOR")) {
                    Double precioSinImpuesto = Double.valueOf(Math.round((p.getPrecio()) / (((p.getValorTasa()) / 100) + 1)));
                    if(p.getQuantity() > 0){
                        base_lmp = base_lmp + (precioSinImpuesto*p.getQuantity());
                        ivacompra = ivacompra + ((p.getPrecio()*p.getQuantity()) - (precioSinImpuesto*p.getQuantity()));
                    }else{
                        base_lmp = base_lmp - (precioSinImpuesto*p.getQuantity());
                        ivacompra = ivacompra - ((p.getPrecio()*p.getQuantity()) - (precioSinImpuesto*p.getQuantity()));
                    }
                }
            }

            //Variables usar en la impresion de facturas
            textplazos = SPM.getString(Constantes.TEXTOS_PLAZOS);
            plazoslist = Arrays.asList(textplazos.split("\n"));
            textlineastencion = SPM.getString(Constantes.TEXTOS_LINEAS_ATENCION);
            lineastencionlist = Arrays.asList(textlineastencion.split("\n"));
            codigobarras = prefijo + "-" + consecutivo;

            //Cambiar texto de Imprimir a Factura
            btnImprimir.setText(R.string.factura);
            cpclConfigLabel = "";
            cpclDtoControl = "";

            esFactura = true;
        } else {
            esFactura = false;

            if(getIntent().getExtras().containsKey("claveValorTef")){
                isClaveValorTef = getIntent().getExtras().getBoolean("claveValorTef", false);
            }

            if(getIntent().getExtras().containsKey("tituloClaveValor")){
                tituloClaveValor = getIntent().getExtras().getString("tituloClaveValor");
            }

            if(getIntent().getExtras().containsKey("listaClaveValor")){
                claveValorTefList = (ArrayList<ClaveValorTef>) getIntent().getSerializableExtra("listaClaveValor");
            }

            switch (lablePrint) {
                case "cupoEmpleado":
                    getSupportActionBar().setSubtitle(R.string.cupo_empleado);
                    break;
                case "CompTEF":
                    getSupportActionBar().setSubtitle(R.string.comprobante_compra_tef);
                    break;
                case "TEFDetallado":
                    getSupportActionBar().setSubtitle(R.string.cierre_detallado);
                    break;
                case "TEFTotales":
                    getSupportActionBar().setSubtitle(R.string.cierre_totales);
                    break;
                case "prueba":
                    getSupportActionBar().setSubtitle(R.string.prueba);
                    break;
                case "anulacion":
                    getSupportActionBar().setSubtitle(R.string.comprobante_anulacion_tef);
                    break;
                case "privacidaddatos":
                    getSupportActionBar().setSubtitle(R.string.politica_privacidad_datos);
                    break;
                case "MercadoPago":
                    getSupportActionBar().setSubtitle(R.string.comprobante_mercado_pago);
                    break;
                case "tef_cont_cierre_detallado":
                    getSupportActionBar().setSubtitle(R.string.tef_cont_cierre_detallado);
                    break;
            }

            Bundle extras = getIntent().getExtras();
            if (extras.containsKey("primeraimpresion")) {
                primeraimpresion = getIntent().getExtras().getBoolean("primeraimpresion");
            }
        }

        //Obteniendo las variable a usar
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        mac = settings.getString(bluetoothAddressKey, "");
        ip = settings.getString(tcpAddressKey, "");
        puerto = settings.getString(tcpPortKey, "");
        cambioImp = false;

        clienteid = SPM.getString(Constantes.DOCUMENTO_CLIENTE);
        tipoCliente = SPM.getString(Constantes.TIPO_CLIENTE);
        tipo_cliente_letra = SPM.getString(Constantes.TIPO_CLIENTE_LETRA);
        empresa_cliente = SPM.getString(Constantes.EMPRESA_CLIENTE);
        clienteName = SPM.getString(Constantes.FIRST_NAME_CLIENTE) + " " + SPM.getString(Constantes.LAST_NAME_CLIENTE);
        clienteName = cleanString(clienteName);

        tienda = SPM.getString(Constantes.TIENDA_CODE);
        tiendaname = SPM.getString(Constantes.NOMBRE_TIENDA);
        currencyId = SPM.getString(Constantes.DIVISA);
        medioscaja = SPM.getString(Constantes.MEDIOS_PAGO);
        pais = SPM.getString(Constantes.PAIS_CODE);
        caja = SPM.getString(Constantes.CAJA_CODE);
        cajeroname = SPM.getString(Constantes.CAJERO_NAME);
        cajeroid = SPM.getString(Constantes.CAJERO_CODE);
        vendedorcode = SPM.getString(Constantes.VENDEDOR_CODE);
        vendedorName = SPM.getString(Constantes.VENDEDOR_NAME);

        isBluetooth = SPM.getBoolean(Constantes.IS_BLUETOOTH);

        currentDate = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.US);
        todayDate = new Date();
        fechahora = currentDate.format(todayDate);

        if (clienteid != null)
            if (clienteid.equals(SPM.getString(Constantes.CLIENTE_GENERICO))) {
                clienteid = "";
            } else {
                tirilaM1gen = true;
                tirilaM2gen = true;
            }

        //Validar que tenga una impresora seleccionada
        reimprimir = etMac.getText().length() <= 0;

        //Variables de impresion
        String ipImpresora = SPM.getString(Constantes.IP_IMPRESORA);
        String ipPuertoImpresora = SPM.getString(Constantes.IP_PUERTO);
        String macImpresora = SPM.getString(Constantes.MAC_IMPRESORA);

        if(ipImpresora != null){
            etIp.setText(ipImpresora);
        }else{
            SPM.setString(Constantes.IP_IMPRESORA, ip);
            etIp.setText(ip);
        }

        if(ipPuertoImpresora != null){
            etPuerto.setText(ipPuertoImpresora);
        }else{
            SPM.setString(Constantes.IP_PUERTO, puerto);
            etPuerto.setText(puerto);
        }

        if(macImpresora != null){
            etMac.setText(macImpresora);
        }else{
            SPM.setString(Constantes.MAC_IMPRESORA, mac);
            etMac.setText(mac);
        }

        events();

        //Obtener la conexion global para bluetooth
        coneccionGlobal = BluetoothImpresora.getInstance(getMac());

        Boolean desabilitar_config = true;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("desabilitar_configuracion")) {
                desabilitar_config = false;
            }
        }

        //Validar si es bluetooth o ip
        if (isBluetooth) {
            try {
                if(manejadorImpresora != null){
                    manejadorImpresora.getConnection();
                }
            } catch (ExcepcionImpresion excepcionImpresion) {
                excepcionImpresion.printStackTrace();
            }
            //Validar que tenga una mac previamente seleionada para realizar la impresion
            if (!mac.isEmpty()) {
                conecImprimir();
            }
            etPuerto.setEnabled(false);
            etIp.setEnabled(false);
        } else {
            rbIp.setChecked(true);
            rbBluetooth.setChecked(false);
            etMac.setEnabled(false);
            if (etIp.getText().length() > 0 && etPuerto.getText().length() > 0) {
                conecImprimir();
            }
        }

        if (desabilitar_config) {
            desabilitar_configuracion();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflaterMenu = getMenuInflater();
        inflaterMenu.inflate(R.menu.menuimpresion, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mItemHabilitarConfImpresion:
                habilitar_configuracion();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constantes.RESP_ACT_BLUE_SELECT:
                    DeviceBluetoothSelectDialogFragment dialogSelectDeviceBluetooth = new DeviceBluetoothSelectDialogFragment();
                    dialogSelectDeviceBluetooth.show(getSupportFragmentManager(), "SelectDeviceBluetooth");
                    break;
                case Constantes.RESP_ACT_BLUE_PRINT:
                    cambioImp = true;
                    conecImprimir();
                    break;
            }
        } else {
            msjToast(getResources().getString(R.string.activat_bluetooth_impr));
        }
    }

    //Asignacion de Referencias
    private void findViews() {
        btnImprimir = this.findViewById(R.id.btnImprimirImpA);
        btncerrar = this.findViewById(R.id.btnCerrarImpA);
        btnresumen = this.findViewById(R.id.btnResumenImpA);
        btnresumen.setVisibility(View.GONE);
        etMac = this.findViewById(R.id.etMacAddressImpA);
        etPuerto = this.findViewById(R.id.etPuertoIPImpA);
        etIp = this.findViewById(R.id.etIpAddressImpA);
        tvEstatus = this.findViewById(R.id.tvEstatusImpA);
        rbBluetooth = this.findViewById(R.id.rbBluetoothImpA);
        rbIp = this.findViewById(R.id.rbIpDnsImpA);
        rgImpresion = this.findViewById(R.id.rgConexionImpA);
        tvMacAddress = this.findViewById(R.id.tvMacAddressImpA);
        tvIpAddress = this.findViewById(R.id.tvIpAddressImpA);
        tvPuertoIP = this.findViewById(R.id.tvPuertoIPImpA);
    }

    private void desabilitar_configuracion() {
        etIp.setVisibility(View.GONE);
        etPuerto.setVisibility(View.GONE);
        etMac.setVisibility(View.GONE);
        rgImpresion.setVisibility(View.GONE);
        rbBluetooth.setVisibility(View.GONE);
        rbIp.setVisibility(View.GONE);
        tvMacAddress.setVisibility(View.GONE);
        tvIpAddress.setVisibility(View.GONE);
        tvPuertoIP.setVisibility(View.GONE);

        etIp.setEnabled(false);
        etPuerto.setEnabled(false);
        etMac.setEnabled(false);
        rgImpresion.setEnabled(false);
        rbBluetooth.setEnabled(false);
        rbIp.setEnabled(false);
    }

    private void habilitar_configuracion() {
        etIp.setVisibility(View.VISIBLE);
        etPuerto.setVisibility(View.VISIBLE);
        etMac.setVisibility(View.VISIBLE);
        rgImpresion.setVisibility(View.VISIBLE);
        rbBluetooth.setVisibility(View.VISIBLE);
        rbIp.setVisibility(View.VISIBLE);
        tvMacAddress.setVisibility(View.VISIBLE);
        tvIpAddress.setVisibility(View.VISIBLE);
        tvPuertoIP.setVisibility(View.VISIBLE);

        rgImpresion.setEnabled(true);
        rbBluetooth.setEnabled(true);
        rbIp.setEnabled(true);

        if (isBluetooth) {
            etMac.setEnabled(true);
        } else {
            etIp.setEnabled(true);
            etPuerto.setEnabled(true);
        }
    }

    //Asignacion de eventos
    private void events() {
        btnImprimir.setOnClickListener(this);
        btncerrar.setOnClickListener(this);
        btnresumen.setOnClickListener(this);
        tvMacAddress.setOnClickListener(this);

        //Evento Check en el RadioGroup
        rgImpresion.setOnCheckedChangeListener((group, checkedId) -> {
            //Validar si es bluetooth o ip
            if (isRbBluetooth()) {
                etPuerto.setEnabled(false);
                etIp.setEnabled(false);
                etMac.setEnabled(true);
                SPM.setBoolean(Constantes.IS_BLUETOOTH, true);
            } else {
                etPuerto.setEnabled(true);
                etIp.setEnabled(true);
                etMac.setEnabled(false);
                SPM.setBoolean(Constantes.IS_BLUETOOTH, false);
            }
            disconnect();
        });

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            //Realizar la imprecion de la factura
            case R.id.btnImprimirImpA:
                //Realizar las validaciones necesarias
                //validar que tenga una impresora previamente seleccionada
                AutorizacionSimpleFragment autorizacionSimpleFragment = new AutorizacionSimpleFragment();
                autorizacionSimpleFragment.show(getSupportFragmentManager(), "AutorizacionSimpleFragment");
                break;
            //Cerrar el activity
            case R.id.btnCerrarImpA:
                cerrar();
                break;
            case R.id.btnResumenImpA:
                //Realizar las validaciones necesarias
                if (isRbBluetooth()) {
                    if (etMac.getText().length() > 0) {
                        if (reimprimir) {
                            reimprimir = false;
                            //Ir a la conexion e imprecion
                            lablePrint = "resumenFac";
                            conecImprimir();
                        } else {
                            msjToast(getResources().getString(R.string.proceso_curso));
                        }
                    } else {
                        reimprimir = true;
                        msjToast(getResources().getString(R.string.selec_impresora));
                    }
                } else {
                    if (etIp.getText().length() > 0 && etPuerto.getText().length() > 0) {
                        if (reimprimir) {
                            reimprimir = false;
                            //Ir a la conexion e imprecion
                            lablePrint = "resumenFac";
                            conecImprimir();
                        } else {
                            msjToast(getResources().getString(R.string.proceso_curso));
                        }
                    } else {
                        reimprimir = true;
                        msjToast(getResources().getString(R.string.introducir_puerto_ip));
                    }
                }
                break;
            //Abrir dialog DeviceBluetoothSelectDialogFragment
            case R.id.tvMacAddressImpA:
                if (rbBluetooth.isChecked()) {
                    mBtAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBtAdapter == null) {
                        msjToast(getResources().getString(R.string.bluetooth_error_adapter));
                    } else {
                        if (mBtAdapter.isEnabled()) {
                            DeviceBluetoothSelectDialogFragment dialogSelectDeviceBluetooth = new DeviceBluetoothSelectDialogFragment();
                            dialogSelectDeviceBluetooth.show(getSupportFragmentManager(), "SelectDeviceBluetooth");
                        } else {
                            Intent enableBtIntent = new Intent(
                                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            startActivityForResult(enableBtIntent,
                                    Constantes.RESP_ACT_BLUE_SELECT);
                        }
                    }
                }
                break;
        }
    }

    private void cerrar() {
        if(NetworkUtil.isConnected(ImpresionActivity.this)){
            guardarPerifericos();
        }else{
            Utilidades.mjsToast("No hay conexión a internet, no se pueden guardar los perifericos.",
                    Constantes.TOAST_TYPE_INFO, Toast.LENGTH_LONG, ImpresionActivity.this);
            if (esFactura) {
                //Pasar al activity de Consultar clientes
                Intent intent = new Intent(ImpresionActivity.this, ClienteConsultaActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }else{
                finish();
            }
        }
    }

    private void guardarPerifericos() {
        util.mostrarDialogProgressBar(ImpresionActivity.this, "Guardando perifericos...",false);
        List<RequestInsertarPerifericos> perifericos = new ArrayList<>();

        if (isRbBluetooth()) {
            perifericos.add(new RequestInsertarPerifericos(SPM.getString(Constantes.CAJA_CODE),
                    SPM.getString(Constantes.TIENDA_CODE),
                    "", "", Constantes.CONST_IMPRESORA_EPSON_BLUETOOTH,
                    SPM.getString(Constantes.MAC_IMPRESORA), "", "", "",
                    "", SPM.getString(Constantes.USER_NAME)));
        }else{
            perifericos.add(new RequestInsertarPerifericos(SPM.getString(Constantes.CAJA_CODE),
                    SPM.getString(Constantes.TIENDA_CODE),
                    SPM.getString(Constantes.IP_IMPRESORA), SPM.getString(Constantes.IP_PUERTO),
                    Constantes.CONST_IMPRESORA_EPSON, "", "", "",
                    "", "", SPM.getString(Constantes.USER_NAME)));
        }

        Call<ResponseGuardarPerifericos> callGuardarPerifericos = Utilidades.servicioRetrofit().doGuardarPerifericos(
                SPM.getString(Constantes.USER_NAME) + " - " + getResources().getString(R.string.version_apk),perifericos);

        callGuardarPerifericos.enqueue(new Callback<ResponseGuardarPerifericos>() {
            @Override
            public void onResponse(@NonNull Call<ResponseGuardarPerifericos> call, @NonNull Response<ResponseGuardarPerifericos> response) {
                if(response.isSuccessful()){
                    assert response.body() != null;
                    if(response.body().isEsValida()){
                        util.ocultarDialogProgressBar();
                        if (esFactura) {
                            //Pasar al activity de Consultar clientes
                            Intent intent = new Intent(ImpresionActivity.this, ClienteConsultaActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }else{
                            finish();
                        }
                    }else{
                        util.ocultarDialogProgressBar();
                        Utilidades.mjsToast(response.body().getMensaje(), Constantes.TOAST_TYPE_ERROR,
                                Toast.LENGTH_LONG, ImpresionActivity.this);
                        finish();
                    }
                }else{
                    LogFile.adjuntarLog("Error ResponseGuardarPerifericos: " + response.message());
                    util.ocultarDialogProgressBar();
                    Utilidades.sweetAlert(getResources().getString(R.string.error), getResources().getString(R.string.error_conexion_sb) + response.message(),
                            SweetAlertDialog.ERROR_TYPE, ImpresionActivity.this);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseGuardarPerifericos> call, @NonNull Throwable t) {
                LogFile.adjuntarLog("Error ResponseGuardarPerifericos: " + call + t);
                util.ocultarDialogProgressBar();
                Utilidades.sweetAlert(getResources().getString(R.string.error), getResources().getString(R.string.error_conexion) + t.getMessage(),
                        SweetAlertDialog.ERROR_TYPE, ImpresionActivity.this);
            }
        });
    }

    private boolean isRbBluetooth() {
        return rbBluetooth.isChecked();
    }

    //Contectar a la impresora e imprimir
    private void conecImprimir() {
        new Thread(new Runnable() {
            public void run() {
                Looper.prepare();
                conectar();
                Looper.loop();
                Looper.myLooper().quit();
            }
        }).start();
    }

    public void conectar() {
        if (isRbBluetooth()) {
            mBtAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBtAdapter == null) {
                msjToast(getResources().getString(R.string.bluetooth_error_adapter));
            } else {
                if (mBtAdapter.isEnabled()) {
                    if (manejadorImpresora == null) {
                        manejadorImpresora = new ManejadorImpresoraPredeterminado(getMac(), "", 0, true);
                        manejadorImpresora.inicializar();
                        SettingsHelper.saveBluetoothAddress(this, getMac());
                    } else {
                        if (cambioImp) {
                            setEstatus(getResources().getString(R.string.desconectando), Color.MAGENTA);
                            try {
                                manejadorImpresora.desconectar();
                            } catch (ExcepcionImpresion e) {
                                setEstatus(getResources().getString(R.string.error_desconectada), Color.RED);
                            }
                            manejadorImpresora = new ManejadorImpresoraPredeterminado(getMac(), "", 0, true);
                            manejadorImpresora.inicializar();
                            SettingsHelper.saveBluetoothAddress(this, getMac());
                        }
                    }
                    abrirConexion();
                } else {
                    setEstatus(getResources().getString(R.string.bluetooth_apagado), Color.RED);
                    Intent enableBtIntent = new Intent(
                            BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent,
                            Constantes.RESP_ACT_BLUE_PRINT);
                }
            }
        } else {
            try {
                int port = Integer.parseInt(getPuerto());
                manejadorImpresora = new ManejadorImpresoraPredeterminado("", getIp(), port, false);
                manejadorImpresora.inicializar();

                SettingsHelper.saveIp(this, getIp());
                SettingsHelper.savePort(this, getPuerto());
                abrirConexion();
            } catch (NumberFormatException e) {
                setEstatus(getResources().getString(R.string.puerto_invalido), Color.RED);
            }
        }
    }

    public void abrirConexion() {
        setEstatus(getResources().getString(R.string.conectado___), Color.YELLOW);
        try {
            manejadorImpresora.abrirConexion();

            setEstatus(getResources().getString(R.string.imprimiendo___), Color.YELLOW);
            if (manejadorImpresora != null) {
                if (manejadorImpresora.isConnected()) {

                    runOnUiThread(new Runnable() {
                        public void run() {
                            btncerrar.setEnabled(false);
                        }
                    });

                    switch (lablePrint) {
                        case "cupoEmpleado":
                            cupoEmpleadoESCPOS();
                            setBtnImprimir(getResources().getString(R.string.re_imprimir));
                            break;
                        case "CompTEF":
                            comprobanteTefESCPOS();
                            setBtnImprimir(getResources().getString(R.string.re_imprimir));
                            break;
                        case "TEFDetallado":
                            TEFdetalladoESCPOS();
                            setBtnImprimir(getResources().getString(R.string.re_imprimir));
                            break;
                        case "TEFTotales":
                            TEFtotalesESCPOS();
                            setBtnImprimir(getResources().getString(R.string.re_imprimir));
                            break;
                        case "factura":
                            facturaESCPOS();
                            setBtnImprimir(getResources().getString(R.string.re_imprimir));
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    btnresumen.setVisibility(View.VISIBLE);
                                }
                            });
                            break;
                        case "prueba":
                            pruebaESCPOS();
                            setBtnImprimir(getResources().getString(R.string.re_imprimir));
                            break;
                        case "resumenFac":
                            resumenFacturaESCPOS();
                            lablePrint = "factura";
                            break;
                        case "anulacion":
                            anulacionESCPOS();
                            setBtnImprimir(getResources().getString(R.string.re_imprimir));
                            break;
                        case "MercadoPago":
                            mercadoPagoESCPOS();
                            setBtnImprimir(getResources().getString(R.string.re_imprimir));
                            break;
                        case "tef_cont_cierre_detallado":
                            TEFContdetalladoESCPOS();
                            setBtnImprimir(getResources().getString(R.string.re_imprimir));
                            break;
                    }

                    runOnUiThread(new Runnable() {
                        public void run() {
                            btncerrar.setEnabled(true);
                        }
                    });

                    primeraimpresion = true;
                    reimprimir = true;
                    if (isRbBluetooth()) {
                        if (manejadorImpresora != null) {
                            setEstatus(getResources().getString(R.string.imprimision_realizada), Color.GREEN);
                        }
                    } else {
                        if (manejadorImpresora != null) {
                            reimprimir = true;
                            manejadorImpresora.desconectar();
                            setEstatus(getResources().getString(R.string.imprimision_realizada), Color.GREEN);
                        }
                    }
                } else {
                    setEstatus(getResources().getString(R.string.error_desconectada), Color.RED);
                    manejadorImpresora.desconectar();
                    disconnect();
                }
            } else {
                setEstatus(getResources().getString(R.string.error_desconectada), Color.RED);
                disconnect();
            }
        } catch (Exception e) {
            if (isRbBluetooth()) {
                msjToast(getResources().getString(R.string.rivise_dispositivo_bluetooth));
            } else {
                msjToast(getResources().getString(R.string.revise_ip_puerto));
            }
            disconnect();
        }
    }

    private void cupoEmpleadoESCPOS() {
        try {
            cupoEmpleado = (ResponseCupoEmpleado) getIntent().getSerializableExtra("cupoEmpleadoBody");

            manejadorImpresora.iniciarImpresion();
            manejadorImpresora.comandoFormatoTexto(ManejadorImpresora.COMANDO_FUENTE_PEQUEÑA);
            manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_CENTRO);
            manejadorImpresora.imprimirTexto("BAZAR PRODUCTORA"+ "\n"+
                    nombreEmpresa.toUpperCase()+" "+ nitEmpresa.toUpperCase(), 0, false, false);
            manejadorImpresora.saltarLinea();
            manejadorImpresora.saltarLinea();

            manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_IZQUIERDA);
            manejadorImpresora.comandoFormatoTexto(ManejadorImpresora.COMANDO_TXT_DOBLE_ALTURA);
            manejadorImpresora.imprimirTexto("CONSULTA CUPO EMPLEADO", 0, true, false);
            manejadorImpresora.comandoFormatoTexto(ManejadorImpresora.COMANDO_TXT_NORMAL);
            manejadorImpresora.saltarLinea();
            manejadorImpresora.comandoFormatoTexto(ManejadorImpresora.COMANDO_FUENTE_PEQUEÑA);
            manejadorImpresora.imprimirSeparador();
            manejadorImpresora.saltarLinea();

            manejadorImpresora.imprimirTexto("CEDULA  : " + clienteid + "\n" +
                    "NOMBRE  : " + cupoEmpleado.getNombre() + "\n" +
                    "EMPRESA : " + cupoEmpleado.getEmpresa() + "\n" +
                    "CUPO    : " + formatea.format(cupoEmpleado.getCupo()) + "\n" +
                    "FECHA   : " + util.getThisDateSimple() + "\n" +
                    "HORA    : " + util.getThisHora() + "\n" +
                    "TIENDA  : " + "Bazar Productora" + "\n" +
                    "CAJERO  : " + cajeroname,0, false, false);
            manejadorImpresora.saltarLinea();

            manejadorImpresora.imprimirSeparador();
            manejadorImpresora.saltarLinea();
            manejadorImpresora.saltarLinea();

            manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_IZQUIERDA);
            manejadorImpresora.imprimirTexto("Firma: _________________________________________________", 0, false, false);
            manejadorImpresora.saltarLinea();
            manejadorImpresora.saltarLinea();

            manejadorImpresora.saltarLinea();
            manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_CENTRO);
            manejadorImpresora.imprimirTexto("GRACIAS POR PREFERIRNOS", 0, false, false);
            manejadorImpresora.saltarLinea();

            //Corte de papel
            manejadorImpresora.saltarLinea();
            manejadorImpresora.confirmarComando();
            manejadorImpresora.alimentaPapel();
            manejadorImpresora.cortarPapel();

        } catch (Exception e) {
            msjToast(getResources().getString(R.string.error_impresion_intentar));
            disconnect();
        }
    }

    public void setBtnImprimir(String btntitulo) {
        runOnUiThread(new Runnable() {
            public void run() {
                btnImprimir.setText(btntitulo);
            }
        });
    }

    //Desconectarse del dispositivo
    public void disconnect() {
        reimprimir = true;
        try {
            if(manejadorImpresora != null){
                setEstatus(getResources().getString(R.string.desconectando), Color.MAGENTA);
                manejadorImpresora.desconectar();
                setEstatus(getResources().getString(R.string.no_conectado), Color.RED);
            }
        } catch (ExcepcionImpresion e) {
            setEstatus(getResources().getString(R.string.error_desconectada), Color.RED);
        }
    }

    //Mostrar un en barra inferior el estatus de la conexion al dispositivo
    private void setEstatus(final String statusMessage, final int color) {
        if (color == Color.RED) {
            reimprimir = true;
        }
        runOnUiThread(new Runnable() {
            public void run() {
                tvEstatus.setBackgroundColor(color);
                tvEstatus.setText(statusMessage);
            }
        });
    }

    //Limpiar string de todos los acentos y caracteres especiales
    public static String cleanString(String texto) {
        texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
        texto = texto.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return texto;
    }

    private void msjToast(String msj) {
        Toast.makeText(ImpresionActivity.this, msj, Toast.LENGTH_SHORT).show();
    }

    //Obtener texto dMac
    private String getMac() {
        return etMac.getText().toString();
    }

    //Obtener texto Puerto
    private String getPuerto() {
        return etPuerto.getText().toString();
    }

    //Obtener texto IP
    private String getIp() {
        return etIp.getText().toString();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        msjToast("Use el boton cerrar");
    }

    private void facturaESCPOS() {
        try {
            //Encabezado
            manejadorImpresora.iniciarImpresion();
            manejadorImpresora.comandoFormatoTexto(ManejadorImpresora.COMANDO_TXT_DOBLE_ANCHO);
            manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_CENTRO);
            manejadorImpresora.imprimirTexto("BAZAR PRODUCTORA", 0 , true, false);
            manejadorImpresora.saltarLinea();
            manejadorImpresora.comandoFormatoTexto(ManejadorImpresora.COMANDO_FUENTE_PEQUEÑA);
            manejadorImpresora.imprimirTexto("Crystal S.A.S - NIT: 890901672-5\n", 0 , false, false);
            manejadorImpresora.saltarLinea();

            manejadorImpresora.imprimirTexto("Numero de Factura: " + prefijo + "-" + consecutivo + "\n\n",0 , false, false);

            if (clienteName.length() > 36) {
                clienteName = clienteName.substring(0, 36);
            }
            //Información
            manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_IZQUIERDA);
            manejadorImpresora.imprimirTexto("Emitida: " + fechahora, 0 , false, false);
            manejadorImpresora.saltarLinea();
            manejadorImpresora.imprimirColumnas(
                    new Object[]{
                            "Tienda: " + tienda,
                            "",
                            "Caja No: " + caja,
                            ""
                    },
                    new int[]{
                            10,
                            10,
                            10,
                            10
                    },
                    new int[]{
                            ManejadorImpresora.ALINEACION_IZQUIERDA,
                            ManejadorImpresora.ALINEACION_DERECHA,
                            ManejadorImpresora.ALINEACION_IZQUIERDA,
                            ManejadorImpresora.ALINEACION_DERECHA
                    },
                    null, false
            );
            manejadorImpresora.saltarLinea();
            String esc_pos;

            if(SPM.getString(Constantes.CLIENTE_GENERICO).equals(clienteid)){
                clienteid = "222222222222";
                clienteName = "CONSUMIDOR FINAL";
            }

            if(numtrans.equals("-")){
                esc_pos = "Cliente: " + clienteid + " " + clienteName;
            }else{
                esc_pos = "Num. Transaccion: " + numtrans + "\n" +
                        "Cliente: " + clienteid + " " + clienteName;
            }

            manejadorImpresora.imprimirTexto(esc_pos,0 , false, false);
            manejadorImpresora.saltarLinea();

            //Encabezado de los productos
            manejadorImpresora.imprimirSeparador();
            manejadorImpresora.saltarLinea();
            manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_IZQUIERDA);

            manejadorImpresora.imprimirColumnas(
                    new Object[]{
                            "Cant",
                            "Descripcion",
                            "Valor",
                            "Imp"
                    },
                    new int[]{
                            5,
                            33,
                            12,
                            1
                    },
                    new int[]{
                            ManejadorImpresora.ALINEACION_IZQUIERDA,
                            ManejadorImpresora.ALINEACION_IZQUIERDA,
                            ManejadorImpresora.ALINEACION_DERECHA,
                            ManejadorImpresora.ALINEACION_DERECHA
                    },
                    null, false
            );

            manejadorImpresora.saltarLinea();
            manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_IZQUIERDA);
            manejadorImpresora.imprimirSeparador();
            manejadorImpresora.saltarLinea();

            Double total = 0.0, bolsaprice = 0.0;

            String tarifa = SPM.getString(Constantes.TARIFA_IVA);
            Integer totalArticulos = 0, bolsanum = 0;

            //Los productos
            for (Producto p : productoList) {

                totalArticulos = totalArticulos + p.getQuantity();
                total = total + (Math.abs(p.getPrecio())*p.getQuantity());

                if (p.getCodigoTasaImpuesto().equals("BO1")) {
                    bolsanum = bolsanum + p.getQuantity();
                    bolsaprice = p.getPrecio();
                }

                String pNombre = p.getNombre();
                if (p.getTalla() != null) {
                    pNombre = pNombre + " " + p.getTalla();
                }
                if (p.getColor() != null) {
                    pNombre = pNombre + " " + p.getColor();
                }

                if (pNombre.length() > 20) {
                    pNombre = pNombre.substring(0, 20);
                }
                pNombre = cleanString(pNombre);

                String pCodigoTasaImpuesto = p.getCodigoTasaImpuesto();
                if (pCodigoTasaImpuesto.length() > 1) {
                    pCodigoTasaImpuesto = pCodigoTasaImpuesto.substring(0, 1);
                }

                if(p.getQuantity() == 1){
                    manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_IZQUIERDA);
                    manejadorImpresora.imprimirColumnas(
                            new Object[]{
                                    p.getQuantity()+"x",
                                    p.getEan() +" "+pNombre,
                                    formatea.format(p.getPrecio()),
                                    "  " + pCodigoTasaImpuesto
                            },
                            new int[]{
                                    3,
                                    38,
                                    10,
                                    1
                            },
                            new int[]{
                                    ManejadorImpresora.ALINEACION_IZQUIERDA,
                                    ManejadorImpresora.ALINEACION_IZQUIERDA,
                                    ManejadorImpresora.ALINEACION_DERECHA,
                                    ManejadorImpresora.ALINEACION_DERECHA
                            },
                            null, false
                    );
                    manejadorImpresora.saltarLinea();
                }else{
                    manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_IZQUIERDA);
                    manejadorImpresora.imprimirColumnas(
                            new Object[]{
                                    p.getQuantity()+"x",
                                    p.getEan() +" "+pNombre,
                                    formatea.format(p.getPrecio()),
                                    ""
                            },
                            new int[]{
                                    3,
                                    38,
                                    10,
                                    1
                            },
                            new int[]{
                                    ManejadorImpresora.ALINEACION_IZQUIERDA,
                                    ManejadorImpresora.ALINEACION_IZQUIERDA,
                                    ManejadorImpresora.ALINEACION_DERECHA,
                                    ManejadorImpresora.ALINEACION_DERECHA
                            },
                            null, false
                    );
                    manejadorImpresora.saltarLinea();

                    manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_IZQUIERDA);
                    manejadorImpresora.imprimirColumnas(
                            new Object[]{
                                    "",
                                    "",
                                    "Valor Final: " + formatea.format(Math.abs(p.getPrecio())*p.getQuantity()),
                                    "  " + pCodigoTasaImpuesto
                            },
                            new int[]{
                                    1,
                                    1,
                                    49,
                                    1
                            },
                            new int[]{
                                    ManejadorImpresora.ALINEACION_IZQUIERDA,
                                    ManejadorImpresora.ALINEACION_IZQUIERDA,
                                    ManejadorImpresora.ALINEACION_DERECHA,
                                    ManejadorImpresora.ALINEACION_DERECHA
                            },
                            null, false
                    );
                    manejadorImpresora.saltarLinea();
                }
            }

            manejadorImpresora.imprimirSeparador();
            manejadorImpresora.saltarLinea();

            //Totales
            manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_IZQUIERDA);
            manejadorImpresora.comandoFormatoTexto(ManejadorImpresora.COMANDO_TXT_DOBLE_ALTURA);
            manejadorImpresora.imprimirColumnas(
                    new Object[]{
                            "TOTAL",
                            "",
                            "",
                            formatea.format(total)
                    },
                    new int[]{
                            18,
                            1,
                            1,
                            23
                    },
                    new int[]{
                            ManejadorImpresora.ALINEACION_IZQUIERDA,
                            ManejadorImpresora.ALINEACION_IZQUIERDA,
                            ManejadorImpresora.ALINEACION_DERECHA,
                            ManejadorImpresora.ALINEACION_DERECHA
                    },
                    null, true
            );
            manejadorImpresora.comandoFormatoTexto(ManejadorImpresora.COMANDO_TXT_NORMAL);
            manejadorImpresora.comandoFormatoTexto(ManejadorImpresora.COMANDO_FUENTE_PEQUEÑA);
            manejadorImpresora.saltarLinea();
            manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_IZQUIERDA);

            if (!primeraimpresion) {
                manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_CENTRO);
                manejadorImpresora.comandoFormatoTexto(ManejadorImpresora.COMANDO_TXT_DOBLE_ANCHO);
                manejadorImpresora.imprimirTexto("DUPLICADO", 0 , true, false);
                manejadorImpresora.comandoFormatoTexto(ManejadorImpresora.COMANDO_TXT_NORMAL);
                manejadorImpresora.comandoFormatoTexto(ManejadorImpresora.COMANDO_FUENTE_PEQUEÑA);
                manejadorImpresora.saltarLinea();
                manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_IZQUIERDA);
            }

            //medios de pagos
            if (totalcompra.intValue() != 0) {
                for (int i = 0; i < (importesList.size()); i++) {
                    manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_IZQUIERDA);
                    manejadorImpresora.imprimirColumnas(
                            new Object[]{
                                    importesList.get(i).getMethodId().equals("C45") ? importesList.get(i).getName().substring(importesList.get(i).getName().indexOf(" ")+1) : importesList.get(i).getName(),
                                    "",
                                    "",
                                    formatea.format(importesList.get(i).getAmount())
                            },
                            new int[]{
                                    25,
                                    2,
                                    1,
                                    24
                            },
                            new int[]{
                                    ManejadorImpresora.ALINEACION_IZQUIERDA,
                                    ManejadorImpresora.ALINEACION_IZQUIERDA,
                                    ManejadorImpresora.ALINEACION_DERECHA,
                                    ManejadorImpresora.ALINEACION_DERECHA
                            },
                            null, false
                    );
                    manejadorImpresora.saltarLinea();
                }
            }

            manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_IZQUIERDA);
            manejadorImpresora.imprimirTexto("TOTAL ARTICULOS: " + totalArticulos, 0,true ,false );
            manejadorImpresora.saltarLinea();

            //DISCRIMINACION TARIFAS IVA
            manejadorImpresora.saltarLinea();
            manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_CENTRO);
            manejadorImpresora.imprimirTexto("DISCRIMINACION TARIFAS IVA", 0,true , false);
            manejadorImpresora.saltarLinea();

            manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_IZQUIERDA);
            manejadorImpresora.imprimirColumnas(
                    new Object[]{
                            "Tarifa",
                            "Compra",
                            "Base/Imp",
                            "Iva"
                    },
                    new int[]{
                            10,
                            14,
                            14,
                            14
                    },
                    new int[]{
                            ManejadorImpresora.ALINEACION_IZQUIERDA,
                            ManejadorImpresora.ALINEACION_IZQUIERDA,
                            ManejadorImpresora.ALINEACION_DERECHA,
                            ManejadorImpresora.ALINEACION_DERECHA
                    },
                    null, false
            );
            manejadorImpresora.saltarLinea();
            manejadorImpresora.imprimirSeparador();
            manejadorImpresora.saltarLinea();

            Double totalcompraIva = totalcompra;
            if (!totalcompra.equals(base_lmp + ivacompra)) {
                totalcompraIva = totalcompra - (totalcompra - base_lmp - ivacompra);
            }

            manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_IZQUIERDA);
            manejadorImpresora.imprimirColumnas(
                    new Object[]{
                            tarifa + "%",
                            formatea.format(totalcompraIva),
                            formatea.format(base_lmp),
                            formatea.format(ivacompra)
                    },
                    new int[]{
                            10,
                            14,
                            14,
                            14
                    },
                    new int[]{
                            ManejadorImpresora.ALINEACION_IZQUIERDA,
                            ManejadorImpresora.ALINEACION_IZQUIERDA,
                            ManejadorImpresora.ALINEACION_DERECHA,
                            ManejadorImpresora.ALINEACION_DERECHA
                    },
                    null, false
            );
            manejadorImpresora.saltarLinea();
            manejadorImpresora.imprimirSeparador();
            manejadorImpresora.saltarLinea();

            //DISCRIMINACION IMPUESTO BOLSA.
            if (bolsanum != 0) {
                manejadorImpresora.saltarLinea();
                manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_CENTRO);
                manejadorImpresora.imprimirTexto("DISCRIMINACION IMPUESTO BOLSA", 0, true, false);
                manejadorImpresora.saltarLinea();

                manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_CENTRO);
                manejadorImpresora.imprimirColumnas(
                        new Object[]{
                                "Cant",
                                "Valor unitario"
                        },
                        new int[]{
                                20,
                                20
                        },
                        new int[]{
                                ManejadorImpresora.ALINEACION_IZQUIERDA,
                                ManejadorImpresora.ALINEACION_IZQUIERDA
                        },
                        null, false
                );
                manejadorImpresora.imprimirTexto("Valor total", 0, false, false);
                manejadorImpresora.saltarLinea();
                manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_IZQUIERDA);
                manejadorImpresora.imprimirSeparador();
                manejadorImpresora.saltarLinea();

                manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_CENTRO);
                manejadorImpresora.imprimirColumnas(
                        new Object[]{
                                bolsanum,
                                formatea.format(bolsaprice)
                        },
                        new int[]{
                                20,
                                20
                        },
                        new int[]{
                                ManejadorImpresora.ALINEACION_IZQUIERDA,
                                ManejadorImpresora.ALINEACION_IZQUIERDA
                        },
                        null, false
                );
                Double bolsaTotal = (bolsanum * bolsaprice);
                manejadorImpresora.imprimirTexto(formatea.format(bolsaTotal),0 , false, false);
                manejadorImpresora.saltarLinea();
                manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_IZQUIERDA);
                manejadorImpresora.imprimirSeparador();
                manejadorImpresora.saltarLinea();
            }

            //TEXTO FACTURA ELECTRONICA
            manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_CENTRO);
            manejadorImpresora.imprimirTexto("REPRESENTACION GRAFICA DE SU FACTURA \n ELECTRONICA DE VENTA", 0, true, false);
            manejadorImpresora.saltarLinea();
            manejadorImpresora.saltarLinea();

            //TEXTOS CONTRIBUYENTES
            manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_CENTRO);
            manejadorImpresora.imprimirTexto(SPM.getString(Constantes.TEXTOS_CONTRIBUYENTES), 0, false, false);
            manejadorImpresora.saltarLinea();
            manejadorImpresora.saltarLinea();

            //TEXTO FISCAL
            manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_CENTRO);
            manejadorImpresora.saltarLinea();
            String textFiscal = textoFiscal;
            textFiscal = textFiscal.replace("  ", " ");
            manejadorImpresora.imprimirTexto(textFiscal, 0, false, false);
            manejadorImpresora.saltarLinea();
            manejadorImpresora.saltarLinea();

            manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_IZQUIERDA);
            manejadorImpresora.imprimirTexto("CUFE: " + cufeHash, 0, false, false);
            manejadorImpresora.saltarLinea();
            manejadorImpresora.saltarLinea();

            manejadorImpresora.imprimirTexto("Emitida: " + fechahora, 0 , false, false);
            manejadorImpresora.saltarLinea();

            manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_CENTRO);
            manejadorImpresora.imprimirQR(SPM.getString(Constantes.URL_BASE_FE_QR)+cufeHash, 40);
            manejadorImpresora.saltarLinea();

            //TEXTO PROVEEDOR TECNOLOGICO
            manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_IZQUIERDA);
            manejadorImpresora.imprimirTexto(SPM.getString(Constantes.PROVEEDOR_TECNOLOGICO_FE), 0, false, false);
            manejadorImpresora.saltarLinea();
            manejadorImpresora.saltarLinea();

            //CODIGO DE BARRAS
            manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_CENTRO);

            manejadorImpresora.imprimirCodigoBarras(codigobarras, ManejadorImpresora.SET_WIDTH_NORMAL);
            manejadorImpresora.imprimirTexto(codigobarras, 0, false, false);
            manejadorImpresora.saltarLinea();
            manejadorImpresora.saltarLinea();
            manejadorImpresora.saltarLinea();
            manejadorImpresora.confirmarComando();
            manejadorImpresora.alimentaPapel();
            manejadorImpresora.cortarPapel();
        } catch (Exception e) {
            msjToast(getResources().getString(R.string.error_impresion_intentar));
            disconnect();
        }
    }

    private void resumenFacturaESCPOS() {

        try {
            //Encabezado
            manejadorImpresora.iniciarImpresion();

            manejadorImpresora.comandoFormatoTexto(ManejadorImpresora.COMANDO_FUENTE_PEQUEÑA);
            manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_CENTRO);
            manejadorImpresora.imprimirTexto("BAZAR PRODUCTORA - RESUMEN", 0 , false, false);
            manejadorImpresora.saltarLinea();

            if (clienteName.length() > 36) {
                clienteName = clienteName.substring(0, 36);
            }

            //Información
            manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_IZQUIERDA);
            manejadorImpresora.imprimirTexto("Emitida: " + fechahora, 0 , false, false);
            manejadorImpresora.saltarLinea();
            manejadorImpresora.imprimirColumnas(
                    new Object[]{
                            "Tienda: " + tienda,
                            "",
                            "Caja No: " + caja,
                            ""
                    },
                    new int[]{
                            10,
                            10,
                            10,
                            10
                    },
                    new int[]{
                            ManejadorImpresora.ALINEACION_IZQUIERDA,
                            ManejadorImpresora.ALINEACION_DERECHA,
                            ManejadorImpresora.ALINEACION_IZQUIERDA,
                            ManejadorImpresora.ALINEACION_DERECHA
                    },
                    null, false
            );
            manejadorImpresora.saltarLinea();
            String esc_pos;

            if(numtrans.equals("-")){
                esc_pos = "Cliente: " + clienteid + " " + clienteName;
            }else{
                esc_pos = "Num. Transaccion: " + numtrans + "\n" +
                        "Cliente: " + clienteid + " " + clienteName;
            }

            manejadorImpresora.imprimirTexto(esc_pos,0 , false, false);
            manejadorImpresora.saltarLinea();

            //Encabezado de los productos
            manejadorImpresora.imprimirSeparador();
            manejadorImpresora.saltarLinea();
            manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_IZQUIERDA);

            manejadorImpresora.imprimirColumnas(
                    new Object[]{
                            "Cant",
                            "Descripcion",
                            "Valor"
                    },
                    new int[]{
                            5,
                            34,
                            13
                    },
                    new int[]{
                            ManejadorImpresora.ALINEACION_IZQUIERDA,
                            ManejadorImpresora.ALINEACION_IZQUIERDA,
                            ManejadorImpresora.ALINEACION_DERECHA,
                    },
                    null, false
            );

            manejadorImpresora.saltarLinea();
            manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_IZQUIERDA);
            manejadorImpresora.imprimirSeparador();
            manejadorImpresora.saltarLinea();

            Double total = 0.0;

            String tarifa = SPM.getString(Constantes.TARIFA_IVA);
            Integer totalArticulos = 0;

            //Los productos
            for (Producto p : productoList) {

                totalArticulos = totalArticulos + p.getQuantity();
                total = total + (Math.abs(p.getPrecio())*p.getQuantity());

                String pNombre = p.getNombre();
                if (p.getTalla() != null) {
                    pNombre = pNombre + " " + p.getTalla();
                }
                if (p.getColor() != null) {
                    pNombre = pNombre + " " + p.getColor();
                }

                if (pNombre.length() > 20) {
                    pNombre = pNombre.substring(0, 20);
                }
                pNombre = cleanString(pNombre);

                manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_IZQUIERDA);
                manejadorImpresora.imprimirColumnas(
                        new Object[]{
                                p.getQuantity()+"x",
                                p.getEan() +" "+pNombre,
                                formatea.format(Math.abs(p.getPrecio())*p.getQuantity())
                        },
                        new int[]{
                                3,
                                38,
                                11
                        },
                        new int[]{
                                ManejadorImpresora.ALINEACION_IZQUIERDA,
                                ManejadorImpresora.ALINEACION_IZQUIERDA,
                                ManejadorImpresora.ALINEACION_DERECHA
                        },
                        null, false
                );
                manejadorImpresora.saltarLinea();
            }

            manejadorImpresora.imprimirSeparador();
            manejadorImpresora.saltarLinea();

            //Totales
            manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_IZQUIERDA);
            manejadorImpresora.imprimirTexto("TOTAL: " + formatea.format(total) + " ("+totalArticulos+" ARTICULOS)", 0,true ,false );
            manejadorImpresora.saltarLinea();

            //medios de pagos
            if (totalcompra.intValue() != 0) {
                for (int i = 0; i < (importesList.size()); i++) {
                    manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_IZQUIERDA);
                    manejadorImpresora.imprimirColumnas(
                            new Object[]{
                                    importesList.get(i).getName(),
                                    formatea.format(importesList.get(i).getAmount())
                            },
                            new int[]{
                                    30,
                                    22
                            },
                            new int[]{
                                    ManejadorImpresora.ALINEACION_IZQUIERDA,
                                    ManejadorImpresora.ALINEACION_IZQUIERDA
                            },
                            null, false
                    );
                    manejadorImpresora.saltarLinea();
                }
            }

            //DISCRIMINACION TARIFAS IVA
            manejadorImpresora.saltarLinea();
            manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_CENTRO);
            manejadorImpresora.imprimirTexto("DISCRIMINACION TARIFAS IVA", 0,true , false);
            manejadorImpresora.saltarLinea();

            manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_IZQUIERDA);
            manejadorImpresora.imprimirColumnas(
                    new Object[]{
                            "Tarifa",
                            "Compra",
                            "Base/Imp",
                            "Iva"
                    },
                    new int[]{
                            10,
                            14,
                            14,
                            14
                    },
                    new int[]{
                            ManejadorImpresora.ALINEACION_IZQUIERDA,
                            ManejadorImpresora.ALINEACION_IZQUIERDA,
                            ManejadorImpresora.ALINEACION_DERECHA,
                            ManejadorImpresora.ALINEACION_DERECHA
                    },
                    null, false
            );
            manejadorImpresora.saltarLinea();
            manejadorImpresora.imprimirSeparador();
            manejadorImpresora.saltarLinea();

            Double totalcompraIva = totalcompra;
            if (!totalcompra.equals(base_lmp + ivacompra)) {
                totalcompraIva = totalcompra - (totalcompra - base_lmp - ivacompra);
            }

            manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_IZQUIERDA);
            manejadorImpresora.imprimirColumnas(
                    new Object[]{
                            tarifa + "%",
                            formatea.format(totalcompraIva),
                            formatea.format(base_lmp),
                            formatea.format(ivacompra)
                    },
                    new int[]{
                            10,
                            14,
                            14,
                            14
                    },
                    new int[]{
                            ManejadorImpresora.ALINEACION_IZQUIERDA,
                            ManejadorImpresora.ALINEACION_IZQUIERDA,
                            ManejadorImpresora.ALINEACION_DERECHA,
                            ManejadorImpresora.ALINEACION_DERECHA
                    },
                    null, false
            );
            manejadorImpresora.saltarLinea();
            manejadorImpresora.confirmarComando();
            manejadorImpresora.alimentaPapel();
            manejadorImpresora.cortarPapel();
        } catch (Exception e) {
            msjToast(getResources().getString(R.string.error_impresion_intentar));
            disconnect();
        }
    }

    private void mercadoPagoESCPOS() {

        MercadoPagoImporte mpImporte = (MercadoPagoImporte) getIntent().getSerializableExtra("mpImporte");
        assert mpImporte != null;
        try {
            manejadorImpresora.iniciarImpresion();

            manejadorImpresora.comandoFormatoTexto(ManejadorImpresora.COMANDO_FUENTE_PEQUEÑA);
            manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_CENTRO);
            manejadorImpresora.imprimirTexto("Crystal S.A.S", 0 , true, false);
            manejadorImpresora.saltarLinea();
            manejadorImpresora.saltarLinea();

            manejadorImpresora.imprimirTexto("MercadoPago - ID Orden", 0 , false, false);
            manejadorImpresora.saltarLinea();
            manejadorImpresora.saltarLinea();

            manejadorImpresora.imprimirTexto(mpImporte.getEr(), 0 , false, false );
            manejadorImpresora.saltarLinea();
            manejadorImpresora.saltarLinea();

            manejadorImpresora.imprimirTexto(mpImporte.getId(), 0 , false, false);
            manejadorImpresora.saltarLinea();
            manejadorImpresora.saltarLinea();

            manejadorImpresora.imprimirTexto(formatea.format(mpImporte.getMonto()), 0 , false, false);
            manejadorImpresora.saltarLinea();

            manejadorImpresora.saltarLinea();
            manejadorImpresora.confirmarComando();
            manejadorImpresora.alimentaPapel();
            manejadorImpresora.cortarPapel();
        } catch (Exception e) {
            msjToast(getResources().getString(R.string.error_impresion_intentar));
            disconnect();
        }
    }

    private void comprobanteTefESCPOS() {
        try{
            if(isClaveValorTef){
                manejadorImpresora.iniciarImpresion();
                manejadorImpresora.comandoFormatoTexto(ManejadorImpresora.COMANDO_FUENTE_PEQUEÑA);
                manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_CENTRO);
                manejadorImpresora.imprimirTexto(tituloClaveValor, 0, true, false);
                manejadorImpresora.saltarLinea();
                manejadorImpresora.saltarLinea();

                for(ClaveValorTef claveValor : claveValorTefList){
                    manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_IZQUIERDA);
                    manejadorImpresora.imprimirTexto(claveValor.getClave() +" "+ claveValor.getValor(), 0, false, false);
                    manejadorImpresora.saltarLinea();
                }
            }else{
                DatafonoEntity respDatafono = (DatafonoEntity) getIntent().getSerializableExtra("respDatafono");
                assert respDatafono != null;

                manejadorImpresora.iniciarImpresion();
                manejadorImpresora.comandoFormatoTexto(ManejadorImpresora.COMANDO_FUENTE_PEQUEÑA);
                manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_CENTRO);

                manejadorImpresora.comandoFormatoTexto(ManejadorImpresora.COMANDO_TXT_DOBLE_ANCHO_ALTO);
                manejadorImpresora.imprimirTexto("CREDIBANCO", 0, true, false);
                manejadorImpresora.comandoFormatoTexto(ManejadorImpresora.COMANDO_TXT_NORMAL);
                manejadorImpresora.comandoFormatoTexto(ManejadorImpresora.COMANDO_FUENTE_PEQUEÑA);
                manejadorImpresora.saltarLinea();

                manejadorImpresora.imprimirTexto(respDatafono.getTiendaNombre().trim(), 0, true, false);
                manejadorImpresora.saltarLinea();

                manejadorImpresora.imprimirTexto(respDatafono.getDirEstablecimiento(), 0, false, false);
                manejadorImpresora.saltarLinea();

                manejadorImpresora.imprimirTexto("CU: " + respDatafono.getCodigoComercio().trim(), 0, false, false);
                manejadorImpresora.saltarLinea();

                manejadorImpresora.imprimirTexto(" Fecha: " + util.convertirFecha(respDatafono.getFecha() + "-" + respDatafono.getHora()), 0, false, false);
                manejadorImpresora.saltarLinea();

                manejadorImpresora.imprimirTexto(respDatafono.getHash(), 0, false, false);
                manejadorImpresora.saltarLinea();
                manejadorImpresora.saltarLinea();

                manejadorImpresora.imprimirColumnas(
                        new Object[]{
                                "TER: " + respDatafono.getCodigoTerminal(),
                                "AUT: " + respDatafono.getCodigoAprobacion()
                        },
                        new int[]{
                                27,
                                27
                        },
                        new int[]{
                                ManejadorImpresora.ALINEACION_IZQUIERDA,
                                ManejadorImpresora.ALINEACION_DERECHA
                        },
                        null, false
                );
                manejadorImpresora.saltarLinea();

                manejadorImpresora.imprimirColumnas(
                        new Object[]{
                                respDatafono.getFranquicia().trim(),
                                respDatafono.getTipoCuenta()
                        },
                        new int[]{
                                27,
                                27
                        },
                        new int[]{
                                ManejadorImpresora.ALINEACION_IZQUIERDA,
                                ManejadorImpresora.ALINEACION_DERECHA
                        },
                        null, false
                );
                manejadorImpresora.saltarLinea();

                manejadorImpresora.imprimirColumnas(
                        new Object[]{
                                "****" + respDatafono.getUltDigitoTarj(),
                                ""
                        },
                        new int[]{
                                27,
                                27
                        },
                        new int[]{
                                ManejadorImpresora.ALINEACION_IZQUIERDA,
                                ManejadorImpresora.ALINEACION_DERECHA
                        },
                        null, false
                );
                manejadorImpresora.saltarLinea();

                manejadorImpresora.imprimirColumnas(
                        new Object[]{
                                "Recibo: " + respDatafono.getNumRecibo(),
                                "TVR: " + respDatafono.getTvr()
                        },
                        new int[]{
                                27,
                                27
                        },
                        new int[]{
                                ManejadorImpresora.ALINEACION_IZQUIERDA,
                                ManejadorImpresora.ALINEACION_DERECHA
                        },
                        null, false
                );
                manejadorImpresora.saltarLinea();

                if (respDatafono.getTipoCuenta().equals("CR")) {
                    manejadorImpresora.imprimirColumnas(
                            new Object[]{
                                    "Cuotas: " + respDatafono.getCuotas(),
                                    "TSI: " + respDatafono.getTsi()
                            },
                            new int[]{
                                    27,
                                    27
                            },
                            new int[]{
                                    ManejadorImpresora.ALINEACION_IZQUIERDA,
                                    ManejadorImpresora.ALINEACION_DERECHA
                            },
                            null, false
                    );
                } else {
                    manejadorImpresora.imprimirColumnas(
                            new Object[]{
                                    "",
                                    "TSI: " + respDatafono.getTsi()
                            },
                            new int[]{
                                    27,
                                    27
                            },
                            new int[]{
                                    ManejadorImpresora.ALINEACION_IZQUIERDA,
                                    ManejadorImpresora.ALINEACION_DERECHA
                            },
                            null, false
                    );
                }
                manejadorImpresora.saltarLinea();

                manejadorImpresora.imprimirColumnas(
                        new Object[]{
                                "RRN: " + respDatafono.getRrn(),
                                "AID: " + respDatafono.getAid()
                        },
                        new int[]{
                                27,
                                27
                        },
                        new int[]{
                                ManejadorImpresora.ALINEACION_IZQUIERDA,
                                ManejadorImpresora.ALINEACION_DERECHA
                        },
                        null, false
                );
                manejadorImpresora.saltarLinea();

                manejadorImpresora.imprimirTexto("Criptograma: " + respDatafono.getCriptograma(), 0, false, false);
                manejadorImpresora.saltarLinea();
                manejadorImpresora.saltarLinea();

                int total = respDatafono.getMonto().intValue();
                int iva = respDatafono.getIva().intValue();
                int compra_neta = total - iva;

                manejadorImpresora.imprimirColumnas(
                        new Object[]{
                                "COMPRA NETA",
                                formatea.format(compra_neta)
                        },
                        new int[]{
                                27,
                                27
                        },
                        new int[]{
                                ManejadorImpresora.ALINEACION_IZQUIERDA,
                                ManejadorImpresora.ALINEACION_DERECHA
                        },
                        null, false
                );
                manejadorImpresora.saltarLinea();

                manejadorImpresora.imprimirColumnas(
                        new Object[]{
                                "IVA",
                                formatea.format(iva)
                        },
                        new int[]{
                                27,
                                27
                        },
                        new int[]{
                                ManejadorImpresora.ALINEACION_IZQUIERDA,
                                ManejadorImpresora.ALINEACION_DERECHA
                        },
                        null, false
                );
                manejadorImpresora.saltarLinea();

                manejadorImpresora.imprimirColumnas(
                        new Object[]{
                                "IAC",
                                formatea.format(respDatafono.getIac())
                        },
                        new int[]{
                                27,
                                27
                        },
                        new int[]{
                                ManejadorImpresora.ALINEACION_IZQUIERDA,
                                ManejadorImpresora.ALINEACION_DERECHA
                        },
                        null, false
                );
                manejadorImpresora.saltarLinea();

                manejadorImpresora.imprimirColumnas(
                        new Object[]{
                                "TOTAL",
                                formatea.format(total)
                        },
                        new int[]{
                                27,
                                27
                        },
                        new int[]{
                                ManejadorImpresora.ALINEACION_IZQUIERDA,
                                ManejadorImpresora.ALINEACION_DERECHA
                        },
                        null, true
                );
                manejadorImpresora.saltarLinea();

                if (!primeraimpresion) {
                    manejadorImpresora.comandoFormatoTexto(ManejadorImpresora.COMANDO_TXT_DOBLE_ANCHO);
                    manejadorImpresora.imprimirTexto("DUPLICADO", 0, true, false);
                    manejadorImpresora.comandoFormatoTexto(ManejadorImpresora.COMANDO_TXT_NORMAL);
                    manejadorImpresora.comandoFormatoTexto(ManejadorImpresora.COMANDO_FUENTE_PEQUEÑA);
                    manejadorImpresora.saltarLinea();
                }

                if (respDatafono.getTipoCuenta().equals("CR")) {
                    manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_CENTRO);
                    manejadorImpresora.imprimirSeparador();
                    manejadorImpresora.saltarLinea();

                    manejadorImpresora.imprimirTexto(SPM.getString(Constantes.TEXTOS_PAGARE_TEF), 0, false, false);
                    manejadorImpresora.saltarLinea();
                    manejadorImpresora.saltarLinea();

                    manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_IZQUIERDA);
                    manejadorImpresora.imprimirTexto("Firma: _________________________________________________", 0, false, false);
                    manejadorImpresora.saltarLinea();
                    manejadorImpresora.saltarLinea();

                    manejadorImpresora.imprimirTexto("C.C: ___________________________________________________", 0, false, false);
                    manejadorImpresora.saltarLinea();
                    manejadorImpresora.saltarLinea();

                    manejadorImpresora.imprimirTexto("Tel: ___________________________________________________", 0, false, false);
                    manejadorImpresora.saltarLinea();
                }

                manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_CENTRO);
                manejadorImpresora.imprimirTexto(respDatafono.getIdCliente().trim(),0, true, false);
                manejadorImpresora.saltarLinea();

                manejadorImpresora.saltarLinea();
            }
            manejadorImpresora.confirmarComando();
            manejadorImpresora.alimentaPapel();
            manejadorImpresora.cortarPapel();
        }catch (Exception e){
            msjToast(getResources().getString(R.string.error_impresion_intentar));
            disconnect();
        }
    }

    private void TEFContdetalladoESCPOS() {

        try{
            List<TEFContinguenciaEntity> tefContList = (ArrayList<TEFContinguenciaEntity>) getIntent().getSerializableExtra("tefContList");
            assert tefContList != null;
            manejadorImpresora.iniciarImpresion();
            manejadorImpresora.comandoFormatoTexto(ManejadorImpresora.COMANDO_TXT_DOBLE_ANCHO);
            manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_CENTRO);
            manejadorImpresora.imprimirTexto("BAZAR PRODUCTORA", 0 , true, false);
            manejadorImpresora.comandoFormatoTexto(ManejadorImpresora.COMANDO_TXT_NORMAL);
            manejadorImpresora.comandoFormatoTexto(ManejadorImpresora.COMANDO_FUENTE_PEQUEÑA);
            manejadorImpresora.saltarLinea();

            manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_IZQUIERDA);
            manejadorImpresora.imprimirTexto("Crystal S.A.S - TEF CONTINGENCIA", 0 , true, false);
            manejadorImpresora.saltarLinea();

            SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.US);
            Date todayDate = new Date();
            String fecha = currentDate.format(todayDate);
            manejadorImpresora.imprimirTexto("Fecha: "+fecha, 0 , true, false);
            manejadorImpresora.saltarLinea();

            manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_CENTRO);
            manejadorImpresora.imprimirTexto("INFORME DETALLADO TEF CONT.", 0 , true, false);
            manejadorImpresora.saltarLinea();

            manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_IZQUIERDA);
            manejadorImpresora.imprimirColumnas(
                    new Object[]{
                            "TIENDA",
                            "MONTO"
                    },
                    new int[]{
                            27,
                            27
                    },
                    new int[]{
                            ManejadorImpresora.ALINEACION_IZQUIERDA,
                            ManejadorImpresora.ALINEACION_IZQUIERDA
                    },
                    null, false
            );
            manejadorImpresora.saltarLinea();

            String franquiciaUso = null, tipoCuentaUso = null;

            for (TEFContinguenciaEntity rd : tefContList) {
                franquiciaUso = rd.getFranquicia();
                tipoCuentaUso = rd.getTipoCuenta();
                break;
            }

            assert franquiciaUso != null;
            manejadorImpresora.imprimirTexto(cleanString(franquiciaUso.trim() + " " + tipoCuentaUso), 0 , false, false);
            manejadorImpresora.saltarLinea();

            //Recorremos el cursor hasta que no haya más registros
            for (TEFContinguenciaEntity rd : tefContList) {
                String franquicia = rd.getFranquicia();
                String tipoCuenta = rd.getTipoCuenta();
                Double monto = rd.getMonto();
                String fechaO = rd.getFecha();
                fechaO = fechaO.substring(0,16);

                if (!franquiciaUso.equals(franquicia) || !tipoCuentaUso.equals(tipoCuenta)) {

                    manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_CENTRO);
                    manejadorImpresora.imprimirSeparador();
                    manejadorImpresora.saltarLinea();
                    manejadorImpresora.imprimirTexto(cleanString(franquiciaUso.trim() + " " + tipoCuenta), 0 , false, false);
                    manejadorImpresora.saltarLinea();
                }
                franquiciaUso = franquicia;
                tipoCuentaUso = tipoCuenta;

                manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_IZQUIERDA);
                manejadorImpresora.imprimirColumnas(
                        new Object[]{
                                fechaO,
                                formatea.format(monto)
                        },
                        new int[]{
                                27,
                                27
                        },
                        new int[]{
                                ManejadorImpresora.ALINEACION_IZQUIERDA,
                                ManejadorImpresora.ALINEACION_IZQUIERDA
                        },
                        null, false
                );
                manejadorImpresora.saltarLinea();
            }

            manejadorImpresora.saltarLinea();
            manejadorImpresora.confirmarComando();
            manejadorImpresora.alimentaPapel();
            manejadorImpresora.cortarPapel();
        } catch (Exception e) {
            msjToast(getResources().getString(R.string.error_impresion_intentar));
            disconnect();
        }
    }

    private void TEFdetalladoESCPOS() {
        try {
            List<DatafonoEntity> listRDDetallado = (ArrayList<DatafonoEntity>) getIntent().getSerializableExtra("listRDDetallado");
            assert listRDDetallado != null;
            if (!listRDDetallado.isEmpty()) {
                String codigoComercio = getIntent().getExtras().getString("codigoComercio");
                String dirEstable = getIntent().getExtras().getString("dirEstable");
                String codTerminal = getIntent().getExtras().getString("codTerminal");

                //Cabecera
                manejadorImpresora.iniciarImpresion();
                manejadorImpresora.comandoFormatoTexto(ManejadorImpresora.COMANDO_FUENTE_PEQUEÑA);
                manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_CENTRO);

                manejadorImpresora.imprimirTexto("CREDIBANCO", 0, true, false);
                manejadorImpresora.saltarLinea();

                SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.US);
                Date todayDate = new Date();
                String fecha = currentDate.format(todayDate);
                manejadorImpresora.imprimirTexto(fecha, 0, false, false);
                manejadorImpresora.saltarLinea();
                manejadorImpresora.saltarLinea();

                manejadorImpresora.imprimirColumnas(
                        new Object[]{
                                codigoComercio,
                                "Crystal S.A.S"
                        },
                        new int[]{
                                27,
                                27
                        },
                        new int[]{
                                ManejadorImpresora.ALINEACION_IZQUIERDA,
                                ManejadorImpresora.ALINEACION_DERECHA
                        },
                        null, false
                );
                manejadorImpresora.saltarLinea();

                manejadorImpresora.imprimirColumnas(
                        new Object[]{
                                dirEstable,
                                "TER: " + codTerminal
                        },
                        new int[]{
                                27,
                                27
                        },
                        new int[]{
                                ManejadorImpresora.ALINEACION_IZQUIERDA,
                                ManejadorImpresora.ALINEACION_DERECHA
                        },
                        null, false
                );
                manejadorImpresora.saltarLinea();
                manejadorImpresora.saltarLinea();

                manejadorImpresora.imprimirTexto("INFORME DETALLADO", 0, true, false);
                manejadorImpresora.saltarLinea();
                manejadorImpresora.saltarLinea();

                manejadorImpresora.imprimirColumnas(
                        new Object[]{
                                "TRAN",
                                "TARJETA",
                                "RECIBO",
                                "TIPO",
                                "MONTO"
                        },
                        new int[]{
                                6,
                                10,
                                10,
                                6,
                                20
                        },
                        new int[]{
                                ManejadorImpresora.ALINEACION_IZQUIERDA,
                                ManejadorImpresora.ALINEACION_IZQUIERDA,
                                ManejadorImpresora.ALINEACION_IZQUIERDA,
                                ManejadorImpresora.ALINEACION_DERECHA,
                                ManejadorImpresora.ALINEACION_DERECHA
                        },
                        null, false
                );
                manejadorImpresora.saltarLinea();
                manejadorImpresora.saltarLinea();

                String franquiciaUso = null, tipoCuentaUso = null;

                for (DatafonoEntity rd : listRDDetallado) {
                    franquiciaUso = rd.getFranquicia();
                    tipoCuentaUso = rd.getTipoCuenta();
                    break;
                }

                manejadorImpresora.imprimirTexto(franquiciaUso.trim() + " " + tipoCuentaUso, 0, false, false);
                manejadorImpresora.saltarLinea();

                //Recorremos el cursor hasta que no haya más registros
                for (DatafonoEntity rd : listRDDetallado) {
                    String franquicia = rd.getFranquicia();
                    Double monto = rd.getMonto();
                    String tipoCuenta = rd.getTipoCuenta();
                    String numRecibo = rd.getNumRecibo();
                    String ultDigitoTarj = rd.getUltDigitoTarj();
                    String tipoOperacion = rd.getDc().trim();

                    if (!franquiciaUso.equals(franquicia) || !tipoCuentaUso.equals(tipoCuenta)) {

                        manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_CENTRO);
                        manejadorImpresora.imprimirSeparador();
                        manejadorImpresora.saltarLinea();
                        manejadorImpresora.saltarLinea();

                        manejadorImpresora.imprimirTexto(franquicia.trim() + " " + tipoCuenta, 0, false, false);
                        manejadorImpresora.saltarLinea();
                    }
                    franquiciaUso = franquicia;
                    tipoCuentaUso = tipoCuenta;

                    manejadorImpresora.imprimirColumnas(
                            new Object[]{
                                    tipoOperacion,
                                    "****" + ultDigitoTarj,
                                    numRecibo,
                                    tipoCuenta,
                                    formatea.format(monto)
                            },
                            new int[]{
                                    6,
                                    10,
                                    10,
                                    6,
                                    20
                            },
                            new int[]{
                                    ManejadorImpresora.ALINEACION_IZQUIERDA,
                                    ManejadorImpresora.ALINEACION_IZQUIERDA,
                                    ManejadorImpresora.ALINEACION_IZQUIERDA,
                                    ManejadorImpresora.ALINEACION_DERECHA,
                                    ManejadorImpresora.ALINEACION_DERECHA
                            },
                            null, false
                    );
                    manejadorImpresora.saltarLinea();
                }
                manejadorImpresora.saltarLinea();
                manejadorImpresora.confirmarComando();
                manejadorImpresora.alimentaPapel();
                manejadorImpresora.cortarPapel();
            }
        } catch (Exception e) {
            msjToast(getResources().getString(R.string.error_impresion_intentar));
            disconnect();
        }
    }

    private void TEFtotalesESCPOS() {
    //Imprimir
        try {
            List<DatafonoTotales> listRDTotales = (ArrayList<DatafonoTotales>) getIntent().getSerializableExtra("listRDTotales");
            List<DatafonoTotales> listRDTotalesCanceladas = (ArrayList<DatafonoTotales>) getIntent().getSerializableExtra("listRDTotalesCanceladas");

            assert listRDTotales != null;
            assert listRDTotalesCanceladas != null;
            if (!listRDTotales.isEmpty()) {
                String codigoComercio = getIntent().getExtras().getString("codigoComercio");
                String dirEstable = getIntent().getExtras().getString("dirEstable");
                String codTerminal = getIntent().getExtras().getString("codTerminal");

                //Cabecera
                manejadorImpresora.iniciarImpresion();
                manejadorImpresora.comandoFormatoTexto(ManejadorImpresora.COMANDO_FUENTE_PEQUEÑA);
                manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_CENTRO);

                manejadorImpresora.imprimirTexto("CREDIBANCO", 0, true, false);
                manejadorImpresora.saltarLinea();

                SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.US);
                Date todayDate = new Date();
                String fecha = currentDate.format(todayDate);
                manejadorImpresora.imprimirTexto(fecha, 0, false, false);
                manejadorImpresora.saltarLinea();
                manejadorImpresora.saltarLinea();

                manejadorImpresora.imprimirColumnas(
                        new Object[]{
                                codigoComercio,
                                "Crystal S.A.S"
                        },
                        new int[]{
                                27,
                                27
                        },
                        new int[]{
                                ManejadorImpresora.ALINEACION_IZQUIERDA,
                                ManejadorImpresora.ALINEACION_DERECHA
                        },
                        null, false
                );
                manejadorImpresora.saltarLinea();

                manejadorImpresora.imprimirColumnas(
                        new Object[]{
                                dirEstable,
                                codTerminal
                        },
                        new int[]{
                                27,
                                27
                        },
                        new int[]{
                                ManejadorImpresora.ALINEACION_IZQUIERDA,
                                ManejadorImpresora.ALINEACION_DERECHA
                        },
                        null, false
                );
                manejadorImpresora.saltarLinea();
                manejadorImpresora.saltarLinea();

                manejadorImpresora.imprimirTexto("TOTAL POR ENTIDAD", 0, true, false);
                manejadorImpresora.saltarLinea();
                manejadorImpresora.saltarLinea();

                //Recorremos los registros
                //Cuerpo

                String franquiciaUso = listRDTotales.get(0).getFranquicia();

                Double montoF = 0.0;
                Double ivaF = 0.0;
                Double propinaF = 0.0;
                Integer numElementosF = 0;
                Double canceladaF = 0.0;
                Integer canceladaCountF = 0;

                Double montoT = 0.0;
                Double ivaT = 0.0;
                Double propinaT = 0.0;
                Integer numElementosT = 0;
                Double canceladaT = 0.0;
                Integer canceladaCountT = 0;

                int cont = 0, count = listRDTotales.size();

                for (DatafonoTotales rd : listRDTotales) {
                    cont++;
                    String franquicia = rd.getFranquicia();
                    String tipoCuenta = rd.getTipoCuenta();
                    Integer numElementos = rd.getTipoCuentaCount();
                    Double monto = rd.getMonto();
                    Double iva = rd.getIva();
                    Double propina = rd.getPropina();
                    Double cancelada = 0.0;
                    Integer canceladaCount = 0;

                    if (!listRDTotalesCanceladas.isEmpty()) {
                        for (DatafonoTotales rdCan : listRDTotalesCanceladas) {
                            if (franquicia.equals(rdCan.getFranquicia()) && tipoCuenta.equals(rdCan.getTipoCuenta())) {
                                cancelada = rdCan.getMonto() * -1;
                                canceladaCount = rdCan.getTipoCuentaCount();
                            }
                        }
                    }

                    //Total Franquicia
                    if (!franquiciaUso.equals(franquicia)) {
                        manejadorImpresora.imprimirTexto("TOTAL "+ franquiciaUso.trim(), 0, false, false);
                        manejadorImpresora.saltarLinea();

                        manejadorImpresora.imprimirColumnas(
                                new Object[]{
                                        "COMPRAS",
                                        ":" + numElementosF,
                                        formatea.format(montoF - ivaF)
                                },
                                new int[]{
                                        16,
                                        10,
                                        26
                                },
                                new int[]{
                                        ManejadorImpresora.ALINEACION_IZQUIERDA,
                                        ManejadorImpresora.ALINEACION_IZQUIERDA,
                                        ManejadorImpresora.ALINEACION_DERECHA
                                },
                                null, false
                        );
                        manejadorImpresora.saltarLinea();

                        manejadorImpresora.imprimirColumnas(
                                new Object[]{
                                        "IVA",
                                        ":",
                                        formatea.format(ivaF)
                                },
                                new int[]{
                                        16,
                                        10,
                                        26
                                },
                                new int[]{
                                        ManejadorImpresora.ALINEACION_IZQUIERDA,
                                        ManejadorImpresora.ALINEACION_IZQUIERDA,
                                        ManejadorImpresora.ALINEACION_DERECHA
                                },
                                null, false
                        );
                        manejadorImpresora.saltarLinea();

                        manejadorImpresora.imprimirColumnas(
                                new Object[]{
                                        "PROPINA",
                                        ":",
                                        formatea.format(propinaF)
                                },
                                new int[]{
                                        16,
                                        10,
                                        26
                                },
                                new int[]{
                                        ManejadorImpresora.ALINEACION_IZQUIERDA,
                                        ManejadorImpresora.ALINEACION_IZQUIERDA,
                                        ManejadorImpresora.ALINEACION_DERECHA
                                },
                                null, false
                        );
                        manejadorImpresora.saltarLinea();
                        manejadorImpresora.imprimirSeparador();
                        manejadorImpresora.saltarLinea();

                        manejadorImpresora.imprimirColumnas(
                                new Object[]{
                                        "SUBTOTAL",
                                        ":" + numElementosF,
                                        formatea.format(montoF)
                                },
                                new int[]{
                                        16,
                                        10,
                                        26
                                },
                                new int[]{
                                        ManejadorImpresora.ALINEACION_IZQUIERDA,
                                        ManejadorImpresora.ALINEACION_IZQUIERDA,
                                        ManejadorImpresora.ALINEACION_DERECHA
                                },
                                null, false
                        );
                        manejadorImpresora.saltarLinea();

                        manejadorImpresora.imprimirColumnas(
                                new Object[]{
                                        "CANCELADAS",
                                        ":" + canceladaCountF,
                                        "-" + formatea.format(canceladaF)
                                },
                                new int[]{
                                        16,
                                        10,
                                        26
                                },
                                new int[]{
                                        ManejadorImpresora.ALINEACION_IZQUIERDA,
                                        ManejadorImpresora.ALINEACION_IZQUIERDA,
                                        ManejadorImpresora.ALINEACION_DERECHA
                                },
                                null, false
                        );
                        manejadorImpresora.saltarLinea();
                        manejadorImpresora.imprimirSeparador();
                        manejadorImpresora.saltarLinea();

                        montoF = 0.0;
                        ivaF = 0.0;
                        propinaF = 0.0;
                        numElementosF = 0;
                        canceladaF = 0.0;
                        canceladaCountT = 0;
                    }
                    franquiciaUso = franquicia;

                    manejadorImpresora.imprimirTexto(franquicia.trim() + " " + tipoCuenta, 0, false, false);
                    manejadorImpresora.saltarLinea();

                    manejadorImpresora.imprimirColumnas(
                            new Object[]{
                                    "COMPRAS",
                                    ":" + numElementos,
                                    formatea.format(monto-iva)
                            },
                            new int[]{
                                    16,
                                    10,
                                    26
                            },
                            new int[]{
                                    ManejadorImpresora.ALINEACION_IZQUIERDA,
                                    ManejadorImpresora.ALINEACION_IZQUIERDA,
                                    ManejadorImpresora.ALINEACION_DERECHA
                            },
                            null, false
                    );
                    manejadorImpresora.saltarLinea();

                    manejadorImpresora.imprimirColumnas(
                            new Object[]{
                                    "IVA",
                                    ":",
                                    formatea.format(iva)
                            },
                            new int[]{
                                    16,
                                    10,
                                    26
                            },
                            new int[]{
                                    ManejadorImpresora.ALINEACION_IZQUIERDA,
                                    ManejadorImpresora.ALINEACION_IZQUIERDA,
                                    ManejadorImpresora.ALINEACION_DERECHA
                            },
                            null, false
                    );
                    manejadorImpresora.saltarLinea();

                    manejadorImpresora.imprimirColumnas(
                            new Object[]{
                                    "PROPINA",
                                    ":",
                                    formatea.format(propina)
                            },
                            new int[]{
                                    16,
                                    10,
                                    26
                            },
                            new int[]{
                                    ManejadorImpresora.ALINEACION_IZQUIERDA,
                                    ManejadorImpresora.ALINEACION_IZQUIERDA,
                                    ManejadorImpresora.ALINEACION_DERECHA
                            },
                            null, false
                    );
                    manejadorImpresora.saltarLinea();
                    manejadorImpresora.imprimirSeparador();
                    manejadorImpresora.saltarLinea();

                    manejadorImpresora.imprimirColumnas(
                            new Object[]{
                                    "SUBTOTAL",
                                    ":" + numElementos,
                                    formatea.format(monto)
                            },
                            new int[]{
                                    16,
                                    10,
                                    26
                            },
                            new int[]{
                                    ManejadorImpresora.ALINEACION_IZQUIERDA,
                                    ManejadorImpresora.ALINEACION_IZQUIERDA,
                                    ManejadorImpresora.ALINEACION_DERECHA
                            },
                            null, false
                    );
                    manejadorImpresora.saltarLinea();

                    manejadorImpresora.imprimirColumnas(
                            new Object[]{
                                    "CANCELADAS",
                                    ":" + canceladaCount,
                                    formatea.format(cancelada)
                            },
                            new int[]{
                                    16,
                                    10,
                                    26
                            },
                            new int[]{
                                    ManejadorImpresora.ALINEACION_IZQUIERDA,
                                    ManejadorImpresora.ALINEACION_IZQUIERDA,
                                    ManejadorImpresora.ALINEACION_DERECHA
                            },
                            null, false
                    );
                    manejadorImpresora.saltarLinea();
                    manejadorImpresora.imprimirSeparador();
                    manejadorImpresora.saltarLinea();

                    montoF = montoF + monto;
                    ivaF = ivaF + iva;
                    propinaF = propinaF + propina;
                    numElementosF = numElementosF + numElementos;
                    canceladaF = canceladaF + cancelada;
                    canceladaCountF = canceladaCountF + canceladaCount;

                    montoT = montoT + monto;
                    ivaT = ivaT + iva;
                    propinaT = propinaT + propina;
                    numElementosT = numElementosT + numElementos;
                    canceladaT = canceladaT + cancelada;
                    canceladaCountT = canceladaCountT + canceladaCount;

                    //Total Franquicia Final
                    if (cont == count) {

                        manejadorImpresora.imprimirTexto("TOTAL " + franquiciaUso.trim(), 0, false, false);
                        manejadorImpresora.saltarLinea();

                        manejadorImpresora.imprimirColumnas(
                                new Object[]{
                                        "COMPRAS",
                                        ":" + numElementosF,
                                        formatea.format(montoF - ivaF)
                                },
                                new int[]{
                                        16,
                                        10,
                                        26
                                },
                                new int[]{
                                        ManejadorImpresora.ALINEACION_IZQUIERDA,
                                        ManejadorImpresora.ALINEACION_IZQUIERDA,
                                        ManejadorImpresora.ALINEACION_DERECHA
                                },
                                null, false
                        );
                        manejadorImpresora.saltarLinea();

                        manejadorImpresora.imprimirColumnas(
                                new Object[]{
                                        "IVA",
                                        ":",
                                        formatea.format(ivaF)
                                },
                                new int[]{
                                        16,
                                        10,
                                        26
                                },
                                new int[]{
                                        ManejadorImpresora.ALINEACION_IZQUIERDA,
                                        ManejadorImpresora.ALINEACION_IZQUIERDA,
                                        ManejadorImpresora.ALINEACION_DERECHA
                                },
                                null, false
                        );
                        manejadorImpresora.saltarLinea();

                        manejadorImpresora.imprimirColumnas(
                                new Object[]{
                                        "PROPINA",
                                        ":",
                                        formatea.format(propinaF)
                                },
                                new int[]{
                                        16,
                                        10,
                                        26
                                },
                                new int[]{
                                        ManejadorImpresora.ALINEACION_IZQUIERDA,
                                        ManejadorImpresora.ALINEACION_IZQUIERDA,
                                        ManejadorImpresora.ALINEACION_DERECHA
                                },
                                null, false
                        );
                        manejadorImpresora.saltarLinea();
                        manejadorImpresora.imprimirSeparador();
                        manejadorImpresora.saltarLinea();

                        manejadorImpresora.imprimirColumnas(
                                new Object[]{
                                        "SUBTOTAL",
                                        ":" + numElementosF,
                                        formatea.format(montoF)
                                },
                                new int[]{
                                        16,
                                        10,
                                        26
                                },
                                new int[]{
                                        ManejadorImpresora.ALINEACION_IZQUIERDA,
                                        ManejadorImpresora.ALINEACION_IZQUIERDA,
                                        ManejadorImpresora.ALINEACION_DERECHA
                                },
                                null, false
                        );
                        manejadorImpresora.saltarLinea();

                        manejadorImpresora.imprimirColumnas(
                                new Object[]{
                                        "CANCELADAS",
                                        ":" + canceladaF,
                                        formatea.format(canceladaF)
                                },
                                new int[]{
                                        16,
                                        10,
                                        26
                                },
                                new int[]{
                                        ManejadorImpresora.ALINEACION_IZQUIERDA,
                                        ManejadorImpresora.ALINEACION_IZQUIERDA,
                                        ManejadorImpresora.ALINEACION_DERECHA
                                },
                                null, false
                        );
                        manejadorImpresora.saltarLinea();
                        manejadorImpresora.imprimirSeparador();
                        manejadorImpresora.saltarLinea();
                    }
                }

                //GRAN TOTAL
                manejadorImpresora.saltarLinea();
                manejadorImpresora.imprimirTexto("GRAN TOTAL", 0, false, false);
                manejadorImpresora.saltarLinea();

                manejadorImpresora.imprimirColumnas(
                        new Object[]{
                                "COMPRAS",
                                ":" + numElementosT,
                                formatea.format(montoT-ivaT)
                        },
                        new int[]{
                                16,
                                10,
                                26
                        },
                        new int[]{
                                ManejadorImpresora.ALINEACION_IZQUIERDA,
                                ManejadorImpresora.ALINEACION_IZQUIERDA,
                                ManejadorImpresora.ALINEACION_DERECHA
                        },
                        null, false
                );
                manejadorImpresora.saltarLinea();

                manejadorImpresora.imprimirColumnas(
                        new Object[]{
                                "IVA",
                                ":",
                                formatea.format(ivaT)
                        },
                        new int[]{
                                16,
                                10,
                                26
                        },
                        new int[]{
                                ManejadorImpresora.ALINEACION_IZQUIERDA,
                                ManejadorImpresora.ALINEACION_IZQUIERDA,
                                ManejadorImpresora.ALINEACION_DERECHA
                        },
                        null, false
                );
                manejadorImpresora.saltarLinea();

                manejadorImpresora.imprimirColumnas(
                        new Object[]{
                                "PROPINA",
                                ":",
                                formatea.format(propinaT)
                        },
                        new int[]{
                                16,
                                10,
                                26
                        },
                        new int[]{
                                ManejadorImpresora.ALINEACION_IZQUIERDA,
                                ManejadorImpresora.ALINEACION_IZQUIERDA,
                                ManejadorImpresora.ALINEACION_DERECHA
                        },
                        null, false
                );
                manejadorImpresora.saltarLinea();
                manejadorImpresora.imprimirSeparador();
                manejadorImpresora.saltarLinea();

                manejadorImpresora.imprimirColumnas(
                        new Object[]{
                                "SUBTOTAL",
                                ":" + numElementosT,
                                formatea.format(montoT)
                        },
                        new int[]{
                                16,
                                10,
                                26
                        },
                        new int[]{
                                ManejadorImpresora.ALINEACION_IZQUIERDA,
                                ManejadorImpresora.ALINEACION_IZQUIERDA,
                                ManejadorImpresora.ALINEACION_DERECHA
                        },
                        null, false
                );
                manejadorImpresora.saltarLinea();

                manejadorImpresora.imprimirColumnas(
                        new Object[]{
                                "CANCELADAS",
                                ":" + canceladaCountT,
                                formatea.format(canceladaT)
                        },
                        new int[]{
                                16,
                                10,
                                26
                        },
                        new int[]{
                                ManejadorImpresora.ALINEACION_IZQUIERDA,
                                ManejadorImpresora.ALINEACION_IZQUIERDA,
                                ManejadorImpresora.ALINEACION_DERECHA
                        },
                        null, false
                );
                manejadorImpresora.saltarLinea();
                manejadorImpresora.imprimirSeparador();
                manejadorImpresora.saltarLinea();

                manejadorImpresora.saltarLinea();
                manejadorImpresora.confirmarComando();
                manejadorImpresora.alimentaPapel();
                manejadorImpresora.cortarPapel();
            }
        } catch (Exception e) {
            msjToast(getResources().getString(R.string.error_impresion_intentar));
            disconnect();
        }
    }

    private void pruebaESCPOS() {
        try {
            manejadorImpresora.abrirCajon();
            manejadorImpresora.iniciarImpresion();

            manejadorImpresora.comandoFormatoTexto(ManejadorImpresora.COMANDO_FUENTE_PEQUEÑA);
            manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_CENTRO);
            manejadorImpresora.imprimirTexto("Crystal S.A.S", 0, false, false);
            manejadorImpresora.saltarLinea();

            //CODIGO DE BARRAS
            manejadorImpresora.imprimirCodigoBarras("PRUEBA-OK", ManejadorImpresora.SET_WIDTH_ANCHO);
            manejadorImpresora.imprimirTexto("PRUEBA-OK", 0, false, false);
            manejadorImpresora.saltarLinea();
            manejadorImpresora.saltarLinea();

            manejadorImpresora.imprimirQR("*Prueba Realizada Satisfactoriamente*", 80);
            manejadorImpresora.saltarLinea();

            //Alican codiciones y restricciones
            manejadorImpresora.imprimirTexto("*Prueba Realizada Satisfactoriamente*", 0, false, false);
            manejadorImpresora.saltarLinea();
            manejadorImpresora.confirmarComando();
            manejadorImpresora.alimentaPapel();
            manejadorImpresora.cortarPapel();

        } catch (Exception e) {
            msjToast(getResources().getString(R.string.error_impresion_intentar));
            disconnect();
        }
    }

    private void anulacionESCPOS() {

        DatafonoEntity respDatafono = (DatafonoEntity) getIntent().getSerializableExtra("respDatafono");
        assert respDatafono != null;
        try{
            manejadorImpresora.iniciarImpresion();
            manejadorImpresora.comandoFormatoTexto(ManejadorImpresora.COMANDO_FUENTE_PEQUEÑA);
            manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_CENTRO);

            manejadorImpresora.comandoFormatoTexto(ManejadorImpresora.COMANDO_TXT_DOBLE_ANCHO_ALTO);
            manejadorImpresora.imprimirTexto("CREDIBANCO", 0, true, false);
            manejadorImpresora.comandoFormatoTexto(ManejadorImpresora.COMANDO_TXT_NORMAL);
            manejadorImpresora.comandoFormatoTexto(ManejadorImpresora.COMANDO_FUENTE_PEQUEÑA);
            manejadorImpresora.saltarLinea();

            manejadorImpresora.imprimirTexto(respDatafono.getTiendaNombre().trim(), 0, true, false);
            manejadorImpresora.saltarLinea();

            manejadorImpresora.imprimirTexto(respDatafono.getDirEstablecimiento(), 0, false, false);
            manejadorImpresora.saltarLinea();

            manejadorImpresora.imprimirTexto("CU: " + respDatafono.getCodigoComercio().trim(), 0, false, false);
            manejadorImpresora.saltarLinea();

            manejadorImpresora.imprimirTexto("Fecha: " + util.convertirFecha(respDatafono.getFecha() + "-" + respDatafono.getHora()), 0, false, false);
            manejadorImpresora.saltarLinea();

            manejadorImpresora.imprimirTexto(respDatafono.getHash(), 0, false, false);
            manejadorImpresora.saltarLinea();
            manejadorImpresora.saltarLinea();

            manejadorImpresora.imprimirColumnas(
                    new Object[]{
                            "TER: " + respDatafono.getCodigoTerminal(),
                            "AUT: " + respDatafono.getCodigoAprobacion()
                    },
                    new int[]{
                            27,
                            27
                    },
                    new int[]{
                            ManejadorImpresora.ALINEACION_IZQUIERDA,
                            ManejadorImpresora.ALINEACION_DERECHA
                    },
                    null, false
            );
            manejadorImpresora.saltarLinea();

            manejadorImpresora.imprimirColumnas(
                    new Object[]{
                            respDatafono.getFranquicia().trim(),
                            respDatafono.getTipoCuenta()
                    },
                    new int[]{
                            27,
                            27
                    },
                    new int[]{
                            ManejadorImpresora.ALINEACION_IZQUIERDA,
                            ManejadorImpresora.ALINEACION_DERECHA
                    },
                    null, false
            );
            manejadorImpresora.saltarLinea();

            manejadorImpresora.imprimirColumnas(
                    new Object[]{
                            "****" + respDatafono.getUltDigitoTarj(),
                            ""
                    },
                    new int[]{
                            27,
                            27
                    },
                    new int[]{
                            ManejadorImpresora.ALINEACION_IZQUIERDA,
                            ManejadorImpresora.ALINEACION_DERECHA
                    },
                    null, false
            );
            manejadorImpresora.saltarLinea();

            manejadorImpresora.imprimirColumnas(
                    new Object[]{
                            "Recibo: " + respDatafono.getNumRecibo(),
                            "RRN: " + respDatafono.getRrn()
                    },
                    new int[]{
                            27,
                            27
                    },
                    new int[]{
                            ManejadorImpresora.ALINEACION_IZQUIERDA,
                            ManejadorImpresora.ALINEACION_DERECHA
                    },
                    null, false
            );
            manejadorImpresora.saltarLinea();

            int total = respDatafono.getMonto().intValue();
            int iva = respDatafono.getIva().intValue();
            int compra_neta = total - iva;

            manejadorImpresora.imprimirColumnas(
                    new Object[]{
                            "COMPRA NETA",
                            formatea.format(compra_neta)
                    },
                    new int[]{
                            27,
                            27
                    },
                    new int[]{
                            ManejadorImpresora.ALINEACION_IZQUIERDA,
                            ManejadorImpresora.ALINEACION_DERECHA
                    },
                    null, false
            );
            manejadorImpresora.saltarLinea();
            manejadorImpresora.saltarLinea();

            manejadorImpresora.imprimirColumnas(
                    new Object[]{
                            "TOTAL",
                            formatea.format(total)
                    },
                    new int[]{
                            27,
                            27
                    },
                    new int[]{
                            ManejadorImpresora.ALINEACION_IZQUIERDA,
                            ManejadorImpresora.ALINEACION_DERECHA
                    },
                    null, true
            );
            manejadorImpresora.saltarLinea();

            manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_CENTRO);
            manejadorImpresora.imprimirSeparador();
            manejadorImpresora.saltarLinea();

            manejadorImpresora.comandoFormatoTexto(ManejadorImpresora.COMANDO_TXT_DOBLE_ANCHO);
            manejadorImpresora.imprimirTexto("ANULACION", 0, false, false);
            manejadorImpresora.comandoFormatoTexto(ManejadorImpresora.COMANDO_TXT_NORMAL);
            manejadorImpresora.comandoFormatoTexto(ManejadorImpresora.COMANDO_FUENTE_PEQUEÑA);
            manejadorImpresora.saltarLinea();

            manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_CENTRO);
            manejadorImpresora.imprimirSeparador();
            manejadorImpresora.saltarLinea();
            manejadorImpresora.saltarLinea();

            if (!primeraimpresion) {
                manejadorImpresora.saltarLinea();
                manejadorImpresora.comandoFormatoTexto(ManejadorImpresora.COMANDO_TXT_DOBLE_ANCHO);
                manejadorImpresora.imprimirTexto("DUPLICADO", 0, true, false);
                manejadorImpresora.comandoFormatoTexto(ManejadorImpresora.COMANDO_TXT_NORMAL);
                manejadorImpresora.comandoFormatoTexto(ManejadorImpresora.COMANDO_FUENTE_PEQUEÑA);
                manejadorImpresora.saltarLinea();
                manejadorImpresora.saltarLinea();
            }

            manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_IZQUIERDA);
            manejadorImpresora.imprimirTexto("Firma: _________________________________________________", 0, false, false);
            manejadorImpresora.saltarLinea();
            manejadorImpresora.saltarLinea();

            manejadorImpresora.imprimirTexto("C.C: ___________________________________________________", 0, false, false);
            manejadorImpresora.saltarLinea();
            manejadorImpresora.saltarLinea();

            manejadorImpresora.imprimirTexto("Tel: ___________________________________________________", 0, false, false);
            manejadorImpresora.saltarLinea();

            manejadorImpresora.alinear(ManejadorImpresora.ALINEACION_CENTRO);
            manejadorImpresora.imprimirTexto(respDatafono.getIdCliente().trim(), 0, true, false);
            manejadorImpresora.saltarLinea();

            manejadorImpresora.saltarLinea();
            manejadorImpresora.confirmarComando();
            manejadorImpresora.alimentaPapel();
            manejadorImpresora.cortarPapel();
        }catch (Exception ex){
            msjToast(getResources().getString(R.string.error_impresion_intentar));
            disconnect();
        }
    }

    @Override
    public void sendInputItemAutorizacionSimpleFrament(Boolean aut) {
        if(aut){
            if (isRbBluetooth()) {
                if (etMac.getText().length() > 0) {
                    if (reimprimir) {
                        reimprimir = false;
                        //Ir a la conexion e imprecion
                        conecImprimir();
                    } else {
                        msjToast(getResources().getString(R.string.proceso_curso));
                    }
                } else {
                    reimprimir = true;
                    msjToast(getResources().getString(R.string.selec_impresora));
                }
            } else {
                if (etIp.getText().length() > 0 && etPuerto.getText().length() > 0) {
                    if (reimprimir) {
                        reimprimir = false;
                        //Ir a la conexion e imprecion
                        conecImprimir();
                    } else {
                        msjToast(getResources().getString(R.string.proceso_curso));
                    }
                } else {
                    reimprimir = true;
                    msjToast(getResources().getString(R.string.introducir_puerto_ip));
                }
            }
        }
    }

    public static class ByteHelper {
        public static byte commandByteRepresentation(final int command) {
            final byte[] bytes = ByteBuffer
                    .allocate(4)
                    .putInt(command)
                    .array();
            return bytes[3];
        }
    }

    @Override
    public void sendInputListSelectDeviceBluetoothDialogFragment(String mac) {
        etMac.setText(mac);
        cambioImp = true;
        reimprimir = true;
        setEstatus(getResources().getString(R.string.pulse_impresion), Color.RED);
    }
}
