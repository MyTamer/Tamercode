package org.nightlabs.jfire.accounting.book;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import org.nightlabs.jfire.accounting.Invoice;
import org.nightlabs.jfire.security.User;
import org.nightlabs.jfire.trade.Article;
import org.nightlabs.jfire.trade.ArticlePrice;
import org.nightlabs.jfire.trade.OrganisationLegalEntity;
import org.nightlabs.jfire.transfer.Anchor;

/**
 * LocalAccountantDelegates are used by {@link org.nightlabs.jfire.accounting.book.LocalAccountant}
 * to assist in the booking procedure. A delegate is registered per ProductType
 * and will be asked to {@link #bookArticle(OrganisationLegalEntity, User, Invoice, Article, BookMoneyTransfer, Set)}
 * when an invoice is booked.
 *
 * @author Alexander Bieber <alex[AT]nightlabs[DOT]de>
 *
 * @jdo.persistence-capable
 *		identity-type = "application"
 *		objectid-class="org.nightlabs.jfire.accounting.book.id.LocalAccountantDelegateID"
 *		detachable="true"
 *		table="JFireTrade_LocalAccountantDelegate"
 *
 * @jdo.inheritance-discriminator strategy="class-name"
 *
 * @jdo.create-objectid-class
 *		field-order="organisationID, localAccountantDelegateID"
 *
 * @jdo.query
 *		name="getChildDelegates"
 *		query="SELECT
 *			WHERE
 *						this.extendedAccountantDelegate != null &&
 *						this.extendedAccountantDelegate.organisationID == paramOrganisationID &&
 *						this.extendedAccountantDelegate.localAccountantDelegateID == paramLocalAccountantDelegateID
 *			PARAMETERS String paramOrganisationID, String paramLocalAccountantDelegateID
 *			import java.lang.String"
 *
 * @jdo.fetch-group name="LocalAccountantDelegate.name" fields="name"
 * @jdo.fetch-group name="LocalAccountantDelegate.extendedAccountantDelegate" fields="extendedAccountantDelegate"
 * @jdo.fetch-group name="LocalAccountantDelegate.this" fetch-groups="default" fields="name, extendedAccountantDelegate"
 *
 */
public abstract class LocalAccountantDelegate implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String FETCH_GROUP_NAME = "LocalAccountantDelegate.name";

    public static final String FETCH_GROUP_EXTENDED_ACCOUNTANT_DELEGATE = "LocalAccountantDelegate.extendedAccountantDelegate";

    /**
	 * @deprecated The *.this-FetchGroups lead to bad programming style and are therefore deprecated, now. They should be removed soon!
	 */
    @Deprecated
    public static final String FETCH_GROUP_THIS_LOCAL_ACCOUNTANT_DELEGATE = "LocalAccountantDelegate.this";

    public static final String QUERY_GET_CHILD_DELEGATES = "getChildDelegates";

    /**
	 * @deprecated Only for JDO
	 */
    @Deprecated
    public LocalAccountantDelegate() {
    }

    /**
	 * @jdo.field primary-key="true"
	 * @jdo.column length="100"
	 */
    private String organisationID;

    /**
	 * @jdo.field primary-key="true"
	 * @jdo.column length="100"
	 */
    private String localAccountantDelegateID;

    /**
	 * @jdo.field persistence-modifier="persistent" dependent="true" mapped-by="localAccountantDelegate"
	 */
    private LocalAccountantDelegateName name;

    /**
	 * @jdo.field persistence-modifier="persistent"
	 */
    private LocalAccountantDelegate extendedAccountantDelegate;

    public LocalAccountantDelegate(String organisationID, String localAccountantDelegateID) {
        this.organisationID = organisationID;
        this.localAccountantDelegateID = localAccountantDelegateID;
        this.name = new LocalAccountantDelegateName(this);
    }

    public LocalAccountantDelegate(LocalAccountantDelegate parent, String organisationID, String localAccountantDelegateID) {
        this(organisationID, localAccountantDelegateID);
        this.extendedAccountantDelegate = parent;
    }

    /**
	 * @return Returns the organisationID.
	 */
    public String getOrganisationID() {
        return organisationID;
    }

    /**
	 */
    public String getLocalAccountantDelegateID() {
        return localAccountantDelegateID;
    }

    /**
	 * @return Returns the extendedAccountantDelegate.
	 */
    public LocalAccountantDelegate getExtendedAccountantDelegate() {
        return extendedAccountantDelegate;
    }

    /**
	 * @return Returns the name of this LocalAccountantDelegate
	 */
    public LocalAccountantDelegateName getName() {
        return name;
    }

    /**
	 * Book the article with the given article-price.
	 * A LocalAccountantDelegate should decide based on its
	 * configuration to which accounts the money is to be booked.
	 *
	 * Subclasses may delegate the work here to
	 * {@link #bookProductTypeParts(OrganisationLegalEntity, User, LinkedList, int, BookMoneyTransfer, Set)}
	 * and only provide new dimensions.
	 *
	 * @param mandator The organisation the LocalAccountant books for.
	 * @param user The user that initiated the booking.
	 * @param invoice The invoice that is currently booked.
	 * @param article The Article to book.
	 * @param container The Container transfer, that is the transfer from the customer to the vendor of the invoice
	 * @param involvedAnchors A List of involved Anchors, so they can be checked after the booking
	 */
    public abstract void bookArticle(OrganisationLegalEntity mandator, User user, Invoice invoice, Article article, BookMoneyTransfer container, Set<Anchor> involvedAnchors);

    /**
	 * Called by LocalAccountant before all articles of an invoice are booked.
	 * Gives the delegate the chance to initialize.
	 *
	 * @param mandator The mandator.
	 * @param user The user that initiated the book.
	 * @param invoice The invoice to book articles for.
	 * @param bookTransfer The container {@link BookMoneyTransfer}
	 * @param involvedAnchors A set of {@link Anchor} that should contain all involved ones when the book is finished.
	 */
    public void preBookArticles(OrganisationLegalEntity mandator, User user, Invoice invoice, BookMoneyTransfer bookTransfer, Set<Anchor> involvedAnchors) {
    }

    /**
	 * Called by LocalAccountant before all articles of an invoice are booked.
	 * Gives the delegate the chance to clean up.
	 *
	 * @param mandator The mandator.
	 * @param user The user that initiated the book.
	 * @param invoice The invoice to book articles for.
	 * @param bookTransfer The container {@link BookMoneyTransfer}
	 * @param involvedAnchors A set of {@link Anchor} that contains all involved ones now that the book is finished.
	 */
    public void postBookArticles(OrganisationLegalEntity mandator, User user, Invoice invoice, BookMoneyTransfer bookTransfer, Set<Anchor> involvedAnchors) {
    }

    /**
	 *
	 *
	 * @param mandator The mandator to book for.
	 * @param user The user that initiated the booking.
	 * @param articlePriceStack A Stack of ArticlePrices representing the ProductType packaging
	 * @param delegationLevel The level of delegation calls to this method
	 * @param container The Container transfer, that is the transfer from the customer to the vendor of the invoice
	 * @param involvedAnchors A List of involved Anchors, so they can be checked after the booking
	 */
    public abstract void bookProductTypeParts(OrganisationLegalEntity mandator, User user, LinkedList<ArticlePrice> articlePriceStack, int delegationLevel, BookMoneyTransfer container, Set<Anchor> involvedAnchors);

    /**
	 * @return The persistenceManager for this AccountantDelegate
	 */
    protected PersistenceManager getPersistenceManager() {
        PersistenceManager pm = JDOHelper.getPersistenceManager(this);
        if (pm == null) throw new IllegalStateException("This instance of MappingBasedAccountantDelegate is not persistent. Can't get PersistenceManager");
        return pm;
    }

    /**
	 * Helper method to get all LocalAccountantDelegates that are not inherited
	 * from another Delegate.
	 */
    @SuppressWarnings("unchecked")
    public static <T extends LocalAccountantDelegate> Collection<T> getTopLevelDelegates(PersistenceManager pm, Class<T> delegateClass) {
        Query q = pm.newQuery(pm.getExtent(delegateClass, false));
        q.setFilter("this.extendedAccountantDelegate == null");
        return (Collection<T>) q.execute();
    }

    /**
	 * Helper method to get all LocalAccountantDelegates that are children of
	 * the Delegate defined by the given organisationID and localAccountantDelegateID.
	 *
	 * @param pm The PersistenceManager to use.
	 * @param organisationID The organisationID of the parent delegate.
	 * @param localAccountantDelegateID The localAccountantDelegateID of the parent delegate
	 */
    @SuppressWarnings("unchecked")
    public static Collection<? extends LocalAccountantDelegate> getChildDelegates(PersistenceManager pm, String organisationID, String localAccountantDelegateID) {
        Query q = pm.newNamedQuery(LocalAccountantDelegate.class, QUERY_GET_CHILD_DELEGATES);
        return (Collection<? extends LocalAccountantDelegate>) q.execute(organisationID, localAccountantDelegateID);
    }
}
