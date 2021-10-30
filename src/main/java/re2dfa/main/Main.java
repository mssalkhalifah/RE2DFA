package re2dfa.main;

import re2dfa.fsm.interfaces.FAGraph;
import re2dfa.fsm.util.FSMFactory;
import re2dfa.scanner.RegexReader;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static HashMap<String, String> symbolTable = new HashMap<>();

    public Main(Scanner scanner) {
        try {
            RegexReader reader = new RegexReader(scanner);
            String postFixedRegex = reader.postfixes();
            System.out.println(postFixedRegex);
            FAGraph nfa = FSMFactory.getFAGraph("nfa").build(postFixedRegex);
            nfa.printGraph();
            FAGraph dfa = FSMFactory.getFAGraph("dfa").build(postFixedRegex);
            dfa.printGraph();
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
    }
}
