package parser.analyzer.info;

import parser.analyzer.report.ClassReport;

public interface MethodInfo {
    String getName();
    int getSrcBeginLine();
    int getEndBeginLine();
    ClassReport getParent();
}
