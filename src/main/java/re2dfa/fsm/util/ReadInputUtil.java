package re2dfa.fsm.util;

import re2dfa.fsm.graph.DFAGraph;
import re2dfa.fsm.graph.Pair;

import java.util.Set;

public class ReadInputUtil {
    private ReadInputUtil() {}

    public static boolean checkInput(String input, DFAGraph dfaGraph) {
        int currentPos = 0;
        Set<Integer> currentState = dfaGraph.getStartingState();

        while (currentPos < input.length()) {
            char currentChar = input.charAt(currentPos);
            boolean hasNextState = false;
            for (Pair<Set<Integer>, String> nextState : dfaGraph.getNextStates(currentState)) {
                if (nextState.getSecond().equalsIgnoreCase(String.valueOf(currentChar))) {
                    currentState = nextState.getFirst();
                    hasNextState = true;
                    break;
                }
            }

            if (!hasNextState) {
                return false;
            }
            ++currentPos;
        }

        return dfaGraph.isAcceptanceState(currentState);
    }
}
