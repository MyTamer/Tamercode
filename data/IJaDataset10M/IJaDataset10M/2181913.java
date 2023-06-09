package rdf;

import genj.gedcom.Gedcom;
import genj.io.GedcomReaderFactory;
import genj.util.Origin;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import rdf.ReportRdf.Extension;

public class GedcomToRdfConverter {

    private static final String LS = System.getProperty("line.separator");

    private static final String HELP = "=== OPTIONS === " + Arrays.deepToString(Option.values()) + LS + "rules  : filename or number of query.rules section in " + ReportRdf.class.getSimpleName() + ".properties" + LS + "         2 includes ancestors and descendants but may take minutes" + LS + "format : default ttl; possible values: " + Arrays.deepToString(Extension.values()) + LS + "uri    : default for the options FAM, INDI, OBJE, NOTE, REPO, SOUR, SUBM" + LS + "         default for uri is " + ReportRdf.DEFAULT_URI + LS + "gedcom : filename, preceding options are applied to the conversion";

    private static enum Option {

        rules, format, uri, FAM, INDI, OBJE, NOTE, REPO, SOUR, SUBM, gedcom
    }

    ;

    public static void main(final String[] args) throws Exception {
        if (args == null || args.length == 0) throw createException("missing arguments");
        final ReportRdf reporter = new ReportRdf();
        String language = Extension.ttl.getLanguage();
        Logger.getLogger("").setLevel(Level.OFF);
        for (int i = 0; i < args.length; i++) {
            final Option option = toOption(args[i]);
            final String value = args[++i];
            switch(option) {
                case format:
                    language = toLanguage(value);
                    break;
                case gedcom:
                    reporter.convert(readGedcom(value)).write(System.out, language);
                    break;
                case rules:
                    reporter.queries.qRules = value;
                    break;
                case FAM:
                    reporter.uriFormats.fam = value;
                    break;
                case INDI:
                    reporter.uriFormats.indi = value;
                    break;
                case OBJE:
                    reporter.uriFormats.obje = value;
                    break;
                case NOTE:
                    reporter.uriFormats.note = value;
                    break;
                case REPO:
                    reporter.uriFormats.repo = value;
                    break;
                case SOUR:
                    reporter.uriFormats.sour = value;
                    break;
                case SUBM:
                    reporter.uriFormats.subm = value;
                    break;
                case uri:
                    reporter.uriFormats.subm = value;
                    reporter.uriFormats.fam = value;
                    reporter.uriFormats.indi = value;
                    reporter.uriFormats.obje = value;
                    reporter.uriFormats.note = value;
                    reporter.uriFormats.repo = value;
                    reporter.uriFormats.sour = value;
                    break;
            }
        }
    }

    private static IllegalArgumentException createException(final String string) {
        System.err.println(HELP);
        return new IllegalArgumentException(string);
    }

    private static String toLanguage(final String value) {
        try {
            return Extension.valueOf(value).getLanguage();
        } catch (final IllegalArgumentException e) {
            throw createException("invalid value for " + Option.format + ": " + value);
        }
    }

    private static Option toOption(final String string) {
        if (!string.startsWith("-")) {
            throw createException("unkown option: " + string);
        }
        try {
            return Option.valueOf(string.substring(1));
        } catch (final IllegalArgumentException e) {
            throw createException("unkown option: " + string);
        }
    }

    private static Gedcom readGedcom(final String url) throws Exception {
        return GedcomReaderFactory.createReader(Origin.create(new URL(url)), null).read();
    }

    @Test(expected = IllegalArgumentException.class)
    public void noArgs() throws Exception {
        main(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyArgs() throws Exception {
        main(new String[] {});
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongOption() throws Exception {
        main(new String[] { "-" });
    }

    @Test
    public void exampleGedcomToTurtle() throws Exception {
        final String uriPattern = "http://my.domain.com/gedcom/{0}.html";
        final String url = new File("gedcom/example.ged").toURI().toURL().toString();
        final String setOfRules = "2";
        main(new String[] { "-rules", setOfRules, "-uri", uriPattern, "-gedcom", url });
    }
}
