package com.crystal.bazarposmobile.retrofit.response.datafonon6;

import com.crystal.bazarposmobile.common.HeaderTef;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RespuestaCompletaTef {
    @SerializedName("header")
    @Expose
    private HeaderTef header;
    @SerializedName("respuestaTef")
    @Expose
    private CuerpoRespuestaDatafonoN6 respuestaTef;

    public RespuestaCompletaTef(HeaderTef header, CuerpoRespuestaDatafonoN6 respuestaTef) {
        this.header = header;
        this.respuestaTef = respuestaTef;
    }

    public HeaderTef getHeader() {
        return header;
    }

    public void setHeader(HeaderTef header) {
        this.header = header;
    }

    public CuerpoRespuestaDatafonoN6 getRespuestaTef() {
        return respuestaTef;
    }

    public void setRespuestaTef(CuerpoRespuestaDatafonoN6 respuestaTef) {
        this.respuestaTef = respuestaTef;
    }

    @Override
    public String toString() {
        return "RespuestaCompletaTef{" +
                "header=" + header +
                ", respuestaTef=" + respuestaTef +
                '}';
    }
}
