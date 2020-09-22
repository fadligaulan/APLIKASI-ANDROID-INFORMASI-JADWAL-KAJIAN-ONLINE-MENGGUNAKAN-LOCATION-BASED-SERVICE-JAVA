package com.crp.infokajianislami;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailVideo extends AppCompatActivity {
    public ImageLoader imageLoader;
    {
        imageLoader = new ImageLoader(null);
    }

    MediaController mediaController;
    Uri video;
    VideoView thumb_image;
    ImageView pin,telp,tgl, wktu, ust;
    JSONArray string_json = null;
    String idvideo, namaMasjid;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    public static final String TAG_ID = "id";
    public static final String TAG_NAMA_MASJID = "nama_masjid";
    public static final String TAG_ALAMAT_MASJID = "alamat_masjid";
    public static final String TAG_CONTACT_MASJID = "contact_masjid";
    public static final String TAG_JUDUL_VIDEO = "judul_video";
    public static final String TAG_DES_VIDEO = "des_video";
    public static final String TAG_TGL_VIDEO = "tgl_video";
    public static final String TAG_NAMA_USTADZ = "nama_ustadz";
    public static final String TAG_FILE_VIDEO = "url_video";
    private static final String url_detail_video =
            "https://infokajianislami.fadligaulan.com/Json_detail_video";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_video);

        pin = (ImageView) findViewById(R.id.img_pin_maps_video);
        ust = (ImageView) findViewById(R.id.img_ustadz_video);
        tgl = (ImageView) findViewById(R.id.img_calender_video);
        Intent i = getIntent();
        idvideo = i.getStringExtra(TAG_ID);
        namaMasjid = i.getStringExtra(TAG_NAMA_MASJID);
 //       Toast.makeText(getApplicationContext(),
//                " " + namaMasjid,
//                Toast.LENGTH_SHORT).show();
        new AmbilDetailVideo().execute();

    }
    class AmbilDetailVideo extends AsyncTask<String, String,
            String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> params1 = new
                        ArrayList<NameValuePair>();
                params1.add(new
                        BasicNameValuePair("id_video",idvideo));
                JSONObject json = jsonParser.makeHttpRequest(
                        url_detail_video, "GET", params1);
                string_json = json.getJSONArray("video");
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        thumb_image = (VideoView) findViewById(R.id.video_detail);
                        TextView judul = (TextView) findViewById(R.id.judulKajian);
                        TextView nama = (TextView) findViewById(R.id.namaMasjid);
                        TextView ustadz = (TextView) findViewById(R.id.ustadzVideo);
                        TextView tanggal = (TextView) findViewById(R.id.tanggalVideo);
                        TextView deskripsi = (TextView) findViewById(R.id.penjelasanVideo);
                        try {
                            // ambil objek member pertama dari JSON Array
                            JSONObject ar =
                                    string_json.getJSONObject(0);
                            String nama_d = ar.getString(TAG_NAMA_MASJID);
                            String judul_d = ar.getString(TAG_JUDUL_VIDEO);
                            String telpon_d = ar.getString(TAG_CONTACT_MASJID);
                            String ustadz_d = ar.getString(TAG_NAMA_USTADZ);
                            String tanggal_d = ar.getString(TAG_TGL_VIDEO);
                            String deskripsi_d = ar.getString(TAG_DES_VIDEO);
                            String url_detail_image = ar.getString(TAG_FILE_VIDEO);
                            nama.setText(nama_d);
                            judul.setText(judul_d);
                            ustadz.setText(ustadz_d);
                            tanggal.setText(tanggal_d);

                            deskripsi.setText(deskripsi_d);
                            System.out.println(nama_d + judul_d + ustadz_d + tanggal_d + deskripsi_d + url_detail_image);
                            videoStream(url_detail_image);
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

        }
        private void videoStream(String url_video) {
            // membuat progressbar
            pDialog = new ProgressDialog(DetailVideo.this);
            pDialog.setMessage("Buffering ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

            try {
                // Memulai MediaController
                mediaController = new MediaController(DetailVideo.this);
                mediaController.setAnchorView(thumb_image);
                // Video URL
                video = Uri.parse(url_video);
                thumb_image.setMediaController(mediaController);
                thumb_image.setVideoURI(video);
                thumb_image.requestFocus();
                thumb_image.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    // Menutup pDialog dan play video
                    public void onPrepared(MediaPlayer mp) {
                        pDialog.dismiss();
                        thumb_image.start();
                    }
                });
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
        }

    }
}
