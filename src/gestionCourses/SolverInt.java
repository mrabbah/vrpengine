package gestionCourses;

import java.util.HashMap;
import java.util.List;

public interface SolverInt {
	
	/**
	 * renvoie la liste des taches trie par opptimisation de la route
	 * @param tasks   list de toutes les taches, la tache d indice 0 comme point de depart coursier, et d indice nombre de tache comme point d arriver
	 * @param costsMatrix    la matrice du temps, meme indice de la liste des taches 
	 * @param endTimeSteed  fin chrono coursier
	 * @return
	 */
	public HashMap<String,List<GCTask>> getGCBestSolution(List<GCTask> tasks,double[][] costsMatrix,double restTimeSteed);

}
