package re2dfa.fsm.util;

import re2dfa.fsm.graph.NFAGraph;
import re2dfa.fsm.graph.State;
import re2dfa.fsm.interfaces.FAFactory;
import re2dfa.scanner.RegexReader;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class NFAFactory implements FAFactory {
    public NFAGraph build(String regex) {
        Stack<NFAGraph> faGraphStack = new Stack<>();
        int currentPos = 0;

        while (currentPos < regex.length()) {
            if (RegexReader.isOperand(regex.charAt(currentPos))) {
                NFAGraph operandGraph = new NFAGraph(new State(), new State());
                operandGraph
                        .getStartState()
                        .addNextState(operandGraph.getFinalState(), String.valueOf(regex.charAt(currentPos)));
                faGraphStack.push(operandGraph);
            } else if (regex.charAt(currentPos) == '|') {
                NFAGraph orOperatorGraph = new NFAGraph(new State(), new State());
                NFAGraph secondOperand = faGraphStack.pop();
                NFAGraph firstOperand = faGraphStack.pop();

                firstOperand.getFinalState().setAcceptanceState(false);
                secondOperand.getFinalState().setAcceptanceState(false);

                orOperatorGraph.getStartState().addNextState(firstOperand.getStartState(), null);
                orOperatorGraph.getStartState().addNextState(secondOperand.getStartState(), null);
                firstOperand.getFinalState().addNextState(orOperatorGraph.getFinalState(), null);
                secondOperand.getFinalState().addNextState(orOperatorGraph.getFinalState(), null);

                faGraphStack.push(orOperatorGraph);
            } else if (regex.charAt(currentPos) == '*') {
                NFAGraph repeatGraph = new NFAGraph(new State(), new State());
                NFAGraph prevGraph = faGraphStack.pop();

                prevGraph.getFinalState().setAcceptanceState(false);
                prevGraph.getFinalState().addNextState(prevGraph.getStartState(), null);

                repeatGraph.getStartState().addNextState(repeatGraph.getFinalState(), null);
                repeatGraph.getStartState().addNextState(prevGraph.getStartState(), null);
                prevGraph.getFinalState().addNextState(repeatGraph.getFinalState(), null);

                faGraphStack.push(repeatGraph);
            } else if (regex.charAt(currentPos) == '.') {
                NFAGraph secondOperand = faGraphStack.pop();
                NFAGraph firstOperand = faGraphStack.pop();

                NFAGraph concatFAGraph = new NFAGraph(firstOperand.getStartState(), secondOperand.getFinalState());

                firstOperand.getFinalState().setAcceptanceState(false);
                firstOperand.getFinalState().addNextState(secondOperand.getStartState(), null);

                concatFAGraph.getFinalState().setAcceptanceState(true);

                faGraphStack.push(concatFAGraph);
            }
            ++currentPos;
        }
        NFAGraph nfaResult = faGraphStack.pop();
        setStateNumber(nfaResult);
        return  nfaResult;
    }

    private static void setStateNumber(NFAGraph faGraph) {
        Queue<State> stateQueue = new LinkedList<>();
        stateQueue.add(faGraph.getStartState());
        faGraph.getStateHashMap().clear();
        int currStateNumber = 0;
        while (!stateQueue.isEmpty()) {
            State state = stateQueue.poll();
            if (state.getStateNumber() < 0) {
                state.setStateNumber(currStateNumber);
                ++currStateNumber;
                faGraph.getStateHashMap().put(state.getStateNumber(), state);
            }
            state.getNextStates().forEach(nextState -> {
                if (nextState.getFirst().getStateNumber() < 0) {
                    stateQueue.add(nextState.getFirst());
                }
            });
        }
    }
}
