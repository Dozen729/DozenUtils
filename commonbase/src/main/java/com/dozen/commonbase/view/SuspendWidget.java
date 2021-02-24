package com.dozen.commonbase.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author: Dozen
 * @description:界面中可移动组件
 * @time: 2020/11/25
 */
public class SuspendWidget {

    /*
    * layout组件结构
    * <LinearLayout>--mainFrame
    *   <LinearLayout>--boxLocation
    *     <LinearLayout>--widgetFrame
    *       <view></view>--scrollFrame
    *       <LinearLayout></LinearLayout>--other view
    *     </LinearLayout>
    *   </LinearLayout>
    * </LinearLayout>
    * */

    /**
     * ui
     */
    private int mainFrame;//移动区域
    private int widgetFrame;//控件大小
    private int scrollFrame;//移动控件
    private View view; //可以通过id获取控件的对象
    /**
     * 变量
     */
    private int width, height;
    private int totalWidth,totalHeight;
    private float mStartX, mStartY, mStopX, mStopY, touchStartX, touchStartY;
    private long touchStartTime;
    private Context context;
    /**
     * 接口
     */
    private View.OnClickListener onClickListener;

    public SuspendWidget(Context context,View view) {
        this.context =context;
        this.view=view;

        totalWidth=-1;
        totalHeight=-1;
    }

    //控制可以移动的区域
    public void setMainFrame(int mainFrame) {
        this.mainFrame = mainFrame;
    }

    //获取控件大小
    public void setWidgetFrame(int widgetFrame) {
        this.widgetFrame = widgetFrame;
    }

    //按住时可移动的控件
    public void setScrollFrame(int scrollFrame) {
        this.scrollFrame = scrollFrame;
        initView(scrollFrame);
    }

    /**
     * 根据id快速找到控件
     *
     * @param id
     * @param <E>
     * @return
     */
    public final <E extends View> E findView(int id) {
        try {
            return (E) view.findViewById(id);
        } catch (ClassCastException ex) {
            throw ex;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView(int id) {
        findView(id).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int action = event.getAction();

                if (totalWidth==-1){
                    initTotalValue();
                }

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

                        width = -(int) (mStopX - mStartX);
                        height = -(int) (mStopY - mStartY);
                        mStartX = mStopX;
                        mStartY = mStopY;
                        totalWidth+=width;
                        totalHeight+=height;
                        if (isMove()){
                            findView(mainFrame).scrollBy(width,height);
                        }else {
                            totalWidth-=width;
                            totalHeight-=height;
                        }
                        break;
                    case MotionEvent.ACTION_UP:

                        width = -(int) (mStopX - mStartX);
                        height = -(int) (mStopY - mStartY);
                        totalWidth+=width;
                        totalHeight+=height;
                        if (isMove()){
                            findView(mainFrame).scrollBy(width,height);
                        }else {
                            totalWidth-=width;
                            totalHeight-=height;
                        }
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

    //初始化控件所在屏幕的坐标
    private void initTotalValue(){
        totalWidth=getMainWidth()-findView(widgetFrame).getRight();
        totalHeight=getMainHeight()-findView(widgetFrame).getBottom();
    }

    //判断控件是否到达边界，为true时没有到达
    private boolean isMove(){
        return totalWidth + getWidgetWidth() <= getMainWidth() && totalHeight + getWidgetHeight() <= getMainHeight() && totalWidth >= 0 && totalHeight >= 0;
    }

    private int getMainWidth(){
        return findView(mainFrame).getWidth();
    }

    private int getMainHeight(){
        return findView(mainFrame).getHeight();
    }

    private int getWidgetWidth(){
        return findView(widgetFrame).getWidth();
    }

    private int getWidgetHeight(){
        return findView(widgetFrame).getHeight();
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

    public void setOnClickListener(int id,View.OnClickListener onClickListener){
        findView(id).setOnClickListener(onClickListener);
    }
}
