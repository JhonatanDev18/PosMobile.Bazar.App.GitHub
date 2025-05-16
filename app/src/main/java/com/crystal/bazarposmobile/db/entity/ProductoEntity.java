package com.crystal.bazarposmobile.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.crystal.bazarposmobile.common.Constantes;

@Entity(tableName = Constantes.TABLA_PRODUCTO)
public class ProductoEntity {

    @NonNull
    @PrimaryKey
    private String ean;

    @NonNull
    private Double precio;

    @NonNull
    private String nombre;

    private String talla;

    private String color;

    private String tipoTarifa;

    private String tienda;

    private String periodoTarifa;

    private String ip;

    private String composicion;

    private String precioUnitario;

    @NonNull
    private String articulo;

    private String codigoTasaImpuesto;

    private Boolean articuloCerrado;

    private Boolean articuloGratuito;

    private String tasaImpuesto;

    private Double precioSinImpuesto;

    @NonNull
    private Double impuesto;

    private Double valorTasa;

    private String fechaTasa;

    private Double precioOriginal;

    private Boolean periodoActivo;

    private String codigoMarca;

    private String marca;

    private String serialNumberId;

    private String vendedorId;

    @NonNull
    private Integer quantity;

    private Boolean descontable;

    private String tipoPrendaCodigo;

    private String tipoPrendaNombre;

    private String generoCodigo;

    private String generoNombre;

    private String categoriaIvaCodigo;

    private String categoriaIvaNombre;

    @NonNull
    private int line;

    public ProductoEntity(@NonNull String ean, @NonNull Double precio, @NonNull String nombre, String talla, String color, String tipoTarifa, String tienda, String periodoTarifa, String ip, String composicion, String precioUnitario, @NonNull String articulo, String codigoTasaImpuesto, Boolean articuloCerrado, Boolean articuloGratuito, String tasaImpuesto, Double precioSinImpuesto, @NonNull Double impuesto, Double valorTasa, String fechaTasa, Double precioOriginal, Boolean periodoActivo, String codigoMarca, String marca, String serialNumberId, String vendedorId, @NonNull Integer quantity, Boolean descontable, String tipoPrendaCodigo, String tipoPrendaNombre, String generoCodigo, String generoNombre, String categoriaIvaCodigo, String categoriaIvaNombre, int line) {
        this.ean = ean;
        this.precio = precio;
        this.nombre = nombre;
        this.talla = talla;
        this.color = color;
        this.tipoTarifa = tipoTarifa;
        this.tienda = tienda;
        this.periodoTarifa = periodoTarifa;
        this.ip = ip;
        this.composicion = composicion;
        this.precioUnitario = precioUnitario;
        this.articulo = articulo;
        this.codigoTasaImpuesto = codigoTasaImpuesto;
        this.articuloCerrado = articuloCerrado;
        this.articuloGratuito = articuloGratuito;
        this.tasaImpuesto = tasaImpuesto;
        this.precioSinImpuesto = precioSinImpuesto;
        this.impuesto = impuesto;
        this.valorTasa = valorTasa;
        this.fechaTasa = fechaTasa;
        this.precioOriginal = precioOriginal;
        this.periodoActivo = periodoActivo;
        this.codigoMarca = codigoMarca;
        this.marca = marca;
        this.serialNumberId = serialNumberId;
        this.vendedorId = vendedorId;
        this.quantity = quantity;
        this.descontable = descontable;
        this.tipoPrendaCodigo = tipoPrendaCodigo;
        this.tipoPrendaNombre = tipoPrendaNombre;
        this.generoCodigo = generoCodigo;
        this.generoNombre = generoNombre;
        this.categoriaIvaCodigo = categoriaIvaCodigo;
        this.categoriaIvaNombre = categoriaIvaNombre;
        this.line = line;
    }

    @NonNull
    public String getEan() {
        return ean;
    }

    public void setEan(@NonNull String ean) {
        this.ean = ean;
    }

    @NonNull
    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(@NonNull Double precio) {
        this.precio = precio;
    }

    @NonNull
    public String getNombre() {
        return nombre;
    }

    public void setNombre(@NonNull String nombre) {
        this.nombre = nombre;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTipoTarifa() {
        return tipoTarifa;
    }

    public void setTipoTarifa(String tipoTarifa) {
        this.tipoTarifa = tipoTarifa;
    }

    public String getTienda() {
        return tienda;
    }

    public void setTienda(String tienda) {
        this.tienda = tienda;
    }

    public String getPeriodoTarifa() {
        return periodoTarifa;
    }

    public void setPeriodoTarifa(String periodoTarifa) {
        this.periodoTarifa = periodoTarifa;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getComposicion() {
        return composicion;
    }

    public void setComposicion(String composicion) {
        this.composicion = composicion;
    }

    public String getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(String precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    @NonNull
    public String getArticulo() {
        return articulo;
    }

    public void setArticulo(@NonNull String articulo) {
        this.articulo = articulo;
    }

    public String getCodigoTasaImpuesto() {
        return codigoTasaImpuesto;
    }

    public void setCodigoTasaImpuesto(String codigoTasaImpuesto) {
        this.codigoTasaImpuesto = codigoTasaImpuesto;
    }

    public Boolean getArticuloCerrado() {
        return articuloCerrado;
    }

    public void setArticuloCerrado(Boolean articuloCerrado) {
        this.articuloCerrado = articuloCerrado;
    }

    public Boolean getArticuloGratuito() {
        return articuloGratuito;
    }

    public void setArticuloGratuito(Boolean articuloGratuito) {
        this.articuloGratuito = articuloGratuito;
    }

    public String getTasaImpuesto() {
        return tasaImpuesto;
    }

    public void setTasaImpuesto(String tasaImpuesto) {
        this.tasaImpuesto = tasaImpuesto;
    }

    public Double getPrecioSinImpuesto() {
        return precioSinImpuesto;
    }

    public void setPrecioSinImpuesto(Double precioSinImpuesto) {
        this.precioSinImpuesto = precioSinImpuesto;
    }

    @NonNull
    public Double getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(@NonNull Double impuesto) {
        this.impuesto = impuesto;
    }

    public Double getValorTasa() {
        return valorTasa;
    }

    public void setValorTasa(Double valorTasa) {
        this.valorTasa = valorTasa;
    }

    public String getFechaTasa() {
        return fechaTasa;
    }

    public void setFechaTasa(String fechaTasa) {
        this.fechaTasa = fechaTasa;
    }

    public Double getPrecioOriginal() {
        return precioOriginal;
    }

    public void setPrecioOriginal(Double precioOriginal) {
        this.precioOriginal = precioOriginal;
    }

    public Boolean getPeriodoActivo() {
        return periodoActivo;
    }

    public void setPeriodoActivo(Boolean periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public String getCodigoMarca() {
        return codigoMarca;
    }

    public void setCodigoMarca(String codigoMarca) {
        this.codigoMarca = codigoMarca;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getSerialNumberId() {
        return serialNumberId;
    }

    public void setSerialNumberId(String serialNumberId) {
        this.serialNumberId = serialNumberId;
    }

    public String getVendedorId() {
        return vendedorId;
    }

    public void setVendedorId(String vendedorId) {
        this.vendedorId = vendedorId;
    }

    @NonNull
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(@NonNull Integer quantity) {
        this.quantity = quantity;
    }

    public Boolean getDescontable() {
        return descontable;
    }

    public void setDescontable(Boolean descontable) {
        this.descontable = descontable;
    }

    public String getTipoPrendaCodigo() {
        return tipoPrendaCodigo;
    }

    public void setTipoPrendaCodigo(String tipoPrendaCodigo) {
        this.tipoPrendaCodigo = tipoPrendaCodigo;
    }

    public String getTipoPrendaNombre() {
        return tipoPrendaNombre;
    }

    public void setTipoPrendaNombre(String tipoPrendaNombre) {
        this.tipoPrendaNombre = tipoPrendaNombre;
    }

    public String getGeneroCodigo() {
        return generoCodigo;
    }

    public void setGeneroCodigo(String generoCodigo) {
        this.generoCodigo = generoCodigo;
    }

    public String getGeneroNombre() {
        return generoNombre;
    }

    public void setGeneroNombre(String generoNombre) {
        this.generoNombre = generoNombre;
    }

    public String getCategoriaIvaCodigo() {
        return categoriaIvaCodigo;
    }

    public void setCategoriaIvaCodigo(String categoriaIvaCodigo) {
        this.categoriaIvaCodigo = categoriaIvaCodigo;
    }

    public String getCategoriaIvaNombre() {
        return categoriaIvaNombre;
    }

    public void setCategoriaIvaNombre(String categoriaIvaNombre) {
        this.categoriaIvaNombre = categoriaIvaNombre;
    }

    public int getLine() {
        return line;
    }
}
