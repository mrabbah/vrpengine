package gestionCourses;

import java.util.ArrayList;


public class GCRoute {    
    
    
		ArrayList<GCTask> tasks;
        GCTask wareHouse;
        Double travalDistance;
        Double travelTime;

        public GCRoute(GCTask wareHouse) {
            this.wareHouse = wareHouse;
            tasks=new ArrayList<GCTask>();
            travalDistance=0.0;
            travelTime=0.0;
        }

        
        
        public ArrayList<GCTask> getTasks() {
            return tasks;
        }

        public void setTasks(ArrayList<GCTask> tasks) {
            this.tasks = tasks;
        }

        public Double getTravalDistance() {
            return travalDistance;
        }

        public void setTravalDistance(Double travalDistance) {
            this.travalDistance = travalDistance;
        }

        public Double getTravelTime() {
            return travelTime;
        }

        public void setTravelTime(Double travelTime) {
            this.travelTime = travelTime;
        }

        

//        public void addTask(GCTask task) {
//            tasks.add(task);
//            addTime(task);
//        }
//        private void addTime(GCTask task) {
//            Double timeToAdd=task.getServiceTime();
//            if(tasks.isEmpty()){
//                timeToAdd+=task.getTimeFromOtherGCTask(wareHouse);
//            }else{
//               GCTask prevTask=tasks.get(tasks.size()-1);
//               timeToAdd+=task.getTimeFromOtherGCTask(prevTask);
//            } 
//            travelTime+=timeToAdd;
//        }
    

        
	
}
