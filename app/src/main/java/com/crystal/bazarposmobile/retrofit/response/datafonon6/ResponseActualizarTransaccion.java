package com.crystal.bazarposmobile.retrofit.response.datafonon6;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseActualizarTransaccion {
    @SerializedName("esValida")
    private boolean esValida;

    @SerializedName("mensaje")
    private String mensaje;

    @SerializedName("dato")
    private Object dato;

    @SerializedName("listado")
    private List<Object> listado;

    public boolean isEsValida() {
        return esValida;
    }

    public ResponseActualizarTransaccion(boolean esValida, String mensaje, Object dato, List<Object> listado) {
        this.esValida = esValida;
        this.mensaje = mensaje;
        this.dato = dato;
        this.listado = listado;
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

    public List<Object> getListado() {
        return listado;
    }

    public void setListado(List<Object> listado) {
        this.listado = listado;
    }

    @Override
    public String toString() {
        return "ResponseActualizarTransaccion{" +
                "esValida=" + esValida +
                ", mensaje='" + mensaje + '\'' +
                ", dato=" + dato +
                ", listado=" + listado +
                '}';
    }
}
