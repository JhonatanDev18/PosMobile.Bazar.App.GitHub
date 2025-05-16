package com.crystal.bazarposmobile.retrofit.response.bancolombiaqr;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QrBancolombia {

	@SerializedName("data")
	private List<DataBancolombiaQr> data;

	@SerializedName("meta")
	private MetaBancolombiaQr metaBancolombiaQr;

	@SerializedName("links")
	private LinksBancolombiaQr linksBancolombiaQr;

	public void setData(List<DataBancolombiaQr> data){
		this.data = data;
	}

	public List<DataBancolombiaQr> getData(){
		return data;
	}

	public void setMetaBancolombiaQr(MetaBancolombiaQr metaBancolombiaQr){
		this.metaBancolombiaQr = metaBancolombiaQr;
	}

	public MetaBancolombiaQr getMetaBancolombiaQr(){
		return metaBancolombiaQr;
	}

	public void setLinksBancolombiaQr(LinksBancolombiaQr linksBancolombiaQr){
		this.linksBancolombiaQr = linksBancolombiaQr;
	}

	public LinksBancolombiaQr getLinksBancolombiaQr(){
		return linksBancolombiaQr;
	}

	@Override
 	public String toString(){
		return 
			"Qr{" + 
			"data = '" + data + '\'' + 
			",meta = '" + metaBancolombiaQr + '\'' +
			",links = '" + linksBancolombiaQr + '\'' +
			"}";
		}
}