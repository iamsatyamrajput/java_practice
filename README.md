# 🚀 Java Learning & Practice Repository

A comprehensive collection of Java examples, Low-Level Design (LLD) implementations, and practical coding exercises.

## 📁 Project Structure

```
demo/
├── src/
│   ├── pom.xml                                    # Maven configuration
│   ├── src/
│   │   ├── main/
│   │   │   └── java/
│   │   │       ├── lld/                          # Low-Level Design Examples
│   │   │       │   ├── elevatorSystem/           # Elevator Management System
│   │   │       │   └── parkingLotExample/        # Parking Lot Management System
│   │   │       └── org/
│   │   │           └── example/                  # Core Java Examples
│   │   │               ├── db/                   # Database Examples
│   │   │               ├── hashmapExample/       # HashMap Implementations
│   │   │               ├── local_db/             # Local Database Examples
│   │   │               ├── rpc/                  # RPC Examples
│   │   │               ├── threading/            # Multi-threading Examples
│   │   │               ├── websocket/            # WebSocket Examples
│   │   │               └── Main.java             # Main entry point
│   │   └── test/
│   │       └── java/
│   │           └── lld/
│   │               └── parkingLotExample/        # Test classes
│   └── target/                                   # Compiled classes
├── LICENSE
└── README.md                                     # This file
```

## 🎯 Categories & Code Paths

### 1. 🏗️ Low-Level Design (LLD) Examples
**Path**: `src/src/main/java/lld/`

#### A. Elevator System (`lld/elevatorSystem/`)
- **Purpose**: Complete elevator management system with multiple lifts
- **Key Files**:
  - `Q11ElevatorSystemInterface.java` - Main interface
  - `Solution.java` - Implementation
  - `ElevatorSystemExample.java` - Usage examples
  - `ElevatorSystemTestRunner.java` - Test runner
  - `Helper11.java` - Utility class
  - `README.md` - Detailed documentation

#### B. Parking Lot System (`lld/parkingLotExample/`)
- **Purpose**: Parking lot management with organized package structure
- **Package Structure**:
  - `core/` - Main interfaces and implementations
  - `model/` - Data models (Vehicle, ParkingSpot, ParkingResult)
  - `manager/` - Business logic managers
  - `util/` - Utility classes
  - `tests/` - Test classes
  - `docs/` - Documentation and UML diagrams

### 2. 🔧 Core Java Examples
**Path**: `src/src/main/java/org/example/`

#### A. Database Examples (`org/example/db/`)
- **Files**:
  - `KeyValueDatabase.java` - Simple key-value database implementation
  - `KeyValueDatabaseTest.java` - Test class

#### B. HashMap Examples (`org/example/hashmapExample/`)
- **Files**:
  - `HashMap1.java` - Custom HashMap implementation

#### C. Local Database (`org/example/local_db/`)
- **Files**:
  - `KeyValueDatabase.java` - Local database implementation
  - `KeyValueDatabaseTest.java` - Test class

#### D. RPC Examples (`org/example/rpc/`)
- **Purpose**: Remote Procedure Call implementations
- **Status**: Directory exists, implementation pending

#### E. Multi-threading Examples (`org/example/threading/`)
- **Files**:
  - `ThreadTest.java` - Basic thread creation and management
  - `ThreadMethods.java` - Thread lifecycle methods
  - `Syncronization.java` - Thread synchronization examples
  - `Locks.java` - Lock mechanisms
  - `FairnessExample.java` - Fairness in thread scheduling
  - `OddEvenPrint.java` - Print odd/even numbers using threads
  - `OddEvenPrint2.java` - Alternative odd/even printing
  - `OddEvenGaurentee.java` - Guaranteed odd/even printing order
  - `PrintABC.java` - Print A, B, C in sequence using threads
  - `ProducerConsumerExample.java` - Producer-consumer pattern
  - `ChatExample.java` - Multi-threaded chat simulation
  - `ExecutorExample.java` - Executor service examples

#### F. WebSocket Examples (`org/example/websocket/`)
- **Files**:
  - `ChatEndpoint.java` - WebSocket server endpoint
  - `WebSocketClient.java` - WebSocket client implementation

#### G. Main Entry Point (`org/example/Main.java`)
- **Purpose**: Main class that currently runs KeyValueDatabaseTest

## 🛠️ Build & Run Instructions

### Prerequisites
- Java 11 (as specified in pom.xml)
- Maven 3.6+

### Build the Project
```bash
cd src
mvn clean compile
```

### Run Examples

#### 1. Run Main Class
```bash
cd src
mvn exec:java -Dexec.mainClass="org.example.Main"
```

#### 2. Run Elevator System
```bash
cd src
javac -cp . lld/elevatorSystem/*.java
java lld.elevatorSystem.ElevatorSystemExample
```

#### 3. Run Parking Lot System
```bash
cd src
javac -cp . lld/parkingLotExample/core/*.java lld/parkingLotExample/model/*.java lld/parkingLotExample/manager/*.java lld/parkingLotExample/util/*.java
java lld.parkingLotExample.tests.ParkingLotTestRunner
```

#### 4. Run Individual Threading Examples
```bash
cd src
javac -cp . org/example/threading/*.java
java org.example.threading.ThreadTest
java org.example.threading.ProducerConsumerExample
java org.example.threading.ChatExample
```

### Run Tests
```bash
cd src
mvn test
```

## 📚 Learning Paths

### 1. 🆕 Beginner Path
1. Start with `org/example/threading/ThreadTest.java`
2. Progress to `org/example/threading/ThreadMethods.java`
3. Learn synchronization with `org/example/threading/Syncronization.java`
4. Practice with `org/example/threading/OddEvenPrint.java`

### 2. 🏗️ LLD Practice Path
1. Study `lld/parkingLotExample/` - Well-organized package structure
2. Implement `lld/elevatorSystem/` - Complex state management
3. Review UML diagrams in `lld/parkingLotExample/docs/`

### 3. 🔧 Advanced Java Path
1. Database implementations in `org/example/db/`
2. Custom HashMap in `org/example/hashmapExample/`
3. WebSocket implementations in `org/example/websocket/`
4. Complex threading patterns in `org/example/threading/`

## 🎯 Key Learning Objectives

### Low-Level Design
- **System Design**: Elevator and Parking Lot systems
- **Package Organization**: Proper separation of concerns
- **Interface Design**: Clean API contracts
- **State Management**: Complex state transitions
- **Algorithm Implementation**: Optimal resource allocation

### Java Fundamentals
- **Multi-threading**: Thread lifecycle, synchronization, locks
- **Collections**: Custom HashMap implementation
- **I/O**: WebSocket communication
- **Design Patterns**: Producer-consumer, manager patterns
- **Testing**: Unit testing with JUnit

### Best Practices
- **Code Organization**: Logical package structure
- **Documentation**: Comprehensive README files
- **Error Handling**: Proper exception management
- **Performance**: Efficient algorithms and data structures
- **Maintainability**: Clean, readable code

## 🔍 Quick Reference

### Common Commands
```bash
# Compile specific package
javac -cp . org/example/threading/*.java

# Run specific class
java org.example.threading.ThreadTest

# Build with Maven
mvn clean compile

# Run tests
mvn test
```

### Key Files by Category
- **Threading**: `org/example/threading/ProducerConsumerExample.java`
- **LLD**: `lld/elevatorSystem/Solution.java`
- **Database**: `org/example/db/KeyValueDatabase.java`
- **WebSocket**: `org/example/websocket/ChatEndpoint.java`
- **Documentation**: `lld/parkingLotExample/docs/ParkingLotUML.md`

## 🚀 Next Steps

1. **Complete RPC Implementation**: Add examples in `org/example/rpc/`
2. **Add More LLD Examples**: Implement additional system designs
3. **Enhance Testing**: Add comprehensive test coverage
4. **Performance Benchmarks**: Add performance testing
5. **Documentation**: Add more detailed comments and examples

## 📝 Notes

- All examples are self-contained and can be run independently
- The project uses Java 11 features where applicable
- Maven is configured for easy dependency management
- Each category has its own documentation and examples
- The structure follows standard Java package conventions

This repository serves as a comprehensive learning resource for Java development, system design, and practical programming concepts.