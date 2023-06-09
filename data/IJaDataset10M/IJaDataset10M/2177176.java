package tudresden.ocl20.pivot.standalone.metamodel;

import tudresden.ocl20.pivot.metamodels.uml2.UML2MetamodelPlugin;
import tudresden.ocl20.pivot.model.IModelProvider;
import tudresden.ocl20.pivot.model.metamodel.IMetamodel;
import tudresden.ocl20.pivot.standalone.model.StandaloneUML2ModelProvider;

/**
 * Instead of using the extension point, meta-models can be added directly by
 * implementing {@link IMetamodel}.
 * 
 * @author Michael Thiele
 * 
 */
public class UMLMetamodel implements IMetamodel {

    private IModelProvider modelProvider = new StandaloneUML2ModelProvider();

    public String getId() {
        return UML2MetamodelPlugin.ID;
    }

    public IModelProvider getModelProvider() {
        return modelProvider;
    }

    public String getName() {
        return "UML";
    }
}
