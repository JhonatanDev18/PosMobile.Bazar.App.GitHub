package com.crystal.bazarposmobile.retrofit.response.documentodetalle;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class DocumentoDetalle implements Serializable {

    @SerializedName("DeliveryAddress")
    private DeliveryAddressDD deliveryAddressDD;
    @SerializedName("Header")
    private HeaderDD headerDD;
    @SerializedName("Lines")
    private List<LineDD> lineDDS;
    @SerializedName("Payments")
    private List<PaymentsDD> paymentsDDS;

    public DocumentoDetalle(DeliveryAddressDD deliveryAddressDD, HeaderDD headerDD, List<LineDD> lineDDS, List<PaymentsDD> paymentsDDS) {
        this.deliveryAddressDD = deliveryAddressDD;
        this.headerDD = headerDD;
        this.lineDDS = lineDDS;
        this.paymentsDDS = paymentsDDS;
    }

    public DeliveryAddressDD getDeliveryAddressDD() {
        return deliveryAddressDD;
    }

    public void setDeliveryAddressDD(DeliveryAddressDD deliveryAddressDD) {
        this.deliveryAddressDD = deliveryAddressDD;
    }

    public HeaderDD getHeaderDD() {
        return headerDD;
    }

    public void setHeaderDD(HeaderDD headerDD) {
        this.headerDD = headerDD;
    }

    public List<LineDD> getLineDDS() {
        return lineDDS;
    }

    public void setLineDDS(List<LineDD> lineDDS) {
        this.lineDDS = lineDDS;
    }

    public List<PaymentsDD> getPaymentsDDS() {
        return paymentsDDS;
    }

    public void setPaymentsDDS(List<PaymentsDD> paymentsDDS) {
        this.paymentsDDS = paymentsDDS;
    }

    @Override
    public String toString() {
        return "DocumentoDetalle{" +
                "deliveryAddressDD=" + deliveryAddressDD +
                ", headerDD=" + headerDD +
                ", lineDDS=" + lineDDS +
                ", paymentsDDS=" + paymentsDDS +
                '}';
    }
}
