package com.crp.infokajianislami;

import android.app.Application;
import android.content.Intent;

public class LocationApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        startService(new Intent(this, LocationService.class));
    }
}
