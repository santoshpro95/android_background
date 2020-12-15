package com.salesforcedrillers.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import static androidx.core.app.NotificationCompat.PRIORITY_MIN;

public class DemoService extends Service implements LocationListener {

    private static final int NOTIF_ID = 123;
    Timer timer = new Timer();
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    private MediaPlayer player;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        // location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
        }

        //getting systems default ringtone
        player = MediaPlayer.create(this,
                Settings.System.DEFAULT_ALARM_ALERT_URI);
        //setting loop play to true
        //this will make the ringtone continuously playing
        player.setLooping(true);
        //staring the player
        player.start();


        // check in console
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.i("current Time", String.valueOf(Calendar.getInstance().getTime()));
            }
        }, 0, 1000);

        return Service.START_STICKY;
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.d("Latitude", String.valueOf(location.getLatitude()));
        Log.d("Longitude", String.valueOf(location.getLongitude()));
    }

    @Override
    public void onCreate() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? createNotificationChannel(notificationManager) : "";
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(PRIORITY_MIN)
                .setSubText("Running")
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .build();

        startForeground(NOTIF_ID, notification);
        super.onCreate();
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel(NotificationManager notificationManager) {
        String channelId = "my_service_channelid";
        String channelName = "My Foreground Service";
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
        // omitted the LED color
        channel.setImportance(NotificationManager.IMPORTANCE_NONE);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        notificationManager.createNotificationChannel(channel);
        return channelId;
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
        player.stop();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }
}
