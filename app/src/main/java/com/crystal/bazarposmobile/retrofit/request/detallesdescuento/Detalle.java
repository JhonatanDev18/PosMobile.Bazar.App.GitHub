package com.crystal.bazarposmobile.retrofit.request.detallesdescuento;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Detalle {

    @SerializedName("motivoDescuento")
    @Expose
    private String motivoDescuento;
    @SerializedName("porcentajeDescuento")
    @Expose
    private Double porcentajeDescuento;
    @SerializedName("valeUrrem")
    @Expose
    private Integer valeUrrem;
    @SerializedName("baseDescuento")
    @Expose
    private Double baseDescuento;
    @SerializedName("baseDescuentoDivisa")
    @Expose
    private Double baseDescuentoDivisa;
    @SerializedName("importeIngresoSinImpuestos")
    @Expose
    private Double importeIngresoSinImpuestos;
    @SerializedName("importeIngresoSinImpuestosDivisa")
    @Expose
    private Double importeIngresoSinImpuestosDivisa;
    @SerializedName("importeIngresoConImpuestos")
    @Expose
    private Double importeIngresoConImpuestos;
    @SerializedName("importeIngresoConImpuestosDivisas")
    @Expose
    private Double importeIngresoConImpuestosDivisas;
    @SerializedName("dotacion")
    @Expose
    private Integer dotacion;
    @SerializedName("tipoDocumento")
    @Expose
    private String tipoDocumento;
    @SerializedName("indiceDocumento")
    @Expose
    private Integer indiceDocumento;
    @SerializedName("numeroLinea")
    @Expose
    private Integer numeroLinea;
    @SerializedName("numeroOrden")
    @Expose
    private Integer numeroOrden;
    @SerializedName("ordenDelDescuento")
    @Expose
    private Integer ordenDelDescuento;
    @SerializedName("numeroRango")
    @Expose
    private Integer numeroRango;
    @SerializedName("establecimiento")
    @Expose
    private String establecimiento;
    @SerializedName("fechaDocumento")
    @Expose
    private String fechaDocumento;
    @SerializedName("divisaSouche")
    @Expose
    private String divisaSouche;
    @SerializedName("divisaEstablecimiento")
    @Expose
    private String divisaEstablecimiento;
    @SerializedName("tipoDescuento")
    @Expose
    private String tipoDescuento;
    @SerializedName("codigoCondicionComercial")
    @Expose
    private String codigoCondicionComercial;
    @SerializedName("codigoEstat")
    @Expose
    private String codigoEstat;

    public Detalle(String motivoDescuento, Double porcentajeDescuento, Integer valeUrrem, Double baseDescuento, Double baseDescuentoDivisa,
                   Double importeIngresoSinImpuestos, Double importeIngresoSinImpuestosDivisa, Double importeIngresoConImpuestos,
                   Double importeIngresoConImpuestosDivisas, Integer dotacion, String tipoDocumento, Integer indiceDocumento,
                   Integer numeroLinea, Integer numeroOrden, Integer ordenDelDescuento, Integer numeroRango, String establecimiento,
                   String fechaDocumento, String divisaSouche, String divisaEstablecimiento,
                   String tipoDescuento, String codigoCondicionComercial, String codigoEstat) {
        super();
        this.motivoDescuento = motivoDescuento;
        this.porcentajeDescuento = porcentajeDescuento;
        this.valeUrrem = valeUrrem;
        this.baseDescuento = baseDescuento;
        this.baseDescuentoDivisa = baseDescuentoDivisa;
        this.importeIngresoSinImpuestos = importeIngresoSinImpuestos;
        this.importeIngresoSinImpuestosDivisa = importeIngresoSinImpuestosDivisa;
        this.importeIngresoConImpuestos = importeIngresoConImpuestos;
        this.importeIngresoConImpuestosDivisas = importeIngresoConImpuestosDivisas;
        this.dotacion = dotacion;
        this.tipoDocumento = tipoDocumento;
        this.indiceDocumento = indiceDocumento;
        this.numeroLinea = numeroLinea;
        this.numeroOrden = numeroOrden;
        this.ordenDelDescuento = ordenDelDescuento;
        this.numeroRango = numeroRango;
        this.establecimiento = establecimiento;
        this.fechaDocumento = fechaDocumento;
        this.divisaSouche = divisaSouche;
        this.divisaEstablecimiento = divisaEstablecimiento;
        this.tipoDescuento = tipoDescuento;
        this.codigoCondicionComercial = codigoCondicionComercial;
        this.codigoEstat = codigoEstat;
    }

    public String getMotivoDescuento() {
        return motivoDescuento;
    }

    public void setMotivoDescuento(String motivoDescuento) {
        this.motivoDescuento = motivoDescuento;
    }

    public Double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(Double porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public Integer getValeUrrem() {
        return valeUrrem;
    }

    public void setValeUrrem(Integer valeUrrem) {
        this.valeUrrem = valeUrrem;
    }

    public Double getBaseDescuento() {
        return baseDescuento;
    }

    public void setBaseDescuento(Double baseDescuento) {
        this.baseDescuento = baseDescuento;
    }

    public Double getBaseDescuentoDivisa() {
        return baseDescuentoDivisa;
    }

    public void setBaseDescuentoDivisa(Double baseDescuentoDivisa) {
        this.baseDescuentoDivisa = baseDescuentoDivisa;
    }

    public Double getImporteIngresoSinImpuestos() {
        return importeIngresoSinImpuestos;
    }

    public void setImporteIngresoSinImpuestos(Double importeIngresoSinImpuestos) {
        this.importeIngresoSinImpuestos = importeIngresoSinImpuestos;
    }

    public Double getImporteIngresoSinImpuestosDivisa() {
        return importeIngresoSinImpuestosDivisa;
    }

    public void setImporteIngresoSinImpuestosDivisa(Double importeIngresoSinImpuestosDivisa) {
        this.importeIngresoSinImpuestosDivisa = importeIngresoSinImpuestosDivisa;
    }

    public Double getImporteIngresoConImpuestos() {
        return importeIngresoConImpuestos;
    }

    public void setImporteIngresoConImpuestos(Double importeIngresoConImpuestos) {
        this.importeIngresoConImpuestos = importeIngresoConImpuestos;
    }

    public Double getImporteIngresoConImpuestosDivisas() {
        return importeIngresoConImpuestosDivisas;
    }

    public void setImporteIngresoConImpuestosDivisas(Double importeIngresoConImpuestosDivisas) {
        this.importeIngresoConImpuestosDivisas = importeIngresoConImpuestosDivisas;
    }

    public Integer getDotacion() {
        return dotacion;
    }

    public void setDotacion(Integer dotacion) {
        this.dotacion = dotacion;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Integer getIndiceDocumento() {
        return indiceDocumento;
    }

    public void setIndiceDocumento(Integer indiceDocumento) {
        this.indiceDocumento = indiceDocumento;
    }

    public Integer getNumeroLinea() {
        return numeroLinea;
    }

    public void setNumeroLinea(Integer numeroLinea) {
        this.numeroLinea = numeroLinea;
    }

    public Integer getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden(Integer numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public Integer getOrdenDelDescuento() {
        return ordenDelDescuento;
    }

    public void setOrdenDelDescuento(Integer ordenDelDescuento) {
        this.ordenDelDescuento = ordenDelDescuento;
    }

    public Integer getNumeroRango() {
        return numeroRango;
    }

    public void setNumeroRango(Integer numeroRango) {
        this.numeroRango = numeroRango;
    }

    public String getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(String establecimiento) {
        this.establecimiento = establecimiento;
    }

    public String getFechaDocumento() {
        return fechaDocumento;
    }

    public void setFechaDocumento(String fechaDocumento) {
        this.fechaDocumento = fechaDocumento;
    }

    public String getDivisaSouche() {
        return divisaSouche;
    }

    public void setDivisaSouche(String divisaSouche) {
        this.divisaSouche = divisaSouche;
    }

    public String getDivisaEstablecimiento() {
        return divisaEstablecimiento;
    }

    public void setDivisaEstablecimiento(String divisaEstablecimiento) {
        this.divisaEstablecimiento = divisaEstablecimiento;
    }

    public String getTipoDescuento() {
        return tipoDescuento;
    }

    public void setTipoDescuento(String tipoDescuento) {
        this.tipoDescuento = tipoDescuento;
    }

    public String getCodigoCondicionComercial() {
        return codigoCondicionComercial;
    }

    public void setCodigoCondicionComercial(String codigoCondicionComercial) {
        this.codigoCondicionComercial = codigoCondicionComercial;
    }

    public String getCodigoEstat() {
        return codigoEstat;
    }

    public void setCodigoEstat(String codigoEstat) {
        this.codigoEstat = codigoEstat;
    }
}
