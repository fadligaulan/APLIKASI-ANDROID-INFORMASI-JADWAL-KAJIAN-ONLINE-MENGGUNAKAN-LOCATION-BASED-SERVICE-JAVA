package com.crp.infokajianislami;



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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

public class AudioActivity extends AppCompatActivity {
    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> DaftarAudio = new
            ArrayList<HashMap<String, String>>();
    private static String url_audio = "https://infokajianislami.fadligaulan.com/Json_audio";
    private static String url_judul_audio = "https://infokajianislami.fadligaulan.com/Json_audio";
    public static final String TAG_ID_AUDIO = "id";
    public static final String TAG_NAMA_MASJID = "nama_masjid";
    public static final String TAG_ALAMAT_MASJID = "alamat_masjid";
    public static final String TAG_CONTACT_MASJID = "contact_masjid";
    public static final String TAG_JUDUL_AUDIO = "judul_audio";
    public static final String TAG_DES_AUDIO = "des_audio";
    public static final String TAG_TGL_AUDIO = "tgl_audio";
    public static final String TAG_NAMA_USTAZD = "nama_ustadz";
    public static final String TAG_FILE_AUDIO = "file_audio";
    JSONArray string_json = null;
    ListView list;
    AudioAdapter adapter;
    private String cariNama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        Toolbar toolbarAudio = (Toolbar)findViewById(R.id.toolbaraudio);
        setSupportActionBar(toolbarAudio);


        getSupportActionBar().setTitle("Daftar Audio");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarAudio.setSubtitle("infokajianislami.fadligaulan.com");
        toolbarAudio.setLogo(R.drawable.logo_header1);
        toolbarAudio.setTitleTextColor(Color.WHITE);
        toolbarAudio.setSubtitleTextColor(Color.WHITE);

        DaftarAudio = new ArrayList<HashMap<String, String>>();
        new AmbilData().execute();
        list = (ListView) findViewById(R.id.listAudio);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> map = DaftarAudio.get(position);
                // Starting new intent
                Intent in = new Intent(getApplicationContext(), DetailAudio.class);
                in.putExtra(TAG_ID_AUDIO, map.get(TAG_ID_AUDIO));
                in.putExtra(TAG_FILE_AUDIO, map.get(TAG_FILE_AUDIO));
                startActivity(in);
            }
        });
    }
    public void SetListViewAdapter(ArrayList<HashMap<String,
            String>> audio) {
        adapter = new AudioAdapter(this, audio);
        list.setAdapter(adapter);
    }
    class AmbilData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AudioActivity.this);
            pDialog.setMessage("Mohon tunggu...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        protected String doInBackground(String... strings) {
            List<NameValuePair> params = new
                    ArrayList<NameValuePair>();

            JSONObject json = jParser.makeHttpRequest(url_audio,
                    "GET", params);
            Log.i("Ini nilai json ", ">" + json);
            try {
                string_json = json.getJSONArray("audio");
                for (int i = 0; i < string_json.length(); i++) {
                    JSONObject c = string_json.getJSONObject(i);
                    String id_audio = c.getString(TAG_ID_AUDIO);
                    String nama_masjid = c.getString(TAG_NAMA_MASJID);
                    String alamat_masjid = c.getString(TAG_ALAMAT_MASJID);
                    String kontak_masjid = c.getString(TAG_CONTACT_MASJID);
                    String judul_audio = c.getString(TAG_JUDUL_AUDIO);
                    String deskripsi_audio = c.getString(TAG_DES_AUDIO);
                    String tgl_audio = c.getString(TAG_TGL_AUDIO);
                    String nama_ustadz = c.getString(TAG_NAMA_USTAZD);
                    String file_audio = c.getString(TAG_FILE_AUDIO);

                    HashMap<String, String> map = new HashMap<String,
                            String>();
                    map.put(TAG_ID_AUDIO, id_audio);
                    map.put(TAG_NAMA_MASJID, nama_masjid);
                    map.put(TAG_ALAMAT_MASJID, alamat_masjid);
                    map.put(TAG_CONTACT_MASJID, kontak_masjid);
                    map.put(TAG_JUDUL_AUDIO, judul_audio);
                    map.put(TAG_DES_AUDIO, deskripsi_audio);
                    map.put(TAG_TGL_AUDIO, tgl_audio);
                    map.put(TAG_NAMA_USTAZD, nama_ustadz);
                    map.put(TAG_FILE_AUDIO, file_audio);
                    DaftarAudio.add(map);

                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    SetListViewAdapter(DaftarAudio);
                }
            });
        }
    }

    class AmbilDataAudio extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AudioActivity.this);
            pDialog.setMessage("Mohon tunggu...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        protected String doInBackground(String... strings) {
            List<NameValuePair> params1 = new
                    ArrayList<NameValuePair>();
            params1.add(new
                    BasicNameValuePair("judul_audio",cariNama));
            JSONObject json = jParser.makeHttpRequest(url_judul_audio,
                    "GET", params1);
            Log.i("Ini nilai json ", ">" + json);
            try {
                string_json = json.getJSONArray("audio");
                for (int i = 0; i < string_json.length(); i++) {
                    JSONObject c = string_json.getJSONObject(i);
                    String id_audio = c.getString(TAG_ID_AUDIO);
                    String nama_masjid = c.getString(TAG_NAMA_MASJID);
                    String alamat_masjid = c.getString(TAG_ALAMAT_MASJID);
                    String kontak_masjid = c.getString(TAG_CONTACT_MASJID);
                    String judul_audio = c.getString(TAG_JUDUL_AUDIO);
                    String deskripsi_audio = c.getString(TAG_DES_AUDIO);
                    String tgl_audio = c.getString(TAG_TGL_AUDIO);
                    String nama_ustadz = c.getString(TAG_NAMA_USTAZD);
                    String file_audio = c.getString(TAG_FILE_AUDIO);

                    HashMap<String, String> map = new HashMap<String,
                            String>();
                    map.put(TAG_ID_AUDIO, id_audio);
                    map.put(TAG_NAMA_MASJID, nama_masjid);
                    map.put(TAG_ALAMAT_MASJID, alamat_masjid);
                    map.put(TAG_CONTACT_MASJID, kontak_masjid);
                    map.put(TAG_JUDUL_AUDIO, judul_audio);
                    map.put(TAG_DES_AUDIO, deskripsi_audio);
                    map.put(TAG_TGL_AUDIO, tgl_audio);
                    map.put(TAG_NAMA_USTAZD, nama_ustadz);
                    map.put(TAG_FILE_AUDIO, file_audio);
                    DaftarAudio.add(map);
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
                    SetListViewAdapter(DaftarAudio);
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
                DaftarAudio.clear();
                adapter.notifyDataSetChanged();
                cariNama = query;
                new AmbilDataAudio().execute();
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
