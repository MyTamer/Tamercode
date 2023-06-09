package org.loon.framework.javase.game.core.graphics.window;

import java.awt.Color;
import java.awt.Font;
import org.loon.framework.javase.game.core.graphics.LComponent;
import org.loon.framework.javase.game.core.graphics.device.LGraphics;
import org.loon.framework.javase.game.utils.GraphicsUtils;

/**
 * 
 * Copyright 2008 - 2009
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * @project loonframework
 * @author chenpeng
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */
public class LTag extends LComponent {

    private Font font;

    private boolean visible;

    private Color color;

    private float alpha;

    private String tag;

    public LTag(String tag, int x, int y, int w, int h) {
        super(x, y, w, h);
        this.font = GraphicsUtils.getFont();
        this.tag = tag;
        this.color = Color.BLACK;
        this.visible = true;
        this.setLocation(x, y);
        this.tag = tag;
        this.customRendering = true;
    }

    public void setFont(String fontName, int type, int size) {
        setFont(GraphicsUtils.getFont(fontName, type, size));
    }

    public void setFont(Font font) {
        this.font = font;
    }

    protected void createCustomUI(LGraphics g, int x, int y, int w, int h) {
        if (visible) {
            Font oldFont = g.getFont();
            Color oldColor = g.getColor();
            g.setFont(font);
            g.setColor(color);
            this.setWidth(g.getFontMetrics().stringWidth(tag));
            this.setHeight(font.getSize());
            g.setAntiAlias(true);
            if (alpha > 0 && alpha <= 1.0) {
                g.setAlpha(alpha);
                g.drawString(tag, x(), y());
                g.setAlpha(1.0F);
            } else {
                g.drawString(tag, x(), y());
            }
            g.setAntiAlias(false);
            g.setFont(oldFont);
            g.setColor(oldColor);
        }
    }

    public String getTag() {
        return tag;
    }

    public void setTag(int tag) {
        setTag(String.valueOf(tag));
    }

    public void setTag(String tag) {
        this.tag = tag;
        this.createUI();
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getUIName() {
        return "Tag";
    }
}
