//Zoya Fatima Ammar
//Received help from Matt, Boyland, Anjali, and Claire Roca

package edu.uwm.cs351;

import java.util.Stack;

/** A sequence of paths where each depends on the previous one. */
public abstract class Path {

    private static final String currentTask= null;

    /** Return the number of tasks on the path, which is one more than the path length, and is also
     * positive (never zero).
     *
     * @return number of tasks in path */
    public abstract int size();

    /** Return the first task on the path
     *
     * @return first task, never null */
    public abstract Task getFirst();

    /** Return the last task on the path.
     *
     * @return last task, never null */
    public abstract Task getLast();

    /** Add a task to the start of the path.
     *
     * @param t task that the first task depends on */
    public Path prepend(Task t) {
        if (!getFirst().getDependencies().contains(t)) {
            throw new IllegalArgumentException("not a dependency path: " + t + " -> " + getFirst());
        }
        return new PrependPath(t, this);
    }

    /** Add a task to the end of the path.
     *
     * @param t task that depends on the last task */
    public Path append(Task t) {
        if (!t.getDependencies().contains(getLast())) {
            throw new IllegalArgumentException("not a dependency path: " + getLast() + " -> " + t);
        }
        return new PostpendPath(this, t);
    }

    /** Stitch two paths together that share a task in common
     *
     * @param p1 path that ends in a task t
     * @param p2 path that start with that task
     * @return a path that does the first and the continue with the next. The resulting path has one
     *         less task than the sum of the two because of the shared task. */
    public Path concat(Path p) {
        if (getLast() != p.getFirst()) {
            throw new IllegalArgumentException("the paths aren't connected");
        }
        if (size() == 1) return p;
        else if (p.size() == 1) return this;
        return new ConcatPath(this, p);
    }

    /** Return whether this path represents a cycle: is not degenerate and has start and stop the
     * same. */
    public boolean isCycle() {
        return size() > 1 && getFirst() == getLast();
    }

    @Override // implementation // for debugging
    public String toString() {
        StringBuilder sb= new StringBuilder();
        for (Task t : toArray()) {
            if (sb.length() != 0) {
                sb.append("<");
            }
            sb.append(t.getName());
        }
        return sb.toString();
    }

    /// The following methods are written to avoid using
    /// recursion, because recursion on the JVM can easily
    /// overwhelm the call stack.

    /// Since the data structure is recursive, and we don't want
    /// to do the work of the recursive calls for them, we
    /// instead use a worklist approach. This is akin to
    /// the "trampoline" approach that you can find online, but
    /// the worklist approach is simpler and more general.

    /** Some work that needs to be done later for a path at the given offset */
    protected static class Work {
        public final Path path;
        public int offset;

        public Work(Path p, int i) {
            path= p;
            offset= i;
        }
    }

    /** Copy all the tasks of this path into a new array and return it.
     *
     * @return array that holds all the tests, never null */
    public Task[] toArray() {
        Task[] result= new Task[size()];
        Stack<Work> worklist= new Stack<>();
        worklist.push(new Work(this, 0));
        while (!worklist.isEmpty()) {
            Work w= worklist.pop();
            w.path.toArrayHelper(worklist, result, w.offset);
        }
        return result;
    }

    /** Copy all the tasks into the given array at the given index.
     *
     * @param worklist add any work that needs to be (instead of recursion).
     * @param array    array to write into, must not be null
     * @param index    index into which to copy tasks, must be legal */
    protected abstract void toArrayHelper(Stack<Work> worklist, Task[] array, int index);

    /** Return true if the path includes this task
     *
     * @param t task to check for, may be null (but then it will return false)
     * @return whether task is in this path */
    public boolean contains(Task t) {
        // TODO: Use worklist and a new helper method
        // The "offset" is not needed and can be ignored.
        // DO NOT USE toArray or toArrayHelper!
        // (That is wasteful of space.)
        Stack<Work> worklist= new Stack<>();
        worklist.push(new Work(this, 0));

        while (!worklist.isEmpty()) {
            Work w= worklist.pop();
            if (w.path.containsHelper(worklist, t)) return true;

        }
        return false;
    }

    // TODO: Declare abstract helper method for contains

    protected abstract boolean containsHelper(Stack<Work> worklist, Task t);

    /** Return the task at the given index from the path.
     *
     * @param index zero-based index of element
     * @exception IndexOutOfBoundsException if index is not in range [0,size)
     * @return */
    public Task get(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Index is out of bounds: " + index);
        }

        Stack<Work> worklist= new Stack<>();
        worklist.push(new Work(this, index));

        while (!worklist.isEmpty()) {
            Work w= worklist.pop();
            Task result= w.path.getHelper(worklist, w.offset);
            if (result != null) { return result; }
        }

        // If the loop finishes without returning a task, throw an exception or return a default
        // value.
        throw new IllegalStateException("Task not found at index: " + index);
    }

    protected abstract Task getHelper(Stack<Work> worklist, int index);

    {

        // Task[] result= new Task[size()];
//    Stack<Work> worklist= new Stack<>();
//    worklist.push(new Work(this, 0));
//    while (!worklist.isEmpty()) {
//        Work w= worklist.pop();
//        w.path.toArrayHelper(worklist, result, w.offset);
//    }
//    return result;
        // TODO: Declare abstract helper method for get

        // Return the task at the specified index
    }

}
