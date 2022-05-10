package reactive.report.packages;

import reactive.report.classes.ClassReport;
import reactive.report.interfaces.InterfaceReport;

import java.util.LinkedList;
import java.util.List;

public class PackageReportBuilder {

    private String fullPackageName;
    private String srcFullFileName;
    private final List<ClassReport> classReports = new LinkedList<>();
    private final List<InterfaceReport> interfaceReports = new LinkedList<>();
    private boolean built = false;

    public PackageReportBuilder fullName(final String name) {
        this.fullPackageName = name;
        return this;
    }

    public PackageReportBuilder fullFileName(final String name) {
        this.srcFullFileName = name;
        return this;
    }

    public PackageReportBuilder addClass(final ClassReport cr) {
        this.classReports.add(cr);
        return this;
    }

    public PackageReportBuilder addInterface(final InterfaceReport ir) {
        this.interfaceReports.add(ir);
        return this;
    }

    public PackageReport build() {
        if (built) {
            throw new IllegalStateException("Cannot build the same builder twice");
        }
        built = true;
        return new PackageReportImpl(
                fullPackageName,
                srcFullFileName,
                classReports,
                interfaceReports
        );
    }
}
