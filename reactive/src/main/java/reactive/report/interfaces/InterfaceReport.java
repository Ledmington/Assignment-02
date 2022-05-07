package reactive.report.interfaces;

import reactive.info.MethodInfo;

import java.util.List;

public interface InterfaceReport {

    String getFullInterfaceName();

    String getSrcFullFileName();

    List<MethodInfo> getMethodsInfo();

}
