package com.talon.bubbleviewdemo;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BubbleView extends View
{
    public static final int MAX_FRAME_INDEX = 1000;

    private List<Bubble> bubbleList;

    private Random random;

    private BubbleFactory bubbleFactory;

    private ObjectAnimator animation;

    private int frameIndex, frameCount;

    private boolean bubbleFromCenter;

    public BubbleView(Context context)
    {
        super(context);
        init(context);
    }

    public BubbleView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    private void init(Context context)
    {
        this.bubbleFromCenter = true;
        this.bubbleList = new ArrayList<>();
        this.random = new Random();
        this.animation = ObjectAnimator.ofFloat(this, "animationPercent", 0, 1);
        animation.setDuration(MAX_FRAME_INDEX * 10);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(ValueAnimator.INFINITE);
        animation.setRepeatMode(ValueAnimator.RESTART);
        animation.setStartDelay(100);
        this.setWillNotDraw(false);
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility)
    {
        super.onVisibilityChanged(changedView, visibility);

        if(visibility == VISIBLE)
        {
            if(!animation.isStarted())
            {
                animation.start();
            }
            else if(animation.isPaused())
            {
                animation.resume();
            }
        }
        else if(animation.isStarted())
        {
            animation.pause();
        }
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        animation.start();
    }

    @Override
    protected void onDetachedFromWindow()
    {
        animation.cancel();
        super.onDetachedFromWindow();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        if(bubbleList != null && !bubbleList.isEmpty())
        {
            Drawable drawable;

            for(Bubble bubble: bubbleList)
            {
                drawable = bubble.getDrawable();
                drawable.setBounds(bubble.getLeft(), bubble.getTop(), bubble.getRight(), bubble.getBottom());
                drawable.setAlpha(bubble.getAlpha());
                drawable.draw(canvas);
            }
        }
    }

    private void move()
    {
        int width = this.getWidth();
        int height = this.getHeight();

        if(width == 0 || height == 0)
        {
            return;
        }

        int bubbleCount = bubbleList.size();
        Bubble bubble;

        for(int i = bubbleCount - 1; i >= 0; i--)
        {
            bubble = bubbleList.get(i);
            bubble.next();

            if(bubble.isDisappeared())
            {
                bubbleList.remove(i);
            }
        }

        if(bubbleFactory != null)
        {
            bubble = bubbleFactory.createBubble(random, frameIndex, MAX_FRAME_INDEX, frameCount);

            if(bubble != null)
            {
                bubble.init(width, height, bubbleFromCenter);
                bubbleList.add(bubble);
            }
        }

        this.invalidate();
    }

    /**
     * 动画专用
     */
    @Deprecated
    private void setAnimationPercent(float animationPercent)
    {
        int index = (int)(frameIndex + MAX_FRAME_INDEX * animationPercent) % MAX_FRAME_INDEX;

        if(this.frameIndex != index)
        {
            frameCount++;

            if(frameCount >= MAX_FRAME_INDEX)
            {
                frameCount = 0;
            }

            this.frameIndex = index;
            move();
        }
    }

    public boolean isBubbleFromCenter()
    {
        return this.bubbleFromCenter;
    }

    public void setBubbleFromCenter(boolean bubbleFromCenter)
    {
        this.bubbleFromCenter = bubbleFromCenter;
    }

    public BubbleFactory getBubbleFactory()
    {
        return this.bubbleFactory;
    }

    public void setBubbleFactory(@NonNull BubbleFactory bubbleFactory)
    {
        this.bubbleFactory = bubbleFactory;
    }

    public interface BubbleFactory
    {
        Bubble createBubble(Random random, int frameIndex, int maxFrameIndex, int frameCount);
    }
}
