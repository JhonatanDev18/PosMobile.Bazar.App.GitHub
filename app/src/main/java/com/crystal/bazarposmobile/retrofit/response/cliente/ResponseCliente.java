package com.crystal.bazarposmobile.retrofit.response.cliente;

import com.google.gson.annotations.SerializedName;

public class ResponseCliente{

    @SerializedName("cliente")
    private Cliente cliente;

    @SerializedName("mensaje")
    private String mensaje;

    @SerializedName("error")
    private Boolean error;

    public void setCliente(Cliente cliente){
        this.cliente = cliente;
    }

    public Cliente getCliente(){
        return cliente;
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
            "ResponseCliente{" + 
            "cliente = '" + cliente + '\'' + 
            ",mensaje = '" + mensaje + '\'' + 
            ",error = '" + error + '\'' + 
            "}";
        }
}