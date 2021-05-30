package com.dc.customview.listdata;

import android.view.View;
import android.view.ViewGroup;

/**
 * @author Lemon
 */
public abstract class BaseMenuAdapter {

    /**
     * 条数
     *
     * @return int
     */
    public abstract int getCount();

    /**
     * 获取当前的TabView
     *
     * @param position int
     * @param parent ViewGroup
     * @return View
     */
    public abstract View getTabView(int position, ViewGroup parent);

    /**
     * 获取当前的菜单内容
     *
     * @param position int
     * @param parent ViewGroup
     * @return View
     */
    public abstract View getMenuView(int position, ViewGroup parent);

    /**
     * 菜单打开
     * @param tabView View
     */
    public void menuOpen(View tabView) {

    }

    /**
     * 菜单关闭
     * @param tabView View
     */
    public void menuClose(View tabView) {

    }
}
