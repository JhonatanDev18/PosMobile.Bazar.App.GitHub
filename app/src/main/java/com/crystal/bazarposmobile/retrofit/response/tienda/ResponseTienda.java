package com.crystal.bazarposmobile.retrofit.response.tienda;

import com.google.gson.annotations.SerializedName;

public class ResponseTienda {
    @SerializedName("tienda")
    public TiendaItem tienda;

    @SerializedName("mensaje")
    public String mensaje;
    @SerializedName("error")
    private Boolean error = false;

    public TiendaItem getTienda() {
        return tienda;
    }

    public void setTienda(TiendaItem tienda) {
        this.tienda = tienda;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "ResponseTienda{" +
                "tienda=" + tienda +
                ", mensaje='" + mensaje + '\'' +
                ", error=" + error +
                '}';
    }
}
