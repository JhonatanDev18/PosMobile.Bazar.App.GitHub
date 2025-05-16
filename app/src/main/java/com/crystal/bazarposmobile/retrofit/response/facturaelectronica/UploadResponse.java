package com.crystal.bazarposmobile.retrofit.response.facturaelectronica;

import com.google.gson.annotations.SerializedName;

public class UploadResponse {
    @SerializedName("cufe")
    private String cufe;

    @SerializedName("qr")
    private String qr;

    @SerializedName("status")
    private String status;

    @SerializedName("statusCode")
    private String statusCode;

    @SerializedName("legalStatus")
    private String legalStatus;

    @SerializedName("governmentResponseCode")
    private String governmentResponseCode;

    @SerializedName("governmentResponseDescripcion")
    private String governmentResponseDescripcion;

    @SerializedName("transactionId")
    private String transactionId;

    public UploadResponse(String cufe, String qr, String status, String statusCode,
                          String legalStatus, String governmentResponseCode, String governmentResponseDescripcion,
                          String transactionId) {
        this.cufe = cufe;
        this.qr = qr;
        this.status = status;
        this.statusCode = statusCode;
        this.legalStatus = legalStatus;
        this.governmentResponseCode = governmentResponseCode;
        this.governmentResponseDescripcion = governmentResponseDescripcion;
        this.transactionId = transactionId;
    }

    public String getCufe() {
        return cufe;
    }

    public void setCufe(String cufe) {
        this.cufe = cufe;
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getLegalStatus() {
        return legalStatus;
    }

    public void setLegalStatus(String legalStatus) {
        this.legalStatus = legalStatus;
    }

    public String getGovernmentResponseCode() {
        return governmentResponseCode;
    }

    public void setGovernmentResponseCode(String governmentResponseCode) {
        this.governmentResponseCode = governmentResponseCode;
    }

    public String getGovernmentResponseDescripcion() {
        return governmentResponseDescripcion;
    }

    public void setGovernmentResponseDescripcion(String governmentResponseDescripcion) {
        this.governmentResponseDescripcion = governmentResponseDescripcion;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return "UploadResponse{" +
                "cufe='" + cufe + '\'' +
                ", qr='" + qr + '\'' +
                ", status='" + status + '\'' +
                ", statusCode='" + statusCode + '\'' +
                ", legalStatus='" + legalStatus + '\'' +
                ", governmentResponseCode='" + governmentResponseCode + '\'' +
                ", governmentResponseDescripcion='" + governmentResponseDescripcion + '\'' +
                ", transactionId='" + transactionId + '\'' +
                '}';
    }
}
