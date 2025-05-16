package com.crystal.bazarposmobile.retrofit.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseExtraerInfoDocumento {
    @SerializedName("esValida")
    private boolean esValida;

    @SerializedName("mensaje")
    private String mensaje;

    @SerializedName("dato")
    private RespuestaExtraerInfoDocumento respuestaInfo;

    @SerializedName("listado")
    private List<Object> listado;

    public ResponseExtraerInfoDocumento(boolean esValida, String mensaje, RespuestaExtraerInfoDocumento respuestaInfo, List<Object> listado) {
        this.esValida = esValida;
        this.mensaje = mensaje;
        this.respuestaInfo = respuestaInfo;
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

    public RespuestaExtraerInfoDocumento getRespuestaInfo() {
        return respuestaInfo;
    }

    public void setRespuestaInfo(RespuestaExtraerInfoDocumento respuestaInfo) {
        this.respuestaInfo = respuestaInfo;
    }

    public List<Object> getListado() {
        return listado;
    }

    public void setListado(List<Object> listado) {
        this.listado = listado;
    }
}