package reactive.info;

import reactive.report.classes.ClassReport;

public interface MethodInfo {
    String getName();

    int getSrcBeginLine();

    int getEndBeginLine();

    ClassReport getParent();
}
