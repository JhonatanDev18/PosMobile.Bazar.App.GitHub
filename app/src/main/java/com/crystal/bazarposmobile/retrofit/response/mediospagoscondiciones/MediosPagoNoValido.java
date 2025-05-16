
package com.crystal.bazarposmobile.retrofit.response.mediospagoscondiciones;

import com.google.gson.annotations.Expose;

public class MediosPagoNoValido {

    @Expose
    private String codigo;
    @Expose
    private String divisa;
    @Expose
    private String nombre;
    @Expose
    private String pais;
    @Expose
    private String tienda;

    public MediosPagoNoValido(String codigo, String divisa, String nombre, String pais, String tienda) {
        this.codigo = codigo;
        this.divisa = divisa;
        this.nombre = nombre;
        this.pais = pais;
        this.tienda = tienda;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDivisa() {
        return divisa;
    }

    public void setDivisa(String divisa) {
        this.divisa = divisa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getTienda() {
        return tienda;
    }

    public void setTienda(String tienda) {
        this.tienda = tienda;
    }

}
