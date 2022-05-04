package parser.report.packages;

import parser.report.classes.ClassReport;
import parser.report.interfaces.InterfaceReport;

import java.util.List;

public interface PackageReport {

	String getFullPackageName();
	
	String getSrcFullFileName();

	List<ClassReport> getClassReports();

	List<InterfaceReport> getInterfaceReports();
	
}
