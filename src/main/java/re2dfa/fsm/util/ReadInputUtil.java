package re2dfa.fsm.util;

import re2dfa.fsm.graph.DFAGraph;
import re2dfa.fsm.graph.Pair;
import re2dfa.main.Main;

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
                if (Main.symbolTable.get(nextState.getSecond()).contains(String.valueOf(currentChar))) {
                    currentState = nextState.getFirst();
                    hasNextState = true;
                    break;
                }
            }

            if (!hasNextState) {
                return dfaGraph.isAcceptanceState(currentState);
            }
            ++currentPos;
        }

        return dfaGraph.isAcceptanceState(currentState);
    }
}
