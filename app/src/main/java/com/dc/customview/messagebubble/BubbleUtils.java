package com.dc.customview.messagebubble;

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class BubbleUtils {

    /**
     * 获取状态栏高度
     *
     * @param context Context
     * @return int
     */
    public static int getStatusBarHeight(Context context) {
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return dip2px(25, context);
    }

    /**
     * dip 转换成 px
     *
     * @param dip     float
     * @param context Context
     * @return int
     */
    public static int dip2px(float dip, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, displayMetrics);
    }

    /**
     * 根据百分比获取两点之间的某个点坐标
     *
     * @param p1 PointF
     * @param p2 PointF
     * @param percent float
     *
     * @return PointF
     */
    public static PointF getPointByPercent(PointF p1, PointF p2, float percent) {
        return new PointF(evaluateValue(percent, p1.x, p2.x), evaluateValue(
                percent, p1.y, p2.y));
    }

    /**
     * 根据分度值，计算从start到end中，fraction位置的值。fraction范围为0 -> 1
     *
     * @param fraction float
     * @param start Number
     * @param end Number
     * @return float
     */
    public static float evaluateValue(float fraction, Number start, Number end) {
        // start = 10   end = 2
        //fraction = 0.5
        // result = 10 + (-8) * fraction = 6
        return start.floatValue() + (end.floatValue() - start.floatValue())
                * fraction;
    }
}
