package com.dc.customview.messagebubble;

import android.app.StatusBarManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * 监听当前View的触摸事件
 *
 * @author Lemon
 */
public class BubbleMessageTouchListener implements View.OnTouchListener, MessageBubbleView.MessageBubbleListener {

    /**
     * 原来要拖动爆炸的View
     */
    private View mStaticView;
    private WindowManager mWindowManager;
    private MessageBubbleView mMessageBubbleView;
    private WindowManager.LayoutParams mParams;
    private Context mContext;

    public BubbleMessageTouchListener(View view, Context context) {
        mStaticView = view;
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mMessageBubbleView = new MessageBubbleView(context);
        mMessageBubbleView.setMessageBubbleListener(this);
        mParams = new WindowManager.LayoutParams();
        // 背景要透明
        mParams.format = PixelFormat.TRANSPARENT;
        mContext = context;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 自己隐藏
                mStaticView.setVisibility(View.INVISIBLE);
                // 要在WindowManager上创建一个贝塞尔View
                mWindowManager.addView(mMessageBubbleView, mParams);
                // 初始化贝塞尔View的点
                mMessageBubbleView.initPoint(event.getRawX(), event.getRawY() - BubbleUtils.getStatusBarHeight(mContext));

                mMessageBubbleView.setDragBitmap(getBitmapByView(mStaticView));
                break;
            case MotionEvent.ACTION_MOVE:
                mMessageBubbleView.updateDragPoint(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                mMessageBubbleView.handleActionUp();
                break;
            default:
                break;
        }
        return true;
    }

    private Bitmap getBitmapByView(View view) {
//        view.buildDrawingCache();
//        Bitmap bitmap = view.getDrawingCache();
//        return bitmap;
        if (view == null) {
            return null;
        }
        Bitmap bitmap;
        bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bitmap);
        c.translate(-view.getScrollX(), -view.getScrollY());
        view.draw(c);
        return bitmap;
    }

    @Override
    public void restore() {
        // 把消息的View移除
        mWindowManager.removeView(mMessageBubbleView);
        // 把原来的View显示
        mStaticView.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismiss(PointF pointF) {

    }

    public interface BubbleDisappearListener {
        void dismiss(View view);
    }
}
