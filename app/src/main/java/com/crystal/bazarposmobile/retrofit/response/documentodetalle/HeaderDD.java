package com.crystal.bazarposmobile.retrofit.response.documentodetalle;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class HeaderDD implements Serializable {

    @SerializedName("Active")
    private String active;
    @SerializedName("CurrencyId")
    private String currencyId;
    @SerializedName("CustomerId")
    private String customerId;
    @SerializedName("Date")
    private String date;
    @SerializedName("InternalReference")
    private String internalReference;
    @SerializedName("Origin")
    private String origin;
    @SerializedName("StoreId")
    private String storeId;
    @SerializedName("Number")
    private String number;
    @SerializedName("TaxExcludedTotalAmount")
    private String taxExcludedTotalAmount;
    @SerializedName("TaxIncludedTotalAmount")
    private String taxIncludedTotalAmount;
    @SerializedName("TotalQuantity")
    private String totalQuantity;

    public HeaderDD(String active, String currencyId, String customerId, String date, String internalReference, String origin, String storeId, String number, String taxExcludedTotalAmount, String taxIncludedTotalAmount, String totalQuantity) {
        this.active = active;
        this.currencyId = currencyId;
        this.customerId = customerId;
        this.date = date;
        this.internalReference = internalReference;
        this.origin = origin;
        this.storeId = storeId;
        this.number = number;
        this.taxExcludedTotalAmount = taxExcludedTotalAmount;
        this.taxIncludedTotalAmount = taxIncludedTotalAmount;
        this.totalQuantity = totalQuantity;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInternalReference() {
        return internalReference;
    }

    public void setInternalReference(String internalReference) {
        this.internalReference = internalReference;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTaxExcludedTotalAmount() {
        return taxExcludedTotalAmount;
    }

    public void setTaxExcludedTotalAmount(String taxExcludedTotalAmount) {
        this.taxExcludedTotalAmount = taxExcludedTotalAmount;
    }

    public String getTaxIncludedTotalAmount() {
        return taxIncludedTotalAmount;
    }

    public void setTaxIncludedTotalAmount(String taxIncludedTotalAmount) {
        this.taxIncludedTotalAmount = taxIncludedTotalAmount;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    @Override
    public String toString() {
        return "HeaderDD{" +
                "active='" + active + '\'' +
                ", currencyId='" + currencyId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", date='" + date + '\'' +
                ", internalReference='" + internalReference + '\'' +
                ", origin='" + origin + '\'' +
                ", storeId='" + storeId + '\'' +
                ", number='" + number + '\'' +
                ", taxExcludedTotalAmount='" + taxExcludedTotalAmount + '\'' +
                ", taxIncludedTotalAmount='" + taxIncludedTotalAmount + '\'' +
                ", totalQuantity='" + totalQuantity + '\'' +
                '}';
    }
}
