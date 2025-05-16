
package com.crystal.bazarposmobile.retrofit.response.caja;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Caja {

    @SerializedName("mediosPagoAutorizados")
    @Expose
    private String mediosPagoAutorizados;

    @SerializedName("codigoCaja")
    @Expose
    private String codigoCaja;

    @SerializedName("codigoTienda")
    @Expose
    private String codigoTienda;

    @SerializedName("prefijoCaja")
    @Expose
    private String prefijoCaja;

    @SerializedName("cajaMovil")
    @Expose
    private String cajaMovil;

    @SerializedName("divisa")
    @Expose
    private String divisa;

    @SerializedName("pais")
    @Expose
    private String pais;

    @SerializedName("identificadorCaja")
    @Expose
    private String identificadorCaja;

    @SerializedName("nombreTienda")
    @Expose
    private String nombreTienda;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Caja() {
    }

    /**
     * @param mediosPagoAutorizados
     * @param codigoCaja
     * @param codigoTienda
     * @param prefijoCaja
     * @param cajaMovil
     * @param divisa
     * @param pais
     * @param identificadorCaja
     * @param nombreTienda
     */
    public Caja(String mediosPagoAutorizados, String codigoCaja, String codigoTienda, String prefijoCaja, String cajaMovil, String divisa, String pais, String identificadorCaja, String nombreTienda) {
        super();
        this.mediosPagoAutorizados = mediosPagoAutorizados;
        this.codigoCaja = codigoCaja;
        this.codigoTienda = codigoTienda;
        this.prefijoCaja = prefijoCaja;
        this.cajaMovil = cajaMovil;
        this.divisa = divisa;
        this.pais = pais;
        this.identificadorCaja = identificadorCaja;
        this.nombreTienda = nombreTienda;
    }

    public String getMediosPagoAutorizados() {
        return mediosPagoAutorizados;
    }

    public void setMediosPagoAutorizados(String mediosPagoAutorizados) {
        this.mediosPagoAutorizados = mediosPagoAutorizados;
    }

    public String getCodigoCaja() {
        return codigoCaja;
    }

    public void setCodigoCaja(String codigoCaja) {
        this.codigoCaja = codigoCaja;
    }

    public String getCodigoTienda() {
        return codigoTienda;
    }

    public void setCodigoTienda(String codigoTienda) {
        this.codigoTienda = codigoTienda;
    }

    public String getCajaMovil() {
        return cajaMovil;
    }

    public void setCajaMovil(String cajaMovil) {
        this.cajaMovil = cajaMovil;
    }

    public String getDivisa() {
        return divisa;
    }

    public void setDivisa(String divisa) {
        this.divisa = divisa;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getPrefijoCaja() {
        return prefijoCaja;
    }

    public void setPrefijoCaja(String prefijoCaja) {
        this.prefijoCaja = prefijoCaja;
    }

    public String getIdentificadorCaja() {
        return identificadorCaja;
    }

    public void setIdentificadorCaja(String identificadorCaja) {
        this.identificadorCaja = identificadorCaja;
    }

    public String getNombreTienda() {
        return nombreTienda;
    }

    public void setNombreTienda(String nombreTienda) {
        this.nombreTienda = nombreTienda;
    }
}
