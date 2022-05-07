package reactive.report.classes;

import reactive.info.FieldInfo;
import reactive.info.MethodInfo;

import java.util.List;

public class ClassReportImpl implements ClassReport {

    private final String className;
    private final String fileName;
    private final List<MethodInfo> methods;
    private final List<FieldInfo> fields;

    public ClassReportImpl(final String className, final String fileName, final List<MethodInfo> methods, final List<FieldInfo> fields) {
        this.className = className;
        this.fileName = fileName;
        this.methods = methods;
        this.fields = fields;
    }

    @Override
    public String getFullClassName() {
        return className;
    }

    @Override
    public String getSrcFullFileName() {
        return fileName;
    }

    @Override
    public List<MethodInfo> getMethodsInfo() {
        return methods;
    }

    @Override
    public List<FieldInfo> getFieldsInfo() {
        return fields;
    }
}
