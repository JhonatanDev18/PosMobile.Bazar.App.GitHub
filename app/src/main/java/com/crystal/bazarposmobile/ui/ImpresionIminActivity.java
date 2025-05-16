package com.crystal.bazarposmobile.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.common.ClaveValorTef;
import com.crystal.bazarposmobile.common.DatafonoTotales;
import com.crystal.bazarposmobile.common.Utilidades;
import com.crystal.bazarposmobile.db.entity.DatafonoEntity;
import com.crystal.bazarposmobile.common.Constantes;
import com.crystal.bazarposmobile.common.SPM;
import com.crystal.bazarposmobile.db.entity.TEFContinguenciaEntity;
import com.crystal.bazarposmobile.retrofit.request.creardocumento.Payment;
import com.crystal.bazarposmobile.retrofit.response.ResponseCupoEmpleado;
import com.crystal.bazarposmobile.retrofit.response.eanes.Producto;
import com.crystal.bazarposmobile.ui.dialogfragmen.AutorizacionSimpleFragment;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.imin.printerlib.IminPrintUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ImpresionIminActivity extends AppCompatActivity
        implements
        View.OnClickListener,
        AutorizacionSimpleFragment.OnInputListener{
    //Comandos de impresion
    private static final byte[] LINE_FEED = {0x0A};
    byte[] GS_COMMAND = {0x1D};
    byte[] GET_HEIGHT_COMMAND_CODE = {0x68};
    byte[] SET_HEIGHT = {0x5A};
    byte[] GET_WIDTH_COMMAND_CODE = {0x77};
    byte[] GET_PRINT_CODE_COMMAND = {0x6B};
    byte[] GET_BARCODE_SYSTEM = {0x45};
    public static final byte[] SET_WIDTH_NORMAL = {0x02};

    //Declaración de los objetos de la interfaz del activity
    Button btncerrar, btnimprimir, btnresumen;
    TextView tvestado;

    //Declaración de la variables del activity
    String pais, tipoCliente, cajeroname, tiendaname, currencyId, vendedorcode, vendedorName,
            tipo_cliente_letra, empresa_cliente, medioscaja, caja, lablePrint,
            numtrans, textoFiscal, cpclConfigLabel, cpclDtoControl, textplazos, textlineastencion,
            clienteName, codigobarras, fechahora, consecutivo, prefijo, tienda, clienteid,cajeroid;
    Double cambio, base_lmp, totalcompra, ivacompra;
    boolean primeraimpresion = true, esFactura;
    SimpleDateFormat currentDate;
    DecimalFormat formatea = new DecimalFormat(Constantes.FORMATO_DECIMAL);
    Date todayDate;
    List<Producto> productoList = new ArrayList<>();
    String separador;
    List<Payment> importesList = new ArrayList<>();
    List<String> plazoslist, lineastencionlist;

    private BluetoothAdapter bluetoothAdapter;

    private IminPrintUtils mIminPrintUtils;
    String cufeHash;
    boolean isClaveValorTef;
    String tituloClaveValor;
    List<ClaveValorTef> claveValorTefList = new ArrayList<>();
    ResponseCupoEmpleado cupoEmpleado;
    Utilidades util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impresion_sumni);
        //Titulo del activity
        this.setTitle(R.string.impresion);

        //Obtener las variables de Intent
        lablePrint = getIntent().getExtras().getString("lablePrint");

        findViews();
        events();

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
            btnimprimir.setText(R.string.factura);
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

        currentDate = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.US);
        todayDate = new Date();
        fechahora = currentDate.format(todayDate);

        //Iniciar la Impresión
        if (checkBluetooth()) {
            iniciarProcesoImpresion();
        }
    }

    private void iniciarProcesoImpresion() {
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Controlador para iniciarSpinnerMenu
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {
                imprimir();
            }
        }, 750);
    }

    //Asignacion de Referencias
    private void findViews() {
        util = new Utilidades();
        separador = Utilidades.separador();
        btnimprimir = this.findViewById(R.id.btnReImprimirIAS);
        btncerrar = this.findViewById(R.id.btnCerrarIAS);
        tvestado = findViewById(R.id.tvEstadoImpresionIAS);
        btnresumen = this.findViewById(R.id.btnResumenIAS);
        btnresumen.setVisibility(View.GONE);
    }

    //Asignacion de eventos
    private void events() {
        btnimprimir.setOnClickListener(this);
        btncerrar.setOnClickListener(this);
        btnresumen.setOnClickListener(this);
    }

    //Limpiar string de todos los acentos y caracteres especiales
    public static String cleanString(String texto) {
        texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
        texto = texto.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return texto;
    }

    private void init() throws IOException {
        mIminPrintUtils = IminPrintUtils.getInstance(ImpresionIminActivity.this);
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice("00:11:22:33:44:55");
        mIminPrintUtils.initPrinter(IminPrintUtils.PrintConnectType.BLUETOOTH, device);
    }

    @SuppressLint("NonConstantResourceId")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            //Realizar la imprecion de la factura
            case R.id.btnReImprimirIAS:
                AutorizacionSimpleFragment autorizacionSimpleFragment = new AutorizacionSimpleFragment();
                autorizacionSimpleFragment.show(getSupportFragmentManager(), "AutorizacionSimpleFragment");
                break;
            case R.id.btnResumenIAS:
                lablePrint = "resumenFac";
                imprimir();
                break;
            case R.id.btnCerrarIAS:
                cerrarImpresion();
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void imprimir() {

        runOnUiThread(new Runnable() {
            public void run() {
                btncerrar.setEnabled(false);
                tvestado.setText(R.string.imprimiendo___);
            }
        });
        setEstado("imprimiendo...");

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
                runOnUiThread(new Runnable() {
                    public void run() {
                        btnresumen.setVisibility(View.VISIBLE);
                    }
                });
                setBtnImprimir(getResources().getString(R.string.re_imprimir));
                break;
            case "prueba":
                pruebaESCPOS();
                setBtnImprimir(getResources().getString(R.string.re_imprimir));
                break;
            case "anulacion":
                anulacionESCPOS();
                setBtnImprimir(getResources().getString(R.string.re_imprimir));
                break;
            case "resumenFac":
                resumenFacturaESCPOS();
                setBtnImprimir(getResources().getString(R.string.re_imprimir));
                lablePrint = "factura";
                break;
            case "tef_cont_cierre_detallado":
                TEFContdetalladoESCPOS();
                setBtnImprimir(getResources().getString(R.string.re_imprimir));
                break;
        }
        primeraimpresion = false;

        runOnUiThread(new Runnable() {
            public void run() {
                btncerrar.setEnabled(true);
                tvestado.setText(R.string.terminado);
            }
        });
        setEstado("Imp. Realizada");
    }

    private void cupoEmpleadoESCPOS() {
        try {
            cupoEmpleado = (ResponseCupoEmpleado) getIntent().getSerializableExtra("cupoEmpleadoBody");

            mIminPrintUtils.setTextTypeface(Typeface.DEFAULT);
            mIminPrintUtils.setAlignment(1);
            mIminPrintUtils.printText("BAZAR PRODUCTORA\nCRYSTAL S.A.S. NIT: 890901672-5\n");

            mIminPrintUtils.setAlignment(0);
            mIminPrintUtils.printText("\nCONSULTA CUPO EMPLEADO\n");
            mIminPrintUtils.printText(separador);

            mIminPrintUtils.printText("CEDULA: " + clienteid + "\n" +
                    "NOMBRE: " + cupoEmpleado.getNombre() + "\n" +
                    "EMPRESA: " + cupoEmpleado.getEmpresa() + "\n" +
                    "CUPO: " + formatea.format(cupoEmpleado.getCupo()) + "\n" +
                    "FECHA: " + util.getThisDateSimple() + "\n" +
                    "HORA: " + util.getThisHora() + "\n" +
                    "TIENDA: " + "Bazar Productora" + "\n" +
                    "CAJERO: " + cajeroname + "\n");
            mIminPrintUtils.printText(separador);

            mIminPrintUtils.printText("\n\nFirma: ________________________________\n");

            mIminPrintUtils.setAlignment(1);
            mIminPrintUtils.printText("\nGRACIAS POR PREFERIRNOS\n");
            mIminPrintUtils.printText("\n\n\n\n\n\n");
        }catch (Exception e){
            msjToast(getResources().getString(R.string.error_impresion_intentar));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constantes.RESP_ACT_BLUE_SELECT) {
            if (checkBluetooth()) {
                try {
                    init();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Controlador para iniciarSpinnerMenu
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void run() {
                        imprimir();
                    }
                }, 750);
            }
        }
    }

    private void cerrarImpresion(){
        mIminPrintUtils.printerPowerOff();
        if (esFactura) {
            //Pasar al activity de Consultar clientes
            Intent intent = new Intent(this, ClienteConsultaActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        finish();
    }

    private boolean checkBluetooth() {
        //Validar que el bluetooth este activo
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null){
            msjToast("Bluetooth no es compatible");
        }else{
            if (bluetoothAdapter.isEnabled()){
                return true;
            }else{
                //Activar el bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent,Constantes.RESP_ACT_BLUE_SELECT);
            }
        }
        return false;
    }

    public void setBtnImprimir(String btntitulo) {
        runOnUiThread(new Runnable() {
            public void run() {
                btnimprimir.setText(btntitulo);
            }
        });
    }

    public void setEstado(final String btntitulo){
        runOnUiThread(new Runnable() {
            public void run() {
                tvestado.setText(btntitulo);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void facturaESCPOS() {
        try{
            //Encabezado
            mIminPrintUtils.setTextTypeface(Typeface.DEFAULT);

            mIminPrintUtils.setAlignment(1);
            mIminPrintUtils.setTextSize(30);
            String esc_pos = "BAZAR PRODUCTORA\n";
            mIminPrintUtils.printText(esc_pos);

            mIminPrintUtils.setTextSize(18);
            esc_pos = "Crystal S.A.S - NIT: 890901672-5\n";
            mIminPrintUtils.printText(esc_pos);

            esc_pos = "Numero de Factura: " + prefijo + "-" + consecutivo + "\n\n";
            mIminPrintUtils.printText(esc_pos);

            if (clienteName.length() > 36) {
                clienteName = clienteName.substring(0, 36);
            }

            //Información
            mIminPrintUtils.setAlignment(0);
            esc_pos = "Emitida: " + fechahora;
            mIminPrintUtils.printText(esc_pos);

            int[] colsWidthArr2 = new int[]{4, 4};
            int[] colsAlign2 = new int[]{0, 2};
            int[] colsSize2 = new int[]{18, 18};
            String[] strings2 = new String[]{"Tienda: " + tienda, "Caja No: " + caja};
            mIminPrintUtils.printColumnsText(strings2, colsWidthArr2, colsAlign2, colsSize2);


            if(!numtrans.equals("-")){
                esc_pos = "Num. Transaccion: " + numtrans + "\n";
                mIminPrintUtils.printText(esc_pos);
            }

            if(SPM.getString(Constantes.CLIENTE_GENERICO).equals(clienteid)){
                esc_pos = "Cliente: "  + "CONSUMIDOR FINAL" + " (222222222222)";
            }else{
                esc_pos = "Cliente: "  + clienteName + " ("+clienteid+")";
            }
            mIminPrintUtils.printText(esc_pos);

            //Encabezado de los productos
            mIminPrintUtils.printText(separador);
            int[] colsWidthArr4 = new int[]{1, 4,2,1};
            int[] colsAlign4 = new int[]{0, 0, 2, 2};
            int[] colsSize4 = new int[]{18, 18, 18, 18};
            String[] strings4 = new String[]{"Cant", "Descripcion", "Valor", "Imp"};
            mIminPrintUtils.printColumnsText(strings4 , colsWidthArr4, colsAlign4, colsSize4);
            mIminPrintUtils.printText(separador);
            colsSize4 = new int[]{16, 16, 18, 18};

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
                if (pNombre.length() > 20) {
                    pNombre = pNombre.substring(0, 20);
                }
                pNombre = cleanString(pNombre);

                String pCodigoTasaImpuesto = p.getCodigoTasaImpuesto();
                if (pCodigoTasaImpuesto.length() > 1) {
                    pCodigoTasaImpuesto = pCodigoTasaImpuesto.substring(0, 1);
                }

                if(p.getQuantity() == 1){
                    strings4 = new String[]{p.getQuantity()+"x", pNombre, formatea.format(p.getPrecio()), "  " + pCodigoTasaImpuesto};
                    mIminPrintUtils.printColumnsText(strings4 , colsWidthArr4, colsAlign4, colsSize4);
                }else{
                    strings4 = new String[]{p.getQuantity()+"x", pNombre, formatea.format(p.getPrecio()), ""};
                    mIminPrintUtils.printColumnsText(strings4 , colsWidthArr4, colsAlign4, colsSize4);

                    strings2 = new String[]{"Valor Final: " + formatea.format(Math.abs(p.getPrecio())*p.getQuantity()), "  " + pCodigoTasaImpuesto};
                    mIminPrintUtils.printColumnsText(strings2, new int[]{7, 1}, new int[]{2, 2}, colsSize2);
                }

            }
            mIminPrintUtils.printText(separador);

            //Totales
            colsSize2 = new int[]{24, 24};
            mIminPrintUtils.setTextStyle(Typeface.BOLD);
            strings2 = new String[]{"TOTAL", formatea.format(total)};
            mIminPrintUtils.printColumnsText(strings2, colsWidthArr2, colsAlign2, colsSize2);
            mIminPrintUtils.setTextStyle(Typeface.NORMAL);
            colsSize2 = new int[]{18, 18};

            if (!primeraimpresion) {
                mIminPrintUtils.setAlignment(1);
                mIminPrintUtils.setTextSize(24);
                mIminPrintUtils.setTextStyle(Typeface.BOLD);
                esc_pos = "DUPLICADO\n";
                mIminPrintUtils.printText(esc_pos);
                mIminPrintUtils.setTextSize(18);
                mIminPrintUtils.setTextStyle(Typeface.NORMAL);
                mIminPrintUtils.setAlignment(0);
            }

            //medios de pagos
            colsWidthArr2 = new int[]{5, 3};
            if (totalcompra.intValue() != 0) {
                for (int i = 0; i < (importesList.size()); i++) {
                    strings2 = new String[]{importesList.get(i).getMethodId().equals("C45") ? importesList.get(i).getName().substring(importesList.get(i).getName().indexOf(" ")+1) : importesList.get(i).getName(), formatea.format(importesList.get(i).getAmount())};
                    mIminPrintUtils.printColumnsText(strings2, colsWidthArr2, colsAlign2, colsSize2);
                }
            }

            mIminPrintUtils.setAlignment(0);
            mIminPrintUtils.setTextStyle(Typeface.BOLD);
            esc_pos = "TOTAL ARTICULOS: " + totalArticulos;
            mIminPrintUtils.printText(esc_pos);
            mIminPrintUtils.setTextStyle(Typeface.NORMAL);

            colsWidthArr4 = new int[]{1, 3,2,2};
            colsAlign4 = new int[]{0, 1, 2, 2};
            colsSize4 = new int[]{16, 16, 16, 16};

            //DISCRIMINACION TARIFAS IVA
            mIminPrintUtils.setTextSize(16);
            mIminPrintUtils.setAlignment(1);
            mIminPrintUtils.setTextStyle(Typeface.BOLD);
            esc_pos = "DISCRIMINACION TARIFAS IVA\n";
            mIminPrintUtils.printText(esc_pos);
            mIminPrintUtils.setTextStyle(Typeface.NORMAL);

            strings4 = new String[]{"Tarifa", "Compra", "Base/Imp", "Iva"};
            mIminPrintUtils.printColumnsText(strings4 , colsWidthArr4, colsAlign4, colsSize4);
            mIminPrintUtils.printText(separador);

            Double totalcompraIva = totalcompra;
            if (!totalcompra.equals(base_lmp + ivacompra)) {
                totalcompraIva = totalcompra - (totalcompra - base_lmp - ivacompra);
            }
            strings4 = new String[]{tarifa + "%", formatea.format(totalcompraIva), formatea.format(base_lmp), formatea.format(ivacompra)};
            mIminPrintUtils.printColumnsText(strings4 , colsWidthArr4, colsAlign4, colsSize4);
            mIminPrintUtils.printText(separador);

            mIminPrintUtils.setAlignment(1);
            mIminPrintUtils.setTextSize(14);
            esc_pos = "MERCANCIA DEL BAZAR NO TIENE CAMBIOS NI DEVOLUCIONES";
            mIminPrintUtils.printText(esc_pos);
            mIminPrintUtils.printText("\n");

            //TEXTO FACTURA ELECTRONICA
            mIminPrintUtils.setAlignment(1);
            mIminPrintUtils.setTextSize(18);
            mIminPrintUtils.setTextStyle(Typeface.BOLD);
            mIminPrintUtils.printText("REPRESENTACION GRAFICA DE SU FACTURA \n ELECTRONICA DE VENTA");
            mIminPrintUtils.printText("\n");
            mIminPrintUtils.setTextStyle(Typeface.NORMAL);

            //TEXTOS CONTRIBUYENTES
            String textcontribuyentes = SPM.getString(Constantes.TEXTOS_CONTRIBUYENTES);
            textcontribuyentes = textcontribuyentes.replace("\n", " ");
            mIminPrintUtils.printText(textcontribuyentes);
            mIminPrintUtils.printText("\n");

            //TEXTO FISCAL
            String textFiscal = textoFiscal;
            textFiscal = textFiscal.replace("  ", " ");
            textFiscal = textFiscal.replace("  ", " ");
            mIminPrintUtils.printText(textFiscal);
            mIminPrintUtils.printText("\n");

            mIminPrintUtils.setAlignment(0);
            mIminPrintUtils.printText("CUFE: " + cufeHash);
            mIminPrintUtils.printText("\n\n");

            mIminPrintUtils.setAlignment(0);
            mIminPrintUtils.printText("Emitida: " + fechahora);
            mIminPrintUtils.printText("\n");

            //QR FACTURA ELECTRONICA
            mIminPrintUtils.setAlignment(1);
            byte[] alignCenter = {0x1B, 0x61, 0x01}; // ESC a 1 (Centro)
            mIminPrintUtils.sendRAWData(alignCenter);

            // Tamaño del módulo QR (1-16)
            int qrTamanoModulo = Math.min(16, Math.max(1, 40 / 10));

            // Comando para establecer el tamaño del módulo QR
            byte[] qrModuleSize = {0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x43, (byte) qrTamanoModulo};
            mIminPrintUtils.sendRAWData(qrModuleSize);

            // Comando para establecer la corrección de errores (0 - baja, 1 - media, 2 - alta, 3 - mejor)
            byte[] qrErrorCorrection = {0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x45, 0x30};
            mIminPrintUtils.sendRAWData(qrErrorCorrection);

            // Texto a imprimir en QR
            byte[] textoBytes = (SPM.getString(Constantes.URL_BASE_FE_QR)+cufeHash).getBytes(StandardCharsets.ISO_8859_1);
            int longitudTexto = textoBytes.length;
            byte pL = (byte) (longitudTexto + 3);
            byte pH = 0x00;

            // Comando para almacenar los datos del QR
            byte[] qrStoreData = new byte[8 + longitudTexto];
            qrStoreData[0] = 0x1D;
            qrStoreData[1] = 0x28;
            qrStoreData[2] = 0x6B;
            qrStoreData[3] = pL;
            qrStoreData[4] = pH;
            qrStoreData[5] = 0x31;
            qrStoreData[6] = 0x50;
            qrStoreData[7] = 0x30;
            System.arraycopy(textoBytes, 0, qrStoreData, 8, longitudTexto);

            mIminPrintUtils.sendRAWData(qrStoreData);

            byte[] qrPrint = {0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x51, 0x30};
            mIminPrintUtils.sendRAWData(qrPrint);
            mIminPrintUtils.printText("\n");

            //TEXTO PROVEEDOR TECNOLOGICO
            mIminPrintUtils.setTextSize(18);
            mIminPrintUtils.setAlignment(0);
            mIminPrintUtils.printText(SPM.getString(Constantes.PROVEEDOR_TECNOLOGICO_FE));
            mIminPrintUtils.printText("\n");

            //CODIGO DE BARRAS
            mIminPrintUtils.setAlignment(1);
            byte[] GET_BARCODE_LENGTH = {ImpresionActivity.ByteHelper.commandByteRepresentation(codigobarras.length())};

            mIminPrintUtils.sendRAWData(GS_COMMAND);
            mIminPrintUtils.sendRAWData(GET_HEIGHT_COMMAND_CODE);
            mIminPrintUtils.sendRAWData(SET_HEIGHT);
            mIminPrintUtils.sendRAWData(GS_COMMAND);
            mIminPrintUtils.sendRAWData(GET_WIDTH_COMMAND_CODE);
            mIminPrintUtils.sendRAWData(SET_WIDTH_NORMAL);
            mIminPrintUtils.sendRAWData(GS_COMMAND);
            mIminPrintUtils.sendRAWData(GET_PRINT_CODE_COMMAND);
            mIminPrintUtils.sendRAWData(GET_BARCODE_SYSTEM);
            mIminPrintUtils.sendRAWData(GET_BARCODE_LENGTH);

            for (int i = 0; i < codigobarras.length(); i++) {
                mIminPrintUtils.sendRAWData((codigobarras.charAt(i) + "").getBytes());
            }
            mIminPrintUtils.printText(codigobarras);
            mIminPrintUtils.printText("\n\n\n\n\n\n\n");
        }catch (Exception e){
            Utilidades.mjsToast(e.getMessage(), Constantes.TOAST_TYPE_ERROR, Toast.LENGTH_LONG,
                    ImpresionIminActivity.this);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void resumenFacturaESCPOS() {

        //Encabezado
        mIminPrintUtils.setTextTypeface(Typeface.DEFAULT);

        mIminPrintUtils.setAlignment(1);
        mIminPrintUtils.setTextSize(24);
        String esc_pos = "BAZAR PRODUCTORA - RESUMEN\n";
        mIminPrintUtils.printText(esc_pos);

        mIminPrintUtils.setAlignment(0);
        mIminPrintUtils.setTextSize(18);
        if (clienteName.length() > 36) {
            clienteName = clienteName.substring(0, 36);
        }
        if(SPM.getString(Constantes.CLIENTE_GENERICO).equals(clienteid)){
            esc_pos = "Cliente: "  + clienteName;
        }else{
            esc_pos = "Cliente: "  + clienteName + " ("+clienteid+")";
        }
        mIminPrintUtils.printText(esc_pos);

        //Información
        esc_pos = "Emitida: " + fechahora;
        mIminPrintUtils.printText(esc_pos);

        int[] colsWidthArr2 = new int[]{4, 4};
        int[] colsAlign2 = new int[]{0, 2};
        int[] colsSize2 = new int[]{18, 18};
        String[] strings2 = new String[]{"Tienda: " + tienda, "Caja No: " + caja};
        mIminPrintUtils.printColumnsText(strings2, colsWidthArr2, colsAlign2, colsSize2);

        if(!numtrans.equals("-")) {
            esc_pos = "Num. Transaccion: " + numtrans + "\n";
            mIminPrintUtils.printText(esc_pos);
        }

        if(SPM.getString(Constantes.CLIENTE_GENERICO).equals(clienteid)){
            esc_pos = "Cliente: "  + clienteName;
        }else{
            esc_pos = "Cliente: "  + clienteName + " ("+clienteid+")";
        }
        mIminPrintUtils.printText(esc_pos);

        //Encabezado de los productos
        String[] strings3 = new String[]{"Cant", "Descripcion", "Valor"};
        int[] colsWidthArr3 = new int[]{1, 4, 3};
        int[] colsAlign3 = new int[]{0,0, 2};
        int[] colsSize3 = new int[]{16, 16, 16};
        mIminPrintUtils.printColumnsText(strings3, colsWidthArr3, colsAlign3, colsSize3);

        Double total = 0.0;

        String tarifa = SPM.getString(Constantes.TARIFA_IVA);
        Integer totalArticulos = 0;

        //Los productos
        for (Producto p : productoList) {
            totalArticulos = totalArticulos + p.getQuantity();
            total = total + (Math.abs(p.getPrecio())*p.getQuantity());
            String pNombre = p.getNombre();

            if (pNombre.length() > 20) {
                pNombre = pNombre.substring(0, 20);
            }
            pNombre = cleanString(pNombre);

            strings3 = new String[]{p.getQuantity()+"x", pNombre, formatea.format(Math.abs(p.getPrecio())*p.getQuantity())};
            mIminPrintUtils.printColumnsText(strings3, colsWidthArr3, colsAlign3, colsSize3);

        }

        //Totales
        mIminPrintUtils.setTextStyle(Typeface.BOLD);
        esc_pos = "TOTAL: " + formatea.format(total) + " ("+totalArticulos+" ARTICULOS)\n";
        mIminPrintUtils.printText(esc_pos);
        mIminPrintUtils.setTextStyle(Typeface.NORMAL);

        //medios de pagos
        colsWidthArr2 = new int[]{5, 3};
        if (totalcompra.intValue() != 0) {
            for (int i = 0; i < (importesList.size()); i++) {
                strings2 = new String[]{importesList.get(i).getName(), formatea.format(importesList.get(i).getAmount())};
                mIminPrintUtils.printColumnsText(strings2, colsWidthArr2, colsAlign2, colsSize2);
            }
        }

        int[] colsWidthArr4 = new int[]{1, 3,2,2};
        int[] colsAlign4 = new int[]{0, 1, 2, 2};
        int[] colsSize4 = new int[]{16, 16, 16, 16};

        //DISCRIMINACION TARIFAS IVA
        mIminPrintUtils.setAlignment(1);
        mIminPrintUtils.setTextSize(16);
        mIminPrintUtils.setTextStyle(Typeface.BOLD);
        esc_pos = "DISCRIMINACION TARIFAS IVA\n";
        mIminPrintUtils.printText(esc_pos);
        mIminPrintUtils.setTextStyle(Typeface.NORMAL);

        String[] strings4 = new String[]{"Tarifa", "Compra", "Base/Imp", "Iva"};
        mIminPrintUtils.printColumnsText(strings4 , colsWidthArr4, colsAlign4, colsSize4);

        Double totalcompraIva = totalcompra;
        if (!totalcompra.equals(base_lmp + ivacompra)) {
            totalcompraIva = totalcompra - (totalcompra - base_lmp - ivacompra);
        }
        strings4 = new String[]{tarifa + "%", formatea.format(totalcompraIva), formatea.format(base_lmp), formatea.format(ivacompra)};
        mIminPrintUtils.printColumnsText(strings4 , colsWidthArr4, colsAlign4, colsSize4);

        mIminPrintUtils.printText("\n\n\n\n\n\n\n");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void comprobanteTefESCPOS() {
        if(isClaveValorTef){
            mIminPrintUtils.setTextTypeface(Typeface.DEFAULT);
            mIminPrintUtils.setAlignment(1);
            mIminPrintUtils.setTextSize(30);
            mIminPrintUtils.printText(tituloClaveValor);
            mIminPrintUtils.printText("\n");
            mIminPrintUtils.setTextSize(18);

            for(ClaveValorTef claveValor : claveValorTefList){
                mIminPrintUtils.setAlignment(0);
                mIminPrintUtils.printText(claveValor.getClave() +" "+ claveValor.getValor()+"\n");
            }

            mIminPrintUtils.printText("\n\n\n\n\n\n");
        }else{
            DatafonoEntity respDatafono = (DatafonoEntity) getIntent().getSerializableExtra("respDatafono");
            assert respDatafono != null;

            mIminPrintUtils.setTextTypeface(Typeface.DEFAULT);
            mIminPrintUtils.setAlignment(1);
            mIminPrintUtils.setTextSize(30);
            String esc_pos = "CREDIBANCO\n";
            mIminPrintUtils.setTextStyle(Typeface.BOLD);
            mIminPrintUtils.printText(esc_pos);

            mIminPrintUtils.setTextSize(18);
            esc_pos = respDatafono.getTiendaNombre().trim();
            mIminPrintUtils.printText(esc_pos);
            mIminPrintUtils.printText("\n");

            esc_pos = respDatafono.getDirEstablecimiento();
            mIminPrintUtils.setTextStyle(Typeface.NORMAL);
            mIminPrintUtils.printText(esc_pos);
            mIminPrintUtils.printText("\n");

            esc_pos = "CU: " + respDatafono.getCodigoComercio().trim();
            mIminPrintUtils.printText(esc_pos);
            mIminPrintUtils.printText("\n");

            String sDate1 = respDatafono.getFecha() + "-" + respDatafono.getHora();
            Date dateD = null;
            try {
                dateD = new SimpleDateFormat("yyMMdd-HHmmss").parse(sDate1);
                sDate1 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.US).format(dateD);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            esc_pos = " Fecha: " + sDate1;
            mIminPrintUtils.printText(esc_pos);
            mIminPrintUtils.printText("\n");

            esc_pos = respDatafono.getHash();
            mIminPrintUtils.printText(esc_pos);
            mIminPrintUtils.printText("\n");

            String[] strings2;
            int[] colsWidthArr2 = new int[]{4, 4};
            int[] colsAlign2 = new int[]{0, 2};
            int[] colsSize2 = new int[]{18, 18};

            strings2 = new String[]{"TER: " + respDatafono.getCodigoTerminal(), "AUT: " + respDatafono.getCodigoAprobacion()};
            mIminPrintUtils.printColumnsText(strings2, colsWidthArr2, colsAlign2, colsSize2);
            strings2 = new String[]{respDatafono.getFranquicia().trim(), respDatafono.getTipoCuenta()};
            mIminPrintUtils.printColumnsText(strings2, colsWidthArr2, colsAlign2, colsSize2);
            strings2 = new String[]{"****" + respDatafono.getUltDigitoTarj(), ""};
            mIminPrintUtils.printColumnsText(strings2, colsWidthArr2, colsAlign2, colsSize2);
            strings2 = new String[]{"Recibo: " + respDatafono.getNumRecibo(), "TVR: " + respDatafono.getTvr()};
            mIminPrintUtils.printColumnsText(strings2, colsWidthArr2, colsAlign2, colsSize2);

            if (respDatafono.getTipoCuenta().equals("CR")) {
                strings2 = new String[]{"Cuotas: " + respDatafono.getCuotas(), "TSI: " + respDatafono.getTsi()};
            } else {
                strings2 = new String[]{"", "TSI: " + respDatafono.getTsi()};
            }
            mIminPrintUtils.printColumnsText(strings2, colsWidthArr2, colsAlign2, colsSize2);

            strings2 = new String[]{"RRN: " + respDatafono.getRrn(), respDatafono.getAid()};
            mIminPrintUtils.printColumnsText(strings2, colsWidthArr2, colsAlign2, colsSize2);

            esc_pos = "Criptograma: " + respDatafono.getCriptograma();
            mIminPrintUtils.printText(esc_pos);
            mIminPrintUtils.printText("\n");

            int total = respDatafono.getMonto().intValue();
            int iva = respDatafono.getIva().intValue();
            int compra_neta = total - iva;

            strings2 = new String[]{"COMPRA NETA", formatea.format(compra_neta)};
            mIminPrintUtils.printColumnsText(strings2, colsWidthArr2, colsAlign2, colsSize2);

            strings2 = new String[]{"IVA", formatea.format(iva)};
            mIminPrintUtils.printColumnsText(strings2, colsWidthArr2, colsAlign2, colsSize2);

            strings2 = new String[]{"IAC", formatea.format(respDatafono.getIac())};
            mIminPrintUtils.printColumnsText(strings2, colsWidthArr2, colsAlign2, colsSize2);

            mIminPrintUtils.setTextStyle(Typeface.BOLD);
            strings2 = new String[]{"TOTAL", formatea.format(total)};
            mIminPrintUtils.printColumnsText(strings2, colsWidthArr2, colsAlign2, colsSize2);
            mIminPrintUtils.setTextStyle(Typeface.NORMAL);
            mIminPrintUtils.setAlignment(1);

            if (!primeraimpresion) {
                mIminPrintUtils.setTextSize(24);
                mIminPrintUtils.setTextStyle(Typeface.BOLD);
                esc_pos = "DUPLICADO\n";
                mIminPrintUtils.printText(esc_pos);
                mIminPrintUtils.setTextSize(18);
                mIminPrintUtils.setTextStyle(Typeface.NORMAL);
            }

            if (respDatafono.getTipoCuenta().equals("CR")) {

                mIminPrintUtils.printText(separador);

                String textpagare = SPM.getString(Constantes.TEXTOS_PAGARE_TEF);
                textpagare = cleanString(textpagare.replace("\n", " "));
                mIminPrintUtils.setTextSize(13);
                mIminPrintUtils.printText(textpagare);
                mIminPrintUtils.printText("\n\n");

                mIminPrintUtils.setAlignment(0);
                mIminPrintUtils.setTextSize(18);
                esc_pos = "Firma: ________________________________\n\n";
                mIminPrintUtils.printText(esc_pos);

                esc_pos = "C.C: __________________________________\n\n";
                mIminPrintUtils.printText(esc_pos);

                esc_pos = "Tel: __________________________________\n";
                mIminPrintUtils.printText(esc_pos);
            }

            mIminPrintUtils.setAlignment(1);
            mIminPrintUtils.setTextStyle(Typeface.BOLD);
            esc_pos = respDatafono.getIdCliente().trim();
            mIminPrintUtils.printText(esc_pos);
            mIminPrintUtils.setTextStyle(Typeface.NORMAL);
            mIminPrintUtils.printText("\n");

            mIminPrintUtils.printText("\n\n\n\n\n\n");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void TEFContdetalladoESCPOS() {

        List<TEFContinguenciaEntity> tefContList = (ArrayList<TEFContinguenciaEntity>) getIntent().getSerializableExtra("tefContList");

        //Encabezado
        mIminPrintUtils.setTextTypeface(Typeface.DEFAULT);
        mIminPrintUtils.setAlignment(1);
        mIminPrintUtils.setTextSize(30);
        String esc_pos = "BAZAR PRODUCTORA\n";
        mIminPrintUtils.printText(esc_pos);

        mIminPrintUtils.setAlignment(0);
        mIminPrintUtils.setTextSize(18);
        esc_pos = "Crystal S.A.S - TEF CONTINGENCIA";
        mIminPrintUtils.printText(esc_pos);
        mIminPrintUtils.printText("\n");

        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.US);
        Date todayDate = new Date();
        String fecha = currentDate.format(todayDate);
        esc_pos = "Fecha: "+fecha;
        mIminPrintUtils.printText(esc_pos);
        mIminPrintUtils.printText("\n");

        mIminPrintUtils.setAlignment(1);
        mIminPrintUtils.setTextStyle(Typeface.BOLD);
        esc_pos = "INFORME DETALLADO TEF CONT.\n";
        mIminPrintUtils.printText(esc_pos);
        mIminPrintUtils.setTextStyle(Typeface.NORMAL);

        String[] strings2 = new String[]{"FECHA", "MONTO"};
        int[] colsWidthArr2 = new int[]{4, 4};
        int[] colsAlign2 = new int[]{0, 2};
        int[] colsSize2 = new int[]{18, 18};
        mIminPrintUtils.printColumnsText(strings2, colsWidthArr2, colsAlign2, colsSize2);

        String franquiciaUso = null, tipoCuentaUso = null;

        for (TEFContinguenciaEntity rd : tefContList) {
            franquiciaUso = rd.getFranquicia();
            tipoCuentaUso = rd.getTipoCuenta();
            break;
        }

        esc_pos = cleanString(franquiciaUso.trim() + " " + tipoCuentaUso);
        mIminPrintUtils.printText(esc_pos);
        mIminPrintUtils.printText("\n");

        //Recorremos el cursor hasta que no haya más registros
        for (TEFContinguenciaEntity rd : tefContList) {
            String franquicia = rd.getFranquicia();
            String tipoCuenta = rd.getTipoCuenta();
            Double monto = rd.getMonto();
            String fechaO = rd.getFecha();
            fechaO = fechaO.substring(0,16);

            if (!franquiciaUso.equals(franquicia) || !tipoCuentaUso.equals(tipoCuenta)) {
                mIminPrintUtils.printText(separador);
                esc_pos = cleanString(franquicia.trim() + " " + tipoCuenta);
                mIminPrintUtils.printText(esc_pos);
                mIminPrintUtils.printText("\n");
            }
            franquiciaUso = franquicia;
            tipoCuentaUso = tipoCuenta;

            strings2 = new String[]{fechaO, formatea.format(monto)};
            mIminPrintUtils.printColumnsText(strings2, colsWidthArr2, colsAlign2, colsSize2);
        }
        mIminPrintUtils.printText("\n\n\n\n\n\n\n");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void TEFdetalladoESCPOS() {

        List<DatafonoEntity> listRDDetallado = (ArrayList<DatafonoEntity>) getIntent().getSerializableExtra("listRDDetallado");
        if (listRDDetallado.size() > 0) {

            String codigoComercio = getIntent().getExtras().getString("codigoComercio");
            String dirEstable = getIntent().getExtras().getString("dirEstable");
            String codTerminal = getIntent().getExtras().getString("codTerminal");

            //Cabecera
            mIminPrintUtils.setTextTypeface(Typeface.DEFAULT);
            mIminPrintUtils.setAlignment(1);
            mIminPrintUtils.setTextSize(30);
            String esc_pos = "CREDIBANCO\n";
            mIminPrintUtils.printText(esc_pos);

            SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.US);
            Date todayDate = new Date();
            mIminPrintUtils.setTextSize(18);
            String fecha = currentDate.format(todayDate);
            mIminPrintUtils.printText(fecha);
            mIminPrintUtils.printText("\n");

            String[] strings2;
            int[] colsWidthArr2 = new int[]{4, 4};
            int[] colsAlign2 = new int[]{0, 2};
            int[] colsSize2 = new int[]{18, 18};
            strings2 = new String[]{codigoComercio, "Crystal S.A.S"};
            mIminPrintUtils.printColumnsText(strings2, colsWidthArr2, colsAlign2, colsSize2);

            mIminPrintUtils.printText(dirEstable);
            mIminPrintUtils.printText("\n");
            mIminPrintUtils.printText("TER: " + codTerminal);
            mIminPrintUtils.printText("\n");

            mIminPrintUtils.setTextStyle(Typeface.BOLD);
            esc_pos = "INFORME DETALLADO\n";
            mIminPrintUtils.printText(esc_pos);
            mIminPrintUtils.setTextStyle(Typeface.NORMAL);


            String[] strings5;
            int[] colsWidthArr5 = new int[]{1, 2,2,1,2};
            int[] colsAlign5 = new int[]{0, 0, 0, 0, 2};
            int[] colsSize5 = new int[]{16, 16, 16, 16, 16};

            strings5 = new String[]{"TRAN", "TARJETA", "RECIBO", "TIPO", "MONTO"};
            mIminPrintUtils.printColumnsText(strings5, colsWidthArr5, colsAlign5, colsSize5);
            colsWidthArr5 = new int[]{1, 1,2,1,3};
            colsAlign5 = new int[]{0, 0, 2, 2, 2};

            String franquiciaUso = null, tipoCuentaUso = null;

            for (DatafonoEntity rd : listRDDetallado) {
                franquiciaUso = rd.getFranquicia();
                tipoCuentaUso = rd.getTipoCuenta();
                break;
            }

            esc_pos = franquiciaUso.trim() + " " + tipoCuentaUso;
            mIminPrintUtils.printText(esc_pos);
            mIminPrintUtils.printText("\n");

            //Recorremos el cursor hasta que no haya más registros
            for (DatafonoEntity rd : listRDDetallado) {
                String franquicia = rd.getFranquicia();
                Double monto = rd.getMonto();
                String tipoCuenta = rd.getTipoCuenta();
                String numRecibo = rd.getNumRecibo();
                String ultDigitoTarj = rd.getUltDigitoTarj();
                String tipoOperacion = rd.getDc().trim();

                if (!franquiciaUso.equals(franquicia) || !tipoCuentaUso.equals(tipoCuenta)) {
                    mIminPrintUtils.printText(separador);
                    esc_pos = franquicia.trim() + " " + tipoCuenta;
                    mIminPrintUtils.printText(esc_pos);
                    mIminPrintUtils.printText("\n");
                }
                franquiciaUso = franquicia;
                tipoCuentaUso = tipoCuenta;

                if (tipoOperacion.equals(Constantes.COD_ANULACION)) {
                    strings5 = new String[]{tipoOperacion, ultDigitoTarj, numRecibo, tipoCuenta, formatea.format(monto)};
                    mIminPrintUtils.printColumnsText(strings5, colsWidthArr5, colsAlign5, colsSize5);

                } else {
                    strings5 = new String[]{tipoOperacion, ultDigitoTarj, numRecibo, tipoCuenta, formatea.format(monto)};
                    mIminPrintUtils.printColumnsText(strings5, colsWidthArr5, colsAlign5, colsSize5);
                }
            }
            mIminPrintUtils.printText("\n\n\n\n\n\n\n");
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void TEFtotalesESCPOS() {
        //Imprimir
        List<DatafonoTotales> listRDTotales = (ArrayList<DatafonoTotales>) getIntent().getSerializableExtra("listRDTotales");
        List<DatafonoTotales> listRDTotalesCanceladas = (ArrayList<DatafonoTotales>) getIntent().getSerializableExtra("listRDTotalesCanceladas");

        if (listRDTotales.size() > 0) {

            String codigoComercio = getIntent().getExtras().getString("codigoComercio");
            String dirEstable = getIntent().getExtras().getString("dirEstable");
            String codTerminal = getIntent().getExtras().getString("codTerminal");

            //Cabecera
            mIminPrintUtils.setTextTypeface(Typeface.DEFAULT);
            mIminPrintUtils.setAlignment(1);
            mIminPrintUtils.setTextSize(30);
            String esc_pos = "CREDIBANCO\n";
            mIminPrintUtils.printText(esc_pos);

            SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.US);
            Date todayDate = new Date();
            String fecha = currentDate.format(todayDate);
            mIminPrintUtils.setTextSize(18);
            mIminPrintUtils.printText(fecha);
            mIminPrintUtils.printText("\n");

            String[] strings2;
            int[] colsWidthArr2 = new int[]{4, 4};
            int[] colsAlign2 = new int[]{0, 2};
            int[] colsSize2 = new int[]{18, 18};
            strings2 = new String[]{codigoComercio, "Crystal S.A.S"};
            mIminPrintUtils.printColumnsText(strings2, colsWidthArr2, colsAlign2, colsSize2);

            mIminPrintUtils.printText(dirEstable);
            mIminPrintUtils.printText("\n");
            mIminPrintUtils.printText("TER: " + codTerminal);
            mIminPrintUtils.printText("\n");

            mIminPrintUtils.setTextStyle(Typeface.BOLD);
            esc_pos = "TOTAL POR ENTIDAD\n";
            mIminPrintUtils.printText(esc_pos);
            mIminPrintUtils.setTextStyle(Typeface.NORMAL);

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

            String[] strings3;
            int[] colsWidthArr3 = new int[]{3, 1,4};
            int[] colsAlign3 = new int[]{0, 0, 2};
            int[] colsSize3 = new int[]{18, 18, 18};

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

                if (listRDTotalesCanceladas.size() > 0) {
                    for (DatafonoTotales rdCan : listRDTotalesCanceladas) {
                        if (franquicia.equals(rdCan.getFranquicia()) && tipoCuenta.equals(rdCan.getTipoCuenta())) {
                            cancelada = rdCan.getMonto() * -1;
                            canceladaCount = rdCan.getTipoCuentaCount();
                        }
                    }
                }

                //Total Franquicia
                if (!franquiciaUso.equals(franquicia)) {
                    esc_pos = "TOTAL " + franquiciaUso.trim();
                    mIminPrintUtils.printText(esc_pos);
                    mIminPrintUtils.printText("\n");

                    strings3 = new String[]{"COMPRAS",":" + numElementosF,formatea.format(montoF - ivaF)};
                    mIminPrintUtils.printColumnsText(strings3, colsWidthArr3, colsAlign3, colsSize3);
                    strings3 = new String[]{"IVA",":",formatea.format(ivaF)};
                    mIminPrintUtils.printColumnsText(strings3, colsWidthArr3, colsAlign3, colsSize3);
                    strings3 = new String[]{"PROPINA",":",formatea.format(propinaF)};
                    mIminPrintUtils.printColumnsText(strings3, colsWidthArr3, colsAlign3, colsSize3);
                    mIminPrintUtils.printText(separador);
                    strings3 = new String[]{"SUBTOTAL",":" + numElementosF,formatea.format(montoF)};
                    mIminPrintUtils.printColumnsText(strings3, colsWidthArr3, colsAlign3, colsSize3);
                    strings3 = new String[]{"CANCELADAS",":" + canceladaCountF,"-" + formatea.format(canceladaF)};
                    mIminPrintUtils.printColumnsText(strings3, colsWidthArr3, colsAlign3, colsSize3);
                    mIminPrintUtils.printText(separador);

                    montoF = 0.0;
                    ivaF = 0.0;
                    propinaF = 0.0;
                    numElementosF = 0;
                    canceladaF = 0.0;
                    canceladaCountT = 0;
                }
                franquiciaUso = franquicia;


                esc_pos = franquicia.trim() + " " + tipoCuenta;
                mIminPrintUtils.printText(esc_pos);
                mIminPrintUtils.printText("\n");

                strings3 = new String[]{"COMPRAS",":" + numElementosF,formatea.format(monto - iva)};
                mIminPrintUtils.printColumnsText(strings3, colsWidthArr3, colsAlign3, colsSize3);
                strings3 = new String[]{"IVA",":",formatea.format(iva)};
                mIminPrintUtils.printColumnsText(strings3, colsWidthArr3, colsAlign3, colsSize3);
                strings3 = new String[]{"PROPINA",":",formatea.format(propina)};
                mIminPrintUtils.printColumnsText(strings3, colsWidthArr3, colsAlign3, colsSize3);
                mIminPrintUtils.printText(separador);
                strings3 = new String[]{"SUBTOTAL",":" + numElementosF,formatea.format(monto)};
                mIminPrintUtils.printColumnsText(strings3, colsWidthArr3, colsAlign3, colsSize3);
                strings3 = new String[]{"CANCELADAS",":" + canceladaCount,"-" + formatea.format(cancelada)};
                mIminPrintUtils.printColumnsText(strings3, colsWidthArr3, colsAlign3, colsSize3);
                mIminPrintUtils.printText(separador);

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

                    esc_pos = "TOTAL " + franquiciaUso.trim();
                    mIminPrintUtils.printText(esc_pos);
                    mIminPrintUtils.printText("\n");

                    strings3 = new String[]{"COMPRAS",":" + numElementosF,formatea.format(montoF - ivaF)};
                    mIminPrintUtils.printColumnsText(strings3, colsWidthArr3, colsAlign3, colsSize3);
                    strings3 = new String[]{"IVA",":",formatea.format(ivaF)};
                    mIminPrintUtils.printColumnsText(strings3, colsWidthArr3, colsAlign3, colsSize3);
                    strings3 = new String[]{"PROPINA",":",formatea.format(propinaF)};
                    mIminPrintUtils.printColumnsText(strings3, colsWidthArr3, colsAlign3, colsSize3);
                    mIminPrintUtils.printText(separador);
                    strings3 = new String[]{"SUBTOTAL",":" + numElementosF,formatea.format(montoF)};
                    mIminPrintUtils.printColumnsText(strings3, colsWidthArr3, colsAlign3, colsSize3);
                    strings3 = new String[]{"CANCELADAS",":" + canceladaCountF,"-" + formatea.format(canceladaF)};
                    mIminPrintUtils.printColumnsText(strings3, colsWidthArr3, colsAlign3, colsSize3);
                    mIminPrintUtils.printText(separador);
                }
            }

            //GRAN TOTAL
            esc_pos = "\nGRAN TOTAL\n";
            mIminPrintUtils.printText(esc_pos);

            strings3 = new String[]{"COMPRAS",":" + numElementosT,formatea.format(montoT - ivaT)};
            mIminPrintUtils.printColumnsText(strings3, colsWidthArr3, colsAlign3, colsSize3);
            strings3 = new String[]{"IVA",":",formatea.format(ivaT)};
            mIminPrintUtils.printColumnsText(strings3, colsWidthArr3, colsAlign3, colsSize3);
            strings3 = new String[]{"PROPINA",":",formatea.format(propinaT)};
            mIminPrintUtils.printColumnsText(strings3, colsWidthArr3, colsAlign3, colsSize3);
            mIminPrintUtils.printText(separador);
            strings3 = new String[]{"SUBTOTAL",":" + numElementosF,formatea.format(montoT)};
            mIminPrintUtils.printColumnsText(strings3, colsWidthArr3, colsAlign3, colsSize3);
            strings3 = new String[]{"CANCELADAS",":" + canceladaCountT,"-" + formatea.format(canceladaT)};
            mIminPrintUtils.printColumnsText(strings3, colsWidthArr3, colsAlign3, colsSize3);
            mIminPrintUtils.printText(separador);
            mIminPrintUtils.printText("\n\n\n\n\n\n");

        }
    }

    private void pruebaESCPOS() {
        mIminPrintUtils.setAlignment(1);
        String esc_pos = "Crystal S.A.S\n";
        mIminPrintUtils.printText(esc_pos,1);
        try {
            mIminPrintUtils.setBarCodeWidth(3);
            mIminPrintUtils.setBarCodeContentPrintPos(2);
            mIminPrintUtils.printBarCode(4, "PRUEBA",1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("logcat",e.getMessage());
        }
        esc_pos = "*Prueba Realizada*\n";
        mIminPrintUtils.printText(esc_pos);
        mIminPrintUtils.printAndFeedPaper(120);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void anulacionESCPOS() {
        DatafonoEntity respDatafono = (DatafonoEntity) getIntent().getSerializableExtra("respDatafono");

        mIminPrintUtils.setTextTypeface(Typeface.DEFAULT);
        mIminPrintUtils.setAlignment(1);
        mIminPrintUtils.setTextSize(30);
        String esc_pos = "CREDIBANCO\n";
        mIminPrintUtils.setTextStyle(Typeface.BOLD);
        mIminPrintUtils.printText(esc_pos);

        mIminPrintUtils.setTextSize(18);
        esc_pos = respDatafono.getTiendaNombre().trim();
        mIminPrintUtils.printText(esc_pos);
        mIminPrintUtils.printText("\n");

        esc_pos = respDatafono.getDirEstablecimiento();
        mIminPrintUtils.setTextStyle(Typeface.NORMAL);
        mIminPrintUtils.printText(esc_pos);
        mIminPrintUtils.printText("\n");

        esc_pos = "CU: " + respDatafono.getCodigoComercio().trim();
        mIminPrintUtils.printText(esc_pos);
        mIminPrintUtils.printText("\n");

        String sDate1 = respDatafono.getFecha() + "-" + respDatafono.getHora();
        Date dateD = null;
        try {
            dateD = new SimpleDateFormat("yyMMdd-HHmmss").parse(sDate1);
            sDate1 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.US).format(dateD);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        esc_pos = " Fecha: " + sDate1;
        mIminPrintUtils.printText(esc_pos);
        mIminPrintUtils.printText("\n");

        esc_pos = respDatafono.getHash();
        mIminPrintUtils.printText(esc_pos);
        mIminPrintUtils.printText("\n");

        String[] strings2;
        int[] colsWidthArr2 = new int[]{4, 4};
        int[] colsAlign2 = new int[]{0, 2};
        int[] colsSize2 = new int[]{18, 18};

        strings2 = new String[]{"TER: " + respDatafono.getCodigoTerminal(), "AUT: " + respDatafono.getCodigoAprobacion()};
        mIminPrintUtils.printColumnsText(strings2, colsWidthArr2, colsAlign2, colsSize2);

        strings2 = new String[]{respDatafono.getFranquicia().trim(), respDatafono.getTipoCuenta()};
        mIminPrintUtils.printColumnsText(strings2, colsWidthArr2, colsAlign2, colsSize2);

        strings2 = new String[]{"****" + respDatafono.getUltDigitoTarj(), ""};
        mIminPrintUtils.printColumnsText(strings2, colsWidthArr2, colsAlign2, colsSize2);

        strings2 = new String[]{"Recibo: " + respDatafono.getNumRecibo(), "RRN: " + respDatafono.getRrn()};
        mIminPrintUtils.printColumnsText(strings2, colsWidthArr2, colsAlign2, colsSize2);

        int total = respDatafono.getMonto().intValue();
        int iva = respDatafono.getIva().intValue();
        int compra_neta = total - iva;

        strings2 = new String[]{"COMPRA NETA", formatea.format(compra_neta)};
        mIminPrintUtils.printColumnsText(strings2, colsWidthArr2, colsAlign2, colsSize2);

        mIminPrintUtils.setTextStyle(Typeface.BOLD);
        strings2 = new String[]{"TOTAL", formatea.format(total)};
        mIminPrintUtils.printColumnsText(strings2, colsWidthArr2, colsAlign2, colsSize2);
        mIminPrintUtils.setTextStyle(Typeface.NORMAL);

        mIminPrintUtils.printText(separador);

        mIminPrintUtils.setTextSize(24);
        mIminPrintUtils.setTextStyle(Typeface.BOLD);
        esc_pos = "ANULACION\n";
        mIminPrintUtils.printText(esc_pos);
        mIminPrintUtils.setTextSize(18);
        mIminPrintUtils.setTextStyle(Typeface.NORMAL);

        mIminPrintUtils.printText(separador);

        if (!primeraimpresion) {
            mIminPrintUtils.setTextSize(24);
            mIminPrintUtils.setTextStyle(Typeface.BOLD);
            esc_pos = "DUPLICADO\n";
            mIminPrintUtils.printText(esc_pos);
            mIminPrintUtils.setTextSize(18);
            mIminPrintUtils.setTextStyle(Typeface.NORMAL);
        }
        mIminPrintUtils.printText("\n");
        mIminPrintUtils.setAlignment(0);
        mIminPrintUtils.setTextSize(18);
        esc_pos = "Firma: ________________________________\n\n";
        mIminPrintUtils.printText(esc_pos);

        esc_pos = "C.C: __________________________________\n\n";
        mIminPrintUtils.printText(esc_pos);

        esc_pos = "Tel: __________________________________\n";
        mIminPrintUtils.printText(esc_pos);

        mIminPrintUtils.setAlignment(1);
        mIminPrintUtils.setTextStyle(Typeface.BOLD);
        esc_pos = respDatafono.getIdCliente().trim();
        mIminPrintUtils.printText(esc_pos);
        mIminPrintUtils.setTextStyle(Typeface.NORMAL);
        mIminPrintUtils.printText("\n");

        mIminPrintUtils.printText("\n\n\n\n\n\n");
        
    }
    @Override
    public void onBackPressed() {
        msjToast("Use el boton cerrar, para salir de la impresión");
    }

    private void msjToast(String msj) {
        Toast.makeText(this, msj, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sendInputItemAutorizacionSimpleFrament(Boolean aut) {
        if(aut){
            imprimir();
        }
    }
}
