package org.dllearner.utilities.owl;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.dllearner.core.owl.Description;
import org.dllearner.core.owl.Individual;
import org.dllearner.core.owl.ObjectProperty;
import org.dllearner.parser.KBParser;
import org.dllearner.reasoning.OWLAPIReasoner;
import org.dllearner.utilities.statistics.SimpleClock;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLPropertyAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;

public class OntologyCloserOWLAPI {

    OWLOntology onto;

    OWLAPIReasoner rs;

    HashMap<Individual, Set<OWLObjectExactCardinality>> indToRestr;

    OWLDataFactory factory;

    OWLOntologyManager manager;

    public int numberOfStatementsChanged = 0;

    public OntologyCloserOWLAPI(OWLAPIReasoner reasoner) {
        this.rs = reasoner;
        this.indToRestr = new HashMap<Individual, Set<OWLObjectExactCardinality>>();
        this.manager = OWLManager.createOWLOntologyManager();
        this.factory = manager.getOWLDataFactory();
        this.onto = reasoner.getOWLAPIOntologies().get(0);
    }

    /**
	 * counts the number of roles used by each individual and assigns
	 * ExactCardinalityRestriction to Individuals
	 */
    public void applyNumberRestrictions() {
        System.out.println("apply ExactCardinalityRestriction to Individuals");
        Set<ObjectProperty> allRoles = this.rs.getObjectProperties();
        System.out.println("found: " + allRoles.size() + " roles");
        testForTransitiveProperties(true);
        for (ObjectProperty oneRole : allRoles) {
            Map<Individual, SortedSet<Individual>> allRoleMembers = this.rs.getPropertyMembers(oneRole);
            for (Individual oneInd : allRoleMembers.keySet()) {
                SortedSet<Individual> fillers = allRoleMembers.get(oneInd);
                if (fillers.size() > 0) {
                    OWLObjectExactCardinality oecr = factory.getOWLObjectExactCardinality(fillers.size(), factory.getOWLObjectProperty(IRI.create(oneRole.getName())), factory.getOWLThing());
                    OWLAxiom axiom = factory.getOWLClassAssertionAxiom(oecr, factory.getOWLNamedIndividual(IRI.create(oneInd.getName())));
                    AddAxiom addAxiom = new AddAxiom(this.onto, axiom);
                    try {
                        manager.applyChange(addAxiom);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
	 * counts the number of roles used by each individual and assigns
	 * ExactCardinalityRestriction, but uses only one Intersection: role1
	 * exactly 1 and role2 exactly 2 and ....
	 * 
	 */
    public void applyNumberRestrictionsConcise() {
        Set<ObjectProperty> allRoles = this.rs.getObjectProperties();
        testForTransitiveProperties(true);
        for (ObjectProperty oneRole : allRoles) {
            Map<Individual, SortedSet<Individual>> allRoleMembers = this.rs.getPropertyMembers(oneRole);
            for (Individual oneInd : allRoleMembers.keySet()) {
                SortedSet<Individual> fillers = allRoleMembers.get(oneInd);
                if (fillers.size() > 0) {
                    OWLObjectExactCardinality oecr = factory.getOWLObjectExactCardinality(fillers.size(), factory.getOWLObjectProperty(IRI.create(oneRole.getName())), factory.getOWLThing());
                    collectExObjRestrForInd(oneInd, oecr);
                }
            }
        }
        Set<OWLClassExpression> target = new HashSet<OWLClassExpression>();
        Set<OWLObjectExactCardinality> s = null;
        for (Individual oneInd : indToRestr.keySet()) {
            s = indToRestr.get(oneInd);
            for (OWLObjectExactCardinality oecr : s) {
                target.add(oecr);
            }
            OWLObjectIntersectionOf intersection = factory.getOWLObjectIntersectionOf(target);
            s = null;
            target = new HashSet<OWLClassExpression>();
            OWLAxiom axiom = factory.getOWLClassAssertionAxiom(intersection, factory.getOWLNamedIndividual(IRI.create(oneInd.getName())));
            AddAxiom addAxiom = new AddAxiom(this.onto, axiom);
            try {
                manager.applyChange(addAxiom);
                numberOfStatementsChanged++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * @param printflag
	 *            boolean for some output
	 * @return true if some roles are transitive
	 */
    public boolean testForTransitiveProperties(boolean printflag) {
        Set<OWLTransitiveObjectPropertyAxiom> ax = onto.getAxioms(AxiomType.TRANSITIVE_OBJECT_PROPERTY);
        boolean retval = !ax.isEmpty();
        for (OWLPropertyAxiom propertyAxiom : ax) {
            if (printflag) {
                System.out.println("WARNING transitive object property can't be used in cardinality restriction\n" + propertyAxiom.toString() + "but I'm ignoring it");
            }
        }
        if (printflag) {
            System.out.println("No transitive Properties found");
        }
        return retval;
    }

    /**
	 * makes some retrieval queries
	 * @param conceptStr
	 */
    public SortedSet<Individual> verifyConcept(String conceptStr) {
        Description d;
        SimpleClock sc = new SimpleClock();
        StringBuffer sb = new StringBuffer();
        sb.append(conceptStr);
        conceptStr = sb.toString();
        SortedSet<Individual> ind = new TreeSet<Individual>();
        try {
            d = KBParser.parseConcept(conceptStr);
            System.out.println("\n*******************\nStarting retrieval");
            System.out.println(d.toManchesterSyntaxString("", new HashMap<String, String>()));
            sc.setTime();
            this.rs.getIndividuals(d);
            System.out.println("retrieved: " + ind.size() + " instances");
            sc.printAndSet();
            for (Individual individual : ind) {
                System.out.print(individual + "|");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ind;
    }

    public void writeOWLFile(URI filename) {
        try {
            manager.saveOntology(this.onto, new RDFXMLOntologyFormat(), IRI.create(filename));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean collectExObjRestrForInd(Individual ind, OWLObjectExactCardinality oecr) {
        Set<OWLObjectExactCardinality> s = indToRestr.get(ind);
        if (s == null) {
            indToRestr.put(ind, new HashSet<OWLObjectExactCardinality>());
            s = indToRestr.get(ind);
        }
        return s.add(oecr);
    }
}
