package com.crystal.bazarposmobile.retrofit.response.facturaelectronica;

import com.google.gson.annotations.Expose;

public class RequestGuardarTrazaFE {
    @Expose
    private int numeroTransaccion;
    @Expose
    private String tienda;

    @Expose
    private String tipoDocumento;

    @Expose
    private String tipoResolucion;

    @Expose
    private String numeroFactura;

    @Expose
    private int consecutivo;

    @Expose
    private String prefijo;

    @Expose
    private String cufe;

    public RequestGuardarTrazaFE(int numeroTransaccion, String tienda, String tipoDocumento,
                                 String tipoResolucion, String numeroFactura, int consecutivo,
                                 String prefijo, String cufe) {
        this.numeroTransaccion = numeroTransaccion;
        this.tienda = tienda;
        this.tipoDocumento = tipoDocumento;
        this.tipoResolucion = tipoResolucion;
        this.numeroFactura = numeroFactura;
        this.consecutivo = consecutivo;
        this.prefijo = prefijo;
        this.cufe = cufe;
    }

    public int getNumeroTransaccion() {
        return numeroTransaccion;
    }

    public void setNumeroTransaccion(int numeroTransaccion) {
        this.numeroTransaccion = numeroTransaccion;
    }

    public String getTienda() {
        return tienda;
    }

    public void setTienda(String tienda) {
        this.tienda = tienda;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getTipoResolucion() {
        return tipoResolucion;
    }

    public void setTipoResolucion(String tipoResolucion) {
        this.tipoResolucion = tipoResolucion;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public int getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(int consecutivo) {
        this.consecutivo = consecutivo;
    }

    public String getPrefijo() {
        return prefijo;
    }

    public void setPrefijo(String prefijo) {
        this.prefijo = prefijo;
    }

    public String getCufe() {
        return cufe;
    }

    public void setCufe(String cufe) {
        this.cufe = cufe;
    }
}
