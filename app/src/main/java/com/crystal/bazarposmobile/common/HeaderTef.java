package com.crystal.bazarposmobile.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HeaderTef {
    @SerializedName("fechaCreacion")
    @Expose
    private String fechaCreacion;
    @SerializedName("numeroTransaccion")
    @Expose
    private String numeroTransaccion;
    @SerializedName("tienda")
    @Expose
    private String tienda;
    @SerializedName("caja")
    @Expose
    private String caja;
    @SerializedName("referenciaInterna")
    @Expose
    private String referenciaInterna;
    @SerializedName("anulada")
    @Expose
    private Boolean anulada;

    public HeaderTef(String fechaCreacion, String numeroTransaccion, String tienda, String caja, String referenciaInterna, Boolean anulada) {
        this.fechaCreacion = fechaCreacion;
        this.numeroTransaccion = numeroTransaccion;
        this.tienda = tienda;
        this.caja = caja;
        this.referenciaInterna = referenciaInterna;
        this.anulada = anulada;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getNumeroTransaccion() {
        return numeroTransaccion;
    }

    public void setNumeroTransaccion(String numeroTransaccion) {
        this.numeroTransaccion = numeroTransaccion;
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

    public String getReferenciaInterna() {
        return referenciaInterna;
    }

    public void setReferenciaInterna(String referenciaInterna) {
        this.referenciaInterna = referenciaInterna;
    }

    public Boolean getAnulada() {
        return anulada;
    }

    public void setAnulada(Boolean anulada) {
        this.anulada = anulada;
    }

    @Override
    public String toString() {
        return "HeaderTef{" +
                "fechaCreacion='" + fechaCreacion + '\'' +
                ", numeroTransaccion='" + numeroTransaccion + '\'' +
                ", tienda='" + tienda + '\'' +
                ", caja='" + caja + '\'' +
                ", referenciaInterna='" + referenciaInterna + '\'' +
                ", anulada=" + anulada +
                '}';
    }
}
