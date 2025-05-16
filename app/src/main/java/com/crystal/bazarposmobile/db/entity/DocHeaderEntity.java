package com.crystal.bazarposmobile.db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.crystal.bazarposmobile.common.Constantes;

@Entity(tableName = Constantes.TABLA_DOCUMENTO_HEADER)
public class  DocHeaderEntity {

    private String active;

    private String currencyId;

    private String customerId;

    private String date;

    @NonNull
    @PrimaryKey
    private String internalReference;

    private String salesPersonId;

    private String storeId;

    private String taxExcluded;

    private String type;

    private String warehouseId;

    private String followedReference;

    private Double total;

    private Double iva;

    private Integer cantidad;

    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    private String fechahora;

    public DocHeaderEntity(String active, String currencyId, String customerId, String date, @NonNull String internalReference, String salesPersonId, String storeId, String taxExcluded, String type, String warehouseId, String followedReference, Double total, Double iva, Integer cantidad) {
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
        this.total = total;
        this.iva = iva;
        this.cantidad = cantidad;
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

    @NonNull
    public String getInternalReference() {
        return internalReference;
    }

    public void setInternalReference(@NonNull String internalReference) {
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

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getIva() {
        return iva;
    }

    public void setIva(Double iva) {
        this.iva = iva;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getFechahora() {
        return fechahora;
    }

    public void setFechahora(String fechahora) {
        this.fechahora = fechahora;
    }

    @Override
    public String toString() {
        return "Header{" +
                "active='" + active + '\'' +
                ", currencyId='" + currencyId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", date='" + date + '\'' +
                ", internalReference='" + internalReference + '\'' +
                ", salesPersonId='" + salesPersonId + '\'' +
                ", storeId='" + storeId + '\'' +
                ", taxExcluded='" + taxExcluded + '\'' +
                ", type='" + type + '\'' +
                ", warehouseId='" + warehouseId + '\'' +
                ", followedReference='" + followedReference + '\'' +
                ", total=" + total +
                ", iva=" + iva +
                ", cantidad=" + cantidad +
                '}';
    }
}
