package com.crystal.bazarposmobile.retrofit.response.datafonon6;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseConsultarCompraN6 {
    @SerializedName("esValida")
    private boolean esValida;

    @SerializedName("mensaje")
    private String mensaje;

    @SerializedName("dato")
    private RespuestaConsultarCompraN6 respuestaCompra;

    @SerializedName("listado")
    private List<Object> listado;

    public ResponseConsultarCompraN6(boolean esValida, String mensaje, RespuestaConsultarCompraN6 respuestaCompra, List<Object> listado) {
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

    public RespuestaConsultarCompraN6 getRespuestaCompra() {
        return respuestaCompra;
    }

    public void setRespuestaCompra(RespuestaConsultarCompraN6 respuestaCompra) {
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
        return "ResponseConsultarCompraN6{" +
                "esValida=" + esValida +
                ", mensaje='" + mensaje + '\'' +
                ", respuestaCompra=" + respuestaCompra +
                ", listado=" + listado +
                '}';
    }
}
