package org.apache.lucene.search;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.util.PriorityQueue;
import org.apache.lucene.util.ToStringUtils;
import java.io.IOException;

/** Implements the fuzzy search query. The similiarity measurement
 * is based on the Levenshtein (edit distance) algorithm.
 */
public final class FuzzyQuery extends MultiTermQuery {

    public static final float defaultMinSimilarity = 0.5f;

    public static final int defaultPrefixLength = 0;

    private float minimumSimilarity;

    private int prefixLength;

    /**
   * Create a new FuzzyQuery that will match terms with a similarity 
   * of at least <code>minimumSimilarity</code> to <code>term</code>.
   * If a <code>prefixLength</code> &gt; 0 is specified, a common prefix
   * of that length is also required.
   * 
   * @param term the term to search for
   * @param minimumSimilarity a value between 0 and 1 to set the required similarity
   *  between the query term and the matching terms. For example, for a
   *  <code>minimumSimilarity</code> of <code>0.5</code> a term of the same length
   *  as the query term is considered similar to the query term if the edit distance
   *  between both terms is less than <code>length(term)*0.5</code>
   * @param prefixLength length of common (non-fuzzy) prefix
   * @throws IllegalArgumentException if minimumSimilarity is &gt;= 1 or &lt; 0
   * or if prefixLength &lt; 0
   */
    public FuzzyQuery(Term term, float minimumSimilarity, int prefixLength) throws IllegalArgumentException {
        super(term);
        if (minimumSimilarity >= 1.0f) throw new IllegalArgumentException("minimumSimilarity >= 1"); else if (minimumSimilarity < 0.0f) throw new IllegalArgumentException("minimumSimilarity < 0");
        if (prefixLength < 0) throw new IllegalArgumentException("prefixLength < 0");
        this.minimumSimilarity = minimumSimilarity;
        this.prefixLength = prefixLength;
    }

    /**
   * Calls {@link #FuzzyQuery(Term, float) FuzzyQuery(term, minimumSimilarity, 0)}.
   */
    public FuzzyQuery(Term term, float minimumSimilarity) throws IllegalArgumentException {
        this(term, minimumSimilarity, defaultPrefixLength);
    }

    /**
   * Calls {@link #FuzzyQuery(Term, float) FuzzyQuery(term, 0.5f, 0)}.
   */
    public FuzzyQuery(Term term) {
        this(term, defaultMinSimilarity, defaultPrefixLength);
    }

    /**
   * Returns the minimum similarity that is required for this query to match.
   * @return float value between 0.0 and 1.0
   */
    public float getMinSimilarity() {
        return minimumSimilarity;
    }

    /**
   * Returns the non-fuzzy prefix length. This is the number of characters at the start
   * of a term that must be identical (not fuzzy) to the query term if the query
   * is to match that term. 
   */
    public int getPrefixLength() {
        return prefixLength;
    }

    protected FilteredTermEnum getEnum(IndexReader reader) throws IOException {
        return new FuzzyTermEnum(reader, getTerm(), minimumSimilarity, prefixLength);
    }

    public Query rewrite(IndexReader reader) throws IOException {
        FilteredTermEnum enumerator = getEnum(reader);
        int maxClauseCount = BooleanQuery.getMaxClauseCount();
        ScoreTermQueue stQueue = new ScoreTermQueue(maxClauseCount);
        try {
            do {
                float minScore = 0.0f;
                float score = 0.0f;
                Term t = enumerator.term();
                if (t != null) {
                    score = enumerator.difference();
                    if (stQueue.size() < maxClauseCount || score > minScore) {
                        stQueue.insert(new ScoreTerm(t, score));
                        minScore = ((ScoreTerm) stQueue.top()).score;
                    }
                }
            } while (enumerator.next());
        } finally {
            enumerator.close();
        }
        BooleanQuery query = new BooleanQuery(true);
        int size = stQueue.size();
        for (int i = 0; i < size; i++) {
            ScoreTerm st = (ScoreTerm) stQueue.pop();
            TermQuery tq = new TermQuery(st.term);
            tq.setBoost(getBoost() * st.score);
            query.add(tq, BooleanClause.Occur.SHOULD);
        }
        return query;
    }

    public String toString(String field) {
        StringBuffer buffer = new StringBuffer();
        Term term = getTerm();
        if (!term.field().equals(field)) {
            buffer.append(term.field());
            buffer.append(":");
        }
        buffer.append(term.text());
        buffer.append('~');
        buffer.append(Float.toString(minimumSimilarity));
        buffer.append(ToStringUtils.boost(getBoost()));
        return buffer.toString();
    }

    private static class ScoreTerm {

        public Term term;

        public float score;

        public ScoreTerm(Term term, float score) {
            this.term = term;
            this.score = score;
        }
    }

    private static class ScoreTermQueue extends PriorityQueue {

        public ScoreTermQueue(int size) {
            initialize(size);
        }

        protected boolean lessThan(Object a, Object b) {
            ScoreTerm termA = (ScoreTerm) a;
            ScoreTerm termB = (ScoreTerm) b;
            if (termA.score == termB.score) return termA.term.compareTo(termB.term) > 0; else return termA.score < termB.score;
        }
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FuzzyQuery)) return false;
        if (!super.equals(o)) return false;
        final FuzzyQuery fuzzyQuery = (FuzzyQuery) o;
        if (minimumSimilarity != fuzzyQuery.minimumSimilarity) return false;
        if (prefixLength != fuzzyQuery.prefixLength) return false;
        return true;
    }

    public int hashCode() {
        int result = super.hashCode();
        result = 29 * result + minimumSimilarity != +0.0f ? Float.floatToIntBits(minimumSimilarity) : 0;
        result = 29 * result + prefixLength;
        return result;
    }
}
