package com.crystal.bazarposmobile.retrofit.response.datafonon6;

import com.crystal.bazarposmobile.common.SeguridadDatafono;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RespuestaCompraN6 {
    @SerializedName("codigoTerminal")
    @Expose
    private String codigoTerminal;
    @SerializedName("idCajero")
    @Expose
    private String idCajero;
    @SerializedName("caja")
    @Expose
    private String caja;
    @SerializedName("numeroFactura")
    @Expose
    private Integer numeroFactura;
    @SerializedName("valorSinIva")
    @Expose
    private Double valorSinIva;
    @SerializedName("valorIva")
    @Expose
    private Double valorIva;
    @SerializedName("valorVenta")
    @Expose
    private Double valorVenta;
    @SerializedName("idTransaccion")
    @Expose
    private Integer idTransaccion;
    @SerializedName("seguridad")
    @Expose
    private SeguridadDatafono seguridad;

    public RespuestaCompraN6(String codigoTerminal, String idCajero, String caja, Integer numeroFactura,
                             Double valorSinIva, Double valorIva, Double valorVenta, Integer idTransaccion,
                             SeguridadDatafono seguridad) {
        this.codigoTerminal = codigoTerminal;
        this.idCajero = idCajero;
        this.caja = caja;
        this.numeroFactura = numeroFactura;
        this.valorSinIva = valorSinIva;
        this.valorIva = valorIva;
        this.valorVenta = valorVenta;
        this.idTransaccion = idTransaccion;
        this.seguridad = seguridad;
    }

    public String getCodigoTerminal() {
        return codigoTerminal;
    }

    public void setCodigoTerminal(String codigoTerminal) {
        this.codigoTerminal = codigoTerminal;
    }

    public String getIdCajero() {
        return idCajero;
    }

    public void setIdCajero(String idCajero) {
        this.idCajero = idCajero;
    }

    public String getCaja() {
        return caja;
    }

    public void setCaja(String caja) {
        this.caja = caja;
    }

    public Integer getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(Integer numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public Double getValorSinIva() {
        return valorSinIva;
    }

    public void setValorSinIva(Double valorSinIva) {
        this.valorSinIva = valorSinIva;
    }

    public Double getValorIva() {
        return valorIva;
    }

    public void setValorIva(Double valorIva) {
        this.valorIva = valorIva;
    }

    public Double getValorVenta() {
        return valorVenta;
    }

    public void setValorVenta(Double valorVenta) {
        this.valorVenta = valorVenta;
    }

    public Integer getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(Integer idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public SeguridadDatafono getSeguridad() {
        return seguridad;
    }

    public void setSeguridad(SeguridadDatafono seguridad) {
        this.seguridad = seguridad;
    }

    @Override
    public String toString() {
        return "RespuestaCompraN6{" +
                "codigoTerminal='" + codigoTerminal + '\'' +
                ", idCajero='" + idCajero + '\'' +
                ", caja='" + caja + '\'' +
                ", numeroFactura=" + numeroFactura +
                ", valorSinIva=" + valorSinIva +
                ", valorIva=" + valorIva +
                ", valorVenta=" + valorVenta +
                ", idTransaccion=" + idTransaccion +
                ", seguridad=" + seguridad +
                '}';
    }
}
