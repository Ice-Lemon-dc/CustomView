package com.dc.customview.parallaxviewpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.LayoutInflaterCompat;
import androidx.core.view.LayoutInflaterFactory;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import com.dc.customview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 视差动画的Fragment
 * @author Lemon
 */
public class ParallaxFragment extends Fragment implements LayoutInflater.Factory2 {

    public static final String LAYOUT_ID_KEY = "LAYOUT_ID_KEY";
    private CompatViewInflater mCompatViewInflater;

    // 存放所有的需要位移的View
    private List<View> mParallaxViews = new ArrayList<>();

    private int[] mParallaxAttrs = new int[]{R.attr.translationXIn,
            R.attr.translationXOut, R.attr.translationYIn, R.attr.translationYOut};

    @Nullable
    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutId = getArguments().getInt(LAYOUT_ID_KEY);
        // 克隆一个出来
        inflater = inflater.cloneInContext(getActivity());
        LayoutInflaterCompat.setFactory2(inflater, this);
        return inflater.inflate(layoutId, container, false);
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull  String name, @NonNull Context context, @NonNull  AttributeSet attrs) {
        // View都会来这里,创建View
        // 拦截到View的创建  获取View之后要去解析
        // 1. 创建View
        // If the Factory didn't handle it, let our createView() method try
        View view = createView(parent, name, context, attrs);

        // 2.1 一个activity的布局肯定对应多个这样的 SkinView
        if (view != null) {
            // Log.e("TAG", "我来创建View");
            // 解析所有的我们自己关注属性
            analysisAttrs(view, context, attrs);
        }
        return view;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return null;
    }

    private void analysisAttrs(View view, Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, mParallaxAttrs);
        if (array.getIndexCount() != 0) {
           /* float xIn = array.getFloat(0,0f);
            float xOut = array.getFloat(1,0f);
            float yIn = array.getFloat(2,0f);
            float yOut = array.getFloat(3,0f);*/
            int n = array.getIndexCount();
            ParallaxTag tag = new ParallaxTag();
            for (int i = 0; i < n; i++) {
                int attr = array.getIndex(i);
                switch (attr) {
                    case 0:
                        tag.translationXIn = array.getFloat(attr,0f);
                        break;
                    case 1:
                        tag.translationXOut = array.getFloat(attr,0f);
                        break;
                    case 2:
                        tag.translationYIn = array.getFloat(attr,0f);
                        break;
                    case 3:
                        tag.translationYOut = array.getFloat(attr,0f);
                        break;
                    default:
                        break;
                }
            }
            // 自定义属性怎么存? 还要一一绑定  在View上面设置一个tag
            view.setTag(R.id.parallax_tag,tag);
            //Log.e("TAG",tag.toString());
            mParallaxViews.add(view);
        }
        array.recycle();
    }

    public View createView(View parent, final String name, @NonNull Context context,
                           @NonNull AttributeSet attrs) {
        final boolean isPre21 = Build.VERSION.SDK_INT < 21;

        if (mCompatViewInflater == null) {
            mCompatViewInflater = new CompatViewInflater();
        }

        // We only want the View to inherit it's context if we're running pre-v21
        final boolean inheritContext = isPre21 && true
                && shouldInheritContext((ViewParent) parent);

        return mCompatViewInflater.createView(parent, name, context, attrs, inheritContext,
                isPre21, /* Only read android:theme pre-L (L+ handles this anyway) */
                true /* Read read app:theme as a fallback at all times for legacy reasons */
        );
    }

    private boolean shouldInheritContext(ViewParent parent) {
        if (parent == null) {
            // The initial parent is null so just return false
            return false;
        }
        while (true) {
            if (parent == null) {
                // Bingo. We've hit a view which has a null parent before being terminated from
                // the loop. This is (most probably) because it's the root view in an inflation
                // call, therefore we should inherit. This works as the inflated layout is only
                // added to the hierarchy at the end of the inflate() call.
                return true;
            } else if (!(parent instanceof View)
                    || ViewCompat.isAttachedToWindow((View) parent)) {
                // We have either hit the window's decor view, a parent which isn't a View
                // (i.e. ViewRootImpl), or an attached view, so we know that the original parent
                // is currently added to the view hierarchy. This means that it has not be
                // inflated in the current inflate() call and we should not inherit the context.
                return false;
            }
            parent = parent.getParent();
        }
    }

    public List<View> getParallaxViews() {
        return mParallaxViews;
    }
}
