package br.ufal.ic.forbile.infra.emathema.ontology;

import edu.stanford.smi.protegex.owl.model.*;

/**
 * Generated by Protege-OWL  (http://protege.stanford.edu/plugins/owl).
 * Source OWL Class: http://www.owl-ontologies.com/emathema.owl#Content
 *
 * @version generated on Wed Sep 27 02:26:47 GMT-03:00 2006
 */
public interface Content extends SpecificLearningResource {

    String getDescription();

    RDFProperty getDescriptionProperty();

    boolean hasDescription();

    void setDescription(String newDescription);

    String getIssue();

    RDFProperty getIssueProperty();

    boolean hasIssue();

    void setIssue(String newIssue);
}
