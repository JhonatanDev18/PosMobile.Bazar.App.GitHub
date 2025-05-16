package com.crystal.bazarposmobile.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.crystal.bazarposmobile.common.Constantes;

@Entity(tableName = Constantes.TABLA_DOCUMENTO_PAYMENT,
        primaryKeys = {"internalReference","id"} )
public class DocPaymentEntity {

    @NonNull
    private String internalReference;

    private String name;

    private Double amount;

    private String currencyId;

    private String dueDate;

    @NonNull
    private Integer id;

    private Integer isReceivedPayment;

    private String methodId;

    public DocPaymentEntity(@NonNull String internalReference, String name, Double amount, String currencyId, String dueDate, @NonNull Integer id, Integer isReceivedPayment, String methodId) {
        this.internalReference = internalReference;
        this.name = name;
        this.amount = amount;
        this.currencyId = currencyId;
        this.dueDate = dueDate;
        this.id = id;
        this.isReceivedPayment = isReceivedPayment;
        this.methodId = methodId;
    }

    @NonNull
    public String getInternalReference() {
        return internalReference;
    }

    public void setInternalReference(@NonNull String internalReference) {
        this.internalReference = internalReference;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    @NonNull
    public Integer getId() {
        return id;
    }

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    public Integer getIsReceivedPayment() {
        return isReceivedPayment;
    }

    public void setIsReceivedPayment(Integer isReceivedPayment) {
        this.isReceivedPayment = isReceivedPayment;
    }

    public String getMethodId() {
        return methodId;
    }

    public void setMethodId(String methodId) {
        this.methodId = methodId;
    }

    @Override
    public String toString() {
        return "Payment{" +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", currencyId='" + currencyId + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", id=" + id +
                ", isReceivedPayment=" + isReceivedPayment +
                ", methodId='" + methodId + '\'' +
                '}';
    }
}
