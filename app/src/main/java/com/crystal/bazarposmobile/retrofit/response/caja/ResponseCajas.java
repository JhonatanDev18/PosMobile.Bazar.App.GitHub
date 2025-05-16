
package com.crystal.bazarposmobile.retrofit.response.caja;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseCajas {

    @SerializedName("cajas")
    @Expose
    private List<Caja> cajas = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public ResponseCajas() {
    }

    /**
     * 
     * @param cajas
     */
    public ResponseCajas(List<Caja> cajas) {
        super();
        this.cajas = cajas;
    }

    public List<Caja> getCajas() {
        return cajas;
    }

    public void setCajas(List<Caja> cajas) {
        this.cajas = cajas;
    }

}
