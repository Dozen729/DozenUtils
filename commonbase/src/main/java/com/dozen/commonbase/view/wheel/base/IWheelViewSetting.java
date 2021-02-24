package com.dozen.commonbase.view.wheel.base;

import androidx.annotation.ColorInt;

/**
 * href="https://github.com/Dozen729"
 * @author Dozen729
 */
public interface IWheelViewSetting {

    void setTextSize(float textSize);

    void setTextColor(@ColorInt int textColor);

    void setShowCount(int showCount);

    void setTotalOffsetX(int totalOffsetX);

    void setItemVerticalSpace(int itemVerticalSpace);

    void setItems(IWheel[] items);

    int getSelectedIndex();

    void setSelectedIndex(int targetIndexPosition);

    void setSelectedIndex(int targetIndexPosition, boolean withAnimation);

    void setOnSelectedListener(WheelView.OnSelectedListener onSelectedListener);

    boolean isScrolling();
}
