package com.crystal.bazarposmobile.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;

import com.crystal.bazarposmobile.retrofit.ApiService;
import com.crystal.bazarposmobile.retrofit.AppCliente;
import com.crystal.bazarposmobile.retrofit.request.datafonon6.RequestAnularCompraN6;
import com.crystal.bazarposmobile.retrofit.request.datafonon6.RequestBaseN6;
import com.crystal.bazarposmobile.retrofit.request.datafonon6.RequestCompraN6;
import com.crystal.bazarposmobile.retrofit.request.datafonon6.RequestCompraN6SO;
import com.crystal.bazarposmobile.retrofit.response.perifericos.RespuestaConsultarPerifericos;
import com.crystal.bazarposmobile.ui.ImpresionActivity;
import com.crystal.bazarposmobile.ui.ImpresionIminActivity;
import com.crystal.bazarposmobile.ui.ImpresionSumniActivity;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.ui.dialogfragmen.PantallaCargaDialogFragment;

public class Utilidades {

    SweetAlertDialog progressDialog;
    private String thisDateSimple, thisDateSimpleFE;
    private transient SimpleDateFormat currentDateSimple, currentDateSimpleFE;
    private transient SimpleDateFormat currentDateHora;
    private PantallaCargaDialogFragment pantallaCarga;
    private Date todayDate;
    private String thisHora;

    @SuppressLint("SimpleDateFormat")
    public Utilidades(){
        todayDate = new Date();
        currentDateSimple = new SimpleDateFormat(Constantes.FECHA_SIMPLE);
        currentDateSimpleFE = new SimpleDateFormat(Constantes.FECHA_SIMPLE_FE);
        currentDateHora = new SimpleDateFormat(Constantes.HORA_FECHA);
        thisDateSimple = currentDateSimple.format(todayDate);
        thisDateSimpleFE = currentDateSimpleFE.format(todayDate);

        thisHora = currentDateHora.format(todayDate);
    }

    public static Intent activityImprimir(Context context){
        Intent i = null;
        String manufacturer = Build.MANUFACTURER;

        for(String imin:manufactureImin()) {
            if(manufacturer.equals(imin)){
                i = new Intent(context, ImpresionIminActivity.class);
                break;
            }
        }

        for(String sunmi:manufactureSunmi()){
            if(manufacturer.equals(sunmi)){
                i = new Intent(context, ImpresionSumniActivity.class);
                break;
            }
        }

        if(i == null){
            i = new Intent(context, ImpresionActivity.class);
        }

        return i;
    }

    public static boolean isImin(){
        boolean validacion = false;
        String manufacturer = Build.MANUFACTURER;

        for(String imin:manufactureImin()) {
            if(manufacturer.equals(imin)){
                validacion = true;
                break;
            }
        }

        return validacion;
    }

    public static ArrayList<String> manufactureImin(){
        return new ArrayList<String>(){
            {
                add(Constantes.MANUFACTURER_IMIN);
                add(Constantes.MANUFACTURER_IMIN11);
                add(Constantes.MANUFACTURER_IMIN_NEOSTRA);
            }
        };
    }

    public static ArrayList<String> manufactureSunmi(){
        return new ArrayList<String>(){
            {
                add(Constantes.MANUFACTURER_SUMIN);
            }
        };
    }

    public static String separador(){
        String separador = "";
        String manufacturer = Build.MANUFACTURER;

        if(manufacturer.equals(Constantes.MANUFACTURER_IMIN_NEOSTRA) || manufacturer.equals(Constantes.MANUFACTURER_IMIN11)){
            separador = "----------------------------------------------------------------------------\n";
        }else{
            separador = "----------------------------------------------------------------\n";
        }

        return separador;
    }

    public static boolean isConsumidorFinal(){
        return SPM.getString(Constantes.DOCUMENTO_CLIENTE).equals(SPM.getString(Constantes.CLIENTE_GENERICO));
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "NewApi"})
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void mostrarDialogProgressBar(Context contexto, String mensaje, boolean cancelable){
        progressDialog = new SweetAlertDialog(contexto, SweetAlertDialog.PROGRESS_TYPE);

        progressDialog.getProgressHelper().setBarColor(contexto.getColor(R.color.progress_sweet));
        progressDialog.setContentText(mensaje);
        progressDialog.setCancelable(cancelable);
        progressDialog.show();
    }

    public boolean estadoProgress(){
        return progressDialog == null;
    }

    public static void sweetAlert(String titulo, String mensaje, int icono, Context contexto){
        SweetAlertDialog pDialog = new SweetAlertDialog(contexto, icono);

        pDialog.setTitleText(titulo)
                .setContentText(mensaje)
                .setConfirmButton(contexto.getResources().getString(R.string.ok), SweetAlertDialog::dismissWithAnimation)
                .setCancelable(false);

        pDialog.show();
    }

    public void ocultarDialogProgressBar(){
        if(progressDialog != null){
            progressDialog.dismissWithAnimation();
            progressDialog = null;
        }
    }

    public static ApiService servicioRetrofit(){
        //Declaración del cliente REST
        AppCliente appCliente = AppCliente.getInstance();

        return appCliente.getApiService();
    }

    public static void mjsToast(String mensaje, int tipo, int duracion, Context contexto){
        switch (tipo){
            case Constantes.TOAST_TYPE_ERROR:
                Toasty.error(contexto, mensaje, duracion).show();
                break;
            case Constantes.TOAST_TYPE_INFO:
                Toasty.info(contexto, mensaje, duracion).show();
                break;
            case Constantes.TOAST_TYPE_WARNING:
                Toasty.warning(contexto, mensaje, duracion).show();
                break;
            case Constantes.TOAST_TYPE_NORMAL:
                Toasty.normal(contexto, mensaje, duracion).show();
                break;
            case Constantes.TOAST_TYPE_SUCCESS:
                Toasty.success(contexto, mensaje, duracion).show();
                break;
        }
    }

    @SuppressLint("SimpleDateFormat")
    public String convertirFecha(String fecha){
        String sDate1 = fecha;
        Date dateD = null;
        try {
            dateD = new SimpleDateFormat("yyMMdd-HHmmss").parse(sDate1);
            sDate1 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.US).format(dateD);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return sDate1;
    }

    public static void sweetAlertCustom(String titulo, String mensaje, Context contexto, int imagen){
        SweetAlertDialog alertDialog = new SweetAlertDialog(contexto, SweetAlertDialog.CUSTOM_IMAGE_TYPE);

        alertDialog.setTitleText(titulo)
                .setCustomImage(imagen)
                .setContentText(mensaje)
                .setConfirmButton(contexto.getResources().getString(R.string.entiendo), SweetAlertDialog::dismissWithAnimation)
                .setCancelable(false);

        alertDialog.show();
    }

    public static void guardarPerifericos(List<RespuestaConsultarPerifericos> listPerifericos){
        //Método que funciona para configurar todos los perifericos
        for(RespuestaConsultarPerifericos periferico : listPerifericos){
            switch (periferico.getTipoDispositivo()){
                case Constantes.CONST_IMPRESORA_EPSON:
                    SPM.setString(Constantes.IP_IMPRESORA, periferico.getIpEquipo());
                    SPM.setString(Constantes.IP_PUERTO, periferico.getPuertoEquipo());
                    break;
                case Constantes.CONST_DATAFONO_CREDIBANCO_N6:
                    SPM.setString(Constantes.USUARIO_DATAFONO_CD, periferico.getUsuarioWs());
                    SPM.setString(Constantes.PASS_DATAFONO_CD, periferico.getPasswordWs());
                    SPM.setString(Constantes.CODIGO_DATAFONO_CD, periferico.getCodigoDispositivo());
                    SPM.setString(Constantes.CODIGO_UNICO_DATAFONO_CD, periferico.getCodigoUnicoComercio());
                    break;
                case Constantes.CONST_IMPRESORA_EPSON_BLUETOOTH:
                    SPM.setString(Constantes.MAC_IMPRESORA, periferico.getMacDispositivo());
                    break;
                case Constantes.CONST_DATAFONO_ROMPEFILAS:
                    SPM.setString(Constantes.MAC_DATAFONO_ROMPEFILAS, periferico.getMacDispositivo());
                    SPM.setString(Constantes.NAME_DATAFONO_ROMPEFILAS, periferico.getPuertoEquipo());
                    break;
            }
        }
    }
    public String getThisDateSimple() {
        return thisDateSimple;
    }
    public String getThisHora() {
        return thisHora;
    }

    public String getThisDateSimpleFE() {
        return thisDateSimpleFE;
    }

    public void mostrarPantallaCargaCustom(FragmentManager fragmentManager, String mensaje,
                                           boolean cancelable, IConfigurableCarga IConfigurableCarga){
        pantallaCarga = new PantallaCargaDialogFragment(mensaje, cancelable, IConfigurableCarga);
        pantallaCarga.show(fragmentManager, "CustomCargaDialogFragment");
    }

    public boolean estadoProgressCustom(){
        return pantallaCarga == null;
    }

    public void ocultarPantallaCargaCustom(){
        if(pantallaCarga != null){
            pantallaCarga.dismiss();
            pantallaCarga = null;
        }
    }

    public static RequestCompraN6 peticionCompraDatafonoN6(PeticionN6 factura){
        boolean isRedeban = SPM.getBoolean(Constantes.IS_REDEBAN);

        String codigoDatafono = isRedeban ? SPM.getString(Constantes.CODIGO_DATAFONO_RB)
                : SPM.getString(Constantes.CODIGO_DATAFONO_CD);
        SeguridadDatafono seguridadDatafono = isRedeban ?
                new SeguridadDatafono(true,
                        SPM.getString(Constantes.CODIGO_UNICO_DATAFONO_RB),
                        SPM.getString(Constantes.USUARIO_DATAFONO_RB),
                        SPM.getString(Constantes.PASS_DATAFONO_RB)) :
                new SeguridadDatafono(false,
                        SPM.getString(Constantes.CODIGO_UNICO_DATAFONO_CD),
                        SPM.getString(Constantes.USUARIO_DATAFONO_CD),
                        SPM.getString(Constantes.PASS_DATAFONO_CD));

        return new RequestCompraN6(codigoDatafono, factura.getCajeroCode(), factura.getCaja(),
                factura.getNumeroFacturaN6(), 0,factura.getValorIvaN6(),
                factura.getValorVentaN6(), seguridadDatafono);
    }

    public static RequestCompraN6SO peticionSobreescribirCompraDatafonoN6(PeticionN6 factura){
        boolean isRedeban = SPM.getBoolean(Constantes.IS_REDEBAN);

        String idTransaccion = SPM.getString(Constantes.ID_TRANSACCION_DATAFONO);
        assert idTransaccion != null;
        String codigoDatafono = isRedeban ? SPM.getString(Constantes.CODIGO_DATAFONO_RB)
                : SPM.getString(Constantes.CODIGO_DATAFONO_CD);
        SeguridadDatafono seguridadDatafono = isRedeban ?
                new SeguridadDatafono(true,
                        SPM.getString(Constantes.CODIGO_UNICO_DATAFONO_RB),
                        SPM.getString(Constantes.USUARIO_DATAFONO_RB),
                        SPM.getString(Constantes.PASS_DATAFONO_RB)) :
                new SeguridadDatafono(false,
                        SPM.getString(Constantes.CODIGO_UNICO_DATAFONO_CD),
                        SPM.getString(Constantes.USUARIO_DATAFONO_CD),
                        SPM.getString(Constantes.PASS_DATAFONO_CD));

        return new RequestCompraN6SO(codigoDatafono, factura.getCajeroCode(), factura.getCaja(),
                factura.getNumeroFacturaN6(), 0,factura.getValorIvaN6(),
                factura.getValorVentaN6(), Integer.parseInt(idTransaccion), seguridadDatafono);
    }

    public static RequestAnularCompraN6 peticionAnularCompraDatafonoN6(int numeroRecibo){
        boolean isRedeban = SPM.getBoolean(Constantes.IS_REDEBAN);

        String codigoDatafono = isRedeban ? SPM.getString(Constantes.CODIGO_DATAFONO_RB)
                : SPM.getString(Constantes.CODIGO_DATAFONO_CD);

        SeguridadDatafono seguridadDatafono = isRedeban ?
                new SeguridadDatafono(true,
                        SPM.getString(Constantes.CODIGO_UNICO_DATAFONO_RB),
                        SPM.getString(Constantes.USUARIO_DATAFONO_RB),
                        SPM.getString(Constantes.PASS_DATAFONO_RB)) :
                new SeguridadDatafono(false,
                        SPM.getString(Constantes.CODIGO_UNICO_DATAFONO_CD),
                        SPM.getString(Constantes.USUARIO_DATAFONO_CD),
                        SPM.getString(Constantes.PASS_DATAFONO_CD));

        return new RequestAnularCompraN6(codigoDatafono, SPM.getString(Constantes.CAJERO_CODE),
                SPM.getString(Constantes.CAJA_CODE), numeroRecibo, seguridadDatafono);
    }

    public static RequestCompraN6 peticionCompraDatafonoN6(int referencia){
        boolean isRedeban = SPM.getBoolean(Constantes.IS_REDEBAN);

        String codigoDatafono = isRedeban ? SPM.getString(Constantes.CODIGO_DATAFONO_RB)
                : SPM.getString(Constantes.CODIGO_DATAFONO_CD);
        SeguridadDatafono seguridadDatafono = isRedeban ?
                new SeguridadDatafono(true,
                        SPM.getString(Constantes.CODIGO_UNICO_DATAFONO_RB),
                        SPM.getString(Constantes.USUARIO_DATAFONO_RB),
                        SPM.getString(Constantes.PASS_DATAFONO_RB)) :
                new SeguridadDatafono(false,
                        SPM.getString(Constantes.CODIGO_UNICO_DATAFONO_CD),
                        SPM.getString(Constantes.USUARIO_DATAFONO_CD),
                        SPM.getString(Constantes.PASS_DATAFONO_CD));

        return new RequestCompraN6(codigoDatafono, "9999999999",
                SPM.getString(Constantes.CAJA_CODE), referencia, 0,0,
                10, seguridadDatafono);
    }

    public static RequestCompraN6SO peticionSobreescribirCompraDatafonoN6(int referencia){
        boolean isRedeban = SPM.getBoolean(Constantes.IS_REDEBAN);

        String idTransaccion = SPM.getString(Constantes.ID_TRANSACCION_DATAFONO);
        assert idTransaccion != null;
        String codigoDatafono = isRedeban ? SPM.getString(Constantes.CODIGO_DATAFONO_RB)
                : SPM.getString(Constantes.CODIGO_DATAFONO_CD);
        SeguridadDatafono seguridadDatafono = isRedeban ?
                new SeguridadDatafono(true,
                        SPM.getString(Constantes.CODIGO_UNICO_DATAFONO_RB),
                        SPM.getString(Constantes.USUARIO_DATAFONO_RB),
                        SPM.getString(Constantes.PASS_DATAFONO_RB)) :
                new SeguridadDatafono(false,
                        SPM.getString(Constantes.CODIGO_UNICO_DATAFONO_CD),
                        SPM.getString(Constantes.USUARIO_DATAFONO_CD),
                        SPM.getString(Constantes.PASS_DATAFONO_CD));

        return new RequestCompraN6SO(codigoDatafono, "9999999999",
                SPM.getString(Constantes.CAJA_CODE), referencia, 0,0,
                10, Integer.parseInt(idTransaccion), seguridadDatafono);
    }

    public static RequestBaseN6 peticionBaseN6(){
        boolean isRedeban = SPM.getBoolean(Constantes.IS_REDEBAN);

        String idTransaccion = SPM.getString(Constantes.ID_TRANSACCION_DATAFONO);
        SeguridadDatafono seguridadDatafono = isRedeban ?
                new SeguridadDatafono(true,
                        SPM.getString(Constantes.CODIGO_UNICO_DATAFONO_RB),
                        SPM.getString(Constantes.USUARIO_DATAFONO_RB),
                        SPM.getString(Constantes.PASS_DATAFONO_RB)) :
                new SeguridadDatafono(false,
                        SPM.getString(Constantes.CODIGO_UNICO_DATAFONO_CD),
                        SPM.getString(Constantes.USUARIO_DATAFONO_CD),
                        SPM.getString(Constantes.PASS_DATAFONO_CD));

        return new RequestBaseN6(idTransaccion, seguridadDatafono);
    }

    public static int tiempoDatafono(){
        boolean isRedeban = SPM.getBoolean(Constantes.IS_REDEBAN);
        return isRedeban ?  transformarTiempoSegundos(SPM.getString(Constantes.TIMEOUT_DATAFONO_RB)):
                transformarTiempoSegundos(SPM.getString(Constantes.TIMEOUT_DATAFONO_CD));
    }

    public static long tiempoEsperaDatafono(){
        boolean isRedeban = SPM.getBoolean(Constantes.IS_REDEBAN);
        return isRedeban ?  transformarTiempoMinutos(SPM.getString(Constantes.TIMEOUT_ESPERA_DATAFONO_RB)):
                transformarTiempoMinutos(SPM.getString(Constantes.TIMEOUT_ESPERA_DATAFONO_CD)) ;
    }
    private static int transformarTiempoSegundos(String tiempo){
        return Integer.parseInt(tiempo)*1000;
    }

    private static int transformarTiempoMinutos(String tiempo){
        return Integer.parseInt(tiempo)*60*1000;
    }

    public static String formatearPrecio(Double precio){
        DecimalFormat formatea = new DecimalFormat(Constantes.FORMATO_DECIMAL);

        return formatea.format(precio);
    }

    public String obtenerHoraConGMT(Date fecha) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ssXXX", Locale.getDefault());
        timeFormat.setTimeZone(TimeZone.getTimeZone("America/Bogota"));
        return timeFormat.format(fecha); // Esto generará algo como "09:40:57-05:00"
    }
}