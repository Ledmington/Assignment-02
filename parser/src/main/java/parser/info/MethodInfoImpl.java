package parser.info;

import parser.report.classes.ClassReport;

import java.util.Objects;

public class MethodInfoImpl implements MethodInfo {

    private final String name;
    private final int startLine;
    private final int endLine;

    public MethodInfoImpl(final String name, final int start, final int end) {
        Objects.requireNonNull(name);

        if(start < 0 || end < 0) {
            throw new IllegalArgumentException("start and end must be >= 0");
        }
        if(start > end) {
            throw new IllegalArgumentException("start must be <= end");
        }

        this.name = name;
        this.startLine = start;
        this.endLine = end;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getSrcBeginLine() {
        return startLine;
    }

    @Override
    public int getEndBeginLine() {
        return endLine;
    }

    @Override
    public ClassReport getParent() {
        throw new Error("Not implemented");
    }
}
