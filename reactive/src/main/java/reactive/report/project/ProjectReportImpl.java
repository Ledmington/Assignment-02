package reactive.report.project;

import reactive.report.classes.ClassReport;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProjectReportImpl implements ProjectReport {

    private final Map<String, ClassReport> reports;
    private final String mainClass;

    public ProjectReportImpl(final String mainClass, final Map<String, ClassReport> reports) {
        Objects.requireNonNull(mainClass);
        Objects.requireNonNull(reports);

        this.reports = reports;
        this.mainClass = mainClass;
    }

    @Override
    public ClassReport getMainClass() {
        return reports.get(mainClass);
    }

    @Override
    public List<ClassReport> getAllClasses() {
        return reports.values().stream().toList();
    }

    @Override
    public ClassReport getClassReport(String fullClassName) {
        return reports.get(fullClassName);
    }

}
