package com.example.client.grid.autofit;

import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.RowEndEditAction;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.VLayout;
import com.example.client.PanelFactory;
import com.example.client.ShowcasePanel;
import com.example.client.data.CountryRecord;

public class AutofitNewRecordsSample extends ShowcasePanel {

    private static final String DESCRIPTION = "Autofit to rows can be made subject to a maximum. Add new rows to the grid, " + "and note that the grid expands to show the new rows. It stops expanding once you have more than 6 rows.";

    public static class Factory implements PanelFactory {

        private String id;

        public Canvas create() {
            AutofitNewRecordsSample panel = new AutofitNewRecordsSample();
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
        CountryRecord[] data = new CountryRecord[] { new CountryRecord("US", "United States", 298444215), new CountryRecord("CH", "China", 1313973713), new CountryRecord("JA", "Japan", 127463611) };
        final ListGrid countryGrid = new ListGrid();
        countryGrid.setWidth(500);
        countryGrid.setAutoFitMaxRecords(6);
        countryGrid.setAutoFitData(Autofit.VERTICAL);
        countryGrid.setCanEdit(true);
        countryGrid.setEditEvent(ListGridEditEvent.CLICK);
        countryGrid.setListEndEditAction(RowEndEditAction.NEXT);
        ListGridField countryCodeField = new ListGridField("countryCode", "Country Code");
        ListGridField nameField = new ListGridField("countryName", "Country");
        ListGridField populationField = new ListGridField("population", "Population");
        countryGrid.setFields(countryCodeField, nameField, populationField);
        countryGrid.setData(data);
        IButton button = new IButton("Edit New");
        button.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                countryGrid.startEditingNew();
            }
        });
        VLayout vLayout = new VLayout(20);
        vLayout.addMember(countryGrid);
        vLayout.addMember(button);
        return vLayout;
    }

    public String getIntro() {
        return DESCRIPTION;
    }
}
