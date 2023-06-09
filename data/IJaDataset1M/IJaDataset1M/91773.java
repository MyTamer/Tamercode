package biologicalObjects.nodes;

import java.util.ArrayList;
import pojos.DBColumn;
import biologicalElements.Elementdeclerations;
import configurations.Wrapper;
import database.dawis.DAWISQueries;
import edu.uci.ics.jung.graph.Vertex;
import graph.jung.graphDrawing.VertexShapes;

public class Enzyme extends Protein {

    private String enzymeClass = "";

    private String sysName = "";

    private String reaction = "";

    private String substrate = "";

    private String produkt = "";

    private String cofactor = "";

    private String reference = "";

    private String effector = "";

    private String orthology = "";

    public Enzyme(String label, String name, Vertex vertex) {
        super(label, name, vertex);
        setBiologicalElement(Elementdeclerations.enzyme);
        shapes = new VertexShapes();
        setShape(shapes.getRegularPolygon(vertex, 3));
        setAbstract(false);
    }

    public String getCofactor() {
        return cofactor;
    }

    public void setCofactor(String cofactor) {
        this.cofactor = cofactor;
    }

    public String getEffector() {
        return effector;
    }

    public void setEffector(String effector) {
        this.effector = effector;
    }

    public String getEnzymeClass() {
        return enzymeClass;
    }

    public void setEnzymeClass(String enzymeClass) {
        this.enzymeClass = enzymeClass;
    }

    public String getOrthology() {
        return orthology;
    }

    public void setOrthology(String orthology) {
        this.orthology = orthology;
    }

    public String getProdukt() {
        return produkt;
    }

    public void setProdukt(String produkt) {
        this.produkt = produkt;
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getSubstrate() {
        return substrate;
    }

    public void setSubstrate(String substrate) {
        this.substrate = substrate;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    @Override
    public void rebuildShape(VertexShapes vs) {
        setShape(vs.getRegularPolygon(getVertex(), 3));
    }

    @Override
    public void lookUpAtAllDatabases() {
        DAWISNode node = getDAWISNode();
        String db = getDB();
        String[] det = { getLabel() };
        ArrayList<DBColumn> results = new ArrayList<DBColumn>();
        if (db.equalsIgnoreCase("KEGG")) {
            results = new Wrapper().requestDbContent(3, DAWISQueries.getTPEnzymeFromKEGGEnzyme, det);
            for (DBColumn column : results) {
                String[] res = column.getColumn();
                String id = res[0];
                node.addID(id, getLabel());
                node.addIDDBRelation("Transpath", id);
            }
        } else if (db.equalsIgnoreCase("Transpath")) {
            results = new Wrapper().requestDbContent(3, DAWISQueries.getKEGGEnzymeFromTPEnzyme, det);
            for (DBColumn column : results) {
                String[] res = column.getColumn();
                String id = res[0];
                node.addID(id, getLabel());
                node.addIDDBRelation("KEGG", id);
            }
        }
    }
}
