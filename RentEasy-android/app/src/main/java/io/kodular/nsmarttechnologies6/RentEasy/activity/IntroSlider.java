package io.kodular.nsmarttechnologies6.RentEasy.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroFragment;

import io.kodular.nsmarttechnologies6.RentEasy.R;

public class IntroSlider extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(AppIntroFragment.newInstance("Find for Rent.", "Find your need for rent with out hustling.",
                R.drawable.search, ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary)));

        addSlide(AppIntroFragment.newInstance("Post Free Ads.", "Post a free Ad for your house, bike, car, function hall for rent.",
                R.drawable.marketing, ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary)));

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
