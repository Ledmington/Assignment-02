package reactive;

public enum ProjectElement {
    PACKAGE("packages"),
    CLASS("classes"),
    FIELD("fields"),
    METHOD("methods"),
    INTERFACE("interfaces"),
    METHOD_SIGNATURE("methodSignatures");

    private final String name;

    ProjectElement(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
