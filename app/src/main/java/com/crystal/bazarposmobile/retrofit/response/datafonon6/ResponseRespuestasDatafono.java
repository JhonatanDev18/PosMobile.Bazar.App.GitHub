package com.crystal.bazarposmobile.retrofit.response.datafonon6;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseRespuestasDatafono {
    @SerializedName("esValida")
    private boolean esValida;

    @SerializedName("mensaje")
    private String mensaje;

    @SerializedName("dato")
    private Object dato;

    @SerializedName("listado")
    @Expose
    private List<RespuestaCompletaTef> listado;

    public ResponseRespuestasDatafono(boolean esValida, String mensaje, Object dato, List<RespuestaCompletaTef> listado) {
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

    public Object getDato() {
        return dato;
    }

    public void setDato(Object dato) {
        this.dato = dato;
    }

    public List<RespuestaCompletaTef> getListado() {
        return listado;
    }

    public void setListado(List<RespuestaCompletaTef> listado) {
        this.listado = listado;
    }

    @Override
    public String toString() {
        return "ResponseRespuestasDatafono{" +
                "esValida=" + esValida +
                ", mensaje='" + mensaje + '\'' +
                ", dato=" + dato +
                ", listado=" + listado +
                '}';
    }
}
