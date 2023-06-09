package com.sodad.weka.core.stemmers;

import com.sodad.weka.core.RevisionUtils;

/**
 <!-- globalinfo-start -->
 * A dummy stemmer that performs no stemming at all.
 * <p/>
 <!-- globalinfo-end -->
 * 
 * @author    FracPete (fracpete at waikato dot ac dot nz)
 * @version   $Revision: 5953 $
 */
public class NullStemmer implements Stemmer {

    /** for serialization */
    static final long serialVersionUID = -3671261636532625496L;

    /**
   * Returns a string describing the stemmer
   * @return a description suitable for
   *         displaying in the explorer/experimenter gui
   */
    public String globalInfo() {
        return "A dummy stemmer that performs no stemming at all.";
    }

    /**
   * Returns the word as it is.
   *
   * @param word      the unstemmed word
   * @return          the unstemmed word, again
   */
    public String stem(String word) {
        return new String(word);
    }

    /**
   * returns a string representation of the stemmer
   * 
   * @return a string representation of the stemmer
   */
    public String toString() {
        return getClass().getName();
    }

    /**
   * Returns the revision string.
   * 
   * @return		the revision
   */
    public String getRevision() {
        return RevisionUtils.extract("$Revision: 5953 $");
    }

    /**
   * Runs the stemmer with the given options
   *
   * @param args      the options
   */
    public static void main(String[] args) {
        try {
            Stemming.useStemmer(new NullStemmer(), args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
