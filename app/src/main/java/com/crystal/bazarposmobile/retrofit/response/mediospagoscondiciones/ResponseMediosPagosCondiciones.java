
package com.crystal.bazarposmobile.retrofit.response.mediospagoscondiciones;

import com.google.gson.annotations.Expose;

import java.util.List;
public class ResponseMediosPagosCondiciones {

    @Expose
    private List<MediosPagoNoValido> mediosPagoNoValidos;

    public ResponseMediosPagosCondiciones(List<MediosPagoNoValido> mediosPagoNoValidos) {
        this.mediosPagoNoValidos = mediosPagoNoValidos;
    }

    public List<MediosPagoNoValido> getMediosPagoNoValidos() {
        return mediosPagoNoValidos;
    }

    public void setMediosPagoNoValidos(List<MediosPagoNoValido> mediosPagoNoValidos) {
        this.mediosPagoNoValidos = mediosPagoNoValidos;
    }

}
