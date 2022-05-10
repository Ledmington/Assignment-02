package reactive.report.classes;

import reactive.info.FieldInfo;
import reactive.info.MethodInfo;

import java.util.List;

public interface ClassReport {

    static ClassReportBuilder builder() {
        return new ClassReportBuilder();
    }

    String getFullClassName();

    String getSrcFullFileName();

    List<MethodInfo> getMethodsInfo();

    List<FieldInfo> getFieldsInfo();
}
