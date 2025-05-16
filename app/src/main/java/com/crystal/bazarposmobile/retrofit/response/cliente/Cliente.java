package com.crystal.bazarposmobile.retrofit.response.cliente;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Cliente implements Serializable {

    @SerializedName("FirstName")
    @Expose
    private String firstName;

    @SerializedName("LastName")
    @Expose
    private String lastName;

    @SerializedName("Sexo")
    @Expose
    private String sexo;

    @SerializedName("CellularPhoneNumber")
    @Expose
    private String celular;

    @SerializedName("Email")
    @Expose
    private String email;

    @SerializedName("Tipo")
    @Expose
    private String tipo;

    @SerializedName("SegmentoGEF")
    @Expose
    private String segmentoGEF;

    @SerializedName("SegmentoPB")
    @Expose
    private String segmentoPB;

    @SerializedName("SegmentoBF")
    @Expose
    private String segmentoBF;

    @SerializedName("SegmentoGX")
    @Expose
    private String segmentoGX;

    @SerializedName("CustomerId")
    @Expose
    private String customerId;

    @SerializedName("Empresa")
    @Expose
    private String empresa;

    @SerializedName("Estado31")
    @Expose
    private String estado31;

    @SerializedName("Medio32")
    @Expose
    private String medio32;

    @SerializedName("Adjunto33")
    @Expose
    private String adjunto33;

    @SerializedName("Parfois35")
    @Expose
    private String parfois35;

    @SerializedName("BirthDateDay")
    @Expose
    private Integer birthDateDay;

    @SerializedName("BirthDateMonth")
    @Expose
    private Integer birthDateMonth;

    @SerializedName("BirthDateYear")
    @Expose
    private Integer birthDateYear;

    @SerializedName("AlternateLastName")
    @Expose
    private String tipoDocumento;

    @SerializedName("PassportNumber")
    @Expose
    private String documento;

    @SerializedName("AddressLine1")
    @Expose
    private String addressLine1;

    @SerializedName("AddressLine2")
    @Expose
    private String addressLine2;

    @SerializedName("City")
    @Expose
    private String city;

    @SerializedName("CountryId")
    @Expose
    private String countryId;

    @SerializedName("RegionId")
    @Expose
    private String regionId;

    @SerializedName("ZipCode")
    @Expose
    private String zipCode;

    public Cliente(String firstName, String lastName, String sexo, String celular, String email, String tipo, String segmentoGEF, String segmentoPB, String segmentoBF, String segmentoGX, String customerId, String empresa, String estado31, String medio32, String adjunto33, String parfois35, Integer birthDateDay, Integer birthDateMonth, Integer birthDateYear, String tipoDocumento, String documento, String addressLine1, String addressLine2, String city, String countryId, String regionId, String zipCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.sexo = sexo;
        this.celular = celular;
        this.email = email;
        this.tipo = tipo;
        this.segmentoGEF = segmentoGEF;
        this.segmentoPB = segmentoPB;
        this.segmentoBF = segmentoBF;
        this.segmentoGX = segmentoGX;
        this.customerId = customerId;
        this.empresa = empresa;
        this.estado31 = estado31;
        this.medio32 = medio32;
        this.adjunto33 = adjunto33;
        this.parfois35 = parfois35;
        this.birthDateDay = birthDateDay;
        this.birthDateMonth = birthDateMonth;
        this.birthDateYear = birthDateYear;
        this.tipoDocumento = tipoDocumento;
        this.documento = documento;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.countryId = countryId;
        this.regionId = regionId;
        this.zipCode = zipCode;
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

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getSegmentoGEF() {
        return segmentoGEF;
    }

    public void setSegmentoGEF(String segmentoGEF) {
        this.segmentoGEF = segmentoGEF;
    }

    public String getSegmentoPB() {
        return segmentoPB;
    }

    public void setSegmentoPB(String segmentoPB) {
        this.segmentoPB = segmentoPB;
    }

    public String getSegmentoBF() {
        return segmentoBF;
    }

    public void setSegmentoBF(String segmentoBF) {
        this.segmentoBF = segmentoBF;
    }

    public String getSegmentoGX() {
        return segmentoGX;
    }

    public void setSegmentoGX(String segmentoGX) {
        this.segmentoGX = segmentoGX;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getEstado31() {
        return estado31;
    }

    public void setEstado31(String estado31) {
        this.estado31 = estado31;
    }

    public String getMedio32() {
        return medio32;
    }

    public void setMedio32(String medio32) {
        this.medio32 = medio32;
    }

    public String getAdjunto33() {
        return adjunto33;
    }

    public void setAdjunto33(String adjunto33) {
        this.adjunto33 = adjunto33;
    }

    public String getParfois35() {
        return parfois35;
    }

    public void setParfois35(String parfois35) {
        this.parfois35 = parfois35;
    }

    public Integer getBirthDateDay() {
        return birthDateDay;
    }

    public void setBirthDateDay(Integer birthDateDay) {
        this.birthDateDay = birthDateDay;
    }

    public Integer getBirthDateMonth() {
        return birthDateMonth;
    }

    public void setBirthDateMonth(Integer birthDateMonth) {
        this.birthDateMonth = birthDateMonth;
    }

    public Integer getBirthDateYear() {
        return birthDateYear;
    }

    public void setBirthDateYear(Integer birthDateYear) {
        this.birthDateYear = birthDateYear;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
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

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
