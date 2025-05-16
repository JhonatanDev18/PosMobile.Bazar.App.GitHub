package com.crystal.bazarposmobile.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.crystal.bazarposmobile.common.Constantes;

@Entity(tableName = Constantes.TABLA_DOCUMENTO_LINE)
public class DocLineEntity {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String internalReference;

    private String netUnitPrice;

    private String quantity;

    private String reference;

    private String salesPersonId;

    private String unitPrice;

    private String discountTypeId;

    private String serialNumberId;

    private String movementReasonId;

    public DocLineEntity(String internalReference, String netUnitPrice, String quantity, String reference, String salesPersonId, String unitPrice, String discountTypeId, String serialNumberId, String movementReasonId) {
        this.internalReference = internalReference;
        this.netUnitPrice = netUnitPrice;
        this.quantity = quantity;
        this.reference = reference;
        this.salesPersonId = salesPersonId;
        this.unitPrice = unitPrice;
        this.discountTypeId = discountTypeId;
        this.serialNumberId = serialNumberId;
        this.movementReasonId = movementReasonId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInternalReference() {
        return internalReference;
    }

    public void setInternalReference(String internalReference) {
        this.internalReference = internalReference;
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

    @Override
    public String toString() {
        return "Line{" +
                ", netUnitPrice='" + netUnitPrice + '\'' +
                ", quantity='" + quantity + '\'' +
                ", reference='" + reference + '\'' +
                ", salesPersonId='" + salesPersonId + '\'' +
                ", unitPrice='" + unitPrice + '\'' +
                ", discountTypeId='" + discountTypeId + '\'' +
                ", serialNumberId='" + serialNumberId + '\'' +
                ", movementReasonId='" + movementReasonId + '\'' +
                '}';
    }
}
