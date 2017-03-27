package com.talon.bubbleviewdemo;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.Random;

public class Bubble implements Serializable
{
    private static final long serialVersionUID = 4942657247430406464L;

    private final Random random;

    private final Drawable drawable;

    private float viewWidth, viewHeight, drawableWidth, drawableHeight;

    private float x, y, speedX, speedY, finalSpeedX, accelerationX;

    private boolean toRight, bubbleFromCenter;

    private int nextCount;

    public Bubble(Random rand, Drawable drawable)
    {
        this.random = rand;
        this.drawable = drawable;
    }

    public void init(int viewWidth, int viewHeight, boolean bubbleFromCenter)
    {
        this.bubbleFromCenter = bubbleFromCenter;
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        this.drawableWidth = drawable.getIntrinsicWidth();
        this.drawableHeight = drawable.getIntrinsicHeight();
        float deltaX = viewWidth / 6f;

        if(bubbleFromCenter)
        {
            this.x = ((viewWidth - drawableWidth) / 2f - deltaX) + (deltaX * 2 * random.nextFloat());
        }
        else
        {
            this.x = random.nextInt((int)(viewWidth - drawableWidth)) + drawableWidth / 2f;
        }

        this.y = viewHeight - drawableHeight;
        this.toRight = random.nextBoolean();
        this.speedX = random.nextFloat() / 1.2f;
        this.speedY = 0.4f + random.nextFloat() / 1.67f;
        this.finalSpeedX = speedX;
    }

    public void next()
    {
        boolean willReverse = nextCount > 3000 + random.nextInt(3000) && random.nextBoolean() == toRight;

        if(speedX < finalSpeedX)
        {
            speedX += accelerationX;

            if(speedX > finalSpeedX)
            {
                speedX = finalSpeedX;
            }
        }
        else if(x < 0 || x > viewWidth - drawableWidth || (willReverse && speedX == finalSpeedX))
        {
            toRight = !toRight;
            finalSpeedX = random.nextFloat() / 1.2f;
            accelerationX = finalSpeedX / 200f;
            speedX = 0;
            nextCount = 0;
        }

        x += speedX * (toRight? 1: -1);
        y -= speedY;
        nextCount++;

        if(nextCount > 1000)
        {
            nextCount = 0;
        }
    }

    public boolean isDisappeared()
    {
        return x <= -drawableWidth || x >= viewWidth || y <= -drawableHeight;
    }

    public int getAlpha()
    {
        float alpha = 1;

        if(y > viewHeight - drawableHeight * 2)
        {
            alpha = Math.max(0, 1 - (y - (viewHeight - drawableHeight * 2)) / drawableHeight);
        }
        else if(y < drawableHeight / 2)
        {
            alpha = Math.max(0, 1 - (drawableHeight / 2 - y) / drawableHeight);
        }

        return (int)(alpha * 255);
    }

    public Drawable getDrawable()
    {
        return this.drawable;
    }

    public int getLeft()
    {
        return (int)this.x;
    }

    public int getRight()
    {
        return (int)(this.x + this.drawableWidth);
    }

    public int getTop()
    {
        return (int)this.y;
    }

    public int getBottom()
    {
        return (int)(this.y + this.drawableHeight);
    }
}