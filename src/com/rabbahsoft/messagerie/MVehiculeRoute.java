package com.rabbahsoft.messagerie;

public class MVehiculeRoute {
	
	MVehicule vehicule;
	MRoute route;
	
	public MVehicule getVehicule() {
		return vehicule;
	}
	public void setVehicule(MVehicule vehicule) {
		this.vehicule = vehicule;
	}
	public MRoute getRoute() {
		return route;
	}
	public void setRoute(MRoute route) {
		this.route = route;
	}
	public MVehiculeRoute(MVehicule vehicule, MRoute route) {
		super();
		this.vehicule = vehicule;
		this.route = route;
	}
	public String toString() {
		return "vehicule:"+vehicule.toString()+"route:"+route.toString();
	}
			
}
