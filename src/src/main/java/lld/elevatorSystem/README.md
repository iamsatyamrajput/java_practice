# 🏗️ Elevator System - Low Level Design

## 📋 Problem Statement

Design a simple elevator management system consisting of multiple lifts in a building with multiple floors.

### 🎯 Key Requirements

- **Lifts**: Numbered 0 to lifts-1
- **Floors**: Numbered 0 to floors-1  
- **Capacity**: Each lift can carry same number of people (liftsCapacity)
- **Movement**: Each lift takes exactly 1 second to move between floors
- **Direction**: Lift direction cannot be changed once set
- **Optimization**: System must select most optimal lift for requests

### 📊 Constraints

- 2 ≤ lifts ≤ 100
- 2 ≤ floors ≤ 200
- 1 ≤ liftsCapacity ≤ 10
- Single-threaded environment
- All lifts start IDLE at ground floor (0th floor)

## 🏗️ System Architecture

### 📦 Package Structure

```
src/main/java/lld/elevatorSystem/
├── Q11ElevatorSystemInterface.java  # Main interface
├── Helper11.java                    # Utility class for logging
├── ElevatorSystemExample.java       # Usage examples
├── ElevatorSystemTestRunner.java    # Test runner
└── README.md                        # Documentation
```

### 🔧 Interface Methods

#### 1. `void init(int floors, int lifts, int liftsCapacity, Helper11 helper)`
- Initialize/reinitialize the elevator system
- Reset all instance variables
- Use helper.print() and helper.println() for logging

#### 2. `int requestLift(int startFloor, char direction)`
- Called when users push up/down button outside any lift
- `startFloor`: Current floor of the user
- `direction`: 'U' for up, 'D' for down
- Returns: Lift index (0 to lifts-1) or -1 if no lift can be assigned

#### 3. `void pressFloorButtonInLift(int liftIndex, int floor)`
- Called when user presses destination floor button inside lift
- `liftIndex`: Index of the lift (0 to lifts-1)
- `floor`: Destination floor

#### 4. `String getLiftState(int liftIndex)`
- Returns current state of a lift
- Format: `"currentFloor-direction-peopleCount"`
- Direction: U (up), D (down), I (idle)
- Examples: "4-U-10", "12-D-2", "0-I-0"

#### 5. `void tick()`
- Called every second to update lift states
- Use for time tracking instead of java.util.Date().time

## 🎯 Lift Assignment Logic

### ✅ Eligibility Criteria

A lift is **ELIGIBLE** if it meets all conditions:

1. **Not Passed**: Lift hasn't passed the requesting floor
2. **Direction Match**: Lift direction matches request direction
3. **No Opposite Requests**: No pending requests in opposite direction
4. **Capacity Available**: Lift has capacity for more people
5. **Valid Floor**: Request floor is within building range

### 🏆 Optimal Selection

Among eligible lifts, select the one that:
- Takes minimum time to reach startFloor
- If multiple lifts have same time, select smallest index

### ⏱️ Time Calculation Examples

```
request(10, 'U'), lift at floor 8 moving up → Time = 2 seconds
request(10, 'D'), lift at floor 12 moving down → Time = 2 seconds
request(10, 'U'), lift at floor 8 idle → Time = 2 seconds
request(10, 'D'), lift at floor 8 moving up with requests(16,'D'), (12,'D') → Time = 16-8+16-10 = 14 seconds
```

## 🧪 Testing

### Running Tests

```bash
# Compile the system
javac -cp . lld/elevatorSystem/*.java

# Run example
java lld.elevatorSystem.ElevatorSystemExample

# Run test runner (when implementation is complete)
java lld.elevatorSystem.ElevatorSystemTestRunner
```

### Test Scenarios

1. **Basic Initialization**: Verify lifts start at floor 0, idle, empty
2. **Basic Lift Request**: Simple up/down requests
3. **Multiple Requests**: Multiple simultaneous requests
4. **Lift Capacity**: Test capacity limits
5. **Direction Constraints**: Verify direction cannot be changed
6. **Optimal Selection**: Test lift selection algorithm
7. **Lift Completion**: Verify lifts become idle after completing requests
8. **Invalid Requests**: Test boundary conditions
9. **Complex Scenarios**: Multiple lifts, multiple floors
10. **Reinitialization**: Test system reset

## 📝 Example Usage

```java
// Initialize system
Q11ElevatorSystemInterface elevatorSystem = new ElevatorSystem();
Helper11 helper = new Helper11();
elevatorSystem.init(6, 2, 2, helper);

// Request lift
int liftIndex = elevatorSystem.requestLift(0, 'U');
elevatorSystem.pressFloorButtonInLift(liftIndex, 4);

// Simulate time
elevatorSystem.tick();

// Check state
String state = elevatorSystem.getLiftState(liftIndex);
// Expected: "1-U-1" (floor 1, going up, 1 person)
```

## 🎯 Implementation Notes

### Key Design Considerations

1. **State Management**: Track each lift's current floor, direction, and passenger count
2. **Request Queue**: Maintain pending requests for each lift
3. **Direction Logic**: Implement complex direction constraints
4. **Time Calculation**: Calculate optimal lift selection based on time
5. **Capacity Management**: Track and enforce lift capacity limits

### Data Structures

- **Lift States**: Array/List to track each lift's state
- **Request Queues**: Priority queues for pending requests
- **Direction Tracking**: Track current and pending directions
- **Time Calculation**: Efficient algorithms for optimal selection

### Thread Safety

- Single-threaded environment
- No concurrent access concerns
- Focus on correct state management

## 🚀 Next Steps

1. **Implement ElevatorSystem class** with all interface methods
2. **Add comprehensive error handling**
3. **Implement optimal lift selection algorithm**
4. **Add detailed logging and debugging**
5. **Create performance benchmarks**
6. **Add more edge case tests**

This elevator system provides a solid foundation for understanding complex scheduling algorithms and state management in real-world systems. 