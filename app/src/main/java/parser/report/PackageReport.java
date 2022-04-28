package parser.report;

import parser.info.FieldInfo;
import parser.info.MethodInfo;

import java.util.List;

public interface PackageReport {

	String getFullClassName();
	
	String getSrcFullFileName();

	List<MethodInfo> getMethodsInfo();

	List<FieldInfo> getFieldsInfo();
	
}
