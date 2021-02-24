package com.dozen.commonbase.view.wheel.base;

/**
 * href="https://github.com/Dozen729"
 * @author Dozen729
 */
public class WheelItem implements IWheel {

    String label;

    public WheelItem(String label) {
        this.label = label;
    }

    @Override
    public String getShowText() {
        return label;
    }
}
