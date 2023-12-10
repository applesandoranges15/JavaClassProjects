//ZOYA AMMAR. RECEIVED HELP FROM OFFICE HOURS
//WITH HENRY TRIMBACH, CLAIRE ROCA, AND MATT

package edu.uwm.cs351.money;

import java.util.NoSuchElementException;

/** A LIFO Container of Coins */
public class DefaultContainer implements Container {

    public Coin head;

    /** Report an invariant error. *
     *
     * @param message message to print */
    protected boolean report(String message) {
        System.out.println("Invariant error: " + message);
        return false;
    }

//
    /** Return true if the data structure invariant is established.
     *
     * @return the value of the predicate for the invariant */
    protected boolean wellFormed() {
//        // 1. No cycles allowed (use Tortoise and Hare)
//        // 2. Every coin in this container must be owned by this container
//
        if (head != null) {
            // This check uses the "tortoise and hare" algorithm attributed to Floyd.
            Coin fast= head.next;
            for (Coin p= head; fast != null && fast.next != null; p= p.next) {
                if (p == fast) return report("list is cyclic!");
                fast= fast.next.next;
            }
        }

        if (head != null) {
            for (Coin p= head; p != null; p= p.next) {
                if (!(p.owner == this)) { return report("Container is null"); }
            }
        }
        return true;
    }

//
//    /** Start adding a coin. We first check that we can add it and then take over ownership.
//     *
//     * @param c coin to take possession of, must be one we can add */
    protected void takeOwnership(Coin c) {
        assert wellFormed();
        if (!canAdd(c)) throw new IllegalArgumentException("cannot add " + c);
        c.owner= this;
    }

//
//    /** Get ready to return a coin. We remove ownership.
//     *
//     * @param c coin to relinquish */
    protected void relinquish(Coin c) {
        assert wellFormed();
        c.owner= null;
    }

//
    @Override // required
    public boolean isEmpty() {
        assert wellFormed();
        if (size() == 0) { return true; }
        assert wellFormed();
        return false;
    }

//
    @Override // required
    public int size() {
        int result= 0;
        for (Coin first= head; first != null; first= first.next) {
            ++result;
        }
        assert wellFormed();
        return result;
    }

    @Override // required
    public boolean canAdd(Coin c) {
        // TODO Auto-generated method stub
        assert wellFormed();
        if (c == null) { return false; }
        if (c.isOwned() == true) { return false; }
        return true;

    }

    @Override // required
    public void add(Coin c) {
        if (c == null) {
            takeOwnership(c);
            throw new IllegalArgumentException("cannot add a null coin");
        }
        if (c.owner != null) { throw new IllegalArgumentException("coin is already owned"); }
        takeOwnership(c);
        Coin after= head;
        head= c;
        head.next= after;
        assert wellFormed();
    }

//
    @Override // required
    public Coin remove() throws NoSuchElementException {
        assert wellFormed();
        if (head == null) {
            throw new NoSuchElementException("there is no more element to remove");
        }
        Coin result= head;
        relinquish(head);
        result.owner= null;
        head= head.next;

        assert wellFormed();
        return result;
    }

}