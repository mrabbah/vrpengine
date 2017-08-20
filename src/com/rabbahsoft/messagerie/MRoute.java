package com.rabbahsoft.messagerie;

import java.util.ArrayList;
import java.util.List;

public class MRoute {
	
	List<MTask> tasks;

	public List<MTask> getTasks() {
		return tasks;
	}

	public void setTasks(List<MTask> tasks) {
		this.tasks = tasks;
	}

	public MRoute() {
		super();
		tasks=new ArrayList<MTask>();
	}
	
	public void addMTask(MTask task) {
		if(task==null || tasks==null) return;
		tasks.add(task);
	}
	
	public String toString() {
		return "Taches:"+tasks.toString();
	}
	
}
