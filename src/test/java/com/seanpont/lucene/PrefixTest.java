package com.seanpont.lucene;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.store.LockObtainFailedException;
import org.junit.Before;
import org.junit.Test;

public class PrefixTest {

    String[] urls = new String[] {
            "http://news.google.com/news?pz=1&ned=us&hl=en&output=rss",
            "http://www.ft.com/servicestools/newstracking/rss",
            "http://news.bbc.co.uk/2/hi/help/3223484.stm",
            "http://feeds.feedburner.com/marketwatch/podcasts/MarketWatchNewsBreak",
            "http://www.csmonitor.com/aboutus/p_subscribe.html#print",
            "http://feeds.nytimes.com/",
            "http://www.washingtonpost.com/wp-dyn/rss/index.html?hpid=distribution#politics",
            "http://rss.cnn.com/rss/cnn_freevideo.rss",
            "http://www.addthis.com/feed.php?pub=timecom&h1=http%3A%2F%2Ffeeds2.feedburner.com%2Ftime%2Fcolumnists&t1=",
            "http://www.reuters.com/tools/rss",
            "http://rss.cnn.com/rss/cnn_space.rss",
            "http://www.npr.org/rss/rss.php?id=1007",
            "http://www.scientificamerican.com/page.cfm?section=rss",
            "http://www.nature.com/webfeeds/index.html",
            "http://dsc.discovery.com/news/rss.html",
            "http://pubs.ama-assn.org/misc/rssfeed.dtl?home",
            "http://rssfeeds.webmd.com/rss/rss.aspx?RSSSource=RSS_PUBLIC",
            "http://feeds2.feedburner.com/houstonchronicle/sciguy",
            "http://www.nlm.nih.gov/medlineplus/feeds/news_en.xml",
    };
    
    @Before
    public void setUp() throws CorruptIndexException, LockObtainFailedException, IOException {
        AutoCompleteUrl ac = new AutoCompleteUrl();
        for (String url : urls) {
            ac.index(url);
        }
        
        List<String> suggestTermsFor = ac.suggestTermsFor("w");
        System.out.println(suggestTermsFor);
    }
    
    @Test
    public void test() {

    }

}
