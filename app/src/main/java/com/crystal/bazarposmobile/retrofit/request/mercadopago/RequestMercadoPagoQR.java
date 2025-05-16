
package com.crystal.bazarposmobile.retrofit.request.mercadopago;

import com.google.gson.annotations.SerializedName;


public class RequestMercadoPagoQR {

    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("external_id")
    private String externalId;

    public RequestMercadoPagoQR(String accessToken, String externalId) {
        this.accessToken = accessToken;
        this.externalId = externalId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

}
