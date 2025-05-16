package com.crystal.bazarposmobile.retrofit.request.detallesdescuento;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestDetallesDescuento {

    @SerializedName("codigoTienda")
    @Expose
    private String codigoTienda;
    @SerializedName("numeroTransaccion")
    @Expose
    private Integer numeroTransaccion;
    @SerializedName("detalle")
    @Expose
    private List<Detalle> detalle;

    public RequestDetallesDescuento(String codigoTienda, Integer numeroTransaccion, List<Detalle> detalle) {
        this.codigoTienda = codigoTienda;
        this.numeroTransaccion = numeroTransaccion;
        this.detalle = detalle;
    }

    public String getCodigoTienda() {
        return codigoTienda;
    }

    public void setCodigoTienda(String codigoTienda) {
        this.codigoTienda = codigoTienda;
    }

    public Integer getNumeroTransaccion() {
        return numeroTransaccion;
    }

    public void setNumeroTransaccion(Integer numeroTransaccion) {
        this.numeroTransaccion = numeroTransaccion;
    }

    public List<Detalle> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<Detalle> detalle) {
        this.detalle = detalle;
    }

}
