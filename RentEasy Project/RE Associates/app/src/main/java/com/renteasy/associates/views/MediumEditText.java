package com.renteasy.associates.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * Created by Arthonsys on 12/2/2019.
 */

public class MediumEditText extends AppCompatEditText {
    public MediumEditText(Context context) {
        super(context);
        setassets(context);
    }

    public MediumEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setassets(context);
    }

    public MediumEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setassets(context);
    }



    private void setassets(Context context) {
        setTypeface(Typeface.createFromAsset(context.getAssets(), "font/MYRIADPRO_SEMIBOLD.OTF"));
    }
}
