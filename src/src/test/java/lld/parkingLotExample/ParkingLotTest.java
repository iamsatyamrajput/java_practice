package lld.parkingLotExample;

import lld.parkingLotExample.Solution.Solution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ParkingLotTest {
    
    private Solution parkingLot;
    private Helper01 helper;
    private String[][][] parking;
    
    @BeforeEach
    void setUp() {
        parkingLot = new Solution();
        helper = new Helper01();
        
        // Initialize a 2-floor parking lot with 4x4 spots each
        parking = new String[2][4][4];
        
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
        
        parkingLot.init(helper, parking);
    }
    
    @Test
    @DisplayName("Test initial free spots count")
    void testInitialFreeSpotsCount() {
        // Floor 0: 5 active 4-wheeler spots, 8 active 2-wheeler spots
        assertEquals(5, parkingLot.getFreeSpotsCount(0, 4));
        assertEquals(8, parkingLot.getFreeSpotsCount(0, 2));
        
        // Floor 1: 7 active 4-wheeler spots, 7 active 2-wheeler spots
        assertEquals(7, parkingLot.getFreeSpotsCount(1, 4));
        assertEquals(7, parkingLot.getFreeSpotsCount(1, 2));
    }
    
    @Test
    @DisplayName("Test parking a 4-wheeler vehicle")
    void testPark4Wheeler() {
        ParkingResult result = parkingLot.park(4, "CAR001", "TKT001");
        
        assertNotNull(result);
        assertEquals(201, result.getStatus());
        assertEquals("CAR001", result.getVehicleNumber());
        assertEquals("TKT001", result.getTicketId());
        assertNotNull(result.getSpotId());
        
        // Verify spot is occupied
        assertEquals(4, parkingLot.getFreeSpotsCount(0, 4));
    }
    
    @Test
    @DisplayName("Test parking a 2-wheeler vehicle")
    void testPark2Wheeler() {
        ParkingResult result = parkingLot.park(2, "BIKE001", "TKT002");
        
        assertNotNull(result);
        assertEquals(201, result.getStatus());
        assertEquals("BIKE001", result.getVehicleNumber());
        assertEquals("TKT002", result.getTicketId());
        assertNotNull(result.getSpotId());
        
        // Verify spot is occupied
        assertEquals(7, parkingLot.getFreeSpotsCount(0, 2));
    }
    
    @Test
    @DisplayName("Test parking when no spots available")
    void testParkWhenNoSpotsAvailable() {
        // Fill all 4-wheeler spots on floor 0
        for (int i = 0; i < 5; i++) {
            parkingLot.park(4, "CAR" + i, "TKT" + i);
        }
        
        // Try to park another 4-wheeler
        ParkingResult result = parkingLot.park(4, "CAR999", "TKT999");
        
        assertNotNull(result);
        assertEquals(404, result.getStatus());
        assertEquals(0, parkingLot.getFreeSpotsCount(0, 4));
    }
    
    @Test
    @DisplayName("Test removing vehicle by spotId")
    void testRemoveVehicleBySpotId() {
        // Park a vehicle
        ParkingResult parkResult = parkingLot.park(4, "CAR001", "TKT001");
        String spotId = parkResult.getSpotId();
        
        // Remove by spotId
        int removeResult = parkingLot.removeVehicle(spotId, "", "");
        assertEquals(201, removeResult);
        
        // Verify spot is free again
        assertEquals(5, parkingLot.getFreeSpotsCount(0, 4));
    }
    
    @Test
    @DisplayName("Test removing vehicle by vehicleNumber")
    void testRemoveVehicleByVehicleNumber() {
        // Park a vehicle
        parkingLot.park(4, "CAR001", "TKT001");
        
        // Remove by vehicleNumber
        int removeResult = parkingLot.removeVehicle("", "CAR001", "");
        assertEquals(201, removeResult);
        
        // Verify spot is free again
        assertEquals(5, parkingLot.getFreeSpotsCount(0, 4));
    }
    
    @Test
    @DisplayName("Test removing vehicle by ticketId")
    void testRemoveVehicleByTicketId() {
        // Park a vehicle
        parkingLot.park(4, "CAR001", "TKT001");
        
        // Remove by ticketId
        int removeResult = parkingLot.removeVehicle("", "", "TKT001");
        assertEquals(201, removeResult);
        
        // Verify spot is free again
        assertEquals(5, parkingLot.getFreeSpotsCount(0, 4));
    }
    
    @Test
    @DisplayName("Test removing non-existent vehicle")
    void testRemoveNonExistentVehicle() {
        int removeResult = parkingLot.removeVehicle("0-0-0", "", "");
        assertEquals(404, removeResult);
    }
    
    @Test
    @DisplayName("Test searching vehicle by spotId")
    void testSearchVehicleBySpotId() {
        // Park a vehicle
        ParkingResult parkResult = parkingLot.park(4, "CAR001", "TKT001");
        String spotId = parkResult.getSpotId();
        
        // Search by spotId
        ParkingResult searchResult = parkingLot.searchVehicle(spotId, "", "");
        
        assertNotNull(searchResult);
        assertEquals(201, searchResult.getStatus());
        assertEquals(spotId, searchResult.getSpotId());
        assertEquals("CAR001", searchResult.getVehicleNumber());
        assertEquals("TKT001", searchResult.getTicketId());
    }
    
    @Test
    @DisplayName("Test searching vehicle by vehicleNumber")
    void testSearchVehicleByVehicleNumber() {
        // Park a vehicle
        parkingLot.park(4, "CAR001", "TKT001");
        
        // Search by vehicleNumber
        ParkingResult searchResult = parkingLot.searchVehicle("", "CAR001", "");
        
        assertNotNull(searchResult);
        assertEquals(201, searchResult.getStatus());
        assertEquals("CAR001", searchResult.getVehicleNumber());
        assertEquals("TKT001", searchResult.getTicketId());
    }
    
    @Test
    @DisplayName("Test searching vehicle by ticketId")
    void testSearchVehicleByTicketId() {
        // Park a vehicle
        parkingLot.park(4, "CAR001", "TKT001");
        
        // Search by ticketId
        ParkingResult searchResult = parkingLot.searchVehicle("", "", "TKT001");
        
        assertNotNull(searchResult);
        assertEquals(201, searchResult.getStatus());
        assertEquals("CAR001", searchResult.getVehicleNumber());
        assertEquals("TKT001", searchResult.getTicketId());
    }
    
    @Test
    @DisplayName("Test searching non-existent vehicle")
    void testSearchNonExistentVehicle() {
        ParkingResult searchResult = parkingLot.searchVehicle("0-0-0", "", "");
        assertEquals(404, searchResult.getStatus());
    }
    
    @Test
    @DisplayName("Test search after vehicle removal")
    void testSearchAfterVehicleRemoval() {
        // Park a vehicle
        ParkingResult parkResult = parkingLot.park(4, "CAR001", "TKT001");
        String spotId = parkResult.getSpotId();
        
        // Remove the vehicle
        parkingLot.removeVehicle(spotId, "", "");
        
        // Search should still return the last known spotId
        ParkingResult searchResult = parkingLot.searchVehicle("", "CAR001", "");
        assertNotNull(searchResult);
        assertEquals(spotId, searchResult.getSpotId());
    }
    
    @Test
    @DisplayName("Test multiple floors")
    void testMultipleFloors() {
        // Park on floor 0
        ParkingResult result1 = parkingLot.park(4, "CAR001", "TKT001");
        assertEquals(201, result1.getStatus());
        
        // Park on floor 1
        ParkingResult result2 = parkingLot.park(4, "CAR002", "TKT002");
        assertEquals(201, result2.getStatus());
        
        // Verify free spots on both floors
        assertEquals(4, parkingLot.getFreeSpotsCount(0, 4));
        assertEquals(6, parkingLot.getFreeSpotsCount(1, 4));
    }
    
    @Test
    @DisplayName("Test concurrent parking operations")
    void testConcurrentParking() throws InterruptedException {
        int numThreads = 10;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);
        
        for (int i = 0; i < numThreads; i++) {
            final int index = i;
            executor.submit(() -> {
                try {
                    ParkingResult result = parkingLot.park(4, "CAR" + index, "TKT" + index);
                    if (result.getStatus() == 201) {
                        successCount.incrementAndGet();
                    } else {
                        failureCount.incrementAndGet();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await();
        executor.shutdown();
        
        // Should have 5 successful parks (available spots) and 5 failures
        assertEquals(5, successCount.get());
        assertEquals(5, failureCount.get());
    }
    
    @Test
    @DisplayName("Test edge case with invalid vehicle type")
    void testInvalidVehicleType() {
        ParkingResult result = parkingLot.park(3, "CAR001", "TKT001");
        assertEquals(404, result.getStatus());
    }
    
    @Test
    @DisplayName("Test edge case with empty vehicle number")
    void testEmptyVehicleNumber() {
        ParkingResult result = parkingLot.park(4, "", "TKT001");
        assertEquals(404, result.getStatus());
    }
    
    @Test
    @DisplayName("Test edge case with empty ticket ID")
    void testEmptyTicketId() {
        ParkingResult result = parkingLot.park(4, "CAR001", "");
        assertEquals(404, result.getStatus());
    }
    
    @Test
    @DisplayName("Test parking lot with all inactive spots")
    void testAllInactiveSpots() {
        // Create parking lot with all inactive spots
        String[][][] inactiveParking = new String[1][2][2];
        inactiveParking[0][0] = new String[]{"4-0", "2-0"};
        inactiveParking[0][1] = new String[]{"4-0", "2-0"};
        
        Solution inactiveParkingLot = new Solution();
        inactiveParkingLot.init(helper, inactiveParking);
        
        // Try to park
        ParkingResult result = inactiveParkingLot.park(4, "CAR001", "TKT001");
        assertEquals(404, result.getStatus());
        assertEquals(0, inactiveParkingLot.getFreeSpotsCount(0, 4));
        assertEquals(0, inactiveParkingLot.getFreeSpotsCount(0, 2));
    }
    
    @Test
    @DisplayName("Test large parking lot")
    void testLargeParkingLot() {
        // Create a larger parking lot (3 floors, 5x5 each)
        String[][][] largeParking = new String[3][5][5];
        
        // Fill with active spots
        for (int floor = 0; floor < 3; floor++) {
            for (int row = 0; row < 5; row++) {
                for (int col = 0; col < 5; col++) {
                    largeParking[floor][row][col] = (col % 2 == 0) ? "4-1" : "2-1";
                }
            }
        }
        
        Solution largeParkingLot = new Solution();
        largeParkingLot.init(helper, largeParking);
        
        // Test parking multiple vehicles
        for (int i = 0; i < 10; i++) {
            ParkingResult result = largeParkingLot.park(4, "CAR" + i, "TKT" + i);
            assertEquals(201, result.getStatus());
        }
        
        // Verify free spots count
        assertEquals(65, largeParkingLot.getFreeSpotsCount(0, 4)); // 75 total - 10 parked
    }
} 