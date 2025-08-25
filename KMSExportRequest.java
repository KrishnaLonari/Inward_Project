package com.api.remitGuru.component.util;

public class KMSExportRequest implements ApiRequest {
	
	private String name;
	
	public KMSExportRequest(String name) {
		this.name=name;
	}

	@Override
	public String toJson() {
		return "{\"name\":\"" + name + "\"}";
	}

}
