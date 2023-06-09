package org.apache.lucene.search.regexp;

import junit.framework.TestCase;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanFirstQuery;

public class TestSpanRegexQuery extends TestCase {

    public void testSpanRegex() throws Exception {
        RAMDirectory directory = new RAMDirectory();
        IndexWriter writer = new IndexWriter(directory, new SimpleAnalyzer(), true);
        Document doc = new Document();
        doc.add(new Field("field", "auto update", false, true, true));
        writer.addDocument(doc);
        doc = new Document();
        doc.add(new Field("field", "first auto update", false, true, true));
        writer.addDocument(doc);
        writer.optimize();
        writer.close();
        IndexSearcher searcher = new IndexSearcher(directory);
        SpanRegexQuery srq = new SpanRegexQuery(new Term("field", "aut.*"));
        SpanFirstQuery sfq = new SpanFirstQuery(srq, 1);
        Hits hits = searcher.search(sfq);
        assertEquals(1, hits.length());
    }
}
