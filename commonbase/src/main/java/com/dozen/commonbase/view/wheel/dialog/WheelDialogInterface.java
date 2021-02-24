package com.dozen.commonbase.view.wheel.dialog;

import com.dozen.commonbase.view.wheel.base.IWheel;

public interface WheelDialogInterface<T extends IWheel> {

    boolean onClick(int witch, int selectedIndex, T item);
}