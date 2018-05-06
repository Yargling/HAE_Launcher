package mearns.douglas.haelauncher.widgets;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import static java.lang.Math.min;

abstract class BaseWidget extends View {
    public BaseWidget(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);

        final int squareDimension = min(heightSize, widthSize);
        final int desiredDimension = squareDimension == 0 ? 300 : squareDimension;

        int width, height;

        //Measure Width
        if (widthMode == View.MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == View.MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = min(desiredDimension, widthSize);
        } else {
            //Be whatever you want
            width = desiredDimension;
        }

        //Measure Height
        if (heightMode == View.MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == View.MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = min(desiredDimension, heightSize);
        } else {
            //Be whatever you want
            height = desiredDimension;
        }

        //MUST CALL THIS
        setMeasuredDimension(width, height);
    }
}
