package com.google.gwt.sample.showcase.client.content.i18n;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.sample.showcase.client.ContentWidget;
import com.google.gwt.sample.showcase.client.ShowcaseAnnotations.ShowcaseData;
import com.google.gwt.sample.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import com.google.gwt.sample.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import java.util.Date;

/**
 * Example file.
 */
@ShowcaseStyle(".cw-RedText")
public class CwDateTimeFormat extends ContentWidget {

    /**
   * The constants used in this Content Widget.
   */
    @ShowcaseSource
    public static interface CwConstants extends Constants, ContentWidget.CwConstants {

        String cwDateTimeFormatDescription();

        String cwDateTimeFormatFailedToParseInput();

        String cwDateTimeFormatFormattedLabel();

        String cwDateTimeFormatInvalidPattern();

        String cwDateTimeFormatName();

        String cwDateTimeFormatPatternLabel();

        String[] cwDateTimeFormatPatterns();

        String cwDateTimeFormatValueLabel();
    }

    /**
   * The {@link DateTimeFormat} that is currently being applied.
   */
    private DateTimeFormat activeFormat = null;

    /**
   * An instance of the constants.
   */
    @ShowcaseData
    private CwConstants constants;

    /**
   * The {@link Label} where the formatted value is displayed.
   */
    @ShowcaseData
    private Label formattedBox = null;

    /**
   * The {@link TextBox} that displays the current pattern.
   */
    @ShowcaseData
    private TextBox patternBox = null;

    /**
   * The {@link ListBox} that holds the patterns.
   */
    @ShowcaseData
    private ListBox patternList = null;

    /**
   * The {@link TextBox} where the user enters a value.
   */
    @ShowcaseData
    private TextBox valueBox = null;

    /**
   * Constructor.
   * 
   * @param constants the constants
   */
    public CwDateTimeFormat(CwConstants constants) {
        super(constants);
        this.constants = constants;
    }

    @Override
    public String getDescription() {
        return constants.cwDateTimeFormatDescription();
    }

    @Override
    public String getName() {
        return constants.cwDateTimeFormatName();
    }

    /**
   * Initialize this example.
   */
    @ShowcaseSource
    @Override
    public Widget onInitialize() {
        Grid layout = new Grid(4, 2);
        CellFormatter formatter = layout.getCellFormatter();
        layout.setCellSpacing(5);
        patternList = new ListBox();
        patternList.setWidth("17em");
        String[] patterns = constants.cwDateTimeFormatPatterns();
        for (String pattern : patterns) {
            patternList.addItem(pattern);
        }
        patternList.addChangeListener(new ChangeListener() {

            public void onChange(Widget sender) {
                updatePattern();
            }
        });
        layout.setHTML(0, 0, constants.cwDateTimeFormatPatternLabel());
        layout.setWidget(0, 1, patternList);
        patternBox = new TextBox();
        patternBox.setWidth("17em");
        patternBox.addKeyboardListener(new KeyboardListenerAdapter() {

            @Override
            public void onKeyUp(Widget sender, char keyCode, int modifiers) {
                updatePattern();
            }
        });
        layout.setWidget(1, 1, patternBox);
        valueBox = new TextBox();
        valueBox.setWidth("17em");
        valueBox.setText("13 September 1999 12:34:56");
        valueBox.addKeyboardListener(new KeyboardListenerAdapter() {

            @Override
            public void onKeyUp(Widget sender, char keyCode, int modifiers) {
                updateFormattedValue();
            }
        });
        layout.setHTML(2, 0, constants.cwDateTimeFormatValueLabel());
        layout.setWidget(2, 1, valueBox);
        formattedBox = new Label();
        formattedBox.setWidth("17em");
        layout.setHTML(3, 0, constants.cwDateTimeFormatFormattedLabel());
        layout.setWidget(3, 1, formattedBox);
        formatter.setVerticalAlignment(3, 0, HasVerticalAlignment.ALIGN_TOP);
        updatePattern();
        return layout;
    }

    /**
   * Show an error message. Pass in null to clear the error message.
   * 
   * @param errorMsg the error message
   */
    @ShowcaseSource
    private void showErrorMessage(String errorMsg) {
        if (errorMsg == null) {
            formattedBox.removeStyleName("cw-RedText");
        } else {
            formattedBox.setText(errorMsg);
            formattedBox.addStyleName("cw-RedText");
        }
    }

    /**
   * Update the formatted value based on the user entered value and pattern.
   */
    @ShowcaseSource
    private void updateFormattedValue() {
        String sValue = valueBox.getText();
        if (!sValue.equals("")) {
            try {
                Date date = new Date(sValue);
                String formattedValue = activeFormat.format(date);
                formattedBox.setText(formattedValue);
                showErrorMessage(null);
            } catch (IllegalArgumentException e) {
                showErrorMessage(constants.cwDateTimeFormatFailedToParseInput());
            }
        } else {
            formattedBox.setText("<None>");
        }
    }

    /**
   * Update the selected pattern based on the pattern in the list.
   */
    @ShowcaseSource
    private void updatePattern() {
        switch(patternList.getSelectedIndex()) {
            case 0:
                activeFormat = DateTimeFormat.getFullDateTimeFormat();
                patternBox.setText(activeFormat.getPattern());
                patternBox.setEnabled(false);
                break;
            case 1:
                activeFormat = DateTimeFormat.getLongDateTimeFormat();
                patternBox.setText(activeFormat.getPattern());
                patternBox.setEnabled(false);
                break;
            case 2:
                activeFormat = DateTimeFormat.getMediumDateTimeFormat();
                patternBox.setText(activeFormat.getPattern());
                patternBox.setEnabled(false);
                break;
            case 3:
                activeFormat = DateTimeFormat.getShortDateTimeFormat();
                patternBox.setText(activeFormat.getPattern());
                patternBox.setEnabled(false);
                break;
            case 4:
                activeFormat = DateTimeFormat.getFullDateFormat();
                patternBox.setText(activeFormat.getPattern());
                patternBox.setEnabled(false);
                break;
            case 5:
                activeFormat = DateTimeFormat.getLongDateFormat();
                patternBox.setText(activeFormat.getPattern());
                patternBox.setEnabled(false);
                break;
            case 6:
                activeFormat = DateTimeFormat.getMediumDateFormat();
                patternBox.setText(activeFormat.getPattern());
                patternBox.setEnabled(false);
                break;
            case 7:
                activeFormat = DateTimeFormat.getShortDateFormat();
                patternBox.setText(activeFormat.getPattern());
                patternBox.setEnabled(false);
                break;
            case 8:
                activeFormat = DateTimeFormat.getFullTimeFormat();
                patternBox.setText(activeFormat.getPattern());
                patternBox.setEnabled(false);
                break;
            case 9:
                activeFormat = DateTimeFormat.getLongTimeFormat();
                patternBox.setText(activeFormat.getPattern());
                patternBox.setEnabled(false);
                break;
            case 10:
                activeFormat = DateTimeFormat.getMediumTimeFormat();
                patternBox.setText(activeFormat.getPattern());
                patternBox.setEnabled(false);
                break;
            case 11:
                activeFormat = DateTimeFormat.getShortTimeFormat();
                patternBox.setText(activeFormat.getPattern());
                patternBox.setEnabled(false);
                break;
            case 12:
                patternBox.setEnabled(true);
                String pattern = patternBox.getText();
                try {
                    activeFormat = DateTimeFormat.getFormat(pattern);
                } catch (IllegalArgumentException e) {
                    showErrorMessage(constants.cwDateTimeFormatInvalidPattern());
                    return;
                }
                break;
        }
        updateFormattedValue();
    }
}
