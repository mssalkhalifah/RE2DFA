package re2dfa.fsm.interfaces;

public interface FAFactory {
    FAGraph build(String regex);
}
