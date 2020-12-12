package com.salesforcedrillers.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Objects;

public class StartReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {


        if (Objects.equals(intent.getAction(), Intent.ACTION_BOOT_COMPLETED)) {
            Intent i = new Intent();
            context.startService(i);
        }


        if (Objects.equals(intent.getAction(), Intent.ACTION_BOOT_COMPLETED)) {

            Toast.makeText(context, "Restart Service", Toast.LENGTH_LONG).show();


        }


    }
}
