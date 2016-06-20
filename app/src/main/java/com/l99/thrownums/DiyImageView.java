package com.l99.thrownums;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.Random;

/**
 * @author Lifeix
 *         created on 2016/6/20
 */
public class DiyImageView extends ImageView {
    public DiyImageView(Context context) {
        super(context);
        loadDefaultImage(context);
    }

    private void loadDefaultImage(Context context) {
        int i = new Random().nextInt(10);
        if(i>0){
            loadMyImage(context, R.mipmap.bg1);
        }else{
            loadMyImage(context, R.mipmap.bg2);
        }
    }

    private void loadMyImage(Context context, int id) {
        Glide.with(context).load(id).transform(new GlideCircleTransform(context)).into(this);
    }

    public DiyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadDefaultImage(context);
    }

    public DiyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadDefaultImage(context);
    }

}
