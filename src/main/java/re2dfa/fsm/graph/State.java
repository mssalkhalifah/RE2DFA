package re2dfa.fsm.graph;


import java.util.LinkedList;
import java.util.List;

public final class State {
    private int stateNumber;
    private boolean isAcceptanceState;
    private final List<Pair<State, String>> nextStates;

    public State() {
        this.stateNumber = -1;
        this.nextStates = new LinkedList<>();
    }

    public void addNextState(State nextState, String weight) {
       nextStates.add(new Pair<>(nextState, weight));
    }

    public List<Pair<State, String>> getNextStates() {
        return nextStates;
    }

    public void setAcceptanceState(boolean acceptanceState) {
        isAcceptanceState = acceptanceState;
    }

    public int getStateNumber() {
        return stateNumber;
    }

    public void setStateNumber(int stateNumber) {
        this.stateNumber = stateNumber;
    }

    @Override
    public String toString() {
        return String.format("State{%d,%b}", stateNumber, isAcceptanceState);
    }
}
