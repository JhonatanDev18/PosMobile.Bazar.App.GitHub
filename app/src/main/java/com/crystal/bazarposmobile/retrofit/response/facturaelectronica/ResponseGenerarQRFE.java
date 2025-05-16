package com.crystal.bazarposmobile.retrofit.response.facturaelectronica;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseGenerarQRFE {
    @SerializedName("esValida")
    private boolean esValida;

    @SerializedName("mensaje")
    private String mensaje;

    @SerializedName("dato")
    private RespuestaGenerarQRFE respuesta;

    @SerializedName("listado")
    private List<Object> listado;

    public ResponseGenerarQRFE(boolean esValida, String mensaje, RespuestaGenerarQRFE respuesta, List<Object> listado) {
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

    public RespuestaGenerarQRFE getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(RespuestaGenerarQRFE respuesta) {
        this.respuesta = respuesta;
    }

    public List<Object> getListado() {
        return listado;
    }

    public void setListado(List<Object> listado) {
        this.listado = listado;
    }
}
