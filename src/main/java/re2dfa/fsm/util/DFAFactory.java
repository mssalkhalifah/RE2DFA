package re2dfa.fsm.util;

import org.jgrapht.alg.util.Pair;
import re2dfa.fsm.graph.DFAGraph;
import re2dfa.fsm.graph.NFAGraph;
import re2dfa.fsm.graph.State;
import re2dfa.fsm.interfaces.FAFactory;
import re2dfa.main.Main;

import java.util.*;

public class DFAFactory implements FAFactory {

    @Override
    public DFAGraph build(String regex) {
        NFAGraph nfa = (NFAGraph) FSMFactory.getFAGraph("nfa").build(regex);
        DFAGraph dfa = new DFAGraph();
        Queue<Set<Integer>> unmarkedStates = new LinkedList<>();
        List<Set<Integer>> markedStates = new LinkedList<>();
        int acceptanceState = nfa.getFinalState().getStateNumber();

        Set<Integer> initialSet = new HashSet<>();
        initialSet.add(nfa.getStartState().getStateNumber());
        unmarkedStates.add(eClosure(initialSet, nfa));
        dfa.addState(0);
        int currentStateNumber = 0;

        while (!unmarkedStates.isEmpty()) {
            Set<Integer> currentSet = unmarkedStates.poll();
            markedStates.add(currentSet);
            for (String symbol : Main.symbolTable.keySet()) {
                Set<Integer> newSet = eClosure(move(currentSet, symbol, nfa), nfa);
                int newStateNumber = isMarkedContainsSet(markedStates, unmarkedStates, newSet);
                int lastAddedState;

                if (newStateNumber >= 0) {
                    lastAddedState = dfa.addNextState(currentStateNumber, symbol, newStateNumber);
                } else {
                    unmarkedStates.add(newSet);
                    lastAddedState = dfa.addNextState(currentStateNumber, symbol, dfa.addState(dfa.size()));
                }

                if (dfa.containsState(lastAddedState, acceptanceState)) {
                    dfa.addAcceptanceState(lastAddedState);
                }
            }
            ++currentStateNumber;
        }

        return dfa;
    }

    private static Set<Integer> move(Set<Integer> stateSet, String symbol, NFAGraph faGraph) {
        Set<Integer> newSet = new HashSet<>();

        for (Integer state : stateSet) {
           Queue<State> stateQueue = new LinkedList<>();
           stateQueue.add(faGraph.getStateHashMap().get(state));

           newSet.addAll(traversal(stateQueue, new HashSet<>(), symbol));
        }

        return newSet;
    }

    private static Set<Integer> eClosure(Set<Integer> stateSet, NFAGraph faGraph) {
        Set<Integer> newSet = new HashSet<>();

        for (Integer state : stateSet) {
            Queue<State> stateQueue = new LinkedList<>();
            stateQueue.add(faGraph.getStateHashMap().get(state));
            newSet.add(state);

            newSet.addAll(traversal(stateQueue, new HashSet<>(), null));
        }

        return newSet;
    }

    private int isMarkedContainsSet(List<Set<Integer>> markedStateSets,
                                    Queue<Set<Integer>> unMarkedStateSets,Set<Integer> newSet) {
        int foundState = 0;
        for (Set<Integer> markedStateSet : markedStateSets) {
            if (markedStateSet.equals(newSet)) {
                return foundState;
            }
            ++foundState;
        }

        for (Set<Integer> unMarkedStateSet : unMarkedStateSets) {
            if (unMarkedStateSet.equals(newSet)) {
                return foundState;
            }
            ++foundState;
        }

        return -1;
    }

    private static Set<Integer> traversal(Queue<State> stateQueue, Set<Integer> stateSet, String symbol) {
        Set<Integer> traversalResult = new HashSet<>();
        while (!stateQueue.isEmpty()) {
            State currentState = stateQueue.poll();
            stateSet.add(currentState.getStateNumber());
            for (Pair<State, String> nextState : currentState.getNextStates()) {
                if (!traversalResult.contains(nextState.getFirst().getStateNumber())) {
                    if (symbol == null) {
                        if (nextState.getSecond() == null) {
                            stateQueue.add(nextState.getFirst());
                            traversalResult.add(nextState.getFirst().getStateNumber());
                        }
                    } else {
                        if (nextState.getSecond() != null && nextState.getSecond().equalsIgnoreCase(symbol)) {
                            stateQueue.add(nextState.getFirst());
                            traversalResult.add(nextState.getFirst().getStateNumber());
                        }
                    }
                }
            }
        }

        return traversalResult;
    }
}
