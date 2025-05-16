package com.crystal.bazarposmobile.retrofit.response.bancolombiaqr;

import com.google.gson.annotations.SerializedName;

public class LinksBancolombiaQr {

	@SerializedName("self")
	private String self;

	public void setSelf(String self){
		this.self = self;
	}

	public String getSelf(){
		return self;
	}

	@Override
 	public String toString(){
		return 
			"Links{" + 
			"self = '" + self + '\'' + 
			"}";
		}
}