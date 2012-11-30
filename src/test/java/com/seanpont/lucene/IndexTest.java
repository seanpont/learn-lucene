package com.seanpont.lucene;

import java.io.IOException;

import junit.framework.Assert;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.junit.Before;
import org.junit.Test;

public class IndexTest {

    String[] ids = {"1", "2"};
    String[] unindexed = {"Neterlands", "Italy"};
    String[] unstored = {"Amsterdam has lots of bridges", "Venice has lots of canals"};
    String[] text = {"Amsterdam", "Venice"};
    
    private Directory directory;

    @Before
    public void setUp() throws Exception {
        System.out.println("setup");
        directory = new RAMDirectory();
        IndexWriter writer = getWriter();
        
        for (int i=0; i< ids.length; i++) {
            Document doc = new Document();
            doc.add(new Field("id", ids[0], Store.YES, Index.NOT_ANALYZED));
            doc.add(new Field("country", unindexed[i], Store.YES, Index.NO));
            doc.add(new Field("contents", unstored[i], Store.NO, Index.ANALYZED));
            doc.add(new Field("city", text[i], Store.YES, Index.ANALYZED));
            
            writer.addDocument(doc);
        }
        
        writer.close();
        
        
        
    }
    
    private IndexWriter getWriter() throws CorruptIndexException, LockObtainFailedException, IOException {
        Analyzer analyzer = new WhitespaceAnalyzer(Version.LUCENE_36);
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36, analyzer);
        return new IndexWriter(directory, config);
    }

    @Test
    public void test() {

    
    }

}
