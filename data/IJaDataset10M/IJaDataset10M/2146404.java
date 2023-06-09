package gnu.javax.swing.text.html.parser.support.low;

import java.io.IOException;
import java.io.Reader;

/**
 * Reader splits the input char sequence into tokens.
 * @author Audrius Meskauskas, Lithuania (AudriusA@Bioinformatics.org)
 */
public class ReaderTokenizer extends Constants {

    /**
   * This is set to true each time the getNextToken is called.
   * Used in preventing loops when all patterns refuse to accept
   * the invalid input.
   */
    protected boolean advanced;

    /**
   * If true, the returned tokens are also placed in the backup
   * queue.
   */
    protected boolean backupMode;

    /**
   * The buffer to read document into.
   */
    Buffer buffer = new Buffer();

    /**
   * The queue for supporting mark().
   */
    Queue backup = new Queue();

    /**
   * The queue of found tokens.
   */
    Queue queue = new Queue();

    /**
   * The reader to read the document from.
   */
    Reader reader;

    /**
   * Array of char tokens
   */
    char[] charTokens;

    /**
   * Array of string tokens.
   */
    String[] stringTokens;

    /**
   * The current reader position.
   */
    int readerPosition = -1;

    /**
   * Creates a new ReaderTokenizer. The reset(...) method must be
   * subsequently called to set the reader.
   */
    public ReaderTokenizer() {
    }

    /**
   * Return the sequence, used to separate lines in the document.
   * @return one of \n, \r or \r\n.
   */
    public String getEndOfLineSequence() {
        return buffer.getEndOfLineSequence();
    }

    /**
   * Get the next token.
   * @return
   */
    public Token getNextToken() {
        Token rt;
        advanced = true;
        try {
            if (queue.isEmpty()) read(1);
            if (!queue.isEmpty()) rt = queue.next(); else rt = new Token(EOF, new Location(readerPosition));
        } catch (IOException ex) {
            throw new ParseException("IO Exception", ex);
        }
        if (backupMode) backup.add(rt);
        return rt;
    }

    /**
   * Get a token, lying the given number of tokens
   * ahead. getToken(0) will return the same token,
   * what would be returned by getNextToken().
   * getToken(..) does change the current position
   * in the input stream. If the end of stream is
   * reached, the EOF token is always returned.
   */
    public Token getTokenAhead(int ahead) {
        try {
            read(ahead - queue.size() + 1);
            return queue.size() >= ahead ? queue.get(ahead) : eofToken();
        } catch (IOException ex) {
            throw new ParseException("IO Exception", ex);
        }
    }

    /**
   * Get a token, bein immediatley ahead.
   * If the end of stream is
   * reached, the EOF token is always returned.
   * The method is equivalent calling getTokenAhead(0).
   */
    public Token getTokenAhead() {
        try {
            if (queue.isEmpty()) read(1);
            if (!queue.isEmpty()) return queue.get(0); else return eofToken();
        } catch (IOException ex) {
            throw new ParseException("IO Exception", ex);
        }
    }

    /**
   * Invokes the error handler.
   */
    public void error(String msg, Token at) {
        System.out.println(msg);
    }

    /**
   * Turns the backup mode on or off.
   * It is possible to return where the mark(true) was last called
   * by calling reset().
   * @param mode True if it is required to save tokens, making
   * returning to the current point possible.
   */
    public void mark(boolean mode) {
        backup.clear();
        backupMode = mode;
    }

    /**
   * Prepare for new parsing from the given stream.
   * @param a_reader A reader to parse from.
   */
    public void reset(Reader a_reader) {
        reader = a_reader;
        readerPosition = -1;
        buffer.reset();
        queue.clear();
    }

    /**
   * Reset the internal cursor to the position where the mark()
   * was last time called. Switches the backup mode off.
   */
    public void reset() {
        if (!backupMode) throw new AssertionError("Call mark(true) before using reset()!");
        backupMode = false;
        while (!queue.isEmpty()) backup.add(queue.next());
        Queue t = queue;
        queue = backup;
        backup = t;
        backup.clear();
    }

    /**
   * Read the given number of the tokens. Add the needed number of EOF
   * tokens if there are no more data in the stream.
   * @param amount The number of additional tokens to read.
   */
    void read(int numberOfTokens) throws IOException {
        if (numberOfTokens <= 0) return;
        reading: for (int i = 0; i < numberOfTokens; i++) readToken();
    }

    /**
   * Read next token from the reader, add it to the queue
   */
    void readToken() throws IOException {
        Token t;
        int ch;
        enlarging: while (true) {
            t = tokenMatches();
            if (t != null) break enlarging; else {
                ch = reader.read();
                readerPosition++;
                if (ch == ETX) ch = ' ';
                if (ch < 0) {
                    if (buffer.length() == 0) {
                        queue.add(eofToken());
                        return;
                    } else {
                        if (buffer.charAt(buffer.length() - 1) != ETX) buffer.append(ETX, readerPosition++); else {
                            buffer.setLength(buffer.length() - 1);
                            if (buffer.length() > 0) {
                                t = new Token(OTHER, buffer.toString(), buffer.getLocation(0, buffer.length()));
                                queue.add(t);
                                buffer.setLength(0);
                            }
                            return;
                        }
                    }
                } else buffer.append((char) ch, readerPosition);
            }
        }
    }

    /**
   * Check if the end of buffer matches one of the tokens. If it does,
   * return this token and remove the token sequence from the end of
   * buffer.
   * @return The matching token.
   */
    Token tokenMatches() {
        Token rt = endMatches(buffer);
        if (rt != null) {
            if (rt.kind == ENTITY) {
                if (buffer.charAt(buffer.length() - 1) == ';') buffer.setLength(buffer.length() - rt.getImage().length() - 1); else {
                    error("Missing closing semicolon for entity '" + rt.getImage() + "'", rt);
                    consumeBuffer(rt);
                }
            } else {
                consumeBuffer(rt);
            }
        }
        if (rt != null) {
            if (buffer.length() > 1) {
                String rest = buffer.toString();
                rest = rest.substring(0, rest.length() - 1);
                Token other = new Token(OTHER, rest, buffer.getLocation(0, buffer.length));
                queue.add(other);
                consumeBuffer(other);
            }
            queue.add(rt);
        }
        return rt;
    }

    private void consumeBuffer(Token rt) {
        buffer.delete(buffer.length() - rt.getImage().length() - 1, buffer.length() - 1);
    }

    /**
   * Create EOF token.
   */
    private Token eofToken() {
        return new Token(EOF, "#", new Location(readerPosition));
    }
}
