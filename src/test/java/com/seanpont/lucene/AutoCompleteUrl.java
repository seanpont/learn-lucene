package com.seanpont.lucene;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenFilter;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenFilter.Side;
import org.apache.lucene.analysis.ngram.NGramTokenFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

/**
 * Search term auto-completer, works for single terms (so use on the last term
 * of the query).
 * <p>
 * Returns more popular terms first.
 */
public final class AutoCompleteUrl {

    private static final Version version = Version.LUCENE_36;
    
    private static final String URL = "url";
    
    private static final String[] STOP_WORDS = new String[] {"www", "com", "org"}; 

    Directory directory;

    private IndexWriter writer;

    public AutoCompleteUrl() throws IOException {
        this.directory = new RAMDirectory();
        
        // use a custom analyzer so we can do EdgeNGramFiltering
        Analyzer analyzer = new Analyzer() {
            
            @Override
            public TokenStream tokenStream(String fieldName, Reader reader) {
                TokenStream result = new StandardTokenizer(version, reader);
//                result = new NGramTokenFilter(result, 1, 5);
                result = new EdgeNGramTokenFilter(result, Side.FRONT,1, 20);
                return result;
            }
            
        };
        IndexWriterConfig config = new IndexWriterConfig(version, analyzer);
        writer = new IndexWriter(directory, config);
    }

    public void index(String url) throws CorruptIndexException, IOException {
        
        url = url.replace("http://", "");
        url = url.substring(0, url.indexOf("/"));
        
        Document doc = new Document();
        doc.add(new Field(URL, url, Field.Store.YES, Index.ANALYZED));
        writer.addDocument(doc);

        writer.commit();
    }
    
    public char suggestNextLetter(String prefix) throws IOException {
        List<String> suggestions = suggestTermsFor(prefix);
        Collections.sort(suggestions);
        System.out.println(suggestions);
        
        return 'a';
    }
    
    public List<String> suggestTermsFor(String term) throws IOException {
        // get the top 5 terms for query
        Query query = new TermQuery(new Term(URL, term));

        IndexReader reader = IndexReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
        
        TopDocs docs = searcher.search(query, 3);
        List<String> suggestions = new ArrayList<String>();
        for (ScoreDoc doc : docs.scoreDocs) {
            suggestions.add(reader.document(doc.doc).get(URL));
        }

        return suggestions;
    }

    public List<String> getTerms() throws CorruptIndexException, IOException {
        IndexReader reader = IndexReader.open(directory);
        TermEnum terms = reader.terms();
        List<String> termList = new ArrayList<String>();
        while (terms.next()) {
            termList.add(terms.term().text());
        }
        
        return termList;
    }


    

}
