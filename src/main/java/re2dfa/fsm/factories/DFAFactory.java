package re2dfa.fsm.factories;

import re2dfa.fsm.graph.Pair;
import re2dfa.fsm.graph.DFAGraph;
import re2dfa.fsm.graph.NFAGraph;
import re2dfa.fsm.graph.State;
import re2dfa.fsm.interfaces.FAFactory;
import re2dfa.main.Main;
import java.util.*;

public class DFAFactory implements FAFactory {

    @Override
    public DFAGraph build(List<String> tokens) {
        NFAGraph nfa = (NFAGraph) FSMFactory.getFAGraph("nfa").build(tokens);
        DFAGraph dfa = new DFAGraph();
        Queue<Set<Integer>> unmarkedStates = new LinkedList<>();
        int acceptanceState = nfa.getFinalState().getStateNumber();

        Set<Integer> initialSet = new HashSet<>();
        initialSet.add(nfa.getStartState().getStateNumber());
        unmarkedStates.add(eClosure(initialSet, nfa));

        while (!unmarkedStates.isEmpty()) {
            Set<Integer> currentSet = unmarkedStates.poll();
            dfa.addState(currentSet);

            if (currentSet.contains(acceptanceState)) {
                dfa.addAcceptanceState(currentSet);
            }

            for (String symbol : Main.symbolTable.keySet()) {
                Set<Integer> newSet = eClosure(move(currentSet, symbol, nfa), nfa);

                if (!newSet.isEmpty()) {
                    if (!dfa.containState(newSet)) {
                        dfa.addState(newSet);
                        unmarkedStates.add(newSet);
                    }

                    dfa.addNextState(currentSet, newSet, symbol);
                }
            }
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
                        if (nextState.getSecond() != null && nextState.getSecond().equalsIgnoreCase(Main.symbolTable.get(symbol))) {
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
