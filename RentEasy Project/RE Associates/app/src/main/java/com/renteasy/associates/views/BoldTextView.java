package com.renteasy.associates.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by Arthonsys on 12/2/2019.
 */

public class BoldTextView extends AppCompatTextView {
    public BoldTextView(Context context) {
        super(context);
        setassets(context);
    }

    public BoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setassets(context);
    }

    public BoldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setassets(context);
    }



    private void setassets(Context context) {
        setTypeface(Typeface.createFromAsset(context.getAssets(), "font/MYRIADPRO_BOLD.OTF"));
    }
}
