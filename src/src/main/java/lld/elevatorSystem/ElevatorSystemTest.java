package lld.elevatorSystem;

public class ElevatorSystemTest {
    
    private Q11ElevatorSystemInterface elevatorSystem;
    private Helper11 helper;
    private int testCount = 0;
    private int passedTests = 0;
    
    public static void main(String[] args) {
        ElevatorSystemTest test = new ElevatorSystemTest();
        test.runAllTests();
    }
    
    void setUp() {
        elevatorSystem = new Solution();
        helper = new Helper11();
    }
    
    void runAllTests() {
        System.out.println("🏗️ Running Elevator System Tests");
        System.out.println("================================\n");
        
        setUp();
        testBasicInitialization();
        
        setUp();
        testBasicLiftRequest();
        
        setUp();
        testMultipleRequests();
        
        setUp();
        testLiftCapacity();
        
        setUp();
        testLiftDirectionConstraints();
        
        setUp();
        testOptimalLiftSelection();
        
        setUp();
        testLiftCompletion();
        
        setUp();
        testInvalidRequests();
        
        setUp();
        testComplexScenario();
        
        setUp();
        testReinitialization();
        
        setUp();
        testEdgeCases();
        
        System.out.println("\n📊 Test Results Summary");
        System.out.println("=======================");
        System.out.println("Total Tests: " + testCount);
        System.out.println("Passed: " + passedTests);
        System.out.println("Failed: " + (testCount - passedTests));
        System.out.println("Success Rate: " + (passedTests * 100 / testCount) + "%");
    }
    
    void assertEquals(String expected, String actual) {
        testCount++;
        if (expected.equals(actual)) {
            passedTests++;
            System.out.println("✅ PASS: Expected '" + expected + "', Got '" + actual + "'");
        } else {
            System.out.println("❌ FAIL: Expected '" + expected + "', Got '" + actual + "'");
            Thread.dumpStack();
        }
    }
    
    void assertEquals(int expected, int actual) {
        testCount++;
        if (expected == actual) {
            passedTests++;
            System.out.println("✅ PASS: Expected " + expected + ", Got " + actual);
        } else {
            System.out.println("❌ FAIL: Expected " + expected + ", Got " + actual);
            Thread.dumpStack();
        }
    }
    
    void assertTrue(boolean condition) {
        testCount++;
        if (condition) {
            passedTests++;
            System.out.println("✅ PASS: Condition is true");
        } else {
            System.out.println("❌ FAIL: Condition is false");
            Thread.dumpStack();
        }
    }
    
    void assertNotNull(Object obj) {
        testCount++;
        if (obj != null) {
            passedTests++;
            System.out.println("✅ PASS: Object is not null");
        } else {
            System.out.println("❌ FAIL: Object is null");
            Thread.dumpStack();
        }
    }
    
    void assertNotEquals(int expected, int actual) {
        testCount++;
        if (expected != actual) {
            passedTests++;
            System.out.println("✅ PASS: Values are different (" + expected + " != " + actual + ")");
        } else {
            System.out.println("❌ FAIL: Values are equal (" + expected + " == " + actual + ")");
            Thread.dumpStack();
        }
    }
    
    void printAllLiftStates(int numLifts) {
        for (int i = 0; i < numLifts; i++) {
            System.out.println("   Lift " + i + " state: " + elevatorSystem.getLiftState(i));
        }
    }
    
    void testBasicInitialization() {
        System.out.println("🧪 Test: Basic Initialization");
        elevatorSystem.init(6, 2, 2, helper);
        
        // Check initial states of both lifts
        assertEquals("0-I-0", elevatorSystem.getLiftState(0));
        assertEquals("0-I-0", elevatorSystem.getLiftState(1));
        printAllLiftStates(2);
        System.out.println();
    }
    
    void testBasicLiftRequest() {
        System.out.println("🧪 Test: Basic Lift Request");
        elevatorSystem.init(6, 2, 2, helper);
        
        // Request lift from floor 0 going up
        int liftIndex = elevatorSystem.requestLift(0, 'U');
        assertTrue(liftIndex >= 0 && liftIndex < 2);
        
        // Press floor button inside lift
        elevatorSystem.pressFloorButtonInLift(liftIndex, 4);
        
        // Simulate time passage
        elevatorSystem.tick();
        
        // Check lift state after 1 tick
        String state = elevatorSystem.getLiftState(liftIndex);
        assertTrue(state.startsWith("1-U-1"));
        printAllLiftStates(2);
        System.out.println();
    }
    
    void testMultipleRequests() {
        System.out.println("🧪 Test: Multiple Requests");
        int numLifts = 2;
        elevatorSystem.init(6, numLifts, 2, helper);
        int lift0 = elevatorSystem.requestLift(0, 'U');
        elevatorSystem.pressFloorButtonInLift(lift0, 4);
        int lift1 = elevatorSystem.requestLift(5, 'D');
        assertTrue(lift1 >= 0 && lift1 < numLifts);
        assertNotEquals(lift0, lift1); // Should assign different lifts
        elevatorSystem.tick();
        elevatorSystem.tick();
        String state0 = elevatorSystem.getLiftState(lift0);
        String state1 = elevatorSystem.getLiftState(lift1);
        System.out.println("[DEBUG] state0: " + state0 + ", state1: " + state1);
        assertTrue(state0.startsWith("2-U-1"));
        assertTrue(state1.startsWith("2-U-0"));
        printAllLiftStates(numLifts);
        System.out.println();
    }
    
    void testLiftCapacity() {
        System.out.println("🧪 Test: Lift Capacity");
        int numLifts = 1;
        elevatorSystem.init(6, numLifts, 2, helper);
        int liftIndex = elevatorSystem.requestLift(0, 'U');
        elevatorSystem.pressFloorButtonInLift(liftIndex, 3);
        elevatorSystem.pressFloorButtonInLift(liftIndex, 4);
        elevatorSystem.tick();
        String state = elevatorSystem.getLiftState(liftIndex);
        System.out.println("[DEBUG] state: " + state);
        assertTrue(state.endsWith("-2")); // Should have 2 people
        printAllLiftStates(numLifts);
        System.out.println();
    }
    
    void testLiftDirectionConstraints() {
        System.out.println("🧪 Test: Lift Direction Constraints");
        int numLifts = 1;
        elevatorSystem.init(10, numLifts, 5, helper);
        int liftIndex = elevatorSystem.requestLift(2, 'U');
        elevatorSystem.pressFloorButtonInLift(liftIndex, 8);
        for (int i = 0; i < 3; i++) {
            elevatorSystem.tick();
        }
        int result = elevatorSystem.requestLift(3, 'D');
        assertEquals(-1, result); // Should not be assigned
        printAllLiftStates(numLifts);
        System.out.println();
    }
    
    void testOptimalLiftSelection() {
        System.out.println("🧪 Test: Optimal Lift Selection");
        int numLifts = 2;
        elevatorSystem.init(10, numLifts, 5, helper);
        elevatorSystem.requestLift(2, 'U');
        elevatorSystem.tick();
        elevatorSystem.tick();
        elevatorSystem.requestLift(8, 'D');
        elevatorSystem.tick();
        elevatorSystem.tick();
        elevatorSystem.tick();
        elevatorSystem.tick();
        elevatorSystem.tick();
        elevatorSystem.tick();
        int selectedLift = elevatorSystem.requestLift(5, 'U');
        assertTrue(selectedLift >= 0);
        printAllLiftStates(numLifts);
        System.out.println();
    }
    
    void testLiftCompletion() {
        System.out.println("🧪 Test: Lift Completion");
        int numLifts = 1;
        elevatorSystem.init(6, numLifts, 2, helper);
        int liftIndex = elevatorSystem.requestLift(0, 'U');
        elevatorSystem.pressFloorButtonInLift(liftIndex, 4);
        for (int i = 0; i < 4; i++) {
            elevatorSystem.tick();
        }
        String state = elevatorSystem.getLiftState(liftIndex);
        System.out.println("[DEBUG] state: " + state);
        assertTrue(state.startsWith("4-I-0"));
        printAllLiftStates(numLifts);
        System.out.println();
    }
    
    void testInvalidRequests() {
        System.out.println("🧪 Test: Invalid Requests");
        elevatorSystem.init(6, 1, 2, helper);
        
        // Test invalid floor request
        int result = elevatorSystem.requestLift(10, 'U'); // Floor 10 doesn't exist
        assertEquals(-1, result);
        
        // Test invalid direction
        result = elevatorSystem.requestLift(2, 'X'); // Invalid direction
        assertEquals(-1, result);
        printAllLiftStates(1);
        System.out.println();
    }
    
    void testComplexScenario() {
        System.out.println("🧪 Test: Complex Scenario");
        int numLifts = 2;
        elevatorSystem.init(8, numLifts, 3, helper);
        int lift0 = elevatorSystem.requestLift(1, 'U');
        elevatorSystem.pressFloorButtonInLift(lift0, 6);
        int lift1 = elevatorSystem.requestLift(7, 'D');
        elevatorSystem.pressFloorButtonInLift(lift1, 2);
        elevatorSystem.pressFloorButtonInLift(lift0, 5);
        for (int i = 0; i < 5; i++) {
            elevatorSystem.tick();
        }
        String state0 = elevatorSystem.getLiftState(lift0);
        String state1 = elevatorSystem.getLiftState(lift1);
        assertNotNull(state0);
        assertNotNull(state1);
        assertTrue(state0.matches("\\d+-[UDI]-\\d+"));
        assertTrue(state1.matches("\\d+-[UDI]-\\d+"));
        printAllLiftStates(numLifts);
        System.out.println();
    }
    
    void testReinitialization() {
        System.out.println("🧪 Test: Reinitialization");
        int numLifts = 3;
        elevatorSystem.init(8, numLifts, 4, helper);
        elevatorSystem.requestLift(0, 'U');
        elevatorSystem.tick();
        elevatorSystem.init(8, numLifts, 4, helper);
        assertEquals("0-I-0", elevatorSystem.getLiftState(0));
        assertEquals("0-I-0", elevatorSystem.getLiftState(1));
        assertEquals("0-I-0", elevatorSystem.getLiftState(2));
        printAllLiftStates(numLifts);
        System.out.println();
    }
    
    void testEdgeCases() {
        System.out.println("🧪 Test: Edge Cases");
        int numLifts = 1;
        elevatorSystem.init(2, numLifts, 1, helper);
        assertEquals("0-I-0", elevatorSystem.getLiftState(0));
        int result = elevatorSystem.requestLift(1, 'D');
        assertTrue(result >= 0);
        elevatorSystem.pressFloorButtonInLift(0, 0);
        elevatorSystem.tick();
        String state = elevatorSystem.getLiftState(0);
        System.out.println("[DEBUG] state: " + state);
        assertTrue(state.startsWith("1-D-1"));
        printAllLiftStates(numLifts);
        System.out.println();
    }
} 
