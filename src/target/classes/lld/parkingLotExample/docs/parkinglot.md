# 🚗 Parking Lot System Design (Multi-Threaded Java)

## Problem Statement

Design a **multi-threaded parking lot system** with the following features:

---

## ✅ Parking Lot Features

- Two types of parking spots:
    - Type `2`: For 2-wheeler vehicles
    - Type `4`: For 4-wheeler vehicles
- Multiple floors, each with:
    - Equal number of rows and columns
    - Some spots are **inactive** (e.g., `2-0`, `4-0`)
- Java implementation will be tested in a **multi-threaded environment**.
- Python implementation will be tested in a **single-threaded environment**.

---

## 🛠️ Methods to Implement in `Solution` Class

### `void init(Helper helper, String[][][] parking)`

- Initializes the parking lot.
- `helper.print()` and `helper.println()` can be used for logging.
- `parking[i][j][k]` refers to the spot at:
    - Floor `i`, Row `j`, Column `k`
    - Examples:
        - `"4-1"` → active 4-wheeler spot
        - `"2-1"` → active 2-wheeler spot
        - `"4-0"` or `"2-0"` → inactive spots

---

### `ParkingResult park(int vehicleType, String vehicleNumber, String ticketId)`

- Assigns an empty spot to the vehicle.
- Maps the vehicle to the assigned `spotId`.
- Returns a `ParkingResult` object with:
    - `status`: 201 if success, 404 if no available spot
    - `spotId`: e.g., `"2-0-15"`
    - `vehicleNumber`
    - `ticketId`

---

### `int removeVehicle(String spotId, String vehicleNumber, String ticketId)`

- Unparks the vehicle using one of the identifiers.
- Exactly one of the 3 parameters will be non-blank.
- Returns:
    - `201` → success
    - `404` → failure (vehicle not found)

---

### `ParkingResult searchVehicle(String spotId, String vehicleNumber, String ticketId)`

- Searches for a vehicle using one of the identifiers.
- Even if the vehicle is already removed, should return the last `spotId`.
- Returns a `ParkingResult` with:
    - `status`
    - `spotId`
    - `vehicleNumber`
    - `ticketId`

---

### `int getFreeSpotsCount(int floor, int vehicleType)`

- Returns the count of free spots for a given `vehicleType` (2 or 4) on the specified `floor`.

---

## 🔢 Constraints

- `vehicleType = 2` for two-wheelers, `4` for four-wheelers
- `1 <= floors <= 5`
- `1 <= rows, columns <= 10,000`
- `rows * columns <= 10,000`
- Java variables use `camelCase`, Python uses `snake_case`

---

## 📥 Input Example 1

```java
parking = [[
  ["4-1", "4-1", "2-1", "2-0"],
  ["2-1", "4-1", "2-1", "2-1"],
  ["4-0", "2-1", "4-0", "2-1"],
  ["4-1", "4-1", "4-1", "2-1"]
]]

park(4, "bh234", "tkt4534")
// returns: ParkingResult { status: 201, spotId: "0-3-1", vehicleNumber: "bh234", ticketId: "tkt4534" }

searchVehicle("", "bh234", "")
// returns: ParkingResult with spotId: "0-3-1"

getFreeSpotsCount(0, 4)
// returns: 5

removeVehicle("0-3-1", "", "")
// returns: 201

getFreeSpotsCount(0, 4)
// returns: 6
