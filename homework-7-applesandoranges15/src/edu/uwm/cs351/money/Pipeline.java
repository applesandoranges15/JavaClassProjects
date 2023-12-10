//ZOYA AMMAR. RECEIVED HELP FROM OFFICE HOURS
//WITH HENRY TRIMBACH, CLAIRE ROCA, AND MATT

package edu.uwm.cs351.money;

import java.util.NoSuchElementException;

/** A FIFO container of coins. */
public class Pipeline extends DefaultContainer {
    protected Coin tail;

    /** Return true if the data structure invariant is established. Ensure that head and tail are
     * Coins in pipeline
     *
     * @return the value of the predicate for the invariant */

    @Override // decorate
    protected boolean wellFormed() {
        if (super.wellFormed() == true) {
            boolean foundHead= false;
            boolean foundTail= false;
            if (head != null) {
                for (Coin p= head; p != null; p= p.next) {
                    if (p == tail) {
                        foundTail= true;
                    }
                    if (p == head) {
                        foundHead= true;
                    }
                }
                if (!foundHead || !foundTail) {
                    return report("head and tail are not found in the list");
                }
            }

            if (head == null && tail != null) {
                return report("when is head is null, tail must also be null");
            }
            if (tail != null) {
                if (tail.next != null) { return report("should not be any coin after tail"); }
            }

            return true;

        }
        return false;
    }

    @Override // decorate
    public boolean isEmpty() {
        assert wellFormed();
        if (size() == 0) { return true; }
        return false;
    }

    @Override // decorate
    public int size() {
        assert wellFormed();
        return super.size();

    }

    @Override // decorate
    public boolean canAdd(Coin c) {
        assert wellFormed();
        if (c == null) { return false; }
        if (c.isOwned() == true) { return false; }
        Boolean found= false;
        for (Coin traverse= head; traverse != null; traverse= traverse.next) {
            if (traverse == c) {
                found= true;
            }
        }
        if (found == true) { return false; }
        return true;

    }

    @Override // implementation
    public void add(Coin c) {
        canAdd(c);
        takeOwnership(c);
        assert wellFormed();
        if (c == null) { throw new IllegalArgumentException("cannot add null"); }
        if (head == null) {
            head= c;
            tail= c;
            tail.next= null;
        } else {
            tail.next= c;
            tail= tail.next;
            tail.next= null;
        }
        assert wellFormed();
    }

    @Override // decorate
    public Coin remove() throws NoSuchElementException {
        assert wellFormed();
        if (head == null) { throw new NoSuchElementException("there is no such element"); }
        Coin result= head;
        relinquish(result);

        if (head == tail) {
            head= tail= null;
        }

        else {
            Coin newHead= head.next;
            head= null;
            head= newHead;

        }

        assert wellFormed();
        return result;

    }
}
