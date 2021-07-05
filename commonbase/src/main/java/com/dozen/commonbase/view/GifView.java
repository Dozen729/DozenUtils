package com.dozen.commonbase.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.dozen.commonbase.R;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/7/5
 */
public class GifView extends View {

    public final static int DEFAULT_MOVIE_VIEW_DURATION = 1000;

    private int movieMovieResourceId = 0;
    private Movie movie;
    private Long movieStart = 0L;
    private int currentAnimationTime = 0;

    private Float movieLeft = 0f;
    private Float movieTop = 0f;
    private Float movieScale = 0f;

    private int movieMeasuredMovieWidth = 0;
    private int movieMeasuredMovieHeight = 0;

    private boolean isPaused = false;

    private boolean isVisible = true;

    private int gifResource;

    public GifView(Context context) {
        super(context);
        setViewAttributes(context, null, 0);
    }

    public GifView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setViewAttributes(context, attrs, 0);
    }

    public GifView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setViewAttributes(context, attrs, defStyleAttr);
    }

    public GifView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setViewAttributes(context, attrs, defStyleRes);
    }

    public boolean isPaused() {
        return isPaused;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public int getGifResource() {
        return movieMovieResourceId;
    }

    public void setGifResource(int gifResource) {
        this.movieMovieResourceId = gifResource;
        movie = Movie.decodeStream(getResources().openRawResource(movieMovieResourceId));
        requestLayout();
    }

    private void setViewAttributes(Context context, AttributeSet attrs, int defStyle) {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.GifView, defStyle, 0);

        //-1 is default value
        movieMovieResourceId = array.getResourceId(R.styleable.GifView_gif, -1);
        isPaused = array.getBoolean(R.styleable.GifView_paused, false);

        array.recycle();

        if (movieMovieResourceId != -1) {
            movie = Movie.decodeStream(getResources().openRawResource(movieMovieResourceId));
        }
    }


    public void play() {
        if (this.isPaused) {
            this.isPaused = false;
            /**
             * Calculate new movie start time, so that it resumes from the same
             * frame.
             */
            movieStart = android.os.SystemClock.uptimeMillis() - currentAnimationTime;
            invalidate();
        }
    }

    public void pause() {
        if (!this.isPaused) {
            this.isPaused = true;
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (movie != null) {
            int movieWidth = movie.width();
            int movieHeight = movie.height();
            /*
             * Calculate horizontal scaling
             */
            float scaleH = 1f;
            int measureModeWidth = MeasureSpec.getMode(widthMeasureSpec);

            if (measureModeWidth != MeasureSpec.UNSPECIFIED) {
                int maximumWidth = MeasureSpec.getSize(widthMeasureSpec);
                if (movieWidth > maximumWidth) {
                    scaleH = movieWidth / maximumWidth;
                }
            }
            /*
             * calculate vertical scaling
             */
            float scaleW = 1f;
            int measureModeHeight = MeasureSpec.getMode(heightMeasureSpec);

            if (measureModeHeight != MeasureSpec.UNSPECIFIED) {
                int maximumHeight = MeasureSpec.getSize(heightMeasureSpec);
                if (movieHeight > maximumHeight) {
                    scaleW = movieHeight / maximumHeight;
                }
            }
            /*
             * calculate overall scale
             */
            movieScale = 1f / Math.max(scaleH, scaleW);
            movieMeasuredMovieWidth = (int) (movieWidth * movieScale);
            movieMeasuredMovieHeight = (int) (movieHeight * movieScale);
            setMeasuredDimension(movieMeasuredMovieWidth, movieMeasuredMovieHeight);
        } else {
            /*
             * No movie set, just set minimum available size.
             */
            setMeasuredDimension(0, 0);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        /*
         * Calculate movieLeft / movieTop for drawing in center
         */
        movieLeft = (getWidth() - movieMeasuredMovieWidth) / 2f;
        movieTop = (getHeight() - movieMeasuredMovieHeight) / 2f;
        isVisible = getVisibility() == View.VISIBLE;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (movie != null) {
            if (!isPaused) {
                updateAnimationTime();
                drawMovieFrame(canvas);
                invalidate();
            } else {
                drawMovieFrame(canvas);
            }
        }
    }

    /**
     * Invalidates view only if it is isVisible.
     * <br></br>
     * [.postInvalidateOnAnimation] is used for Jelly Bean and higher.
     */

    @Override
    public void invalidate() {
        super.invalidate();
        if (isVisible) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                postInvalidateOnAnimation();
            } else {
                invalidate();
            }
        }
    }

    /**
     * Calculate current animation time
     */
    private void updateAnimationTime() {
        long now = android.os.SystemClock.uptimeMillis();

        if (movieStart == 0L) {
            movieStart = now;
        }

        int duration = movie.duration();

        if (duration == 0) {
            duration = DEFAULT_MOVIE_VIEW_DURATION;
        }

        currentAnimationTime = (int) ((now - movieStart) % duration);
    }

    /**
     * Draw current GIF frame
     */
    private void drawMovieFrame(Canvas canvas) {
        movie.setTime(currentAnimationTime);
        canvas.save();
        canvas.scale(movieScale, movieScale);
        movie.draw(canvas, movieLeft / movieScale, movieTop / movieScale);
        canvas.restore();
    }


    public void onScreenStateChanged(int screenState) {
        super.onScreenStateChanged(screenState);
        isVisible = screenState == View.SCREEN_STATE_ON;
        invalidate();
    }

    public void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        isVisible = visibility == View.VISIBLE;
        invalidate();
    }

    public void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        isVisible = visibility == View.VISIBLE;
        invalidate();
    }

}
