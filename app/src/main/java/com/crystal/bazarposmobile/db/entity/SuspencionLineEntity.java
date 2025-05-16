package com.crystal.bazarposmobile.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.crystal.bazarposmobile.common.Constantes;

@Entity(tableName = Constantes.TABLA_SUSPENSION_LINE)
public class SuspencionLineEntity {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String referencia;

    @NonNull
    private String ean;

    @NonNull
    private Integer cantidad;

    public SuspencionLineEntity(String referencia, @NonNull String ean, @NonNull Integer cantidad) {
        this.referencia = referencia;
        this.ean = ean;
        this.cantidad = cantidad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getEan() {
        return ean;
    }

    public void setEan(@NonNull String ean) {
        this.ean = ean;
    }

    @NonNull
    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(@NonNull Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    @Override
    public String toString() {
        return "SuspencionLineEntity{" +
                "id=" + id +
                ", referencia='" + referencia + '\'' +
                ", ean='" + ean + '\'' +
                ", cantidad=" + cantidad +
                '}';
    }
}
