//Zoya Fatima Ammar
//Received help from Matt, Boyland, Anjali, and Claire Roca

package edu.uwm.cs351;

import java.util.Stack;

/** A path built up by concatenating two paths. It doesn't add any edges. The lengths are added, but
 * the size of the result is one less that the size of the two parts. */
public class ConcatPath extends Path {
    // TODO: Data Structure (no wellFormed needed)
    // Remember: all fields should be final
    private final Path first;
    private final Path second;

    /** Connect two paths
     *
     * @param p1 non-degenerate path that ends in one task
     * @param p2 non-degenerate path start starts with that same task */
    public ConcatPath(Path p1, Path p2) {
        first= p1;
        second= p2;
        // want to postpend second element of p2 to p1
        // want to ensure that last task of p1 and first task of p2 are equal
        // want to prepend all tasks other than last task of p1 to p2

        // TODO
        if (p1 == null || p2 == null) { throw new NullPointerException("cannot add a null path"); }

        if (p1.size() == 1 ||
            p2.size() == 1) {
            throw new IllegalArgumentException("cannot concatenate a degenerate path");
        }
        if (p1.getLast() != p2.getFirst()) {
            throw new IllegalArgumentException(
                "cannot concatenate because first is not equal to last");
        }
    }

    @Override // required
    public int size() {
        return first.size() + second.size() - 1;
        // TODO
    }

    @Override // required
    public Task getFirst() {
        return first.getFirst(); // TODO
    }

    @Override // required
    public Task getLast() {
        return second.getLast(); // TODO
    }

    @Override // required
    protected void toArrayHelper(Stack<Work> worklist, Task[] array, int index) {
        // call toArrayHelper on first path
        // call to ArrayHelper on second path. For second path, want
        // to make sure that you don't start at same index. make sure you push both
        // first and second stack into the path
        // push work
        // toArray
        int secondIndex= first.size() - 1;
        worklist.push(new Work(first, index));
        worklist.push(new Work(second, secondIndex));
        // TODO
    }

    // TODO: other helper methods
    @Override
    protected boolean containsHelper(Stack<Work> worklist, Task t) {
        // check if containsHelper

        worklist.push(new Work(first, 0));
        worklist.push(new Work(second, 0));
        return false;

    }

    @Override
    protected Task getHelper(Stack<Work> worklist, int index) {
        worklist.push(new Work(first, 0));
        worklist.push(new Work(second, first.size() - 1));
        return null;

    }

}
