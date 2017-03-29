package com.example.liuhailong.longviewsample.bean;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 16/9/15
 * update time:
 * email: 723526676@qq.com
 */
public class ModeType {

    @ModeTypeChecker
    public static final int TYPE_CIRCLE = 1;
    @ModeTypeChecker
    public static final int TYPE_SCALEX = 2;
    @ModeTypeChecker
    public static final int TYPE_SCALEY = 3;
    @ModeTypeChecker
    public static final int TYPE_ROTATEXSCALEY = 4;
    @ModeTypeChecker
    public static final int TYPE_ROTETEYSCALEX = 5;
    @ModeTypeChecker
    public static final int TYPE_CIRCLE_NO_LOOP = 6;


    @IntDef({TYPE_CIRCLE,TYPE_SCALEX,TYPE_SCALEY,TYPE_ROTETEYSCALEX,TYPE_ROTATEXSCALEY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ModeTypeChecker{}
}
