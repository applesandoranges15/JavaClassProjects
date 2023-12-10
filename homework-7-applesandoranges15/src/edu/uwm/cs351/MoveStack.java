//ZOYA AMMAR. RECEIVED HELP FROM OFFICE HOURS
//WITH HENRY TRIMBACH, CLAIRE ROCA, AND MATT

package edu.uwm.cs351;

import edu.uwm.cs351.money.Bank;
import edu.uwm.cs351.money.Stack;
import edu.uwm.cs351.money.Type;

/** A variation on Towers of Hanoi */

//SOURCE
//https://www.geeksforgeeks.org/java-program-for-tower-of-hanoi/

//Step 1 − Move n-1 disks from source to aux
//Step 2 − Move nth disk from source to dest
//Step 3 − Move n-1 disks from aux to dest

public class MoveStack {

    /** Move all the coins from one stack to another, in the same order, with the help of another
     * stack.
     *
     * @param from   stack to get coins from, must not be null
     * @param to     stack to which the coins should be added, must not be null and must be empty.
     * @param helper stack which can be used to hold coins temporarily. It must not be the same as
     *               the "to" stack, must not be null and must not already have coins in it. */
    public static void doMove(Stack from, Stack to, Stack helper) {

        if (from == null || to == null || helper == null)
            throw new NullPointerException("from stack should not be null");
        if (helper == to || !helper.isEmpty() || !to.isEmpty())
            throw new IllegalArgumentException("helper cannot be empty or same as to");
        move(from.size(), from, to, helper);

    }

    public static void move(int n, Stack f, Stack t, Stack h) {
        if (n == 0) return;
        move(n - 1, f, h, t);
        t.add(f.remove());
        move(n - 1, h, t, f);
    }

    public static void helpMethod(Stack from) {

    }

    // TODO: Helper method
    // coins to request. We only have $2.00, so we
    // use a lot of pennies
    private static final Type[] coinsToStack= {
            Type.HALFDOLLAR, Type.DOLLAR, Type.QUARTER,
            Type.NICKEL, Type.NICKEL,
            Type.PENNY, Type.PENNY, Type.PENNY, Type.PENNY, Type.PENNY,
            Type.DIME,
    };

    public static void main(String[] args) {
        Bank b= Bank.getInstance();
        // TODO
        // 1. Create a stack and place in it coins
        // withdrawn from the bank (all the ones in coinsToStack).
        // 2. Create two other stacks.
        // 3. Call doMove to move coins from first stack to second stack
        // using a third stack as the helper stack.
        // Do not use spies!

        Stack stack1= new Stack();
        Stack stack2= new Stack();
        for (Type ty : coinsToStack) {
            stack1.add(b.withdraw(ty));
        }
        System.out.println("At start: " + stack1);
        doMove(stack1, stack2, new Stack());
        System.out.println("At end: " + stack2);

    }
}
