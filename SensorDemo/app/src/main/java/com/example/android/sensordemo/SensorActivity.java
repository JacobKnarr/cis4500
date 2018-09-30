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

/* To interact with the device sensors you need to implement the SensorEventListener class*/
public class SensorActivity extends Activity implements SensorEventListener {

    /*  These private fields are used to manage and gain access to the device sensors.
        They are needed in order to use any sensor in Android development.
     */
    private SensorManager mSensorManager;
    private Sensor mSensor;

    /*  This float is used to capture the returned value of the sensor.
        It is dependent on the type of sensor being used and may change.
     */
    private float currentLux = 0;

    /*  Used to display the sensors recorded lux value in the main activity. */
    private TextView LuxValue;


    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*  Sets the UI elements to for sensor information display later on. */
        TextView vendor = findViewById(R.id.Vendor);
        TextView Version = findViewById(R.id.Version);
        LuxValue = findViewById(R.id.LuxValue);

        /*  This line is needed when dealing with sensors.
            It is a standard call that sets up the sensor manager for the particular device.
         */
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        /* Checks if there is a Light sensor on the device. */
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null){
            // Success! There's a light sensor.
            /*  Gets a list of light sensors.
                Used to see if there are multiple of the same sensor.
                Allows the developer to choose a specific sensor.
                Can be used with TYPE_ALL to get all available device sensors.
             */
            List<Sensor> lightSensors = mSensorManager.getSensorList(Sensor.TYPE_LIGHT);
            /*  Iterate through the list of sensors. */
            for(int i=0; i<lightSensors.size(); i++) {
                /*  These lines get the vendor and version of the specific sensor at the current index. */
                System.out.println("Vendor of Light sensor " + (i+1) + ": " + lightSensors.get(i).getVendor());
                System.out.println("Version of Light sensor " + (i+1) + ": " + lightSensors.get(i).getVersion());
                /*  Simply updates the UI with the sensor information. */
                vendor.setText(lightSensors.get(i).getVendor());
                Version.setText(String.format(Locale.getDefault(),"%d", lightSensors.get(i).getVersion()));
            }
            /*  Needed to set the sensor to the devices default sensor of TYPE_LIGHT. */
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        } else {
            // Failure! No light.
            System.out.println("Unfortunately there is no light sensor on this device!");
        }
    }

    /*  Needed when interacting with sensors.
        This method is called when the sensor returns an event.
        This is the method that you can adjust the timing of with SENSOR_DELAY.
        Set the delay as high as possible for you specific design to reduce battery usage.
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        /*  The light sensor only returns a single value.
            The value represents the intensity of light emitted from the device.
        */
        float lux = event.values[0];
        /*  This block checks if the current events lux value is different than the stored value.
            It then prints the lux value and also sets the stored value and updates the UI.
         */
        if (currentLux != event.values[0]) {
            System.out.println("onSensorChanged: The value returned by the light sensor is " + lux);
            currentLux = event.values[0];
            LuxValue.setText(String.format("%s", currentLux));
        }
    }

    /*  Needed when interacting with sensors.
        This method is called when the sensors accuracy level changes.
        This could be due to the specific devices settings, user input, or a change in;
        battery level, orientation, device location, etc.
        There are 4 levels of accuracy which can be seen in the cases of the switch statement.
     */
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

    /*  Register the sensor when the application is resumed using the sensor manager. */
    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /*  Unregister the sensor when the application is paused(background) using the sensor manager. */
    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

}
