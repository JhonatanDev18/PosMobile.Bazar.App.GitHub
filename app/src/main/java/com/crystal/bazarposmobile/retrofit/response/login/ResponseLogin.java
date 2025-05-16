
package com.crystal.bazarposmobile.retrofit.response.login;

import com.google.gson.annotations.Expose;

public class ResponseLogin {

    @Expose
    private Boolean esValida;
    @Expose
    private String token;
    @Expose
    private Usuario datos;
    @Expose
    private String mensaje;

    public ResponseLogin(Boolean esValida, String token, Usuario datos, String mensaje) {
        this.esValida = esValida;
        this.token = token;
        this.datos = datos;
        this.mensaje = mensaje;
    }

    public Boolean getEsValida() {
        return esValida;
    }

    public void setEsValida(Boolean esValida) {
        this.esValida = esValida;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Usuario getDatos() {
        return datos;
    }

    public void setDatos(Usuario datos) {
        this.datos = datos;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
