package lld.elevatorSystem;

public class DebugTest {
    public static void main(String[] args) {
        System.out.println("🔍 Debug Test - Elevator System");
        System.out.println("===============================\n");
        
        Q11ElevatorSystemInterface elevatorSystem = new Solution();
        Helper11 helper = new Helper11();
        
        // Simple test to verify destination addition
        System.out.println("=== Simple Destination Test ===");
        elevatorSystem.init(6, 1, 2, helper);
        
        System.out.println("Initial state: " + elevatorSystem.getLiftState(0));
        
        // Just press button for floor 4 without requesting lift
        elevatorSystem.pressFloorButtonInLift(0, 4);
        System.out.println("After button press: " + elevatorSystem.getLiftState(0));
        
        // Test Lift Completion step by step (exactly like the test)
        System.out.println("\n=== Debug: Lift Completion Test (4 ticks) ===");
        elevatorSystem.init(6, 1, 2, helper);
        
        System.out.println("Initial state: " + elevatorSystem.getLiftState(0));
        
        int liftIndex = elevatorSystem.requestLift(0, 'U');
        System.out.println("After request: " + elevatorSystem.getLiftState(0));
        
        elevatorSystem.pressFloorButtonInLift(liftIndex, 4);
        System.out.println("After button press: " + elevatorSystem.getLiftState(0));
        
        for (int i = 1; i <= 4; i++) {
            elevatorSystem.tick();
            System.out.println("After tick " + i + ": " + elevatorSystem.getLiftState(0));
        }
        
        // Add one more tick to see if it becomes idle
        elevatorSystem.tick();
        System.out.println("After tick 5: " + elevatorSystem.getLiftState(0));
        
        System.out.println("\n=== Debug: Multiple Requests Test ===");
        elevatorSystem.init(6, 2, 2, helper);
        int lift0 = elevatorSystem.requestLift(0, 'U');
        elevatorSystem.pressFloorButtonInLift(lift0, 4);
        int lift1 = elevatorSystem.requestLift(5, 'D');
        System.out.println("After requests - Lift 0: " + elevatorSystem.getLiftState(lift0) + ", Lift 1: " + elevatorSystem.getLiftState(lift1));
        
        elevatorSystem.tick();
        System.out.println("After tick 1 - Lift 0: " + elevatorSystem.getLiftState(lift0) + ", Lift 1: " + elevatorSystem.getLiftState(lift1));
        
        elevatorSystem.tick();
        System.out.println("After tick 2 - Lift 0: " + elevatorSystem.getLiftState(lift0) + ", Lift 1: " + elevatorSystem.getLiftState(lift1));
        
        System.out.println("\n=== Debug: Edge Cases Test ===");
        elevatorSystem.init(2, 1, 1, helper);
        System.out.println("Initial state: " + elevatorSystem.getLiftState(0));
        
        int result = elevatorSystem.requestLift(1, 'D');
        System.out.println("After request from floor 1: " + elevatorSystem.getLiftState(0));
        
        elevatorSystem.pressFloorButtonInLift(0, 0);
        System.out.println("After button press for floor 0: " + elevatorSystem.getLiftState(0));
        
        elevatorSystem.tick();
        System.out.println("After tick: " + elevatorSystem.getLiftState(0));
        
        elevatorSystem.tick();
        System.out.println("After tick 2: " + elevatorSystem.getLiftState(0));
    }
} 