package parser.info;

import parser.report.ClassReport;

public interface FieldInfo {
    String getName();
    String getFieldTypeFullName();
    ClassReport getParent();
}
