package com.crystal.bazarposmobile.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.crystal.bazarposmobile.common.Constantes;

import java.io.Serializable;

@Entity(tableName = Constantes.TABLA_TEF_CONTINGUENCIA)
public class TEFContinguenciaEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String numtrans;

    @NonNull
    private String codigo;

    @NonNull
    private String nombre;

    @NonNull
    private String franquicia;

    @NonNull
    private String tipoCuenta;

    @NonNull
    private String pais;

    @NonNull
    private String tipo;

    @NonNull
    private Double monto;

    @NonNull
    private Double iva;

    @NonNull
    private String fecha;

    public TEFContinguenciaEntity(@NonNull String numtrans, @NonNull String codigo, @NonNull String nombre,
                                  @NonNull String franquicia, @NonNull String tipoCuenta, @NonNull String pais, @NonNull String tipo, @NonNull Double monto,
                                  @NonNull Double iva, @NonNull String fecha) {
        this.numtrans = numtrans;
        this.codigo = codigo;
        this.nombre = nombre;
        this.franquicia = franquicia;
        this.tipoCuenta = tipoCuenta;
        this.pais = pais;
        this.tipo = tipo;
        this.monto = monto;
        this.iva = iva;
        this.fecha = fecha;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getNumtrans() {
        return numtrans;
    }

    public void setNumtrans(@NonNull String numtrans) {
        this.numtrans = numtrans;
    }

    @NonNull
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(@NonNull String codigo) {
        this.codigo = codigo;
    }

    @NonNull
    public String getNombre() {
        return nombre;
    }

    public void setNombre(@NonNull String nombre) {
        this.nombre = nombre;
    }

    @NonNull
    public String getPais() {
        return pais;
    }

    public void setPais(@NonNull String pais) {
        this.pais = pais;
    }

    @NonNull
    public String getTipo() {
        return tipo;
    }

    public void setTipo(@NonNull String tipo) {
        this.tipo = tipo;
    }

    @NonNull
    public Double getMonto() {
        return monto;
    }

    public void setMonto(@NonNull Double monto) {
        this.monto = monto;
    }

    @NonNull
    public Double getIva() {
        return iva;
    }

    public void setIva(@NonNull Double iva) {
        this.iva = iva;
    }

    @NonNull
    public String getFecha() {
        return fecha;
    }

    public void setFecha(@NonNull String fecha) {
        this.fecha = fecha;
    }

    @NonNull
    public String getFranquicia() {
        return franquicia;
    }

    public void setFranquicia(@NonNull String franquicia) {
        this.franquicia = franquicia;
    }

    @NonNull
    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(@NonNull String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }
}
