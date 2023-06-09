package wicket.contrib.tinymce.settings;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.html.WebResource;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringBufferResourceStream;
import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.event.SpellCheckEvent;
import com.swabunga.spell.event.SpellCheckListener;
import com.swabunga.spell.event.SpellChecker;
import com.swabunga.spell.event.StringWordTokenizer;

/**
 * Wicket web resource that acts as backend spell checker for tinymce component.
 * @author ivaynberg
 * @author Iulian Costan (iulian.costan@gmail.com)
 */
class JazzySpellChecker extends WebResource {

    private static final Log log = LogFactory.getLog(JazzySpellChecker.class);

    private static final long serialVersionUID = 1L;

    private static final String dictFile = "wicket/contrib/tinymce/jazzy/english.0";

    private SpellDictionary dict;

    /**
	 * Construct spell checker resource.
	 */
    public JazzySpellChecker() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(dictFile);
        InputStreamReader reader = new InputStreamReader(inputStream);
        try {
            dict = new SpellDictionaryHashMap(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
	 * @see org.apache.wicket.Resource#getResourceStream()
	 */
    public IResourceStream getResourceStream() {
        StringBufferResourceStream resourceStream = new StringBufferResourceStream();
        final String cmd = getParameters().getString("cmd");
        final String lang = getParameters().getString("lang");
        final String id = getParameters().getString("id");
        final String check = getParameters().getString("check");
        if (check == null) {
            handleEmptyCheckList(resourceStream, cmd, id);
        } else if ("spell".equals(cmd)) {
            doSpell(resourceStream, cmd, id, check);
        } else if ("suggest".equals(cmd)) {
            doSuggest(resourceStream, cmd, id, check);
        }
        log.debug("Spellcheck response: " + resourceStream.asString());
        return resourceStream;
    }

    protected void setHeaders(WebResponse response) {
        response.setContentType("text/xml; charset=utf-8");
    }

    private void handleEmptyCheckList(StringBufferResourceStream resourceStream, final String cmd, final String id) {
        respond(null, cmd, id, resourceStream);
    }

    private void doSuggest(StringBufferResourceStream resourceStream, final String cmd, final String id, final String check) {
        final SpellChecker checker = new SpellChecker(dict);
        List suggestions = checker.getSuggestions(check, 2);
        respond(suggestions.iterator(), cmd, id, resourceStream);
    }

    private void doSpell(StringBufferResourceStream resourceStream, final String cmd, final String id, final String check) {
        final SpellChecker checker = new SpellChecker(dict);
        final Set errors = new HashSet();
        checker.addSpellCheckListener(new SpellCheckListener() {

            public void spellingError(SpellCheckEvent event) {
                errors.add(event.getInvalidWord());
            }
        });
        checker.checkSpelling(new StringWordTokenizer(check));
        respond(errors.iterator(), cmd, id, resourceStream);
    }

    private void respond(Iterator words, String cmd, String id, StringBufferResourceStream out) {
        out.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        out.append("\n");
        out.append("<res id=\"");
        out.append(id);
        out.append("\" cmd=\"");
        out.append(cmd);
        out.append("\"");
        if (words == null || words.hasNext() == false) {
            out.append("/>");
            out.append("\n");
        } else {
            out.append(">");
            while (words.hasNext()) {
                out.append(words.next().toString());
                if (words.hasNext()) {
                    out.append(" ");
                }
            }
            out.append("</res>");
        }
    }
}
