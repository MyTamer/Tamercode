package etch.compiler.opt;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import etch.compiler.EtchGrammarConstants;
import etch.compiler.ParseException;
import etch.compiler.Token;
import etch.compiler.ast.Except;
import etch.compiler.ast.Name;
import etch.compiler.ast.Opt;
import etch.compiler.ast.Struct;

/**
 * Option which sets the string format of a struct or exception.
 */
public class ToString extends Opt {

    /**
	 * Constructs the ToString.
	 *
	 * @param name
	 * @param args
	 * @throws ParseException
	 */
    public ToString(Name name, Token... args) throws ParseException {
        super(name.name, name.token.beginLine);
        addType(Except.class);
        addType(Struct.class);
        if (args.length != 1) throw new ParseException(String.format("ToString accepts one string argument at line %d", name.token.beginLine));
        Token arg = args[0];
        if (arg.kind != EtchGrammarConstants.STR) throw new ParseException(String.format("ToString accepts one string argument at line %d", arg.beginLine));
        try {
            format = parse(arg.image);
        } catch (IOException e) {
            throw new ParseException(String.format("ToString: problem with format string at line %d: %s", arg.beginLine, e.getMessage()));
        }
    }

    private final List<FmtItem> format;

    @Override
    public void dump(String indent) {
        System.out.printf("%s@ToString( %s )\n", indent, format);
    }

    @Override
    public String toString() {
        return String.format("@ToString( %s )", format);
    }

    /**
	 * @return the format for the ToString.
	 */
    public List<FmtItem> getFormat() {
        return format;
    }

    private static List<FmtItem> parse(String s) throws IOException {
        if (s.startsWith("\"")) s = s.substring(1);
        if (s.endsWith("\"")) s = s.substring(0, s.length() - 1);
        List<FmtItem> list = new ArrayList<FmtItem>();
        StringReader rdr = new StringReader(s);
        while (stringItem(list, rdr) && fieldItem(list, rdr)) {
        }
        return list;
    }

    private static boolean stringItem(List<FmtItem> list, StringReader rdr) throws IOException {
        StringBuffer sb = new StringBuffer();
        int c;
        boolean escape = false;
        while ((c = rdr.read()) >= 0) {
            if (!escape) {
                if (c == '{') {
                    break;
                }
                if (c == '\\') {
                    escape = true;
                    continue;
                }
            } else {
                if (c == 't') c = '\t'; else if (c == 'r') c = '\r'; else if (c == 'n') c = '\n'; else if (c == '{') c = '{'; else if (c == '\\') c = '\\'; else throw new IOException("bad escape. expecting one of t, r, n, {, or \\. got '" + ((char) c) + "'");
                escape = false;
            }
            sb.append((char) c);
        }
        if (escape) throw new IOException("partial escape at end of string");
        if (sb.length() > 0) list.add(new StringItem(sb.toString()));
        return c >= 0;
    }

    private static boolean fieldItem(List<FmtItem> list, StringReader rdr) throws IOException {
        StringBuffer sb = new StringBuffer();
        int c;
        while ((c = rdr.read()) >= 0) {
            if (c == '}') break;
            sb.append((char) c);
        }
        if (c != '}') throw new IOException("unclosed field item at end of string");
        if (sb.length() > 0) list.add(new FieldItem(sb.toString()));
        return c >= 0;
    }

    /**
	 * A format item.
	 */
    public interface FmtItem {

        /**
		 * @return the value of the format item.
		 */
        public String value();
    }

    /**
	 * A string format item.
	 */
    public static class StringItem implements FmtItem {

        /**
		 * Constructs the string format item.
		 * @param s the string format item.
		 */
        public StringItem(String s) {
            this.s = s;
        }

        private final String s;

        @Override
        public String toString() {
            return "String: '" + s + "'";
        }

        public String value() {
            return s;
        }
    }

    /**
	 * A field format item.
	 */
    public static class FieldItem implements FmtItem {

        /**
		 * Constructs the field format item.
		 * @param s the name of the field.
		 */
        public FieldItem(String s) {
            this.s = s;
        }

        private final String s;

        @Override
        public String toString() {
            return "Field: " + s;
        }

        public String value() {
            return s;
        }
    }
}
