package parser.report;

import parser.report.classes.ClassReport;

import java.util.List;

public interface ProjectReport {

	ClassReport getMainClass();
	
	List<ClassReport> getAllClasses();
	
	ClassReport getClassReport(String fullClassName);
}
