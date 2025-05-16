package com.crystal.bazarposmobile.retrofit.response.perifericos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseConsultarPerifericos {
    @SerializedName("esValida")
    private boolean esValida;

    @SerializedName("mensaje")
    private String mensaje;

    @SerializedName("dato")
    private Object dato;

    @SerializedName("listado")
    private List<RespuestaConsultarPerifericos> listPerifericos;

    public ResponseConsultarPerifericos(boolean esValida, String mensaje, Object dato, List<RespuestaConsultarPerifericos> listPerifericos) {
        this.esValida = esValida;
        this.mensaje = mensaje;
        this.dato = dato;
        this.listPerifericos = listPerifericos;
    }

    public boolean isEsValida() {
        return esValida;
    }

    public void setEsValida(boolean esValida) {
        this.esValida = esValida;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Object getDato() {
        return dato;
    }

    public void setDato(Object dato) {
        this.dato = dato;
    }

    public List<RespuestaConsultarPerifericos> getListPerifericos() {
        return listPerifericos;
    }

    public void setListPerifericos(List<RespuestaConsultarPerifericos> listPerifericos) {
        this.listPerifericos = listPerifericos;
    }
}
