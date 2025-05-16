package com.crystal.bazarposmobile.retrofit.response.postdocumento;

import com.google.gson.annotations.SerializedName;

public class PostDocumento{

    @SerializedName("referenciaFiscalFirma")
    private String referenciaFiscalFirma;

    @SerializedName("naturaleza")
    private String naturaleza;

    @SerializedName("numeroTransaccion")
    private String numeroTransaccion;

    @SerializedName("indice")
    private Integer indice;

    @SerializedName("referenciaInterna")
    private String referenciaInterna;

    @SerializedName("codigoCaja")
    private String codigoCaja;

    @SerializedName("numeroDia")
    private Integer numeroDia;

    @SerializedName("codigoTienda")
    private String codigoTienda;

    public PostDocumento(String referenciaFiscalFirma, String naturaleza, String numeroTransaccion, Integer indice, String referenciaInterna, String codigoCaja, Integer numeroDia, String codigoTienda) {
        this.referenciaFiscalFirma = referenciaFiscalFirma;
        this.naturaleza = naturaleza;
        this.numeroTransaccion = numeroTransaccion;
        this.indice = indice;
        this.referenciaInterna = referenciaInterna;
        this.codigoCaja = codigoCaja;
        this.numeroDia = numeroDia;
        this.codigoTienda = codigoTienda;
    }

    public String getReferenciaFiscalFirma() {
        return referenciaFiscalFirma;
    }

    public void setReferenciaFiscalFirma(String referenciaFiscalFirma) {
        this.referenciaFiscalFirma = referenciaFiscalFirma;
    }

    public String getNaturaleza() {
        return naturaleza;
    }

    public void setNaturaleza(String naturaleza) {
        this.naturaleza = naturaleza;
    }

    public String getNumeroTransaccion() {
        return numeroTransaccion;
    }

    public void setNumeroTransaccion(String numeroTransaccion) {
        this.numeroTransaccion = numeroTransaccion;
    }

    public Integer getIndice() {
        return indice;
    }

    public void setIndice(Integer indice) {
        this.indice = indice;
    }

    public String getReferenciaInterna() {
        return referenciaInterna;
    }

    public void setReferenciaInterna(String referenciaInterna) {
        this.referenciaInterna = referenciaInterna;
    }

    public String getCodigoCaja() {
        return codigoCaja;
    }

    public void setCodigoCaja(String codigoCaja) {
        this.codigoCaja = codigoCaja;
    }

    public Integer getNumeroDia() {
        return numeroDia;
    }

    public void setNumeroDia(Integer numeroDia) {
        this.numeroDia = numeroDia;
    }

    public String getCodigoTienda() {
        return codigoTienda;
    }

    public void setCodigoTienda(String codigoTienda) {
        this.codigoTienda = codigoTienda;
    }

    @Override
     public String toString(){
        return 
            "PostDocumento{" + 
            "referenciaFiscalFirma = '" + referenciaFiscalFirma + '\'' + 
            ",naturaleza = '" + naturaleza + '\'' + 
            ",numeroTransaccion = '" + numeroTransaccion + '\'' + 
            ",indice = '" + indice + '\'' + 
            ",referenciaInterna = '" + referenciaInterna + '\'' + 
            ",codigoCaja = '" + codigoCaja + '\'' + 
            ",numeroDia = '" + numeroDia + '\'' + 
            ",codigoTienda = '" + codigoTienda + '\'' + 
            "}";
        }
}