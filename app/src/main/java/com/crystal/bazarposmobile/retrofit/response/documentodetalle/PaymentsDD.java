package com.crystal.bazarposmobile.retrofit.response.documentodetalle;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class PaymentsDD implements Serializable {

    @Expose
    private String amount;
    @Expose
    private String code;
    @Expose
    private String currency;
    @Expose
    private String date;

    public PaymentsDD(String amount, String code, String currency, String date) {
        this.amount = amount;
        this.code = code;
        this.currency = currency;
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "PaymentsDD{" +
                "amount='" + amount + '\'' +
                ", code='" + code + '\'' +
                ", currency='" + currency + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
