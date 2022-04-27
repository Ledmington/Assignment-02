package parser.analyzer.report;

import parser.analyzer.info.FieldInfo;
import parser.analyzer.info.MethodInfo;

import java.util.List;

public interface ClassReport {

	String getFullClassName();
	
	String getSrcFullFileName();

	List<MethodInfo> getMethodsInfo();

	List<FieldInfo> getFieldsInfo();
	
}
