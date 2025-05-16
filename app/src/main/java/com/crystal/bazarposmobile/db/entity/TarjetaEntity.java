package com.crystal.bazarposmobile.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.crystal.bazarposmobile.common.Constantes;

@Entity(tableName = Constantes.TABLA_TARJETA_BANCARIA)
public class TarjetaEntity {

    @NonNull
    @PrimaryKey
    private String codigo;

    @NonNull
    private String nombre;

    @NonNull
    private String pais;

    @NonNull
    private String tipo;

    public TarjetaEntity(@NonNull String codigo, @NonNull String nombre, @NonNull String pais, @NonNull String tipo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.pais = pais;
        this.tipo = tipo;
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
}
