package com.seanpont.lucene;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.AnalyzerUtils;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

public class AnalyzerDemo {

    static Version version = Version.LUCENE_36;

    private static final String[] examples = {
        "The quick brown fox jumped over the lazy dog",
        "XY&Z Corporation - xyz@example.com"
    };
    private static final Analyzer[] analyzers = new Analyzer[] {
        new WhitespaceAnalyzer(version),
        new SimpleAnalyzer(version),
        new StopAnalyzer(Version.LUCENE_30),
        new StandardAnalyzer(Version.LUCENE_30)
    };
    public static void main(String[] args) throws IOException {
        String[] strings = examples;
        if (args.length > 0) {
            strings = args;
        }
        for (String text : strings) {
            analyze(text);
        }
    }
    private static void analyze(String text) throws IOException {
        System.out.println("Analyzing \"" + text + "\"");
        for (Analyzer analyzer : analyzers) {
            String name = analyzer.getClass().getSimpleName();
            System.out.println(" " + name + ":");
            System.out.print(" ");
            AnalyzerUtils.displayTokens(analyzer, text);
            System.out.println("\n");
        }
    }
}