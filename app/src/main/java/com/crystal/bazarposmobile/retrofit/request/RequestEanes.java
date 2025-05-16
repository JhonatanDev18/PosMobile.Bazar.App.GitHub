package com.crystal.bazarposmobile.retrofit.request;

import com.google.gson.annotations.Expose;

import java.util.List;

public class RequestEanes {

    @Expose
    private String pais;
    @Expose
    private List<String> eanes;
    @Expose
    private String tienda;

    public RequestEanes(List<String> eanes, String pais, String tienda) {
        this.pais = pais;
        this.eanes = eanes;
        this.tienda = tienda;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public List<String> getEanes() {
        return eanes;
    }

    public void setEanes(List<String> eanes) {
        this.eanes = eanes;
    }

    public String getTienda() {
        return tienda;
    }

    public void setTienda(String tienda) {
        this.tienda = tienda;
    }
}
