package com.google.gwt.typedarrays.shared;

/**
 * A view representing an {@link ArrayBuffer} as 16-bit unsigned integers.  Storing
 * out-of-range values are mapped to valid values by taking the bottom 16 bits of
 * the value.
 * 
 * {@link "http://www.khronos.org/registry/typedarray/specs/latest/#7"}
 */
public interface Uint16Array extends ArrayBufferView {

    final int BYTES_PER_ELEMENT = 2;

    /**
   * The length in elements of this view.
   * 
   * @return non-negative length
   */
    int length();

    /**
   * Retrieve one element of this view.
   * 
   * @param index
   * @return the requested element
   */
    int get(int index);

    /**
   * Set one element in this view.
   * 
   * @param index
   * @param value
   */
    void set(int index, int value);

    /**
   * Set multiple elements in this view from another view, storing starting at 0.
   * 
   * @param array
   */
    void set(Uint16Array array);

    /**
   * Set multiple elements in this view from another view, storing starting at the
   * requested offset.
   * 
   * @param array
   */
    void set(Uint16Array array, int offset);

    /**
   * Set multiple elements in this view from an array, storing starting at 0.
   * 
   * @param array
   */
    void set(int[] array);

    /**
   * Set multiple elements in this view from an array, storing starting at the
   * requested offset.
   * 
   * @param array
   */
    void set(int[] array, int offset);

    /**
   * Create a new view from the same array, from {@code offset} to the end of
   * this view. These offset is clamped to legal indices into this view, so it
   * is not an error to specify an invalid index.
   * 
   * @param begin offset into this view if non-negative; if negative, an index
   *        from the end of this view
   * @return a new {@link Uint16Array} instance
   */
    Uint16Array subarray(int begin);

    /**
   * Create a new view from the same array, from {@code offset} to (but not
   * including) {@code end} in this view.  These indices are clamped to legal
   * indices into this view, so it is not an error to specify invalid indices.
   * 
   * @param begin offset into this view if non-negative; if negative, an index from
   *     the end of this view
   * @param end offset into this view if non-negative; if negative, an index from
   *     the end of this view
   * @return a new {@link Uint16Array} instance
   */
    Uint16Array subarray(int begin, int end);
}
