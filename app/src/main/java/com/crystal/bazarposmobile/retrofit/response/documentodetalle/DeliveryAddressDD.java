package com.crystal.bazarposmobile.retrofit.response.documentodetalle;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DeliveryAddressDD implements Serializable {

    @SerializedName("City")
    private String city;
    @SerializedName("ContactNumber")
    private String contactNumber;
    @SerializedName("CountryId")
    private String countryId;
    @SerializedName("CountryIdType")
    private String countryIdType;
    @SerializedName("FirstName")
    private String firstName;
    @SerializedName("LastName")
    private String lastName;
    @SerializedName("Line1")
    private String line1;
    @SerializedName("Line2")
    private String line2;
    @SerializedName("PhoneNumber")
    private String phoneNumber;
    @SerializedName("Region")
    private String region;
    @SerializedName("TitleId")
    private String titleId;
    @SerializedName("ZipCode")
    private String zipCode;

    public DeliveryAddressDD(String city, String contactNumber, String countryId, String countryIdType, String firstName, String lastName, String line1, String line2, String phoneNumber, String region, String titleId, String zipCode) {
        this.city = city;
        this.contactNumber = contactNumber;
        this.countryId = countryId;
        this.countryIdType = countryIdType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.line1 = line1;
        this.line2 = line2;
        this.phoneNumber = phoneNumber;
        this.region = region;
        this.titleId = titleId;
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountryIdType() {
        return countryIdType;
    }

    public void setCountryIdType(String countryIdType) {
        this.countryIdType = countryIdType;
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

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Override
    public String toString() {
        return "DeliveryAddressDD{" +
                "city='" + city + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", countryId='" + countryId + '\'' +
                ", countryIdType='" + countryIdType + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", line1='" + line1 + '\'' +
                ", line2='" + line2 + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", region='" + region + '\'' +
                ", titleId='" + titleId + '\'' +
                ", zipCode='" + zipCode + '\'' +
                '}';
    }
}
