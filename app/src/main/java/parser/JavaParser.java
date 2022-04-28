package parser;

import parser.view.ParserFrame;

public class JavaParser {
	public static void main (final String[] args) {
		ProjectAnalyzer pa = new ProjectAnalyzerImpl();
		ParserFrame gui = new ParserFrame(pa);
	}
}
