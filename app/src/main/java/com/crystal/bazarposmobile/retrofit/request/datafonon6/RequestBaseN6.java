package com.crystal.bazarposmobile.retrofit.request.datafonon6;

import com.crystal.bazarposmobile.common.SeguridadDatafono;
import com.google.gson.annotations.Expose;

public class RequestBaseN6 {
    @Expose
    private String idTransacion;
    @Expose
    private SeguridadDatafono seguridad;

    public RequestBaseN6(String idTransacion, SeguridadDatafono seguridad) {
        this.idTransacion = idTransacion;
        this.seguridad = seguridad;
    }

    public String getIdTransacion() {
        return idTransacion;
    }

    public void setIdTransacion(String idTransacion) {
        this.idTransacion = idTransacion;
    }

    public SeguridadDatafono getSeguridad() {
        return seguridad;
    }

    public void setSeguridad(SeguridadDatafono seguridad) {
        this.seguridad = seguridad;
    }

    @Override
    public String toString() {
        return "RequestBaseN6{" +
                "idTransacion='" + idTransacion + '\'' +
                ", seguridad=" + seguridad +
                '}';
    }
}
