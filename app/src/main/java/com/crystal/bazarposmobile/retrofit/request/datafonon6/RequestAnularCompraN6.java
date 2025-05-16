package com.crystal.bazarposmobile.retrofit.request.datafonon6;

import com.crystal.bazarposmobile.common.SeguridadDatafono;
import com.google.gson.annotations.Expose;

public class RequestAnularCompraN6 {
    @Expose
    private String codigoDeTerminal;
    @Expose
    private String idCajero;
    @Expose
    private String caja;
    @Expose
    private int numeroDeRecibo;
    @Expose
    private SeguridadDatafono seguridad;

    public RequestAnularCompraN6(String codigoDeTerminal, String idCajero, String caja, int numeroDeRecibo, SeguridadDatafono seguridad) {
        this.codigoDeTerminal = codigoDeTerminal;
        this.idCajero = idCajero;
        this.caja = caja;
        this.numeroDeRecibo = numeroDeRecibo;
        this.seguridad = seguridad;
    }

    public String getCodigoDeTerminal() {
        return codigoDeTerminal;
    }

    public void setCodigoDeTerminal(String codigoDeTerminal) {
        this.codigoDeTerminal = codigoDeTerminal;
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

    public int getNumeroDeRecibo() {
        return numeroDeRecibo;
    }

    public void setNumeroDeRecibo(int numeroDeRecibo) {
        this.numeroDeRecibo = numeroDeRecibo;
    }

    public SeguridadDatafono getSeguridad() {
        return seguridad;
    }

    public void setSeguridad(SeguridadDatafono seguridad) {
        this.seguridad = seguridad;
    }

    @Override
    public String toString() {
        return "RequestAnularCompraN6{" +
                "codigoDeTerminal='" + codigoDeTerminal + '\'' +
                ", idCajero='" + idCajero + '\'' +
                ", caja='" + caja + '\'' +
                ", numeroDeRecibo=" + numeroDeRecibo +
                ", seguridad=" + seguridad +
                '}';
    }
}
