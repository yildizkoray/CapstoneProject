package com.example.koray.capstoneproject.Utils;

import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by Koray on 12.12.2017.
 */

public class CalculateWidthHeight {
    private int width, height;

    public CalculateWidthHeight() {

    }

    public int calculateWidth(final View view) {
        ViewTreeObserver observer = view.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                width = view.getMeasuredWidth();
                return true;
            }
        });
        return width;
    }

    public int calculateHeight(final View view) {
        ViewTreeObserver observer = view.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                height = view.getMeasuredHeight();
                return true;
            }
        });
        return height;
    }

}
