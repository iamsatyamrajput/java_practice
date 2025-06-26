# 🚗 Parking Lot System - Test Guide

## Overview

This directory contains a comprehensive parking lot system implementation with test classes to validate the functionality. The system supports multi-floor parking with different vehicle types (2-wheelers and 4-wheelers).

## 📁 File Structure

```
parkingLotExample/
├── Q001ParkingLotInterface.java    # Interface defining the parking lot operations
├── ParkingResult.java              # Data class for parking operation results
├── Helper01.java                   # Utility class with helper methods
├── Solution/
│   └── Solution.java               # Your implementation (currently empty)
├── ParkingLotExample.java          # Main example class
├── ParkingLotTestRunner.java       # Simple test runner (no JUnit required)
├── parkinglot.md                   # Detailed problem specification
└── README_TESTS.md                 # This file
```

## 🧪 Test Classes

### 1. ParkingLotTestRunner.java
A simple test runner that doesn't require any external dependencies. You can run this directly to test your implementation.

**To run:**
```bash
cd src/src/main/java
javac lld/parkingLotExample/*.java lld/parkingLotExample/Solution/*.java
java lld.parkingLotExample.ParkingLotTestRunner
```

### 2. ParkingLotTest.java (JUnit 5)
A comprehensive JUnit 5 test suite with detailed test cases. Requires Maven setup.

**To run with Maven:**
```bash
cd src
mvn test -Dtest=ParkingLotTest
```

## 🔧 Required Methods to Implement

Your `Solution` class must implement these methods:

### 1. `void init(Helper01 helper, String[][][] parking)`
- Initializes the parking lot with the given configuration
- `parking[i][j][k]` represents floor i, row j, column k
- Spot values: `"4-1"` (active 4-wheeler), `"2-1"` (active 2-wheeler), `"4-0"` or `"2-0"` (inactive)

### 2. `ParkingResult park(int vehicleType, String vehicleNumber, String ticketId)`
- Parks a vehicle of the specified type (2 or 4)
- Returns `ParkingResult` with status 201 (success) or 404 (no spot available)
- Assigns a spotId in format "floor-row-column"

### 3. `int removeVehicle(String spotId, String vehicleNumber, String ticketId)`
- Removes a vehicle using one of the three identifiers (only one will be non-empty)
- Returns 201 (success) or 404 (vehicle not found)

### 4. `ParkingResult searchVehicle(String spotId, String vehicleNumber, String ticketId)`
- Searches for a vehicle using one of the three identifiers
- Returns vehicle information even if already removed
- Returns status 201 (found) or 404 (not found)

### 5. `int getFreeSpotsCount(int floor, int vehicleType)`
- Returns the number of available spots for the given vehicle type on the specified floor

## 📊 Test Scenarios Covered

### Basic Operations
- ✅ Initial parking lot setup and free spot counting
- ✅ Parking 2-wheelers and 4-wheelers
- ✅ Vehicle removal by spotId, vehicleNumber, or ticketId
- ✅ Vehicle search by spotId, vehicleNumber, or ticketId

### Edge Cases
- ✅ Parking when no spots are available
- ✅ Removing non-existent vehicles
- ✅ Searching for non-existent vehicles
- ✅ Invalid vehicle types
- ✅ Empty vehicle numbers or ticket IDs
- ✅ Parking lots with all inactive spots

### Advanced Scenarios
- ✅ Multiple floors
- ✅ Large parking lots
- ✅ Concurrent operations (multi-threading)
- ✅ Search after vehicle removal

## 🎯 Example Test Data

The test uses a 2-floor parking lot (4x4 spots each):

**Floor 0:**
```
Row 0: ["4-1", "4-1", "2-1", "2-0"]  // 2 active 4-wheelers, 1 active 2-wheeler, 1 inactive
Row 1: ["2-1", "4-1", "2-1", "2-1"]  // 1 active 4-wheeler, 3 active 2-wheelers
Row 2: ["4-0", "2-1", "4-0", "2-1"]  // 2 inactive 4-wheelers, 2 active 2-wheelers
Row 3: ["4-1", "4-1", "4-1", "2-1"]  // 3 active 4-wheelers, 1 active 2-wheeler
```

**Floor 1:**
```
Row 0: ["2-1", "4-1", "2-1", "4-1"]  // 2 active 4-wheelers, 2 active 2-wheelers
Row 1: ["4-1", "2-0", "4-1", "2-1"]  // 2 active 4-wheelers, 1 active 2-wheeler, 1 inactive
Row 2: ["2-1", "4-1", "2-1", "4-0"]  // 1 active 4-wheeler, 3 active 2-wheelers, 1 inactive
Row 3: ["4-1", "2-1", "4-1", "2-1"]  // 2 active 4-wheelers, 2 active 2-wheelers
```

**Expected Free Spots:**
- Floor 0: 5 active 4-wheelers, 8 active 2-wheelers
- Floor 1: 7 active 4-wheelers, 7 active 2-wheelers

## 🚀 Getting Started

1. **Implement the Solution class** in `Solution/Solution.java`
2. **Run the simple test runner** to verify basic functionality:
   ```bash
   java lld.parkingLotExample.ParkingLotTestRunner
   ```
3. **Run JUnit tests** (if Maven is set up):
   ```bash
   mvn test -Dtest=ParkingLotTest
   ```

## 🔍 Understanding the Problem

The parking lot system is designed to handle:
- **Multi-floor parking** with different vehicle types
- **Active/Inactive spots** (spots marked with "-0" are inactive)
- **Multi-threaded operations** (Java implementation)
- **Spot assignment** in format "floor-row-column"
- **Vehicle tracking** by spotId, vehicleNumber, or ticketId

The system should be thread-safe and handle concurrent parking/removal operations correctly.

## 📝 Notes

- The current `Solution.java` is empty - you need to implement all methods
- The system supports both 2-wheelers (type 2) and 4-wheelers (type 4)
- Inactive spots (marked with "-0") should not be used for parking
- The `Helper01` class provides utility methods for spot ID generation and parsing
- All operations should be thread-safe for the Java implementation 