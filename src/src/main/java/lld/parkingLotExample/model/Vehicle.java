package lld.parkingLotExample.model;

import java.util.Objects;

public class Vehicle {
    private final String vehicleNumber;
    private final String ticketId;
    private final int vehicleType;
    private String spotId;

    public Vehicle(String vehicleNumber, String ticketId, int vehicleType) {
        this.vehicleNumber = vehicleNumber;
        this.ticketId = ticketId;
        this.vehicleType = vehicleType;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public String getTicketId() {
        return ticketId;
    }

    public int getVehicleType() {
        return vehicleType;
    }

    public String getSpotId() {
        return spotId;
    }

    public void setSpotId(String spotId) {
        this.spotId = spotId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vehicle vehicle = (Vehicle) obj;
        return vehicleType == vehicle.vehicleType &&
                Objects.equals(vehicleNumber, vehicle.vehicleNumber) &&
                Objects.equals(ticketId, vehicle.ticketId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vehicleNumber, ticketId, vehicleType);
    }
} 