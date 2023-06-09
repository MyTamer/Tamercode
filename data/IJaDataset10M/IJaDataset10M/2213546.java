package com.example.client.grid.frozen;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.example.client.PanelFactory;
import com.example.client.ShowcasePanel;
import com.example.client.data.ItemSupplyXmlDS;

public class GridSimpleFreezeSample extends ShowcasePanel {

    private static final String DESCRIPTION = "Setting <code>frozen:true</code> on a column definition establishes a frozen column. Column resize and reorder work normally.";

    public static class Factory implements PanelFactory {

        private String id;

        public Canvas create() {
            GridSimpleFreezeSample panel = new GridSimpleFreezeSample();
            id = panel.getID();
            return panel;
        }

        public String getID() {
            return id;
        }

        public String getDescription() {
            return DESCRIPTION;
        }
    }

    public Canvas getViewPanel() {
        final ListGrid supplyItemGrid = new ListGrid();
        supplyItemGrid.setWidth(500);
        supplyItemGrid.setHeight(224);
        supplyItemGrid.setDataSource(ItemSupplyXmlDS.getInstance());
        supplyItemGrid.setAutoFetchData(true);
        ListGridField nameField = new ListGridField("itemName", 150);
        nameField.setFrozen(true);
        ListGridField categoryField = new ListGridField("category", 100);
        ListGridField skuField = new ListGridField("SKU", 100);
        ListGridField unitsField = new ListGridField("units", 80);
        ListGridField descriptionField = new ListGridField("description", 250);
        supplyItemGrid.setFields(nameField, categoryField, skuField, unitsField, descriptionField);
        return supplyItemGrid;
    }

    public String getIntro() {
        return DESCRIPTION;
    }
}
