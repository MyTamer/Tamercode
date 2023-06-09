package koopa.trees.antlr.filter.filters;

import koopa.trees.antlr.filter.Filter;
import koopa.trees.antlr.filter.FilteringTokenizer;
import org.antlr.runtime.tree.Tree;

public class LiteralFilter implements Filter {

    private String value;

    public LiteralFilter(String value) {
        this.value = value;
    }

    public boolean filter(FilteringTokenizer tokenizer) {
        final Tree upcoming = tokenizer.LA(1);
        if (DEBUG) {
            Log.enter(this);
        }
        if (upcoming == null || !upcoming.getText().equalsIgnoreCase(this.value)) {
            if (DEBUG) {
                Log.log("Wrong value: " + upcoming + ": " + (upcoming == null ? null : upcoming.getText()));
                Log.fail(this);
            }
            return false;
        } else {
            if (DEBUG) {
                Log.log("Correct value: " + upcoming + ": " + (upcoming == null ? null : upcoming.getText()));
                Log.exit(this);
            }
            tokenizer.consume(1);
            return true;
        }
    }

    public String toString() {
        return "\"" + this.value + "\"";
    }
}
