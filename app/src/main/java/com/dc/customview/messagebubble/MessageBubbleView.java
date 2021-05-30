package com.dc.customview.messagebubble;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.Nullable;

/**
 * @author Lemon
 */
public class MessageBubbleView extends View {

    private Paint mPaint;
    private PointF mFixationPoint;
    private PointF mDragPoint;

    /**
     * 拖拽圆的半径
     */
    private int mDragRadius = 10;

    /**
     * 固定圆的最大半径（初始半径）
     */
    private int mFixationRadiusMax = 7;
    private int mFixationRadiusMin = 3;
    private int mFixationRadius;
    private Bitmap mDragBitmap;

    private MessageBubbleListener mListener;

    public MessageBubbleView(Context context) {
        this(context, null);
    }

    public MessageBubbleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MessageBubbleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDragRadius = dip2px(mDragRadius);
        mFixationRadiusMax = dip2px(mFixationRadiusMax);
        mFixationRadiusMin = dip2px(mFixationRadiusMin);
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }

    private int dip2px(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mDragPoint == null || mFixationPoint == null) {
            return;
        }
        canvas.drawCircle(mDragPoint.x, mDragPoint.y, mDragRadius, mPaint);

        Path bezierPath = getBezierPath();
        if (bezierPath != null) {
            canvas.drawCircle(mFixationPoint.x, mFixationPoint.y, mFixationRadius, mPaint);
            // 画贝塞尔曲线
            canvas.drawPath(bezierPath, mPaint);
        }

        if (mDragBitmap != null) {
            canvas.drawBitmap(mDragBitmap, mDragPoint.x - mDragBitmap.getWidth() / 2f, mDragPoint.y - mDragBitmap.getHeight() / 2f, null);
        }
    }

    private Path getBezierPath() {
        double distance = getDistance(mDragPoint, mFixationPoint);
        mFixationRadius = (int) (mFixationRadiusMax - distance / 14);
        if (mFixationRadius < mFixationRadiusMin) {
            // 超过一定距离 贝塞尔和固定圆都不要画了
            return null;
        }
        Path bezierPath = new Path();
        // 求角 a
        // 求斜率
        float dy = (mDragPoint.y-mFixationPoint.y);
        float dx = (mDragPoint.x-mFixationPoint.x);
        float tanA = dy/dx;
        // 求角a
        double arcTanA = Math.atan(tanA);

        // p0
        float p0x = (float) (mFixationPoint.x + mFixationRadius*Math.sin(arcTanA));
        float p0y = (float) (mFixationPoint.y - mFixationRadius*Math.cos(arcTanA));

        // p1
        float p1x = (float) (mDragPoint.x + mDragRadius*Math.sin(arcTanA));
        float p1y = (float) (mDragPoint.y - mDragRadius*Math.cos(arcTanA));

        // p2
        float p2x = (float) (mDragPoint.x - mDragRadius*Math.sin(arcTanA));
        float p2y = (float) (mDragPoint.y + mDragRadius*Math.cos(arcTanA));

        // p3
        float p3x = (float) (mFixationPoint.x - mFixationRadius*Math.sin(arcTanA));
        float p3y = (float) (mFixationPoint.y + mFixationRadius*Math.cos(arcTanA));

        // 拼装 贝塞尔的曲线路径
        bezierPath.moveTo(p0x,p0y);
        // 两个点
        PointF controlPoint = getControlPoint();
        // 画了第一条  第一个点（控制点,两个圆心的中心点），终点
        bezierPath.quadTo(controlPoint.x,controlPoint.y,p1x,p1y);

        // 画第二条
        bezierPath.lineTo(p2x,p2y);
        bezierPath.quadTo(controlPoint.x,controlPoint.y,p3x,p3y);
        bezierPath.close();

        return bezierPath;
    }

    public PointF getControlPoint() {
        return new PointF((mDragPoint.x+mFixationPoint.x)/2,(mDragPoint.y+mFixationPoint.y)/2);
    }

    private double getDistance(PointF point1, PointF point2) {
        return Math.sqrt((point1.x - point2.x) * (point1.x - point2.x) + (point1.y - point2.y) * (point1.y - point2.y));
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                float downX = event.getX();
//                float downY = event.getY();
//                initPoint(downX, downY);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                float moveX = event.getX();
//                float moveY = event.getY();
//                updateDragPoint(moveX, moveY);
//                break;
//            default:
//                break;
//        }
//        invalidate();
//        return true;
//    }

    public void updateDragPoint(float moveX, float moveY) {
        mDragPoint.x = moveX;
        mDragPoint.y = moveY;
        invalidate();
    }

    public void initPoint(float downX, float downY) {
        mFixationPoint = new PointF(downX, downY);
        mDragPoint = new PointF(downX, downY);
    }

    public static void attach(View view, BubbleMessageTouchListener.BubbleDisappearListener disappearListener) {
        view.setOnTouchListener(new BubbleMessageTouchListener(view, view.getContext()));
    }

    public void setDragBitmap(Bitmap bitmap) {
        this.mDragBitmap = bitmap;
    }

    public void handleActionUp() {
        if (mFixationRadius > mFixationRadiusMin) {
            // 回弹  值变化动画， 0变化到1
            ValueAnimator animator = ObjectAnimator.ofFloat(1);
            animator.setDuration(250);
            final PointF start = new PointF(mDragPoint.x, mDragPoint.y);
            final PointF end = new PointF(mFixationPoint.x, mFixationPoint.y);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float percent = (float) animation.getAnimatedValue();
                    PointF pointF = BubbleUtils.getPointByPercent(start, end, percent);
                    // 更新拖拽点
                    updateDragPoint(pointF.x, pointF.y);
                }
            });
            animator.setInterpolator(new OvershootInterpolator(3f));
            animator.start();
            // 还要通知 TouchListener 移除当前View 然后显示静态的 View
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if(mListener != null){
                        mListener.restore();
                    }
                }
            });
        } else {
            // 爆炸
        }
    }

    public void setMessageBubbleListener(MessageBubbleListener listener) {
        this.mListener = listener;
    }

    public interface MessageBubbleListener {

        /**
         * 还原
         */
        void restore();

        /**
         * 消失爆炸
         *
         * @param pointF PointF
         */
        void dismiss(PointF pointF);
    }
}
