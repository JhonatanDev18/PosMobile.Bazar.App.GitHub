package com.crystal.bazarposmobile.db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.crystal.bazarposmobile.common.Constantes;

@Entity(tableName = Constantes.TABLA_SUSPENSION_HEADER)
public class SuspencionHeaderEntity {

    @NonNull
    @PrimaryKey
    private String referencia;

    private String texto;

    @NonNull
    private Integer cantidad;

    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    private String fechahora;

    public SuspencionHeaderEntity(@NonNull String referencia, String texto, @NonNull Integer cantidad) {
        this.referencia = referencia;
        this.texto = texto;
        this.cantidad = cantidad;
    }

    @NonNull
    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(@NonNull String referencia) {
        this.referencia = referencia;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    @NonNull
    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(@NonNull Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getFechahora() {
        return fechahora;
    }

    public void setFechahora(String fechahora) {
        this.fechahora = fechahora;
    }

    @Override
    public String toString() {
        return "SuspencionHeaderEntity{" +
                "referencia='" + referencia + '\'' +
                ", texto='" + texto + '\'' +
                ", cantidad=" + cantidad +
                ", fechahora='" + fechahora + '\'' +
                '}';
    }
}
