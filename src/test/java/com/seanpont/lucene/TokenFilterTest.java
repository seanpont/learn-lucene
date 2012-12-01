package com.seanpont.lucene;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ngram.NGramTokenFilter;
import org.junit.Test;

public class TokenFilterTest {

    @Test
    public void test() {
        TokenStream input = null;
        NGramTokenFilter filter = new NGramTokenFilter(input);
        
    }

}
