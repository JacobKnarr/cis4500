package com.example.android.sensordemo;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class SensorActivity extends Activity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private float currentLux = 0;
    private TextView LuxValue;


    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView vendor = findViewById(R.id.Vendor);
        TextView Version = findViewById(R.id.Version);
        LuxValue = findViewById(R.id.LuxValue);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null){
            // Success! There's a light sensor.
            List<Sensor> lightSensors = mSensorManager.getSensorList(Sensor.TYPE_LIGHT);
            for(int i=0; i<lightSensors.size(); i++) {
                System.out.println("Vendor of Light sensor " + (i+1) + ": " + lightSensors.get(i).getVendor());
                System.out.println("Version of Light sensor " + (i+1) + ": " + lightSensors.get(i).getVersion());
                vendor.setText(lightSensors.get(i).getVendor());
                Version.setText(String.format(Locale.getDefault(),"%d", lightSensors.get(i).getVersion()));
            }
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        } else {
            // Failure! No light.
            System.out.println("Unfortunately there is no light sensor on this device!");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        /*  The light sensor only returns a single value.
            The value represents the intensity of light emitted from the device.
        */
        float lux = event.values[0];
        if (currentLux != event.values[0]) {
            System.out.println("onSensorChanged: The value returned by the light sensor is " + lux);
            currentLux = event.values[0];
            LuxValue.setText(String.format("%s", currentLux));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (sensor == mSensor) {
            switch (accuracy) {
                case 0:
                    System.out.println("Unreliable");
                    break;
                case 1:
                    System.out.println("Low Accuracy");
                    break;
                case 2:
                    System.out.println("Medium Accuracy");
                    break;
                case 3:
                    System.out.println("High Accuracy");
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

}
