package com.crystal.bazarposmobile.retrofit.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestExtraerInfoDocumento {
    @SerializedName("numeroTransaccion")
    private int numeroTransaccion;

    @SerializedName("tienda")
    private String tienda;

    @SerializedName("tipoDocumento")
    private String tipoDocumento;

    public RequestExtraerInfoDocumento(int numeroTransaccion, String tienda, String tipoDocumento) {
        this.numeroTransaccion = numeroTransaccion;
        this.tienda = tienda;
        this.tipoDocumento = tipoDocumento;
    }

    public int getNumeroTransaccion() {
        return numeroTransaccion;
    }

    public void setNumeroTransaccion(int numeroTransaccion) {
        this.numeroTransaccion = numeroTransaccion;
    }

    public String getTienda() {
        return tienda;
    }

    public void setTienda(String tienda) {
        this.tienda = tienda;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
}