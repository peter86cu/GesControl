package com.ayalait.gesventas.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DetalleArqueo {
	@JsonProperty
	private int id;
	@JsonProperty
	private int value;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
	
	
	

}
