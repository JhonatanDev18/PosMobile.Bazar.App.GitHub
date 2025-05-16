package com.crystal.bazarposmobile.retrofit.response.documentodetalle;

import com.google.gson.annotations.Expose;

public class ResponseDD {

    @Expose
    private Boolean error;
    @Expose
    private String mensaje;
    @Expose
    private DocumentoDetalle documentoDetalle;

    public ResponseDD(Boolean error, String mensaje, DocumentoDetalle documentoDetalle) {
        this.error = error;
        this.mensaje = mensaje;
        this.documentoDetalle = documentoDetalle;
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

    public DocumentoDetalle getDocumentoDetalle() {
        return documentoDetalle;
    }

    public void setDocumentoDetalle(DocumentoDetalle documentoDetalle) {
        this.documentoDetalle = documentoDetalle;
    }
}
