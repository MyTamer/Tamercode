package gatech.mmpm.sensor.constant;

import org.jdom.Element;
import gatech.mmpm.ActionParameterType;
import gatech.mmpm.Context;
import gatech.mmpm.GameState;
import gatech.mmpm.sensor.Sensor;
import gatech.mmpm.util.Pair;
import gatech.mmpm.util.XMLWriter;

/**
 * Sensor that provides a constant Float value.
 * 
 * @author David Llanso 
 * @date November, 2009
 */
public class ConstantFloat extends ConstantSensor {

    public ConstantFloat() {
        _value = null;
    }

    public ConstantFloat(Float f) {
        _value = f;
    }

    public ConstantFloat(double d) {
        _value = (float) d;
    }

    public ConstantFloat(ConstantFloat cf) {
        super(cf);
        _value = cf._value.floatValue();
    }

    /**
	 * Return the type of the sensor.
	 * 
	 * Keep in mind that this is <em>not</em> the real Java type,
	 * but the MMPM type. See the 
	 * gatech.mmpm.ActionParameterType.getJavaType() method
	 * for more information.
	 * 
	 * @return Type of the sensor. 
	 */
    public ActionParameterType getType() {
        return ActionParameterType.FLOAT;
    }

    public Object evaluate(int cycle, GameState gs, String player, Context parameters) {
        return _value;
    }

    public Object clone() {
        return new ConstantFloat(_value);
    }

    /**
	 * Writes the LogicalOperator Sensor to an XMLWriter object 
	 * @param w The XMLWriter object
	 */
    public void writeToXML(XMLWriter w) {
        w.tagWithAttributes("Sensor", "type = '" + this.getClass().getName() + "'");
        w.tag("Value", ActionParameterType.FLOAT.toString(_value));
        w.tag("/Sensor");
    }

    /**
	 * Creates a Sensor from an xml Element.
	 * @param xml Element for creating the Sensor.
	 * @return Created Sensor.
	 */
    public static Sensor loadFromXMLInternal(Element xml) {
        try {
            Class<?> askedClass;
            askedClass = Class.forName(xml.getAttributeValue("type"));
            Class<? extends ConstantFloat> baseClass = askedClass.asSubclass(ConstantFloat.class);
            ConstantFloat ret = baseClass.newInstance();
            String value = xml.getChildText("Value");
            ret._value = (Float) ActionParameterType.FLOAT.fromString(value);
            return ret;
        } catch (Exception e) {
            System.out.println("System crashes when loading " + xml.getAttributeValue("type") + " sensor.");
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * Public method that provides the parameters that 
	 * this sensor uses to be evaluated. This method provides
	 * all the parameters that can be used in the evaluation, 
	 * nevertheless some sensor can be evaluated with only 
	 * some of them.
	 * 
	 * @return The list of needed parameters this sensor needs
	 * to be evaluated.
	 */
    public java.util.List<Pair<String, ActionParameterType>> getNeededParameters() {
        return _listOfNeededParameters;
    }

    /**
	 * Public static method that provides the parameters that 
	 * this sensor uses to be evaluated. This method provides
	 * all the parameters that can be used in the evaluation, 
	 * nevertheless some sensor can be evaluated with only 
	 * some of them.
	 * 
	 * @return The list of needed parameters this sensor needs
	 * to be evaluated.
	 */
    public static java.util.List<Pair<String, ActionParameterType>> getStaticNeededParameters() {
        return _listOfNeededParameters;
    }

    static java.util.List<Pair<String, ActionParameterType>> _listOfNeededParameters;

    static {
        _listOfNeededParameters = new java.util.LinkedList<Pair<String, ActionParameterType>>(gatech.mmpm.sensor.constant.ConstantSensor.getStaticNeededParameters());
    }

    /**
	 * Float value.
	 */
    Float _value;
}
