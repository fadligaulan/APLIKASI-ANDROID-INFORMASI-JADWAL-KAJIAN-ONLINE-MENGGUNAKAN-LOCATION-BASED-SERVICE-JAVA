package com.crp.infokajianislami;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KajianSekitarActivity extends AppCompatActivity {
    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> DaftarKajian = new
            ArrayList<HashMap<String, String>>();
    private static String url_kajian = "https://infokajianislami.fadligaulan.com/Json_kajian_sekitar";



    private static String url_judul_kajian = "https://onlinecashier.id/infokajianislami/web/json/Judul_kajian.php";
    public static final String TAG_ID_KAJIAN = "id";
    public static final String TAG_NAMA_MASJID = "nama_masjid";
    public static final String TAG_ALAMAT_MASJID = "alamat_masjid";
    public static final String TAG_CONTACT_MASJID = "contact_masjid";
    public static final String TAG_JUDUL_KAJIAN = "judul_kajian";
    public static final String TAG_DESKRIPSI_KAJIAN = "deskripsi_kajian";
    public static final String TAG_TANGGAL_KAJIAN = "tgl_kajian";
    public static final String TAG_WAKTU_KAJIAN = "waktu_kajian";
    public static final String TAG_NAMA_USTADZ = "nama_ustadz";
    public static final String TAG_POSTER_KAJIAN = "poster_kajian";

    JSONArray string_json = null;
    ListView list;
    KajianAdapter adapter;
    private String cariNama;
    TextView info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kajian_sekitar);
        Toolbar toolbarKajianSekitar = (Toolbar)findViewById(R.id.toolbarkajiansekitar);
        setSupportActionBar(toolbarKajianSekitar);


        getSupportActionBar().setTitle("Daftar Kajian Sekitar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarKajianSekitar.setSubtitle("infokajianislami.fadligaulan.com");
        toolbarKajianSekitar.setLogo(R.drawable.logo_header1);
        toolbarKajianSekitar.setTitleTextColor(Color.WHITE);
        toolbarKajianSekitar.setSubtitleTextColor(Color.WHITE);

        DaftarKajian = new ArrayList<HashMap<String, String>>();
        new AmbilData().execute();
        info = (TextView) findViewById(R.id.infokj);
        list = (ListView) findViewById(R.id.listKajian);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> map = DaftarKajian.get(position);
                // Starting new intent
                Intent in = new Intent(getApplicationContext(), DetailKajian.class);
                in.putExtra(TAG_ID_KAJIAN, map.get(TAG_ID_KAJIAN));
                in.putExtra(TAG_POSTER_KAJIAN, map.get(TAG_POSTER_KAJIAN));
                startActivity(in);
            }
        });
    }
    public void SetListViewAdapter(ArrayList<HashMap<String,
            String>> kajian) {
        adapter = new KajianAdapter(this, kajian);
        list.setAdapter(adapter);
    }
    class AmbilData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(KajianSekitarActivity.this);
            pDialog.setMessage("Mohon tunggu...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        protected String doInBackground(String... strings) {
            List<NameValuePair> params = new
                    ArrayList<NameValuePair>();
            String androidId = Settings.Secure.getString(getContentResolver(),
                    Settings.Secure.ANDROID_ID);

            params.add(new
                    BasicNameValuePair("device_code", androidId));
            JSONObject json = jParser.makeHttpRequest(url_kajian,
                    "GET", params);


            Log.i("Ini nilai json ", "------------------------------------------------------------------------------->" + json);
            try {
                string_json = json.getJSONArray("kajian");
                for (int i = 0; i < string_json.length(); i++) {
                    JSONObject c = string_json.getJSONObject(i);
                    String id_kajian = c.getString(TAG_ID_KAJIAN);
                    String nama_masjid = c.getString(TAG_NAMA_MASJID);
                    String alamat_masjid = c.getString(TAG_ALAMAT_MASJID);
                    String kontak_masjid = c.getString(TAG_CONTACT_MASJID);
                    String judul_kajian = c.getString(TAG_JUDUL_KAJIAN);
                    String deskripsi_kajian = c.getString(TAG_DESKRIPSI_KAJIAN);
                    String tanggal_kajian = c.getString(TAG_TANGGAL_KAJIAN);
                    String waktu_kajian = c.getString(TAG_WAKTU_KAJIAN);
                    String nama_ustadz = c.getString(TAG_NAMA_USTADZ);
                    String poster_kajian = c.getString(TAG_POSTER_KAJIAN);
                    HashMap<String, String> map = new HashMap<String,
                            String>();
                    map.put(TAG_ID_KAJIAN, id_kajian);
                    map.put(TAG_NAMA_MASJID, nama_masjid);
                    map.put(TAG_ALAMAT_MASJID, alamat_masjid);
                    map.put(TAG_CONTACT_MASJID, kontak_masjid);
                    map.put(TAG_JUDUL_KAJIAN, judul_kajian);
                    map.put(TAG_DESKRIPSI_KAJIAN, deskripsi_kajian);
                    map.put(TAG_TANGGAL_KAJIAN, tanggal_kajian);
                    map.put(TAG_WAKTU_KAJIAN, waktu_kajian);
                    map.put(TAG_NAMA_USTADZ, nama_ustadz);
                    map.put(TAG_POSTER_KAJIAN, poster_kajian);
                    DaftarKajian.add(map);
                }
                Log.i("CEK JUMLAH KAJIAN ", "JUMLAH : " + string_json.length());

//                if (string_json.length() < 1 ){
//                    info.setText("Tidak Ada Data Kajian Sekitar");
//                } else {
//                    info.setText("");
//                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    SetListViewAdapter(DaftarKajian);
                }
            });
        }
    }
}
