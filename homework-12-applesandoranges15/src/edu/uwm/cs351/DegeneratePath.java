package edu.uwm.cs351;

import java.util.Stack;

/** A path without edges, just a single task. */
//Zoya Fatima Ammar
//Received help from Matt, Boyland, Anjali, and Claire Roca

public class DegeneratePath extends Path {
    private final Task element; // never null

    public DegeneratePath(Task task) {
        if (task == null) throw new NullPointerException("path requires non-null task");
        element= task;
    }

    @Override // required
    public int size() {
        return 1;
    }

    @Override // required
    public Task getFirst() {
        return element;
    }

    @Override // required
    public Task getLast() {
        return element;
    }

    @Override // required
    protected void toArrayHelper(Stack<Work> worklist, Task[] array, int index) {
        array[index]= element;

        // don't add anything to worklist since there are no sub paths here
    }

    @Override // required
    protected Task getHelper(Stack<Work> worklist, int index) {

        if (index == size() - 1) { return element; }
        return null;
    }

    // TODO Helpers for contains and get
    @Override // required

    protected boolean containsHelper(Stack<Work> worklist, Task t) {
        if (element == t) { return true; }
        return false;

    }
}
