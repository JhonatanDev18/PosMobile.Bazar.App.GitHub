package com.crystal.bazarposmobile.retrofit.response.tienda;

import com.google.gson.annotations.SerializedName;

public class TiendaItem {

    @SerializedName("nombreRegion")
    private String nombreRegion;

    @SerializedName("codigoPostal")
    private String codigoPostal;

    @SerializedName("divisa")
    private String divisa;

    @SerializedName("formato")
    private String formato;

    @SerializedName("direccion")
    private String direccion;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("pais")
    private String pais;

    @SerializedName("tienda")
    private String tienda;

    @SerializedName("cerrado")
    private String cerrado;

    @SerializedName("ciudad")
    private String ciudad;

    @SerializedName("serieRFID")
    private String serieRFID;

    @SerializedName("codigoRegion")
    private String codigoRegion;

    @SerializedName("correoEntrada")
    private String correoEntrada;

    @SerializedName("tipoValor")
    private String tipoValor;

    @SerializedName("telefono")
    private String telefono;

    @SerializedName("correoSalida")
    private String correoSalida;
    @SerializedName("merchantID")
    private String merchantID;

    public void setNombreRegion(String nombreRegion){
        this.nombreRegion = nombreRegion;
    }

    public String getNombreRegion(){
        return nombreRegion;
    }

    public void setCodigoPostal(String codigoPostal){
        this.codigoPostal = codigoPostal;
    }

    public String getCodigoPostal(){
        return codigoPostal;
    }

    public void setDivisa(String divisa){
        this.divisa = divisa;
    }

    public String getDivisa(){
        return divisa;
    }

    public void setFormato(String formato){
        this.formato = formato;
    }

    public String getFormato(){
        return formato;
    }

    public void setDireccion(String direccion){
        this.direccion = direccion;
    }

    public String getDireccion(){
        return direccion;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public String getNombre(){
        return nombre;
    }

    public void setPais(String pais){
        this.pais = pais;
    }

    public String getPais(){
        return pais;
    }

    public void setTienda(String tienda){
        this.tienda = tienda;
    }

    public String getTienda(){
        return tienda;
    }

    public void setCerrado(String cerrado){
        this.cerrado = cerrado;
    }

    public String getCerrado(){
        return cerrado;
    }

    public void setCiudad(String ciudad){
        this.ciudad = ciudad;
    }

    public String getCiudad(){
        return ciudad;
    }

    public void setSerieRFID(String serieRFID){
        this.serieRFID = serieRFID;
    }

    public String getSerieRFID(){
        return serieRFID;
    }

    public void setCodigoRegion(String codigoRegion){
        this.codigoRegion = codigoRegion;
    }

    public String getCodigoRegion(){
        return codigoRegion;
    }

    public void setCorreoEntrada(String correoEntrada){
        this.correoEntrada = correoEntrada;
    }

    public String getCorreoEntrada(){
        return correoEntrada;
    }

    public void setTipoValor(String tipoValor){
        this.tipoValor = tipoValor;
    }

    public String getTipoValor(){
        return tipoValor;
    }

    public void setTelefono(String telefono){
        this.telefono = telefono;
    }

    public String getTelefono(){
        return telefono;
    }

    public void setCorreoSalida(String correoSalida){
        this.correoSalida = correoSalida;
    }

    public String getCorreoSalida(){
        return correoSalida;
    }
    public void setMerchantID(String merchantID){
        this.merchantID = merchantID;
    }

    public String getMerchantID(){
        return merchantID;
    }

    @Override
    public String toString(){
        return
                "TiendasItem{" +
                        "nombreRegion = '" + nombreRegion + '\'' +
                        ",codigoPostal = '" + codigoPostal + '\'' +
                        ",divisa = '" + divisa + '\'' +
                        ",formato = '" + formato + '\'' +
                        ",direccion = '" + direccion + '\'' +
                        ",nombre = '" + nombre + '\'' +
                        ",pais = '" + pais + '\'' +
                        ",tienda = '" + tienda + '\'' +
                        ",cerrado = '" + cerrado + '\'' +
                        ",ciudad = '" + ciudad + '\'' +
                        ",serieRFID = '" + serieRFID + '\'' +
                        ",codigoRegion = '" + codigoRegion + '\'' +
                        ",correoEntrada = '" + correoEntrada + '\'' +
                        ",tipoValor = '" + tipoValor + '\'' +
                        ",telefono = '" + telefono + '\'' +
                        ",correoSalida = '" + correoSalida + '\'' +
                        "}";
    }
}
