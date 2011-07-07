package com.djpsoft.remote;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

public class TrackpadView extends TouchView {

    private static final String TAG = "TrackpadView";

    public static final String TAP_TO_CLICK = "TAP_TO_CLICK";
    public static final String DOUBLE_TAP_AND_DRAG = "DOUBLE_TAP_AND_DRAG";
    public static final String SHOW_BUTTONS = "SHOW_BUTTONS";
    public static final String TWO_FINGER_TAP_RIGHT_CLICK = "TWO_FINGER_TAP_RIGHT_CLICK";
    public static final String TWO_FINGER_DRAG_SCROLL = "TWO_FINGER_DRAG_SCROLL";

    public static final int TAP_THRESHOLD = 5;
    public static final int TAP_AND_DRAG_TIMEOUT = 250;
    public static final int TAP_AND_DRAG_AGAIN_TIMEOUT = 400;

    private static final int SIG_TOUCH_DOWN0 = 0;
    private static final int SIG_TOUCH_MOVE0 = 1;
    private static final int SIG_TOUCH_UP0 = 2;
    private static final int SIG_TOUCH_DOWN1 = 3;
    private static final int SIG_TOUCH_MOVE1 = 4;
    private static final int SIG_TOUCH_UP1 = 5;
    private static final int SIG_TAP_TIMEOUT = 6;

    private boolean tapToClick;
    private boolean doubleTapAndDrag;
    private boolean twoFingerTapRightClick;
    private boolean twoFingerDragScroll;
    private boolean showButtons;

    private boolean preventExcessIOMsgs = false;

    interface StateFunction {
        void function(int sig);
    }
    private StateFunction currentState;
    private boolean lastPt0Valid = false;
    private boolean lastPt1Valid = false;
    private float lastX0, lastY0;
    private float lastX1, lastY1;
    private float initialX0, initialY0;
    private float initialX1, initialY1;
    private Timer tapTimer = null;

    private boolean buttonLeftDepressed, buttonRightDepressed;
    private RectF trackPad, buttonLeft, buttonRight;

    public TrackpadView(Context context, boolean tapToClick, boolean doubleTapAndDrag, boolean twoFingerTapRightClick, boolean twoFingerDragScroll, boolean showButtons) {
        super(context);
        this.tapToClick = tapToClick;
        this.doubleTapAndDrag =  doubleTapAndDrag;
        this.twoFingerTapRightClick = twoFingerTapRightClick;
        this.twoFingerDragScroll = twoFingerDragScroll;
        this.showButtons = showButtons;
        currentState = idle;
        InitLayout(context);
    }

    private void InitLayout(Context context) {
        int width = getWidth();
        int height = getHeight();
        if (showButtons) {
            trackPad = new RectF(0, 0, width, height * 4 / 5.0f);
            buttonLeft = new RectF(0, height * 4 / 5.0f, width / 2.0f - 1, height - 1);
            buttonRight = new RectF(width / 2.0f, height * 4 / 5.0f, width - 1, height - 1);
        }
        else {
            trackPad = new RectF(0, 0, width, height);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        InitLayout(this.context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        if (pts[0] != null) {
            if (!lastPt0Valid)
                currentState.function(SIG_TOUCH_DOWN0);
            else
                currentState.function(SIG_TOUCH_MOVE0);
            lastX0 = pts[0].x;
            lastY0 = pts[0].y;
        }
        else {
            if (lastPt0Valid)
                currentState.function(SIG_TOUCH_UP0);
        }
        if (pts[1] != null) {
            if (!lastPt1Valid)
                currentState.function(SIG_TOUCH_DOWN1);
            else
                currentState.function(SIG_TOUCH_MOVE1);
            lastX1 = pts[1].x;
            lastY1 = pts[1].y;
        }
        else {
            if (lastPt1Valid)
                currentState.function(SIG_TOUCH_UP1);
        }
        lastPt0Valid = pts[0] != null;
        lastPt1Valid = pts[1] != null;

        invalidate();

        // we have handled the touch event
        return true;
    }

    private StateFunction idle = new StateFunction() {
        @Override
        public void function(int sig) {
            if (sig == SIG_TOUCH_DOWN0) {
                if (showButtons && GeometryHelper.AnyPointInBounds(pts, buttonLeft)) {
                    buttonLeftDepressed = true;
                    performEvent(false, 1, 0, 0);
                    currentState = button_click;
                }
                else if (showButtons && GeometryHelper.AnyPointInBounds(pts, buttonRight)) {
                    buttonRightDepressed = true;
                    performEvent(false, 2, 0, 0);
                    currentState = button_click;
                }
                else {
                    initialX0 = pts[0].x;
                    initialY0 = pts[0].y;
                    currentState = touch_down;
                }
            }
        }
    };

    private StateFunction touch_down = new StateFunction() {
        @Override
        public void function(int sig) {
            if (sig == SIG_TOUCH_MOVE0) {
                float dx = pts[0].x - initialX0;
                float dy = pts[0].y - initialY0;
                if (Math.abs(dx) > TAP_THRESHOLD &&
                    Math.abs(dy) > TAP_THRESHOLD) {
                    performEvent(false, 0, Math.round(dx), Math.round(dy));
                    currentState = touch_dragging;
                }
            }
            else if (sig == SIG_TOUCH_UP0) {
                createTapTimer(TAP_AND_DRAG_TIMEOUT);
                currentState = tap_release;
            }
            else if (sig == SIG_TOUCH_DOWN1) {
                initialX1 = pts[1].x;
                initialY1 = pts[1].y;
                currentState = two_touch;
            }
        }
    };

    private StateFunction touch_dragging = new StateFunction() {
        @Override
        public void function(int sig) {
            if (sig == SIG_TOUCH_MOVE0)
                performEvent(false, 0,
                        Math.round(pts[0].x - lastX0),
                        Math.round(pts[0].y - lastY0));
            else if (sig == SIG_TOUCH_UP0) {
                currentState = idle;
            }
        }
    };

    private StateFunction tap_release = new StateFunction() {
        @Override
        public void function(int sig) {
            if (sig == SIG_TOUCH_DOWN0) {
                purgeTapTimer();
                if (doubleTapAndDrag) {
                    initialX0 = pts[0].x;
                    initialY0 = pts[0].y;
                    currentState = double_tap;
                }
            }
            else if (sig == SIG_TAP_TIMEOUT) {
                if (tapToClick) {
                    performEvent(false, 1, 0, 0);
                    performEvent(false, 0, 0, 0);
                }
                currentState = idle;
            }
        }
    };

    private StateFunction double_tap = new StateFunction() {
        @Override
        public void function(int sig) {
            if (sig == SIG_TOUCH_MOVE0) {
                float dx = pts[0].x - initialX0;
                float dy = pts[0].y - initialY0;
                if (Math.abs(dx) > TAP_THRESHOLD &&
                    Math.abs(dy) > TAP_THRESHOLD) {
                    performEvent(false, 1, 0, 0);
                    currentState = double_tap_drag;
                }
            }
            else if (sig == SIG_TOUCH_UP0) {
                performEvent(false, 1, 0, 0);
                performEvent(false, 0, 0, 0);
                performEvent(false, 1, 0, 0);
                performEvent(false, 0, 0, 0);
                currentState = idle;
            }
        }
    };

    private StateFunction double_tap_drag = new StateFunction() {
        @Override
        public void function(int sig) {
            if (sig == SIG_TOUCH_MOVE0) {
                performEvent(false, 1,
                        Math.round(pts[0].x - lastX0),
                        Math.round(pts[0].y - lastY0));
            }
            else if (sig == SIG_TOUCH_UP0) {
                createTapTimer(TAP_AND_DRAG_AGAIN_TIMEOUT);
                currentState = double_tap_wait;
            }
        }
    };

    private StateFunction double_tap_wait = new StateFunction() {
        @Override
        public void function(int sig) {
            if (sig == SIG_TOUCH_DOWN0) {
                purgeTapTimer();
                currentState = double_tap;
            }
            else if (sig == SIG_TAP_TIMEOUT) {
                performEvent(false, 0, 0, 0);
                currentState = idle;
            }
        }
    };

    private StateFunction two_touch = new StateFunction() {
        @Override
        public void function(int sig) {
            switch (sig) {
            case SIG_TOUCH_MOVE0:
                float dx = pts[0].x - initialX0;
                float dy = pts[0].y - initialY0;
                if (twoFingerDragScroll &&
                        Math.abs(dx) > TAP_THRESHOLD &&
                        Math.abs(dy) > TAP_THRESHOLD) {
                    performEvent(true, 0, Math.round(dx), Math.round(dy));
                    currentState = scrolling;
                }
                break;
            case SIG_TOUCH_UP0:
            case SIG_TOUCH_UP1:
                if (twoFingerTapRightClick) {
                    performEvent(false, 2, 0, 0);
                    performEvent(false, 0, 0, 0);
                }
                currentState = two_touch_dead;
                break;
            }
        }
    };

    private StateFunction scrolling = new StateFunction() {
        @Override
        public void function(int sig) {
            switch (sig) {
            case SIG_TOUCH_MOVE0:
                performEvent(true, 0,
                        Math.round(pts[0].x - lastX0),
                        Math.round(pts[0].y - lastY0));
                break;
            case SIG_TOUCH_UP0:
            case SIG_TOUCH_UP1:
                currentState = two_touch_dead;
                break;
            }
        }
    };

    private StateFunction two_touch_dead = new StateFunction() {
        @Override
        public void function(int sig) {
            switch (sig) {
            case SIG_TOUCH_UP0:
            case SIG_TOUCH_UP1:
                currentState = idle;
                break;
            }
        }
    };

    private StateFunction button_click = new StateFunction() {
        @Override
        public void function(int sig) {
            if (sig == SIG_TOUCH_UP0) {
                buttonLeftDepressed = false;
                buttonRightDepressed = false;
                performEvent(false, 0, 0, 0);
                currentState = idle;
            }
        }
    };

    void createTapTimer(long timeout) {
        tapTimer = new Timer();
        tapTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                execTapTimer();
            }
        }, timeout);
    }

    void execTapTimer() {
        currentState.function(SIG_TAP_TIMEOUT);
    }

    void purgeTapTimer() {
        if (tapTimer != null) {
            tapTimer.cancel();
            tapTimer.purge();
            tapTimer = null;
        }
    }

    void performEvent(boolean scroll, int btnState, int dx, int dy) {
        if (RemoteSetup.oscClient != null) {
            try {
                if (scroll)
                    RemoteSetup.oscClient.SendScrollEvent(dx, dy);
                else
                    RemoteSetup.oscClient.SendMouseEvent(btnState, dx, dy);
            }
            catch (IOException e) {
                Log.e(TAG, "performEvent: IOException");
                if (preventExcessIOMsgs == false) {
                    preventExcessIOMsgs = true;
                    Toast toast = Toast.makeText(context, R.string.send_msg_io_exception, Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        }
        else {
            Log.e(TAG, "performEvent: RemoteSetup.oscClient == null");
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint p = new Paint();

        // draw mouse buttons
        if (showButtons) {
            p.setColor(0xFFFFFFFF);
            if (buttonLeftDepressed)
                p.setStyle(Style.FILL);
            else
                p.setStyle(Style.STROKE);
            canvas.drawRect(buttonLeft, p);
            if (buttonRightDepressed)
                p.setStyle(Style.FILL);
            else
                p.setStyle(Style.STROKE);
            canvas.drawRect(buttonRight, p);
        }
        // draw trackpad text
        p.setColor(0xAAAAAAAA);
        p.setTypeface(Typeface.DEFAULT_BOLD);
        drawText(canvas, p, "Trackpad", trackPad);
        // draw button text
        if (showButtons) {
            drawText(canvas, p, "Left Button", buttonLeft);
            drawText(canvas, p, "Right Button", buttonRight);
        }

        // draw touch locations
        drawTouchPoints(canvas);
    }

    private void drawText(Canvas canvas, Paint p, String text, RectF box) {
        Rect bounds = new Rect();
        p.getTextBounds(text, 0, text.length(), bounds);
        canvas.drawText(text,
                box.left + box.width() / 2.0f - bounds.width() / 2.0f,
                box.top + box.height() / 2.0f - bounds.height() / 2.0f,
                p);
    }
}
