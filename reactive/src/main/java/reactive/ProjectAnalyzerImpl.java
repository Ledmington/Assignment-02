package reactive;

import java.io.FileNotFoundException;

import io.reactivex.subjects.SingleSubject;
import reactive.report.classes.ClassReport;
import reactive.report.interfaces.InterfaceReport;
import reactive.report.packages.PackageReport;
import reactive.report.project.ProjectReport;

public class ProjectAnalyzerImpl implements ProjectAnalyzer {

    @Override
    public SingleSubject<InterfaceReport> getInterfaceReport(String srcInterfacePath) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SingleSubject<ClassReport> getClassReport(String srcClassPath) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SingleSubject<PackageReport> getPackageReport(String srcPackagePath) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SingleSubject<ProjectReport> getProjectReport(String srcProjectFolderPath) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SingleSubject<Integer> analyzeProject(String srcProjectFolderName) throws FileNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

}
