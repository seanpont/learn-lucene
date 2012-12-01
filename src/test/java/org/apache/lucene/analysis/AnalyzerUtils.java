package org.apache.lucene.analysis;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.tokenattributes.TermAttribute;

public class AnalyzerUtils {

    public static void displayTokens(Analyzer analyzer,
            String text) throws IOException {
        displayTokens(analyzer.tokenStream("contents", new StringReader(text)));
    }
    public static void displayTokens(TokenStream stream) throws IOException {
        TermAttribute term = stream.addAttribute(TermAttribute.class);
        while(stream.incrementToken()) {
            System.out.print("[" + term.term() + "] ");
        }
    }

}
