
package com.crystal.bazarposmobile.retrofit.response.mediospagocaja;

import com.google.gson.annotations.Expose;
import java.io.Serializable;

public class MediosCaja implements Serializable {

    @Expose
    private String caja;
    @Expose
    private String codigo;
    @Expose
    private String divisa;
    @Expose
    private String nombre;
    @Expose
    private String pais;
    @Expose
    private String tipo;
    @Expose
    private Boolean tpe;
    @Expose
    private String tpeValue;

    private boolean isEnabled = false;

    public MediosCaja(String caja, String codigo, String divisa, String nombre, String pais, String tipo, Boolean tpe, String tpeValue) {
        this.caja = caja;
        this.codigo = codigo;
        this.divisa = divisa;
        this.nombre = nombre;
        this.pais = pais;
        this.tipo = tipo;
        this.tpe = tpe;
        this.tpeValue = tpeValue;

    }

    public String getCaja() {
        return caja;
    }

    public void setCaja(String caja) {
        this.caja = caja;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Boolean getTpe() {
        return tpe;
    }

    public void setTpe(Boolean tpe) {
        this.tpe = tpe;
    }

    public String getTpeValue() {
        return tpeValue;
    }

    public void setTpeValue(String tpeValue) {
        this.tpeValue = tpeValue;
    }

    public boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}
