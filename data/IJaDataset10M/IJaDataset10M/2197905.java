package vqwiki.utils.lucene;

import org.apache.lucene.document.DateField;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import java.io.File;
import java.io.IOException;

/**
 * A utility for making Lucene Documents for HTML documents.
 *
 * @version $Id: HTMLDocument.java 365 2003-10-05 05:07:32Z garethc $
 */
public class HTMLDocument {

    /**
   * TODO: Document this field.
   */
    static char dirSep = System.getProperty("file.separator").charAt(0);

    /**
   *Creates a new HTMLDocument.
   */
    private HTMLDocument() {
    }

    /**
   * TODO: Document this method.
   *
   * @param f TODO: Document this parameter.
   * @return TODO: Document the result.
   * @exception IOException TODO: Document this exception.
   * @exception InterruptedException TODO: Document this exception.
   */
    public static Document Document(File f) throws IOException, InterruptedException {
        Document doc = new Document();
        doc.add(Field.UnIndexed("url", f.getPath().replace(dirSep, '/')));
        doc.add(Field.Keyword("modified", DateField.timeToString(f.lastModified())));
        doc.add(new Field("uid", uid(f), false, true, false));
        HTMLParser parser = new HTMLParser(f);
        doc.add(Field.Text("contents", parser.getReader()));
        doc.add(Field.UnIndexed("summary", parser.getSummary()));
        doc.add(Field.Text("title", parser.getTitle()));
        return doc;
    }

    /**
   * TODO: Document this method.
   *
   * @param f TODO: Document this parameter.
   * @return TODO: Document the result.
   */
    public static String uid(File f) {
        return f.getPath().replace(dirSep, ' ') + " " + DateField.timeToString(f.lastModified());
    }

    /**
   * TODO: Document this method.
   *
   * @param uid TODO: Document this parameter.
   * @return TODO: Document the result.
   */
    public static String uid2url(String uid) {
        String url = uid.replace(' ', '/');
        return url.substring(0, url.lastIndexOf('/'));
    }
}
