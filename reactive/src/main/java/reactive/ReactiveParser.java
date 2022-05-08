package reactive;

import reactive.view.ParserFrame;

public class ReactiveParser {
    public static void main(final String[] args) {
        ProjectAnalyzer pa = new ProjectAnalyzerImpl();
        new ParserFrame(pa);
    }
}
