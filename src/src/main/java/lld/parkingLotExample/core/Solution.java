package lld.parkingLotExample.core;

import lld.parkingLotExample.model.ParkingResult;
import lld.parkingLotExample.model.Vehicle;
import lld.parkingLotExample.model.ParkingSpot;
import lld.parkingLotExample.manager.VehicleManager;
import lld.parkingLotExample.manager.SpotManager;
import lld.parkingLotExample.manager.ParkingLotManager;
import lld.parkingLotExample.util.Helper01;
import lld.parkingLotExample.Q001ParkingLotInterface;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class Solution implements Q001ParkingLotInterface {
    
    private ParkingLotManager parkingLotManager;
    
    public Solution() {
        this.parkingLotManager = new ParkingLotManager();
    }
    
    @Override
    public void init(Helper01 helper, String[][][] parking) {
        parkingLotManager.init(helper, parking);
    }
    
    @Override
    public ParkingResult park(int vehicleType, String vehicleNumber, String ticketId) {
        return parkingLotManager.park(vehicleType, vehicleNumber, ticketId);
    }
    
    @Override
    public int removeVehicle(String spotId, String vehicleNumber, String ticketId) {
        return parkingLotManager.removeVehicle(spotId, vehicleNumber, ticketId);
    }
    
    @Override
    public ParkingResult searchVehicle(String spotId, String vehicleNumber, String ticketId) {
        return parkingLotManager.searchVehicle(spotId, vehicleNumber, ticketId);
    }
    
    @Override
    public int getFreeSpotsCount(int floor, int vehicleType) {
        return parkingLotManager.getFreeSpotsCount(floor, vehicleType);
    }
    
    // --- ParkingLotManager: Core business logic ---
    private static class ParkingLotManager {
        private Helper01 helper;
        private String[][][] parkingLot;
        private VehicleManager vehicleManager;
        private SpotManager spotManager;
        
        public void init(Helper01 helper, String[][][] parking) {
            this.helper = helper;
            this.parkingLot = parking;
            this.vehicleManager = new VehicleManager();
            this.spotManager = new SpotManager();
            spotManager.initializeSpots(parking);
            if (helper != null) helper.println("Parking lot initialized successfully");
        }
        
        public ParkingResult park(int vehicleType, String vehicleNumber, String ticketId) {
            if (!isValidParkingRequest(vehicleType, vehicleNumber, ticketId))
                return new ParkingResult(404, "", vehicleNumber, ticketId);
            if (vehicleManager.isVehicleAlreadyParked(vehicleNumber, ticketId))
                return new ParkingResult(404, "", vehicleNumber, ticketId);
            ParkingSpot spot = spotManager.findAvailableSpot(vehicleType);
            if (spot == null)
                return new ParkingResult(404, "", vehicleNumber, ticketId);
            Vehicle vehicle = new Vehicle(vehicleNumber, ticketId, vehicleType);
            vehicle.setSpotId(spot.getSpotId());
            vehicleManager.addVehicle(vehicle, spot.getSpotId());
            spotManager.occupySpot(spot);
            if (helper != null) helper.println("Vehicle " + vehicleNumber + " parked at " + spot.getSpotId());
            return new ParkingResult(201, spot.getSpotId(), vehicleNumber, ticketId);
        }
        
        public int removeVehicle(String spotId, String vehicleNumber, String ticketId) {
            Vehicle vehicle = vehicleManager.findVehicle(spotId, vehicleNumber, ticketId);
            if (vehicle == null) return 404;
            ParkingSpot spot = spotManager.getSpotById(vehicle.getSpotId());
            if (spot == null) return 404;
            vehicleManager.removeVehicle(vehicle);
            spotManager.freeSpot(spot);
            if (helper != null) helper.println("Vehicle " + vehicle.getVehicleNumber() + " removed from " + spot.getSpotId());
            return 201;
        }
        
        public ParkingResult searchVehicle(String spotId, String vehicleNumber, String ticketId) {
            Vehicle vehicle = vehicleManager.findVehicle(spotId, vehicleNumber, ticketId);
            if (vehicle == null)
                return new ParkingResult(404, "", vehicleNumber != null ? vehicleNumber : "", ticketId != null ? ticketId : "");
            return new ParkingResult(201, vehicle.getSpotId(), vehicle.getVehicleNumber(), vehicle.getTicketId());
        }
        
        public int getFreeSpotsCount(int floor, int vehicleType) {
            return spotManager.getFreeSpotsCount(floor, vehicleType);
        }
        
        private boolean isValidParkingRequest(int vehicleType, String vehicleNumber, String ticketId) {
            return (vehicleType == 2 || vehicleType == 4) &&
                   vehicleNumber != null && !vehicleNumber.trim().isEmpty() &&
                   ticketId != null && !ticketId.trim().isEmpty();
        }
    }
    
    // --- VehicleManager ---
    private static class VehicleManager {
        private final Map<String, Vehicle> spotToVehicle = new ConcurrentHashMap<>();
        private final Map<String, String> vehicleToSpot = new ConcurrentHashMap<>();
        private final Map<String, String> ticketToSpot = new ConcurrentHashMap<>();
        private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        
        public void addVehicle(Vehicle vehicle, String spotId) {
            lock.writeLock().lock();
            try {
                vehicle.setSpotId(spotId);
                spotToVehicle.put(spotId, vehicle);
                vehicleToSpot.put(vehicle.getVehicleNumber(), spotId);
                ticketToSpot.put(vehicle.getTicketId(), spotId);
            } finally { lock.writeLock().unlock(); }
        }
        
        public void removeVehicle(Vehicle vehicle) {
            lock.writeLock().lock();
            try {
                spotToVehicle.remove(vehicle.getSpotId());
                vehicleToSpot.remove(vehicle.getVehicleNumber());
                ticketToSpot.remove(vehicle.getTicketId());
            } finally { lock.writeLock().unlock(); }
        }
        
        public Vehicle findVehicle(String spotId, String vehicleNumber, String ticketId) {
            lock.readLock().lock();
            try {
                if (spotId != null && !spotId.trim().isEmpty()) return spotToVehicle.get(spotId);
                else if (vehicleNumber != null && !vehicleNumber.trim().isEmpty()) {
                    String foundSpotId = vehicleToSpot.get(vehicleNumber);
                    return foundSpotId != null ? spotToVehicle.get(foundSpotId) : null;
                } else if (ticketId != null && !ticketId.trim().isEmpty()) {
                    String foundSpotId = ticketToSpot.get(ticketId);
                    return foundSpotId != null ? spotToVehicle.get(foundSpotId) : null;
                }
                return null;
            } finally { lock.readLock().unlock(); }
        }
        
        public boolean isVehicleAlreadyParked(String vehicleNumber, String ticketId) {
            lock.readLock().lock();
            try {
                return vehicleToSpot.containsKey(vehicleNumber) || ticketToSpot.containsKey(ticketId);
            } finally { lock.readLock().unlock(); }
        }
    }
    
    // --- SpotManager ---
    private static class SpotManager {
        private final List<ParkingSpot> allSpots = new ArrayList<>();
        private final AtomicInteger[][] freeSpotsCount = new AtomicInteger[5][2];
        private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        
        public SpotManager() {
            for (int floor = 0; floor < 5; floor++) {
                freeSpotsCount[floor][0] = new AtomicInteger(0);
                freeSpotsCount[floor][1] = new AtomicInteger(0);
            }
        }
        
        public void initializeSpots(String[][][] parking) {
            lock.writeLock().lock();
            try {
                for (int floor = 0; floor < parking.length; floor++) {
                    for (int row = 0; row < parking[floor].length; row++) {
                        for (int col = 0; col < parking[floor][row].length; col++) {
                            String spotConfig = parking[floor][row][col];
                            if (spotConfig != null) {
                                ParkingSpot spot = createSpot(floor, row, col, spotConfig);
                                allSpots.add(spot);
                                if (spot.isActive()) {
                                    int vehicleTypeIndex = (spot.getVehicleType() == 2) ? 0 : 1;
                                    freeSpotsCount[floor][vehicleTypeIndex].incrementAndGet();
                                }
                            }
                        }
                    }
                }
            } finally { lock.writeLock().unlock(); }
        }
        
        private ParkingSpot createSpot(int floor, int row, int col, String spotConfig) {
            boolean isActive = spotConfig.endsWith("-1");
            int vehicleType = spotConfig.startsWith("2-") ? 2 : 4;
            String spotId = floor + "-" + row + "-" + col;
            return new ParkingSpot(floor, row, col, vehicleType, spotId, isActive);
        }
        
        public ParkingSpot findAvailableSpot(int vehicleType) {
            lock.readLock().lock();
            try {
                for (ParkingSpot spot : allSpots) {
                    if (spot.isActive() && !spot.isOccupied() && spot.getVehicleType() == vehicleType) {
                        return spot;
                    }
                }
                return null;
            } finally { lock.readLock().unlock(); }
        }
        
        public void occupySpot(ParkingSpot spot) {
            lock.writeLock().lock();
            try {
                spot.setOccupied(true);
                int vehicleTypeIndex = (spot.getVehicleType() == 2) ? 0 : 1;
                freeSpotsCount[spot.getFloor()][vehicleTypeIndex].decrementAndGet();
            } finally { lock.writeLock().unlock(); }
        }
        
        public void freeSpot(ParkingSpot spot) {
            lock.writeLock().lock();
            try {
                spot.setOccupied(false);
                int vehicleTypeIndex = (spot.getVehicleType() == 2) ? 0 : 1;
                freeSpotsCount[spot.getFloor()][vehicleTypeIndex].incrementAndGet();
            } finally { lock.writeLock().unlock(); }
        }
        
        public ParkingSpot getSpotById(String spotId) {
            lock.readLock().lock();
            try {
                for (ParkingSpot spot : allSpots) {
                    if (spot.getSpotId().equals(spotId)) {
                        return spot;
                    }
                }
                return null;
            } finally { lock.readLock().unlock(); }
        }
        
        public int getFreeSpotsCount(int floor, int vehicleType) {
            if (floor < 0 || floor >= 5 || (vehicleType != 2 && vehicleType != 4)) return 0;
            lock.readLock().lock();
            try {
                int vehicleTypeIndex = (vehicleType == 2) ? 0 : 1;
                return freeSpotsCount[floor][vehicleTypeIndex].get();
            } finally { lock.readLock().unlock(); }
        }
    }
    
    // --- Vehicle ---
    private static class Vehicle {
        private final String vehicleNumber;
        private final String ticketId;
        private final int vehicleType;
        private String spotId;
        
        public Vehicle(String vehicleNumber, String ticketId, int vehicleType) {
            this.vehicleNumber = vehicleNumber;
            this.ticketId = ticketId;
            this.vehicleType = vehicleType;
        }
        
        public String getVehicleNumber() { return vehicleNumber; }
        public String getTicketId() { return ticketId; }
        public int getVehicleType() { return vehicleType; }
        public String getSpotId() { return spotId; }
        public void setSpotId(String spotId) { this.spotId = spotId; }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Vehicle vehicle = (Vehicle) obj;
            return vehicleNumber.equals(vehicle.vehicleNumber) || 
                   ticketId.equals(vehicle.ticketId);
        }
        
        @Override
        public int hashCode() {
            return vehicleNumber.hashCode() + ticketId.hashCode();
        }
    }
    
    // --- ParkingSpot ---
    private static class ParkingSpot {
        private final int floor, row, col, vehicleType;
        private final String spotId;
        private boolean isOccupied, isActive;
        
        public ParkingSpot(int floor, int row, int col, int vehicleType, String spotId, boolean isActive) {
            this.floor = floor; this.row = row; this.col = col; this.vehicleType = vehicleType;
            this.spotId = spotId; this.isActive = isActive; this.isOccupied = false;
        }
        
        public int getFloor() { return floor; }
        public int getRow() { return row; }
        public int getCol() { return col; }
        public int getVehicleType() { return vehicleType; }
        public String getSpotId() { return spotId; }
        public boolean isOccupied() { return isOccupied; }
        public boolean isActive() { return isActive; }
        public void setOccupied(boolean occupied) { isOccupied = occupied; }
    }
}
