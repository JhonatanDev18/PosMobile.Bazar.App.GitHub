
package com.crystal.bazarposmobile.retrofit.request.tef;

import com.google.gson.annotations.Expose;

public class PagosTEF {

    @Expose
    private String codigoCaja;
    @Expose
    private String codigoMedioPago;
    @Expose
    private String cuatroUltimosDigitosTarjeta;
    @Expose
    private String fechaVencimientoTarjeta;
    @Expose
    private String numeroAutorizacion;
    @Expose
    private Integer numeroCuotas;
    @Expose
    private Integer numeroPago;
    @Expose
    private Integer numeroTransaccion;
    @Expose
    private String numeroTransaccionDatafono;
    @Expose
    private String respuestaTEF;
    @Expose
    private String tipoTarjeta;

    public PagosTEF(String codigoCaja, String codigoMedioPago, String cuatroUltimosDigitosTarjeta, String fechaVencimientoTarjeta, String numeroAutorizacion, Integer numeroCuotas, Integer numeroPago, Integer numeroTransaccion, String numeroTransaccionDatafono, String respuestaTEF, String tipoTarjeta) {
        this.codigoCaja = codigoCaja;
        this.codigoMedioPago = codigoMedioPago;
        this.cuatroUltimosDigitosTarjeta = cuatroUltimosDigitosTarjeta;
        this.fechaVencimientoTarjeta = fechaVencimientoTarjeta;
        this.numeroAutorizacion = numeroAutorizacion;
        this.numeroCuotas = numeroCuotas;
        this.numeroPago = numeroPago;
        this.numeroTransaccion = numeroTransaccion;
        this.numeroTransaccionDatafono = numeroTransaccionDatafono;
        this.respuestaTEF = respuestaTEF;
        this.tipoTarjeta = tipoTarjeta;
    }

    public String getCodigoCaja() {
        return codigoCaja;
    }

    public void setCodigoCaja(String codigoCaja) {
        this.codigoCaja = codigoCaja;
    }

    public String getCodigoMedioPago() {
        return codigoMedioPago;
    }

    public void setCodigoMedioPago(String codigoMedioPago) {
        this.codigoMedioPago = codigoMedioPago;
    }

    public String getCuatroUltimosDigitosTarjeta() {
        return cuatroUltimosDigitosTarjeta;
    }

    public void setCuatroUltimosDigitosTarjeta(String cuatroUltimosDigitosTarjeta) {
        this.cuatroUltimosDigitosTarjeta = cuatroUltimosDigitosTarjeta;
    }

    public String getFechaVencimientoTarjeta() {
        return fechaVencimientoTarjeta;
    }

    public void setFechaVencimientoTarjeta(String fechaVencimientoTarjeta) {
        this.fechaVencimientoTarjeta = fechaVencimientoTarjeta;
    }

    public String getNumeroAutorizacion() {
        return numeroAutorizacion;
    }

    public void setNumeroAutorizacion(String numeroAutorizacion) {
        this.numeroAutorizacion = numeroAutorizacion;
    }

    public Integer getNumeroCuotas() {
        return numeroCuotas;
    }

    public void setNumeroCuotas(Integer numeroCuotas) {
        this.numeroCuotas = numeroCuotas;
    }

    public Integer getNumeroPago() {
        return numeroPago;
    }

    public void setNumeroPago(Integer numeroPago) {
        this.numeroPago = numeroPago;
    }

    public Integer getNumeroTransaccion() {
        return numeroTransaccion;
    }

    public void setNumeroTransaccion(Integer numeroTransaccion) {
        this.numeroTransaccion = numeroTransaccion;
    }

    public String getNumeroTransaccionDatafono() {
        return numeroTransaccionDatafono;
    }

    public void setNumeroTransaccionDatafono(String numeroTransaccionDatafono) {
        this.numeroTransaccionDatafono = numeroTransaccionDatafono;
    }

    public String getRespuestaTEF() {
        return respuestaTEF;
    }

    public void setRespuestaTEF(String respuestaTEF) {
        this.respuestaTEF = respuestaTEF;
    }

    public String getTipoTarjeta() {
        return tipoTarjeta;
    }

    public void setTipoTarjeta(String tipoTarjeta) {
        this.tipoTarjeta = tipoTarjeta;
    }

    @Override
    public String toString() {
        return "PagosTEF{" +
                "codigoCaja='" + codigoCaja + '\'' +
                ", codigoMedioPago='" + codigoMedioPago + '\'' +
                ", cuatroUltimosDigitosTarjeta='" + cuatroUltimosDigitosTarjeta + '\'' +
                ", fechaVencimientoTarjeta='" + fechaVencimientoTarjeta + '\'' +
                ", numeroAutorizacion='" + numeroAutorizacion + '\'' +
                ", numeroCuotas=" + numeroCuotas +
                ", numeroPago=" + numeroPago +
                ", numeroTransaccion=" + numeroTransaccion +
                ", numeroTransaccionDatafono='" + numeroTransaccionDatafono + '\'' +
                ", respuestaTEF='" + respuestaTEF + '\'' +
                ", tipoTarjeta='" + tipoTarjeta + '\'' +
                '}';
    }
}
