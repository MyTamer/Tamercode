package com.jaxws.json.codec.encode;

import java.beans.BeanDescriptor;
import java.beans.IndexedPropertyDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.CharacterIterator;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.jaxws.json.codec.BeanAware;
import com.jaxws.json.codec.DateFormat;
import com.jaxws.json.codec.JSONCodec;
import com.jaxws.json.codec.JSONFault;
import com.jaxws.json.codec.PublicFieldPropertyDescriptor;
import com.jaxws.json.codec.decode.WSJSONPopulator;
import com.jaxws.json.feature.JSONWebService;
import com.jaxws.json.serializer.JSONObjectCustomizer;

/**
 * @author Sundaramurthi
 * 
 * JSON response writter.
 */
public class WSJSONWriter extends BeanAware {

    private static final Logger LOG = Logger.getLogger(WSJSONWriter.class.getName());

    private static final String XML_DEFAULT = "##default";

    private static final String NULL = " ";

    /**
	 * HEX character list for unicode generation.
	 */
    static final char[] hex = "0123456789ABCDEF".toCharArray();

    /**
     * JSON output stream. Initialized while object construction.
     * 
     */
    private final OutputStream output;

    /**
     * rootObject 
     */
    private final Object rootObject;

    /**
     * Cyclic finder Object stack
     */
    private final Stack<Object> stack;

    /**
     * List of user registered JSON customizer.
     */
    private final Map<Class<? extends Object>, JSONObjectCustomizer> objectCustomizers;

    /**
	 * Date format used to serialize JSON
	 * 
	 * User may customize this property using JSON web service configuration property file. 
	 */
    private DateFormat dateFormat = DateFormat.RFC3339;

    /**
	 * Either include or exclude property specified this expression flag turn true. 
	 * As a result JSON path expression constructed to handle include and/or exclude
	 * 
	 * If flag goes true there is miner performance delay may happen
	 */
    private boolean buildExpr = false;

    /**
	 * Currently processing JSON expression stack. Used to match include and/or exclude configuration. 
	 */
    private String exprStack = "";

    /**
	 * List of properties that excluded from serialization
	 */
    private Collection<Pattern> excludeProperties;

    /**
	 * List of properties that included from serialization
	 */
    private Collection<Pattern> includeProperties;

    /**
	 * 
	 */
    private Pattern listMapKey;

    /**
	 * 
	 */
    private Pattern listMapValue;

    /**
	 * Flag which enable write object as possible JSON document format or not.
	 */
    private boolean metaDataMode = false;

    private boolean schemaMode = false;

    /**
	 * List of response attachments.
	 */
    private List<Map<String, Object>> attachments = new ArrayList<Map<String, Object>>();

    private static final Pattern pattern = Pattern.compile("[\"\b\t\f\n\r/\\\\\\x00\\x1F\\x7F\\x9F]");

    private Matcher matcher = pattern.matcher("");

    private final Stack<Class<?>> stackNillableInstances;

    /**
     * Writer instance with parameter passed writer object.
     * @param writer
     */
    public WSJSONWriter(OutputStream output, Object rootObject, Map<Class<? extends Object>, JSONObjectCustomizer> objectCustomizers) {
        if (output == null) {
            throw new RuntimeException("Writer can't be null");
        }
        if (rootObject == null) {
            throw new RuntimeException("rootObject can't be null");
        }
        this.output = output;
        this.rootObject = rootObject;
        this.stack = new Stack<Object>();
        this.stackNillableInstances = new Stack<Class<?>>();
        this.objectCustomizers = objectCustomizers != null ? objectCustomizers : new HashMap<Class<? extends Object>, JSONObjectCustomizer>();
    }

    /**
     * Serialize passed object to JSON string, writes into constructor passed writer object.
     * @param object Map object to serialize.
     */
    public void write(DateFormat dateFormat, Collection<Pattern> excludeProperties, Collection<Pattern> includeProperties, Pattern listMapKey, Pattern listMapValue) {
        this.initValues(dateFormat, excludeProperties, includeProperties, listMapKey, listMapValue);
        this.process(rootObject, null, null);
    }

    /**
     * Serialize passed object to JSON string, writes into constructor passed writer object. 
     * For null properties new instance created and serialized as meta data.
     * @param object Map object to serialize.
     */
    public void writeMetadata(DateFormat dateFormat, Collection<Pattern> excludeProperties, Collection<Pattern> includeProperties, Pattern listMapKey, Pattern listMapValue) {
        this.writeMetadata(dateFormat, excludeProperties, includeProperties, listMapKey, listMapValue, false);
    }

    /**
     * Serialize passed object to JSON string, writes into constructor passed writer object. 
     * For null properties new instance created and serialized as meta data.
     * @param object Map object to serialize.
     */
    public void writeMetadata(DateFormat dateFormat, Collection<Pattern> excludeProperties, Collection<Pattern> includeProperties, Pattern listMapKey, Pattern listMapValue, boolean isSchema) {
        this.initValues(dateFormat, excludeProperties, includeProperties, listMapKey, listMapValue);
        this.schemaMode = isSchema;
        this.metaDataMode = true;
        this.process(rootObject, null, null);
    }

    /**
     * Utility method to serialize object.
     * @param rootObject
     * @param objectCustomizers
     * @return
     */
    public static final String writeMetadata(Object rootObject, Map<Class<? extends Object>, JSONObjectCustomizer> objectCustomizers, boolean isSchema) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        WSJSONWriter writter = new WSJSONWriter(out, rootObject, objectCustomizers);
        writter.writeMetadata(JSONCodec.dateFormat, JSONCodec.excludeProperties, JSONCodec.includeProperties, JSONCodec.globalMapKeyPattern, JSONCodec.globalMapValuePattern, isSchema);
        return out.toString();
    }

    /**
     * Utility method to serialize object.
     * @param rootObject
     * @param objectCustomizers
     * @return
     */
    public static final String writeMetadata(Object rootObject, Map<Class<? extends Object>, JSONObjectCustomizer> objectCustomizers) {
        return writeMetadata(rootObject, objectCustomizers, false);
    }

    /**
     * Pass write iniatated values.
     * @param dateFormat
     * @param excludeProperties
     * @param includeProperties
     * @param listMapKey
     * @param listMapValue
     */
    private void initValues(DateFormat dateFormat, Collection<Pattern> excludeProperties, Collection<Pattern> includeProperties, Pattern listMapKey, Pattern listMapValue) {
        if (dateFormat != null) {
            this.dateFormat = dateFormat;
        }
        this.excludeProperties = excludeProperties;
        this.includeProperties = includeProperties;
        this.listMapKey = listMapKey;
        this.listMapValue = listMapValue;
        this.buildExpr = ((excludeProperties != null) && !excludeProperties.isEmpty()) || ((includeProperties != null) && !includeProperties.isEmpty());
    }

    /**
     * Object processing entry.
     * Serialize object into json
     * @param object
     * @param method
     * @throws JSONFault
     */
    private void process(Object object, Method method, PropertyDescriptor descriptor) throws JSONFault {
        if (object == null) {
            this.add("null");
            return;
        }
        Class<?> clazz = object.getClass();
        JSONWebService customInfo = method != null ? method.getAnnotation(JSONWebService.class) : null;
        if (this.stack.contains(object) && !(clazz.isPrimitive() || clazz.equals(String.class))) {
            LOG.log(Level.FINE, "Cyclic reference detected on " + object);
            if (object instanceof ArrayList && this.schemaMode) {
                this.stack.push(object);
            } else {
                this.add("null");
                return;
            }
        } else {
            this.stack.push(object);
        }
        if (this.objectCustomizers.containsKey(clazz)) {
            this.objectCustomizers.get(clazz).encode(this.output, object);
            return;
        }
        if (JSONCodec.listWrapperSkip && !(clazz.isPrimitive() || clazz.isEnum())) {
            Object wrapperContent = getWrapperList(object, clazz);
            if (wrapperContent != null) {
                object = wrapperContent;
            }
        }
        if (this.schemaMode) {
            this.add("{");
            if (descriptor != null) {
                this.add("\"title\":\"" + descriptor.getDisplayName() + "\"," + "\"description\":\"" + descriptor.getShortDescription() + "\"," + "\"readonly\":" + (descriptor.getWriteMethod() == null) + ",");
            } else if (!isJSONPrimitive(clazz)) {
                BeanDescriptor beanDescriptor = getBeanDescriptor(clazz);
                if (beanDescriptor != null) this.add("\"title\":\"" + beanDescriptor.getDisplayName() + "\"," + "\"description\":\"" + beanDescriptor.getShortDescription() + "\",");
            }
            if (object instanceof String || object instanceof Character || object instanceof Locale || object instanceof Class) {
                this.add("\"type\":\"string\",\"nillable\":true,\"default\":");
            } else if (object instanceof Number) {
                this.add("\"type\":\"number\",\"default\":");
            } else if (object instanceof Boolean) {
                this.add("\"type\":\"boolean\",\"default\":");
            } else if (object instanceof Date) {
                this.add("\"type\":\"string\",\"nillable\":true,\"default\":");
            } else if (object instanceof Calendar) {
                this.add("\"type\":\"string\",\"nillable\":true,\"default\":");
            } else if (object instanceof Map) {
                this.add("\"type\":\"object\",\"properties\":");
            } else if (object.getClass().isArray()) {
                this.add("\"type\":\"array\",\"items\":");
            } else if (object instanceof Iterable) {
                this.add("\"type\":\"array\",\"items\":");
            } else if (object instanceof Enum) {
                this.add("\"type\":\"string\",\"enum\":[");
                for (Object o : clazz.getEnumConstants()) {
                    this.add("\"" + ((Enum<?>) o).name() + "\",");
                }
                this.add("null],\"default\":");
            } else if (object instanceof JAXBElement<?>) {
            } else if (object instanceof Node) {
                this.add("\"type\":\"any\",\"default\":");
            } else {
                this.add("\"type\":\"object\",\"properties\":");
            }
        }
        if (object instanceof String || object instanceof Character || object instanceof Locale || object instanceof Class) {
            this.string(object.toString());
        } else if (object instanceof Number) {
            this.add(object.toString());
        } else if (object instanceof Boolean) {
            this.bool(((Boolean) object).booleanValue());
        } else if (object instanceof Date) {
            this.date((Date) object, method, customInfo);
        } else if (object instanceof Calendar) {
            this.date(((Calendar) object).getTime(), method, customInfo);
        } else if (object instanceof Map) {
            this.map((Map<?, ?>) object, method, customInfo);
        } else if (object.getClass().isArray()) {
            this.array(object, method, customInfo);
        } else if (object instanceof Iterable) {
            this.array(((Iterable<?>) object).iterator(), method, customInfo);
        } else if (object instanceof Enum) {
            this.enumeration((Enum<?>) object, clazz);
        } else if (object instanceof JAXBElement<?>) {
            this.process(((JAXBElement<?>) object).getValue(), null, descriptor);
        } else if (object instanceof Node) {
            this.xmlNode((Node) object, null, false);
        } else {
            this.bean(object, clazz);
        }
        if (this.schemaMode) {
            this.add("}");
        }
        this.stack.pop();
    }

    /**
     * Step 5.1:  Convert to string with escape.
     * escape characters
     */
    private void string(String string) {
        this.add('"');
        if (!matcher.reset(string).find()) {
            add(string);
        } else {
            CharacterIterator it = new StringCharacterIterator(string);
            for (char c = it.first(); c != CharacterIterator.DONE; c = it.next()) {
                if (c == '"') {
                    this.add("\\\"");
                } else if (c == '\\') {
                    this.add("\\\\");
                } else if (c == '/') {
                    this.add("\\/");
                } else if (c == '\b') {
                    this.add("\\b");
                } else if (c == '\f') {
                    this.add("\\f");
                } else if (c == '\n') {
                    this.add("\\n");
                } else if (c == '\r') {
                    this.add("\\r");
                } else if (c == '\t') {
                    this.add("\\t");
                } else if (Character.isISOControl(c)) {
                    this.unicode(c);
                } else {
                    this.add(Character.toString(c));
                }
            }
        }
        this.add('"');
    }

    /**
     * Step 5.2:  Add directly to writer, 
     * Add object to buffer
     */
    private void add(String obj) {
        try {
            this.output.write(obj.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Step 5.3: add boolean
     * Add boolean to buffer
     */
    private void bool(boolean b) {
        this.add(b ? "true" : "false");
    }

    /**
     * Step 5.4, 5.5: add date
     * Add date to buffer
     */
    private void date(Date date, Method method, JSONWebService customInfo) {
        assert date != null;
        DateFormat currentFormat = (customInfo != null && customInfo.format().length() > 0) ? DateFormat.CUSTOM : this.dateFormat;
        switch(currentFormat) {
            case PLAIN:
                this.add(String.valueOf(date.getTime()));
                break;
            case CUSTOM:
                this.string(this.date2String(date, customInfo.format()));
                return;
            default:
                this.string(this.date2String(date, currentFormat.getFormat()));
        }
    }

    /**
     * Step 5.6: add date
     * Add map to buffer
     */
    private void map(Map<?, ?> map, Method method, JSONWebService customInfo) throws JSONFault {
        this.add("{");
        Iterator<?> it = map.entrySet().iterator();
        boolean hasData = false;
        while (it.hasNext()) {
            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) it.next();
            Object key = entry.getKey();
            String expr = null;
            if (this.buildExpr) {
                if (key == null) {
                    LOG.log(Level.WARNING, "Cannot build expression for null key in " + this.exprStack);
                    continue;
                } else {
                    expr = this.expandExpr(key.toString());
                    if (this.shouldExcludeProperty(expr, customInfo)) {
                        continue;
                    }
                    expr = this.setExprStack(expr);
                }
            }
            if (hasData) {
                this.add(',');
            }
            hasData = true;
            this.string(String.valueOf(key));
            this.add(":");
            this.process(entry.getValue(), method, null);
            if (this.buildExpr) {
                this.setExprStack(expr);
            }
        }
        this.add("}");
    }

    /**
     * Step 5.7: add as array.
     * Add array to buffer
     */
    private void array(Object object, Method method, JSONWebService customInfo) throws JSONFault {
        this.add("[");
        int length = Array.getLength(object);
        boolean hasData = false;
        for (int i = 0; i < length; ++i) {
            String expr = null;
            if (this.buildExpr) {
                expr = this.expandExpr(i);
                if (this.shouldExcludeProperty(expr, customInfo)) {
                    continue;
                }
                expr = this.setExprStack(expr);
            }
            if (hasData) {
                this.add(',');
            }
            hasData = true;
            this.process(Array.get(object, i), method, null);
            if (this.buildExpr) {
                this.setExprStack(expr);
            }
        }
        this.add("]");
    }

    /**
     * Step 5.8: add as list array.
     * Add array to buffer
     */
    private void array(Iterator<?> iterator, Method method, JSONWebService customInfo) throws JSONFault {
        if (listMapKey != null && method != null && method.getGenericReturnType() != null && method.getGenericReturnType() instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments();
            if (types.length == 1) {
                try {
                    Class<?> clazz = (Class<?>) types[0];
                    PropertyDescriptor[] props = getBeanProperties(clazz);
                    PropertyDescriptor keyProperty = null;
                    PropertyDescriptor valueProperty = null;
                    for (PropertyDescriptor prop : props) {
                        if (listMapKey.matcher(clazz.getName() + "." + prop.getName()).find()) {
                            keyProperty = prop;
                            break;
                        }
                    }
                    if (listMapValue != null) {
                        for (PropertyDescriptor prop : props) {
                            if (listMapValue.matcher(clazz.getName() + "." + prop.getName()).find()) {
                                valueProperty = prop;
                                break;
                            }
                        }
                    }
                    if (keyProperty != null) {
                        Method keyReadMethod = keyProperty.getReadMethod();
                        Method valueReadMethod = valueProperty != null ? valueProperty.getReadMethod() : null;
                        HashMap<String, Object> map = new LinkedHashMap<String, Object>();
                        while (iterator.hasNext()) {
                            Object ob = iterator.next();
                            map.put(String.valueOf(keyReadMethod.invoke(ob)), valueReadMethod != null ? valueReadMethod.invoke(ob) : ob);
                        }
                        map(map, keyReadMethod, customInfo);
                        return;
                    }
                } catch (Throwable th) {
                }
            }
        }
        if (!this.schemaMode) this.add("[");
        if (this.metaDataMode && method != null && method.getGenericReturnType() != null) {
            Class<?> parameterType = (Class<?>) ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0];
            if (parameterType.equals(Object.class) || WSJSONPopulator.isJSONPrimitive(parameterType)) {
                if (method != null) {
                    this.process(getMetaDataInstance(parameterType, customInfo, getDeclaredField(method.getDeclaringClass(), Introspector.decapitalize(method.getName().substring(3)))), null, null);
                }
            } else {
                this.process(getNewInstance(parameterType), method, null);
            }
        }
        boolean hasData = false;
        for (int i = 0; iterator.hasNext(); i++) {
            String expr = null;
            if (this.buildExpr) {
                expr = this.expandExpr(i);
                if (this.shouldExcludeProperty(expr, customInfo)) {
                    iterator.next();
                    continue;
                }
                expr = this.setExprStack(expr);
            }
            if (hasData) {
                this.add(',');
            }
            hasData = true;
            this.process(iterator.next(), method, null);
            if (this.buildExpr) {
                this.setExprStack(expr);
            }
        }
        if (!this.schemaMode) this.add("]");
    }

    /**
     * Step 5.9: add as Enumeration.
     * Instrospect an Enum and serialize it as a name/value pair or as a bean including all its own properties
     */
    private void enumeration(Enum<?> enumeration, Class<?> clazz) throws JSONFault {
        try {
            PropertyDescriptor[] props = getBeanProperties(clazz);
            if (props.length > 0) {
                this.bean(enumeration, clazz);
            } else {
                String value = enumeration.name();
                try {
                    value = clazz.getDeclaredField(value).getAnnotation(XmlEnumValue.class).value();
                } catch (Throwable th) {
                }
                ;
                this.string(value);
            }
        } catch (IntrospectionException e) {
            this.string(enumeration.name());
        }
    }

    /**
     * Step 5.10: serialize as Object
     * Instrospect bean and serialize its properties
     */
    private void bean(Object object, Class<?> clazz) throws JSONFault {
        assert object != null && clazz != null && !clazz.isPrimitive();
        this.add("{");
        boolean hasData = false;
        if (clazz.equals(Object.class)) {
            clazz = object.getClass();
            hasData = hasData | this.add("class", clazz.getName(), null, hasData, null);
        }
        try {
            PropertyDescriptor[] props = getBeanProperties(clazz);
            nextProperty: for (PropertyDescriptor property : props) {
                Class<?> propertyType = property.getPropertyType();
                String name = property.getName();
                if (this.shouldHardExcludeProperty(name)) {
                    continue;
                }
                Method accessor = property.getReadMethod();
                if (accessor == null) {
                    if (property instanceof IndexedPropertyDescriptor) {
                        IndexedPropertyDescriptor idexedProp = (IndexedPropertyDescriptor) property;
                        accessor = idexedProp.getReadMethod();
                        propertyType = idexedProp.getIndexedPropertyType();
                    } else if (propertyType.isAssignableFrom(Boolean.class)) {
                        try {
                            accessor = clazz.getMethod("is" + name.substring(0, 1).toUpperCase() + name.substring(1), ((Class[]) null));
                        } catch (Throwable th) {
                        }
                    }
                }
                if (accessor != null) {
                    Object value = null;
                    try {
                        value = accessor.invoke(object, new Object[0]);
                    } catch (Throwable th) {
                        if (property instanceof PublicFieldPropertyDescriptor) {
                            value = ((PublicFieldPropertyDescriptor) property).getValue(object);
                        }
                    }
                    if (value == null) {
                        try {
                            Field declaredField = getDeclaredField(clazz, name);
                            XmlElement xmlElm = declaredField.getAnnotation(XmlElement.class);
                            if (xmlElm != null) {
                                if (!xmlElm.defaultValue().equals(NULL) && isJSONPrimitive(propertyType)) {
                                    value = xmlElm.defaultValue();
                                } else if (!xmlElm.nillable() && createDefaultOnNonNullable) {
                                    if (!isJSONPrimitive(propertyType)) {
                                        if (!stackNillableInstances.contains(propertyType)) {
                                            stackNillableInstances.push(propertyType);
                                            value = getNewInstance(propertyType);
                                            hasData = this.add(name, value, null, hasData, property) || hasData;
                                            stackNillableInstances.pop();
                                        }
                                        continue nextProperty;
                                    } else {
                                    }
                                }
                            }
                        } catch (Throwable th) {
                        }
                        if (value == null && JSONCodec.excludeNullProperties && !this.metaDataMode) {
                            continue nextProperty;
                        } else if (this.metaDataMode) {
                            value = getMetaDataInstance(propertyType, accessor.getAnnotation(JSONWebService.class), getDeclaredField(clazz, name));
                        }
                    } else if (this.metaDataMode && propertyType.isPrimitive()) {
                        value = getMetaDataInstance(propertyType, accessor.getAnnotation(JSONWebService.class), getDeclaredField(clazz, name));
                    }
                    JSONWebService properyConfig = accessor.getAnnotation(JSONWebService.class);
                    if (properyConfig != null) {
                        if (!properyConfig.serialize()) continue; else if (properyConfig.name().length() > 0) name = properyConfig.name();
                    } else {
                        try {
                            Field declaredField = getDeclaredField(clazz, name);
                            XmlElement xmlElm = declaredField.getAnnotation(XmlElement.class);
                            if (declaredField.isAnnotationPresent(XmlMimeType.class)) {
                                if (this.metaDataMode) {
                                    value = declaredField.getAnnotation(XmlMimeType.class).value();
                                } else {
                                    Map<String, Object> attachment = new HashMap<String, Object>();
                                    ;
                                    attachment.put("name", name);
                                    attachment.put("value", value);
                                    attachment.put("mimeType", declaredField.getAnnotation(XmlMimeType.class).value());
                                    attachments.add(attachment);
                                    continue nextProperty;
                                }
                            } else if (xmlElm != null) {
                                if (!xmlElm.name().equals(XML_DEFAULT)) {
                                    name = xmlElm.name();
                                }
                            } else if (declaredField.isAnnotationPresent(XmlElements.class) && Collection.class.isAssignableFrom(declaredField.getType()) && value instanceof Collection) {
                                XmlElements xmlElms = declaredField.getAnnotation(XmlElements.class);
                                Collection<?> valueList = (Collection<?>) value;
                                if (!valueList.isEmpty()) {
                                    ArrayList<Map<String, Object>> group = new ArrayList<Map<String, Object>>();
                                    for (Object ob : valueList) {
                                        for (XmlElement elm : xmlElms.value()) {
                                            if (((Class<?>) elm.type()).isAssignableFrom(ob.getClass())) {
                                                Map<String, Object> objectMap = new HashMap<String, Object>();
                                                objectMap.put(elm.name(), ob);
                                                group.add(objectMap);
                                                break;
                                            }
                                        }
                                    }
                                    hasData = this.add(name, group, null, hasData, property) || hasData;
                                    continue nextProperty;
                                } else {
                                    if (!this.metaDataMode) {
                                        continue nextProperty;
                                    } else {
                                        name = "CHOICE";
                                        Map<String, Object> choices = new HashMap<String, Object>();
                                        for (XmlElement elm : xmlElms.value()) {
                                            try {
                                                choices.put(elm.name(), getMetaDataInstance(elm.type(), null, null));
                                            } catch (Throwable th) {
                                                choices.put(elm.name(), "object");
                                            }
                                        }
                                        value = choices;
                                    }
                                }
                            } else if (declaredField.isAnnotationPresent(XmlElementRefs.class) && Collection.class.isAssignableFrom(declaredField.getType()) && value instanceof Collection) {
                                XmlElementRefs xmlElms = declaredField.getAnnotation(XmlElementRefs.class);
                                Collection<?> valueList = (Collection<?>) value;
                                if (!valueList.isEmpty()) {
                                    Map<String, List<Object>> group = new HashMap<String, List<Object>>();
                                    for (Object ob : valueList) {
                                        for (XmlElementRef elm : xmlElms.value()) {
                                            if (ob instanceof JAXBElement<?>) {
                                                JAXBElement<?> element = (JAXBElement<?>) ob;
                                                if (element.getName().getLocalPart().equals(elm.name())) {
                                                    name = elm.name();
                                                    if (!group.containsKey(name)) group.put(name, new ArrayList<Object>());
                                                    group.get(name).add(element.getValue());
                                                    break;
                                                }
                                            } else {
                                                if (((Class<?>) elm.type()).isAssignableFrom(ob.getClass())) {
                                                    name = elm.name();
                                                    if (!group.containsKey(name)) group.put(name, new ArrayList<Object>());
                                                    group.get(name).add(ob);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    for (Map.Entry<String, List<Object>> entry : group.entrySet()) {
                                        hasData = this.add(entry.getKey(), entry.getValue(), null, hasData, property) || hasData;
                                    }
                                    continue nextProperty;
                                } else {
                                    if (!this.metaDataMode) {
                                        continue nextProperty;
                                    } else {
                                        name = "CHOICE";
                                        Map<String, Object> choices = new HashMap<String, Object>();
                                        for (XmlElementRef elm : xmlElms.value()) {
                                            try {
                                                choices.put(elm.name(), getMetaDataInstance(elm.type(), null, null));
                                            } catch (Throwable th) {
                                                choices.put(elm.name(), "object");
                                            }
                                        }
                                        value = choices;
                                    }
                                }
                            } else if (declaredField.isAnnotationPresent(XmlAttribute.class)) {
                                if (!declaredField.getAnnotation(XmlAttribute.class).name().equals(XML_DEFAULT)) {
                                    name = declaredField.getAnnotation(XmlAttribute.class).name();
                                }
                            } else if (accessor.isAnnotationPresent(XmlTransient.class) || (declaredField != null && declaredField.isAnnotationPresent(XmlTransient.class))) {
                                continue nextProperty;
                            }
                        } catch (Throwable th) {
                            LOG.log(Level.FINER, "Processing xml annatation failed for field: " + name);
                        }
                    }
                    String expr = null;
                    if (this.buildExpr) {
                        expr = this.expandExpr(name);
                        if (this.shouldExcludeProperty(expr, properyConfig)) {
                            continue;
                        }
                        expr = this.setExprStack(expr);
                    }
                    boolean propertyPrinted = this.add(name, value, accessor, hasData, property);
                    hasData = hasData || propertyPrinted;
                    if (this.buildExpr) {
                        this.setExprStack(expr);
                    }
                }
            }
            if (object instanceof Enum) {
                Object value = ((Enum<?>) object).name();
                this.add("_name", value, object.getClass().getMethod("name"), hasData, null);
            }
        } catch (Exception e) {
            throw new JSONFault("Server.json", "Failed to serialize object " + clazz.getName(), "JSONCodec", null, e);
        }
        this.add("}");
    }

    /**
     * Step 5.10.1: add object again
     * Add name/value pair to buffer
     */
    private boolean add(String name, Object value, Method method, boolean hasData, PropertyDescriptor descriptor) throws JSONFault {
        if (!JSONCodec.excludeNullProperties || value != null || this.metaDataMode) {
            if (hasData) {
                this.add(',');
            }
            this.add('"');
            this.add(name);
            this.add("\":");
            this.process(value, method, descriptor);
            return true;
        }
        return false;
    }

    /**
     * Step 5.10.11: process as xml elment
     * Add name/value pair to buffer
     */
    private boolean xmlNode(Node node, JSONWebService config, boolean hasData) {
        if (node instanceof Element) {
            Element elem = (Element) node;
            if (hasData) {
                this.add(',');
            }
            this.add('{');
            this.add('"');
            this.add(elem.getTagName());
            this.add("\":[");
            NodeList childs = elem.getChildNodes();
            int childLength = childs.getLength();
            for (int c = 0; c < childLength; c++) {
                hasData = xmlNode(childs.item(c), null, hasData && c != 0);
            }
            this.add("]}");
            return true;
        } else if (node instanceof Attr) {
            Attr atr = (Attr) node;
            return this.add(atr.getName(), atr.getValue(), null, false, null);
        }
        return false;
    }

    /**
     * Private method to chack exclude or include.
     * @param expr
     * @return
     */
    private boolean shouldExcludeProperty(String expr, JSONWebService config) {
        if (config != null && config.excludeProperties().length > 0) {
            for (String match : config.excludeProperties()) {
                if (Pattern.compile(match).matcher(expr).matches()) {
                    LOG.log(Level.FINEST, "Ignoring property because of exclude set to true in annotation: " + expr);
                    return true;
                }
            }
        }
        if (this.excludeProperties != null) {
            for (Pattern pattern : this.excludeProperties) {
                if (pattern.matcher(expr).matches()) {
                    LOG.log(Level.FINEST, "Ignoring property because of exclude rule: " + expr);
                    return true;
                }
            }
        }
        if (config != null && config.includeProperties().length > 0) {
            for (String match : config.includeProperties()) {
                if (Pattern.compile(match).matcher(expr).matches()) {
                    return false;
                }
            }
            LOG.log(Level.FINEST, "Ignoring property because of include rule set to true in annotation: " + expr);
        }
        if (this.includeProperties != null) {
            for (Pattern pattern : this.includeProperties) {
                if (pattern.matcher(expr).matches()) {
                    return false;
                }
            }
            LOG.log(Level.FINEST, "Ignoring property because of exclude rule: " + expr);
            return true;
        }
        return false;
    }

    private String expandExpr(int i) {
        return this.exprStack + "[" + i + "]";
    }

    private String expandExpr(String property) {
        if (this.exprStack.length() == 0) return property;
        return this.exprStack + "." + property;
    }

    private String setExprStack(String expr) {
        String s = this.exprStack;
        this.exprStack = expr;
        return s;
    }

    /**
     * Utility V  write as character
     * Add char to buffer
     */
    private void add(char c) {
        try {
            this.output.write(c);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Utility IV  write as unicode
     * Represent as unicode
     *
     * @param c character to be encoded
     */
    private void unicode(char c) {
        this.add("\\u");
        int n = c;
        for (int i = 0; i < 4; ++i) {
            int digit = (n & 0xf000) >> 12;
            this.add(hex[digit]);
            n <<= 4;
        }
    }

    /**
     * Utility III  never serializable properties. 
     * Ignore "class" field
     */
    private boolean shouldHardExcludeProperty(String name) throws SecurityException, NoSuchFieldException {
        if (name.equals("class") || name.equals("declaringClass") || name.equals("serialVersionUID")) {
            return true;
        }
        return false;
    }

    /**
     * Utility II to convert date to string.
     *
     * @param date
     * @return
     * @see
     */
    private String date2String(Date date, String timePattern) {
        if (timePattern == null || timePattern.trim().isEmpty()) {
            timePattern = "yyyy-MM-dd'T'HH:mm:ssZ";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(timePattern);
        if (JSONCodec.useTimezoneSeparator) {
            StringBuffer dateStr = new StringBuffer(formatter.format(date));
            return dateStr.length() > 22 ? dateStr.insert(22, ':').toString() : dateStr.toString();
        } else {
            return formatter.format(date);
        }
    }

    /**
 	 * Utility I method to read Object list from Wrapper 
 	 * @param object
 	 * @param clazz
 	 * @return
 	 */
    private Object getWrapperList(Object object, Class<?> clazz) {
        try {
            if (object == null || clazz == null || clazz.isPrimitive()) return null;
            XmlType xmlType = clazz.getAnnotation(XmlType.class);
            if (xmlType != null && xmlType.propOrder().length == 1 && xmlType.name().equals(xmlType.propOrder()[0] + "s")) {
                PropertyDescriptor[] props = getBeanProperties(clazz);
                if (props.length == 1 && Collection.class.isAssignableFrom(props[0].getReadMethod().getReturnType())) {
                    return props[0].getReadMethod().invoke(object, (Object[]) null);
                }
            }
        } catch (Throwable e) {
        }
        return null;
    }

    /**
 	 * Utility method return instance from class. For meta data document generation.
 	 * @param propertyType
 	 * @param field 
 	 * @param webService 
 	 * @return
 	 */
    private Object getMetaDataInstance(Class<?> propertyType, JSONWebService webService, Field field) {
        String defaultVal = null;
        if (field != null && field.isAnnotationPresent(XmlElement.class)) {
            XmlElement element = field.getAnnotation(XmlElement.class);
            if (!element.defaultValue().equals(NULL)) {
                if (propertyType.isEnum()) {
                    defaultVal = element.defaultValue();
                } else if (Boolean.TYPE.equals(propertyType) || Boolean.class.equals(propertyType)) {
                    return Boolean.valueOf(element.defaultValue());
                } else {
                    return element.defaultValue();
                }
            }
        }
        if (WSJSONPopulator.isJSONPrimitive(propertyType)) {
            if (Number.class.isAssignableFrom(propertyType) || Integer.TYPE.equals(propertyType) || Byte.TYPE.equals(propertyType) || Short.TYPE.equals(propertyType) || Long.TYPE.equals(propertyType)) {
                return 0;
            } else if (propertyType.isAssignableFrom(String.class)) {
                return "";
            } else if (propertyType.isAssignableFrom(Boolean.class) || Boolean.TYPE.equals(propertyType)) {
                return false;
            } else if (propertyType.isAssignableFrom(Date.class)) {
                return new Date();
            } else if (propertyType.isAssignableFrom(Calendar.class)) {
                return Calendar.getInstance();
            } else if (propertyType.isEnum()) {
                if (this.schemaMode) return propertyType.getEnumConstants()[0];
                StringBuffer b = new StringBuffer();
                if (defaultVal != null) {
                    b.append(defaultVal);
                }
                for (Object cont : propertyType.getEnumConstants()) {
                    String value = ((Enum<?>) cont).name();
                    try {
                        value = propertyType.getDeclaredField(value).getAnnotation(XmlEnumValue.class).value();
                    } catch (Throwable th) {
                    }
                    ;
                    if (value.equals(defaultVal)) continue;
                    b.append((b.length() != 0 ? "|" : "") + value);
                }
                return b.toString();
            } else if (Float.TYPE.equals(propertyType) || Double.TYPE.equals(propertyType)) {
                return 0.0;
            } else {
                return null;
            }
        } else {
            for (Object ob : stack) {
                if (ob.getClass().equals(propertyType)) {
                    return ob;
                }
            }
            Object instance = getNewInstance(propertyType);
            return instance == null ? propertyType.getSimpleName() : instance;
        }
    }

    /**
	 * Getter to return attachments
	 * @return
	 */
    public List<Map<String, Object>> getAttachments() {
        return attachments;
    }
}
