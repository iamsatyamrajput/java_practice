# 🏗️ Parking Lot System - UML Class Diagram

## 📊 Complete UML Class Diagram

```mermaid
classDiagram
    %% Interface
    class Q001ParkingLotInterface {
        <<interface>>
        +init(Helper01 helper, String[][][] parking) void
        +park(int vehicleType, String vehicleNumber, String ticketId) ParkingResult
        +removeVehicle(String spotId, String vehicleNumber, String ticketId) int
        +searchVehicle(String spotId, String vehicleNumber, String ticketId) ParkingResult
        +getFreeSpotsCount(int floor, int vehicleType) int
    }

    %% Main Solution Class
    class Solution {
        -ParkingLotManager parkingLotManager
        +Solution()
        +init(Helper01 helper, String[][][] parking) void
        +park(int vehicleType, String vehicleNumber, String ticketId) ParkingResult
        +removeVehicle(String spotId, String vehicleNumber, String ticketId) int
        +searchVehicle(String spotId, String vehicleNumber, String ticketId) ParkingResult
        +getFreeSpotsCount(int floor, int vehicleType) int
    }

    %% Core Business Logic Manager
    class ParkingLotManager {
        -Helper01 helper
        -String[][][] parkingLot
        -VehicleManager vehicleManager
        -SpotManager spotManager
        +init(Helper01 helper, String[][][] parking) void
        +park(int vehicleType, String vehicleNumber, String ticketId) ParkingResult
        +removeVehicle(String spotId, String vehicleNumber, String ticketId) int
        +searchVehicle(String spotId, String vehicleNumber, String ticketId) ParkingResult
        +getFreeSpotsCount(int floor, int vehicleType) int
        -isValidParkingRequest(int vehicleType, String vehicleNumber, String ticketId) boolean
    }

    %% Vehicle Management
    class VehicleManager {
        -Map~String, Vehicle~ spotToVehicle
        -Map~String, String~ vehicleToSpot
        -Map~String, String~ ticketToSpot
        -ReentrantReadWriteLock lock
        +addVehicle(Vehicle vehicle, String spotId) void
        +removeVehicle(Vehicle vehicle) void
        +findVehicle(String spotId, String vehicleNumber, String ticketId) Vehicle
        +isVehicleAlreadyParked(String vehicleNumber, String ticketId) boolean
    }

    %% Spot Management
    class SpotManager {
        -List~ParkingSpot~ allSpots
        -AtomicInteger[][] freeSpotsCount
        -ReentrantReadWriteLock lock
        +SpotManager()
        +initializeSpots(String[][][] parking) void
        +findAvailableSpot(int vehicleType) ParkingSpot
        +occupySpot(ParkingSpot spot) void
        +freeSpot(ParkingSpot spot) void
        +getSpotById(String spotId) ParkingSpot
        +getFreeSpotsCount(int floor, int vehicleType) int
        -createSpot(int floor, int row, int col, String spotConfig) ParkingSpot
    }

    %% Data Classes
    class Vehicle {
        -String vehicleNumber
        -String ticketId
        -int vehicleType
        -String spotId
        +Vehicle(String vehicleNumber, String ticketId, int vehicleType)
        +getVehicleNumber() String
        +getTicketId() String
        +getVehicleType() int
        +getSpotId() String
        +setSpotId(String spotId) void
        +equals(Object obj) boolean
        +hashCode() int
    }

    class ParkingSpot {
        -int floor
        -int row
        -int col
        -int vehicleType
        -String spotId
        -boolean isOccupied
        -boolean isActive
        +ParkingSpot(int floor, int row, int col, int vehicleType, String spotId, boolean isActive)
        +getFloor() int
        +getRow() int
        +getCol() int
        +getVehicleType() int
        +getSpotId() String
        +isOccupied() boolean
        +isActive() boolean
        +setOccupied(boolean occupied) void
    }

    %% External Classes
    class ParkingResult {
        -int status
        -String spotId
        -String vehicleNumber
        -String ticketId
        +ParkingResult(int status, String spotId, String vehicleNumber, String ticketId)
        +getStatus() int
        +getSpotId() String
        +getVehicleNumber() String
        +getTicketId() String
        +equals(Object obj) boolean
        +toString() String
    }

    class Helper01 {
        +print(String s) void
        +println(String s) void
        +getSpotId(int floor, int row, int column) String
        +getSpotLocation(String spotId) Integer[]
    }

    %% Relationships
    Solution ..|> Q001ParkingLotInterface : implements
    Solution --> ParkingLotManager : uses
    
    ParkingLotManager --> VehicleManager : manages
    ParkingLotManager --> SpotManager : manages
    ParkingLotManager --> Helper01 : uses
    ParkingLotManager --> Vehicle : creates
    ParkingLotManager --> ParkingResult : returns
    
    VehicleManager --> Vehicle : manages
    SpotManager --> ParkingSpot : manages
    
    Vehicle --> ParkingSpot : assigned to
```

## 🔄 Sequence Diagram - Parking Operation

```mermaid
sequenceDiagram
    participant Client
    participant Solution
    participant ParkingLotManager
    participant VehicleManager
    participant SpotManager
    participant Vehicle
    participant ParkingSpot
    participant ParkingResult

    Client->>Solution: park(vehicleType, vehicleNumber, ticketId)
    Solution->>ParkingLotManager: park(vehicleType, vehicleNumber, ticketId)
    
    ParkingLotManager->>ParkingLotManager: isValidParkingRequest()
    ParkingLotManager->>VehicleManager: isVehicleAlreadyParked(vehicleNumber, ticketId)
    VehicleManager-->>ParkingLotManager: false
    
    ParkingLotManager->>SpotManager: findAvailableSpot(vehicleType)
    SpotManager-->>ParkingLotManager: ParkingSpot
    
    ParkingLotManager->>ParkingLotManager: new Vehicle(vehicleNumber, ticketId, vehicleType)
    ParkingLotManager->>Vehicle: setSpotId(spotId)
    ParkingLotManager->>VehicleManager: addVehicle(vehicle, spotId)
    ParkingLotManager->>SpotManager: occupySpot(spot)
    ParkingLotManager->>ParkingResult: new ParkingResult(201, spotId, vehicleNumber, ticketId)
    
    ParkingLotManager-->>Solution: ParkingResult
    Solution-->>Client: ParkingResult
```

## 🔄 Sequence Diagram - Vehicle Removal

```mermaid
sequenceDiagram
    participant Client
    participant Solution
    participant ParkingLotManager
    participant VehicleManager
    participant SpotManager

    Client->>Solution: removeVehicle(spotId, vehicleNumber, ticketId)
    Solution->>ParkingLotManager: removeVehicle(spotId, vehicleNumber, ticketId)
    
    ParkingLotManager->>VehicleManager: findVehicle(spotId, vehicleNumber, ticketId)
    VehicleManager-->>ParkingLotManager: Vehicle
    
    ParkingLotManager->>SpotManager: getSpotById(vehicle.spotId)
    SpotManager-->>ParkingLotManager: ParkingSpot
    
    ParkingLotManager->>VehicleManager: removeVehicle(vehicle)
    ParkingLotManager->>SpotManager: freeSpot(spot)
    
    ParkingLotManager-->>Solution: 201
    Solution-->>Client: 201
```

## 🎯 Design Patterns Used

### 1. **Manager Pattern**
- `ParkingLotManager`: Orchestrates business logic
- `VehicleManager`: Manages vehicle data and relationships
- `SpotManager`: Manages parking spots and availability

### 2. **Factory Pattern**
- `SpotManager.createSpot()`: Creates parking spots from configuration

### 3. **Value Object Pattern**
- `Vehicle`: Encapsulates vehicle data
- `ParkingSpot`: Encapsulates spot data

### 4. **Facade Pattern**
- `ParkingLotManager`: Simplifies complex parking operations

## 🏗️ Architecture Benefits

### **Separation of Concerns**
- **Solution**: Interface implementation and delegation
- **ParkingLotManager**: Business logic and coordination
- **VehicleManager**: Vehicle tracking and relationships
- **SpotManager**: Spot management and allocation
- **Vehicle/ParkingSpot**: Data encapsulation

### **Thread Safety**
- `ReentrantReadWriteLock` for concurrent access
- `ConcurrentHashMap` for thread-safe collections
- `AtomicInteger` for thread-safe counters

### **Extensibility**
- Easy to add new vehicle types
- Easy to add new parking strategies
- Easy to add new business rules

### **Maintainability**
- Clear class responsibilities
- Minimal coupling
- Comprehensive error handling

## 📊 Data Flow

```
Client Request → Solution → ParkingLotManager → [VehicleManager + SpotManager] → Response
```

## 🔧 Key Features

1. **Multi-threading Support**: Thread-safe operations
2. **Flexible Search**: Search by spotId, vehicleNumber, or ticketId
3. **Robust Error Handling**: Consistent error responses
4. **Efficient Data Structures**: O(1) lookups for most operations
5. **Business Logic Centralization**: All rules in ParkingLotManager

This UML diagram represents a production-ready, scalable parking lot system with proper design patterns and thread safety. 