package parser;

public enum ProjectElement {
    PACKAGE ("packages"),
    CLASS ("classes"),
    FIELD ("fields"),
    METHOD ("methods"),
    INTERFACE ("interfaces"),
    METHOD_SIGNATURE ("methodSignatures");

    private final String name;

    private ProjectElement(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
