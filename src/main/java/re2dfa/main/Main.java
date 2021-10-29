package re2dfa.main;

import re2dfa.fsm.fa.NFAGraph;
import re2dfa.scanner.RegexReader;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public Main(Scanner scanner) {
        try {
            RegexReader reader = new RegexReader(scanner);
            String postFixedRegex = reader.postfixes();
            System.out.println(postFixedRegex);
            NFAGraph nfa = new NFAGraph();
            nfa.generateNFA(postFixedRegex);
            nfa.printGraph();
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
    }
}
