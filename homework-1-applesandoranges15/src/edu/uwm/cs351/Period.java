package edu.uwm.cs351;

/** A time period within historical time. */
public class Period {
    // TODO: Choose fields
    private final Time start;
    private final Duration length;

    /** Construct a period given the start time and length.
     *
     * @param s start time, must not be null
     * @param l length, must not be null */
    public Period(Time s, Duration l) {
        if (s == null || l == null)
            throw new NullPointerException("period arguments must not be null");
        start= s;
        length= l;
    }

    /** Construct a period given the start and end time
     *
     * @param from start time, must not be null
     * @param to   end time, must not be null or before the start time */
    public Period(Time from, Time to) {
        if (from.compareTo(to) > 0) throw new IllegalArgumentException("out of order items");
        start= from;
        length= from.difference(to);
    }

    /** Construct a period given the length and end time.
     *
     * @param len length of the period, must not be null
     * @param end end time of the period. */
    public Period(Duration len, Time end) {
        if (len == null || end == null)
            throw new NullPointerException("length and end time cannot both be null");
        start= end.subtract(len);
        length= len;
    }

    /** Return the start time of the period.
     *
     * @return beginning time */
    public Time getStart() {
        return start;
    }

    /** Return the stop time of the period.
     *
     * @return end time */
    public Time getStop() {
        return start.add(length);
    }

    /** Return the length of the period.
     *
     * @return the amount of time in this period */
    public Duration getLength() {
        return length;
    }

    @Override // implementation
    public boolean equals(Object x) {
        if (!(x instanceof Period)) return false;
        Period p= (Period) x;
        return start.equals(p.start) && length.equals(p.length);
    }

    @Override // implementation
    public int hashCode() {
        return start.hashCode() * 257 + length.hashCode();
    }

    @Override // implementation
    public String toString() {
        return "[" + start.toString() + ";" + length + "]";
    }

    /** Return whether this period overlaps with the parameter. If one appointment starts where the
     * other ends, they do not overlap.
     *
     * @param p period to compare to, must not be null
     * @return whether this period overlaps the parameter */
    public boolean overlap(Period p) {
        return getStart().compareTo(p.getStop()) < 0 && p.getStart().compareTo(getStop()) < 0;
    }
}
