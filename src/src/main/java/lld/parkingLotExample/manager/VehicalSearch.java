package lld.parkingLotExample.manager;


import lld.parkingLotExample.model.ParkingResult;

import java.util.concurrent.ConcurrentHashMap;

public class VehicalSearch {
    
    ConcurrentHashMap<String, ParkingResult> parkingResults;
    public VehicalSearch() {
        parkingResults = new ConcurrentHashMap<>();
    }
    public void index(ParkingResult parkingResult) {
        parkingResults.put(parkingResult.getSpotId(), parkingResult);
        parkingResults.put(parkingResult.getTicketId(), parkingResult);
        parkingResults.put(parkingResult.getVehicleNumber(), parkingResult);
    }
    public ParkingResult searchParking(String id, String ticketId, String vehicleNumber) {
        return parkingResults.getOrDefault(id, null);   
    }
}

