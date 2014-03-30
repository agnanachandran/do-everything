package ca.pluszero.emotive.utils;

import android.content.res.Resources;

public final class ScreenUtils {

    public static int getNavbarHeight(Resources res) {
        int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return res.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

}
