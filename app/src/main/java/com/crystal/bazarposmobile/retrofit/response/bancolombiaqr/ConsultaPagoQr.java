package com.crystal.bazarposmobile.retrofit.response.bancolombiaqr;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ConsultaPagoQr implements Serializable  {
    @SerializedName("error")
    @Expose
    private Boolean error;

    @SerializedName("mensaje")
    @Expose
    private String mensaje;

    @SerializedName("pago")
    @Expose
    private PagoBancolombiaQr pago;

    public ConsultaPagoQr(Boolean error, String mensaje, PagoBancolombiaQr pago) {
        this.error = error;
        this.mensaje = mensaje;
        this.pago = pago;
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

    public PagoBancolombiaQr getPago() {
        return pago;
    }

    public void setPago(PagoBancolombiaQr pago) {
        this.pago = pago;
    }
}
