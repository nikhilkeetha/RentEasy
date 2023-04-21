package com.renteasy.associates.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * Created by Arthonsys on 12/2/2019.
 */

public class RegularEditText extends AppCompatEditText {
    public RegularEditText(Context context) {
        super(context);
        setassets(context);
    }

    public RegularEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setassets(context);
    }

    public RegularEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setassets(context);
    }


    private void setassets(Context context) {
        setTypeface(Typeface.createFromAsset(context.getAssets(), "font/MYRIADPRO_REGULAR.OTF"));

    }
}
