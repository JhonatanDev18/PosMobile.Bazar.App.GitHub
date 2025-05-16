
package com.crystal.bazarposmobile.retrofit.request.mercadopago;

import com.google.gson.annotations.SerializedName;

public class RequestMPEliminarOrden {

    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("collector_id")
    private String collectorId;
    @SerializedName("external_id")
    private String externalId;

    public RequestMPEliminarOrden(String accessToken, String collectorId, String externalId) {
        this.accessToken = accessToken;
        this.collectorId = collectorId;
        this.externalId = externalId;
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

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

}
