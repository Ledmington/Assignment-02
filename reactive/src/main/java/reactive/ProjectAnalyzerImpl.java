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
import reactive.report.interfaces.InterfaceReportImpl;
import reactive.report.packages.PackageReport;
import reactive.report.packages.PackageReportBuilder;
import reactive.report.project.ProjectReport;
import reactive.report.project.ProjectReportImpl;
import reactive.utils.Pair;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
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
        return Single.fromCallable(() -> {
            ClassOrInterfaceDeclaration interDecl = getFirstInside(srcInterfacePath);
            String fullInterfaceName = interDecl.getFullyQualifiedName().orElseThrow();
            List<MethodInfo> methodsInfo = collectMethods(interDecl);
            return new InterfaceReportImpl(fullInterfaceName, srcInterfacePath, methodsInfo);
        });
    }

    @Override
    public Single<ClassReport> getClassReport(String srcClassPath) {
        return Single.fromCallable(() -> {
            final ClassOrInterfaceDeclaration decl = getFirstInside(srcClassPath);
            return new ClassReportImpl(
                    decl.getFullyQualifiedName().orElseThrow(),
                    srcClassPath,
                    collectMethods(decl),
                    collectFields(decl));
        });
    }

    private String getPackageNameFromFile(final File file) {
        System.out.println(file.getPath());
        try {
            return new JavaParser()
                    .parse(file)
                    .getResult()
                    .orElseThrow()
                    .getPackageDeclaration()
                    .orElseThrow()
                    .getNameAsString();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Single<PackageReport> getPackageReport(String srcPackagePath) {
        return Single.fromCallable(() -> {
            final PackageReportBuilder prb = PackageReport.builder();
            final List<File> innerFiles = Arrays.stream(Objects.requireNonNull(new File(srcPackagePath).listFiles())).toList();
            prb.fullName(getPackageNameFromFile(innerFiles.get(0)));
            prb.fullFileName(srcPackagePath);

            for (File file : innerFiles) {
                if (file.isDirectory()) continue;
                final ClassOrInterfaceDeclaration decl = getFirstInside(file);

                if (decl.isInterface()) {
                    prb.addInterface(
                            getInterfaceReport(file.getAbsolutePath()).blockingGet()
                    );
                } else if (decl.isClassOrInterfaceDeclaration()) {
                    prb.addClass(
                            getClassReport(file.getAbsolutePath()).blockingGet()
                    );
                } else {
                    throw new Error("Unknown type");
                }
            }
            return prb.build();
        });
    }

    @Override
    public Single<ProjectReport> getProjectReport(String srcProjectFolderPath) {
        Map<String, ClassReport> crm = new ConcurrentHashMap<>();
        return Single.fromCallable(() -> {
            AtomicReference<ClassReport> mainClass = new AtomicReference<>();
            List<Single<ProjectReport>> projRepToWait = new ArrayList<>();
            // Start async computations.
            var packRep = getPackageReport(srcProjectFolderPath);
            Arrays.stream(Objects.requireNonNull(new File(srcProjectFolderPath).listFiles()))
                    .filter(File::isDirectory)
                    .map(f -> getProjectReport(f.getPath()))
                    .peek(projRepToWait::add)
                    .close();

            // Wait for computations and return aggregated results.
            mainClass.set(
                    packRep.blockingGet().getClassReports().stream()
                            .peek(cr -> crm.put(cr.getFullClassName(), cr))
                            .filter(cr -> cr.getMethodsInfo().stream().anyMatch(mi -> mi.getName().equals("main")))
                            .findFirst()
                            .orElse(new ClassReportImpl("null", "", null, null))
            );
            projRepToWait.stream()
                    .map(Single::blockingGet)
                    .forEach(pr -> {
                        if (pr.getMainClass() != null) {
                            mainClass.set(pr.getMainClass());
                        }
                        pr.getAllClasses().forEach(cr -> crm.put(cr.getFullClassName(), cr));
                    });
            return new ProjectReportImpl(mainClass.get().getFullClassName(), crm);
        });
    }

    @Override
    public ConnectableFlowable<Pair<ProjectElement, String>> analyzeProject(String srcProjectFolderName) throws FileNotFoundException {
        return null;
    }

    @Override
    public Single<Void> stopAnalyze() {
        return null;
    }
}
