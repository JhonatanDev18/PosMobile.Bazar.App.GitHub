package com.crystal.bazarposmobile.ui;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.crystal.bazarposmobile.common.Constantes;
import com.crystal.bazarposmobile.common.IConfigurableCarga;
import com.crystal.bazarposmobile.common.LogFile;
import com.crystal.bazarposmobile.common.SPM;
import com.crystal.bazarposmobile.common.Utilidades;
import com.crystal.bazarposmobile.retrofit.ApiService;
import com.crystal.bazarposmobile.retrofit.AppCliente;
import com.crystal.bazarposmobile.retrofit.request.RequestBancolombiaQR;
import com.crystal.bazarposmobile.retrofit.request.creardocumento.Payment;
import com.crystal.bazarposmobile.retrofit.response.bancolombiaqr.ConsultaPagoQr;
import com.crystal.bazarposmobile.retrofit.response.bancolombiaqr.PagoBancolombiaQr;
import com.crystal.bazarposmobile.retrofit.response.bancolombiaqr.ResponseBancolombiaQr;
import com.crystal.bazarposmobile.retrofit.response.mediospagocaja.MediosCaja;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import com.crystal.bazarposmobile.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BancolombiaQrActivity
        extends
            AppCompatActivity
        implements
            View.OnClickListener,
            IConfigurableCarga {

    //Declaración del Cliente REST
    ApiService apiService;
    String usuario = SPM.getString(Constantes.USER_NAME) + " - "+SPM.getString(Constantes.CAJA_CODE);

    //Declaración de los objetos de la interfaz del DialogFragment
    private TextView tvtotal,tvdeuda;
    private EditText etimporte;
    private Button btncalcular,btnconsultarqr;
    private ProgressBar pb;
    private ImageView ivqr;
    //Declaración de la variables del activity
    DecimalFormat formatea = new DecimalFormat(Constantes.FORMATO_DECIMAL);
    private Double totalCompra;
    private Integer deuda, amount;
    private MediosCaja mediopago;
    boolean consultarQR = true;
    String reference,caja;
    Utilidades util;
    private IConfigurableCarga configurableCarga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bancolombia_qr);
        setTitle(R.string.title_activity_bancolombia_qr);

        if(SPM.getBoolean(Constantes.MODO_AUTONOMO)){
            Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorModoAutonomo)));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Toolbar toolbar = this.findViewById(R.id.action_bar);
                if (toolbar!= null){
                    toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        }

        //variables a utilizar para una compra desde el intent
        totalCompra = getIntent().getExtras().getDouble("total");
        double deudaD = getIntent().getExtras().getDouble("deuda");
        deuda = (int) deudaD;
        mediopago = (MediosCaja)getIntent().getSerializableExtra("itemMedio");
        util = new Utilidades();

        retrofitInit();
        findViews();
        events();

        tvtotal.setText(formatea.format(totalCompra));
        tvdeuda.setText(formatea.format(deuda));
        caja = SPM.getString(Constantes.CAJA_CODE);
    }

    private void events() {
        btncalcular.setOnClickListener(this);
        btnconsultarqr.setOnClickListener(this);

        etimporte.requestFocus();
        //Ocultar el teclado de pantalla
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void GenerarQR(String qrCode){
        String imagenSvg = decodificarBase64(qrCode);
        try {
            SVG svg = SVG.getFromString(imagenSvg);
            svg.setDocumentWidth("100%");
            svg.setDocumentHeight("100%");

            //create a drawable from svg
            PictureDrawable drawable = new PictureDrawable(svg.renderToPicture());
            ivqr.setImageDrawable(drawable);
            util.ocultarPantallaCargaCustom();
        } catch (SVGParseException e) {
            mensajeSimpleDialog("Error", getResources().getString(R.string.error_crear_qr));
            LogFile.adjuntarLog("Error Metodo GenerarQR: "+ e);
        }
    }

    public String decodificarBase64(String dato){
        byte[] datasd = Base64.decode(dato, Base64.DEFAULT);
        dato = new String(datasd, StandardCharsets.UTF_8);
        return dato;
    }

    private void retrofitInit() {
        AppCliente appCliente = AppCliente.getInstance();
        apiService = appCliente.getApiService();
    }

    private void findViews() {
        configurableCarga = this;
        tvtotal = findViewById(R.id.tvTotalBQRA);
        tvdeuda = findViewById(R.id.tvDeudaBQRA);
        btncalcular = findViewById(R.id.btnCalcularBQRA);
        btnconsultarqr = findViewById(R.id.btnConsultarQrBQRA);
        etimporte = findViewById(R.id.etImporteBQRA);
        ivqr = findViewById(R.id.ivBQRA);
        ivqr.setVisibility(View.GONE);
        pb = findViewById(R.id.progressBarBQRA);
        pb.setVisibility(View.GONE);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCalcularBQRA:
                calcular();
                break;
            case R.id.btnConsultarQrBQRA:
                if(consultarQR){
                    if(etimporte.getText().length() > 0){
                        crear_qr();
                    }else{
                        msjToast(getResources().getString(R.string.importe_invalido));
                    }
                }else{
                    consultar_pago();
                }
                break;
        }
    }

    private void consultar_pago() {
        iPB();
        Call<ConsultaPagoQr> call = apiService.doConsultarPagoBancolombiaQr(usuario,reference);
        call.enqueue(new Callback<ConsultaPagoQr>() {
            @Override
            public void onResponse(Call<ConsultaPagoQr> call, Response<ConsultaPagoQr> response) {
                if(response.isSuccessful()) {
                    assert response.body() != null;
                    if(response.body().getError()){
                        mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.pago_no_encontrado_qr_bancolombia));
                    }else{
                        Payment pagoBamcolombiaQR = new Payment(mediopago.getNombre(), Double.parseDouble(amount.toString()), mediopago.getDivisa(), "", 0, 1, mediopago.getCodigo());
                        pagoBamcolombiaQR.setTEF(true);
                        PagoBancolombiaQr responseQR = response.body().getPago();
                        pagoBamcolombiaQR.setRespuestaQrBancolombia(responseQR);
                        Intent intent= new Intent();
                        intent.putExtra("bancolombiaqr", (Serializable) pagoBamcolombiaQR);
                        setResult(Constantes.RESP_BANCOLOMBIA_QR,intent);
                        finish();
                    }
                }else{
                    mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion_bancolombia) + response.message());
                }
                pPB();
            }

            @Override
            public void onFailure(Call<ConsultaPagoQr> call, Throwable t) {
                LogFile.adjuntarLog("Error ConsultarPagoBancolombiaQr: " + call + t);
                mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion) + t.getMessage());
                pPB();
            }
        });
    }

    private void crear_qr() {
        util.mostrarPantallaCargaCustom(getSupportFragmentManager(),"Generando QR...",
                false, configurableCarga);
        iPB();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentDate = new SimpleDateFormat(Constantes.FECHA_PEGADA);
        Date todayDate = new Date();
        reference = currentDate.format(todayDate);
        reference = caja.concat(reference);

        amount = Integer.parseInt(etimporte.getText().toString());
        String docType = SPM.getString(Constantes.TIPO_DOCUMENTO_CLIENTE_DESC_L);
        String docNumber = SPM.getString(Constantes.DOCUMENTO_CLIENTE);
        String cusName = SPM.getString(Constantes.FIRST_NAME_CLIENTE) +" "+ SPM.getString(Constantes.LAST_NAME_CLIENTE);

        RequestBancolombiaQR rBQR = new RequestBancolombiaQR(reference,amount.toString(),docType,docNumber,cusName,
                Constantes.PROPOSITO_BANCOLOMBIA_QR_COMPRA,SPM.getString(Constantes.TIENDA_MERCHANTID), SPM.getString(Constantes.TIENDA_NOMBRE));
        Call<ResponseBancolombiaQr> call = apiService.doBancolombiaQr(usuario,rBQR);
        call.enqueue(new Callback<ResponseBancolombiaQr>() {
            @Override
            public void onResponse(Call<ResponseBancolombiaQr> call, Response<ResponseBancolombiaQr> response) {

                if(response.isSuccessful()) {
                    assert response.body() != null;
                    if(response.body().isError()){
                        util.ocultarPantallaCargaCustom();
                        mensajeSimpleDialog(getResources().getString(R.string.error),response.body().getMensaje());
                    }else{
                        etimporte.setEnabled(false);
                        btncalcular.setEnabled(false);
                        btnconsultarqr.setText(R.string.consultar_pago);
                        GenerarQR(response.body().getQrBancolombia());
                        ivqr.setVisibility(View.VISIBLE);
                        consultarQR = false;
                    }
                    pPB();
                }else{
                    util.ocultarPantallaCargaCustom();
                    mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion_bancolombia) + response.message());
                }
                pPB();
            }

            @Override
            public void onFailure(Call<ResponseBancolombiaQr> call, Throwable t) {
                util.ocultarPantallaCargaCustom();
                LogFile.adjuntarLog("Error ResponseBancolombiaQr: " + call + t);
                mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion) + t.getMessage());
                pPB();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void calcular() {
        String montoText = etimporte.getText().toString();
        if(montoText.length() == 0){
            etimporte.setText(deuda.toString());
            tvdeuda.setText("0");
            tvdeuda.setTextColor(Color.RED);
        }else{
            if(montoText.length() > 9){
                msjToast(getResources().getString(R.string.importe_invalido));
            }else{
                double impMonto = Double.parseDouble(montoText);
                if((int) impMonto > 0){
                    double rest = deuda - (int) impMonto;
                    if(rest >= 0){
                        msjToast(getResources().getString(R.string.dueda_supera_cupo_disponible));
                    }else{
                        msjToast(getResources().getString(R.string.importe_igual_menor_cero));
                    }
                }else{
                    msjToast(getResources().getString(R.string.importe_invalido));
                }
            }
            etimporte.setText("");
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BancolombiaQrActivity.this);
        builder.setTitle(getResources().getString(R.string.cierre_qr_bancolombia))
                .setIcon(R.mipmap.qr_bancolombia)
                .setCancelable(false)
                .setMessage(getResources().getString(R.string.salir_sin_pago_qrbancolombia))
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
    }

    private void iPB() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        pb.setVisibility(View.VISIBLE);

    }

    private void pPB() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        pb.setVisibility(View.GONE);
    }

    private void msjToast(String msj) {
        Toast.makeText(this, msj, Toast.LENGTH_SHORT).show();
    }

    //Alert Dialog para mostrar mensaje de Error o alerta
    public void mensajeSimpleDialog(String titulo, String msj) {

        int icon = R.drawable.msj_alert_30;
        if(titulo.equals(getResources().getString(R.string.error))){
            icon = R.drawable.msj_error_30;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titulo)
                .setCancelable(false)
                .setMessage(msj)
                .setIcon(icon)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void cerrarFragmentCarga() {

    }
}