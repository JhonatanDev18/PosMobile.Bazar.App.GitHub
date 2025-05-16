package com.crystal.bazarposmobile.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SeguridadDatafono {
    @SerializedName("isRedeban")
    @Expose
    private boolean isRedeban;

    @SerializedName("codigoUnico")
    @Expose
    private String codigoUnico;

    @SerializedName("usuario")
    @Expose
    private String usuario;

    @SerializedName("contrasena")
    @Expose
    private String contrasena;

    public SeguridadDatafono(boolean isRedeban, String codigoUnico, String usuario, String contrasena) {
        this.isRedeban = isRedeban;
        this.codigoUnico = codigoUnico;
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    public boolean isRedeban() {
        return isRedeban;
    }

    public void setRedeban(boolean redeban) {
        isRedeban = redeban;
    }

    public String getCodigoUnico() {
        return codigoUnico;
    }

    public void setCodigoUnico(String codigoUnico) {
        this.codigoUnico = codigoUnico;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    @Override
    public String toString() {
        return "SeguridadDatafono{" +
                "isRedeban=" + isRedeban +
                ", codigoUnico='" + codigoUnico + '\'' +
                ", usuario='" + usuario + '\'' +
                ", contrasena='" + contrasena + '\'' +
                '}';
    }
}
