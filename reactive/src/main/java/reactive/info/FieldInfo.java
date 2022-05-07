package reactive.info;

import reactive.report.classes.ClassReport;

public interface FieldInfo {
    String getName();

    String getFieldTypeFullName();

    ClassReport getParent();
}
