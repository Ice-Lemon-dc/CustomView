package com.dc.customview.parallaxviewpager;

public class ParallaxTag {

    public float translationXIn;
    public float translationXOut;
    public float translationYIn;
    public float translationYOut;

    @Override
    public String toString() {
        return "translationXIn->"+translationXIn+" translationXOut->"+translationXOut
                +" translationYIn->"+translationYIn+" translationYOut->"+translationYOut;
    }
}
