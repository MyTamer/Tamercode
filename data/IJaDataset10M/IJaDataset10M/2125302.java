package org.richfaces.demo.ajaxsupport;

import java.util.ArrayList;
import java.util.List;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

/**
 * @author Ilya Shaikovsky
 *
 */
public class SelectsBean {

    private String currentType = "";

    private String currentItem = "";

    public List<SelectItem> firstList = new ArrayList<SelectItem>();

    public List<SelectItem> secondList = new ArrayList<SelectItem>();

    private static final String[] FRUITS = { "Banana", "Cranberry", "Blueberry", "Orange" };

    private static final String[] VEGETABLES = { "Potatoes", "Broccoli", "Garlic", "Carrot" };

    public SelectsBean() {
        SelectItem item = new SelectItem("fruits", "Fruits");
        firstList.add(item);
        item = new SelectItem("vegetables", "Vegetables");
        firstList.add(item);
        for (int i = 0; i < FRUITS.length; i++) {
            item = new SelectItem(FRUITS[i]);
        }
    }

    public List<SelectItem> getFirstList() {
        return firstList;
    }

    public List<SelectItem> getSecondList() {
        return secondList;
    }

    public static String[] getFRUITS() {
        return FRUITS;
    }

    public static String[] getVEGETABLES() {
        return VEGETABLES;
    }

    public void valueChanged(ValueChangeEvent event) {
        secondList.clear();
        String[] currentItems;
        if (((String) event.getNewValue()).equals("fruits")) {
            currentItems = FRUITS;
        } else {
            currentItems = VEGETABLES;
        }
        for (int i = 0; i < currentItems.length; i++) {
            SelectItem item = new SelectItem(currentItems[i]);
            secondList.add(item);
        }
    }

    public String getCurrentType() {
        return currentType;
    }

    public void setCurrentType(String currentType) {
        this.currentType = currentType;
    }

    public String getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(String currentItem) {
        this.currentItem = currentItem;
    }
}
