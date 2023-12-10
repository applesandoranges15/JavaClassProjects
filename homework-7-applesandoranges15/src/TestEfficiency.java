import edu.uwm.cs351.money.Container;
import edu.uwm.cs351.money.DefaultContainer;
import edu.uwm.cs351.money.Pipeline;
import edu.uwm.cs351.money.Spy;
import edu.uwm.cs351.money.Stack;
import edu.uwm.cs351.money.Type;
import junit.framework.TestCase;

public class TestEfficiency extends TestCase {
    Container self;

    @Override
    public void setUp() {
        try {
            assert self.size() == 0 : "OK";
            assertTrue(true);
        } catch (NullPointerException ex) {
            System.err.println("Assertions must NOT be enabled to use this test suite.");
            System.err.println(
                "In Eclipse: remove -ea from the VM Arguments box under Run>Run Configurations>Arguments");
            assertFalse("Assertions must NOT be enabled while running efficiency tests.", true);
        }
    }

    private static final int POWER= 20;
    private static final int MAX_LENGTH= 1 << POWER; // one million
    private static final int SAMPLE= 100;

    private void addMany(int number) {
        for (int i= 0; i < number; ++i) {
            self.add(Spy.newCoin(Type.DOLLAR));
        }
    }

    public void testA() {
        self= new DefaultContainer();
        addMany(MAX_LENGTH);
        assertEquals(MAX_LENGTH, self.size());
    }

    public void testB() {
        self= new Stack();
        addMany(MAX_LENGTH);
        assertEquals(MAX_LENGTH, self.size());
    }

    public void testC() {
        self= new Pipeline();
        addMany(MAX_LENGTH);
        assertEquals(MAX_LENGTH, self.size());
    }

    public void testD() {
        int width= MAX_LENGTH / SAMPLE;
        for (int i= 0; i < width; ++i) {
            self= new DefaultContainer();
            addMany(SAMPLE);
            assertEquals(SAMPLE, self.size());
        }
    }

    public void testE() {
        int width= MAX_LENGTH / SAMPLE;
        for (int i= 0; i < width; ++i) {
            self= new Stack();
            addMany(SAMPLE);
            assertEquals(SAMPLE, self.size());
        }
    }

    public void testF() {
        int width= MAX_LENGTH / SAMPLE;
        for (int i= 0; i < width; ++i) {
            self= new Pipeline();
            addMany(SAMPLE);
            assertEquals(SAMPLE, self.size());
        }
    }

    public void testG() {
        self= new DefaultContainer();
        addMany(MAX_LENGTH);
        for (int i= 0; i < MAX_LENGTH; ++i) {
            assertFalse(self.isEmpty());
        }
    }

    public void testH() {
        self= new Stack();
        addMany(MAX_LENGTH);
        for (int i= 0; i < MAX_LENGTH; ++i) {
            assertFalse(self.isEmpty());
        }
    }

    public void testI() {
        self= new Pipeline();
        addMany(MAX_LENGTH);
        for (int i= 0; i < MAX_LENGTH; ++i) {
            assertFalse(self.isEmpty());
        }
    }

    public void testJ() {
        self= new DefaultContainer();
        addMany(MAX_LENGTH);
        for (int i= 0; i < MAX_LENGTH; ++i) {
            assertEquals(Type.DOLLAR, self.remove().getType());
        }
        assertTrue(self.isEmpty());
    }

    public void testK() {
        self= new Stack();
        addMany(MAX_LENGTH);
        for (int i= 0; i < MAX_LENGTH; ++i) {
            assertEquals(Type.DOLLAR, self.remove().getType());
        }
        assertTrue(self.isEmpty());
    }

    public void testL() {
        self= new Pipeline();
        addMany(MAX_LENGTH);
        for (int i= 0; i < MAX_LENGTH; ++i) {
            assertEquals(Type.DOLLAR, self.remove().getType());
        }
        assertTrue(self.isEmpty());
    }

    public void testM() {
        self= new DefaultContainer();
        addMany(MAX_LENGTH);
        for (int i= 0; i < MAX_LENGTH; ++i) {
            assertTrue(self.canAdd(Spy.newCoin(Type.HALFDOLLAR)));
        }
    }

    public void testN() {
        self= new Stack();
        addMany(MAX_LENGTH);
        for (int i= 0; i < MAX_LENGTH / 2; ++i) {
            assertTrue(self.canAdd(Spy.newCoin(Type.QUARTER)));
            assertFalse(self.canAdd(Spy.newCoin(Type.HALFDOLLAR)));
        }
    }

    public void testO() {
        self= new Pipeline();
        addMany(MAX_LENGTH);
        for (int i= 0; i < MAX_LENGTH; ++i) {
            assertTrue(self.canAdd(Spy.newCoin(Type.HALFDOLLAR)));
        }
    }

}
