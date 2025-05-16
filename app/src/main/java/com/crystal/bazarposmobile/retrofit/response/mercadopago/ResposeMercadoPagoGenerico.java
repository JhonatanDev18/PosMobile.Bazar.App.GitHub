
package com.crystal.bazarposmobile.retrofit.response.mercadopago;


import com.google.gson.annotations.Expose;


public class ResposeMercadoPagoGenerico {

    @Expose
    private Boolean error;
    @Expose
    private String mensaje;
    @Expose
    private String resultado;

    public ResposeMercadoPagoGenerico(Boolean error, String mensaje, String resultado) {
        this.error = error;
        this.mensaje = mensaje;
        this.resultado = resultado;
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

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

}
