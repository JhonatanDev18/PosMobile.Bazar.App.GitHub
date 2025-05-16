package com.crystal.bazarposmobile.retrofit.response.documentodetalle;

import com.google.gson.annotations.Expose;

import java.util.List;

public class ResponseDDList {

    @Expose
    private Boolean error;
    @Expose
    private String mensaje;
    @Expose
    private List<DocumentoDetalle> documentoDetalles;

    public ResponseDDList(Boolean error, String mensaje, List<DocumentoDetalle> documentoDetalles) {
        this.error = error;
        this.mensaje = mensaje;
        this.documentoDetalles = documentoDetalles;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public List<DocumentoDetalle> getDocumentoDetalleList() {
        return documentoDetalles;
    }

    public void setDocumentoDetalleList(List<DocumentoDetalle> documentoDetalleList) {
        this.documentoDetalles = documentoDetalleList;
    }

    @Override
    public String toString() {
        return "ResponseDDList{" +
                "error=" + error +
                ", mensaje='" + mensaje + '\'' +
                ", documentoDetalles=" + documentoDetalles +
                '}';
    }
}
