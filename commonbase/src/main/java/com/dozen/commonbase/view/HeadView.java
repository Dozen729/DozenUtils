package com.dozen.commonbase.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.dozen.commonbase.R;

/**
 * @author: Dozen
 * @description:
 * @time: 2020/12/3
 */

@SuppressLint("AppCompatCustomView")
public class HeadView extends ImageView {

    private String name;
    private Drawable picture;
    private Bitmap bitmap;
    private int width = 100;
    private int height = 100;
    private int mins = 100;

    public HeadView(Context context) {
        this(context, null);
    }

    public HeadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HelloView);

        if (typedArray.hasValue(R.styleable.HelloView_exampleDrawable)) {
            picture = typedArray.getDrawable(R.styleable.HelloView_exampleDrawable);
        }
        typedArray.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = getSize(100, widthMeasureSpec);
        height = getSize(100, heightMeasureSpec);

        mins = Math.min(width, height) / 2;
    }

    private int getSize(int defaultSize, int measureSpec) {
        int mySize = defaultSize;

        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
                mySize = dip2px(defaultSize);
                break;
            case MeasureSpec.AT_MOST:
                mySize = dip2px(size);
                break;
            case MeasureSpec.EXACTLY:
                mySize = size;
                break;
        }

        return mySize;
    }

    private int dip2px(float dpValue) {
        float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dpValue / scale + 0.5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setStrokeWidth(1);
        paint.setDither(true);
        paint.setAntiAlias(true);
        float mScale = 1;
        if (picture != null) {
            bitmap = drawableToBitmap(picture);
        }
        if (bitmap!=null){
            BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            mScale = (mins * 2.0f) / Math.min(bitmap.getHeight(), bitmap.getWidth());
            Matrix matrix = new Matrix();
            matrix.setScale(mScale, mScale);
            bitmapShader.setLocalMatrix(matrix);
            paint.setShader(bitmapShader);
            canvas.drawCircle(mins, mins, mins, paint);
        }
    }

    @Override
    public void setBackground(Drawable background) {
        this.picture = background;
        invalidate();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void setBackgroundResource(int resid) {
        this.picture = getContext().getResources().getDrawable(resid);
    }

    public void setPicture(Drawable picture) {
        this.picture = picture;
        invalidate();
    }

    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
        invalidate();
    }


    public static final Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

}