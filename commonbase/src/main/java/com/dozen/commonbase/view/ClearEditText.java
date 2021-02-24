package com.dozen.commonbase.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.TextView;

import com.dozen.commonbase.R;

/**
 * 自定义带清除EditText
 */
@SuppressLint("AppCompatCustomView")
public class ClearEditText extends EditText implements View.OnFocusChangeListener, TextWatcher {

    /**
     * 删除按钮的引用
     */
    private Drawable mClearDrawable;
    /**
     * 控件是否有焦点
     */
    private boolean hasFocus;
    //下划线
    private TextView lineView;
    //是否显示一键清空图标,默认显示
    private boolean needClearDrawable;

    public ClearEditText(Context context) {
        this(context,null);
    }
    public ClearEditText(Context context, AttributeSet attrs){
        //这里构造方法也很重要，不加这个很多属性不能再XML里面定义
        this(context, attrs, android.R.attr.editTextStyle);
        init(attrs,context);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs,context);
    }

    private void init(AttributeSet attrs, Context context) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ClearDrawable);
        needClearDrawable = a.getBoolean(R.styleable.ClearDrawable_needClearDrawable,true);
        a.recycle();
        //获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
        mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null) {
            mClearDrawable = getResources().getDrawable(R.drawable.icon_remove_bg);
        }
        mClearDrawable.setBounds(0, 0, mClearDrawable.getMinimumWidth(), mClearDrawable.getMinimumHeight());
        //默认设置隐藏图标
        setClearIconVisible(false);
        //设置焦点改变的监听
        setOnFocusChangeListener(this);
        //设置输入框里面内容发生改变的监听
        addTextChangedListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mClearDrawable != null && event.getAction() == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            //判断触摸点是否在水平范围内
            boolean isInnerWidth = (x > (getWidth() - getTotalPaddingRight())) &&
                    (x < (getWidth() - getPaddingRight()));
            //获取删除图标的边界，返回一个Rect对象
            Rect rect = mClearDrawable.getBounds();
            //获取删除图标的高度
            int height = rect.height();
            int y = (int) event.getY();
            //计算图标底部到控件底部的距离
            int distance = (getHeight() - height) / 2;
            //判断触摸点是否在竖直范围内(可能会有点误差)
            //触摸点的纵坐标在distance到（distance+图标自身的高度）之内，则视为点中删除图标
            boolean isInnerHeight = (y > distance) && (y < (distance + height));
            if (isInnerHeight && isInnerWidth) {
                this.setText("");
                if(contentClearListener != null)
                    contentClearListener.OnContentClear(this);
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     *
     * @param visible
     */
    private void setClearIconVisible(boolean visible) {
        //如果不需要则不显示一键清除图标
        if(!needClearDrawable)
            return;
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1],
                right, getCompoundDrawables()[3]);
    }

    /**
     * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFocus = hasFocus;
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
        if (lineView != null)
            lineView.setEnabled(!hasFocus);
        if (focusChangeListener != null)
            focusChangeListener.onFocusChange(v, hasFocus);
    }

    /**
     * 当输入框里面内容发生变化的时候回调的方法
     */
    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        if (hasFocus) {
            setClearIconVisible(text.length() > 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable editable){
        formatBankCard(editable);
        formatMoney(editable);
    }

    /**
     * 设置晃动动画
     */
    public void setShakeAnimation() {
        this.setAnimation(shakeAnimation(5));
    }

    /**
     * 晃动动画
     *
     * @param counts 1秒钟晃动多少下
     * @return
     */
    public static Animation shakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(1000);
        return translateAnimation;
    }

    /**
     * 设置下划线View
     * @param lineView
     */
    public void setLineView(TextView lineView) {

        this.lineView = lineView;
    }

    private OnFocusChangeListener  focusChangeListener;
    public void setOnFocusChangeListener(OnFocusChangeListener  focusChangeListener){
        this.focusChangeListener = focusChangeListener;
    }

    /**
     * EditText 焦点监听
     */
    public interface OnFocusChangeListener{
        void onFocusChange(View v, boolean hasFocus);
    }

    private OnContentClearListener contentClearListener;

    public void setOnContentClearListener(OnContentClearListener contentClearListener){
        this.contentClearListener = contentClearListener;
    }
    /**
     * 一键清除监听
     */
    public interface OnContentClearListener{
        void OnContentClear(View v);
    }

    /**
     * 格式化银行卡号,继承类中重写该方法
     * @param editable
     */
    protected void formatBankCard(Editable editable){

    }

    /**
     * 格式化金额，继承类中重写该方法
     * @param editable
     */
    protected void formatMoney(Editable editable){
        String editStr = editable.toString().trim();

        int posDot = editStr.indexOf(".");
        //不允许输入3位小数,超过三位就删掉
        if (posDot < 0) {
            return;
        }
        if (editStr.length() - posDot - 1 > 2) {
            editable.delete(posDot + 3, posDot + 4);
        } else {
            //TODO...这里写逻辑
        }
    }
}
