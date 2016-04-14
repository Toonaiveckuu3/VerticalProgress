package com.telchina.verticalprogresstool.partstepview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.telchina.verticalprogresstool.R;


/**
 * 垂直排列的步骤指示器
 * Created by GISirFive on 2016-4-13.
 */
public class PartStepView extends View {

    private static final String TAG = PartStepView.class.getSimpleName();

    /**线型，以圆点为界，分为上下两条线*/
    public enum LINES{
        BOTH(0), UP(1), DOWN(2), NONE(3);

        private int value;

        LINES(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 实心圆画笔
     */
    private Paint mCirclePaint;
    /**
     * 环形画笔
     */
    private Paint mRingPaint;
    /**
     * 连接线画笔
     */
    private Paint mLinePaint;

    /**
     * 环形颜色透明度
     */
    private float mRingAlpha = 0.25f;

    /**
     * 当没有设置圆形半径时，圆的半径占据view总高度（宽度）的比例
     */
    private float mRatioWithCircle = 0.5f;

    /**
     * 环形宽度占圆半径的比例
     */
    private float mRatioWithRing = 0.5f;

    /**
     * 线的宽度
     */
    private int mLineWidth = 0;

    /**
     * 连接线的颜色
     */
    private int mLineColor = 0;

    /**
     * 选中时圆的颜色
     */
    private int mActiveCircleColor = 0;

    /**
     * 未选中时圆的颜色
     */
    private int mInactiveCircleColor = 0;

    /**
     * 是否被选中
     */
    private boolean mChecked = false;

    /**
     * 默认上下两根线都显示
     */
    private LINES mLines = LINES.BOTH;

    /**
     * Simple constructor to use when creating a view from code.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     */
    public PartStepView(Context context) {
        super(context);
        init(context, null);
    }

    /**
     * Constructor that is called when inflating a view from XML. This is called
     * when a view is being constructed from an XML file, supplying attributes
     * that were specified in the XML file. This version uses a default style of
     * 0, so the only attribute values applied are those in the Context's Theme
     * and the given AttributeSet.
     * <p/>
     * <p/>
     * The method onFinishInflate() will be called after all children have been
     * added.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     */
    public PartStepView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * Perform inflation from XML and apply a class-specific base style from a
     * theme attribute. This constructor of View allows subclasses to use their
     * own base style when they are inflating. For example, a Button class's
     * constructor would call this version of the super class constructor and
     * supply <code>R.attr.buttonStyle</code> for <var>defStyleAttr</var>; this
     * allows the theme's button style to modify all of the base view attributes
     * (in particular its background) as well as the Button class's attributes.
     *
     * @param context      The Context the view is running in, through which it can
     *                     access the current theme, resources, etc.
     * @param attrs        The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr An attribute in the current theme that contains a
     *                     reference to a style resource that supplies default values for
     *                     the view. Can be 0 to not look for defaults.
     */
    public PartStepView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);

    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray systemType = getContext().getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.colorPrimary});
        mActiveCircleColor = systemType.getColor(0, Color.BLUE);
        mInactiveCircleColor = Color.LTGRAY;
        mLineWidth = dipToPx(getContext(), 2);
        systemType.recycle();

        if (attrs != null) {
            TypedArray customType = context.obtainStyledAttributes(attrs, R.styleable.PartStepView);
            mChecked = customType.getBoolean(R.styleable.PartStepView_psv_checked, mChecked);
            mActiveCircleColor = customType.getColor(R.styleable.PartStepView_psv_activeColor, mActiveCircleColor);
            mInactiveCircleColor = customType.getColor(R.styleable.PartStepView_psv_inactiveColor, mInactiveCircleColor);
            mLineColor = mInactiveCircleColor;
            mLineWidth = (int) customType.getDimension(R.styleable.PartStepView_psv_lineWidth, mLineWidth);
            mRatioWithCircle = customType.getFloat(R.styleable.PartStepView_psv_circleRatio, mRatioWithCircle);
            mRatioWithCircle = (mRatioWithCircle > 1f) ? 1 : mRatioWithCircle;
            int lineStyle = customType.getInt(R.styleable.PartStepView_psv_line, LINES.BOTH.value);
            for (LINES line :LINES.values()) {
                if(line.value == lineStyle){
                    mLines = line;
                    break;
                }
            }
            customType.recycle();
        }

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(mInactiveCircleColor);
        mCirclePaint.setStyle(Paint.Style.FILL);

        mRingPaint = new Paint(mCirclePaint);
        mRingPaint.setAlpha((int) (mRingAlpha * 255));
        mRingPaint.setStyle(Paint.Style.STROKE);

        mLinePaint = new Paint();
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mLinePaint.setStrokeWidth(mLineWidth);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(mLineColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(
                measureLength(widthMeasureSpec, true),
                measureLength(heightMeasureSpec, false));
    }

    /**
     * 测量长宽
     *
     * @param measureSpec
     * @param flag        true-宽, false-高
     * @return
     */
    private int measureLength(int measureSpec, boolean flag) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {//精确值模式，指定具体数值
            result = specSize;
        } else {
            int otherLength = 0;
            if (flag)
                otherLength = getPaddingLeft() + getPaddingRight();
            else
                otherLength = getPaddingTop() + getPaddingBottom();
            Log.d(TAG, otherLength + "");
            ////先设置一个默认大小
            result = dipToPx(getContext(), 32) + otherLength;
            //最大值模式，layout_width 或 layout_height 为 wrap_content 时，
            // 控件大小随控件的内容变化而变化，此时控件尺寸只要不超过父控件允许的最大尺寸即可。
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);//取出我们指定的大小和 specSize 中最小的一个来作为最后的测量值
            }
            //MeasureSpec.UNSPECIFIED 不指定其大小，View 想多大就多大
        }
        Log.d(TAG, "result=" + result);
        return result;
    }

    /**
     * Implement this to do your drawing.
     *
     * @param canvas the canvas on which the background will be drawn
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int circleWidth = canvas.getWidth();
        int circleHeight = canvas.getHeight();
        int centerX = circleWidth / 2 + getPaddingLeft() - getPaddingRight();
        int centerY = circleHeight / 2 + getPaddingTop() - getPaddingBottom();
        //画线，分为上线和下线
        switch (mLines) {
            case BOTH://两根线都画
                canvas.drawLine(centerX, 0, centerX, circleHeight, mLinePaint);
                break;
            case UP://只画上面的线
                canvas.drawLine(centerX, 0, centerX, centerY, mLinePaint);
                break;
            case DOWN://只画下面的线
                canvas.drawLine(centerX, centerY, centerX, circleHeight, mLinePaint);
                break;
            case NONE:
                break;
        }
        Log.d(TAG, "画线，线宽" + mLineWidth);
        //画圆
        int realWidth = circleWidth - getPaddingLeft() - getPaddingRight();
        int realHeight = circleHeight - getPaddingTop() - getPaddingBottom();
        float radius = mRatioWithCircle * Math.min(realWidth, realHeight) / 2;
        mCirclePaint.setColor(mChecked ? mActiveCircleColor : mInactiveCircleColor);
        canvas.drawCircle(centerX, centerY, radius, mCirclePaint);
        Log.d(TAG, "绘制圆，半径" + radius);
        if (mChecked) {
            //画环
            float ringWidth = radius * mRatioWithRing;//环形宽度
            mRingPaint.setStrokeWidth(ringWidth);
            mRingPaint.setColor(mChecked ? mActiveCircleColor : mInactiveCircleColor);
            mRingPaint.setAlpha((int) (mRingAlpha * 255));
            canvas.drawCircle(centerX, centerY, radius + ringWidth / 2, mRingPaint);
            Log.d(TAG, "绘制环，半径" + (radius + ringWidth / 2));
        }
    }

    /**
     * 是否被选中
     *
     * @param checked
     */
    public void setChecked(boolean checked) {
        if (mChecked == checked)
            return;
        mChecked = checked;
        invalidate();
    }

    /**
     * 设置线型
     * @param lines
     */
    public void setLines(LINES lines){
        mLines = lines;
        invalidate();
    }

    private static int dipToPx(Context context, int dip) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5F);
    }
}
