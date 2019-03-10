package com.example.oreooo.timming;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Oreo https://github.com/OreoChap
 * @date 2018/12/4
 */

public class TimingView extends View {

    Context mContext;
    //钟表背景画笔
    Paint mBackGroundPaint = new Paint();

    //钟表框画笔
    Paint mPaint;

    //钟表时间选择背景画笔
    Paint mTimingPaint = new Paint();

    //钟表时间选择边框画笔
    Paint mGetTimingPaint = new Paint();

    //圆心画笔
    Paint mCentreCirlePaint = new Paint();

    //钟表刻度画笔
    Paint mDegreeScalePaint = new Paint();

    //钟表数字画笔
    Paint mDegreeNumberPaint = new Paint();

    //屏幕宽高
    private int mScreenWidth, mScreenHeight;

    //钟表点击区域
    Region mCircleRegion;
    Path mCirclePath;

    //钟表时间选择的坐标
    private int mTimingX, mTimingY;

    //钟表时间旋转角度
    float clockDegree = 0;

    //1、4象限的旋转角度处理
    float Degree = 0;

    //钟表半径radius
    float mRadius;

    //钟表计时状态
    boolean IsTiming = false;

    //是否初始化View
    boolean mFirstInitView = true;

    //圆心坐标
    float centreCircleX, centreCircleY;

    private OnButtonClickListener mListener;


    //TODO 在最上面增加显示的时间
    public TimingView(Context context) {
        this(context, null);
    }

    public TimingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        mBackGroundPaint.setStyle(Paint.Style.FILL);
        mBackGroundPaint.setColor(getResources().getColor(R.color.colorClockBackGround));

        mGetTimingPaint.setStyle(Paint.Style.FILL);
        mGetTimingPaint.setColor(getResources().getColor(R.color.colorSetTime));
        mGetTimingPaint.setStrokeWidth(5f);

        mTimingPaint.setColor(getResources().getColor(R.color.colorSumTime));

        mCentreCirlePaint.setStyle(Paint.Style.FILL);
        mCentreCirlePaint.setColor(getResources().getColor(R.color.colorSetTime));
        mCentreCirlePaint.setStrokeWidth(8f);

        mDegreeScalePaint.setColor(getResources().getColor(R.color.colorSetTime));
        mDegreeScalePaint.setStrokeWidth(5f);

        mDegreeNumberPaint.setStyle(Paint.Style.FILL);
        mDegreeNumberPaint.setColor(getResources().getColor(R.color.colorSetTime));
        mDegreeNumberPaint.setTextSize(25);

        mCircleRegion = new Region();
        mCirclePath = new Path();
    }

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.mListener = listener;
    }

    public interface OnButtonClickListener {
        void onButtonClick();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centreCircleX = w / 2;
        centreCircleY = h / 3;
        mRadius = w / 3;
        mCirclePath.addCircle(centreCircleX, centreCircleY, mRadius, Path.Direction.CW);
        Region region = new Region(-w, -h, w, h);
        mCircleRegion.setPath(mCirclePath, region);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制基本图形
        canvas.translate(centreCircleX, centreCircleY);
        canvas.drawCircle(0, 0, mRadius, mBackGroundPaint);

        //判断View的状态
        canvas.save();
        if (!IsTiming && mFirstInitView) {
            canvas.drawArc(-mRadius, -mRadius, mRadius, mRadius,
                    -90, 150, true, mTimingPaint);
            canvas.drawLine(0, 0, 0, -mRadius, mGetTimingPaint);
            canvas.rotate(150);
            canvas.drawLine(0, 0, 0, -mRadius, mGetTimingPaint);
            clockDegree = 150f;
            mFirstInitView = false;
        } else if (!IsTiming) {
            canvas.drawArc(-mRadius, -mRadius, mRadius, mRadius,
                    -90, (float) clockDegree, true, mTimingPaint);
            canvas.drawLine(0, 0, 0,  - mRadius, mGetTimingPaint);
            canvas.rotate((float) clockDegree);
            canvas.drawLine(0, 0, 0,  - mRadius, mGetTimingPaint);
        }else if (0 < clockDegree) {
            canvas.drawArc(-mRadius, -mRadius, mRadius, mRadius,
                    -90, (float) clockDegree, true, mTimingPaint);
            canvas.drawLine(0, 0, 0,  - mRadius, mGetTimingPaint);
            canvas.rotate((float) clockDegree);
            canvas.drawLine(0, 0, 0,  - mRadius, mGetTimingPaint);
        } else if (0 > clockDegree) {
            canvas.drawLine(0, 0, 0,  - mRadius, mGetTimingPaint);
            Toast.makeText(mContext, "计时完毕" , Toast.LENGTH_SHORT).show();
            IsTiming = false;
        }
        canvas.restore();

        //钟表刻度
        canvas.save();
        for (int i = 0; i < 12; i++){
            if (i % 3 == 0) {
                canvas.drawLine(0, -mRadius, 0, - mRadius + 40f, mDegreeScalePaint);
            } else {
                canvas.drawLine(0, -mRadius, 0, - mRadius + 15f, mDegreeScalePaint);
            }
            canvas.rotate(30f);
        }
        canvas.restore();

        //钟表数字
        for (int i = 1; i < 13; i++) {
            canvas.save();
            drawNumbers(canvas, i + "", mDegreeNumberPaint, i);
            canvas.restore();
        }

        canvas.drawPoint(0, 0, mCentreCirlePaint);
    }

    private void drawNumbers(Canvas canvas, String text, Paint paint, int i) {
        canvas.rotate(i * 30f);
        canvas.translate(0, - mRadius + 60f);
        canvas.rotate(- i * 30);
        Rect textBound = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBound);
        canvas.drawText(text, -textBound.width() / 2, textBound.height() / 2, paint);
    }

    // TODO 限制1、4象限之间的触摸
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (IsTiming) return true;
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mCircleRegion.contains(x, y)) {
                    computeAngel(x, y);
                    invalidate();
                }
                return true;
             case MotionEvent.ACTION_MOVE:
                 if (mCircleRegion.contains(x, y)) {
                     computeAngel(x, y);
                     invalidate();
                 }
        }
        return true;
    }

    /**计算触摸屏幕时的角度
     *
     * @param x  坐标原点为左上角时，X 轴触摸坐标
     * @param y  坐标原点为左上角时，Y 轴触摸坐标
     */
    private void computeAngel(int x, int y) {
        //计算坐标原点为圆心时的 x、y坐标
        int Y = y - getHeight() / 3;
        int X = x - getWidth() / 2;

        float degree = (float)Math.toDegrees(Math.atan2(Math.abs(Y), Math.abs(X)));
        //判断象限
        if (X > 0 && Y > 0) {
            clockDegree = 90 + degree;
        } else if (X < 0 && Y > 0) {
            clockDegree = 270 - degree;
        } else if (X < 0 && Y < 0) {
            clockDegree = 270 + degree;
        } else if (X > 0 && Y < 0) {
            clockDegree = 90 - degree;
        }
        Degree = clockDegree;
    }

    //初始化计时器，进行计时
    public void initClockTiming() {
        if (IsTiming) {
            Toast.makeText(mContext, "正在计时", Toast.LENGTH_LONG).show();
            return;
        } else {
            Toast.makeText(mContext, "开始计时", Toast.LENGTH_LONG).show();
        }

        final Timer mTimer = new Timer();
        final TimerTask mTimerTask = new TimerTask() {
            @Override
            public void run() {
                clockDegree = clockDegree - 0.01f;
                if (0 < clockDegree) {
                    postInvalidate();
                } else if (0 > clockDegree){
                    postInvalidate();
                    mTimer.cancel();
                }
            }
        };
        IsTiming = true;
        if (0 != clockDegree) {
            mTimer.schedule(mTimerTask, 0, 100);
        } else {
            Toast.makeText(mContext, "请选择时间", Toast.LENGTH_SHORT).show();
        }
    }
}

