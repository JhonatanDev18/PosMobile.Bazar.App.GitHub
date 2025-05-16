
package com.crystal.bazarposmobile.retrofit.response.tarjetasbancarias;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Tarjeta  implements Serializable {

    @Expose
    private String codigo;
    @Expose
    private String nombre;
    @Expose
    private String pais;
    @Expose
    private String tipo;

    public Tarjeta(String codigo, String nombre, String pais, String tipo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.pais = pais;
        this.tipo = tipo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}
