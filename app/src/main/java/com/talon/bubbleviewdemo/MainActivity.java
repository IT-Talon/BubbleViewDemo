package com.talon.bubbleviewdemo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements BubbleView.BubbleFactory {
    int[] bubbleResIds;
    int[] smallBubbleResIds;
    int[] colorBubbleResIds;

    private BubbleView mBubbleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBubbleView = (BubbleView) findViewById(R.id.bubble_View);
        mBubbleView.setBubbleFactory(this);
    }

    public Bubble createSmallBubble(Context context, Random random, int frameCount) {
        Bubble bubble = null;

        if (smallBubbleResIds == null) {
            smallBubbleResIds = new int[7];

            for (int i = 0; i < smallBubbleResIds.length; i++) {
                smallBubbleResIds[i] = context.getResources().getIdentifier("small_bubble_" + (i + 1), "drawable", context.getPackageName());
            }
        }

        if (frameCount % 5 == 0) {
            int drawableIndex = random.nextInt(7);
            bubble = new Bubble(random, ResourceUtil.getDrawable(context, smallBubbleResIds[drawableIndex]));
        }

        return bubble;
    }

    public Bubble createColorBubble(Context context, Random random, int frameCount) {
        Bubble bubble = null;

        if (colorBubbleResIds == null) {
            colorBubbleResIds = new int[6];

            for (int i = 0; i < colorBubbleResIds.length; i++) {
                colorBubbleResIds[i] = context.getResources().getIdentifier("color_bubble_" + (i + 1), "drawable", context.getPackageName());
            }
        }

        if (frameCount % 30 == 0) {
            int drawableIndex = random.nextInt(6);
            bubble = new Bubble(random, ResourceUtil.getDrawable(context, colorBubbleResIds[drawableIndex]));
        }

        return bubble;
    }

    @Override
    public Bubble createBubble(Random random, int frameIndex, int maxFrameIndex, int frameCount) {
        Bubble bubble = null;

        if (bubbleResIds == null) {
            bubbleResIds = new int[13];

            for (int i = 0; i < bubbleResIds.length; i++) {
                bubbleResIds[i] = getResources().getIdentifier("bubble_" + (i + 1), "drawable", getPackageName());
            }
        }

        if (frameCount % 25 == 0 && random.nextBoolean()) {
            int drawableIndex;

            if (frameCount % 500 == 0 && random.nextBoolean()) {
                drawableIndex = random.nextInt(6);
            } else {
                drawableIndex = 6 + random.nextInt(7);
            }

            bubble = new Bubble(random, ResourceUtil.getDrawable(this, bubbleResIds[drawableIndex]));
        }

        return bubble;
    }
}
