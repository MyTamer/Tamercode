package com.sun.kvem.midp.pim;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;
import javax.microedition.pim.PIM;
import javax.microedition.pim.PIMException;
import javax.microedition.pim.PIMItem;
import javax.microedition.pim.PIMList;
import javax.microedition.pim.UnsupportedFieldException;

/**
 * Generic PIM list.
 *
 */
public abstract class AbstractPIMList implements PIMList {

    /** Label for the list. */
    private final String name;

    /** Items in the list. */
    private Vector items = new Vector();

    /** Read or write mode allowed for the list. */
    private int mode;

    /** Current state of the list. */
    private boolean open = true;

    /** The type of the list. */
    private final int type;

    /** List handle */
    private Object handle;

    /**
     * Base type for PIM list structures.
     * @param type identifier for list type
     * @param name label for the list
     * @param mode readable or writable mode
     * @param handle handle of the list
     */
    AbstractPIMList(int type, String name, int mode, Object handle) {
        this.type = type;
        this.name = name;
        this.mode = mode;
        this.handle = handle;
    }

    public void addCategory(String category) throws PIMException {
        checkWritePermission();
        checkOpen();
        checkNullCategory(category);
        PIMHandler handler = PIMHandler.getInstance();
        String[] categories = handler.getCategories(handle);
        for (int i = 0; i < categories.length; i++) {
            if (category.equals(categories[i])) {
                return;
            }
        }
        handler.addCategory(handle, category);
        String[] newCategories = new String[categories.length + 1];
        System.arraycopy(categories, 0, newCategories, 0, categories.length);
        newCategories[categories.length] = category;
    }

    public Enumeration itemsByCategory(String category) throws PIMException {
        checkReadPermission();
        checkOpen();
        Vector v = new Vector();
        if (category == null || category.equals("UNCATEGORIZED")) {
            for (Enumeration e = items.elements(); e.hasMoreElements(); ) {
                AbstractPIMItem item = (AbstractPIMItem) e.nextElement();
                if (item.getCategoriesRaw() == null) {
                    v.addElement(item);
                }
            }
        } else {
            for (Enumeration e = items.elements(); e.hasMoreElements(); ) {
                AbstractPIMItem item = (AbstractPIMItem) e.nextElement();
                if (item.isInCategory(category)) {
                    v.addElement(item);
                }
            }
        }
        return v.elements();
    }

    public Enumeration items(PIMItem matchingItem) throws PIMException {
        checkReadPermission();
        checkOpen();
        if (!equals(matchingItem.getPIMList())) {
            throw new IllegalArgumentException("Cannot match item " + "from another list");
        }
        int[] searchFields = matchingItem.getFields();
        int[] searchFieldDataTypes = new int[searchFields.length];
        Object[][] searchData = new Object[searchFields.length][];
        for (int i = 0; i < searchFields.length; i++) {
            searchFieldDataTypes[i] = getFieldDataType(searchFields[i]);
            searchData[i] = new Object[matchingItem.countValues(searchFields[i])];
            for (int j = 0; j < searchData[i].length; j++) {
                searchData[i][j] = ((AbstractPIMItem) matchingItem).getField(searchFields[i], false, false).getValue(j);
                switch(searchFieldDataTypes[i]) {
                    case PIMItem.STRING:
                        {
                            String s = (String) searchData[i][j];
                            if (s != null) {
                                searchData[i][j] = s.toUpperCase();
                            }
                            break;
                        }
                    case PIMItem.STRING_ARRAY:
                        {
                            String[] a = (String[]) searchData[i][j];
                            if (a != null) {
                                for (int k = 0; k < a.length; k++) {
                                    if (a[k] != null) {
                                        a[k] = a[k].toUpperCase();
                                    }
                                }
                            }
                            break;
                        }
                }
            }
        }
        Vector v = new Vector();
        nextItem: for (Enumeration e = items.elements(); e.hasMoreElements(); ) {
            AbstractPIMItem item = (AbstractPIMItem) e.nextElement();
            for (int i = 0; i < searchFields.length; i++) {
                int field = searchFields[i];
                int itemIndices = item.countValues(field);
                for (int j = 0; j < searchData[i].length; j++) {
                    boolean matchedThisIndex = false;
                    for (int k = 0; k < itemIndices && !matchedThisIndex; k++) {
                        Object value = item.getField(field, false, false).getValue(k);
                        switch(searchFieldDataTypes[i]) {
                            case PIMItem.DATE:
                            case PIMItem.INT:
                            case PIMItem.BOOLEAN:
                                {
                                    if (searchData[i][j].equals(value)) {
                                        matchedThisIndex = true;
                                    }
                                    break;
                                }
                            case PIMItem.BINARY:
                                {
                                    byte[] a = (byte[]) searchData[i][j];
                                    byte[] b = (byte[]) value;
                                    if (b != null && a.length == b.length) {
                                        boolean arrayMatches = true;
                                        for (int m = 0; m < a.length && arrayMatches; m++) {
                                            arrayMatches = (a[m] == b[m]);
                                        }
                                        matchedThisIndex = arrayMatches;
                                    }
                                    break;
                                }
                            case PIMItem.STRING:
                                {
                                    String s1 = (String) searchData[i][j];
                                    String s2 = (String) value;
                                    if (s2 == null) {
                                        if (s1 == null) {
                                            matchedThisIndex = true;
                                        }
                                    } else if (s2.toUpperCase().indexOf(s1) != -1) {
                                        matchedThisIndex = true;
                                    }
                                    break;
                                }
                            case PIMItem.STRING_ARRAY:
                                {
                                    String[] a = (String[]) searchData[i][j];
                                    String[] b = (String[]) value;
                                    if (a == null) {
                                        if (b == null) {
                                            matchedThisIndex = true;
                                        }
                                    } else if (b != null && a.length == b.length) {
                                        boolean arrayMatches = true;
                                        for (int m = 0; m < a.length && arrayMatches; m++) {
                                            if (a[m] != null && a[m].length() > 0) {
                                                arrayMatches = (b[m] != null) && b[m].toUpperCase().indexOf(a[m]) != -1;
                                            }
                                        }
                                        matchedThisIndex = arrayMatches;
                                    }
                                    break;
                                }
                        }
                    }
                    if (!matchedThisIndex) {
                        continue nextItem;
                    }
                }
            }
            v.addElement(item);
        }
        return v.elements();
    }

    public void close() throws PIMException {
        checkOpen();
        this.open = false;
        this.items = null;
        PIMHandler.getInstance().closeList(handle);
    }

    public String getArrayElementLabel(int stringArrayField, int arrayElement) {
        if (getFieldDataType(stringArrayField) != PIMItem.STRING_ARRAY) {
            throw new IllegalArgumentException("Not a string array field");
        }
        if (!isSupportedArrayElement(stringArrayField, arrayElement)) {
            throw new IllegalArgumentException("Invalid array element " + arrayElement);
        }
        return PIMHandler.getInstance().getArrayElementLabel(handle, stringArrayField, arrayElement);
    }

    public int[] getSupportedArrayElements(int stringArrayField) {
        if (getFieldDataType(stringArrayField) != PIMItem.STRING_ARRAY) {
            throw new IllegalArgumentException("Not a string array field");
        }
        return PIMHandler.getInstance().getSupportedArrayElements(handle, stringArrayField);
    }

    public String getAttributeLabel(int attribute) {
        checkAttribute(attribute);
        String label = PIMHandler.getInstance().getAttributeLabel(handle, attribute);
        if (label == null) {
            throw new IllegalArgumentException("Invalid attribute: " + attribute);
        }
        return label;
    }

    public int maxValues(int field) {
        try {
            checkField(field);
        } catch (UnsupportedFieldException e) {
            return 0;
        }
        return PIMHandler.getInstance().getMaximumValues(handle, field);
    }

    public boolean isSupportedAttribute(int field, int attribute) {
        if (!isSupportedField(field)) {
            return false;
        }
        if (attribute == PIMItem.ATTR_NONE) {
            return true;
        }
        int i = attribute;
        while ((i & 1) == 0 && i != 0) {
            i >>= 1;
        }
        if (i != 1) {
            return false;
        }
        return PIMHandler.getInstance().isSupportedAttribute(handle, field, attribute);
    }

    public int maxCategories() {
        return -1;
    }

    public String[] getCategories() throws PIMException {
        checkOpen();
        return PIMHandler.getInstance().getCategories(handle);
    }

    public boolean isCategory(String category) throws PIMException {
        checkOpen();
        checkNullCategory(category);
        String[] categories = PIMHandler.getInstance().getCategories(handle);
        for (int i = 0; i < categories.length; i++) {
            if (category.equals(categories[i])) {
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public int stringArraySize(int stringArrayField) {
        if (getFieldDataType(stringArrayField) != PIMItem.STRING_ARRAY) {
            throw new IllegalArgumentException("Not a string array field");
        }
        return PIMHandler.getInstance().getStringArraySize(handle, stringArrayField);
    }

    public boolean isSupportedField(int field) {
        return PIMHandler.getInstance().isSupportedField(handle, field);
    }

    public void deleteCategory(String category, boolean deleteUnassignedItems) throws PIMException {
        checkWritePermission();
        checkOpen();
        checkNullCategory(category);
        String[] categories = PIMHandler.getInstance().getCategories(handle);
        int categoryIndex = -1;
        for (int i = 0; i < categories.length && categoryIndex == -1; i++) {
            if (category.equals(categories[i])) {
                categoryIndex = i;
            }
        }
        if (categoryIndex == -1) {
            return;
        }
        String[] newCategories = new String[categories.length - 1];
        System.arraycopy(categories, 0, newCategories, 0, categoryIndex);
        System.arraycopy(categories, categoryIndex + 1, newCategories, categoryIndex, newCategories.length - categoryIndex);
        PIMHandler.getInstance().deleteCategory(handle, category);
        final AbstractPIMItem[] a = new AbstractPIMItem[items.size()];
        items.copyInto(a);
        for (int i = 0; i < a.length; i++) {
            if (a[i].isInCategory(category)) {
                a[i].removeFromCategory(category);
                if (deleteUnassignedItems && a[i].getCategories().length == 0) {
                    items.removeElement(a[i]);
                }
            }
        }
    }

    public Enumeration items() throws PIMException {
        checkReadPermission();
        checkOpen();
        final PIMItem[] data = new PIMItem[items.size()];
        items.copyInto(data);
        return new Enumeration() {

            /** Initial offset int the list. */
            int index = 0;

            /**
		 * Checks for more elements int the list.
		 * @return <code>true</code> if more elements exist
		 */
            public boolean hasMoreElements() {
                return index < data.length;
            }

            /**
		 * Fetched the next element int the list.
		 * @return next element int the list
		 */
            public Object nextElement() {
                try {
                    return data[index++];
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new NoSuchElementException();
                }
            }
        };
    }

    public int[] getSupportedAttributes(int field) {
        checkField(field);
        return PIMHandler.getInstance().getSupportedAttributes(handle, field);
    }

    public int getFieldDataType(int field) {
        int dataType = PIMHandler.getInstance().getFieldDataType(handle, field);
        if (dataType == -1) {
            throw AbstractPIMItem.complaintAboutField(type, field);
        }
        return dataType;
    }

    public String getFieldLabel(int field) {
        checkField(field);
        return PIMHandler.getInstance().getFieldLabel(handle, field);
    }

    public void renameCategory(String currentCategory, String newCategory) throws PIMException {
        checkWritePermission();
        checkOpen();
        if (currentCategory == null || newCategory == null) {
            throw new NullPointerException("Null category");
        }
        String[] categories = PIMHandler.getInstance().getCategories(handle);
        if (newCategory.equals(currentCategory)) {
            return;
        }
        int oldCategoryIndex = -1;
        int newCategoryIndex = -1;
        for (int i = 0; i < categories.length; i++) {
            if (currentCategory.equals(categories[i])) {
                oldCategoryIndex = i;
            } else if (newCategory.equals(categories[i])) {
                newCategoryIndex = i;
            }
        }
        if (oldCategoryIndex == -1) {
            throw new PIMException("No such category: " + currentCategory);
        }
        if (newCategoryIndex == -1) {
            categories[oldCategoryIndex] = newCategory;
            PIMHandler.getInstance().renameCategory(handle, currentCategory, newCategory);
        } else {
            String[] a = new String[categories.length - 1];
            System.arraycopy(categories, 0, a, 0, oldCategoryIndex);
            System.arraycopy(categories, oldCategoryIndex + 1, a, oldCategoryIndex, a.length - oldCategoryIndex);
            categories = a;
            PIMHandler.getInstance().deleteCategory(handle, currentCategory);
        }
        for (Enumeration e = items.elements(); e.hasMoreElements(); ) {
            AbstractPIMItem item = (AbstractPIMItem) e.nextElement();
            if (item.isInCategory(currentCategory)) {
                item.removeFromCategory(currentCategory);
                item.addToCategory(newCategory);
            }
        }
    }

    public boolean isSupportedArrayElement(int stringArrayField, int arrayElement) {
        int dataType = PIMHandler.getInstance().getFieldDataType(handle, stringArrayField);
        if (dataType != PIMItem.STRING_ARRAY) {
            return false;
        }
        return PIMHandler.getInstance().isSupportedArrayElement(handle, stringArrayField, arrayElement);
    }

    public int[] getSupportedFields() {
        return PIMHandler.getInstance().getSupportedFields(handle);
    }

    public Enumeration items(String matchingValue) throws PIMException {
        checkReadPermission();
        checkOpen();
        matchingValue = matchingValue.toUpperCase();
        Vector v = new Vector();
        nextItem: for (Enumeration e = items.elements(); e.hasMoreElements(); ) {
            AbstractPIMItem item = (AbstractPIMItem) e.nextElement();
            int[] fields = item.getFields();
            for (int i = 0; i < fields.length; i++) {
                switch(getFieldDataType(fields[i])) {
                    case PIMItem.STRING:
                        for (int j = item.countValues(fields[i]) - 1; j >= 0; j--) {
                            String value = item.getString(fields[i], j);
                            if (value != null) {
                                if (value.toUpperCase().indexOf(matchingValue) != -1) {
                                    v.addElement(item);
                                    continue nextItem;
                                }
                            }
                        }
                        break;
                    case PIMItem.STRING_ARRAY:
                        for (int j = item.countValues(fields[i]) - 1; j >= 0; j--) {
                            String[] a = item.getStringArray(fields[i], j);
                            if (a == null) {
                                continue;
                            }
                            for (int k = 0; k < a.length; k++) {
                                if (a[k] != null) {
                                    if (a[k].toUpperCase().indexOf(matchingValue) != -1) {
                                        v.addElement(item);
                                        continue nextItem;
                                    }
                                }
                            }
                        }
                        break;
                }
            }
        }
        return v.elements();
    }

    /**
     * Verifies read permission.
     * @throws SecurityException if operation is not permitted
     */
    protected void checkReadPermission() throws SecurityException {
        if (mode == PIM.WRITE_ONLY) {
            throw new SecurityException("List cannot be read");
        }
    }

    /**
     * Verifies write permission.
     * @throws SecurityException if operation is not permitted
     */
    protected void checkWritePermission() throws SecurityException {
        if (mode == PIM.READ_ONLY) {
            throw new SecurityException("List cannot be written");
        }
    }

    /**
     * Ensures that the list is already open.
     * @throws PIMException if list is closed
     */
    protected void checkOpen() throws PIMException {
        if (!open) {
            throw new PIMException("List is closed.");
        }
    }

    /**    
     * Ensure the category is not missing.
     * @throws NullPointerException if category is 
     * <code>null</code>
     * @param category data to validate
     */
    protected void checkNullCategory(String category) {
        if (category == null) {
            throw new NullPointerException("Null category");
        }
    }

    /**
     * Adds an item to a PIM list.
     * @param item item to add to the list
     */
    public void addItem(AbstractPIMItem item) {
        this.items.addElement(item);
        item.setPIMList(this);
    }

    /**
     * Removes an item from a PIM list.
     * @param item to add to the list
     * @throws  PIMException if the item was not int the list
     * @throws SecurityException if operation is not permitted
     * @throws NullPointerException if item is <code>null</code>
     */
    void removeItem(PIMItem item) throws PIMException {
        checkWritePermission();
        checkOpen();
        if (item == null) {
            throw new NullPointerException("Null item");
        }
        if (!this.items.removeElement(item)) {
            throw new PIMException("Item not in list");
        }
        ((AbstractPIMItem) item).remove();
    }

    /**
     * Commits an item to the database
     *
     * @param key the key of the item to commit. This can be null.
     * @param data the binary data for the item
     * @param categories list of item's categories
     * @return an updated non-null key for the item
     * @throws PIMException in case of I/O error
     */
    Object commit(Object key, byte[] data, String[] categories) throws PIMException {
        return PIMHandler.getInstance().commitListElement(handle, key, data, categories);
    }

    /**
     * Gets the type of this PIM list (PIM.CONTACT_LIST, PIM.EVENT_LIST or
     * PIM.TODO_LIST).
     * @return the type of the PIM list
     */
    int getType() {
        return type;
    }

    /**
     * Returns list handle
     *
     * @return handle of the list
     */
    public Object getHandle() {
        return handle;
    }

    /**
     * Ensures the field is valid.   
     * @throws   IllegalArgumentException if field is not a valid
     *           field (i.e. not a standard field and not an extended field).
     *           IllegalArgumentException takes precedence over
     *           UnsupportedFieldException when checking the provided field.
     * @throws   UnsupportedFieldException if the field is not supported in
     *           the implementing instance of the class.
     * @param field Field to validate
     */
    private void checkField(int field) {
        if (PIMHandler.getInstance().getFieldDataType(handle, field) == -1) {
            throw AbstractPIMItem.complaintAboutField(type, field);
        }
    }

    /**    
     * Ensures the attribute is valid.
     * @throws IllegalArgumentException more than one bit set
     * in attribute selector
     * @param attribute identifier for specific attribute
     */
    private void checkAttribute(int attribute) {
        if (attribute == 0) {
            return;
        } else {
            while ((attribute & 1) == 0 && attribute != 0) {
                attribute >>= 1;
            }
            if (attribute != 1) {
                throw new IllegalArgumentException("Invalid attribute: " + attribute);
            }
        }
    }
}
