package com.crystal.bazarposmobile.retrofit.response.suspensiones;

import java.util.List;

import com.crystal.bazarposmobile.retrofit.request.suspension.SuspesionLinea;
import com.google.gson.annotations.SerializedName;

public class SuspensionItem {

    @SerializedName("texto")
    private String texto;

    @SerializedName("lineas")
    private List<SuspesionLinea> lineas;

    @SerializedName("cliente")
    private String cliente;

    @SerializedName("tienda")
    private String tienda;

    @SerializedName("caja")
    private String caja;

    @SerializedName("referencia")
    private String referencia;

    private Integer cantidad;

    public SuspensionItem(String texto, String cliente, String referencia, Integer cantidad) {
        this.texto = texto;
        this.cliente = cliente;
        this.referencia = referencia;
        this.cantidad = cantidad;
    }

    public void setTexto(String texto){
        this.texto = texto;
    }

    public String getTexto(){
        return texto;
    }

    public void setLineas(List<SuspesionLinea> lineas){
        this.lineas = lineas;
    }

    public List<SuspesionLinea> getLineas(){
        return lineas;
    }

    public void setCliente(String cliente){
        this.cliente = cliente;
    }

    public String getCliente(){
        return cliente;
    }

    public void setTienda(String tienda){
        this.tienda = tienda;
    }

    public String getTienda(){
        return tienda;
    }

    public void setCaja(String caja){
        this.caja = caja;
    }

    public String getCaja(){
        return caja;
    }

    public void setReferencia(String referencia){
        this.referencia = referencia;
    }

    public String getReferencia(){
        return referencia;
    }

    @Override
     public String toString(){
        return 
            "SuspencionesItem{" + 
            "texto = '" + texto + '\'' + 
            ",lineas = '" + lineas + '\'' + 
            ",cliente = '" + cliente + '\'' + 
            ",tienda = '" + tienda + '\'' + 
            ",caja = '" + caja + '\'' + 
            ",referencia = '" + referencia + '\'' + 
            "}";
        }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}