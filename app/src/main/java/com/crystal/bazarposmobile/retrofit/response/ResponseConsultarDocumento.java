package com.crystal.bazarposmobile.retrofit.response;

import com.crystal.bazarposmobile.retrofit.response.eanes.Producto;

import java.util.List;

public class ResponseConsultarDocumento {

    Boolean error;

    String mensaje;

    List<Producto> productos;

    public ResponseConsultarDocumento(Boolean error, String mensaje, List<Producto> productos) {
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
