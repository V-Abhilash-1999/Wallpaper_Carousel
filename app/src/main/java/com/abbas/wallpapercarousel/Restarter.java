package com.abbas.wallpapercarousel;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.util.Random;

public class Restarter extends BroadcastReceiver {
    public Restarter(){ }
    @Override
    public void onReceive(Context context, Intent intent) {
        if(
                (Intent.ACTION_USER_PRESENT.equals(intent.getAction()))||
                (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()))||
                (Intent.ACTION_SCREEN_ON.equals(intent.getAction()))
        ){
            setWallpaper(context);
        }
        context.startService(new Intent(context, Alarm.class));
    }
    public IntentFilter getFilter(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        return intentFilter;
    }

    public void setWallpaper(Context context){
        try {
            File dir =new File(Environment.getExternalStorageDirectory() + "/Abbas Carousel");
            File[] filelist = dir.listFiles();
            if(filelist==null){
                Toast.makeText(context, "Folder Empty", Toast.LENGTH_SHORT).show();
            }
            int totalfiles = filelist.length;
            Random random = new Random();
            int randint = random.nextInt(totalfiles);
            File images = filelist[randint];
            if(images.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(images.getAbsolutePath());
                WallpaperManager manager = WallpaperManager.getInstance(context);
                manager.setBitmap(bitmap);
            }
        } catch (Exception e) {
            // Toast.makeText(context, "Exception : " + e+" "+MainActivity.totalfiles, Toast.LENGTH_SHORT).show();
        }
    }

}

