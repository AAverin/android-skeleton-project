package pro.anton.averin.android.skeleton.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import pro.averin.anton.android.skeleton.R;

/**
 * Created by AAverin on 16-7-2014.
 */
public class ExtendedTextView extends TextView {

    boolean fitText = false;
    private Paint textPaint;
    protected interface TextViewOrientation {
        final static int HORIZONTAL = 0;
        final static int VERTICAL = 1;
    }
    protected int currentOrientation = TextViewOrientation.HORIZONTAL;

    public ExtendedTextView(Context context) {
        this(context, null);
    }

    public ExtendedTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExtendedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ExtendedTextView, 0, 0);

        fitText = attributes.getBoolean(R.styleable.ExtendedTextView_pro_aaverin_fitText, false);
        attributes.recycle();

        textPaint = new Paint();
        textPaint.set(this.getPaint());
    }

    float fittedLo = 0;
    private void fitText(String text, int limit) {
        if (limit <= 0) {
            return;
        }

        if (fittedLo == 0) {
            Rect textBounds = new Rect();
            float screenDensity = getResources().getDisplayMetrics().density;
            float hi = getTextSize();
            fittedLo = getTextSize() / 2;
            final float threshold = 0.1f; // How close we have to be
            textPaint.set(this.getPaint());

            int targetLimit = 0;

            if (currentOrientation == TextViewOrientation.HORIZONTAL) { //horizontal
                targetLimit = limit - this.getPaddingLeft() - this.getPaddingRight();
            } else { //vertical
                targetLimit = limit - this.getPaddingBottom() - this.getPaddingTop();
            }

            while((hi - fittedLo) > threshold) {
                float size = (hi+fittedLo)/2;
                textPaint.setTextSize(size);
                textPaint.getTextBounds(text, 0, text.length(), textBounds);
                if (textBounds.width() + 10 >= targetLimit) {
                    hi = size; // too big
                } else {
                    fittedLo = size; // too small
                }
            }
        }

        // Use lo so that we undershoot rather than overshoot
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, fittedLo);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        fittedLo = 0;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (fitText) {
            if (currentOrientation == TextViewOrientation.HORIZONTAL) {
                fitText(this.getText().toString(), getMeasuredWidth());
            } else {
                fitText(this.getText().toString(), getMeasuredHeight());
            }
        }
    }
}
