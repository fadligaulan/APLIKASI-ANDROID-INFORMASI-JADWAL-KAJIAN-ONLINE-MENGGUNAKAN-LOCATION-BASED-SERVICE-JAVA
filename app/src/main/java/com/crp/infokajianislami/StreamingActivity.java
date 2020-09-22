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

public class StreamingActivity extends AppCompatActivity {
    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> DaftarStreaming = new
            ArrayList<HashMap<String, String>>();
    private static String url_streaming = "https://infokajianislami.fadligaulan.com/Json_streaming";
    private static String url_judul_streaming = "https://infokajianislami.fadligaulan.com/Json_streaming";
    public static final String TAG_ID = "id";
    public static final String TAG_NAMA_MASJID = "nama_masjid";
    public static final String TAG_ALAMAT_MASJID = "alamat_masjid";
    public static final String TAG_CONTACT_MASJID = "contact_masjid";
    public static final String TAG_JUDUL_STREAMING = "judul_streaming";
    public static final String TAG_DES_STREAMING = "des_streaming";
    public static final String TAG_TGL_STREAMING_ = "tgl_streaming";
    public static final String TAG_NAMA_USTADZ = "nama_ustadz";
    public static final String TAG_URL_STREAMING = "url_streaming";
    JSONArray string_json = null;
    ListView list;
    StreamingAdapter adapter;
    private String cariNama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streaming);
        Toolbar toolbarStreaming = (Toolbar)findViewById(R.id.toolbarstreaming);
        setSupportActionBar(toolbarStreaming);


        getSupportActionBar().setTitle("Daftar Streaming");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarStreaming.setSubtitle("infokajianislami.fadligaulan.com");
        toolbarStreaming.setLogo(R.drawable.logo_header1);
        toolbarStreaming.setTitleTextColor(Color.WHITE);
        toolbarStreaming.setSubtitleTextColor(Color.WHITE);


        DaftarStreaming = new ArrayList<HashMap<String, String>>();
        new AmbilData().execute();
        list = (ListView) findViewById(R.id.listStreaming);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> map = DaftarStreaming.get(position);
                // Starting new intent
                Intent in = new Intent(getApplicationContext(), DetailYoutube.class);
                in.putExtra(TAG_ID, map.get(TAG_ID));
                in.putExtra(TAG_URL_STREAMING, map.get(TAG_URL_STREAMING));
                startActivity(in);
            }
        });
    }
    public void SetListViewAdapter(ArrayList<HashMap<String,
            String>> streaming) {
        adapter = new StreamingAdapter(this, streaming);
        list.setAdapter(adapter);
    }
    class AmbilData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(StreamingActivity.this);
            pDialog.setMessage("Mohon tunggu...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... strings) {
            List<NameValuePair> params = new
                    ArrayList<NameValuePair>();
            JSONObject json = jParser.makeHttpRequest(url_streaming,
                    "GET", params);
            Log.i("Ini nilai json ", ">" + json);
            try {
                string_json = json.getJSONArray("streaming");
                for (int i = 0; i < string_json.length(); i++) {
                    JSONObject c = string_json.getJSONObject(i);
                    String id_streaming = c.getString(TAG_ID);
                    String nama_masjid = c.getString(TAG_NAMA_MASJID);
                    String alamat_masjid = c.getString(TAG_ALAMAT_MASJID);
                    String kontak_masjid = c.getString(TAG_CONTACT_MASJID);
                    String judul_streaming = c.getString(TAG_JUDUL_STREAMING);
                    String des_streaming = c.getString(TAG_DES_STREAMING);
                    String tgl_streaming = c.getString(TAG_TGL_STREAMING_);

                    String nama_ustadz = c.getString(TAG_NAMA_USTADZ);
                    String url_streaming = c.getString(TAG_URL_STREAMING);

                    HashMap<String, String> map = new HashMap<String,
                            String>();
                    map.put(TAG_ID, id_streaming);
                    map.put(TAG_NAMA_MASJID, nama_masjid);
                    map.put(TAG_ALAMAT_MASJID, alamat_masjid);
                    map.put(TAG_CONTACT_MASJID, kontak_masjid);
                    map.put(TAG_JUDUL_STREAMING, judul_streaming);
                    map.put(TAG_DES_STREAMING, des_streaming);
                    map.put(TAG_TGL_STREAMING_, tgl_streaming);

                    map.put(TAG_NAMA_USTADZ, nama_ustadz);
                    map.put(TAG_URL_STREAMING, url_streaming);
                    DaftarStreaming.add(map);

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
                    SetListViewAdapter(DaftarStreaming);
                }
            });
        }
    }

        class AmbilDataStreaming extends AsyncTask<String, String, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(StreamingActivity.this);
                pDialog.setMessage("Mohon tunggu...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();
            }
            protected String doInBackground(String... params) {
                List<NameValuePair> params1 = new
                        ArrayList<NameValuePair>();
                params1.add(new
                        BasicNameValuePair("judul_streaming", cariNama));
                JSONObject json = jParser.makeHttpRequest(url_judul_streaming,
                        "GET", params1);
                Log.i("Ini nilai json ", ">" + json);
                try {
                    string_json = json.getJSONArray("streaming");
                    for (int i = 0; i < string_json.length(); i++) {
                        JSONObject c = string_json.getJSONObject(i);
                        String id_streaming = c.getString(TAG_ID);
                        String nama_masjid = c.getString(TAG_NAMA_MASJID);
                        String alamat_masjid = c.getString(TAG_ALAMAT_MASJID);
                        String kontak_masjid = c.getString(TAG_CONTACT_MASJID);
                        String judul_streaming = c.getString(TAG_JUDUL_STREAMING);
                        String des_streaming = c.getString(TAG_DES_STREAMING);
                        String tgl_streaming = c.getString(TAG_TGL_STREAMING_);

                        String nama_ustadz = c.getString(TAG_NAMA_USTADZ);
                        String url_streaming = c.getString(TAG_URL_STREAMING);

                        HashMap<String, String> map = new HashMap<String,
                                String>();
                        map.put(TAG_ID, id_streaming);
                        map.put(TAG_NAMA_MASJID, nama_masjid);
                        map.put(TAG_ALAMAT_MASJID, alamat_masjid);
                        map.put(TAG_CONTACT_MASJID, kontak_masjid);
                        map.put(TAG_JUDUL_STREAMING, judul_streaming);
                        map.put(TAG_DES_STREAMING, des_streaming);
                        map.put(TAG_TGL_STREAMING_, tgl_streaming);

                        map.put(TAG_NAMA_USTADZ, nama_ustadz);
                        map.put(TAG_URL_STREAMING, url_streaming);
                        DaftarStreaming.add(map);
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
                        SetListViewAdapter(DaftarStreaming);
                    }
                });
            }
        }

        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_search, menu);
            MenuItem searchItem = menu.findItem(R.id.search);
            final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    DaftarStreaming.clear();
                    adapter.notifyDataSetChanged();
                    cariNama = query;
                    new AmbilDataStreaming().execute();
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