package com.crp.infokajianislami;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DetailMasjid extends AppCompatActivity {
    public ImageLoader imageLoader;
    {
        imageLoader = new ImageLoader(null);
    }

    String goolgeMap = "com.google.android.apps.maps"; // identitas package aplikasi google masps android
    Uri gmmIntentUri;
    Intent mapIntent;

    ListView list;

    ArrayList<HashMap<String, String>> DaftarKajian = new
            ArrayList<HashMap<String, String>>();

    KajianAdapter adapter;

    ImageView pin,telp,thn;

    JSONArray string_json = null;
    String idmasjid, namaMasjid, lat, longi;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    public static final String TAG_IDMASJID = "id";
    public static final String TAG_NAMA_MASJID = "nama_masjid";
    public static final String TAG_ALAMAT_MASJID = "alamat_masjid";
    public static final String TAG_TAHUN = "thn_berdiri";
    public static final String TAG_DES_MASJID= "des_masjid";
    public static final String TAG_CONTACT_MASJID = "contact_masjid";
    public static final String TAG_LATITUDE = "lat";
    public static final String TAG_LONGITUDE = "lng";
    public static final String TAG_FOTO_MASJID = "foto_masjid";
    private static final String url_detail_masjid = "https://infokajianislami.fadligaulan.com/Json_detail_masjid";
//    private static final String url_detail_masjid = "https://fadligaulan.com/DetailMasjid.php";
    //https://fadligaulan.com/DetailMasjid.php

//    public static final String TAG_ID = "id";
//    public static final String TAG_NAMA_KAJIAN = "nama_masjid";
//    public static final String TAG_ALAMAT_KAJIAN = "alamat_masjid";
//    public static final String TAG_CONTACT_KAJIAN = "contact_masjid";
//    public static final String TAG_JUDUL_KAJIAN = "judul_kajian";
//    public static final String TAG_DESKRIPSI_KAJIAN = "deskripsi_kajian";
//    public static final String TAG_TGL_KAJIAN = "tgl_kajian";
//    public static final String TAG_WAKTU_KAJIAN = "waktu_kajian";
//    public static final String TAG_NAMA_USTADZ = "nama_ustadz";
//    public static final String TAG_POSTER_KAJIAN = "poster_kajian";
//
//    private static final String url_kajian_masjid =
//            "https://onlinecashier.id/infokajianislami/web/json/KajianMasjid.php";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_masjid);

        pin = (ImageView) findViewById(R.id.img_pin_maps_masjid);
        telp = (ImageView) findViewById(R.id.img_tlpn_masjid);
        thn = (ImageView) findViewById(R.id.img_calender_masjid);
        Intent i = getIntent();
        idmasjid = i.getStringExtra(TAG_IDMASJID);
        namaMasjid = i.getStringExtra(TAG_NAMA_MASJID);

        new AmbilDetailMasjid().execute();
        Button rute = (Button) findViewById(R.id.btn_petunjuk);
        rute.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Buat Uri dari intent string. Gunakan hasilnya untuk membuat Intent.
                gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + longi);

                // Buat Uri dari intent gmmIntentUri. Set action => ACTION_VIEW
                mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

                // Set package Google Maps untuk tujuan aplikasi yang di Intent yaitu google maps
                mapIntent.setPackage(goolgeMap);

                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);

                } else {
                    Toast.makeText(DetailMasjid.this, "Google Maps Belum Terinstal. Install Terlebih dahulu.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

//        new AmbilDataKajianMasjid().execute();
//        list = (ListView) findViewById(R.id.list);
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                HashMap<String, String> map = DaftarKajian.get(position);
//                // Starting new intent
//                Intent in = new Intent(getApplicationContext(), DetailKajian.class);
//                in.putExtra(TAG_ID, map.get(TAG_ID));
//                in.putExtra(TAG_POSTER_KAJIAN, map.get(TAG_POSTER_KAJIAN));
//                startActivity(in);
//            }
//        });
    }

//    public void SetListViewAdapter(ArrayList<HashMap<String,
//            String>> kajian) {
//        adapter = new KajianAdapter(this, kajian);
//        list.setAdapter(adapter);
//
//    }
    class AmbilDetailMasjid extends AsyncTask<String, String,
            String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DetailMasjid.this);
            pDialog.setMessage("Mohon Tunggu ... !");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> params1 = new
                        ArrayList<NameValuePair>();
                params1.add(new
                        BasicNameValuePair("id_masjid",idmasjid));
                JSONObject json = jsonParser.makeHttpRequest(
                        url_detail_masjid, "GET", params1);
                string_json = json.getJSONArray("detail_masjid");
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        ImageView thumb_image = (ImageView) findViewById(R.id.gambarMasjid);
                        TextView nama = (TextView) findViewById(R.id.nama);
                        TextView alamat = (TextView) findViewById(R.id.alamat);
                        TextView telpon = (TextView) findViewById(R.id.notelp);
                        TextView tahun = (TextView) findViewById(R.id.tahun);
                        TextView deskripsi = (TextView) findViewById(R.id.deskripsiMasjid);

                        try{
                            // ambil objek member pertama dari JSON Array
                            JSONObject ar =
                                    string_json.getJSONObject(0);
                            String nama_d = ar.getString(TAG_NAMA_MASJID);
                            String alamat_d = ar.getString(TAG_ALAMAT_MASJID);
                            String telpon_d = ar.getString(TAG_CONTACT_MASJID);
                            String tahun_d = ar.getString(TAG_TAHUN);
                            String deskripsi_d = ar.getString(TAG_DES_MASJID);
                            String lat_d = ar.getString(TAG_LATITUDE);
                            String long_d = ar.getString(TAG_LONGITUDE);
                            String url_detail_image = ar.getString(TAG_FOTO_MASJID);

                            nama.setText(nama_d);
                            alamat.setText(alamat_d);
                            telpon.setText(telpon_d);
                            tahun.setText(tahun_d);
                            deskripsi.setText(deskripsi_d);

                            Picasso.with(getApplicationContext())
                                    .load(url_detail_image)
                                    .error(R.drawable.no_image)
                                    .into(thumb_image);

                            lat = lat_d;
                            longi = long_d;

//                            imageLoader.DisplayImage(ar.getString(TAG_FOTO_MASJID),thumb_image);
//                            Picasso.with(getApplicationContext())
//                                    .load(url_detail_image)
//                                    .error(R.drawable.no_image)
//                                    .into(thumb_image);

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (Exception e){
                            e.printStackTrace();

                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
        }

    }
//    class AmbilDataKajianMasjid extends AsyncTask<String, String, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//        }
//
//        protected String doInBackground(String... params) {
//            List<NameValuePair> params2 = new
//                    ArrayList<NameValuePair>();
//            params2.add(new
//                    BasicNameValuePair("id_masjid",idmasjid));
//            JSONObject json = jsonParser.makeHttpRequest(url_kajian_masjid,
//                    "GET", params2);
//            Log.i("Ini nilai json ", ">" + json);
//            try {
//                string_json = json.getJSONArray("kajian");
//                for (int i = 0; i < string_json.length(); i++) {
//                    JSONObject c = string_json.getJSONObject(i);
//                    String id_kajian = c.getString(TAG_ID);
//                    String nama_masjid = c.getString(TAG_NAMA_KAJIAN);
//                    String alamat_masjid = c.getString(TAG_ALAMAT_KAJIAN);
//                    String kontak = c.getString(TAG_CONTACT_KAJIAN);
//                    String judul = c.getString(TAG_JUDUL_KAJIAN);
//                    String deskripsi = c.getString(TAG_DESKRIPSI_KAJIAN);
//                    String tanggal = c.getString(TAG_TGL_KAJIAN);
//                    String waktu = c.getString(TAG_WAKTU_KAJIAN);
//                    String ustaz = c.getString(TAG_NAMA_USTADZ);
//                    String foto = c.getString(TAG_POSTER_KAJIAN);
//                    HashMap<String, String> map = new HashMap<String,
//                            String>();
//                    map.put(TAG_ID, id_kajian);
//                    map.put(TAG_NAMA_KAJIAN, nama_masjid);
//                    map.put(TAG_ALAMAT_MASJID, alamat_masjid);
//                    map.put(TAG_CONTACT_KAJIAN, kontak);
//                    map.put(TAG_JUDUL_KAJIAN, judul);
//                    map.put(TAG_DESKRIPSI_KAJIAN, deskripsi);
//                    map.put(TAG_TGL_KAJIAN, tanggal);
//                    map.put(TAG_WAKTU_KAJIAN, waktu);
//                    map.put(TAG_NAMA_USTADZ, ustaz);
//                    map.put(TAG_POSTER_KAJIAN, foto);
//                    DaftarKajian.add(map);
//
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//        protected void onPostExecute(String file_url) {
//            pDialog.dismiss();
//            runOnUiThread(new Runnable() {
//                public void run() {
//                    SetListViewAdapter(DaftarKajian);
//                }
//            });
//        }
//    }
}
