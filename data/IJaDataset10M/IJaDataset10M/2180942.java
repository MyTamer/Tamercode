package org.openrdf.sail.memory.model;

import java.util.Arrays;

/**
 * A dedicated data structure for storing MemStatement objects, offering
 * operations optimized for their use in the memory Sail.
 */
public class MemStatementList {

    private MemStatement[] statements;

    private int size;

    /**
	 * Creates a new MemStatementList.
	 */
    public MemStatementList() {
        this(4);
    }

    public MemStatementList(int capacity) {
        statements = new MemStatement[capacity];
        size = 0;
    }

    public MemStatementList(MemStatementList other) {
        this(other.size);
        addAll(other);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public MemStatement get(int index) {
        if (index < 0 || index >= size) {
            if (index < 0) {
                throw new IndexOutOfBoundsException("index < 0");
            } else {
                throw new IndexOutOfBoundsException("index >= size");
            }
        }
        return statements[index];
    }

    public void add(MemStatement st) {
        if (size == statements.length) {
            growArray((size == 0) ? 1 : 2 * size);
        }
        statements[size] = st;
        ++size;
    }

    public void addAll(MemStatementList other) {
        if (size + other.size >= statements.length) {
            growArray(size + other.size);
        }
        System.arraycopy(other.statements, 0, statements, size, other.size);
        size += other.size;
    }

    public void remove(int index) {
        if (index < 0 || index >= size) {
            if (index < 0) {
                throw new IndexOutOfBoundsException("index < 0");
            } else {
                throw new IndexOutOfBoundsException("index >= size");
            }
        }
        if (index == size - 1) {
            statements[index] = null;
            --size;
        } else {
            --size;
            statements[index] = statements[size];
            statements[size] = null;
        }
    }

    public void remove(MemStatement st) {
        for (int i = 0; i < size; ++i) {
            if (statements[i] == st) {
                remove(i);
                return;
            }
        }
    }

    public void clear() {
        Arrays.fill(statements, 0, size, null);
        size = 0;
    }

    private void growArray(int newSize) {
        MemStatement[] newArray = new MemStatement[newSize];
        System.arraycopy(statements, 0, newArray, 0, size);
        statements = newArray;
    }
}
