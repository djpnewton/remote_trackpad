package com.djpsoft.remote;

import android.graphics.PointF;
import android.graphics.RectF;

public final class GeometryHelper {
    // Suppress default constructor for noninstantiability
    private GeometryHelper() {
        throw new AssertionError();
    }

    public static boolean AnyPointInBounds(PointF[] pts, RectF bounds) {
       return AnyPointInBounds(pts, bounds.left, bounds.top, bounds.width(), bounds.height());
    }

    public static boolean AnyPointInBounds(PointF[] pts, float x, float y, float width, float height) {
        for (int i = 0; i < pts.length; i++) {
            if (pts[i] != null) {
                if (pts[i].x >= x && pts[i].x <= x + width &&
                    pts[i].y >= y && pts[i].y <= y + height)
                    return true;
            }
        }
        return false;
    }
}
