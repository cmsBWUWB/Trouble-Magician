package com.hfut.trouble.game;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.hfut.trouble.R;

/**
 * 最大血量决定画几个圆
 */
public class BloodView extends View {
    private static final String TAG = BloodView.class.getSimpleName();

    private Paint mPaint;
    private int maxBlood = 3;
    private int space = 4;
    private int radius = 10;

    private int currentBlood = 3;
    private int[] centerX = new int[3];
    private int centerY;

    public BloodView(Context context) {
        super(context);
        initPaint();
    }

    public BloodView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initPaint();

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BloodView);

        int count = ta.getIndexCount();
        for (int i = 0; i < count; i++) {
            int index = ta.getIndex(i);
            if (index == R.styleable.BloodView_max_blood) {
                maxBlood = ta.getInteger(R.styleable.BloodView_max_blood, 3);
                currentBlood = maxBlood;
                centerX = new int[maxBlood];
            } else if (index == R.styleable.BloodView_space) {
                space = ta.getDimensionPixelSize(R.styleable.BloodView_space, 4);
            } else if (index == R.styleable.BloodView_radius) {
                radius = ta.getDimensionPixelSize(R.styleable.BloodView_radius, 10);
            }
        }
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = measureWidth();
        int measureHeight = measureHeight();

        int width = measureWidth + getPaddingStart() + getPaddingEnd();
        int height = measureHeight + getPaddingTop() + getPaddingBottom();

        setMeasuredDimension(width, height);

        for (int i = 0; i < centerX.length; i++) {
            centerX[i] = radius + i * (radius * 2 + space);
        }
        centerY = (int) (radius * 1.5);
    }

    private int measureWidth() {
        return maxBlood * radius * 2 + space * (maxBlood - 1);
    }

    private int measureHeight() {
        return radius * 3;
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);
    }

    ObjectAnimator objectAnimator;

    public int getCurrentBlood(){
        return currentBlood;
    }

    public void setCurrentBlood(int currentBlood) {
        if(objectAnimator != null){
            objectAnimator.end();
        }

        currentBlood = Math.max(currentBlood, 0);
        currentBlood = Math.min(currentBlood, maxBlood);

        changeBlood = currentBlood - this.currentBlood;
        this.currentBlood = currentBlood;

        if(changeBlood < 0) {
            //如果是扣血
            objectAnimator = ObjectAnimator.ofFloat(this, "shake", 0, 1, -1, 0);
            objectAnimator.setRepeatMode(ValueAnimator.RESTART);
            objectAnimator.setRepeatCount(6);
            objectAnimator.setDuration(50);
            objectAnimator.start();
            objectAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    changeBlood = 0;
                    invalidate();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    changeBlood = 0;
                    invalidate();
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
        }else if(changeBlood > 0){
            //如果是加血
            objectAnimator = ObjectAnimator.ofFloat(this, "bloodAlpha", 0, 1);
            objectAnimator.setDuration(500);
            objectAnimator.start();
            objectAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    changeBlood = 0;
                    invalidate();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    changeBlood = 0;
                    invalidate();
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
    }

    private int changeBlood;
    private float shake;
    private float bloodAlpha;

    public void setShake(float shake){
        this.shake = shake;
        invalidate();
    }
    public void setBloodAlpha(float bloodAlpha){
        this.bloodAlpha = bloodAlpha;
        invalidate();
    }

    /**
     * 抖动绘制
     */
    private void drawShake(Canvas canvas, int startIndex, int endIndex){
        for(int i = startIndex; i <= endIndex; i++){
            canvas.drawCircle(centerX[i], centerY + (int)(shake * radius / 2), radius, mPaint);
        }
    }

    /**
     * 加血绘制
     */
    private void drawAddBlood(Canvas canvas, int startIndex, int endIndex){
        mPaint.setAlpha((int) (255 * bloodAlpha));
        for(int i = startIndex; i <= endIndex; i++){
            canvas.drawCircle(centerX[i], centerY, radius, mPaint);
        }
        mPaint.setAlpha(255);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int startIndex = 0, endIndex = 0;
        if(changeBlood < 0) {
            startIndex = maxBlood - currentBlood;
            endIndex = maxBlood - 1;
        }else if(changeBlood > 0){
            startIndex = maxBlood - currentBlood + changeBlood;
            endIndex = maxBlood - 1;
        }else{
            startIndex = maxBlood - currentBlood;
            endIndex = maxBlood - 1;
        }

        for (int i = startIndex; i <= endIndex; i++) {
            canvas.drawCircle(centerX[i], centerY, radius, mPaint);
        }

        if(changeBlood < 0){
            drawShake(canvas, startIndex + changeBlood, startIndex - 1);
        }else if(changeBlood > 0){
            drawAddBlood(canvas, startIndex - changeBlood, startIndex - 1);
        }
    }
}