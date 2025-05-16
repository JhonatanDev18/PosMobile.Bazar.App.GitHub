package com.crystal.bazarposmobile.retrofit.request.creardocumento;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeliveryAddressSD {

    @SerializedName("City")
    @Expose
    private String city;

    @SerializedName("CountryId")
    @Expose
    private String countryId;

    @SerializedName("FirstName")
    @Expose
    private String firstName;

    @SerializedName("LastName")
    @Expose
    private String lastName;

    @SerializedName("Line1")
    @Expose
    private String line1;

    @SerializedName("Line2")
    @Expose
    private String line2;

    @SerializedName("PhoneNumber")
    @Expose
    private String phoneNumber;

    @SerializedName("Region")
    @Expose
    private String region;

    @SerializedName("ZipCode")
    @Expose
    private String zipCode;

    public DeliveryAddressSD(String city, String countryId, String firstName, String lastName, String line1, String line2, String phoneNumber, String region, String zipCode) {
        this.city = city;
        this.countryId = countryId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.line1 = line1;
        this.line2 = line2;
        this.phoneNumber = phoneNumber;
        this.region = region;
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
