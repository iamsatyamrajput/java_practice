package lld.parkingLotExample.model;

public class ParkingSpot {
    private final int floor;
    private final int row;
    private final int col;
    private final int vehicleType;
    private final String spotId;
    private boolean isOccupied;
    private final boolean isActive;

    public ParkingSpot(int floor, int row, int col, int vehicleType, String spotId, boolean isActive) {
        this.floor = floor;
        this.row = row;
        this.col = col;
        this.vehicleType = vehicleType;
        this.spotId = spotId;
        this.isActive = isActive;
        this.isOccupied = false;
    }

    public int getFloor() {
        return floor;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getVehicleType() {
        return vehicleType;
    }

    public String getSpotId() {
        return spotId;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setOccupied(boolean occupied) {
        this.isOccupied = occupied;
    }
}
