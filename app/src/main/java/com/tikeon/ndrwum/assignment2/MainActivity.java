package com.tikeon.ndrwum.assignment2;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sm;
    private Sensor accelSensor, lightSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);


        accelSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        lightSensor = sm.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (accelSensor == null) {
            setStatus((TextView) findViewById(R.id.accel_stat), "Not Found");
            setRange((TextView) findViewById(R.id.accel_range), "N/A");
            setResolution((TextView) findViewById(R.id.accel_res), "N/A");

        } else {
            setStatus((TextView) findViewById(R.id.accel_stat), "Found");
            setRange((TextView) findViewById(R.id.accel_range), accelSensor.getMaximumRange());
            setResolution((TextView) findViewById(R.id.accel_res), accelSensor.getResolution());

            sm.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (lightSensor == null) {
            setStatus((TextView) findViewById(R.id.light_stat), "Not Found");
            setRange((TextView) findViewById(R.id.light_range), "N/A");
            setResolution((TextView) findViewById(R.id.light_res), "N/A");

        } else {
            setStatus((TextView) findViewById(R.id.light_stat), "Found");
            setRange((TextView) findViewById(R.id.light_range), lightSensor.getMaximumRange());
            setResolution((TextView) findViewById(R.id.light_res), lightSensor.getResolution());

            sm.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void setStatus(TextView tv, String s) {
        tv.setText("Status: " + s);
    }

    public void setRange(TextView tv, float range) {
        tv.setText("Range: " + range);
    }

    public void setRange(TextView tv, String s) {
        tv.setText("Range: " + s);
    }

    public void setResolution(TextView tv, float res) {
        tv.setText("Resolution: " + res);
    }

    public void setResolution(TextView tv, String s) {
        tv.setText("Resolution: " + s);
    }

    public void startAccel(View v) {

        if (accelSensor == null) {
            Toast toast = Toast.makeText(getApplicationContext(), "Accelerometer sensor could not be found.",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
            toast.show();

        } else {
            Intent intent = new Intent(getApplicationContext(), SensorActivity.class);
            intent.putExtra("sensorName", "accelSensor");

            startActivity(intent);
        }
    }

    public void startLight(View v) {

        if (lightSensor == null) {
            Toast toast = Toast.makeText(getApplicationContext(), "Light sensor could not be found.",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
            toast.show();

        } else {
            Intent intent = new Intent(getApplicationContext(), SensorActivity.class);
            intent.putExtra("sensorName", "lightSensor");

            startActivity(intent);
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (accelSensor != null)
            sm.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
        if (lightSensor != null)
            sm.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
}
