package com.renteasy.associates;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroFragment;
import com.renteasy.associates.Activities.LoginActivity;

public class IntroSlider extends AppIntro {

    Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;

        // below line is for adding the new slide to our app.
        // we are creating a new instance and inside that
        // we are adding the title, description, image and
        // background color for our slide.
        // below line is use for creating first slide
        addSlide(AppIntroFragment.newInstance("Testing", "Example",
                R.drawable.logo__10_, ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary)));

        addSlide(AppIntroFragment.newInstance("Testing", "Example",
                R.drawable.logo__10_, ContextCompat.getColor(getApplicationContext(), R.color.black)));

        addSlide(AppIntroFragment.newInstance("Testing", "Example",
                R.drawable.logo__10_, ContextCompat.getColor(getApplicationContext(), R.color.yellow)));

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent i = new Intent(IntroSlider.this, LoginActivity.class);
        startActivity(i);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent i = new Intent(IntroSlider.this, LoginActivity.class);
        startActivity(i);
    }
}
