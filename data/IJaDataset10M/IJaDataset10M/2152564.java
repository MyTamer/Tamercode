package org.openXpertya.model;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.logging.Level;
import org.openXpertya.util.Env;
import org.openXpertya.util.Msg;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya    
 */
public class CalloutPacking extends CalloutEngine {

    /**
     * Descripción de Método
     *
     *
     * @param ctx
     * @param WindowNo
     * @param mTab
     * @param mField
     * @param value
     *
     * @return
     *
     * @throws Exception
     */
    public String uomPacking(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value) throws Exception {
        if (isCalloutActive() || (value == null)) {
            return "";
        }
        setCalloutActive(true);
        int Packing_ID = 0;
        Packing_ID = Env.getContextAsInt(ctx, WindowNo, mTab.getTabNo(), "M_ProductPacking_ID");
        MProduct mp = new MProduct(ctx, Packing_ID, null);
        mTab.setValue("C_UOMPACKING_ID", new Integer(mp.getC_UOM_ID()));
        setCalloutActive(false);
        return "";
    }

    /**
     * Descripción de Método
     *
     *
     * @param ctx
     * @param WindowNo
     * @param mTab
     * @param mField
     * @param value
     *
     * @return
     *
     * @throws Exception
     */
    public String costAmt(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value) throws Exception {
        if (isCalloutActive() || (value == null)) {
            return "";
        }
        setCalloutActive(true);
        int C_UOMPacking_ID = 0;
        int Packing_ID = 0;
        BigDecimal PackingQty = Env.ZERO;
        BigDecimal RealQty = Env.ZERO;
        BigDecimal PackingCost = Env.ZERO;
        String retMsg = "";
        MProductPO product_po = null;
        try {
            C_UOMPacking_ID = Env.getContextAsInt(ctx, WindowNo, mTab.getTabNo(), "C_UOMPACKING_ID");
            Packing_ID = Env.getContextAsInt(ctx, WindowNo, mTab.getTabNo(), "M_ProductPacking_ID");
            PackingQty = new BigDecimal(Env.getContext(ctx, WindowNo, mTab.getTabNo(), "PackingQty"));
            product_po = MProductPO.getOfOneProduct(ctx, Packing_ID, null);
            if (product_po == null) {
                MProduct mp = new MProduct(ctx, Packing_ID, null);
                retMsg = Msg.translate(Env.getCtx(), "ErrorNoCurrentVendor");
                retMsg = MessageFormat.format(retMsg, new Object[] { mp.getName() });
            } else {
                MProductPricing pp = new MProductPricing(Packing_ID, product_po.getC_BPartner_ID(), PackingQty, true);
                MBPartner bp = new MBPartner(ctx, product_po.getC_BPartner_ID(), null);
                if (bp.getPO_PriceList_ID() != 0) {
                    pp.setM_PriceList_ID(bp.getPO_PriceList_ID());
                    RealQty = MUOMConversion.convertProductFrom(ctx, Packing_ID, C_UOMPacking_ID, PackingQty);
                    if (RealQty == null) {
                        MProduct mp = new MProduct(ctx, Packing_ID, null);
                        RealQty = MUOMConversion.convert(C_UOMPacking_ID, mp.getC_UOM_ID(), PackingQty, false);
                    }
                    if (RealQty != null) {
                        PackingCost = pp.getPriceStd().multiply(RealQty);
                    } else {
                        MProduct mp = new MProduct(ctx, Packing_ID, null);
                        MUOM muFrom = new MUOM(ctx, C_UOMPacking_ID, null);
                        MUOM muTo = new MUOM(ctx, mp.getC_UOM_ID(), null);
                        retMsg = Msg.translate(Env.getCtx(), "ErrorUOMConversion");
                        retMsg = MessageFormat.format(retMsg, new Object[] { muFrom.getName(), muTo.getName() });
                    }
                } else {
                    retMsg = Msg.translate(Env.getCtx(), "ErrorNoPriceList");
                    retMsg = MessageFormat.format(retMsg, new Object[] { bp.getName() });
                }
            }
            log.fine("costAmt: Packing_ID=" + Packing_ID + ", C_UOM_Packing_ID=" + C_UOMPacking_ID + ", PackingQty=" + PackingQty + ", Packing_Cost=" + PackingCost);
        } catch (Exception e) {
            mTab.setValue("Packing_Cost", Env.ZERO);
            log.log(Level.SEVERE, "costAmt: Packing_ID=" + Packing_ID + ", C_UOM_Packing_ID=" + C_UOMPacking_ID + ", PackingQty=" + PackingQty + ", Packing_Cost=" + PackingCost);
            setCalloutActive(false);
            throw e;
        }
        mTab.setValue("Packing_Cost", PackingCost);
        setCalloutActive(false);
        return retMsg;
    }
}
