package com.crystal.bazarposmobile.retrofit.response.bancolombiaqr;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ClientInformation implements Serializable {
    @SerializedName("name")
    private String name;

    @SerializedName("payerReference")
    private String payerReference;

    @SerializedName("accountNumber")
    private String accountNumber;
}
