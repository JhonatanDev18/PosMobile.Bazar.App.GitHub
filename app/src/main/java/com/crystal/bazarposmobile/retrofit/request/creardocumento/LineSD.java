
package com.crystal.bazarposmobile.retrofit.request.creardocumento;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class LineSD {

    @SerializedName("NetUnitPrice")
    @Expose
    private String netUnitPrice;

    @SerializedName("Quantity")
    @Expose
    private String quantity;

    @SerializedName("Reference")
    @Expose
    private String reference;

    @SerializedName("SalesPersonId")
    @Expose
    private String salesPersonId;

    @SerializedName("UnitPrice")
    @Expose
    private String unitPrice;

    @SerializedName("DiscountTypeId")
    @Expose
    private String discountTypeId;

    @SerializedName("SerialNumberId")
    @Expose
    private String serialNumberId;

    @SerializedName("MovementReasonId")
    @Expose
    private String movementReasonId;

    public LineSD(String netUnitPrice, String quantity, String reference, String salesPersonId, String unitPrice, String discountTypeId, String serialNumberId, String movementReasonId) {
        this.netUnitPrice = netUnitPrice;
        this.quantity = quantity;
        this.reference = reference;
        this.salesPersonId = salesPersonId;
        this.unitPrice = unitPrice;
        this.discountTypeId = discountTypeId;
        this.serialNumberId = serialNumberId;
        this.movementReasonId = movementReasonId;
    }

    public String getNetUnitPrice() {
        return netUnitPrice;
    }

    public void setNetUnitPrice(String netUnitPrice) {
        this.netUnitPrice = netUnitPrice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getSalesPersonId() {
        return salesPersonId;
    }

    public void setSalesPersonId(String salesPersonId) {
        this.salesPersonId = salesPersonId;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }


    public String getDiscountTypeId() {
        return discountTypeId;
    }

    public void setDiscountTypeId(String discountTypeId) {
        this.discountTypeId = discountTypeId;
    }

    public String getSerialNumberId() {
        return serialNumberId;
    }

    public void setSerialNumberId(String serialNumberId) {
        this.serialNumberId = serialNumberId;
    }

    public String getMovementReasonId() {
        return movementReasonId;
    }

    public void setMovementReasonId(String movementReasonId) {
        this.movementReasonId = movementReasonId;
    }
}
