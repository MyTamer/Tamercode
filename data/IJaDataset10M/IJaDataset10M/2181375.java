package xmlOutput.sbml;

import gui.RangeSelector;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import petriNet.ContinuousTransition;
import petriNet.DiscreteTransition;
import petriNet.PNEdge;
import petriNet.Place;
import petriNet.StochasticTransition;
import petriNet.Transition;
import biologicalElements.Pathway;
import biologicalObjects.edges.BiologicalEdgeAbstract;
import biologicalObjects.edges.KEGGEdge;
import biologicalObjects.edges.ReactionPairEdge;
import biologicalObjects.nodes.BiologicalNodeAbstract;
import biologicalObjects.nodes.DAWISNode;
import biologicalObjects.nodes.KEGGNode;
import biologicalObjects.nodes.Protein;

/**
 * @author sebastian and olga
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class VAMLoutput {

    private File file = null;

    private Pathway pw = null;

    private XMLStreamWriter writer;

    @SuppressWarnings("unchecked")
    Hashtable speciesTypeID = new Hashtable();

    @SuppressWarnings("unchecked")
    Hashtable compartments = new Hashtable();

    public VAMLoutput(File file, Pathway pathway) throws IOException {
        this.file = file;
        this.pw = pathway;
        pw.setFilename(file);
        try {
            write();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void getCompartment() throws XMLStreamException {
        Iterator it = pw.getAllNodes().iterator();
        writer.writeStartElement("listOfCompartments");
        int i = 1;
        while (it.hasNext()) {
            BiologicalNodeAbstract bna = (BiologicalNodeAbstract) it.next();
            writer.writeStartElement("compartment");
            writer.writeAttribute("id", "com_" + i);
            writer.writeAttribute("compartmentType", "c_1");
            writer.writeAttribute("size", "190");
            writer.writeEndElement();
            compartments.put(bna.getVertex().toString(), "com_" + i);
            i++;
        }
        writer.writeEndElement();
    }

    private void getCompartmentTypes() throws XMLStreamException {
        writer.writeStartElement("listOfCompartmentTypes");
        writer.writeStartElement("compartmentType");
        writer.writeAttribute("id", "c_1");
        writer.writeAttribute("name", "Cell");
        writer.writeEndElement();
        writer.writeEndElement();
    }

    private void getAnnotation() throws XMLStreamException {
        Iterator it = pw.getAllNodes().iterator();
        writer.writeStartElement("annotations");
        writer.writeStartElement("ListOfSpeciesProperties");
        while (it.hasNext()) {
            BiologicalNodeAbstract bna = (BiologicalNodeAbstract) it.next();
            writer.writeStartElement("speciesProperties");
            writer.writeAttribute("id", "speciesProperties_" + bna.getVertex().toString());
            writer.writeAttribute("species", bna.getVertex().toString());
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeEndElement();
    }

    private void writeProject() throws XMLStreamException {
        writer.writeStartElement("project");
        writer.writeStartElement("isPetriNet");
        writer.writeCData(((Boolean) pw.isPetriNet()).toString());
        writer.writeEndElement();
        writer.writeStartElement("title");
        writer.writeCData(pw.getTitle());
        writer.writeEndElement();
        writer.writeStartElement("organism");
        writer.writeCData(pw.getOrganism());
        writer.writeEndElement();
        writer.writeStartElement("organismSpecification");
        writer.writeCData(pw.getSpecificationAsString());
        writer.writeEndElement();
        writer.writeStartElement("author");
        writer.writeCData(pw.getAuthor());
        writer.writeEndElement();
        writer.writeStartElement("version");
        writer.writeCData(pw.getVersion());
        writer.writeEndElement();
        writer.writeStartElement("date");
        writer.writeCData(pw.getDate());
        writer.writeEndElement();
        writer.writeStartElement("description");
        writer.writeCData(pw.getDescription());
        writer.writeEndElement();
        if (pw.isDAWISProject()) {
            writer.writeStartElement("settings");
            String[] settings = pw.getSettingsAsString();
            for (int i = 0; i < settings.length; i++) {
                writer.writeStartElement("setting");
                writer.writeCData(settings[i]);
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
        writer.writeEndElement();
    }

    @SuppressWarnings("unchecked")
    private void writeKEGGProperties(KEGGNode node) throws XMLStreamException {
        writer.writeStartElement("entryID");
        writer.writeCData(node.getKEGGentryID());
        writer.writeEndElement();
        writer.writeStartElement("entryMap");
        writer.writeCData(node.getKEGGentryMap());
        writer.writeEndElement();
        writer.writeStartElement("entryName");
        writer.writeCData(node.getKEGGentryName());
        writer.writeEndElement();
        writer.writeStartElement("entryType");
        writer.writeCData(node.getKEGGentryType());
        writer.writeEndElement();
        writer.writeStartElement("entryLink");
        writer.writeCData(node.getKEGGentryLink());
        writer.writeEndElement();
        writer.writeStartElement("entryReaction");
        writer.writeCData(node.getKEGGentryReaction());
        writer.writeEndElement();
        writer.writeStartElement("entryComponent");
        writer.writeCData(node.getKEGGComponent());
        writer.writeEndElement();
        writer.writeStartElement("entryPathway");
        writer.writeCData(node.getKEGGPathway());
        writer.writeEndElement();
        writer.writeStartElement("entryComment");
        writer.writeCData(node.getKeggComment());
        writer.writeEndElement();
        writer.writeStartElement("enzymeClass");
        writer.writeCData(node.getKeggenzymeClass());
        writer.writeEndElement();
        writer.writeStartElement("enzymeSysName");
        writer.writeCData(node.getKeggsysName());
        writer.writeEndElement();
        writer.writeStartElement("enzymeReaction");
        writer.writeCData(node.getKeggreaction());
        writer.writeEndElement();
        writer.writeStartElement("enzymeSubstrate");
        writer.writeCData(node.getKeggsubstrate());
        writer.writeEndElement();
        writer.writeStartElement("enzymeProduct");
        writer.writeCData(node.getKeggprodukt());
        writer.writeEndElement();
        writer.writeStartElement("enzymeCoFactor");
        writer.writeCData(node.getKeggcofactor());
        writer.writeEndElement();
        writer.writeStartElement("enzymeReference");
        writer.writeCData(node.getKeggreference());
        writer.writeEndElement();
        writer.writeStartElement("enzymeEffector");
        writer.writeCData(node.getKeggeffector());
        writer.writeEndElement();
        writer.writeStartElement("enzymeOrthology");
        writer.writeCData(node.getKeggorthology());
        writer.writeEndElement();
        writer.writeStartElement("compoundFormular");
        writer.writeCData(node.getCompoundFormula());
        writer.writeEndElement();
        writer.writeStartElement("compoundMass");
        writer.writeCData(node.getCompoundMass());
        writer.writeEndElement();
        writer.writeStartElement("compoundComment");
        writer.writeCData(node.getCompoundComment());
        writer.writeEndElement();
        writer.writeStartElement("compoundRemarks");
        writer.writeCData(node.getCompoundRemarks());
        writer.writeEndElement();
        writer.writeStartElement("compoundAtomsNr");
        writer.writeCData(node.getCompoundAtomsNr());
        writer.writeEndElement();
        writer.writeStartElement("compoundAtoms");
        writer.writeCData(node.getCompoundAtoms());
        writer.writeEndElement();
        writer.writeStartElement("compoundBondNr");
        writer.writeCData(node.getCompoundBondNr());
        writer.writeEndElement();
        writer.writeStartElement("compoundBonds");
        writer.writeCData(node.getCompoundBonds());
        writer.writeEndElement();
        writer.writeStartElement("compoundSequence");
        writer.writeCData(node.getCompoundSequence());
        writer.writeEndElement();
        writer.writeStartElement("compoundModule");
        writer.writeCData(node.getCompoundModule());
        writer.writeEndElement();
        writer.writeStartElement("compoundOrganism");
        writer.writeCData(node.getCompoundOrganism());
        writer.writeEndElement();
        writer.writeStartElement("geneName");
        writer.writeCData(node.getGeneName());
        writer.writeEndElement();
        writer.writeStartElement("geneDefenition");
        writer.writeCData(node.getGeneDefinition());
        writer.writeEndElement();
        writer.writeStartElement("genePosition");
        writer.writeCData(node.getGenePosition());
        writer.writeEndElement();
        writer.writeStartElement("geneCodons");
        writer.writeCData(node.getGeneCodonUsage());
        writer.writeEndElement();
        writer.writeStartElement("geneAAseqNr");
        writer.writeCData(node.getGeneAAseqNr());
        writer.writeEndElement();
        writer.writeStartElement("geneAAseq");
        writer.writeCData(node.getGeneAAseq());
        writer.writeEndElement();
        writer.writeStartElement("geneNtSeqNr");
        writer.writeCData(node.getGeneNtseqNr());
        writer.writeEndElement();
        writer.writeStartElement("geneNtseq");
        writer.writeCData(node.getGeneNtSeq());
        writer.writeEndElement();
        writer.writeStartElement("geneOrthology");
        writer.writeCData(node.getGeneOrthology());
        writer.writeEndElement();
        writer.writeStartElement("geneOrthologyName");
        writer.writeCData(node.getGeneOrthologyName());
        writer.writeEndElement();
        writer.writeStartElement("geneEnzyme");
        writer.writeCData(node.getGeneEnzyme());
        writer.writeEndElement();
        writer.writeStartElement("glycanOrthology");
        writer.writeCData(node.getGlycanOrthology());
        writer.writeEndElement();
        writer.writeStartElement("glycanBracket");
        writer.writeCData(node.getGlycanBracket());
        writer.writeEndElement();
        writer.writeStartElement("glycanComposition");
        writer.writeCData(node.getGlycanComposition());
        writer.writeEndElement();
        writer.writeStartElement("glycanNode");
        writer.writeCData(node.getGlycanNode());
        writer.writeEndElement();
        writer.writeStartElement("glycanEdge");
        writer.writeCData(node.getGlycanEdge());
        writer.writeEndElement();
        writer.writeStartElement("glycanName");
        writer.writeCData(node.getGlycanName());
        writer.writeEndElement();
        writer.writeStartElement("graphicLabel");
        writer.writeCData(node.getNodeLabel());
        writer.writeEndElement();
        writer.writeStartElement("graphic_xPos");
        writer.writeCData(node.getXPos() + "");
        writer.writeEndElement();
        writer.writeStartElement("graphic_yPos");
        writer.writeCData(node.getYPos() + "");
        writer.writeEndElement();
        writer.writeStartElement("graphicShape");
        writer.writeCData(node.getShape());
        writer.writeEndElement();
        writer.writeStartElement("graphicWidth");
        writer.writeCData(node.getWidth());
        writer.writeEndElement();
        writer.writeStartElement("graphicHeight");
        writer.writeCData(node.getHeight());
        writer.writeEndElement();
        writer.writeStartElement("graphicForegroundColour");
        writer.writeCData(node.getForegroundColour());
        writer.writeEndElement();
        writer.writeStartElement("graphicBackgroundColour");
        writer.writeCData(node.getBackgroundColour());
        writer.writeEndElement();
        writer.writeStartElement("allNames");
        Iterator it = node.getAllNamesAsVector().iterator();
        while (it.hasNext()) {
            String element = it.next().toString();
            writer.writeStartElement("name");
            writer.writeCData(element);
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeStartElement("allDBLinks");
        it = node.getAllDBLinksAsVector().iterator();
        while (it.hasNext()) {
            String element = it.next().toString();
            writer.writeStartElement("db");
            writer.writeCData(element);
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeStartElement("allPathways");
        it = node.getAllPathwayLinksAsVector().iterator();
        while (it.hasNext()) {
            String element = it.next().toString();
            writer.writeStartElement("pathway");
            writer.writeCData(element);
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeStartElement("allStructures");
        it = node.getAllStructuresAsVector().iterator();
        while (it.hasNext()) {
            String element = it.next().toString();
            writer.writeStartElement("structure");
            writer.writeCData(element);
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeStartElement("allMotifs");
        it = node.getAllGeneMotifsAsVector().iterator();
        while (it.hasNext()) {
            String element = it.next().toString();
            writer.writeStartElement("motif");
            writer.writeCData(element);
            writer.writeEndElement();
        }
        writer.writeEndElement();
    }

    @SuppressWarnings("unchecked")
    private void writeEdges() throws XMLStreamException {
        Iterator it = pw.getAllEdges().iterator();
        while (it.hasNext()) {
            BiologicalEdgeAbstract bna = (BiologicalEdgeAbstract) it.next();
            writer.writeStartElement("edge");
            writer.writeStartElement("elementSpecification");
            writer.writeCData(bna.getBiologicalElement());
            writer.writeEndElement();
            writer.writeStartElement("from");
            writer.writeCData(bna.getEdge().getEndpoints().getFirst().toString());
            writer.writeEndElement();
            writer.writeStartElement("to");
            writer.writeCData(bna.getEdge().getEndpoints().getSecond().toString());
            writer.writeEndElement();
            writer.writeStartElement("label");
            writer.writeCData(bna.getLabel());
            writer.writeEndElement();
            writer.writeStartElement("name");
            writer.writeCData(bna.getName());
            writer.writeEndElement();
            writer.writeStartElement("reference");
            writer.writeCData(bna.isReference() + "");
            writer.writeEndElement();
            writer.writeStartElement("comment");
            writer.writeCData(bna.getComments());
            writer.writeEndElement();
            writer.writeStartElement("colour");
            writer.writeAttribute("r", bna.getColor().getRed() + "");
            writer.writeAttribute("g", bna.getColor().getGreen() + "");
            writer.writeAttribute("b", bna.getColor().getBlue() + "");
            writer.writeEndElement();
            writer.writeStartElement("isDirected");
            writer.writeCData(bna.isDirected() + "");
            writer.writeEndElement();
            if (bna instanceof PNEdge) {
                PNEdge e = (PNEdge) bna;
                writer.writeStartElement("function");
                writer.writeCData(e.getFunction());
                writer.writeEndElement();
            }
            if (bna.hasKEGGEdge()) {
                writer.writeStartElement("KeggEdge");
                KEGGEdge edge = bna.getKeggEdge();
                writer.writeStartElement("entry1");
                writer.writeCData(edge.getEntry1());
                writer.writeEndElement();
                writer.writeStartElement("entry2");
                writer.writeCData(edge.getEntry2());
                writer.writeEndElement();
                writer.writeStartElement("type");
                writer.writeCData(edge.getType());
                writer.writeEndElement();
                writer.writeStartElement("description");
                writer.writeCData(edge.getDescription());
                writer.writeEndElement();
                writer.writeStartElement("name");
                writer.writeCData(edge.getName());
                writer.writeEndElement();
                writer.writeStartElement("remark");
                writer.writeCData(edge.getRemark());
                writer.writeEndElement();
                writer.writeStartElement("orthology");
                writer.writeCData(edge.getOrthology());
                writer.writeEndElement();
                writer.writeStartElement("reference");
                writer.writeCData(edge.getReference());
                writer.writeEndElement();
                writer.writeStartElement("comment");
                writer.writeCData(edge.getComment());
                writer.writeEndElement();
                writer.writeStartElement("definition");
                writer.writeCData(edge.getDefinition());
                writer.writeEndElement();
                writer.writeStartElement("equation");
                writer.writeCData(edge.getEquation());
                writer.writeEndElement();
                writer.writeStartElement("rpair");
                writer.writeCData(edge.getRpair());
                writer.writeEndElement();
                writer.writeStartElement("KEEGReactionID");
                writer.writeCData(edge.getKEEGReactionID());
                writer.writeEndElement();
                writer.writeStartElement("products");
                Iterator it3 = edge.getAllProducts().iterator();
                while (it3.hasNext()) {
                    String element = it3.next().toString();
                    writer.writeStartElement("product");
                    writer.writeCData(element);
                    writer.writeEndElement();
                }
                writer.writeEndElement();
                writer.writeStartElement("InvolvedEnzyme");
                writer.writeCData(edge.getInvolvedEnzyme());
                writer.writeEndElement();
                writer.writeStartElement("substrates");
                it3 = edge.getAllSubstrates().iterator();
                while (it3.hasNext()) {
                    String element = it3.next().toString();
                    writer.writeStartElement("substrate");
                    writer.writeCData(element);
                    writer.writeEndElement();
                }
                writer.writeEndElement();
                writer.writeEndElement();
            }
            if (bna.hasReactionPairEdge()) {
                writer.writeStartElement("ReactionPairEdge");
                ReactionPairEdge edge = bna.getReactionPairEdge();
                writer.writeStartElement("name");
                writer.writeCData(edge.getName());
                writer.writeEndElement();
                writer.writeStartElement("id");
                writer.writeCData(edge.getReactionPairID());
                writer.writeEndElement();
                writer.writeStartElement("type");
                writer.writeCData(edge.getType());
                writer.writeEndElement();
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
    }

    @SuppressWarnings("unchecked")
    private void writeAnnotation() throws XMLStreamException {
        Iterator it = pw.getAllNodes().iterator();
        writer.writeStartElement("annotation");
        writer.writeStartElement("NetworkEditorSettings");
        writeProject();
        while (it.hasNext()) {
            BiologicalNodeAbstract bna = (BiologicalNodeAbstract) it.next();
            writer.writeStartElement("element");
            writer.writeAttribute("id", bna.getVertex().toString());
            writer.writeAttribute("ElementID", Integer.toString(bna.getID()));
            if (bna instanceof Place) {
                Place p = (Place) bna;
                writer.writeAttribute("token", p.getToken() + "");
                writer.writeAttribute("tokenMin", p.getTokenMin() + "");
                writer.writeAttribute("tokenMax", p.getTokenMax() + "");
                writer.writeAttribute("tokenStart", p.getTokenStart() + "");
            }
            if (bna instanceof Transition) {
                if (bna instanceof DiscreteTransition) {
                    DiscreteTransition t = (DiscreteTransition) bna;
                    writer.writeAttribute("delay", t.getDelay() + "");
                } else if (bna instanceof ContinuousTransition) {
                    ContinuousTransition t = (ContinuousTransition) bna;
                } else if (bna instanceof StochasticTransition) {
                    StochasticTransition t = (StochasticTransition) bna;
                    writer.writeAttribute("distribution", t.getDistribution());
                }
            }
            writer.writeStartElement("coordinates");
            Point2D p = pw.getGraph().getClusteringLayout().getLocation(bna.getVertex());
            writer.writeAttribute("x", p.getX() + "");
            writer.writeAttribute("y", p.getY() + "");
            writer.writeEndElement();
            writer.writeStartElement("elementSpecification");
            writer.writeCData(bna.getBiologicalElement());
            writer.writeEndElement();
            writer.writeStartElement("colour");
            writer.writeAttribute("r", bna.getColor().getRed() + "");
            writer.writeAttribute("g", bna.getColor().getGreen() + "");
            writer.writeAttribute("b", bna.getColor().getBlue() + "");
            writer.writeEndElement();
            writer.writeStartElement("label");
            writer.writeCData(bna.getLabel());
            writer.writeEndElement();
            writer.writeStartElement("name");
            writer.writeCData(bna.getName());
            writer.writeEndElement();
            writer.writeStartElement("reference");
            writer.writeCData(bna.isReference() + "");
            writer.writeEndElement();
            writer.writeStartElement("location");
            writer.writeCData(bna.getCompartment() + "");
            writer.writeEndElement();
            writer.writeStartElement("comment");
            writer.writeCData(bna.getComments());
            writer.writeEndElement();
            if (bna instanceof Protein) {
                Protein protein = (Protein) bna;
                writer.writeStartElement("aaSequence");
                writer.writeCData(protein.getAaSequence());
                writer.writeEndElement();
            }
            if (bna.hasDAWISNode()) {
                writer.writeStartElement("dawisProperties");
                writeDAWISProperties(bna.getDAWISNode());
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
        writeEdges();
        writeRanges();
        writer.writeEndElement();
        writer.writeEndElement();
    }

    @SuppressWarnings("unchecked")
    private void writeDAWISProperties(DAWISNode node) throws XMLStreamException {
        writer.writeStartElement("loaded");
        writer.writeCData(node.getDataLoadedAsString());
        writer.writeEndElement();
        writer.writeStartElement("object");
        writer.writeCData(node.getObject());
        writer.writeEndElement();
        writer.writeStartElement("id");
        writer.writeCData(node.getID());
        writer.writeEndElement();
        writer.writeStartElement("name");
        writer.writeCData(node.getName());
        writer.writeEndElement();
        writer.writeStartElement("db");
        writer.writeCData(node.getDB());
        writer.writeEndElement();
        writer.writeStartElement("organism");
        writer.writeCData(node.getOrganism());
        writer.writeEndElement();
        writer.writeStartElement("diagnosisType");
        writer.writeCData(node.getDiagnosisType());
        writer.writeEndElement();
        writer.writeStartElement("disorder");
        writer.writeCData(node.getDisorder());
        writer.writeEndElement();
        writer.writeStartElement("pathwayMap");
        writer.writeCData(node.getPathwayMap());
        writer.writeEndElement();
        writer.writeStartElement("ontology");
        writer.writeCData(node.getOntology());
        writer.writeEndElement();
        writer.writeStartElement("definition");
        writer.writeCData(node.getDefinition());
        writer.writeEndElement();
        writer.writeStartElement("position");
        writer.writeCData(node.getPosition());
        writer.writeEndElement();
        writer.writeStartElement("codonUsage");
        writer.writeCData(node.getCodonUsage());
        writer.writeEndElement();
        writer.writeStartElement("nucleotidSequenceLength");
        writer.writeCData(node.getNucleotidSequenceLength());
        writer.writeEndElement();
        writer.writeStartElement("nucleotidSequence");
        writer.writeCData(node.getNucleotidSequence());
        writer.writeEndElement();
        writer.writeStartElement("aminoAcidSequenceLength");
        writer.writeCData(node.getAminoAcidSeqLength());
        writer.writeEndElement();
        writer.writeStartElement("aminoAcidSequence");
        writer.writeCData(node.getAminoAcidSeq());
        writer.writeEndElement();
        writer.writeStartElement("motif");
        writer.writeCData(node.getMotifs());
        writer.writeEndElement();
        writer.writeStartElement("organelle");
        writer.writeCData(node.getOrganelle());
        writer.writeEndElement();
        writer.writeStartElement("weight");
        writer.writeCData(node.getWeigth());
        writer.writeEndElement();
        writer.writeStartElement("comment");
        writer.writeCData(node.getComment());
        writer.writeEndElement();
        writer.writeStartElement("equation");
        writer.writeCData(node.getEquation());
        writer.writeEndElement();
        writer.writeStartElement("rDM");
        writer.writeCData(node.getRDM());
        writer.writeEndElement();
        writer.writeStartElement("formula");
        writer.writeCData(node.getFormula());
        writer.writeEndElement();
        writer.writeStartElement("atoms");
        writer.writeCData(node.getAtoms());
        writer.writeEndElement();
        writer.writeStartElement("atomsNumber");
        writer.writeCData(node.getAtomsNr());
        writer.writeEndElement();
        writer.writeStartElement("bonds");
        writer.writeCData(node.getBonds());
        writer.writeEndElement();
        writer.writeStartElement("bondsNumber");
        writer.writeCData(node.getBondsNumber());
        writer.writeEndElement();
        writer.writeStartElement("module");
        writer.writeCData(node.getModule());
        writer.writeEndElement();
        writer.writeStartElement("sequenceSource");
        writer.writeCData(node.getSequenceSource());
        writer.writeEndElement();
        writer.writeStartElement("remark");
        writer.writeCData(node.getRemarks());
        writer.writeEndElement();
        writer.writeStartElement("composition");
        writer.writeCData(node.getComposition());
        writer.writeEndElement();
        writer.writeStartElement("node");
        writer.writeCData(node.getNode());
        writer.writeEndElement();
        writer.writeStartElement("edge");
        writer.writeCData(node.getEdge());
        writer.writeEndElement();
        writer.writeStartElement("target");
        writer.writeCData(node.getTarget());
        writer.writeEndElement();
        writer.writeStartElement("bracket");
        writer.writeCData(node.getBracket());
        writer.writeEndElement();
        writer.writeStartElement("original");
        writer.writeCData(node.getOriginal());
        writer.writeEndElement();
        writer.writeStartElement("repeat");
        writer.writeCData(node.getRepeat());
        writer.writeEndElement();
        writer.writeStartElement("activity");
        writer.writeCData(node.getActivity());
        writer.writeEndElement();
        writer.writeStartElement("type");
        writer.writeCData(node.getType());
        writer.writeEndElement();
        writer.writeStartElement("effect");
        writer.writeCData(node.getEffect());
        writer.writeEndElement();
        writer.writeStartElement("information");
        writer.writeCData(node.getInformation());
        writer.writeEndElement();
        writer.writeStartElement("isoelectricPoint");
        writer.writeCData(node.getIsoelectricPoint());
        writer.writeEndElement();
        writer.writeStartElement("isoformenNumber");
        writer.writeCData(node.getIsoformenNumber());
        writer.writeEndElement();
        writer.writeStartElement("specifityNegative");
        writer.writeCData(node.getSpecificityNeg());
        writer.writeEndElement();
        writer.writeStartElement("specifityPositiv");
        writer.writeCData(node.getSpecificityPos());
        writer.writeEndElement();
        writer.writeStartElement("factorClass");
        writer.writeCData(node.getFactorClass());
        writer.writeEndElement();
        writer.writeStartElement("encodingGene");
        writer.writeCData(node.getEncodingGene());
        writer.writeEndElement();
        writer.writeStartElement("startPoint");
        writer.writeCData(node.getStartPoint());
        writer.writeEndElement();
        writer.writeStartElement("endPoint");
        writer.writeCData(node.getEndPoint());
        writer.writeEndElement();
        writer.writeStartElement("complexName");
        writer.writeCData(node.getComplexName());
        writer.writeEndElement();
        Iterator it;
        writer.writeStartElement("allCollectorElements");
        it = node.getElementsAsVector().iterator();
        while (it.hasNext()) {
            String[] element = (String[]) it.next();
            writer.writeStartElement("collectorElementID");
            writer.writeCData(element[0]);
            writer.writeEndElement();
            if (element.length > 1) {
                writer.writeStartElement("collectorElementName");
                writer.writeCData(element[1]);
                writer.writeEndElement();
            }
        }
        writer.writeEndElement();
        writer.writeStartElement("allSynonyms");
        it = node.getSynonymsAsVector().iterator();
        while (it.hasNext()) {
            String element = it.next().toString();
            writer.writeStartElement("synonym");
            writer.writeCData(element);
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeStartElement("allDomains");
        it = node.getDomainsAsVector().iterator();
        while (it.hasNext()) {
            String element = it.next().toString();
            writer.writeStartElement("domain");
            writer.writeCData(element);
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeStartElement("allFeatures");
        it = node.getFeaturesAsVector().iterator();
        while (it.hasNext()) {
            String element = it.next().toString();
            writer.writeStartElement("feature");
            writer.writeCData(element);
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeStartElement("allLocations");
        it = node.getLocationsAsVector().iterator();
        while (it.hasNext()) {
            String element = it.next().toString();
            writer.writeStartElement("location");
            writer.writeCData(element);
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeStartElement("allGeneNames");
        it = node.getGeneNamesAsVector().iterator();
        while (it.hasNext()) {
            String element = it.next().toString();
            writer.writeStartElement("geneName");
            writer.writeCData(element);
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeStartElement("allPDBs");
        it = node.getPDBsAsVector().iterator();
        while (it.hasNext()) {
            String element = it.next().toString();
            writer.writeStartElement("pDB");
            writer.writeCData(element);
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeStartElement("allAccessionnumbers");
        it = node.getAccessionnumbersAsVector().iterator();
        while (it.hasNext()) {
            String element = it.next().toString();
            writer.writeStartElement("accessionnumber");
            writer.writeCData(element);
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeStartElement("allClassifications");
        it = node.getClassificationAsVector().iterator();
        while (it.hasNext()) {
            String element = it.next().toString();
            writer.writeStartElement("classification");
            writer.writeCData(element);
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeStartElement("allSubstrates");
        it = node.getSubstratesAsVector().iterator();
        while (it.hasNext()) {
            String element = it.next().toString();
            writer.writeStartElement("substrate");
            writer.writeCData(element);
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeStartElement("allSubstrateNames");
        it = node.getSubstrateNamesAsVector().iterator();
        while (it.hasNext()) {
            String element = it.next().toString();
            writer.writeStartElement("substrateName");
            writer.writeCData(element);
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeStartElement("allProducts");
        it = node.getProductsAsVector().iterator();
        while (it.hasNext()) {
            String element = it.next().toString();
            writer.writeStartElement("product");
            writer.writeCData(element);
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeStartElement("allProductNames");
        it = node.getProductNamesAsVector().iterator();
        while (it.hasNext()) {
            String element = it.next().toString();
            writer.writeStartElement("productName");
            writer.writeCData(element);
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeStartElement("allCofactors");
        it = node.getCofactorsAsVector().iterator();
        while (it.hasNext()) {
            String element = it.next().toString();
            writer.writeStartElement("cofactor");
            writer.writeCData(element);
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeStartElement("allCofactorNames");
        it = node.getCofactorNamesAsVector().iterator();
        while (it.hasNext()) {
            String element = it.next().toString();
            writer.writeStartElement("cofactorsName");
            writer.writeCData(element);
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeStartElement("allInhibitors");
        it = node.getInhibitorsAsVector().iterator();
        while (it.hasNext()) {
            String element = it.next().toString();
            writer.writeStartElement("inhibitor");
            writer.writeCData(element);
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeStartElement("allInhibitorNames");
        it = node.getInhibitorNamesAsVector().iterator();
        while (it.hasNext()) {
            String element = it.next().toString();
            writer.writeStartElement("inhibitorName");
            writer.writeCData(element);
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeStartElement("allEffectors");
        it = node.getEffectorsAsVector().iterator();
        while (it.hasNext()) {
            String element = it.next().toString();
            writer.writeStartElement("effector");
            writer.writeCData(element);
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeStartElement("allEffectorNames");
        it = node.getEffectorNamesAsVector().iterator();
        while (it.hasNext()) {
            String element = it.next().toString();
            writer.writeStartElement("effectorName");
            writer.writeCData(element);
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeStartElement("allOrthologys");
        it = node.getOrthologyAsVector().iterator();
        while (it.hasNext()) {
            String element = it.next().toString();
            writer.writeStartElement("orthology");
            writer.writeCData(element);
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeStartElement("allDBLinks");
        it = node.getDBLinksAsVector().iterator();
        while (it.hasNext()) {
            String element = it.next().toString();
            writer.writeStartElement("dbLink");
            writer.writeCData(element);
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeStartElement("catalysts");
        it = node.getCatalystsAsVector().iterator();
        while (it.hasNext()) {
            String element = it.next().toString();
            writer.writeStartElement("catalyst");
            writer.writeCData(element);
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeStartElement("catalystNames");
        it = node.getCatalystNamesAsVector().iterator();
        while (it.hasNext()) {
            String element = it.next().toString();
            writer.writeStartElement("catalystName");
            writer.writeCData(element);
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeStartElement("superfamilies");
        it = node.getSuperfamiliesAsVector().iterator();
        while (it.hasNext()) {
            String element = it.next().toString();
            writer.writeStartElement("superfamily");
            writer.writeCData(element);
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeStartElement("subfamilies");
        it = node.getSubfamiliesAsVector().iterator();
        while (it.hasNext()) {
            String element = it.next().toString();
            writer.writeStartElement("subfamily");
            writer.writeCData(element);
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeStartElement("expressions");
        it = node.getExpressionsAsVector().iterator();
        while (it.hasNext()) {
            String element = it.next().toString();
            writer.writeStartElement("expression");
            writer.writeCData(element);
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeStartElement("prozesses");
        it = node.getProzessesAsVector().iterator();
        while (it.hasNext()) {
            String element = it.next().toString();
            writer.writeStartElement("prozess");
            writer.writeCData(element);
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeStartElement("functions");
        it = node.getFunctionsAsVector().iterator();
        while (it.hasNext()) {
            String element = it.next().toString();
            writer.writeStartElement("function");
            writer.writeCData(element);
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeStartElement("references");
        it = node.getReferenceAsVector().iterator();
        while (it.hasNext()) {
            String element = it.next().toString();
            writer.writeStartElement("reference");
            writer.writeCData(element);
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeStartElement("motifs");
        it = node.getMotifsAsVector().iterator();
        while (it.hasNext()) {
            String element = it.next().toString();
            writer.writeStartElement("motif");
            writer.writeCData(element);
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeStartElement("methods");
        it = node.getMethodsAsSVector().iterator();
        while (it.hasNext()) {
            String element = it.next().toString();
            writer.writeStartElement("method");
            writer.writeCData(element);
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeStartElement("idDBRelations");
        Hashtable<String, String> ht = node.getAllIDDBRelationsAsHashtable();
        Set<String> keys = ht.keySet();
        it = keys.iterator();
        while (it.hasNext()) {
            String id = it.next().toString();
            writer.writeStartElement("key");
            writer.writeCData(id);
            writer.writeEndElement();
            String db = ht.get(id);
            writer.writeStartElement("value");
            writer.writeCData(db);
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeStartElement("idIDRelations");
        ht = node.getAllIDsAsHashtable();
        keys = ht.keySet();
        it = keys.iterator();
        while (it.hasNext()) {
            String id = it.next().toString();
            writer.writeStartElement("key");
            writer.writeCData(id);
            writer.writeEndElement();
            String db = ht.get(id);
            writer.writeStartElement("value");
            writer.writeCData(db);
            writer.writeEndElement();
        }
        writer.writeEndElement();
    }

    private void writeRanges() throws XMLStreamException {
        List<Map<String, String>> rangeInfos = RangeSelector.getInstance().getRangesInMyGraph(pw.getGraph());
        if (rangeInfos != null) {
            for (Map<String, String> range : rangeInfos) {
                writer.writeStartElement("rangeInfo");
                for (String key : range.keySet()) {
                    String value = range.get(key);
                    writer.writeAttribute(key, value);
                }
                writer.writeEndElement();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void getSpeciesType() throws XMLStreamException {
        Iterator it = pw.getAllNodes().iterator();
        writer.writeStartElement("listOfSpeciesTypes");
        int i = 1;
        while (it.hasNext()) {
            BiologicalNodeAbstract bna = (BiologicalNodeAbstract) it.next();
            if (!speciesTypeID.containsKey(bna.getBiologicalElement())) {
                writer.writeStartElement("speciesType");
                writer.writeAttribute("id", "s_" + i);
                writer.writeAttribute("name", bna.getBiologicalElement());
                writer.writeEndElement();
                speciesTypeID.put(bna.getBiologicalElement(), "s_" + i);
                i++;
            }
        }
        writer.writeEndElement();
    }

    @SuppressWarnings("unchecked")
    private void getSpecies() throws XMLStreamException {
        Iterator it = pw.getAllNodes().iterator();
        writer.writeStartElement("listOfSpecies");
        while (it.hasNext()) {
            BiologicalNodeAbstract bna = (BiologicalNodeAbstract) it.next();
            writer.writeStartElement("species");
            writer.writeAttribute("compartment", compartments.get(bna.getVertex().toString()) + "");
            writer.writeAttribute("id", bna.getVertex().toString());
            writer.writeAttribute("speciesType", speciesTypeID.get(bna.getBiologicalElement()) + "");
            writer.writeAttribute("initialAmount", "1.0");
            writer.writeAttribute("name", bna.getName());
            writer.writeEndElement();
        }
        writer.writeEndElement();
    }

    public void write() throws XMLStreamException, IOException {
        OutputStream out = new FileOutputStream(file);
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        writer = factory.createXMLStreamWriter(out);
        writer.writeStartDocument();
        writer.writeStartElement("sbml");
        writer.writeNamespace("xmlns", "http://www.sbml.org/sbml/level2/version3");
        writer.writeAttribute("level", "2");
        writer.writeAttribute("version", "3");
        writer.writeStartElement("model");
        writeAnnotation();
        getCompartmentTypes();
        getSpeciesType();
        getCompartment();
        getSpecies();
        writer.writeEndElement();
        writer.writeEndElement();
        writer.writeEndDocument();
        writer.flush();
        writer.close();
        out.close();
    }
}
