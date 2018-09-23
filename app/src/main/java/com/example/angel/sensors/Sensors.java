package com.example.angel.sensors;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.hardware.Sensor;

import java.util.ArrayList;
import java.util.List;

public class Sensors extends AppCompatActivity {

    private SensorManager mSensorManager;
    ListView simpleList;
    List sensorList = new ArrayList<String>();
    List<Sensor> aviableSensors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        aviableSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        for(int i=0; i<aviableSensors.size(); i++){
            sensorList.add(aviableSensors.get(i).getName());
        }

        simpleList = findViewById(R.id.simpleListView);
        ListAdapter arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_listview, R.id.textView, sensorList);
        simpleList.setAdapter(arrayAdapter);

        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(Sensors.this, SensorDetail.class);
                intent.putExtra("SENSOR_SELECTED", aviableSensors.get(position).getType());
                startActivity(intent);
            }
        });
    }
}
