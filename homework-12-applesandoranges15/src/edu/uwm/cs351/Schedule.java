package edu.uwm.cs351;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * A plan to perform tasks in which no task is listed until
 * after all its dependencies (that are subject to scheduling).
 */
public class Schedule {
	private LinkedList<Task> order;
	private Map<Task,Integer> index;
	private Set<Task> dependencies;
	private int lo, hi = 0;
	
	private static Consumer<String> reporter = (s) -> { System.err.println("Invariant error: " + s); };
	
	private boolean report(String error) {
		reporter.accept(error);
		return false;
	}

	private boolean wellFormed() {
		if (order == null) return report("order is null");
		if (index == null) return report("index is null");
		if (dependencies == null) return report("dependencies set is null");
		if (lo > hi) return report("lo=" + lo + "is greater than hi=" + hi);
		for (Integer pri : index.values()) {
			if (pri == null) return report("Found null index for task");
			if (pri < lo || pri >= hi) return report("Found index out of bounds: " + pri);
		}
		if (index.size() != hi - lo) {
			return report("index has " + index.size() + " elements, but there are only " + (hi-lo) + " levels");
		}
		int i = lo;
		Set<Task> nonLocal = new HashSet<>();
		for (Task t : order) {
			if (t == null) return report("schedule item is null");
			if (index.get(t) != i) return report("index is inconsistent"); 
			for (Task d : t.getDependencies()) {
				if (index.containsKey(d)) {
					if (index.get(d) >= i) {
						return report("task " + t + " depends on later task " + d);						
					}
				} else {
					nonLocal.add(d);
				}
			}
			++i;
		}
		if (i != hi) return report("hi is invalid");
		if (!nonLocal.equals(dependencies)) {
			return report("external dependencies are " + nonLocal + " not " + dependencies);
		}
		return true;
	}
	
	/**
	 * Create an empty schedule.
	 */
	public Schedule() {
		order = new LinkedList<>();
		index = new HashMap<>();
		dependencies = new HashSet<>();
		assert wellFormed() : "invariant broken by constructor";
	}
	
	/**
	 * Return whether the task has already been scheduled.
	 * @param x task to check
	 * @return whether the task has already been scheduled.
	 */
	public boolean contains(Task x) {
		assert wellFormed() : "invariant broken in contains";
		return index.containsKey(x);
	}
	
	/**
	 * Add this task before all other elements currently in the schedule.
	 * @param t task to add, must not be null
	 * @exception IllegalArgumentException if the task depends on something in the schedule, or
	 * if it depends on itself.
	 */
	public void prepend(Task t) {
		assert wellFormed() : "invariant broken before prepend";
		Set<Task> newDeps = new HashSet<>();
		for (Task d : t.getDependencies()) {
			if (d == t) {
				throw new IllegalArgumentException("self dependency fequires a mutual task group");
			}
			Integer sch = index.get(d);
			if (sch != null) {
				throw new IllegalArgumentException("dependency is scheduled later " + d);
			}	
			newDeps.add(d);
		}
		--lo;
		index.put(t, lo);
		dependencies.remove(t);
		dependencies.addAll(newDeps);
		order.addFirst(t);
		assert wellFormed() : "invariant broken after prepend";
	}
	
	/**
	 * Add a task to the end of the schedule.
	 * @param t task to add, must not be null
	 * @exception IllegalArgumentException if something already scheduled depends on this task,
	 * or if the task depends on itself.
	 */
	public void append(Task t) {
		assert wellFormed() : "invariant broken before append";
		if (dependencies.contains(t)) {
			throw new IllegalArgumentException("Cannot schedule " + t + " after a scheduled task which depends on it");
		}
		if (t.getDependencies().contains(t)) {
			throw new IllegalArgumentException("cannot schedule something that depends on itself");
		}
		index.put(t, hi);
		++hi;
		order.addLast(t);
		for (Task d : t.getDependencies()) {
			if (!index.containsKey(d)) {
				dependencies.add(d);
			}
		}
		assert wellFormed() : "invariant broken after prepend";		
	}
	
	/**
	 * Return the schedule as a list of tasks to be done in order.
	 * @return a list that cannot be modified.
	 */
	public List<Task> asList() {
		assert wellFormed() : "invariant broken in asList";
		return Collections.unmodifiableList(order);
	}
}
