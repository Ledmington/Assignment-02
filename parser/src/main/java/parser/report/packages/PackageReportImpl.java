package parser.report.packages;

import parser.report.classes.ClassReport;
import parser.report.interfaces.InterfaceReport;

import java.util.List;

public class PackageReportImpl implements PackageReport {

    private final String fullPackageName;
    private final String srcFullFileName;
    private final List<ClassReport> classReports;
    private final List<InterfaceReport> interfaceReports;

    public PackageReportImpl(String fullPackageName, String srcFullFileName, List<ClassReport> classReports, List<InterfaceReport> interfaceReports) {
        this.fullPackageName = fullPackageName;
        this.srcFullFileName = srcFullFileName;
        this.classReports = classReports;
        this.interfaceReports = interfaceReports;
    }

    @Override
    public String getFullPackageName() {
        return fullPackageName;
    }

    @Override
    public String getSrcFullFileName() {
        return srcFullFileName;
    }

    @Override
    public List<ClassReport> getClassReports() {
        return classReports;
    }

    @Override
    public List<InterfaceReport> getInterfaceReports() {
        return interfaceReports;
    }
}
