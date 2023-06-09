package ca.ucalgary.cpsc.ebe.fitClipse.render.testutil;

import junit.framework.TestCase;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest extends TestCase {

    public static void assertMatches(String regexp, String string) {
        assertHasRegexp(regexp, string);
    }

    public static void assertNotMatches(String regexp, String string) {
        assertDoesntHaveRegexp(regexp, string);
    }

    public static void assertHasRegexp(String regexp, String output) {
        Matcher match = Pattern.compile(regexp, Pattern.MULTILINE | Pattern.DOTALL).matcher(output);
        boolean found = match.find();
        if (!found) fail("The regexp <" + regexp + "> was not found.");
    }

    public static void assertDoesntHaveRegexp(String regexp, String output) {
        Matcher match = Pattern.compile(regexp, Pattern.MULTILINE).matcher(output);
        boolean found = match.find();
        if (found) fail("The regexp <" + regexp + "> was found.");
    }

    public static void assertSubString(String substring, String string) {
        if (string.indexOf(substring) == -1) fail("substring '" + substring + "' not found.");
    }

    public static void assertNotSubString(String subString, String string) {
        if (string.indexOf(subString) > -1) fail("unexpected substring found");
    }

    public static String divWithIdAndContent(String id, String expectedDivContent) {
        return "<div.*?id=\"" + id + "\".*?>" + expectedDivContent + "</div>";
    }
}
