package org.unitils.dbmaintainer.script.impl;

import org.unitils.core.UnitilsException;
import org.unitils.dbmaintainer.script.ScriptParser;
import org.unitils.dbmaintainer.script.StatementBuilder;
import org.unitils.dbmaintainer.script.parsingstate.ParsingState;
import org.unitils.dbmaintainer.script.parsingstate.impl.*;
import org.unitils.util.PropertyUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Properties;

/**
 * A class for parsing statements out of sql scripts.
 * <p/>
 * All statements should be separated with a semicolon (;). The last statement will be
 * added even if it does not end with a semicolon. The semicolons will not be included in the returned statements.
 * <p/>
 * This parser also takes quoted literals, double quoted text and in-line (--comment) and block (/ * comment * /)
 * into account when parsing the statements.
 *
 * @author Tim Ducheyne
 * @author Filip Neven
 * @author Stefan Bangels
 */
public class DefaultScriptParser implements ScriptParser {

    /**
     * Property indicating if the characters can be escaped by using backslashes. For example '\'' instead of the standard SQL way ''''.
     */
    public static final String PROPKEY_BACKSLASH_ESCAPING_ENABLED = "org.unitils.dbmaintainer.script.ScriptParser.backSlashEscapingEnabled";

    /**
     * The starting state.
     */
    protected ParsingState initialParsingState;

    /**
     * The current state.
     */
    protected ParsingState currentParsingState;

    /**
     * The current parsed character
     */
    protected int currentChar;

    /**
     * The reader for the script content stream.
     */
    protected Reader scriptReader;

    /**
     * Initializes the parser with the given configuration settings.
     *
     * @param configuration The config, not null
     * @param scriptReader  the script stream, not null
     */
    public void init(Properties configuration, Reader scriptReader) {
        boolean backSlashEscapingEnabled = PropertyUtils.getBoolean(PROPKEY_BACKSLASH_ESCAPING_ENABLED, configuration);
        this.initialParsingState = createInitialParsingState(backSlashEscapingEnabled);
        this.currentParsingState = initialParsingState;
        this.scriptReader = new BufferedReader(scriptReader);
    }

    /**
     * Parses the next statement out of the given script stream.
     *
     * @return the statements, null if no more statements
     */
    public String getNextStatement() {
        try {
            return getNextStatementImpl();
        } catch (IOException e) {
            throw new UnitilsException("Unable to parse next statement out of script.", e);
        }
    }

    /**
     * Actual implementation of getNextStatement.
     *
     * @return the statements, null if no more statements
     */
    protected String getNextStatementImpl() throws IOException {
        currentChar = scriptReader.read();
        if (currentChar == -1) {
            return null;
        }
        char previousChar = 0;
        currentParsingState = initialParsingState;
        StatementBuilder statementBuilder = createStatementBuilder();
        while (currentChar != -1) {
            if (statementBuilder.getLength() == 0 && currentChar <= ' ') {
                currentChar = scriptReader.read();
                continue;
            }
            int nextCharInt = scriptReader.read();
            char nextChar;
            if (nextCharInt == -1) {
                nextChar = 0;
            } else {
                nextChar = (char) nextCharInt;
            }
            currentParsingState = currentParsingState.handleNextChar(previousChar, (char) currentChar, nextChar, statementBuilder);
            previousChar = (char) currentChar;
            currentChar = nextCharInt;
            if (currentParsingState == null) {
                String statement = statementBuilder.createStatement();
                previousChar = 0;
                statementBuilder.clear();
                statementBuilder.setExecutable(false);
                currentParsingState = initialParsingState;
                if (statement != null) {
                    return statement;
                }
            }
        }
        if (statementBuilder.isExecutable()) {
            String finalStatement = statementBuilder.createStatement();
            if (finalStatement != null) {
                throw new UnitilsException("Last statement in script was not ended correctly. Each statement should end with one of " + Arrays.toString(statementBuilder.getTrailingSeparatorCharsToRemove()));
            }
        }
        return null;
    }

    /**
     * Builds the initial parsing state.
     * This will create a normal, in-line-comment, in-block-comment, in-double-quotes and in-single-quotes state
     * and link them together.
     *
     * @param backSlashEscapingEnabled True if a backslash can be used for escaping characters
     * @return The initial parsing state, not null
     */
    protected ParsingState createInitialParsingState(boolean backSlashEscapingEnabled) {
        NormalParsingState normalParsingState = createNormalParsingState();
        InLineCommentParsingState inLineCommentParsingState = createInLineCommentParsingState();
        InBlockCommentParsingState inBlockCommentParsingState = createInBlockCommentParsingState();
        InSingleQuotesParsingState inSingleQuotesParsingState = createInSingleQuotesParsingState();
        InDoubleQuotesParsingState inDoubleQuotesParsingState = createInDoubleQuotesParsingState();
        inLineCommentParsingState.init(normalParsingState);
        inBlockCommentParsingState.init(normalParsingState);
        inSingleQuotesParsingState.init(normalParsingState, backSlashEscapingEnabled);
        inDoubleQuotesParsingState.init(normalParsingState, backSlashEscapingEnabled);
        normalParsingState.init(inLineCommentParsingState, inBlockCommentParsingState, inSingleQuotesParsingState, inDoubleQuotesParsingState, backSlashEscapingEnabled);
        return normalParsingState;
    }

    /**
     * Factory method for the statement builder.
     *
     * @return The statement builder, not null
     */
    protected StatementBuilder createStatementBuilder() {
        return new StatementBuilder();
    }

    /**
     * Factory method for the normal parsing state.
     *
     * @return The normal state, not null
     */
    protected NormalParsingState createNormalParsingState() {
        return new NormalParsingState();
    }

    /**
     * Factory method for the in-line comment (-- comment) parsing state.
     *
     * @return The normal state, not null
     */
    protected InLineCommentParsingState createInLineCommentParsingState() {
        return new InLineCommentParsingState();
    }

    /**
     * Factory method for the in-block comment (/ * comment * /) parsing state.
     *
     * @return The normal state, not null
     */
    protected InBlockCommentParsingState createInBlockCommentParsingState() {
        return new InBlockCommentParsingState();
    }

    /**
     * Factory method for the single quotes ('text') parsing state.
     *
     * @return The normal state, not null
     */
    protected InSingleQuotesParsingState createInSingleQuotesParsingState() {
        return new InSingleQuotesParsingState();
    }

    /**
     * Factory method for the double quotes ("text") literal parsing state.
     *
     * @return The normal state, not null
     */
    protected InDoubleQuotesParsingState createInDoubleQuotesParsingState() {
        return new InDoubleQuotesParsingState();
    }
}
