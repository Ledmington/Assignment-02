package parser.report.interfaces;

import parser.info.MethodInfo;
import parser.info.ProjectElem;

import java.util.List;

public interface InterfaceReport extends ProjectElem {

    String getFullInterfaceName();

    String getSrcFullFileName();

    List<MethodInfo> getMethodsInfo();

}
