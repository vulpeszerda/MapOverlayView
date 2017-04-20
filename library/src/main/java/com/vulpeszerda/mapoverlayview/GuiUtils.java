package com.vulpeszerda.mapoverlayview;

import android.content.Context;

/**
 * Created by vulpes on 2017. 4. 19..
 */

final class GuiUtils {

    static float spToPx(Context ctx, int sp) {
        float density = ctx.getResources().getDisplayMetrics().scaledDensity;
        return (sp * density);
    }

    static float dpToPx(Context ctx, int dp) {
        float density = ctx.getResources().getDisplayMetrics().density;
        return (dp * density);
    }

    static float pxToDp(Context ctx, int px) {
        float density = ctx.getResources().getDisplayMetrics().density;
        return (px / density);
    }
}
