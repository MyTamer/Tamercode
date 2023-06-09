package org.nightlabs.jfire.trade;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import org.nightlabs.jfire.trade.id.OfferRequirementID;

/**
 * One instance of OfferRequirement exists for each Offer on the side of the vendor.
 * The OfferRequirement bundles all other offers that the vendor needs to create to
 * fulfill its own offer.
 *
 * @author Marco Schulze - marco at nightlabs dot de
 * 
 * @jdo.persistence-capable
 *		identity-type="application"
 *		objectid-class="org.nightlabs.jfire.trade.id.OfferRequirementID"
 *		detachable="true"
 *		table="JFireTrade_OfferRequirement"
 *
 * @jdo.inheritance strategy="new-table"
 *
 * @jdo.create-objectid-class field-order="organisationID, offerIDPrefix, offerID"
 */
public class OfferRequirement implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
	 * This method returns a previously existing {@link OfferRequirement} or creates and persists
	 * a new instance, if not existent.
	 */
    public static OfferRequirement createOfferRequirement(PersistenceManager pm, Offer offer) {
        OfferRequirement res = getOfferRequirement(pm, offer, false);
        if (res != null) return res;
        return pm.makePersistent(new OfferRequirement(offer));
    }

    /**
	 * This method returns a previously existing {@link OfferRequirement} or <code>null</code>, if none exists
	 * and <code>throwExceptionIfNotExistent == false</code>.
	 *
	 * @param pm The <code>PersistenceManager</code> used for accessing the datastore.
	 * @param offer The <code>Offer</code> for which to search an <code>OfferRequirement</code> instance.
	 * @param throwExceptionIfNotExistent if <code>true</code> and there exists no <code>OfferRequirement</code>, this method will throw a {@link JDOObjectNotFoundException} - if <code>false</code>, it will simply return <code>null</code> instead.
	 * @throws JDOObjectNotFoundException if <code>throwExceptionIfNotExistent == true</code> and the <code>OfferRequirement</code> does not exist.
	 */
    public static OfferRequirement getOfferRequirement(PersistenceManager pm, Offer offer, boolean throwExceptionIfNotExistent) {
        OfferRequirementID offerRequirementID = OfferRequirementID.create(offer.getOrganisationID(), offer.getOfferIDPrefix(), offer.getOfferID());
        pm.getExtent(OfferRequirement.class);
        try {
            OfferRequirement res = (OfferRequirement) pm.getObjectById(offerRequirementID);
            res.getOffer();
            return res;
        } catch (JDOObjectNotFoundException x) {
            if (throwExceptionIfNotExistent) throw x;
            return null;
        }
    }

    /**
	 * @jdo.field primary-key="true"
	 * @jdo.column length="100"
	 */
    private String organisationID;

    /**
	 * @jdo.field primary-key="true"
	 * @jdo.column length="50"
	 */
    private String offerIDPrefix;

    /**
	 * @jdo.field primary-key="true"
	 */
    private long offerID;

    private Offer offer;

    /**
	 * key: LegalEntity vendor<br/>
	 * value: Offer offer
	 * <br/><br/>
	 *
	 * @jdo.field
	 *		persistence-modifier="persistent"
	 *		collection-type="map"
	 *		key-type="LegalEntity"
	 *		value-type="Offer"
	 *		null-value="exception"
	 *		table="JFireTrade_OfferRequirement_vendor2offer"
	 *
	 * @jdo.join
	 */
    private Map<LegalEntity, Offer> vendor2offer = new HashMap<LegalEntity, Offer>();

    public OfferRequirement() {
    }

    public OfferRequirement(Offer offer) {
        if (offer == null) throw new NullPointerException("offer");
        this.offer = offer;
        this.organisationID = offer.getOrganisationID();
        this.offerIDPrefix = offer.getOfferIDPrefix();
        this.offerID = offer.getOfferID();
    }

    public void addPartnerOffer(Offer offer) {
        LegalEntity vendor = offer.getOrder().getVendor();
        Offer other = vendor2offer.get(vendor);
        if (offer.equals(other)) return;
        if (other != null) throw new IllegalStateException("Vendor-Offer cannot be added, because another Offer is already assigned for this vendor! offer.primaryKey=" + offer.getPrimaryKey() + " otherOffer.primaryKey=" + other.getPrimaryKey());
        vendor2offer.put(vendor, offer);
    }

    /**
	 * Returns the Offer for this vendor or null.
	 * @param vendor
	 */
    public Offer getPartnerOffer(LegalEntity vendor) {
        return vendor2offer.get(vendor);
    }

    public Collection<Offer> getPartnerOffers() {
        return vendor2offer.values();
    }

    /**
	 * @return The associated Offer.
	 */
    public Offer getOffer() {
        return offer;
    }
}
