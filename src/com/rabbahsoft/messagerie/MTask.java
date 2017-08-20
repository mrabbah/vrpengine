package com.rabbahsoft.messagerie;

public class MTask {
	
	Long id;
	Double poids;
	Double volume;
	Double startTime;//horaire debut
	Double endTime;//horaire fin
	Double serviceTime;//dureeTacheEstimee
	Double arrivalTime=0.0;// a remplir au retour de la solution
    Double assignmentTime=0.0;// a remplir au retour de la solution
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Double getPoids() {
		return poids;
	}
	public void setPoids(Double poids) {
		this.poids = poids;
	}
	public Double getVolume() {
		return volume;
	}
	public void setVolume(Double volume) {
		this.volume = volume;
	}
	
	public Double getServiceTime() {
		return serviceTime;
	}
	public void setServiceTime(Double serviceTime) {
		this.serviceTime = serviceTime;
	}
	public Double getArrivalTime() {
		return arrivalTime;
	}
	public void setArrivalTime(Double arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	public Double getAssignmentTime() {
		return assignmentTime;
	}
	public void setAssignmentTime(Double assignmentTime) {
		this.assignmentTime = assignmentTime;
	}
	public Double getStartTime() {
		return startTime;
	}
	public void setStartTime(Double startTime) {
		this.startTime = startTime;
	}
	public Double getEndTime() {
		return endTime;
	}
	public void setEndTime(Double endTime) {
		this.endTime = endTime;
	}
	public MTask(Long id, Double poids, Double volume) {
		super();
		this.id = id;
		this.poids = poids;
		this.volume = volume;
	}
	public String toString() {
		return "id:"+id.toString()+" poids:"+poids.toString()+" volume:"+volume.toString()+" assigneemnt time:"+getAssignmentTime().toString()+" arrivalTime:"+arrivalTime.toString()+" endRealisation time:"+getEndRealizationTime().toString();
	}
	public Double getEndRealizationTime() {
		if(arrivalTime==null || serviceTime==null) return 0.0;
        return arrivalTime+serviceTime;
    }
	

}
