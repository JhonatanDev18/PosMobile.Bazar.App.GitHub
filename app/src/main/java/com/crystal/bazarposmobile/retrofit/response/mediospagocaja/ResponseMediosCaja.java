
package com.crystal.bazarposmobile.retrofit.response.mediospagocaja;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseMediosCaja {

    @SerializedName("MediosCaja")
    @Expose
    private List<MediosCaja> mediosCaja;

    public ResponseMediosCaja(List<MediosCaja> mediosCaja) {
        this.mediosCaja = mediosCaja;
    }

    public List<MediosCaja> getMediosCaja() {
        return mediosCaja;
    }

    public void setMediosCaja(List<MediosCaja> mediosCaja) {
        this.mediosCaja = mediosCaja;
    }

}
