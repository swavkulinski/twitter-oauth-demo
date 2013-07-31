package com.swavdev.tc;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGBuilder;

/**
 * Created by swav on 15/06/13.
 */
public class SVGPicture extends View {

    private static final String TAG = SVGPicture.class.getSimpleName();

    Picture mPicture;

    public SVGPicture(Context context) {
        super(context);
    }

    public SVGPicture(Context context, AttributeSet attrs) {
        super(context, attrs);
        processAttributes(context, attrs);
    }

    public SVGPicture(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        processAttributes(context, attrs);
    }

    private void processAttributes(Context context, AttributeSet attrs) {
        TypedArray attributes = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SVGPicture,
                0, 0
        );
        try {
            if (attributes.hasValue(R.styleable.SVGPicture_svg)) {
                int resource = attributes.getResourceId(R.styleable.SVGPicture_svg, -1);
                SVG svg = new SVGBuilder()
                        .readFromResource(context.getResources(), resource)
                        .build();
                mPicture = svg.getPicture();
            }

        } catch (Exception e) {
            Log.e(TAG, "Exception while parsing attributes:" + e.getMessage());
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPicture != null) {
            Rect bounds = new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight());
            canvas.drawPicture(mPicture, bounds);
        }
    }


}
