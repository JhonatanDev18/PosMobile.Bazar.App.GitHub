package com.crystal.bazarposmobile.retrofit.response.bancolombiaqr;

import com.google.gson.annotations.SerializedName;

public class DataBancolombiaQr {

	@SerializedName("qrCode")
	private String qrCode;

	@SerializedName("header")
	private HeaderBancolombiaQr headerBancolombiaQr;

	@SerializedName("qrId")
	private String qrId;

	@SerializedName("qrImage")
	private String qrImage;

	public void setQrCode(String qrCode){
		this.qrCode = qrCode;
	}

	public String getQrCode(){
		return qrCode;
	}

	public void setHeaderBancolombiaQr(HeaderBancolombiaQr headerBancolombiaQr){
		this.headerBancolombiaQr = headerBancolombiaQr;
	}

	public HeaderBancolombiaQr getHeaderBancolombiaQr(){
		return headerBancolombiaQr;
	}

	public void setQrId(String qrId){
		this.qrId = qrId;
	}

	public String getQrId(){
		return qrId;
	}

	public void setQrImage(String qrImage){
		this.qrImage = qrImage;
	}

	public String getQrImage(){
		return qrImage;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"qrCode = '" + qrCode + '\'' + 
			",header = '" + headerBancolombiaQr + '\'' +
			",qrId = '" + qrId + '\'' + 
			",qrImage = '" + qrImage + '\'' + 
			"}";
		}
}