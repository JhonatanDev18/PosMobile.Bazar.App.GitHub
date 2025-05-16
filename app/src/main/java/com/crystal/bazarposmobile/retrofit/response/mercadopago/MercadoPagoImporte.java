
package com.crystal.bazarposmobile.retrofit.response.mercadopago;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class MercadoPagoImporte implements Serializable {

    @Expose
    private String er;

    @Expose
    private String id;

    @Expose
    private String respuestaJSON;

    @Expose
    private Double monto;

    public MercadoPagoImporte(String er, String id, Double monto, String respuestaJSON) {
        this.er = er;
        this.id = id;
        this.respuestaJSON = respuestaJSON;
        this.monto = monto;
    }

    public String getEr() {
        return er;
    }

    public void setEr(String er) {
        this.er = er;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRespuestaJSON() {
        return respuestaJSON;
    }

    public void setRespuestaJSON(String respuestaJSON) {
        this.respuestaJSON = respuestaJSON;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }
}
