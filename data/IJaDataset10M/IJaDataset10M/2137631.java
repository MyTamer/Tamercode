package libsidutils.stringsearch;

/**
 * An implementation of the BNDM algorithm with wildcards ("don't care"
 * symbols). The wildcard character is initially '?', but any character can be
 * used through the {@link #processChars(char[], char)} and the
 * {@link #processBytes (byte[], byte)} methods. <br>
 * <br>
 * This algorithm is around five times faster than
 * {@link com.eaio.stringsearch.ShiftOrWildcards}. <br>
 * <br>
 * 
 * <pre>
 * Preprocessing: O(2m + &sum;) time
 * </pre>
 * 
 * @see #processBytes(byte[], byte)
 * @see #processChars(char[], char)
 * @see com.eaio.stringsearch.BNDM
 * @author <a href="mailto:jb@eaio.com">Johann Burkard</a>
 * @version 1.2
 */
public class BNDMWildcards extends BNDM {

    /**
	 * The wildcard character (initially '?').
	 */
    public static char wildcard = '?';

    /**
	 * Constructor for BNDMWildcards. Note that it is not required to create
	 * multiple instances.
	 */
    public BNDMWildcards() {
    }

    /**
	 * Pre-processing of the pattern. The pattern may not exceed 32 bytes in
	 * length. If it does, <b>only it's first 32 bytes</b> are processed which
	 * might lead to unexpected results. The wildcard character is obtained from
	 * the static {@link #wildcard} field.
	 * 
	 * @see com.eaio.stringsearch.StringSearch#processBytes(byte[])
	 * @see #processBytes(byte[], byte)
	 */
    @Override
    public Object processBytes(byte[] pattern) {
        return processBytes(pattern, (byte) wildcard);
    }

    /**
	 * Pre-processing of the pattern. The pattern may not exceed 32 bytes in
	 * length. If it does, <b>only it's first 32 bytes</b> are processed which
	 * might lead to unexpected results. Returns an <code>int</code> array.
	 * 
	 * @param pattern
	 *            the <code>byte</code> array containing the pattern, may not be
	 *            <code>null</code>
	 * @param w
	 *            the wildcard <code>byte</code> character
	 * @return an <code>int</code> array
	 */
    public Object processBytes(byte[] pattern, byte w) {
        int j = 0;
        int end = pattern.length < 32 ? pattern.length : 32;
        for (int i = 0; i < end; ++i) {
            if (pattern[i] == w) {
                j |= (1 << end - i - 1);
            }
        }
        int[] b = new int[256];
        if (j != 0) {
            for (int i = 0; i < b.length; i++) {
                b[i] = j;
            }
        }
        j = 1;
        for (int i = end - 1; i >= 0; --i, j <<= 1) {
            b[index(pattern[i])] |= j;
        }
        return b;
    }

    /**
	 * Pre-processes the pattern. The pattern may not exceed 32 characters in
	 * length. If it does, <b>only it's first 32 bytes</b> are processed which
	 * might lead to unexpected results. The wildcard character is obtained from
	 * the static {@link #wildcard} field.
	 * 
	 * @param pattern
	 *            the <code>char</code> array containing the pattern, may not be
	 *            <code>null</code>
	 * @return a {@link CharIntMap}
	 * @see StringSearch#processChars(char[])
	 * @see #processChars(char[], char)
	 */
    @Override
    public Object processChars(char[] pattern) {
        return processChars(pattern, wildcard);
    }

    /**
	 * Pre-processes the pattern. The pattern may not exceed 32 characters in
	 * length. If it does, <b>only it's first 32 bytes</b> are processed which
	 * might lead to unexpected results. Returns a {@link CharIntMap}.
	 * 
	 * @param pattern
	 *            the <code>char</code> array containing the pattern, may not be
	 *            <code>null</code>
	 * @param w
	 *            the wildcard character
	 * @return a {@link CharIntMap}.
	 */
    public Object processChars(char[] pattern, char w) {
        int j = 0;
        int end = pattern.length < 32 ? pattern.length : 32;
        for (int i = 0; i < end; ++i) {
            if (pattern[i] == w) {
                j |= (1 << end - i - 1);
            }
        }
        CharIntMap b = createCharIntMap(pattern, j);
        j = 1;
        for (int i = end - 1; i >= 0; --i, j <<= 1) {
            b.set(pattern[i], b.get(pattern[i]) | j);
        }
        return b;
    }

    /**
	 * Pre-processes the pattern. The pattern may not exceed 32 characters in
	 * length. If it does, <b>only it's first 32 bytes</b> are processed which
	 * might lead to unexpected results. Returns a {@link CharIntMap}.
	 * 
	 * @param pattern
	 *            the String array containing the pattern, may not be
	 *            <code>null</code>
	 * @param w
	 *            the wildcard character
	 * @return a {@link CharIntMap}.
	 */
    public Object processString(String pattern, char w) {
        return processChars(pattern.toCharArray(), w);
    }
}
