package com.crystal.bazarposmobile.retrofit.response.bancolombiaqr;

import com.google.gson.annotations.SerializedName;

public class MetaBancolombiaQr {

	@SerializedName("_responseSize")
	private Integer responseSize;

	@SerializedName("_clientRequest")
	private String clientRequest;

	@SerializedName("_version")
	private String version;

	@SerializedName("_messageId")
	private String messageId;

	@SerializedName("_requestDate")
	private String requestDate;

	public void setResponseSize(Integer responseSize){
		this.responseSize = responseSize;
	}

	public Integer getResponseSize(){
		return responseSize;
	}

	public void setClientRequest(String clientRequest){
		this.clientRequest = clientRequest;
	}

	public String getClientRequest(){
		return clientRequest;
	}

	public void setVersion(String version){
		this.version = version;
	}

	public String getVersion(){
		return version;
	}

	public void setMessageId(String messageId){
		this.messageId = messageId;
	}

	public String getMessageId(){
		return messageId;
	}

	public void setRequestDate(String requestDate){
		this.requestDate = requestDate;
	}

	public String getRequestDate(){
		return requestDate;
	}

	@Override
 	public String toString(){
		return 
			"Meta{" + 
			"_responseSize = '" + responseSize + '\'' + 
			",_clientRequest = '" + clientRequest + '\'' + 
			",_version = '" + version + '\'' + 
			",_messageId = '" + messageId + '\'' + 
			",_requestDate = '" + requestDate + '\'' + 
			"}";
		}
}