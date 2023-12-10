// Zoya Fatima Ammar
// Received help from Claire Roca, Dr Boyland, and Anjali Venugopal

package edu.uwm.cs351;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

import edu.uwm.cs.junit.LockedTestCase;

/****************************************************************************** This class is a
 * homework assignment; A NewApptBook ("book" for short) is a collection of Appointment objects in
 * sorted order. ******************************************************************************/
public class NewApptBook extends AbstractCollection<Appointment> implements Cloneable {
    // TODO: Declare the data structure

    private static class Node {
        Appointment data;
        Node left, right;

        Node(Appointment a) {
            data= a;
        }
    }

    private static Consumer<String> reporter= (
        s) -> { System.err.println("Invariant error: " + s); };

    private boolean report(String error) {
        reporter.accept(error);
        return false;
    }

    Node root;
    int manyItems;
    int version;

    private boolean checkHeight(Node r, int max) {
        if (max < 0) {
            return false;
        } else if (r == null && max >= 0) {
            return true;
        } else if (max == 0) { return false; }
        return checkHeight(r.right, max - 1) && checkHeight(r.left, max - 1);
    }

    private int countNodes(Node r) {
        if (r == null) {
            return 0;
        } else {
            return 1 + countNodes(r.right) + countNodes(r.left);
        }
    }

    private Node searchTree(Appointment search, Node in) {
        if (in == null) { return null; }
        if (in.data == search) { return in; }
        Node one= searchTree(search, in.right);
        Node two= searchTree(search, in.left);
        if (one != null) {
            return one;
        } else if (two != null) {
            return two;
        } else {
            return null;
        }
    }

    private Node getParent(Node r, Node tree) {
        if (r == tree) { return null; }
        if (tree == null) { return null; }
        if (tree.right == r) { return tree; }
        if (tree.left == r) { return tree; }
        Node right= getParent(r, tree.right);
        Node left= getParent(r, tree.left);
        if (right != null) {
            return right;
        } else {
            return left;
        }
    }

    private boolean foundNode(Node r, Node in) {
        if (in == r) {
            return true;
        } else if (r == null) {
            return false;
        } else {
            return foundNode(r.right, in) || foundNode(r.left, in);
        }
    }

    private boolean foundAppointment(Node r, Appointment in) {
        if (r == null) {
            return false;
        } else if (r.data == in) {
            return true;
        } else {
            return foundAppointment(r.right, in) || foundAppointment(r.left, in);
        }
    }

    public Node getMax(Node r) {
        if (r == null) {
            return null;
        } else if (r.right == null) {
            return r;

        } else {
            return getMax(r.right);
        }
    }

    public Node getMin(Node r) {
        if (r == null) {
            return null;
        } else if (r.left == null) {
            return r;

        } else {

            return getMin(r.left);
        }
    }

    public Node nextNode(Node r) {
        ArrayList<Node> zoya= new ArrayList<>();
        zoya.add(root);
        Boolean found= false;
        Node result= null;
        while (zoya.size() != 0) {
            result= zoya.remove(0);
            if (found == true) {
                break;
            }
            if (result == r) {
                found= true;
            }
            if (result.left != null) {
                zoya.add(result.left);
            }
            if (result.right != null) {
                zoya.add(result.right);
            }
        }
        return result;
    }

    private Node nextInTree(Node r, Appointment appt, boolean acceptEquivalent, Node alt) {
        if (r == null) { return alt; }
        int c= appt.compareTo(r.data);
        if (c == 0 && acceptEquivalent) { return r; }
        if (c >= 0) { return nextInTree(r.right, appt, acceptEquivalent, alt); }
        return nextInTree(r.left, appt, acceptEquivalent, r);
    }

    private boolean allInRange(Node r, Appointment lo, Appointment hi) {
        if (r == null) return true;
        if (r.data == null) return report("found null data in tree");
        if (lo != null && lo.compareTo(r.data) > 0)
            return report("Out of bound:" + r.data + "is too low");
        if (hi != null && hi.compareTo(r.data) <= 0)
            return report("Out of bound" + r.data + "is too high");
        return allInRange(r.left, lo, r.data) && allInRange(r.right, r.data, hi);
    }

    private Node doAdd(Node r, Appointment element) {
        if (r == null) return new Node(element);
        if (element.compareTo(r.data) >= 0) {
            r.right= doAdd(r.right, element);
        } else {
            r.left= doAdd(r.left, element);
        }
        ++version;
        return r;
    }

    @Override // implementation
    public boolean add(Appointment element) {
        if (element == null && manyItems == 0) throw new NullPointerException("cannot add null");
        if (element == null) { return true; }
        root= doAdd(root, element);
        ++manyItems;
        ++version;
        return true;
    }

    @Override // efficiency
    public boolean isEmpty() {
        return manyItems == 0;
    }

    @Override // implementation
    public boolean addAll(Collection<? extends Appointment> c) {
        Boolean check= c instanceof NewApptBook;
        if (!check) {
            Iterator<?> it= c.iterator();
            while (it.hasNext()) {
                Appointment result= (Appointment) it.next();
                add(result);
            }
        }

        else {
            NewApptBook zoya= ((NewApptBook) c).clone();
            super.addAll(zoya);
        }
        return true;
    }

    @Override // required
    public int size() {
        return manyItems;
    }

    @Override // efficiency
    public void clear() {
        if (manyItems == 0)
            return;
        else {
            root= null;
            ++version;
            manyItems= 0;
        }
    }

    @Override // efficiency
    public boolean contains(Object a) {
        Boolean check= a instanceof Appointment;
        if (check) {
            Appointment zoya= (Appointment) a;
            return foundAppointment(root, zoya);
        } else {
            return false;
        }
    }

    public Node doClone(Node r, NewApptBook answer) {
        if (r == null) { return null; }
        Node copy= new Node(r.data);
        copy.left= doClone(r.left, answer);
        copy.right= doClone(r.right, answer);
        return copy;
    }

    @Override // implementation
    public NewApptBook clone() {
        assert wellFormed() : "invariant failed at start of clone";
        NewApptBook answer;
        try {
            answer= (NewApptBook) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("This class does not implement Cloneable");
        }

        assert wellFormed() : "invariant failed at end of clone";
        answer.root= doClone(root, answer);
        assert answer.wellFormed() : "invariant on answer failed at end of clone";
        return answer;
    }

    private boolean wellFormed() {
        if (!checkHeight(root, manyItems)) { return report("height is not bounded by manyItems"); }
        if (countNodes(root) != manyItems) {
            return report("manyItems does not match number of items in the tree");
        }
        if (allInRange(root, null, null) == false) { return false; }
        return true;
    }

    private NewApptBook(boolean testInvariant) {}

    /** Initialize an empty book. **/
    public NewApptBook() {
        assert wellFormed() : "invariant failed at end of constructor";
    }

    @Override
    public Iterator<Appointment> iterator() {
        assert wellFormed();
        return new MyIterator(); // TODO
    }

    public Iterator<Appointment> iterator(Appointment start) {
        assert wellFormed();
        if (start == null) throw new NullPointerException("cannot start at a null");

        return new MyIterator(start); // TODO
    }

    public class MyIterator implements Iterator<Appointment> {

        public Node cursor;
        public Node nextCursor;
        public int colVersion;

        public boolean wellFormed() {
            if (!NewApptBook.this.wellFormed()) { return false; }
            if (colVersion != version) { return true; }
            if (cursor != null) {
                if (foundNode(root, cursor) == false) {
                    return report("cursor is not found in the tree");
                }
            }
            if (nextCursor != null) {
                if (foundNode(root, nextCursor) == false) {
                    return report("nextCursor is not found in the tree");
                }
            }
            if (cursor == null && nextCursor != null) {
                return report("when cursor is null, nextCursor cannot be null");
            }

            if (cursor != nextCursor) {
                Node result1= nextInTree(root, cursor.data, false, null);
                Node result2= nextInTree(cursor.right, cursor.data, true, null);
                Boolean found= false;
                if (result2 != null) {
                    if (nextCursor == result2) {
                        found= true;
                    }
                }
                if (result2 == null) {
                    if (nextCursor == result1) {
                        found= true;
                    }
                }
                if (found == false) { return report("nextCursor not in correct spot"); }
            }

            return true;
        }

        private Node firstInTree(Node r) {
            if (r == null) { return r; }
            while (r.left != null) {
                r= r.left;
            }
            return r;
        }

        MyIterator(boolean ignored) {} // do not change this

        MyIterator() {
            Node result= firstInTree(root);
            cursor= result;
            nextCursor= result;
            colVersion= version;
            assert wellFormed() : "invariant failed in iterator constructor";
        }

        MyIterator(Appointment start) {
            Boolean wasThere= contains(start);
            if (wasThere == false) {
                add(start);
                Node result= nextInTree(root, start, false, null);
                cursor= result;
                nextCursor= result;
                colVersion= version;
            } else {
                Boolean exactAppt= foundAppointment(root, start);

                if (exactAppt) {
                    Node zoya= nextInTree(root, start, true, null);
                    cursor= zoya;
                    nextCursor= zoya;

                } else {
                    add(start);
                    Node zoya= nextInTree(root, start, true, null);
                    cursor= zoya;
                    nextCursor= zoya;
                    ++manyItems;
                }

                colVersion= version;
            }

            assert wellFormed() : "invariant failed in iterator constructor";

        }

        private void checkVersion() {
            if (colVersion != version) throw new ConcurrentModificationException("stale iterator");
        }

        @Override // required
        public boolean hasNext() {
            checkVersion();
            if (nextCursor != null) {
                return true;
            } else {
                return false;
            }
        }

        @Override // required
        public Appointment next() {
            checkVersion();
            if (!hasNext()) { throw new NoSuchElementException("there is no next element"); }
            if (cursor == nextCursor) {
                Node result1= nextInTree(root, cursor.data, false, null);
                Node result2= nextInTree(cursor.right, cursor.data, true, null);
                if (result2 != null) {
                    nextCursor= result2;
                } else {
                    nextCursor= result1;
                }
            } else {
                Node result1= nextInTree(root, cursor.data, false, null);
                Node result2= nextInTree(cursor.right, cursor.data, true, null);
                if (result2 != null) {
                    cursor= result2;
                } else {
                    cursor= result1;
                }

                Node result3= nextInTree(root, cursor.data, false, null);
                Node result4= nextInTree(cursor.right, cursor.data, true, null);
                if (result4 != null) {
                    nextCursor= result4;
                } else {
                    nextCursor= result3;
                }
            }
            return cursor.data;
        }

        @Override // implementation
        public void remove() {
            checkVersion();
            if (cursor == nextCursor) {
                throw new IllegalStateException("there is no element to remove");
            }

            removeHelper(cursor, root);
            cursor= nextCursor;
            ++version;
            ++colVersion;
            --manyItems;
        }

        private void removeHelper(Node r, Node tree) {
            Node parent= getParent(r, tree);

            if (parent == null) {
                if (r.left == null && r.right == null) {
                    root= null;
                } else if (r.right == null) {
                    root= r.left;
                } else if (r.left == null) {
                    root= r.right;
                } else {
                    Node min= getMin(r.right);
                    r.data= min.data;
                    removeHelper(min, tree);
                }
            } else {
                if (parent.left == r) {
                    if (r.right == null && r.left == null) {
                        parent.left= null;
                    } else if (r.right == null) {
                        parent.left= r.left;
                    } else if (r.left == null) {
                        parent.left= r.right;
                    } else {
                        Node min= getMin(r.right);
                        r.data= min.data;
                        removeHelper(min, tree);
                    }
                } else if (parent.right == r) {
                    if (r.right == null && r.left == null) {
                        parent.right= null;
                    } else if (r.right == null) {
                        parent.right= r.left;
                    } else if (r.left == null) {
                        parent.right= r.right;
                    } else {
                        Node min= getMin(r.right);
                        r.data= min.data;
                        removeHelper(min, tree);
                    }

                }

            }

        }

        public void setCursor(edu.uwm.cs351.NewApptBook.TestInvariantChecker.Node n1) {
            // TODO Auto-generated method stub

        }
    }

    // don't change this nested class:
    public static class TestInvariantChecker extends LockedTestCase {
        protected NewApptBook self;
        protected NewApptBook.MyIterator iterator;

        protected Consumer<String> getReporter() {
            return reporter;
        }

        protected void setReporter(Consumer<String> c) {
            reporter= c;
        }

        private static Appointment a= new Appointment(new Period(new Time(), Duration.HOUR),
            "default");

        protected class Node extends NewApptBook.Node {
            public Node(Appointment d, Node n1, Node n2) {
                super(a);
                data= d;
                left= n1;
                right= n2;
            }

            public void setLeft(Node l) {
                left= l;
            }

            public void setRight(Node r) {
                right= r;
            }
        }

        protected class Iterator extends MyIterator {
            public Iterator(Node n1, Node n2, int v) {
                self.super(false);
                cursor= n1;

                colVersion= v;
            }

            public Iterator() {
                this(null, null, self.version);
            }

            @Override
            public boolean wellFormed() {
                return super.wellFormed();
            }

            public void setColVersion(int cv) {
                colVersion= cv;
            }

            @Override
            public void setCursor(Node c) {
                cursor= c;
            }

            public void setNextCursor(Node c) {
                nextCursor= c;
            }
        }

        protected Node newNode(Appointment a, Node l, Node r) {
            return new Node(a, l, r);
        }

        protected void setRoot(Node n) {
            self.root= n;
        }

        protected void setManyItems(int mi) {
            self.manyItems= mi;
        }

        @Override
        protected void setUp() {
            self= new NewApptBook(false);
            self.root= null;
            self.manyItems= 0;
        }

        protected boolean wellFormed() {
            return self.wellFormed();
        }

        /// Prevent this test suite from running by itself

        public void test() {
            assertFalse("Don't attempt to run this test", true);
        }
    }

}