package net.sourceforge.chaperon.process.simple.internal;

import java.util.HashMap;
import net.sourceforge.chaperon.common.Decoder;
import net.sourceforge.chaperon.common.SortedCharSet;
import net.sourceforge.chaperon.common.StringSet;
import net.sourceforge.chaperon.model.extended.Grammar;
import net.sourceforge.chaperon.model.extended.Pattern;
import net.sourceforge.chaperon.model.extended.PatternIterator;
import net.sourceforge.chaperon.model.extended.PatternSet;
import org.apache.commons.logging.Log;

/**
 * This class contains a automaton of states.
 *
 * @author <a href="mailto:stephan@apache.org">Stephan Michels</a>
 * @version CVS $Id: Automaton.java,v 1.1 2005/01/07 16:12:48 benedikta Exp $
 */
public class Automaton {

    public State first = null;

    private Grammar grammar;

    private Log log;

    private String[] symbols;

    private PatternSet[] firstSets;

    private PatternSet[] lastSets;

    private boolean[] nullable;

    private HashMap definitions = new HashMap();

    /**
   * Create a new automaton of states, calculated with  the aid of the grammar. The constructor
   * calculates all state  and transitions and combine all states with the same core.
   *
   * @param grammar ExtendedGrammar.
   */
    public Automaton(Grammar grammar) {
        this(grammar, null);
    }

    /**
   * Create a new automaton of states, calculated with  the aid of the grammar. The constructor
   * calculates all state  and transitions and combine all states with the same core.
   *
   * @param grammar ExtendedGrammar
   * @param firstsets First sets.
   * @param log Log, which should be used.
   */
    public Automaton(Grammar grammar, Log log) {
        this.grammar = grammar;
        this.log = log;
        grammar.update();
        if (grammar.getStartSymbol() == null) throw new IllegalArgumentException("Start symbol is not defined");
        State startState = new State(grammar);
        startState.addItem(new Item(grammar.getStartPattern()));
        addState(startState);
        int index = 0;
        for (State state = first; state != null; state = state.next) {
            SortedCharSet limits = new SortedCharSet();
            StringSet nonterminals = new StringSet();
            PatternSet gotoPattern = new PatternSet();
            for (Item item = state.first; item != null; item = item.next) {
                for (PatternIterator i = item.pattern.getSuccessors(); i.hasNext(); ) {
                    Pattern nextPattern = i.next();
                    if (nextPattern.getSymbol() != null) {
                        nonterminals.addString(nextPattern.getSymbol());
                        if (grammar.isNullable(nextPattern.getSymbol())) {
                            for (PatternIterator j = nextPattern.getSuccessors(); j.hasNext(); ) limits.addChar(j.next().getLimits());
                            for (PatternIterator j = nextPattern.getAscendingSuccessors(); j.hasNext(); ) limits.addChar(j.next().getLimits());
                            for (PatternIterator j = nextPattern.getDescendingSuccessors(); j.hasNext(); ) limits.addChar(j.next().getLimits());
                        }
                    }
                    limits.addChar(nextPattern.getLimits());
                }
                for (PatternIterator i = item.pattern.getAscendingSuccessors(); i.hasNext(); ) {
                    Pattern nextPattern = i.next();
                    if (nextPattern.getSymbol() != null) {
                        nonterminals.addString(nextPattern.getSymbol());
                        if (grammar.isNullable(nextPattern.getSymbol())) {
                            for (PatternIterator j = nextPattern.getSuccessors(); j.hasNext(); ) limits.addChar(j.next().getLimits());
                            for (PatternIterator j = nextPattern.getAscendingSuccessors(); j.hasNext(); ) limits.addChar(j.next().getLimits());
                            for (PatternIterator j = nextPattern.getDescendingSuccessors(); j.hasNext(); ) limits.addChar(j.next().getLimits());
                        }
                    }
                    limits.addChar(nextPattern.getLimits());
                }
                for (PatternIterator i = item.pattern.getDescendingSuccessors(); i.hasNext(); ) {
                    Pattern nextPattern = i.next();
                    if (nextPattern.getSymbol() != null) nonterminals.addString(nextPattern.getSymbol());
                    limits.addChar(nextPattern.getLimits());
                }
            }
            if ((limits.getCharCount() >= 1) && (limits.getChar(0) > ' ')) {
                addShiftAction(state, ' ', (char) (limits.getChar(0) - 1));
                addReduceAction(state, ' ', (char) (limits.getChar(0) - 1));
            }
            for (int j = 0; j < limits.getCharCount(); j++) {
                if ((j > 0) && ((limits.getChar(j) - limits.getChar(j - 1)) > 1)) {
                    addShiftAction(state, (char) (limits.getChar(j - 1) + 1), (char) (limits.getChar(j) - 1));
                    addReduceAction(state, (char) (limits.getChar(j - 1) + 1), (char) (limits.getChar(j) - 1));
                }
                addShiftAction(state, limits.getChar(j), limits.getChar(j));
                addReduceAction(state, limits.getChar(j), limits.getChar(j));
            }
            if (limits.getCharCount() >= 1) {
                addShiftAction(state, (char) (limits.getChar(limits.getCharCount() - 1) + 1), '䀀');
                addReduceAction(state, (char) (limits.getChar(limits.getCharCount() - 1) + 1), '䀀');
            }
            if (limits.getCharCount() == 0) {
                addShiftAction(state, ' ', '䀀');
                addReduceAction(state, ' ', '䀀');
            }
            for (int j = 0; j < nonterminals.getStringCount(); j++) addGotoAction(state, nonterminals.getString(j));
        }
    }

    /**
   * Return the state as start point for the calculation.
   *
   * @return Start point for the calculation.
   */
    public State getStartState() {
        return first;
    }

    public Grammar getExtendedGrammar() {
        return grammar;
    }

    /**
   * Add a state to this automaton.
   *
   * @param state State.
   *
   * @return Index of the state in the automaton.
   */
    public State addState(State newState) {
        newState.setExtendedParserAutomaton(this);
        if (first == null) {
            first = newState;
            return newState;
        }
        for (State state = first; state != null; state = state.next) {
            if (state.equals(newState)) return state;
            if (state.next == null) {
                state.next = newState;
                return newState;
            }
        }
        return newState;
    }

    /**
   * Return the index of an state. If the automaton does not contain the state, then return this
   * method will return -1.
   *
   * @param state State, which should be found.
   *
   * @return Index of the state.
   */
    public int indexOf(State foreignState) {
        int index = 0;
        for (State state = first; state != null; state = state.next, index++) if (state.equals(foreignState)) return index;
        return -1;
    }

    /**
   * If this automaton contains the state.
   *
   * @param state State, which should be found.
   *
   * @return True, if the automaton contains the state.
   */
    public boolean contains(State foreignState) {
        for (State state = first; state != null; state = state.next) if (state.equals(foreignState)) return true;
        return false;
    }

    private void addShiftAction(State state, char minimum, char maximum) {
        State newState = new State(grammar);
        for (Item item = state.first; item != null; item = item.next) for (PatternIterator i = item.pattern.getAscendingSuccessors(); i.hasNext(); ) {
            Pattern nextPattern = i.next();
            if (nextPattern.contains(minimum, maximum)) newState.addItem(new Item(nextPattern));
        }
        if (!newState.isEmpty()) {
            newState = addState(newState);
            state.addShiftAction(minimum, maximum, false, newState);
        }
        newState = new State(grammar);
        for (Item item = state.first; item != null; item = item.next) for (PatternIterator i = item.pattern.getSuccessors(); i.hasNext(); ) {
            Pattern nextPattern = i.next();
            if (nextPattern.contains(minimum, maximum)) newState.addItem(new Item(nextPattern));
        }
        if (!newState.isEmpty()) {
            newState = addState(newState);
            state.addShiftAction(minimum, maximum, true, newState);
        }
    }

    private void addGotoAction(State state, String symbol) {
        State newState = new State(grammar);
        for (Item item = state.first; item != null; item = item.next) for (PatternIterator i = item.pattern.getAscendingSuccessors(); i.hasNext(); ) {
            Pattern nextPattern = i.next();
            if (symbol.equals(nextPattern.getSymbol())) newState.addItem(new Item(nextPattern));
        }
        if (!newState.isEmpty()) {
            newState = addState(newState);
            state.addGotoAction(symbol, false, newState);
        }
        newState = new State(grammar);
        for (Item item = state.first; item != null; item = item.next) for (PatternIterator i = item.pattern.getSuccessors(); i.hasNext(); ) {
            Pattern nextPattern = i.next();
            if (symbol.equals(nextPattern.getSymbol())) newState.addItem(new Item(nextPattern));
        }
        if (!newState.isEmpty()) {
            newState = addState(newState);
            state.addGotoAction(symbol, true, newState);
        }
    }

    private void addReduceAction(State state, char minimum, char maximum) {
        for (Item item = state.first; item != null; item = item.next) for (PatternIterator i = item.pattern.getDescendingSuccessors(); i.hasNext(); ) {
            Pattern nextPattern = i.next();
            if (nextPattern.contains(minimum, maximum)) if ((item.pattern != grammar.getStartPattern()) && (item.pattern.getDefinition().isLastPattern(item.pattern))) state.addReduceAction(minimum, maximum, item.pattern.getDefinition().getSymbol(), false);
        }
        for (Item item = state.first; item != null; item = item.next) for (PatternIterator i = item.pattern.getSuccessors(); i.hasNext(); ) {
            Pattern nextPattern = i.next();
            if ((nextPattern.getSymbol() != null) && (grammar.isNullable(nextPattern.getSymbol()))) {
                for (PatternIterator j = nextPattern.getSuccessors(); j.hasNext(); ) {
                    Pattern descendingPattern = j.next();
                    if (descendingPattern.contains(minimum, maximum)) {
                        state.addReduceAction(minimum, maximum, nextPattern.getSymbol(), true);
                        break;
                    }
                }
                for (PatternIterator j = nextPattern.getAscendingSuccessors(); j.hasNext(); ) {
                    Pattern descendingPattern = j.next();
                    if (descendingPattern.contains(minimum, maximum)) {
                        state.addReduceAction(minimum, maximum, nextPattern.getSymbol(), true);
                        break;
                    }
                }
                for (PatternIterator j = nextPattern.getDescendingSuccessors(); j.hasNext(); ) {
                    Pattern descendingPattern = j.next();
                    if (descendingPattern.contains(minimum, maximum)) {
                        state.addReduceAction(minimum, maximum, nextPattern.getSymbol(), true);
                        break;
                    }
                }
            }
        }
        for (Item item = state.first; item != null; item = item.next) for (PatternIterator i = item.pattern.getAscendingSuccessors(); i.hasNext(); ) {
            Pattern nextPattern = i.next();
            if ((nextPattern.getSymbol() != null) && (grammar.isNullable(nextPattern.getSymbol()))) {
                for (PatternIterator j = nextPattern.getSuccessors(); j.hasNext(); ) {
                    Pattern descendingPattern = j.next();
                    if (descendingPattern.contains(minimum, maximum)) {
                        state.addReduceAction(minimum, maximum, nextPattern.getSymbol(), true);
                        break;
                    }
                }
                for (PatternIterator j = nextPattern.getAscendingSuccessors(); j.hasNext(); ) {
                    Pattern descendingPattern = j.next();
                    if (descendingPattern.contains(minimum, maximum)) {
                        state.addReduceAction(minimum, maximum, nextPattern.getSymbol(), true);
                        break;
                    }
                }
                for (PatternIterator j = nextPattern.getDescendingSuccessors(); j.hasNext(); ) {
                    Pattern descendingPattern = j.next();
                    if (descendingPattern.contains(minimum, maximum)) {
                        state.addReduceAction(minimum, maximum, nextPattern.getSymbol(), true);
                        break;
                    }
                }
            }
        }
    }

    /**
   * Return a string representation of the automaton.
   *
   * @return String representation of the automaton.
   */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        for (State state = first; state != null; state = state.next) {
            buffer.append("State ");
            buffer.append(indexOf(state));
            buffer.append(":\n");
            buffer.append(state.toString());
            for (ShiftAction shiftAction = state.getShiftAction(); shiftAction != null; shiftAction = shiftAction.next) {
                buffer.append("Shift  ");
                buffer.append(Decoder.toClass(shiftAction.minimum, shiftAction.maximum));
                buffer.append(" -> State ");
                buffer.append(indexOf(shiftAction.state));
                if (shiftAction.kernel) buffer.append(" (kernel)"); else buffer.append(" (nonkernel)");
                buffer.append("\n");
            }
            for (ReduceAction reduceAction = state.getReduceAction(); reduceAction != null; reduceAction = reduceAction.next) {
                buffer.append("Reduce ");
                buffer.append(reduceAction.symbol);
                buffer.append(" for ");
                buffer.append(Decoder.toClass(reduceAction.minimum, reduceAction.maximum));
                if (reduceAction.empty) buffer.append(" (empty)");
                buffer.append("\n");
            }
            for (GotoAction gotoAction = state.getGotoAction(); gotoAction != null; gotoAction = gotoAction.next) {
                buffer.append("Goto ");
                buffer.append(gotoAction.symbol);
                buffer.append(" -> State ");
                buffer.append(indexOf(gotoAction.state));
                if (gotoAction.kernel) buffer.append(" (kernel)"); else buffer.append(" (nonkernel)");
                buffer.append("\n");
            }
            buffer.append("\n");
        }
        return buffer.toString();
    }
}
