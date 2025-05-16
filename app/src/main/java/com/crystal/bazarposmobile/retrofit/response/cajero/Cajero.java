
package com.crystal.bazarposmobile.retrofit.response.cajero;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cajero {

    @SerializedName("codigo")
    @Expose
    private String codigo;
    @SerializedName("nombre")
    @Expose
    private String nombre;
    @SerializedName("apellido")
    @Expose
    private String apellido;
    @SerializedName("tienda")
    @Expose
    private String tienda;
    @SerializedName("fecha")
    @Expose
    private String fecha;
    @SerializedName("usuario")
    @Expose
    private String usuario;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Cajero() {
    }

    /**
     * 
     * @param apellido
     * @param nombre
     * @param codigo
     * @param fecha
     * @param usuario
     * @param tienda
     */
    public Cajero(String codigo, String nombre, String apellido, String tienda, String fecha, String usuario) {
        super();
        this.codigo = codigo;
        this.nombre = nombre;
        this.apellido = apellido;
        this.tienda = tienda;
        this.fecha = fecha;
        this.usuario = usuario;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTienda() {
        return tienda;
    }

    public void setTienda(String tienda) {
        this.tienda = tienda;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

}
