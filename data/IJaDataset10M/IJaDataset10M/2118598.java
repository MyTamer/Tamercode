package nu.xom.samples;

import java.io.IOException;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;

/**
 * <p>
 * Demonstrates extracting the content 
 * of particular named elements
 * in a particular context 
 * from an XML document.
 * </p>
 * 
 * @author Elliotte Rusty Harold
 * @version 1.0
 *
 */
public class ExampleLister {

    private static int chapter = 0;

    public static void list(Element root) {
        chapter = 0;
        if (root.getLocalName().equals("chapter")) {
            chapter++;
            exampleNumber = 0;
            list(root);
        } else {
            Elements elements = root.getChildElements();
            for (int i = 0; i < elements.size(); i++) {
                Element child = elements.get(i);
                if (child.getLocalName().equals("chapter")) {
                    chapter++;
                    exampleNumber = 0;
                    findExamples(child);
                } else {
                    list(child);
                }
            }
        }
    }

    private static int exampleNumber = 0;

    private static void findExamples(Element element) {
        Elements elements = element.getChildElements();
        for (int i = 0; i < elements.size(); i++) {
            Element child = elements.get(i);
            if (child.getQualifiedName().equals("example")) {
                printExample(child);
            } else {
                findExamples(child);
            }
        }
    }

    private static void printExample(Element example) {
        exampleNumber++;
        Element title = example.getFirstChildElement("title");
        String caption = "Example " + chapter + "." + exampleNumber + ": " + title.getValue();
        System.out.println(caption);
    }

    public static void main(String[] args) {
        if (args.length <= 0) {
            System.out.println("Usage: java nu.xom.samples.ExampleLister URL");
            return;
        }
        String url = args[0];
        try {
            Builder builder = new Builder();
            Document document = builder.build(args[0]);
            list(document.getRootElement());
        } catch (ParsingException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
            System.out.println("Due to an IOException, the parser could not read " + url);
            System.out.println(ex);
        }
    }
}
