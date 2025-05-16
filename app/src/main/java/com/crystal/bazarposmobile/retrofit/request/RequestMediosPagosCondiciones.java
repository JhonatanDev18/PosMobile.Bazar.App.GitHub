
package com.crystal.bazarposmobile.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestMediosPagosCondiciones {

    @SerializedName("CodigoComercial")
    @Expose
    private List<String> codigoComercial;

    @SerializedName("MediosPago")
    @Expose
    private List<String> mediosPago;

    public RequestMediosPagosCondiciones(List<String> codigoComercial, List<String> mediosPago) {
        this.codigoComercial = codigoComercial;
        this.mediosPago = mediosPago;
    }

    public List<String> getCodigoComercial() {
        return codigoComercial;
    }

    public void setCodigoComercial(List<String> codigoComercial) {
        codigoComercial = codigoComercial;
    }

    public List<String> getMediosPago() {
        return mediosPago;
    }

    public void setMediosPago(List<String> mediosPago) {
        mediosPago = mediosPago;
    }

}
