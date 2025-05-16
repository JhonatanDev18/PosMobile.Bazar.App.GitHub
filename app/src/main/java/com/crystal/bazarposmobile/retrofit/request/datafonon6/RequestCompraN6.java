package com.crystal.bazarposmobile.retrofit.request.datafonon6;

import com.crystal.bazarposmobile.common.SeguridadDatafono;
import com.google.gson.annotations.Expose;

public class RequestCompraN6 {
    @Expose
    private String codigoTerminal;
    @Expose
    private String idCajero;
    @Expose
    private String caja;
    @Expose
    private int numeroFactura;
    @Expose
    private int valorSinIva;
    @Expose
    private int valorIva;
    @Expose
    private int valorVenta;
    @Expose
    private SeguridadDatafono seguridad;

    public RequestCompraN6(String codigoTerminal, String idCajero, String caja, int numeroFactura,
                           int valorSinIva, int valorIva, int valorVenta, SeguridadDatafono seguridad) {
        this.codigoTerminal = codigoTerminal;
        this.idCajero = idCajero;
        this.caja = caja;
        this.numeroFactura = numeroFactura;
        this.valorSinIva = valorSinIva;
        this.valorIva = valorIva;
        this.valorVenta = valorVenta;
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

    public int getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(int numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public int getValorSinIva() {
        return valorSinIva;
    }

    public void setValorSinIva(int valorSinIva) {
        this.valorSinIva = valorSinIva;
    }

    public int getValorIva() {
        return valorIva;
    }

    public void setValorIva(int valorIva) {
        this.valorIva = valorIva;
    }

    public int getValorVenta() {
        return valorVenta;
    }

    public void setValorVenta(int valorVenta) {
        this.valorVenta = valorVenta;
    }

    public SeguridadDatafono getSeguridad() {
        return seguridad;
    }

    public void setSeguridad(SeguridadDatafono seguridad) {
        this.seguridad = seguridad;
    }

    @Override
    public String toString() {
        return "RequestCompraN6{" +
                "codigoTerminal='" + codigoTerminal + '\'' +
                ", idCajero='" + idCajero + '\'' +
                ", caja='" + caja + '\'' +
                ", numeroFactura=" + numeroFactura +
                ", valorSinIva=" + valorSinIva +
                ", valorIva=" + valorIva +
                ", valorVenta=" + valorVenta +
                ", seguridad=" + seguridad +
                '}';
    }
}