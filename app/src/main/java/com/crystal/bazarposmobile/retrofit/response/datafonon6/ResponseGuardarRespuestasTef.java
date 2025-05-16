package com.crystal.bazarposmobile.retrofit.response.datafonon6;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseGuardarRespuestasTef {
    @SerializedName("esValida")
    private boolean esValida;

    @SerializedName("mensaje")
    private String mensaje;

    @SerializedName("dato")
    private boolean respuesta;

    @SerializedName("listado")
    private List<Object> listado;

    public ResponseGuardarRespuestasTef(boolean esValida, String mensaje, boolean respuesta, List<Object> listado) {
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

    public boolean isRespuesta() {
        return respuesta;
    }

    public void setRespuesta(boolean respuesta) {
        this.respuesta = respuesta;
    }

    public List<Object> getListado() {
        return listado;
    }

    public void setListado(List<Object> listado) {
        this.listado = listado;
    }
}
