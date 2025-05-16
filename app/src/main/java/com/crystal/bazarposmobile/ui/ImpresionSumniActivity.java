package com.crystal.bazarposmobile.ui;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
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
import com.crystal.bazarposmobile.utilsSunmi.SunmiPrintHelper;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ImpresionSumniActivity extends AppCompatActivity
        implements
        View.OnClickListener,
        AutorizacionSimpleFragment.OnInputListener {

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
    List<Producto> listProductos = new ArrayList<Producto>();
    List<Producto> lpPD = new ArrayList<Producto>();
    List<Payment> listImportes = new ArrayList<Payment>();
    List<String> plazoslist, lineastencionlist;
    String cufeHash;
    private BluetoothAdapter bluetoothAdapter;

    //Comandos de impresion
    private static final byte[] LINE_FEED = {0x0A};

    byte[] LINEA_PUN = "--------------------------------".getBytes();
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
            listProductos = (ArrayList<Producto>) getIntent().getSerializableExtra("listProductos");
            listImportes = (ArrayList<Payment>) getIntent().getSerializableExtra("listImportes");

            lpPD = (ArrayList<Producto>) getIntent().getSerializableExtra("lpPD");

            base_lmp = 0.0;
            ivacompra = 0.0;
            for (Producto p : listProductos) {
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
        //Iniciar la Impresión
        init();

        //Controlador para iniciarSpinnerMenu
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {
                try {
                    imprimir();
                } catch (Exception e) {
                    Utilidades.mjsToast(e.getMessage(), Constantes.TOAST_TYPE_ERROR, Toast.LENGTH_LONG,
                            ImpresionSumniActivity.this);
                }
            }
        }, 750);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constantes.RESP_ACT_BLUE_SELECT) {
            if (checkBluetooth()) {
                iniciarProcesoImpresion();
            }
        }
    }

    //Asignacion de Referencias
    private void findViews() {
        util = new Utilidades();
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

    private void init(){
        SunmiPrintHelper.getInstance().initSunmiPrinterService(this);
    }

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
                if (esFactura) {
                    //Pasar al activity de Consultar clientes
                    Intent intent = new Intent(ImpresionSumniActivity.this, ClienteConsultaActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                finish();
                break;
        }
    }

    private void imprimir() {

        runOnUiThread(() -> {
            btncerrar.setEnabled(false);
            tvestado.setText(R.string.imprimiendo___);
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
        try{
            cupoEmpleado = (ResponseCupoEmpleado) getIntent().getSerializableExtra("cupoEmpleadoBody");

            SunmiPrintHelper.getInstance().sendRawData(new byte[]{0x1B, 0x33, 20});
            SunmiPrintHelper.getInstance().setAlign(1);
            SunmiPrintHelper.getInstance().printText("BAZAR PRODUCTORA\nCRYSTAL S.A.S. NIT: 890901672-5\n", 20, true, false);

            SunmiPrintHelper.getInstance().setAlign(0);
            SunmiPrintHelper.getInstance().printText("\nCONSULTA CUPO EMPLEADO\n", 20, true, false);
            SunmiPrintHelper.getInstance().sendRawData(LINEA_PUN);

            SunmiPrintHelper.getInstance().printText("CEDULA: " + clienteid + "\n" +
                    "NOMBRE: " + cupoEmpleado.getNombre() + "\n" +
                    "EMPRESA: " + cupoEmpleado.getEmpresa() + "\n" +
                    "CUPO: " + formatea.format(cupoEmpleado.getCupo()) + "\n" +
                    "FECHA: " + util.getThisDateSimple() + "\n" +
                    "HORA: " + util.getThisHora() + "\n" +
                    "TIENDA: " + "Bazar Productora" + "\n" +
                    "CAJERO: " + cajeroname + "\n", 20, true, false);
            SunmiPrintHelper.getInstance().sendRawData(LINEA_PUN);

            SunmiPrintHelper.getInstance().printText("\n\nFirma: ________________________________\n", 20, true, false);

            SunmiPrintHelper.getInstance().setAlign(1);
            SunmiPrintHelper.getInstance().printText("GRACIAS POR PREFERIRNOS\n", 20, true, false);
            SunmiPrintHelper.getInstance().printText("\n\n\n\n\n\n", 20, true, false);
        }catch (Exception e){
            msjToast(getResources().getString(R.string.error_impresion_intentar));
        }
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

    private void resumenFacturaESCPOS() {

        //Encabezado
        SunmiPrintHelper.getInstance().sendRawData(new byte[]{0x1B, 0x33, 20});
        SunmiPrintHelper.getInstance().setAlign(1);
        String esc_pos = "BAZAR PRODUCTORA - RESUMEN\n";
        SunmiPrintHelper.getInstance().printText(esc_pos, 20, true, false);

        if (clienteName.length() > 36) {
            clienteName = clienteName.substring(0, 36);
        }

        //Información
        SunmiPrintHelper.getInstance().setAlign(0);
        esc_pos = "Emitida: " + fechahora;
        SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        esc_pos = "Tienda: " + tienda + " - " + "Caja No: " + caja;
        SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        if(!numtrans.equals("-")) {
            esc_pos = "Num. Transaccion: " + numtrans;
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        }

        if(SPM.getString(Constantes.CLIENTE_GENERICO).equals(clienteid)){
            esc_pos = "Cliente: "  + clienteName;
        }else{
            esc_pos = "Cliente: "  + clienteName + " ("+clienteid+")";
        }
        SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        //Encabezado de los productos
        SunmiPrintHelper.getInstance().setAlign(0);
        esc_pos = String.format("%1$-4s %2$-12s %3$18s", "Cant", "Descripcion", "Valor");
        SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        SunmiPrintHelper.getInstance().setAlign(0);

        Double total = 0.0;

        String tarifa = SPM.getString(Constantes.TARIFA_IVA);
        Integer totalArticulos = 0;

        //Los productos
        for (Producto p : listProductos) {

            totalArticulos = totalArticulos + p.getQuantity();
            total = total + (Math.abs(p.getPrecio())*p.getQuantity());

            String pNombre = p.getNombre();
            if (pNombre.length() > 14) {
                pNombre = pNombre.substring(0, 14);
            }
            pNombre = cleanString(pNombre);

            SunmiPrintHelper.getInstance().setAlign(0);
            esc_pos = String.format("%1$-3s %2$-14s %3$15s",   p.getQuantity()+"x", pNombre, formatea.format(Math.abs(p.getPrecio())*p.getQuantity()));
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        }

        //Totales
        SunmiPrintHelper.getInstance().setAlign(0);
        esc_pos = String.format("TOTAL: " + formatea.format(total) + " ("+totalArticulos+" ART)");
        SunmiPrintHelper.getInstance().printText(esc_pos, 20, true, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        //medios de pagos
        if (totalcompra.intValue() != 0) {
            for (int i = 0; i < (listImportes.size()); i++) {
                SunmiPrintHelper.getInstance().setAlign(0);
                if(listImportes.get(i).getName().length() > 23 || formatea.format(listImportes.get(i).getAmount()).length() > 14){
                    esc_pos = listImportes.get(i).getName();
                    SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
                    SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
                    SunmiPrintHelper.getInstance().setAlign(2);
                    esc_pos = formatea.format(listImportes.get(i).getAmount());
                    SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
                    SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
                    SunmiPrintHelper.getInstance().setAlign(0);
                }else{
                    esc_pos = String.format("%1$-23s %2$14s", listImportes.get(i).getName(), formatea.format(listImportes.get(i).getAmount()));
                    SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
                    SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
                }
            }
        }

        //DISCRIMINACION TARIFAS IVA
        SunmiPrintHelper.getInstance().setAlign(1);
        esc_pos = "DESCRIMINACION TARIFAS IVA";
        SunmiPrintHelper.getInstance().printText(esc_pos, 20, true, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        SunmiPrintHelper.getInstance().setAlign(0);

        Double totalcompraIva = totalcompra;
        if (!totalcompra.equals(base_lmp + ivacompra)) {
            totalcompraIva = totalcompra - (totalcompra - base_lmp - ivacompra);
        }

        if(formatea.format(totalcompraIva).length() > 11){
            esc_pos = String.format("%1$-9s %2$28s", "Tarifa", tarifa + "%");
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
            esc_pos = String.format("%1$-9s %2$28s", "Compra", formatea.format(totalcompraIva));
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
            esc_pos = String.format("%1$-9s %2$28s", "Base/Imp", formatea.format(base_lmp));
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
            esc_pos = String.format("%1$-9s %2$28s", "Iva", formatea.format(ivacompra));
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        }else{
            esc_pos = String.format("%1$-5s %2$-10s %3$10s %4$9s", "Tarifa", "Compra", "Base/Imp", "Iva");
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

            SunmiPrintHelper.getInstance().setAlign(0);
            esc_pos = String.format("%1$-4s %2$-11s %3$11s %4$9s", tarifa + "%", formatea.format(totalcompraIva), formatea.format(base_lmp), formatea.format(ivacompra));
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        }

        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        //fin
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        SunmiPrintHelper.getInstance().feedPaper();
    }

    private void facturaESCPOS() {

        //Encabezado
        SunmiPrintHelper.getInstance().sendRawData(new byte[]{0x1B, 0x33, 20});
        SunmiPrintHelper.getInstance().setAlign(1);
        String esc_pos = "BAZAR PRODUCTORA";
        SunmiPrintHelper.getInstance().printText(esc_pos, 36, true, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        SunmiPrintHelper.getInstance().setAlign(1);
        esc_pos = "Crystal S.A.S - NIT: 890901672-5\n";
        SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);

        esc_pos = "Numero de Factura: " + prefijo + "-" + consecutivo + "\n";
        SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        //Información
        SunmiPrintHelper.getInstance().setAlign(0);
        esc_pos = "Emitida: " + fechahora;
        SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        esc_pos = "Tienda: " + tienda + " - " + "Caja No: " + caja;
        SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        if(!numtrans.equals("-")) {
            esc_pos = "Num. Transaccion: " + numtrans;
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        }
        if(SPM.getString(Constantes.CLIENTE_GENERICO).equals(clienteid)){
            esc_pos = "Cliente: "  + "CONSUMIDOR FINAL" + " (222222222222)";
        }else{
            esc_pos = "Cliente: "  + clienteName + " ("+clienteid+")";
        }
        SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        //Encabezado de los productos
        SunmiPrintHelper.getInstance().sendRawData(LINEA_PUN);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        SunmiPrintHelper.getInstance().setAlign(0);
        esc_pos = String.format("%1$-4s %2$-14s %3$13s %4$4s", "Cant", "Descripcion", "Valor", "Imp");
        SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        SunmiPrintHelper.getInstance().setAlign(0);
        SunmiPrintHelper.getInstance().sendRawData(LINEA_PUN);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        Double total = 0.0, bolsaprice = 0.0;

        String tarifa = SPM.getString(Constantes.TARIFA_IVA);
        Integer totalArticulos = 0, bolsanum = 0;

        //Los productos
        for (Producto p : listProductos) {
            totalArticulos = totalArticulos + p.getQuantity();
            total = total + (Math.abs(p.getPrecio())*p.getQuantity());
            if (p.getCodigoTasaImpuesto().equals("BO1")) {
                bolsanum = bolsanum + p.getQuantity();
                bolsaprice = p.getPrecio();
            }

            String pCodigoTasaImpuesto = p.getCodigoTasaImpuesto();
            if (pCodigoTasaImpuesto.length() > 1) {
                pCodigoTasaImpuesto = pCodigoTasaImpuesto.substring(0, 1);
            }

            String pNombre = p.getNombre();
            if (pNombre.length() > 16) {
                pNombre = pNombre.substring(0, 16);
            }

            SunmiPrintHelper.getInstance().setAlign(0);
            esc_pos = String.format("%1$-4s %2$-16s %3$11s %4$2s", p.getQuantity(), pNombre, formatea.format(Math.abs(p.getPrecio())*p.getQuantity()), pCodigoTasaImpuesto);
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

            if(p.getQuantity() > 1){
                SunmiPrintHelper.getInstance().setAlign(2);
                esc_pos = "Valor Final: "+formatea.format(Math.abs(p.getPrecio())*p.getQuantity());
                SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
                SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
            }
        }

        SunmiPrintHelper.getInstance().sendRawData(LINEA_PUN);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        //Totales
        SunmiPrintHelper.getInstance().setAlign(0);
        esc_pos = String.format("%1$-6s %2$20s", "TOTAL", formatea.format(total));
        SunmiPrintHelper.getInstance().printText(esc_pos, 28, true, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        if (!primeraimpresion) {
            SunmiPrintHelper.getInstance().setAlign(1);
            esc_pos = "DUPLICADO";
            SunmiPrintHelper.getInstance().printText(esc_pos, 36, true, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        }

        //medios de pagos
        if (totalcompra.intValue() != 0) {
            for (int i = 0; i < (listImportes.size()); i++) {
                SunmiPrintHelper.getInstance().setAlign(0);
                if(listImportes.get(i).getName().length() > 21 || formatea.format(listImportes.get(i).getAmount()).length() > 16){
                    esc_pos = listImportes.get(i).getName();
                    SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
                    SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
                    SunmiPrintHelper.getInstance().setAlign(2);
                    esc_pos = formatea.format(listImportes.get(i).getAmount());
                    SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
                    SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
                    SunmiPrintHelper.getInstance().setAlign(0);
                }else{
                    esc_pos = String.format("%1$-21s %2$16s", listImportes.get(i).getName(), formatea.format(listImportes.get(i).getAmount()));
                    SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
                    SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
                }
            }
        }

        SunmiPrintHelper.getInstance().setAlign(0);
        esc_pos = "TOTAL ARTICULOS: " + totalArticulos;
        SunmiPrintHelper.getInstance().printText(esc_pos, 20, true, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        //DISCRIMINACION TARIFAS IVA
        SunmiPrintHelper.getInstance().setAlign(1);
        esc_pos = "DISCRIMINACION TARIFAS IVA";
        SunmiPrintHelper.getInstance().printText(esc_pos, 20, true, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        SunmiPrintHelper.getInstance().setAlign(0);

        Double totalcompraIva = totalcompra;
        if (!totalcompra.equals(base_lmp + ivacompra)) {
            totalcompraIva = totalcompra - (totalcompra - base_lmp - ivacompra);
        }

        if(formatea.format(totalcompraIva).length() > 11){
            SunmiPrintHelper.getInstance().sendRawData(LINEA_PUN);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
            esc_pos = String.format("%1$-9s %2$28s", "Tarifa", tarifa + "%");
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
            esc_pos = String.format("%1$-9s %2$28s", "Compra", formatea.format(totalcompraIva));
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
            esc_pos = String.format("%1$-9s %2$28s", "Base/Imp", formatea.format(base_lmp));
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
            esc_pos = String.format("%1$-9s %2$28s", "Iva", formatea.format(ivacompra));
        }else{
            esc_pos = String.format("%1$-5s %2$-10s %3$10s %4$9s", "Tarifa", "Compra", "Base/Imp", "Iva");
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
            SunmiPrintHelper.getInstance().sendRawData(LINEA_PUN);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
            SunmiPrintHelper.getInstance().setAlign(0);
            esc_pos = String.format("%1$-4s %2$-11s %3$11s %4$9s", tarifa + "%", formatea.format(totalcompraIva), formatea.format(base_lmp), formatea.format(ivacompra));
        }
        SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        SunmiPrintHelper.getInstance().sendRawData(LINEA_PUN);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        //DISCRIMINACION IMPUESTO BOLSA.
        if (bolsanum != 0) {
            SunmiPrintHelper.getInstance().setAlign(1);
            esc_pos = "DESCRIMINACION IMPUESTO BOLSA";
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, true, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

            SunmiPrintHelper.getInstance().setAlign(0);
            esc_pos = String.format("%1$-5s %2$-15s %3$16s", "Cant", "V. Unitario", "V. Total");
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
            SunmiPrintHelper.getInstance().sendRawData(LINEA_PUN);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

            SunmiPrintHelper.getInstance().setAlign(0);
            esc_pos = String.format("%1$-5s %2$-15s %3$16s", bolsanum, formatea.format(bolsaprice), formatea.format(bolsanum * bolsaprice));
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
            SunmiPrintHelper.getInstance().setAlign(0);
            SunmiPrintHelper.getInstance().sendRawData(LINEA_PUN);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        }
        //no devoluciones
        SunmiPrintHelper.getInstance().setAlign(1);
        esc_pos = "MERCANCIA DEL BAZAR NO TIENE CAMBIOS NI DEVOLUCIONES";
        SunmiPrintHelper.getInstance().printText(esc_pos, 20, true, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        SunmiPrintHelper.getInstance().setAlign(1);
        SunmiPrintHelper.getInstance().printText("REPRESENTACION GRAFICA DE SU FACTURA \n ELECTRONICA DE VENTA", 20, true, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        //TEXTOS CONTRIBUYENTES
        String textcontribuyentes = SPM.getString(Constantes.TEXTOS_CONTRIBUYENTES);
        textcontribuyentes = textcontribuyentes.replace("\n", " ");
        SunmiPrintHelper.getInstance().setAlign(1);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        SunmiPrintHelper.getInstance().printText(textcontribuyentes, 20, false, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        //TEXTO FISCAL
        String textFiscal = textoFiscal;
        textFiscal = textFiscal.replace("  ", " ");
        textFiscal = textFiscal.replace("  ", " ");
        SunmiPrintHelper.getInstance().setAlign(1);
        SunmiPrintHelper.getInstance().printText(textFiscal, 20, false, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        SunmiPrintHelper.getInstance().setAlign(0);
        SunmiPrintHelper.getInstance().printText("CUFE: " + cufeHash, 19, false, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        SunmiPrintHelper.getInstance().setAlign(0);
        SunmiPrintHelper.getInstance().printText("Emitida: " + fechahora, 19, false, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        SunmiPrintHelper.getInstance().setAlign(1);
        SunmiPrintHelper.getInstance().printQr(SPM.getString(Constantes.URL_BASE_FE_QR)+cufeHash, 4, 0);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        //TEXTO PROVEEDOR TECNOLOGICO
        SunmiPrintHelper.getInstance().setAlign(0);
        SunmiPrintHelper.getInstance().printText(SPM.getString(Constantes.PROVEEDOR_TECNOLOGICO_FE), 19, false, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        //CODIGO DE BARRAS
        SunmiPrintHelper.getInstance().setAlign(1);
        SunmiPrintHelper.getInstance().printBarCode(codigobarras, 8, 90, 2, 2);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        SunmiPrintHelper.getInstance().feedPaper();
    }

    private void pruebaESCPOS() {
        SunmiPrintHelper.getInstance().setAlign(1);
        String esc_pos = "Crystal S.A.S";
        SunmiPrintHelper.getInstance().printText(esc_pos, 20, true, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        SunmiPrintHelper.getInstance().printBarCode("PRUEBA", 8, 90, 3, 2);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        //Alican codiciones y restricciones
        SunmiPrintHelper.getInstance().printQr("*Prueba Realizada*", 4, 0);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        esc_pos = "*Prueba Realizada*";
        SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        //fin
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        SunmiPrintHelper.getInstance().feedPaper();
    }

    private void comprobanteTefESCPOS() {
        if(isClaveValorTef){
            SunmiPrintHelper.getInstance().sendRawData(new byte[]{0x1B, 0x33, 20});
            SunmiPrintHelper.getInstance().setAlign(1);
            SunmiPrintHelper.getInstance().printText(tituloClaveValor, 36, true, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

            for(ClaveValorTef claveValor : claveValorTefList){
                SunmiPrintHelper.getInstance().setAlign(0);
                SunmiPrintHelper.getInstance().printText(claveValor.getClave() +" "+ claveValor.getValor(), 20, true, false);
                SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
            }

            SunmiPrintHelper.getInstance().printText("\n\n\n\n\n\n", 20, true, false);
        }else{
            DatafonoEntity respDatafono = (DatafonoEntity) getIntent().getSerializableExtra("respDatafono");
            assert respDatafono != null;

            SunmiPrintHelper.getInstance().sendRawData(new byte[]{0x1B, 0x33, 20});
            SunmiPrintHelper.getInstance().setAlign(1);
            String esc_pos = "CREDIBANCO";
            SunmiPrintHelper.getInstance().printText(esc_pos, 36, true, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

            esc_pos = respDatafono.getTiendaNombre().trim();
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, true, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

            esc_pos = respDatafono.getDirEstablecimiento();
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

            esc_pos = "CU: " + respDatafono.getCodigoComercio().trim();
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

            String sDate1 = respDatafono.getFecha() + "-" + respDatafono.getHora();
            Date dateD = null;
            try {
                dateD = new SimpleDateFormat("yyMMdd-HHmmss").parse(sDate1);
                sDate1 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.US).format(dateD);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            esc_pos = " Fecha: " + sDate1;
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

            esc_pos = respDatafono.getHash();
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);


            esc_pos = String.format("%1$-19s %2$18s", "TER: " + respDatafono.getCodigoTerminal(), " AUT: " + respDatafono.getCodigoAprobacion());
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

            esc_pos = String.format("%1$-19s %2$18s", respDatafono.getFranquicia().trim(), respDatafono.getTipoCuenta());
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

            esc_pos = String.format("%1$-19s %2$18s", "****" + respDatafono.getUltDigitoTarj(), "");
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

            esc_pos = String.format("%1$-19s %2$18s", "Recibo: " + respDatafono.getNumRecibo(), "TVR: " + respDatafono.getTvr());
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

            if (respDatafono.getTipoCuenta().equals("CR")) {
                esc_pos = String.format("%1$-19s %2$18s", "Cuotas: " + respDatafono.getCuotas(), "TSI: " + respDatafono.getTsi());
                SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
                SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
            } else {
                esc_pos = String.format("%1$-19s %2$18s", "", "TSI: " + respDatafono.getTsi());
                SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
                SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
            }

            esc_pos = String.format("%1$-19s %2$18s", "RRN: " + respDatafono.getRrn(), "AID: " + respDatafono.getAid());
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

            esc_pos = "Criptograma: " + respDatafono.getCriptograma();
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

            int total = respDatafono.getMonto().intValue();
            int iva = respDatafono.getIva().intValue();
            int compra_neta = total - iva;

            esc_pos = String.format("%1$-19s %2$18s", "COMPRA NETA", formatea.format(compra_neta));
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

            esc_pos = String.format("%1$-19s %2$18s", "IVA", formatea.format(iva));
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

            esc_pos = String.format("%1$-19s %2$18s", "IAC", formatea.format(respDatafono.getIac()));
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

            esc_pos = String.format("%1$-19s %2$18s", "TOTAL", formatea.format(total));
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, true, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

            if (!primeraimpresion) {
                esc_pos = "DUPLICADO";
                SunmiPrintHelper.getInstance().printText(esc_pos, 36, true, false);
                SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
            }

            if (respDatafono.getTipoCuenta().equals("CR")) {

                SunmiPrintHelper.getInstance().setAlign(1);
                SunmiPrintHelper.getInstance().sendRawData(LINEA_PUN);
                SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

                String textpagare = SPM.getString(Constantes.TEXTOS_PAGARE_TEF);
                textpagare = textpagare.replace("\n", " ");
                esc_pos = cleanString(textpagare);
                SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
                SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
                SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

                SunmiPrintHelper.getInstance().setAlign(0);
                esc_pos = "Firma: ______________________________";
                SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
                SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
                SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

                esc_pos = "C.C: ________________________________";
                SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
                SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
                SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

                esc_pos = "Tel: ________________________________";
                SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
                SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
            }

            SunmiPrintHelper.getInstance().setAlign(1);
            esc_pos = respDatafono.getIdCliente().trim();
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, true, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
            //fin
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
            SunmiPrintHelper.getInstance().feedPaper();
        }
    }

    private void anulacionESCPOS() {

        DatafonoEntity respDatafono = (DatafonoEntity) getIntent().getSerializableExtra("respDatafono");

        SunmiPrintHelper.getInstance().setAlign(1);
        SunmiPrintHelper.getInstance().sendRawData(new byte[]{0x1B, 0x33, 20});

        String esc_pos = "CREDIBANCO";
        SunmiPrintHelper.getInstance().printText(esc_pos, 36, true, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        esc_pos = respDatafono.getTiendaNombre().trim();
        SunmiPrintHelper.getInstance().printText(esc_pos, 20, true, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        esc_pos = respDatafono.getDirEstablecimiento();
        SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        esc_pos = "CU: " + respDatafono.getCodigoComercio().trim();
        SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        String sDate1 = respDatafono.getFecha() + "-" + respDatafono.getHora();
        Date dateD = null;
        try {
            dateD = new SimpleDateFormat("yyMMdd-HHmmss").parse(sDate1);
            sDate1 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.US).format(dateD);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        esc_pos = " Fecha: " + sDate1;
        SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        esc_pos = respDatafono.getHash();
        SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        esc_pos = String.format("%1$-19s %2$18s", "TER: " + respDatafono.getCodigoTerminal(), "AUT: " + respDatafono.getCodigoAprobacion());
        SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        esc_pos = String.format("%1$-19s %2$18s", respDatafono.getFranquicia().trim(), respDatafono.getTipoCuenta());
        SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        esc_pos = String.format("%1$-19s %2$18s", "****" + respDatafono.getUltDigitoTarj(), "");
        SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        esc_pos = String.format("%1$-19s %2$18s", "Recibo: " + respDatafono.getNumRecibo(), "RRN: " + respDatafono.getRrn());
        SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        int total = respDatafono.getMonto().intValue();
        int iva = respDatafono.getIva().intValue();
        int compra_neta = total - iva;

        esc_pos = String.format("%1$-19s %2$18s", "COMPRA NETA", formatea.format(compra_neta));
        SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        esc_pos = String.format("%1$-19s %2$18s", "TOTAL", formatea.format(total));
        SunmiPrintHelper.getInstance().printText(esc_pos, 20, true, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        SunmiPrintHelper.getInstance().setAlign(1);
        SunmiPrintHelper.getInstance().sendRawData(LINEA_PUN);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        esc_pos = "ANULACION";
        SunmiPrintHelper.getInstance().printText(esc_pos, 36, true, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        SunmiPrintHelper.getInstance().setAlign(1);
        SunmiPrintHelper.getInstance().sendRawData(LINEA_PUN);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        if (!primeraimpresion) {
            esc_pos = "DUPLICADO";
            SunmiPrintHelper.getInstance().printText(esc_pos, 36, true, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        }

        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        SunmiPrintHelper.getInstance().setAlign(0);
        esc_pos = "Firma: ______________________________";
        SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        esc_pos = "C.C: ________________________________";
        SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        esc_pos = "Tel: ________________________________";
        SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        SunmiPrintHelper.getInstance().setAlign(1);
        esc_pos = respDatafono.getIdCliente().trim();
        SunmiPrintHelper.getInstance().printText(esc_pos, 20, true, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        //fin
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        SunmiPrintHelper.getInstance().feedPaper();

    }

    private void TEFContdetalladoESCPOS() {

        List<TEFContinguenciaEntity> tefContList = (ArrayList<TEFContinguenciaEntity>) getIntent().getSerializableExtra("tefContList");

        //Encabezado
        SunmiPrintHelper.getInstance().sendRawData(new byte[]{0x1B, 0x33, 20});
        SunmiPrintHelper.getInstance().setAlign(1);
        String esc_pos = "BAZAR PRODUCTORA";
        SunmiPrintHelper.getInstance().printText(esc_pos, 36, true, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        SunmiPrintHelper.getInstance().setAlign(0);
        esc_pos = "Crystal S.A.S - TEF CONTINGENCIA";
        SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.US);
        Date todayDate = new Date();
        String fecha = currentDate.format(todayDate);
        esc_pos = "Fecha: "+fecha;
        SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        SunmiPrintHelper.getInstance().setAlign(1);
        esc_pos = "INFORME DETALLADO TEF CONT.";
        SunmiPrintHelper.getInstance().printText(esc_pos, 21, true, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        esc_pos = String.format("%1$-19s %2$18s", "FECHA", "MONTO");
        SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        String franquiciaUso = null, tipoCuentaUso = null;

        for (TEFContinguenciaEntity rd : tefContList) {
            franquiciaUso = rd.getFranquicia();
            tipoCuentaUso = rd.getTipoCuenta();
            break;
        }

        esc_pos = cleanString(franquiciaUso.trim() + " " + tipoCuentaUso);
        SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

        //Recorremos el cursor hasta que no haya más registros
        for (TEFContinguenciaEntity rd : tefContList) {
            String franquicia = rd.getFranquicia();
            String tipoCuenta = rd.getTipoCuenta();
            Double monto = rd.getMonto();
            String fechaO = rd.getFecha();
            fechaO = fechaO.substring(0,16);

            if (!franquiciaUso.equals(franquicia) || !tipoCuentaUso.equals(tipoCuenta)) {

                SunmiPrintHelper.getInstance().setAlign(1);
                SunmiPrintHelper.getInstance().sendRawData(LINEA_PUN);
                SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

                esc_pos = cleanString(franquicia + " " + tipoCuenta);
                SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
                SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
            }
            franquiciaUso = franquicia;
            tipoCuentaUso = tipoCuenta;

            esc_pos = String.format("%1$-19s %2$18s", fechaO, formatea.format(monto));
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        }
        //fin
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
        SunmiPrintHelper.getInstance().feedPaper();
    }

    private void TEFdetalladoESCPOS() {

        List<DatafonoEntity> listRDDetallado = (ArrayList<DatafonoEntity>) getIntent().getSerializableExtra("listRDDetallado");
        if (listRDDetallado.size() > 0) {
            String codigoComercio = getIntent().getExtras().getString("codigoComercio");
            codigoComercio = codigoComercio.trim();
            String dirEstable = getIntent().getExtras().getString("dirEstable");
            String codTerminal = getIntent().getExtras().getString("codTerminal");

            //Cabecera
            SunmiPrintHelper.getInstance().sendRawData(new byte[]{0x1B, 0x33, 20});
            SunmiPrintHelper.getInstance().setAlign(1);
            String esc_pos = "CREDIBANCO";
            SunmiPrintHelper.getInstance().printText(esc_pos, 22, true, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

            SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.US);
            Date todayDate = new Date();
            String fecha = currentDate.format(todayDate);
            SunmiPrintHelper.getInstance().printText(fecha, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

            esc_pos = "Crystal S.A.S - "+codigoComercio;
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

            SunmiPrintHelper.getInstance().printText(dirEstable, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
            SunmiPrintHelper.getInstance().setAlign(2);
            SunmiPrintHelper.getInstance().printText("TER: "+codTerminal, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

            SunmiPrintHelper.getInstance().setAlign(1);
            esc_pos = "INFORME DETALLADO";
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, true, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

            esc_pos = String.format("%1$-4s %2$-6s %3$-5s %4$3s %5$13s", "TRAN", "TARJETA", "RECIBO", "TIPO", "MONTO");
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

            String franquiciaUso = null, tipoCuentaUso = null;

            for (DatafonoEntity rd : listRDDetallado) {
                franquiciaUso = rd.getFranquicia();
                tipoCuentaUso = rd.getTipoCuenta();
                break;
            }

            esc_pos = franquiciaUso.trim() + " " + tipoCuentaUso;
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

            //Recorremos el cursor hasta que no haya más registros
            for (DatafonoEntity rd : listRDDetallado) {
                String franquicia = rd.getFranquicia();
                Double monto = rd.getMonto();
                String tipoCuenta = rd.getTipoCuenta();
                String numRecibo = rd.getNumRecibo();
                String ultDigitoTarj = rd.getUltDigitoTarj();
                String tipoOperacion = rd.getDc().trim();

                if (!franquiciaUso.equals(franquicia) || !tipoCuentaUso.equals(tipoCuenta)) {

                    SunmiPrintHelper.getInstance().setAlign(1);
                    SunmiPrintHelper.getInstance().sendRawData(LINEA_PUN);
                    SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

                    esc_pos = franquicia.trim() + " " + tipoCuenta;
                    SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
                    SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
                }
                franquiciaUso = franquicia;
                tipoCuentaUso = tipoCuenta;

                if (tipoOperacion.equals(Constantes.COD_ANULACION)) {
                    esc_pos = String.format("%1$-4s %2$-5s %3$-5s %4$4s %5$15s", tipoOperacion, "*" + ultDigitoTarj, numRecibo, tipoCuenta, formatea.format(monto));
                    SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);

                } else {
                    esc_pos = String.format("%1$-4s %2$-5s %3$-5s %4$4s %5$15s", tipoOperacion, "*" + ultDigitoTarj, numRecibo, tipoCuenta, formatea.format(monto));
                    SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
                }
                SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
            }
            //fin
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
            SunmiPrintHelper.getInstance().feedPaper();
        }
    }

    private void TEFtotalesESCPOS() {

        List<DatafonoTotales> listRDTotales = (ArrayList<DatafonoTotales>) getIntent().getSerializableExtra("listRDTotales");
        List<DatafonoTotales> listRDTotalesCanceladas = (ArrayList<DatafonoTotales>) getIntent().getSerializableExtra("listRDTotalesCanceladas");

        if (listRDTotales.size() > 0) {

            String codigoComercio = getIntent().getExtras().getString("codigoComercio");
            String dirEstable = getIntent().getExtras().getString("dirEstable");
            String codTerminal = getIntent().getExtras().getString("codTerminal");

            //Cabecera
            SunmiPrintHelper.getInstance().sendRawData(new byte[]{0x1B, 0x33, 20});
            SunmiPrintHelper.getInstance().setAlign(1);
            String esc_pos = "CREDIBANCO";
            SunmiPrintHelper.getInstance().printText(esc_pos, 22, true, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

            SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.US);
            Date todayDate = new Date();
            String fecha = currentDate.format(todayDate);
            SunmiPrintHelper.getInstance().printText(fecha, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

            esc_pos = "Crystal S.A.S - "+codigoComercio.trim();
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

            SunmiPrintHelper.getInstance().printText(dirEstable, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
            SunmiPrintHelper.getInstance().setAlign(2);
            SunmiPrintHelper.getInstance().printText("TER: "+codTerminal, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

            SunmiPrintHelper.getInstance().setAlign(1);
            esc_pos = "TOTAL POR ENTIDAD";
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, true, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

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
                    SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
                    SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

                    esc_pos = String.format("%1$-10s %2$-4s %3$21s", "COMPRAS", ":" + numElementosF, formatea.format(montoF - ivaF));
                    SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
                    SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
                    esc_pos = String.format("%1$-10s %2$-4s %3$21s", "IVA", ":", formatea.format(ivaF));
                    SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
                    SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
                    esc_pos = String.format("%1$-10s %2$-4s %3$21s", "PROPINA", ":", formatea.format(propinaF));
                    SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
                    SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
                    SunmiPrintHelper.getInstance().sendRawData(LINEA_PUN);
                    SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
                    esc_pos = String.format("%1$-10s %2$-4s %3$21s", "SUBTOTAL", ":" + numElementosF, formatea.format(montoF));
                    SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
                    SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
                    esc_pos = String.format("%1$-10s %2$-4s %3$21s", "CANCELADAS", ":" + canceladaCountF, "-" + formatea.format(canceladaF));
                    SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
                    SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
                    SunmiPrintHelper.getInstance().sendRawData(LINEA_PUN);
                    SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

                    montoF = 0.0;
                    ivaF = 0.0;
                    propinaF = 0.0;
                    numElementosF = 0;
                    canceladaF = 0.0;
                    canceladaCountT = 0;
                }
                franquiciaUso = franquicia;


                esc_pos = franquicia.trim() + " " + tipoCuenta;
                SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
                SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

                esc_pos = String.format("%1$-10s %2$-4s %3$21s", "COMPRAS", ":" + numElementos, formatea.format(monto - iva));
                SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
                SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
                esc_pos = String.format("%1$-10s %2$-4s %3$21s", "IVA", ":", formatea.format(iva));
                SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
                SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
                esc_pos = String.format("%1$-10s %2$-4s %3$21s", "PROPINA", ":", formatea.format(propina));
                SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
                SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
                SunmiPrintHelper.getInstance().sendRawData(LINEA_PUN);
                SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
                esc_pos = String.format("%1$-10s %2$-4s %3$21s", "SUBTOTAL", ":" + numElementos, formatea.format(monto));
                SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
                SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
                esc_pos = String.format("%1$-10s %2$-4s %3$21s", "CANCELADAS", ":" + canceladaCount, "-" + formatea.format(cancelada));
                SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
                SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
                SunmiPrintHelper.getInstance().sendRawData(LINEA_PUN);
                SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

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
                    SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
                    SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

                    esc_pos = String.format("%1$-10s %2$-4s %3$21s", "COMPRAS", ":" + numElementosF, formatea.format(montoF - ivaF));
                    SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
                    SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
                    esc_pos = String.format("%1$-10s %2$-4s %3$21s", "IVA", ":", formatea.format(ivaF));
                    SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
                    SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
                    esc_pos = String.format("%1$-10s %2$-4s %3$21s", "PROPINA", ":", formatea.format(propinaF));
                    SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
                    SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
                    SunmiPrintHelper.getInstance().sendRawData(LINEA_PUN);
                    SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
                    esc_pos = String.format("%1$-10s %2$-4s %3$21s", "SUBTOTAL", ":" + numElementosF, formatea.format(montoF));
                    SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
                    SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
                    esc_pos = String.format("%1$-10s %2$-4s %3$21s", "CANCELADAS", ":" + canceladaCountF, "-" + formatea.format(canceladaF));
                    SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
                    SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
                    SunmiPrintHelper.getInstance().sendRawData(LINEA_PUN);
                    SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
                }
            }

            //GRAN TOTAL
            esc_pos = "GRAN TOTAL";
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);

            esc_pos = String.format("%1$-10s %2$-4s %3$21s", "COMPRAS", ":" + numElementosT, formatea.format(montoT - ivaT));
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
            esc_pos = String.format("%1$-10s %2$-4s %3$21s", "IVA", ":", formatea.format(ivaT));
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
            esc_pos = String.format("%1$-10s %2$-4s %3$21s", "PROPINA", ":", formatea.format(propinaT));
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
            SunmiPrintHelper.getInstance().sendRawData(LINEA_PUN);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
            esc_pos = String.format("%1$-10s %2$-4s %3$21s", "SUBTOTAL", ":" + numElementosT, formatea.format(montoT));
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
            esc_pos = String.format("%1$-10s %2$-4s %3$21s", "CANCELADAS", ":" + canceladaCountT, "-" + formatea.format(canceladaT));
            SunmiPrintHelper.getInstance().printText(esc_pos, 20, false, false);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
            SunmiPrintHelper.getInstance().sendRawData(LINEA_PUN);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
            //fin
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
            SunmiPrintHelper.getInstance().sendRawData(LINE_FEED);
            SunmiPrintHelper.getInstance().feedPaper();

        }
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
