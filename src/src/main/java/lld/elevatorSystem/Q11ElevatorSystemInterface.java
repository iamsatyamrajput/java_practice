package lld.elevatorSystem;

public interface Q11ElevatorSystemInterface {
    /**
     * Initialize/reinitialize the elevator system and reset all instance variables.
     * @param floors Number of floors in the building (0 to floors-1)
     * @param lifts Number of lifts (0 to lifts-1)
     * @param liftsCapacity Maximum number of people a lift can carry at any time
     * @param helper Helper object for printing logs
     */
    void init(int floors, int lifts, int liftsCapacity, Helper11 helper);
    
    /**
     * Request a lift to come to a specific floor in a specific direction.
     * @param startFloor The floor where the user is requesting the lift
     * @param direction 'U' for up, 'D' for down
     * @return Lift index (0 to lifts-1) or -1 if no lift can be assigned
     */
    int requestLift(int startFloor, char direction);
    
    /**
     * Press destination floor button inside a lift.
     * @param liftIndex Index of the lift (0 to lifts-1)
     * @param floor Destination floor
     */
    void pressFloorButtonInLift(int liftIndex, int floor);
    
    /**
     * Get the current state of a lift.
     * @param liftIndex Index of the lift (0 to lifts-1)
     * @return String in format "currentFloor-direction-peopleCount"
     *         direction: U for up, D for down, I for idle
     *         Example: "4-U-10", "12-D-2", "0-I-0"
     */
    String getLiftState(int liftIndex);
    
    /**
     * Called every second to update lift states and simulate time passage.
     */
    void tick();
} 