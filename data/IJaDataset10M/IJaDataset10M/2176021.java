package org.waveprotocol.wave.model.conversation;

import junit.framework.TestCase;
import org.waveprotocol.wave.model.document.MutableDocumentImpl;
import org.waveprotocol.wave.model.document.raw.impl.Element;
import org.waveprotocol.wave.model.document.raw.impl.Node;
import org.waveprotocol.wave.model.document.raw.impl.Text;
import org.waveprotocol.wave.model.document.util.DocProviders;
import org.waveprotocol.wave.model.util.CollectionUtils;
import java.util.List;

/**
 * Tests for {@link TagsDocument}.
 *
 */
public class TagsDocumentTest extends TestCase {

    /**
   * A simple test Listener which accumulates additions/removals of tags.
   */
    public static class TestListener implements TagsDocument.Listener {

        public List<String> addedTags = CollectionUtils.newArrayList();

        public List<Integer> removedTags = CollectionUtils.newArrayList();

        @Override
        public void onAdd(String tagName) {
            addedTags.add(tagName);
        }

        @Override
        public void onRemove(int tagPosition) {
            removedTags.add(tagPosition);
        }
    }

    private TestListener listener;

    private TagsDocument<?, ?, ?> doc;

    @Override
    protected void setUp() throws Exception {
        listener = new TestListener();
        doc = createDocument("");
        doc.addListener(listener);
    }

    private TagsDocument<?, ?, ?> createDocument(String xmlContent) {
        MutableDocumentImpl<Node, Element, Text> baseDocument = DocProviders.MOJO.parse(xmlContent);
        return new TagsDocument<Node, Element, Text>(baseDocument);
    }

    public void testAddTag() throws Exception {
        doc.addTag("new-tag");
        doc.processInitialState();
        assertTrue(listener.addedTags.contains("new-tag"));
    }

    public void testDeleteTag() throws Exception {
        doc.addTag("new-tag");
        doc.deleteTag(0);
        doc.processInitialState();
        assertTrue(listener.addedTags.isEmpty());
    }

    public void testDeleteTagByName() throws Exception {
        doc.addTag("new-tag");
        doc.deleteTag("new-tag");
        doc.processInitialState();
        assertTrue(listener.addedTags.isEmpty());
    }

    public void testAddTagPosition() throws Exception {
        doc.addTag("new-tag");
        doc.addTag("second-tag", 0);
        doc.processInitialState();
        assertEquals(CollectionUtils.newArrayList("second-tag", "new-tag"), listener.addedTags);
    }
}
