package com.crystal.bazarposmobile.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestActualizarCupoEmpleado {
    @SerializedName("documentoEmpleado")
    @Expose
    private String documentoEmpleado;
    @SerializedName("cupo")
    @Expose
    private Integer cupo;
    @SerializedName("empresa")
    @Expose
    private String empresa;
    @SerializedName("nombre")
    @Expose
    private String nombre;

    public RequestActualizarCupoEmpleado(String documentoEmpleado, Integer cupo, String empresa, String nombre) {
        this.documentoEmpleado = documentoEmpleado;
        this.cupo = cupo;
        this.empresa = empresa;
        this.nombre = nombre;
    }

    public String getDocumentoEmpleado() {
        return documentoEmpleado;
    }

    public void setDocumentoEmpleado(String documentoEmpleado) {
        this.documentoEmpleado = documentoEmpleado;
    }

    public Integer getCupo() {
        return cupo;
    }

    public void setCupo(Integer cupo) {
        this.cupo = cupo;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "RequestActualizarCupoEmpleado{" +
                "documentoEmpleado='" + documentoEmpleado + '\'' +
                ", cupo=" + cupo +
                ", empresa='" + empresa + '\'' +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
