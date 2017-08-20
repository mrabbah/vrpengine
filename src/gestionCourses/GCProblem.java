package gestionCourses;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class GCProblem {

	ArrayList<GCTask> tasks;
        //double vehicleCapacity;
        GCTask warehouse;
        String instance_name;
        //Double minStartTime;
	
	
	public GCProblem(String name) {//,double capacity
		instance_name = name;
		//vehicleCapacity = capacity;
		
		tasks = new ArrayList<GCTask>();
                try{
			
                    FileInputStream fstream = new FileInputStream("");
                    DataInputStream in = new DataInputStream(fstream);
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    String strLine;

                    while ((strLine = br.readLine()) != null)   { // read file line by line
                            String[] data = strLine.split("\t");

                                //GCTask task = new GCTask(Integer.parseInt(data[0]), Double.parseDouble(data[1]), Double.parseDouble(data[2]), Double.parseDouble(data[3]), Double.parseDouble(data[4]), Double.parseDouble(data[6]));
                                //tasks.add(task);		
                    }
                    if(warehouse==null){
                        //juste pour le test sinon on affecte le warehouse et les taches au debut 
                        warehouse=tasks.get(0);tasks.remove(0);

                     }
                     GCUtils.sortTasks(tasks, null, GCUtils.TypeSort.minStartTime);
                     //minStartTime=tasks.get(0).getStartTime();
                     
                     in.close(); //Close the input stream
		} catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	/**
	 * Esegue una stampa a video della "mappa" del problema
	 */
	public void show() {
		System.out.println("Problema "+instance_name);
		
	}
	
	public String getInstanceName() {
		return instance_name;
	}
	
	public GCTask getTask(int taskId) {
//		for (GCTask t : tasks)
//			if (t.getId(). == taskId)
//				return t;
		return null;		
	}

        public ArrayList<GCTask> getTasks() {
            return tasks;
        }
	
        
	
//	public double getVehicleCapacity() {
//		return vehicleCapacity;
//	}

        public GCTask getWarehouse() {
            return warehouse;
        }

        public void setWarehouse(GCTask warehouse) {
            this.warehouse = warehouse;
        }
	
	
}