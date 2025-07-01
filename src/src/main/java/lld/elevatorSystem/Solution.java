package lld.elevatorSystem;

import java.util.*;

public class Solution implements Q11ElevatorSystemInterface{
    LiftManager liftManager = new LiftManager();
    @Override
    public void init(int floors, int lifts, int liftsCapacity, Helper11 helper) {
        liftManager.init(floors, lifts, liftsCapacity, helper);
    }

    @Override
    public int requestLift(int startFloor, char direction) {
        return liftManager.requestLift(startFloor, direction);
    }

    @Override
    public void pressFloorButtonInLift(int liftIndex, int floor) {
        liftManager.pressFloorButtonInLift(liftIndex, floor);
    }

    @Override
    public String getLiftState(int liftIndex) {
        return liftManager.getLiftState(liftIndex);
    }

    @Override
    public void tick() {
        liftManager.tick();
    }
    
    
}
class LiftManager{
    private Lift [] liftList;
    public void init(int floors, int lifts, int liftsCapacity, Helper11 helper) {
        liftList = new Lift [lifts];
        for (int i = 0; i < lifts; i++) {
            liftList[i] = new Lift(floors,liftsCapacity);
        }
    }
    
    public int requestLift(int startFloor, char direction) {
        // Validate input parameters
        if (startFloor < 0 || startFloor >= liftList[0].getFloors()) {
            return -1; // Invalid floor
        }
        if (direction != 'U' && direction != 'D') {
            return -1; // Invalid direction
        }
        
        LiftRequest liftRequest = new LiftRequest(startFloor, direction);
        for (int i = 0; i < liftList.length; i++) {
            if(liftList[i].request(liftRequest)) {
                return i;
            }
        }
        return -1;
    }

    public void pressFloorButtonInLift(int liftIndex, int floor) {
        liftList[liftIndex].pressFloorButtonInLift(floor);
    }

    public String getLiftState(int liftIndex) {
        return liftList[liftIndex].getState().getStateString(liftList[liftIndex]);
    }

    public void tick() {
        for (int i = 0; i < liftList.length; i++) {
            liftList[i].tick();
        }
    }
}
class Lift{
    private LiftStateInterface liftState;
    private int floors;
    private int liftCapacity;
    
    Lift(int floors, int liftCapacity){
        this.floors = floors;
        this.liftCapacity = liftCapacity;
        this.liftState = new IdleState();
    }

    public boolean request(LiftRequest liftRequest) {
        if(liftState.canAcceptRequest(this, liftRequest)) {
            // Don't increment people count here - only when pressing floor button
            if (liftState instanceof LiftState) {
                // If the request is for the current floor, don't add it as pending
                if (liftRequest.startFloor != ((LiftState) liftState).getCurrentFloor()) {
                    ((LiftState) liftState).addPendingRequest(liftRequest);
                }
            } else if (liftState instanceof IdleState) {
                // For idle state, add the request floor as a destination
                IdleState idleState = (IdleState) liftState;
                idleState.addStop(this, liftRequest.startFloor);
            }
            return true;
        } else {
            return false;
        }
    }

    public void pressFloorButtonInLift(int floor) {
        // Only add stop and increment people count if not at capacity and not already requested
        if (liftState instanceof LiftState) {
            LiftState state = (LiftState) liftState;
            if (!state.getDestinationFloors().contains(floor) && state.getCurrentLiftCapacity() < liftCapacity) {
                state.setCurrentLiftCapacity(state.getCurrentLiftCapacity() + 1);
            }
            liftState.addStop(this, floor);
        }
    }
    
    public LiftStateInterface getState() {
        return liftState;
    }
    
    public void setState(LiftStateInterface newState) {
        this.liftState = newState;
    }

    public void tick() {
        liftState.tick(this);
    }
    
    public int getFloors() {
        return floors;
    }
    
    public int getLiftCapacity() {
        return liftCapacity;
    }
}
// State interface for the State Pattern
interface LiftStateInterface {
    void move(Lift lift);
    void addStop(Lift lift, int floor);
    void tick(Lift lift);
    String getStateString(Lift lift);
    boolean canAcceptRequest(Lift lift, LiftRequest request);
    char getDirection();
}

// Base state class with common functionality
abstract class LiftState implements LiftStateInterface {
    protected int currentFloor;
    protected int currentLiftCapacity;
    protected char direction;
    protected Set<Integer> destinationFloors;
    protected Queue<LiftRequest> pendingRequests;
    
    public LiftState() {
        this.currentFloor = 0;
        this.currentLiftCapacity = 0;
        this.destinationFloors = new HashSet<>();
        this.pendingRequests = new LinkedList<>();
    }
    
    public int getCurrentFloor() {
        return currentFloor;
    }
    
    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }
    
    public int getCurrentLiftCapacity() {
        return currentLiftCapacity;
    }
    
    public void setCurrentLiftCapacity(int currentLiftCapacity) {
        this.currentLiftCapacity = currentLiftCapacity;
    }
    
    public char getDirection() {
        return direction;
    }
    
    public Set<Integer> getDestinationFloors() {
        return destinationFloors;
    }
    
    public Queue<LiftRequest> getPendingRequests() {
        return pendingRequests;
    }
    
    public void addDestinationFloor(int floor) {
        destinationFloors.add(floor);
    }
    
    public void removeDestinationFloor(int floor) {
        destinationFloors.remove(floor);
    }
    
    public void addPendingRequest(LiftRequest request) {
        pendingRequests.offer(request);
    }
    
    public boolean hasPendingRequests() {
        return !pendingRequests.isEmpty();
    }
    
    public boolean hasDestinationFloors() {
        return !destinationFloors.isEmpty();
    }
}

// Idle State - Lift is not moving
class IdleState extends LiftState {
    public IdleState() {
        super();
        this.direction = 'I';
    }
    
    @Override
    public void move(Lift lift) {
        // Idle lift doesn't move
    }
    
    @Override
    public void addStop(Lift lift, int floor) {
        if (floor != currentFloor) {
            addDestinationFloor(floor);
            if (floor > currentFloor) {
                UpState upState = new UpState(currentFloor, currentLiftCapacity, destinationFloors, pendingRequests);
                lift.setState(upState);
            } else {
                DownState downState = new DownState(currentFloor, currentLiftCapacity, destinationFloors, pendingRequests);
                lift.setState(downState);
            }
        } else {
            // If the floor is the same as current floor, clear any pending requests for this floor
            pendingRequests.removeIf(request -> request.startFloor == currentFloor);
        }
    }
    
    @Override
    public void tick(Lift lift) {
        // Check if there are any pending requests or destinations
        if (hasDestinationFloors() || hasPendingRequests()) {
            // Process next request or destination
            if (hasPendingRequests()) {
                LiftRequest request = pendingRequests.poll();
                addStop(lift, request.startFloor);
            }
        }
    }
    
    @Override
    public String getStateString(Lift lift) {
        return currentFloor + "-" + direction + "-" + currentLiftCapacity;
    }
    
    @Override
    public boolean canAcceptRequest(Lift lift, LiftRequest request) {
        // Idle lift can accept any request
        return currentLiftCapacity < lift.getLiftCapacity();
    }
}

// Up State - Lift is moving upward
class UpState extends LiftState {
    public UpState() {
        super();
        this.direction = 'U';
    }
    
    public UpState(int currentFloor, int currentLiftCapacity, Set<Integer> destinationFloors, Queue<LiftRequest> pendingRequests) {
        super();
        this.currentFloor = currentFloor;
        this.currentLiftCapacity = currentLiftCapacity;
        this.destinationFloors = new HashSet<>(destinationFloors);
        this.pendingRequests = new LinkedList<>(pendingRequests);
        this.direction = 'U';
    }
    
    @Override
    public void move(Lift lift) {
        if (currentFloor < lift.getFloors() - 1) {
            currentFloor++;
        }
    }
    
    @Override
    public void addStop(Lift lift, int floor) {
        if (floor >= currentFloor) {
            // Can only add stops in upward direction
            addDestinationFloor(floor);
        } else {
            // Add to pending requests for opposite direction
            addPendingRequest(new LiftRequest(floor, 'D'));
        }
    }
    
    @Override
    public void tick(Lift lift) {
        // Check if current floor is a destination
        if (destinationFloors.contains(currentFloor)) {
            removeDestinationFloor(currentFloor);
            // People get off at this floor
            if (currentLiftCapacity > 0) {
                currentLiftCapacity--;
            }
            
            // If no more destinations and no pending requests, go idle immediately
            if (destinationFloors.isEmpty() && !hasPendingRequests()) {
                lift.setState(new IdleState());
                IdleState idleState = (IdleState) lift.getState();
                idleState.setCurrentFloor(currentFloor);
                idleState.setCurrentLiftCapacity(0); // All people have left
                return;
            }
        }
        
        // Check if there are more destinations in upward direction
        boolean hasMoreUpDestinations = destinationFloors.stream().anyMatch(floor -> floor > currentFloor);
        
        if (destinationFloors.isEmpty() && !hasPendingRequests()) {
            // No more destinations and no pending requests, go idle
            lift.setState(new IdleState());
            IdleState idleState = (IdleState) lift.getState();
            idleState.setCurrentFloor(currentFloor);
            idleState.setCurrentLiftCapacity(0); // All people have left
        } else if (hasMoreUpDestinations) {
            // Continue moving up
            move(lift);
        } else if (hasPendingRequests()) {
            // No more upward destinations, but have pending requests in opposite direction
            LiftRequest request = pendingRequests.poll();
            lift.setState(new DownState(currentFloor, currentLiftCapacity, destinationFloors, pendingRequests));
            lift.getState().addStop(lift, request.startFloor);
        } else {
            // No more destinations and no pending requests, go idle
            lift.setState(new IdleState());
            IdleState idleState = (IdleState) lift.getState();
            idleState.setCurrentFloor(currentFloor);
            idleState.setCurrentLiftCapacity(0); // All people have left
        }
    }
    
    @Override
    public String getStateString(Lift lift) {
        return currentFloor + "-" + direction + "-" + currentLiftCapacity;
    }
    
    @Override
    public boolean canAcceptRequest(Lift lift, LiftRequest request) {
        // Can only accept requests going up and not passed the floor
        return request.direction == 'U' && 
               request.startFloor >= currentFloor && 
               currentLiftCapacity < lift.getLiftCapacity();
    }
}

// Down State - Lift is moving downward
class DownState extends LiftState {
    public DownState() {
        super();
        this.direction = 'D';
    }
    
    public DownState(int currentFloor, int currentLiftCapacity, Set<Integer> destinationFloors, Queue<LiftRequest> pendingRequests) {
        super();
        this.currentFloor = currentFloor;
        this.currentLiftCapacity = currentLiftCapacity;
        this.destinationFloors = new HashSet<>(destinationFloors);
        this.pendingRequests = new LinkedList<>(pendingRequests);
        this.direction = 'D';
    }
    
    @Override
    public void move(Lift lift) {
        if (currentFloor > 0) {
            currentFloor--;
        }
    }
    
    @Override
    public void addStop(Lift lift, int floor) {
        if (floor <= currentFloor) {
            // Can only add stops in downward direction
            addDestinationFloor(floor);
        } else {
            // Add to pending requests for opposite direction
            addPendingRequest(new LiftRequest(floor, 'U'));
        }
    }
    
    @Override
    public void tick(Lift lift) {
        // Check if current floor is a destination
        if (destinationFloors.contains(currentFloor)) {
            removeDestinationFloor(currentFloor);
            // People get off at this floor
            if (currentLiftCapacity > 0) {
                currentLiftCapacity--;
            }
        }
        
        // Check if there are more destinations in downward direction
        boolean hasMoreDownDestinations = destinationFloors.stream().anyMatch(floor -> floor < currentFloor);
        
        if (destinationFloors.isEmpty() && !hasPendingRequests()) {
            // No more destinations and no pending requests, go idle
            lift.setState(new IdleState());
            IdleState idleState = (IdleState) lift.getState();
            idleState.setCurrentFloor(currentFloor);
            idleState.setCurrentLiftCapacity(0); // All people have left
        } else if (hasMoreDownDestinations) {
            // Continue moving down
            move(lift);
        } else if (hasPendingRequests()) {
            // No more downward destinations, but have pending requests in opposite direction
            LiftRequest request = pendingRequests.poll();
            lift.setState(new UpState(currentFloor, currentLiftCapacity, destinationFloors, pendingRequests));
            lift.getState().addStop(lift, request.startFloor);
        }
    }
    
    @Override
    public String getStateString(Lift lift) {
        return currentFloor + "-" + direction + "-" + currentLiftCapacity;
    }
    
    @Override
    public boolean canAcceptRequest(Lift lift, LiftRequest request) {
        // Can only accept requests going down and not passed the floor
        return request.direction == 'D' && 
               request.startFloor <= currentFloor && 
               currentLiftCapacity < lift.getLiftCapacity();
    }
}
class LiftRequest{
    int startFloor;
    char direction;

    public LiftRequest(int startFloor, char direction) {
        this.startFloor = startFloor;
        this.direction = direction;
    }
}

