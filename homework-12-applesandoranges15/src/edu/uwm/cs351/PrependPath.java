//Zoya Fatima Ammar
//Received help from Matt, Boyland, Anjali, and Claire Roca

package edu.uwm.cs351;

import java.util.Stack;

/** A path created by adding a task before an existing path. */
public class PrependPath extends Path {
    private final Path rest;
    private final Task task;
    private final Task last;
    private final int size;

    /** Create a path that has a task before another path
     *
     * @param t task to put before
     * @param p path to put the task before */
    public PrependPath(Task t, Path p) {
        if (t == null) throw new NullPointerException("task cannot be null");
        if (!p.getFirst().getDependencies().contains(t)) {
            throw new IllegalArgumentException("path is not connected to task");
        }
        task= t;
        rest= p;
        last= rest.getLast();
        size= p.size() + 1;
    }

    @Override // required
    public int size() {
        return size;
    }

    @Override // required
    public Task getFirst() {
        return task;
    }

    @Override // required
    public Task getLast() {
        return last;
    }

    @Override // required
    protected void toArrayHelper(Stack<Work> worklist, Task[] array, int index) {
        array[index]= task;
        worklist.push(new Work(rest, index + 1));
    }

    // TODO override helpers for contains and get

    @Override // required
    public boolean containsHelper(Stack<Work> worklist, Task t) {
        if (t == task) { return true; }
        if (t == last) {
            return true;
        } else {
            worklist.push(new Work(rest, 0));
            return false;

        }
    }

    @Override // required
    protected Task getHelper(Stack<Work> worklist, int index) {

        int thisindex= size();
        if (thisindex == index) { return task; }
        worklist.push(new Work(rest, thisindex));
        return null;

    }

}
