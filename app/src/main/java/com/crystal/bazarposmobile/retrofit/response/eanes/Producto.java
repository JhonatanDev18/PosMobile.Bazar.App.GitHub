
package com.crystal.bazarposmobile.retrofit.response.eanes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Producto implements Serializable, Cloneable {

    @SerializedName("ean")
    @Expose
    private String ean;

    @SerializedName("precio")
    @Expose
    private Double precio;

    @SerializedName("nombre")
    @Expose
    private String nombre;

    @SerializedName("talla")
    @Expose
    private String talla;

    @SerializedName("color")
    @Expose
    private String color;

    @SerializedName("tipoTarifa")
    @Expose
    private String tipoTarifa;

    @SerializedName("tienda")
    @Expose
    private String tienda;

    @SerializedName("periodoTarifa")
    @Expose
    private String periodoTarifa;

    @SerializedName("ip")
    @Expose
    private String ip;

    @SerializedName("composicion")
    @Expose
    private String composicion;

    @SerializedName("precioUnitario")
    @Expose
    private String precioUnitario;

    @SerializedName("articulo")
    @Expose
    private String articulo;

    @SerializedName("codigoTasaImpuesto")
    @Expose
    private String codigoTasaImpuesto;

    @SerializedName("articuloCerrado")
    @Expose
    private Boolean articuloCerrado;

    @SerializedName("articuloGratuito")
    @Expose
    private Boolean articuloGratuito;

    @SerializedName("tasaImpuesto")
    @Expose
    private String tasaImpuesto;

    @SerializedName("precioSinImpuesto")
    @Expose
    private Double precioSinImpuesto;

    @SerializedName("impuesto")
    @Expose
    private Double impuesto;

    @SerializedName("valorTasa")
    @Expose
    private Double valorTasa;

    @SerializedName("fechaTasa")
    @Expose
    private String fechaTasa;

    @SerializedName("precioOriginal")
    @Expose
    private Double precioOriginal;

    @SerializedName("periodoActivo")
    @Expose
    private Boolean periodoActivo;

    @SerializedName("codigoMarca")
    @Expose
    private String codigoMarca;

    @SerializedName("marca")
    @Expose
    private String marca;

    @SerializedName("serial")
    @Expose
    private String serialNumberId;

    @SerializedName("vendedorId")
    @Expose
    private String vendedorId;

    @SerializedName("quantity")
    @Expose
    private Integer quantity;

    @SerializedName("descontable")
    @Expose
    private Boolean descontable;

    @SerializedName("tipoPrendaCodigo")
    @Expose
    private String tipoPrendaCodigo;

    @SerializedName("tipoPrendaNombre")
    @Expose
    private String tipoPrendaNombre;

    @SerializedName("generoCodigo")
    @Expose
    private String generoCodigo;

    @SerializedName("generoNombre")
    @Expose
    private String generoNombre;

    @SerializedName("categoriaIvaCodigo")
    @Expose
    private String categoriaIvaCodigo;

    @SerializedName("categoriaIvaNombre")
    @Expose
    private String categoriaIvaNombre;

    private int line;
    private String motivoDevolucionId;
    private String motivoDevolucionDesc;
    private String motivoDevolucionFactura;
    private int motivoDevolucionLinea;

    public Producto(String ean, Double precio, String nombre, String talla, String color, String tipoTarifa, String tienda, String periodoTarifa, String ip, String composicion, String precioUnitario, String articulo, String codigoTasaImpuesto, Boolean articuloCerrado, Boolean articuloGratuito, String tasaImpuesto, Double precioSinImpuesto, Double impuesto, Double valorTasa, String fechaTasa, Double precioOriginal, Boolean periodoActivo, String codigoMarca, String marca, String serialNumberId, String vendedorId, Integer quantity, Boolean descontable, String tipoPrendaCodigo, String tipoPrendaNombre, String generoCodigo, String generoNombre, String categoriaIvaCodigo, String categoriaIvaNombre, int line) {
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

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
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

    public String getArticulo() {
        return articulo;
    }

    public void setArticulo(String articulo) {
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

    public Double getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(Double impuesto) {
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
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

    public void setLine(int line) {
        this.line = line;
    }

    public String getMotivoDevolucionId() {
        return motivoDevolucionId;
    }

    public void setMotivoDevolucionId(String motivoDevolucionId) {
        this.motivoDevolucionId = motivoDevolucionId;
    }

    public String getMotivoDevolucionDesc() {
        return motivoDevolucionDesc;
    }

    public void setMotivoDevolucionDesc(String motivoDevolucionDesc) {
        this.motivoDevolucionDesc = motivoDevolucionDesc;
    }

    public String getMotivoDevolucionFactura() {
        return motivoDevolucionFactura;
    }

    public void setMotivoDevolucionFactura(String motivoDevolucionFactura) {
        this.motivoDevolucionFactura = motivoDevolucionFactura;
    }

    public int getMotivoDevolucionLinea() {
        return motivoDevolucionLinea;
    }

    public void setMotivoDevolucionLinea(int motivoDevolucionLinea) {
        this.motivoDevolucionLinea = motivoDevolucionLinea;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "ean='" + ean + '\'' +
                ", precio=" + precio +
                ", nombre='" + nombre + '\'' +
                ", line=" + line +
                '}';
    }
}
