package com.example.client.grid.sortfilter;

import com.google.gwt.i18n.client.NumberFormat;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.example.client.PanelFactory;
import com.example.client.ShowcasePanel;
import com.example.client.data.CountryData;

public class DisableSortSample extends ShowcasePanel {

    private static final String DESCRIPTION = "Sorting is disabled on the \"Flag\" column. Click on any other column header to sort on the corresponding column.";

    public static class Factory implements PanelFactory {

        private String id;

        public Canvas create() {
            DisableSortSample panel = new DisableSortSample();
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
        final ListGrid countryGrid = new ListGrid();
        countryGrid.setWidth(500);
        countryGrid.setHeight(224);
        countryGrid.setShowAllRecords(true);
        ListGridField countryCodeField = new ListGridField("countryCode", "Flag", 50);
        countryCodeField.setAlign(Alignment.CENTER);
        countryCodeField.setType(ListGridFieldType.IMAGE);
        countryCodeField.setImageURLPrefix("flags/16/");
        countryCodeField.setImageURLSuffix(".png");
        countryCodeField.setCanSort(false);
        final NumberFormat nf = NumberFormat.getFormat("0,000");
        ListGridField nameField = new ListGridField("countryName", "Country");
        ListGridField populationField = new ListGridField("population", "Population");
        populationField.setCellFormatter(new CellFormatter() {

            public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
                if (value == null) return null;
                try {
                    return nf.format(((Number) value).longValue());
                } catch (Exception e) {
                    return value.toString();
                }
            }
        });
        ListGridField areaField = new ListGridField("area", "Area (km&sup2;)");
        areaField.setType(ListGridFieldType.INTEGER);
        areaField.setCellFormatter(new CellFormatter() {

            public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
                NumberFormat nf = NumberFormat.getFormat("0,000");
                if (value == null) return null;
                String val = null;
                try {
                    val = nf.format(((Number) value).longValue());
                } catch (Exception e) {
                    return value.toString();
                }
                return val + "km&sup2";
            }
        });
        countryGrid.setFields(countryCodeField, nameField, populationField, areaField);
        countryGrid.setData(CountryData.getRecords());
        countryGrid.setSortField(2);
        countryGrid.setSortDirection(SortDirection.DESCENDING);
        return countryGrid;
    }

    public String getIntro() {
        return DESCRIPTION;
    }
}
