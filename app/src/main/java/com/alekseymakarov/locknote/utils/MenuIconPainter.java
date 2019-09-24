package com.alekseymakarov.locknote.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MenuItem;

import com.alekseymakarov.locknote.R;

public class MenuIconPainter {

    public static void tintMenuItem (Context context, MenuItem menuItem){
        Drawable drawable = menuItem.getIcon();
        drawable.setTint(context.getResources().getColor(R.color.secondaryLightColor));
    }
}
