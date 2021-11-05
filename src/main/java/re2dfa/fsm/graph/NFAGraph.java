package re2dfa.fsm.graph;

import re2dfa.fsm.interfaces.FAGraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public final class NFAGraph implements FAGraph {
    private final State startState;
    private final State finalState;
    private final HashMap<Integer, State> stateHashMap;

// --Commented out by Inspection START (11/3/21, 5:21 PM):
//    public NFAGraph() {
//        this.stateHashMap = new HashMap<>();
//        this.startState = new State();
//        this.finalState = startState;
//        this.finalState.setAcceptanceState(true);
//    }
// --Commented out by Inspection STOP (11/3/21, 5:21 PM)

    public NFAGraph(State startState, State finalState) {
        stateHashMap = new HashMap<>();
        this.startState = startState;
        this.finalState = finalState;
        this.finalState.setAcceptanceState(true);
    }

    public State getStartState() {
        return startState;
    }

    public State getFinalState() {
        return finalState;
    }

    public HashMap<Integer, State> getStateHashMap() {
        return stateHashMap;
    }

    @Override
    public int size() {
        return stateHashMap.size();
    }

    public void printGraph() {
        Queue<State> stateQueue = new LinkedList<>();
        stateQueue.add(startState);
        StringBuilder stringBuilder = new StringBuilder();
        HashSet<Integer> stateNumberSet = new HashSet<>();
        while (!stateQueue.isEmpty()) {
            State state = stateQueue.poll();
            if (!stateNumberSet.contains(state.getStateNumber())) {
                stringBuilder.append(String.format("%s[%d]", state, state.getNextStates().size()));
                stateNumberSet.add(state.getStateNumber());
                state.getNextStates().forEach(nextState -> {
                    stateQueue.add(nextState.getFirst());
                    stringBuilder.append(String.format("\n\t:-[%s]->%s", nextState.getSecond(), nextState.getFirst()));
                });
            }
            stringBuilder.append('\n');
        }

        System.out.println(stringBuilder);
    }
}
