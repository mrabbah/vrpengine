package gestionCourses;

import java.util.ArrayList;



import java.util.Collection;

import jsprit.analysis.toolbox.GraphStreamViewer;
import jsprit.analysis.toolbox.GraphStreamViewer.Label;
import jsprit.analysis.toolbox.SolutionPrinter;
import jsprit.analysis.toolbox.SolutionPrinter.Print;
import jsprit.core.algorithm.VehicleRoutingAlgorithm;
import jsprit.core.algorithm.box.SchrimpfFactory;
import jsprit.core.problem.VehicleRoutingProblem;
import jsprit.core.problem.io.VrpXMLWriter;
import jsprit.core.problem.job.Service;
import jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import jsprit.core.problem.vehicle.Vehicle;
import jsprit.core.problem.vehicle.VehicleImpl;
import jsprit.core.problem.vehicle.VehicleImpl.Builder;
import jsprit.core.problem.vehicle.VehicleType;
import jsprit.core.problem.vehicle.VehicleTypeImpl;
import jsprit.core.util.Coordinate;
import jsprit.core.util.Solutions;
import jsprit.util.Examples;





public class GCTest {
	
	public static void main(String[] args) throws InterruptedException {
		/*GCProblem problem = new GCProblem("C102");//"C101"
		//problem.show();
		GCSolver solver = new GCSolver();
		//solver.activateDebugMode();
		//solver.activateDrawingSolutionsMode();
		System.out.println("* commencer l'optimisation *");
		final GCSolution solution = solver.resolve(problem);
		System.out.println("* fin de l'optimisation *");
		solution.show();*/
            /*
		 * some preparation - create output folder
		 /*
		 * some preparation - create output folder
		 */
		Examples.createOutputFolder();
		
		/*
		 * get a vehicle type-builder and build a type with the typeId "vehicleType" and one capacity dimension, i.e. weight, and capacity dimension value of 2
		 */
		final int WEIGHT_INDEX = 0;
		VehicleTypeImpl.Builder vehicleTypeBuilder = VehicleTypeImpl.Builder.newInstance("vehicleType").addCapacityDimension(WEIGHT_INDEX, 2);
		VehicleType vehicleType = vehicleTypeBuilder.build();
		
		/*
		 * get a vehicle-builder and build a vehicle located at (10,10) with type "vehicleType"
		 */
		Builder vehicleBuilder = VehicleImpl.Builder.newInstance("vehicle");
		vehicleBuilder.setStartLocationCoordinate(Coordinate.newInstance(10, 10));
		vehicleBuilder.setType(vehicleType);
		Vehicle vehicle = vehicleBuilder.build();
		
		/*
		 * build services at the required locations, each with a capacity-demand of 1.
		 */
		Service service1 = Service.Builder.newInstance("1").addSizeDimension(WEIGHT_INDEX, 1).setCoord(Coordinate.newInstance(5, 7)).build();
		Service service2 = Service.Builder.newInstance("2").addSizeDimension(WEIGHT_INDEX, 1).setCoord(Coordinate.newInstance(5, 13)).build();
		
		Service service3 = Service.Builder.newInstance("3").addSizeDimension(WEIGHT_INDEX, 1).setCoord(Coordinate.newInstance(15, 7)).build();
		Service service4 = Service.Builder.newInstance("4").addSizeDimension(WEIGHT_INDEX, 1).setCoord(Coordinate.newInstance(15, 13)).build();
		
		
		VehicleRoutingProblem.Builder vrpBuilder = VehicleRoutingProblem.Builder.newInstance();
		vrpBuilder.addVehicle(vehicle);
		vrpBuilder.addJob(service1).addJob(service2).addJob(service3).addJob(service4);
		
		VehicleRoutingProblem problem = vrpBuilder.build();
		
		/*
		 * get the algorithm out-of-the-box. 
		 */
		VehicleRoutingAlgorithm algorithm = new SchrimpfFactory().createAlgorithm(problem);
		
		/*
		 * and search a solution
		 */
		Collection<VehicleRoutingProblemSolution> solutions = algorithm.searchSolutions();
		
		/*
		 * get the best 
		 */
		VehicleRoutingProblemSolution bestSolution = Solutions.bestOf(solutions);
		
		new VrpXMLWriter(problem, solutions).write("output/problem-with-solution.xml");
		
		SolutionPrinter.print(problem,bestSolution,Print.VERBOSE);
		
		/*
		 * plot
		 */
//		SolutionPlotter.plotSolutionAsPNG(problem, bestSolution, "output/solution.png", "solution");
		
		new GraphStreamViewer(problem, bestSolution).labelWith(Label.ID).setRenderDelay(200).display();
            
            
            
            
		
	}

	
	/*public GCSolution resolve(GCProblem problem){
            GCTask wareHouse=problem.getWarehouse();
            GCSolution solution =new GCSolution();
            GCRoute route =new GCRoute(wareHouse);
            ArrayList<GCTask> tasks=problem.tasks;
            // first task la plus proche du depot
            GCTask firstTask=getFirstTask(tasks,wareHouse);
            pathComplete(firstTask,tasks,wareHouse,route);
            solution.setRoute(route);
        
            return solution;   
	}*/
        
        /*public void pathComplete(GCTask task,ArrayList<GCTask> tasks,GCTask wareHouse,GCRoute route){
       
            tasks.remove(task);
            route.addTask(task); 
            if(task.hasSuccessor()){//&& task.isHead
               GCTask successor=task.getSuccessor(tasks);
              
            }else{
                tasks.remove(task);
                route.addTask(task); 
                //candidateTask=GCUtils.getTaskByLargestSaving(task, tasks, wareHouse); 
            } 
             //pathComplete(candidateTask,tasks,wareHouse,route);        
	}
  
        public GCTask addSuccessorsAndToleranceTask(GCTask task,GCTask successor,ArrayList<GCTask> tasks,GCRoute route){ 
            GCTask toleranceTask=GCUtils.getTaskByTestTolerance(task, tasks, successor);
            tasks.remove(toleranceTask);
            route.addTask(toleranceTask);
            //addSuccessorsAndToleranceTask(toleranceTask,successor,tasks,route);
            return null;
	}
        
        
        public GCTask savingsHeuristic(GCTask task,ArrayList<GCTask> tasks,GCTask wareHouse,GCRoute route){   
            tasks.remove(task);
            route.addTask(task); 
            if(route.getTravelTime()<GCParameters.coursierEndTime && !tasks.isEmpty()){
                GCTask candidateTask=GCUtils.getTaskByLargestSaving(task, tasks, wareHouse);
                savingsHeuristic(candidateTask,tasks,wareHouse,route);
            }
            return task;
	}
        
        public GCTask getFirstTask(ArrayList<GCTask> tasks,GCTask wareHouse){   
            GCTask firstTask=GCUtils.sortTasks(tasks, wareHouse, GCUtils.TypeSort.nearestFromWarehouse);
            return firstTask;   
	}
        
        
        
        
        
        
        
        
        
        public void pathComplete1(GCTask task,ArrayList<GCTask> tasks,GCTask wareHouse,GCRoute route){   
              //add task and all successor and remove all from tasks lst
            GCTask lastSuccessor=addTaskWithSuccessorsToRoute(task,tasks,route);
              // C&W Savings Heuristic
            GCTask candidateTask=GCUtils.getTaskByLargestSaving(lastSuccessor, tasks, wareHouse);
            
            if(tasks!=null && tasks.size()>0) pathComplete(candidateTask,tasks,wareHouse,route);
	}
        
        public void pathComplete2(GCTask task,ArrayList<GCTask> tasks,GCTask wareHouse,GCRoute route){
            GCTask candidateTask;
            if(task.hasSuccessor()){//&& task.isHead
                // la tolerance GCParameters.neighborhoodRadiusTime
               
               ArrayList<GCTask> nearestTasks=GCUtils.getNeighborhoodTasks(task, tasks, GCParameters.neighborhoodRadiusTime);
               nearestTasks.add(0, task);
               candidateTask=savingsHeuristic(task,nearestTasks,wareHouse,route);
               tasks.removeAll(nearestTasks);
            }else{
                tasks.remove(task);
                route.addTask(task); 
                candidateTask=GCUtils.getTaskByLargestSaving(task, tasks, wareHouse); 
            } 
             pathComplete(candidateTask,tasks,wareHouse,route);        
	}
        
	
        // fonction recurssive
	public GCTask addTaskWithSuccessorsToRoute(GCTask task,ArrayList<GCTask> tasks,GCRoute route){ 
            tasks.remove(task);
            route.addTask(task);          
            if(task.hasSuccessor()){
                 GCTask successor=task.getSuccessor(tasks);
                 addTaskWithSuccessorsToRoute(successor,tasks,route);
            }
            return task;
	}*/
        	

}
