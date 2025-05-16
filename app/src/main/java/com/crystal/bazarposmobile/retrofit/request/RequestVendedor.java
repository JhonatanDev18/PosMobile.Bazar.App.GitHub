
package com.crystal.bazarposmobile.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestVendedor {

    @SerializedName("usuario")
    @Expose
    private String usuario;
    @SerializedName("fecha")
    @Expose
    private String fecha;

    /**
     * No args constructor for use in serialization
     * 
     */
    public RequestVendedor() {
    }

    /**
     * 
     * @param fecha
     * @param usuario
     */
    public RequestVendedor(String usuario, String fecha) {
        super();
        this.usuario = usuario;
        this.fecha = fecha;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

}
