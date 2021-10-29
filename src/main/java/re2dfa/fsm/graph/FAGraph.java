package re2dfa.fsm.graph;

import java.util.LinkedList;
import java.util.List;

public class FAGraph {
    private List<State> states;
    private State startState;
    private State finalState;

    public FAGraph() {
        states = new LinkedList<>();
        startState = new State();
        finalState = new State();
        finalState.setAcceptanceState(true);
    }

    public List<State> getStates() {
        return states;
    }

    public void setFinalState(State finalState) {
        this.finalState = finalState;
    }

    public State getStartState() {
        return startState;
    }

    public State getFinalState() {
        return finalState;
    }
}
