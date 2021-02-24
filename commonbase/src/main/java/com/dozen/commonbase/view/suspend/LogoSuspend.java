package com.dozen.commonbase.view.suspend;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.dozen.commonbase.R;

/**
 * Created by Dozen on 19-7-25.
 * Describe:悬浮窗控件
 */
public class LogoSuspend extends BaseSuspend {

    /**
     * ui
     */
    private ImageView ivLogo;
    /**
     * 变量
     */
    private int width, height;
    private float mStartX, mStartY, mStopX, mStopY, touchStartX, touchStartY;
    private long touchStartTime;
    /**
     * 接口
     */
    private View.OnClickListener onClickListener;

    public LogoSuspend(Context context) {
        super(context);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.suspend_logo;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView() {
        ivLogo = findView(R.id.iv_logo);
        ivLogo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int action = event.getAction();
                mStopX = event.getRawX();
                mStopY = event.getRawY();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // 以当前父视图左上角为原点
                        mStartX = event.getRawX();
                        mStartY = event.getRawY();
                        touchStartX = event.getRawX();
                        touchStartY = event.getRawY();
                        touchStartTime = getTimeForLong();//获取当前时间戳
                        break;
                    case MotionEvent.ACTION_MOVE:
                        width = (int) (mStopX - mStartX);
                        height = (int) (mStopY - mStartY);
                        mStartX = mStopX;
                        mStartY = mStopY;
                        updateSuspend(width, height);
                        break;
                    case MotionEvent.ACTION_UP:
                        width = (int) (mStopX - mStartX);
                        height = (int) (mStopY - mStartY);
                        updateSuspend(width, height);
                        WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) getView().getLayoutParams();
                        SuspensionCache.setSuspendSize(new SizeEntity(layoutParams.x + width, layoutParams.y + height));//缓存一下当前位置
                        if ((mStopX - touchStartX) < 30 && (mStartY - touchStartY) < 30 && (getTimeForLong() - touchStartTime) < 300) {
                            //左右上下移动距离不超过30的，并且按下和抬起时间少于300毫秒，算是单击事件，进行回调
                            if (onClickListener != null) {
                                onClickListener.onClick(view);
                            }
                        }
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onCreateSuspension() {

    }

    private long getTimeForLong(){
        return System.currentTimeMillis();
    }

    /**
     * 设置点击监听
     *
     * @param onClickListener
     */
    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
