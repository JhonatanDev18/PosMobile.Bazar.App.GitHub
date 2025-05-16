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

public class RespuestaTransaccion {
    @SerializedName("codigoDeRespuesta")
    @Expose
    private String codigoDeRespuesta;
    @SerializedName("mensajeDeRespuesta")
    @Expose
    private String mensajeDeRespuesta;
    @SerializedName("codigoDeAprobacion")
    @Expose
    private String codigoDeAprobacion;
    @SerializedName("valorTotal")
    @Expose
    private Double valorTotal;
    @SerializedName("valorIVA")
    @Expose
    private Double valorIVA;
    @SerializedName("numeroDeRecibo")
    @Expose
    private Integer numeroDeRecibo;
    @SerializedName("rrn")
    @Expose
    private String rrn;
    @SerializedName("fechaTransaccion")
    @Expose
    private String fechaTransaccion;
    @SerializedName("horaTransaccion")
    @Expose
    private String horaTransaccion;
    @SerializedName("franquicia")
    @Expose
    private String franquicia;
    @SerializedName("numeroDeCuotas")
    @Expose
    private Integer numeroDeCuotas;
    @SerializedName("ultimosDigitosTarjeta")
    @Expose
    private String ultimosDigitosTarjeta;
    @SerializedName("binDeTarjeta")
    @Expose
    private String binDeTarjeta;
    @SerializedName("direccionComercio")
    @Expose
    private String direccionComercio;
    @SerializedName("tipoCuenta")
    @Expose
    private String tipoCuenta;
    @SerializedName("codigoDeTerminal")
    @Expose
    private String codigoDeTerminal;

    public RespuestaTransaccion(String codigoDeRespuesta, String mensajeDeRespuesta,
                                String codigoDeAprobacion, Double valorTotal, Double valorIVA,
                                Integer numeroDeRecibo, String rrn, String fechaTransaccion,
                                String horaTransaccion, String franquicia, Integer numeroDeCuotas,
                                String ultimosDigitosTarjeta, String binDeTarjeta, String direccionComercio,
                                String tipoCuenta, String codigoDeTerminal) {
        this.codigoDeRespuesta = codigoDeRespuesta;
        this.mensajeDeRespuesta = mensajeDeRespuesta;
        this.codigoDeAprobacion = codigoDeAprobacion;
        this.valorTotal = valorTotal;
        this.valorIVA = valorIVA;
        this.numeroDeRecibo = numeroDeRecibo;
        this.rrn = rrn;
        this.fechaTransaccion = fechaTransaccion;
        this.horaTransaccion = horaTransaccion;
        this.franquicia = franquicia;
        this.numeroDeCuotas = numeroDeCuotas;
        this.ultimosDigitosTarjeta = ultimosDigitosTarjeta;
        this.binDeTarjeta = binDeTarjeta;
        this.direccionComercio = direccionComercio;
        this.tipoCuenta = tipoCuenta;
        this.codigoDeTerminal = codigoDeTerminal;
    }

    public String getCodigoDeRespuesta() {
        return codigoDeRespuesta;
    }

    public void setCodigoDeRespuesta(String codigoDeRespuesta) {
        this.codigoDeRespuesta = codigoDeRespuesta;
    }

    public String getMensajeDeRespuesta() {
        return mensajeDeRespuesta;
    }

    public void setMensajeDeRespuesta(String mensajeDeRespuesta) {
        this.mensajeDeRespuesta = mensajeDeRespuesta;
    }

    public String getCodigoDeAprobacion() {
        return codigoDeAprobacion;
    }

    public void setCodigoDeAprobacion(String codigoDeAprobacion) {
        this.codigoDeAprobacion = codigoDeAprobacion;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Double getValorIVA() {
        return valorIVA;
    }

    public void setValorIVA(Double valorIVA) {
        this.valorIVA = valorIVA;
    }

    public Integer getNumeroDeRecibo() {
        return numeroDeRecibo;
    }

    public void setNumeroDeRecibo(Integer numeroDeRecibo) {
        this.numeroDeRecibo = numeroDeRecibo;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
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

    public Integer getNumeroDeCuotas() {
        return numeroDeCuotas;
    }

    public void setNumeroDeCuotas(Integer numeroDeCuotas) {
        this.numeroDeCuotas = numeroDeCuotas;
    }

    public String getUltimosDigitosTarjeta() {
        return ultimosDigitosTarjeta;
    }

    public void setUltimosDigitosTarjeta(String ultimosDigitosTarjeta) {
        this.ultimosDigitosTarjeta = ultimosDigitosTarjeta;
    }

    public String getBinDeTarjeta() {
        return binDeTarjeta;
    }

    public void setBinDeTarjeta(String binDeTarjeta) {
        this.binDeTarjeta = binDeTarjeta;
    }

    public String getDireccionComercio() {
        return direccionComercio;
    }

    public void setDireccionComercio(String direccionComercio) {
        this.direccionComercio = direccionComercio;
    }

    public String getCodigoDeTerminal() {
        return codigoDeTerminal;
    }

    public void setCodigoDeTerminal(String codigoDeTerminal) {
        this.codigoDeTerminal = codigoDeTerminal;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public List<ClaveValorTef> toStringClaveValor(){
        List<ClaveValorTef> listaClaveValor = new ArrayList<>();

        // Extraer el mes y el día del string
        String monthStr = fechaTransaccion.substring(0, 2);
        String dayStr = fechaTransaccion.substring(2, 4);

        int month = Integer.parseInt(monthStr);
        int day = Integer.parseInt(dayStr);

        String monthName = new DateFormatSymbols(new Locale("es")).getMonths()[month - 1];
        String fechaTransaccionFormat = day + " de " + monthName;

        listaClaveValor.add(new ClaveValorTef("Aprobación:", codigoDeAprobacion));
        listaClaveValor.add(new ClaveValorTef("Valor total:", Utilidades.formatearPrecio(valorTotal)));
        listaClaveValor.add(new ClaveValorTef("Valor iva:", Utilidades.formatearPrecio(valorIVA)));
        listaClaveValor.add(new ClaveValorTef("Recibo:", Integer.toString(numeroDeRecibo)));
        listaClaveValor.add(new ClaveValorTef("Terminal:", codigoDeTerminal));
        listaClaveValor.add(new ClaveValorTef("Fecha transaccion:", fechaTransaccionFormat));
        listaClaveValor.add(new ClaveValorTef("Hora transaccion:", convertTimeFormat(horaTransaccion)));
        listaClaveValor.add(new ClaveValorTef("Franquicia:", franquicia));
        listaClaveValor.add(new ClaveValorTef("Cantidad cuotas:", Integer.toString(numeroDeCuotas)));
        listaClaveValor.add(new ClaveValorTef("Dirección comercio:", direccionComercio));

        return listaClaveValor;
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

    @Override
    public String toString() {
        return "Mensaje Respuesta='" + mensajeDeRespuesta + "\n"+'\'' +
                "Codigo Aprobacion='" + codigoDeAprobacion + '\'' +
                ", valorTotal=" + valorTotal +
                ", valorIVA=" + valorIVA +
                ", numeroDeRecibo=" + numeroDeRecibo +
                ", rrn='" + rrn + '\'' +
                ", fechaTransaccion='" + fechaTransaccion + '\'' +
                ", horaTransaccion='" + horaTransaccion + '\'' +
                ", franquicia='" + franquicia + '\'' +
                ", numeroDeCuotas=" + numeroDeCuotas +
                ", ultimosDigitosTarjeta='" + ultimosDigitosTarjeta + '\'' +
                ", binDeTarjeta='" + binDeTarjeta + '\'' +
                ", direccionComercio='" + direccionComercio + '\'' +
                ", codigoDeTerminal='" + codigoDeTerminal + '\'' +
                '}';
    }
}
