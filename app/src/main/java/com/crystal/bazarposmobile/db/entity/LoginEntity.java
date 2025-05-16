package com.crystal.bazarposmobile.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.crystal.bazarposmobile.common.Constantes;

@Entity(tableName = Constantes.TABLA_LOGIN)
public class LoginEntity {

    @NonNull
    @PrimaryKey
    private String usuario;

    @NonNull
    private String contrasena;

    private Boolean estado;

    private String grupo;

    private String usuarioUtil;

    private String tiendaPorDefecto;

    private String pais;

    public LoginEntity(@NonNull String usuario, @NonNull String contrasena, Boolean estado, String grupo, String usuarioUtil, String tiendaPorDefecto, String pais) {
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.estado = estado;
        this.grupo = grupo;
        this.usuarioUtil = usuarioUtil;
        this.tiendaPorDefecto = tiendaPorDefecto;
        this.pais = pais;
    }

    @NonNull
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(@NonNull String usuario) {
        this.usuario = usuario;
    }

    @NonNull
    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(@NonNull String contrasena) {
        this.contrasena = contrasena;
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
