package com.crystal.bazarposmobile.retrofit.response.facturaelectronica;

import com.google.gson.annotations.SerializedName;

public class RespuestaGenerarQRFE {
    @SerializedName("uploadResponse")
    private UploadResponse uploadResponse;

    public RespuestaGenerarQRFE(UploadResponse uploadResponse) {
        this.uploadResponse = uploadResponse;
    }

    public UploadResponse getUploadResponse() {
        return uploadResponse;
    }

    public void setUploadResponse(UploadResponse uploadResponse) {
        this.uploadResponse = uploadResponse;
    }
}
