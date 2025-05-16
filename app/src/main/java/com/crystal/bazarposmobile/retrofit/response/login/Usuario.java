
package com.crystal.bazarposmobile.retrofit.response.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Usuario {

    @SerializedName("estado")
    @Expose
    private Boolean estado;
    @SerializedName("grupo")
    @Expose
    private String grupo;
    @SerializedName("usuarioUtil")
    @Expose
    private String usuarioUtil;
    @SerializedName("tiendaPorDefecto")
    @Expose
    private String tiendaPorDefecto;
    @SerializedName("pais")
    @Expose
    private String pais;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Usuario() {
    }

    /**
     * 
     * @param grupo
     * @param estado
     * @param pais
     * @param usuarioUtil
     * @param tiendaPorDefecto
     */
    public Usuario(Boolean estado, String grupo, String usuarioUtil, String tiendaPorDefecto, String pais) {
        super();
        this.estado = estado;
        this.grupo = grupo;
        this.usuarioUtil = usuarioUtil;
        this.tiendaPorDefecto = tiendaPorDefecto;
        this.pais = pais;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getUsuarioUtil() {
        return usuarioUtil;
    }

    public void setUsuarioUtil(String usuarioUtil) {
        this.usuarioUtil = usuarioUtil;
    }

    public String getTiendaPorDefecto() {
        return tiendaPorDefecto;
    }

    public void setTiendaPorDefecto(String tiendaPorDefecto) {
        this.tiendaPorDefecto = tiendaPorDefecto;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

}
