package com.rabbahsoft.messagerie;

public class MTypeVehicule {
	
	String libelle;
	Double tonnage;
    Double largeur;
    Double hauteur;
    Double longueur;
    Double coutVariable;
    Double coutFix;
    
    
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	public Double getTonnage() {
		return tonnage;
	}
	public void setTonnage(Double tonnage) {
		this.tonnage = tonnage;
	}
	public Double getLargeur() {
		return largeur;
	}
	public void setLargeur(Double largeur) {
		this.largeur = largeur;
	}
	public Double getHauteur() {
		return hauteur;
	}
	public void setHauteur(Double hauteur) {
		this.hauteur = hauteur;
	}
	public Double getLongueur() {
		return longueur;
	}
	public void setLongueur(Double longueur) {
		this.longueur = longueur;
	}
	public Double getCoutVariable() {
		return coutVariable;
	}
	public void setCoutVariable(Double coutVariable) {
		this.coutVariable = coutVariable;
	}
	public Double getCoutFix() {
		return coutFix;
	}
	public void setCoutFix(Double coutFix) {
		this.coutFix = coutFix;
	}
    
	public Double getVolume() {
		if(largeur==null || hauteur==null || longueur==null) return 0.0;
		return longueur*hauteur*largeur;
	}
	public MTypeVehicule(String libelle, Double tonnage, Double largeur,
			Double hauteur, Double longueur, Double coutVariable, Double coutFix) {
		super();
		this.libelle = libelle;
		this.tonnage = tonnage;
		this.largeur = largeur;
		this.hauteur = hauteur;
		this.longueur = longueur;
		this.coutVariable = coutVariable;
		this.coutFix = coutFix;
	}
	public String toString() {
		return "libelle:"+libelle.toString();
	}
	
	
	
    
}
