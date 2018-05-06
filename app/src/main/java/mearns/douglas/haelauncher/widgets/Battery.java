package mearns.douglas.haelauncher.widgets;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.BatteryManager;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicInteger;

import mearns.douglas.haelauncher.R;

import static java.lang.Math.min;

/**
 * TODO: document your custom view class.
 */
public final class Battery extends FrameLayout {

    public Battery(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.battery_widget_layout, this, true);

        box = findViewById(R.id.percentageBox);
        bar = findViewById(R.id.batteryBar);

        context.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    private final BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            final int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    box.setText("Battery: " + Integer.toString(level) + "%");
                    bar.setProgress(level);
                }
            });
        }
    };

    private Handler handler = new Handler();
    private final TextView box;
    private final ProgressBar bar;
}