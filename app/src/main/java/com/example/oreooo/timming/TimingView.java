package com.example.oreooo.timming;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.LayoutInflater;
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
    Canvas mCanvas;
    //钟表背景画笔
    Paint mBackGroundPaint = new Paint();

    //钟表框画笔
    Paint mPaint;

    //钟表时间选择背景画笔
    Paint mTimingPaint = new Paint();


    //钟表时间选择边框画笔
    Paint mGetTimingPaint = new Paint();


    //屏幕宽高
    private int mScreenWidth, mScreenHeight;


    //钟表点击区域
    Region mCircleRegion;
    Path mCirclePath;

    //钟表时间选择的坐标
    private int mTimingX, mTimingY;

    //钟表时间旋转角度
    float clockDegree = 0;

    //钟表半径radius
    float mRadius;

    //钟表计时状态
     private enum clockStatus {
         TIMING(1), NOT_TIMING(2);
         int status;
         clockStatus(int status){this.status = status;}

         static clockStatus getStatus(int i) {
             for (clockStatus status: clockStatus.values()) if (i == status.status) return status;
             return NOT_TIMING;
         }
    }
    boolean IsTiming = false;

     //钟表计时器
   // Timer mTimer;
  //  TimerTask mTimerTask;


    //
    boolean mFirstInitView = true;

    private OnButtonClickListener mListener;


    public TimingView(Context context) {
        super(context);
        mContext = context;
    }

    public TimingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        mBackGroundPaint.setStyle(Paint.Style.FILL);
        mBackGroundPaint.setColor(getResources().getColor(R.color.colorClockBackGround));

        mCircleRegion = new Region();
        mCirclePath = new Path();

        mGetTimingPaint.setStyle(Paint.Style.FILL);
        mGetTimingPaint.setColor(getResources().getColor(R.color.colorSetTime));
        mGetTimingPaint.setStrokeWidth(2f);

        mTimingPaint.setColor(getResources().getColor(R.color.colorSumTime));

    }

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.mListener = listener;
    }

    public interface OnButtonClickListener {
        void onButtonClick();
    }

    private void startTiming(Context context) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mScreenWidth = w;
        mScreenHeight = h;
        mCirclePath.addCircle(w / 2, h / 3, mScreenWidth / 3, Path.Direction.CW);
        Region region = new Region(-w, -h, w, h);
        mCircleRegion.setPath(mCirclePath, region);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(mScreenWidth / 2, mScreenHeight / 3);

        mRadius = mScreenWidth / 3;

        canvas.drawCircle(0, 0, mRadius, mBackGroundPaint);


        //判断View的状态
        if (!IsTiming && mFirstInitView) {
            canvas.drawArc(-mRadius, -mRadius, mRadius, mRadius,
                    -90, 150, true, mTimingPaint);
            canvas.drawLine(0, 0, 0, -mRadius, mGetTimingPaint);
            canvas.rotate(150);
            canvas.drawLine(0, 0, 0, -mRadius, mGetTimingPaint);
            mFirstInitView = false;
        } else if (!IsTiming) {
            canvas.drawArc(-mRadius, -mRadius, mRadius, mRadius,
                    -90, (float) clockDegree, true, mTimingPaint);

            canvas.drawLine(0, 0, 0,  - mRadius, mGetTimingPaint);

            canvas.rotate((float) clockDegree);

            canvas.drawLine(0, 0, 0,  - mRadius, mGetTimingPaint);


        }else if (0 < clockDegree && IsTiming) {
            canvas.drawArc(-mRadius, -mRadius, mRadius, mRadius,
                    -90, (float) clockDegree, true, mTimingPaint);

            canvas.drawLine(0, 0, 0,  - mRadius, mGetTimingPaint);

            canvas.rotate((float) clockDegree);

            canvas.drawLine(0, 0, 0,  - mRadius, mGetTimingPaint);
        } else if (0 > clockDegree && IsTiming) {
            canvas.drawLine(0, 0, 0,  - mRadius, mGetTimingPaint);
            Toast.makeText(mContext, "计时完毕" , Toast.LENGTH_SHORT).show();
            IsTiming = false;
        }
    }

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
    }

    //初始化计时器
    public void initClockTiming() {
        if (IsTiming) {
            Toast.makeText(mContext, "正在计时", Toast.LENGTH_LONG).show();
            return;
        }

        final Timer mTimer = new Timer();
        final TimerTask mTimerTask = new TimerTask() {
            @Override
            public void run() {
                clockDegree = clockDegree - 2f;
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
            mTimer.schedule(mTimerTask, 0, 1000);
        } else {
            Toast.makeText(mContext, "请选择时间", Toast.LENGTH_SHORT).show();

        }
    }
}

