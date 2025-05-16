
package com.crystal.bazarposmobile.retrofit.response.consecutivofiscal;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ResponseConsecutivoFiscal {

    @Expose
    private String caja;
    @Expose
    private Long consecutivo;
    @Expose
    private Boolean esValida;
    @Expose
    private List<String> mensaje;
    @Expose
    private String prefijo;
    @Expose
    @SerializedName("dato")
    private DatosCC cc;

    public ResponseConsecutivoFiscal(String caja, Long consecutivo, Boolean esValida, List<String> mensaje, String prefijo, DatosCC cc) {
        this.caja = caja;
        this.consecutivo = consecutivo;
        this.esValida = esValida;
        this.mensaje = mensaje;
        this.prefijo = prefijo;
        this.cc = cc;
    }

    public String getCaja() {
        return caja;
    }

    public void setCaja(String caja) {
        this.caja = caja;
    }

    public Long getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(Long consecutivo) {
        this.consecutivo = consecutivo;
    }

    public Boolean getEsValida() {
        return esValida;
    }

    public void setEsValida(Boolean esValida) {
        this.esValida = esValida;
    }

    public List<String> getMensaje() {
        return mensaje;
    }

    public void setMensaje(List<String> mensaje) {
        this.mensaje = mensaje;
    }

    public String getPrefijo() {
        return prefijo;
    }

    public void setPrefijo(String prefijo) {
        this.prefijo = prefijo;
    }

    public DatosCC getCc() {
        return cc;
    }

    public void setCc(DatosCC cc) {
        this.cc = cc;
    }
}
