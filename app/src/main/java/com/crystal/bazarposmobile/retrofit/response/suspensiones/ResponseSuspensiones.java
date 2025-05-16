package com.crystal.bazarposmobile.retrofit.response.suspensiones;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseSuspensiones{

    @SerializedName("suspensiones")
    private List<SuspensionItem> suspenciones;

    @SerializedName("mensaje")
    private String mensaje;

    @SerializedName("error")
    private Boolean error;

    public void setSuspensiones(List<SuspensionItem> suspenciones){
        this.suspenciones = suspenciones;
    }

    public List<SuspensionItem> getSuspensiones(){
        return suspenciones;
    }

    public void setMensaje(String mensaje){
        this.mensaje = mensaje;
    }

    public String getMensaje(){
        return mensaje;
    }

    public void setError(Boolean error){
        this.error = error;
    }

    public boolean isError(){
        return error;
    }

    @Override
     public String toString(){
        return 
            "ResponseSuspensiones{" + 
            "suspenciones = '" + suspenciones + '\'' + 
            ",mensaje = '" + mensaje + '\'' + 
            ",error = '" + error + '\'' + 
            "}";
        }
}