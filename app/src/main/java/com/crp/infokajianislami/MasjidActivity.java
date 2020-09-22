package com.crp.infokajianislami;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
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

public class MasjidActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> DaftarMasjid = new
            ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> NamaMasjid = new
            ArrayList<HashMap<String, String>>();

    private static String url_masjid = "https://infokajianislami.fadligaulan.com/Json_masjid";
    private static String url_nama_masjid = "https://infokajianislami.fadligaulan.com/Json_masjid";
    public static final String TAG_ID_MASJID = "id";
    public static final String TAG_NAMA_MASJID = "nama_masjid";
    public static final String TAG_ALAMAT_MASJID = "alamat_masjid";
    public static final String TAG_THN_BERDIRI = "thn_berdiri";
    public static final String TAG_DES_MASJID = "des_masjid";
    public static final String TAG_CONTACT_MASJID = "contact_masjid";
    public static final String TAG_LAT = "lat";
    public static final String TAG_LNG = "lng";
    public static final String TAG_FOTO_MASJID = "foto_masjid";

    JSONArray string_json = null;
    ListView list;
    MasjidAdapter adapter;
    private String cariNama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masjid);
        Toolbar toolbarMasjid = (Toolbar)findViewById(R.id.toolbarmasjid);
        setSupportActionBar(toolbarMasjid);


        getSupportActionBar().setTitle("Daftar Masjid");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarMasjid.setSubtitle("infokajianislami.fadligaulan.com");
        toolbarMasjid.setLogo(R.drawable.logo_header1);
        toolbarMasjid.setTitleTextColor(Color.WHITE);
        toolbarMasjid.setSubtitleTextColor(Color.WHITE);


        DaftarMasjid = new ArrayList<HashMap<String, String>>();
        NamaMasjid = new ArrayList<HashMap<String, String>>();
        new AmbilData().execute();
        list = (ListView) findViewById(R.id.listMasjid);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> map = DaftarMasjid.get(position);
                // Starting new intent
                Intent in = new Intent(getApplicationContext(), DetailMasjid.class);
                in.putExtra(TAG_ID_MASJID, map.get(TAG_ID_MASJID));
                in.putExtra(TAG_FOTO_MASJID, map.get(TAG_FOTO_MASJID));
                startActivity(in);
            }
        });
    }
    public void SetListViewAdapter(ArrayList<HashMap<String,
            String>> masjid) {
        adapter = new MasjidAdapter(this, masjid);
        list.setAdapter(adapter);
    }

    class AmbilData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MasjidActivity.this);
            pDialog.setMessage("Mohon tunggu...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        protected String doInBackground(String... strings) {
            List<NameValuePair> params = new
                    ArrayList<NameValuePair>();

            JSONObject json = jParser.makeHttpRequest(url_masjid,
                    "GET", params);

            Log.i("Ini nilai json ", ">" + json);
            try {
                string_json = json.getJSONArray("masjid");
                for (int i = 0; i < string_json.length(); i++) {
                    JSONObject c = string_json.getJSONObject(i);
                    String id_masjid = c.getString(TAG_ID_MASJID);
                    String nama_masjid = c.getString(TAG_NAMA_MASJID);
                    String alamat_masjid = c.getString(TAG_ALAMAT_MASJID);
                    String thn_berdiri = c.getString(TAG_THN_BERDIRI);
                    String des_masjid = c.getString(TAG_DES_MASJID);
                    String kontak_masjid = c.getString(TAG_CONTACT_MASJID);
                    String lat = c.getString(TAG_LAT);
                    String lng = c.getString(TAG_LNG);
                    String foto_masjid = c.getString(TAG_FOTO_MASJID);
                    HashMap<String, String> map = new HashMap<String,
                            String>();
                    map.put(TAG_ID_MASJID, id_masjid);
                    map.put(TAG_NAMA_MASJID, nama_masjid);
                    map.put(TAG_ALAMAT_MASJID, alamat_masjid);
                    map.put(TAG_THN_BERDIRI, thn_berdiri);
                    map.put(TAG_DES_MASJID, des_masjid);
                    map.put(TAG_CONTACT_MASJID, kontak_masjid);
                    map.put(TAG_LAT, lat);
                    map.put(TAG_LNG, lng);
                    map.put(TAG_FOTO_MASJID, foto_masjid);
                    DaftarMasjid.add(map);

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
                    SetListViewAdapter(DaftarMasjid);
                }
            });
        }
    }
    class AmbilDataMasjid extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MasjidActivity.this);
            pDialog.setMessage("Mohon tunggu...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... params) {
            List<NameValuePair> params1 = new
                    ArrayList<NameValuePair>();
            params1.add(new
                    BasicNameValuePair("nama_masjid", cariNama));
            JSONObject json = jParser.makeHttpRequest(url_nama_masjid,
                    "GET", params1);
            Log.i("Ini nilai json ", ">" + json);
            try {
                string_json = json.getJSONArray("masjid");
                for (int i = 0; i < string_json.length(); i++) {
                    JSONObject c = string_json.getJSONObject(i);
                    String id_masjid = c.getString(TAG_ID_MASJID);
                    String nama_masjid = c.getString(TAG_NAMA_MASJID);
                    String alamat_masjid = c.getString(TAG_ALAMAT_MASJID);
                    String thn_berdiri = c.getString(TAG_THN_BERDIRI);
                    String des_masjid = c.getString(TAG_DES_MASJID);
                    String kontak_masjid = c.getString(TAG_CONTACT_MASJID);
                    String lat = c.getString(TAG_LAT);
                    String lng = c.getString(TAG_LNG);
                    String foto_masjid = c.getString(TAG_FOTO_MASJID);
                    HashMap<String, String> map = new HashMap<String,
                            String>();
                    map.put(TAG_ID_MASJID, id_masjid);
                    map.put(TAG_NAMA_MASJID, nama_masjid);
                    map.put(TAG_ALAMAT_MASJID, alamat_masjid);
                    map.put(TAG_THN_BERDIRI, thn_berdiri);
                    map.put(TAG_DES_MASJID, des_masjid);
                    map.put(TAG_CONTACT_MASJID, kontak_masjid);
                    map.put(TAG_LAT, lat);
                    map.put(TAG_LNG, lng);
                    map.put(TAG_FOTO_MASJID, foto_masjid);
                    DaftarMasjid.add(map);
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
                    SetListViewAdapter(DaftarMasjid);
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
                DaftarMasjid.clear();
                adapter.notifyDataSetChanged();
                cariNama = query;
                new AmbilDataMasjid().execute();
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
