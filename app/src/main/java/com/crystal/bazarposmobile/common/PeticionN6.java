package com.crystal.bazarposmobile.common;

public class PeticionN6 {
    private String cajeroCode;
    private String caja;
    private String nombreTienda;
    private int numeroFacturaN6;
    private int valorIvaN6;
    private int valorVentaN6;
    private String referenciaInternaValidacion;
    private String prefijo;
    private String consecutivo;

    public PeticionN6(String cajeroCode, String caja, String nombreTienda) {
        this.cajeroCode = cajeroCode;
        this.caja = caja;
        this.nombreTienda = nombreTienda;
    }

    public void construirReferenciaInterna(){
        numeroFacturaN6 = Integer.parseInt(consecutivo);
        referenciaInternaValidacion = prefijo + "  " + consecutivo;
    }

    public String getCajeroCode() {
        return cajeroCode;
    }

    public void setCajeroCode(String cajeroCode) {
        this.cajeroCode = cajeroCode;
    }

    public String getCaja() {
        return caja;
    }

    public void setCaja(String caja) {
        this.caja = caja;
    }

    public int getNumeroFacturaN6() {
        return numeroFacturaN6;
    }

    public void setNumeroFacturaN6(int numeroFacturaN6) {
        this.numeroFacturaN6 = numeroFacturaN6;
    }

    public int getValorIvaN6() {
        return valorIvaN6;
    }

    public void setValorIvaN6(int valorIvaN6) {
        this.valorIvaN6 = valorIvaN6;
    }

    public int getValorVentaN6() {
        return valorVentaN6;
    }

    public void setValorVentaN6(int valorVentaN6) {
        this.valorVentaN6 = valorVentaN6;
    }

    public String getReferenciaInternaValidacion() {
        return referenciaInternaValidacion;
    }

    public void setReferenciaInternaValidacion(String referenciaInternaValidacion) {
        this.referenciaInternaValidacion = referenciaInternaValidacion;
    }

    public String getPrefijo() {
        return prefijo;
    }

    public void setPrefijo(String prefijo) {
        this.prefijo = prefijo;
    }

    public String getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(String consecutivo) {
        this.consecutivo = consecutivo;
    }

    public String getNombreTienda() {
        return nombreTienda;
    }

    public void setNombreTienda(String nombreTienda) {
        this.nombreTienda = nombreTienda;
    }
}