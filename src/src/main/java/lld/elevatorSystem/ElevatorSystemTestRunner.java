package lld.elevatorSystem;

public class ElevatorSystemTestRunner {
    
    public static void main(String[] args) {
        System.out.println("🏗️ Elevator System Test Runner");
        System.out.println("==============================\n");
        
        ElevatorSystemTestRunner runner = new ElevatorSystemTestRunner();
        runner.runAllTests();
    }
    
    public void runAllTests() {
        testBasicInitialization();
        testBasicLiftRequest();
        testMultipleRequests();
        testLiftCapacity();
        testLiftDirectionConstraints();
        testOptimalLiftSelection();
        testLiftCompletion();
        testInvalidRequests();
        testComplexScenario();
        testReinitialization();
        testEdgeCases();
        
        System.out.println("\n✅ All tests completed!");
    }
    
    private void testBasicInitialization() {
        System.out.println("🧪 Test: Basic Initialization");
        try {
            Q11ElevatorSystemInterface elevatorSystem = new Solution();
            Helper11 helper = new Helper11();
            
            elevatorSystem.init(6, 2, 2, helper);
            
            String state0 = elevatorSystem.getLiftState(0);
            String state1 = elevatorSystem.getLiftState(1);
            
            assertEqual("0-I-0", state0, "Lift 0 initial state");
            assertEqual("0-I-0", state1, "Lift 1 initial state");
            
            System.out.println("✅ Basic Initialization - PASSED");
        } catch (Exception e) {
            System.out.println("❌ Basic Initialization - FAILED: " + e.getMessage());
        }
    }
    
    private void testBasicLiftRequest() {
        System.out.println("🧪 Test: Basic Lift Request");
        try {
            Q11ElevatorSystemInterface elevatorSystem = new Solution();
            Helper11 helper = new Helper11();
            
            elevatorSystem.init(6, 2, 2, helper);
            
            int liftIndex = elevatorSystem.requestLift(0, 'U');
            assertTrue(liftIndex >= 0 && liftIndex < 2, "Valid lift index");
            
            elevatorSystem.pressFloorButtonInLift(liftIndex, 4);
            elevatorSystem.tick();
            
            String state = elevatorSystem.getLiftState(liftIndex);
            assertTrue(state.startsWith("1-U-1"), "Lift moved up with passenger");
            
            System.out.println("✅ Basic Lift Request - PASSED");
        } catch (Exception e) {
            System.out.println("❌ Basic Lift Request - FAILED: " + e.getMessage());
        }
    }
    
    private void testMultipleRequests() {
        System.out.println("🧪 Test: Multiple Requests");
        try {
            Q11ElevatorSystemInterface elevatorSystem = new Solution();
            Helper11 helper = new Helper11();
            
            elevatorSystem.init(6, 2, 2, helper);
            
            int lift0 = elevatorSystem.requestLift(0, 'U');
            elevatorSystem.pressFloorButtonInLift(lift0, 4);
            
            int lift1 = elevatorSystem.requestLift(5, 'D');
            assertTrue(lift1 >= 0 && lift1 < 2, "Valid second lift");
            assertTrue(lift0 != lift1, "Different lifts assigned");
            
            elevatorSystem.tick();
            elevatorSystem.tick();
            
            String state0 = elevatorSystem.getLiftState(lift0);
            String state1 = elevatorSystem.getLiftState(lift1);
            
            assertTrue(state0.startsWith("2-U-1"), "Lift 0 state after 2 ticks");
            assertTrue(state1.startsWith("2-U-0"), "Lift 1 state after 2 ticks");
            
            System.out.println("✅ Multiple Requests - PASSED");
        } catch (Exception e) {
            System.out.println("❌ Multiple Requests - FAILED: " + e.getMessage());
        }
    }
    
    private void testLiftCapacity() {
        System.out.println("🧪 Test: Lift Capacity");
        try {
            Q11ElevatorSystemInterface elevatorSystem = new Solution();
            Helper11 helper = new Helper11();
            
            elevatorSystem.init(6, 1, 2, helper);
            
            int liftIndex = elevatorSystem.requestLift(0, 'U');
            elevatorSystem.pressFloorButtonInLift(liftIndex, 3);
            elevatorSystem.pressFloorButtonInLift(liftIndex, 4);
            
            elevatorSystem.tick();
            
            String state = elevatorSystem.getLiftState(liftIndex);
            assertTrue(state.endsWith("-2"), "Lift has 2 people");
            
            System.out.println("✅ Lift Capacity - PASSED");
        } catch (Exception e) {
            System.out.println("❌ Lift Capacity - FAILED: " + e.getMessage());
        }
    }
    
    private void testLiftDirectionConstraints() {
        System.out.println("🧪 Test: Lift Direction Constraints");
        try {
            Q11ElevatorSystemInterface elevatorSystem = new Solution();
            Helper11 helper = new Helper11();
            
            elevatorSystem.init(10, 1, 5, helper);
            
            int liftIndex = elevatorSystem.requestLift(2, 'U');
            elevatorSystem.pressFloorButtonInLift(liftIndex, 8);
            
            for (int i = 0; i < 3; i++) {
                elevatorSystem.tick();
            }
            
            int result = elevatorSystem.requestLift(3, 'D');
            assertEqual(-1, result, "Should not assign lift going in opposite direction");
            
            System.out.println("✅ Lift Direction Constraints - PASSED");
        } catch (Exception e) {
            System.out.println("❌ Lift Direction Constraints - FAILED: " + e.getMessage());
        }
    }
    
    private void testOptimalLiftSelection() {
        System.out.println("🧪 Test: Optimal Lift Selection");
        try {
            Q11ElevatorSystemInterface elevatorSystem = new Solution();
            Helper11 helper = new Helper11();
            
            elevatorSystem.init(10, 2, 5, helper);
            
            elevatorSystem.requestLift(2, 'U');
            elevatorSystem.tick();
            elevatorSystem.tick();
            
            elevatorSystem.requestLift(8, 'D');
            for (int i = 0; i < 6; i++) {
                elevatorSystem.tick();
            }
            
            int selectedLift = elevatorSystem.requestLift(5, 'U');
            assertTrue(selectedLift >= 0, "Should select a lift");
            
            System.out.println("✅ Optimal Lift Selection - PASSED");
        } catch (Exception e) {
            System.out.println("❌ Optimal Lift Selection - FAILED: " + e.getMessage());
        }
    }
    
    private void testLiftCompletion() {
        System.out.println("🧪 Test: Lift Completion");
        try {
            Q11ElevatorSystemInterface elevatorSystem = new Solution();
            Helper11 helper = new Helper11();
            
            elevatorSystem.init(6, 1, 2, helper);
            
            int liftIndex = elevatorSystem.requestLift(0, 'U');
            elevatorSystem.pressFloorButtonInLift(liftIndex, 4);
            
            for (int i = 0; i < 4; i++) {
                elevatorSystem.tick();
            }
            
            String state = elevatorSystem.getLiftState(liftIndex);
            assertTrue(state.startsWith("4-I-0"), "Lift should be idle at destination");
            
            System.out.println("✅ Lift Completion - PASSED");
        } catch (Exception e) {
            System.out.println("❌ Lift Completion - FAILED: " + e.getMessage());
        }
    }
    
    private void testInvalidRequests() {
        System.out.println("🧪 Test: Invalid Requests");
        try {
            Q11ElevatorSystemInterface elevatorSystem = new Solution();
            Helper11 helper = new Helper11();
            
            elevatorSystem.init(6, 1, 2, helper);
            
            int result = elevatorSystem.requestLift(10, 'U');
            assertEqual(-1, result, "Invalid floor should return -1");
            
            result = elevatorSystem.requestLift(2, 'X');
            assertEqual(-1, result, "Invalid direction should return -1");
            
            System.out.println("✅ Invalid Requests - PASSED");
        } catch (Exception e) {
            System.out.println("❌ Invalid Requests - FAILED: " + e.getMessage());
        }
    }
    
    private void testComplexScenario() {
        System.out.println("🧪 Test: Complex Scenario");
        try {
            Q11ElevatorSystemInterface elevatorSystem = new Solution();
            Helper11 helper = new Helper11();
            
            elevatorSystem.init(8, 2, 3, helper);
            
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
            
            assertNotNull(state0, "State 0 should not be null");
            assertNotNull(state1, "State 1 should not be null");
            assertTrue(state0.matches("\\d+-[UDI]-\\d+"), "Valid state format 0");
            assertTrue(state1.matches("\\d+-[UDI]-\\d+"), "Valid state format 1");
            
            System.out.println("✅ Complex Scenario - PASSED");
        } catch (Exception e) {
            System.out.println("❌ Complex Scenario - FAILED: " + e.getMessage());
        }
    }
    
    private void testReinitialization() {
        System.out.println("🧪 Test: Reinitialization");
        try {
            Q11ElevatorSystemInterface elevatorSystem = new Solution();
            Helper11 helper = new Helper11();
            
            elevatorSystem.init(6, 2, 2, helper);
            elevatorSystem.requestLift(0, 'U');
            elevatorSystem.tick();
            
            elevatorSystem.init(8, 3, 4, helper);
            
            assertEqual("0-I-0", elevatorSystem.getLiftState(0), "Reset lift 0");
            assertEqual("0-I-0", elevatorSystem.getLiftState(1), "Reset lift 1");
            assertEqual("0-I-0", elevatorSystem.getLiftState(2), "Reset lift 2");
            
            System.out.println("✅ Reinitialization - PASSED");
        } catch (Exception e) {
            System.out.println("❌ Reinitialization - FAILED: " + e.getMessage());
        }
    }
    
    private void testEdgeCases() {
        System.out.println("🧪 Test: Edge Cases");
        try {
            Q11ElevatorSystemInterface elevatorSystem = new Solution();
            Helper11 helper = new Helper11();
            
            elevatorSystem.init(2, 1, 1, helper);
            
            assertEqual("0-I-0", elevatorSystem.getLiftState(0), "Initial state");
            
            int result = elevatorSystem.requestLift(1, 'D');
            assertTrue(result >= 0, "Valid request");
            
            elevatorSystem.pressFloorButtonInLift(0, 0);
            elevatorSystem.tick();
            
            String state = elevatorSystem.getLiftState(0);
            assertTrue(state.startsWith("1-D-1"), "Lift moved down with passenger");
            
            System.out.println("✅ Edge Cases - PASSED");
        } catch (Exception e) {
            System.out.println("❌ Edge Cases - FAILED: " + e.getMessage());
        }
    }
    
    // Helper assertion methods
    private void assertEqual(Object expected, Object actual, String message) {
        if (!expected.equals(actual)) {
            throw new AssertionError(message + ": Expected " + expected + " but got " + actual);
        }
    }
    
    private void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
    
    private void assertNotNull(Object obj, String message) {
        if (obj == null) {
            throw new AssertionError(message);
        }
    }
} 