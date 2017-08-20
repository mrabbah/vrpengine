package gestionCourses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestSolver {
	
	
	 public static void main(String[] args) {
		 
		 List<GCTask> tasks= new ArrayList<GCTask>();
		 HashMap<String,List<GCTask>> result= new HashMap<String,List<GCTask>>();
//Integer id, Double startTime, Double endTime,Double serviceTime, Integer idSuccessor, Boolean isHead)
		 GCTask task0=new GCTask(0L,null,null,0.0,null,false);
		 tasks.add(task0);
		 GCTask task1=new GCTask(1L,null,null,2.0,2L,true);
		 task1.setStartTime(50400D);
	     task1.setEndTime(54000D);
		 tasks.add(task1);
		 GCTask task2=new GCTask(2L,null,null,2.0,3L,false);
		 task2.setStartTime(50400D);
	     task2.setEndTime(54000D);
		 tasks.add(task2);
		 GCTask task3=new GCTask(3L,null,null,2.0,null,false);
		 tasks.add(task3);
		 task3.setStartTime(50400D);
	     task3.setEndTime(54000D);
		 GCTask task4=new GCTask(4L,null,null,2.0,5L,true);//15.0,100.0
		 tasks.add(task4);
		 
		 task0.setStartTime(50400D);
	     task0.setEndTime(54000D);
		 
		 
		 
		 int taskNbr=tasks.size();
		 Double[][] costsMatrix=new Double[5][5];
                 
                 costsMatrix[0][1]=995.0;
                 costsMatrix[0][2]=1050.0;
                 costsMatrix[0][3]=1461.0;
                 costsMatrix[0][4]=0.0;
                 
                 costsMatrix[1][2]=555.0;
                 costsMatrix[1][3]=875.0;
                 costsMatrix[1][4]=1275.0;
                 
                 costsMatrix[2][3]=513.0;
                 costsMatrix[2][4]=1162.0;
                 
                 costsMatrix[3][4]=1372.0;
	
		 /*for(int i=0; i<taskNbr-1; i++){
			for(int j=i+1; j<taskNbr; j++){
				costsMatrix[i][j]=Math.random();//Math.random()
			}		
		}*/
		 
		 //GCSolver solver=new GCSolver();
		 result=GCSolver.getGCBestSolution(tasks, costsMatrix, 60000D,8*3600D);
		 
		 System.out.println(result);
                 
                 
                 /*GCTask task0=new GCTask(0L,null,null,0.0,null,false);
		 tasks.add(task0);
		 GCTask task1=new GCTask(1L,15.0,50.0,2.0,2L,true);
		 tasks.add(task1);
		 GCTask task2=new GCTask(2L,null,null,2.0,3L,false);
		 tasks.add(task2);
		 GCTask task3=new GCTask(3L,null,null,2.0,null,false);
		 tasks.add(task3);
		 GCTask task4=new GCTask(4L,null,null,2.0,5L,true);//15.0,100.0
		 tasks.add(task4);
		 GCTask task5=new GCTask(5L,null,null,2.0,6L,false);
		 tasks.add(task5);
		 GCTask task6=new GCTask(6L,null,null,2.0,null,false);
		 tasks.add(task6);
		 GCTask task7=new GCTask(7L,null,null,2.0,null,false);
		 tasks.add(task7);
		 GCTask task8=new GCTask(8L,null,null,0.0,null,false);
		 tasks.add(task8);*/
		 
	 }
	
	

}
