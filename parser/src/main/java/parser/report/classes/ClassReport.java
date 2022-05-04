package parser.report.classes;

import parser.info.FieldInfo;
import parser.info.MethodInfo;
import parser.info.ProjectElem;

import java.util.List;

public interface ClassReport extends ProjectElem {

	String getFullClassName();
	
	String getSrcFullFileName();

	List<MethodInfo> getMethodsInfo();

	List<FieldInfo> getFieldsInfo();

	static ClassReportBuilder builder() {
		return new ClassReportBuilder();
	}
}
