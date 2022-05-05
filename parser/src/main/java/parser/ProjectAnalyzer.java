package parser;

import io.vertx.core.Future;
import parser.report.classes.ClassReport;
import parser.report.interfaces.InterfaceReport;
import parser.report.packages.PackageReport;
import parser.report.project.ProjectReport;

import java.io.FileNotFoundException;

public interface ProjectAnalyzer {

    /**
     * Async method to retrieve the report about a specific interface,
     * given the full path of the interface source file
     *
     * @param srcInterfacePath Full path of interface source file
     */
    Future<InterfaceReport> getInterfaceReport(String srcInterfacePath);

    /**
     * Async method to retrieve the report about a specific class,
     * given the full path of the class source file
     *
     * @param srcClassPath Full path of class source file
     */
    Future<ClassReport> getClassReport(String srcClassPath);

    /**
     * Async method to retrieve the report about a package,
     * given the full path of the package folder
     *
     * @param srcPackagePath Full path of package folder
     */
    Future<PackageReport> getPackageReport(String srcPackagePath);

    /**
     * Async method to retrieve the report about a project
     * given the full path of the project folder
     *
     * @param srcProjectFolderPath Full path of project folder
     */
    Future<ProjectReport> getProjectReport(String srcProjectFolderPath);

    /**
     * Async function that analyze a project given the full path of the project folder,
     * publishing every element everytime one is found
     *
     * @param srcProjectFolderName Full path of project folder
     */
    Future<Integer> analyzeProject(String srcProjectFolderName) throws FileNotFoundException;
}
