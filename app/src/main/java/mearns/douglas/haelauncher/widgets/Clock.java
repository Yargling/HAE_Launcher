package mearns.douglas.haelauncher.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.GregorianCalendar;
import java.util.Iterator;

import mearns.douglas.haelauncher.util.FloatRange;

import static java.lang.Math.min;

/**
 * Note: Not thread safe; however, given GUI elements have to be used from the 'main' thread, this
 * is not an issue.
 */
public final class Clock extends BaseWidget {
    public Clock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        handler.post(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                Clock.this.invalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        getLocalVisibleRect(visibleRect);

        drawBackground(canvas);
        drawHands(canvas);
    }

    private void drawBackground(Canvas canvas) {
        canvas.drawCircle(visibleRect.exactCenterX(), visibleRect.exactCenterY(), radius(), BACKGROUND_PAINTER);
        for (float angle : markerAngles) {
            drawMarkerLine(canvas, angle);
        }
    }

    private void drawHands(Canvas canvas) {
        GregorianCalendar currentTime = new GregorianCalendar();
        final int hour = currentTime.get(GregorianCalendar.HOUR_OF_DAY) % 12;
        final int minute = currentTime.get(GregorianCalendar.MINUTE);
        final int second = currentTime.get(GregorianCalendar.SECOND);

        final float radius = radius();
        drawLineFromCentre((hour/12.0f), radius / 2.5f, HOUR_HAND_PAINT , canvas);
        drawLineFromCentre((minute/60.0f), radius / 1.5f, MINUTE_HAND_PAINT, canvas);
        drawLineFromCentre((second/60.0f), radius / 1.2f, SECOND_HAND_PAINT, canvas);
    }

    private void drawLineFromCentre(float ratioAround, float length, Paint paint, Canvas canvas) {
        final float angleRadians = (float)(ratioAround * 2 * Math.PI);
        canvas.drawLine(visibleRect.exactCenterX(), visibleRect.exactCenterY(),
                calculateXForAngle(angleRadians, length), calculateYForAngle(angleRadians, length),
                paint);
    }

    private void drawMarkerLine(Canvas canvas, float angle) {
        final float radius = radius() - 50;
        canvas.drawCircle(calculateXForAngle(angle, radius), calculateYForAngle(angle, radius), 20, MARKER_PAINTER);
    }

    private float radius() {
        return min(visibleRect.width(), visibleRect.height()) / 2f;
    }

    private float calculateXForAngle(float angle, float distanceFromCentre) {
        return (float)(visibleRect.exactCenterX() + distanceFromCentre * Math.sin(angle));
    }

    private float calculateYForAngle(float angle, float distanceFromCentre) {
        return (float)(visibleRect.exactCenterY() - distanceFromCentre * Math.cos(angle));
    }

    private final Handler handler = new Handler();
    private final Rect visibleRect = new Rect();
    private static final Iterable<Float> markerAngles = FloatRange.create(0, 11, (float)Math.PI*2);
    private static final Paint SECOND_HAND_PAINT = new Paint();
    private static final Paint MINUTE_HAND_PAINT = new Paint();
    private static final Paint HOUR_HAND_PAINT = new Paint();
    private static final Paint MARKER_PAINTER = new Paint();
    private static final Paint BACKGROUND_PAINTER = new Paint();

    static {
        SECOND_HAND_PAINT.setARGB(200, 200, 50, 50);
        SECOND_HAND_PAINT.setStrokeWidth(5);
        SECOND_HAND_PAINT.setAntiAlias(true);

        MINUTE_HAND_PAINT.setARGB(200, 50, 200, 50);
        MINUTE_HAND_PAINT.setStrokeWidth(10);
        MINUTE_HAND_PAINT.setAntiAlias(true);

        HOUR_HAND_PAINT.setARGB(200, 50, 50, 200);
        HOUR_HAND_PAINT.setStrokeWidth(15);
        HOUR_HAND_PAINT.setAntiAlias(true);

        MARKER_PAINTER.setARGB(255, 0, 0, 0);
        MARKER_PAINTER.setStrokeWidth(15);
        MARKER_PAINTER.setAntiAlias(true);

        BACKGROUND_PAINTER.setARGB(255, 240, 240, 240);
        BACKGROUND_PAINTER.setAntiAlias(true);
    }
}
