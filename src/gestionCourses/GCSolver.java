package gestionCourses;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import jsprit.core.algorithm.VehicleRoutingAlgorithm;
import jsprit.core.algorithm.VehicleRoutingAlgorithmBuilder;
import jsprit.core.algorithm.state.StateManager;
import jsprit.core.algorithm.state.StateUpdater;
import jsprit.core.problem.VehicleRoutingProblem;
import jsprit.core.problem.VehicleRoutingProblem.FleetSize;
import jsprit.core.problem.constraint.ConstraintManager;
import jsprit.core.problem.constraint.HardActivityStateLevelConstraint;
import jsprit.core.problem.constraint.HardRouteStateLevelConstraint;
import jsprit.core.problem.constraint.HardActivityStateLevelConstraint.ConstraintsStatus;
import jsprit.core.problem.cost.VehicleRoutingTransportCosts;
import jsprit.core.problem.job.Service;
import jsprit.core.problem.misc.JobInsertionContext;
import jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import jsprit.core.problem.solution.route.VehicleRoute;
import jsprit.core.problem.solution.route.activity.ActivityVisitor;
import jsprit.core.problem.solution.route.activity.TimeWindow;
import jsprit.core.problem.solution.route.activity.TourActivity;
import jsprit.core.problem.solution.route.state.StateFactory;
import jsprit.core.problem.solution.route.state.StateFactory.StateId;
import jsprit.core.problem.vehicle.Vehicle;
import jsprit.core.problem.vehicle.VehicleImpl;
import jsprit.core.problem.vehicle.VehicleType;
import jsprit.core.problem.vehicle.VehicleTypeImpl;
import jsprit.core.problem.vehicle.VehicleImpl.Builder;
import jsprit.core.util.Solutions;
import jsprit.core.util.VehicleRoutingTransportCostsMatrix;
import jsprit.util.Examples;

public class GCSolver {

    static class KeyStatusUpdater implements StateUpdater, ActivityVisitor {

        StateManager stateManager;

        HashMap<String, StateId> stateMap;

        private VehicleRoute route;

        KeyStatusUpdater(StateManager stateManager, HashMap<String, StateId> stateMap) {
            this.stateManager = stateManager;
            this.stateMap = stateMap;
        }

        @Override
        public void begin(VehicleRoute route) {
            this.route = route;
        }

        @Override
        public void visit(TourActivity activity) {
            if (((TourActivity.JobActivity) activity).getJob().getId().contains("Task")) {
                stateManager.putProblemState(stateMap.get(((TourActivity.JobActivity) activity).getJob().getId()), VehicleRoute.class, route);
            }

        }

        @Override
        public void finish() {
        }
    }

    static class GetUseAndDeliverHardRouteContraint implements HardRouteStateLevelConstraint {

        StateManager stateManager;

        HashMap<String, StateId> stateMap;

        public GetUseAndDeliverHardRouteContraint(StateManager stateManager, HashMap<String, StateId> stateMap) {
            this.stateManager = stateManager;
            this.stateMap = stateMap;
        }

        @Override
        public boolean fulfilled(JobInsertionContext iFacts) {
            VehicleRoute firstTaskRoute = null;
            VehicleRoute secondTaskRoute = null;
            VehicleRoute thirdTaskRoute = null;
            VehicleRoute lastTaskRoute = null;
            if (iFacts.getJob().getId().contains("Task")) {
                int lastChar = iFacts.getJob().getId().indexOf("|");
                String idArray = iFacts.getJob().getId().substring(0, lastChar);
                //int idCourse=Integer.parseInt(idArray[0]);
                for (String id : stateMap.keySet()) {
                    if (id.contains(idArray + "|")) {
                        if (id.contains("first")) {
                            firstTaskRoute = stateManager.getProblemState(stateMap.get(id), VehicleRoute.class);
                        }
                        if (id.contains("second")) {
                            secondTaskRoute = stateManager.getProblemState(stateMap.get(id), VehicleRoute.class);
                        }
                        if (id.contains("third")) {
                            thirdTaskRoute = stateManager.getProblemState(stateMap.get(id), VehicleRoute.class);
                        }
                        if (id.contains("last")) {
                            lastTaskRoute = stateManager.getProblemState(stateMap.get(id), VehicleRoute.class);
                        }
                    }
                }

                if (firstTaskRoute != null) {
                    if (firstTaskRoute != iFacts.getRoute()) {
                        return false;
                    }
                }
                if (secondTaskRoute != null) {
                    if (secondTaskRoute != iFacts.getRoute()) {
                        return false;
                    }
                }
                if (thirdTaskRoute != null) {
                    if (thirdTaskRoute != iFacts.getRoute()) {
                        return false;
                    }
                }
                if (lastTaskRoute != null) {
                    if (lastTaskRoute != iFacts.getRoute()) {
                        return false;
                    }
                }
            }
            return true;

        }
    }

    static class GetUseAndDeliverKeySimpleHardActivityConstraint implements HardActivityStateLevelConstraint {

        StateManager stateManager;

        HashMap<String, StateId> stateMap;

        GetUseAndDeliverKeySimpleHardActivityConstraint(StateManager stateManager, HashMap<String, StateId> stateMap) {
            this.stateManager = stateManager;
            this.stateMap = stateMap;
        }

        @Override
        public ConstraintsStatus fulfilled(JobInsertionContext iFacts, TourActivity prevAct, TourActivity newAct, TourActivity nextAct, double prevActDepTime) {
            VehicleRoute firstTaskRoute = null;
            VehicleRoute secondTaskRoute = null;
            VehicleRoute thirdTaskRoute = null;
            VehicleRoute lastTaskRoute = null;

            String firstTaskId = "";
            String secondTaskId = "";
            String thirdTaskId = "";
            String lastTaskId = "";
            if (iFacts.getJob().getId().contains("Task")) {
                int lastChar = iFacts.getJob().getId().indexOf("|");
                String idArray = iFacts.getJob().getId().substring(0, lastChar);
                //int idCourse=Integer.parseInt(idArray[0]);
                for (String id : stateMap.keySet()) {
                    if (id.contains(idArray + "|")) {
                        if (id.contains("first")) {
                            firstTaskRoute = stateManager.getProblemState(stateMap.get(id), VehicleRoute.class);
                            firstTaskId = id;
                        }
                        if (id.contains("second")) {
                            secondTaskRoute = stateManager.getProblemState(stateMap.get(id), VehicleRoute.class);
                            secondTaskId = id;
                        }
                        if (id.contains("third")) {
                            thirdTaskRoute = stateManager.getProblemState(stateMap.get(id), VehicleRoute.class);
                            thirdTaskId = id;
                        }
                        if (id.contains("last")) {
                            lastTaskRoute = stateManager.getProblemState(stateMap.get(id), VehicleRoute.class);
                            lastTaskId = id;
                        }
                    }
                }

                if (isStateKey(newAct, firstTaskId) && isStateKey(prevAct, secondTaskId)) {
                    return ConstraintsStatus.NOT_FULFILLED_BREAK;
                }
                if (isStateKey(newAct, firstTaskId) && isStateKey(prevAct, lastTaskId)) {
                    return ConstraintsStatus.NOT_FULFILLED_BREAK;
                }
                if (isStateKey(newAct, firstTaskId) && isStateKey(prevAct, thirdTaskId)) {
                    return ConstraintsStatus.NOT_FULFILLED_BREAK;
                }

                if (isStateKey(newAct, secondTaskId) && isStateKey(nextAct, firstTaskId)) {
                    return ConstraintsStatus.NOT_FULFILLED;
                }
                if (isStateKey(newAct, secondTaskId) && isStateKey(prevAct, lastTaskId)) {
                    return ConstraintsStatus.NOT_FULFILLED_BREAK;
                }
                if (isStateKey(newAct, secondTaskId) && isStateKey(prevAct, thirdTaskId)) {
                    return ConstraintsStatus.NOT_FULFILLED_BREAK;
                }

                if (isStateKey(newAct, thirdTaskId) && isStateKey(nextAct, firstTaskId)) {
                    return ConstraintsStatus.NOT_FULFILLED;
                }
                if (isStateKey(newAct, thirdTaskId) && isStateKey(nextAct, secondTaskId)) {
                    return ConstraintsStatus.NOT_FULFILLED;
                }
                if (isStateKey(newAct, thirdTaskId) && isStateKey(prevAct, lastTaskId)) {
                    return ConstraintsStatus.NOT_FULFILLED_BREAK;
                }

                if (isStateKey(newAct, lastTaskId) && isStateKey(nextAct, firstTaskId)) {
                    return ConstraintsStatus.NOT_FULFILLED;
                }
                if (isStateKey(newAct, lastTaskId) && isStateKey(nextAct, secondTaskId)) {
                    return ConstraintsStatus.NOT_FULFILLED;
                }
                if (isStateKey(newAct, lastTaskId) && isStateKey(nextAct, thirdTaskId)) {
                    return ConstraintsStatus.NOT_FULFILLED;
                }

                int indexFirstTask = -1;
                int indexSecondTask = -1;
                int indexThirdTask = -1;
                int indexLastTask = -1;
                if (isStateKey(newAct, firstTaskId)) {
                    indexFirstTask = iFacts.getRoute().getActivities().indexOf(prevAct) + 1;

                    if (lastTaskRoute != null) {
                        indexLastTask = getIndexOff(lastTaskId, lastTaskRoute);
                        if (indexFirstTask > indexLastTask) {
                            return ConstraintsStatus.NOT_FULFILLED_BREAK;
                        }
                    }
                    if (thirdTaskRoute != null) {
                        indexThirdTask = getIndexOff(thirdTaskId, thirdTaskRoute);
                        if (indexFirstTask > indexThirdTask) {
                            return ConstraintsStatus.NOT_FULFILLED_BREAK;
                        }
                    }
                    if (secondTaskRoute != null) {
                        indexSecondTask = getIndexOff(secondTaskId, secondTaskRoute);
                        if (indexFirstTask > indexSecondTask) {
                            return ConstraintsStatus.NOT_FULFILLED_BREAK;
                        }
                    }
                }
                if (isStateKey(newAct, secondTaskId)) {
                    indexSecondTask = iFacts.getRoute().getActivities().indexOf(prevAct) + 1;
                    if (lastTaskRoute != null) {
                        indexLastTask = getIndexOff(lastTaskId, lastTaskRoute);
                        if (indexSecondTask > indexLastTask) {
                            return ConstraintsStatus.NOT_FULFILLED_BREAK;
                        }
                    }
                    if (thirdTaskRoute != null) {
                        indexThirdTask = getIndexOff(thirdTaskId, thirdTaskRoute);
                        if (indexSecondTask > indexThirdTask) {
                            return ConstraintsStatus.NOT_FULFILLED_BREAK;
                        }
                    }
                    if (firstTaskRoute != null) {
                        indexFirstTask = getIndexOff(firstTaskId, firstTaskRoute);
                        if (indexFirstTask > indexSecondTask) {
                            return ConstraintsStatus.NOT_FULFILLED;
                        }
                    }
                }
                if (isStateKey(newAct, thirdTaskId)) {
                    indexThirdTask = iFacts.getRoute().getActivities().indexOf(prevAct) + 1;
                    if (lastTaskRoute != null) {
                        indexLastTask = getIndexOff(lastTaskId, lastTaskRoute);
                        if (indexThirdTask > indexLastTask) {
                            return ConstraintsStatus.NOT_FULFILLED_BREAK;
                        }
                    }
                    if (secondTaskRoute != null) {
                        indexSecondTask = getIndexOff(secondTaskId, secondTaskRoute);
                        if (indexSecondTask > indexThirdTask) {
                            return ConstraintsStatus.NOT_FULFILLED;
                        }
                    }
                    if (firstTaskRoute != null) {
                        indexFirstTask = getIndexOff(firstTaskId, firstTaskRoute);
                        if (indexFirstTask > indexThirdTask) {
                            return ConstraintsStatus.NOT_FULFILLED;
                        }
                    }
                }
                if (isStateKey(newAct, lastTaskId)) {
                    indexLastTask = iFacts.getRoute().getActivities().indexOf(prevAct) + 1;
                    if (secondTaskRoute != null) {
                        indexSecondTask = getIndexOff(secondTaskId, secondTaskRoute);
                        if (indexSecondTask > indexLastTask) {
                            return ConstraintsStatus.NOT_FULFILLED;
                        }
                    }
                    if (thirdTaskRoute != null) {
                        indexThirdTask = getIndexOff(thirdTaskId, thirdTaskRoute);
                        if (indexThirdTask > indexLastTask) {
                            return ConstraintsStatus.NOT_FULFILLED;
                        }
                    }
                    if (firstTaskRoute != null) {
                        indexFirstTask = getIndexOff(firstTaskId, firstTaskRoute);
                        if (indexFirstTask > indexLastTask) {
                            return ConstraintsStatus.NOT_FULFILLED;
                        }
                    }
                }
            }

            return ConstraintsStatus.FULFILLED;

        }

        private boolean isStateKey(TourActivity act, String id) {
            if (!(act instanceof TourActivity.JobActivity)) {
                return false;
            }
            return ((TourActivity.JobActivity) act).getJob().getId().equals(id);
        }

        private int getIndexOff(String id, VehicleRoute route) {
            int ans = -1;
            List<TourActivity> list = route.getActivities();
            for (TourActivity activity : list) {
                if (((TourActivity.JobActivity) activity).getJob().getId().equals(id)) {
                    ans = list.indexOf(activity);
                    break;
                }
            }
            return ans;
        }

    }

    public static HashMap<String, List<GCTask>> getGCBestSolution(List<GCTask> tasks, Double[][] costsMatrix, Double restTimeSteed, Double heureDebutTravail) {

        /*
         * some preparation - create output folder
         */
        //Examples.createOutputFolder();
        List<GCTask> routeSolution = new ArrayList<GCTask>();

        HashMap<String, List<GCTask>> restAndResult = new HashMap<String, List<GCTask>>();

        if (tasks == null || tasks.isEmpty() || costsMatrix == null || costsMatrix.length == 0) {
            restAndResult.put("result", tasks);
            restAndResult.put("rest", new ArrayList<GCTask>());
            return restAndResult;
        }
        try {
            // 0 ----> point depart size-----> point arrivee
            int taskNbr = tasks.size();

            HashMap<Integer, Long> affectationIdIndex = new HashMap<Integer, Long>();

            // affectation id task ---------> index i cost Matrix
            for (int i = 0; i < taskNbr; i++) {
                affectationIdIndex.put(i, tasks.get(i).getId());
            }

            /*
             * get a vehicle type-builder and build a type with the typeId 
             */
            //final int WEIGHT_INDEX = 0;
            VehicleTypeImpl.Builder vehicleTypeBuilder = VehicleTypeImpl.Builder.newInstance("vehicleType").setCostPerTime(1);
            VehicleType vehicleType = vehicleTypeBuilder.build();

            /*
             * get a vehicle-builder and build a vehicle located at (10,10) with type "vehicleType"
             */
            Builder vehicleBuilder = VehicleImpl.Builder.newInstance("vehicle").setStartLocationId("0").setEndLocationId("" + (taskNbr - 1));
            //vehicleBuilder.setLatestArrival(endTimeSteed);
            vehicleBuilder.setType(vehicleType);
            if (heureDebutTravail != null) {
                vehicleBuilder.setEarliestStart(heureDebutTravail);
            }
            if (restTimeSteed != null) {
                vehicleBuilder.setLatestArrival(restTimeSteed);
            }
            Vehicle vehicle = vehicleBuilder.build();

            //matrice time or distance
            VehicleRoutingTransportCostsMatrix.Builder costMatrixBuilder = VehicleRoutingTransportCostsMatrix.Builder.newInstance(true);

            for (int i = 0; i < taskNbr - 1; i++) {
                for (int j = i + 1; j < taskNbr; j++) {
                    costMatrixBuilder.addTransportTime("" + i, "" + j, costsMatrix[i][j]);
                }
            }

            VehicleRoutingTransportCosts costMatrix = costMatrixBuilder.build();

            /*
             * again define a builder to build the VehicleRoutingProblem
             */
            VehicleRoutingProblem.Builder vrpBuilder = VehicleRoutingProblem.Builder.newInstance();
            vrpBuilder.setFleetSize(FleetSize.INFINITE).setRoutingCost(costMatrix).addVehicle(vehicle);

            // creation des courses a partir des taches
            HashMap<String, StateId> stateMap = new HashMap<String, StateId>();
            StateId key;
            GCTask task;
            Service.Builder serviceBuilder;
            Service service;
            int index = 0;
            long idCourse = 0;
            for (int i = 1; i < taskNbr - 1; i++) {
                task = tasks.get(i);
                // restart count 0 
	            	/*if(task.getStartTime()!=null && task.getEndTime()!=null){
                 task.setStartTime(task.getStartTime()-heureDebutTravail);
                 task.setEndTime(task.getEndTime()-heureDebutTravail);
                 }*/

                if (task.hasSuccessor()) {
                    System.out.println("creation services a partir des taches hasSuccessor-----------------");
                    index++;
                    if (index == 1) {
                        idCourse = task.getId();
                    }
                    serviceBuilder = Service.Builder.newInstance(idCourse + getIdService(index) + task.getId());//.addSizeDimension(0,30);
                    System.out.println("task id = " + task.getId());
                    System.out.println("LocationId costTime= " + i);
                    System.out.println("start time= " + task.getStartTime());
                    System.out.println("end time= " + task.getEndTime());
                    System.out.println("index= " + index);
                    if (index == 1 && task.getStartTime() != null && task.getEndTime() != null) {
                        serviceBuilder.setTimeWindow(TimeWindow.newInstance(task.getStartTime(), task.getEndTime()));
                    }
                    if (task.getServiceTime() != null) {
                        serviceBuilder.setServiceTime(task.getServiceTime());
                    }
                    serviceBuilder.setLocationId("" + i);
                    service = serviceBuilder.build();
                    vrpBuilder.addJob(service);
                    key = StateFactory.createId(idCourse + getIdService(index) + task.getId());
                    stateMap.put(idCourse + getIdService(index) + task.getId(), key);
                } else {
                    System.out.println("creation services-----------------");
                    System.out.println(i);
                    if (index > 0) {
                        index++;
                        serviceBuilder = Service.Builder.newInstance(idCourse + getIdService(index) + task.getId());
                        key = StateFactory.createId(idCourse + getIdService(index) + task.getId());
                        stateMap.put(idCourse + getIdService(index) + task.getId(), key);
                    } else {
                        serviceBuilder = Service.Builder.newInstance("" + i);//.addSizeDimension(0,30);
                    }
                    System.out.println("task id = " + task.getId());
                    System.out.println("start time" + task.getStartTime());
                    System.out.println("end time" + task.getEndTime());
                    if (index == 0 && task.getStartTime() != null && task.getEndTime() != null) {
                        serviceBuilder.setTimeWindow(TimeWindow.newInstance(task.getStartTime(), task.getEndTime()));
                    }
                    if (task.getServiceTime() != null) {
                        serviceBuilder.setServiceTime(task.getServiceTime());
                    }
                    serviceBuilder.setLocationId("" + i);
                    service = serviceBuilder.build();
                    vrpBuilder.addJob(service);
                    index = 0;
                }

            }
            System.out.println("end creation services-----------------");
            /*
             * build the problem
             * by default, the problem is specified such that FleetSize is INFINITE, i.e. an infinite number of 
             * the defined vehicles can be used to solve the problem
             * by default, transport costs are computed as Euclidean distances
             */
            VehicleRoutingProblem problem = vrpBuilder.build();
            VehicleRoutingAlgorithmBuilder vraBuilder = new VehicleRoutingAlgorithmBuilder(problem, "rr_ta.xml");//input/

            vraBuilder.addCoreConstraints();
            vraBuilder.addDefaultCostCalculators();

            StateManager stateManager = new StateManager(problem.getTransportCosts());

            stateManager.addStateUpdater(new KeyStatusUpdater(stateManager, stateMap));

            ConstraintManager constraintManager = new ConstraintManager(problem, stateManager);
            constraintManager.addConstraint(new GetUseAndDeliverKeySimpleHardActivityConstraint(stateManager, stateMap), ConstraintManager.Priority.CRITICAL);
            constraintManager.addConstraint(new GetUseAndDeliverHardRouteContraint(stateManager, stateMap));

            vraBuilder.setStateAndConstraintManager(stateManager, constraintManager);

            VehicleRoutingAlgorithm vra = vraBuilder.build();

            vra.setNuOfIterations(100);

            /*
             * and search a solution
             */
            Collection<VehicleRoutingProblemSolution> solutions = vra.searchSolutions();//algorithm.searchSolutions();

            /*
             * get the best and create list solution from tourActivities
             */
            VehicleRoutingProblemSolution bestSolution = Solutions.bestOf(solutions);
            Collection<VehicleRoute> routes = bestSolution.getRoutes();

            if (routes == null || routes.isEmpty()) {
                restAndResult.put("result", new ArrayList<GCTask>());
                restAndResult.put("rest", new ArrayList<GCTask>());
                return restAndResult;
            }

            Iterator<VehicleRoute> itr = routes.iterator();
            VehicleRoute route = itr.next();
            List<TourActivity> tourActivities = route.getActivities();
            Long idTache;
            Integer indexTache;
            GCTask taskSolution;
            GCTask predecessorTask = null;
            for (TourActivity activity : tourActivities) {
                indexTache = Integer.parseInt(activity.getLocationId());
                System.out.println(indexTache);
                idTache = affectationIdIndex.get(indexTache);
                System.out.println(idTache);
                taskSolution = GCUtils.getTaskById(tasks, idTache);
                taskSolution.setArrivalTime(activity.getArrTime());
                // juste pour la premiere tache de la course:  arrivee + attente jussqu a time window 
                if (taskSolution.getStartTime() != null && (taskSolution.getStartTime() > activity.getArrTime())) {
                    taskSolution.setArrivalTime(taskSolution.getStartTime());//predecessorTask==null &&
                }
                System.out.println("activity.getArrTime(): " + activity.getArrTime());
                System.out.println("taskSolution.getArrivalTime(): " + taskSolution.getArrivalTime());
                //System.out.println("predecessorTask= "+predecessorTask);
                if (predecessorTask != null) {
                    taskSolution.setAssignmentTime(predecessorTask.getEndRealizationTime());
                } else {
                    taskSolution.setAssignmentTime(taskSolution.getStartTime() - costsMatrix[0][indexTache]);
                }
                System.out.println("AssignmentTime= " + taskSolution.getAssignmentTime());
                routeSolution.add(taskSolution);
                predecessorTask = taskSolution;
                //tasks.remove(taskSolution);
            }
            System.out.println("routeSolution: " + routeSolution);
            //routeSolution=GCUtils.getSubListByRestTimeSteed(routeSolution, restTimeSteed);
            System.out.println("routeSolution: apres  " + routeSolution);
            System.out.println("tasks: " + tasks);
            tasks.remove(0);
            tasks.remove(tasks.size() - 1);
            tasks.removeAll(routeSolution);
            //adaptListTask(tasks,heureDebutTravail);            
            restAndResult.put("result", routeSolution);
            restAndResult.put("rest", tasks);
            System.out.println("rest: " + tasks);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.out.println("pas de reponse engine");
            restAndResult.put("result", new ArrayList<GCTask>());
            adaptListTask(tasks, heureDebutTravail);
            restAndResult.put("rest", tasks);
        }
        //pour le test
        //System.out.println(restAndResult);

        /*new VrpXMLWriter(problem, solutions).write("output/problem-with-solution.xml");

         SolutionPrinter.print(problem,bestSolution,Print.VERBOSE);


         new GraphStreamViewer(problem, bestSolution).labelWith(Label.ID).setRenderDelay(200).display();*/
        return restAndResult;
    }

    public static String getIdService(int index) {
        String ans = "";
        switch (index) {
            case 1:
                ans = "|firstTask|";
                break;
            case 2:
                ans = "|secondTask|";
                break;
            case 3:
                ans = "|thirdTask|";
                break;
            case 4:
                ans = "|lastTask|";
                break;
            default:
                break;
        }
        return ans;
    }

    /**
     * renitialiser les taches pour le prochain coursier pour ne pas refaire les
     * modif deja fait par le moteur
     *
     * @param tasks
     * @param heureDebutTravail
     */
    public static void adaptListTask(List<GCTask> tasks, Double heureDebutTravail) {
        for (GCTask tache : tasks) {
            tache.arrivalTime = 0.0;
            tache.assignmentTime = 0.0;
            if (tache.getStartTime() != null && tache.getEndTime() != null) {
                tache.setStartTime(tache.getStartTime() + heureDebutTravail);
                tache.setEndTime(tache.getEndTime() + heureDebutTravail);
            }
            //tache.endTime = 0.0;
        }
    }

}
