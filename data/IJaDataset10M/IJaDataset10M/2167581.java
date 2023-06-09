package com.sun.org.apache.xalan.internal.xsltc.compiler;

import java.util.Vector;
import com.sun.org.apache.bcel.internal.generic.ALOAD;
import com.sun.org.apache.bcel.internal.generic.ASTORE;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.GETFIELD;
import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import com.sun.org.apache.xml.internal.serializer.ElemDesc;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import com.sun.org.apache.xml.internal.utils.XML11Char;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 * @author Morten Jorgensen
 * @author Erwin Bolwidt <ejb@klomp.org>
 * @author Gunnlaugur Briem <gthb@dimon.is>
 */
final class XslAttribute extends Instruction {

    private String _prefix;

    private AttributeValue _name;

    private AttributeValueTemplate _namespace = null;

    private boolean _ignore = false;

    private boolean _isLiteral = false;

    /**
     * Returns the name of the attribute
     */
    public AttributeValue getName() {
        return _name;
    }

    /**
     * Displays the contents of the attribute
     */
    public void display(int indent) {
        indent(indent);
        Util.println("Attribute " + _name);
        displayContents(indent + IndentIncrement);
    }

    /**
     * Parses the attribute's contents. Special care taken for namespaces.
     */
    public void parseContents(Parser parser) {
        boolean generated = false;
        final SymbolTable stable = parser.getSymbolTable();
        String name = getAttribute("name");
        String namespace = getAttribute("namespace");
        QName qname = parser.getQName(name, false);
        final String prefix = qname.getPrefix();
        if (((prefix != null) && (prefix.equals(XMLNS_PREFIX))) || (name.equals(XMLNS_PREFIX))) {
            reportError(this, parser, ErrorMsg.ILLEGAL_ATTR_NAME_ERR, name);
            return;
        }
        _isLiteral = Util.isLiteral(name);
        if (_isLiteral) {
            if (!XML11Char.isXML11ValidQName(name)) {
                reportError(this, parser, ErrorMsg.ILLEGAL_ATTR_NAME_ERR, name);
                return;
            }
        }
        final SyntaxTreeNode parent = getParent();
        final Vector siblings = parent.getContents();
        for (int i = 0; i < parent.elementCount(); i++) {
            SyntaxTreeNode item = (SyntaxTreeNode) siblings.elementAt(i);
            if (item == this) break;
            if (item instanceof XslAttribute) continue;
            if (item instanceof UseAttributeSets) continue;
            if (item instanceof LiteralAttribute) continue;
            if (item instanceof Text) continue;
            if (item instanceof If) continue;
            if (item instanceof Choose) continue;
            if (item instanceof CopyOf) continue;
            if (item instanceof VariableBase) continue;
            reportWarning(this, parser, ErrorMsg.STRAY_ATTRIBUTE_ERR, name);
        }
        if (namespace != null && namespace != Constants.EMPTYSTRING) {
            _prefix = lookupPrefix(namespace);
            _namespace = new AttributeValueTemplate(namespace, parser, this);
        } else if (prefix != null && prefix != Constants.EMPTYSTRING) {
            _prefix = prefix;
            namespace = lookupNamespace(prefix);
            if (namespace != null) {
                _namespace = new AttributeValueTemplate(namespace, parser, this);
            }
        }
        if (_namespace != null) {
            if (_prefix == null || _prefix == Constants.EMPTYSTRING) {
                if (prefix != null) {
                    _prefix = prefix;
                } else {
                    _prefix = stable.generateNamespacePrefix();
                    generated = true;
                }
            } else if (prefix != null && !prefix.equals(_prefix)) {
                _prefix = prefix;
            }
            name = _prefix + ":" + qname.getLocalPart();
            if ((parent instanceof LiteralElement) && (!generated)) {
                ((LiteralElement) parent).registerNamespace(_prefix, namespace, stable, false);
            }
        }
        if (parent instanceof LiteralElement) {
            ((LiteralElement) parent).addAttribute(this);
        }
        _name = AttributeValue.create(this, name, parser);
        parseChildren(parser);
    }

    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        if (!_ignore) {
            _name.typeCheck(stable);
            if (_namespace != null) {
                _namespace.typeCheck(stable);
            }
            typeCheckContents(stable);
        }
        return Type.Void;
    }

    /**
     *
     */
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        final ConstantPoolGen cpg = classGen.getConstantPool();
        final InstructionList il = methodGen.getInstructionList();
        if (_ignore) return;
        _ignore = true;
        if (_namespace != null) {
            il.append(methodGen.loadHandler());
            il.append(new PUSH(cpg, _prefix));
            _namespace.translate(classGen, methodGen);
            il.append(methodGen.namespace());
        }
        if (!_isLiteral) {
            LocalVariableGen nameValue = methodGen.addLocalVariable2("nameValue", Util.getJCRefType(STRING_SIG), il.getEnd());
            _name.translate(classGen, methodGen);
            il.append(new ASTORE(nameValue.getIndex()));
            il.append(new ALOAD(nameValue.getIndex()));
            final int check = cpg.addMethodref(BASIS_LIBRARY_CLASS, "checkAttribQName", "(" + STRING_SIG + ")V");
            il.append(new INVOKESTATIC(check));
            il.append(methodGen.loadHandler());
            il.append(DUP);
            il.append(new ALOAD(nameValue.getIndex()));
        } else {
            il.append(methodGen.loadHandler());
            il.append(DUP);
            _name.translate(classGen, methodGen);
        }
        if ((elementCount() == 1) && (elementAt(0) instanceof Text)) {
            il.append(new PUSH(cpg, ((Text) elementAt(0)).getText()));
        } else {
            il.append(classGen.loadTranslet());
            il.append(new GETFIELD(cpg.addFieldref(TRANSLET_CLASS, "stringValueHandler", STRING_VALUE_HANDLER_SIG)));
            il.append(DUP);
            il.append(methodGen.storeHandler());
            translateContents(classGen, methodGen);
            il.append(new INVOKEVIRTUAL(cpg.addMethodref(STRING_VALUE_HANDLER, "getValue", "()" + STRING_SIG)));
        }
        SyntaxTreeNode parent = getParent();
        if (parent instanceof LiteralElement && ((LiteralElement) parent).allAttributesUnique()) {
            int flags = 0;
            ElemDesc elemDesc = ((LiteralElement) parent).getElemDesc();
            if (elemDesc != null && _name instanceof SimpleAttributeValue) {
                String attrName = ((SimpleAttributeValue) _name).toString();
                if (elemDesc.isAttrFlagSet(attrName, ElemDesc.ATTREMPTY)) {
                    flags = flags | SerializationHandler.HTML_ATTREMPTY;
                } else if (elemDesc.isAttrFlagSet(attrName, ElemDesc.ATTRURL)) {
                    flags = flags | SerializationHandler.HTML_ATTRURL;
                }
            }
            il.append(new PUSH(cpg, flags));
            il.append(methodGen.uniqueAttribute());
        } else {
            il.append(methodGen.attribute());
        }
        il.append(methodGen.storeHandler());
    }
}
