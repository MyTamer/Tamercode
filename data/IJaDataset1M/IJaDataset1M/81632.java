package com.eaio.stringsearch;

/**
 * Subclasses of MismatchSearch allow for searching with a fixed number of
 * possible errors. Subclasses of this class return a
 * <code>int</code> array with the first <code>int</code> being the position at
 * which the hit occurred and the second <code>int</code> being the number of
 * mismatches at the position.
 * <br><br>
 * Example:
 * <pre>
 * int[] positions = new ShiftOrMismatches().searchString("this is null",
 * "nall", 1);
 * </pre>
 * positions[0] would be 8, positions[1] (the number of mismatches) would be 1.
 * 
 * @author <a href="mailto:jb@eaio.com">Johann Burkard</a>
 * @version 1.2
 */
public abstract class MismatchSearch extends StringSearch {

    /**
  * Constructor for MismatchSearch. Note that it is not required to create
  * multiple instances.
  */
    protected MismatchSearch() {
    }

    /**
  * Pre-process the pattern, allowing <b>zero</b> errors.
  * <br><br>
  * Identical to <code>process(pattern, 0)</code>
  * 
  * @param pattern the <code>byte</code> array containing the pattern, may not
  * be <code>null</code>
  * @see com.eaio.stringsearch.StringSearch#processBytes(byte[])
  * @see #processBytes(byte[], int)
  */
    public final Object processBytes(byte[] pattern) {
        return processBytes(pattern, 0);
    }

    /**
  * Pre-processes the pattern, allowing k errors.
  * 
  * @param pattern the <code>byte</code> array containing the pattern, may not
  * be <code>null</code>
  * @param k the editing distance
  * @return an Object
  */
    public abstract Object processBytes(byte[] pattern, int k);

    /**
  * Pre-processes the pattern, allowing <b>zero</b> errors.
  * <br><br>
  * Identical to <code>process(pattern, 0)</code>.
  * 
  * @param pattern a <code>char</code> array containing the pattern, may not be
  * <code>null</code>
  * @return an Object
  * @see #processChars(char[], int)
  * @see com.eaio.stringsearch.StringSearch#processChars(char[])
  */
    public final Object processChars(char[] pattern) {
        return processChars(pattern, 0);
    }

    /**
  * Pre-processes a <code>char</code> array, allowing k errors.
  * 
  * @param pattern a <code>char</code> array containing the pattern, may not be
  * <code>null</code>
  * @param k the editing distance
  * @return an Object
  */
    public abstract Object processChars(char[] pattern, int k);

    /**
  * Pre-processes a String, allowing k errors. This method should not be used
  * directly because it is implicitly called in the
  * {@link #searchString(String, String)} methods.
  * 
  * @param pattern the String containing the pattern, may not be
  * <code>null</code>
  * @param k the editing distance
  * @return an Object
  */
    public final Object processString(String pattern, int k) {
        return processChars(StringSearch.activeDispatch.charsOf(pattern), k);
    }

    /**
  * @see com.eaio.stringsearch.StringSearch#searchBytes(byte[], int, int, byte[], Object)
  * @see #searchBytes(byte[], int, int, byte[], Object, int)
  */
    public final int searchBytes(byte[] text, int textStart, int textEnd, byte[] pattern, Object processed) {
        return searchBytes(text, textStart, textEnd, pattern, processed, 0)[0];
    }

    /**
  * Returns the position in the text at which the pattern was found. Returns -1
  * if the pattern was not found.
  * 
  * @param text the <code>byte</code> array containing the text, may not be
  * <code>null</code>
  * @param pattern the <code>byte</code> array containing the pattern, may not
  * be <code>null</code>
  * @param k the editing distance
  * @return the position in the text or -1 if the pattern was not found
  * @see #searchBytes(byte[], int, int, byte[], Object, int)
  */
    public final int[] searchBytes(byte[] text, byte[] pattern, int k) {
        return searchBytes(text, 0, text.length, pattern, processBytes(pattern, k), k);
    }

    /**
  * Returns the position in the text at which the pattern was found. Returns -1
  * if the pattern was not found.
  * 
  * @param text the <code>byte</code> array containing the text, may not be
  * <code>null</code>
  * @param pattern the <code>byte</code> array containing the pattern, may not
  * be <code>null</code>
  * @param processed an Object as returned from
  * {@link #processBytes(byte[], int)}, may not be <code>null</code>
  * @param k the editing distance
  * @return the position in the text or -1 if the pattern was not found
  * @see #searchBytes(byte[], int, int, byte[], Object, int)
  */
    public final int[] searchBytes(byte[] text, byte[] pattern, Object processed, int k) {
        return searchBytes(text, 0, text.length, pattern, processed, k);
    }

    /**
  * Returns the position in the text at which the pattern was found. Returns -1
  * if the pattern was not found.
  * 
  * @param text the <code>byte</code> array containing the text, may not be
  * <code>null</code>
  * @param textStart at which position in the text the comparing should start
  * @param pattern the <code>byte</code> array containing the pattern, may not
  * be <code>null</code>
  * @param k the editing distance
  * @return int the position in the text or -1 if the pattern was not found
  * @see #searchBytes(byte[], int, int, byte[], Object, int)
  */
    public final int[] searchBytes(byte[] text, int textEnd, byte[] pattern, int k) {
        return searchBytes(text, 0, textEnd, pattern, processBytes(pattern, k), k);
    }

    /**
  * Returns the position in the text at which the pattern was found. Returns -1
  * if the pattern was not found.
  * 
  * @param text the <code>byte</code> array containing the text, may not be
  * <code>null</code>
  * @param textStart at which position in the text the comparing should start
  * @param pattern the pattern to search for, may not be <code>null</code>
  * @param processed
  * @param k the editing distance
  * @return the position in the text or -1 if the pattern was not found
  * @see #searchBytes(byte[], int, int, byte[], Object, int)
  */
    public final int[] searchBytes(byte[] text, int textEnd, byte[] pattern, Object processed, int k) {
        return searchBytes(text, 0, textEnd, pattern, processed, k);
    }

    /**
  * Returns the position in the text at which the pattern was found. Returns -1
  * if the pattern was not found.
  * 
  * @param text text the <code>byte</code> array containing the text, may not be
  * <code>null</code>
  * @param textStart at which position in the text the comparing should start
  * @param textEnd at which position in the text comparing should stop
  * @param pattern the <code>byte</code> array containing the pattern, may not
  * be <code>null</code>
  * @param k the editing distance
  * @return the position in the text or -1 if the pattern was not found
  * @see #searchBytes(byte[], int, int, byte[], Object, int)
  */
    public final int[] searchBytes(byte[] text, int textStart, int textEnd, byte[] pattern, int k) {
        return searchBytes(text, textStart, textEnd, pattern, processBytes(pattern, k), k);
    }

    /**
  * Returns the position in the text at which the pattern was found. Returns -1
  * if the pattern was not found.
  * 
  * @param text text the <code>byte</code> array containing the text, may not be
  * <code>null</code>
  * @param textStart at which position in the text the comparing should start
  * @param textEnd at which position in the text comparing should stop
  * @param pattern the pattern to search for, may not be <code>null</code>
  * @param processed an Object as returned from
  * {@link #processBytes(byte[], int)}, may not be <code>null</code>
  * @param k the editing distance
  * @return the position in the text or -1 if the pattern was not found
  * @see #processBytes(byte[], int)
  */
    public abstract int[] searchBytes(byte[] text, int textStart, int textEnd, byte[] pattern, Object processed, int k);

    /**
  * Finder for the given pattern in the text, starting at textStart and
  * comparing to at most textEnd, allowing zero errors.
  * 
  * @see StringSearch#searchChars(char[], int, int, char[], Object)
  * @see #processChars(char[], int)
  */
    public final int searchChars(char[] text, int textStart, int textEnd, char[] pattern, Object processed) {
        return searchChars(text, textStart, textEnd, pattern, processed, 0)[0];
    }

    /**
  * Finder for the given pattern in the text, allowing k errors.
  * 
  * @param text the String containing the text, may not be <code>null</code>
  * @param pattern the pattern to search for, may not be <code>null</code>
  * @param k the maximum number of mismatches (the editing distance)
  * @return the position in the text or -1 if the pattern was not found
  * @see #searchChars(char[], int, int, char[], Object, int)
  */
    public final int[] searchChars(char[] text, char[] pattern, int k) {
        return searchChars(text, 0, text.length, pattern, processChars(pattern, k), k);
    }

    /**
  * Finder for the given pattern in the text, allowing k errors.
  * 
  * @param text the String containing the text, may not be <code>null</code>
  * @param pattern the pattern to search for, may not be <code>null</code>
  * @param processed an Object as returned from
  * {@link #processChars(char[], int)} or {@link #processString(String, int)},
  * may not be <code>null</code>
  * @param k the maximum number of mismatches (the editing distance)
  * @return the position in the text or -1 if the pattern was not found
  * @see #searchChars(char[], int, int, char[], Object, int)
  */
    public final int[] searchChars(char[] text, char[] pattern, Object processed, int k) {
        return searchChars(text, 0, text.length, pattern, processed, k);
    }

    /**
  * Finder for the given pattern in the text, starting at textStart, allowing k
  * errors.
  * 
  * @param text the String containing the text, may not be <code>null</code>
  * @param textStart at which position in the text the comparing should start
  * @param pattern the pattern to search for, may not be <code>null</code>
  * @param processed an Object as returned from
  * {@link #processChars(char[], int)} or {@link #processString(String), int},
  * may not be <code>null</code>
  * @param k the maximum number of mismatches (the editing distance)
  * @return the position in the text or -1 if the pattern was not found
  * @see #searchChars(char[], int, int, char[], Object)
  */
    public final int[] searchChars(char[] text, int textStart, char[] pattern, int k) {
        return searchChars(text, textStart, text.length, pattern, processChars(pattern, k), k);
    }

    /**
  * Finder for the given pattern in the text, starting at textStart, allowing k
  * errors.
  * 
  * @param text the String containing the text, may not be <code>null</code>
  * @param textStart at which position in the text the comparing should start
  * @param pattern the pattern to search for, may not be <code>null</code>
  * @param processed an Object as returned from
  * {@link #processChars(char[], int)} or {@link #processString(String, int)},
  * may not be <code>null</code>
  * @param k the maximum number of mismatches (the editing distance)
  * @return the position in the text or -1 if the pattern was not found
  * @see #searchChars(char[], int, int, char[], Object, int)
  */
    public final int[] searchChars(char[] text, int textStart, char[] pattern, Object processed, int k) {
        return searchChars(text, textStart, text.length, pattern, processed, k);
    }

    /**
  * Finder for the given pattern in the text, starting at textStart and
  * comparing to at most textEnd, allowing k errors.
  * 
  * @param text the String containing the text, may not be <code>null</code>
  * @param textStart at which position in the text the comparing should start
  * @param textEnd at which position in the text comparing should stop
  * @param pattern the pattern to search for, may not be <code>null</code>
  * @param k the maximum number of mismatches (the editing distance)
  * @return the position in the text or -1 if the pattern was not found
  */
    public final int[] searchChars(char[] text, int textStart, int textEnd, char[] pattern, int k) {
        return searchChars(text, textStart, textEnd, pattern, processChars(pattern, k), k);
    }

    /**
  * Finder for the given pattern in the text, starting at textStart and
  * comparing to at most textEnd, allowing k errors.
  * 
  * @param text the String containing the text, may not be <code>null</code>
  * @param textStart at which position in the text the comparing should start
  * @param textEnd at which position in the text comparing should stop
  * @param pattern the pattern to search for, may not be <code>null</code>
  * @param processed an Object as returned from
  * {@link #processChars(char[], int)} or {@link #processString(String, int)},
  * may not be <code>null</code>
  * @param k the maximum number of mismatches (the editing distance)
  * @return the position in the text or -1 if the pattern was not found
  */
    public abstract int[] searchChars(char[] text, int textStart, int textEnd, char[] pattern, Object processed, int k);

    /**
  * Convenience method to search for patterns in Strings. Returns the position
  * in the text at which the pattern was found. Returns -1 if the pattern was
  * not found.
  * 
  * @param text the String containing the text, may not be <code>null</code>
  * @param pattern the String containing the pattern, may not be
  * <code>null</code>
  * @param k the maximum number of mismatches (the editing distance)
  * @return the position in the text or -1 if the pattern was not found
  * @see #searchChars(char[], int, int, char[], int)
  */
    public final int[] searchString(String text, String pattern, int k) {
        return searchString(text, 0, text.length(), pattern, k);
    }

    /**
  * Convenience method to search for patterns in Strings. Returns the position
  * in the text at which the pattern was found. Returns -1 if the pattern was
  * not found.
  * 
  * @param text the String containing the text, may not be <code>null</code>
  * @param pattern the String containing the pattern, may not be
  * <code>null</code>
  * @param processed an Object as returned from
  * {@link #processChars(char[], int)} or {@link #processString(String, int)},
  * may not be <code>null</code>
  * @param k the maximum number of mismatches (the editing distance)
  * @return the position in the text or -1 if the pattern was not found
  * @see #searchChars(char[], int, int, char[], Object, int)
  */
    public final int[] searchString(String text, String pattern, Object processed, int k) {
        return searchString(text, 0, text.length(), pattern, processed, k);
    }

    /**
  * Convenience method to search for patterns in Strings. Returns the position
  * in the text at which the pattern was found. Returns -1 if the pattern was
  * not found.
  * 
  * @param text the String containing the text, may not be <code>null</code>
  * @param textStart at which position in the text the comparing should start
  * @param pattern the String containing the pattern, may not be
  * <code>null</code>
  * @param k the maximum number of mismatches (the editing distance)
  * @return the position in the text or -1 if the pattern was not found
  * @see #searchChars(char[], int, int, char[], int)
  */
    public final int[] searchString(String text, int textStart, String pattern, int k) {
        return searchString(text, textStart, text.length(), pattern, k);
    }

    /**
  * Convenience method to search for patterns in Strings. Returns the position
  * in the text at which the pattern was found. Returns -1 if the pattern was
  * not found.
  * 
  * @param text the String containing the text, may not be <code>null</code>
  * @param textStart at which position in the text the comparing should start
  * @param pattern the String containing the pattern, may not be
  * <code>null</code>
  * @param processed an Object as returned from
  * {@link #processChars(char[], int)} or {@link #processString(String, int)},
  * may not be <code>null</code>
  * @param k the maximum number of mismatches (the editing distance)
  * @return the position in the text or -1 if the pattern was not found
  * @see #searchChars(char[], int, int, char[], Object, int)
  */
    public final int[] searchString(String text, int textStart, String pattern, Object processed, int k) {
        return searchString(text, textStart, text.length(), pattern, processed, k);
    }

    /**
  * Convenience method to search for patterns in Strings. Returns the position
  * in the text at which the pattern was found. Returns -1 if the pattern was
  * not found.
  * 
  * @param text the String containing the text, may not be <code>null</code>
  * @param textStart at which position in the text the comparing should start
  * @param textEnd at which position in the text comparing should stop
  * @param pattern the String containing the pattern, may not be
  * <code>null</code>
  * @param k the maximum number of mismatches (the editing distance)
  * @return the position in the text or -1 if the pattern was not found
  * @see #searchChars(char[], int, int, char[], int)
  */
    public final int[] searchString(String text, int textStart, int textEnd, String pattern, int k) {
        return StringSearch.activeDispatch.searchString(text, textStart, textEnd, pattern, k, this);
    }

    /**
  * Convenience method to search for patterns in Strings. Returns the position
  * in the text at which the pattern was found. Returns -1 if the pattern was
  * not found.
  * 
  * @param text the String containing the text, may not be <code>null</code>
  * @param textStart at which position in the text the comparing should start
  * @param textEnd at which position in the text comparing should stop
  * @param pattern the String containing the pattern, may not be
  * <code>null</code>
  * @param processed an Object as returned from
  * {@link #processChars(char[], int)} or {@link #processString(String, int)},
  * may not be <code>null</code>
  * @param k the maximum number of mismatches (the editing distance)
  * @return the position in the text or -1 if the pattern was not found
  * @see #searchChars(char[], int, int, char[], Object, int)
  */
    public final int[] searchString(String text, int textStart, int textEnd, String pattern, Object processed, int k) {
        return StringSearch.activeDispatch.searchString(text, textStart, textEnd, pattern, processed, k, this);
    }
}
