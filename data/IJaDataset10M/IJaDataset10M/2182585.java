package au.org.emii.portal.composer;

import au.org.emii.portal.menu.MapLayer;
import au.org.emii.portal.settings.SettingsSupplementary;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.ala.spatial.analysis.web.ScatterplotWCController;
import org.ala.spatial.data.Query;
import org.ala.spatial.data.QueryField;
import org.ala.spatial.data.UploadQuery;
import org.ala.spatial.util.LegendMaker;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Slider;

/**
 *
 * @author Adam
 */
public class LayerLegendComposer extends GenericAutowireAutoforwardComposer {

    SettingsSupplementary settingsSupplementary = null;

    Slider opacitySlider;

    Label opacityLabel;

    Slider redSlider;

    Slider greenSlider;

    Slider blueSlider;

    Slider sizeSlider;

    Slider plotSizeSlider;

    Checkbox chkUncertaintySize;

    public Button btnPointsCluster;

    Label lblFupload;

    Label redLabel;

    Label greenLabel;

    Label blueLabel;

    Label sizeLabel;

    Label plotSizeLabel;

    Listbox activeLayersList;

    Div layerControls;

    Div uncertainty;

    Hbox uncertaintyLegend;

    Div colourChooser;

    Div sizeChooser;

    Image legendImg;

    Image legendImgUri;

    Div legendHtml;

    Label legendLabel;

    Div divUserColours;

    Combobox cbColour;

    Comboitem ciColourUser;

    Label layerName;

    EventListener listener;

    Query query;

    MapLayer mapLayer;

    @Override
    public void afterCompose() {
        super.afterCompose();
        cbColour.setSelectedIndex(0);
    }

    public void onScroll$opacitySlider(Event e) {
        float opacity = ((float) opacitySlider.getCurpos()) / 100;
        int percentage = (int) (opacity * 100);
        opacitySlider.setCurpos(percentage);
        opacityLabel.setValue(percentage + "%");
    }

    public void updateLegendImage() {
        LegendMaker lm = new LegendMaker();
        int red = redSlider.getCurpos();
        int blue = blueSlider.getCurpos();
        int green = greenSlider.getCurpos();
        Color c = new Color(red, green, blue);
        legendImg.setContent(lm.singleCircleImage(c, 50, 50, 20.0));
        sizeChooser.setVisible(true);
        if (cbColour.getSelectedItem() != ciColourUser) {
            legendHtml.setVisible(true);
            legendImg.setVisible(false);
            showPointsColourModeLegend();
        } else {
            legendImg.setVisible(true);
            legendHtml.setVisible(false);
        }
    }

    public void onScroll$sizeSlider() {
        int size = sizeSlider.getCurpos();
        sizeLabel.setValue(String.valueOf(size));
    }

    public void onScroll$plotSizeSlider() {
        int size = plotSizeSlider.getCurpos();
        plotSizeLabel.setValue(String.valueOf(size));
    }

    public void onScroll$blueSlider() {
        int blue = blueSlider.getCurpos();
        blueLabel.setValue(String.valueOf(blue));
        updateLegendImage();
    }

    public void onScroll$redSlider() {
        int red = redSlider.getCurpos();
        redLabel.setValue(String.valueOf(red));
        updateLegendImage();
    }

    public void onScroll$greenSlider() {
        int green = greenSlider.getCurpos();
        greenLabel.setValue(String.valueOf(green));
        updateLegendImage();
    }

    public void selectColour(Object obj) {
        Div div = (Div) obj;
        String style = div.getStyle();
        String background_color = "background-color";
        int a = style.indexOf(background_color);
        if (a >= 0) {
            String colour = style.substring(a + background_color.length() + 2, a + background_color.length() + 8);
            int r = Integer.parseInt(colour.substring(0, 2), 16);
            int g = Integer.parseInt(colour.substring(2, 4), 16);
            int b = Integer.parseInt(colour.substring(4, 6), 16);
            redSlider.setCurpos(r);
            greenSlider.setCurpos(g);
            blueSlider.setCurpos(b);
            redLabel.setValue(String.valueOf(r));
            greenLabel.setValue(String.valueOf(g));
            blueLabel.setValue(String.valueOf(b));
            updateLegendImage();
        }
    }

    public void onChange$cbColour(Event event) {
        updateUserColourDiv();
        updateLegendImage();
    }

    void updateUserColourDiv() {
        if (cbColour.getSelectedItem() == ciColourUser) {
            divUserColours.setVisible(true);
        } else {
            divUserColours.setVisible(false);
        }
    }

    void updateComboBoxesColour(MapLayer currentSelection) {
        if (currentSelection.isClustered()) {
            cbColour.setSelectedItem(ciColourUser);
            cbColour.setDisabled(true);
        } else {
            cbColour.setDisabled(false);
            for (int i = 0; i < cbColour.getItemCount(); i++) {
                if (cbColour.getItemAtIndex(i).getValue().equals(currentSelection.getColourMode())) {
                    cbColour.setSelectedIndex(i);
                }
            }
            updateUserColourDiv();
        }
    }

    void showPointsColourModeLegend() {
        while (legendHtml.getChildren().size() > 0) {
            legendHtml.removeChild(legendHtml.getFirstChild());
        }
        Map map = new HashMap();
        map.put("query", query);
        map.put("layer", mapLayer);
        map.put("readonly", "true");
        map.put("colourmode", (String) cbColour.getSelectedItem().getValue());
        if (!mapLayer.getColourMode().equals("grid") && query.getLegend((String) cbColour.getSelectedItem().getValue()).getCategories() != null) {
            map.put("checkmarks", "true");
        }
        map.put("disableselection", "true");
        try {
            Executions.createComponents("/WEB-INF/zul/AnalysisClassificationLegend.zul", legendHtml, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init(Query query, MapLayer mapLayer, int red, int green, int blue, int size, int opacity, String colourMode, EventListener listener) {
        this.query = query;
        this.mapLayer = mapLayer;
        opacitySlider.setCurpos(opacity);
        onScroll$opacitySlider(null);
        redSlider.setCurpos(red);
        onScroll$redSlider();
        greenSlider.setCurpos(green);
        onScroll$greenSlider();
        blueSlider.setCurpos(blue);
        onScroll$blueSlider();
        sizeSlider.setCurpos(mapLayer.getSizeVal());
        onScroll$sizeSlider();
        plotSizeSlider.setCurpos(size);
        onScroll$plotSizeSlider();
        for (Comboitem item : (List<Comboitem>) cbColour.getItems()) {
            if (item.getValue().equals(colourMode)) {
                cbColour.setSelectedItem(item);
                break;
            }
        }
        this.listener = listener;
        setupCBColour(query);
        updateUserColourDiv();
        updateLegendImage();
    }

    public int getRed() {
        return redSlider.getCurpos();
    }

    public int getGreen() {
        return greenSlider.getCurpos();
    }

    public int getBlue() {
        return blueSlider.getCurpos();
    }

    public int getSize() {
        return sizeSlider.getCurpos();
    }

    public int getPlotSize() {
        return plotSizeSlider.getCurpos();
    }

    public int getOpacity() {
        return opacitySlider.getCurpos();
    }

    public String getColourMode() {
        return (String) cbColour.getSelectedItem().getValue();
    }

    public void onClick$btnApply(Event event) {
        if (listener != null) {
            try {
                Component c = getParent().getFellowIfAny("scatterplotwindow");
                if (c != null && c instanceof ScatterplotWCController) {
                    ((ScatterplotWCController) c).updateFromLegend();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onClick$btnClose(Event event) {
        this.detach();
    }

    public void onClick$btnCancel(Event event) {
        this.detach();
    }

    private void setupCBColour(Query q) {
        for (int i = 0; i < cbColour.getItemCount(); i++) {
            if (cbColour.getItemAtIndex(i) != ciColourUser) {
                cbColour.removeItemAt(i);
                i--;
            }
        }
        if (q != null) {
            ArrayList<QueryField> fields = q.getFacetFieldList();
            Comboitem seperator = new Comboitem("seperator");
            seperator.setLabel("------------------Taxonomic------------------");
            seperator.setParent(cbColour);
            seperator.setDisabled(true);
            for (int i = 0; i < fields.size(); i++) {
                if (q != null && fields.get(i).getFieldType() == QueryField.FieldType.STRING && (q instanceof UploadQuery || !(fields.get(i).getName().equalsIgnoreCase("year") || fields.get(i).getName().equalsIgnoreCase("coordinate_uncertainty") || fields.get(i).getName().equalsIgnoreCase("month")))) {
                    System.out.println("adding to combobox: '" + fields.get(i).getName() + "'");
                    Comboitem ci = new Comboitem(fields.get(i).getDisplayName());
                    ci.setValue(fields.get(i).getName());
                    ci.setParent(cbColour);
                    if (ci.getValue().equals("rank")) {
                        Comboitem seperator1 = new Comboitem("seperator");
                        seperator1.setLabel("------------------Geospatial------------------");
                        seperator1.setParent(cbColour);
                        seperator1.setDisabled(true);
                    }
                    if (ci.getValue().equals("cl620")) {
                        Comboitem seperator3 = new Comboitem("seperator");
                        seperator3.setLabel("------------------Record Details------------------");
                        seperator3.setParent(cbColour);
                        seperator3.setDisabled(true);
                    }
                    if (ci.getValue().equals("collector")) {
                        Comboitem seperator4 = new Comboitem("seperator");
                        seperator4.setLabel("------------------Attribution------------------");
                        seperator4.setParent(cbColour);
                        seperator4.setDisabled(true);
                    }
                    if (ci.getValue().equals("institution_name")) {
                        Comboitem seperator5 = new Comboitem("seperator");
                        seperator5.setLabel("------------------Record Assertions------------------");
                        seperator5.setParent(cbColour);
                        seperator5.setDisabled(true);
                    }
                }
            }
        }
    }
}
