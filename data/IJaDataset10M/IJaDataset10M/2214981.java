package org.squin.dataset.query.arq.iterators;

import java.util.Iterator;
import org.openjena.atlas.lib.Closeable;
import com.hp.hpl.jena.sparql.core.Var;
import com.hp.hpl.jena.sparql.engine.QueryIterator;
import com.hp.hpl.jena.sparql.engine.binding.Binding;
import org.squin.dataset.jenacommon.NodeDictionary;
import org.squin.dataset.query.BindingProvenance;
import org.squin.dataset.query.SolutionMapping;
import org.squin.dataset.query.impl.FixedSizeSolutionMappingImpl;
import org.squin.dataset.query.arq.IdBasedExecutionContext;
import org.squin.dataset.query.arq.VarDictionary;

/**
 * This iterator converts {@link Binding}s to {@link SolutionMapping}s.
 *
 * @author Olaf Hartig (hartig@informatik.hu-berlin.de)
 */
public class EncodeBindingsIterator implements Iterator<SolutionMapping>, Closeable {

    protected final NodeDictionary nodeDict;

    protected final VarDictionary varDict;

    /** the input iterator consumed by this one */
    protected final QueryIterator input;

    public EncodeBindingsIterator(QueryIterator input, IdBasedExecutionContext execCxt) {
        this.input = input;
        this.nodeDict = execCxt.nodeDict;
        this.varDict = execCxt.varDict;
    }

    public boolean hasNext() {
        return input.hasNext();
    }

    public SolutionMapping next() {
        Binding curInput = input.next();
        SolutionMapping curOutput = new FixedSizeSolutionMappingImpl(varDict.size());
        Iterator<Var> itVar = curInput.vars();
        while (itVar.hasNext()) {
            Var var = itVar.next();
            curOutput.set(varDict.getId(var), nodeDict.getId(curInput.get(var)), new BindingProvenanceImpl());
        }
        return curOutput;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public void close() {
        input.close();
    }

    static class BindingProvenanceImpl implements BindingProvenance {

        public org.squin.dataset.TraceableTriple getMatchingTriple() {
            return null;
        }

        public org.squin.dataset.query.TriplePattern getMatchedTriplePattern() {
            return null;
        }
    }
}
