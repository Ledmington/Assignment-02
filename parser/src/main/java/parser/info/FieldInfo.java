package parser.info;

import parser.report.classes.ClassReport;

public interface FieldInfo extends ProjectElem{
    String getName();
    String getFieldTypeFullName();
    ClassReport getParent();
}
