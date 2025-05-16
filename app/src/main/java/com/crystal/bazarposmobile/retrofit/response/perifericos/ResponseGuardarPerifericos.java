package com.crystal.bazarposmobile.retrofit.response.perifericos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseGuardarPerifericos {
    @SerializedName("esValida")
    private boolean esValida;

    @SerializedName("mensaje")
    private String mensaje;

    @SerializedName("dato")
    private boolean dato;

    @SerializedName("listado")
    private List<Object> listado;

    public ResponseGuardarPerifericos(boolean esValida, String mensaje, boolean dato, List<Object> listado) {
        this.esValida = esValida;
        this.mensaje = mensaje;
        this.dato = dato;
        this.listado = listado;
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

    public boolean isDato() {
        return dato;
    }

    public void setDato(boolean dato) {
        this.dato = dato;
    }

    public List<Object> getListado() {
        return listado;
    }

    public void setListado(List<Object> listado) {
        this.listado = listado;
    }
}
