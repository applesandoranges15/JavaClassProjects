package edu.uwm.cs351.money;

/** A class for use with testing the money ADT classes. This class should not be used by regular
 * business-logic classes. It allows us to check how many times various methods are called, and what
 * internal errors are reported. It can create non-legal-tender coins, and specialized versions of
 * the three container classes (for which we can count calls). */
public class Spy {

    /** A method for which we can count calls. */
    public enum Statistic {
        REPORT, WELLFORMED, TAKEOWNERSHIP, RELINQUISH;
    }

    private static int statistics[]= new int[Statistic.values().length];

    private static boolean doReport= true;
    private static String lastReport;

    /** Get statistic for this call;
     *
     * @param st statistic, must not be null
     * @return number of times the method for this statistic was called. */
    public static int getStat(Statistic st) {
        return statistics[st.ordinal()];
    }

    /** Clear the statistics for all methods, and clear the last report. */
    public static void clearStats() {
        for (int i= 0; i < statistics.length; ++i) {
            statistics[i]= 0;
        }
        lastReport= null;
    }

    /** Get the last report from the invariant checker.
     *
     * @return the last generated report */
    public static String getReport() {
        return lastReport;
    }

    /** Create a default container for which we can count calls.
     *
     * @param h the value of "head"
     * @return new default container instance that can be tested. */
    public static DefaultContainer makeContainer(Coin h) {
        return new DefaultContainer() {
            @Override // decorate
            protected boolean report(String message) {
                ++statistics[Statistic.REPORT.ordinal()];
                lastReport= message;
                if (doReport) super.report(message);
                return false;
            }

            @Override // decorate
            protected boolean wellFormed() {
                ++statistics[Statistic.WELLFORMED.ordinal()];
                return super.wellFormed();
            }

            @Override // decorate
            protected void takeOwnership(Coin c) {
                ++statistics[Statistic.TAKEOWNERSHIP.ordinal()];
                super.takeOwnership(c);
            }

            @Override // decorate
            protected void relinquish(Coin c) {
                ++statistics[Statistic.RELINQUISH.ordinal()];
                super.relinquish(c);
            }

            {
                head= h;
            }
        };
    }

    /** Create a stack for which we can count calls.
     *
     * @param h the value of "head"
     * @return new stack instance that can be tested. */
    public static Stack makeStack(Coin h) {
        return new Stack() {
            @Override // decorate
            protected boolean report(String message) {
                ++statistics[Statistic.REPORT.ordinal()];
                lastReport= message;
                if (doReport) super.report(message);
                return false;
            }

            @Override // decorate
            protected boolean wellFormed() {
                ++statistics[Statistic.WELLFORMED.ordinal()];
                return super.wellFormed();
            }

            @Override // decorate
            protected void takeOwnership(Coin c) {
                ++statistics[Statistic.TAKEOWNERSHIP.ordinal()];
                super.takeOwnership(c);
            }

            @Override // decorate
            protected void relinquish(Coin c) {
                ++statistics[Statistic.RELINQUISH.ordinal()];
                super.relinquish(c);
            }

            {
                head= h;
            }
        };
    }

    /** Create a new Pipeline object for which we can count calls.
     *
     * @param h initial value of head
     * @param t initial value of tail
     * @return new pipeline object that can count calls */
    public static Pipeline makePipeline(Coin h, Coin t) {
        return new Pipeline() {
            @Override // decorate
            protected boolean report(String message) {
                ++statistics[Statistic.REPORT.ordinal()];
                lastReport= message;
                if (doReport) super.report(message);
                return false;
            }

            @Override // decorate
            protected boolean wellFormed() {
                ++statistics[Statistic.WELLFORMED.ordinal()];
                return super.wellFormed();
            }

            @Override // decorate
            protected void takeOwnership(Coin c) {
                ++statistics[Statistic.TAKEOWNERSHIP.ordinal()];
                super.takeOwnership(c);
            }

            @Override // decorate
            protected void relinquish(Coin c) {
                ++statistics[Statistic.RELINQUISH.ordinal()];
                super.relinquish(c);
            }

            {
                head= h;
                tail= t;
            }
        };
    }

    /** Set up an internal test list of coins.
     *
     * @param index index of coin that first coin should link to
     * @param coins coins to link */
    public static void link(int index, Coin... coins) {
        Coin last= index >= 0 ? coins[index] : null;
        for (Coin c : coins) {
            c.next= last;
            last= c;
        }
    }

    /** These coins are all given to the container
     *
     * @param container container to use, may be null
     * @param coins     coins to affect, must not be null */
    public static void own(Container container, Coin... coins) {
        for (Coin c : coins) {
            c.owner= container;
        }
    }

    /** Clear all the mutable fields in the coins
     *
     * @param coins coins to clear, must not be null */
    public void clean(Coin... coins) {
        for (Coin c : coins) {
            c.next= null;
            c.owner= null;
        }
    }

    private static int serial= 0;

    /** Create a coin outside of the Mint process
     *
     * @param ty type, must be be null
     * @return new coin */
    public static Coin newCoin(Type ty) {
        return new Coin(ty, spyMint, ++serial);
    }

    private static Mint spyMint= null;

    /** Check whether a container is well formed.
     *
     * @param c container to check, must be a default container and not null
     * @return whether the well formed return true */
    public static boolean wellFormed(Container c) {
        if (c == null) {
            System.err.println("Test case incorrectly poassed a null");
        }
        boolean saved= doReport;
        try {
            doReport= false;
            return ((DefaultContainer) c).wellFormed();
        } finally {
            doReport= saved;
        }
    }
}
