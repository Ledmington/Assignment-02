package parser.analyzer.report;

import parser.analyzer.info.MethodInfo;

import java.util.List;

public interface InterfaceReport {

    String getFullInterfaceName();

    String getSrcFullFileName();

    List<MethodInfo> getMethodsInfo();

}
