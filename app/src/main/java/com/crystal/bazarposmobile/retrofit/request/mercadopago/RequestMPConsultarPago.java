
package com.crystal.bazarposmobile.retrofit.request.mercadopago;


import com.google.gson.annotations.SerializedName;

public class RequestMPConsultarPago {

    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("external_reference")
    private String externalReference;
    @SerializedName("external_id")
    private String externalId;

    public RequestMPConsultarPago(String accessToken, String externalReference, String externalId) {
        this.accessToken = accessToken;
        this.externalReference = externalReference;
        this.externalId = externalId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getExternalReference() {
        return externalReference;
    }

    public void setExternalReference(String externalReference) {
        this.externalReference = externalReference;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
}
