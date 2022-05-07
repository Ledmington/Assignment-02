package reactive.report.interfaces;

import reactive.info.MethodInfo;

import java.util.List;

public class InterfaceReportImpl implements InterfaceReport {

    private final String fullInterfaceName;
    private final String srcInterfacePath;
    private final List<MethodInfo> methodsInfo;

    public InterfaceReportImpl(String fullInterfaceName, String srcInterfacePath, List<MethodInfo> methodsInfo) {
        this.fullInterfaceName = fullInterfaceName;
        this.srcInterfacePath = srcInterfacePath;
        this.methodsInfo = methodsInfo;
    }

    @Override
    public String getFullInterfaceName() {
        return fullInterfaceName;
    }

    @Override
    public String getSrcFullFileName() {
        return srcInterfacePath;
    }

    @Override
    public List<MethodInfo> getMethodsInfo() {
        return methodsInfo;
    }
}
