package com.dozen.commonbase.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.graphics.drawable.DrawableCompat;

import com.dozen.commonbase.ActivityManager;
import com.dozen.commonbase.act.CommonBaseActivity;
import com.dozen.commonbase.R;
import com.dozen.commonbase.view.roundview.RoundLinearLayout;

/**
 * @author: Dozen
 * @date: 2020/11/23
 * @description: 头部view
 */
public class TitleView extends LinearLayout {

    private Context mContext;
    private ImageView backPicture;//返回键
    private TextView titleView;//标题文字
    private TextView rightText;//右边文字
    private ImageView rightPicture;//右边图标
    private RoundLinearLayout rllRightBg;//右边背景
    private int topContentColor = getResources().getColor(R.color.top_content_color);

    public TitleView(Context context) {
        super(context);
        mContext = context;
        init(context);
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(context);
        setViewInfo(context, attrs);
    }

    private void init(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_title, null);
        addView(view);
        backPicture = (ImageView) findViewById(R.id.back_picture);
        titleView = (TextView) findViewById(R.id.title_name);
        rightText = (TextView) findViewById(R.id.tv_right_name);
        rightPicture = (ImageView) findViewById(R.id.iv_right_picture);
        rllRightBg = (RoundLinearLayout) findViewById(R.id.right_item_bg);
        setLeftBackClick(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CommonBaseActivity) context).hintKb();
                Context act = v.getContext();
                if (act instanceof Activity) {
                    ((Activity) act).finish();
                } else {
                    v.setClickable(false);
                    ActivityManager.getIntance().currentActivity().finish();
                }
            }
        });
    }

    /**
     * 方法名：  setViewInfo	<br>
     * 方法描述：设置自定义style<br>
     *
     * @param context
     * @param attrs
     */
    private void setViewInfo(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TitleView);
        String title = ta.getString(R.styleable.TitleView_titleName);
        setTitle(title);
        int color = ta.getColor(R.styleable.TitleView_titleColor, topContentColor);
        titleView.setTextColor(color);
        float dimension = ta.getDimension(R.styleable.TitleView_titleSize, 20);
        titleView.setTextSize(dimension);

        boolean titleLeftShow = ta.getBoolean(R.styleable.TitleView_titleLeftShow, true);
        boolean titleLine = ta.getBoolean(R.styleable.TitleView_titleLine, true);
        findViewById(R.id.title_back).setVisibility(titleLeftShow ? View.VISIBLE : View.INVISIBLE);
        findViewById(R.id.title_line).setVisibility(titleLine ? VISIBLE : View.GONE);

        String rightTitle = ta.getString(R.styleable.TitleView_rightTextName);
        rightText.setText(rightTitle);
        int rightColor = ta.getColor(R.styleable.TitleView_rightTextColor, topContentColor);
        rightText.setTextColor(rightColor);
        float rightDimension = ta.getDimension(R.styleable.TitleView_rightTextSize, 16);
        rightText.setTextSize(rightDimension);
        boolean rightTextShow = ta.getBoolean(R.styleable.TitleView_rightTextShow, false);
        rightText.setVisibility(rightTextShow ? View.VISIBLE : View.GONE);

        boolean pictureV = ta.getBoolean(R.styleable.TitleView_rightImageShow, false);
        rightPicture.setVisibility(pictureV ? View.VISIBLE : View.GONE);
        int picture = ta.getResourceId(R.styleable.TitleView_rightImagePicture,R.mipmap.icon_white_add);
        rightPicture.setBackgroundResource(picture);

        int rightBgColor = ta.getColor(R.styleable.TitleView_rightBackgroundColor, 0);
        if (rightBgColor != 0) {
            rllRightBg.getDelegate().setBackgroundColor(rightBgColor);
        }

        int bgColor = ta.getColor(R.styleable.TitleView_topBackground, Color.WHITE);
        findViewById(R.id.top_title_bg).setBackgroundColor(bgColor);

        int backP = ta.getResourceId(R.styleable.TitleView_backPicture,R.mipmap.icon_left_return_gray);
        int backPColor = ta.getColor(R.styleable.TitleView_backPictureColor, topContentColor);

        @SuppressLint("UseCompatLoadingForDrawables") Drawable drawable = getResources().getDrawable(backP);
        backPicture.setImageResource(0);
        backPicture.setBackgroundResource(0);
        DrawableCompat.setTint(drawable,backPColor);
        backPicture.setImageDrawable(drawable);

        ta.recycle();
    }

    /**
     * 设置返回按钮监听
     *
     * @param listener
     */
    public void setLeftBackClick(OnClickListener listener) {
        findViewById(R.id.title_back).setOnClickListener(listener);
    }

    /**
     * 设置点击按钮监听
     *
     * @param listener
     */
    public void setRightTextClick(OnClickListener listener) {
        findViewById(R.id.rightLayout).setOnClickListener(listener);
    }

    /**
     * 设置返回按钮隐藏
     */
    public void setLeftBackBtnHidden() {
        findViewById(R.id.title_back).setVisibility(View.GONE);
    }

    /**
     * 设置右图标功能
     *
     * @param resId
     */
    public void setRightImageBtn(int resId) {
        ImageView rightImageBtn = (ImageView) findViewById(R.id.iv_right_picture);
        rightImageBtn.setImageResource(resId);
    }

    /**
     * 设置右文字功能
     *
     * @param btnText
     */
    public void setRightTextBtn(String btnText) {
        rightText.setText(btnText);
    }

    /**
     * 设置右文字功能和颜色
     *
     * @param btnText
     */
    public void setRightTextBtn(String btnText, int color) {
        rightText.setText(btnText);
        rightText.setTextColor(mContext.getResources().getColor(color));
    }

    /**
     * 设置右边按钮可见性
     *
     * @param visible
     */
    public void setRightBtnVisibility(boolean visible) {
        findViewById(R.id.rightLayout).setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * 设置标题文字内容
     *
     * @param title
     */
    public void setTitle(String title) {
        titleView.setText(title);
    }

    /**
     * 获取标题文字内容
     *
     * @return
     */
    public String getTitle() {
        return titleView.getText().toString();
    }

    /**
     * 获取右边文字内容
     *
     * @return
     */
    public String getRightText() {
        return rightText.getText().toString();
    }

    /**
     * 设置右边文字内容
     *
     * @param title
     */
    public void setRightTitle(String title) {
        rightText.setText(title);
    }

}
