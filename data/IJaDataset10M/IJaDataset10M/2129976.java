package edu.ncssm.iwp.objects;

import edu.ncssm.iwp.math.*;
import edu.ncssm.iwp.plugin.IWPObjectXmlCreator;
import edu.ncssm.iwp.toolkit.xml.*;

public class DObject_OutputXMLCreator implements IWPObjectXmlCreator {

    DObject_Output object;

    MCalculatorXMLCreator calculatorCreator;

    public DObject_OutputXMLCreator(DObject_Output object) {
        this.object = object;
    }

    public XMLElement getElement() {
        MCalculatorXMLCreator calcCreator = new MCalculatorXMLCreator(object.getCalculator());
        XMLElement elem = new XMLElement("output");
        elem.addElement(new XMLElement("name", "" + object.getName()));
        elem.addElement(new XMLElement("text", "" + object.getText()));
        elem.addElement(new XMLElement("units", "" + object.getUnits()));
        elem.addElement(calcCreator.getElement());
        if (!object.isVisible()) {
            elem.addElement(new XMLElement("hidden", "1"));
        }
        return elem;
    }
}
