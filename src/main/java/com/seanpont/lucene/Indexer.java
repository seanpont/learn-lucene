package com.seanpont.lucene;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexFileNames;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {

    
    private IndexWriter writer;
    
    public Indexer(String indexDir) throws IOException {
        Directory dir = FSDirectory.open(new File(indexDir));
        
        Analyzer analyzer = null;
        
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer );
        writer = new IndexWriter(dir, config);
    }
    
    public void close() throws IOException {
        writer.close();
    }
    
    
    public int index(String dataDir, FileFilter filter) throws Exception {
        File[] files  = new File(dataDir).listFiles();
        for (File f : files) {
            if (!f.isDirectory() &&)!f.isHidden() &&
            f.exists() &&
            f.canRead() &&
            (filter == null || filter.accept(f))) {
                indexFile(f);
            }
        }
        
        return writer.numDocs();
    }
    
    private static class TextFilesFilter implements FileFilter {
        public boolean accept(File path) {
            return path.getName().toLowerCase().endsWith(".txt");
        }
    }
    
    protected Document getDocument(File f) throws Exception {
        Document doc = new Document();
        doc.add(new TextField("contents", new FileReader(f)));
        doc.add(new Field("filename", f.getName(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        
        
        return null;
    }
    
    
    
    public static void main(String[] args) throws Exception {
        //parsing args
        if (args.length != 2) {
            throw new IllegalArgumentException("Usage: java "+Indexer.class.getName() + " <index dir> <data dir>");
        }
        
        String indexDir = args[0];
        String dataDir = args[1];
        long start = System.currentTimeMillis();
        Indexer indexer = new Indexer(indexDir);
        int numIndexed;
        try {
            numIndexed = indexer.index(dataDir, new TextFilesFilter());
        } finally {
            indexer.close();
        }
        
        long end = System.currentTimeMillis();
        
        System.out.println("Indexing "+numIndexed+ " files took "+(end-start) + " milliseconds");
    }
    
}
