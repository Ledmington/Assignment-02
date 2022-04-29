package parser.report.classes;

import parser.info.FieldInfo;
import parser.info.MethodInfo;

import java.util.LinkedList;
import java.util.List;

public class ClassReportBuilder {

    private String className;
    private String fileName;
    private List<MethodInfo> methods = new LinkedList<>();
    private List<FieldInfo> fields = new LinkedList<>();
    private boolean built = false;

    public ClassReportBuilder className(final String name) {
        this.className = name;
        return this;
    }

    public ClassReportBuilder fileName(final String name) {
        this.fileName = name;
        return this;
    }

    public ClassReportBuilder addMethod(final MethodInfo m) {
        this.methods.add(m);
        return this;
    }

    public ClassReportBuilder addField(final FieldInfo f) {
        this.fields.add(f);
        return this;
    }

    public ClassReport build() {
        if(built) {
            throw new IllegalStateException("Cannot use the same CLassReportBuilder twice");
        }
        built = true;
        return new ClassReportImpl(className, fileName, methods, fields);
    }
}
