package reactive.report.packages;

import reactive.report.classes.ClassReport;
import reactive.report.interfaces.InterfaceReport;

import java.util.List;

public interface PackageReport {

    String getFullPackageName();

    String getSrcFullFileName();

    List<ClassReport> getClassReports();

    List<InterfaceReport> getInterfaceReports();

}
