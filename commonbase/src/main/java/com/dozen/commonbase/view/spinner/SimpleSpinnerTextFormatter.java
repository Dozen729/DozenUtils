package com.dozen.commonbase.view.spinner;

import android.text.Spannable;
import android.text.SpannableString;

public class SimpleSpinnerTextFormatter implements SpinnerTextFormatter<Object> {

    @Override
    public Spannable format(Object item) {
        return new SpannableString(item.toString());
    }
}
