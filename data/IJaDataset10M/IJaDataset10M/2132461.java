package org.bungeni.editor.metadata;

/**
 * Extended metadata model for the debaterecord document type
 * @author undesa
 */
public class DebateRecordMetaModel extends BaseEditorDocMetaModel {

    public DebateRecordMetaModel() {
        super();
    }

    @Override
    public void setup() {
        super.setup();
        this.docMeta.put("BungeniParliamentID", "");
        this.docMeta.put("BungeniParliamentSitting", "");
        this.docMeta.put("BungeniParliamentSession", "");
        this.docMeta.put("BungeniDebateOfficialDate", "");
        this.docMeta.put("BungeniDebateOfficialTime", "");
    }
}
