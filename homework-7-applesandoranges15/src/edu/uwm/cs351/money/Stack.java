
//ZOYA AMMAR. RECEIVED HELP FROM OFFICE HOURS
//WITH HENRY TRIMBACH, CLAIRE ROCA, AND MATT

package edu.uwm.cs351.money;

import java.util.NoSuchElementException;

/** A LIFO container of coins that requires a coin cannot be placed on smaller ones */
public class Stack extends DefaultContainer {
    // TODO
    // Add the least amount that gives the required semantics.
//
//    r This class implements a container something like the Bag class discussed in lecture. It uses a singly-linked list (all the containers are singly-linked) with a
//    “head” pointer. The data structure is not private, but rather “default” access. Other
//    classes in the package are trusted to work together to maintain the integrity of the
//    money system.
//    It is effectively LIFO in than coins are added and removed at the head. This class
//    should not declare a “node” class, because for this assignment, all the containers are
//    endogenous.

    @Override // decorate
    protected boolean wellFormed() {
        if (super.wellFormed() == true) {
            // size of next Coin must be less than size of this Coin
            if (head != null) {

                for (Coin traverse= head; traverse.next != null; traverse= traverse.next) {
                    Coin comparison= traverse.next;
                    if (traverse.getType().getSize() > comparison.getType().getSize()) {
                        return report("Cannot have larger coin on top of smaller coin");
                    }
                }

            }
            return true;
        }
        return false;
    }

    @Override // decorate
    public Coin remove() throws NoSuchElementException {
        assert wellFormed();
        Coin c= super.remove();
        assert wellFormed();
        return c;
    }

    @Override // decorate
    public boolean canAdd(Coin c) {
        assert wellFormed();
        if (super.canAdd(c) == true) {
            if (head != null) {
                if (c.getType().getSize() > head.getType().getSize()) { return false; }
            }
        }
        return super.canAdd(c);
    }

    @Override // decorate
    public void add(Coin c) {
        assert wellFormed();
        super.add(c);
        assert wellFormed();
    }

}
