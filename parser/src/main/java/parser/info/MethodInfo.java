package parser.info;

import parser.report.classes.ClassReport;

public interface MethodInfo extends ProjectElem{
    String getName();
    int getSrcBeginLine();
    int getEndBeginLine();
    ClassReport getParent();
}
