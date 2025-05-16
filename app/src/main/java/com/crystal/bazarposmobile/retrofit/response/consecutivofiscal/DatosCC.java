package com.crystal.bazarposmobile.retrofit.response.consecutivofiscal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DatosCC{
    @SerializedName("codigoTienda")
    @Expose
    private String codigoTienda;
    @SerializedName("caja")
    @Expose
    private String caja;
    @SerializedName("fechaFinal")
    @Expose
    private String fechaFinal;
    @SerializedName("numeroInicial")
    @Expose
    private Integer numeroInicial;
    @SerializedName("numeroFinal")
    @Expose
    private Integer numeroFinal;
    @SerializedName("diaAlerta")
    @Expose
    private Integer diaAlerta;
    @SerializedName("mensajeAlertaLimiteFiscal")
    @Expose
    private String mensajeAlertaLimiteFiscal;
    @SerializedName("mensajeAlertaLimiteConsesecutivo")
    @Expose
    private String mensajeAlertaLimiteConsesecutivo;
    @SerializedName("mensajeAlertaFechaResolucion")
    @Expose
    private String mensajeAlertaFechaResolucion;
    @SerializedName("mensajeAlertaVencimientoResolucion")
    @Expose
    private String mensajeAlertaVencimientoResolucion;
    @SerializedName("numeroAlerta")
    @Expose
    private Integer numeroAlerta;
    @SerializedName("numeroResolucion")
    @Expose
    private String numeroResolucion;
    @SerializedName("fechaAutorizacion")
    @Expose
    private String fechaAutorizacion;
    @SerializedName("prefijo")
    @Expose
    private String prefijo;
    @SerializedName("comenzarEn")
    @Expose
    private Integer comenzarEn;
    @SerializedName("tipoResolucion")
    @Expose
    private String tipoResolucion;
    @SerializedName("consecutivo")
    @Expose
    private Integer consecutivo;
    @SerializedName("claveFacturaElectronica")
    @Expose
    private String claveFacturaElectronica;

    public DatosCC(String codigoTienda, String caja, String fechaFinal, Integer numeroInicial,
                             Integer numeroFinal, Integer diaAlerta, String mensajeAlertaLimiteFiscal,
                             String mensajeAlertaLimiteConsesecutivo, String mensajeAlertaFechaResolucion,
                             String mensajeAlertaVencimientoResolucion, Integer numeroAlerta, String numeroResolucion,
                             String fechaAutorizacion, String prefijo, Integer comenzarEn, String tipoResolucion,
                             Integer consecutivo, String claveFacturaElectronica) {
        this.codigoTienda = codigoTienda;
        this.caja = caja;
        this.fechaFinal = fechaFinal;
        this.numeroInicial = numeroInicial;
        this.numeroFinal = numeroFinal;
        this.diaAlerta = diaAlerta;
        this.mensajeAlertaLimiteFiscal = mensajeAlertaLimiteFiscal;
        this.mensajeAlertaLimiteConsesecutivo = mensajeAlertaLimiteConsesecutivo;
        this.mensajeAlertaFechaResolucion = mensajeAlertaFechaResolucion;
        this.mensajeAlertaVencimientoResolucion = mensajeAlertaVencimientoResolucion;
        this.numeroAlerta = numeroAlerta;
        this.numeroResolucion = numeroResolucion;
        this.fechaAutorizacion = fechaAutorizacion;
        this.prefijo = prefijo;
        this.comenzarEn = comenzarEn;
        this.tipoResolucion = tipoResolucion;
        this.consecutivo = consecutivo;
        this.claveFacturaElectronica = claveFacturaElectronica;
    }

    public String getCodigoTienda() {
        return codigoTienda;
    }

    public void setCodigoTienda(String codigoTienda) {
        this.codigoTienda = codigoTienda;
    }

    public String getCaja() {
        return caja;
    }

    public void setCaja(String caja) {
        this.caja = caja;
    }

    public String getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(String fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public Integer getNumeroInicial() {
        return numeroInicial;
    }

    public void setNumeroInicial(Integer numeroInicial) {
        this.numeroInicial = numeroInicial;
    }

    public Integer getNumeroFinal() {
        return numeroFinal;
    }

    public void setNumeroFinal(Integer numeroFinal) {
        this.numeroFinal = numeroFinal;
    }

    public Integer getDiaAlerta() {
        return diaAlerta;
    }

    public void setDiaAlerta(Integer diaAlerta) {
        this.diaAlerta = diaAlerta;
    }

    public String getMensajeAlertaLimiteFiscal() {
        return mensajeAlertaLimiteFiscal;
    }

    public void setMensajeAlertaLimiteFiscal(String mensajeAlertaLimiteFiscal) {
        this.mensajeAlertaLimiteFiscal = mensajeAlertaLimiteFiscal;
    }

    public String getMensajeAlertaLimiteConsesecutivo() {
        return mensajeAlertaLimiteConsesecutivo;
    }

    public void setMensajeAlertaLimiteConsesecutivo(String mensajeAlertaLimiteConsesecutivo) {
        this.mensajeAlertaLimiteConsesecutivo = mensajeAlertaLimiteConsesecutivo;
    }

    public String getMensajeAlertaFechaResolucion() {
        return mensajeAlertaFechaResolucion;
    }

    public void setMensajeAlertaFechaResolucion(String mensajeAlertaFechaResolucion) {
        this.mensajeAlertaFechaResolucion = mensajeAlertaFechaResolucion;
    }

    public String getMensajeAlertaVencimientoResolucion() {
        return mensajeAlertaVencimientoResolucion;
    }

    public void setMensajeAlertaVencimientoResolucion(String mensajeAlertaVencimientoResolucion) {
        this.mensajeAlertaVencimientoResolucion = mensajeAlertaVencimientoResolucion;
    }

    public Integer getNumeroAlerta() {
        return numeroAlerta;
    }

    public void setNumeroAlerta(Integer numeroAlerta) {
        this.numeroAlerta = numeroAlerta;
    }

    public String getNumeroResolucion() {
        return numeroResolucion;
    }

    public void setNumeroResolucion(String numeroResolucion) {
        this.numeroResolucion = numeroResolucion;
    }

    public String getFechaAutorizacion() {
        return fechaAutorizacion;
    }

    public void setFechaAutorizacion(String fechaAutorizacion) {
        this.fechaAutorizacion = fechaAutorizacion;
    }

    public String getPrefijo() {
        return prefijo;
    }

    public void setPrefijo(String prefijo) {
        this.prefijo = prefijo;
    }

    public Integer getComenzarEn() {
        return comenzarEn;
    }

    public void setComenzarEn(Integer comenzarEn) {
        this.comenzarEn = comenzarEn;
    }

    public String getTipoResolucion() {
        return tipoResolucion;
    }

    public void setTipoResolucion(String tipoResolucion) {
        this.tipoResolucion = tipoResolucion;
    }

    public Integer getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(Integer consecutivo) {
        this.consecutivo = consecutivo;
    }

    public String getClaveFacturaElectronica() {
        return claveFacturaElectronica;
    }

    public void setClaveFacturaElectronica(String claveFacturaElectronica) {
        this.claveFacturaElectronica = claveFacturaElectronica;
    }
}