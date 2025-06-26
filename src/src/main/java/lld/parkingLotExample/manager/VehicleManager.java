package lld.parkingLotExample.manager;

import lld.parkingLotExample.model.Vehicle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class VehicleManager {
    private final ConcurrentHashMap<String, Vehicle> spotToVehicle;
    private final ConcurrentHashMap<String, String> vehicleToSpot;
    private final ConcurrentHashMap<String, String> ticketToSpot;
    private final ReentrantReadWriteLock lock;

    public VehicleManager() {
        this.spotToVehicle = new ConcurrentHashMap<>();
        this.vehicleToSpot = new ConcurrentHashMap<>();
        this.ticketToSpot = new ConcurrentHashMap<>();
        this.lock = new ReentrantReadWriteLock();
    }

    public void addVehicle(Vehicle vehicle, String spotId) {
        lock.writeLock().lock();
        try {
            vehicle.setSpotId(spotId);
            spotToVehicle.put(spotId, vehicle);
            vehicleToSpot.put(vehicle.getVehicleNumber(), spotId);
            ticketToSpot.put(vehicle.getTicketId(), spotId);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void removeVehicle(Vehicle vehicle) {
        lock.writeLock().lock();
        try {
            String spotId = vehicle.getSpotId();
            if (spotId != null) {
                spotToVehicle.remove(spotId);
                vehicleToSpot.remove(vehicle.getVehicleNumber());
                ticketToSpot.remove(vehicle.getTicketId());
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Vehicle findVehicle(String spotId, String vehicleNumber, String ticketId) {
        lock.readLock().lock();
        try {
            // Search by spotId
            if (spotId != null && !spotId.trim().isEmpty()) {
                Vehicle vehicle = spotToVehicle.get(spotId);
                if (vehicle != null && 
                    (vehicleNumber == null || vehicle.getVehicleNumber().equals(vehicleNumber)) &&
                    (ticketId == null || vehicle.getTicketId().equals(ticketId))) {
                    return vehicle;
                }
            }
            
            // Search by vehicleNumber
            if (vehicleNumber != null && !vehicleNumber.trim().isEmpty()) {
                String foundSpotId = vehicleToSpot.get(vehicleNumber);
                if (foundSpotId != null) {
                    Vehicle vehicle = spotToVehicle.get(foundSpotId);
                    if (vehicle != null && 
                        (ticketId == null || vehicle.getTicketId().equals(ticketId))) {
                        return vehicle;
                    }
                }
            }
            
            // Search by ticketId
            if (ticketId != null && !ticketId.trim().isEmpty()) {
                String foundSpotId = ticketToSpot.get(ticketId);
                if (foundSpotId != null) {
                    Vehicle vehicle = spotToVehicle.get(foundSpotId);
                    if (vehicle != null && 
                        (vehicleNumber == null || vehicle.getVehicleNumber().equals(vehicleNumber))) {
                        return vehicle;
                    }
                }
            }
            
            return null;
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean isVehicleAlreadyParked(String vehicleNumber, String ticketId) {
        lock.readLock().lock();
        try {
            return vehicleToSpot.containsKey(vehicleNumber) || ticketToSpot.containsKey(ticketId);
        } finally {
            lock.readLock().unlock();
        }
    }
} 