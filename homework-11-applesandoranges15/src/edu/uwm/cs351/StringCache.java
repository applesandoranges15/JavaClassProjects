//ZOYA FATIMA AMMAR
//RECEIVED HELP FROM CLAIRE ROCA, HENRY TRIMBACH, YASH, and MATT

package edu.uwm.cs351;

import java.util.function.Consumer;

import edu.uwm.cs.util.Primes;

/** A class to manage string instances. All equal string instances that are interned will be
 * identical. */

public class StringCache {
    // even with a Spy, we still use "private":
    private String[] table;
    private int numEntries;

    // TODO: hash helper function used by wellFormed and intern

    private static Consumer<String> reporter= (
        s) -> { System.err.println("Invariant error: " + s); };

    private boolean report(String error) {
        reporter.accept(error);
        return false;
    }

//will return number of times a String object (key) is found in the table array
    /*if the key is not in the table, will return 0
     * @param String key
    * @return int*/
    private int findString(String key) {
        int count= 0;
        int amount= 0;
        while (count < table.length) {
            if (table[count] != null) {
                if (table[count].equals(key) == true) { amount++ ; }
            }
            count++ ;
        }
        return amount;
    }

    /*will return index of String object (value) if found in the table array
    *if the value is not in the table, will return -1
     * @param String value
    * @return int*/
    private int findIndex(String value) {
        int hash= Math.abs(value.hashCode() % table.length);
        int i= hash;
        while (i >= 0 && table[i] != null) {
            if (table[i].equals(value) == true) { return i; }
            i-- ;
        }
        int j= table.length - 1;
        while (j > hash && table[j] != null) {
            if (table[j].equals(value) == true) { return j; }
            j-- ;
        }
        return -1;
    }

    /*will return number of non-null entries found in the table array
      * @param none
     * @return int*/
    private int numberEntries() {
        int count= 0;
        for (int i= 0; i < table.length; i++ ) {
            if (table[i] != null) {
                count++ ;
            }
        }
        return count;

    }

    /*will return number of non-null entries found in the table array
     * @param String value
    * @return boolean*/
    private boolean correctPosition(String value) {
        int n= Math.abs(value.hashCode() % table.length);
        int i= n;
        while (i >= 0 && table[i] != value) {
            if (table[i] == null) { return false; }
            i-- ;
        }
        if (i < 0) {
            int j= table.length - 1;
            while (j > n && table[j] != value) {
                if (table[j] == null) { return false; }
                j-- ;
            }
        }
        return true;
    }

    private boolean wellFormed() {
        // 1. table is non-null and prime length
        // 2. number of entries is never more half the table size
        // 3. number of non-null entries in the table is numEntries
        // 4. every string in the array is hashed into the correct place
        // using backward linear probing
        // TODO
        if (table == null) { return report("table is null"); }
        if (Primes.isPrime(table.length) == false) {
            return report("length of table is not prime");
        }

        if (numberEntries() != numEntries) {
            return report("count is not equal to number of entries");
        }
        if (numEntries > 0.5 * table.length) {
            return report("number of entries is greater than half the table size");
        }

        for (String s : table) {
            if (s != null) {
                if (correctPosition(s) == false) {
                    return report("string is not in correct position");
                }
                if (findString(s) != 1) { return report("cannot have duplicates"); }
            }
        }
        return true;
    }

    private StringCache(boolean ignored) {} // do not change

    /** Create an empty string cache. */
    public StringCache() {
        table= new String[2];
        numEntries= 0;
        assert wellFormed() : "invariant broken in constructor";
    }

    /* Will compute hash of given String object(value) and place value in hash index
    or next available index according to backwards hashing
    @param String value
    /* @return String[] */
    private void placeString(String value, String[] sample) {
        int n= Math.abs(value.hashCode() % sample.length);
        int i= n;
        while (i >= 0) {
            if (sample[i] == null) {
                sample[i]= value;
                break;
            }
            i-- ;
        }
        if (i < 0) {
            int j= sample.length - 1;
            while (j > n) {
                if (sample[j] == null) {
                    sample[j]= value;
                    break;
                }
                j-- ;
            }
        }
    }

    // will resize the table and place each entry in new location
    // * @param n int
    // * @return void */
    private void rehash(int n) {
        int newLength= Primes.nextPrime(4 * n);
        String[] temptable= new String[newLength];
        for (String a : table) {
            if (a != null) {
                placeString(a, temptable);
            }
        }
        table= temptable;
    }

    /** Return a string equal to the argument. For equal strings, the same (identical) result is
     * always returned. As a special case, if null is passed, it is returned.
     *
     * @param value string, may be null
     * @return a string equal to the argument (or null if the argument is null). */
    public String intern(String value) {
        assert wellFormed() : "invariant broken before intern";
        if (value == null) { return null; }
        int isThere= findIndex(value);
        if (isThere != -1) { return table[isThere]; }
        if (numEntries + 1 > 0.5 * table.length) {
            rehash(numEntries + 1);
        }
        placeString(value, table);
        numEntries++ ;
        assert wellFormed() : "invariant broken after intern";
        return value;
    }

    public static class Spy { // do not modify (or use!) this class
        /** Create a String Cache with the given data structure, that has not been checked.
         *
         * @return new debugging version of a StringCache */
        public StringCache create(String[] t, int c) {
            StringCache sc= new StringCache(false);
            sc.table= t;
            sc.numEntries= c;
            return sc;
        }

        /** Return the number of entries in the string cache
         *
         * @param sc string cache, must not be null
         * @return number of entries in the cache. */
        public int getSize(StringCache sc) {
            return sc.numEntries;
        }

        /** Return capacity of the table in the cache
         *
         * @param sc cache to examine, must not be null
         * @return capacity */
        public int getCapacity(StringCache sc) {
            return sc.table.length;
        }

        public boolean wellFormed(StringCache sc) {
            return sc.wellFormed();
        }

        public Consumer<String> getReporter() {
            return reporter;
        }

        public void setReporter(Consumer<String> c) {
            reporter= c;
        }

    }
}
