package com.crystal.bazarposmobile.retrofit.request.datafonon6;

import com.crystal.bazarposmobile.retrofit.response.datafonon6.CuerpoRespuestaDatafonoN6;
import com.google.gson.annotations.Expose;

public class RequestGuardarRespuestasTef {
    @Expose
    private String horaCreacion;

    @Expose
    private String minutosCreacion;

    @Expose
    private String numeroTransaccion;

    @Expose
    private String tienda;

    @Expose
    private String caja;

    @Expose
    private String referenciaInterna;

    @Expose
    private CuerpoRespuestaDatafonoN6 respuestaTef;

    public RequestGuardarRespuestasTef(String horaCreacion, String minutosCreacion, String numeroTransaccion,
                                       String tienda, String caja, String referenciaInterna,
                                       CuerpoRespuestaDatafonoN6 respuestaTef) {
        this.horaCreacion = horaCreacion;
        this.minutosCreacion = minutosCreacion;
        this.numeroTransaccion = numeroTransaccion;
        this.tienda = tienda;
        this.caja = caja;
        this.referenciaInterna = referenciaInterna;
        this.respuestaTef = respuestaTef;
    }

    public String getHoraCreacion() {
        return horaCreacion;
    }

    public void setHoraCreacion(String horaCreacion) {
        this.horaCreacion = horaCreacion;
    }

    public String getMinutosCreacion() {
        return minutosCreacion;
    }

    public void setMinutosCreacion(String minutosCreacion) {
        this.minutosCreacion = minutosCreacion;
    }

    public String getNumeroTransaccion() {
        return numeroTransaccion;
    }

    public void setNumeroTransaccion(String numeroTransaccion) {
        this.numeroTransaccion = numeroTransaccion;
    }

    public String getTienda() {
        return tienda;
    }

    public void setTienda(String tienda) {
        this.tienda = tienda;
    }

    public String getCaja() {
        return caja;
    }

    public void setCaja(String caja) {
        this.caja = caja;
    }

    public String getReferenciaInterna() {
        return referenciaInterna;
    }

    public void setReferenciaInterna(String referenciaInterna) {
        this.referenciaInterna = referenciaInterna;
    }

    public CuerpoRespuestaDatafonoN6 getRespuestaTef() {
        return respuestaTef;
    }

    public void setRespuestaTef(CuerpoRespuestaDatafonoN6 respuestaTef) {
        this.respuestaTef = respuestaTef;
    }
}
