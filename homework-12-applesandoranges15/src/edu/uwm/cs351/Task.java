package edu.uwm.cs351;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * An abstraction of a task that may have dependencies.
 * The implementation is mostly immutable, but new dependencies may be added
 * as long as the dependencies have not been queried yet.
 *
 */
public class Task {
	private final String name;
	private final Set<Task> dependencies = new HashSet<>(); 
	private boolean initialized;
	
	/**
	 * Create a task with given name
	 * @param name, must not be null
	 */
	public Task(String name) {
		if (name == null) throw new NullPointerException("name is null");
		this.name = name;
	}
	
	/**
	 * Return the name of this task
	 * @return name, never null
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Record that this task depends on its argument.
	 * This can only be called before any ons for the dependencies.
	 * @param t task to depend on, must not be null
	 * @exception IllegalStateException if someone has already asked for the dependencies.
	 */
	public void dependsOn(Task t) {
		if (initialized) throw new IllegalStateException("too late to add dependencies");
		if (t == null) throw new NullPointerException("cannot depend on null");
		dependencies.add(t);
	}
	
	/**
	 * Return all the tasks that this task depends on being done first.
	 * Once this method is called, one cannot add any more dependencies.
	 * @return all dependencies, not modifiable
	 */
	public Set<Task> getDependencies() {
		initialized = true;
		return Collections.unmodifiableSet(dependencies);
	}
	
	@Override // implementation
	public String toString() {
		return name;
	}
}
