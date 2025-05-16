package com.crystal.bazarposmobile.retrofit.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseParametros {

    @SerializedName("error")
    @Expose
    private Boolean error;

    @SerializedName("mensaje")
    @Expose
    private String mensaje;

    @SerializedName("FechaHora")
    @Expose
    private String fechaHora;

    @SerializedName("ZPOSM:TIQUETE:CONTRIBUYENTE")
    @Expose
    private String contribuyentesText;

    @SerializedName("ZPOSM:TIQUETE:PLAZOCAMBIO")
    @Expose
    private String plazosText;

    @SerializedName("ZPOSM:TIQUETE:POLITICASCAMBIO")
    @Expose
    private String politicasCambiosText;

    @SerializedName("ZPOSM:TIQUETE:LINEAATENCION")
    @Expose
    private String lineasAtencionText;

    @SerializedName("ZPOSM:TEF:CREDIBANCOPAGARE")
    @Expose
    private String pagareText;

    @SerializedName("ZPOSM:TIQUETE:TEXTOIVA")
    @Expose
    private String tarifaIVAtext;

    @SerializedName("ZPOSM:GRUPOSROL:CAJERO")
    @Expose
    private String usuarioCajero;

    @SerializedName("ZPOSM:GRUPOSROL:CONFIGURADOR")
    @Expose
    private String usuarioConfigurador;

    @SerializedName("ZPOSM:GRUPOSROL:ADMINISTRADOR")
    @Expose
    private String usuarioAdministrador;

    @SerializedName("ZPOSM:CLIENTE:GENERICO")
    @Expose
    private String clienteGenerico;

    @SerializedName("WCONSENT:PoliticaDatos")
    @Expose
    private String politicaDatosText;

    @SerializedName("ZPOSM:MEDIOPAGOEFECTIVO")
    @Expose
    private String medioPagoEfectivoCaja;

    @SerializedName("ZPOSM:MEDIOPAGOTEFCONTINGENCIA")
    @Expose
    private String medioPagoTefContinguencia;

    @SerializedName("ZPOSM:BAZAR:EANES")
    @Expose
    private String eanesBazar;

    @SerializedName("ZPOSM:BAZAR:AUTORIZADEV")
    @Expose
    private String codigoAutDevo;

    @SerializedName("ZPOSM:MEDIOPAGO:QRBANCOLOMBIA")
    @Expose
    private String medioPagoQrBancolombia;

    @SerializedName("ZPOSM:FE:URLBASE:QR")
    @Expose
    private String urlBaseFacturaElectronicaQR;

    @SerializedName("ZPOSM:FE:PROVEEDORTEC")
    @Expose
    private String proveedorFacturaElectronica;

    @SerializedName("FACT:ELECTRO:AMBIENTE")
    @Expose
    private String ambienteFacturaElectronica;

    @SerializedName("ZPOSM:BAZAR:DATAFONOS:CREDIBAN")
    @Expose
    private String datafonosCredibanCo;

    @SerializedName("ZPOSM:BAZAR:DATAFONOS:REDEBAN")
    @Expose
    private String datafonosRedeban;

    @SerializedName("ZPOSM:DF:TIMEOUT")
    @Expose
    private String timeoutDatafono;

    @SerializedName("ZPOSM:DF:TIMEOUT:ES")
    @Expose
    private String timeoutDatafonoEspera;

    @SerializedName("ZPOSM:MEDIOSPAGO:CREDITOS")
    @Expose
    private String mediosPagosCreditos;

    @SerializedName("WCUPO:TEMPORALES")
    @Expose

    private String empresaTemporales;

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

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getContribuyentesText() {
        return contribuyentesText;
    }

    public void setContribuyentesText(String contribuyentesText) {
        this.contribuyentesText = contribuyentesText;
    }

    public String getPlazosText() {
        return plazosText;
    }

    public void setPlazosText(String plazosText) {
        this.plazosText = plazosText;
    }

    public String getPoliticasCambiosText() {
        return politicasCambiosText;
    }

    public void setPoliticasCambiosText(String politicasCambiosText) {
        this.politicasCambiosText = politicasCambiosText;
    }

    public String getLineasAtencionText() {
        return lineasAtencionText;
    }

    public void setLineasAtencionText(String lineasAtencionText) {
        this.lineasAtencionText = lineasAtencionText;
    }

    public String getPagareText() {
        return pagareText;
    }

    public void setPagareText(String pagareText) {
        this.pagareText = pagareText;
    }

    public String getTarifaIVAtext() {
        return tarifaIVAtext;
    }

    public void setTarifaIVAtext(String tarifaIVAtext) {
        this.tarifaIVAtext = tarifaIVAtext;
    }

    public String getUsuarioCajero() {
        return usuarioCajero;
    }

    public void setUsuarioCajero(String usuarioCajero) {
        this.usuarioCajero = usuarioCajero;
    }

    public String getUsuarioConfigurador() {
        return usuarioConfigurador;
    }

    public void setUsuarioConfigurador(String usuarioConfigurador) {
        this.usuarioConfigurador = usuarioConfigurador;
    }

    public String getUsuarioAdministrador() {
        return usuarioAdministrador;
    }

    public void setUsuarioAdministrador(String usuarioAdministrador) {
        this.usuarioAdministrador = usuarioAdministrador;
    }

    public String getClienteGenerico() {
        return clienteGenerico;
    }

    public void setClienteGenerico(String clienteGenerico) {
        this.clienteGenerico = clienteGenerico;
    }

    public String getPoliticaDatosText() {
        return politicaDatosText;
    }

    public void setPoliticaDatosText(String politicaDatosText) {
        this.politicaDatosText = politicaDatosText;
    }

    public String getMedioPagoEfectivoCaja() {
        return medioPagoEfectivoCaja;
    }

    public void setMedioPagoEfectivoCaja(String medioPagoEfectivoCaja) {
        this.medioPagoEfectivoCaja = medioPagoEfectivoCaja;
    }

    public String getMedioPagoTefContinguencia() {
        return medioPagoTefContinguencia;
    }

    public void setMedioPagoTefContinguencia(String medioPagoTefContinguencia) {
        this.medioPagoTefContinguencia = medioPagoTefContinguencia;
    }

    public String getEanesBazar() {
        return eanesBazar;
    }

    public void setEanesBazar(String eanesBazar) {
        this.eanesBazar = eanesBazar;
    }

    public String getCodigoAutDevo() {
        return codigoAutDevo;
    }

    public void setCodigoAutDevo(String codigoAutDevo) {
        this.codigoAutDevo = codigoAutDevo;
    }

    public String getMedioPagoQrBancolombia() {
        return medioPagoQrBancolombia;
    }

    public void setMedioPagoQrBancolombia(String medioPagoQrBancolombia) {
        this.medioPagoQrBancolombia = medioPagoQrBancolombia;
    }

    public String getUrlBaseFacturaElectronicaQR() {
        return urlBaseFacturaElectronicaQR;
    }

    public String getProveedorFacturaElectronica() {
        return proveedorFacturaElectronica;
    }

    public String getAmbienteFacturaElectronica() {
        return ambienteFacturaElectronica;
    }

    public String getDatafonosCredibanCo() {
        return datafonosCredibanCo;
    }

    public String getDatafonosRedeban() {
        return datafonosRedeban;
    }

    public String getTimeoutDatafono() {
        return timeoutDatafono;
    }

    public String getTimeoutDatafonoEspera() {
        return timeoutDatafonoEspera;
    }

    public String getMediosPagosCreditos() {
        return mediosPagosCreditos;
    }

    public String getEmpresaTemporales() {
        return empresaTemporales;
    }
}