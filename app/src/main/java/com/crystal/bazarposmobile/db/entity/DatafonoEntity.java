package com.crystal.bazarposmobile.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.crystal.bazarposmobile.common.Constantes;

import java.io.Serializable;

@Entity(tableName = Constantes.TABLA_DATAFONO)
public class DatafonoEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String dc;

    private String bin;

    private String codigoAprobacion;

    private String codigoRespuesta;

    private String dirEstablecimiento;

    private String fechaVenTarjeta;

    @NonNull
    private String franquicia;

    @NonNull
    private Double monto;

    @NonNull
    private Double iva;

    private String criptograma;

    private Long tvr;

    private Long tsi;

    private String aid;

    private String idCliente;

    private String cuotas;

    @NonNull
    private String numRecibo;

    private String codigoTerminal;

    private String rrn;

    @NonNull
    private String tipoCuenta;

    private String ultDigitoTarj;

    private String codigoComercio;

    private Double iac;

    private String fecha;

    private String hora;

    private String hash;

    @NonNull
    private Double propina;

    private String tiendaNombre;

    private Boolean esCierre;

    public DatafonoEntity(String dc, String bin, String codigoAprobacion, String codigoRespuesta,
                          String dirEstablecimiento, String fechaVenTarjeta, @NonNull String franquicia,
                          @NonNull Double monto, @NonNull Double iva, String criptograma, Long tvr, Long tsi,
                          String aid, String idCliente, String cuotas, @NonNull String numRecibo, String codigoTerminal,
                          String rrn, @NonNull String tipoCuenta, String ultDigitoTarj, String codigoComercio, Double iac,
                          String fecha, String hora, String hash, @NonNull Double propina, String tiendaNombre, Boolean esCierre) {
        this.dc = dc;
        this.bin = bin;
        this.codigoAprobacion = codigoAprobacion;
        this.codigoRespuesta = codigoRespuesta;
        this.dirEstablecimiento = dirEstablecimiento;
        this.fechaVenTarjeta = fechaVenTarjeta;
        this.franquicia = franquicia;
        this.monto = monto;
        this.iva = iva;
        this.criptograma = criptograma;
        this.tvr = tvr;
        this.tsi = tsi;
        this.aid = aid;
        this.idCliente = idCliente;
        this.cuotas = cuotas;
        this.numRecibo = numRecibo;
        this.codigoTerminal = codigoTerminal;
        this.rrn = rrn;
        this.tipoCuenta = tipoCuenta;
        this.ultDigitoTarj = ultDigitoTarj;
        this.codigoComercio = codigoComercio;
        this.iac = iac;
        this.fecha = fecha;
        this.hora = hora;
        this.hash = hash;
        this.propina = propina;
        this.tiendaNombre = tiendaNombre;
        this.esCierre = esCierre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDc() {
        return dc;
    }

    public void setDc(String dc) {
        this.dc = dc;
    }

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public String getCodigoAprobacion() {
        return codigoAprobacion;
    }

    public void setCodigoAprobacion(String codigoAprobacion) {
        this.codigoAprobacion = codigoAprobacion;
    }

    public String getCodigoRespuesta() {
        return codigoRespuesta;
    }

    public void setCodigoRespuesta(String codigoRespuesta) {
        this.codigoRespuesta = codigoRespuesta;
    }

    public String getDirEstablecimiento() {
        return dirEstablecimiento;
    }

    public void setDirEstablecimiento(String dirEstablecimiento) {
        this.dirEstablecimiento = dirEstablecimiento;
    }

    public String getFechaVenTarjeta() {
        return fechaVenTarjeta;
    }

    public void setFechaVenTarjeta(String fechaVenTarjeta) {
        this.fechaVenTarjeta = fechaVenTarjeta;
    }

    @NonNull
    public String getFranquicia() {
        return franquicia;
    }

    public void setFranquicia(String franquicia) {
        this.franquicia = franquicia;
    }

    @NonNull
    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    @NonNull
    public Double getIva() {
        return iva;
    }

    public void setIva(Double iva) {
        this.iva = iva;
    }

    public String getCriptograma() {
        return criptograma;
    }

    public void setCriptograma(String criptograma) {
        this.criptograma = criptograma;
    }

    public Long getTvr() {
        return tvr;
    }

    public void setTvr(Long tvr) {
        this.tvr = tvr;
    }

    public Long getTsi() {
        return tsi;
    }

    public void setTsi(Long tsi) {
        this.tsi = tsi;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getCuotas() {
        return cuotas;
    }

    public void setCuotas(String cuotas) {
        this.cuotas = cuotas;
    }

    @NonNull
    public String getNumRecibo() {
        return numRecibo;
    }

    public void setNumRecibo(String numRecibo) {
        this.numRecibo = numRecibo;
    }

    public String getCodigoTerminal() {
        return codigoTerminal;
    }

    public void setCodigoTerminal(String codigoTerminal) {
        this.codigoTerminal = codigoTerminal;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    @NonNull
    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public String getUltDigitoTarj() {
        return ultDigitoTarj;
    }

    public void setUltDigitoTarj(String ultDigitoTarj) {
        this.ultDigitoTarj = ultDigitoTarj;
    }

    public String getCodigoComercio() {
        return codigoComercio;
    }

    public void setCodigoComercio(String codigoComercio) {
        this.codigoComercio = codigoComercio;
    }

    public Double getIac() {
        return iac;
    }

    public void setIac(Double iac) {
        this.iac = iac;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @NonNull
    public Double getPropina() {
        return propina;
    }

    public void setPropina(Double propina) {
        this.propina = propina;
    }

    public String getTiendaNombre() {
        return tiendaNombre;
    }

    public void setTiendaNombre(String tiendaNombre) {
        this.tiendaNombre = tiendaNombre;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public Boolean getEsCierre() {
        return esCierre;
    }

    public void setEsCierre(Boolean esCierre) {
        this.esCierre = esCierre;
    }

    @Override
    public String toString() {
        return "DatafonoEntity{" +
                "numRecibo='" + numRecibo + '\'' +
                ", dc='" + dc + '\'' +
                ", bin='" + bin + '\'' +
                ", codigoAprobacion='" + codigoAprobacion + '\'' +
                ", codigoRespuesta='" + codigoRespuesta + '\'' +
                ", dirEstablecimiento='" + dirEstablecimiento + '\'' +
                ", fechaVenTarjeta='" + fechaVenTarjeta + '\'' +
                ", franquicia='" + franquicia + '\'' +
                ", monto=" + monto +
                ", iva=" + iva +
                ", criptograma='" + criptograma + '\'' +
                ", tvr=" + tvr +
                ", tsi=" + tsi +
                ", aid='" + aid + '\'' +
                ", idCliente='" + idCliente + '\'' +
                ", cuotas='" + cuotas + '\'' +
                ", codigoTerminal='" + codigoTerminal + '\'' +
                ", rrn='" + rrn + '\'' +
                ", tipoCuenta='" + tipoCuenta + '\'' +
                ", ultDigitoTarj='" + ultDigitoTarj + '\'' +
                ", codigoComercio='" + codigoComercio + '\'' +
                ", iac=" + iac +
                ", fecha='" + fecha + '\'' +
                ", hora='" + hora + '\'' +
                ", hash='" + hash + '\'' +
                ", propina=" + propina +
                ", tiendaNombre='" + tiendaNombre + '\'' +
                ", esCierre='" + esCierre + '\'' + '}';
    }
}
