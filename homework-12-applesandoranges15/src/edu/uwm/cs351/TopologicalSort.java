//Zoya Fatima Ammar
//Received help from Matt, Boyland, Anjali, and Claire Roca and
//https://www.geeksforgeeks.org/python-program-for-topological-sorting/amp/

package edu.uwm.cs351;

import java.util.HashSet;
import java.util.Set;

/** Schedule a collection tasks so that each task is scheduled after anything that it depends on. */
public class TopologicalSort {
    private Schedule schedule;
    private Path cycle;

    /** Perform a topological sort of the given tasks. If a cycle is found, scheduled is terminated
     * early, and a cycle is set.
     *
     * @param tasks (non-null) list of tasks to schedule, none of which may be null */
    public TopologicalSort(Set<Task> tasks) {
        // TODO
        // want to go through all of the tasks in the set
        // if any of the tasks is null, throw a NullPointerException
        // if a cycle is found, set cycle equal to path we are at
        // terminate

        // create a visited Map to indicate whether each Task in
        // map has bee visited or not

        schedule= new Schedule();
        Set<Task> visited= new HashSet<>();

        for (Task t : tasks) {
            cycle= doSort(t, tasks, visited);
            if (cycle != null) break;
        }
    }

    private Path doSort(Task t, Set<Task> domain, Set<Task> visited) {
        if (visited.add(t)) {
            for (Task d : t.getDependencies()) {
                if (!domain.contains(d)) continue;
                Path p= doSort(d, domain, visited);
                if (p == null) continue;
                if (p.isCycle()) return p;
                return p.append(t);
            }
            schedule.append(t);
        } else {
            if (!schedule.contains(t)) { return new DegeneratePath(t); }
        }
        return null;
    }

    // TODO: space for a (temporary) recursive helper method,
    // but you will have to stop using it (see homework description).

    /** Return the schedule of tasks found. If there are no cycles, this schedule will include all
     * tasks.
     *
     * @return schedule, never null */
    public Schedule getSchedule() {
        return schedule;
    }

    /** Return the cycle found while scheduling tasks. If no cycle was found, return null. The path
     * returned will always be a cycle.
     *
     * @return cycle found, or null */
    public Path getCycle() {
        return cycle;
    }
}
