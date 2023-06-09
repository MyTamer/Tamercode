package org.posterita.businesslogic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import org.compiere.model.MBPBankAccount;
import org.compiere.model.MBPartner;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MCountry;
import org.compiere.model.MLocation;
import org.compiere.model.MOrg;
import org.compiere.model.MRegion;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.posterita.beans.AddressBean;
import org.posterita.beans.BPartnerBean;
import org.posterita.core.TrxPrefix;
import org.posterita.exceptions.OperationException;
import org.posterita.lib.UdiConstants;
import org.posterita.model.UDIMBPBankAccount;
import org.posterita.model.UDIMBPartner;
import org.posterita.model.UDIMBPartnerLocation;
import org.posterita.util.PoManager;

public class BPartnerManager {

    public static MBPartner activateBPartner(Properties ctx, int bpartnerId, boolean isActive, String trxName) throws OperationException {
        MBPartner bpartner = new MBPartner(ctx, bpartnerId, trxName);
        if (bpartner == null) throw new OperationException("Business Partner does not exist!");
        if (isActive == true) {
            bpartner.setIsActive(false);
        } else {
            bpartner.setIsActive(true);
        }
        UDIMBPartner ubpartner = new UDIMBPartner(bpartner);
        ubpartner.save();
        return ubpartner.getMBPartner();
    }

    public static MBPartner createBPartner(Properties ctx, int parentBpId, String bPName, String name2, boolean isCustomer, boolean isVendor, boolean isEmployee, boolean isSalesRep, String address1, String address2, String postalAddress1, Integer regionId, String city, String bpPhone, String fax, int countryId, boolean isShipTo, boolean isBillTo, String trxName) throws OperationException {
        return saveBPartner(ctx, 0, parentBpId, bPName, name2, isCustomer, isVendor, isEmployee, isSalesRep, address1, address2, postalAddress1, regionId, city, bpPhone, "", fax, countryId, isShipTo, isBillTo, trxName);
    }

    public static MBPartner createBPartner(Properties ctx, int parentBpId, String bPName, String name2, boolean isCustomer, boolean isVendor, boolean isEmployee, boolean isSalesRep, String address1, String postalAddress1, String city, String bpPhone, int countryId, String trxName) throws OperationException {
        return saveBPartner(ctx, 0, parentBpId, bPName, name2, isCustomer, isVendor, isEmployee, isSalesRep, address1, "", postalAddress1, null, city, bpPhone, "", "", countryId, true, true, trxName);
    }

    public static MBPBankAccount createBPBankAcc(Properties ctx, int bPartnerId, String aName, String city, String street, String trxName) throws OperationException {
        MBPBankAccount bankAcc = new MBPBankAccount(ctx, 0, trxName);
        try {
            bankAcc.setC_BPartner_ID(bPartnerId);
            bankAcc.setA_Name(aName);
            bankAcc.setA_Street(street);
            bankAcc.setA_City(city);
            UDIMBPBankAccount udiBPAcc = new UDIMBPBankAccount(bankAcc);
            udiBPAcc.save();
        } catch (OperationException e) {
            throw new OperationException("Cannot create BP Bank Account!");
        }
        return bankAcc;
    }

    public static MBPartner createLinkedBPartner(Properties ctx, int parentBpId, String bPName, String name2, boolean isCustomer, boolean isVendor, boolean isEmployee, boolean isSalesRep, String address1, String postalAddress1, String city, String bpPhone, int countryId, String trxName) throws OperationException {
        MBPartner bpartner = saveBPartner(ctx, 0, parentBpId, bPName, name2, isCustomer, isVendor, isEmployee, isSalesRep, address1, "", postalAddress1, null, city, bpPhone, "", "", countryId, true, true, trxName);
        int orgId = Env.getAD_Org_ID(ctx);
        bpartner.setAD_OrgBP_ID(orgId);
        UDIMBPartner udiBPartner = new UDIMBPartner(bpartner);
        udiBPartner.save();
        return bpartner;
    }

    public static MBPartner createShippingBPartner(Properties ctx, int parentBpId, String bPName, String name2, boolean isCustomer, boolean isVendor, boolean isEmployee, boolean isSalesRep, String address1, String postalAddress1, String city, String bpPhone, int countryId, boolean isShipTo, boolean isBillTo, String trxName) throws OperationException {
        return saveBPartner(ctx, 0, parentBpId, bPName, name2, isCustomer, isVendor, isEmployee, isSalesRep, address1, "", postalAddress1, null, city, bpPhone, "", "", countryId, isShipTo, isBillTo, trxName);
    }

    public static MBPartner deactivateBPartner(Properties ctx, int bpartnerId, String trxName) throws OperationException {
        MBPartner bpartner = new MBPartner(ctx, bpartnerId, trxName);
        if (bpartner == null) throw new OperationException("Business Partner does not exist!");
        bpartner.setIsActive(false);
        UDIMBPartner ubpartner = new UDIMBPartner(bpartner);
        ubpartner.save();
        return ubpartner.getMBPartner();
    }

    public static ArrayList<BPartnerBean> getAllBpartners(Properties ctx, String trxName) throws OperationException {
        int adClientID = Env.getAD_Client_ID(ctx);
        String sql;
        sql = " select bp.c_bpartner_id, " + " bp.name," + " bp.name2," + " bp.isactive," + " bp.iscustomer," + " bp.isemployee," + " bp.isvendor, " + " bp.issalesrep," + " cl.address1," + " cl.address2," + " cl.city," + " cl.postal_add," + " bpl.c_bpartner_id," + " cl.c_location_id," + " bpl.phone," + " bpl.fax," + " cl.REGIONNAME " + " from  C_BPARTNER bp left outer join (c_bpartner_location bpl left outer join c_location cl on cl.c_location_id=bpl.c_location_id) on bpl.c_bpartner_id = bp.c_bpartner_id, AD_ORG org " + " where bp.ad_org_id = org.ad_org_id and " + " bp.AD_CLIENT_ID = " + adClientID + " order by bp.name";
        PreparedStatement pstmt = null;
        System.out.println(sql);
        BPartnerBean bpartner = null;
        ResultSet rs = null;
        ArrayList<BPartnerBean> bpartners = new ArrayList<BPartnerBean>();
        try {
            pstmt = DB.prepareStatement(sql, trxName);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                bpartner = new BPartnerBean();
                bpartner.setBpartnerId(Integer.valueOf(rs.getInt(1)));
                bpartner.setPartnerName(rs.getString(2));
                bpartner.setName2(rs.getString(3));
                if (rs.getString(4).equals("Y")) {
                    bpartner.setIsActive(Boolean.valueOf(true));
                } else bpartner.setIsActive(Boolean.valueOf(false));
                if (rs.getString(5).equals("Y")) {
                    bpartner.setIsCustomer(Boolean.valueOf(true));
                } else bpartner.setIsCustomer(Boolean.valueOf(false));
                if (rs.getString(6).equals("Y")) {
                    bpartner.setIsEmployee(Boolean.valueOf(true));
                } else bpartner.setIsEmployee(Boolean.valueOf(false));
                if (rs.getString(7).equals("Y")) {
                    bpartner.setIsVendor(Boolean.valueOf(true));
                } else bpartner.setIsVendor(Boolean.valueOf(false));
                if (rs.getString(8).equals("Y")) {
                    bpartner.setIsSalesRep(Boolean.valueOf(true));
                } else bpartner.setIsSalesRep(Boolean.valueOf(false));
                bpartner.setAddress1(rs.getString(9));
                bpartner.setAddress2(rs.getString(10));
                bpartner.setCity(rs.getString(11));
                bpartner.setPostalAddress(rs.getString(12));
                bpartner.setPhone(rs.getString(15));
                bpartner.setFax(rs.getString(16));
                String regionName = rs.getString(17);
                int regionId = 0;
                int id[] = MRegion.getAllIDs(MRegion.Table_Name, " name= '" + regionName + "'", trxName);
                if (id.length != 0) {
                    regionId = id[0];
                }
                bpartner.setRegionId(Integer.valueOf(regionId));
                bpartners.add(bpartner);
            }
            rs.close();
        } catch (SQLException e) {
            throw new OperationException(e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (Exception ex) {
            }
            pstmt = null;
        }
        return bpartners;
    }

    public static BPartnerBean getBpartner(Properties ctx, int bPartnerId, String trxName) throws OperationException {
        ArrayList bPartnerList = getAllBpartners(ctx, trxName);
        return getBpartner(ctx, Integer.valueOf(bPartnerId), bPartnerList);
    }

    public static BPartnerBean getBpartner(Properties ctx, Integer bPartnerId, ArrayList bpartners) {
        Iterator iter = (bpartners.iterator());
        while (iter.hasNext()) {
            BPartnerBean bpartnerBean = (BPartnerBean) iter.next();
            if (bpartnerBean.getBpartnerId().equals(bPartnerId)) return bpartnerBean;
        }
        return new BPartnerBean();
    }

    public static MBPartnerLocation getBPartnerSingleLocation(Properties ctx, int bpartnerId) throws OperationException {
        MBPartnerLocation bpartnerLocations[] = MBPartnerLocation.getForBPartner(ctx, bpartnerId);
        if (bpartnerLocations.length == 0) throw new OperationException("No location found business partner with id: " + bpartnerId); else if (bpartnerLocations.length > 1) throw new OperationException("More than 1 location found for business partner with id: " + bpartnerId); else return bpartnerLocations[0];
    }

    public static MBPartner getCreateLinkedBPartner(Properties ctx, MOrg org, String trxName) throws OperationException {
        MBPartner linkedBPartner;
        if (org == null) throw new OperationException("Organisation cannot be null");
        int linkedBPId = org.getLinkedC_BPartner_ID(trxName);
        if (linkedBPId != 0) {
            linkedBPartner = loadBPartner(ctx, linkedBPId, trxName);
            return linkedBPartner;
        } else {
            linkedBPartner = BPartnerManager.saveBPartner(ctx, 0, 0, org.getName(), "", false, false, false, false, " ", " ", " ", null, "", "", "", "", MCountry.getDefault(ctx).get_ID(), true, true, trxName);
            linkedBPartner.setAD_OrgBP_ID(org.get_ID());
            PoManager.save(linkedBPartner, trxName);
            return linkedBPartner;
        }
    }

    public static ArrayList<AddressBean> getMyBPartners(Properties ctx, int partnerId, String trxName) throws OperationException {
        ArrayList<AddressBean> bpartnerList = new ArrayList<AddressBean>();
        if (partnerId == 0) throw new OperationException("Parent business partner not present!");
        MBPartner me = new MBPartner(ctx, partnerId, trxName);
        AddressBean myAddressBean = populateAddressBean(ctx, me);
        bpartnerList.add(myAddressBean);
        int[] partnerIds = MBPartner.getAllIDs(MBPartner.Table_Name, "BPARTNER_PARENT_ID=" + partnerId, null);
        for (int i = 0; i < partnerIds.length; i++) {
            MBPartner partner = new MBPartner(ctx, partnerIds[i], trxName);
            AddressBean bean = populateAddressBean(ctx, partner);
            bpartnerList.add(bean);
        }
        return bpartnerList;
    }

    public static MBPartner getOrCreateBPartner(Properties ctx, int parentBpId, String bPName, String name2, boolean isCustomer, boolean isVendor, boolean isEmployee, boolean isSalesRep, String address1, String postalAddress1, String city, String bpPhone, int countryId, String trxName) throws OperationException {
        return getOrCreateBPartner(ctx, parentBpId, bPName, name2, isCustomer, isVendor, isEmployee, isSalesRep, address1, "", postalAddress1, city, bpPhone, "", countryId, trxName);
    }

    public static MBPartner getOrCreateBPartner(Properties ctx, int bPartnerParentId, String bPName, String name2, boolean isCustomer, boolean isVendor, boolean isEmployee, boolean isSalesRep, String address1, String address2, String postalAddress1, String city, String bpPhone, String fax, int countryId, String trxName) throws OperationException {
        String sqlQuery = " upper(Name)=upper('" + bPName + "') and AD_CLIENT_ID=" + Env.getAD_Client_ID(ctx) + " and AD_ORG_ID=" + Env.getAD_Org_ID(ctx);
        int partners[] = MBPartner.getAllIDs(MBPartner.Table_Name, sqlQuery, trxName);
        MBPartner partner;
        if (partners.length != 0) partner = new MBPartner(ctx, partners[0], trxName); else partner = saveBPartner(ctx, 0, bPartnerParentId, bPName, name2, isCustomer, isVendor, isEmployee, isSalesRep, address1, address2, postalAddress1, null, city, bpPhone, "", fax, countryId, true, true, trxName);
        return partner;
    }

    public static AddressBean getShipmentAddressBPartner(Properties ctx, int partnerId) throws OperationException {
        if (partnerId == 0) throw new OperationException("Business partner not present!");
        MBPartner partner = new MBPartner(ctx, partnerId, null);
        AddressBean bean = populateAddressBean(ctx, partner);
        return bean;
    }

    public static boolean isBPartnerPresent(Properties ctx, int cBpartnerId, String trxName) {
        boolean isPresent = true;
        String whereClause = " C_BPARTNER_ID = " + cBpartnerId + " AND AD_CLIENT_ID = " + Env.getAD_Client_ID(ctx) + " AND ISACTIVE = 'Y'";
        int[] ids = MBPartner.getAllIDs(MBPartner.Table_Name, whereClause, trxName);
        if ((ids == null) || (ids.length == 0)) {
            isPresent = false;
        }
        return isPresent;
    }

    public static MBPartner loadBPartner(Properties ctx, int bpartnerID, String trxName) throws OperationException {
        MBPartner bPartner = new MBPartner(ctx, bpartnerID, trxName);
        if (bPartner.get_ID() == 0) throw new OperationException("Cannot load Business Partner with id: " + bpartnerID);
        return bPartner;
    }

    private static AddressBean populateAddressBean(Properties ctx, MBPartner partner) throws OperationException {
        MBPartnerLocation[] partnerLocations = MBPartnerLocation.getForBPartner(ctx, partner.get_ID());
        if (partnerLocations.length == 0) throw new OperationException("Partner " + partner.getName() + " " + partner.getName2() + " (" + partner.get_ID() + ") " + "does not have any location!");
        MBPartnerLocation partnerLocation = partnerLocations[0];
        MLocation location = new MLocation(ctx, partnerLocation.getC_Location_ID(), partner.get_TrxName());
        AddressBean bean = new AddressBean();
        bean.setBpartnerId(partner.get_ID());
        bean.setUsername(partner.getName());
        bean.setUserSurname(partner.getName2());
        bean.setAddress1(location.getAddress1());
        bean.setAddress2(location.getAddress2());
        bean.setPostalAddress(location.getPostal_Add());
        bean.setCity(location.getCity());
        bean.setCountryId(location.getC_Country_ID());
        bean.setCountryName(location.getCountryName());
        return bean;
    }

    public static ArrayList<BPartnerBean> searchBpartners(Properties ctx, String name, String trxName) throws OperationException {
        int adClientID = Env.getAD_Client_ID(ctx);
        String sql;
        sql = " select bp.c_bpartner_id, " + " bp.name," + " bp.name2," + " bp.isactive," + " bp.iscustomer," + " bp.isemployee," + " bp.isvendor, " + " bp.issalesrep," + " cl.address1," + " cl.address2," + " cl.city," + " cl.postal_add," + " bpl.c_bpartner_id," + " cl.c_location_id," + " bpl.phone," + " bpl.fax," + " cl.REGIONNAME " + " from  C_BPARTNER bp left outer join (c_bpartner_location bpl left outer join c_location cl on cl.c_location_id=bpl.c_location_id) on bpl.c_bpartner_id = bp.c_bpartner_id " + " where bp.ad_org_id in (" + Env.getContext(ctx, UdiConstants.USER_ORG_CTX_PARAM) + ")" + " and bp.AD_CLIENT_ID = " + adClientID + " and (upper(bp.name) like upper('%" + name + "%')" + " or upper(bp.name2) like upper('%" + name + "%'))" + " order by bp.name";
        PreparedStatement pstmt = null;
        BPartnerBean bpartner = null;
        ResultSet rs = null;
        ArrayList<BPartnerBean> bpartners = new ArrayList<BPartnerBean>();
        try {
            pstmt = DB.prepareStatement(sql, trxName);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                bpartner = new BPartnerBean();
                bpartner.setBpartnerId(Integer.valueOf(rs.getInt(1)));
                bpartner.setPartnerName(rs.getString(2));
                bpartner.setName2(rs.getString(3));
                if (rs.getString(4).equals("Y")) {
                    bpartner.setIsActive(Boolean.valueOf(true));
                } else bpartner.setIsActive(Boolean.valueOf(false));
                if (rs.getString(5).equals("Y")) {
                    bpartner.setIsCustomer(Boolean.valueOf(true));
                } else bpartner.setIsCustomer(Boolean.valueOf(false));
                if (rs.getString(6).equals("Y")) {
                    bpartner.setIsEmployee(Boolean.valueOf(true));
                } else bpartner.setIsEmployee(Boolean.valueOf(false));
                if (rs.getString(7).equals("Y")) {
                    bpartner.setIsVendor(Boolean.valueOf(true));
                } else bpartner.setIsVendor(Boolean.valueOf(false));
                if (rs.getString(8).equals("Y")) {
                    bpartner.setIsSalesRep(Boolean.valueOf(true));
                } else bpartner.setIsSalesRep(Boolean.valueOf(false));
                bpartner.setAddress1(rs.getString(9));
                bpartner.setAddress2(rs.getString(10));
                bpartner.setCity(rs.getString(11));
                bpartner.setPostalAddress(rs.getString(12));
                bpartner.setPhone(rs.getString(15));
                bpartner.setFax(rs.getString(16));
                String regionName = rs.getString(17);
                int regionId = 0;
                int id[] = MRegion.getAllIDs(MRegion.Table_Name, " name= '" + regionName + "'", trxName);
                if (id.length != 0) {
                    regionId = id[0];
                }
                bpartner.setRegionId(Integer.valueOf(regionId));
                bpartners.add(bpartner);
            }
            rs.close();
        } catch (SQLException e) {
            throw new OperationException(e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (Exception ex) {
            }
            pstmt = null;
        }
        return bpartners;
    }

    public static MBPartner saveBPartner(Properties ctx, int partnerId, int parentBpId, String name, String name2, boolean isCustomer, boolean isVendor, boolean isEmployee, boolean isSalesRep, String address1, String address2, String postalAddress1, Integer regionId, String city, String bpPhone, String phone2, String fax, int countryId, boolean isShipTo, boolean isBillTo, String trxName) throws OperationException {
        MBPartner bPartner;
        int locationId;
        if (partnerId == 0) {
            bPartner = new MBPartner(ctx, -1, trxName);
            locationId = 0;
        } else {
            bPartner = loadBPartner(ctx, partnerId, trxName);
            MBPartnerLocation[] bplocations = MBPartnerLocation.getForBPartner(ctx, partnerId);
            if (bplocations.length > 1) throw new OperationException("Error: Business Partner has more than 1 location!"); else if (bplocations.length == 0) locationId = 0; else {
                MBPartnerLocation bpLocation = bplocations[0];
                locationId = bpLocation.getC_Location_ID();
            }
        }
        bPartner.setValue(name + "_" + name2 + "_" + TrxPrefix.getPrefix());
        bPartner.setName(name);
        bPartner.setName2(name2);
        bPartner.setIsCustomer(isCustomer);
        bPartner.setIsVendor(isVendor);
        bPartner.setIsEmployee(isEmployee);
        bPartner.setIsSalesRep(isSalesRep);
        bPartner.setBPartner_Parent_ID(parentBpId);
        bPartner.setAD_Org_ID(Env.getAD_Org_ID(ctx));
        bPartner.setSalesRep_ID(Env.getAD_User_ID(ctx));
        PoManager.save(bPartner);
        MLocation location = LocationManager.saveLocation(ctx, locationId, address1, address2, postalAddress1, regionId, city, countryId, trxName);
        MBPartnerLocation bplocation = null;
        if (bPartner.getPrimaryC_BPartner_Location_ID() == 0) bplocation = new MBPartnerLocation(bPartner); else bplocation = new MBPartnerLocation(ctx, bPartner.getPrimaryC_BPartner_Location_ID(), trxName);
        bplocation.setPhone(bpPhone);
        bplocation.setPhone2(phone2);
        bplocation.setFax(fax);
        bplocation.setC_Location_ID(location.get_ID());
        bplocation.setIsShipTo(isShipTo);
        bplocation.setIsBillTo(isBillTo);
        PoManager.save(bplocation);
        bPartner.setPrimaryC_BPartner_Location_ID(bplocation.get_ID());
        PoManager.save(bPartner);
        return bPartner;
    }

    public static MBPartner editBPartner(Properties ctx, int bpartnerId, String name, String name2, boolean isCustomer, boolean isVendor, boolean isEmployee, boolean isSalesRep, String address1, String address2, String postalAddress, String city, String phoneNo, String phone2, String fax, int regionId, String trxName) throws OperationException {
        MBPartner bPartner = new MBPartner(ctx, bpartnerId, trxName);
        bPartner.setName(name);
        bPartner.setName2(name2);
        bPartner.setIsVendor(isVendor);
        bPartner.setIsCustomer(isCustomer);
        bPartner.setIsEmployee(isEmployee);
        bPartner.setIsSalesRep(isSalesRep);
        if (bPartner.getSalesRep_ID() == 0) {
            bPartner.setSalesRep_ID(Env.getAD_User_ID(ctx));
        }
        MBPartnerLocation bpLocation = editPhoneAndFax(ctx, bpartnerId, phoneNo, phone2, fax, trxName);
        LocationManager.editLocation(ctx, bpLocation.getC_Location_ID(), address1, address2, postalAddress, regionId, city, bpLocation.getLocation(true).getC_Country_ID(), trxName);
        UDIMBPartnerLocation uBpLocation = new UDIMBPartnerLocation(bpLocation);
        uBpLocation.save();
        UDIMBPartner uBpartner = new UDIMBPartner(bPartner);
        uBpartner.save();
        return bPartner;
    }

    public static MBPartnerLocation editPhoneAndFax(Properties ctx, int bpId, String phone, String phone2, String fax, String trxName) throws OperationException {
        if (bpId <= 0) throw new OperationException("BPartner does not exist");
        MBPartner bp = new MBPartner(ctx, bpId, trxName);
        MBPartnerLocation bpLocation;
        try {
            bpLocation = new MBPartnerLocation(ctx, bp.getPrimaryC_BPartner_Location_ID(), trxName);
            if (bpLocation == null) throw new OperationException("BPartner Location does not exist");
            bpLocation.setPhone(phone);
            bpLocation.setPhone2(phone2);
            bpLocation.setFax(fax);
            UDIMBPartnerLocation udiBPLocation = new UDIMBPartnerLocation(bpLocation);
            udiBPLocation.save();
        } catch (OperationException e) {
            throw new OperationException("Error: Could not edit phone and fax!!");
        }
        return bpLocation;
    }
}
