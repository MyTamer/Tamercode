package org.cagrid.transfer.context.service.globus.resource;

import org.cagrid.transfer.context.common.TransferServiceContextConstants;
import org.cagrid.transfer.context.stubs.TransferServiceContextResourceProperties;
import org.globus.wsrf.ResourceException;
import org.globus.wsrf.ResourceKey;
import org.globus.wsrf.impl.ResourceHomeImpl;
import org.globus.wsrf.impl.SimpleResourceKey;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.MessageContext;
import org.globus.wsrf.ResourceContext;
import org.globus.wsrf.utils.AddressingUtils;
import org.apache.axis.components.uuid.UUIDGen;
import org.apache.axis.components.uuid.UUIDGenFactory;

/** 
 * DO NOT EDIT:  This class is autogenerated!
 *
 * This class implements the resource home for the resource type represented
 * by this service.
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public class TransferServiceContextResourceHome extends ResourceHomeImpl {

    private static final UUIDGen UUIDGEN = UUIDGenFactory.getUUIDGen();

    /**
 	* Creates a new Resource, adds it to the list of resources managed by this resource home,
 	* and returns the key to the resource.
 	*/
    public ResourceKey createResource() throws Exception {
        TransferServiceContextResource resource = (TransferServiceContextResource) createNewInstance();
        TransferServiceContextResourceProperties props = new TransferServiceContextResourceProperties();
        Object id = UUIDGEN.nextUUID();
        ResourceKey key = new SimpleResourceKey(getKeyTypeName(), id);
        resource.setResourceKey(key);
        resource.initialize(props, TransferServiceContextConstants.RESOURCE_PROPERTY_SET, id);
        add(key, resource);
        return key;
    }

    /**
 	* Take a resource key managed by this resource, locates the resource, and created a typed EPR for the resource.
 	*/
    public org.cagrid.transfer.context.stubs.types.TransferServiceContextReference getResourceReference(ResourceKey key) throws Exception {
        MessageContext ctx = MessageContext.getCurrentContext();
        String transportURL = (String) ctx.getProperty(org.apache.axis.MessageContext.TRANS_URL);
        transportURL = transportURL.substring(0, transportURL.lastIndexOf('/') + 1);
        transportURL += "TransferServiceContext";
        EndpointReferenceType epr = AddressingUtils.createEndpointReference(transportURL, key);
        org.cagrid.transfer.context.stubs.types.TransferServiceContextReference ref = new org.cagrid.transfer.context.stubs.types.TransferServiceContextReference();
        ref.setEndpointReference(epr);
        return ref;
    }

    /**
 	* Given the key of a resource managed by this resource home, a type resource will be returned.
 	*/
    public TransferServiceContextResource getResource(ResourceKey key) throws ResourceException {
        TransferServiceContextResource thisResource = (TransferServiceContextResource) find(key);
        return thisResource;
    }

    /**
     * Get the resouce that is being addressed in this current context
     */
    public TransferServiceContextResource getAddressedResource() throws Exception {
        TransferServiceContextResource thisResource;
        thisResource = (TransferServiceContextResource) ResourceContext.getResourceContext().getResource();
        return thisResource;
    }
}