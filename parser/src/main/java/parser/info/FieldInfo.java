package parser.info;

import parser.report.classes.ClassReport;

public interface FieldInfo {
    String getName();

    String getFieldTypeFullName();

    ClassReport getParent();
}
