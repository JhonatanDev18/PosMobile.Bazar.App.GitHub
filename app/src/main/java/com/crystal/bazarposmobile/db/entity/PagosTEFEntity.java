package com.crystal.bazarposmobile.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.crystal.bazarposmobile.common.Constantes;

@Entity(tableName = Constantes.TABLA_PAGO_TEF)
public class PagosTEFEntity {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String internalReference;

    private String codigoCaja;

    private String codigoMedioPago;

    private String cuatroUltimosDigitosTarjeta;

    private String fechaVencimientoTarjeta;

    private String numeroAutorizacion;

    private Integer numeroCuotas;

    private Integer numeroPago;

    private String numeroTransaccionDatafono;

    private String respuestaTEF;

    private String tipoTarjeta;

    public PagosTEFEntity(String internalReference, String codigoCaja, String codigoMedioPago, String cuatroUltimosDigitosTarjeta, String fechaVencimientoTarjeta, String numeroAutorizacion, Integer numeroCuotas, Integer numeroPago, String numeroTransaccionDatafono, String respuestaTEF, String tipoTarjeta) {
        this.internalReference = internalReference;
        this.codigoCaja = codigoCaja;
        this.codigoMedioPago = codigoMedioPago;
        this.cuatroUltimosDigitosTarjeta = cuatroUltimosDigitosTarjeta;
        this.fechaVencimientoTarjeta = fechaVencimientoTarjeta;
        this.numeroAutorizacion = numeroAutorizacion;
        this.numeroCuotas = numeroCuotas;
        this.numeroPago = numeroPago;
        this.numeroTransaccionDatafono = numeroTransaccionDatafono;
        this.respuestaTEF = respuestaTEF;
        this.tipoTarjeta = tipoTarjeta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInternalReference() {
        return internalReference;
    }

    public void setInternalReference(String internalReference) {
        this.internalReference = internalReference;
    }

    public String getCodigoCaja() {
        return codigoCaja;
    }

    public void setCodigoCaja(String codigoCaja) {
        this.codigoCaja = codigoCaja;
    }

    public String getCodigoMedioPago() {
        return codigoMedioPago;
    }

    public void setCodigoMedioPago(String codigoMedioPago) {
        this.codigoMedioPago = codigoMedioPago;
    }

    public String getCuatroUltimosDigitosTarjeta() {
        return cuatroUltimosDigitosTarjeta;
    }

    public void setCuatroUltimosDigitosTarjeta(String cuatroUltimosDigitosTarjeta) {
        this.cuatroUltimosDigitosTarjeta = cuatroUltimosDigitosTarjeta;
    }

    public String getFechaVencimientoTarjeta() {
        return fechaVencimientoTarjeta;
    }

    public void setFechaVencimientoTarjeta(String fechaVencimientoTarjeta) {
        this.fechaVencimientoTarjeta = fechaVencimientoTarjeta;
    }

    public String getNumeroAutorizacion() {
        return numeroAutorizacion;
    }

    public void setNumeroAutorizacion(String numeroAutorizacion) {
        this.numeroAutorizacion = numeroAutorizacion;
    }

    public Integer getNumeroCuotas() {
        return numeroCuotas;
    }

    public void setNumeroCuotas(Integer numeroCuotas) {
        this.numeroCuotas = numeroCuotas;
    }

    public Integer getNumeroPago() {
        return numeroPago;
    }

    public void setNumeroPago(Integer numeroPago) {
        this.numeroPago = numeroPago;
    }

    public String getNumeroTransaccionDatafono() {
        return numeroTransaccionDatafono;
    }

    public void setNumeroTransaccionDatafono(String numeroTransaccionDatafono) {
        this.numeroTransaccionDatafono = numeroTransaccionDatafono;
    }

    public String getRespuestaTEF() {
        return respuestaTEF;
    }

    public void setRespuestaTEF(String respuestaTEF) {
        this.respuestaTEF = respuestaTEF;
    }

    public String getTipoTarjeta() {
        return tipoTarjeta;
    }

    public void setTipoTarjeta(String tipoTarjeta) {
        this.tipoTarjeta = tipoTarjeta;
    }

    @Override
    public String toString() {
        return "PagosTEF{" +
                "codigoCaja='" + codigoCaja + '\'' +
                ", codigoMedioPago='" + codigoMedioPago + '\'' +
                ", cuatroUltimosDigitosTarjeta='" + cuatroUltimosDigitosTarjeta + '\'' +
                ", fechaVencimientoTarjeta='" + fechaVencimientoTarjeta + '\'' +
                ", numeroAutorizacion='" + numeroAutorizacion + '\'' +
                ", numeroCuotas=" + numeroCuotas +
                ", numeroPago=" + numeroPago +
                ", numeroTransaccionDatafono='" + numeroTransaccionDatafono + '\'' +
                ", respuestaTEF='" + respuestaTEF + '\'' +
                ", tipoTarjeta='" + tipoTarjeta + '\'' +
                '}';
    }
}
