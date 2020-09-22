package com.crp.infokajianislami;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DetailYoutube extends YouTubeBaseActivity {
    public ImageLoader imageLoader;
    {
        imageLoader = new ImageLoader(null);
    }
    MediaController mediaController;
    Uri video;
    VideoView thumb_image;
    ImageView pin,telp,tgl, wktu, ust;
    JSONArray string_json = null;
    String idstreaming, namaMasjid;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    public static final String TAG_ID = "id";
    public static final String TAG_NAMA_MASJID = "nama_masjid";
    public static final String TAG_ALAMAT_MASJID = "alamat_masjid";
    public static final String TAG_CONTACT_MASJID = "contact_masjid";
    public static final String TAG_JUDUL_STREAMING = "judul_streaming";
    public static final String TAG_DES_STREAMING = "des_streaming";
    public static final String TAG_TGL_STREAMING = "tgl_streaming";
    public static final String TAG_NAMA_USTADZ = "nama_ustadz";
    public static final String TAG_URL_STREAMING = "url_streaming";
    private static final String url_detail_streaming =
            "https://infokajianislami.fadligaulan.com/Json_detail_streaming";


    private static final String TAG = DetailYoutube.class.getSimpleName();

    private YouTubePlayerView youTubeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_youtube);




        pin = (ImageView) findViewById(R.id.img_ustadz_streaming);
        ust = (ImageView) findViewById(R.id.img_ustadz_streaming);
        tgl = (ImageView) findViewById(R.id.img_calender_streaming);
        Intent i = getIntent();
        idstreaming = i.getStringExtra(TAG_ID);
        namaMasjid = i.getStringExtra(TAG_NAMA_MASJID);

        new DetailYoutube.AmbilDetailYoutube().execute();

        //        videoID = getIntent().getStringExtra("video_id");
        youTubeView = findViewById(R.id.youtube_view);



    }

    class AmbilDetailYoutube extends AsyncTask<String, String,
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
                        BasicNameValuePair("id_streaming",idstreaming));
                JSONObject json = jsonParser.makeHttpRequest(
                        url_detail_streaming, "GET", params1);
                string_json = json.getJSONArray("streaming");
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        thumb_image = (VideoView) findViewById(R.id.streaming_detail);
                        TextView judul = (TextView) findViewById(R.id.judulStreaming);
                        TextView nama = (TextView) findViewById(R.id.namaMasjid);
                        TextView ustadz = (TextView) findViewById(R.id.ustadzStreaming);
                        TextView tanggal = (TextView) findViewById(R.id.tanggalStreaming);
                        TextView deskripsi = (TextView) findViewById(R.id.penjelasanStreaming);

                        try{
                            // ambil objek member pertama dari JSON Array
                            JSONObject ar =
                                    string_json.getJSONObject(0);
                            String nama_d = ar.getString(TAG_NAMA_MASJID);
                            String judul_d = ar.getString(TAG_JUDUL_STREAMING);
                            String ustadz_d = ar.getString(TAG_NAMA_USTADZ);
                            String tanggal_d = ar.getString(TAG_TGL_STREAMING);
                            String deskripsi_d = ar.getString(TAG_DES_STREAMING);
                            String url_detail_image = ar.getString(TAG_URL_STREAMING);
                            nama.setText(nama_d);
                            judul.setText(judul_d);
                            ustadz.setText(ustadz_d);
                            tanggal.setText(tanggal_d);
                            deskripsi.setText(deskripsi_d);


                            System.out.println(nama_d + judul_d + ustadz_d + tanggal_d + deskripsi_d + url_detail_image);
                            initializeYoutubePlayer(url_detail_image);
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
    }

    /**
     * initialize the youtube player
     */

    private void initializeYoutubePlayer(final String url_video) {
        youTubeView.initialize(Config.YOUTUBE_API_KEY , new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer,
                                                boolean wasRestored) {

                video = Uri.parse(url_video);

                //if initialization success then load the video id to youtube player
                if (!wasRestored) {
                    //set the player style here: like CHROMELESS, MINIMAL, DEFAULT
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);

                    //load the video
                    youTubePlayer.loadVideo(String.valueOf(video));

                    //OR

                    //cue the video
                    //youTubePlayer.cueVideo(videoID);

                    //if you want when activity start it should be in full screen uncomment below comment
                    //  youTubePlayer.setFullscreen(true);

                    //If you want the video should play automatically then uncomment below comment
                    //  youTubePlayer.play();

                    //If you want to control the full screen event you can uncomment the below code
                    //Tell the player you want to control the fullscreen change
                   /*player.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
                    //Tell the player how to control the change
                    player.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
                        @Override
                        public void onFullscreen(boolean arg0) {
                            // do full screen stuff here, or don't.
                            Log.e(TAG,"Full screen mode");
                        }
                    });*/

                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
                //print or show error if initialization failed
                Log.e(TAG, "Youtube Player View initialization failed");
            }
        });
    }
}
