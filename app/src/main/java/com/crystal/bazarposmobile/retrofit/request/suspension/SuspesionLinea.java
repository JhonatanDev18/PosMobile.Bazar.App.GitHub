package com.crystal.bazarposmobile.retrofit.request.suspension;

import com.google.gson.annotations.SerializedName;

public class SuspesionLinea {

	@SerializedName("ean")
	private String ean;

	@SerializedName("cantidad")
	private Integer cantidad;

	@SerializedName("orden")
	private Integer orden;

	public SuspesionLinea(String ean, Integer cantidad, Integer orden) {
		this.ean = ean;
		this.cantidad = cantidad;
		this.orden = orden;
	}

	public void setEan(String ean){
		this.ean = ean;
	}

	public String getEan(){
		return ean;
	}

	public void setCantidad(Integer cantidad){
		this.cantidad = cantidad;
	}

	public Integer getCantidad(){
		return cantidad;
	}

	public void setOrden(Integer orden){
		this.orden = orden;
	}

	public Integer getOrden(){
		return orden;
	}

	@Override
 	public String toString(){
		return 
			"LineasItem{" + 
			"ean = '" + ean + '\'' + 
			",cantidad = '" + cantidad + '\'' + 
			",orden = '" + orden + '\'' + 
			"}";
		}
}