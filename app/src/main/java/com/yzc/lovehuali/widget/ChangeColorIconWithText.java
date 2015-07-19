package com.yzc.lovehuali.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.yzc.lovehuali.R;

/**
 * Created by Administrator on 2015/2/5 0005.
 */
public class ChangeColorIconWithText extends View{

    private int mColor = 0xFF2196F3;
    private Bitmap mIconBitmap;
    private String mText = "TabName";
    private int mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,12,getResources().getDisplayMetrics());


    //成员变量
    private Canvas mCanvas;
    private Bitmap mBitmap;
    private Paint mPaint;

    private float mAlpha=0;
    private Rect mIconRect;
    private Rect mTextBound;

    private Paint mTextPaint;


    public ChangeColorIconWithText(Context context) {
        this(context, null);
    }

    public ChangeColorIconWithText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChangeColorIconWithText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ChangeColorIconWithText);
        int n = a.getIndexCount();

        for (int i=0;i < n; i++){
            int attr = a.getIndex(i);
            switch (attr){
                case R.styleable.ChangeColorIconWithText_cit_icon:
                    BitmapDrawable drawable = (BitmapDrawable)a.getDrawable(attr);
                    mIconBitmap = drawable.getBitmap();
                    break;
                case  R.styleable.ChangeColorIconWithText_cit_color:
                    mColor = a.getColor(attr,0xFF2196F3);
                    break;
                case R.styleable.ChangeColorIconWithText_text:
                    mText = a.getString(attr);
                    break;
                case R.styleable.ChangeColorIconWithText_text_size:
                    mTextSize = (int) a.getDimension(attr,TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,12,getResources().getDisplayMetrics()));
                    break;
            }
        }
        a.recycle();

        mTextBound = new Rect();
        mTextPaint = new Paint();
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(0xFF808080);

        mTextPaint.getTextBounds(mText,0,mText.length(),mTextBound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int iconWidth = Math.min(getMeasuredWidth()-getPaddingLeft()-getPaddingRight(),
                getMeasuredHeight()-getPaddingTop()-getPaddingBottom()-mTextBound.height());

        int left = getMeasuredWidth()/2-iconWidth/2;

        int top = getMeasuredHeight()/2 -(mTextBound.height() + iconWidth)/2;

        mIconRect = new Rect(left,top,left + iconWidth,top + iconWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mIconBitmap,null,mIconRect,null);//绘制原图

        int alpha = (int)Math.ceil(255*mAlpha);

        //内存去准备mBitmap,setAlpha，纯色，xfermode,图标

        setupTargetBitmap(alpha);

        //1绘制原文本 2.绘制变色的文本
        drawSourceText(canvas,alpha);

        drawTargetText(canvas,alpha);

        canvas.drawBitmap(mBitmap,0,0,null);

    }

    //绘制变色文本
    private void drawTargetText(Canvas canvas, int alpha) {

        mTextPaint.setColor(0xFF2196F3);
        mTextPaint.setAlpha(alpha);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);

        int x = getMeasuredWidth()/2 - mTextBound.width()/2;
        int y = mIconRect.bottom + mTextBound.height();

        canvas.drawText(mText,x,y,mTextPaint);
    }

    //绘制原文本
    private void drawSourceText(Canvas canvas, int alpha) {
        mTextPaint.setColor(0xFF808080);
        mTextPaint.setAlpha(255-alpha);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);

        int x = getMeasuredWidth()/2 - mTextBound.width()/2;
        int y = mIconRect.bottom + mTextBound.height();

        canvas.drawText(mText,x,y,mTextPaint);

    }

    //在内存中绘制可变色的icon
    private void setupTargetBitmap(int alpha) {
        mBitmap = Bitmap.createBitmap(getMeasuredWidth(),getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setAlpha(alpha);

        mCanvas.drawRect(mIconRect,mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mPaint.setAlpha(255);
        mCanvas.drawBitmap(mIconBitmap,null,mIconRect,mPaint);


    }

    public void setIconAlpha(float alpha){
        this.mAlpha = alpha;
        invalidateView();
    }

    //重绘
    private void invalidateView() {
        if(Looper.getMainLooper() == Looper.myLooper()){
            invalidate();
        }
        else {
            postInvalidate();
        }
    }
}
