package com.android.ide.eclipse.adt.internal.editors.descriptors;

import com.android.ide.eclipse.adt.AndroidConstants;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutConstants;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.resources.ResourceType;
import com.android.ide.eclipse.adt.internal.resources.DeclareStyleableInfo.AttributeInfo;
import com.android.ide.eclipse.adt.internal.resources.DeclareStyleableInfo.AttributeInfo.Format;
import com.android.sdklib.SdkConstants;
import org.eclipse.swt.graphics.Image;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility methods related to descriptors handling.
 */
public final class DescriptorsUtils {

    private static final String DEFAULT_WIDGET_PREFIX = "widget";

    private static final int JAVADOC_BREAK_LENGTH = 60;

    /**
     * The path in the online documentation for the manifest description.
     * <p/>
     * This is NOT a complete URL. To be used, it needs to be appended
     * to {@link AndroidConstants#CODESITE_BASE_URL} or to the local SDK
     * documentation.
     */
    public static final String MANIFEST_SDK_URL = "/reference/android/R.styleable.html#";

    public static final String IMAGE_KEY = "image";

    private static final String CODE = "$code";

    private static final String LINK = "$link";

    private static final String ELEM = "$elem";

    private static final String BREAK = "$break";

    /**
     * The {@link ITextAttributeCreator} interface is used by the appendAttribute() method
     * to provide a way for caller to override the kind of {@link TextAttributeDescriptor}
     * created for a give XML attribute name.
     */
    public interface ITextAttributeCreator {

        /**
         * Creates a new {@link TextAttributeDescriptor} instance for the given XML name,
         * UI name and tooltip.
         *
         * @param xmlName The XML attribute name.
         * @param uiName The UI attribute name.
         * @param nsUri The URI of the attribute. Can be null if attribute has no namespace.
         *              See {@link SdkConstants#NS_RESOURCES} for a common value.
         * @param tooltip An optional tooltip.
         * @return A new {@link TextAttributeDescriptor} (or derived) instance.
         */
        public TextAttributeDescriptor create(String xmlName, String uiName, String nsUri, String tooltip);
    }

    /**
     * Add all {@link AttributeInfo} to the the array of {@link AttributeDescriptor}.
     *
     * @param attributes The list of {@link AttributeDescriptor} to append to
     * @param elementXmlName Optional XML local name of the element to which attributes are
     *              being added. When not null, this is used to filter overrides.
     * @param nsUri The URI of the attribute. Can be null if attribute has no namespace.
     *              See {@link SdkConstants#NS_RESOURCES} for a common value.
     * @param infos The array of {@link AttributeInfo} to read and append to attributes
     * @param requiredAttributes An optional set of attributes to mark as "required" (i.e. append
     *        a "*" to their UI name as a hint for the user.) If not null, must contains
     *        entries in the form "elem-name/attr-name". Elem-name can be "*".
     * @param overrides A map [attribute name => TextAttributeDescriptor creator]. A creator
     *        can either by a Class<? extends TextAttributeDescriptor> or an instance of
     *        {@link ITextAttributeCreator} that instantiates the right TextAttributeDescriptor.
     */
    public static void appendAttributes(ArrayList<AttributeDescriptor> attributes, String elementXmlName, String nsUri, AttributeInfo[] infos, Set<String> requiredAttributes, Map<String, Object> overrides) {
        for (AttributeInfo info : infos) {
            boolean required = false;
            if (requiredAttributes != null) {
                String attr_name = info.getName();
                if (requiredAttributes.contains("*/" + attr_name) || requiredAttributes.contains(elementXmlName + "/" + attr_name)) {
                    required = true;
                }
            }
            appendAttribute(attributes, elementXmlName, nsUri, info, required, overrides);
        }
    }

    /**
     * Add an {@link AttributeInfo} to the the array of {@link AttributeDescriptor}.
     *
     * @param attributes The list of {@link AttributeDescriptor} to append to
     * @param elementXmlName Optional XML local name of the element to which attributes are
     *              being added. When not null, this is used to filter overrides.
     * @param info The {@link AttributeInfo} to append to attributes
     * @param nsUri The URI of the attribute. Can be null if attribute has no namespace.
     *              See {@link SdkConstants#NS_RESOURCES} for a common value.
     * @param required True if the attribute is to be marked as "required" (i.e. append
     *        a "*" to its UI name as a hint for the user.)
     * @param overrides A map [attribute name => TextAttributeDescriptor creator]. A creator
     *        can either by a Class<? extends TextAttributeDescriptor> or an instance of
     *        {@link ITextAttributeCreator} that instantiates the right TextAttributeDescriptor.
     */
    public static void appendAttribute(ArrayList<AttributeDescriptor> attributes, String elementXmlName, String nsUri, AttributeInfo info, boolean required, Map<String, Object> overrides) {
        AttributeDescriptor attr = null;
        String xmlLocalName = info.getName();
        String uiName = prettyAttributeUiName(info.getName());
        if (required) {
            uiName += "*";
        }
        String tooltip = null;
        String rawTooltip = info.getJavaDoc();
        if (rawTooltip == null) {
            rawTooltip = "";
        }
        String deprecated = info.getDeprecatedDoc();
        if (deprecated != null) {
            if (rawTooltip.length() > 0) {
                rawTooltip += "@@";
            }
            rawTooltip += "* Deprecated";
            if (deprecated.length() != 0) {
                rawTooltip += ": " + deprecated;
            }
            if (deprecated.length() == 0 || !deprecated.endsWith(".")) {
                rawTooltip += ".";
            }
        }
        Format[] formats_list = info.getFormats();
        int flen = formats_list.length;
        if (flen > 0) {
            HashSet<Format> formats_set = new HashSet<Format>();
            StringBuilder sb = new StringBuilder();
            if (rawTooltip != null && rawTooltip.length() > 0) {
                sb.append(rawTooltip);
                sb.append(" ");
            }
            if (sb.length() > 0) {
                sb.append("@@");
            }
            sb.append("[");
            for (int i = 0; i < flen; i++) {
                Format f = formats_list[i];
                formats_set.add(f);
                sb.append(f.toString().toLowerCase());
                if (i < flen - 1) {
                    sb.append(", ");
                }
            }
            sb.append("]");
            if (required) {
                sb.append(".@@* ");
                sb.append("Required.");
            }
            sb.append(" ");
            rawTooltip = sb.toString();
            tooltip = formatTooltip(rawTooltip);
            if (overrides != null) {
                for (Entry<String, Object> entry : overrides.entrySet()) {
                    String key = entry.getKey();
                    String elements[] = key.split("/");
                    String overrideAttrLocalName = null;
                    if (elements.length < 1) {
                        continue;
                    } else if (elements.length == 1) {
                        overrideAttrLocalName = elements[0];
                        elements = null;
                    } else {
                        overrideAttrLocalName = elements[elements.length - 1];
                        elements = elements[0].split(",");
                    }
                    if (overrideAttrLocalName == null || !overrideAttrLocalName.equals(xmlLocalName)) {
                        continue;
                    }
                    boolean ok_element = elements != null && elements.length < 1;
                    if (!ok_element && elements != null) {
                        for (String element : elements) {
                            if (element.equals("*") || element.equals(elementXmlName)) {
                                ok_element = true;
                                break;
                            }
                        }
                    }
                    if (!ok_element) {
                        continue;
                    }
                    Object override = entry.getValue();
                    if (override instanceof Class<?>) {
                        try {
                            @SuppressWarnings("unchecked") Class<? extends TextAttributeDescriptor> clazz = (Class<? extends TextAttributeDescriptor>) override;
                            Constructor<? extends TextAttributeDescriptor> cons;
                            cons = clazz.getConstructor(new Class<?>[] { String.class, String.class, String.class, String.class });
                            attr = cons.newInstance(new Object[] { xmlLocalName, uiName, nsUri, tooltip });
                        } catch (SecurityException e) {
                        } catch (NoSuchMethodException e) {
                        } catch (IllegalArgumentException e) {
                        } catch (InstantiationException e) {
                        } catch (IllegalAccessException e) {
                        } catch (InvocationTargetException e) {
                        }
                    } else if (override instanceof ITextAttributeCreator) {
                        attr = ((ITextAttributeCreator) override).create(xmlLocalName, uiName, nsUri, tooltip);
                    }
                }
            }
            if (attr == null) {
                if (formats_set.contains(Format.REFERENCE)) {
                    attr = new ReferenceAttributeDescriptor(xmlLocalName, uiName, nsUri, tooltip);
                } else if (formats_set.contains(Format.ENUM)) {
                    attr = new ListAttributeDescriptor(xmlLocalName, uiName, nsUri, tooltip, info.getEnumValues());
                } else if (formats_set.contains(Format.FLAG)) {
                    attr = new FlagAttributeDescriptor(xmlLocalName, uiName, nsUri, tooltip, info.getFlagValues());
                } else if (formats_set.contains(Format.BOOLEAN)) {
                    attr = new BooleanAttributeDescriptor(xmlLocalName, uiName, nsUri, tooltip);
                } else if (formats_set.contains(Format.STRING)) {
                    attr = new ReferenceAttributeDescriptor(ResourceType.STRING, xmlLocalName, uiName, nsUri, tooltip);
                }
            }
        }
        if (attr == null) {
            if (tooltip == null) {
                tooltip = formatTooltip(rawTooltip);
            }
            attr = new TextAttributeDescriptor(xmlLocalName, uiName, nsUri, tooltip);
        }
        attr.setDeprecated(info.getDeprecatedDoc() != null);
        attributes.add(attr);
    }

    /**
     * Indicates the the given {@link AttributeInfo} already exists in the ArrayList of
     * {@link AttributeDescriptor}. This test for the presence of a descriptor with the same
     * XML name.
     *
     * @param attributes The list of {@link AttributeDescriptor} to compare to.
     * @param nsUri The URI of the attribute. Can be null if attribute has no namespace.
     *              See {@link SdkConstants#NS_RESOURCES} for a common value.
     * @param info The {@link AttributeInfo} to know whether it is included in the above list.
     * @return True if this {@link AttributeInfo} is already present in
     *         the {@link AttributeDescriptor} list.
     */
    public static boolean containsAttribute(ArrayList<AttributeDescriptor> attributes, String nsUri, AttributeInfo info) {
        String xmlLocalName = info.getName();
        for (AttributeDescriptor desc : attributes) {
            if (desc.getXmlLocalName().equals(xmlLocalName)) {
                if (nsUri == desc.getNamespaceUri() || (nsUri != null && nsUri.equals(desc.getNamespaceUri()))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Create a pretty attribute UI name from an XML name.
     * <p/>
     * The original xml name starts with a lower case and is camel-case,
     * e.g. "maxWidthForView". The pretty name starts with an upper case
     * and has space separators, e.g. "Max width for view".
     */
    public static String prettyAttributeUiName(String name) {
        if (name.length() < 1) {
            return name;
        }
        StringBuffer buf = new StringBuffer();
        char c = name.charAt(0);
        buf.append((char) (c >= 'a' && c <= 'z' ? c + 'A' - 'a' : c));
        int len = name.length();
        for (int i = 1; i < len; i++) {
            c = name.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                buf.append(' ');
                if (c >= 'X' && c <= 'Z' && (i == len - 1 || (i < len - 1 && name.charAt(i + 1) >= 'A' && name.charAt(i + 1) <= 'Z'))) {
                    buf.append(c);
                } else {
                    buf.append((char) (c - 'A' + 'a'));
                }
            } else if (c == '_') {
                buf.append(' ');
            } else {
                buf.append(c);
            }
        }
        name = buf.toString();
        name = name.replaceAll("(?<=^| )sdk(?=$| )", "SDK");
        name = name.replaceAll("(?<=^| )uri(?=$| )", "URI");
        return name;
    }

    /**
     * Capitalizes the string, i.e. transforms the initial [a-z] into [A-Z].
     * Returns the string unmodified if the first character is not [a-z].
     *
     * @param str The string to capitalize.
     * @return The capitalized string
     */
    public static String capitalize(String str) {
        if (str == null || str.length() < 1 || str.charAt(0) < 'a' || str.charAt(0) > 'z') {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        sb.append((char) (str.charAt(0) + 'A' - 'a'));
        sb.append(str.substring(1));
        return sb.toString();
    }

    /**
     * Formats the javadoc tooltip to be usable in a tooltip.
     */
    public static String formatTooltip(String javadoc) {
        ArrayList<String> spans = scanJavadoc(javadoc);
        StringBuilder sb = new StringBuilder();
        boolean needBreak = false;
        for (int n = spans.size(), i = 0; i < n; ++i) {
            String s = spans.get(i);
            if (CODE.equals(s)) {
                s = spans.get(++i);
                if (s != null) {
                    sb.append('"').append(s).append('"');
                }
            } else if (LINK.equals(s)) {
                String base = spans.get(++i);
                String anchor = spans.get(++i);
                String text = spans.get(++i);
                if (base != null) {
                    base = base.trim();
                }
                if (anchor != null) {
                    anchor = anchor.trim();
                }
                if (text != null) {
                    text = text.trim();
                }
                if (text == null || text.length() == 0) {
                    text = anchor;
                }
                if (base != null && base.length() > 0) {
                    if (text == null || text.length() == 0) {
                        text = base;
                    }
                }
                if (text != null) {
                    sb.append(text);
                }
            } else if (ELEM.equals(s)) {
                s = spans.get(++i);
                if (s != null) {
                    sb.append(s);
                }
            } else if (BREAK.equals(s)) {
                needBreak = true;
            } else if (s != null) {
                if (needBreak && s.trim().length() > 0) {
                    sb.append('\r');
                }
                sb.append(s);
                needBreak = false;
            }
        }
        return sb.toString();
    }

    /**
     * Formats the javadoc tooltip to be usable in a FormText.
     * <p/>
     * If the descriptor can provide an icon, the caller should provide
     * elementsDescriptor.getIcon() as "image" to FormText, e.g.:
     * <code>formText.setImage(IMAGE_KEY, elementsDescriptor.getIcon());</code>
     *
     * @param javadoc The javadoc to format. Cannot be null.
     * @param elementDescriptor The element descriptor parent of the javadoc. Cannot be null.
     * @param androidDocBaseUrl The base URL for the documentation. Cannot be null. Should be
     *   <code>FrameworkResourceManager.getInstance().getDocumentationBaseUrl()</code>
     */
    public static String formatFormText(String javadoc, ElementDescriptor elementDescriptor, String androidDocBaseUrl) {
        ArrayList<String> spans = scanJavadoc(javadoc);
        String fullSdkUrl = androidDocBaseUrl + MANIFEST_SDK_URL;
        String sdkUrl = elementDescriptor.getSdkUrl();
        if (sdkUrl != null && sdkUrl.startsWith(MANIFEST_SDK_URL)) {
            fullSdkUrl = androidDocBaseUrl + sdkUrl;
        }
        StringBuilder sb = new StringBuilder();
        Image icon = elementDescriptor.getIcon();
        if (icon != null) {
            sb.append("<form><li style=\"image\" value=\"" + IMAGE_KEY + "\">");
        } else {
            sb.append("<form><p>");
        }
        for (int n = spans.size(), i = 0; i < n; ++i) {
            String s = spans.get(i);
            if (CODE.equals(s)) {
                s = spans.get(++i);
                if (elementDescriptor.getXmlName().equals(s) && fullSdkUrl != null) {
                    sb.append("<a href=\"");
                    sb.append(fullSdkUrl);
                    sb.append("\">");
                    sb.append(s);
                    sb.append("</a>");
                } else if (s != null) {
                    sb.append('"').append(s).append('"');
                }
            } else if (LINK.equals(s)) {
                String base = spans.get(++i);
                String anchor = spans.get(++i);
                String text = spans.get(++i);
                if (base != null) {
                    base = base.trim();
                }
                if (anchor != null) {
                    anchor = anchor.trim();
                }
                if (text != null) {
                    text = text.trim();
                }
                if (text == null || text.length() == 0) {
                    text = anchor;
                }
                if ((base == null || base.length() == 0) && fullSdkUrl != null) {
                    base = fullSdkUrl;
                }
                String url = null;
                if (base != null && base.length() > 0) {
                    if (base.startsWith("http")) {
                        url = base;
                        if (anchor != null && anchor.length() > 0) {
                            int pos = url.lastIndexOf('#');
                            if (pos < 0) {
                                url += "#";
                            } else if (pos < url.length() - 1) {
                                url = url.substring(0, pos + 1);
                            }
                            url += anchor;
                        }
                    } else if (text == null || text.length() == 0) {
                        text = base;
                    }
                }
                if (url != null && text != null) {
                    sb.append("<a href=\"");
                    sb.append(url);
                    sb.append("\">");
                    sb.append(text);
                    sb.append("</a>");
                } else if (text != null) {
                    sb.append("<b>").append(text).append("</b>");
                }
            } else if (ELEM.equals(s)) {
                s = spans.get(++i);
                if (sdkUrl != null && s != null) {
                    sb.append("<a href=\"");
                    sb.append(sdkUrl);
                    sb.append("\">");
                    sb.append(s);
                    sb.append("</a>");
                } else if (s != null) {
                    sb.append("<b>").append(s).append("</b>");
                }
            } else if (BREAK.equals(s)) {
            } else if (s != null) {
                sb.append(s);
            }
        }
        if (icon != null) {
            sb.append("</li></form>");
        } else {
            sb.append("</p></form>");
        }
        return sb.toString();
    }

    private static ArrayList<String> scanJavadoc(String javadoc) {
        ArrayList<String> spans = new ArrayList<String>();
        if (javadoc != null) {
            javadoc = javadoc.replaceAll("[ \t\f\r\n]+", " ");
        }
        Pattern p_link = Pattern.compile("\\{@link\\s+([^#\\}\\s]*)(?:#([^\\s\\}]*))?(?:\\s*([^\\}]*))?\\}(.*)");
        Pattern p_code = Pattern.compile("<code>(.+?)</code>(.*)");
        Pattern p_elem = Pattern.compile("@([\\w -]+)@(.*)");
        Pattern p_break = Pattern.compile("@@(.*)");
        Pattern p_open = Pattern.compile("([@<\\{])(.*)");
        Pattern p_text = Pattern.compile("([^@<\\{]+)(.*)");
        int currentLength = 0;
        String text = null;
        while (javadoc != null && javadoc.length() > 0) {
            Matcher m;
            String s = null;
            if ((m = p_code.matcher(javadoc)).matches()) {
                spans.add(CODE);
                spans.add(text = cleanupJavadocHtml(m.group(1)));
                javadoc = m.group(2);
                if (text != null) {
                    currentLength += text.length();
                }
            } else if ((m = p_link.matcher(javadoc)).matches()) {
                spans.add(LINK);
                spans.add(m.group(1));
                spans.add(m.group(2));
                spans.add(text = cleanupJavadocHtml(m.group(3)));
                javadoc = m.group(4);
                if (text != null) {
                    currentLength += text.length();
                }
            } else if ((m = p_elem.matcher(javadoc)).matches()) {
                spans.add(ELEM);
                spans.add(text = cleanupJavadocHtml(m.group(1)));
                javadoc = m.group(2);
                if (text != null) {
                    currentLength += text.length() - 2;
                }
            } else if ((m = p_break.matcher(javadoc)).matches()) {
                spans.add(BREAK);
                currentLength = 0;
                javadoc = m.group(1);
            } else if ((m = p_open.matcher(javadoc)).matches()) {
                s = m.group(1);
                javadoc = m.group(2);
            } else if ((m = p_text.matcher(javadoc)).matches()) {
                s = m.group(1);
                javadoc = m.group(2);
            } else {
                s = javadoc;
                javadoc = null;
            }
            if (s != null && s.length() > 0) {
                s = cleanupJavadocHtml(s);
                if (currentLength >= JAVADOC_BREAK_LENGTH) {
                    spans.add(BREAK);
                    currentLength = 0;
                }
                while (currentLength + s.length() > JAVADOC_BREAK_LENGTH) {
                    int pos = s.indexOf(' ', JAVADOC_BREAK_LENGTH - currentLength);
                    if (pos <= 0) {
                        break;
                    }
                    spans.add(s.substring(0, pos + 1));
                    spans.add(BREAK);
                    currentLength = 0;
                    s = s.substring(pos + 1);
                }
                spans.add(s);
                currentLength += s.length();
            }
        }
        return spans;
    }

    /**
     * Remove anything that looks like HTML from a javadoc snippet, as it is supported
     * neither by FormText nor a standard text tooltip.
     */
    private static String cleanupJavadocHtml(String s) {
        if (s != null) {
            s = s.replaceAll("&lt;", "\"");
            s = s.replaceAll("&gt;", "\"");
            s = s.replaceAll("<[^>]+>", "");
        }
        return s;
    }

    /**
     * Sets the default layout attributes for the a new UiElementNode.
     * <p/>
     * Note that ideally the node should already be part of a hierarchy so that its
     * parent layout and previous sibling can be determined, if any.
     * <p/>
     * This does not override attributes which are not empty.
     */
    public static void setDefaultLayoutAttributes(UiElementNode ui_node, boolean updateLayout) {
        boolean fill = ui_node.getDescriptor().hasChildren() && ui_node.getUiParent() instanceof UiDocumentNode;
        ui_node.setAttributeValue(LayoutConstants.ATTR_LAYOUT_WIDTH, fill ? LayoutConstants.VALUE_FILL_PARENT : LayoutConstants.VALUE_WRAP_CONTENT, false);
        ui_node.setAttributeValue(LayoutConstants.ATTR_LAYOUT_HEIGHT, fill ? LayoutConstants.VALUE_FILL_PARENT : LayoutConstants.VALUE_WRAP_CONTENT, false);
        String widget_id = getFreeWidgetId(ui_node);
        if (widget_id != null) {
            ui_node.setAttributeValue(LayoutConstants.ATTR_ID, widget_id, false);
        }
        ui_node.setAttributeValue(LayoutConstants.ATTR_TEXT, widget_id, false);
        if (updateLayout) {
            UiElementNode ui_parent = ui_node.getUiParent();
            if (ui_parent != null && ui_parent.getDescriptor().getXmlLocalName().equals(LayoutConstants.RELATIVE_LAYOUT)) {
                UiElementNode ui_previous = ui_node.getUiPreviousSibling();
                if (ui_previous != null) {
                    String id = ui_previous.getAttributeValue(LayoutConstants.ATTR_ID);
                    if (id != null && id.length() > 0) {
                        id = id.replace("@+", "@");
                        ui_node.setAttributeValue(LayoutConstants.ATTR_LAYOUT_BELOW, id, false);
                    }
                }
            }
        }
    }

    /**
     * Given a UI root node, returns the first available id that matches the
     * pattern "prefix%02d".
     * <p/>TabWidget is a special case and the method will always return "@android:id/tabs".
     *
     * @param uiNode The UI node that gives the prefix to match.
     * @return A suitable generated id in the attribute form needed by the XML id tag
     * (e.g. "@+id/something")
     */
    public static String getFreeWidgetId(UiElementNode uiNode) {
        String name = uiNode.getDescriptor().getXmlLocalName();
        if ("TabWidget".equals(name)) {
            return "@android:id/tabs";
        }
        return "@+id/" + getFreeWidgetId(uiNode.getUiRoot(), new Object[] { name, null, null, null });
    }

    /**
     * Given a UI root node, returns the first available id that matches the
     * pattern "prefix%02d".
     *
     * For recursion purposes, a "context" is given. Since Java doesn't have in-out parameters
     * in methods and we're not going to do a dedicated type, we just use an object array which
     * must contain one initial item and several are built on the fly just for internal storage:
     * <ul>
     * <li> prefix(String): The prefix of the generated id, i.e. "widget". Cannot be null.
     * <li> index(Integer): The minimum index of the generated id. Must start with null.
     * <li> generated(String): The generated widget currently being searched. Must start with null.
     * <li> map(Set<String>): A set of the ids collected so far when walking through the widget
     *                        hierarchy. Must start with null.
     * </ul>
     *
     * @param uiRoot The Ui root node where to start searching recursively. For the initial call
     *               you want to pass the document root.
     * @param params An in-out context of parameters used during recursion, as explained above.
     * @return A suitable generated id
     */
    @SuppressWarnings("unchecked")
    private static String getFreeWidgetId(UiElementNode uiRoot, Object[] params) {
        Set<String> map = (Set<String>) params[3];
        if (map == null) {
            params[3] = map = new HashSet<String>();
        }
        int num = params[1] == null ? 0 : ((Integer) params[1]).intValue();
        String generated = (String) params[2];
        String prefix = (String) params[0];
        if (generated == null) {
            int pos = prefix.indexOf('.');
            if (pos >= 0) {
                prefix = prefix.substring(pos + 1);
            }
            pos = prefix.indexOf('$');
            if (pos >= 0) {
                prefix = prefix.substring(pos + 1);
            }
            prefix = prefix.replaceAll("[^a-zA-Z]", "");
            if (prefix.length() == 0) {
                prefix = DEFAULT_WIDGET_PREFIX;
            }
            do {
                num++;
                generated = String.format("%1$s%2$02d", prefix, num);
            } while (map.contains(generated));
            params[0] = prefix;
            params[1] = num;
            params[2] = generated;
        }
        String id = uiRoot.getAttributeValue(LayoutConstants.ATTR_ID);
        if (id != null) {
            id = id.replace("@+id/", "");
            id = id.replace("@id/", "");
            if (map.add(id) && map.contains(generated)) {
                do {
                    num++;
                    generated = String.format("%1$s%2$02d", prefix, num);
                } while (map.contains(generated));
                params[1] = num;
                params[2] = generated;
            }
        }
        for (UiElementNode uiChild : uiRoot.getUiChildren()) {
            getFreeWidgetId(uiChild, params);
        }
        return (String) params[2];
    }
}
