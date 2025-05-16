package com.crystal.bazarposmobile.retrofit.response.datafonon6;

import android.annotation.SuppressLint;

import com.crystal.bazarposmobile.common.ClaveValorTef;
import com.crystal.bazarposmobile.common.Utilidades;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CuerpoRespuestaDatafonoN6 {
    @SerializedName("aprobacion")
    @Expose
    private String aprobacion;
    @SerializedName("valorTotal")
    @Expose
    private String valorTotal;
    @SerializedName("valorIva")
    @Expose
    private String valorIva;
    @SerializedName("recibo")
    @Expose
    private String recibo;
    @SerializedName("consecutivoInterno")
    @Expose
    private String consecutivoInterno;
    @SerializedName("terminal")
    @Expose
    private String terminal;
    @SerializedName("fechaTransaccion")
    @Expose
    private String fechaTransaccion;
    @SerializedName("horaTransaccion")
    @Expose
    private String horaTransaccion;
    @SerializedName("franquicia")
    @Expose
    private String franquicia;
    @SerializedName("tipoCuentaMedioPago")
    @Expose
    private String tipoCuentaMedioPago;
    @SerializedName("numeroCuotas")
    @Expose
    private String numeroCuotas;
    @SerializedName("ultimosDigitosTarjeta")
    @Expose
    private String ultimosDigitosTarjeta;
    @SerializedName("binTarjeta")
    @Expose
    private String binTarjeta;
    @SerializedName("fechaVencimiento")
    @Expose
    private String fechaVencimiento;
    @SerializedName("codigoComercio")
    @Expose
    private String codigoComercio;
    @SerializedName("direccionComercio")
    @Expose
    private String direccionComercio;
    @SerializedName("caja")
    @Expose
    private String caja;

    public CuerpoRespuestaDatafonoN6(String aprobacion, String valorTotal, String valorIva, String recibo,
                                     String consecutivoInterno, String terminal, String fechaTransaccion,
                                     String horaTransaccion, String franquicia, String tipoCuentaMedioPago,
                                     String numeroCuotas, String ultimosDigitosTarjeta, String binTarjeta,
                                     String fechaVencimiento, String codigoComercio, String direccionComercio,
                                     String caja) {
        this.aprobacion = aprobacion;
        this.valorTotal = valorTotal;
        this.valorIva = valorIva;
        this.recibo = recibo;
        this.consecutivoInterno = consecutivoInterno;
        this.terminal = terminal;
        this.fechaTransaccion = fechaTransaccion;
        this.horaTransaccion = horaTransaccion;
        this.franquicia = franquicia;
        this.tipoCuentaMedioPago = tipoCuentaMedioPago;
        this.numeroCuotas = numeroCuotas;
        this.ultimosDigitosTarjeta = ultimosDigitosTarjeta;
        this.binTarjeta = binTarjeta;
        this.fechaVencimiento = fechaVencimiento;
        this.codigoComercio = codigoComercio;
        this.direccionComercio = direccionComercio;
        this.caja = caja;
    }

    public String getAprobacion() {
        return aprobacion;
    }

    public void setAprobacion(String aprobacion) {
        this.aprobacion = aprobacion;
    }

    public String getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(String valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getValorIva() {
        return valorIva;
    }

    public void setValorIva(String valorIva) {
        this.valorIva = valorIva;
    }

    public String getRecibo() {
        return recibo;
    }

    public void setRecibo(String recibo) {
        this.recibo = recibo;
    }

    public String getConsecutivoInterno() {
        return consecutivoInterno;
    }

    public void setConsecutivoInterno(String consecutivoInterno) {
        this.consecutivoInterno = consecutivoInterno;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getFechaTransaccion() {
        return fechaTransaccion;
    }

    public void setFechaTransaccion(String fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }

    public String getHoraTransaccion() {
        return horaTransaccion;
    }

    public void setHoraTransaccion(String horaTransaccion) {
        this.horaTransaccion = horaTransaccion;
    }

    public String getFranquicia() {
        return franquicia;
    }

    public void setFranquicia(String franquicia) {
        this.franquicia = franquicia;
    }

    public String getTipoCuentaMedioPago() {
        return tipoCuentaMedioPago;
    }

    public void setTipoCuentaMedioPago(String tipoCuentaMedioPago) {
        this.tipoCuentaMedioPago = tipoCuentaMedioPago;
    }

    public String getNumeroCuotas() {
        return numeroCuotas;
    }

    public void setNumeroCuotas(String numeroCuotas) {
        this.numeroCuotas = numeroCuotas;
    }

    public String getUltimosDigitosTarjeta() {
        return ultimosDigitosTarjeta;
    }

    public void setUltimosDigitosTarjeta(String ultimosDigitosTarjeta) {
        this.ultimosDigitosTarjeta = ultimosDigitosTarjeta;
    }

    public String getBinTarjeta() {
        return binTarjeta;
    }

    public void setBinTarjeta(String binTarjeta) {
        this.binTarjeta = binTarjeta;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getCodigoComercio() {
        return codigoComercio;
    }

    public void setCodigoComercio(String codigoComercio) {
        this.codigoComercio = codigoComercio;
    }

    public String getDireccionComercio() {
        return direccionComercio;
    }

    public void setDireccionComercio(String direccionComercio) {
        this.direccionComercio = direccionComercio;
    }

    public String getCaja() {
        return caja;
    }

    public void setCaja(String caja) {
        this.caja = caja;
    }

    public List<ClaveValorTef> toStringClaveValor(){
        try{
            List<ClaveValorTef> listaClaveValor = new ArrayList<>();

            // Extraer el mes y el día del string
            String monthStr = fechaTransaccion.substring(0, 2);
            String dayStr = fechaTransaccion.substring(2, 4);

            int month = Integer.parseInt(monthStr);
            int day = Integer.parseInt(dayStr);

            String monthName = new DateFormatSymbols(new Locale("es")).getMonths()[month - 1];
            String fechaTransaccionFormat = day + " de " + monthName;

            listaClaveValor.add(new ClaveValorTef("Aprobación:", aprobacion));
            listaClaveValor.add(new ClaveValorTef("Valor total:", Utilidades.formatearPrecio(Double.parseDouble(valorTotal))));
            listaClaveValor.add(new ClaveValorTef("Valor iva:", Utilidades.formatearPrecio(Double.parseDouble(valorIva))));
            listaClaveValor.add(new ClaveValorTef("Recibo:", recibo));
            listaClaveValor.add(new ClaveValorTef("Terminal:", terminal));
            listaClaveValor.add(new ClaveValorTef("Codigo Comercio:", codigoComercio));
            listaClaveValor.add(new ClaveValorTef("Fecha transaccion:", fechaTransaccionFormat));
            listaClaveValor.add(new ClaveValorTef("Hora transaccion:", convertTimeFormat(horaTransaccion)));
            listaClaveValor.add(new ClaveValorTef("Franquicia:", franquicia));
            listaClaveValor.add(new ClaveValorTef("Cantidad cuotas:", numeroCuotas));
            listaClaveValor.add(new ClaveValorTef("Dirección comercio:", direccionComercio));

            return listaClaveValor;
        }catch (Exception ex){
            return null;
        }
    }

    public static String convertTimeFormat(String timeStr) {
        // Formato de entrada en 24 horas
        @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormat = new SimpleDateFormat("HHmm");

        // Formato de salida en 12 horas
        @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormat = new SimpleDateFormat("h:mm a");

        try {
            // Parsear la hora en formato de 24 horas
            Date date = inputFormat.parse(timeStr);

            // Formatear la hora en formato de 12 horas
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
