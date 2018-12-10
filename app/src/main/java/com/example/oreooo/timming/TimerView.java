package com.example.oreooo.timming;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * @author Oreo https://github.com/OreoChap
 * @date 2018/12/7
 */
public class TimerView extends FrameLayout {

    public TimerView(Context context) {
        super(context);
        initView(context);
    }

    public TimerView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    private void initView(Context context) {
        inflate(context, R.layout.view_timer, this);
    }
}
