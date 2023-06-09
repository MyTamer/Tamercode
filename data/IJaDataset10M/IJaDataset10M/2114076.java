package org.openXpertya.apps.form;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import org.openXpertya.apps.ProcessCtl;
import org.openXpertya.apps.form.VModelHelper.ResultItem;
import org.openXpertya.cc.CurrentAccountManager;
import org.openXpertya.model.CalloutInvoiceExt;
import org.openXpertya.model.MAllocationHdr;
import org.openXpertya.model.MCashLine;
import org.openXpertya.model.MDiscountConfig;
import org.openXpertya.model.MDiscountSchema;
import org.openXpertya.model.MDocType;
import org.openXpertya.model.MEntidadFinancieraPlan;
import org.openXpertya.model.MInvoice;
import org.openXpertya.model.MInvoiceLine;
import org.openXpertya.model.MInvoicePaySchedule;
import org.openXpertya.model.MLetraComprobante;
import org.openXpertya.model.MOrg;
import org.openXpertya.model.MOrgInfo;
import org.openXpertya.model.MPInstance;
import org.openXpertya.model.MPOS;
import org.openXpertya.model.MPOSJournal;
import org.openXpertya.model.MPOSPaymentMedium;
import org.openXpertya.model.MPayment;
import org.openXpertya.model.MPaymentTerm;
import org.openXpertya.model.MRefList;
import org.openXpertya.model.MTax;
import org.openXpertya.model.RetencionProcessor;
import org.openXpertya.model.X_C_AllocationHdr;
import org.openXpertya.process.ProcessInfo;
import org.openXpertya.rc.Invoice;
import org.openXpertya.rc.ReciboDeCliente;
import org.openXpertya.util.ASyncProcess;
import org.openXpertya.util.CLogger;
import org.openXpertya.util.CPreparedStatement;
import org.openXpertya.util.DB;
import org.openXpertya.util.DisplayType;
import org.openXpertya.util.Env;
import org.openXpertya.util.KeyNamePair;
import org.openXpertya.util.Msg;
import org.openXpertya.util.TimeUtil;
import org.openXpertya.util.Util;
import org.openXpertya.util.ValueNamePair;

public class VOrdenCobroModel extends VOrdenPagoModel {

    /** Locale AR activo? */
    public final boolean LOCALE_AR_ACTIVE = CalloutInvoiceExt.ComprobantesFiscalesActivos();

    /** Error por defecto de punto de venta */
    public static final String DEFAULT_POS_ERROR_MSG = "CanGetPOSNumberButEmptyCR";

    /** Mensaje de error de punto de venta */
    public static String POS_ERROR_MSG = DEFAULT_POS_ERROR_MSG;

    /**
	 * Medios de pago disponibles para selección. Asociación por tipo de medio
	 * de pago.
	 */
    protected Map<String, List<MPOSPaymentMedium>> paymentMediums;

    /** Entidades Financieras y sus planes */
    protected Map<Integer, List<MEntidadFinancieraPlan>> entidadFinancieraPlans;

    /** Lista de Bancos */
    protected Map<String, String> banks;

    /** Recibo de Cliente con el matching de facturas y discount calculators */
    private ReciboDeCliente reciboDeCliente;

    /** Punto de Venta */
    private Integer POS;

    /** Facturas creadas como débito y/o crédito en base a descuentos/recargos */
    private List<MInvoice> customInvoices = new ArrayList<MInvoice>();

    /** Info de la organización actual */
    private MOrgInfo orgInfo;

    /** Existe alguna factura vencida? */
    private boolean existsOverdueInvoices = false;

    public VOrdenCobroModel() {
        super();
        getMsgMap().put("TenderType", "CustomerTenderType");
        getMsgMap().put("AdvancedPayment", "AdvancedCustomerPayment");
        getMsgMap().put("Payment", "CustomerPayment");
        reciboDeCliente = new ReciboDeCliente(getCtx(), getTrxName());
    }

    @Override
    protected OpenInvoicesTableModel getInvoicesTableModel() {
        return new OpenInvoicesCustomerReceiptsTableModel();
    }

    @Override
    protected String getIsSOTrx() {
        return "Y";
    }

    @Override
    protected int getSignoIsSOTrx() {
        return 1;
    }

    @Override
    protected void calculateRetencions() {
    }

    @Override
    public String getChequeChequeraSqlValidation() {
        return " C_BankAccount.BankAccountType = 'C'";
    }

    public String getRetencionSqlValidation() {
        return " C_RetencionSchema.RetencionApplication = 'S' ";
    }

    /**
	 * Agrega una retención como medio de cobro.
	 * @param retencionSchemaID Esquema de retención
	 * @param retencionNumber Nro. de la retención
	 * @param amount Monto de la retención
	 * @param retencionDate Fecha de la retención
	 * @throws Exception En caso de que se produzca un error al intentar cargar los datos
	 * del esquema de retención.
	 */
    public void addRetencion(Integer retencionSchemaID, String retencionNumber, BigDecimal amount, Timestamp retencionDate) throws Exception {
        if (retencionSchemaID == null || retencionSchemaID == 0) throw new Exception("@FillMandatory@ @C_Withholding_ID@");
        if (retencionNumber == null || retencionNumber.length() == 0) throw new Exception("@FillMandatory@ @RetencionNumber@");
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) throw new Exception("@NoAmountError@");
        if (retencionDate == null) throw new Exception("@FillMandatory@ @Date@");
        RetencionProcessor retProcessor = getGeneratorRetenciones().addRetencion(retencionSchemaID);
        if (retProcessor == null) throw new Exception("@SaveRetencionError@");
        retProcessor.setDateTrx(retencionDate);
        retProcessor.setAmount(amount);
        retProcessor.setRetencionNumber(retencionNumber);
        addRetencion(retProcessor);
    }

    /**
	 * Quita una retención manual de la lista de retenciones.
	 */
    public void removeRetencion(RetencionProcessor processor) {
        getRetenciones().remove(processor);
        updateRemovingRetencion(processor);
        updateTreeModel();
    }

    public void addRetencion(RetencionProcessor processor) {
        updateAddingRetencion(processor);
        updateTreeModel();
    }

    /**
	 * Quita un pago adelantado de la lista de retenciones.
	 */
    public void removePagoAdelantado(MedioPagoAdelantado mpa) {
        getMediosPago().remove(mpa);
        updateTreeModel();
    }

    @Override
    protected String getAllocHdrDescriptionMsg() {
        return "@CustomerReceipt@";
    }

    @Override
    protected String getHdrAllocationType() {
        if (isNormalPayment()) return MAllocationHdr.ALLOCATIONTYPE_CustomerReceipt; else return MAllocationHdr.ALLOCATIONTYPE_AdvancedCustomerReceipt;
    }

    @Override
    protected String getCashType() {
        return MCashLine.CASHTYPE_GeneralReceipts;
    }

    @Override
    protected String getReportValue() {
        return "CustomerReceipt";
    }

    /**
	 * Inicializa los medios de pago disponibles por tipo de medio de pago
	 */
    public void initPaymentMediums() {
        List<MPOSPaymentMedium> posPaymentMediums = MPOSPaymentMedium.getAvailablePaymentMediums(getCtx(), null, MPOSPaymentMedium.CONTEXT_POSOnly, true, getTrxName());
        paymentMediums = new HashMap<String, List<MPOSPaymentMedium>>();
        List<MPOSPaymentMedium> mediums = null;
        for (MPOSPaymentMedium mposPaymentMedium : posPaymentMediums) {
            mediums = paymentMediums.get(mposPaymentMedium.getTenderType());
            if (mediums == null) {
                mediums = new ArrayList<MPOSPaymentMedium>();
            }
            mediums.add(mposPaymentMedium);
            paymentMediums.put(mposPaymentMedium.getTenderType(), mediums);
        }
    }

    /**
	 * Inicializa los planes de entidades financieras existentes
	 */
    public void initEntidadFinancieraPlans() {
        List<MEntidadFinancieraPlan> eFPlans = MEntidadFinancieraPlan.getPlansAvailables(getCtx(), null, getTrxName());
        entidadFinancieraPlans = new HashMap<Integer, List<MEntidadFinancieraPlan>>();
        List<MEntidadFinancieraPlan> plans = null;
        for (MEntidadFinancieraPlan mEntidadFinancieraPlan : eFPlans) {
            plans = entidadFinancieraPlans.get(mEntidadFinancieraPlan.getID());
            if (plans == null) {
                plans = new ArrayList<MEntidadFinancieraPlan>();
            }
            plans.add(mEntidadFinancieraPlan);
            entidadFinancieraPlans.put(mEntidadFinancieraPlan.getM_EntidadFinanciera_ID(), plans);
        }
    }

    /**
	 * Obtengo los medios de pago de un tipo de medio de pago parámetro
	 * 
	 * @param tenderType
	 *            tipo de medio de pago
	 * @return lista de medios de pago del tipo de medio de pago parámetro
	 */
    public List<MPOSPaymentMedium> getPaymentMediums(String tenderType) {
        if (paymentMediums == null) {
            initPaymentMediums();
        }
        return paymentMediums.get(tenderType);
    }

    /**
	 * Obtengo los planes de la entidad financiera parámetro
	 * 
	 * @param entidadFinancieraID
	 *            id de la entidad financiera
	 * @return lista de los planes disponibles de la entidad financiera
	 *         parámetro
	 */
    public List<MEntidadFinancieraPlan> getPlans(Integer entidadFinancieraID) {
        if (entidadFinancieraPlans == null) {
            initEntidadFinancieraPlans();
        }
        return entidadFinancieraPlans.get(entidadFinancieraID);
    }

    /**
	 * Inicializo los bancos
	 */
    public void initBanks() {
        ValueNamePair[] bankList = MRefList.getList(MPOSPaymentMedium.BANK_AD_Reference_ID, false);
        banks = new HashMap<String, String>();
        for (int i = 0; i < bankList.length; i++) {
            banks.put(bankList[i].getValue(), bankList[i].getName());
        }
    }

    /**
	 * @param value value de la lista de bancos
	 * @return nombre del valor de la lista pasado como parámetro
	 */
    public String getBankName(String value) {
        if (banks == null) {
            initBanks();
        }
        return banks.get(value);
    }

    /**
	 * Agrego la tarjeta de crédito como medio de pago
	 * @param paymentMedium 
	 * @param plan
	 * @param creditCardNo
	 * @param couponNo
	 * @param amt
	 * @param bank
	 */
    public void addCreditCard(MPOSPaymentMedium paymentMedium, MEntidadFinancieraPlan plan, String creditCardNo, String couponNo, BigDecimal amt, String bank, int cuotasCount, BigDecimal cuotaAmt, Integer campaignID, Integer projectID) throws Exception {
        if (amt == null || amt.compareTo(BigDecimal.ZERO) <= 0) throw new Exception("@NoAmountError@");
        if (paymentMedium == null) throw new Exception("@FillMandatory@ @ReceiptMedium@");
        if (plan == null) throw new Exception("@FillMandatory@ @M_EntidadFinancieraPlan_ID@");
        if (bank == null) throw new Exception("@FillMandatory@ @C_Bank_ID@");
        MedioPagoTarjetaCredito tarjetaCredito = new MedioPagoTarjetaCredito();
        tarjetaCredito.setCouponNo(couponNo);
        tarjetaCredito.setCreditCardNo(creditCardNo);
        tarjetaCredito.setPaymentMedium(paymentMedium);
        tarjetaCredito.setEntidadFinancieraPlan(plan);
        tarjetaCredito.setImporte(amt);
        tarjetaCredito.setCuotasCount(cuotasCount);
        tarjetaCredito.setCuotaAmt(cuotaAmt);
        tarjetaCredito.setCampaign(campaignID);
        tarjetaCredito.setProject(projectID);
        tarjetaCredito.setSOTrx(true);
        tarjetaCredito.setBank(bank);
        tarjetaCredito.setDiscountSchemaToApply(getCurrentGeneralDiscount());
        addMedioPago(tarjetaCredito);
    }

    /**
	 * Agrega un cheque como medio de pago al árbol
	 * 
	 * @param paymentMedium
	 * @param chequeraID
	 * @param checkNo
	 * @param amount
	 * @param fechaEmi
	 * @param fechaPago
	 * @param chequeALaOrden
	 * @param bankName
	 * @param CUITLibrador
	 * @param checkDescription
	 * @throws Exception
	 */
    public void addCheck(MPOSPaymentMedium paymentMedium, Integer chequeraID, String checkNo, BigDecimal amount, Timestamp fechaEmi, Timestamp fechaPago, String chequeALaOrden, String bankName, String CUITLibrador, String checkDescription, Integer campaignID, Integer projectID) throws Exception {
        if (Util.isEmpty(chequeraID, true)) throw new Exception("@FillMandatory@ @C_BankAccount_ID@");
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) throw new Exception("@NoAmountError@");
        if (paymentMedium == null) throw new Exception("@FillMandatory@ @ReceiptMedium@");
        if (fechaEmi == null) throw new Exception("@FillMandatory@ @EmittingDate@");
        if (fechaPago == null) throw new Exception("@FillMandatory@ @PayDate@");
        if (fechaPago.compareTo(fechaEmi) < 0) throw new Exception(getMsg("InvalidCheckDueDate"));
        if (fechaPago.compareTo(getFechaPagoCheque(fechaEmi, paymentMedium)) > 0) {
            Date maxDueDate = getFechaPagoCheque(fechaEmi, paymentMedium);
            throw new Exception(getMsg("InvalidCheckDueDateError") + ": " + DisplayType.getDateFormat(DisplayType.Date).format(maxDueDate));
        }
        if (checkNo.trim().equals("")) throw new Exception("@FillMandatory@ @CheckNo@");
        MedioPagoCheque cheque = new MedioPagoCheque(chequeraID, checkNo, amount, fechaEmi, fechaPago, chequeALaOrden);
        cheque.setPaymentMedium(paymentMedium);
        cheque.banco = bankName;
        cheque.cuitLibrador = CUITLibrador;
        cheque.descripcion = checkDescription;
        cheque.setCampaign(campaignID);
        cheque.setProject(projectID);
        cheque.setSOTrx(true);
        cheque.setDiscountSchemaToApply(getCurrentGeneralDiscount());
        addMedioPago(cheque);
    }

    @Override
    protected void addCustomPaymentInfo(MPayment pay, MedioPago mp) {
        if (mp.getTipoMP().equals(MedioPago.TIPOMEDIOPAGO_TARJETACREDITO)) {
            MedioPagoTarjetaCredito tarjeta = (MedioPagoTarjetaCredito) mp;
            pay.setTenderType(MPayment.TENDERTYPE_CreditCard);
            pay.setTrxType(MPayment.TRXTYPE_CreditPayment);
            pay.setCouponNumber(tarjeta.getCouponNo());
            pay.setCreditCardNumber(tarjeta.getCreditCardNo());
            pay.setCreditCardType(tarjeta.getCreditCardType());
            pay.setA_Bank(tarjeta.getBank());
            pay.setM_EntidadFinancieraPlan_ID(tarjeta.getEntidadFinancieraPlan().getID());
            tarjeta.setPayment(pay);
        }
    }

    /** Actualiza el modelo de la tabla de facturas
	 * 
	 * Tambien setea en cero los montos ingresados de cada factura. 
	 *
	 */
    public void actualizarFacturas() {
        int i = 1;
        if (m_facturas == null) {
            m_facturas = new Vector<ResultItem>();
            m_facturasTableModel.setResultItem(m_facturas);
        }
        m_facturas.clear();
        if ((!m_esPagoNormal) || (m_fechaFacturas == null || C_BPartner_ID == 0)) {
            m_facturasTableModel.fireChanged(false);
            return;
        }
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT c_invoice_id, c_invoicepayschedule_id, orgname, documentno, dateinvoiced, duedate, convertedamt, openamt FROM ");
        sql.append("  (SELECT i.C_Invoice_ID, i.C_InvoicePaySchedule_ID, org.name as orgname, i.DocumentNo, dateinvoiced, coalesce(duedate,dateinvoiced) as DueDate, ");
        sql.append("    abs(currencyConvert( i.GrandTotal, i.C_Currency_ID, ?, i.DateAcct, null, i.AD_Client_ID, i.AD_Org_ID)) as ConvertedAmt, ");
        sql.append("    currencyConvert( invoiceOpen(i.C_Invoice_ID, COALESCE(i.C_InvoicePaySchedule_ID, 0)), i.C_Currency_ID, ?, i.DateAcct, null, i.AD_Client_ID, i.AD_Org_ID) AS openAmt ");
        sql.append("  FROM c_invoice_v AS i ");
        sql.append("  LEFT JOIN ad_org org ON (org.ad_org_id=i.ad_org_id) ");
        sql.append("  LEFT JOIN c_invoicepayschedule AS ips ON (i.c_invoicepayschedule_id=ips.c_invoicepayschedule_id) ");
        sql.append("  INNER JOIN C_DocType AS dt ON (dt.C_DocType_ID=i.C_DocType_ID) ");
        sql.append("  WHERE i.IsActive = 'Y' AND i.DocStatus IN ('CO', 'CL') ");
        sql.append("    AND i.IsSOTRx = '" + getIsSOTrx() + "' AND GrandTotal <> 0.0 AND C_BPartner_ID = ? ");
        sql.append("    AND dt.signo_issotrx = " + getSignoIsSOTrx());
        if (AD_Org_ID != 0) sql.append("  AND i.ad_org_id = ?  ");
        sql.append("  ) as openInvoices ");
        sql.append(" GROUP BY c_invoice_id, c_invoicepayschedule_id, orgname, documentno, dateinvoiced, duedate, convertedamt, openamt  ");
        sql.append(" HAVING openamt > 0.0 ");
        if (!m_allInvoices && !getBPartner().isGroupInvoices()) sql.append("  AND ( duedate IS NULL OR duedate <= ? ) ");
        sql.append(" ORDER BY DueDate");
        try {
            CPreparedStatement ps = DB.prepareStatement(sql.toString(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE, getTrxName());
            ps.setInt(i++, C_Currency_ID);
            ps.setInt(i++, C_Currency_ID);
            ps.setInt(i++, C_BPartner_ID);
            if (AD_Org_ID != 0) ps.setInt(i++, AD_Org_ID);
            if (!m_allInvoices && !getBPartner().isGroupInvoices()) ps.setTimestamp(i++, m_fechaFacturas);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ResultItemFactura rif = new ResultItemFactura(rs);
                updatePaymentTermInfo(rif);
                m_facturas.add(rif);
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "", e);
        }
        applyBPartnerDiscount();
        m_facturasTableModel.fireChanged(false);
    }

    /**
	 * Actualización del descuento/recargo de las facturas
	 * 
	 * @param rif
	 *            ítem de factura de la tabla, factura o cuota
	 */
    protected void updatePaymentTermInfo(ResultItemFactura rif) {
        int invoiceID = (Integer) rif.getItem(m_facturasTableModel.getIdColIdx());
        Integer invoicePayScheduleID = (Integer) rif.getItem(m_facturasTableModel.getInvoicePayScheduleColIdx());
        Timestamp dateInvoiced = (Timestamp) rif.getItem(((OpenInvoicesCustomerReceiptsTableModel) m_facturasTableModel).getDateInvoicedColIdx());
        Timestamp dueDate = (Timestamp) rif.getItem(m_facturasTableModel.getDueDateColIdx());
        BigDecimal openAmt = (BigDecimal) rif.getItem(m_facturasTableModel.getOpenAmtColIdx());
        int paymentTermID = DB.getSQLValue(getTrxName(), "SELECT c_paymentterm_id FROM c_invoice WHERE c_invoice_id = ?", invoiceID);
        MPaymentTerm paymentTerm = new MPaymentTerm(getCtx(), paymentTermID, getTrxName());
        MInvoicePaySchedule ips = null;
        if (!Util.isEmpty(invoicePayScheduleID, true)) {
            ips = new MInvoicePaySchedule(getCtx(), invoicePayScheduleID, getTrxName());
        }
        BigDecimal discount = paymentTerm.getDiscount(ips, dateInvoiced, dueDate, new Timestamp(System.currentTimeMillis()), openAmt);
        rif.setPaymentTermDiscount(discount);
    }

    @Override
    public void updateBPartner(Integer bPartnerID) {
        super.updateBPartner(bPartnerID);
        reciboDeCliente.updateBPartner(bPartnerID);
    }

    /**
	 * Determino si existen facturas vencidas a partir de la fecha de
	 * vencimiento parámetro
	 * 
	 * @param dueDate
	 *            fecha de vencimiento de una factura o cuota
	 */
    protected void setIsOverdueInvoice(Timestamp dueDate) {
        if (dueDate == null) return;
        if (TimeUtil.getDiffDays(dueDate, Env.getDate()) > 0) {
            existsOverdueInvoices = true;
        }
    }

    /**
	 * Aplica el esquema de descuento a las facturas existentes si pueden ser
	 * aplicables según cada calculator discount propio
	 */
    public void applyBPartnerDiscount() {
        reciboDeCliente.applyBPartnerDiscount();
    }

    public MDiscountSchema getDiscountFrom(MEntidadFinancieraPlan plan) {
        return reciboDeCliente.getDiscountFrom(plan);
    }

    public MDiscountSchema getDiscountFrom(MPOSPaymentMedium paymentMedium) {
        return reciboDeCliente.getDiscountFrom(paymentMedium);
    }

    public MDiscountSchema getDiscount(MPOSPaymentMedium paymentMedium, MEntidadFinancieraPlan plan) {
        return reciboDeCliente.getDiscount(paymentMedium, plan);
    }

    /**
	 * @param discountSchema
	 * @param baseAmt
	 */
    public void updateGeneralDiscount(MDiscountSchema discountSchema) {
        reciboDeCliente.setCurrentGeneralDiscountSchema(discountSchema);
    }

    public MDiscountSchema getCurrentGeneralDiscount() {
        return reciboDeCliente.getCurrentGeneralDiscountSchema();
    }

    /**
	 * @param verifyApplication
	 *            true si se debe verificar si es posible aplicar el descuento
	 *            de entidad comercial en base a configuración de descuento,
	 *            false si solo se debe retornar el descuento de entidad
	 *            comercial configurado
	 * @return esquema de descuentos de la entidad comercial o null caso que no
	 *         exista ninguno configurado o no se pueda aplicar (si es true el
	 *         parámetro)
	 */
    public MDiscountSchema getbPartnerDiscountSchema(boolean verifyApplication) {
        MDiscountSchema discountSchema = reciboDeCliente.getbPartnerDiscountSchema();
        if (verifyApplication) {
            if (discountSchema != null) {
                if (!reciboDeCliente.isBPartnerDiscountApplicable(discountSchema.getDiscountContextType(), getBPartner().getDiscountContext())) {
                    discountSchema = null;
                }
            }
        }
        return discountSchema;
    }

    @Override
    protected void updateInvoicesModel(Vector<Integer> facturasProcesar, Vector<BigDecimal> manualAmounts, Vector<ResultItemFactura> resultsProcesar) {
        reciboDeCliente.clear();
        int totalInvoice = facturasProcesar.size();
        BigDecimal paymentTermDiscount;
        for (int i = 0; i < totalInvoice; i++) {
            paymentTermDiscount = resultsProcesar.get(i).getPaymentTermDiscount();
            reciboDeCliente.add(facturasProcesar.get(i), manualAmounts.get(i), paymentTermDiscount, true);
        }
        reciboDeCliente.makeGlobalInvoice();
    }

    public BigDecimal getToPayAmount(MDiscountSchema discountSchema) {
        BigDecimal toPay = reciboDeCliente.getToPayAmt(discountSchema);
        return toPay;
    }

    /**
	 * @param discountContextType
	 *            tipo de contexto de descuento
	 * @return Indica si son aplicables los descuentos por medios de pagos en el
	 *         contexto pasado como parámetro
	 */
    public boolean isPaymentMediumDiscountApplicable(String discountContextType) {
        return reciboDeCliente.isGeneralDocumentDiscountApplicable(discountContextType);
    }

    @Override
    protected void updateAditionalInfo() {
        reciboDeCliente.updateBPDiscountSchema();
    }

    /**
	 * Suma los descuentos realizados al recibo de cliente (a todas las
	 * facturas)
	 * 
	 * @return suma de los descuentos aplicados hasta el momento
	 */
    public BigDecimal getSumaDescuentos() {
        return reciboDeCliente.getTotalDiscountAmt();
    }

    @Override
    protected void updateAddingMedioPago(MedioPago mp) {
        if (getToPayAmount(null).compareTo(BigDecimal.ZERO) != 0) {
            reciboDeCliente.addMedioPago(mp);
        }
    }

    @Override
    protected void updateRemovingMedioPago(MedioPago mp) {
        reciboDeCliente.removeMedioPago(mp);
    }

    @Override
    protected void updateAddingRetencion(RetencionProcessor processor) {
        if (getToPayAmount(null).compareTo(BigDecimal.ZERO) != 0) {
            reciboDeCliente.addRetencion(processor);
        }
    }

    @Override
    protected void updateRemovingRetencion(RetencionProcessor processor) {
        reciboDeCliente.removeRetencion(processor);
    }

    /**
	 * Actualiza todos los descuentos
	 */
    public void updateDiscounts() {
        reciboDeCliente.updateDiscounts();
    }

    @Override
    public void addDebit(MInvoice invoice) {
        reciboDeCliente.addDebit(invoice);
    }

    protected List<Invoice> ordenarFacturas(List<Invoice> invoicesToSort) {
        Comparator<Invoice> cmp = new Comparator<Invoice>() {

            public int compare(Invoice arg0, Invoice arg1) {
                BigDecimal b0 = arg0.getManualAmt();
                BigDecimal b1 = arg1.getManualAmt();
                return b0.compareTo(b1);
            }
        };
        Invoice[] invoicesSorted = new Invoice[invoicesToSort.size()];
        invoicesToSort.toArray(invoicesSorted);
        Arrays.sort(invoicesSorted, cmp);
        return Arrays.asList(invoicesSorted);
    }

    @Override
    public void updateOrg(int orgID) {
        super.updateOrg(orgID);
        orgInfo = MOrgInfo.get(getCtx(), orgID);
        reciboDeCliente.updateOrg(orgID);
        setPOS(getPOSNumber());
    }

    /**
	 * @return true si es posible obtener el punto de venta 
	 */
    public boolean autoGettingPOSValidation() {
        return LOCALE_AR_ACTIVE && getPOSNumber() != null;
    }

    /**
	 * @return true si existe la posibilidad de obtener el punto de venta
	 *         automáticamente, false caso contrario
	 */
    public boolean mustGettingPOSNumber() {
        return LOCALE_AR_ACTIVE;
    }

    /**
	 * Obtengo el nro de punto de venta para el usuario actual. Primero verifico
	 * la caja diaria activa, sino obtengo de la configuración del TPV para ese
	 * usuario y organización. Si existe más de una configuración no se coloca
	 * ninguna para que el usuario inserte la adecuada.
	 * 
	 * @return nro de punto de venta para el usuario y organización actual, null
	 *         si no existe ninguna o más de una
	 */
    public Integer getPOSNumber() {
        Integer posNro = MPOSJournal.getCurrentPOSNumber();
        boolean error = true;
        if (Util.isEmpty(posNro, true)) {
            List<MPOS> poss = MPOS.get(getCtx(), AD_Org_ID, null, getTrxName());
            if (poss.size() == 1) {
                posNro = poss.get(0).getPOSNumber();
            } else if (poss.size() > 1) {
                POS_ERROR_MSG = "CanGetPOSNumberButMoreThanOne";
                error = false;
            }
        }
        if (Util.isEmpty(posNro, true) && error) {
            posNro = null;
            POS_ERROR_MSG = DEFAULT_POS_ERROR_MSG;
        }
        return posNro;
    }

    /**
	 * @return el cargo de cobro de facturas vencidas de la organización
	 */
    public BigDecimal getOrgCharge() {
        BigDecimal charge = BigDecimal.ZERO;
        if (existsOverdueInvoices && orgInfo != null) {
            charge = orgInfo.getOverdueInvoicesCharge();
        }
        return charge;
    }

    public Integer getPOS() {
        return POS;
    }

    public void setPOS(Integer pOS) {
        POS = pOS;
    }

    @Override
    public void updateOnlineAllocationLines(Vector<MedioPago> pays) {
        onlineAllocationLines = null;
        onlineAllocationLines = new ArrayList<AllocationLine>();
        BigDecimal saldoMediosPago = getSaldoMediosPago(true);
        Vector<MedioPago> credits = new Vector<MedioPago>(pays);
        Vector<Invoice> debits = new Vector<Invoice>(ordenarFacturas(reciboDeCliente.getRealInvoices()));
        BigDecimal sobraDelPago = null;
        int payIdx = 0;
        int totalFacturas = debits.size();
        int totalPagos = credits.size();
        Invoice invoice;
        int C_Invoice_ID;
        BigDecimal deboPagar;
        BigDecimal sumaPagos;
        ArrayList<MedioPago> subPagos;
        ArrayList<BigDecimal> montosSubPagos;
        for (int i = 0; i < totalFacturas && payIdx < totalPagos; i++) {
            invoice = debits.get(i);
            C_Invoice_ID = invoice.getInvoiceID();
            deboPagar = invoice.getManualAmt().add(invoice.getTotalPaymentTermDiscount());
            sumaPagos = (sobraDelPago != null) ? sobraDelPago : credits.get(payIdx).getImporte();
            subPagos = new ArrayList<MedioPago>();
            montosSubPagos = new ArrayList<BigDecimal>();
            subPagos.add(credits.get(payIdx));
            montosSubPagos.add(sumaPagos);
            boolean follow = true;
            while (compararMontos(deboPagar, sumaPagos) > 0 && follow) {
                payIdx++;
                follow = payIdx < totalPagos;
                if (follow) {
                    BigDecimal importe = credits.get(payIdx).getImporte();
                    subPagos.add(credits.get(payIdx));
                    montosSubPagos.add(importe);
                    sumaPagos = sumaPagos.add(importe);
                }
            }
            if (compararMontos(deboPagar, sumaPagos) < 0) {
                int x = montosSubPagos.size() - 1;
                sobraDelPago = sumaPagos.subtract(deboPagar);
                montosSubPagos.set(x, montosSubPagos.get(x).subtract(sobraDelPago));
            } else {
                payIdx++;
                sobraDelPago = null;
            }
            for (int subpayIdx = 0; subpayIdx < subPagos.size(); subpayIdx++) {
                MedioPago mp = subPagos.get(subpayIdx);
                BigDecimal AppliedAmt = montosSubPagos.get(subpayIdx);
                BigDecimal DiscountAmt = Env.ZERO;
                BigDecimal WriteoffAmt = Env.ZERO;
                BigDecimal OverunderAmt = Env.ZERO;
                if (saldoMediosPago.signum() != 0) {
                    WriteoffAmt = saldoMediosPago;
                    saldoMediosPago = BigDecimal.ZERO;
                }
                AllocationLine allocLine = new AllocationLine();
                allocLine.setAmt(AppliedAmt);
                allocLine.setDiscountAmt(DiscountAmt);
                allocLine.setWriteOffAmt(WriteoffAmt);
                allocLine.setOverUnderAmt(OverunderAmt);
                allocLine.setDebitDocumentID(C_Invoice_ID);
                allocLine.setCreditPaymentMedium(mp);
                onlineAllocationLines.add(allocLine);
            }
        }
    }

    @Override
    protected void makeCustomDebitsCredits(Vector<MedioPago> pays) throws Exception {
        Map<String, BigDecimal> discountsPerKind = reciboDeCliente.getDiscountsSumPerKind();
        Set<String> kinds = discountsPerKind.keySet();
        MInvoice credit = null;
        MInvoice debit = null;
        MInvoice inv = null;
        MInvoiceLine invoiceLine = null;
        BigDecimal amt;
        boolean isCredit;
        boolean createInvoice;
        MTax tax = MTax.getTaxExemptRate(getCtx(), getTrxName());
        for (String discountKind : kinds) {
            amt = discountsPerKind.get(discountKind);
            if (amt.compareTo(BigDecimal.ZERO) != 0) {
                isCredit = amt.compareTo(BigDecimal.ZERO) > 0;
                if (discountKind.equals(ReciboDeCliente.CHARGE_DISCOUNT)) {
                    isCredit = !isCredit;
                }
                createInvoice = isCredit ? credit == null : debit == null;
                if (createInvoice) {
                    inv = createCreditDebitInvoice(isCredit);
                    inv.setBPartner(getBPartner());
                    if (!inv.save()) {
                        throw new Exception("Can't create " + (isCredit ? "credit" : "debit") + " document for discounts. Original Error: " + CLogger.retrieveErrorAsString());
                    }
                    if (isCredit) {
                        credit = inv;
                    } else {
                        debit = inv;
                    }
                }
                inv = isCredit ? credit : debit;
                invoiceLine = createInvoiceLine(inv, isCredit, amt, tax, discountKind);
                if (!invoiceLine.save()) {
                    throw new Exception("Can't create " + (isCredit ? "credit" : "debit") + " document line for discounts. Original Error: " + CLogger.retrieveErrorAsString());
                }
            }
        }
        if (credit != null) {
            if (!needFiscalPrint(credit)) {
                processDocument(credit, MInvoice.DOCACTION_Complete);
            }
            MedioPagoCredito credito = new MedioPagoCredito(true);
            credito.setC_invoice_ID(credit.getID());
            credito.setImporte(credit.getGrandTotal());
            pays.add(credito);
            customInvoices.add(credit);
        }
        if (debit != null) {
            if (!needFiscalPrint(debit)) {
                processDocument(debit, MInvoice.DOCACTION_Complete);
            }
            addDebit(debit);
            customInvoices.add(debit);
        }
    }

    /**
	 * Creo una factura como crédito o débito, dependiendo configuración.
	 * 
	 * @param credit
	 *            true si se debe crear un crédito o false si es débito
	 * @return factura creada
	 * @throws Exception en caso de error
	 */
    protected MInvoice createCreditDebitInvoice(boolean credit) throws Exception {
        MInvoice invoice = new MInvoice(getCtx(), 0, getTrxName());
        invoice.setBPartner(getBPartner());
        invoice = setDocType(invoice, credit);
        if (LOCALE_AR_ACTIVE) {
            invoice = addLocaleARData(invoice, credit);
        }
        invoice.setCreateCashLine(false);
        invoice.setDocAction(MInvoice.DOCACTION_Complete);
        invoice.setDocStatus(MInvoice.DOCSTATUS_Drafted);
        invoice.setCurrentAccountVerified(true);
        invoice.setUpdateBPBalance(false);
        return invoice;
    }

    /**
	 * Setea el tipo de documento a la factura parámetro
	 * 
	 * @param invoice
	 *            factura a modificar
	 * @param isCredit
	 *            booleano que determina si lo que estoy creando es un débito o
	 *            un crédito
	 * @return factura con el tipo de doc seteada
	 */
    protected MInvoice setDocType(MInvoice invoice, boolean isCredit) throws Exception {
        Integer docType = null;
        String generalDocType = null;
        if (isCredit) {
            generalDocType = reciboDeCliente.getGeneralCreditDocType();
            if (generalDocType.equals(MDiscountConfig.CREDITDOCUMENTTYPE_Other)) {
                docType = reciboDeCliente.getCreditDocType();
            }
        } else {
            generalDocType = reciboDeCliente.getGeneralDebitDocType();
            if (generalDocType.equals(MDiscountConfig.DEBITDOCUMENTTYPE_Other)) {
                docType = reciboDeCliente.getDebitDocType();
            }
        }
        MDocType documentType = null;
        if (docType != null) {
            documentType = new MDocType(getCtx(), docType, getTrxName());
        }
        if (documentType == null) {
            String docTypeKey = getRealDocTypeKey(generalDocType, isCredit);
            if (LOCALE_AR_ACTIVE) {
                MLetraComprobante letra = getLetraComprobante();
                if (Util.isEmpty(getPOS(), true)) throw new Exception(getMsg("NotExistPOSNumber"));
                Integer posNumber = Integer.valueOf(getPOS());
                documentType = MDocType.getDocType(getCtx(), docTypeKey, letra.getLetra(), posNumber, getTrxName());
                if (documentType == null) {
                    throw new Exception(Msg.getMsg(getCtx(), "NonexistentPOSDocType", new Object[] { letra, posNumber }));
                }
                if (!Util.isEmpty(posNumber, true)) {
                    invoice.setPuntoDeVenta(posNumber);
                }
            } else {
                documentType = MDocType.getDocType(getCtx(), docTypeKey, getTrxName());
            }
        }
        invoice.setC_DocTypeTarget_ID(documentType.getID());
        return invoice;
    }

    /**
	 * Obtener la letra del comprobante
	 * 
	 * @return letra del comprobante
	 * @throws Exception
	 *             si la compañía o el cliente no tienen configurado una
	 *             categoría de IVA y si no existe una Letra que los corresponda
	 */
    protected MLetraComprobante getLetraComprobante() throws Exception {
        Integer categoriaIVAclient = CalloutInvoiceExt.darCategoriaIvaClient();
        Integer categoriaIVACustomer = getBPartner().getC_Categoria_Iva_ID();
        if (categoriaIVAclient == null || categoriaIVAclient == 0) {
            throw new Exception(getMsg("ClientWithoutIVAError"));
        } else if (categoriaIVACustomer == null || categoriaIVACustomer == 0) {
            throw new Exception(getMsg("BPartnerWithoutIVAError"));
        }
        Integer letraID = CalloutInvoiceExt.darLetraComprobante(categoriaIVACustomer, categoriaIVAclient);
        if (letraID == null || letraID == 0) {
            throw new Exception(getMsg("LetraCalculationError"));
        }
        return new MLetraComprobante(getCtx(), letraID, getTrxName());
    }

    /**
	 * Obtener la clave del tipo de documento real en base al general y si el
	 * comprobante que estoy creando es un crédito o un débito
	 * 
	 * @param generalDocType
	 *            tipo de documento general
	 * @param isCredit
	 *            true si estamos ante un crédito, false caso contrario
	 * @return
	 */
    protected String getRealDocTypeKey(String generalDocType, boolean isCredit) {
        String docTypeKey = generalDocType;
        if (LOCALE_AR_ACTIVE) {
            if (isCredit && generalDocType.equals(MDiscountConfig.CREDITDOCUMENTTYPE_CreditNote)) {
                docTypeKey = MDocType.DOCTYPE_CustomerCreditNote;
            }
            if (!isCredit && generalDocType.equals(MDiscountConfig.DEBITDOCUMENTTYPE_DebitNote)) {
                docTypeKey = MDocType.DOCTYPE_CustomerDebitNote;
            }
        }
        return docTypeKey;
    }

    /**
	 * Agregar la info de locale ar necesaria a la factura con localización
	 * argentina.
	 * 
	 * @param invoice
	 *            factura
	 * @param credit true si estamos ante un crédito, false si es débito
	 * @return factura parámetro con toda la info localeAr necesaria cargada
	 * @throws Exception en caso de error
	 */
    protected MInvoice addLocaleARData(MInvoice invoice, boolean credit) throws Exception {
        MLetraComprobante letraCbte = getLetraComprobante();
        invoice.setC_Letra_Comprobante_ID(letraCbte.getID());
        Integer nroComprobante = CalloutInvoiceExt.getNextNroComprobante(invoice.getC_DocTypeTarget_ID());
        if (nroComprobante != null) invoice.setNumeroComprobante(nroComprobante);
        String cuit = getBPartner().getTaxID();
        invoice.setCUIT(cuit);
        if (credit && LOCALE_AR_ACTIVE) {
            Invoice firstInvoice = reciboDeCliente.getRealInvoices().get(0);
            if (firstInvoice != null) {
                invoice.setC_Invoice_Orig_ID(firstInvoice.getInvoiceID());
            }
        }
        return invoice;
    }

    /**
	 * Crea una línea de factura de la factura y datos parámetro.
	 * 
	 * @param invoice
	 *            factura
	 * @param isCredit
	 *            true si estamos creando un crédito, false caso contrario
	 * @param amt
	 *            monto de la línea
	 * @param tax
	 *            impuesto para la línea
	 * @param discountKind
	 *            tipo de descuento para obtener el artículo correspondiente de
	 *            la configuración de descuentos
	 * @return línea de la factura creada
	 * @throws Excepción
	 *             en caso de error
	 */
    public MInvoiceLine createInvoiceLine(MInvoice invoice, boolean isCredit, BigDecimal amt, MTax tax, String discountKind) throws Exception {
        MInvoiceLine invoiceLine = new MInvoiceLine(invoice);
        invoiceLine.setQty(1);
        amt = amt.abs();
        Integer sign = reciboDeCliente.getGeneralDocTypeSign(isCredit);
        amt = amt.multiply(new BigDecimal(sign));
        invoiceLine.setPriceEntered(amt);
        invoiceLine.setPriceActual(amt);
        invoiceLine.setPriceList(amt);
        invoiceLine.setC_Tax_ID(tax.getID());
        invoiceLine.setLineNetAmt();
        Integer productID = reciboDeCliente.getConfigProductID(isCredit, discountKind);
        if (Util.isEmpty(productID, true)) {
            throw new Exception("Falta configuracion de articulos para crear créditos/débitos para descuentos/recargos");
        }
        invoiceLine.setM_Product_ID(productID);
        return invoiceLine;
    }

    /**
	 * Indica si la factura debe ser emitida mediante un controlador fiscal.
	 * @param invoice Factura a evaluar.
	 */
    private boolean needFiscalPrint(MInvoice invoice) {
        return MDocType.isFiscalDocType(invoice.getC_DocTypeTarget_ID()) && LOCALE_AR_ACTIVE;
    }

    /**
	 * Procesa la factura en base a un docaction parámetro
	 * 
	 * @param invoice
	 *            factura
	 * @param docAction
	 *            acción
	 * @throws Exception
	 *             si hubo error al realizar el procesamiento o al guardar
	 */
    public void processDocument(MInvoice invoice, String docAction) throws Exception {
        if (!invoice.processIt(docAction)) {
            throw new Exception(invoice.getProcessMsg());
        }
        if (!invoice.save()) {
            throw new Exception(CLogger.retrieveErrorAsString());
        }
    }

    @Override
    public BigDecimal getSaldoMediosPago() {
        BigDecimal saldo = super.getSaldoMediosPago();
        saldo = saldo.subtract(getSumaDescuentos());
        return saldo;
    }

    @Override
    public BigDecimal getSumaTotalPagarFacturas() {
        BigDecimal suma = new BigDecimal(0);
        if (m_esPagoNormal) {
            suma = super.getSumaTotalPagarFacturas();
            suma = suma.add(existsOverdueInvoices ? getOrgCharge() : BigDecimal.ZERO);
        } else {
            suma = m_montoPagoAnticipado;
        }
        return suma;
    }

    @Override
    public BigDecimal getAllocationHdrTotalAmt() {
        return reciboDeCliente.getTotalAPagar();
    }

    @Override
    protected void performAditionalCustomCurrentAccountWork(MOrg org, CurrentAccountManager manager) throws Exception {
        for (MInvoice invoice : customInvoices) {
            performAditionalCurrentAccountWork(org, getBPartner(), manager, invoice, true);
        }
    }

    @Override
    public void reset() {
        super.reset();
        reciboDeCliente = new ReciboDeCliente(getCtx(), getTrxName());
        customInvoices = new ArrayList<MInvoice>();
        existsOverdueInvoices = false;
    }

    public BigDecimal getSaldoMediosPago(boolean finishing) {
        if (!finishing) return getSaldoMediosPago();
        BigDecimal saldo = super.getSaldoMediosPago();
        if (saldo.compareTo(BigDecimal.ZERO) != 0 && saldo.abs().compareTo(ReciboDeCliente.ROUND_TOLERANCE) > 0) {
            saldo = saldo.subtract(getSumaDescuentos());
        }
        return saldo;
    }

    /**
	 * Verificar si estamos cobrando facturas vencidas
	 */
    protected void updateOverdueInvoicesCharge() {
        Timestamp dueDate = null;
        existsOverdueInvoices = false;
        if (m_facturas != null) {
            for (ResultItem f : m_facturas) {
                ResultItemFactura fac = (ResultItemFactura) f;
                if (fac.getManualAmount().signum() > 0) {
                    dueDate = (Timestamp) fac.getItem(m_facturasTableModel.getDueDateColIdx());
                    setIsOverdueInvoice(dueDate);
                }
            }
        }
        reciboDeCliente.setOrgCharge(getOrgCharge());
    }

    /**
	 * @return monto equivalente al total abierto de las facturas a fecha de
	 *         vencimiento menor o igual a la actual o del mismo mes + cargo si
	 *         hubiere
	 */
    public BigDecimal getDefaultGroupingValue() {
        BigDecimal defaultValue = BigDecimal.ZERO;
        int totalInvoices = m_facturas.size();
        ResultItemFactura fac;
        boolean charged = false;
        boolean stop = false;
        Timestamp dueDate;
        Timestamp loginDate = Env.getDate();
        Calendar loginCalendar = Calendar.getInstance();
        loginCalendar.setTimeInMillis(loginDate.getTime());
        Calendar dueCalendar = Calendar.getInstance();
        for (int i = 0; i < totalInvoices && !stop; i++) {
            fac = (ResultItemFactura) m_facturas.get(i);
            dueDate = (Timestamp) fac.getItem(m_facturasTableModel.getDueDateColIdx());
            if (dueDate != null) {
                dueCalendar.setTimeInMillis(dueDate.getTime());
            }
            stop = dueDate != null && (TimeUtil.getDiffDays(loginDate, dueDate) > 0 || ((loginCalendar.get(Calendar.YEAR) == dueCalendar.get(Calendar.YEAR)) && (loginCalendar.get(Calendar.MONTH) == dueCalendar.get(Calendar.MONTH))));
            if (!stop) {
                if (!charged) {
                    setIsOverdueInvoice(dueDate);
                    if (getOrgCharge().compareTo(BigDecimal.ZERO) > 0) {
                        defaultValue = defaultValue.add(getOrgCharge());
                        charged = true;
                    }
                }
                defaultValue = defaultValue.add(fac.getToPayAmt(true));
            }
        }
        return defaultValue;
    }

    /**
	 * Actualizar los montos manuales de las facturas prorateando el monto
	 * grupal parámetro
	 * 
	 * @param groupingAmt
	 *            monto de agrupación
	 */
    public void updateGroupingAmtInvoices(BigDecimal groupingAmt) {
        BigDecimal amt = groupingAmt;
        int totalInvoices = m_facturas.size();
        ResultItemFactura fac;
        BigDecimal currentManualAmt, toPay;
        boolean chargeDecremented = false;
        int i = 0;
        for (; i < totalInvoices && amt.compareTo(BigDecimal.ZERO) > 0; i++) {
            fac = (ResultItemFactura) m_facturas.get(i);
            if (!chargeDecremented) {
                setIsOverdueInvoice((Timestamp) fac.getItem(m_facturasTableModel.getDueDateColIdx()));
                if (getOrgCharge().compareTo(BigDecimal.ZERO) > 0) {
                    amt = amt.subtract(getOrgCharge());
                    chargeDecremented = true;
                }
            }
            if (amt.compareTo(BigDecimal.ZERO) > 0) {
                toPay = fac.getToPayAmt(true);
                if (toPay.compareTo(amt) > 0) {
                    currentManualAmt = amt;
                } else {
                    currentManualAmt = toPay;
                }
                fac.setManualAmount(currentManualAmt);
                amt = amt.subtract(currentManualAmt);
            }
        }
        for (; i < totalInvoices; i++) {
            fac = (ResultItemFactura) m_facturas.get(i);
            fac.setManualAmount(BigDecimal.ZERO);
        }
    }

    /**
	 * Calcula la fecha de pago del cheque a partir del plazo de pago
	 * configurado del medio de pago
	 * 
	 * @param fechaEmi
	 *            fecha de emisión
	 * @param paymentMedium
	 *            medio de pago
	 * @return fecha de pago del cheque
	 */
    public Date getFechaPagoCheque(Date fechaEmi, MPOSPaymentMedium paymentMedium) {
        Calendar dueCalendar = Calendar.getInstance();
        dueCalendar.setTimeInMillis(fechaEmi.getTime());
        String checkDeadLine = paymentMedium.getCheckDeadLine();
        int days = 0;
        if (checkDeadLine.equals(MPOSPaymentMedium.CHECKDEADLINE_30)) {
            days = 30;
        } else if (checkDeadLine.equals(MPOSPaymentMedium.CHECKDEADLINE_60)) {
            days = 60;
        } else if (checkDeadLine.equals(MPOSPaymentMedium.CHECKDEADLINE_90)) {
            days = 90;
        }
        dueCalendar.add(Calendar.DATE, days);
        return dueCalendar.getTime();
    }

    /**
	 * @return el total de descuento/recargo de esquema de vencimientos. Si es
	 *         negativo es recargo, positivo descuento
	 */
    public BigDecimal getTotalPaymentTermDiscount() {
        return reciboDeCliente.getGlobalInvoice().getTotalPaymentTermDiscount();
    }

    /**
	 * @return value del proceso de impresión de facturas
	 */
    protected String getInvoiceReportValue() {
        return "Factura (Impresion)";
    }

    @Override
    public void printCustomDocuments(ASyncProcess asyncProcess) {
        if (customInvoices == null || customInvoices.size() == 0) return;
        int defaultProcessID = DB.getSQLValue(null, "SELECT AD_Process_ID FROM AD_Process WHERE value='" + getInvoiceReportValue() + "' ");
        if (defaultProcessID <= 0) return;
        int tableID = DB.getSQLValue(null, "SELECT ad_table_id FROM AD_Table WHERE tablename = 'C_Invoice'");
        Integer processID = defaultProcessID;
        MDocType docType;
        ProcessInfo pi;
        MPInstance instance;
        for (MInvoice invoice : customInvoices) {
            if (!needFiscalPrint(invoice)) {
                docType = new MDocType(m_ctx, invoice.getC_DocTypeTarget_ID(), null);
                processID = Util.isEmpty(docType.getAD_Process_ID(), true) ? defaultProcessID : docType.getAD_Process_ID();
                instance = new MPInstance(Env.getCtx(), processID, 0, null);
                if (!instance.save()) {
                    log.log(Level.SEVERE, "Error at mostrarInforme: instance.save()");
                    return;
                }
                pi = new ProcessInfo("Factura", processID);
                pi.setAD_PInstance_ID(instance.getAD_PInstance_ID());
                pi.setRecord_ID(invoice.getID());
                pi.setAD_User_ID(instance.getAD_User_ID());
                pi.setTable_ID(tableID);
                ProcessCtl worker = new ProcessCtl(asyncProcess, pi, null);
                worker.start();
            }
        }
    }

    @Override
    public void doPostProcesarNormalCustom() throws Exception {
        for (MInvoice invoice : customInvoices) {
            if (needFiscalPrint(invoice)) {
                processDocument(invoice, MInvoice.DOCACTION_Complete);
            }
        }
    }

    public void setAssumeGeneralDiscountAdded(boolean value) {
        reciboDeCliente.setAssumeGeneralDiscountAdded(value);
    }

    /**
	 * Subclasificación del tablemodel de ordenes de pago para adaptar nueva
	 * lógica de descuentos/recargos de esquemas de vencimiento
	 * 
	 * @author Equipo de Desarrollo Libertya - Matías Cap
	 */
    protected class OpenInvoicesCustomerReceiptsTableModel extends OpenInvoicesTableModel {

        private boolean allowManualAmtEditable = true;

        public OpenInvoicesCustomerReceiptsTableModel() {
            super();
            columnNames = new Vector<String>();
            columnNames.add("#$#" + Msg.getElement(Env.getCtx(), "C_Invoice_ID"));
            columnNames.add("#$#" + Msg.getElement(Env.getCtx(), "C_InvoicePaySchedule_ID"));
            columnNames.add(Msg.translate(Env.getCtx(), "AD_Org_ID"));
            columnNames.add(Msg.getElement(Env.getCtx(), "DocumentNo"));
            columnNames.add(Msg.getElement(Env.getCtx(), "DateInvoiced"));
            columnNames.add(Msg.translate(Env.getCtx(), "DueDate"));
            columnNames.add(Msg.translate(Env.getCtx(), "GrandTotal"));
            columnNames.add(Msg.translate(Env.getCtx(), "openAmt"));
            columnNames.add(Msg.translate(Env.getCtx(), "DiscountSurchargeDue"));
            columnNames.add(Msg.translate(Env.getCtx(), "ToPay"));
        }

        public int getOpenAmtColIdx() {
            return 7;
        }

        public int getIdColIdx() {
            return 0;
        }

        public int getInvoicePayScheduleColIdx() {
            return 1;
        }

        public int getDueDateColIdx() {
            return 5;
        }

        public int getDateInvoicedColIdx() {
            return 4;
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            if (column < getColumnCount() - 2) return super.isCellEditable(row, column);
            boolean editable = false;
            if (column == getColumnCount() - 1 && isAllowManualAmtEditable()) {
                editable = true;
            }
            return editable;
        }

        @Override
        public Object getValueAt(int row, int column) {
            if (column < getColumnCount() - 2) return super.getValueAt(row, column);
            BigDecimal value = null;
            if (column == getColumnCount() - 1) {
                value = ((ResultItemFactura) item.get(row)).getManualAmount();
            } else if (column == columnNames.size() - 2) {
                value = ((ResultItemFactura) item.get(row)).getPaymentTermDiscount();
                if (value.compareTo(BigDecimal.ZERO) != 0) {
                    value = value.negate();
                }
            }
            return value;
        }

        @Override
        public void setValueAt(Object arg0, int row, int column) {
            if (column < getColumnCount() - 2) super.setValueAt(arg0, row, column);
            if (column == getColumnCount() - 1) {
                ((ResultItemFactura) item.get(row)).setManualAmount((BigDecimal) arg0);
            } else if (column == columnNames.size() - 2) {
                ((ResultItemFactura) item.get(row)).setPaymentTermDiscount((BigDecimal) arg0);
            }
            fireTableCellUpdated(row, column);
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex != getColumnCount() - 1) return super.getColumnClass(columnIndex);
            return BigDecimal.class;
        }

        public void setAllowManualAmtEditable(boolean allowManualAmtEditable) {
            this.allowManualAmtEditable = allowManualAmtEditable;
        }

        public boolean isAllowManualAmtEditable() {
            return allowManualAmtEditable;
        }
    }

    protected String getAllocTypes() {
        return "(" + "'" + X_C_AllocationHdr.ALLOCATIONTYPE_CustomerReceipt + "'," + "'" + X_C_AllocationHdr.ALLOCATIONTYPE_AdvancedCustomerReceipt + "'," + "'" + X_C_AllocationHdr.ALLOCATIONTYPE_SalesTransaction + "'" + ")";
    }
}
