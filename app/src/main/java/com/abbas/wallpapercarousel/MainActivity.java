package com.abbas.wallpapercarousel;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    Intent mServiceIntent;
    Alarm alarm;
    Restarter restarter = new Restarter();
    Switch aSwitch;
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Copied and Pasted and a little bit improved
        alarm = new Alarm(this);
        mServiceIntent = new Intent(this,alarm.getClass());
        checkAndRequestPermissions();

        aSwitch = findViewById(R.id.switch1);

        if((isMyServiceRunning(alarm.getClass())) || (restarter.isOrderedBroadcast())) {
            aSwitch.setChecked(true);
        }


        try{
            File dir = null;
            dir = new File(Environment.getExternalStorageDirectory() + "/Abbas Carousel");
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }catch(Exception e){  }



        aSwitch.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(aSwitch.isChecked())
                            startintentservice();
                        else
                            stopintentservice();
                    }
                }
        );



    }

    public void startintentservice(){
        if(isMyServiceRunning(alarm.getClass())) {
            Toast.makeText(this, "Already Running", Toast.LENGTH_SHORT).show();
            return;
        }
        startService(mServiceIntent);
        registerReceiver(restarter, restarter.getFilter());
        Toast.makeText(this, "Carousel Enabled", Toast.LENGTH_SHORT).show();
    }

    public void stopintentservice(){
        if((!isMyServiceRunning(alarm.getClass()))||(!restarter.isOrderedBroadcast())){
            Toast.makeText(this, "Already Stopped", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            stopService(mServiceIntent);
            Toast.makeText(this, "Carousel Disabled", Toast.LENGTH_SHORT).show();
            unregisterReceiver(restarter);
        }catch(Exception e){
            Toast.makeText(this, "Exception "+ e, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        stopService(mServiceIntent);
        super.onDestroy();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass){
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service: manager.getRunningServices(Integer.MAX_VALUE)){
            if(serviceClass.getName().equals(service.service.getClassName())){
                return true;
            }
        }
        return false;
    }

    private void checkAndRequestPermissions() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},123);
        }
    }


}