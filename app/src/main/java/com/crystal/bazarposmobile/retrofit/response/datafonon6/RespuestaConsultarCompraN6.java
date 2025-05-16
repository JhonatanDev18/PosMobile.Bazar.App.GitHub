package com.crystal.bazarposmobile.retrofit.response.datafonon6;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RespuestaConsultarCompraN6 {
    @SerializedName("codigoDeComercio")
    @Expose
    private String codigoDeComercio;
    @SerializedName("idDeTransaccion")
    @Expose
    private Integer idDeTransaccion;
    @SerializedName("idCajero")
    @Expose
    private String idCajero;
    @SerializedName("codigoDeTerminal")
    @Expose
    private String codigoDeTerminal;
    @SerializedName("numeroDeCaja")
    @Expose
    private String numeroDeCaja;
    @SerializedName("valorVenta")
    @Expose
    private Double valorVenta;
    @SerializedName("valorIVA")
    @Expose
    private Double valorIVA;
    @SerializedName("valorIAC")
    @Expose
    private Double valorIAC;
    @SerializedName("valorPropina")
    @Expose
    private Double valorPropina;
    @SerializedName("numeroDeFactura")
    @Expose
    private Integer numeroDeFactura;
    @SerializedName("combustible")
    @Expose
    private Object combustible;
    @SerializedName("estadoDeTransaccion")
    @Expose
    private String estadoDeTransaccion;
    @SerializedName("descuento")
    @Expose
    private Object descuento;
    @SerializedName("valorTotal")
    @Expose
    private Double valorTotal;
    @SerializedName("respuestaTransaccion")
    @Expose
    private RespuestaTransaccion respuestaTransaccion;

    public RespuestaConsultarCompraN6(String codigoDeComercio, Integer idDeTransaccion, String idCajero,
                                      String codigoDeTerminal, String numeroDeCaja, Double valorVenta,
                                      Double valorIVA, Double valorIAC, Double valorPropina, Integer numeroDeFactura,
                                      Object combustible, String estadoDeTransaccion, Object descuento,
                                      Double valorTotal, RespuestaTransaccion respuestaTransaccion) {
        this.codigoDeComercio = codigoDeComercio;
        this.idDeTransaccion = idDeTransaccion;
        this.idCajero = idCajero;
        this.codigoDeTerminal = codigoDeTerminal;
        this.numeroDeCaja = numeroDeCaja;
        this.valorVenta = valorVenta;
        this.valorIVA = valorIVA;
        this.valorIAC = valorIAC;
        this.valorPropina = valorPropina;
        this.numeroDeFactura = numeroDeFactura;
        this.combustible = combustible;
        this.estadoDeTransaccion = estadoDeTransaccion;
        this.descuento = descuento;
        this.valorTotal = valorTotal;
        this.respuestaTransaccion = respuestaTransaccion;
    }

    public String getCodigoDeComercio() {
        return codigoDeComercio;
    }

    public void setCodigoDeComercio(String codigoDeComercio) {
        this.codigoDeComercio = codigoDeComercio;
    }

    public Integer getIdDeTransaccion() {
        return idDeTransaccion;
    }

    public void setIdDeTransaccion(Integer idDeTransaccion) {
        this.idDeTransaccion = idDeTransaccion;
    }

    public String getIdCajero() {
        return idCajero;
    }

    public void setIdCajero(String idCajero) {
        this.idCajero = idCajero;
    }

    public String getCodigoDeTerminal() {
        return codigoDeTerminal;
    }

    public void setCodigoDeTerminal(String codigoDeTerminal) {
        this.codigoDeTerminal = codigoDeTerminal;
    }

    public String getNumeroDeCaja() {
        return numeroDeCaja;
    }

    public void setNumeroDeCaja(String numeroDeCaja) {
        this.numeroDeCaja = numeroDeCaja;
    }

    public Double getValorVenta() {
        return valorVenta;
    }

    public void setValorVenta(Double valorVenta) {
        this.valorVenta = valorVenta;
    }

    public Double getValorIVA() {
        return valorIVA;
    }

    public void setValorIVA(Double valorIVA) {
        this.valorIVA = valorIVA;
    }

    public Double getValorIAC() {
        return valorIAC;
    }

    public void setValorIAC(Double valorIAC) {
        this.valorIAC = valorIAC;
    }

    public Double getValorPropina() {
        return valorPropina;
    }

    public void setValorPropina(Double valorPropina) {
        this.valorPropina = valorPropina;
    }

    public Integer getNumeroDeFactura() {
        return numeroDeFactura;
    }

    public void setNumeroDeFactura(Integer numeroDeFactura) {
        this.numeroDeFactura = numeroDeFactura;
    }

    public Object getCombustible() {
        return combustible;
    }

    public void setCombustible(Object combustible) {
        this.combustible = combustible;
    }

    public String getEstadoDeTransaccion() {
        return estadoDeTransaccion;
    }

    public void setEstadoDeTransaccion(String estadoDeTransaccion) {
        this.estadoDeTransaccion = estadoDeTransaccion;
    }

    public Object getDescuento() {
        return descuento;
    }

    public void setDescuento(Object descuento) {
        this.descuento = descuento;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public RespuestaTransaccion getRespuestaTransaccion() {
        return respuestaTransaccion;
    }

    public void setRespuestaTransaccion(RespuestaTransaccion respuestaTransaccion) {
        this.respuestaTransaccion = respuestaTransaccion;
    }

    @Override
    public String toString() {
        return  "CodigoDeComercio: " + codigoDeComercio + '\n' +
                "IdDeTransaccion: " + idDeTransaccion + '\n' +
                "IdCajero: " + idCajero + '\n' +
                "CodigoDeTerminal: " + codigoDeTerminal + '\n' +
                "NumeroDeCaja: " + numeroDeCaja + '\n' +
                "ValorVenta: " + valorVenta + '\n' +
                "ValorIVA: " + valorIVA + '\n' +
                "ValorIAC: " + valorIAC + '\n' +
                "ValorPropina: " + valorPropina + '\n' +
                "NumeroDeFactura: " + numeroDeFactura + '\n' +
                "Combustible: " + combustible + '\n' +
                "EstadoDeTransaccion: " + estadoDeTransaccion + '\n' +
                "Descuento: " + descuento + '\n' +
                "ValorTotal: " + valorTotal + '\n' +
                "RespuestaTransaccion: " + respuestaTransaccion;
    }
}
