package com.crystal.bazarposmobile.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.crystal.bazarposmobile.common.Constantes;

@Entity(tableName = Constantes.TABLA_MEDIO_PAGO_CAJA)
public class MediosPagoCajaEntity {

    @NonNull
    private String caja;

    @NonNull
    @PrimaryKey
    private String codigo;

    @NonNull
    private String divisa;

    @NonNull
    private String nombre;

    @NonNull
    private String pais;

    @NonNull
    private String tipo;

    @NonNull
    private Boolean tpe;

    @NonNull
    private String tpeValue;

    public MediosPagoCajaEntity(@NonNull String caja, @NonNull String codigo, @NonNull String divisa, @NonNull String nombre, @NonNull String pais, @NonNull String tipo, @NonNull Boolean tpe, @NonNull String tpeValue) {
        this.caja = caja;
        this.codigo = codigo;
        this.divisa = divisa;
        this.nombre = nombre;
        this.pais = pais;
        this.tipo = tipo;
        this.tpe = tpe;
        this.tpeValue = tpeValue;
    }

    @NonNull
    public String getCaja() {
        return caja;
    }

    public void setCaja(@NonNull String caja) {
        this.caja = caja;
    }

    @NonNull
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(@NonNull String codigo) {
        this.codigo = codigo;
    }

    @NonNull
    public String getDivisa() {
        return divisa;
    }

    public void setDivisa(@NonNull String divisa) {
        this.divisa = divisa;
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
    public Boolean getTpe() {
        return tpe;
    }

    public void setTpe(@NonNull Boolean tpe) {
        this.tpe = tpe;
    }

    @NonNull
    public String getTpeValue() {
        return tpeValue;
    }

    public void setTpeValue(@NonNull String tpeValue) {
        this.tpeValue = tpeValue;
    }
}
