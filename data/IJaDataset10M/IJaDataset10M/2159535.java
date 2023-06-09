package com.sodad.weka.core.tokenizers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;
import com.sodad.weka.core.OptionHandler;
import com.sodad.weka.core.RevisionHandler;

/**
 * A superclass for all tokenizer algorithms.
 * 
 * @author  FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision: 5953 $
 */
public abstract class Tokenizer implements Enumeration, OptionHandler, Serializable, RevisionHandler {

    /**
   * Returns a string describing the stemmer
   * 
   * @return 		a description suitable for displaying in the 
   * 			explorer/experimenter gui
   */
    public abstract String globalInfo();

    /**
   * Returns an enumeration of all the available options..
   *
   * @return 		an enumeration of all available options.
   */
    public Enumeration listOptions() {
        return (new Vector()).elements();
    }

    /**
   * Gets the current option settings for the OptionHandler.
   *
   * @return 		the list of current option settings as an array of 
   * 			strings
   */
    public String[] getOptions() {
        return new String[0];
    }

    /**
   * Sets the OptionHandler's options using the given list. All options
   * will be set (or reset) during this call (i.e. incremental setting
   * of options is not possible).
   *
   * @param options 	the list of options as an array of strings
   * @throws Exception 	if an option is not supported
   */
    public void setOptions(String[] options) throws Exception {
    }

    /**
   * Tests if this enumeration contains more elements.
   * 
   * @return 		true if and only if this enumeration object contains 
   * 			at least one more element to provide; false otherwise.
   */
    public abstract boolean hasMoreElements();

    /**
   * Returns the next element of this enumeration if this enumeration object 
   * has at least one more element to provide.
   * 
   * @return		the next element of this enumeration.
   */
    public abstract Object nextElement();

    /**
   * Sets the string to tokenize. Tokenization happens immediately.
   * 
   * @param s		the string to tokenize
   */
    public abstract void tokenize(String s);

    /**
   * initializes the given tokenizer with the given options and runs the
   * tokenizer over all the remaining strings in the options array. If no 
   * strings remained in the option string then data is read from stdin, line 
   * by line.
   * 
   * @param tokenizer	the tokenizer to use
   * @param options	the options for the tokenizer
   * @return		the tokenized strings
   * @throws Exception	if setting of options or tokenization fails
   */
    public static String[] tokenize(Tokenizer tokenizer, String[] options) throws Exception {
        Vector<String> result;
        Vector<String> tmpResult;
        Vector<String> data;
        int i;
        boolean processed;
        BufferedReader reader;
        String line;
        result = new Vector<String>();
        tokenizer.setOptions(options);
        data = new Vector<String>();
        processed = false;
        for (i = 0; i < options.length; i++) {
            if (options[i].length() != 0) {
                processed = true;
                data.add(options[i]);
            }
        }
        if (!processed) {
            reader = new BufferedReader(new InputStreamReader(System.in));
            while ((line = reader.readLine()) != null) {
                data.add(line);
            }
        }
        for (i = 0; i < data.size(); i++) {
            tmpResult = new Vector<String>();
            tokenizer.tokenize(data.get(i));
            while (tokenizer.hasMoreElements()) tmpResult.add((String) tokenizer.nextElement());
            result.addAll(tmpResult);
        }
        return result.toArray(new String[result.size()]);
    }

    /**
   * initializes the given tokenizer with the given options and runs the
   * tokenizer over all the remaining strings in the options array. The 
   * generated tokens are then printed to stdout. If no strings remained
   * in the option string then data is read from stdin, line by line.
   * 
   * @param tokenizer	the tokenizer to use
   * @param options	the options for the tokenizer
   */
    public static void runTokenizer(Tokenizer tokenizer, String[] options) {
        String[] result;
        int i;
        try {
            result = tokenize(tokenizer, options);
            for (i = 0; i < result.length; i++) System.out.println(result[i]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
