package io.RentEasy.support;

import android.graphics.Rect;
import android.view.TouchDelegate;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class ViewUtils {

    private static final Rect outRect = new Rect();
    private static final int[] location = new int[2];

    public static boolean inViewBounds(View view, int x, int y) {
        view.getDrawingRect(outRect);
        view.getLocationOnScreen(location);
        outRect.offset(location[0], location[1]);
        return outRect.contains(x, y);
    }


    /**
     * @link http://stackoverflow.com/questions/4946295/android-expand-collapse-animation
     */
    public static void expand(final View v, final int startHeight, final int targetHeight, final long duration) {
        v.setVisibility(View.VISIBLE);
        Animation anim = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1 ? v.getLayoutParams().height
                        : startHeight + (int) ((targetHeight - startHeight) * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        anim.setFillAfter(true);
        anim.setDuration(duration);
        v.startAnimation(anim);
    }

    /**
     * @link http://stackoverflow.com/questions/4946295/android-expand-collapse-animation
     */
    public static void collapse(final View v, final int startHeight, final int targetHeight, final long duration, final boolean hide) {

        Animation anim = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    if (hide) v.setVisibility(View.INVISIBLE); // GONE
                } else {
                    v.getLayoutParams().height = startHeight - (int) ((startHeight - targetHeight) * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        anim.setFillAfter(true);
        anim.setDuration(duration);
        v.startAnimation(anim);
    }

    /**
     * @link http://stackoverflow.com/questions/4946295/android-expand-collapse-animation
     */
    public static void expand(final View v) {
        v.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        //a.setInterpolator(new FastOutSlowInInterpolator());
        v.startAnimation(a);
    }

    /**
     * @link http://stackoverflow.com/questions/4946295/android-expand-collapse-animation
     */
    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        //a.setInterpolator(new FastOutSlowInInterpolator());
        v.startAnimation(a);
    }


    /**
     * @param scrollView
     * @return
     */
    public static boolean canScroll(ScrollView scrollView) {
        View child = scrollView.getChildAt(0);
        if (child != null) {
            int childHeight = (child).getHeight();
            return scrollView.getHeight() < childHeight + scrollView.getPaddingTop() + scrollView.getPaddingBottom();
        }
        return false;
    }


    /**
     * @param bigView
     * @param smallView
     * @param extraPadding
     */
    public static void expandTouchArea(final View bigView, final View smallView, final int extraPadding) {
        bigView.post(new Runnable() {
            @Override
            public void run() {
                Rect rect = new Rect();
                smallView.getHitRect(rect);
                rect.top -= extraPadding;
                rect.left -= extraPadding;
                rect.right += extraPadding;
                rect.bottom += extraPadding;
                bigView.setTouchDelegate(new TouchDelegate(rect, smallView));
            }
        });
    }
}
