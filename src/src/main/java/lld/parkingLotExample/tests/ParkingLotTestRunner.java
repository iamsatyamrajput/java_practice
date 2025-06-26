package lld.parkingLotExample.tests;

import lld.parkingLotExample.core.Solution;
import lld.parkingLotExample.core.Q001ParkingLotInterface;
import lld.parkingLotExample.model.ParkingResult;
import lld.parkingLotExample.util.Helper01;

public class ParkingLotTestRunner {
    
    public static void main(String[] args) {
        System.out.println("🚗 Parking Lot System Test Runner");
        System.out.println("================================\n");
        
        ParkingLotTestRunner runner = new ParkingLotTestRunner();
        
        // Run all tests
        runner.testInitialSetup();
        runner.testParkingOperations();
        runner.testRemovalOperations();
        runner.testSearchOperations();
        runner.testEdgeCases();
        runner.testConcurrentOperations();
        
        System.out.println("\n✅ All tests completed!");
    }
    
    private void testInitialSetup() {
        System.out.println("📋 Testing Initial Setup...");
        
        Solution parkingLot = new Solution();
        Helper01 helper = new Helper01();
        
        // Create test parking lot
        String[][][] parking = createTestParkingLot();
        parkingLot.init(helper, parking);
        
        // Test initial free spots count
        int free4Wheelers = parkingLot.getFreeSpotsCount(0, 4);
        int free2Wheelers = parkingLot.getFreeSpotsCount(0, 2);
        
        System.out.println("   Floor 0 - Free 4-wheelers: " + free4Wheelers + " (expected: 5)");
        System.out.println("   Floor 0 - Free 2-wheelers: " + free2Wheelers + " (expected: 8)");
        
        if (free4Wheelers == 5 && free2Wheelers == 8) {
            System.out.println("   ✅ Initial setup test passed");
        } else {
            System.out.println("   ❌ Initial setup test failed");
        }
        System.out.println();
    }
    
    private void testParkingOperations() {
        System.out.println("🚗 Testing Parking Operations...");
        
        Solution parkingLot = new Solution();
        Helper01 helper = new Helper01();
        String[][][] parking = createTestParkingLot();
        parkingLot.init(helper, parking);
        
        // Test parking 4-wheeler
        ParkingResult result1 = parkingLot.park(4, "CAR001", "TKT001");
        if (result1 != null && result1.getStatus() == 201) {
            System.out.println("   ✅ 4-wheeler parking successful: " + result1.getSpotId());
        } else {
            System.out.println("   ❌ 4-wheeler parking failed");
        }
        
        // Test parking 2-wheeler
        ParkingResult result2 = parkingLot.park(2, "BIKE001", "TKT002");
        if (result2 != null && result2.getStatus() == 201) {
            System.out.println("   ✅ 2-wheeler parking successful: " + result2.getSpotId());
        } else {
            System.out.println("   ❌ 2-wheeler parking failed");
        }
        
        // Test parking when full
        for (int i = 0; i < 10; i++) {
            parkingLot.park(4, "CAR" + i, "TKT" + i);
        }
        ParkingResult result3 = parkingLot.park(4, "CAR999", "TKT999");
        if (result3 != null && result3.getStatus() == 404) {
            System.out.println("   ✅ Full parking lot handled correctly");
        } else {
            System.out.println("   ❌ Full parking lot not handled correctly");
        }
        System.out.println();
    }
    
    private void testRemovalOperations() {
        System.out.println("🚪 Testing Removal Operations...");
        
        Solution parkingLot = new Solution();
        Helper01 helper = new Helper01();
        String[][][] parking = createTestParkingLot();
        parkingLot.init(helper, parking);
        
        // Park a vehicle
        ParkingResult parkResult = parkingLot.park(4, "CAR001", "TKT001");
        String spotId = parkResult.getSpotId();
        
        // Test removal by spotId
        int removeResult1 = parkingLot.removeVehicle(spotId, "", "");
        if (removeResult1 == 201) {
            System.out.println("   ✅ Removal by spotId successful");
        } else {
            System.out.println("   ❌ Removal by spotId failed");
        }
        
        // Test removal by vehicleNumber
        parkingLot.park(4, "CAR002", "TKT002");
        int removeResult2 = parkingLot.removeVehicle("", "CAR002", "");
        if (removeResult2 == 201) {
            System.out.println("   ✅ Removal by vehicleNumber successful");
        } else {
            System.out.println("   ❌ Removal by vehicleNumber failed");
        }
        
        // Test removal by ticketId
        parkingLot.park(4, "CAR003", "TKT003");
        int removeResult3 = parkingLot.removeVehicle("", "", "TKT003");
        if (removeResult3 == 201) {
            System.out.println("   ✅ Removal by ticketId successful");
        } else {
            System.out.println("   ❌ Removal by ticketId failed");
        }
        
        // Test removal of non-existent vehicle
        int removeResult4 = parkingLot.removeVehicle("0-0-0", "", "");
        if (removeResult4 == 404) {
            System.out.println("   ✅ Non-existent vehicle removal handled correctly");
        } else {
            System.out.println("   ❌ Non-existent vehicle removal not handled correctly");
        }
        System.out.println();
    }
    
    private void testSearchOperations() {
        System.out.println("🔍 Testing Search Operations...");
        
        Solution parkingLot = new Solution();
        Helper01 helper = new Helper01();
        String[][][] parking = createTestParkingLot();
        parkingLot.init(helper, parking);
        
        // Park a vehicle
        ParkingResult parkResult = parkingLot.park(4, "CAR001", "TKT001");
        String spotId = parkResult.getSpotId();
        
        // Test search by spotId
        ParkingResult searchResult1 = parkingLot.searchVehicle(spotId, "", "");
        if (searchResult1 != null && searchResult1.getStatus() == 201) {
            System.out.println("   ✅ Search by spotId successful");
        } else {
            System.out.println("   ❌ Search by spotId failed");
        }
        
        // Test search by vehicleNumber
        ParkingResult searchResult2 = parkingLot.searchVehicle("", "CAR001", "");
        if (searchResult2 != null && searchResult2.getStatus() == 201) {
            System.out.println("   ✅ Search by vehicleNumber successful");
        } else {
            System.out.println("   ❌ Search by vehicleNumber failed");
        }
        
        // Test search by ticketId
        ParkingResult searchResult3 = parkingLot.searchVehicle("", "", "TKT001");
        if (searchResult3 != null && searchResult3.getStatus() == 201) {
            System.out.println("   ✅ Search by ticketId successful");
        } else {
            System.out.println("   ❌ Search by ticketId failed");
        }
        
        // Test search after removal
        parkingLot.removeVehicle(spotId, "", "");
        ParkingResult searchResult4 = parkingLot.searchVehicle("", "CAR001", "");
        if (searchResult4 != null && searchResult4.getSpotId().equals(spotId)) {
            System.out.println("   ✅ Search after removal successful");
        } else {
            System.out.println("   ❌ Search after removal failed");
        }
        System.out.println();
    }
    
    private void testEdgeCases() {
        System.out.println("⚠️  Testing Edge Cases...");
        
        Solution parkingLot = new Solution();
        Helper01 helper = new Helper01();
        String[][][] parking = createTestParkingLot();
        parkingLot.init(helper, parking);
        
        // Test invalid vehicle type
        ParkingResult result1 = parkingLot.park(3, "CAR001", "TKT001");
        if (result1 != null && result1.getStatus() == 404) {
            System.out.println("   ✅ Invalid vehicle type handled correctly");
        } else {
            System.out.println("   ❌ Invalid vehicle type not handled correctly");
        }
        
        // Test empty vehicle number
        ParkingResult result2 = parkingLot.park(4, "", "TKT001");
        if (result2 != null && result2.getStatus() == 404) {
            System.out.println("   ✅ Empty vehicle number handled correctly");
        } else {
            System.out.println("   ❌ Empty vehicle number not handled correctly");
        }
        
        // Test all inactive spots
        String[][][] inactiveParking = new String[1][2][2];
        inactiveParking[0][0] = new String[]{"4-0", "2-0"};
        inactiveParking[0][1] = new String[]{"4-0", "2-0"};
        
        Solution inactiveParkingLot = new Solution();
        inactiveParkingLot.init(helper, inactiveParking);
        
        ParkingResult result3 = inactiveParkingLot.park(4, "CAR001", "TKT001");
        if (result3 != null && result3.getStatus() == 404) {
            System.out.println("   ✅ All inactive spots handled correctly");
        } else {
            System.out.println("   ❌ All inactive spots not handled correctly");
        }
        System.out.println();
    }
    
    private void testConcurrentOperations() {
        System.out.println("🔄 Testing Concurrent Operations...");
        
        Solution parkingLot = new Solution();
        Helper01 helper = new Helper01();
        String[][][] parking = createTestParkingLot();
        parkingLot.init(helper, parking);
        
        // Simulate concurrent parking
        int successCount = 0;
        int failureCount = 0;
        
        for (int i = 0; i < 10; i++) {
            ParkingResult result = parkingLot.park(4, "CAR" + i, "TKT" + i);
            if (result != null && result.getStatus() == 201) {
                successCount++;
            } else {
                failureCount++;
            }
        }
        
        System.out.println("   Concurrent parking results:");
        System.out.println("   - Successful parks: " + successCount + " (expected: 5)");
        System.out.println("   - Failed parks: " + failureCount + " (expected: 5)");
        
        if (successCount == 5 && failureCount == 5) {
            System.out.println("   ✅ Concurrent operations handled correctly");
        } else {
            System.out.println("   ❌ Concurrent operations not handled correctly");
        }
        System.out.println();
    }
    
    private String[][][] createTestParkingLot() {
        // Create a 2-floor parking lot with 4x4 spots each
        String[][][] parking = new String[2][4][4];
        
        // Floor 0
        parking[0][0] = new String[]{"4-1", "4-1", "2-1", "2-0"};
        parking[0][1] = new String[]{"2-1", "4-1", "2-1", "2-1"};
        parking[0][2] = new String[]{"4-0", "2-1", "4-0", "2-1"};
        parking[0][3] = new String[]{"4-1", "4-1", "4-1", "2-1"};
        
        // Floor 1
        parking[1][0] = new String[]{"2-1", "4-1", "2-1", "4-1"};
        parking[1][1] = new String[]{"4-1", "2-0", "4-1", "2-1"};
        parking[1][2] = new String[]{"2-1", "4-1", "2-1", "4-0"};
        parking[1][3] = new String[]{"4-1", "2-1", "4-1", "2-1"};
        
        return parking;
    }
} 