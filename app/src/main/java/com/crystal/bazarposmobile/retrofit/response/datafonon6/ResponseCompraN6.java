package com.crystal.bazarposmobile.retrofit.response.datafonon6;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseCompraN6 {
    @SerializedName("esValida")
    private boolean esValida;

    @SerializedName("mensaje")
    private String mensaje;

    @SerializedName("dato")
    private RespuestaCompraN6 respuestaCompra;

    @SerializedName("listado")
    private List<Object> listado;

    public ResponseCompraN6(boolean esValida, String mensaje, RespuestaCompraN6 respuestaCompra, List<Object> listado) {
        this.esValida = esValida;
        this.mensaje = mensaje;
        this.respuestaCompra = respuestaCompra;
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

    public RespuestaCompraN6 getRespuestaCompra() {
        return respuestaCompra;
    }

    public void setRespuestaCompra(RespuestaCompraN6 respuestaCompra) {
        this.respuestaCompra = respuestaCompra;
    }

    public List<Object> getListado() {
        return listado;
    }

    public void setListado(List<Object> listado) {
        this.listado = listado;
    }

    @Override
    public String toString() {
        return "ResponseCompraN6{" +
                "esValida=" + esValida +
                ", mensaje='" + mensaje + '\'' +
                ", respuestaCompra=" + respuestaCompra +
                ", listado=" + listado +
                '}';
    }
}
