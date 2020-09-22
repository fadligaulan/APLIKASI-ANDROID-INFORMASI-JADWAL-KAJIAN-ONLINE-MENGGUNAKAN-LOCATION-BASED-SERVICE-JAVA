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

public class VideoActivity extends AppCompatActivity {
    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> DaftarVideo = new
            ArrayList<HashMap<String, String>>();
    private static String url_video = "https://infokajianislami.fadligaulan.com/Json_video";
    private static String url_judul_video = "https://infokajianislami.fadligaulan.com/Json_video";
    public static final String TAG_ID_VIDEO = "id";
    public static final String TAG_NAMA_MASJID = "nama_masjid";
    public static final String TAG_ALAMAT_MASJID = "alamat_masjid";
    public static final String TAG_CONTACT_MASJID = "contact_masjid";
    public static final String TAG_JUDUL_VIDEO = "judul_video";
    public static final String TAG_DES_VIDEO = "des_video";
    public static final String TAG_TGL_VIDEO = "tgl_video";
    public static final String TAG_NAMA_USTADZ = "nama_ustadz";
    public static final String TAG_FILE_VIDEO = "url_video";


    JSONArray string_json = null;
    ListView list;
    VideoAdapter adapter;
    private String cariNama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        Toolbar toolbarVideo = (Toolbar)findViewById(R.id.toolbarvideo);
        setSupportActionBar(toolbarVideo);


        getSupportActionBar().setTitle("Daftar Video");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarVideo.setSubtitle("infokajianislami.fadligaulan.com");
        toolbarVideo.setLogo(R.drawable.logo_header1);
        toolbarVideo.setTitleTextColor(Color.WHITE);
        toolbarVideo.setSubtitleTextColor(Color.WHITE);

        DaftarVideo = new ArrayList<HashMap<String, String>>();
        new AmbilData().execute();
        list = (ListView) findViewById(R.id.listVideo);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> map = DaftarVideo.get(position);
                // Starting new intent
                Intent in = new Intent(getApplicationContext(), DetailVideo.class);
                in.putExtra(TAG_ID_VIDEO, map.get(TAG_ID_VIDEO));
                in.putExtra(TAG_FILE_VIDEO, map.get(TAG_FILE_VIDEO));
                startActivity(in);
            }
        });
    }
    public void SetListViewAdapter(ArrayList<HashMap<String,
            String>> video) {
        adapter = new VideoAdapter(this, video);
        list.setAdapter(adapter);
    }

    class AmbilData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(VideoActivity.this);
            pDialog.setMessage("Mohon tunggu...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        protected String doInBackground(String... strings) {
            List<NameValuePair> params = new
                    ArrayList<NameValuePair>();

            JSONObject json = jParser.makeHttpRequest(url_video,
                    "GET", params);

            Log.i("Ini nilai json ", ">" + json);
            try {
                string_json = json.getJSONArray("video");
                for (int i = 0; i < string_json.length(); i++) {
                    JSONObject c = string_json.getJSONObject(i);
                    String id_video = c.getString(TAG_ID_VIDEO);
                    String nama_masjid = c.getString(TAG_NAMA_MASJID);
                    String alamat_masjid = c.getString(TAG_ALAMAT_MASJID);
                    String kontak_masjid = c.getString(TAG_CONTACT_MASJID);
                    String judul_video= c.getString(TAG_JUDUL_VIDEO);
                    String deskripsi_video = c.getString(TAG_DES_VIDEO);
                    String tgl_video = c.getString(TAG_TGL_VIDEO);

                    String nama_ustadz = c.getString(TAG_NAMA_USTADZ);
                    String file_video = c.getString(TAG_FILE_VIDEO);

                    HashMap<String, String> map = new HashMap<String,
                            String>();
                    map.put(TAG_ID_VIDEO, id_video);
                    map.put(TAG_NAMA_MASJID, nama_masjid);
                    map.put(TAG_ALAMAT_MASJID, alamat_masjid);
                    map.put(TAG_CONTACT_MASJID, kontak_masjid);
                    map.put(TAG_JUDUL_VIDEO, judul_video);
                    map.put(TAG_DES_VIDEO, deskripsi_video);
                    map.put(TAG_TGL_VIDEO, tgl_video);

                    map.put(TAG_NAMA_USTADZ, nama_ustadz);
                    map.put(TAG_FILE_VIDEO, file_video);
                    DaftarVideo.add(map);
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
                    SetListViewAdapter(DaftarVideo);
                }
            });
        }

    }

    class AmbilDataVideo extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(VideoActivity.this);
            pDialog.setMessage("Mohon tunggu...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        protected String doInBackground(String... strings) {
            List<NameValuePair> params1 = new
                    ArrayList<NameValuePair>();
            params1.add(new
                    BasicNameValuePair("judul_video",cariNama));
            JSONObject json = jParser.makeHttpRequest(url_judul_video,
                    "GET", params1);
            Log.i("Ini nilai json ", ">" + json);
            try{
                string_json = json.getJSONArray("video");
                for (int i = 0; i < string_json.length(); i++) {
                    JSONObject c = string_json.getJSONObject(i);
                    String id_video = c.getString(TAG_ID_VIDEO);
                    String nama_masjid = c.getString(TAG_NAMA_MASJID);
                    String alamat_masjid = c.getString(TAG_ALAMAT_MASJID);
                    String kontak_masjid = c.getString(TAG_CONTACT_MASJID);
                    String judul_video= c.getString(TAG_JUDUL_VIDEO);
                    String deskripsi_video = c.getString(TAG_DES_VIDEO);
                    String tgl_video = c.getString(TAG_TGL_VIDEO);

                    String nama_ustadz = c.getString(TAG_NAMA_USTADZ);
                    String file_video = c.getString(TAG_FILE_VIDEO);

                    HashMap<String, String> map = new HashMap<String,
                            String>();
                    map.put(TAG_ID_VIDEO, id_video);
                    map.put(TAG_NAMA_MASJID, nama_masjid);
                    map.put(TAG_ALAMAT_MASJID, alamat_masjid);
                    map.put(TAG_CONTACT_MASJID, kontak_masjid);
                    map.put(TAG_JUDUL_VIDEO, judul_video);
                    map.put(TAG_DES_VIDEO, deskripsi_video);
                    map.put(TAG_TGL_VIDEO, tgl_video);

                    map.put(TAG_NAMA_USTADZ, nama_ustadz);
                    map.put(TAG_FILE_VIDEO, file_video);
                    DaftarVideo.add(map);
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
                    SetListViewAdapter(DaftarVideo);
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
                DaftarVideo.clear();
                adapter.notifyDataSetChanged();
                cariNama = query;
                new AmbilDataVideo().execute();
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
