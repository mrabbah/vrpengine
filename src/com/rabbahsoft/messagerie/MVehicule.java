package com.rabbahsoft.messagerie;

public class MVehicule {
	
	String code;
	String type; //libelle type vehicule
	
	public MVehicule(String code, String type) {
		super();
		this.code = code;
		this.type = type;
	}

	public String toString() {
		return "code:"+code.toString()+" type:"+type.toString();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
	
	    
}
