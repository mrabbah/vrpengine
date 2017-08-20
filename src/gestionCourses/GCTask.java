package gestionCourses;

import java.util.ArrayList;

public class GCTask {

		Long id;
		Double positionX;
		Double positionY;
		Double startTime;//horaire début
		Double endTime;//horaire fin
		Double serviceTime;//dureeTacheEstimee
		Long idSuccessor;
		Boolean isHead;// est ce premiere tache de la course?
		Double arrivalTime=0.0;// a remplir au retour de la solution
        Double assignmentTime=0.0;// a remplir au retour de la solution
	
	
		public GCTask(Long id, Double position_x, Double position_y,Double ready_time, Double due_date, Double service_time,Long idSuccessor) {	
			this.id = id;
			positionX = position_x;
			positionY = position_y;
			startTime = ready_time;
			endTime = due_date;
			serviceTime = service_time;
                        this.idSuccessor=idSuccessor;
		}
                
                public GCTask(Long id, double position_x, double position_y, double service_time) {	
			this.id = id;
			positionX = position_x;
			positionY = position_y;
			serviceTime = service_time;
		}
		
		
	    
                 public GCTask(Long id, Double startTime, Double endTime,
				Double serviceTime, Long idSuccessor, Boolean isHead) {
			super();
			this.id = id;
			this.startTime = startTime;
			this.endTime = endTime;
			this.serviceTime = serviceTime;
			this.idSuccessor = idSuccessor;
			this.isHead = isHead;
		}

                public Long getId() {
                    return id;
                }

                public void setId(Long id) {
                    this.id = id;
                }

                public Long getIdSuccessor() {
                    return idSuccessor;
                }

                public void setIdSuccessor(Long idSuccessor) {
                    this.idSuccessor = idSuccessor;
                }

		public Double getPositionX() {
			return positionX;
		}


		public void setPositionX(Double positionX) {
			this.positionX = positionX;
		}


		public Double getPositionY() {
			return positionY;
		}


		public void setPositionY(Double positionY) {
			this.positionY = positionY;
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


		public Double getServiceTime() {
			return serviceTime;
		}


		public void setServiceTime(Double serviceTime) {
			this.serviceTime = serviceTime;
		}
                
		public Boolean getIsHead() {
			return isHead;
		}


		public void setIsHead(Boolean isHead) {
			this.isHead = isHead;
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

                
		public Double getDistanceFromOtherGCTask(GCTask other) {
                    return GCUtils.distance(this,other );
                }
	    
//                public Double getTimeFromOtherGCTask(GCTask other) {
//                    return 3600*getDistanceFromOtherGCTask(other)/GCParameters.averageSpeedMotorCycle;
//                }

                public Double getWindowTimeMesure() {
                    return endTime-startTime;
                }

                public Boolean hasSuccessor() {
                        return (idSuccessor!=null);
                }

                public GCTask getSuccessor(ArrayList<GCTask> tasks) { 
                   GCTask successor=null;
                   for (GCTask task : tasks){
                        if(task.getId().equals(this.idSuccessor)){
                            successor=task;
                            break;
                        }                    
                    }
                    return successor;
                }



                public String toString() {
                    String ans="id: "+id;
                    if(arrivalTime!=null) {
                        ans+="  arrivalTime: "+arrivalTime;
                        if(serviceTime!=null) ans+="  EndRealizationTime: "+getEndRealizationTime();
                    }
                     
                    //if(idSuccessor!=null) ans+="  idSuccsssor: "+idSuccessor;
                   // if(startTime!=null) ans+="  startTime: "+startTime;
                   // if(endTime!=null) ans+="  endTime: "+endTime;
                   // if(isHead!=null) ans+="  isHead: "+isHead;
                    return ans;
                }
		
		public Double getEndRealizationTime() {
                    return arrivalTime+serviceTime;
                }
		
        
               
}
