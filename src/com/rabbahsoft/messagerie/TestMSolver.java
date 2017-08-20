package com.rabbahsoft.messagerie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestMSolver {
	
	public static void main(String[] args) {
		 
		 List<MTask> tasks= new ArrayList<MTask>();
		 List<MVehiculeRoute> result= new ArrayList<MVehiculeRoute>();
		 List<MVehicule> vehicules= new ArrayList<MVehicule>();

		 MTask task0=new MTask(-1L,0.0,0.0);
		 task0.setStartTime(50400.0);
	     task0.setEndTime(60000.0);
		 task0.setServiceTime(600.0);
		 tasks.add(task0);
		 
		 MTask task1=new MTask(4900L,5.0,125.0);
		 task0.setStartTime(50400.0);
	     task0.setEndTime(70000.0);
		 task0.setServiceTime(600.0);
		 tasks.add(task1);
		 
		 MTask task2=new MTask(4904L,4.0,8.0);
		 task0.setStartTime(50400.0);
	     task0.setEndTime(70000.0);
		 task0.setServiceTime(600.0);
		 tasks.add(task2);
		 
//		 MTask task3=new MTask(3L,3.5,0.5);
//		 tasks.add(task3);
		 
		 MTypeVehicule type1=new MTypeVehicule("type1", 100.0,20.0,20.0, 20.0, 20.0, 20.0);
		 
		 MVehicule veh1= new MVehicule("veh1", type1.getLibelle());
		 MVehicule veh2= new MVehicule("veh2", type1.getLibelle());
		 vehicules.add(veh1);vehicules.add(veh2);
		 
		 
		 
		 
		 HashMap<MTypeVehicule, List<MVehicule>> vehiculeMap= new  HashMap<MTypeVehicule, List<MVehicule>>();
		 
		 vehiculeMap.put(type1, vehicules);
		 
		 //int taskNbr=tasks.size();
		 Double[][] costsMatrix=new Double[3][3];
		 	costsMatrix[0][0]=0.0;    
	        costsMatrix[0][1]=1030.0;
	        costsMatrix[0][2]=1103.0;
	        //costsMatrix[0][3]=1461.0;
	        //costsMatrix[0][4]=0.0;
	        costsMatrix[1][0]=989.0;
	        costsMatrix[1][1]=0.0;  
	        costsMatrix[1][2]=818.0;
	         
	        //costsMatrix[1][3]=875.0;
	        //costsMatrix[1][4]=1275.0;
	        
	        //costsMatrix[2][3]=513.0;
	        //costsMatrix[2][4]=1162.0;
	        
	        //costsMatrix[3][4]=1372.0;
	        costsMatrix[2][0]=1119.0;
	        costsMatrix[2][1]=961.0;
	        costsMatrix[2][2]=0.0;
		 
		 result=MessagerieSolver.getMBestSolution(tasks, costsMatrix, vehiculeMap,1000.0);
		 
		 System.out.println(result);
                
		 
	 }

}
