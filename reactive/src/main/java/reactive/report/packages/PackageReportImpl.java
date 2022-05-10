package reactive.report.packages;

import reactive.report.classes.ClassReport;
import reactive.report.interfaces.InterfaceReport;

import java.util.List;
import java.util.Objects;

public class PackageReportImpl implements PackageReport {

    private final String fullPackageName;
    private final String srcFullFileName;
    private final List<ClassReport> classReports;
    private final List<InterfaceReport> interfaceReports;

    public PackageReportImpl(String fullPackageName, String srcFullFileName, List<ClassReport> classReports, List<InterfaceReport> interfaceReports) {
        Objects.requireNonNull(fullPackageName);
        Objects.requireNonNull(srcFullFileName);
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
