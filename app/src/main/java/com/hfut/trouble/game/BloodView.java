package com.hfut.trouble.game;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.hfut.trouble.R;

/**
 * 最大血量决定画几个圆
 * 高度决定半径
 * 宽度决定空隙
 * <p>
 * 宽度和高度取最小的那个
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
            switch (index) {
                case R.styleable.BloodView_max_blood:
                    maxBlood = ta.getInteger(R.styleable.BloodView_max_blood, 3);
                    currentBlood = maxBlood;
                    centerX = new int[maxBlood];
                    break;
                case R.styleable.BloodView_space:
                    space = ta.getDimensionPixelSize(R.styleable.BloodView_space, 4);
                    break;
                case R.styleable.BloodView_radius:
                    radius = ta.getDimensionPixelSize(R.styleable.BloodView_radius, 10);
                    break;
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
        centerY = radius;
    }

    private int measureWidth() {
        return maxBlood * radius * 2 + space * (maxBlood - 1);
    }

    private int measureHeight() {
        return radius * 2;
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);
    }

    public void setBlood(int currentBlood) {
        currentBlood = Math.max(currentBlood, 0);
        currentBlood = Math.min(currentBlood, maxBlood);
        this.currentBlood = currentBlood;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = centerX.length - 1; i >= maxBlood - currentBlood; i--) {
            canvas.drawCircle(centerX[i], centerY, radius, mPaint);
        }
    }
}
