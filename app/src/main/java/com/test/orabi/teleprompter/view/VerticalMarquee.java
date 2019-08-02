package com.test.orabi.teleprompter.view;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;

public class VerticalMarquee extends androidx.appcompat.widget.AppCompatTextView {


    private final Context mContext;
    private boolean isUserScrolling, isPaused, stop;
    private boolean isNotDrawn = true;
    private long duration;
    private int pixelYOffSet;

    public VerticalMarquee(Context context, AttributeSet attrs,
                           int defStyle) {
        super(context, attrs, defStyle);

        this.mContext = context;

        if (!isInEditMode()) {
            init();
        }
    }

    public VerticalMarquee(Context context, AttributeSet attrs) {

        super(context, attrs);

        this.mContext = context;

        if (!isInEditMode()) {
            init();
        }
    }


    public VerticalMarquee(Context context) {
        super(context);

        this.mContext = context;
        if (!isInEditMode()) {
            init();
        }
    }

    private void init() {
        setDuration(4L);
        setPixelYOffSet(1);

        isUserScrolling = isPaused = stop = false;

        startMarquee();

    }

    public void setDuration(long duration) {
        if (duration <= 0) {
            this.duration = 65L;
        } else {
            this.duration = duration;
        }

    }

    public void setPixelYOffSet(int pixelYOffSet) {
        if (pixelYOffSet < 1) {
            this.pixelYOffSet = 1;
        } else {
            this.pixelYOffSet = pixelYOffSet;
        }
    }

    private void startMarquee() {
        new AutoScrollTextView().execute();
    }

    public void stopMarquee() {
        stop = true;
    }

    public void pauseMarquee() {
        isPaused = true;
    }

    public void resumeMarquee() {
        isPaused = false;
    }


    public boolean isPaused() {
        return isPaused || isUserScrolling;
    }

    @SuppressLint("StaticFieldLeak")
    private class AutoScrollTextView extends AsyncTask<Void, Void, Void> {

        Activity mActivity = (Activity) mContext;
        private int pixelCount;

        @Override
        protected Void doInBackground(Void... params) {

            // Check to see if the VMTV has been drawn to get proper sizing.
            while (textViewNotDrawn()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            // Changing stop to true will permanently cancel autoscrolling.
            // Cannot be restarted.
            while (!stop) {

                // Allows scrolling to resume after VMTV has been released.
                if (!(VerticalMarquee.this).isPressed()
                        && isUserScrolling && !isPaused) {
                    isUserScrolling = false;
                }

                while (!isUserScrolling && !stop && !isPaused) {

                    // Sleep duration amount between scrollBy pixelYOffSet
                    try {
                        Thread.sleep(duration);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    mActivity.runOnUiThread(() -> {

                        // If the user is manually scrolling pause the auto
                        // scrolling marquee
                        if ((VerticalMarquee.this).isPressed()) {

                            isUserScrolling = true;

                        } else { // Otherwise auto scroll marquee

                            // if VMTV has reached or exceeded the last
                            // Y pixel scroll back to top
                            if ((VerticalMarquee.this).getScrollY() >= pixelCount) {

                                (VerticalMarquee.this).scrollTo(0,
                                        0);

                            } else { // Otherwise scroll by the pixelYOffSet

                                (VerticalMarquee.this).scrollBy(0,
                                        pixelYOffSet);
                            }

                            (VerticalMarquee.this).invalidate();
                        }

                    });
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
        }

        private boolean textViewNotDrawn() {

            mActivity.runOnUiThread(() -> {
                // Checks to see if VMTV has been drawn.
                // In theory line count should be greater than 0 if drawn.
                if ((VerticalMarquee.this).getLineCount() > 0) {
                    // Calculate the total pixel height that needs to be
                    // scrolled.
                    // May need additional calculations if there is
                    // additional padding.
                    pixelCount = (VerticalMarquee.this)
                            .getLineHeight()
                            * (VerticalMarquee.this).getLineCount();
                    isNotDrawn = false;
                }

            });

            return isNotDrawn;
        }

    }


}
