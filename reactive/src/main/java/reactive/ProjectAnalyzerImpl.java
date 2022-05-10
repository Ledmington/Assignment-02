package reactive;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.flowables.ConnectableFlowable;
import reactive.info.FieldInfo;
import reactive.info.FieldInfoImpl;
import reactive.info.MethodInfo;
import reactive.info.MethodInfoImpl;
import reactive.report.classes.ClassReport;
import reactive.report.classes.ClassReportImpl;
import reactive.report.interfaces.InterfaceReport;
import reactive.report.packages.PackageReport;
import reactive.report.project.ProjectReport;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.function.Function;

public class ProjectAnalyzerImpl implements ProjectAnalyzer {

    private <X, T> List<T> collect(final List<X> fieldsOrMethods, final Function<X, T> mapper) {
        return fieldsOrMethods.stream().map(mapper).toList();
    }

    private List<MethodInfo> collectMethods(final ClassOrInterfaceDeclaration decl) {
        return collect(decl.getMethods(), MethodInfoImpl::new);
    }

    private List<FieldInfo> collectFields(final ClassOrInterfaceDeclaration decl) {
        return collect(decl.getFields(), FieldInfoImpl::new);
    }

    private ClassOrInterfaceDeclaration getFirstInside(final File file) throws FileNotFoundException {
        return new JavaParser()
                .parse(file)
                .getResult()
                .flatMap(result -> result.findFirst(ClassOrInterfaceDeclaration.class))
                .orElseThrow();
    }

    private ClassOrInterfaceDeclaration getFirstInside(final String filePath) throws FileNotFoundException {
        return getFirstInside(new File(filePath));
    }

    @Override
    public Single<InterfaceReport> getInterfaceReport(String srcInterfacePath) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Single<ClassReport> getClassReport(String srcClassPath) {
        return Single.fromCallable(() -> {
            final ClassOrInterfaceDeclaration decl = getFirstInside(srcClassPath);
            return ClassReport.builder()
                    .className(decl.getNameAsString())
                    .fileName(srcClassPath)
                    .build();
        });
    }

    @Override
    public Single<PackageReport> getPackageReport(String srcPackagePath) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Single<ProjectReport> getProjectReport(String srcProjectFolderPath) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ConnectableFlowable<Void> analyzeProject(String srcProjectFolderName) throws FileNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Single<Void> stopAnalyze() {
        return null;
    }
}
