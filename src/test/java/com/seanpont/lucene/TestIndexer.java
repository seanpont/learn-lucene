package com.seanpont.lucene;

import com.seanpont.lucene.Indexer.TextFilesFilter;


public class TestIndexer {

    
    public static void main(String[] args) throws Exception {
        
        String indexDir = "C:/workspace/learn-lucene/src/main/resources/index";
        String dataDir = "C:/workspace/learn-lucene/src/main/resources/docs";

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
