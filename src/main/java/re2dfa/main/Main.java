package re2dfa.main;

import org.graphstream.graph.Graph;
import org.graphstream.ui.view.Viewer;
import re2dfa.fsm.graph.DFAGraph;
import re2dfa.fsm.interfaces.FAGraph;
import re2dfa.fsm.util.FSMFactory;
import re2dfa.fsm.util.GraphStreamFactory;
import re2dfa.fsm.util.ReadInputUtil;
import re2dfa.scanner.RegexReader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public final static HashMap<String, String> symbolTable = new HashMap<>();

    private Main() {}

    public static void start(Scanner scanner, String input, boolean visualMode, boolean debugMode) {
        RegexReader reader = new RegexReader(scanner);
        String postFixedRegex = reader.postfixes();

        System.out.println(postFixedRegex);

        FAGraph nfa = FSMFactory.getFAGraph("nfa").build(postFixedRegex);
        nfa.printGraph();
        FAGraph dfa = FSMFactory.getFAGraph("dfa").build(postFixedRegex);
        dfa.printGraph();

        System.out.printf("Input: %s is %b%n", input, ReadInputUtil.checkInput(input, (DFAGraph) dfa));

        if (visualMode) {
            InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("graphstream.style");
            Scanner s = new Scanner(Objects.requireNonNull(inputStream)).useDelimiter("\\A");
            String graphAttribute = s.hasNext() ? s.next() : "";
            System.setProperty("org.graphstream.ui", "swing");
            Graph dfaGraph = GraphStreamFactory.build((DFAGraph) dfa);
            dfaGraph.setAttribute("ui.stylesheet", graphAttribute);
            Viewer viewer = dfaGraph.display();
            viewer.getDefaultView().enableMouseOptions();
        }
    }
}
