package com.crystal.bazarposmobile.retrofit.request;

import com.google.gson.annotations.SerializedName;

public class RequestInsertarPerifericos {
    @SerializedName("caja")
    private String caja;

    @SerializedName("tienda")
    private String tienda;

    @SerializedName("ipEquipo")
    private String ipEquipo;

    @SerializedName("puertoEquipo")
    private String puertoEquipo;

    @SerializedName("tipoDispositivo")
    private String tipoDispositivo;

    @SerializedName("macDispositivo")
    private String macDispositivo;

    @SerializedName("usuarioWs")
    private String usuarioWs;

    @SerializedName("passwordWs")
    private String passwordWs;

    @SerializedName("codigoDispositivo")
    private String codigoDispositivo;

    @SerializedName("codigoUnicoComercio")
    private String codigoUnicoComercio;

    @SerializedName("usuarioModificador")
    private String usuarioModificador;

    public RequestInsertarPerifericos(String caja, String tienda, String ipEquipo, String puertoEquipo,
                                      String tipoDispositivo, String macDispositivo, String usuarioWs,
                                      String passwordWs, String codigoDispositivo, String codigoUnicoComercio,
                                      String usuarioModificador) {
        this.caja = caja;
        this.tienda = tienda;
        this.ipEquipo = ipEquipo;
        this.puertoEquipo = puertoEquipo;
        this.tipoDispositivo = tipoDispositivo;
        this.macDispositivo = macDispositivo;
        this.usuarioWs = usuarioWs;
        this.passwordWs = passwordWs;
        this.codigoDispositivo = codigoDispositivo;
        this.codigoUnicoComercio = codigoUnicoComercio;
        this.usuarioModificador = usuarioModificador;
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

    public String getIpEquipo() {
        return ipEquipo;
    }

    public void setIpEquipo(String ipEquipo) {
        this.ipEquipo = ipEquipo;
    }

    public String getPuertoEquipo() {
        return puertoEquipo;
    }

    public void setPuertoEquipo(String puertoEquipo) {
        this.puertoEquipo = puertoEquipo;
    }

    public String getTipoDispositivo() {
        return tipoDispositivo;
    }

    public void setTipoDispositivo(String tipoDispositivo) {
        this.tipoDispositivo = tipoDispositivo;
    }

    public String getMacDispositivo() {
        return macDispositivo;
    }

    public void setMacDispositivo(String macDispositivo) {
        this.macDispositivo = macDispositivo;
    }

    public String getUsuarioWs() {
        return usuarioWs;
    }

    public void setUsuarioWs(String usuarioWs) {
        this.usuarioWs = usuarioWs;
    }

    public String getPasswordWs() {
        return passwordWs;
    }

    public void setPasswordWs(String passwordWs) {
        this.passwordWs = passwordWs;
    }

    public String getCodigoDispositivo() {
        return codigoDispositivo;
    }

    public void setCodigoDispositivo(String codigoDispositivo) {
        this.codigoDispositivo = codigoDispositivo;
    }

    public String getCodigoUnicoComercio() {
        return codigoUnicoComercio;
    }

    public void setCodigoUnicoComercio(String codigoUnicoComercio) {
        this.codigoUnicoComercio = codigoUnicoComercio;
    }

    public String getUsuarioModificador() {
        return usuarioModificador;
    }

    public void setUsuarioModificador(String usuarioModificador) {
        this.usuarioModificador = usuarioModificador;
    }
}
