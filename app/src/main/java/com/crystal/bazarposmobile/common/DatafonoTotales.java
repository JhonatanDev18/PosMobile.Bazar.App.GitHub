package com.crystal.bazarposmobile.common;

import java.io.Serializable;

public class DatafonoTotales implements Serializable {

    private Double monto;

    private Double iva;

    private Double propina;

    private Integer tipoCuentaCount;

    private String franquicia;

    private String tipoCuenta;

    public DatafonoTotales(String franquicia, String tipoCuenta, Integer tipoCuentaCount, Double monto, Double iva, Double propina){
        this.monto = monto;
        this.iva = iva;
        this.franquicia = franquicia;
        this.tipoCuenta = tipoCuenta;
        this.tipoCuentaCount = tipoCuentaCount;
        this.propina = propina;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public Double getIva() {
        return iva;
    }

    public void setIva(Double iva) {
        this.iva = iva;
    }

    public Double getPropina() {
        return propina;
    }

    public void setPropina(Double propina) {
        this.propina = propina;
    }

    public Integer getTipoCuentaCount() {
        return tipoCuentaCount;
    }

    public void setTipoCuentaCount(Integer tipoCuentaCount) {
        this.tipoCuentaCount = tipoCuentaCount;
    }

    public String getFranquicia() {
        return franquicia;
    }

    public void setFranquicia(String franquicia) {
        this.franquicia = franquicia;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    @Override
    public String toString() {
        return "DatafonoTotales{" +
                "monto=" + monto +
                ", iva=" + iva +
                ", propina=" + propina +
                ", tipoCuentaCount=" + tipoCuentaCount +
                ", franquicia='" + franquicia + '\'' +
                ", tipoCuenta='" + tipoCuenta + '\'' +
                '}';
    }
}
