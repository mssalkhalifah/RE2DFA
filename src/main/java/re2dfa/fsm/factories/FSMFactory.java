package re2dfa.fsm.factories;

import re2dfa.fsm.interfaces.FAFactory;

public class FSMFactory {

    private FSMFactory() {}

    public static FAFactory getFAGraph(String faType) {
        if (faType.equalsIgnoreCase("NFA")) {
            return new NFAFactory();
        } else if (faType.equalsIgnoreCase("DFA")) {
            return new DFAFactory();
        }

        throw new IllegalArgumentException("Type not found");
    }
}
