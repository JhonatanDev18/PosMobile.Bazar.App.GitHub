package com.crystal.bazarposmobile.retrofit.request.datafonon6;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RespuestaBaseN6 {
    @SerializedName("mensajeDeRespuesta")
    @Expose
    private String mensajeDeRespuesta;
    @SerializedName("codigoDeTerminal")
    @Expose
    private String codigoDeTerminal;
    @SerializedName("codigoDeComercio")
    @Expose
    private String codigoDeComercio;
    @SerializedName("idDeTransaccion")
    @Expose
    private Integer idDeTransaccion;

    public RespuestaBaseN6(String mensajeDeRespuesta, String codigoDeTerminal, String codigoDeComercio, Integer idDeTransaccion) {
        this.mensajeDeRespuesta = mensajeDeRespuesta;
        this.codigoDeTerminal = codigoDeTerminal;
        this.codigoDeComercio = codigoDeComercio;
        this.idDeTransaccion = idDeTransaccion;
    }

    public String getMensajeDeRespuesta() {
        return mensajeDeRespuesta;
    }

    public void setMensajeDeRespuesta(String mensajeDeRespuesta) {
        this.mensajeDeRespuesta = mensajeDeRespuesta;
    }

    public String getCodigoDeTerminal() {
        return codigoDeTerminal;
    }

    public void setCodigoDeTerminal(String codigoDeTerminal) {
        this.codigoDeTerminal = codigoDeTerminal;
    }

    public String getCodigoDeComercio() {
        return codigoDeComercio;
    }

    public void setCodigoDeComercio(String codigoDeComercio) {
        this.codigoDeComercio = codigoDeComercio;
    }

    public Integer getIdDeTransaccion() {
        return idDeTransaccion;
    }

    public void setIdDeTransaccion(Integer idDeTransaccion) {
        this.idDeTransaccion = idDeTransaccion;
    }

    @Override
    public String toString() {
        return "RespuestaBaseN6{" +
                "mensajeDeRespuesta='" + mensajeDeRespuesta + '\'' +
                ", codigoDeTerminal='" + codigoDeTerminal + '\'' +
                ", codigoDeComercio='" + codigoDeComercio + '\'' +
                ", idDeTransaccion=" + idDeTransaccion +
                '}';
    }
}
