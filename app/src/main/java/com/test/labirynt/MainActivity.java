package com.test.labirynt;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements FragmentMenu.onZdarzenieListener {

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

    TextView bestTimeTv, lastTimeTv;

    int bestTimeMilis = 1000000000;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = (TabLayout) findViewById(R.id.tablayout_id);
        viewPager = (ViewPager) findViewById(R.id.viewpager_id);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new FragmentMenu(),"Menu");
        adapter.addFragment(new FragmentInfo(),"Info");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        sharedPreferences = getSharedPreferences("Times", MODE_PRIVATE);
        bestTimeMilis = restoreBestTimeInMilis();


    }


    @Override
    public void onZdarzenie() {
        bestTimeTv = findViewById(R.id.bestTimeValTv);
    }

    public void newGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if(resultCode == Activity.RESULT_OK) {
                String time = data.getStringExtra("time");
                int lastTimeMilis = Integer.parseInt(data.getStringExtra("timeMilis"));
                lastTimeTv = findViewById(R.id.lastTimeValTv);
                if(bestTimeMilis>lastTimeMilis) {
                    bestTimeMilis = lastTimeMilis;
                    Toast.makeText(getApplicationContext(), "New best time!", Toast.LENGTH_SHORT).show();
                    bestTimeTv.setText(time);
                }
                lastTimeTv.setText(time);
               // Toast.makeText(getApplicationContext(), "Your time - " + time, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public int restoreBestTimeInMilis(){
        return Integer.parseInt(sharedPreferences.getString("bestTimeMilis","1000000000"));
    }

    public void saveBestTimeInMilis(){
        SharedPreferences.Editor sharedEditor = sharedPreferences.edit();
        sharedEditor.putString("bestTimeMilis",String.valueOf(bestTimeMilis));
        sharedEditor.commit();
    }

    public void setBestTimeTv(){
            int milisec = bestTimeMilis % 1000;
            int seconds = bestTimeMilis / 1000;
            int minutes = seconds / 60;
            seconds = seconds % 60;
            bestTimeTv.setText(String.format("%d:%02d:%03d", minutes, seconds, milisec));

    }

    @Override
    protected void onStop() {
        super.onStop();
        saveBestTimeInMilis();
    }

    public void setBestTimeTv(View view){
        bestTimeTv = (TextView) view;
    }
}
//        List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_ALL);
//        String sensorss = "";
//        for(Sensor s:sensors){
//            System.out.println(s);
//            sensorss = sensors +","+ s.toString();
//        }
//        Toast.makeText(getApplicationContext(),sensorss,Toast.LENGTH_LONG).show();