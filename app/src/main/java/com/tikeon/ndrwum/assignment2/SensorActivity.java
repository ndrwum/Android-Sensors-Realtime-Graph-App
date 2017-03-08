package com.tikeon.ndrwum.assignment2;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {

    final private int FAST = 12;
    final private int DARK = 10000;
    PlotView pv;
    ImageView imv;
    TextView tv;
    private SensorManager sm;
    private Sensor s;
    private long lastUpdate = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        tv = (TextView) findViewById(R.id.plot_title);
        pv = (PlotView) findViewById(R.id.pv);
        imv = (ImageView) findViewById(R.id.animateImg);

        sm.registerListener(this, s, SensorManager.SENSOR_DELAY_NORMAL);

        if (getIntent().getStringExtra("sensorName").equals("accelSensor")) {
            s = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sm.registerListener(this, s, SensorManager.SENSOR_DELAY_NORMAL);

            tv.setText("Accelerometer");
        } else if (getIntent().getStringExtra("sensorName").equals("lightSensor")) {
            s = sm.getDefaultSensor(Sensor.TYPE_LIGHT);
            sm.registerListener(this, s, SensorManager.SENSOR_DELAY_NORMAL);

            tv.setText("Light");
        }


    }

    public void onSensorChanged(SensorEvent event) {
        if (s.getType() == Sensor.TYPE_LIGHT
                && event.sensor.getType() == Sensor.TYPE_LIGHT) {
            float val = event.values[0];

            long curTime = System.currentTimeMillis();
            if ((curTime - lastUpdate) > 1000) {
                lastUpdate = curTime;
                pv.addPoint(val);
                pv.invalidate();
            }

            double last_mean = pv.getLastAvg();
            if (last_mean < DARK) {
                imv.setImageResource(R.drawable.offlight);
                imv.setMaxHeight(50);

            } else {
                imv.setImageResource(R.drawable.onlight);
                imv.setMaxHeight(50);

            }
        } else {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            long curTime = System.currentTimeMillis();
            if ((curTime - lastUpdate) > 1000) {
                lastUpdate = curTime;

                float accelVal = (float) Math.sqrt((x * x) + (y * y) + (z * z));

                pv.addPoint(accelVal);
                pv.invalidate();
            }
            double last_mean = pv.getLastAvg();
            if (last_mean > FAST) {
                imv.setImageResource(R.drawable.fast);
                imv.setMaxHeight(50);

            } else {
                imv.setImageResource(R.drawable.slow);
                imv.setMaxHeight(50);

            }
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    public void goBack(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
