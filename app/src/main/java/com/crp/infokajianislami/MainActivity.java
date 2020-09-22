package com.crp.infokajianislami;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Messenger;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    Button mnKajian, mnVideo, mnAudio, mnStreaming, mnMasjid, mnAbout;


    NotificationManager manager;
    Notification myNotication;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbarMain = (Toolbar)findViewById(R.id.toolbarmainactivity);
        setSupportActionBar(toolbarMain);


        getSupportActionBar().setTitle("Menu Utama");
        toolbarMain.setSubtitle("infokajianislami.fadligaulan.com");
        toolbarMain.setLogo(R.drawable.logo_header1);
        toolbarMain.setTitleTextColor(Color.WHITE);
        toolbarMain.setSubtitleTextColor(Color.WHITE);

        requestPermissions();

        String androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.i("ANDROID_DEVICE", " MY ANDROID ID IS ：" + androidId);

        mnKajian = (Button) findViewById(R.id.btnKajian);
        mnKajian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, KajianActivity.class);
                startActivity(i);
            }
        });


        mnAudio = (Button) findViewById(R.id.btnAudio);
        mnAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AudioActivity.class);
                startActivity(i);
            }
        });

        mnVideo = (Button) findViewById(R.id.btnVideo);
        mnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, VideoActivity.class);
                startActivity(i);
            }
        });

        mnStreaming = (Button) findViewById(R.id.btnStreaming);
        mnStreaming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, StreamingActivity.class);
                startActivity(i);
            }
        });

        mnMasjid = (Button) findViewById(R.id.btnMasjid);
        mnMasjid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MasjidActivity.class);
                startActivity(i);
            }
        });

        mnAbout = (Button) findViewById(R.id.btnKajianSekitar);
        mnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, KajianSekitarActivity.class);
                startActivity(i);
            }
        });

    }



    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            // Request permission
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
                finish();
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // can be schedule in this way also
                //  Utils.scheduleJob(this, LocationUpdatesService.class);
                //doing this way to communicate via messenger
                // Start service and provide it a way to communicate with this class.

                startService(new Intent(this, BackgroundLocationUpdateService.class));

            } else {
                // Permission denied.
                finish();
            }
        }
    }


}
