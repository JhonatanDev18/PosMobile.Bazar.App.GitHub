package com.crystal.bazarposmobile.retrofit.response.documentodetalle;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class LineDD implements Serializable {

    @Expose
    private String deliveryDate;
    @Expose
    private String initialDeliveryDate;
    @Expose
    private String itemCode;
    @Expose
    private String itemId;
    @Expose
    private String itemReference;
    @Expose
    private String label;
    @Expose
    private String origin;
    @Expose
    private String quantity;
    @Expose
    private String taxExcludedNetUnitPrice;
    @Expose
    private String taxExcludedUnitPrice;
    @Expose
    private String taxIncludedNetUnitPrice;
    @Expose
    private String taxIncludedUnitPrice;
    @Expose
    private String warehouseId;

    private boolean entontrado = false;
    private String marca;
    private String tipoPrenda;
    private String genero;
    private String stock;

    public LineDD(String deliveryDate, String initialDeliveryDate, String itemCode, String itemId, String itemReference, String label, String origin, String quantity, String taxExcludedNetUnitPrice, String taxExcludedUnitPrice, String taxIncludedNetUnitPrice, String taxIncludedUnitPrice, String warehouseId) {
        this.deliveryDate = deliveryDate;
        this.initialDeliveryDate = initialDeliveryDate;
        this.itemCode = itemCode;
        this.itemId = itemId;
        this.itemReference = itemReference;
        this.label = label;
        this.origin = origin;
        this.quantity = quantity;
        this.taxExcludedNetUnitPrice = taxExcludedNetUnitPrice;
        this.taxExcludedUnitPrice = taxExcludedUnitPrice;
        this.taxIncludedNetUnitPrice = taxIncludedNetUnitPrice;
        this.taxIncludedUnitPrice = taxIncludedUnitPrice;
        this.warehouseId = warehouseId;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getInitialDeliveryDate() {
        return initialDeliveryDate;
    }

    public void setInitialDeliveryDate(String initialDeliveryDate) {
        this.initialDeliveryDate = initialDeliveryDate;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemReference() {
        return itemReference;
    }

    public void setItemReference(String itemReference) {
        this.itemReference = itemReference;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTaxExcludedNetUnitPrice() {
        return taxExcludedNetUnitPrice;
    }

    public void setTaxExcludedNetUnitPrice(String taxExcludedNetUnitPrice) {
        this.taxExcludedNetUnitPrice = taxExcludedNetUnitPrice;
    }

    public String getTaxExcludedUnitPrice() {
        return taxExcludedUnitPrice;
    }

    public void setTaxExcludedUnitPrice(String taxExcludedUnitPrice) {
        this.taxExcludedUnitPrice = taxExcludedUnitPrice;
    }

    public String getTaxIncludedNetUnitPrice() {
        return taxIncludedNetUnitPrice;
    }

    public void setTaxIncludedNetUnitPrice(String taxIncludedNetUnitPrice) {
        this.taxIncludedNetUnitPrice = taxIncludedNetUnitPrice;
    }

    public String getTaxIncludedUnitPrice() {
        return taxIncludedUnitPrice;
    }

    public void setTaxIncludedUnitPrice(String taxIncludedUnitPrice) {
        this.taxIncludedUnitPrice = taxIncludedUnitPrice;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public boolean isEntontrado() {
        return entontrado;
    }

    public void setEntontrado(boolean entontrado) {
        this.entontrado = entontrado;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getTipoPrenda() {
        return tipoPrenda;
    }

    public void setTipoPrenda(String tipoPrenda) {
        this.tipoPrenda = tipoPrenda;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "LineDD{" +
                "deliveryDate='" + deliveryDate + '\'' +
                ", initialDeliveryDate='" + initialDeliveryDate + '\'' +
                ", itemCode='" + itemCode + '\'' +
                ", itemId='" + itemId + '\'' +
                ", itemReference='" + itemReference + '\'' +
                ", label='" + label + '\'' +
                ", origin='" + origin + '\'' +
                ", quantity='" + quantity + '\'' +
                ", taxExcludedNetUnitPrice='" + taxExcludedNetUnitPrice + '\'' +
                ", taxExcludedUnitPrice='" + taxExcludedUnitPrice + '\'' +
                ", taxIncludedNetUnitPrice='" + taxIncludedNetUnitPrice + '\'' +
                ", taxIncludedUnitPrice='" + taxIncludedUnitPrice + '\'' +
                ", warehouseId='" + warehouseId + '\'' +
                ", entontrado=" + entontrado +
                ", marca='" + marca + '\'' +
                ", tipoPrenda='" + tipoPrenda + '\'' +
                ", genero='" + genero + '\'' +
                ", stock='" + stock + '\'' +
                '}';
    }
}
