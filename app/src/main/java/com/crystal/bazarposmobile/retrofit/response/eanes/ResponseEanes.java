package com.crystal.bazarposmobile.retrofit.response.eanes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseEanes {

    @SerializedName("error")
    @Expose
    private Boolean error;

    @SerializedName("mensaje")
    @Expose
    private String mensaje;

    @SerializedName("productos")
    @Expose
    private List<Producto> productos;

    public ResponseEanes(Boolean error, String mensaje, List<Producto> productos) {
        this.error = error;
        this.mensaje = mensaje;
        this.productos = productos;
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

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }
}
