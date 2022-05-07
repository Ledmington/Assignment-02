package reactive.info;

import reactive.report.classes.ClassReport;

import java.util.Objects;

public class MethodInfoImpl implements MethodInfo {

    private final String name;
    private final Range range;

    public MethodInfoImpl(final String name, final int start, final int end) {
        Objects.requireNonNull(name);
        this.name = name;
        this.range = new Range(start, end);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getSrcBeginLine() {
        return range.start();
    }

    @Override
    public int getEndBeginLine() {
        return range.start();
    }

    @Override
    public ClassReport getParent() {
        throw new Error("Not implemented");
    }
}