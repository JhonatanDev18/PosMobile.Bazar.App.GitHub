
package com.crystal.bazarposmobile.retrofit.request.creardocumento;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Payments {

    @SerializedName("Payment")
    @Expose
    private List<Payment> payment;

    public Payments(List<Payment> payment) {
        this.payment = payment;
    }

    public List<Payment> getPayment() {
        return payment;
    }

    public void setPayment(List<Payment> payment) {
        this.payment = payment;
    }

}
