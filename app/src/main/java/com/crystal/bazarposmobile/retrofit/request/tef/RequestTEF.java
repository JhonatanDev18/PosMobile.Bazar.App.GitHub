
package com.crystal.bazarposmobile.retrofit.request.tef;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class RequestTEF {

    @SerializedName("PagosTEF")
    private List<PagosTEF> pagosTEF;

    public RequestTEF(List<PagosTEF> pagosTEF) {
        this.pagosTEF = pagosTEF;
    }

    public List<PagosTEF> getPagosTEF() {
        return pagosTEF;
    }

    public void setPagosTEF(List<PagosTEF> pagosTEF) {
        this.pagosTEF = pagosTEF;
    }

    @Override
    public String toString() {
        return "RequestTEF{" +
                "pagosTEF=" + pagosTEF +
                '}';
    }
}
