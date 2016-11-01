package com.victor.myprogressbar;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import org.w3c.dom.Attr;

import static android.R.attr.textSize;


public class RoundProgressBar extends View implements Runnable {

    private final static int DEFAULT_HEIGHT_DP = 150;

    private Paint paint = null;
    private Paint textPaint = null;
    private Resources res;
    private String textContent = "100%";
    // attr可配置数据
    private int textColor;
    private int progessColor;
    private int progressBgColor;
    private int strokeColor;
    private int strokeWidth;
    private float textSize;

    private float textX, textY;
    private float textLen;
    private int height;
    private int showProgress, currProgress;
    private boolean isUpdate = false;

    public RoundProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.RoundProgressBar);
        for (int i = 0; i < mTypedArray.getIndexCount(); i++) {
            int attr = mTypedArray.getIndex(i);
            if (attr == R.styleable.RoundProgressBar_text_color) {
                textColor = mTypedArray.getColor(attr, Color.WHITE);
            } else if (attr == R.styleable.RoundProgressBar_progress_color) {
                progessColor = mTypedArray.getColor(attr, Color.GRAY);
            } else if (attr == R.styleable.RoundProgressBar_stroke_color) {
                strokeColor = mTypedArray.getColor(attr, Color.BLACK);
            } else if (attr == R.styleable.RoundProgressBar_progress_background_color) {
                progressBgColor = mTypedArray.getColor(attr, Color.BLUE);
            } else if (attr == R.styleable.RoundProgressBar_text_size) {
                textSize = mTypedArray.getDimensionPixelSize(attr, 25);
            } else if (attr == R.styleable.RoundProgressBar_stroke_width) {
                strokeWidth = mTypedArray.getDimensionPixelSize(attr, 2);
            }
        }

        mTypedArray.recycle();
        init();
    }

    public RoundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressBar(Context context) {
        this(context, null);
    }

    private void init() {
        // 禁止硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        res = getResources();
        paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStyle(Style.FILL);
        paint.setStrokeWidth(strokeWidth);

        textPaint = new Paint();
        textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(textColor);

        showProgress = 0;
        currProgress = 0;
        setTextContent(textContent);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        height = 0;
        switch (heightSpecMode) {
            case MeasureSpec.AT_MOST: // 子最大可以达到的指定大小
                height = (int) dp2px(DEFAULT_HEIGHT_DP);
                break;
            case MeasureSpec.EXACTLY: // 当设置width或height为match_parent时，模式为EXACTLY
            case MeasureSpec.UNSPECIFIED: // 父不没有对子施加任何约束，子可以是任意大小
                    height = heightSpecSize;
                break;
        }
        setMeasuredDimension(widthSpecSize, height);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            setTextContent("" + showProgress + "%");
            if (showProgress < currProgress && isUpdate) {
                ++showProgress;
                handler.sendEmptyMessageDelayed(1, 20);
            } else {
                isUpdate = false;
            }
        }
    };

    private float dp2px(int dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return dp * density;
    }

    public void setTextColor(int color) {
        textColor = color;
        invalidate();
    }

    public void setTextContent(String content) {
        textContent = content;
        measureText();
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        if (textPaint.getTextAlign() == Paint.Align.LEFT) { //左
            textX = height / 2 - (int) (textLen / 2);
        } else if (textPaint.getTextAlign() == Paint.Align.CENTER) { //中
            textX = height / 2;
        } else { //右
            textX = height / 2 + (int) (textLen / 2);
        }
        textY = (height - fontMetrics.ascent - fontMetrics.descent) / 2;
        postInvalidate();
    }

    /**
     * 更新进度
     * @param progress 100制
     */
    public void updateProgress(int progress) {
        if (progress > 100) {
            this.currProgress = 100;
        } else if (currProgress < 0) {
            this.currProgress = 0;
        } else {
            this.currProgress = progress;
        }
        if (showProgress >= 100) {
            showProgress = 0;
        }
        if (!isUpdate) {
            isUpdate = true;
            handler.sendEmptyMessageDelayed(1, 10);
        }
    }

    public void clearProgress() {
        currProgress = 0;
        showProgress = 0;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 每次清除下
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        paint.setStyle(Style.STROKE);
        paint.setColor(strokeColor);
        canvas.drawCircle(height / 2, height /2, (height - strokeWidth) / 2, paint); // 外框圆
        // 扇形进度
        paint.setStyle(Style.FILL);
        paint.setColor(progessColor);
        canvas.drawArc(new RectF(strokeWidth, strokeWidth, height- strokeWidth, height - strokeWidth),
                0, showProgress * 360 / 100, true, paint); // 扇形进度
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        paint.setColor(progressBgColor);
        canvas.drawCircle(height / 2, height /2, (height - 30) / 2, paint); // 内圆
        paint.setStyle(Style.STROKE);
        paint.setColor(strokeColor);
        canvas.drawCircle(height / 2, height /2, (height - 30) / 2, paint); // 内圆框
        // 绘制文本
        if (textX > 0) { // 避免初始化绘制在原点
            canvas.drawText(textContent, textX, textY, textPaint);
        }
    }

    private void measureText() {
        textLen = textPaint.measureText(textContent);
    }

    @Override
    public void run() {

    }
}
