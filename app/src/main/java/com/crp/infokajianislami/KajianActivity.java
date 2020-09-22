package com.crp.infokajianislami;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.widget.Toolbar;


import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;




import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class KajianActivity extends AppCompatActivity {
    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> DaftarKajian = new
            ArrayList<HashMap<String, String>>();
//    private static String url_kajian = "https://onlinecashier.id/infokajianislami/web/json/kajian.php";
//    private static String url_judul_kajian = "https://onlinecashier.id/infokajianislami/web/json/Judul_kajian.php";

    private static String url_kajian = "https://infokajianislami.fadligaulan.com/Json_kajian";
    private static String url_judul_kajian = "https://infokajianislami.fadligaulan.com/Json_kajian";

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


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kajian);
        Toolbar toolbarKajian = (Toolbar)findViewById(R.id.toolbarkajian);
        setSupportActionBar(toolbarKajian);


        getSupportActionBar().setTitle("Daftar Kajian");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarKajian.setSubtitle("infokajianislami.fadligaulan.com");
        toolbarKajian.setLogo(R.drawable.logo_header1);
        toolbarKajian.setTitleTextColor(Color.WHITE);
        toolbarKajian.setSubtitleTextColor(Color.WHITE);



        DaftarKajian = new ArrayList<HashMap<String, String>>();
        new AmbilData().execute();
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
            pDialog = new ProgressDialog(KajianActivity.this);
            pDialog.setMessage("Mohon tunggu...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        protected String doInBackground(String... strings) {
            List<NameValuePair> params = new
                    ArrayList<NameValuePair>();

            JSONObject json = jParser.makeHttpRequest(url_kajian,
                    "GET", params);
            Log.i("Ini nilai json ", ">" + json);
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

    class AmbilDataKajian extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(KajianActivity.this);
            pDialog.setMessage("Mohon tunggu...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        protected String doInBackground(String... strings) {
            List<NameValuePair> params1 = new
                    ArrayList<NameValuePair>();
            params1.add(new
                    BasicNameValuePair("judul_kajian", cariNama));
            JSONObject json = jParser.makeHttpRequest(url_judul_kajian,
                    "GET", params1);
            Log.i("Ini nilai json ", ">" + json);
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
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView=(SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                DaftarKajian.clear();
                adapter.notifyDataSetChanged();
                cariNama = query;
                new AmbilDataKajian().execute();
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return
                super.onCreateOptionsMenu(menu);
    }


}
