package com.crystal.bazarposmobile.retrofit.response.postdocumento;

import com.google.gson.annotations.SerializedName;

public class ResponsePostDocumento{

    @SerializedName("postDocumento")
    private PostDocumento postDocumento;

    @SerializedName("mensaje")
    private String mensaje;

    @SerializedName("error")
    private boolean error;

    public ResponsePostDocumento(PostDocumento postDocumento, String mensaje, boolean error) {
        this.postDocumento = postDocumento;
        this.mensaje = mensaje;
        this.error = error;
    }

    public void setPostDocumento(PostDocumento postDocumento){
        this.postDocumento = postDocumento;
    }

    public PostDocumento getPostDocumento(){
        return postDocumento;
    }

    public void setMensaje(String mensaje){
        this.mensaje = mensaje;
    }

    public String getMensaje(){
        return mensaje;
    }

    public void setError(boolean error){
        this.error = error;
    }

    public boolean isError(){
        return error;
    }

    @Override
     public String toString(){
        return 
            "ResponsePostDocumento{" + 
            "postDocumento = '" + postDocumento + '\'' + 
            ",mensaje = '" + mensaje + '\'' + 
            ",error = '" + error + '\'' + 
            "}";
        }
}