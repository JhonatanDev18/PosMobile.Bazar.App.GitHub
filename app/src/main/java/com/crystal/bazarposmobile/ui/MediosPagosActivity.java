package com.crystal.bazarposmobile.ui;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.common.Constantes;
import com.crystal.bazarposmobile.common.IConfigurableActivity;
import com.crystal.bazarposmobile.common.LogFile;
import com.crystal.bazarposmobile.common.SPM;
import com.crystal.bazarposmobile.common.TipoDatafono;
import com.crystal.bazarposmobile.common.Utilidades;
import com.crystal.bazarposmobile.db.entity.DatafonoEntity;
import com.crystal.bazarposmobile.db.BazarPosMovilDB;
import com.crystal.bazarposmobile.db.entity.DetalleDescuentoEntity;
import com.crystal.bazarposmobile.db.entity.DocHeaderEntity;
import com.crystal.bazarposmobile.db.entity.DocLineEntity;
import com.crystal.bazarposmobile.db.entity.DocPaymentEntity;
import com.crystal.bazarposmobile.db.entity.MediosPagoCajaEntity;
import com.crystal.bazarposmobile.db.entity.PagosTEFEntity;
import com.crystal.bazarposmobile.db.entity.TarjetaEntity;
import com.crystal.bazarposmobile.retrofit.ApiService;
import com.crystal.bazarposmobile.retrofit.AppCliente;
import com.crystal.bazarposmobile.retrofit.request.RequestActualizarCupoEmpleado;
import com.crystal.bazarposmobile.retrofit.request.RequestExtraerInfoDocumento;
import com.crystal.bazarposmobile.retrofit.request.RequestPostDocumento;
import com.crystal.bazarposmobile.retrofit.request.creardocumento.HeaderSD;
import com.crystal.bazarposmobile.retrofit.request.creardocumento.LineSD;
import com.crystal.bazarposmobile.retrofit.request.creardocumento.LinesSD;
import com.crystal.bazarposmobile.retrofit.request.creardocumento.Payment;
import com.crystal.bazarposmobile.retrofit.request.creardocumento.Payments;
import com.crystal.bazarposmobile.retrofit.request.creardocumento.RequestSaleDocumentCreate;
import com.crystal.bazarposmobile.retrofit.request.datafonon6.RequestGuardarRespuestasTef;
import com.crystal.bazarposmobile.retrofit.request.detallesdescuento.Detalle;
import com.crystal.bazarposmobile.retrofit.request.detallesdescuento.RequestDetallesDescuento;
import com.crystal.bazarposmobile.retrofit.request.facturaelectronica.RequestGenerarQRFE;
import com.crystal.bazarposmobile.retrofit.request.tef.PagosTEF;
import com.crystal.bazarposmobile.retrofit.request.tef.RequestTEF;
import com.crystal.bazarposmobile.retrofit.response.ResponseCupoEmpleado;
import com.crystal.bazarposmobile.retrofit.response.ResponseExtraerInfoDocumento;
import com.crystal.bazarposmobile.retrofit.response.ResponseValidarAperturaCaja;
import com.crystal.bazarposmobile.retrofit.response.consecutivofiscal.ResponseConsecutivoFiscal;
import com.crystal.bazarposmobile.retrofit.response.ResponseDomentoFiscal;
import com.crystal.bazarposmobile.retrofit.response.datafonon6.CuerpoRespuestaDatafonoN6;
import com.crystal.bazarposmobile.retrofit.response.datafonon6.ResponseConsultarCompraN6;
import com.crystal.bazarposmobile.retrofit.response.datafonon6.ResponseGuardarRespuestasTef;
import com.crystal.bazarposmobile.retrofit.response.documentodetalle.LineDD;
import com.crystal.bazarposmobile.retrofit.response.documentodetalle.PaymentsDD;
import com.crystal.bazarposmobile.retrofit.response.facturaelectronica.RequestGuardarTrazaFE;
import com.crystal.bazarposmobile.retrofit.response.facturaelectronica.ResponseGenerarQRFE;
import com.crystal.bazarposmobile.retrofit.response.facturaelectronica.ResponseGuardarTrazaFE;
import com.crystal.bazarposmobile.retrofit.response.facturaelectronica.UploadResponse;
import com.crystal.bazarposmobile.retrofit.response.mediospagocaja.MediosCaja;
import com.crystal.bazarposmobile.retrofit.response.mercadopago.MercadoPagoImporte;
import com.crystal.bazarposmobile.retrofit.response.eanes.Producto;
import com.crystal.bazarposmobile.retrofit.response.documentodetalle.ResponseDD;
import com.crystal.bazarposmobile.retrofit.response.postdocumento.ResponsePostDocumento;
import com.crystal.bazarposmobile.retrofit.response.tarjetasbancarias.Tarjeta;
import com.crystal.bazarposmobile.ui.dialogfragmen.ImporteGripDialogFragment;
import com.crystal.bazarposmobile.ui.dialogfragmen.MedioPagoSelecDialogFragment;
import com.crystal.bazarposmobile.ui.dialogfragmen.MostrarInfoDialogFragment;
import com.crystal.bazarposmobile.ui.dialogfragmen.PagoEfectivoDialogFragment;
import com.crystal.bazarposmobile.ui.dialogfragmen.AutorizacionDialogFragment;
import com.crystal.bazarposmobile.ui.dialogfragmen.PagoTEFContinguenciaDialogFragment;
import com.crystal.bazarposmobile.ui.dialogfragmen.PagoTipoBonoDialogFragment;
import com.crystal.bazarposmobile.ui.dialogfragmen.TipoTefDialogFragment;
import com.crystal.bazarposmobile.ui.fragment.ImporteGripFragment;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MediosPagosActivity
        extends
            AppCompatActivity
        implements
        View.OnClickListener,
        ImporteGripFragment.OnListFragmentInteractionListener,
        PagoEfectivoDialogFragment.OnInputListener,
        MedioPagoSelecDialogFragment.OnInputListener,
        ImporteGripDialogFragment.OnInputListener,
        PagoTipoBonoDialogFragment.OnInputListener,
        PagoTEFContinguenciaDialogFragment.OnInputListener,
        AutorizacionDialogFragment.OnInputListener,
        IConfigurableActivity {

    //Declaración del Cliente REST
    ApiService apiService;
    String usuario = SPM.getString(Constantes.USER_NAME) + " - " + SPM.getString(Constantes.CAJA_CODE);

    Date fechaCreacionFE;
    String empresaTemporal;

    //Declaración de los objetos de la interfaz del activity
    ImporteGripFragment importeGF;
    Utilidades util;
    String tipoResolucion, claveFacturaElectronica, referenciaInternaValidacion;
    ProgressBar pb;
    TextView tvmontopago, tvefectivo, tvtarjeta, tvotrosmedios, tvimporte, tvcambio, tvpendiente, tvtarjetacont;
    ImageView btntarjeta, btnefectivo, btnotrosmedios,btntarjetacont, btnimprimir;
    String cufeHash;
    private Double totalCompraIva;
    private Double baseLmp;
    private Double ivaCompra;
    //Declaración de la variables del activity
    List<Producto> productosList = new ArrayList<>();
    List<String> mediospagoslist = new ArrayList<>();
    List<MediosCaja> mediospagosdatafonolist = new ArrayList<>();
    List<MediosCaja> mediospagosotroslist = new ArrayList<>();
    List<Payment> importesList = new ArrayList<>();
    Double netopagar, netoimporte, cambio, deuda, totalcompra, ivacompra, ivacompraFE;
    Integer cantP;
    boolean isEfectivo = true, validateDoc = false;
    MedioPagoSelecDialogFragment medioPagoSelecDF;
    String divisa, caja, tienda, nombreTienda, cajerocode, vendedorcode, clienteid,
            consecutivo, prefijo, customerid, thisDate, internalReference, textoFiscal,
            numtrans, vaucherPalabra, vaucherText, MPEFECTIVO,MPEFECTIVONOMBRE, MPTEFCONTINGUENCIA,
            MPTEFCONTINGUENCIANOMBRE,MPMERCADOPAGO;

    DecimalFormat formatea = new DecimalFormat(Constantes.FORMATO_DECIMAL);
    TipoTefDialogFragment tipoTefDialogFragment;
    double descuentoCupoEmpleado;
    double cupoEmpleadoTotal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medios_pagos);
        this.setTitle(R.string.titulo_medios_pagos);

        if(SPM.getBoolean(Constantes.MODO_AUTONOMO)){
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorModoAutonomo)));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Toolbar toolbar = this.findViewById(R.id.action_bar);
                if (toolbar!= null){
                    toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        }

        //Obtiendo la informacion desde la caja
        productosList = (ArrayList<Producto>) getIntent().getSerializableExtra("listProductos");
        mediospagoslist = (ArrayList<String>) getIntent().getSerializableExtra("listMediosPagoPermitidos");
        vaucherPalabra = getIntent().getExtras().getString("vaucherPalabra");
        vaucherText = getIntent().getExtras().getString("vaucherText");
        netopagar = getIntent().getExtras().getDouble("total");

        //Instanciando el GripRecyclerViewAdapter
        importeGF = (ImporteGripFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragmentGripImportes);
        importeGF.setPayments(importesList);

        //Obteniendo las variable a usar
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
        Date todayDate = new Date();
        thisDate = currentDate.format(todayDate);
        divisa = SPM.getString(Constantes.DIVISA);
        caja = SPM.getString(Constantes.CAJA_CODE);
        tienda = SPM.getString(Constantes.TIENDA_CODE);
        nombreTienda = SPM.getString(Constantes.NOMBRE_TIENDA);
        cajerocode = SPM.getString(Constantes.CAJERO_CODE);
        vendedorcode = SPM.getString(Constantes.VENDEDOR_CODE);
        clienteid = SPM.getString(Constantes.DOCUMENTO_CLIENTE);
        customerid = SPM.getString(Constantes.CUSTOMER_ID);

        MPEFECTIVO = SPM.getString(Constantes.MEDIO_PAGO_EFECTIVO_CAJA);
        MPEFECTIVONOMBRE = "EFECTIVO";
        MPTEFCONTINGUENCIA = SPM.getString(Constantes.MEDIO_PAGO_TEF_CONTIGUENCIA);
        MPTEFCONTINGUENCIANOMBRE = "CONTINGENCIA - TARJETAS";
        MPMERCADOPAGO = SPM.getString(Constantes.MEDIO_PAGO_MERCADOPAGO);

        cambio = 0.0;
        netoimporte = 0.0;
        deuda = netopagar;
        netoimporte = 0.0;
        totalcompra = 0.0;
        ivacompra = 0.0;
        cantP = 0;

        for (Producto p : productosList) {
            totalcompra = totalcompra + (Math.abs(p.getPrecio())*p.getQuantity());
            cantP = cantP + p.getQuantity();
            if (p.getCodigoTasaImpuesto().equals("NOR")) {
                Double precioSinImpuesto = (double) Math.round((p.getPrecio()) / (((p.getValorTasa()) / 100) + 1));
                ivacompra = ivacompra + (Math.abs(p.getPrecio() - precioSinImpuesto)*p.getQuantity());
            }
        }

        findViews();
        retrofitInit();
        events();
        desabilitarMedios();

        if (netopagar.intValue() == 0) {
            btnefectivo.setEnabled(false);
            tvefectivo.setEnabled(false);
            tvefectivo.setTextColor(getResources().getColor(R.color.colorBlackDisable));
            btnefectivo.setImageResource(R.drawable.efectivo_desactivado);
        } else if (netopagar.intValue() > 0) {
            //Comprobar medio de pago efectivo
            String medioscaja = SPM.getString(Constantes.MEDIOS_PAGO);
            if (!medioscaja.contains(MPEFECTIVO)) {
                isEfectivo = false;
                tvefectivo.setTextColor(getResources().getColor(R.color.colorBlackDisable));
                btnefectivo.setImageResource(R.drawable.efectivo_desactivado);
                btnefectivo.setEnabled(false);
                tvefectivo.setEnabled(false);
            }
            if (medioscaja.contains(MPTEFCONTINGUENCIA)) {
                btntarjetacont.setImageResource(R.drawable.tef_cont);
                btntarjetacont.setEnabled(true);
                tvtarjetacont.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }else{
                btntarjetacont.setEnabled(false);
                tvtarjetacont.setEnabled(false);
            }
            new CargarMediosPagoAsyncTask().execute();
        }else {
            btntarjetacont.setImageResource(R.drawable.tef_cont);
            btntarjetacont.setEnabled(true);
            tvtarjetacont.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflaterMenu = getMenuInflater();
        inflaterMenu.inflate(R.menu.menumediospagos, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.mItemTEFUltimaMP:
                TipoDatafono tipoDatafono = (TipoDatafono) SPM.getObject(Constantes.OBJECT_TIPO_DATAFONO, TipoDatafono.class);
                if(tipoDatafono != null){
                    if(tipoDatafono.getNombreDispositivo().equals(Constantes.DATAFONO_SMARTPOS_N6)){
                        if(SPM.getString(Constantes.ID_TRANSACCION_DATAFONO) != null){
                            consultarUltimaTefN6();
                        }else{
                            Utilidades.sweetAlert(getResources().getString(R.string.mensaje),
                                    "No se encontro una ultima Transacción para el datafono",
                                    SweetAlertDialog.NORMAL_TYPE, MediosPagosActivity.this);
                        }
                    }else{
                        pPB();
                        i = new Intent(MediosPagosActivity.this, DatafonoActivity.class);
                        i.putExtra("tipoOperacion", "ultimaTransaccion");
                        i.putExtra("tipoDatafono", tipoDatafono);
                        startActivity(i);
                    }
                }else{
                    Utilidades.sweetAlert(getResources().getString(R.string.alerta),
                            "Debe configurar el tipo de Datafono a utilizar",
                            SweetAlertDialog.WARNING_TYPE, MediosPagosActivity.this);
                }
                break;
            case R.id.mItemTestComunicacionMP:
                tipoTefDialogFragment = new TipoTefDialogFragment(this);
                tipoTefDialogFragment.show(getSupportFragmentManager(),"TipoTefDialogFragment");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void consultarUltimaTefN6() {
        util.mostrarPantallaCargaCustom(getSupportFragmentManager(),
                getResources().getString(R.string.progress_consultando_ultima_tef),
                false, null);
        Call<ResponseConsultarCompraN6> call = Utilidades.servicioRetrofit().doConsultarCompraN6(SPM.getString(Constantes.USER_NAME)+" - "+getResources().getString(R.string.version_apk),
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
                                    SweetAlertDialog.NORMAL_TYPE, MediosPagosActivity.this);
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

    //Recibir respuesta desde otos activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constantes.RESP_TEF_VENTA && data != null) {
            Payment newPayment = (Payment) data.getSerializableExtra("pagoDatafono");
            newPayment.setDueDate(thisDate);
            newPayment.setEliminable(false);
            newPayment.setId(importesList.size() + 1);
            importesList.add(newPayment);
            actualizarGrip();

            //Pasar a la impresión del comprobante solo para rompefilas
            TipoDatafono tipoDatafono = (TipoDatafono) SPM.getObject(Constantes.OBJECT_TIPO_DATAFONO, TipoDatafono.class);
            if(tipoDatafono.getNombreDispositivo().equals(Constantes.DATAFONO_SMARTPOS_N6)){
                Utilidades.mjsToast("Transaccion exitosa", Constantes.TOAST_TYPE_SUCCESS,
                        Toast.LENGTH_LONG, MediosPagosActivity.this);
            }else{
                pPB();
                Intent i = Utilidades.activityImprimir(MediosPagosActivity.this);
                i.putExtra("lablePrint", (Serializable) "CompTEF");
                i.putExtra("primeraimpresion", true);
                i.putExtra("claveValorTef", false);
                i.putExtra("respDatafono", newPayment.getDatafonoEntity());
                startActivity(i);
            }
        } else if (requestCode == Constantes.RESP_MERCADO_PAGO && data != null) {
            Payment newPayment = (Payment) data.getSerializableExtra("pagoMercadoPago");
            newPayment.setDueDate(thisDate);
            newPayment.setEliminable(false);
            newPayment.setId(importesList.size() + 1);
            importesList.add(newPayment);
            actualizarGrip();

            //Pasar a la impresión del comprobante Mercado Pago
            Intent i = Utilidades.activityImprimir(MediosPagosActivity.this);
            i.putExtra("lablePrint", (Serializable) "MercadoPago");
            i.putExtra("mpImporte", newPayment.getRespuestaMercadoPago());
            startActivity(i);
        } else if (requestCode == Constantes.RESP_SODEXO && data != null) {
            Payment newPayment = (Payment) data.getSerializableExtra("pagoSodexo");
            newPayment.setDueDate(thisDate);
            newPayment.setEliminable(false);
            newPayment.setId(importesList.size() + 1);
            importesList.add(newPayment);
            actualizarGrip();
        }else if (requestCode == Constantes.RESP_BANCOLOMBIA_QR && data != null) {
            Payment newPayment = (Payment) data.getSerializableExtra("bancolombiaqr");
            newPayment.setDueDate(thisDate);
            newPayment.setEliminable(false);
            newPayment.setId(importesList.size() + 1);
            importesList.add(newPayment);
            actualizarGrip();
        }
    }

    //Actualizar el GripRecyclerViewAdapter
    private void actualizarGrip() {
        importeGF.setPayments(importesList);
        totalizar();
    }

    @Override
    public void tipoComunicacionDatafono(TipoDatafono tipoDatafono) {
        tipoTefDialogFragment.dismiss();
        Intent i;

        if(tipoDatafono.getNombreDispositivo().equals(Constantes.DATAFONO_SMARTPOS_N6)){
            i = new Intent(MediosPagosActivity.this, DatafonoN6Activity.class);
        }else{
            i = new Intent(MediosPagosActivity.this, DatafonoActivity.class);
        }

        i.putExtra("tipoOperacion",  "testComunicacion");
        i.putExtra("tipoDatafono", tipoDatafono);
        startActivity(i);
    }

    private class CargarMediosPagoAsyncTask extends AsyncTask<Void,Void, List<MediosPagoCajaEntity>> {

        @Override
        protected List<MediosPagoCajaEntity> doInBackground(Void... voids) {
            return BazarPosMovilDB.getBD(getApplication()).mediosPagoCajaDao().getAll();
        }

        @Override
        protected void onPostExecute(List<MediosPagoCajaEntity> mediosPagoCaja) {
            super.onPostExecute(mediosPagoCaja);
            List<MediosCaja> listmediospagoscaja = new ArrayList<>();
            for (int i = 0; i < mediosPagoCaja.size(); i++) {
                MediosCaja mc = new MediosCaja(mediosPagoCaja.get(i).getCaja(),mediosPagoCaja.get(i).getCodigo(),
                        mediosPagoCaja.get(i).getDivisa(),mediosPagoCaja.get(i).getNombre(),mediosPagoCaja.get(i).getPais(),
                        mediosPagoCaja.get(i).getTipo(),mediosPagoCaja.get(i).getTpe(),mediosPagoCaja.get(i).getTpeValue());
                listmediospagoscaja.add(mc);
            }

            String mediosdisponibles = "";
            for (String str : mediospagoslist)
                mediosdisponibles = mediosdisponibles + "," + str;

            if (!(mediosdisponibles.indexOf(MPEFECTIVO) > 0)) {
                isEfectivo = false;
            }

            for (int i = 0; i < listmediospagoscaja.size(); i++) {
                if (mediosdisponibles.indexOf(listmediospagoscaja.get(i).getCodigo()) > 0) {
                    listmediospagoscaja.get(i).setIsEnabled(true);
                } else {
                    listmediospagoscaja.get(i).setIsEnabled(false);
                }
                if (listmediospagoscaja.get(i).getCodigo().equals(MPEFECTIVO)) {
                    MPEFECTIVONOMBRE = listmediospagoscaja.get(i).getNombre();
                } else if (listmediospagoscaja.get(i).getCodigo().equals(MPTEFCONTINGUENCIA)) {
                    MPTEFCONTINGUENCIANOMBRE = listmediospagoscaja.get(i).getNombre();
                } else if (listmediospagoscaja.get(i).getTpe()) {
                    mediospagosdatafonolist.add(listmediospagoscaja.get(i));
                } else {
                    mediospagosotroslist.add(listmediospagoscaja.get(i));
                }
                habilitarMedios();
                pPB();
            }
        }
    }

    private void desabilitarMedios() {
        btntarjetacont.setEnabled(false);
        btnotrosmedios.setEnabled(false);
        tvotrosmedios.setEnabled(false);
        btntarjeta.setEnabled(false);
        tvtarjeta.setEnabled(false);
    }
    private void habilitarMedios() {
        if(!mediospagosotroslist.isEmpty()){
            btnotrosmedios.setImageResource(R.drawable.menu_hamburguesatef);
            tvotrosmedios.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            btnotrosmedios.setEnabled(true);
            tvotrosmedios.setEnabled(true);
        }
        if(!mediospagosdatafonolist.isEmpty()){
            btntarjeta.setImageResource(R.drawable.medios_de_pago_tef);
            tvtarjeta.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            btntarjeta.setEnabled(true);
            tvtarjeta.setEnabled(true);
        }
    }

    //Instanciar el Cliebte REST
    private void retrofitInit() {
        AppCliente appCliente = AppCliente.getInstance();
        apiService = appCliente.getApiService();
    }

    //Asignacion de Referencias
    private void findViews() {
        util = new Utilidades();
        tvmontopago = findViewById(R.id.tvNetoPagarMontoMP);
        tvmontopago.setText(formatea.format(netopagar));
        tvimporte = findViewById(R.id.tvImporteIngresadoMontoMP);
        tvcambio = findViewById(R.id.tvCambioMontoMP);
        tvcambio.setText("0");
        tvpendiente = findViewById(R.id.tvPedienteMontoMP);
        tvpendiente.setText(formatea.format(netopagar));
        btnefectivo = findViewById(R.id.ivEfectivoMP);
        btntarjeta = findViewById(R.id.ivTEFonlineMP);
        btntarjetacont = findViewById(R.id.ivTEFcontiguenciaMP);
        btnotrosmedios = findViewById(R.id.ivOtrosMediosMP);
        btnimprimir = findViewById(R.id.ibImprimirMP);
        btnimprimir.setVisibility(View.GONE);
        tvefectivo = findViewById(R.id.tvEfectivoMP);
        tvtarjetacont = findViewById(R.id.tvTEFcontiguenciaMP);
        tvtarjeta = findViewById(R.id.tvTEFonlineMP);
        tvotrosmedios = findViewById(R.id.tvOtrosMediosMP);
        pb = findViewById(R.id.pbMediosPagos);
        pb.setVisibility(View.GONE);
    }

    //Asignacion de eventos
    private void events() {
        btnefectivo.setOnClickListener(this);
        tvefectivo.setOnClickListener(this);
        btntarjeta.setOnClickListener(this);
        tvtarjeta.setOnClickListener(this);
        tvotrosmedios.setOnClickListener(this);
        btnotrosmedios.setOnClickListener(this);
        btntarjetacont.setOnClickListener(this);
        tvtarjetacont.setOnClickListener(this);
        btnimprimir.setOnClickListener(this);
    }

    private long mLastClickTime = 0;
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {

        // Preventing multiple clicks, using threshold of 1 second
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        int id = v.getId();

        switch (id) {
            case R.id.ivTEFcontiguenciaMP:
            case R.id.tvTEFcontiguenciaMP:
                if (deuda.intValue() == 0) {
                    msjNoImporte();
                } else {
                    new CargarTarjetaAsyncTask(MPTEFCONTINGUENCIANOMBRE,deuda).execute();
                }
                break;
            case R.id.ivEfectivoMP:
            case R.id.tvEfectivoMP:
                if (deuda.intValue() == 0) {
                    msjNoImporte();
                } else {
                    efectivo();
                }
                break;
            case R.id.ivTEFonlineMP:
            case R.id.tvTEFonlineMP:
                if (deuda.intValue() == 0) {
                    msjNoImporte();
                } else {
                    if(mediospagosdatafonolist.size() == 1){
                        datafono(mediospagosdatafonolist.get(0));
                    }else{
                        selectMedioPago("TEF Online");
                    }
                }
                break;
            case R.id.tvOtrosMediosMP:
            case R.id.ivOtrosMediosMP:
                if (deuda.intValue() == 0) {
                    msjNoImporte();
                } else {
                    selectMedioPago("Otros Medios");
                }
                break;
            case R.id.ibImprimirMP:
                if(!pbActivo){
                    descuentoCupoEmpleado = 0.0;
                    for (Payment payment : importesList) {
                        if(SPM.getString(Constantes.MEDIOS_PAGOS_CREDITOS).contains(payment.getMethodId())){
                            descuentoCupoEmpleado = descuentoCupoEmpleado + payment.getAmount();
                        }
                    }

                    if (descuentoCupoEmpleado != 0.0) {
                        consultarCupoEmpleado();
                    } else {
                        validarImpresion();
                    }
                }else{
                    msjToast("Procesando pago");
                }
                break;
        }
    }

    private void consultarCupoEmpleado() {
        iPB();
        btnimprimir.setVisibility(View.GONE);
        //Consultar la Api de Cliente
        Call<ResponseCupoEmpleado> call = apiService.doCupoEmpleado(SPM.getString(Constantes.USER_NAME)+" - "+getResources().getString(R.string.version_apk), clienteid);
        call.enqueue(new Callback<ResponseCupoEmpleado>() {
            @SuppressLint("StringFormatMatches")
            @Override
            public void onResponse(@NonNull Call<ResponseCupoEmpleado> call, @NonNull Response<ResponseCupoEmpleado> response) {

                if (response.isSuccessful()) {
                    pPB();
                    btnimprimir.setVisibility(View.VISIBLE);
                    //Validar que exista el cliente
                    assert response.body() != null;
                    if (response.body().getEsValida()) {
                        ResponseCupoEmpleado cupoEmpleado = response.body();
                        empresaTemporal = cupoEmpleado.getEmpresa();
                        if (descuentoCupoEmpleado <= cupoEmpleado.getCupo() ||
                                validarEmpresaTemporales(cupoEmpleado.getEmpresa())) {
                            cupoEmpleadoTotal = cupoEmpleado.getCupo();
                            validarImpresion();
                        }else{
                            double cupo = cupoEmpleado.getCupo();
                            Utilidades.sweetAlert(getResources().getString(R.string.informacion),
                                    String.format(getResources().getString(R.string.error_cupo_empleado),
                                            Utilidades.formatearPrecio(cupo), Utilidades.formatearPrecio(descuentoCupoEmpleado)),
                                    SweetAlertDialog.WARNING_TYPE, MediosPagosActivity.this);
                        }
                    } else {
                        Utilidades.sweetAlert(getResources().getString(R.string.error),
                                response.body().getMensaje() + " Cod ("+ Constantes.SERVICIO_CONSULTA_CUPO_EMPLEADO +")", SweetAlertDialog.ERROR_TYPE, MediosPagosActivity.this);
                    }
                } else {
                    pPB();
                    btnimprimir.setVisibility(View.VISIBLE);
                    Utilidades.sweetAlert(getResources().getString(R.string.error),
                            getResources().getString(R.string.error_conexion_sb) + response.message() + " Cod ("+ Constantes.SERVICIO_CONSULTA_CUPO_EMPLEADO +")",
                            SweetAlertDialog.ERROR_TYPE, MediosPagosActivity.this);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseCupoEmpleado> call, @NonNull Throwable t) {
                pPB();
                btnimprimir.setVisibility(View.VISIBLE);
                Utilidades.sweetAlert(getResources().getString(R.string.error),
                        getResources().getString(R.string.error_conexion) + t.getMessage()+ " Cod ("+ Constantes.SERVICIO_CONSULTA_CUPO_EMPLEADO +")",
                        SweetAlertDialog.ERROR_TYPE, MediosPagosActivity.this);
            }
        });
    }

    private boolean validarEmpresaTemporales(String empresaCliente){
        boolean temporal = false;
        String[] empresas = SPM.getString(Constantes.EMPRESA_TEMPORALES).split(";");
        for(String empresa: empresas){
            if(empresa.equals(empresaCliente)){
                temporal = true;
                break;
            }
        }

        return temporal;
    }

    public void validarImpresion(){
        iPB();
        btnimprimir.setVisibility(View.GONE);
        if(SPM.getBoolean(Constantes.MODO_AUTONOMO)){
            try {
                guardarCompra();
            }  catch (ExecutionException | InterruptedException | ParseException e) {
                msjToast("Error al realiza la actualización de la base de datos: "+e.getMessage());
            }
        }else{
            if(error_realizar_pagos_tef){
                if(count_realizar_pagos_tef == 0){
                    count_realizar_pagos_tef++;
                    realizar_pagos_tef();
                }else{
                    impresion();
                }
            }else if(error_enviar_descuentos){
                if(count_enviar_descuentos == 0){
                    count_enviar_descuentos++;
                    enviar_descuentos();
                }else{
                    realizar_pagos_tef();
                }
            }else if(error_guardar_pagos_tef){
                error_guardar_pagos_tef = false;
                guardarPagosTef();
            }else if(error_cerrar_documento_venta){
                if(count_cerrar_documento_venta == 0){
                    cerrar_documento_venta(numtrans);
                }else{
                    enviar_descuentos();
                }
            }else if(errorActualizarCupoEmpleado){
                errorActualizarCupoEmpleado = false;
                actualizarCupoEmpleado();
            }else if(errorExtraerInfoDocumento){
                errorExtraerInfoDocumento = false;
                extraerInfoDocumento();
            }else if(errorFacturaElectronicaQr){
                errorFacturaElectronicaQr = false;
                facturaElectronicaQR();
            }else if(errorGuardarTraza){
                errorGuardarTraza = false;
                guardarTrazaFE();
            }else if(error_enviar_documento){
                existe_documento();
            }else{
                validar_apertura_consecutivo();
            }
        }
    }

    boolean pbActivo = false;
    boolean cargarMedioPagoCambio = true;
    boolean error_enviar_documento = false;
    boolean error_cerrar_documento_venta = false;
    int count_cerrar_documento_venta = 0;
    boolean error_enviar_descuentos = false;
    int count_enviar_descuentos = 0;
    boolean error_realizar_pagos_tef = false;
    boolean error_guardar_pagos_tef = false;
    boolean errorFacturaElectronicaQr = false;

    boolean errorExtraerInfoDocumento = false;
    boolean errorGuardarTraza = false;
    boolean cufeCalculado = false;
    int count_realizar_pagos_tef = 0;
    boolean errorActualizarCupoEmpleado = false;

    private void validar_apertura_consecutivo() {
        //Consultar la Api de Apertura de Caja
        Call<ResponseValidarAperturaCaja> callAperturaCaja = apiService.doAperturaCaja(usuario,caja);
        callAperturaCaja.enqueue(new Callback<ResponseValidarAperturaCaja>() {
            @Override
            public void onResponse(Call<ResponseValidarAperturaCaja> call, Response<ResponseValidarAperturaCaja> response) {
                //Validar que la respuesta de Api sea correcta
                if (response.isSuccessful()) {
                    //Validar que la caja tiene apertura
                    if (response.body().getEsValida()) {
                        //Validar que la caja este abierta
                        if (response.body().getEstadoCaja().equals("ABIERTA")) {
                            SPM.setString(Constantes.CAJERO_CODE, response.body().getCajero());
                            SPM.setString(Constantes.CAJERO_NAME, response.body().getNombreVendedor());
                            //Consultar la Api de Consecutivo Fiscal
                            Call<ResponseConsecutivoFiscal> callConsecutivoFiscal = apiService.doConsecutivoFiscal(usuario,caja);
                            callConsecutivoFiscal.enqueue(new Callback<ResponseConsecutivoFiscal>() {
                                @Override
                                public void onResponse(Call<ResponseConsecutivoFiscal> call, Response<ResponseConsecutivoFiscal> response) {
                                    if (response.isSuccessful()) {
                                        if (response.body().getEsValida()) {
                                            consecutivo = response.body().getConsecutivo().toString();
                                            prefijo = response.body().getPrefijo();
                                            internalReference = prefijo + "-" + consecutivo;
                                            tipoResolucion = response.body().getCc().getTipoResolucion();
                                            claveFacturaElectronica = response.body().getCc().getClaveFacturaElectronica();
                                            referenciaInternaValidacion = prefijo + "  " + consecutivo;

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
                                            SPM.setInt(Constantes.CC_CONSECUTIVO, response.body().getCc().getConsecutivo()+1);

                                            enviar_documento();
                                        } else {
                                            for (String s : response.body().getMensaje()) {
                                                LogFile.adjuntarLog("Error ErrorResponseConsecutivoFiscal: " + s);
                                                mensajeSimpleDialog(getResources().getString(R.string.error), s);
                                            }
                                            pPB();
                                            btnimprimir.setVisibility(View.VISIBLE);
                                        }
                                    } else {
                                        LogFile.adjuntarLog("Error ErrorResponseConsecutivoFiscal: " + response.message());
                                        mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion_sb) + response.message());
                                        pPB();
                                        btnimprimir.setVisibility(View.VISIBLE);
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseConsecutivoFiscal> call, Throwable t) {
                                    LogFile.adjuntarLog("Error ErrorResponseConsecutivoFiscal: " + call + t);
                                    mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion) + t.getMessage());
                                    pPB();
                                    btnimprimir.setVisibility(View.VISIBLE);
                                }
                            });
                        } else {
                            String msjbase = getResources().getString(R.string.caja_n_cerrada);
                            String msjbaseFormateada = String.format(msjbase, caja);
                            mensajeSimpleDialog(getResources().getString(R.string.error), msjbaseFormateada);
                            pPB();
                            btnimprimir.setVisibility(View.VISIBLE);
                        }
                    } else {
                        msjToast(response.body().getMensaje());
                        pPB();
                        btnimprimir.setVisibility(View.VISIBLE);
                    }
                } else {
                    LogFile.adjuntarLog("Error ResponseValidarAperturaCaja: " + response.message());
                    mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion_sb) + response.message());
                    pPB();
                    btnimprimir.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ResponseValidarAperturaCaja> call, Throwable t) {
                LogFile.adjuntarLog("Error ResponseValidarAperturaCaja: " + call + t);
                mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion) + t.getMessage());
                pPB();
                btnimprimir.setVisibility(View.VISIBLE);
            }
        });
    }

    private void enviar_documento() {
        error_enviar_documento = true;
        if(cargarMedioPagoCambio){
            cargarMedioPagoCambio = false;
            if (cambio > 0) {
                importesList.add(new Payment("CAMBIO", (cambio * -1), divisa, thisDate, importesList.size() + 1, 1, MPEFECTIVO));
            } else {
                //Se agrega un medio de pago en mas y menos para que el servicio web finalice la transacción
                if (netopagar.intValue() == 0 && importesList.size() == 0) {
                    importesList.add(new Payment("EFECTIVO", 1.0, divisa, thisDate, importesList.size() + 1, 1, MPEFECTIVO));
                    importesList.add(new Payment("EFECTIVO", -1.0, divisa, thisDate, importesList.size() + 1, 1, MPEFECTIVO));
                }
            }
        }

        String referidoId = SPM.getString(Constantes.REFERIDO_ID);
        HeaderSD headersd = new HeaderSD("1", divisa, customerid, thisDate, internalReference, cajerocode, tienda, "0", "Receipt", tienda, referidoId);
        Payments payments = new Payments(importesList);

        List<LineSD> listLineSD = new ArrayList<LineSD>();
        for (Producto p : productosList) {
            LineSD item = null;
            if (p.getQuantity() > 0) {
                item = new LineSD(p.getPrecio().toString(), p.getQuantity().toString(), p.getEan(), vendedorcode, p.getPrecioOriginal().toString(), "", null, "");
            }else{
                Double precioP = Math.abs(p.getPrecio());
                item = new LineSD(precioP.toString(), p.getQuantity().toString(), p.getEan(), vendedorcode, p.getPrecioOriginal().toString(), "", null, "");
            }
            listLineSD.add(item);
        }

        LinesSD linessd = new LinesSD(listLineSD);
        RequestSaleDocumentCreate requestSaleDocumentCreate = new RequestSaleDocumentCreate(null, headersd, linessd, payments, caja);
        Call<ResponseDomentoFiscal> callSaleDocument = apiService.doSaleDocument(usuario,requestSaleDocumentCreate);
        callSaleDocument.enqueue(new Callback<ResponseDomentoFiscal>() {
            @Override
            public void onResponse(Call<ResponseDomentoFiscal> call, Response<ResponseDomentoFiscal> response) {

                if (response.isSuccessful()) {
                    if (response.body().getEsValida()) {
                        numtrans = response.body().getNumber();
                        textoFiscal = response.body().getReferenciaFiscalFirma();
                        enviar_descuentos();
                    } else {
                        mensajeSimpleDialog(getResources().getString(R.string.error), response.body().getMensaje());
                        pPB();
                        btnimprimir.setVisibility(View.VISIBLE);
                    }
                } else {
                    LogFile.adjuntarLog("Error ResponseSaleDocumentCreate: " + internalReference + ": " + response.message());
                    mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion_sb) + response.message());
                    pPB();
                    btnimprimir.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ResponseDomentoFiscal> call, Throwable t) {
                LogFile.adjuntarLog("Error ResponseSaleDocumentCreate: " + internalReference + ": " + call + t);
                mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion) + t.getMessage());
                pPB();
                btnimprimir.setVisibility(View.VISIBLE);
            }
        });
    }

    private void existe_documento() {

        Call<ResponseDD> call = apiService.doConsultarDocumentoDetalle(usuario,internalReference);
        call.enqueue(new Callback<ResponseDD>() {
            @Override
            public void onResponse(Call<ResponseDD> call, Response<ResponseDD> response) {
                if(response.isSuccessful()){
                    if(response.body().getError()){
                        enviar_documento();
                    }else{
                        String customeridDD = response.body().getDocumentoDetalle().getHeaderDD().getCustomerId();
                        String fechaDD = response.body().getDocumentoDetalle().getHeaderDD().getDate();
                        if(fechaDD.length() > 10){
                            fechaDD = fechaDD.substring(0,10);
                        }
                        String tiendaDD = response.body().getDocumentoDetalle().getHeaderDD().getStoreId();
                        Double totalDD = Double.parseDouble(response.body().getDocumentoDetalle().getHeaderDD().getTaxIncludedTotalAmount());
                        Double cantDouble = Double.parseDouble(response.body().getDocumentoDetalle().getHeaderDD().getTotalQuantity());
                        Integer cantDD = cantDouble.intValue();

                        boolean esigual = true;
                        if(!customeridDD.equals(customerid)){
                            esigual = false;
                        }
                        if(!tiendaDD.equals(tienda)){
                            esigual = false;
                        }
                        if(!fechaDD.equals(thisDate)){
                            esigual = false;
                        }
                        if(!totalDD.equals(totalcompra)){
                            esigual = false;
                        }
                        if(!cantDD.equals(cantP)){
                            esigual = false;
                        }
                        if(productosList.size() != response.body().getDocumentoDetalle().getLineDDS().size()){
                            esigual = false;
                        }
                        if(importesList.size() != response.body().getDocumentoDetalle().getPaymentsDDS().size()){
                            esigual = false;
                        }

                        if(esigual){
                            List<LineDD> linesDDS = response.body().getDocumentoDetalle().getLineDDS();
                            for (int i = 0; i < productosList.size(); i++) {
                                if(!linesDDS.get(i).getItemReference().equals(productosList.get(i).getEan())){
                                    esigual = false;
                                    break;
                                }
                                Double cantEAN = Double.parseDouble(linesDDS.get(i).getQuantity());
                                if (!productosList.get(i).getQuantity().equals(cantEAN.intValue())) {
                                    esigual = false;
                                    break;
                                }
                            }
                            if(esigual){
                                List<PaymentsDD> payDDS = response.body().getDocumentoDetalle().getPaymentsDDS();
                                for (int i = 0; i < importesList.size(); i++) {
                                    if(!importesList.get(i).getMethodId().equals(payDDS.get(i).getCode())){
                                        esigual = false;
                                        break;
                                    }
                                    Double amount = Double.parseDouble(payDDS.get(i).getAmount());
                                    if (!importesList.get(i).getAmount().equals(amount)) {
                                        esigual = false;
                                        break;
                                    }
                                }
                                if(esigual){
                                    error_enviar_documento = false;
                                    numtrans = response.body().getDocumentoDetalle().getHeaderDD().getNumber();
                                    cerrar_documento_venta(response.body().getDocumentoDetalle().getHeaderDD().getNumber());
                                }else{
                                    enviar_documento();
                                }
                            }else{
                                enviar_documento();
                            }

                        }else{
                            enviar_documento();
                        }
                    }
                }else{
                    LogFile.adjuntarLog("Error ResponseConsultarDocumento: " + internalReference + ": " + response.message());
                    mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion_sb) + response.message());
                    pPB();
                    btnimprimir.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ResponseDD> call, Throwable t) {
                pPB();
                LogFile.adjuntarLog("Error ResponseConsultarDocumento: " + internalReference + ": " + call + t);
                mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion) + t.getMessage());
                btnimprimir.setVisibility(View.VISIBLE);
            }
        });
    }

    private void cerrar_documento_venta(String number) {

        RequestPostDocumento requestPostDocumento = new RequestPostDocumento(Integer.parseInt(number),caja);
        Call<ResponsePostDocumento> callPostDocumento = apiService.doPostDocumento(usuario,requestPostDocumento);
        callPostDocumento.enqueue(new Callback<ResponsePostDocumento>() {
            @Override
            public void onResponse(Call<ResponsePostDocumento> call, Response<ResponsePostDocumento> response) {
                if(response.isSuccessful()){
                    if(response.body().isError()){
                        LogFile.adjuntarLog("Error ResponsePostDocumento: " + internalReference + ": " + response.body().getMensaje());
                        msjToast(response.body().getMensaje());
                        btnimprimir.setVisibility(View.VISIBLE);
                        count_cerrar_documento_venta++;
                        pPB();
                    }else{
                        textoFiscal = response.body().getPostDocumento().getReferenciaFiscalFirma();
                        error_cerrar_documento_venta = false;
                        enviar_descuentos();
                    }
                }else{
                    LogFile.adjuntarLog("Error ResponsePostDocumento: " + internalReference + ": " + response.message());
                    mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion_sb) + response.message());
                    pPB();
                    btnimprimir.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ResponsePostDocumento> call, Throwable t) {
                pPB();
                LogFile.adjuntarLog("Error ResponsePostDocumento: " + internalReference + ": " + call + t);
                mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion) + t.getMessage());
                btnimprimir.setVisibility(View.VISIBLE);
            }
        });
    }

    private void enviar_descuentos() {
        error_enviar_descuentos = true;

        //Los decuentos pos document
        List<Detalle> detalleDesc = new ArrayList<>();
        int cont = 1;

        for (Producto p : productosList) {
            Double precio = Math.abs(p.getPrecio());
            if (!p.getPrecioOriginal().equals(precio)) {
                Double importeIngresoConImpuestos = p.getPrecioOriginal() - precio;
                Double porcDto = (importeIngresoConImpuestos / p.getPrecioOriginal()) * 100.00;

                Double ivaCalc = 1.00;
                if(p.getValorTasa() != null){
                    if (p.getValorTasa() > 0) {
                        ivaCalc = 1 + (p.getValorTasa() / 100);
                    }
                }

                String motivoDescuento = "SOL";
                if (p.getPeriodoTarifa().equals("000006")) {
                    motivoDescuento = "SAL";
                }

                Double porcentajeDescuento = round(porcDto, 2);
                porcDto = importeIngresoConImpuestos / ivaCalc;
                Double importeIngresoSinImpuestos = round(porcDto, 0);

                Detalle d = new Detalle(motivoDescuento, porcentajeDescuento, 0, p.getPrecioOriginal() * p.getQuantity(),
                        p.getPrecioOriginal() * p.getQuantity(), importeIngresoSinImpuestos * p.getQuantity(),
                        importeIngresoSinImpuestos * p.getQuantity(),
                        importeIngresoConImpuestos * p.getQuantity(), importeIngresoConImpuestos * p.getQuantity(),
                        0, "FFO", 0, cont, cont, 1,
                        0, tienda, thisDate, divisa, divisa, "001", "", "0");

                detalleDesc.add(d);
            }
            cont++;
        }

        if (!detalleDesc.isEmpty()) {

            RequestDetallesDescuento requestDetallesDescuento = new RequestDetallesDescuento(tienda, Integer.parseInt(numtrans), detalleDesc);
            Call<String> callRequestCondicionesPosVenta = apiService.doRequestDetallesDescuento(usuario,requestDetallesDescuento);
            callRequestCondicionesPosVenta.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        error_enviar_descuentos = false;
                        realizar_pagos_tef();
                    }else{
                        LogFile.adjuntarLog("Error ResponseDetallesDescuento: " + internalReference + ": " + response.message());
                        mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion_sb_doc_finalizado) + response.message());
                        pPB();
                        btnimprimir.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    LogFile.adjuntarLog("Error ResponseDetallesDescuento: " + internalReference + ": " + call + t);
                    mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion_doc_finalizado) + t.getMessage());
                    btnimprimir.setVisibility(View.VISIBLE);
                    pPB();
                }
            });
        }else{
            error_enviar_descuentos = false;
            realizar_pagos_tef();
        }
    }

    private void realizar_pagos_tef() {
        error_realizar_pagos_tef = true;
        //Realizar Pagos TEF
        List<PagosTEF> pagosTEF = new ArrayList<>();
        for (Payment pay : importesList) {
            if (pay.getIsTEF() && pay.getDatafonoEntity() != null) {
                DatafonoEntity rd = pay.getDatafonoEntity();
                String respuestaTEF = "Autorizador=Credibanco|Franquicia=" + rd.getFranquicia() + "|TipoCuenta=" + rd.getTipoCuenta() + "|Iva=" + rd.getIva() + "|Cuotas=" + rd.getCuotas() + "|CodTerminal=" + rd.getCodigoTerminal() + "|RRN=" + rd.getRrn() + "|BIN=" + rd.getBin() + "|CodRta=" + rd.getCodigoRespuesta() + "|CodigoUnico=" + rd.getCodigoAprobacion() + "|VlrBaseDevolu= |DirEstablecimiento=" + rd.getDirEstablecimiento() + "|FechaVenTarjeta=" + rd.getFechaVenTarjeta() + "|";
                PagosTEF nuevoPagosTEF = new PagosTEF(caja, pay.getMethodId(), rd.getUltDigitoTarj(),
                        rd.getFechaVenTarjeta(), rd.getCodigoAprobacion(), Integer.parseInt(rd.getCuotas()), pay.getId(), Integer.parseInt(numtrans),
                        rd.getNumRecibo(), respuestaTEF, "");
                pagosTEF.add(nuevoPagosTEF);
            } else if (pay.getMethodId().equals(MPTEFCONTINGUENCIA)) {
                PagosTEF nuevoPagosTEF = new PagosTEF(caja, pay.getMethodId(), "",
                        "", "", null, pay.getId(), Integer.parseInt(numtrans),
                        "", "", pay.getCodigoTarjeta());
                pagosTEF.add(nuevoPagosTEF);
            } else if (pay.getRespuestaMercadoPago() != null) {
                PagosTEF nuevoPagosTEF = new PagosTEF(caja, pay.getMethodId(), "",
                        "", pay.getRespuestaMercadoPago().getId(), null, pay.getId(), Integer.parseInt(numtrans),
                        pay.getRespuestaMercadoPago().getEr(), pay.getRespuestaMercadoPago().getRespuestaJSON(), "");
                pagosTEF.add(nuevoPagosTEF);
            }
        }

        if (!pagosTEF.isEmpty()) {
            RequestTEF requestTEF = new RequestTEF(pagosTEF);
            Call<String> callTEF = apiService.doRequestTEF(usuario,requestTEF);
            callTEF.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    if (response.isSuccessful()) {
                        if (response.body().equals("true")) {
                            error_realizar_pagos_tef = false;
                            guardarPagosTef();
                        }else{
                            LogFile.adjuntarLog("Error ResponseRequestTEF: " + internalReference + ": " + response.body());
                            mensajeSimpleDialog(getResources().getString(R.string.error),"Error al enviar los pagos TEF");
                            pPB();
                            btnimprimir.setVisibility(View.VISIBLE);
                        }
                    } else {
                        LogFile.adjuntarLog("Error ResponseRequestTEF: " + internalReference + ": " + response.message());
                        mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion_sb_doc_finalizado) + response.message());
                        pPB();
                        btnimprimir.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    LogFile.adjuntarLog("Error ResponseRequestTEF: " + internalReference + ": " + call + t);
                    mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion_doc_finalizado) + t.getMessage());
                    btnimprimir.setVisibility(View.VISIBLE);
                    pPB();
                }
            });
        } else {
            error_realizar_pagos_tef = false;
            guardarPagosTef();
        }
    }

    private void guardarPagosTef() {
        List<RequestGuardarRespuestasTef> respuestaDatafonoList = new ArrayList<>();
        for(Payment payment : importesList){
            if(payment.getIsTEF() && payment.getDatafonoEntity() != null){
                String horaTransaccion;
                String minutosTrasaccion;
                boolean errorHoraMinutos = false;
                if(payment.getDatafonoEntity().getHora().trim().length() == 4){
                    horaTransaccion = payment.getDatafonoEntity().getHora().substring(0, 2);
                    minutosTrasaccion = payment.getDatafonoEntity().getHora().substring(2, 4);
                }else{
                    horaTransaccion = "";
                    minutosTrasaccion = "";
                    errorHoraMinutos = true;
                }

                CuerpoRespuestaDatafonoN6 datafonoN6 = new CuerpoRespuestaDatafonoN6(payment.getDatafonoEntity().getCodigoAprobacion(),
                        Double.toString(payment.getDatafonoEntity().getMonto()), Double.toString(payment.getDatafonoEntity().getIva()),
                        payment.getDatafonoEntity().getNumRecibo(), null, payment.getDatafonoEntity().getCodigoTerminal(),
                        payment.getDatafonoEntity().getFecha(), payment.getDatafonoEntity().getHora(), payment.getDatafonoEntity().getFranquicia(),
                        payment.getDatafonoEntity().getTipoCuenta(), payment.getDatafonoEntity().getCuotas(),
                        payment.getDatafonoEntity().getUltDigitoTarj(), payment.getDatafonoEntity().getBin(),
                        payment.getDatafonoEntity().getFechaVenTarjeta(), payment.getDatafonoEntity().getCodigoComercio(),
                        payment.getDatafonoEntity().getDirEstablecimiento(), caja);
                RequestGuardarRespuestasTef respuestaTef = new RequestGuardarRespuestasTef(errorHoraMinutos ? "" : horaTransaccion,
                        errorHoraMinutos ? "" : minutosTrasaccion, numtrans, tienda, caja, internalReference, datafonoN6);

                respuestaDatafonoList.add(respuestaTef);
            }
        }

        if(!respuestaDatafonoList.isEmpty()){
            Call<ResponseGuardarRespuestasTef> call = Utilidades.servicioRetrofit().doGuardarRespuestasTef(
                    usuario+" - "+getResources().getString(R.string.version_apk), respuestaDatafonoList);
            call.enqueue(new Callback<ResponseGuardarRespuestasTef>() {
                @Override
                public void onResponse(@NonNull Call<ResponseGuardarRespuestasTef> call, @NonNull Response<ResponseGuardarRespuestasTef> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        if(response.body().isEsValida()){
                            error_guardar_pagos_tef = false;
                            actualizarCupoEmpleado();
                        }else{
                            mensajeSimpleDialog(getResources().getString(R.string.error), response.body().getMensaje(),
                                    Constantes.SERVICIO_GUARDAR_RESPUESTAS_TEF);
                            pPB();
                            btnimprimir.setVisibility(View.VISIBLE);
                            error_guardar_pagos_tef = true;
                        }
                    }else{
                        LogFile.adjuntarLog("Error ResponseGuardarRespuestasTef: " + internalReference + ": " + response.message());
                        mensajeSimpleDialog(getResources().getString(R.string.error), getResources().getString(R.string.error_conexion_sb) + response.message(),
                                Constantes.SERVICIO_GUARDAR_RESPUESTAS_TEF);
                        pPB();
                        btnimprimir.setVisibility(View.VISIBLE);
                        error_guardar_pagos_tef = true;
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseGuardarRespuestasTef> call, @NonNull Throwable t) {
                    LogFile.adjuntarLog("Error ResponseGuardarRespuestasTef: " + internalReference + ": " + call + t);
                    mensajeSimpleDialog(getResources().getString(R.string.error), getResources().getString(R.string.error_conexion) + t.getMessage(), Constantes.SERVICIO_GUARDAR_RESPUESTAS_TEF);
                    btnimprimir.setVisibility(View.VISIBLE);
                    pPB();
                    error_guardar_pagos_tef = true;
                }
            });
        }else{
            actualizarCupoEmpleado();
        }
    }

    private void actualizarCupoEmpleado(){
        if(descuentoCupoEmpleado != 0.0){
            if(validarEmpresaTemporales(empresaTemporal)){
                extraerInfoDocumento();
            }else{
                int actualizarCupo = (int) (cupoEmpleadoTotal - descuentoCupoEmpleado);
                //Api para actualizar el cupo del empleado
                RequestActualizarCupoEmpleado requestActualizarCupoEmpleado
                        = new RequestActualizarCupoEmpleado(clienteid, actualizarCupo,
                        empresaTemporal,
                        SPM.getString(Constantes.FIRST_NAME_CLIENTE) + " " + SPM.getString(Constantes.LAST_NAME_CLIENTE));
                Call<String> callActualizarCupo = apiService.doActualizarCupoEmpleado(SPM.getString(Constantes.USER_NAME)+" - "+getResources().getString(R.string.version_apk),
                        requestActualizarCupoEmpleado);
                callActualizarCupo.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            if (response.body().equals("true")) {
                                errorActualizarCupoEmpleado = false;
                                extraerInfoDocumento();
                            }else{
                                errorActualizarCupoEmpleado = true;
                                Utilidades.sweetAlert(getResources().getString(R.string.error),
                                        "Error al actualizar el cupo del empleado",
                                        SweetAlertDialog.ERROR_TYPE, MediosPagosActivity.this);
                            }
                        }else{
                            errorActualizarCupoEmpleado = true;
                            pPB();
                            btnimprimir.setVisibility(View.VISIBLE);
                            Utilidades.sweetAlert(getResources().getString(R.string.error),
                                    getResources().getString(R.string.error_conexion_sb) + response.message(),
                                    SweetAlertDialog.ERROR_TYPE, MediosPagosActivity.this);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        errorActualizarCupoEmpleado = true;
                        btnimprimir.setVisibility(View.VISIBLE);
                        Utilidades.sweetAlert(getResources().getString(R.string.error),
                                getResources().getString(R.string.error_conexion) + t.getMessage(),
                                SweetAlertDialog.ERROR_TYPE, MediosPagosActivity.this);
                        pPB();
                    }
                });
            }
        }else{
            extraerInfoDocumento();
        }
    }

    private void extraerInfoDocumento(){
        RequestExtraerInfoDocumento requestExtraerInfoDocumento = new RequestExtraerInfoDocumento(
                Integer.parseInt(numtrans),
                tienda,
                "FFO");
        Call<ResponseExtraerInfoDocumento> call = Utilidades.servicioRetrofit().doExtraerInfoDocumento(
                SPM.getString(Constantes.USER_NAME)+" - "+getResources().getString(R.string.version_apk),
                requestExtraerInfoDocumento);

        call.enqueue(new Callback<ResponseExtraerInfoDocumento>() {
            @Override
            public void onResponse(@NonNull Call<ResponseExtraerInfoDocumento> call, @NonNull Response<ResponseExtraerInfoDocumento> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isEsValida()) {
                        try {
                            errorFacturaElectronicaQr = false;
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                            fechaCreacionFE = dateFormat.parse(response.body().getRespuestaInfo().getFechaCreacion());
                            facturaElectronicaQR();
                        } catch (ParseException e) {
                            errorFacturaElectronicaQr = true;
                            mensajeSimpleDialog(getResources().getString(R.string.error), "Extraer info api: "+ e.getMessage());
                            throw new RuntimeException(e);
                        }
                    }else{
                        pPB();
                        btnimprimir.setVisibility(View.VISIBLE);
                        mensajeSimpleDialog(getResources().getString(R.string.error), response.body().getMensaje());
                        errorFacturaElectronicaQr = true;
                    }
                }else{
                    errorExtraerInfoDocumento = true;
                    mensajeSimpleDialog(getResources().getString(R.string.error), getResources().getString(R.string.error_conexion_sb) + response.message());
                    pPB();
                    btnimprimir.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseExtraerInfoDocumento> call, @NonNull Throwable t) {
                errorExtraerInfoDocumento = true;
                pPB();
                mensajeSimpleDialog(getResources().getString(R.string.error), getResources().getString(R.string.error_conexion) + t.getMessage());
                btnimprimir.setVisibility(View.VISIBLE);
            }
        });
    }

    private void facturaElectronicaQR(){
        RequestGenerarQRFE requestGenerarQRFE = new RequestGenerarQRFE(Integer.parseInt(numtrans),
                SPM.getString(Constantes.TIENDA_CODE), "FFO", "FV", referenciaInternaValidacion,
                textoFiscal);

        Call<ResponseGenerarQRFE> call = Utilidades.servicioRetrofit().doFacturaElectronicaQR(
                SPM.getString(Constantes.USER_NAME)+" - "+getResources().getString(R.string.version_apk),
                requestGenerarQRFE
        );
        call.enqueue(new Callback<ResponseGenerarQRFE>() {
            @Override
            public void onResponse(@NonNull Call<ResponseGenerarQRFE> call, @NonNull Response<ResponseGenerarQRFE> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isEsValida()) {
                        UploadResponse upload = response.body().getRespuesta().getUploadResponse();
                        errorFacturaElectronicaQr = false;
                        cufeHash = upload.getCufe();
                        cufeCalculado = false;
                        guardarTrazaFE();
                    }else{
                        errorFacturaElectronicaQr = false;
                        if(response.body().getMensaje().equals("No es posible hacer mas reintentos.")
                                || response.body().getMensaje().contains("ERROR CARVAJAL:")){
                            calcularCufe();
                            cufeCalculado = true;
                            guardarTrazaFE();
                        }else if(response.body().getMensaje().contains("ERROR:")){
                            pPB();
                            btnimprimir.setVisibility(View.VISIBLE);
                            mensajeSimpleDialog(getResources().getString(R.string.error), response.body().getMensaje());
                            errorFacturaElectronicaQr = true;
                        }else{
                            facturaElectronicaQR();
                        }
                    }
                }else{
                    errorFacturaElectronicaQr = true;
                    mensajeSimpleDialog(getResources().getString(R.string.error), getResources().getString(R.string.error_conexion_sb) + response.message());
                    pPB();
                    btnimprimir.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseGenerarQRFE> call, @NonNull Throwable t) {
                errorFacturaElectronicaQr = true;
                pPB();
                mensajeSimpleDialog(getResources().getString(R.string.error), getResources().getString(R.string.error_conexion) + t.getMessage());
                btnimprimir.setVisibility(View.VISIBLE);
            }
        });
    }

    private void calcularCufe() {
        calcularTasaImpuesto();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#0.00", symbols);

        BigDecimal totalCompra = BigDecimal.valueOf(totalcompra);
        BigDecimal ivaCompra = BigDecimal.valueOf(ivacompraFE);
        BigDecimal totalSinIva = totalCompra.subtract(ivaCompra);

        String numFac = prefijo + consecutivo;
        String fecha = util.getThisDateSimpleFE();
        String horFac = util.obtenerHoraConGMT(fechaCreacionFE);
        String valorFac = decimalFormat.format(totalSinIva.setScale(2, RoundingMode.HALF_UP));
        String valImp1 = decimalFormat.format(ivaCompra.setScale(2, RoundingMode.HALF_UP));
        String valImp2 = "0.00";
        String valImp3 = "0.00";
        String valTotal = decimalFormat.format(totalCompra.setScale(2, RoundingMode.HALF_UP));
        String nitFE = "890901672";
        String numAdq = clienteid.equals(SPM.getString(Constantes.CLIENTE_GENERICO)) ? "222222222222": clienteid;
        String clTec = claveFacturaElectronica;
        String tipoAmbiente = SPM.getString(Constantes.AMBIENTE_FE_QR);

        String codImp1 = "01";
        String codImp2 = "04";
        String codImp3 = "03";

        String cufeInput = numFac + fecha + horFac + valorFac + codImp1 + valImp1 + codImp2 + valImp2 + codImp3
                    + valImp3 + valTotal + nitFE + numAdq + clTec + tipoAmbiente;

        cufeHash = generateSHA384Hash(cufeInput).toUpperCase();
    }

    private static String generateSHA384Hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-384");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating SHA-384 hash", e);
        }
    }

    public void calcularTotalCompraIva(){
        totalCompraIva = totalcompra;
        if (!totalcompra.equals(baseLmp + ivacompraFE)) {
            totalCompraIva = totalcompra - (totalcompra - baseLmp - ivacompraFE);
        }
    }

    public void calcularTasaImpuesto(){
        baseLmp = 0.0;
        ivaCompra = 0.0;
        ivacompraFE = 0.0;
        totalcompra = 0.0;
        for (Producto producto : productosList) {
            totalcompra = totalcompra + producto.getPrecio();
            if (producto.getCodigoTasaImpuesto().equals("NOR")) {
                Double precioSinImpuestoFE = (producto.getPrecio()) / (((producto.getValorTasa()) / 100) + 1);
                Double precioSinImpuesto = (double) Math.round((producto.getPrecio()) / (((producto.getValorTasa()) / 100) + 1));
                baseLmp = baseLmp + precioSinImpuesto;
                ivacompraFE = ivacompraFE + (producto.getPrecio() - precioSinImpuestoFE);
                ivaCompra = ivaCompra + (producto.getPrecio() - precioSinImpuesto);
            }
        }
    }

    public void guardarTrazaFE(){
        RequestGuardarTrazaFE requestGuardarTrazaFE = new RequestGuardarTrazaFE(
                Integer.parseInt(numtrans),
                SPM.getString(Constantes.TIENDA_CODE), "FFO", tipoResolucion,
                referenciaInternaValidacion, Integer.parseInt(consecutivo),
                prefijo, cufeCalculado ? cufeHash : "");

        Call<ResponseGuardarTrazaFE> call = Utilidades.servicioRetrofit().doFacturaElectronicaGuardarTraza(
                SPM.getString(Constantes.USER_NAME)+" - "+getResources().getString(R.string.version_apk),
                requestGuardarTrazaFE);
        call.enqueue(new Callback<ResponseGuardarTrazaFE>() {
            @Override
            public void onResponse(@NonNull Call<ResponseGuardarTrazaFE> call, @NonNull Response<ResponseGuardarTrazaFE> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isEsValida()) {
                        pPB();
                        impresion();
                    }else{
                        pPB();
                        if(response.body().getMensaje().equals("La referencia ya se encuentra insertada")){
                            impresion();
                        }else{
                            btnimprimir.setVisibility(View.VISIBLE);
                            mensajeSimpleDialog(getResources().getString(R.string.error), response.body().getMensaje());
                        }
                    }
                }else{
                    errorGuardarTraza = true;
                    mensajeSimpleDialog(getResources().getString(R.string.error), getResources().getString(R.string.error_conexion_sb) + response.message());
                    pPB();
                    btnimprimir.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseGuardarTrazaFE> call, @NonNull Throwable t) {
                errorGuardarTraza = true;
                pPB();
                mensajeSimpleDialog(getResources().getString(R.string.error), getResources().getString(R.string.error_conexion) + t.getMessage());
                btnimprimir.setVisibility(View.VISIBLE);
            }
        });
    }

    private void impresion() {
        Intent intent = Utilidades.activityImprimir(MediosPagosActivity.this);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("lablePrint", (Serializable) "factura");
        intent.putExtra("prefijo", (Serializable) prefijo);
        intent.putExtra("consecutivo", (Serializable) consecutivo);
        intent.putExtra("numtrans", (Serializable) numtrans);
        intent.putExtra("textoFiscal", (Serializable) textoFiscal);
        intent.putExtra("cufeHash", cufeHash);
        intent.putExtra("cambio", cambio);
        intent.putExtra("totalcompra", totalcompra);
        intent.putExtra("listProductos", (Serializable) productosList);
        intent.putExtra("listImportes", (Serializable) importesList);
        intent.putExtra("vaucherText", (Serializable) vaucherText);
        intent.putExtra("vaucherPalabra", (Serializable) vaucherPalabra);
        intent.putExtra("primeraimpresion", true);
        startActivity(intent);
        finish();
    }

    private void guardarCompra() throws ExecutionException, InterruptedException, ParseException {

        //inicia validaciones fiscales
        Integer numini = SPM.getInt(Constantes.CC_NUMERO_INICIAL);
        Integer numfin = SPM.getInt(Constantes.CC_NUMERO_FINAL);
        Integer consecutivoNum = SPM.getInt(Constantes.CC_CONSECUTIVO);

        if(consecutivoNum <= numini || consecutivoNum >= numfin){
            mensajeSimpleDialog(getResources().getString(R.string.error),SPM.getString(Constantes.CC_ALERTA_LIMITE_FISCAL));
            pPB();
            LogFile.adjuntarLog("Error en consusumo de consecutivo fiscal.");
            return;
        }
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String fechaF = SPM.getString(Constantes.CC_FECHA_FINAL);
        Date fechaFinal = format.parse(fechaF.substring(0,10));
        String fechaA = SPM.getString(Constantes.CC_FECHA_AUTORIZACION);
        Date fechaAutorizacion = format.parse(fechaA.substring(0,10));
        Date hoy = new Date();

        if (hoy.before(fechaAutorizacion) || hoy.after(fechaFinal)){
            mensajeSimpleDialog(getResources().getString(R.string.error),SPM.getString(Constantes.CC_ALERTA_VENCIMIENTO_RESOLUCION));
            pPB();
            LogFile.adjuntarLog("Error en fecha de Autorización de consecutivo fiscal.");
            return;
        }
        consecutivo = consecutivoNum.toString();
        prefijo = SPM.getString(Constantes.CC_PREFIJO);
        internalReference = prefijo + "-" + consecutivo;
        String log = "Trx: "+internalReference;

        String anoA = fechaA.substring(0,4);
        String mesA = fechaA.substring(5,7);
        String diaA = fechaA.substring(8,10);

        textoFiscal = "Factura : "+prefijo+" "+consecutivo+" Res. Dian "+SPM.getString(Constantes.CC_NUMERO_RESOLUCION)+" DE "+diaA+"/"+mesA+"/"+anoA+" Rango "+numini+" A "+numfin+" AUTORIZA";
        numtrans = "-";
        //fin validaciones fiscales

        //inicia guarda documento
        String referidoId = SPM.getString(Constantes.REFERIDO_ID);
        DocHeaderEntity docHeader = new DocHeaderEntity("1", divisa, customerid, thisDate, internalReference, cajerocode, tienda, "0", "Receipt", tienda, referidoId,totalcompra,ivacompra,cantP);
        new DocHeaderCrearAsyncTask().execute(docHeader).get();
        log = log + "\nHeader: "+docHeader.toString();

        if(!validateDoc){
            validateDoc = true;
            if (cambio > 0) {
                importesList.add(new Payment("CAMBIO", (cambio * -1), divisa, thisDate, importesList.size() + 1, 1, MPEFECTIVO));
            } else {
                //Se agrega un medio de pago en mas y menos para que el servicio web finalice la transacción
                if (netopagar.intValue() == 0 && importesList.size() == 0) {
                    importesList.add(new Payment("EFECTIVO", 1.0, divisa, thisDate, importesList.size() + 1, 1, MPEFECTIVO));
                    importesList.add(new Payment("EFECTIVO", -1.0, divisa, thisDate, importesList.size() + 1, 1, MPEFECTIVO));
                }
            }
        }
        log = log + "\nLine: "+productosList.size();
        int cont = 1;

        for (Producto p : productosList) {
            Double precio = Math.abs(p.getPrecio());
            DocLineEntity docLine = new DocLineEntity(internalReference,precio.toString(), p.getQuantity().toString(),
                    p.getEan(), vendedorcode, p.getPrecioOriginal().toString(),
                    "", p.getSerialNumberId(), "");
            new DocLineCrearAsyncTask().execute(docLine).get();
            log = log + "\n"+docLine.toString();

            if (!p.getPrecioOriginal().equals(precio)) {
                Double importeIngresoConImpuestos = p.getPrecioOriginal() - precio;
                Double porcDto = (importeIngresoConImpuestos / p.getPrecioOriginal()) * 100.00;

                Double ivaCalc = 1.00;
                if(p.getValorTasa() != null){
                    if (p.getValorTasa() > 0) {
                        ivaCalc = 1 + (p.getValorTasa() / 100);
                    }
                }

                String motivoDescuento = "SOL";
                if (p.getPeriodoTarifa().equals("000006")) {
                    motivoDescuento = "SAL";
                }

                Double porcentajeDescuento = round(porcDto, 2);
                porcDto = importeIngresoConImpuestos / ivaCalc;
                Double importeIngresoSinImpuestos = round(porcDto, 0);

                DetalleDescuentoEntity docDD = new DetalleDescuentoEntity(internalReference,motivoDescuento, porcentajeDescuento, 0, p.getPrecioOriginal() * p.getQuantity(),
                        p.getPrecioOriginal() * p.getQuantity(), importeIngresoSinImpuestos * p.getQuantity(), importeIngresoSinImpuestos * p.getQuantity(),
                        importeIngresoConImpuestos * p.getQuantity(), importeIngresoConImpuestos * p.getQuantity(),
                        0, "FFO", 0, cont, cont, 1,
                        0, tienda, thisDate, divisa, divisa, "001", "", "0");

                new DetalleDescuentoCrearAsyncTask().execute(docDD).get();
                log = log + "\n"+docDD.toString();
            }
            cont++;
        }

        log = log + "\nPayment: "+importesList.size();
        for (Payment pay : importesList) {
            DocPaymentEntity docPayment = new DocPaymentEntity(internalReference,pay.getName(),pay.getAmount(),pay.getCurrencyId(),pay.getDueDate(),pay.getId(),pay.getIsReceivedPayment(),pay.getMethodId());
            new DocPaymentCrearAsyncTask().execute(docPayment).get();
            log = log + "\n"+docPayment.toString();

            if (pay.getIsTEF() && pay.getDatafonoEntity() != null) {
                DatafonoEntity rd = pay.getDatafonoEntity();
                String respuestaTEF = "Autorizador=Credibanco|Franquicia=" + rd.getFranquicia() + "|TipoCuenta=" + rd.getTipoCuenta() + "|Iva=" + rd.getIva() + "|Cuotas=" + rd.getCuotas() + "|CodTerminal=" + rd.getCodigoTerminal() + "|RRN=" + rd.getRrn() + "|BIN=" + rd.getBin() + "|CodRta=" + rd.getCodigoRespuesta() + "|CodigoUnico=" + rd.getCodigoAprobacion() + "|VlrBaseDevolu= |DirEstablecimiento=" + rd.getDirEstablecimiento() + "|FechaVenTarjeta=" + rd.getFechaVenTarjeta() + "|";
                PagosTEFEntity pagoTEF = new PagosTEFEntity(internalReference,caja, pay.getMethodId(), rd.getUltDigitoTarj(),
                        rd.getFechaVenTarjeta(), rd.getCodigoAprobacion(), Integer.parseInt(rd.getCuotas()), pay.getId(),
                        rd.getNumRecibo(), respuestaTEF, "");
                new PagoTEFCrearAsyncTask().execute(pagoTEF).get();
                log = log + "\nTEF: "+pagoTEF.toString();
            } else if (pay.getMethodId().equals(MPTEFCONTINGUENCIA)) {
                PagosTEFEntity pagoTEF = new PagosTEFEntity(internalReference,caja, pay.getMethodId(), "",
                        "", "", null, pay.getId(),
                        "", "", pay.getCodigoTarjeta());
                new PagoTEFCrearAsyncTask().execute(pagoTEF).get();
                log = log + "\nTEF CONTINGUENCIA: "+pagoTEF.toString();

                //Lista de string de medio de pago en la caja
                List<String> listFranq = Arrays.asList(pay.getTajeta().getNombre().split(" - "));
                String franquicia = listFranq.get(0);
                String tipoCuenta = listFranq.get(1);
                pay.setName(cleanString("CONT. "+franquicia+": "+tipoCuenta));

            } else if (pay.getMethodId().equals(MPMERCADOPAGO) && pay.getRespuestaMercadoPago() != null) {
                PagosTEFEntity pagoTEF = new PagosTEFEntity(internalReference,caja, pay.getMethodId(), "",
                        "", pay.getRespuestaMercadoPago().getId(), null, pay.getId(),
                        pay.getRespuestaMercadoPago().getEr(), pay.getRespuestaMercadoPago().getRespuestaJSON(), "");
                new PagoTEFCrearAsyncTask().execute(pagoTEF).get();

            }
        }
        //fin guarda documento
        LogFile.adjuntarLog(log);
        SPM.setInt(Constantes.CC_CONSECUTIVO,(consecutivoNum+1));
        impresion();
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

    private class DocLineCrearAsyncTask extends AsyncTask<DocLineEntity,Void,Void>{

        @Override
        protected Void doInBackground(DocLineEntity... docLineEntity) {
            try{
                BazarPosMovilDB.getBD(getApplication()).documentoDao().insertLine(docLineEntity[0]);
            }catch (Exception ex){
                msjToast("Error al crear la linea: "+ex.getMessage());
            }
            return null;
        }
    }

    private class DocPaymentCrearAsyncTask extends AsyncTask<DocPaymentEntity,Void,Void>{

        @Override
        protected Void doInBackground(DocPaymentEntity... docPaymentEntity) {
            try{
                BazarPosMovilDB.getBD(getApplication()).documentoDao().insertPayment(docPaymentEntity[0]);
            }catch (Exception ex){
                msjToast("Error al crear un pago: "+ex.getMessage());
            }
            return null;
        }
    }

    private class PagoTEFCrearAsyncTask extends AsyncTask<PagosTEFEntity,Void,Void>{

        @Override
        protected Void doInBackground(PagosTEFEntity... pagosTEF) {
            try{
                BazarPosMovilDB.getBD(getApplication()).documentoDao().insertPagoTEF(pagosTEF[0]);
            }catch (Exception ex){
                msjToast("Error al crear un pago TEF: "+ex.getMessage());
            }
            return null;
        }
    }

    private class DetalleDescuentoCrearAsyncTask extends AsyncTask<DetalleDescuentoEntity,Void,Void>{

        @Override
        protected Void doInBackground(DetalleDescuentoEntity... detalleDescuento) {
            try{
                BazarPosMovilDB.getBD(getApplication()).documentoDao().insertDetalleDescuento(detalleDescuento[0]);
            }catch (Exception ex){
                msjToast("Error al crear el detalle de descuento: "+ex.getMessage());
            }
            return null;
        }
    }

    private void msjNoImporte() {
        msjToast(getResources().getString(R.string.no_importe_neto_pago));
    }

    private void efectivo() {
        if (isEfectivo) {
            PagoEfectivoDialogFragment dialogPagoEfectivo = new PagoEfectivoDialogFragment(deuda);
            dialogPagoEfectivo.show(getSupportFragmentManager(), "PagoEfectivo");
        } else {
            msjToast(getResources().getString(R.string.medio_efectivo_no));
        }
    }

    private void datafono(MediosCaja itemMedio) {
        TipoDatafono tipoDatafono = (TipoDatafono) SPM.getObject(Constantes.OBJECT_TIPO_DATAFONO, TipoDatafono.class);
        if(tipoDatafono != null){
            Intent i;

            if(tipoDatafono.getNombreDispositivo().equals(Constantes.DATAFONO_SMARTPOS_N6)){
                i = new Intent(MediosPagosActivity.this, DatafonoN6Activity.class);
            }else{
                i = new Intent(MediosPagosActivity.this, DatafonoActivity.class);
            }

            pPB();

            i.putExtra("tipoOperacion", "compra");
            i.putExtra("tipoDatafono", tipoDatafono);
            i.putExtra("total", netopagar);
            i.putExtra("deuda", deuda);
            i.putExtra("iva", ivacompra);
            i.putExtra("itemMedio", (Serializable) itemMedio);
            startActivityForResult(i, 44);
        }else{
            Utilidades.sweetAlert(getResources().getString(R.string.alerta),
                    "Debe configurar el tipo de Datafono a utilizar",
                    SweetAlertDialog.WARNING_TYPE, MediosPagosActivity.this);
        }
    }

    private void otrosMedios(MediosCaja item) {
        PagoTipoBonoDialogFragment dialogPagoTipoBono = new PagoTipoBonoDialogFragment(item.getNombre(), item.getCodigo(), netopagar, deuda);
        dialogPagoTipoBono.show(getSupportFragmentManager(), "PagoEfectivo");
    }

    private void iPB() {
        pbActivo = true;
        if(util.estadoProgressCustom()){
            util.mostrarPantallaCargaCustom(getSupportFragmentManager(), "Cargando...",
                    false, null);
        }
    }

    private void pPB() {
        pbActivo = false;
        util.ocultarPantallaCargaCustom();
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    @Override
    public void onListFragmentInteraction(Payment item, Integer position) {
        ImporteGripDialogFragment dialogImporteGrip = new ImporteGripDialogFragment(item, position);
        dialogImporteGrip.show(getSupportFragmentManager(), "ImporteGrip");
    }

    private void selectMedioPago(String tipo) {
        if (tipo.equals("TEF Online")) {
            medioPagoSelecDF = new MedioPagoSelecDialogFragment(tipo, mediospagosdatafonolist);
            medioPagoSelecDF.show(getSupportFragmentManager(), "MedioPagoSelec");
        } else {
            medioPagoSelecDF = new MedioPagoSelecDialogFragment(tipo, mediospagosotroslist);
            medioPagoSelecDF.show(getSupportFragmentManager(), "MedioPagoSelec");
        }
    }

    private void totalizar() {
        Double calcImportes = 0.0, calcCambio = 0.0;
        btnimprimir.setVisibility(View.GONE);
        if (importesList.size() > 0) {
            for (int i = 0; i < importesList.size(); i++) {
                if (importesList.get(i).getAmount().intValue() > 0) {
                    calcImportes = calcImportes + importesList.get(i).getAmount().intValue();
                } else {
                    if (netopagar > 0) {
                        importesList.remove(i);
                    } else {
                        calcImportes = calcImportes + importesList.get(i).getAmount().intValue();
                    }
                }
            }
            if (Math.abs(calcImportes) >= Math.abs(netopagar)) {
                calcCambio = Math.abs(calcImportes) - Math.abs(netopagar);
                deuda = 0.0;
                btnimprimir.setVisibility(View.VISIBLE);
            } else {
                deuda = netopagar - (calcImportes);
            }
        } else {
            deuda = netopagar;
        }
        netoimporte = calcImportes;
        cambio = calcCambio;

        tvimporte.setText(formatea.format(netoimporte));
        tvcambio.setText(formatea.format(cambio));
        tvpendiente.setText(formatea.format(deuda));

        if (calcCambio <= 0) {
            tvcambio.setTextColor(Color.parseColor("#8D8D8D"));
        } else {
            tvcambio.setTextColor(Color.RED);
        }

    }

    public void mensajeSimpleDialog(String titulo, String msj, int servicio) {
        int icon = SweetAlertDialog.WARNING_TYPE;
        if (titulo.equals(getResources().getString(R.string.error))) {
            icon = SweetAlertDialog.ERROR_TYPE;
        }

        Utilidades.sweetAlert(titulo, msj + " Cod ("+servicio+")", icon, MediosPagosActivity.this);
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
    public void sendInputItemPagoEfectivoDialogFragment(Double importe) {
        importesList.add(new Payment("EFECTIVO", importe, divisa, thisDate, importesList.size() + 1, 1, MPEFECTIVO));
        actualizarGrip();
    }

    @Override
    public void sendInputListMedioPagoSelecDialogFragment(String tipo, MediosCaja item) {
        abirMedioPago(tipo, item);
    }

    @Override
    public void sendInputItemAutorizacion(String code){
        abirMedioPago("Otros Medios", mediospagosotroslist.get(Integer.parseInt(code)));
    }


    private void abirMedioPago(String tipo, MediosCaja item) {
        if (tipo.equals("TEF Online")) {
            datafono(item);
        }else if(item.getCodigo().equals(SPM.getString(Constantes.MEDIO_PAGO_QRBANCOLOMBIA))){
            Intent i = new Intent(MediosPagosActivity.this, BancolombiaQrActivity.class);
            i.putExtra("total",  netopagar);
            i.putExtra("deuda",  deuda);
            i.putExtra("itemMedio", item);
            startActivityForResult(i,Constantes.RESP_BANCOLOMBIA_QR);
        }else{
            otrosMedios(item);
        }
    }

    private class CargarTarjetaAsyncTask extends AsyncTask<Void,Void, List<TarjetaEntity>> {

        String nombre;
        Double duedaT;

        CargarTarjetaAsyncTask(String nombre, Double dueda) {
            // list all the parameters like in normal class define
            this.nombre = nombre;
            this.duedaT = dueda;
        }

        @Override
        protected List<TarjetaEntity> doInBackground(Void... voids) {
            return BazarPosMovilDB.getBD(getApplication()).tarjetaDao().getAll();
        }

        @Override
        protected void onPostExecute(List<TarjetaEntity> tarjetaList) {
            super.onPostExecute(tarjetaList);
            List<Tarjeta> listTarjetasB = new ArrayList<>();
            for (int i = 0; i < tarjetaList.size(); i++) {
                Tarjeta t = new Tarjeta(tarjetaList.get(i).getCodigo(),tarjetaList.get(i).getNombre(),
                        tarjetaList.get(i).getPais(),tarjetaList.get(i).getTipo());
                listTarjetasB.add(t);
            }
            PagoTEFContinguenciaDialogFragment dialogPagoTEFContinguencia = new PagoTEFContinguenciaDialogFragment(nombre, duedaT, listTarjetasB);
            dialogPagoTEFContinguencia.show(getSupportFragmentManager(), "PagoTEFContinguencia");
        }
    }

    @Override
    public void sendInputItemImporteGripDialogFragment(Integer inputposition) {
        importesList.remove(inputposition.intValue());
        actualizarGrip();
    }

    @Override
    public void sendInputItemImporteTEFGripDialogFragment(DatafonoEntity respDatafono, MercadoPagoImporte mpImporte) {

        Intent i = Utilidades.activityImprimir(MediosPagosActivity.this);
        if (respDatafono != null) {
            //Pasar a la impresión del comprobante TEF
            i.putExtra("lablePrint", (Serializable) "CompTEF");
            i.putExtra("respDatafono", respDatafono);
            i.putExtra("primeraimpresion", false);
        } else {
            //Pasar a la impresión del comprobante Mercado Pago
            i.putExtra("lablePrint", (Serializable) "MercadoPago");
            i.putExtra("mpImporte", mpImporte);
        }
        startActivity(i);
    }

    @Override
    public void sendInputItemPagoTipoBonoDialogFragment(String titulo, String tipoPago, Double importe) {
        importesList.add(new Payment(titulo, importe, divisa, thisDate, importesList.size() + 1, 1, tipoPago));
        actualizarGrip();
    }


    @Override
    public void sendInputItemPagoTEFContinguenciaDialogFragment(Tarjeta tajeta, Double importe, String nombre) {
        Payment newPayment = new Payment(nombre, importe, divisa, thisDate, importesList.size() + 1, 1, MPTEFCONTINGUENCIA);
        newPayment.setCodigoTarjeta(tajeta.getCodigo());
        newPayment.setTajeta(tajeta);
        importesList.add(newPayment);
        actualizarGrip();
    }

    private void msjToast(String msj) {
        Toast.makeText(MediosPagosActivity.this, msj, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (importesList.size() > 0) {
            //Alerta para confirmar el cierre de medios de pagos
            AlertDialog.Builder builder = new AlertDialog.Builder(MediosPagosActivity.this);
            builder.setTitle(getResources().getString(R.string.cierre_medios_pago))
                    .setMessage(getResources().getString(R.string.importe_realizados_salir))
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

    //Limpiar string de todos los acentos y caracteres especiales
    public static String cleanString(String texto) {
        texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
        texto = texto.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return texto;
    }
}
