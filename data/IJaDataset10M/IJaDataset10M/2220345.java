package quickfix.fix43;

import quickfix.FieldNotFound;
import quickfix.Group;
import quickfix.field.*;

public class IndicationOfInterest extends Message {

    public IndicationOfInterest() {
        getHeader().setField(new MsgType("6"));
    }

    public IndicationOfInterest(quickfix.field.IOIid aIOIid, quickfix.field.IOITransType aIOITransType, quickfix.field.Side aSide, quickfix.field.IOIQty aIOIQty) {
        getHeader().setField(new MsgType("6"));
        set(aIOIid);
        set(aIOITransType);
        set(aSide);
        set(aIOIQty);
    }

    public void set(quickfix.field.IOIid value) {
        setField(value);
    }

    public quickfix.field.IOIid get(quickfix.field.IOIid value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.IOIid getIOIid() throws FieldNotFound {
        quickfix.field.IOIid value = new quickfix.field.IOIid();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.IOIid field) {
        return isSetField(field);
    }

    public boolean isSetIOIid() {
        return isSetField(23);
    }

    public void set(quickfix.field.IOITransType value) {
        setField(value);
    }

    public quickfix.field.IOITransType get(quickfix.field.IOITransType value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.IOITransType getIOITransType() throws FieldNotFound {
        quickfix.field.IOITransType value = new quickfix.field.IOITransType();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.IOITransType field) {
        return isSetField(field);
    }

    public boolean isSetIOITransType() {
        return isSetField(28);
    }

    public void set(quickfix.field.IOIRefID value) {
        setField(value);
    }

    public quickfix.field.IOIRefID get(quickfix.field.IOIRefID value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.IOIRefID getIOIRefID() throws FieldNotFound {
        quickfix.field.IOIRefID value = new quickfix.field.IOIRefID();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.IOIRefID field) {
        return isSetField(field);
    }

    public boolean isSetIOIRefID() {
        return isSetField(26);
    }

    public void set(quickfix.field.Symbol value) {
        setField(value);
    }

    public quickfix.field.Symbol get(quickfix.field.Symbol value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.Symbol getSymbol() throws FieldNotFound {
        quickfix.field.Symbol value = new quickfix.field.Symbol();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.Symbol field) {
        return isSetField(field);
    }

    public boolean isSetSymbol() {
        return isSetField(55);
    }

    public void set(quickfix.field.SymbolSfx value) {
        setField(value);
    }

    public quickfix.field.SymbolSfx get(quickfix.field.SymbolSfx value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.SymbolSfx getSymbolSfx() throws FieldNotFound {
        quickfix.field.SymbolSfx value = new quickfix.field.SymbolSfx();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.SymbolSfx field) {
        return isSetField(field);
    }

    public boolean isSetSymbolSfx() {
        return isSetField(65);
    }

    public void set(quickfix.field.SecurityID value) {
        setField(value);
    }

    public quickfix.field.SecurityID get(quickfix.field.SecurityID value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.SecurityID getSecurityID() throws FieldNotFound {
        quickfix.field.SecurityID value = new quickfix.field.SecurityID();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.SecurityID field) {
        return isSetField(field);
    }

    public boolean isSetSecurityID() {
        return isSetField(48);
    }

    public void set(quickfix.field.SecurityIDSource value) {
        setField(value);
    }

    public quickfix.field.SecurityIDSource get(quickfix.field.SecurityIDSource value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.SecurityIDSource getSecurityIDSource() throws FieldNotFound {
        quickfix.field.SecurityIDSource value = new quickfix.field.SecurityIDSource();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.SecurityIDSource field) {
        return isSetField(field);
    }

    public boolean isSetSecurityIDSource() {
        return isSetField(22);
    }

    public void set(quickfix.field.Product value) {
        setField(value);
    }

    public quickfix.field.Product get(quickfix.field.Product value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.Product getProduct() throws FieldNotFound {
        quickfix.field.Product value = new quickfix.field.Product();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.Product field) {
        return isSetField(field);
    }

    public boolean isSetProduct() {
        return isSetField(460);
    }

    public void set(quickfix.field.CFICode value) {
        setField(value);
    }

    public quickfix.field.CFICode get(quickfix.field.CFICode value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.CFICode getCFICode() throws FieldNotFound {
        quickfix.field.CFICode value = new quickfix.field.CFICode();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.CFICode field) {
        return isSetField(field);
    }

    public boolean isSetCFICode() {
        return isSetField(461);
    }

    public void set(quickfix.field.SecurityType value) {
        setField(value);
    }

    public quickfix.field.SecurityType get(quickfix.field.SecurityType value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.SecurityType getSecurityType() throws FieldNotFound {
        quickfix.field.SecurityType value = new quickfix.field.SecurityType();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.SecurityType field) {
        return isSetField(field);
    }

    public boolean isSetSecurityType() {
        return isSetField(167);
    }

    public void set(quickfix.field.MaturityMonthYear value) {
        setField(value);
    }

    public quickfix.field.MaturityMonthYear get(quickfix.field.MaturityMonthYear value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.MaturityMonthYear getMaturityMonthYear() throws FieldNotFound {
        quickfix.field.MaturityMonthYear value = new quickfix.field.MaturityMonthYear();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.MaturityMonthYear field) {
        return isSetField(field);
    }

    public boolean isSetMaturityMonthYear() {
        return isSetField(200);
    }

    public void set(quickfix.field.MaturityDate value) {
        setField(value);
    }

    public quickfix.field.MaturityDate get(quickfix.field.MaturityDate value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.MaturityDate getMaturityDate() throws FieldNotFound {
        quickfix.field.MaturityDate value = new quickfix.field.MaturityDate();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.MaturityDate field) {
        return isSetField(field);
    }

    public boolean isSetMaturityDate() {
        return isSetField(541);
    }

    public void set(quickfix.field.CouponPaymentDate value) {
        setField(value);
    }

    public quickfix.field.CouponPaymentDate get(quickfix.field.CouponPaymentDate value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.CouponPaymentDate getCouponPaymentDate() throws FieldNotFound {
        quickfix.field.CouponPaymentDate value = new quickfix.field.CouponPaymentDate();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.CouponPaymentDate field) {
        return isSetField(field);
    }

    public boolean isSetCouponPaymentDate() {
        return isSetField(224);
    }

    public void set(quickfix.field.IssueDate value) {
        setField(value);
    }

    public quickfix.field.IssueDate get(quickfix.field.IssueDate value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.IssueDate getIssueDate() throws FieldNotFound {
        quickfix.field.IssueDate value = new quickfix.field.IssueDate();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.IssueDate field) {
        return isSetField(field);
    }

    public boolean isSetIssueDate() {
        return isSetField(225);
    }

    public void set(quickfix.field.RepoCollateralSecurityType value) {
        setField(value);
    }

    public quickfix.field.RepoCollateralSecurityType get(quickfix.field.RepoCollateralSecurityType value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.RepoCollateralSecurityType getRepoCollateralSecurityType() throws FieldNotFound {
        quickfix.field.RepoCollateralSecurityType value = new quickfix.field.RepoCollateralSecurityType();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.RepoCollateralSecurityType field) {
        return isSetField(field);
    }

    public boolean isSetRepoCollateralSecurityType() {
        return isSetField(239);
    }

    public void set(quickfix.field.RepurchaseTerm value) {
        setField(value);
    }

    public quickfix.field.RepurchaseTerm get(quickfix.field.RepurchaseTerm value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.RepurchaseTerm getRepurchaseTerm() throws FieldNotFound {
        quickfix.field.RepurchaseTerm value = new quickfix.field.RepurchaseTerm();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.RepurchaseTerm field) {
        return isSetField(field);
    }

    public boolean isSetRepurchaseTerm() {
        return isSetField(226);
    }

    public void set(quickfix.field.RepurchaseRate value) {
        setField(value);
    }

    public quickfix.field.RepurchaseRate get(quickfix.field.RepurchaseRate value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.RepurchaseRate getRepurchaseRate() throws FieldNotFound {
        quickfix.field.RepurchaseRate value = new quickfix.field.RepurchaseRate();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.RepurchaseRate field) {
        return isSetField(field);
    }

    public boolean isSetRepurchaseRate() {
        return isSetField(227);
    }

    public void set(quickfix.field.Factor value) {
        setField(value);
    }

    public quickfix.field.Factor get(quickfix.field.Factor value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.Factor getFactor() throws FieldNotFound {
        quickfix.field.Factor value = new quickfix.field.Factor();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.Factor field) {
        return isSetField(field);
    }

    public boolean isSetFactor() {
        return isSetField(228);
    }

    public void set(quickfix.field.CreditRating value) {
        setField(value);
    }

    public quickfix.field.CreditRating get(quickfix.field.CreditRating value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.CreditRating getCreditRating() throws FieldNotFound {
        quickfix.field.CreditRating value = new quickfix.field.CreditRating();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.CreditRating field) {
        return isSetField(field);
    }

    public boolean isSetCreditRating() {
        return isSetField(255);
    }

    public void set(quickfix.field.InstrRegistry value) {
        setField(value);
    }

    public quickfix.field.InstrRegistry get(quickfix.field.InstrRegistry value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.InstrRegistry getInstrRegistry() throws FieldNotFound {
        quickfix.field.InstrRegistry value = new quickfix.field.InstrRegistry();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.InstrRegistry field) {
        return isSetField(field);
    }

    public boolean isSetInstrRegistry() {
        return isSetField(543);
    }

    public void set(quickfix.field.CountryOfIssue value) {
        setField(value);
    }

    public quickfix.field.CountryOfIssue get(quickfix.field.CountryOfIssue value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.CountryOfIssue getCountryOfIssue() throws FieldNotFound {
        quickfix.field.CountryOfIssue value = new quickfix.field.CountryOfIssue();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.CountryOfIssue field) {
        return isSetField(field);
    }

    public boolean isSetCountryOfIssue() {
        return isSetField(470);
    }

    public void set(quickfix.field.StateOrProvinceOfIssue value) {
        setField(value);
    }

    public quickfix.field.StateOrProvinceOfIssue get(quickfix.field.StateOrProvinceOfIssue value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.StateOrProvinceOfIssue getStateOrProvinceOfIssue() throws FieldNotFound {
        quickfix.field.StateOrProvinceOfIssue value = new quickfix.field.StateOrProvinceOfIssue();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.StateOrProvinceOfIssue field) {
        return isSetField(field);
    }

    public boolean isSetStateOrProvinceOfIssue() {
        return isSetField(471);
    }

    public void set(quickfix.field.LocaleOfIssue value) {
        setField(value);
    }

    public quickfix.field.LocaleOfIssue get(quickfix.field.LocaleOfIssue value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.LocaleOfIssue getLocaleOfIssue() throws FieldNotFound {
        quickfix.field.LocaleOfIssue value = new quickfix.field.LocaleOfIssue();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.LocaleOfIssue field) {
        return isSetField(field);
    }

    public boolean isSetLocaleOfIssue() {
        return isSetField(472);
    }

    public void set(quickfix.field.RedemptionDate value) {
        setField(value);
    }

    public quickfix.field.RedemptionDate get(quickfix.field.RedemptionDate value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.RedemptionDate getRedemptionDate() throws FieldNotFound {
        quickfix.field.RedemptionDate value = new quickfix.field.RedemptionDate();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.RedemptionDate field) {
        return isSetField(field);
    }

    public boolean isSetRedemptionDate() {
        return isSetField(240);
    }

    public void set(quickfix.field.StrikePrice value) {
        setField(value);
    }

    public quickfix.field.StrikePrice get(quickfix.field.StrikePrice value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.StrikePrice getStrikePrice() throws FieldNotFound {
        quickfix.field.StrikePrice value = new quickfix.field.StrikePrice();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.StrikePrice field) {
        return isSetField(field);
    }

    public boolean isSetStrikePrice() {
        return isSetField(202);
    }

    public void set(quickfix.field.OptAttribute value) {
        setField(value);
    }

    public quickfix.field.OptAttribute get(quickfix.field.OptAttribute value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.OptAttribute getOptAttribute() throws FieldNotFound {
        quickfix.field.OptAttribute value = new quickfix.field.OptAttribute();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.OptAttribute field) {
        return isSetField(field);
    }

    public boolean isSetOptAttribute() {
        return isSetField(206);
    }

    public void set(quickfix.field.ContractMultiplier value) {
        setField(value);
    }

    public quickfix.field.ContractMultiplier get(quickfix.field.ContractMultiplier value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.ContractMultiplier getContractMultiplier() throws FieldNotFound {
        quickfix.field.ContractMultiplier value = new quickfix.field.ContractMultiplier();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.ContractMultiplier field) {
        return isSetField(field);
    }

    public boolean isSetContractMultiplier() {
        return isSetField(231);
    }

    public void set(quickfix.field.CouponRate value) {
        setField(value);
    }

    public quickfix.field.CouponRate get(quickfix.field.CouponRate value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.CouponRate getCouponRate() throws FieldNotFound {
        quickfix.field.CouponRate value = new quickfix.field.CouponRate();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.CouponRate field) {
        return isSetField(field);
    }

    public boolean isSetCouponRate() {
        return isSetField(223);
    }

    public void set(quickfix.field.SecurityExchange value) {
        setField(value);
    }

    public quickfix.field.SecurityExchange get(quickfix.field.SecurityExchange value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.SecurityExchange getSecurityExchange() throws FieldNotFound {
        quickfix.field.SecurityExchange value = new quickfix.field.SecurityExchange();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.SecurityExchange field) {
        return isSetField(field);
    }

    public boolean isSetSecurityExchange() {
        return isSetField(207);
    }

    public void set(quickfix.field.Issuer value) {
        setField(value);
    }

    public quickfix.field.Issuer get(quickfix.field.Issuer value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.Issuer getIssuer() throws FieldNotFound {
        quickfix.field.Issuer value = new quickfix.field.Issuer();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.Issuer field) {
        return isSetField(field);
    }

    public boolean isSetIssuer() {
        return isSetField(106);
    }

    public void set(quickfix.field.EncodedIssuerLen value) {
        setField(value);
    }

    public quickfix.field.EncodedIssuerLen get(quickfix.field.EncodedIssuerLen value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.EncodedIssuerLen getEncodedIssuerLen() throws FieldNotFound {
        quickfix.field.EncodedIssuerLen value = new quickfix.field.EncodedIssuerLen();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.EncodedIssuerLen field) {
        return isSetField(field);
    }

    public boolean isSetEncodedIssuerLen() {
        return isSetField(348);
    }

    public void set(quickfix.field.EncodedIssuer value) {
        setField(value);
    }

    public quickfix.field.EncodedIssuer get(quickfix.field.EncodedIssuer value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.EncodedIssuer getEncodedIssuer() throws FieldNotFound {
        quickfix.field.EncodedIssuer value = new quickfix.field.EncodedIssuer();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.EncodedIssuer field) {
        return isSetField(field);
    }

    public boolean isSetEncodedIssuer() {
        return isSetField(349);
    }

    public void set(quickfix.field.SecurityDesc value) {
        setField(value);
    }

    public quickfix.field.SecurityDesc get(quickfix.field.SecurityDesc value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.SecurityDesc getSecurityDesc() throws FieldNotFound {
        quickfix.field.SecurityDesc value = new quickfix.field.SecurityDesc();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.SecurityDesc field) {
        return isSetField(field);
    }

    public boolean isSetSecurityDesc() {
        return isSetField(107);
    }

    public void set(quickfix.field.EncodedSecurityDescLen value) {
        setField(value);
    }

    public quickfix.field.EncodedSecurityDescLen get(quickfix.field.EncodedSecurityDescLen value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.EncodedSecurityDescLen getEncodedSecurityDescLen() throws FieldNotFound {
        quickfix.field.EncodedSecurityDescLen value = new quickfix.field.EncodedSecurityDescLen();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.EncodedSecurityDescLen field) {
        return isSetField(field);
    }

    public boolean isSetEncodedSecurityDescLen() {
        return isSetField(350);
    }

    public void set(quickfix.field.EncodedSecurityDesc value) {
        setField(value);
    }

    public quickfix.field.EncodedSecurityDesc get(quickfix.field.EncodedSecurityDesc value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.EncodedSecurityDesc getEncodedSecurityDesc() throws FieldNotFound {
        quickfix.field.EncodedSecurityDesc value = new quickfix.field.EncodedSecurityDesc();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.EncodedSecurityDesc field) {
        return isSetField(field);
    }

    public boolean isSetEncodedSecurityDesc() {
        return isSetField(351);
    }

    public void set(quickfix.field.NoSecurityAltID value) {
        setField(value);
    }

    public quickfix.field.NoSecurityAltID get(quickfix.field.NoSecurityAltID value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.NoSecurityAltID getNoSecurityAltID() throws FieldNotFound {
        quickfix.field.NoSecurityAltID value = new quickfix.field.NoSecurityAltID();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.NoSecurityAltID field) {
        return isSetField(field);
    }

    public boolean isSetNoSecurityAltID() {
        return isSetField(454);
    }

    public static class NoSecurityAltID extends Group {

        public NoSecurityAltID() {
            super(454, 455, new int[] { 455, 456, 0 });
        }

        public void set(quickfix.field.SecurityAltID value) {
            setField(value);
        }

        public quickfix.field.SecurityAltID get(quickfix.field.SecurityAltID value) throws FieldNotFound {
            getField(value);
            return value;
        }

        public quickfix.field.SecurityAltID getSecurityAltID() throws FieldNotFound {
            quickfix.field.SecurityAltID value = new quickfix.field.SecurityAltID();
            getField(value);
            return value;
        }

        public boolean isSet(quickfix.field.SecurityAltID field) {
            return isSetField(field);
        }

        public boolean isSetSecurityAltID() {
            return isSetField(455);
        }

        public void set(quickfix.field.SecurityAltIDSource value) {
            setField(value);
        }

        public quickfix.field.SecurityAltIDSource get(quickfix.field.SecurityAltIDSource value) throws FieldNotFound {
            getField(value);
            return value;
        }

        public quickfix.field.SecurityAltIDSource getSecurityAltIDSource() throws FieldNotFound {
            quickfix.field.SecurityAltIDSource value = new quickfix.field.SecurityAltIDSource();
            getField(value);
            return value;
        }

        public boolean isSet(quickfix.field.SecurityAltIDSource field) {
            return isSetField(field);
        }

        public boolean isSetSecurityAltIDSource() {
            return isSetField(456);
        }
    }

    public void set(quickfix.field.Side value) {
        setField(value);
    }

    public quickfix.field.Side get(quickfix.field.Side value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.Side getSide() throws FieldNotFound {
        quickfix.field.Side value = new quickfix.field.Side();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.Side field) {
        return isSetField(field);
    }

    public boolean isSetSide() {
        return isSetField(54);
    }

    public void set(quickfix.field.QuantityType value) {
        setField(value);
    }

    public quickfix.field.QuantityType get(quickfix.field.QuantityType value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.QuantityType getQuantityType() throws FieldNotFound {
        quickfix.field.QuantityType value = new quickfix.field.QuantityType();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.QuantityType field) {
        return isSetField(field);
    }

    public boolean isSetQuantityType() {
        return isSetField(465);
    }

    public void set(quickfix.field.IOIQty value) {
        setField(value);
    }

    public quickfix.field.IOIQty get(quickfix.field.IOIQty value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.IOIQty getIOIQty() throws FieldNotFound {
        quickfix.field.IOIQty value = new quickfix.field.IOIQty();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.IOIQty field) {
        return isSetField(field);
    }

    public boolean isSetIOIQty() {
        return isSetField(27);
    }

    public void set(quickfix.field.PriceType value) {
        setField(value);
    }

    public quickfix.field.PriceType get(quickfix.field.PriceType value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.PriceType getPriceType() throws FieldNotFound {
        quickfix.field.PriceType value = new quickfix.field.PriceType();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.PriceType field) {
        return isSetField(field);
    }

    public boolean isSetPriceType() {
        return isSetField(423);
    }

    public void set(quickfix.field.Price value) {
        setField(value);
    }

    public quickfix.field.Price get(quickfix.field.Price value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.Price getPrice() throws FieldNotFound {
        quickfix.field.Price value = new quickfix.field.Price();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.Price field) {
        return isSetField(field);
    }

    public boolean isSetPrice() {
        return isSetField(44);
    }

    public void set(quickfix.field.Currency value) {
        setField(value);
    }

    public quickfix.field.Currency get(quickfix.field.Currency value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.Currency getCurrency() throws FieldNotFound {
        quickfix.field.Currency value = new quickfix.field.Currency();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.Currency field) {
        return isSetField(field);
    }

    public boolean isSetCurrency() {
        return isSetField(15);
    }

    public void set(quickfix.field.ValidUntilTime value) {
        setField(value);
    }

    public quickfix.field.ValidUntilTime get(quickfix.field.ValidUntilTime value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.ValidUntilTime getValidUntilTime() throws FieldNotFound {
        quickfix.field.ValidUntilTime value = new quickfix.field.ValidUntilTime();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.ValidUntilTime field) {
        return isSetField(field);
    }

    public boolean isSetValidUntilTime() {
        return isSetField(62);
    }

    public void set(quickfix.field.IOIQltyInd value) {
        setField(value);
    }

    public quickfix.field.IOIQltyInd get(quickfix.field.IOIQltyInd value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.IOIQltyInd getIOIQltyInd() throws FieldNotFound {
        quickfix.field.IOIQltyInd value = new quickfix.field.IOIQltyInd();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.IOIQltyInd field) {
        return isSetField(field);
    }

    public boolean isSetIOIQltyInd() {
        return isSetField(25);
    }

    public void set(quickfix.field.IOINaturalFlag value) {
        setField(value);
    }

    public quickfix.field.IOINaturalFlag get(quickfix.field.IOINaturalFlag value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.IOINaturalFlag getIOINaturalFlag() throws FieldNotFound {
        quickfix.field.IOINaturalFlag value = new quickfix.field.IOINaturalFlag();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.IOINaturalFlag field) {
        return isSetField(field);
    }

    public boolean isSetIOINaturalFlag() {
        return isSetField(130);
    }

    public void set(quickfix.field.Text value) {
        setField(value);
    }

    public quickfix.field.Text get(quickfix.field.Text value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.Text getText() throws FieldNotFound {
        quickfix.field.Text value = new quickfix.field.Text();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.Text field) {
        return isSetField(field);
    }

    public boolean isSetText() {
        return isSetField(58);
    }

    public void set(quickfix.field.EncodedTextLen value) {
        setField(value);
    }

    public quickfix.field.EncodedTextLen get(quickfix.field.EncodedTextLen value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.EncodedTextLen getEncodedTextLen() throws FieldNotFound {
        quickfix.field.EncodedTextLen value = new quickfix.field.EncodedTextLen();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.EncodedTextLen field) {
        return isSetField(field);
    }

    public boolean isSetEncodedTextLen() {
        return isSetField(354);
    }

    public void set(quickfix.field.EncodedText value) {
        setField(value);
    }

    public quickfix.field.EncodedText get(quickfix.field.EncodedText value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.EncodedText getEncodedText() throws FieldNotFound {
        quickfix.field.EncodedText value = new quickfix.field.EncodedText();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.EncodedText field) {
        return isSetField(field);
    }

    public boolean isSetEncodedText() {
        return isSetField(355);
    }

    public void set(quickfix.field.TransactTime value) {
        setField(value);
    }

    public quickfix.field.TransactTime get(quickfix.field.TransactTime value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.TransactTime getTransactTime() throws FieldNotFound {
        quickfix.field.TransactTime value = new quickfix.field.TransactTime();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.TransactTime field) {
        return isSetField(field);
    }

    public boolean isSetTransactTime() {
        return isSetField(60);
    }

    public void set(quickfix.field.URLLink value) {
        setField(value);
    }

    public quickfix.field.URLLink get(quickfix.field.URLLink value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.URLLink getURLLink() throws FieldNotFound {
        quickfix.field.URLLink value = new quickfix.field.URLLink();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.URLLink field) {
        return isSetField(field);
    }

    public boolean isSetURLLink() {
        return isSetField(149);
    }

    public void set(quickfix.field.Spread value) {
        setField(value);
    }

    public quickfix.field.Spread get(quickfix.field.Spread value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.Spread getSpread() throws FieldNotFound {
        quickfix.field.Spread value = new quickfix.field.Spread();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.Spread field) {
        return isSetField(field);
    }

    public boolean isSetSpread() {
        return isSetField(218);
    }

    public void set(quickfix.field.BenchmarkCurveCurrency value) {
        setField(value);
    }

    public quickfix.field.BenchmarkCurveCurrency get(quickfix.field.BenchmarkCurveCurrency value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.BenchmarkCurveCurrency getBenchmarkCurveCurrency() throws FieldNotFound {
        quickfix.field.BenchmarkCurveCurrency value = new quickfix.field.BenchmarkCurveCurrency();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.BenchmarkCurveCurrency field) {
        return isSetField(field);
    }

    public boolean isSetBenchmarkCurveCurrency() {
        return isSetField(220);
    }

    public void set(quickfix.field.BenchmarkCurveName value) {
        setField(value);
    }

    public quickfix.field.BenchmarkCurveName get(quickfix.field.BenchmarkCurveName value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.BenchmarkCurveName getBenchmarkCurveName() throws FieldNotFound {
        quickfix.field.BenchmarkCurveName value = new quickfix.field.BenchmarkCurveName();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.BenchmarkCurveName field) {
        return isSetField(field);
    }

    public boolean isSetBenchmarkCurveName() {
        return isSetField(221);
    }

    public void set(quickfix.field.BenchmarkCurvePoint value) {
        setField(value);
    }

    public quickfix.field.BenchmarkCurvePoint get(quickfix.field.BenchmarkCurvePoint value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.BenchmarkCurvePoint getBenchmarkCurvePoint() throws FieldNotFound {
        quickfix.field.BenchmarkCurvePoint value = new quickfix.field.BenchmarkCurvePoint();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.BenchmarkCurvePoint field) {
        return isSetField(field);
    }

    public boolean isSetBenchmarkCurvePoint() {
        return isSetField(222);
    }

    public void set(quickfix.field.Benchmark value) {
        setField(value);
    }

    public quickfix.field.Benchmark get(quickfix.field.Benchmark value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.Benchmark getBenchmark() throws FieldNotFound {
        quickfix.field.Benchmark value = new quickfix.field.Benchmark();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.Benchmark field) {
        return isSetField(field);
    }

    public boolean isSetBenchmark() {
        return isSetField(219);
    }

    public void set(quickfix.field.NoIOIQualifiers value) {
        setField(value);
    }

    public quickfix.field.NoIOIQualifiers get(quickfix.field.NoIOIQualifiers value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.NoIOIQualifiers getNoIOIQualifiers() throws FieldNotFound {
        quickfix.field.NoIOIQualifiers value = new quickfix.field.NoIOIQualifiers();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.NoIOIQualifiers field) {
        return isSetField(field);
    }

    public boolean isSetNoIOIQualifiers() {
        return isSetField(199);
    }

    public static class NoIOIQualifiers extends Group {

        public NoIOIQualifiers() {
            super(199, 104, new int[] { 104, 0 });
        }

        public void set(quickfix.field.IOIQualifier value) {
            setField(value);
        }

        public quickfix.field.IOIQualifier get(quickfix.field.IOIQualifier value) throws FieldNotFound {
            getField(value);
            return value;
        }

        public quickfix.field.IOIQualifier getIOIQualifier() throws FieldNotFound {
            quickfix.field.IOIQualifier value = new quickfix.field.IOIQualifier();
            getField(value);
            return value;
        }

        public boolean isSet(quickfix.field.IOIQualifier field) {
            return isSetField(field);
        }

        public boolean isSetIOIQualifier() {
            return isSetField(104);
        }
    }

    public void set(quickfix.field.NoRoutingIDs value) {
        setField(value);
    }

    public quickfix.field.NoRoutingIDs get(quickfix.field.NoRoutingIDs value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public quickfix.field.NoRoutingIDs getNoRoutingIDs() throws FieldNotFound {
        quickfix.field.NoRoutingIDs value = new quickfix.field.NoRoutingIDs();
        getField(value);
        return value;
    }

    public boolean isSet(quickfix.field.NoRoutingIDs field) {
        return isSetField(field);
    }

    public boolean isSetNoRoutingIDs() {
        return isSetField(215);
    }

    public static class NoRoutingIDs extends Group {

        public NoRoutingIDs() {
            super(215, 216, new int[] { 216, 217, 0 });
        }

        public void set(quickfix.field.RoutingType value) {
            setField(value);
        }

        public quickfix.field.RoutingType get(quickfix.field.RoutingType value) throws FieldNotFound {
            getField(value);
            return value;
        }

        public quickfix.field.RoutingType getRoutingType() throws FieldNotFound {
            quickfix.field.RoutingType value = new quickfix.field.RoutingType();
            getField(value);
            return value;
        }

        public boolean isSet(quickfix.field.RoutingType field) {
            return isSetField(field);
        }

        public boolean isSetRoutingType() {
            return isSetField(216);
        }

        public void set(quickfix.field.RoutingID value) {
            setField(value);
        }

        public quickfix.field.RoutingID get(quickfix.field.RoutingID value) throws FieldNotFound {
            getField(value);
            return value;
        }

        public quickfix.field.RoutingID getRoutingID() throws FieldNotFound {
            quickfix.field.RoutingID value = new quickfix.field.RoutingID();
            getField(value);
            return value;
        }

        public boolean isSet(quickfix.field.RoutingID field) {
            return isSetField(field);
        }

        public boolean isSetRoutingID() {
            return isSetField(217);
        }
    }
}
