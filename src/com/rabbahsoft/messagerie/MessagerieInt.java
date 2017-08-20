/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rabbahsoft.messagerie;


import java.util.HashMap;
import java.util.List;

/**
 *
 * @author hp_magaz
 */
public interface MessagerieInt {
    
	public List<MVehiculeRoute> getMBestSolution(List<MTask> tasks,Double[][] costsMatrix,HashMap<MTypeVehicule,List<MVehicule>> vehiculeMap,Double heureDebutTravail);

}
