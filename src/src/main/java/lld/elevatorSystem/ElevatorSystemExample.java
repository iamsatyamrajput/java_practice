package lld.elevatorSystem;

public class ElevatorSystemExample {
    
    public static void main(String[] args) {
        System.out.println("🏗️ Elevator System Example");
        System.out.println("==========================\n");
        
        // This example demonstrates how the elevator system interface would be used
        // Note: This requires an actual implementation of ElevatorSystem class
        
        System.out.println("Example usage of the elevator system:");
        System.out.println("1. Initialize system with 6 floors, 2 lifts, capacity 2");
        System.out.println("2. Request lift from floor 0 going up");
        System.out.println("3. Press floor button inside lift to go to floor 4");
        System.out.println("4. Simulate time passage with tick()");
        System.out.println("5. Check lift state");
        
        System.out.println("\nExpected output:");
        System.out.println("init(6, 2, 2, helper)");
        System.out.println("requestLift(0, 'U') -> returns lift index (0 or 1)");
        System.out.println("pressFloorButtonInLift(liftIndex, 4)");
        System.out.println("tick()");
        System.out.println("getLiftState(liftIndex) -> returns \"1-U-1\"");
        
        System.out.println("\nTo run actual tests, implement the ElevatorSystem class");
        System.out.println("and then run ElevatorSystemTestRunner.main()");
    }
    
    public static void demonstrateInterface() {
        System.out.println("\n📋 Interface Methods:");
        System.out.println("1. void init(int floors, int lifts, int liftsCapacity, Helper11 helper)");
        System.out.println("2. int requestLift(int startFloor, char direction)");
        System.out.println("3. void pressFloorButtonInLift(int liftIndex, int floor)");
        System.out.println("4. String getLiftState(int liftIndex)");
        System.out.println("5. void tick()");
        
        System.out.println("\n🎯 Key Requirements:");
        System.out.println("- Lifts are numbered 0 to lifts-1");
        System.out.println("- Floors are numbered 0 to floors-1");
        System.out.println("- Each lift takes 1 second to move between floors");
        System.out.println("- Lift direction cannot be changed once set");
        System.out.println("- System must select most optimal lift for requests");
        System.out.println("- State format: \"currentFloor-direction-peopleCount\"");
        System.out.println("  direction: U (up), D (down), I (idle)");
    }
} 