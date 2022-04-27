package parser.analyzer.info;

import parser.analyzer.report.ClassReport;

public interface FieldInfo {
    String getName();
    String getFieldTypeFullName();
    ClassReport getParent();
}
