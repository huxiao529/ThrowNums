package com.l99.thrownums;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * @author Lifeix
 *         created on 2016/6/20
 */
public class DiyLinearLayout extends LinearLayout {
    public DiyLinearLayout(Context context) {
        super(context);
        init(context);
    }

    public DiyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DiyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        params.height = 0;
        params.weight = 1;
        //初始化,添加3个imageView
        for (int i = 0; i < 3; i++) {
            DiyImageView diyImageView = new DiyImageView(context);
            diyImageView.setLayoutParams(params);
            addView(diyImageView);
        }
    }

    public DiyLinearLayout showPosition(int position){
        setMyVisibility(position, View.VISIBLE);
        return this;
    }

    public DiyLinearLayout gonePosition(int position){
        setMyVisibility(position, View.GONE);
        return this;
    }

    public DiyLinearLayout hidePosition(int position){
        setMyVisibility(position, View.INVISIBLE);
        return this;
    }

    private void setMyVisibility(int position, int visibility) {
        int childCount = getChildCount();
        if(position >= 0 && position < childCount){
            getChildAt(position).setVisibility(visibility);
        }
        Log.i("DiyLinearLayout", "setMyVisibility: " + position);
    }
}
