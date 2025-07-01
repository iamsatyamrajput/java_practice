# 🏗️ Elevator System - Entity Relationship Diagram (ERD)

## 📊 Class Diagram

```mermaid
classDiagram
    %% Main Interface
    class Q11ElevatorSystemInterface {
        <<interface>>
        +init(floors, lifts, liftsCapacity, helper)
        +requestLift(startFloor, direction) int
        +pressFloorButtonInLift(liftIndex, floor)
        +getLiftState(liftIndex) String
        +tick()
    }

    %% Main Implementation
    class Solution {
        -LiftManager liftManager
        +init(floors, lifts, liftsCapacity, helper)
        +requestLift(startFloor, direction) int
        +pressFloorButtonInLift(liftIndex, floor)
        +getLiftState(liftIndex) String
        +tick()
    }

    %% Manager Layer
    class LiftManager {
        -Lift[] liftList
        +init(floors, lifts, liftsCapacity, helper)
        +requestLift(startFloor, direction) int
        +pressFloorButtonInLift(liftIndex, floor)
        +getLiftState(liftIndex) String
        +tick()
    }

    %% Core Entity
    class Lift {
        -LiftStateInterface liftState
        -int floors
        -int liftCapacity
        +Lift(floors, liftCapacity)
        +request(liftRequest) boolean
        +pressFloorButtonInLift(floor)
        +getState() LiftStateInterface
        +setState(newState)
        +tick()
        +getFloors() int
        +getLiftCapacity() int
    }

    %% State Pattern Interface
    class LiftStateInterface {
        <<interface>>
        +move(lift)
        +addStop(lift, floor)
        +tick(lift)
        +getStateString(lift) String
        +canAcceptRequest(lift, request) boolean
        +getDirection() char
    }

    %% Base State Class
    class LiftState {
        <<abstract>>
        #int currentFloor
        #int currentLiftCapacity
        #char direction
        #Set~Integer~ destinationFloors
        #Queue~LiftRequest~ pendingRequests
        +getCurrentFloor() int
        +setCurrentFloor(floor)
        +getCurrentLiftCapacity() int
        +setCurrentLiftCapacity(capacity)
        +getDirection() char
        +getDestinationFloors() Set
        +getPendingRequests() Queue
        +addDestinationFloor(floor)
        +removeDestinationFloor(floor)
        +addPendingRequest(request)
        +hasPendingRequests() boolean
        +hasDestinationFloors() boolean
    }

    %% Concrete State Classes
    class IdleState {
        +IdleState()
        +move(lift)
        +addStop(lift, floor)
        +tick(lift)
        +getStateString(lift) String
        +canAcceptRequest(lift, request) boolean
    }

    class UpState {
        +UpState()
        +UpState(currentFloor, capacity, destinations, requests)
        +move(lift)
        +addStop(lift, floor)
        +tick(lift)
        +getStateString(lift) String
        +canAcceptRequest(lift, request) boolean
    }

    class DownState {
        +DownState()
        +DownState(currentFloor, capacity, destinations, requests)
        +move(lift)
        +addStop(lift, floor)
        +tick(lift)
        +getStateString(lift) String
        +canAcceptRequest(lift, request) boolean
    }

    %% Data Transfer Object
    class LiftRequest {
        +int startFloor
        +char direction
        +LiftRequest(startFloor, direction)
    }

    %% Utility Class
    class Helper11 {
        +print(message)
        +println(message)
    }

    %% Relationships
    Solution ..|> Q11ElevatorSystemInterface : implements
    Solution --> LiftManager : has
    LiftManager --> Lift : manages
    Lift --> LiftStateInterface : has state
    LiftState ..|> LiftStateInterface : implements
    IdleState --|> LiftState : extends
    UpState --|> LiftState : extends
    DownState --|> LiftState : extends
    LiftState --> LiftRequest : contains
    Solution --> Helper11 : uses
```

## 🔗 Relationship Details

### 1. **Solution → LiftManager**
- **Type:** Composition (1:1)
- **Description:** Solution contains one LiftManager instance
- **Purpose:** Delegates all elevator operations to LiftManager

### 2. **LiftManager → Lift**
- **Type:** Aggregation (1:N)
- **Description:** LiftManager manages an array of Lift objects
- **Purpose:** Coordinates multiple lifts in the building

### 3. **Lift → LiftStateInterface**
- **Type:** Association (1:1)
- **Description:** Each Lift has one current state
- **Purpose:** Implements State Pattern for lift behavior

### 4. **LiftState → LiftRequest**
- **Type:** Aggregation (1:N)
- **Description:** State contains collections of requests and destinations
- **Purpose:** Manages pending requests and destination floors

### 5. **State Inheritance Hierarchy**
- **Type:** Inheritance
- **Description:** IdleState, UpState, DownState extend LiftState
- **Purpose:** Different behaviors for different lift states

## 📋 Entity Attributes

### **Solution**
- `liftManager: LiftManager` - Manager instance

### **LiftManager**
- `liftList: Lift[]` - Array of lift instances

### **Lift**
- `liftState: LiftStateInterface` - Current state
- `floors: int` - Total number of floors
- `liftCapacity: int` - Maximum people capacity

### **LiftState (Base)**
- `currentFloor: int` - Current floor position
- `currentLiftCapacity: int` - Current number of people
- `direction: char` - Current direction (U/D/I)
- `destinationFloors: Set<Integer>` - Floors to stop at
- `pendingRequests: Queue<LiftRequest>` - Pending requests

### **LiftRequest**
- `startFloor: int` - Requesting floor
- `direction: char` - Requested direction (U/D)

### **Helper11**
- Utility methods for logging

## 🎯 Design Patterns Used

### 1. **State Pattern**
- **Purpose:** Manage lift behavior based on current state
- **Classes:** LiftStateInterface, LiftState, IdleState, UpState, DownState
- **Benefits:** Clean state transitions, extensible behavior

### 2. **Facade Pattern**
- **Purpose:** Simplify complex subsystem interface
- **Classes:** Solution implements Q11ElevatorSystemInterface
- **Benefits:** Clean API, hides internal complexity

### 3. **Manager Pattern**
- **Purpose:** Centralized coordination of multiple entities
- **Classes:** LiftManager manages Lift array
- **Benefits:** Single point of control, easy scaling

## 🔄 State Transitions

```mermaid
stateDiagram-v2
    [*] --> Idle
    Idle --> Up : addStop(floor > currentFloor)
    Idle --> Down : addStop(floor < currentFloor)
    Up --> Down : no more upward destinations + pending requests
    Up --> Idle : no more destinations + no pending requests
    Down --> Up : no more downward destinations + pending requests
    Down --> Idle : no more destinations + no pending requests
```

## 📊 Data Flow

1. **Initialization:** Solution → LiftManager → Lift[] → IdleState
2. **Request Processing:** Request → LiftManager → Lift → State.canAcceptRequest()
3. **State Changes:** Lift.setState() → New State Instance
4. **Time Simulation:** tick() → State.tick() → Movement/State Transitions
5. **State Reporting:** getLiftState() → State.getStateString()

This ERD shows a well-structured, object-oriented design with clear separation of concerns and proper use of design patterns for the elevator system. 