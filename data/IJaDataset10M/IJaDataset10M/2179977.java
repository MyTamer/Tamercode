package org.makumba.providers.datadefinition;

import org.makumba.DataDefinition;
import org.makumba.MakumbaError;
import org.makumba.MetadataAspect;
import org.makumba.TypeMetadata;
import org.makumba.providers.DataDefinitionProvider;
import org.makumba.providers.AnnotationMetadataReader;

public class TypeMetadataImpl implements TypeMetadata {

    private final DataDefinitionProvider ddp;

    private final AnnotationMetadataReader r;

    private Class<?> clazz;

    private DataDefinition dd;

    public Class<?> getClazz() {
        return this.clazz;
    }

    public DataDefinition getDataDefinition() {
        return this.dd;
    }

    public TypeMetadataImpl(String typeName) {
        this.ddp = DataDefinitionProvider.getInstance();
        this.r = AnnotationMetadataReader.getInstance();
        this.dd = ddp.getDataDefinition(typeName);
        try {
            this.clazz = Class.forName(typeName);
        } catch (ClassNotFoundException e) {
            throw new MakumbaError("Could not find class of type '" + typeName + "'");
        }
    }

    public Object getAspect(MetadataAspect a) {
        return null;
    }
}
