
package com.crystal.bazarposmobile.retrofit.response.tarjetasbancarias;

import java.util.List;
import com.google.gson.annotations.Expose;


public class ResponseTarjetasBancarias {

    @Expose
    private List<Tarjeta> tarjetas;

    public ResponseTarjetasBancarias(List<Tarjeta> tarjetas) {
        this.tarjetas = tarjetas;
    }

    public List<Tarjeta> getTarjetas() {
        return tarjetas;
    }

    public void setTarjetas(List<Tarjeta> tarjetas) {
        this.tarjetas = tarjetas;
    }

}
