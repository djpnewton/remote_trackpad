package com.djpsoft.remote;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;

public class JoyButton extends GamepadObject {
    float x, y;
    Drawable drawable, drawableDepressed;

    public JoyButton(Drawable drawable, Drawable drawableDepressed, float x, float y) {
        this.x = x;
        this.y = y;

        // drawing resources
        this.drawable = drawable;
        this.drawableDepressed = drawableDepressed;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float value) {
        x = value;
    }

    public void setY(float value) {
        y = value;
    }

    public int getWidth() {
        return drawable.getBounds().width();
    }

    public int getHeight() {
        return drawable.getBounds().height();
    }

    public boolean AnyPointInBounds(PointF[] pts) {
        return GeometryHelper.AnyPointInBounds(pts, x, y, getWidth(), getHeight());
    }

    public void Draw(Canvas canvas, boolean depressed) {
        canvas.translate(x, y);
        if (depressed)
            drawableDepressed.draw(canvas);
        else
            drawable.draw(canvas);
        canvas.translate(-x, -y);
    }
}
