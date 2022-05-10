package reactive;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.flowables.ConnectableFlowable;
import reactive.report.classes.ClassReport;
import reactive.report.interfaces.InterfaceReport;
import reactive.report.packages.PackageReport;
import reactive.report.project.ProjectReport;

import java.io.FileNotFoundException;

public interface ProjectAnalyzer {

    // TODO: redefine interface.

    /**
     * Async method to retrieve the report about a specific interface,
     * given the full path of the interface source file
     *
     * @param srcInterfacePath Full path of interface source file
     */
    Single<InterfaceReport> getInterfaceReport(String srcInterfacePath);

    /**
     * Async method to retrieve the report about a specific class,
     * given the full path of the class source file
     *
     * @param srcClassPath Full path of class source file
     */
    Single<ClassReport> getClassReport(String srcClassPath);

    /**
     * Async method to retrieve the report about a package,
     * given the full path of the package folder
     *
     * @param srcPackagePath Full path of package folder
     */
    Single<PackageReport> getPackageReport(String srcPackagePath);

    /**
     * Async method to retrieve the report about a project
     * given the full path of the project folder
     *
     * @param srcProjectFolderPath Full path of project folder
     */
    Single<ProjectReport> getProjectReport(String srcProjectFolderPath);

    /**
     * Async function that analyze a project given the full path of the project folder,
     * publishing every element everytime one is found
     *
     * @param srcProjectFolderName Full path of project folder
     */
    ConnectableFlowable<Void> analyzeProject(String srcProjectFolderName) throws FileNotFoundException;

    /**
     * Sync function that retrieve the event bus where topics are going to be published after analyzeProject is called.
     */
    //EventBus getEventBus();

    /**
     * Async function to stop background computations.
     * After calling this method it is guaranteed that any computation running in background will be stopped as soon as possible.
     * Keep in mind that after calling this none of the Future will never be completed.
     * @return A future completed when all computations got stopped.
     */
    Single<Void> stopAnalyze();

}
