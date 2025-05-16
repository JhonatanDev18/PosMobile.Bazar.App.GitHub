
package com.crystal.bazarposmobile.retrofit.request.mercadopago;

import com.google.gson.annotations.SerializedName;


public class RequestMPCrearOrden {

    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("collector_id")
    private String collectorId;
    @SerializedName("currency_id")
    private String currencyId;
    @SerializedName("external_id")
    private String externalId;
    @SerializedName("external_reference")
    private String externalReference;
    @SerializedName("unit_price")
    private Double unitPrice;

    public RequestMPCrearOrden(String accessToken, String collectorId, String currencyId,
                               String externalId, String externalReference, Double unitPrice) {
        this.accessToken = accessToken;
        this.collectorId = collectorId;
        this.currencyId = currencyId;
        this.externalId = externalId;
        this.externalReference = externalReference;
        this.unitPrice = unitPrice;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getCollectorId() {
        return collectorId;
    }

    public void setCollectorId(String collectorId) {
        this.collectorId = collectorId;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getExternalReference() {
        return externalReference;
    }

    public void setExternalReference(String externalReference) {
        this.externalReference = externalReference;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

}
