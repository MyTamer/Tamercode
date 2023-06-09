package com.google.gwt.user.client.ui;

/**
 * This tag interface specifies that the implementing widget will call
 * {@link RequiresResize#onResize()} on its children whenever their size may
 * have changed.
 * 
 * <p>
 * With limited exceptions (such as {@link RootLayoutPanel}), widgets that
 * implement this interface will also implement {@link RequiresResize}. A typical
 * widget will implement {@link RequiresResize#onResize()} like this:
 * 
 * <code>
 * public void onResize() {
 *   for (Widget child : getChildren()) {
 *     if (child instanceof RequiresResize) {
 *       ((RequiresResize) child).onResize();
 *     }
 *   }
 * }
 * </code>
 * </p>
 */
public interface ProvidesResize {
}
