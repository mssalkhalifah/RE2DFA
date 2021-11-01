package re2dfa.fsm.graph;

import re2dfa.fsm.interfaces.FAGraph;
import java.util.*;

public final class DFAGraph implements FAGraph {
    private final HashMap<Set<Integer>, List<Pair<Set<Integer>, String>>> graph;
    private final HashSet<Set<Integer>> acceptanceStates;

    public DFAGraph() {
        graph = new LinkedHashMap<>(32);
        acceptanceStates = new HashSet<>();
    }

    public void addState(Set<Integer> state) {
        if (!graph.containsKey(state)) {
            graph.put(state, new LinkedList<>());
        }
    }

    public int indexOfState(Set<Integer> stateIndex) {
        int index = 0;
        for (Set<Integer> state : graph.keySet()) {
            if (state.equals(stateIndex)) {
                return index;
            }
            ++index;
        }
        return -1;
    }

    public Set<Map.Entry<Set<Integer>, List<Pair<Set<Integer>, String>>>> getStates() {
        return graph.entrySet();
    }

    public void addAcceptanceState(Set<Integer> acceptanceState) {
        if (graph.containsKey(acceptanceState)) {
            acceptanceStates.add(acceptanceState);
        }
    }

    public boolean isAcceptanceState(Set<Integer> state) {
        return acceptanceStates.contains(state);
    }

    public void addNextState(Set<Integer> state, Set<Integer> nextState, String symbol) {
        if (graph.containsKey(state)) {
            graph.get(state).add(new Pair<>(nextState, symbol));
        } else {
            throw new IllegalArgumentException("State does not exist");
        }
    }

    public boolean containState(Set<Integer> state) {
        return graph.containsKey(state);
    }

    @Override
    public int size() {
        return graph.size();
    }

    @Override
    public void printGraph() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Map.Entry<Set<Integer>, List<Pair<Set<Integer>, String>>> state : graph.entrySet()) {
            stringBuilder.append(String.format("%d%s", indexOfState(state.getKey()),
                    (acceptanceStates.contains(state.getKey()) ? "{Accept}:" : ":")));
            for (Pair<Set<Integer>, String> nextState : state.getValue()) {
                stringBuilder.append(String.format("\n\t:-[%s]->%d", nextState.getSecond(),
                        indexOfState(nextState.getFirst())));
            }
            stringBuilder.append('\n');
        }

        System.out.println(stringBuilder);
    }
}
