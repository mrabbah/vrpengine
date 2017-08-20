package gestionCourses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class GCUtils {
    
        public enum TypeSort {
		nearestFromWarehouse,//plus proche du depot
		minStartTime,//minimum start time
		minTimeWindow//minimun mesure of time window
		
	};
	public static Double distance(double a_x, double a_y, double b_x, double b_y) {
		return Math.sqrt((a_x-b_x)*(a_x-b_x) + (a_y-b_y)*(a_y-b_y));
	}
	public static Double distance(GCTask a, GCTask b) {
		return distance(a.getPositionX(), a.getPositionY(), b.getPositionX(), b.getPositionY());
	}
        
        public static GCTask sortTasks(ArrayList<GCTask> tasks,final GCTask wareHouse,final TypeSort typeSort) {    
            Collections.sort(tasks, new Comparator<GCTask>(){
                @Override
                public int compare(GCTask t1, GCTask t2) {
                     
                   if(typeSort==TypeSort.minStartTime) {
                        return -1*t1.getStartTime().compareTo(t2.getStartTime());        
                   }
                   if(typeSort==TypeSort.minTimeWindow) {
                        return -1*t1.getWindowTimeMesure().compareTo(t2.getWindowTimeMesure());        
                   }
                   return t1.getDistanceFromOtherGCTask(wareHouse).compareTo(t2.getDistanceFromOtherGCTask(wareHouse));        

                }
            });       
            return tasks.get(0);
	}
        
        public static Double calculateSaving(GCTask task,GCTask wareHouse,GCTask otherTask){
            
            Double saving=task.getDistanceFromOtherGCTask(wareHouse)+otherTask.getDistanceFromOtherGCTask(wareHouse)
                                -task.getDistanceFromOtherGCTask(otherTask); 
            return saving;
        }
        
        public static GCTask getTaskByLargestSaving(GCTask task,ArrayList<GCTask> tasks,GCTask wareHouse){
            
            HashMap< Double, GCTask> savingsMap = new HashMap< Double, GCTask>();
            Double saving;
            for (GCTask candidateTask : tasks){
                saving=calculateSaving(task,wareHouse,candidateTask);
                savingsMap.put(saving,candidateTask);
            }
            Double maxKey=Collections.max(savingsMap.keySet());
            return savingsMap.get(maxKey);
        }
        
//        public static ArrayList<GCTask> getTasksWithSpecificTimeStart(ArrayList<GCTask> tasks,Double timeStart){
//            ArrayList<GCTask> subset=new ArrayList<GCTask>();
//            for (GCTask task : tasks){
//               if(task.getStartTime().equals(timeStart))
//                   subset.add(task);
//            }
//            
//            if(subset.isEmpty()) subset=getTasksWithSpecificTimeStart(tasks,timeStart+GCParameters.stepTime);
//            return subset;
//        }
        
       /* public static ArrayList<GCTask> getNeighborhoodTasks(GCTask task,ArrayList<GCTask> tasks,Double neighborhoodRadiusTime){
            ArrayList<GCTask> subset=new ArrayList<GCTask>();
            for (GCTask candidateTask : tasks){
               if(candidateTask.isHead && task.getTimeFromOtherGCTask(candidateTask)<=neighborhoodRadiusTime)
                   subset.add(candidateTask);
                   if(candidateTask.hasSuccessor())subset.addAll(getSuccessorTasks(candidateTask,tasks));
            }
            return subset;
        }*/
        public static ArrayList<GCTask> getSuccessorTasks(GCTask task,ArrayList<GCTask> tasks){
            ArrayList<GCTask> successors=new ArrayList<GCTask>();
            int index=tasks.indexOf(task)+1;
            for (int i=index; i<tasks.size(); i++){
               successors.add(tasks.get(i));
               if(!tasks.get(i+1).hasSuccessor()) break;
            }
            return successors;
        }
        
        
        
        
        
        /*public static Double testTolerance(GCTask task,GCTask successor,GCTask candidateTask){
            Double ans=-1.0;
            Double timeToSuccessor=task.getTimeFromOtherGCTask(successor); 
            Double addTime=calculeTolerance(task,successor,candidateTask);
            if ((timeToSuccessor+timeToSuccessor*GCParameters.toleranceMargin)>addTime) ans=addTime; 
            return ans;
        }
        
        public static Double calculeTolerance(GCTask task,GCTask successor,GCTask candidateTask){ 
            return task.getTimeFromOtherGCTask(candidateTask)+candidateTask.getDistanceFromOtherGCTask(successor)+candidateTask.getServiceTime();     
        }
        
        public static GCTask getTaskByTestTolerance(GCTask task,ArrayList<GCTask> tasks,GCTask successor){
            HashMap< Double, GCTask> toleranceMap = new HashMap< Double, GCTask>();
            ArrayList<GCTask> tasksClone=(ArrayList)tasks.clone() ;
            tasksClone.remove(task);tasksClone.remove(successor);
            Double addTime;
            for (GCTask candidateTask : tasksClone){
                addTime=testTolerance(task,successor,candidateTask);
                if(addTime>0) toleranceMap.put(addTime,candidateTask);
            }  
            Double minKey=Collections.min(toleranceMap.keySet());
            return toleranceMap.get(minKey);
        }*/
        
        /**
         * partie GCsolver
         * @param tasks
         * @param id
         * @return
         */
        
        public static GCTask getTaskById(List<GCTask> tasks,Long id){
        	GCTask ans=null;
    		for (GCTask candidateTask : tasks){
    			if(candidateTask.getId().equals(id)){
    				ans=candidateTask;
    				break;
    			}			
    		}
        	return ans;
        }
        
        public static List<GCTask> getSubListByRestTimeSteed(List<GCTask> tasks,double restTimeSteed){
            List<GCTask> ans=new ArrayList<GCTask>();
            List<Long> listIdSuccessor=new ArrayList<Long>();
            //Integer idEndTask=null;
            for (GCTask candidateTask : tasks){
                if(candidateTask.getEndRealizationTime()!=null && candidateTask.getEndRealizationTime()>restTimeSteed && listIdSuccessor.isEmpty()){
                        //idEndTask=tasks.indexOf(candidateTask);
                        break;
                }else{
                    ans.add(candidateTask);
                    if(listIdSuccessor.contains(candidateTask.getId())){
                        listIdSuccessor.remove(candidateTask.getId());
                    }
                    if(candidateTask.hasSuccessor()){
                        listIdSuccessor.add(candidateTask.getIdSuccessor());
                        //System.out.println("IdSuccessor: "+candidateTask.getIdSuccessor());
                    }
                }   			
            }
            /*if(idEndTask!=null) ans=tasks.subList(0, idEndTask);
            else ans=tasks;*/
            return ans;
        }
               
}
