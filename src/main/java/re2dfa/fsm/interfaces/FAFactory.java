package re2dfa.fsm.interfaces;

import java.util.List;

public interface FAFactory {
    FAGraph build(List<String> tokens);
}
