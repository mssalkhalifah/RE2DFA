package re2dfa.main;

import org.graphstream.graph.Graph;
import org.graphstream.ui.view.Viewer;
import re2dfa.fsm.graph.DFAGraph;
import re2dfa.fsm.interfaces.FAGraph;
import re2dfa.fsm.factories.FSMFactory;
import re2dfa.fsm.factories.GraphStreamFactory;
import re2dfa.fsm.util.ReadInputUtil;
import re2dfa.scanner.RegexReader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public final static HashMap<String, String> symbolTable = new HashMap<>();

    private Main() {}

    public static void start(Scanner scanner, String input, boolean visualMode) {
        String regex = scanner.next();
        List<String> tokens = RegexReader.getTokenList(regex);

        System.out.printf("Tokens: %s%n%n", tokens);

        FAGraph nfa = FSMFactory.getFAGraph("nfa").build(tokens);
        nfa.printGraph();
        FAGraph dfa = FSMFactory.getFAGraph("dfa").build(tokens);
        dfa.printGraph();

        System.out.printf("NFA constructed with %d states%nTransformed into DFA with %d states%n", nfa.size(), dfa.size());
        if (!input.isEmpty()) {
            System.out.printf("Regex: %s%nInput: %s is %b%n", regex, input, ReadInputUtil.checkInput(input, (DFAGraph) dfa));
        }

        if (visualMode) {
            InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("graphstream.style");
            Scanner s = new Scanner(Objects.requireNonNull(inputStream)).useDelimiter("\\A");
            String graphAttribute = s.hasNext() ? s.next() : "";
            System.setProperty("org.graphstream.ui", "swing");
            Graph dfaGraph = GraphStreamFactory.build((DFAGraph) dfa);
            dfaGraph.setAttribute("ui.stylesheet", graphAttribute);
            dfaGraph.setAttribute("ui.quality");
            dfaGraph.setAttribute("ui.antialias");
            Viewer viewer = dfaGraph.display();
            viewer.getDefaultView().enableMouseOptions();
        }
    }
}
