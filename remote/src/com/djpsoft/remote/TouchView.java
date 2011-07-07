package com.djpsoft.remote;

import com.djpsoft.remote.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;

public class TouchView extends View {

    final int MAX_POINTS = 5;

    Context context;
    Drawable icon;
    PointF[] pts = new PointF[MAX_POINTS];

    boolean InvalidateOnTouch = false;
    boolean DrawTouchPoints = false;

    public TouchView(Context context) {
        super(context);

        this.context = context;

        // icon drawing resources
        icon = context.getResources().getDrawable(R.drawable.icon);
        icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // update pointer positions
        final int action = event.getAction() & MotionEvent.ACTION_MASK;
        int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT;
        int pointerId = event.getPointerId(pointerIndex);
        if (pointerId < MAX_POINTS) {
            switch (action) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                pts[pointerId] = null;
                break;
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                pts[pointerId] = new PointF();
            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i < event.getPointerCount(); i++) {
                    pointerId = event.getPointerId(i);
                    pts[pointerId].x = event.getX(i);
                    pts[pointerId].y = event.getY(i);
                }
                break;
            }
        }
        if (InvalidateOnTouch)
            invalidate();
        return true;
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (DrawTouchPoints)
            drawTouchPoints(canvas);
    }

    protected void drawTouchPoints(Canvas canvas) {
        float offsetx, offsety;
        canvas.save();
        for (int i = 0; i < MAX_POINTS; i++) {
            if (pts[i] != null) {
                offsetx = pts[i].x - icon.getBounds().width() / 2.0f;
                offsety = pts[i].y - icon.getBounds().height() / 2.0f;
                canvas.translate(offsetx, offsety);
                icon.draw(canvas);
                canvas.translate(-offsetx, -offsety);
            }
        }
        canvas.restore();
    }
}
