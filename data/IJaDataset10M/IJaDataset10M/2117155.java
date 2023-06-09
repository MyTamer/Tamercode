package org.zkoss.zkmob.xml;

import java.util.Vector;

/** A class for events indicating the start of a new element */
public class StartTag extends Tag {

    private Vector _attributes;

    private boolean _degenerated;

    private PrefixMap _prefixMap;

    /**
	 * creates a new StartTag. The attributes are not copied and may be reused
	 * in e.g. the DOM. So DO NOT CHANGE the attribute vector after handing
	 * over, the effects are undefined
	 */
    public StartTag(StartTag parent, String namespace, String name, Vector attributes, boolean degenerated, boolean processNamespaces) {
        super(Xml.START_TAG, parent, namespace, name);
        _attributes = (attributes == null || attributes.size() == 0) ? null : attributes;
        _degenerated = degenerated;
        _prefixMap = parent == null ? PrefixMap.DEFAULT : parent._prefixMap;
        if (!processNamespaces) {
            return;
        }
        boolean any = false;
        for (int i = getAttributeCount() - 1; i >= 0; i--) {
            Attribute attr = (Attribute) attributes.elementAt(i);
            String attrName = attr.getName();
            int cut = attrName.indexOf(':');
            String prefix;
            if (cut != -1) {
                prefix = attrName.substring(0, cut);
                attrName = attrName.substring(cut + 1);
            } else if (attrName.equals("xmlns")) {
                prefix = attrName;
                attrName = "";
            } else {
                continue;
            }
            if (!prefix.equals("xmlns")) {
                if (!prefix.equals("xml")) {
                    any = true;
                }
            } else {
                _prefixMap = new PrefixMap(_prefixMap, attrName, attr.getValue());
                attributes.removeElementAt(i);
            }
        }
        final int len = getAttributeCount();
        if (any) {
            for (int i = 0; i < len; i++) {
                Attribute attr = (Attribute) attributes.elementAt(i);
                String attrName = attr.getName();
                int cut = attrName.indexOf(':');
                if (cut == 0) throw new RuntimeException("illegal attribute name: " + attrName + " at " + this); else if (cut != -1) {
                    String attrPrefix = attrName.substring(0, cut);
                    if (!attrPrefix.equals("xml")) {
                        attrName = attrName.substring(cut + 1);
                        String attrNs = _prefixMap.getNamespace(attrPrefix);
                        if (attrNs == null) {
                            throw new RuntimeException("Undefined Prefix: " + attrPrefix + " in " + this);
                        }
                        attributes.setElementAt(new Attribute(attrNs, attrName, attr.getValue()), i);
                    }
                }
            }
        }
        int cut = name.indexOf(':');
        String prefix;
        if (cut == -1) {
            prefix = "";
        } else if (cut == 0) {
            throw new RuntimeException("illegal tag name: " + name + " at " + this);
        } else {
            prefix = name.substring(0, cut);
            _name = name.substring(cut + 1);
        }
        _namespace = _prefixMap.getNamespace(prefix);
        if (_namespace == null) {
            if (prefix.length() != 0) throw new RuntimeException("undefined prefix: " + prefix + " in " + _prefixMap + " at " + this);
            _namespace = Xml.NO_NAMESPACE;
        }
    }

    /** returns the attribute vector. May return null for no attributes. */
    public Vector getAttributes() {
        return _attributes;
    }

    public boolean getDegenerated() {
        return _degenerated;
    }

    public PrefixMap getPrefixMap() {
        return _prefixMap;
    }

    /**
	 * Simplified (!) toString method for debugging purposes only. In order to
	 * actually write valid XML, please use a XmlWriter.
	 */
    public String toString() {
        return "StartTag <" + _name + "> line: " + lineNumber + " attr: " + _attributes;
    }

    public void setPrefixMap(PrefixMap map) {
        _prefixMap = map;
    }
}
