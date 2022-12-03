package com.moutamid.stepdetectorandcounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView counterTxt;
    private Button resetBtn;
    private static final int PHYSICAL_CODE = 1;
    SensorManager sensorManager;
    //Sensor stepCounterSensor;
    Sensor stepDetectorSensor,stepCounterSensor;

    int currentStepsDetected;

    int stepCounter;
    int newStepCounter;
    int counter = 0;
    private SharedPreferencesManager prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        counterTxt = findViewById(R.id.counter);
        resetBtn = findViewById(R.id.reset);
        prefs = new SharedPreferencesManager(MainActivity.this);

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter = 0;
                prefs.storeInt("steps",counter);
                counterTxt.setText(String.valueOf(counter));
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACTIVITY_RECOGNITION) ==
                    PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, PHYSICAL_CODE);
            }else{
                viewInit();
                Toast.makeText(MainActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            viewInit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0) {
            if (requestCode == PHYSICAL_CODE) {
                boolean readExternalStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (readExternalStorage) {

                    viewInit();
                } else {
                    Toast.makeText(MainActivity.this, " Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void viewInit() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        counter = prefs.retrieveInt("steps",0);
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            int countSteps = (int) event.values[0];
            // Initially stepCounter will be zero
            if (stepCounter == 0) {
                stepCounter = (int) event.values[0];
            }
            newStepCounter = countSteps - stepCounter;

         /*   if (counter != 0){
                newStepCounter += counter;
            }*/
        }

        // Step detector sensor
        if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {

            int detectSteps = (int) event.values[0];
            counter += detectSteps;
           /* if (counter != 0){
                currentStepsDetected = counter;
                currentStepsDetected += detectSteps;
            }else {
                currentStepsDetected += detectSteps;
            }*/
            prefs.storeInt("steps",counter);
        }
        counterTxt.setText(String.valueOf(counter));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_UI);
            sensorManager.registerListener(this, stepDetectorSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}