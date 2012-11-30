package com.seanpont.lucene;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * Indexer that indexes .txt files.
 * From Lucene in action, Second Edition
 * @author seanpont
 */
public class Indexer {

    private IndexWriter writer;
    
    public Indexer(String indexDir) throws IOException {
        Directory dir = FSDirectory.open(new File(indexDir));
        
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
        
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36, analyzer);
        writer = new IndexWriter(dir, config);
    }
    
    public void close() throws IOException {
        writer.close();
    }
    
    
    public int index(String dataDir, FileFilter filter) throws Exception {
        File dataFile = new File(dataDir);
        assert dataFile.exists();
        System.out.println(dataFile.getAbsolutePath()); 
        
        File[] files  = dataFile.listFiles();
        for (File f : files) {
            if (!f.isDirectory() &&
                    !f.isHidden() &&
                    f.exists() &&
                    f.canRead() &&
                    (filter == null || filter.accept(f))) {
                indexFile(f);
            }
        }
        
        return writer.numDocs();
    }
    
    public static class TextFilesFilter implements FileFilter {
        public boolean accept(File path) {
            return path.getName().toLowerCase().endsWith(".txt");
        }
    }
    
    protected Document getDocument(File f) throws Exception {
        Document doc = new Document();
        doc.add(new Field("contents", new FileReader(f)));
        doc.add(new Field("filename", f.getName(),          Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field("fullpath", f.getCanonicalPath(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        return doc;
    }
    
    private void indexFile(File f) throws Exception {
        System.out.println("Indexing "+f.getCanonicalPath());
        Document doc = getDocument(f);
        writer.addDocument(doc);
    }
    
    
}
