package parser.info;

import com.github.javaparser.ast.body.FieldDeclaration;
import parser.report.classes.ClassReport;

import java.util.Objects;

public class FieldInfoImpl implements FieldInfo {

    private final String name;
    private final String typeName;

    public FieldInfoImpl(final String name, final String type) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(type);
        this.name = name;
        this.typeName = type;
    }

    public FieldInfoImpl(final FieldDeclaration fieldDecl) {
        this(fieldDecl.getVariables().get(0).getNameAsString(),
                fieldDecl.getVariables().get(0).getTypeAsString());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getFieldTypeFullName() {
        return typeName;
    }

    @Override
    public ClassReport getParent() {
        throw new Error("Not implemented");
    }
}
