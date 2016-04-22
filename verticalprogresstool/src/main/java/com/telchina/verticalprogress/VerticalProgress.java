package com.telchina.verticalprogress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.telchina.verticalprogresstool.R;


/**
 * 垂直排列的步骤指示器
 * Created by ZH on 2016-4-22.
 */
public class VerticalProgress extends View {

    private static final String TAG = VerticalProgress.class.getSimpleName();

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

    /***
     * 文本画笔
     */
    private TextPaint mTextPaint;

    /**
     * 环形颜色透明度
     */
    private float mRingAlpha = 0.25f;

    /**
     * 圆的半径
     */
    private int mRatioWithCircle = 30;

    /**
     * 环形宽度占圆半径的比例
     */
    private float mRatioWithRing = 0.5f;

    /**
     * 线的宽度
     */
    private int mLineWidth = 1;

    /**
     * 连接线的颜色
     */
    private int mLineColor = Color.LTGRAY;

    /**
     * 选中时圆的颜色
     */
    private int mActiveCircleColor = Color.BLACK;

    /**
     * 未选中时圆的颜色
     */
    private int mInactiveCircleColor = Color.BLACK;

    /**
     * 选中文本颜色
     */
    private int mActiveTextColor = Color.BLACK;

    /**
     * 未选中文本颜色
     */
    private int mInactiveTextColor = Color.BLACK;
    /**
     * 选中文本尺寸
     */
    private int mActiveTextSize = 30;

    /**
     * 未选中文本尺寸
     */
    private int mInactiveTextSize = 30;

    /**
     * 圆与文字之间的距离
     */
    private float mBetweenCircleText = 50;

    /**
     * 显示方向：
     * true:向上
     * false:向下
     */
    private boolean mUpOrDown = true;

    /**
     * 显示标签数量
     */
    private int mCount = 1;

    private String[] mLables = null;

    /**
     * Simple constructor to use when creating a view from code.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     */
    public VerticalProgress(Context context) {
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
    public VerticalProgress(Context context, AttributeSet attrs) {
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
    public VerticalProgress(Context context, AttributeSet attrs, int defStyleAttr) {
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
            TypedArray customType = context.obtainStyledAttributes(attrs, R.styleable.VerticalProgress);
            mActiveCircleColor = customType.getColor(R.styleable.VerticalProgress_psv_activeColor, mActiveCircleColor);
            mInactiveCircleColor = customType.getColor(R.styleable.VerticalProgress_psv_inactiveColor, mInactiveCircleColor);
            mLineColor = mInactiveCircleColor;
            mLineWidth = (int) customType.getDimension(R.styleable.VerticalProgress_psv_lineWidth, mLineWidth);
            mRatioWithCircle = (int) customType.getDimension(R.styleable.VerticalProgress_psv_circleRatio, mRatioWithCircle);
            mActiveTextColor =  customType.getColor(R.styleable.VerticalProgress_psv_activeTextColor,mActiveTextColor);
            mInactiveTextColor = customType.getColor(R.styleable.VerticalProgress_psv_inactiveTextColor,mInactiveTextColor);
            mActiveTextSize = (int) customType.getDimension(R.styleable.VerticalProgress_psv_activeTextSize,mActiveTextSize);
            mInactiveTextSize = (int) customType.getDimension(R.styleable.VerticalProgress_psv_inactiveTextSize,mInactiveTextSize);
            mBetweenCircleText = customType.getDimension(R.styleable.VerticalProgress_psc_betweenCircleText,mBetweenCircleText);
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

        mTextPaint = new TextPaint();
        mTextPaint.setColor(mInactiveTextColor);
        mTextPaint.setTextSize(mInactiveTextSize);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(
                measureLength(widthMeasureSpec, true),
                measureLength(heightMeasureSpec, false));
    }

    /***
     * 设置显示标签
     *
     * @param lables 显示记录标签
     */
    public VerticalProgress setLables(String[] lables) {
        this.mLables = lables;
        mCount = mLables.length;
        return this;
    }

    /**
     * 设置显示方向
     *
     * @param upOrDown true:向上  false:向下
     */
    public VerticalProgress setDirection(boolean upOrDown) {
        this.mUpOrDown = upOrDown;
        return this;
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
            // 最大值模式，layout_width 或 layout_height 为 wrap_content 时，
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

        //X轴偏移量
        float offsetX = mRatioWithCircle * (mRatioWithRing + 1) + getPaddingLeft();
        //Y轴偏移量
        float offsetY = mRatioWithCircle * (mRatioWithRing + 1) + getPaddingTop() + mTextPaint.getTextSize();
        //Y轴增量
        float incrementY = (canvas.getHeight()- getPaddingTop() - (mRatioWithCircle * (mRatioWithRing + 1) +
                mTextPaint.getTextSize()) * 2) / (mCount - 1);

        //字符串偏移量
        mBetweenCircleText = mBetweenCircleText < offsetX ? offsetX : mBetweenCircleText;
        int textWidht = (int) (canvas.getWidth() - offsetX - mBetweenCircleText);

        //画线
        float centerX = offsetX;
        float lineY = offsetY;
        float stopLineY = lineY + incrementY * (mCount - 1);

        canvas.drawLine(centerX, lineY, centerX, stopLineY, mLinePaint);
        Log.d(TAG, "画线，线宽" + mLineWidth);
        for (int i = 0; i < mCount; i++) {
            //画圆
            float centerY = lineY + incrementY * i;
            float radius = mRatioWithCircle;

            //画圈圈
            float ringWidth = radius * mRatioWithRing;//环形宽度
            mRingPaint.setStrokeWidth(ringWidth);
            mRingPaint.setAlpha((int) (mRingAlpha * 255));

            if (mUpOrDown) {
                //如果true：向上，最上面为选中，其余为非选中。反之则反之
                if (i == 0) {
                    mCirclePaint.setColor(mActiveCircleColor);
                    mTextPaint.setColor(mActiveTextColor);
                    mTextPaint.setTextSize(mActiveTextSize);
                    canvas.drawCircle(centerX, centerY, radius + ringWidth / 2, mRingPaint);
                } else {
                    mCirclePaint.setColor(mInactiveCircleColor);
                    mTextPaint.setColor(mInactiveTextColor);
                    mTextPaint.setTextSize(mInactiveTextSize);
                }
            } else {
                if (i == (mCount - 1)) {
                    mCirclePaint.setColor(mActiveCircleColor);
                    mTextPaint.setColor(mActiveTextColor);
                    mTextPaint.setTextSize(mActiveTextSize);
                    canvas.drawCircle(centerX, centerY, radius + ringWidth / 2, mRingPaint);
                } else {
                    mCirclePaint.setColor(mInactiveCircleColor);
                    mTextPaint.setColor(mInactiveTextColor);
                    mTextPaint.setTextSize(mInactiveTextSize);
                }
            }
            canvas.drawCircle(centerX, centerY, radius, mCirclePaint);
            Log.d(TAG, "绘制圆，半径" + radius);

            //写字
            StaticLayout textLayout = new StaticLayout(mLables[i], mTextPaint, textWidht,
                    Layout.Alignment.ALIGN_NORMAL, 1.5F, 0.0f, false);
            canvas.translate(centerX + mBetweenCircleText, centerY - textLayout.getHeight() / 2);
            textLayout.draw(canvas);
            canvas.translate(-centerX - mBetweenCircleText, -centerY + textLayout.getHeight() / 2);
        }
    }


    private static int dipToPx(Context context, int dip) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5F);
    }

    public VerticalProgress setActiveCircleColor(int color) {
        mActiveCircleColor = color;
        return this;
    }

    public VerticalProgress setInactiveColor(int color) {
        mInactiveCircleColor = color;
        mLineColor = mInactiveCircleColor;
        return this;
    }

    public VerticalProgress setRatioWithCircle(int ratio) {
        mRatioWithCircle = ratio;
        return this;
    }

    public VerticalProgress setLineWidth(int width) {
        mLineWidth = width;
        return this;
    }


}
