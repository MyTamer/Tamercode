package tests.api.java.util;

import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import java.util.Collections;
import tests.api.java.util.HashMapTest.ReusableKey;
import tests.support.Support_MapTest2;
import tests.support.Support_UnmodifiableCollectionTest;

@TestTargetClass(Hashtable.class)
public class HashtableTest extends junit.framework.TestCase {

    private Hashtable ht10;

    private Hashtable ht100;

    private Hashtable htfull;

    private Vector keyVector;

    private Vector elmVector;

    private String h10sVal;

    /**
     * @tests java.util.Hashtable#Hashtable()
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "Hashtable", args = {  })
    public void test_Constructor() {
        new Support_MapTest2(new Hashtable()).runTest();
        Hashtable h = new Hashtable();
        assertEquals("Created incorrect hashtable", 0, h.size());
    }

    /**
     * @tests java.util.Hashtable#Hashtable(int)
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "Hashtable", args = { int.class })
    public void test_ConstructorI() {
        Hashtable h = new Hashtable(9);
        assertEquals("Created incorrect hashtable", 0, h.size());
        Hashtable empty = new Hashtable(0);
        assertNull("Empty hashtable access", empty.get("nothing"));
        empty.put("something", "here");
        assertTrue("cannot get element", empty.get("something") == "here");
        try {
            new Hashtable(-1);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * @tests java.util.Hashtable#Hashtable(int, float)
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "Hashtable", args = { int.class, float.class })
    public void test_ConstructorIF() {
        Hashtable h = new java.util.Hashtable(10, 0.5f);
        assertEquals("Created incorrect hashtable", 0, h.size());
        Hashtable empty = new Hashtable(0, 0.75f);
        assertNull("Empty hashtable access", empty.get("nothing"));
        empty.put("something", "here");
        assertTrue("cannot get element", empty.get("something") == "here");
        try {
            new Hashtable(-1, 0.75f);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        }
        try {
            new Hashtable(0, -0.75f);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * @tests java.util.Hashtable#Hashtable(java.util.Map)
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "Hashtable", args = { java.util.Map.class })
    public void test_ConstructorLjava_util_Map() {
        Map map = new TreeMap();
        Object firstVal = "Gabba";
        Object secondVal = new Integer(5);
        map.put("Gah", firstVal);
        map.put("Ooga", secondVal);
        Hashtable ht = new Hashtable(map);
        assertTrue("a) Incorrect Hashtable constructed", ht.get("Gah") == firstVal);
        assertTrue("b) Incorrect Hashtable constructed", ht.get("Ooga") == secondVal);
        try {
            new Hashtable(null);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
        }
    }

    /**
     * @tests java.util.Hashtable#Hashtable(java.util.Map)
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "Hashtable", args = { java.util.Map.class })
    public void test_ConversionConstructorNullValue() {
        Map<String, Void> map = Collections.singletonMap("Dog", null);
        try {
            new Hashtable<String, Void>(map);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
        }
    }

    /**
     * @tests java.util.Hashtable#clear()
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "clear", args = {  })
    public void test_clear() {
        Hashtable h = hashtableClone(htfull);
        h.clear();
        assertEquals("Hashtable was not cleared", 0, h.size());
        Enumeration el = h.elements();
        Enumeration keys = h.keys();
        assertTrue("Hashtable improperly cleared", !el.hasMoreElements() && !(keys.hasMoreElements()));
    }

    /**
     * @tests java.util.Hashtable#clone()
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "clone", args = {  })
    public void test_clone() {
        Hashtable h = (Hashtable) htfull.clone();
        assertTrue("Clone different size than original", h.size() == htfull.size());
        Enumeration org = htfull.keys();
        Enumeration cpy = h.keys();
        String okey, ckey;
        while (org.hasMoreElements()) {
            assertTrue("Key comparison failed", (okey = (String) org.nextElement()).equals(ckey = (String) cpy.nextElement()));
            assertTrue("Value comparison failed", ((String) htfull.get(okey)).equals((String) h.get(ckey)));
        }
        assertTrue("Copy has more keys than original", !cpy.hasMoreElements());
    }

    /**
     * @tests java.util.Hashtable#contains(java.lang.Object)
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "contains", args = { java.lang.Object.class })
    public void test_containsLjava_lang_Object() {
        assertTrue("Element not found", ht10.contains("Val 7"));
        assertTrue("Invalid element found", !ht10.contains("ZZZZZZZZZZZZZZZZ"));
        try {
            ht10.contains(null);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
        }
    }

    /**
     * @tests java.util.Hashtable#containsKey(java.lang.Object)
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "containsKey", args = { java.lang.Object.class })
    public void test_containsKeyLjava_lang_Object() {
        assertTrue("Failed to find key", htfull.containsKey("FKey 4"));
        assertTrue("Failed to find key", !htfull.containsKey("FKey 99"));
        try {
            htfull.containsKey(null);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
        }
    }

    /**
     * @tests java.util.Hashtable#containsValue(java.lang.Object)
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "containsValue", args = { java.lang.Object.class })
    public void test_containsValueLjava_lang_Object() {
        Enumeration e = elmVector.elements();
        while (e.hasMoreElements()) assertTrue("Returned false for valid value", ht10.containsValue(e.nextElement()));
        assertTrue("Returned true for invalid value", !ht10.containsValue(new Object()));
        try {
            ht10.containsValue(null);
            fail("NullPointerException expected");
        } catch (NullPointerException ee) {
        }
    }

    /**
     * @tests java.util.Hashtable#elements()
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "elements", args = {  })
    public void test_elements() {
        Enumeration elms = ht10.elements();
        int i = 0;
        while (elms.hasMoreElements()) {
            String s = (String) elms.nextElement();
            assertTrue("Missing key from enumeration", elmVector.contains(s));
            ++i;
        }
        assertEquals("All keys not retrieved", 10, ht10.size());
    }

    /**
     * @tests java.util.Hashtable#entrySet()
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "entrySet", args = {  })
    public void test_entrySet() {
        Set s = ht10.entrySet();
        Set s2 = new HashSet();
        Iterator i = s.iterator();
        while (i.hasNext()) s2.add(((Map.Entry) i.next()).getValue());
        Enumeration e = elmVector.elements();
        while (e.hasMoreElements()) assertTrue("Returned incorrect entry set", s2.contains(e.nextElement()));
        boolean exception = false;
        try {
            ((Map.Entry) ht10.entrySet().iterator().next()).setValue(null);
        } catch (NullPointerException e1) {
            exception = true;
        }
        assertTrue("Should not be able to assign null to a Hashtable entrySet() Map.Entry", exception);
    }

    /**
     * @tests java.util.Hashtable#equals(java.lang.Object)
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "equals", args = { java.lang.Object.class })
    public void test_equalsLjava_lang_Object() {
        Hashtable h = hashtableClone(ht10);
        assertTrue("Returned false for equal tables", ht10.equals(h));
        assertTrue("Returned true for unequal tables", !ht10.equals(htfull));
    }

    /**
     * @tests java.util.Hashtable#get(java.lang.Object)
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "get", args = { java.lang.Object.class })
    public void test_getLjava_lang_Object() {
        Hashtable h = hashtableClone(htfull);
        assertEquals("Could not retrieve element", "FVal 2", ((String) h.get("FKey 2")));
    }

    /**
     * @tests java.util.Hashtable#hashCode()
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "hashCode", args = {  })
    public void test_hashCode() {
        Set entrySet = ht10.entrySet();
        Iterator iterator = entrySet.iterator();
        int expectedHash;
        for (expectedHash = 0; iterator.hasNext(); expectedHash += iterator.next().hashCode()) ;
        assertTrue("Incorrect hashCode returned.  Wanted: " + expectedHash + " got: " + ht10.hashCode(), expectedHash == ht10.hashCode());
    }

    /**
     * @tests java.util.Hashtable#isEmpty()
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "isEmpty", args = {  })
    public void test_isEmpty() {
        assertTrue("isEmpty returned incorrect value", !ht10.isEmpty());
        assertTrue("isEmpty returned incorrect value", new java.util.Hashtable().isEmpty());
        final Hashtable ht = new Hashtable();
        ht.put("0", "");
        Thread t1 = new Thread() {

            public void run() {
                while (!ht.isEmpty()) ;
                ht.put("final", "");
            }
        };
        t1.start();
        for (int i = 1; i < 10000; i++) {
            synchronized (ht) {
                ht.remove(String.valueOf(i - 1));
                ht.put(String.valueOf(i), "");
            }
            int size;
            if ((size = ht.size()) != 1) {
                String result = "Size is not 1: " + size + " " + ht;
                ht.clear();
                fail(result);
            }
        }
        ht.clear();
    }

    /**
     * @tests java.util.Hashtable#keys()
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "keys", args = {  })
    public void test_keys() {
        Enumeration keys = ht10.keys();
        int i = 0;
        while (keys.hasMoreElements()) {
            String s = (String) keys.nextElement();
            assertTrue("Missing key from enumeration", keyVector.contains(s));
            ++i;
        }
        assertEquals("All keys not retrieved", 10, ht10.size());
    }

    /**
     * @tests java.util.Hashtable#keys()
     */
    @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "", method = "keys", args = {  })
    public void test_keys_subtest0() {
        final Hashtable ht = new Hashtable(3);
        ht.put("initial", "");
        Enumeration en = ht.keys();
        en.hasMoreElements();
        ht.remove("initial");
        boolean exception = false;
        try {
            Object result = en.nextElement();
            assertTrue("unexpected: " + result, "initial".equals(result));
        } catch (NoSuchElementException e) {
            exception = true;
        }
        assertTrue("unexpected NoSuchElementException", !exception);
    }

    /**
     * @tests java.util.Hashtable#keySet()
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "keySet", args = {  })
    public void test_keySet() {
        Set s = ht10.keySet();
        Enumeration e = keyVector.elements();
        while (e.hasMoreElements()) assertTrue("Returned incorrect key set", s.contains(e.nextElement()));
        Map map = new Hashtable(101);
        map.put(new Integer(1), "1");
        map.put(new Integer(102), "102");
        map.put(new Integer(203), "203");
        Iterator it = map.keySet().iterator();
        Integer remove1 = (Integer) it.next();
        it.remove();
        Integer remove2 = (Integer) it.next();
        it.remove();
        ArrayList list = new ArrayList(Arrays.asList(new Integer[] { new Integer(1), new Integer(102), new Integer(203) }));
        list.remove(remove1);
        list.remove(remove2);
        assertTrue("Wrong result", it.next().equals(list.get(0)));
        assertEquals("Wrong size", 1, map.size());
        assertTrue("Wrong contents", map.keySet().iterator().next().equals(list.get(0)));
        Map map2 = new Hashtable(101);
        map2.put(new Integer(1), "1");
        map2.put(new Integer(4), "4");
        Iterator it2 = map2.keySet().iterator();
        Integer remove3 = (Integer) it2.next();
        Integer next;
        if (remove3.intValue() == 1) next = new Integer(4); else next = new Integer(1);
        it2.hasNext();
        it2.remove();
        assertTrue("Wrong result 2", it2.next().equals(next));
        assertEquals("Wrong size 2", 1, map2.size());
        assertTrue("Wrong contents 2", map2.keySet().iterator().next().equals(next));
    }

    /**
     * @tests java.util.Hashtable#keySet()
     */
    @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "", method = "keySet", args = {  })
    public void test_keySet_subtest0() {
        Set s1 = ht10.keySet();
        assertTrue("should contain key", s1.remove("Key 0"));
        assertTrue("should not contain key", !s1.remove("Key 0"));
        final int iterations = 10000;
        final Hashtable ht = new Hashtable();
        Thread t1 = new Thread() {

            public void run() {
                for (int i = 0; i < iterations; i++) {
                    ht.put(String.valueOf(i), "");
                    ht.remove(String.valueOf(i));
                }
            }
        };
        t1.start();
        Set set = ht.keySet();
        for (int i = 0; i < iterations; i++) {
            Iterator it = set.iterator();
            try {
                it.next();
                it.remove();
                int size;
                if ((size = ht.size()) < 0) {
                    fail("invalid size: " + size);
                }
            } catch (NoSuchElementException e) {
            } catch (ConcurrentModificationException e) {
            }
        }
    }

    /**
     * @tests java.util.Hashtable#put(java.lang.Object, java.lang.Object)
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "put", args = { java.lang.Object.class, java.lang.Object.class })
    public void test_putLjava_lang_ObjectLjava_lang_Object() {
        Hashtable h = hashtableClone(ht100);
        Integer key = new Integer(100);
        h.put("Value 100", key);
        assertTrue("Key/Value not inserted", h.size() == 1 && (h.contains(key)));
        h = hashtableClone(htfull);
        h.put("Value 100", key);
        assertTrue("Key/Value not inserted into full table", h.size() == 8 && (h.contains(key)));
        try {
            h.put(null, key);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
        }
        try {
            h.put("Value 100", null);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
        }
    }

    /**
     * @tests java.util.Hashtable#putAll(java.util.Map)
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "putAll", args = { java.util.Map.class })
    public void test_putAllLjava_util_Map() {
        Hashtable h = new Hashtable();
        h.putAll(ht10);
        Enumeration e = keyVector.elements();
        while (e.hasMoreElements()) {
            Object x = e.nextElement();
            assertTrue("Failed to put all elements", h.get(x).equals(ht10.get(x)));
        }
        try {
            h.putAll(null);
            fail("NullPointerException expected");
        } catch (NullPointerException ee) {
        }
    }

    /**
     * @tests java.util.Hashtable#remove(java.lang.Object)
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "remove", args = { java.lang.Object.class })
    public void test_removeLjava_lang_Object() {
        Hashtable h = hashtableClone(htfull);
        Object k = h.remove("FKey 0");
        assertTrue("Remove failed", !h.containsKey("FKey 0") || k == null);
        assertNull(h.remove("FKey 0"));
        try {
            h.remove(null);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
        }
    }

    /**
     * @tests java.util.Hashtable#size()
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "size", args = {  })
    public void test_size() {
        assertTrue("Returned invalid size", ht10.size() == 10 && (ht100.size() == 0));
        final Hashtable ht = new Hashtable();
        ht.put("0", "");
        Thread t1 = new Thread() {

            public void run() {
                while (ht.size() > 0) ;
                ht.put("final", "");
            }
        };
        t1.start();
        for (int i = 1; i < 10000; i++) {
            synchronized (ht) {
                ht.remove(String.valueOf(i - 1));
                ht.put(String.valueOf(i), "");
            }
            int size;
            if ((size = ht.size()) != 1) {
                String result = "Size is not 1: " + size + " " + ht;
                ht.clear();
                fail(result);
            }
        }
        ht.clear();
    }

    /**
     * @tests java.util.Hashtable#toString()
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "toString", args = {  })
    public void test_toString() {
        Hashtable h = new Hashtable();
        assertEquals("Incorrect toString for Empty table", "{}", h.toString());
        h.put("one", "1");
        h.put("two", h);
        h.put(h, "3");
        h.put(h, h);
        String result = h.toString();
        assertTrue("should contain self ref", result.indexOf("(this") > -1);
    }

    /**
     * @tests java.util.Hashtable#values()
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "values", args = {  })
    public void test_values() {
        Collection c = ht10.values();
        Enumeration e = elmVector.elements();
        while (e.hasMoreElements()) assertTrue("Returned incorrect values", c.contains(e.nextElement()));
        Hashtable myHashtable = new Hashtable();
        for (int i = 0; i < 100; i++) myHashtable.put(new Integer(i), new Integer(i));
        Collection values = myHashtable.values();
        new Support_UnmodifiableCollectionTest("Test Returned Collection From Hashtable.values()", values).runTest();
        values.remove(new Integer(0));
        assertTrue("Removing from the values collection should remove from the original map", !myHashtable.containsValue(new Integer(0)));
    }

    /**
     * Regression Test for JIRA 2181
     */
    @TestTargets({ @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "", method = "entrySet", args = {  }), @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "", method = "remove", args = { java.lang.Object.class }) })
    public void test_entrySet_remove() {
        Hashtable<String, String> hashtable = new Hashtable<String, String>();
        hashtable.put("my.nonexistent.prop", "AAA");
        hashtable.put("parse.error", "BBB");
        Iterator<Map.Entry<String, String>> iterator = hashtable.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = iterator.next();
            final Object value = entry.getValue();
            if (value.equals("AAA")) {
                iterator.remove();
            }
        }
        assertFalse(hashtable.containsKey("my.nonexistent.prop"));
    }

    class Mock_Hashtable extends Hashtable {

        boolean flag = false;

        public Mock_Hashtable(int i) {
            super(i);
        }

        @Override
        protected void rehash() {
            flag = true;
            super.rehash();
        }

        public boolean isRehashed() {
            return flag;
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "rehash", args = {  })
    public void test_rehash() {
        Mock_Hashtable mht = new Mock_Hashtable(5);
        assertFalse(mht.isRehashed());
        for (int i = 0; i < 10; i++) {
            mht.put(i, "New value");
        }
        assertTrue(mht.isRehashed());
    }

    protected Hashtable hashtableClone(Hashtable s) {
        return (Hashtable) s.clone();
    }

    /**
     * Sets up the fixture, for example, open a network connection. This method
     * is called before a test is executed.
     */
    protected void setUp() {
        ht10 = new Hashtable(10);
        ht100 = new Hashtable(100);
        htfull = new Hashtable(10);
        keyVector = new Vector(10);
        elmVector = new Vector(10);
        for (int i = 0; i < 10; i++) {
            ht10.put("Key " + i, "Val " + i);
            keyVector.addElement("Key " + i);
            elmVector.addElement("Val " + i);
        }
        for (int i = 0; i < 7; i++) htfull.put("FKey " + i, "FVal " + i);
    }

    /**
     * Tears down the fixture, for example, close a network connection. This
     * method is called after a test is executed.
     */
    protected void tearDown() {
        ht10 = null;
        ht100 = null;
        htfull = null;
        keyVector = null;
        elmVector = null;
    }
}
