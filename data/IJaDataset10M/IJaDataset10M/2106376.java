package blue.orchestra;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import junit.framework.TestCase;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import blue.BlueSystem;
import blue.utility.ObjectUtilities;
import electric.xml.Element;

/**
 * @author Steven Yi
 */
public class CloneTest extends TestCase {

    public void testSerialize() {
        Class[] instruments = BlueSystem.getInstrumentClasses();
        for (int i = 0; i < instruments.length; i++) {
            Class class1 = instruments[i];
            Instrument instr = null;
            try {
                instr = (Instrument) class1.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            assertNotNull(instr);
            if (instr == null) {
                continue;
            }
            Object obj = ObjectUtilities.clone(instr);
            assertNotNull(obj);
        }
    }

    public void testClone() {
        Class[] instruments = BlueSystem.getInstrumentClasses();
        for (int i = 0; i < instruments.length; i++) {
            Class class1 = instruments[i];
            Instrument instr = null;
            try {
                instr = (Instrument) class1.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            assertNotNull(instr);
            if (instr == null) {
                continue;
            }
            Instrument clone = (Instrument) ObjectUtilities.clone(instr);
            boolean isEqual = EqualsBuilder.reflectionEquals(instr, clone);
            if (!isEqual) {
                StringBuffer buffer = new StringBuffer();
                buffer.append("Problem with class: " + class1.getName() + "\n");
                buffer.append("Original Object\n");
                buffer.append(ToStringBuilder.reflectionToString(instr) + "\n");
                buffer.append("Cloned Object\n");
                buffer.append(ToStringBuilder.reflectionToString(clone) + "\n");
                System.out.println(buffer.toString());
            }
            assertTrue(isEqual);
            Element elem1 = instr.saveAsXML();
            Element elem2 = (clone).saveAsXML();
            assertEquals(elem1.getTextString(), elem2.getTextString());
        }
    }

    public void testLoadSave() {
        Class[] instruments = BlueSystem.getInstrumentClasses();
        for (int i = 0; i < instruments.length; i++) {
            Class class1 = instruments[i];
            Instrument instr = null;
            try {
                instr = (Instrument) class1.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            assertNotNull(instr);
            if (instr == null) {
                continue;
            }
            Element elem1 = instr.saveAsXML();
            Method m = null;
            try {
                m = class1.getMethod("loadFromXML", new Class[] { Element.class });
            } catch (SecurityException e1) {
                e1.printStackTrace();
            } catch (NoSuchMethodException e1) {
                e1.printStackTrace();
            }
            Instrument instr2 = null;
            assertNotNull(m);
            if (m == null) {
                continue;
            }
            try {
                instr2 = (Instrument) m.invoke(instr, new Object[] { elem1 });
            } catch (IllegalArgumentException e2) {
                e2.printStackTrace();
            } catch (IllegalAccessException e2) {
                e2.printStackTrace();
            } catch (InvocationTargetException e2) {
                e2.printStackTrace();
            }
            boolean isEqual = EqualsBuilder.reflectionEquals(instr, instr2);
            if (!isEqual) {
                StringBuffer buffer = new StringBuffer();
                buffer.append("Problem with class: " + class1.getName() + "\n");
                buffer.append("Original Object\n");
                buffer.append(ToStringBuilder.reflectionToString(instr) + "\n");
                buffer.append("Cloned Object\n");
                buffer.append(ToStringBuilder.reflectionToString(instr2) + "\n");
                System.out.println(buffer.toString());
            }
            assertTrue(isEqual);
        }
    }
}
