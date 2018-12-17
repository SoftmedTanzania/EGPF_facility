package com.softmed.htmr_facility.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.softmed.htmr_facility.services.PostOfficeService;

/**
 * Created by issy on 1/6/18.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferralApp
 */

public class AlarmReceiver extends BroadcastReceiver {

    public static final int REQUEST_CODE = 12345;
    public static final String ACTION = "com.codepath.example.servicesdemo.alarm";

    // Triggered by the Alarm periodically (starts the service to run task)
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, PostOfficeService.class);
        //i.putExtra("foo", "bar");
        context.startService(i);
    }

}
