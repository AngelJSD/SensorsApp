package com.example.angel.sensors;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;
import android.widget.TextView;
import org.apache.commons.io.IOUtils;

import com.felipecsl.gifimageview.library.GifImageView;

import java.io.IOException;
import java.io.InputStream;

public class SensorDetail extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;

    TextView data;
    ImageView colorCanvas;
    GifImageView gifImageView;
    boolean isPlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_detail);
        isPlaying = false;

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        data = findViewById(R.id.sensorView);
        int sensorSelected = getIntent().getIntExtra("SENSOR_SELECTED", 0);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        switch(sensorSelected){
            case Sensor.TYPE_ACCELEROMETER:
                mSensorManager.registerListener(this,
                    mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_NORMAL);
                break;
            case Sensor.TYPE_LIGHT:
                colorCanvas = findViewById(R.id.imageView);
                colorCanvas.setVisibility(View.VISIBLE);
                mSensorManager.registerListener(this,
                        mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
                        SensorManager.SENSOR_DELAY_NORMAL);
                break;
            case Sensor.TYPE_PROXIMITY:
                colorCanvas = findViewById(R.id.imageView);
                colorCanvas.setVisibility(View.VISIBLE);

                gifImageView = findViewById(R.id.gifImageView);
                gifImageView.setVisibility(View.VISIBLE);
                try {
                    InputStream inputStream = getAssets().open("time_stone3.gif");
                    byte[] bytes = IOUtils.toByteArray(inputStream);
                    gifImageView.setBytes(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mSensorManager.registerListener(this,
                        mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
                        SensorManager.SENSOR_DELAY_NORMAL);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();

        if(id == android.R.id.home){
            mSensorManager.unregisterListener(this);
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                float[] values = event.values;
                // Movement
                float x = values[0];
                float y = values[1];
                float z = values[2];

                String msg = "X value: " + Float.toString(x) +
                        "\nY value: " + Float.toString(y) +
                        "\nZ value: " + Float.toString(z);

                if(x<5.0 && z<5.0 && y>7.5){
                    msg += "\nVERTICAL";
                }
                else if(y<5.0 && z<5.0 && x>7.5){
                    msg += "\nHORIZONTAL A LA IZQUIERDA";
                }
                else if(y<5.0 && z<5.0 && x<-7.5){
                    msg += "\nHORIZONTAL A LA DERECHA";
                }
                else if(x<5.0 && y<5.0 && z>7.5){
                    msg += "\nBOCA ARRIBA";
                }
                else if(x<5.0 && y<5.0 && z<-7.5){
                    msg += "\nBOCA ABAJO";
                }
                else{
                    msg += "\nINDETERMINADA";
                }
                data.setText(msg);

                break;
            case Sensor.TYPE_LIGHT:
                float l = event.values[0];
                if (l<200){
                    colorCanvas.setImageResource(R.drawable.sun1);
                }
                else if (l<500){
                    colorCanvas.setImageResource(R.drawable.sun2);
                }
                else if (l<700){
                    colorCanvas.setImageResource(R.drawable.sun3);
                }
                else if (l<1400){
                    colorCanvas.setImageResource(R.drawable.sun4);
                }
                else if (l<2000){
                    colorCanvas.setImageResource(R.drawable.sun5);
                }
                else {
                    colorCanvas.setImageResource(R.drawable.sun6);
                }
                data.setText("Light value: " + Float.toString(l));
                break;
            case Sensor.TYPE_PROXIMITY:
                float p = event.values[0];
                colorCanvas.setImageResource(R.drawable.time_stone);
                if(p==0.0 && !isPlaying) {
                    gifImageView.startAnimation();
                    isPlaying = true;
                    //gifImageView.run();
                }
                else if(p==5.0 && isPlaying){
                    gifImageView.stopAnimation();
                    isPlaying = false;
                }
                data.setText("Proximity value: " + Float.toString(p));
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
