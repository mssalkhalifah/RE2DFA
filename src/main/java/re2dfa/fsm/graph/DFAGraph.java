package re2dfa.fsm.graph;

import org.jgrapht.alg.util.Pair;
import re2dfa.fsm.interfaces.FAGraph;

import java.util.*;

public class DFAGraph implements FAGraph {
    private final HashMap<Integer, List<Pair<Integer, String>>> graph;
    private final Set<Integer> acceptanceStates;

    public DFAGraph() {
        graph = new HashMap<>();
        acceptanceStates = new HashSet<>();
    }

    public int addState(int stateNumber) {
        if (!graph.containsKey(stateNumber)) {
            graph.put(stateNumber, new LinkedList<>());
            return stateNumber;
        }
        throw new IllegalArgumentException("DFA already contains " + stateNumber + " State");
    }

    public int addAcceptanceState(int acceptanceState) {
        if (graph.containsKey(acceptanceState)) {
            acceptanceStates.add(acceptanceState);
            return acceptanceState;
        }
        throw new IllegalArgumentException(String.format("%d does not exist in DFA", acceptanceState));
    }

    public boolean containsState(int state, int findState) {
        for (Pair<Integer, String> nextState : graph.get(state)) {
            if (nextState.getFirst() == findState) {
                return true;
            }
        }

        return false;
    }

    public Set<Integer> getAcceptanceStates() {
        return acceptanceStates;
    }

    public int addNextState(int state, String symbol, int nextState) {
        graph.get(state).add(new Pair<>(nextState, symbol));
        return nextState;
    }

    @Override
    public int size() {
        return graph.size();
    }

    @Override
    public void printGraph() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<Integer, List<Pair<Integer, String>>> state : graph.entrySet()) {
            stringBuilder.append(String.format("%d%s", state.getKey(),
                    (acceptanceStates.contains(state.getKey()) ? " {Accept}:" : ":")));
            for (Pair<Integer, String> nextState : state.getValue()) {
                stringBuilder.append(String.format("\n\t-[%s]-> %s", nextState.getSecond(),
                        (acceptanceStates.contains(nextState.getFirst())
                                ? String.format("%d AcceptanceState", nextState.getFirst())
                                : String.valueOf(nextState.getFirst()))));
            }
            stringBuilder.append('\n');
        }

        System.out.println(stringBuilder);
    }
}
