
package com.crystal.bazarposmobile.retrofit.response.cliente;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseClienteLista {

    @SerializedName("error")
    @Expose
    private Boolean error;

    @SerializedName("mensaje")
    @Expose
    private String mensaje;

    @SerializedName("clientes")
    @Expose
    private List<Cliente> clientes;

    public ResponseClienteLista(Boolean error, String mensaje, List<Cliente> clientes) {
        this.error = error;
        this.mensaje = mensaje;
        this.clientes = clientes;
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

    public List<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }
}