package parser.utils;

public class Pair<A, B> {
    
    private final A first;
    private final B second;

    public Pair(final A first, final B second) {
        this.first = first;
        this.second = second;
    }

    public A first() {
        return first;
    }

    public B second() {
        return second;
    }
}
