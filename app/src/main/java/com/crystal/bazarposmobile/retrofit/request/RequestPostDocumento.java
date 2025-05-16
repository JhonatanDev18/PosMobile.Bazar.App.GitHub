package com.crystal.bazarposmobile.retrofit.request;

import com.google.gson.annotations.SerializedName;

public class RequestPostDocumento {

    @SerializedName("numeroTransaccion")
    private Integer numeroTransaccion;

    @SerializedName("codigoCaja")
    private String codigoCaja;

    public RequestPostDocumento(int numeroTransaccion, String codigoCaja) {
        this.numeroTransaccion = numeroTransaccion;
        this.codigoCaja = codigoCaja;
    }

    public Integer getNumeroTransaccion(){
        return numeroTransaccion;
    }

    public String getCodigoCaja(){
        return codigoCaja;
    }
}