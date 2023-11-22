//TAKEN FROM DR JOHN BOYLAND

package edu.uwm.cs351;

/** An amount of time, always positive. To create a duration, scale an existing duration. */
public class Duration implements Comparable<Duration> {
    private final long extent; // this must remain private. Do NOT add a getter!

    public static final Duration INSTANTANEOUS= new Duration(
        0);
    public static final Duration MILLISECOND= new Duration(1);
    public static final Duration SECOND= new Duration(1000);
    public static final Duration MINUTE= new Duration(SECOND.extent * 60);
    public static final Duration HOUR= new Duration(SECOND.extent * 3600);
    public static final Duration DAY= new Duration(HOUR.extent * 24);
    public static final Duration YEAR= new Duration(DAY.extent * 365 + HOUR.extent * 6);

    // this constructor must remain private:
    private Duration(long milliseconds) {
        extent= milliseconds;
    }

    // TODO: For all constants, have a line:
    // public static final Duration CONSTANT = new Duration(...);

    // If you are overriding a method from a super class, always
    // annotate it "@Override" as here, overriding Object#equals(Object)
    @Override // implementation
    public boolean equals(Object x) {
        if (x instanceof Duration == false) return false;
        Duration other= (Duration) x;
        return other.extent == extent;
    }

    @Override // implementation
    public int hashCode() {
        return Long.valueOf(extent).hashCode(); // TODO: Do something efficient. Do NOT create a
                                                // String.
    }

    // If you are overriding a method from an interface, then Java 5
    // says you CANNOT use Override, but anything later says you MAY.
    // Please always do unless you write a javadoc comment.
    @Override // required
    public int compareTo(Duration other) {
        if (extent == other.extent) return 0;
        if (extent < other.extent) return -1;
        else return 1;
    }

    @Override // implementation
    public String toString() {
        if (extent < SECOND.extent) return extent + ".0 ms.";
        if (extent < MINUTE.extent) return divide(SECOND) + " s.";
        if (extent < DAY.extent) return divide(HOUR) + " hr.";
        if (extent < YEAR.extent) return divide(DAY) + " days";
        return (double) extent / YEAR.extent + " years";
    }

    // Methods that are not standard or private must have a documentation comment:

    /** Add two durations together to get a larger duration.
     *
     * @param d duration being added to this, must not be null
     * @return new duration equal to the combined duration of this and the argument.
     * @throws NullPointerException if d is null */
    public Duration add(Duration d) {
        return new Duration(d.extent + extent);
    }

    // TODO: three other public methods: subtract, scale & divide
    // Don't forget to write documentation comments.
    public Duration subtract(Duration d) {
        if (d.extent > extent) throw new ArithmeticException("cannot subtract a larger duration");
        return new Duration(extent - d.extent);
    }

    public Duration scale(double d) {
        if (d < 0) throw new IllegalArgumentException("cannot scale negatively");
        return new Duration(Math.round(extent * d));
    }

    public double divide(Duration d) {
        if (d.extent == 0) throw new ArithmeticException("divide by zero");
        return (double) extent / d.extent;
    }
}

//////I COPIED THIS FROM THE ANSWER KEY
