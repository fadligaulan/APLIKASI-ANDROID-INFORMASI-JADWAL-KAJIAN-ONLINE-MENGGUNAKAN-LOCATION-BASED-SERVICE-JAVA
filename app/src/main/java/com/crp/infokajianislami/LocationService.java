package com.crp.infokajianislami;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LocationService extends Service {

    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> DaftarKajian = new
            ArrayList<HashMap<String, String>>();
    private static String url_kajian = "https://onlinecashier.id/infokajianislami/Nilai/test";


    JSONArray string_json = null;
    ListView list;
    KajianAdapter adapter;
    private String cariNama;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // do your jobs here

        int a = 1;

        final Handler handler = new Handler();
        Runnable task = new Runnable() {
            @Override
            public void run() {
                //getLastLocation();
                DaftarKajian = new ArrayList<HashMap<String, String>>();
//                new AmbilData().execute();
                handler.postDelayed(this, 3000);
            }
        };
        handler.post(task);


        return super.onStartCommand(intent, flags, startId);
    }

    class AmbilData extends AsyncTask<String, String, String> {

        protected String doInBackground(String... strings) {
            List<NameValuePair> params = new
                    ArrayList<NameValuePair>();

            JSONObject json = jParser.makeHttpRequest(url_kajian,
                    "GET", params);
            Log.i("Ini nilai json ", ">" + json);

            return null;
        }
    }
}
