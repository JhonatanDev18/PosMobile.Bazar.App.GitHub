package com.crystal.bazarposmobile.retrofit.response.bancolombiaqr;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PagoBancolombiaQr implements Serializable {

    @SerializedName("transferVoucher")
    private String transferVoucher;

    @SerializedName("transferAmount")
    private String transferAmount;

    @SerializedName("transferDescription")
    private String transferDescription;

    @SerializedName("transferState")
    private String transferState;

    @SerializedName("requestDate")
    private String requestDate;

    @SerializedName("transferDate")
    private String transferDate;

    @SerializedName("sign")
    private String sign;

    @SerializedName("transferReference")
    private String transferReference;

    @SerializedName("transferCodeResponse")
    private String transferCodeResponse;

    @SerializedName("transferDescriptionResponse")
    private String transferDescriptionResponse;

    @SerializedName("destinationAccountNumber")
    private String destinationAccountNumber;

    @SerializedName("clientInformation")
    private ClientInformation clientInformation;


    public String getTransferReference(){
        return transferReference;
    }

    public String getTransferVoucher(){
        return transferVoucher;
    }
}
