package org.nightlabs.jfire.accounting;

import java.io.Serializable;
import org.nightlabs.util.Util;

/**
 * @author Marco Schulze - marco at nightlabs dot de
 * 
 * @jdo.persistence-capable
 *		identity-type = "application"
 *		objectid-class = "org.nightlabs.jfire.accounting.id.CurrencyID"
 *		detachable = "true"
 *		table="JFireTrade_Currency"
 *
 * @jdo.create-objectid-class
 *
 * @jdo.inheritance strategy = "new-table"
 */
public class Currency implements Serializable, org.nightlabs.l10n.Currency {

    private static final long serialVersionUID = 1L;

    /**
	 * This is the ISO 4217 identifier for the currency. Usually a two or three-letter-abbreviation.
	 *
	 * @jdo.field primary-key="true"
	 * @jdo.column length="100"
	 */
    private String currencyID;

    /**
	 * @jdo.field persistence-modifier="persistent" null-value="exception"
	 */
    private String currencySymbol;

    /**
	 * @jdo.field persistence-modifier="persistent"
	 */
    private int decimalDigitCount = -1;

    protected Currency() {
    }

    public Currency(String currencyID, String currencySymbol, int decimalDigitCount) {
        if (currencyID == null) throw new IllegalArgumentException("currencyID must not be null!");
        if (currencySymbol == null) throw new IllegalArgumentException("currencySymbol must not be null!");
        if (decimalDigitCount < 0) throw new IllegalArgumentException("decimalDigitCount must be >= 0! It is: " + decimalDigitCount);
        this.currencyID = currencyID;
        this.currencySymbol = currencySymbol;
        this.decimalDigitCount = decimalDigitCount;
    }

    /**
	 * @return Returns the currencyID.
	 */
    public String getCurrencyID() {
        return currencyID;
    }

    /**
	 * @see org.nightlabs.l10n.Currency#getDecimalDigitCount()
	 */
    public int getDecimalDigitCount() {
        return decimalDigitCount;
    }

    /**
	 * @see org.nightlabs.l10n.Currency#getCurrencySymbol()
	 */
    public String getCurrencySymbol() {
        return currencySymbol;
    }

    /**
	 * @param currencySymbol The currencySymbol to set.
	 */
    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Currency)) return false;
        Currency o = (Currency) obj;
        return Util.equals(o.currencyID, this.currencyID);
    }

    @Override
    public int hashCode() {
        return Util.hashCode(currencyID);
    }

    /**
	 * Returns the given amount in the double value of this currency.
	 * <p>
	 *   amount / 10^(decimalDigitCount)
	 * <p>
	 * 
	 * @param amount The amount to convert
	 * @return the approximate value as double - there might be rounding differences.
	 */
    public double toDouble(long amount) {
        return amount / Math.pow(10, getDecimalDigitCount());
    }

    /**
	 * Convert the given amount to the long value of this currency.
	 * <p>
	 *   amount * 10^(decimalDigitCount)
	 * <p>
	 * 
	 * @param amount The amount to convert
	 * @return the approximate value as long - there might be rounding differences.
	 */
    public long toLong(double amount) {
        return (long) (amount * Math.pow(10, getDecimalDigitCount()));
    }
}
