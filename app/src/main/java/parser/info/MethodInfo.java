package parser.info;

import parser.report.ClassReport;

public interface MethodInfo {
    String getName();
    int getSrcBeginLine();
    int getEndBeginLine();
    ClassReport getParent();
}
