package re2dfa.fsm.util;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import re2dfa.fsm.graph.DFAGraph;
import re2dfa.fsm.graph.Pair;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GraphStreamFactory {
    public static Graph build(DFAGraph dfaGraph) {
        Graph graph = new MultiGraph("DFA");
        graph.setStrict(false);

        for (Map.Entry<Set<Integer>, List<Pair<Set<Integer>, String>>> state : dfaGraph.getStates()) {
            String currentState = String.valueOf(dfaGraph.indexOfState(state.getKey()));
            Node node = graph.addNode(currentState);

            if (dfaGraph.isAcceptanceState(state.getKey())) {
                node.setAttribute("ui.style", "fill-color: rgb(255,0,0);");
            }

            node.setAttribute("ui.label", currentState);

            for (Pair<Set<Integer>, String> nextState : state.getValue()) {
                String nextStateIndex = String.valueOf(dfaGraph.indexOfState(nextState.getFirst()));
                Node nextNode = graph.addNode(nextStateIndex);
                nextNode.setAttribute("ui.label", nextStateIndex);

                Edge edge = graph.addEdge(currentState + nextStateIndex, node, nextNode, true);
                edge.setAttribute("ui.label", nextState.getSecond());
            }
        }

        return graph;
    }
}
