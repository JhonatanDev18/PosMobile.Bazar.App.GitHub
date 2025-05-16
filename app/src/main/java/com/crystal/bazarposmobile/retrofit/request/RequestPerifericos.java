package com.crystal.bazarposmobile.retrofit.request;

import com.google.gson.annotations.SerializedName;

public class RequestPerifericos {
    @SerializedName("tienda")
    private String tienda;

    @SerializedName("caja")
    private String caja;

    public RequestPerifericos(String tienda, String caja) {
        this.tienda = tienda;
        this.caja = caja;
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
}
