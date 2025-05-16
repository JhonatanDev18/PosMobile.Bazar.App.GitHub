package com.crystal.bazarposmobile.common;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class TipoDatafono implements Serializable {
    @Expose
    private String nombreDispositivo;

    @Expose
    private boolean activo;

    @Expose
    private boolean redeban;

    public TipoDatafono(String nombreDispositivo, boolean activo, boolean redeban) {
        this.nombreDispositivo = nombreDispositivo;
        this.activo = activo;
        this.redeban = redeban;
    }

    public String getNombreDispositivo() {
        return nombreDispositivo;
    }

    public void setNombreDispositivo(String nombreDispositivo) {
        this.nombreDispositivo = nombreDispositivo;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public boolean isRedeban() {
        return redeban;
    }

    public void setRedeban(boolean redeban) {
        this.redeban = redeban;
    }
}