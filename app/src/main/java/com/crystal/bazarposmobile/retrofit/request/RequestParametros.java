package com.crystal.bazarposmobile.retrofit.request;

import com.google.gson.annotations.Expose;

public class RequestParametros {

    @Expose
    String tag;

    @Expose
    String caja;

    @Expose
    String tienda;

    public RequestParametros(String tag, String caja, String tienda) {
        this.tag = tag;
        this.caja = caja;
        this.tienda = tienda;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCaja() {
        return caja;
    }

    public void setCaja(String caja) {
        this.caja = caja;
    }

    public String getTienda() {
        return tienda;
    }

    public void setTienda(String tienda) {
        this.tienda = tienda;
    }
}
