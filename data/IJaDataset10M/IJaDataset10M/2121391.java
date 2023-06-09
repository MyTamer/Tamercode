package org.ejbca.cvc;

import java.util.Date;
import org.ejbca.cvc.exception.ConstructionException;

/**
 * Represents a CertificateBody
 * 
 * @author Keijo Kurkinen, Swedish National Police Board
 *
 */
public class CVCertificateBody extends AbstractSequence {

    private static final long serialVersionUID = -7593852557673998727L;

    private static CVCTagEnum[] allowedFields = new CVCTagEnum[] { CVCTagEnum.PROFILE_IDENTIFIER, CVCTagEnum.CA_REFERENCE, CVCTagEnum.PUBLIC_KEY, CVCTagEnum.HOLDER_REFERENCE, CVCTagEnum.HOLDER_AUTH_TEMPLATE, CVCTagEnum.EFFECTIVE_DATE, CVCTagEnum.EXPIRATION_DATE };

    @Override
    CVCTagEnum[] getAllowedFields() {
        return allowedFields;
    }

    /**
    * Creates an empty instance
    */
    CVCertificateBody() {
        super(CVCTagEnum.CERTIFICATE_BODY);
    }

    /**
    * Creates an instance suitable for CertificateRequest
    * @param authorityReference
    * @param publicKey
    * @param holderReference
    * @throws ConstructionException
    */
    public CVCertificateBody(CAReferenceField authorityReference, CVCPublicKey publicKey, HolderReferenceField holderReference) throws ConstructionException {
        this();
        if (publicKey == null) {
            throw new IllegalArgumentException("publicKey is null");
        }
        if (holderReference == null) {
            throw new IllegalArgumentException("holderReference is null");
        }
        addSubfield(new IntegerField(CVCTagEnum.PROFILE_IDENTIFIER, CVC_VERSION));
        addSubfield(authorityReference);
        addSubfield(publicKey);
        addSubfield(holderReference);
    }

    /**
    * Creates an instance suitable for a CVCertificate
    * @param authorityReference
    * @param publicKey
    * @param holderReference
    * @param authRole
    * @param accessRight
    * @param validFrom
    * @param validTo
    */
    public CVCertificateBody(CAReferenceField authorityReference, CVCPublicKey publicKey, HolderReferenceField holderReference, AuthorizationRole authRole, AccessRight accessRight, Date validFrom, Date validTo) throws ConstructionException {
        this(authorityReference, publicKey, holderReference);
        if (authRole == null) {
            throw new IllegalArgumentException("authRole is null");
        }
        if (accessRight == null) {
            throw new IllegalArgumentException("accessRight is null");
        }
        if (validFrom == null) {
            throw new IllegalArgumentException("validFrom is null");
        }
        if (validTo == null) {
            throw new IllegalArgumentException("validTo is null");
        }
        addSubfield(new CVCAuthorizationTemplate(authRole, accessRight));
        addSubfield(new DateField(CVCTagEnum.EFFECTIVE_DATE, validFrom));
        addSubfield(new DateField(CVCTagEnum.EXPIRATION_DATE, validTo));
    }

    /**
    * Returns CVCAuthorizationTemplate
    * @return
    */
    public CVCAuthorizationTemplate getAuthorizationTemplate() throws NoSuchFieldException {
        return (CVCAuthorizationTemplate) getSubfield(CVCTagEnum.HOLDER_AUTH_TEMPLATE);
    }

    /**
    * Returns 'Effective Date' 
    * @return
    */
    public Date getValidFrom() throws NoSuchFieldException {
        return ((DateField) getSubfield(CVCTagEnum.EFFECTIVE_DATE)).getDate();
    }

    /**
    * Returns 'Expiration Date' 
    * @return
    */
    public Date getValidTo() throws NoSuchFieldException {
        return ((DateField) getSubfield(CVCTagEnum.EXPIRATION_DATE)).getDate();
    }

    /**
    * Returns 'Certificate Authority Reference'
    * Since this field is optional in a CVCRequest this method may return null
    * @return
    */
    public CAReferenceField getAuthorityReference() throws NoSuchFieldException {
        return (CAReferenceField) getOptionalSubfield(CVCTagEnum.CA_REFERENCE);
    }

    /**
    * Returns the public key
    * @return
    */
    public CVCPublicKey getPublicKey() throws NoSuchFieldException {
        return (CVCPublicKey) getSubfield(CVCTagEnum.PUBLIC_KEY);
    }

    /**
    * Returns 'Certificate Holder Reference'
    * @return
    */
    public HolderReferenceField getHolderReference() throws NoSuchFieldException {
        return (HolderReferenceField) getSubfield(CVCTagEnum.HOLDER_REFERENCE);
    }
}
