package parser;

import parser.view.ParserFrame;

public class AsyncParser {
	public static void main (final String[] args) {
		ProjectAnalyzer pa = new ProjectAnalyzerImpl();
		new ParserFrame(pa);
	}
}
