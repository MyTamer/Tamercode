package com.dyuproject.protostuff.me;

import com.dyuproject.protostuff.me.StringSerializer.STRING;

/**
 * An IO utility for dealing with raw ascii bytes.
 *
 * @author David Yu
 * @created Nov 19, 2010
 */
public final class NumberParser {

    private NumberParser() {
    }

    /**
     * Parse an ascii int from a raw buffer.
     */
    public static int parseInt(final byte[] buffer, final int start, final int length, final int radix) throws NumberFormatException {
        if (length == 0) throw new NumberFormatException(STRING.deser(buffer, start, length));
        if (buffer[start] == '-') {
            if (length == 1) throw new NumberFormatException(STRING.deser(buffer, start, length));
            return parseInt(buffer, start + 1, length - 1, radix, false);
        }
        return parseInt(buffer, start, length, radix, true);
    }

    static int parseInt(final byte[] buffer, int start, final int length, final int radix, final boolean positive) throws NumberFormatException {
        int max = Integer.MIN_VALUE / radix;
        int result = 0;
        for (int offset = start, limit = start + length; offset < limit; ) {
            int digit = Character.digit((char) buffer[offset++], radix);
            if (digit == -1) {
                throw new NumberFormatException(STRING.deser(buffer, start, length));
            }
            if (max > result) {
                throw new NumberFormatException(STRING.deser(buffer, start, length));
            }
            int next = result * radix - digit;
            if (next > result) {
                throw new NumberFormatException(STRING.deser(buffer, start, length));
            }
            result = next;
        }
        if (positive) {
            result = -result;
            if (result < 0) {
                throw new NumberFormatException(STRING.deser(buffer, start, length));
            }
        }
        return result;
    }

    /**
     * Parse an ascii long from a raw buffer.
     */
    public static long parseLong(final byte[] buffer, final int start, final int length, final int radix) throws NumberFormatException {
        if (length == 0) throw new NumberFormatException(STRING.deser(buffer, start, length));
        if (buffer[start] == '-') {
            if (length == 1) throw new NumberFormatException(STRING.deser(buffer, start, length));
            return parseLong(buffer, start + 1, length - 1, radix, false);
        }
        return parseLong(buffer, start, length, radix, true);
    }

    static long parseLong(final byte[] buffer, final int start, final int length, final int radix, final boolean positive) throws NumberFormatException {
        long max = Long.MIN_VALUE / radix;
        long result = 0;
        for (int offset = start, limit = start + length; offset < limit; ) {
            int digit = Character.digit((char) buffer[offset++], radix);
            if (digit == -1) {
                throw new NumberFormatException(STRING.deser(buffer, start, length));
            }
            if (max > result) {
                throw new NumberFormatException(STRING.deser(buffer, start, length));
            }
            long next = result * radix - digit;
            if (next > result) {
                throw new NumberFormatException(STRING.deser(buffer, start, length));
            }
            result = next;
        }
        if (positive) {
            result = -result;
            if (result < 0) {
                throw new NumberFormatException(STRING.deser(buffer, start, length));
            }
        }
        return result;
    }
}
