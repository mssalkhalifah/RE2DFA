package re2dfa.fsm.graph;

public final class Pair<S, U> {
    private final S firstValue;
    private final U secondValue;

    public Pair(S firstValue, U secondValue) {
        this.firstValue = firstValue;
        this.secondValue = secondValue;
    }

    public S getFirst() {
        return firstValue;
    }

    public U getSecond() {
        return secondValue;
    }

    @Override
    public String toString() {
        return String.format("(%s,%s)", firstValue, secondValue);
    }
}
