package com.crystal.bazarposmobile.retrofit.request.datafonon6;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestActualizarTransaccion {
    @SerializedName("numeroTransaccion")
    @Expose
    private String numeroTransaccion;
    @SerializedName("tienda")
    @Expose
    private String tienda;
    @SerializedName("caja")
    @Expose
    private String caja;
    @SerializedName("referenciaInterna")
    @Expose
    private String referenciaInterna;

    public RequestActualizarTransaccion(String numeroTransaccion, String tienda, String caja, String referenciaInterna) {
        this.numeroTransaccion = numeroTransaccion;
        this.tienda = tienda;
        this.caja = caja;
        this.referenciaInterna = referenciaInterna;
    }

    public String getNumeroTransaccion() {
        return numeroTransaccion;
    }

    public void setNumeroTransaccion(String numeroTransaccion) {
        this.numeroTransaccion = numeroTransaccion;
    }

    public String getTienda() {
        return tienda;
    }

    public void setTienda(String tienda) {
        this.tienda = tienda;
    }

    public String getCaja() {
        return caja;
    }

    public void setCaja(String caja) {
        this.caja = caja;
    }

    public String getReferenciaInterna() {
        return referenciaInterna;
    }

    public void setReferenciaInterna(String referenciaInterna) {
        this.referenciaInterna = referenciaInterna;
    }

    @Override
    public String toString() {
        return "RequestActualizarTransaccion{" +
                "numeroTransaccion='" + numeroTransaccion + '\'' +
                ", tienda='" + tienda + '\'' +
                ", caja='" + caja + '\'' +
                ", referenciaInterna='" + referenciaInterna + '\'' +
                '}';
    }
}
