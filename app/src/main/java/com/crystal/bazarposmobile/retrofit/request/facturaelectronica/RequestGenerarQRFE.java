package com.crystal.bazarposmobile.retrofit.request.facturaelectronica;

import com.google.gson.annotations.Expose;

public class RequestGenerarQRFE {
    @Expose
    private int numeroTransaccion;
    @Expose
    private String tienda;

    @Expose
    private String tipoDocumento;

    @Expose
    private String tipoFactura;

    @Expose
    private String numeroFactura;

    @Expose
    private String referenciaFirmaFiscal;

    public RequestGenerarQRFE(int numeroTransaccion, String tienda, String tipoDocumento, String tipoFactura,
                              String numeroFactura, String referenciaFirmaFiscal) {
        this.numeroTransaccion = numeroTransaccion;
        this.tienda = tienda;
        this.tipoDocumento = tipoDocumento;
        this.tipoFactura = tipoFactura;
        this.numeroFactura = numeroFactura;
        this.referenciaFirmaFiscal = referenciaFirmaFiscal;
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

    public String getTipoFactura() {
        return tipoFactura;
    }

    public void setTipoFactura(String tipoFactura) {
        this.tipoFactura = tipoFactura;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getReferenciaFirmaFiscal() {
        return referenciaFirmaFiscal;
    }

    public void setReferenciaFirmaFiscal(String referenciaFirmaFiscal) {
        this.referenciaFirmaFiscal = referenciaFirmaFiscal;
    }
}
