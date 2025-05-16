
package com.crystal.bazarposmobile.retrofit.response.cajero;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseCajeros {

    @SerializedName("lista")
    @Expose
    private List<Cajero> lista = null;
    @SerializedName("error")
    @Expose
    private Boolean error;

    /**
     * No args constructor for use in serialization
     * 
     */
    public ResponseCajeros() {
    }

    /**
     * 
     * @param error
     * @param lista
     */
    public ResponseCajeros(List<Cajero> lista, Boolean error) {
        super();
        this.lista = lista;
        this.error = error;
    }

    public List<Cajero> getLista() {
        return lista;
    }

    public void setLista(List<Cajero> lista) {
        this.lista = lista;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

}
