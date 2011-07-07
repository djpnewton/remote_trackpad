package com.djpsoft.remote;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import com.djpsoft.remote.R;
import com.djpsoft.remote.GamepadView.JoypadButton;

public class Dpad extends GamepadObject {

    float x, y, borderX, borderY;
    RectF boundsCornerBorder, boundsXBorder, boundsYBorder;
    float width6th, height6th;
    Drawable drawable, drawableLeft, drawableTop, drawableRight, drawableBottom;

    public Dpad(Context context, float x, float y) {
        this.x = x;
        this.y = y;
        this.borderX = 0;
        this.borderY = 0;

        // drawing resources
        drawable = context.getResources().getDrawable(R.drawable.dpad);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawableLeft = context.getResources().getDrawable(R.drawable.dpad_left);
        drawableLeft.setBounds(0, 0, drawableLeft.getIntrinsicWidth(), drawableLeft.getIntrinsicHeight());
        drawableTop = context.getResources().getDrawable(R.drawable.dpad_top);
        drawableTop.setBounds(0, 0, drawableTop.getIntrinsicWidth(), drawableTop.getIntrinsicHeight());
        drawableRight = context.getResources().getDrawable(R.drawable.dpad_right);
        drawableRight.setBounds(0, 0, drawableRight.getIntrinsicWidth(), drawableRight.getIntrinsicHeight());
        drawableBottom = context.getResources().getDrawable(R.drawable.dpad_bottom);
        drawableBottom.setBounds(0, 0, drawableBottom.getIntrinsicWidth(), drawableBottom.getIntrinsicHeight());

        createBorderBounds();
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

    public void setBorderX(float value) {
        borderX = value;
        createBorderBounds();
    }

    public void setBorderY(float value) {
        borderY = value;
        createBorderBounds();
    }

    public void createBorderBounds() {
        width6th = getWidth() / 6.0f;
        height6th = getHeight() / 6.0f;
        boundsCornerBorder = CreateDPadArrowBounds(borderX + width6th, borderY + height6th, 0, 1, 0, 1);
        boundsXBorder = CreateDPadArrowBounds(borderX, getHeight(), 0, 1, 0, 1);
        boundsYBorder = CreateDPadArrowBounds(getWidth(), borderY, 0, 1, 0, 1);
    }

    public int getWidth() {
        return drawable.getBounds().width();
    }

    public int getHeight() {
        return drawable.getBounds().height();
    }

    private RectF CreateDPadArrowBounds(float width, float height, float xstart, float xstop, float ystart, float ystop) {
        RectF res = new RectF();
        res.left = xstart * width;
        res.right = xstop * width;
        res.top = ystart * height;
        res.bottom = ystop * height;
        return res;
    }

    public boolean AnyPointInDpadLeft(PointF[] pts) {
        RectF bounds = CreateDPadArrowBounds(getWidth(), getHeight(), 0, 0.33f, 0, 1);
        return GeometryHelper.AnyPointInBounds(pts, x + bounds.left, y + bounds.top, bounds.width(), bounds.height()) ||
        GeometryHelper.AnyPointInBounds(pts, x - borderX, y, boundsXBorder.width(), boundsXBorder.height()) ||
        GeometryHelper.AnyPointInBounds(pts, x - borderX, y - borderY, boundsCornerBorder.width(), boundsCornerBorder.height()) ||
        GeometryHelper.AnyPointInBounds(pts, x - borderX, y + getHeight() - height6th, boundsCornerBorder.width(), boundsCornerBorder.height());
    }

    public boolean AnyPointInDpadTop(PointF[] pts) {
        RectF bounds = CreateDPadArrowBounds(getWidth(), getHeight(), 0, 1, 0, 0.33f);
        return GeometryHelper.AnyPointInBounds(pts, x + bounds.left, y + bounds.top, bounds.width(), bounds.height()) ||
        GeometryHelper.AnyPointInBounds(pts, x, y - borderY, boundsYBorder.width(), boundsYBorder.height()) ||
        GeometryHelper.AnyPointInBounds(pts, x - borderX, y - borderY, boundsCornerBorder.width(), boundsCornerBorder.height()) ||
        GeometryHelper.AnyPointInBounds(pts, x + getWidth() - borderX, y - borderY, boundsCornerBorder.width(), boundsCornerBorder.height());
    }

    public boolean AnyPointInDpadRight(PointF[] pts) {
        RectF bounds = CreateDPadArrowBounds(getWidth(), getHeight(), 0.66f, 1, 0, 1);
        return GeometryHelper.AnyPointInBounds(pts, x + bounds.left, y + bounds.top, bounds.width(), bounds.height()) ||
        GeometryHelper.AnyPointInBounds(pts, x + getWidth(), y, boundsXBorder.width(), boundsXBorder.height()) ||
        GeometryHelper.AnyPointInBounds(pts, x + getWidth() - width6th, y - borderY, boundsCornerBorder.width(), boundsCornerBorder.height()) ||
        GeometryHelper.AnyPointInBounds(pts, x + getWidth() - width6th, y + getHeight() - height6th, boundsCornerBorder.width(), boundsCornerBorder.height());
    }

    public boolean AnyPointInDpadBottom(PointF[] pts) {
        RectF bounds = CreateDPadArrowBounds(getWidth(), getHeight(), 0, 1, 0.66f, 1);
        return GeometryHelper.AnyPointInBounds(pts, x + bounds.left, y + bounds.top, bounds.width(), bounds.height()) ||
        GeometryHelper.AnyPointInBounds(pts, x, y + getHeight(), boundsYBorder.width(), boundsYBorder.height()) ||
        GeometryHelper.AnyPointInBounds(pts, x - borderX, y + getHeight() - height6th, boundsCornerBorder.width(), boundsCornerBorder.height()) ||
        GeometryHelper.AnyPointInBounds(pts, x + getWidth() - borderX, y + getHeight() - height6th, boundsCornerBorder.width(), boundsCornerBorder.height());
    }

    public void Draw(Canvas canvas, JoypadButton button) {
        canvas.translate(x, y);
        switch (button) {
        case DPAD_LEFT:
            drawableLeft.draw(canvas);
            break;
        case DPAD_TOP:
            drawableTop.draw(canvas);
            break;
        case DPAD_RIGHT:
            drawableRight.draw(canvas);
            break;
        case DPAD_BOTTOM:
            drawableBottom.draw(canvas);
            break;
        }
        canvas.translate(-x, -y);
    }

    public void Draw(Canvas canvas) {
        canvas.translate(x, y);
        drawable.draw(canvas);
        canvas.translate(-x, -y);
    }
}
