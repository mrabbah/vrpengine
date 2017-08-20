/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rabbahsoft.messagerie;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import jsprit.core.algorithm.VehicleRoutingAlgorithm;
import jsprit.core.algorithm.VehicleRoutingAlgorithmBuilder;
import jsprit.core.problem.VehicleRoutingProblem;
import jsprit.core.problem.VehicleRoutingProblem.FleetSize;
import jsprit.core.problem.cost.VehicleRoutingTransportCosts;
import jsprit.core.problem.job.Service;
import jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import jsprit.core.problem.solution.route.VehicleRoute;
import jsprit.core.problem.solution.route.activity.TimeWindow;
import jsprit.core.problem.solution.route.activity.TourActivity;
import jsprit.core.problem.vehicle.Vehicle;
import jsprit.core.problem.vehicle.VehicleImpl;
import jsprit.core.problem.vehicle.VehicleType;
import jsprit.core.problem.vehicle.VehicleTypeImpl;
import jsprit.core.problem.vehicle.VehicleImpl.Builder;
import jsprit.core.util.Solutions;
import jsprit.core.util.VehicleRoutingTransportCostsMatrix;


/**
 *
 * @author hp_magaz
 */
public class MessagerieSolver {

	
	public static List<MVehiculeRoute> getMBestSolution(List<MTask> tasks,
			Double[][] costsMatrix,
			HashMap<MTypeVehicule, List<MVehicule>> vehiculeMap,Double heureDebutTravail) {
		
		/*
		 * some preparation - create output folder
		 */
		//Examples.createOutputFolder();
				
		final int WEIGHT_INDEX = 0;
		final int VOLUME_INDEX = 1;
		
		List<MVehiculeRoute> result = new ArrayList<MVehiculeRoute>();
		
		int taskNbr=tasks.size();
		
		/**
		 * Matrice Time 
		 */
		
        VehicleRoutingTransportCostsMatrix.Builder costMatrixBuilder = VehicleRoutingTransportCostsMatrix.Builder.newInstance(true);

        for(int i=0; i<taskNbr; i++){
                for(int j=i+1; j<taskNbr; j++){
                        costMatrixBuilder.addTransportTime(""+i, ""+j, costsMatrix[i][j]);
                }		
        }

        VehicleRoutingTransportCosts costMatrix = costMatrixBuilder.build();
		
		/*
    	 * define a builder to build the VehicleRoutingProblem
    	 */
    	VehicleRoutingProblem.Builder vrpBuilder = VehicleRoutingProblem.Builder.newInstance();
    	vrpBuilder.setFleetSize(FleetSize.FINITE).setRoutingCost(costMatrix);
        	
		/*
		 * get a vehicle type-builder and build a type with the typeId "type.getLibelle()" and one capacity dimension, i.e. weight, and capacity dimension value of 2
		 */
    	
    	VehicleTypeImpl.Builder vehicleTypeBuilder;
    	VehicleType vehicleType;
    	HashMap<String, MVehicule> vehicules =new HashMap<String, MVehicule>();
		for (MTypeVehicule type : vehiculeMap.keySet()){
			vehicleTypeBuilder = VehicleTypeImpl.Builder.newInstance(type.getLibelle())
			.addCapacityDimension(WEIGHT_INDEX,type.getTonnage().intValue()).addCapacityDimension(VOLUME_INDEX, type.getVolume().intValue()).setFixedCost(type.getCoutFix()).setCostPerDistance(type.getCoutVariable());
			vehicleType = vehicleTypeBuilder.build();
			
			/*
			 * get a vehicle-builder and build a vehicle located at WareHouse with type "vehicleType"
			 */
			Builder vehicleBuilder;
			Vehicle vehicle;
			//map code MVehicule
			for(MVehicule MVeh : vehiculeMap.get(type)){
				vehicleBuilder = VehicleImpl.Builder.newInstance(MVeh.code).setStartLocationId("0");//type.getLibelle()+"|"+MVeh.code
				vehicleBuilder.setReturnToDepot(true);
				vehicleBuilder.setType(vehicleType);
				if(heureDebutTravail!=null)vehicleBuilder.setEarliestStart(heureDebutTravail);
				vehicle = vehicleBuilder.build();
		    	vrpBuilder.addVehicle(vehicle);
		    	vehicules.put(MVeh.code, MVeh);
			}
		}
		
			
		Service.Builder serviceBuilder;
	    Service service;
	    MTask task;
	    //map indice Mtask
	    HashMap<String, MTask> taskMap= new HashMap<String, MTask>();
	    for (int i=1; i<tasks.size(); i++){
	    	task = tasks.get(i);
	    	serviceBuilder = Service.Builder.newInstance(""+i).addSizeDimension(WEIGHT_INDEX,task.getPoids().intValue())
	    	.addSizeDimension(VOLUME_INDEX, task.getVolume().intValue());
            if(task.getStartTime()!=null && task.getEndTime()!=null)serviceBuilder.setTimeWindow(TimeWindow.newInstance(task.getStartTime(),task.getEndTime()));
	        serviceBuilder.setLocationId(""+i);
	        service = serviceBuilder.build();
	        vrpBuilder.addJob(service);
	        taskMap.put(""+i, task);
	    }
			
	    VehicleRoutingProblem problem = vrpBuilder.build();	
        VehicleRoutingAlgorithmBuilder vraBuilder = new VehicleRoutingAlgorithmBuilder(problem, "algorithmConfig.xml");//input/

        vraBuilder.addCoreConstraints(); 
        vraBuilder.addDefaultCostCalculators(); 	
		
        VehicleRoutingAlgorithm vra = vraBuilder.build();
		
        
        
        /*
		 * TODO return list VehicleRoute
		 */
		Collection<VehicleRoutingProblemSolution> solutions = vra.searchSolutions();
		
		/*
		 * get the best 
		 */
		VehicleRoutingProblemSolution bestSolution = Solutions.bestOf(solutions);
		
		Collection<VehicleRoute> routes=bestSolution.getRoutes();

        if(routes==null || routes.isEmpty()) {       	
        	return result;
        }
        
        
        Iterator<VehicleRoute> itr=routes.iterator();
        VehicleRoute route;
        List<TourActivity> tourActivities;
        Vehicle veh;
        MVehicule mVeh;
        MRoute mRoute;
        MTask mTask;
        MTask predecessorTask=null;
        while (itr.hasNext()){
	    	route=itr.next();
	    	tourActivities=route.getActivities();
	    	veh=route.getVehicle();
	    	mVeh = vehicules.get(veh.getId());
	    	mRoute=new MRoute();
	    	//Integer indexTache;
	    	for (TourActivity activity : tourActivities){  		
	    		mTask= taskMap.get(activity.getLocationId());
	    		mTask.setArrivalTime(activity.getArrTime());
	    		// juste pour la premiere tache arrivee + attente jussqu a time window 
                if(predecessorTask==null && mTask.getStartTime()!=null && (mTask.getStartTime()>activity.getArrTime())) mTask.setArrivalTime(mTask.getStartTime());
                System.out.println("activity="+activity.getArrTime());
                System.out.println("mTask="+mTask.getArrivalTime());
                if(predecessorTask!=null) mTask.setAssignmentTime(predecessorTask.getEndRealizationTime());
                else mTask.setAssignmentTime(heureDebutTravail);
                predecessorTask=mTask;
                mRoute.addMTask(mTask);
	    	}
	    	MVehiculeRoute mVehRoute=new MVehiculeRoute(mVeh,mRoute);
	    	result.add(mVehRoute);
        }
        
        
		
		/*new VrpXMLWriter(problem, solutions).write("output/problem-with-solution.xml");
		
		SolutionPrinter.print(problem,bestSolution,Print.VERBOSE);
		
		new GraphStreamViewer(problem, bestSolution).labelWith(Label.ID).setRenderDelay(200).display();*/
		
		//TODO Auto-generated method stub
		return result;
	}



	
	
    
}
