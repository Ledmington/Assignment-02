package parser.report;

import parser.info.MethodInfo;

import java.util.List;

public interface InterfaceReport {

    String getFullInterfaceName();

    String getSrcFullFileName();

    List<MethodInfo> getMethodsInfo();

}
