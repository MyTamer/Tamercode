package relocated.morphia.org.apache.commons.collections;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 *  Hashtable-based {@link Map} implementation that allows
 *  mappings to be removed by the garbage collector.<P>
 *
 *  When you construct a <Code>ReferenceMap</Code>, you can 
 *  specify what kind of references are used to store the
 *  map's keys and values.  If non-hard references are 
 *  used, then the garbage collector can remove mappings
 *  if a key or value becomes unreachable, or if the 
 *  JVM's memory is running low.  For information on how
 *  the different reference types behave, see
 *  {@link Reference}.<P>
 *
 *  Different types of references can be specified for keys
 *  and values.  The keys can be configured to be weak but
 *  the values hard, in which case this class will behave
 *  like a <A HREF="http://java.sun.com/j2se/1.4/docs/api/java/util/WeakHashMap.html">
 *  <Code>WeakHashMap</Code></A>.  However, you
 *  can also specify hard keys and weak values, or any other
 *  combination.  The default constructor uses hard keys
 *  and soft values, providing a memory-sensitive cache.<P>
 *
 *  The algorithms used are basically the same as those
 *  in {@link java.util.HashMap}.  In particular, you 
 *  can specify a load factor and capacity to suit your
 *  needs.  All optional {@link Map} operations are 
 *  supported.<P>
 *
 *  However, this {@link Map} implementation does <I>not</I>
 *  allow null elements.  Attempting to add a null key or
 *  or a null value to the map will raise a 
 *  <Code>NullPointerException</Code>.<P>
 *
 *  As usual, this implementation is not synchronized.  You
 *  can use {@link java.util.Collections#synchronizedMap} to 
 *  provide synchronized access to a <Code>ReferenceMap</Code>.
 *
 *  @author Paul Jack 
 *  @version $Id: ReferenceMap.java,v 1.7.2.1 2004/05/22 12:14:01 scolebourne Exp $
 *  @since 2.1
 *  @see java.lang.ref.Reference
 */
public class ReferenceMap extends AbstractMap {

    /**
     *  For serialization.
     */
    private static final long serialVersionUID = -3370601314380922368L;

    /**
     *  Constant indicating that hard references should be used.
     */
    public static final int HARD = 0;

    /**
     *  Constant indiciating that soft references should be used.
     */
    public static final int SOFT = 1;

    /**
     *  Constant indicating that weak references should be used.
     */
    public static final int WEAK = 2;

    /**
     *  The reference type for keys.  Must be HARD, SOFT, WEAK.
     *  Note: I originally marked this field as final, but then this class
     *   didn't compile under JDK1.2.2.
     *  @serial
     */
    private int keyType;

    /**
     *  The reference type for values.  Must be HARD, SOFT, WEAK.
     *  Note: I originally marked this field as final, but then this class
     *   didn't compile under JDK1.2.2.
     *  @serial
     */
    private int valueType;

    /**
     *  The threshold variable is calculated by multiplying
     *  table.length and loadFactor.  
     *  Note: I originally marked this field as final, but then this class
     *   didn't compile under JDK1.2.2.
     *  @serial
     */
    private float loadFactor;

    /**
     *  ReferenceQueue used to eliminate stale mappings.
     *  @see #purge
     */
    private transient ReferenceQueue queue = new ReferenceQueue();

    /**
     *  The hash table.  Its length is always a power of two.  
     */
    private transient Entry[] table;

    /**
     *  Number of mappings in this map.
     */
    private transient int size;

    /**
     *  When size reaches threshold, the map is resized.  
     *  @see resize
     */
    private transient int threshold;

    /**
     *  Number of times this map has been modified.
     */
    private transient volatile int modCount;

    /**
     *  Cached key set.  May be null if key set is never accessed.
     */
    private transient Set keySet;

    /**
     *  Cached entry set.  May be null if entry set is never accessed.
     */
    private transient Set entrySet;

    /**
     *  Cached values.  May be null if values() is never accessed.
     */
    private transient Collection values;

    /**
     *  Constructs a new <Code>ReferenceMap</Code> that will
     *  use hard references to keys and soft references to values.
     */
    public ReferenceMap() {
        this(HARD, SOFT);
    }

    /**
     *  Constructs a new <Code>ReferenceMap</Code> that will
     *  use the specified types of references.
     *
     *  @param keyType  the type of reference to use for keys;
     *   must be {@link #HARD}, {@link #SOFT}, {@link #WEAK}
     *  @param valueType  the type of reference to use for values;
     *   must be {@link #HARD}, {@link #SOFT}, {@link #WEAK}
     */
    public ReferenceMap(int keyType, int valueType) {
        this(keyType, valueType, 16, 0.75f);
    }

    /**
     *  Constructs a new <Code>ReferenceMap</Code> with the
     *  specified reference types, load factor and initial
     *  capacity.
     *
     *  @param keyType  the type of reference to use for keys;
     *   must be {@link #HARD}, {@link #SOFT}, {@link #WEAK}
     *  @param valueType  the type of reference to use for values;
     *   must be {@link #HARD}, {@link #SOFT}, {@link #WEAK}
     *  @param capacity  the initial capacity for the map
     *  @param loadFactor  the load factor for the map
     */
    public ReferenceMap(int keyType, int valueType, int capacity, float loadFactor) {
        super();
        verify("keyType", keyType);
        verify("valueType", valueType);
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        if ((loadFactor <= 0.0f) || (loadFactor >= 1.0f)) {
            throw new IllegalArgumentException("Load factor must be greater than 0 and less than 1.");
        }
        this.keyType = keyType;
        this.valueType = valueType;
        int v = 1;
        while (v < capacity) v *= 2;
        this.table = new Entry[v];
        this.loadFactor = loadFactor;
        this.threshold = (int) (v * loadFactor);
    }

    private static void verify(String name, int type) {
        if ((type < HARD) || (type > WEAK)) {
            throw new IllegalArgumentException(name + " must be HARD, SOFT, WEAK.");
        }
    }

    /**
     *  Writes this object to the given output stream.
     *
     *  @param out  the output stream to write to
     *  @throws IOException  if the stream raises it
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(table.length);
        for (Iterator iter = entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry entry = (Map.Entry) iter.next();
            out.writeObject(entry.getKey());
            out.writeObject(entry.getValue());
        }
        out.writeObject(null);
    }

    /**
     *  Reads the contents of this object from the given input stream.
     *
     *  @param inp  the input stream to read from
     *  @throws IOException  if the stream raises it
     *  @throws ClassNotFoundException  if the stream raises it
     */
    private void readObject(ObjectInputStream inp) throws IOException, ClassNotFoundException {
        inp.defaultReadObject();
        table = new Entry[inp.readInt()];
        threshold = (int) (table.length * loadFactor);
        queue = new ReferenceQueue();
        Object key = inp.readObject();
        while (key != null) {
            Object value = inp.readObject();
            put(key, value);
            key = inp.readObject();
        }
    }

    /**
     *  Constructs a reference of the given type to the given 
     *  referent.  The reference is registered with the queue
     *  for later purging.
     *
     *  @param type  HARD, SOFT or WEAK
     *  @param referent  the object to refer to
     *  @param hash  the hash code of the <I>key</I> of the mapping;
     *    this number might be different from referent.hashCode() if
     *    the referent represents a value and not a key
     */
    private Object toReference(int type, Object referent, int hash) {
        switch(type) {
            case HARD:
                return referent;
            case SOFT:
                return new SoftRef(hash, referent, queue);
            case WEAK:
                return new WeakRef(hash, referent, queue);
            default:
                throw new Error();
        }
    }

    /**
     *  Returns the entry associated with the given key.
     *
     *  @param key  the key of the entry to look up
     *  @return  the entry associated with that key, or null
     *    if the key is not in this map
     */
    private Entry getEntry(Object key) {
        if (key == null) return null;
        int hash = key.hashCode();
        int index = indexFor(hash);
        for (Entry entry = table[index]; entry != null; entry = entry.next) {
            if ((entry.hash == hash) && key.equals(entry.getKey())) {
                return entry;
            }
        }
        return null;
    }

    /**
     *  Converts the given hash code into an index into the
     *  hash table.
     */
    private int indexFor(int hash) {
        hash += ~(hash << 15);
        hash ^= (hash >>> 10);
        hash += (hash << 3);
        hash ^= (hash >>> 6);
        hash += ~(hash << 11);
        hash ^= (hash >>> 16);
        return hash & (table.length - 1);
    }

    /**
     *  Resizes this hash table by doubling its capacity.
     *  This is an expensive operation, as entries must
     *  be copied from the old smaller table to the new 
     *  bigger table.
     */
    private void resize() {
        Entry[] old = table;
        table = new Entry[old.length * 2];
        for (int i = 0; i < old.length; i++) {
            Entry next = old[i];
            while (next != null) {
                Entry entry = next;
                next = next.next;
                int index = indexFor(entry.hash);
                entry.next = table[index];
                table[index] = entry;
            }
            old[i] = null;
        }
        threshold = (int) (table.length * loadFactor);
    }

    /**
     *  Purges stale mappings from this map.<P>
     *
     *  Ordinarily, stale mappings are only removed during
     *  a write operation; typically a write operation will    
     *  occur often enough that you'll never need to manually
     *  invoke this method.<P>
     *
     *  Note that this method is not synchronized!  Special
     *  care must be taken if, for instance, you want stale
     *  mappings to be removed on a periodic basis by some
     *  background thread.
     */
    private void purge() {
        Reference ref = queue.poll();
        while (ref != null) {
            purge(ref);
            ref = queue.poll();
        }
    }

    private void purge(Reference ref) {
        int hash = ref.hashCode();
        int index = indexFor(hash);
        Entry previous = null;
        Entry entry = table[index];
        while (entry != null) {
            if (entry.purge(ref)) {
                if (previous == null) table[index] = entry.next; else previous.next = entry.next;
                this.size--;
                return;
            }
            previous = entry;
            entry = entry.next;
        }
    }

    /**
     *  Returns the size of this map.
     *
     *  @return  the size of this map
     */
    public int size() {
        purge();
        return size;
    }

    /**
     *  Returns <Code>true</Code> if this map is empty.
     *
     *  @return <Code>true</Code> if this map is empty
     */
    public boolean isEmpty() {
        purge();
        return size == 0;
    }

    /**
     *  Returns <Code>true</Code> if this map contains the given key.
     *
     *  @return true if the given key is in this map
     */
    public boolean containsKey(Object key) {
        purge();
        Entry entry = getEntry(key);
        if (entry == null) return false;
        return entry.getValue() != null;
    }

    /**
     *  Returns the value associated with the given key, if any.
     *
     *  @return the value associated with the given key, or <Code>null</Code>
     *   if the key maps to no value
     */
    public Object get(Object key) {
        purge();
        Entry entry = getEntry(key);
        if (entry == null) return null;
        return entry.getValue();
    }

    /**
     *  Associates the given key with the given value.<P>
     *  Neither the key nor the value may be null.
     *
     *  @param key  the key of the mapping
     *  @param value  the value of the mapping
     *  @return  the last value associated with that key, or 
     *   null if no value was associated with the key
     *  @throws NullPointerException if either the key or value
     *   is null
     */
    public Object put(Object key, Object value) {
        if (key == null) throw new NullPointerException("null keys not allowed");
        if (value == null) throw new NullPointerException("null values not allowed");
        purge();
        if (size + 1 > threshold) resize();
        int hash = key.hashCode();
        int index = indexFor(hash);
        Entry entry = table[index];
        while (entry != null) {
            if ((hash == entry.hash) && key.equals(entry.getKey())) {
                Object result = entry.getValue();
                entry.setValue(value);
                return result;
            }
            entry = entry.next;
        }
        this.size++;
        modCount++;
        key = toReference(keyType, key, hash);
        value = toReference(valueType, value, hash);
        table[index] = new Entry(key, hash, value, table[index]);
        return null;
    }

    /**
     *  Removes the key and its associated value from this map.
     *
     *  @param key  the key to remove
     *  @return the value associated with that key, or null if
     *   the key was not in the map
     */
    public Object remove(Object key) {
        if (key == null) return null;
        purge();
        int hash = key.hashCode();
        int index = indexFor(hash);
        Entry previous = null;
        Entry entry = table[index];
        while (entry != null) {
            if ((hash == entry.hash) && key.equals(entry.getKey())) {
                if (previous == null) table[index] = entry.next; else previous.next = entry.next;
                this.size--;
                modCount++;
                return entry.getValue();
            }
            previous = entry;
            entry = entry.next;
        }
        return null;
    }

    /**
     *  Clears this map.
     */
    public void clear() {
        Arrays.fill(table, null);
        size = 0;
        while (queue.poll() != null) ;
    }

    /**
     *  Returns a set view of this map's entries.
     *
     *  @return a set view of this map's entries
     */
    public Set entrySet() {
        if (entrySet != null) return entrySet;
        entrySet = new AbstractSet() {

            public int size() {
                return ReferenceMap.this.size();
            }

            public void clear() {
                ReferenceMap.this.clear();
            }

            public boolean contains(Object o) {
                if (o == null) return false;
                if (!(o instanceof Map.Entry)) return false;
                Map.Entry e = (Map.Entry) o;
                Entry e2 = getEntry(e.getKey());
                return (e2 != null) && e.equals(e2);
            }

            public boolean remove(Object o) {
                boolean r = contains(o);
                if (r) {
                    Map.Entry e = (Map.Entry) o;
                    ReferenceMap.this.remove(e.getKey());
                }
                return r;
            }

            public Iterator iterator() {
                return new EntryIterator();
            }

            public Object[] toArray() {
                return toArray(new Object[0]);
            }

            public Object[] toArray(Object[] arr) {
                ArrayList list = new ArrayList();
                Iterator iterator = iterator();
                while (iterator.hasNext()) {
                    Entry e = (Entry) iterator.next();
                    list.add(new DefaultMapEntry(e.getKey(), e.getValue()));
                }
                return list.toArray(arr);
            }
        };
        return entrySet;
    }

    /**
     *  Returns a set view of this map's keys.
     *
     *  @return a set view of this map's keys
     */
    public Set keySet() {
        if (keySet != null) return keySet;
        keySet = new AbstractSet() {

            public int size() {
                return size;
            }

            public Iterator iterator() {
                return new KeyIterator();
            }

            public boolean contains(Object o) {
                return containsKey(o);
            }

            public boolean remove(Object o) {
                Object r = ReferenceMap.this.remove(o);
                return r != null;
            }

            public void clear() {
                ReferenceMap.this.clear();
            }
        };
        return keySet;
    }

    /**
     *  Returns a collection view of this map's values.
     *
     *  @return a collection view of this map's values.
     */
    public Collection values() {
        if (values != null) return values;
        values = new AbstractCollection() {

            public int size() {
                return size;
            }

            public void clear() {
                ReferenceMap.this.clear();
            }

            public Iterator iterator() {
                return new ValueIterator();
            }
        };
        return values;
    }

    private class Entry implements Map.Entry {

        Object key;

        Object value;

        int hash;

        Entry next;

        public Entry(Object key, int hash, Object value, Entry next) {
            this.key = key;
            this.hash = hash;
            this.value = value;
            this.next = next;
        }

        public Object getKey() {
            return (keyType > HARD) ? ((Reference) key).get() : key;
        }

        public Object getValue() {
            return (valueType > HARD) ? ((Reference) value).get() : value;
        }

        public Object setValue(Object object) {
            Object old = getValue();
            if (valueType > HARD) ((Reference) value).clear();
            value = toReference(valueType, object, hash);
            return old;
        }

        public boolean equals(Object o) {
            if (o == null) return false;
            if (o == this) return true;
            if (!(o instanceof Map.Entry)) return false;
            Map.Entry entry = (Map.Entry) o;
            Object key = entry.getKey();
            Object value = entry.getValue();
            if ((key == null) || (value == null)) return false;
            return key.equals(getKey()) && value.equals(getValue());
        }

        public int hashCode() {
            Object v = getValue();
            return hash ^ ((v == null) ? 0 : v.hashCode());
        }

        public String toString() {
            return getKey() + "=" + getValue();
        }

        boolean purge(Reference ref) {
            boolean r = (keyType > HARD) && (key == ref);
            r = r || ((valueType > HARD) && (value == ref));
            if (r) {
                if (keyType > HARD) ((Reference) key).clear();
                if (valueType > HARD) ((Reference) value).clear();
            }
            return r;
        }
    }

    private class EntryIterator implements Iterator {

        int index;

        Entry entry;

        Entry previous;

        Object nextKey, nextValue;

        Object currentKey, currentValue;

        int expectedModCount;

        public EntryIterator() {
            index = (size() != 0 ? table.length : 0);
            expectedModCount = modCount;
        }

        public boolean hasNext() {
            checkMod();
            while (nextNull()) {
                Entry e = entry;
                int i = index;
                while ((e == null) && (i > 0)) {
                    i--;
                    e = table[i];
                }
                entry = e;
                index = i;
                if (e == null) {
                    currentKey = null;
                    currentValue = null;
                    return false;
                }
                nextKey = e.getKey();
                nextValue = e.getValue();
                if (nextNull()) entry = entry.next;
            }
            return true;
        }

        private void checkMod() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }

        private boolean nextNull() {
            return (nextKey == null) || (nextValue == null);
        }

        protected Entry nextEntry() {
            checkMod();
            if (nextNull() && !hasNext()) throw new NoSuchElementException();
            previous = entry;
            entry = entry.next;
            currentKey = nextKey;
            currentValue = nextValue;
            nextKey = null;
            nextValue = null;
            return previous;
        }

        public Object next() {
            return nextEntry();
        }

        public void remove() {
            checkMod();
            if (previous == null) throw new IllegalStateException();
            ReferenceMap.this.remove(currentKey);
            previous = null;
            currentKey = null;
            currentValue = null;
            expectedModCount = modCount;
        }
    }

    private class ValueIterator extends EntryIterator {

        public Object next() {
            return nextEntry().getValue();
        }
    }

    private class KeyIterator extends EntryIterator {

        public Object next() {
            return nextEntry().getKey();
        }
    }

    private static class SoftRef extends SoftReference {

        private int hash;

        public SoftRef(int hash, Object r, ReferenceQueue q) {
            super(r, q);
            this.hash = hash;
        }

        public int hashCode() {
            return hash;
        }
    }

    private static class WeakRef extends WeakReference {

        private int hash;

        public WeakRef(int hash, Object r, ReferenceQueue q) {
            super(r, q);
            this.hash = hash;
        }

        public int hashCode() {
            return hash;
        }
    }
}
