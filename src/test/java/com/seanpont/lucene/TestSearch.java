package com.seanpont.lucene;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class TestSearch {

    
    public static void main(String[] args) throws Exception {
        String indexDir = "C:/workspace/learn-lucene/src/main/resources/index";
        String dataDir = "C:/workspace/learn-lucene/src/main/resources/docs";

        String query = "christmas";
        search(indexDir, query);
        
    }

    private static void search(String indexDir, String q) throws IOException, ParseException {
        //open index directory
        Directory dir = FSDirectory.open(new File(indexDir));
        IndexReader reader = IndexReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
        
        QueryParser parser = new QueryParser(
                Version.LUCENE_36, "contents", 
                new StandardAnalyzer(Version.LUCENE_36));
        Query query = parser.parse(q);
        
//        TermQuery query = new TermQuery(new Term("contents", "house"));
        
        long start = System.currentTimeMillis();
        TopDocs hits = searcher.search(query, 3);
        long end = System.currentTimeMillis();
        
        System.out.println("query took "+(end-start)+"millis");
        
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            System.out.println(doc.get("fullpath") + " score: "+scoreDoc.score); 
            
        }
        
        
    }
}
