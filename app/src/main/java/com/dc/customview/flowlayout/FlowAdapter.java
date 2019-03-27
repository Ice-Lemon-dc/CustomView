package com.dc.customview.flowlayout;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;

public abstract class FlowAdapter {

    public abstract int getCount();

    public abstract View getView(int position, ViewGroup parent);

    /**
     * 3.观察者模式及时通知更新
     *
     * @param observer DataSetObserver
     */
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    public void registerDataSetObserver(DataSetObserver observer) {

    }
}
