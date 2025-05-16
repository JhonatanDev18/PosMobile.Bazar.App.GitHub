
package com.crystal.bazarposmobile.retrofit.request.creardocumento;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestSaleDocumentCreate {


    @SerializedName("DeliveryAddressSD")
    @Expose
    private DeliveryAddressSD deliveryAddressSD;

    @SerializedName("HeaderSD")
    @Expose
    private HeaderSD headerSD;

    @SerializedName("LinesSD")
    @Expose
    private LinesSD linesSD;

    @SerializedName("Payments")
    @Expose
    private Payments payments;

    @SerializedName("Caja")
    @Expose
    private String caja;

    public RequestSaleDocumentCreate(DeliveryAddressSD deliveryAddressSD, HeaderSD headerSD, LinesSD linesSD, Payments payments, String caja) {
        this.deliveryAddressSD = deliveryAddressSD;
        this.headerSD = headerSD;
        this.linesSD = linesSD;
        this.payments = payments;
        this.caja = caja;
    }

    public DeliveryAddressSD getDeliveryAddressSD() {
        return deliveryAddressSD;
    }

    public void setDeliveryAddressSD(DeliveryAddressSD deliveryAddressSD) {
        this.deliveryAddressSD = deliveryAddressSD;
    }

    public HeaderSD getHeaderSD() {
        return headerSD;
    }

    public void setHeaderSD(HeaderSD headerSD) {
        this.headerSD = headerSD;
    }

    public LinesSD getLinesSD() {
        return linesSD;
    }

    public void setLinesSD(LinesSD linesSD) {
        this.linesSD = linesSD;
    }

    public Payments getPayments() {
        return payments;
    }

    public void setPayments(Payments payments) {
        this.payments = payments;
    }

    public String getCaja() {
        return caja;
    }

    public void setCaja(String caja) {
        this.caja = caja;
    }
}
