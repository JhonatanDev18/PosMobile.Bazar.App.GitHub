package com.crystal.bazarposmobile.retrofit.response;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class ResponseCupoEmpleado implements Serializable {
    @Expose
    private Long cupo;
    @Expose
    private String documentoEmpleado;
    @Expose
    private String empresa;
    @Expose
    private Boolean esValida;
    @Expose
    private String fecha;
    @Expose
    private String mensaje;
    @Expose
    private String nombre;

    public ResponseCupoEmpleado(Long cupo, String documentoEmpleado, String empresa, Boolean esValida, String fecha, String mensaje, String nombre) {
        this.cupo = cupo;
        this.documentoEmpleado = documentoEmpleado;
        this.empresa = empresa;
        this.esValida = esValida;
        this.fecha = fecha;
        this.mensaje = mensaje;
        this.nombre = nombre;
    }

    public Long getCupo() {
        return cupo;
    }

    public void setCupo(Long cupo) {
        this.cupo = cupo;
    }

    public String getDocumentoEmpleado() {
        return documentoEmpleado;
    }

    public void setDocumentoEmpleado(String documentoEmpleado) {
        this.documentoEmpleado = documentoEmpleado;
    }

    public String getEmpresa() {
        return empresa.trim();
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public Boolean getEsValida() {
        return esValida;
    }

    public void setEsValida(Boolean esValida) {
        this.esValida = esValida;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
