package com.pqq.banner.util;

import android.content.Context;
import android.util.TypedValue;

public class PixAndDpUtil {
    public static int dp2px(int value, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
    }

    public static int dp2sp(int value, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, context.getResources().getDisplayMetrics());
    }
}
