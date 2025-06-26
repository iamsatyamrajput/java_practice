package lld.parkingLotExample.manager;

import lld.parkingLotExample.model.ParkingSpot;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SpotManager {
    private List<ParkingSpot> allSpots;
    private AtomicInteger[][] freeSpotsCount;
    private final ReentrantReadWriteLock lock;

    public SpotManager() {
        this.allSpots = new ArrayList<>();
        this.lock = new ReentrantReadWriteLock();
    }

    public void initializeSpots(String[][][] parking) {
        lock.writeLock().lock();
        try {
            allSpots.clear();
            
            // Initialize free spots count array
            int floors = parking.length;
            int maxVehicleTypes = 2; // 2-wheeler and 4-wheeler
            freeSpotsCount = new AtomicInteger[floors][maxVehicleTypes];
            
            for (int floor = 0; floor < floors; floor++) {
                for (int vehicleType = 0; vehicleType < maxVehicleTypes; vehicleType++) {
                    freeSpotsCount[floor][vehicleType] = new AtomicInteger(0);
                }
            }
            
            // Create spots from parking configuration
            for (int floor = 0; floor < parking.length; floor++) {
                for (int row = 0; row < parking[floor].length; row++) {
                    for (int col = 0; col < parking[floor][row].length; col++) {
                        String spotConfig = parking[floor][row][col];
                        ParkingSpot spot = createSpot(floor, row, col, spotConfig);
                        if (spot != null && spot.isActive()) {
                            allSpots.add(spot);
                            if (!spot.isOccupied()) {
                                freeSpotsCount[floor][spot.getVehicleType() - 1].incrementAndGet();
                            }
                        }
                    }
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public ParkingSpot findAvailableSpot(int vehicleType) {
        lock.readLock().lock();
        try {
            for (ParkingSpot spot : allSpots) {
                if (spot.getVehicleType() == vehicleType && 
                    spot.isActive() && 
                    !spot.isOccupied()) {
                    return spot;
                }
            }
            return null;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void occupySpot(ParkingSpot spot) {
        lock.writeLock().lock();
        try {
            if (spot != null && !spot.isOccupied()) {
                spot.setOccupied(true);
                freeSpotsCount[spot.getFloor()][spot.getVehicleType() - 1].decrementAndGet();
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void freeSpot(ParkingSpot spot) {
        lock.writeLock().lock();
        try {
            if (spot != null && spot.isOccupied()) {
                spot.setOccupied(false);
                freeSpotsCount[spot.getFloor()][spot.getVehicleType() - 1].incrementAndGet();
            }
        } finally {
            lock.writeLock().unlock();
        }
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
        } finally {
            lock.readLock().unlock();
        }
    }

    public int getFreeSpotsCount(int floor, int vehicleType) {
        lock.readLock().lock();
        try {
            if (freeSpotsCount != null && 
                floor >= 0 && floor < freeSpotsCount.length &&
                vehicleType >= 1 && vehicleType <= freeSpotsCount[0].length) {
                return freeSpotsCount[floor][vehicleType - 1].get();
            }
            return 0;
        } finally {
            lock.readLock().unlock();
        }
    }

    private ParkingSpot createSpot(int floor, int row, int col, String spotConfig) {
        if (spotConfig == null || spotConfig.trim().isEmpty()) {
            return null;
        }
        
        String spotId = String.format("%d_%d_%d", floor, row, col);
        boolean isActive = !spotConfig.equals("0");
        boolean isOccupied = spotConfig.equals("1");
        
        int vehicleType;
        if (spotConfig.equals("2") || spotConfig.equals("1")) {
            vehicleType = 2; // 2-wheeler
        } else if (spotConfig.equals("4") || spotConfig.equals("3")) {
            vehicleType = 4; // 4-wheeler
        } else {
            return null; // Invalid spot configuration
        }
        
        return new ParkingSpot(floor, row, col, vehicleType, spotId, isActive);
    }
} 