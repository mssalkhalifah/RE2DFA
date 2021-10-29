package re2dfa.fsm.fa;

import re2dfa.fsm.graph.FAGraph;
import re2dfa.fsm.graph.State;
import re2dfa.scanner.RegexReader;

import java.util.*;

public class NFAGraph {
    private FAGraph nfa;
    private HashSet<State> stateHashSet;

    public NFAGraph() {
        stateHashSet = new HashSet<>();
    }

    public HashSet<State> getStateHashSet() {
        return stateHashSet;
    }

    public FAGraph getNfa() {
        return nfa;
    }

    public void generateNFA(String regex) {
        Stack<FAGraph> faGraphStack = new Stack<>();
        int currentPos = 0;

        while (currentPos < regex.length()) {
           if (RegexReader.isOperand(regex.charAt(currentPos))) {
              FAGraph operandGraph = new FAGraph();
              operandGraph
                      .getStartState()
                      .addNextState(operandGraph.getFinalState(), ""+ regex.charAt(currentPos));
              faGraphStack.push(operandGraph);
           } else if (regex.charAt(currentPos) == '|') {
               FAGraph orOperatorGraph = new FAGraph();
               FAGraph secondOperand = faGraphStack.pop();
               FAGraph firstOperand = faGraphStack.pop();

               firstOperand.getFinalState().setAcceptanceState(false);
               secondOperand.getFinalState().setAcceptanceState(false);

               orOperatorGraph.getStartState().addNextState(firstOperand.getStartState(), null);
               orOperatorGraph.getStartState().addNextState(secondOperand.getStartState(), null);
               firstOperand.getFinalState().addNextState(orOperatorGraph.getFinalState(), null);
               secondOperand.getFinalState().addNextState(orOperatorGraph.getFinalState(), null);

               faGraphStack.push(orOperatorGraph);
           } else if (regex.charAt(currentPos) == '*') {
               FAGraph repeatGraph = new FAGraph();
               FAGraph prevGraph = faGraphStack.pop();

               prevGraph.getFinalState().setAcceptanceState(false);
               prevGraph.getFinalState().addNextState(prevGraph.getStartState(), null);

               repeatGraph.getStartState().addNextState(repeatGraph.getFinalState(), null);
               repeatGraph.getStartState().addNextState(prevGraph.getStartState(), null);
               prevGraph.getFinalState().addNextState(repeatGraph.getFinalState(), null);

               faGraphStack.push(repeatGraph);
           } else if (regex.charAt(currentPos) == '.') {
               FAGraph secondOperand = faGraphStack.pop();
               FAGraph firstOperand = faGraphStack.pop();

               firstOperand.getFinalState().setAcceptanceState(false);
               firstOperand.getFinalState().addNextState(secondOperand.getStartState(), null);

               faGraphStack.push(firstOperand);
           }
           ++currentPos;
        }
        nfa = faGraphStack.pop();
        setStateNumber(nfa);
    }

    public void printGraph() {
        Queue<State> stateQueue = new LinkedList<>();
        stateQueue.add(nfa.getStartState());
        StringBuilder stringBuilder = new StringBuilder();
        HashSet<Integer> stateNumberSet = new HashSet<>();
        while (!stateQueue.isEmpty()) {
            State state = stateQueue.poll();
            if (!stateNumberSet.contains(state.getStateNumber())) {
                stringBuilder.append(String.format("%s[%d]", state, state.getNextStates().size()));
                stateNumberSet.add(state.getStateNumber());
                state.getNextStates().forEach(nextState -> {
                    stateQueue.add(nextState.getFirst());
                    stringBuilder.append(String.format(":-[%s]->%s", nextState.getSecond(), nextState.getFirst()));
                });
            }
            stringBuilder.append('\n');
        }

        System.out.println(stringBuilder);
    }

    private void setStateNumber(FAGraph faGraph) {
        Queue<State> stateQueue = new LinkedList<>();
        stateQueue.add(faGraph.getStartState());
        int currStateNumber = 0;
        while (!stateQueue.isEmpty()) {
            State state = stateQueue.poll();
            if (state.getStateNumber() < 0) {
                state.setStateNumber(currStateNumber);
                ++currStateNumber;
                stateHashSet.add(state);
            }
            state.getNextStates().forEach(nextState -> {
                if (nextState.getFirst().getStateNumber() < 0) {
                    stateQueue.add(nextState.getFirst());
                }
            });
        }
    }
}
