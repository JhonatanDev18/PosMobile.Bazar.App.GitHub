
package com.crystal.bazarposmobile.retrofit.request.creardocumento;


import com.crystal.bazarposmobile.db.entity.DatafonoEntity;
import com.crystal.bazarposmobile.retrofit.response.bancolombiaqr.PagoBancolombiaQr;
import com.crystal.bazarposmobile.retrofit.response.mercadopago.MercadoPagoImporte;
import com.crystal.bazarposmobile.retrofit.response.tarjetasbancarias.Tarjeta;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Payment implements Serializable {

    @SerializedName("Nombre")
    @Expose
    private String name;

    @SerializedName("Amount")
    @Expose
    private Double amount;

    @SerializedName("CurrencyId")
    @Expose
    private String currencyId;

    @SerializedName("DueDate")
    @Expose
    private String dueDate;

    @SerializedName("Id")
    @Expose
    private Integer id;

    @SerializedName("IsReceivedPayment")
    @Expose
    private Integer isReceivedPayment;

    @SerializedName("MethodId")
    @Expose
    private String methodId;

    private boolean isTEF;
    private DatafonoEntity datafonoEntity;
    private String codigoTarjeta;
    private PagoBancolombiaQr respuestaQrBancolombia;
    private MercadoPagoImporte respuestaMercadoPago;
    private Tarjeta tajeta;
    private boolean eliminable;

    public Payment(String name, Double amount, String currencyId, String dueDate, Integer id, Integer isReceivedPayment, String methodId) {
        this.name = name;
        this.amount = amount;
        this.currencyId = currencyId;
        this.dueDate = dueDate;
        this.id = id;
        this.isReceivedPayment = isReceivedPayment;
        this.methodId = methodId;
        this.isTEF = false;
        this.eliminable = true;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIsReceivedPayment() {
        return isReceivedPayment;
    }

    public void setIsReceivedPayment(Integer isReceivedPayment) {
        this.isReceivedPayment = isReceivedPayment;
    }

    public String getMethodId() {
        return methodId;
    }

    public void setMethodId(String methodId) {
        this.methodId = methodId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsTEF() {
        return isTEF;
    }

    public void setTEF(boolean TEF) {
        isTEF = TEF;
    }

    public DatafonoEntity getDatafonoEntity() {
        return datafonoEntity;
    }

    public void setDatafonoEntity(DatafonoEntity datafonoEntity) {
        this.datafonoEntity = datafonoEntity;
    }

    public String getCodigoTarjeta() {
        return codigoTarjeta;
    }

    public void setCodigoTarjeta(String codigoTarjeta) {
        this.codigoTarjeta = codigoTarjeta;
    }

    public MercadoPagoImporte getRespuestaMercadoPago() {
        return respuestaMercadoPago;
    }

    public void setRespuestaMercadoPago(MercadoPagoImporte respuestaMercadoPago) {
        this.respuestaMercadoPago = respuestaMercadoPago;
    }

    public PagoBancolombiaQr getRespuestaQrBancolombia() {
        return respuestaQrBancolombia;
    }

    public void setRespuestaQrBancolombia(PagoBancolombiaQr respuestaBancolombiaQr) {
        this.respuestaQrBancolombia = respuestaBancolombiaQr;
    }

    public boolean isEliminable() {
        return eliminable;
    }

    public void setEliminable(boolean eliminable) {
        this.eliminable = eliminable;
    }

    public Tarjeta getTajeta() {
        return tajeta;
    }

    public void setTajeta(Tarjeta tajeta) {
        this.tajeta = tajeta;
    }
}
