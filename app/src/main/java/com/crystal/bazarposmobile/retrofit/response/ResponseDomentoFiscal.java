
package com.crystal.bazarposmobile.retrofit.response;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class ResponseDomentoFiscal implements Serializable {

    @Expose
    private Boolean esValida;
    @Expose
    private String mensaje;
    @Expose
    private String number;
    @Expose
    private String referenciaFiscalFirma;

    public ResponseDomentoFiscal(Boolean esValida, String mensaje, String number, String referenciaFiscalFirma) {
        this.esValida = esValida;
        this.mensaje = mensaje;
        this.number = number;
        this.referenciaFiscalFirma = referenciaFiscalFirma;
    }

    public Boolean getEsValida() {
        return esValida;
    }

    public void setEsValida(Boolean esValida) {
        this.esValida = esValida;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getReferenciaFiscalFirma() {
        return referenciaFiscalFirma;
    }

    public void setReferenciaFiscalFirma(String referenciaFiscalFirma) {
        this.referenciaFiscalFirma = referenciaFiscalFirma;
    }

}
