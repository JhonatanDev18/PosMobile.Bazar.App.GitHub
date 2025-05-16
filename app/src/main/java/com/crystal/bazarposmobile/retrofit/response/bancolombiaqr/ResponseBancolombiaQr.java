package com.crystal.bazarposmobile.retrofit.response.bancolombiaqr;

import com.google.gson.annotations.SerializedName;

public class ResponseBancolombiaQr{

	@SerializedName("qr")
	private String qrBancolombia;

	@SerializedName("mensaje")
	private String mensaje;

	@SerializedName("error")
	private Boolean error;

	public String getQrBancolombia(){
		return qrBancolombia;
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
			"ResponseBancolombiaQr{" + 
			"qr = '" + qrBancolombia + '\'' +
			",mensaje = '" + mensaje + '\'' + 
			",error = '" + error + '\'' + 
			"}";
		}
}