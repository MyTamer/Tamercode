package com.memoire.jedit;

import javax.swing.text.Segment;

/**
 * A token marker that splits lines of text into tokens. Each token carries
 * a length field and an indentification tag that can be mapped to a color
 * for painting that token.<p>
 *
 * For performance reasons, the linked list of tokens is reused after each
 * line is tokenized. Therefore, the return value of <code>markTokens</code>
 * should only be used for immediate painting. Notably, it cannot be
 * cached.
 *
 * @author Slava Pestov
 * @version $Id: JEditTokenMarker.java,v 1.2 2006-09-19 14:35:03 deniger Exp $
 *
 * @see org.gjt.sp.jedit.syntax.JEditToken
 */
public abstract class JEditTokenMarker {

    /**
   * A wrapper for the lower-level <code>markTokensImpl</code> method
   * that is called to split a line up into tokens.
   * @param line The line
   * @param lineIndex The line number
   */
    public JEditToken markTokens(Segment line, int lineIndex) {
        if (lineIndex >= length) {
            throw new IllegalArgumentException("JEditTokenizing invalid line: " + lineIndex);
        }
        lastJEditToken = null;
        LineInfo info = lineInfo[lineIndex];
        LineInfo prev;
        if (lineIndex == 0) prev = null; else prev = lineInfo[lineIndex - 1];
        byte oldJEditToken = info.token;
        byte token = markTokensImpl(prev == null ? JEditToken.NULL : prev.token, line, lineIndex);
        info.token = token;
        if (!(lastLine == lineIndex && nextLineRequested)) nextLineRequested = (oldJEditToken != token);
        lastLine = lineIndex;
        addToken(0, JEditToken.END);
        return firstJEditToken;
    }

    /**
   * An abstract method that splits a line up into tokens. It
   * should parse the line, and call <code>addToken()</code> to
   * add syntax tokens to the token list. Then, it should return
   * the initial token type for the next line.<p>
   *
   * For example if the current line contains the start of a
   * multiline comment that doesn't end on that line, this method
   * should return the comment token type so that it continues on
   * the next line.
   *
   * @param token The initial token type for this line
   * @param line The line to be tokenized
   * @param lineIndex The index of the line in the document,
   * starting at 0
   * @return The initial token type for the next line
   */
    protected abstract byte markTokensImpl(byte token, Segment line, int lineIndex);

    /**
   * Returns if the token marker supports tokens that span multiple
   * lines. If this is true, the object using this token marker is
   * required to pass all lines in the document to the
   * <code>markTokens()</code> method (in turn).<p>
   *
   * The default implementation returns true; it should be overridden
   * to return false on simpler token markers for increased speed.
   */
    public boolean supportsMultilineTokens() {
        return true;
    }

    /**
   * Informs the token marker that lines have been inserted into
   * the document. This inserts a gap in the <code>lineInfo</code>
   * array.
   * @param index The first line number
   * @param lines The number of lines
   */
    public void insertLines(int index, int lines) {
        if (lines <= 0) return;
        length += lines;
        ensureCapacity(length);
        int len = index + lines;
        System.arraycopy(lineInfo, index, lineInfo, len, lineInfo.length - len);
        for (int i = index + lines - 1; i >= index; i--) {
            lineInfo[i] = new LineInfo();
        }
    }

    /**
   * Informs the token marker that line have been deleted from
   * the document. This removes the lines in question from the
   * <code>lineInfo</code> array.
   * @param index The first line number
   * @param lines The number of lines
   */
    public void deleteLines(int index, int lines) {
        if (lines <= 0) return;
        int len = index + lines;
        length -= lines;
        System.arraycopy(lineInfo, len, lineInfo, index, lineInfo.length - len);
    }

    /**
   * Returns true if the next line should be repainted. This
   * will return true after a line has been tokenized that starts
   * a multiline token that continues onto the next line.
   */
    public boolean isNextLineRequested() {
        return nextLineRequested;
    }

    /**
   * The first token in the list. This should be used as the return
   * value from <code>markTokens()</code>.
   */
    protected JEditToken firstJEditToken;

    /**
   * The last token in the list. New tokens are added here.
   * This should be set to null before a new line is to be tokenized.
   */
    protected JEditToken lastJEditToken;

    /**
   * An array for storing information about lines. It is enlarged and
   * shrunk automatically by the <code>insertLines()</code> and
   * <code>deleteLines()</code> methods.
   */
    protected LineInfo[] lineInfo;

    /**
   * The length of the <code>lineInfo</code> array.
   */
    protected int length;

    /**
   * The last tokenized line.
   */
    protected int lastLine;

    /**
   * True if the next line should be painted.
   */
    protected boolean nextLineRequested;

    /**
   * Creates a new <code>TokenMarker</code>. This DOES NOT create
   * a lineInfo array; an initial call to <code>insertLines()</code>
   * does that.
   */
    protected JEditTokenMarker() {
        lastLine = -1;
    }

    /**
   * Ensures that the <code>lineInfo</code> array can contain the
   * specified index. This enlarges it if necessary. No action is
   * taken if the array is large enough already.<p>
   *
   * It should be unnecessary to call this under normal
   * circumstances; <code>insertLine()</code> should take care of
   * enlarging the line info array automatically.
   *
   * @param index The array index
   */
    protected void ensureCapacity(int index) {
        if (lineInfo == null) lineInfo = new LineInfo[index + 1]; else if (lineInfo.length <= index) {
            LineInfo[] lineInfoN = new LineInfo[(index + 1) * 2];
            System.arraycopy(lineInfo, 0, lineInfoN, 0, lineInfo.length);
            lineInfo = lineInfoN;
        }
    }

    /**
   * Adds a token to the token list.
   * @param length The length of the token
   * @param id The id of the token
   */
    protected void addToken(int length, byte id) {
        if (id >= JEditToken.INTERNAL_FIRST && id <= JEditToken.INTERNAL_LAST) throw new InternalError("Invalid id: " + id);
        if (length == 0 && id != JEditToken.END) return;
        if (firstJEditToken == null) {
            firstJEditToken = new JEditToken(length, id);
            lastJEditToken = firstJEditToken;
        } else if (lastJEditToken == null) {
            lastJEditToken = firstJEditToken;
            firstJEditToken.length = length;
            firstJEditToken.id = id;
        } else if (lastJEditToken.next == null) {
            lastJEditToken.next = new JEditToken(length, id);
            lastJEditToken = lastJEditToken.next;
        } else {
            lastJEditToken = lastJEditToken.next;
            lastJEditToken.length = length;
            lastJEditToken.id = id;
        }
    }

    /**
   * Inner class for storing information about tokenized lines.
   */
    public class LineInfo {

        /**
     * Creates a new LineInfo object with token = JEditToken.NULL
     * and obj = null.
     */
        public LineInfo() {
        }

        /**
     * Creates a new LineInfo object with the specified
     * parameters.
     */
        public LineInfo(byte token, Object obj) {
            this.token = token;
            this.obj = obj;
        }

        /**
     * The id of the last token of the line.
     */
        public byte token;

        /**
     * This is for use by the token marker implementations
     * themselves. It can be used to store anything that
     * is an object and that needs to exist on a per-line
     * basis.
     */
        public Object obj;
    }
}
