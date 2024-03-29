package com.dozen.commonbase.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.dozen.commonbase.R;
import com.dozen.commonbase.view.magicindicator.buildins.UIUtil;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/3/8
 */
public class CircleLoadingView extends View {

    private Context mContext;

    // 刻度画笔
    private Paint mScalePaint;

    // 小原点画笔
    private Paint mDotPaint;

    // 文字画笔
    private Paint mTextPaint;

    // 当前进度
    private int progress = 0;

    /**
     * 小圆点的当前进度
     */
    public float mDotProgress;

    // View宽
    private int mWidth;

    // View高
    private int mHeight;

    private int indexColor;

    private int baseColor;

    private int dotColor;

    private int textSize;

    private int textColor;

    private boolean textShow = false;

    private boolean progressShow = true;

    private float lineWidth = 1.0f;

    private int dotSize = 3;

    private int dotLocation = 3;

    private ValueAnimator animator;

    public CircleLoadingView(Context context) {
        this(context, null);
    }

    public CircleLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 获取用户配置属性
        TypedArray tya = context.obtainStyledAttributes(attrs, R.styleable.CircleLoading);
        baseColor = tya.getColor(R.styleable.CircleLoading_baseColor, Color.LTGRAY);
        indexColor = tya.getColor(R.styleable.CircleLoading_indexColor, Color.BLUE);
        textColor = tya.getColor(R.styleable.CircleLoading_textColor, Color.BLUE);
        dotColor = tya.getColor(R.styleable.CircleLoading_dotColor, Color.RED);
        textSize = tya.getDimensionPixelSize(R.styleable.CircleLoading_textSize, 36);
        textShow = tya.getBoolean(R.styleable.CircleLoading_textShow, false);
        progressShow = tya.getBoolean(R.styleable.CircleLoading_progressShow, true);
        lineWidth = tya.getFloat(R.styleable.CircleLoading_lineWidth, 1.0f);
        dotSize = tya.getInteger(R.styleable.CircleLoading_dotSize, 3);
        dotLocation = tya.getInteger(R.styleable.CircleLoading_dotLocation, 3);
        tya.recycle();

        initUI();
    }

    private void initUI() {
        mContext = getContext();

        // 刻度画笔
        mScalePaint = new Paint();
        mScalePaint.setAntiAlias(true);
        mScalePaint.setStrokeWidth(UIUtil.dip2px(mContext, 1));
        mScalePaint.setStrokeCap(Paint.Cap.ROUND);
        mScalePaint.setColor(baseColor);
        mScalePaint.setStyle(Paint.Style.STROKE);

        // 小圆点画笔
        mDotPaint = new Paint();
        mDotPaint.setAntiAlias(true);
        mDotPaint.setColor(dotColor);
        mDotPaint.setStrokeWidth(UIUtil.dip2px(mContext, 1));
        mDotPaint.setStyle(Paint.Style.FILL);

        // 文字画笔
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setStrokeWidth(UIUtil.dip2px(mContext, 1));
        mTextPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (progressShow) {
            drawArcScale(canvas);
        }
        if (textShow) {
            drawTextValue(canvas);
        }
        drawRotateDot(canvas);
    }

    /**
     * 画刻度
     */
    private void drawArcScale(Canvas canvas) {
        canvas.save();

        for (int i = 0; i < 100; i++) {
            if (progress > i) {
                mScalePaint.setColor(indexColor);
            } else {
                mScalePaint.setColor(baseColor);
            }
            canvas.drawLine(mWidth / 2, 0, mHeight / 2, UIUtil.dip2px(mContext, lineWidth), mScalePaint);
            // 旋转的度数 = 100 / 360
            canvas.rotate(3.6f, mWidth / 2, mHeight / 2);
        }

        canvas.restore();
    }

    /**
     * 画内部数值
     */
    private void drawTextValue(Canvas canvas) {
        canvas.save();

        String showValue = String.valueOf(progress);
        Rect textBound = new Rect();
        mTextPaint.getTextBounds(showValue, 0, showValue.length(), textBound);    // 获取文字的矩形范围
        float textWidth = textBound.right - textBound.left;  // 获得文字宽
        float textHeight = textBound.bottom - textBound.top; // 获得文字高
        canvas.drawText(showValue, mWidth / 2 - textWidth / 2, mHeight / 2 + textHeight / 2, mTextPaint);

        canvas.restore();
    }

    /**
     * 画旋转小圆点
     */
    private void drawRotateDot(final Canvas canvas) {
        canvas.save();

        canvas.rotate(mDotProgress * 3.6f, mWidth / 2, mHeight / 2);
        canvas.drawCircle(mWidth / 2, UIUtil.dip2px(mContext, lineWidth) + UIUtil.dip2px(mContext, dotLocation), UIUtil.dip2px(mContext, dotSize), mDotPaint);

        canvas.restore();
    }

    /**
     * 启动小圆点旋转动画
     */
    public void startDotAnimator() {
        animator = ValueAnimator.ofFloat(0, 100);
        animator.setDuration(1500);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 设置小圆点的进度，并通知界面重绘
                mDotProgress = (Float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }

    /**
     * 设置进度
     */
    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int myWidthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int myWidthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int myHeightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int myHeightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        // 获取宽
        if (myWidthSpecMode == MeasureSpec.EXACTLY) {
            // match_parent/精确值
            mWidth = myWidthSpecSize;
        } else {
            // wrap_content
            mWidth = UIUtil.dip2px(mContext, 120);
        }

        // 获取高
        if (myHeightSpecMode == MeasureSpec.EXACTLY) {
            // match_parent/精确值
            mHeight = myHeightSpecSize;
        } else {
            // wrap_content
            mHeight = UIUtil.dip2px(mContext, 120);
        }

        // 设置该view的宽高
        setMeasuredDimension(mWidth, mHeight);
    }
}