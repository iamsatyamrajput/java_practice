package lld.parkingLotExample.manager;

import lld.parkingLotExample.model.ParkingResult;
import lld.parkingLotExample.model.Vehicle;
import lld.parkingLotExample.model.ParkingSpot;
import lld.parkingLotExample.util.Helper01;

public class ParkingLotManager {
    private Helper01 helper;
    private String[][][] parkingLot;
    private VehicleManager vehicleManager;
    private SpotManager spotManager;

    public ParkingLotManager() {
        this.vehicleManager = new VehicleManager();
        this.spotManager = new SpotManager();
    }

    public void init(Helper01 helper, String[][][] parking) {
        this.helper = helper;
        this.parkingLot = parking;
        this.spotManager.initializeSpots(parking);
    }

    public ParkingResult park(int vehicleType, String vehicleNumber, String ticketId) {
        // Validate request
        if (!isValidParkingRequest(vehicleType, vehicleNumber, ticketId)) {
            return new ParkingResult(400, null, vehicleNumber, ticketId);
        }

        // Check if vehicle is already parked
        if (vehicleManager.isVehicleAlreadyParked(vehicleNumber, ticketId)) {
            return new ParkingResult(409, null, vehicleNumber, ticketId);
        }

        // Find available spot
        ParkingSpot spot = spotManager.findAvailableSpot(vehicleType);
        if (spot == null) {
            return new ParkingResult(404, null, vehicleNumber, ticketId);
        }

        // Create vehicle and park
        Vehicle vehicle = new Vehicle(vehicleNumber, ticketId, vehicleType);
        vehicle.setSpotId(spot.getSpotId());
        
        // Add to vehicle manager and occupy spot
        vehicleManager.addVehicle(vehicle, spot.getSpotId());
        spotManager.occupySpot(spot);

        // Log the parking
        helper.println("Vehicle " + vehicleNumber + " parked at spot " + spot.getSpotId());

        return new ParkingResult(201, spot.getSpotId(), vehicleNumber, ticketId);
    }

    public int removeVehicle(String spotId, String vehicleNumber, String ticketId) {
        // Find the vehicle
        Vehicle vehicle = vehicleManager.findVehicle(spotId, vehicleNumber, ticketId);
        if (vehicle == null) {
            return 404;
        }

        // Get the spot
        ParkingSpot spot = spotManager.getSpotById(vehicle.getSpotId());
        if (spot == null) {
            return 404;
        }

        // Remove from vehicle manager and free spot
        vehicleManager.removeVehicle(vehicle);
        spotManager.freeSpot(spot);

        // Log the removal
        helper.println("Vehicle " + vehicle.getVehicleNumber() + " removed from spot " + spot.getSpotId());

        return 201;
    }

    public ParkingResult searchVehicle(String spotId, String vehicleNumber, String ticketId) {
        Vehicle vehicle = vehicleManager.findVehicle(spotId, vehicleNumber, ticketId);
        if (vehicle == null) {
            return new ParkingResult(404, null, vehicleNumber, ticketId);
        }
        return new ParkingResult(200, vehicle.getSpotId(), vehicle.getVehicleNumber(), vehicle.getTicketId());
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