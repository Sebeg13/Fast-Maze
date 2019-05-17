package com.test.labirynt;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FragmentInfo.onZdarzenieListener, SensorEventListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    SensorManager mySM;

    public SensorManager getMySM() {
        return mySM;
    }

    public Sensor getMySensor() {
        return mySensor;
    }

    Sensor mySensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = (TabLayout) findViewById(R.id.tablayout_id);
        viewPager = (ViewPager) findViewById(R.id.viewpager_id);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new FragmentMenu(),"Menu");
        adapter.addFragment(new FragmentGame(),"Second");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        mySM = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mySensor = mySM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mySM.registerListener(MainActivity.this,mySensor,SensorManager.SENSOR_DELAY_GAME);


    }

    @Override
    public void onZdarzenie() {


    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        TextView textView = findViewById(R.id.textView);


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void newGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
}
//        List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_ALL);
//        String sensorss = "";
//        for(Sensor s:sensors){
//            System.out.println(s);
//            sensorss = sensors +","+ s.toString();
//        }
//        Toast.makeText(getApplicationContext(),sensorss,Toast.LENGTH_LONG).show();