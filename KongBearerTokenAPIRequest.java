package com.api.remitGuru.component.util;

public class KongBearerTokenAPIRequest implements ApiRequest{
	
	String grantType;
	
	public KongBearerTokenAPIRequest(String grantType) {
		this.grantType=grantType;
	}

	@Override
	public String toJson() {
		return "{\"grantType\":\"" + grantType + "\"}";
	}

}
