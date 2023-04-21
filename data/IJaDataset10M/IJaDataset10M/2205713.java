package org.stheos.pos.dao;

import java.util.HashSet;
import java.util.Set;

/**
 * Product generated by hbm2java
 */
public class ProductDAO implements java.io.Serializable {

    private Integer id;

    private CategoryDAO category;

    private String label;

    private String shortCode;

    private String barCode;

    private int netPrice;

    private boolean favorite;

    private short tchNum;

    private String tchL1;

    private String tchL2;

    private String tchUseImg;

    private String tchBgImg;

    private String tchColor;

    private int nbUsage;

    private short stockLeft;

    private short stockRef;

    private Set<TicketDetailDAO> ticketDetails = new HashSet<TicketDetailDAO>(0);

    private Set<ProductTaxDAO> productTaxes = new HashSet<ProductTaxDAO>(0);

    public ProductDAO() {
    }

    public ProductDAO(CategoryDAO category, String label, String shortCode, String barCode, int netPrice, boolean favorite, short tchNum, String tchL1, String tchL2, String tchUseImg, String tchBgImg, int nbUsage, short stockLeft, short stockRef) {
        this.category = category;
        this.label = label;
        this.shortCode = shortCode;
        this.barCode = barCode;
        this.netPrice = netPrice;
        this.favorite = favorite;
        this.tchNum = tchNum;
        this.tchL1 = tchL1;
        this.tchL2 = tchL2;
        this.tchUseImg = tchUseImg;
        this.tchBgImg = tchBgImg;
        this.nbUsage = nbUsage;
        this.stockLeft = stockLeft;
        this.stockRef = stockRef;
    }

    public ProductDAO(CategoryDAO category, String label, String shortCode, String barCode, int netPrice, boolean favorite, short tchNum, String tchL1, String tchL2, String tchUseImg, String tchBgImg, String tchColor, int nbUsage, short stockLeft, short stockRef, Set<TicketDetailDAO> ticketDetails, Set<ProductTaxDAO> productTaxes) {
        this.category = category;
        this.label = label;
        this.shortCode = shortCode;
        this.barCode = barCode;
        this.netPrice = netPrice;
        this.favorite = favorite;
        this.tchNum = tchNum;
        this.tchL1 = tchL1;
        this.tchL2 = tchL2;
        this.tchUseImg = tchUseImg;
        this.tchBgImg = tchBgImg;
        this.tchColor = tchColor;
        this.nbUsage = nbUsage;
        this.stockLeft = stockLeft;
        this.stockRef = stockRef;
        this.ticketDetails = ticketDetails;
        this.productTaxes = productTaxes;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CategoryDAO getCategory() {
        return this.category;
    }

    public void setCategory(CategoryDAO category) {
        this.category = category;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getShortCode() {
        return this.shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getBarCode() {
        return this.barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public int getNetPrice() {
        return this.netPrice;
    }

    public void setNetPrice(int netPrice) {
        this.netPrice = netPrice;
    }

    public boolean isFavorite() {
        return this.favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public short getTchNum() {
        return this.tchNum;
    }

    public void setTchNum(short tchNum) {
        this.tchNum = tchNum;
    }

    public String getTchL1() {
        return this.tchL1;
    }

    public void setTchL1(String tchL1) {
        this.tchL1 = tchL1;
    }

    public String getTchL2() {
        return this.tchL2;
    }

    public void setTchL2(String tchL2) {
        this.tchL2 = tchL2;
    }

    public String getTchUseImg() {
        return this.tchUseImg;
    }

    public void setTchUseImg(String tchUseImg) {
        this.tchUseImg = tchUseImg;
    }

    public String getTchBgImg() {
        return this.tchBgImg;
    }

    public void setTchBgImg(String tchBgImg) {
        this.tchBgImg = tchBgImg;
    }

    public String getTchColor() {
        return this.tchColor;
    }

    public void setTchColor(String tchColor) {
        this.tchColor = tchColor;
    }

    public int getNbUsage() {
        return this.nbUsage;
    }

    public void setNbUsage(int nbUsage) {
        this.nbUsage = nbUsage;
    }

    public short getStockLeft() {
        return this.stockLeft;
    }

    public void setStockLeft(short stockLeft) {
        this.stockLeft = stockLeft;
    }

    public short getStockRef() {
        return this.stockRef;
    }

    public void setStockRef(short stockRef) {
        this.stockRef = stockRef;
    }

    public Set<TicketDetailDAO> getTicketDetails() {
        return this.ticketDetails;
    }

    public void setTicketDetails(Set<TicketDetailDAO> ticketDetails) {
        this.ticketDetails = ticketDetails;
    }

    public Set<ProductTaxDAO> getProductTaxes() {
        return this.productTaxes;
    }

    public void setProductTaxes(Set<ProductTaxDAO> productTaxes) {
        this.productTaxes = productTaxes;
    }
}