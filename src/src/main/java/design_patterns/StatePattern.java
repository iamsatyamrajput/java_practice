package design_patterns;

public class StatePattern {
    public static void main(String[] args) {
        Lift lift = new Lift();
        lift.showState();
        lift.openGate();
        lift.closeGate();

        lift.setTargetFloor(3);
        lift.move();

        lift.showState();
        lift.openGate();
        lift.move();
        lift.move();
        lift.move();
        lift.openGate();
        lift.showState();
        lift.closeGate();
        lift.setTargetFloor(1);
        lift.move();
        lift.showState();
        lift.closeGate();
    }
}
class Lift {
    private LiftState state;
    private int currentFloor;
    private int targetFloor;

    public Lift() {
        this.state = new IdleState();
        this.currentFloor = 0;
        this.targetFloor = 0;
    }

    public void setState(LiftState state) {
        this.state = state;
    }

    public void setCurrentFloor(int floor) {
        this.currentFloor = floor;
    }

    public int getCurrentFloor() {
        return this.currentFloor;
    }

    public void setTargetFloor(int floor) {
        this.targetFloor = floor;
    }

    public int getTargetFloor() {
        return this.targetFloor;
    }
    public void move() {
        state.move(this);
    }

    public void openGate() {
        state.openGate(this);
    }

    public void closeGate() {
        state.closeGate(this);
    }

    public void showState() {
        state.currentState();
    }
}

interface LiftState {
    void move(Lift lift);
    void openGate(Lift lift);
    void closeGate(Lift lift);
    void currentState();
}
class IdleState implements LiftState {
    @Override
    public void move(Lift lift) {
        if (lift.getTargetFloor() > lift.getCurrentFloor()) {
            System.out.println("Lift starting to move up...");
            lift.setState(new MovingUpState());
        } else if (lift.getTargetFloor() < lift.getCurrentFloor()) {
            System.out.println("Lift starting to move down...");
            lift.setState(new MovingDownState());
        } else {
            System.out.println("Already at target floor.");
        }
    }

    @Override
    public void openGate(Lift lift) {
        System.out.println("Opening Gate at floor " + lift.getCurrentFloor());
    }

    @Override
    public void closeGate(Lift lift) {
        System.out.println("Closing Gate.");
    }

    @Override
    public void currentState() {
        System.out.println("Lift is idle.");
    }
}

class MovingUpState implements LiftState {
    @Override
    public void move(Lift lift) {
        System.out.println("Moving up from " + lift.getCurrentFloor() + " to " + lift.getTargetFloor());
        lift.setCurrentFloor(lift.getTargetFloor());
        lift.setState(new IdleState());
    }

    @Override
    public void openGate(Lift lift) {
        System.out.println("Can't open gate while moving up.");
    }

    @Override
    public void closeGate(Lift lift) {
        System.out.println("Gate already closed.");
    }

    @Override
    public void currentState() {
        System.out.println("Lift is moving up.");
    }
}
class MovingDownState implements LiftState {
    @Override
    public void move(Lift lift) {
        System.out.println("Moving down from " + lift.getCurrentFloor() + " to " + lift.getTargetFloor());
        lift.setCurrentFloor(lift.getTargetFloor());
        lift.setState(new IdleState());
    }

    @Override
    public void openGate(Lift lift) {
        System.out.println("Can't open gate while moving down.");
    }

    @Override
    public void closeGate(Lift lift) {
        System.out.println("Gate already closed.");
    }

    @Override
    public void currentState() {
        System.out.println("Lift is moving down.");
    }
}



