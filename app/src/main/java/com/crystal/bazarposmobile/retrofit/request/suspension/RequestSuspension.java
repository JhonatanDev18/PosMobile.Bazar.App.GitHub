package com.crystal.bazarposmobile.retrofit.request.suspension;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class RequestSuspension{

	@SerializedName("texto")
	private String texto;

	@SerializedName("lineas")
	private List<SuspesionLinea> lineas;

	@SerializedName("cliente")
	private String cliente;

	@SerializedName("tienda")
	private String tienda;

	@SerializedName("caja")
	private String caja;

	@SerializedName("referencia")
	private String referencia;

	public RequestSuspension(String texto, List<SuspesionLinea> lineas, String cliente, String tienda, String caja, String referencia) {
		this.texto = texto;
		this.lineas = lineas;
		this.cliente = cliente;
		this.tienda = tienda;
		this.caja = caja;
		this.referencia = referencia;
	}

	public void setTexto(String texto){
		this.texto = texto;
	}

	public String getTexto(){
		return texto;
	}

	public void setLineas(List<SuspesionLinea> lineas){
		this.lineas = lineas;
	}

	public List<SuspesionLinea> getLineas(){
		return lineas;
	}

	public void setCliente(String cliente){
		this.cliente = cliente;
	}

	public String getCliente(){
		return cliente;
	}

	public void setCaja(String caja){
		this.caja = caja;
	}

	public String getCaja(){
		return caja;
	}

	public void setReferencia(String referencia){
		this.referencia = referencia;
	}

	public String getReferencia(){
		return referencia;
	}

	public String getTienda() {
		return tienda;
	}

	public void setTienda(String tienda) {
		this.tienda = tienda;
	}

	@Override
 	public String toString(){
		return 
			"RequestSuspension{" + 
			"texto = '" + texto + '\'' + 
			",lineas = '" + lineas + '\'' + 
			",cliente = '" + cliente + '\'' +
			",tienda = '" + tienda + '\'' +
			",caja = '" + caja + '\'' +
			",referencia = '" + referencia + '\'' +
			"}";
		}
}