
package com.crystal.bazarposmobile.retrofit.response;

import com.google.gson.annotations.Expose;

public class ResponseValidarAperturaCaja {

    @Expose
    private Boolean esValida;
    @Expose
    private String mensaje;
    @Expose
    private String apellidoVendedor;
    @Expose
    private String cajero;
    @Expose
    private String codigoCaja;
    @Expose
    private String codigoEstadoCaja;
    @Expose
    private String estadoCaja;
    @Expose
    private String fechaApertura;
    @Expose
    private String nombreVendedor;
    @Expose
    private Integer numeroDia;

    public ResponseValidarAperturaCaja(Boolean esValida, String mensaje, String apellidoVendedor, String cajero, String codigoCaja, String codigoEstadoCaja, String estadoCaja, String fechaApertura, String nombreVendedor, Integer numeroDia) {
        this.esValida = esValida;
        this.mensaje = mensaje;
        this.apellidoVendedor = apellidoVendedor;
        this.cajero = cajero;
        this.codigoCaja = codigoCaja;
        this.codigoEstadoCaja = codigoEstadoCaja;
        this.estadoCaja = estadoCaja;
        this.fechaApertura = fechaApertura;
        this.nombreVendedor = nombreVendedor;
        this.numeroDia = numeroDia;
    }

    public String getApellidoVendedor() {
        return apellidoVendedor;
    }

    public void setApellidoVendedor(String apellidoVendedor) {
        this.apellidoVendedor = apellidoVendedor;
    }

    public String getCajero() {
        return cajero;
    }

    public void setCajero(String cajero) {
        this.cajero = cajero;
    }

    public String getCodigoCaja() {
        return codigoCaja;
    }

    public void setCodigoCaja(String codigoCaja) {
        this.codigoCaja = codigoCaja;
    }

    public String getCodigoEstadoCaja() {
        return codigoEstadoCaja;
    }

    public void setCodigoEstadoCaja(String codigoEstadoCaja) {
        this.codigoEstadoCaja = codigoEstadoCaja;
    }

    public String getEstadoCaja() {
        return estadoCaja;
    }

    public void setEstadoCaja(String estadoCaja) {
        this.estadoCaja = estadoCaja;
    }

    public String getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(String fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public String getNombreVendedor() {
        return nombreVendedor;
    }

    public void setNombreVendedor(String nombreVendedor) {
        this.nombreVendedor = nombreVendedor;
    }

    public Integer getNumeroDia() {
        return numeroDia;
    }

    public void setNumeroDia(Integer numeroDia) {
        this.numeroDia = numeroDia;
    }

    public Boolean getEsValida() {
        return esValida;
    }

    public void setEsValida(Boolean esValida) {
        this.esValida = esValida;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
