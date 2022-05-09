package parser.info;

import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.Objects;

public class MethodInfoImpl implements MethodInfo {

    private final String name;
    private final Range range;

    public MethodInfoImpl(final String name, final int start, final int end) {
        Objects.requireNonNull(name);
        this.name = name;
        this.range = new Range(start, end);
    }

    public MethodInfoImpl(final MethodDeclaration methDecl) {
        this(methDecl.getNameAsString(),
                methDecl.getName().getBegin().orElseThrow().line,
                methDecl.getName().getEnd().orElseThrow().line);
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
}
