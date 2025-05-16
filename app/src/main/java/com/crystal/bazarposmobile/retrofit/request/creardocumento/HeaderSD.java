package com.crystal.bazarposmobile.retrofit.request.creardocumento;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HeaderSD {

    @SerializedName("Active")
    @Expose
    private String active;

    @SerializedName("CurrencyId")
    @Expose
    private String currencyId;

    @SerializedName("CustomerId")
    @Expose
    private String customerId;

    @SerializedName("Date")
    @Expose
    private String date;

    @SerializedName("InternalReference")
    @Expose
    private String internalReference;

    @SerializedName("SalesPersonId")
    @Expose
    private String salesPersonId;

    @SerializedName("StoreId")
    @Expose
    private String storeId;

    @SerializedName("TaxExcluded")
    @Expose
    private String taxExcluded;

    @SerializedName("Type")
    @Expose
    private String type;

    @SerializedName("WarehouseId")
    @Expose
    private String warehouseId;

    @SerializedName("FollowedReference")
    @Expose
    private String followedReference;

    @SerializedName("UserField13")
    @Expose
    private String userF13;

    public HeaderSD(String active, String currencyId, String customerId, String date, String internalReference, String salesPersonId, String storeId, String taxExcluded, String type, String warehouseId, String followedReference) {
        this.active = active;
        this.currencyId = currencyId;
        this.customerId = customerId;
        this.date = date;
        this.internalReference = internalReference;
        this.salesPersonId = salesPersonId;
        this.storeId = storeId;
        this.taxExcluded = taxExcluded;
        this.type = type;
        this.warehouseId = warehouseId;
        this.followedReference = followedReference;
    }

    public HeaderSD(String active, String currencyId, String customerId, String date, String internalReference, String salesPersonId, String storeId, String taxExcluded, String type, String warehouseId, String followedReference, String userF13) {
        this.active = active;
        this.currencyId = currencyId;
        this.customerId = customerId;
        this.date = date;
        this.internalReference = internalReference;
        this.salesPersonId = salesPersonId;
        this.storeId = storeId;
        this.taxExcluded = taxExcluded;
        this.type = type;
        this.warehouseId = warehouseId;
        this.followedReference = followedReference;
        this.userF13 = userF13;
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

    public String getSalesPersonId() {
        return salesPersonId;
    }

    public void setSalesPersonId(String salesPersonId) {
        this.salesPersonId = salesPersonId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getTaxExcluded() {
        return taxExcluded;
    }

    public void setTaxExcluded(String taxExcluded) {
        this.taxExcluded = taxExcluded;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getFollowedReference() {
        return followedReference;
    }

    public void setFollowedReference(String followedReference) {
        this.followedReference = followedReference;
    }

    public String getUserF13() {
        return userF13;
    }

    public void setUserF13(String userF13) {
        this.userF13 = userF13;
    }
}
