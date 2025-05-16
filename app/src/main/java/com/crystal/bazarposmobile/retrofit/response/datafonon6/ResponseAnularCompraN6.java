package com.crystal.bazarposmobile.retrofit.response.datafonon6;

import com.crystal.bazarposmobile.retrofit.request.datafonon6.RespuestaBaseN6;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseAnularCompraN6 {
    @SerializedName("esValida")
    private boolean esValida;

    @SerializedName("mensaje")
    private String mensaje;

    @SerializedName("dato")
    private RespuestaBaseN6 respuesta;

    @SerializedName("listado")
    private List<Object> listado;

    public ResponseAnularCompraN6(boolean esValida, String mensaje, RespuestaBaseN6 respuesta, List<Object> listado) {
        this.esValida = esValida;
        this.mensaje = mensaje;
        this.respuesta = respuesta;
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

    public RespuestaBaseN6 getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(RespuestaBaseN6 respuesta) {
        this.respuesta = respuesta;
    }

    public List<Object> getListado() {
        return listado;
    }

    public void setListado(List<Object> listado) {
        this.listado = listado;
    }

    @Override
    public String toString() {
        return "ResponseAnularCompraN6{" +
                "esValida=" + esValida +
                ", mensaje='" + mensaje + '\'' +
                ", respuesta=" + respuesta +
                ", listado=" + listado +
                '}';
    }
}
