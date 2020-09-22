package com.crp.infokajianislami;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DetailAudio extends AppCompatActivity implements Runnable {

    public ImageLoader imageLoader;

    {
        imageLoader = new ImageLoader(null);
    }

    ImageView pin, telp, tgl, wktu, ust;
    JSONArray string_json = null;
    String idaudio, namaMasjid, url_audio;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    public static final String TAG_ID = "id";
    public static final String TAG_NAMA_MASJID = "nama_masjid";
//    public static final String TAG_ALAMAT = "alamat_masjid";
//    public static final String TAG_CONTACT = "contact_masjid";
    public static final String TAG_JUDUL_AUDIO = "judul_audio";
    public static final String TAG_DES_AUDIO = "des_audio";
    public static final String TAG_TGL_AUDIO = "tgl_audio";
    public static final String TAG_NAMA_USTADZ = "nama_ustadz";
    public static final String TAG_FILE_AUDIO = "file_audio";
    private static final String url_detail_audio =
            "https://infokajianislami.fadligaulan.com/Json_detail_audio";
    private Button startButton;
    public SeekBar soundSeekBar;

    //    public MediaPlayer player;
    private Handler mHandler = new Handler();
    public Thread soundThread;
    Uri audio;
    private boolean playPause;
    private ProgressDialog progressDialog;
    private boolean initialStage = true;

    public MediaPlayer player = new MediaPlayer();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_audio);


        pin = (ImageView) findViewById(R.id.img_pin_maps_audio);
        ust = (ImageView) findViewById(R.id.img_ustadz_audio);
        tgl = (ImageView) findViewById(R.id.img_calender_audio);


        Intent i = getIntent();
        idaudio = i.getStringExtra(TAG_ID);
        namaMasjid = i.getStringExtra(TAG_NAMA_MASJID);

        new AmbilDetailAudio().execute();

        startButton = (Button) findViewById(R.id.playButtonAudio);
        soundThread = new Thread(this);

        soundThread.start();

        soundSeekBar = (SeekBar) findViewById(R.id.audio_detail);

        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        progressDialog = new ProgressDialog(this);

        startButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (!playPause) {
                    startButton.setText("Pause");
                    startButton.setBackgroundColor(getResources().getColor(R.color.bg_screen1));

                    if (initialStage) {
                        new Player().execute(url_audio);
                    }else {
                        if (!player.isPlaying())
                            player.start();
                    }

                    playPause = true;

                } else {
                    startButton.setText("Play");
                    startButton.setBackgroundColor(getResources().getColor(R.color.bg_screen3));
                    if (player.isPlaying()) {
                        player.pause();
                    }

                    playPause = false;

                }

            }
        });

        soundSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBar.setMax(player.getDuration());
                if (fromUser) {
                    player.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }



    @Override
    protected void onPause() {

        super.onPause();

        if (player != null) {
            player.reset();
            player.release();
            player = null;
        }
    }



    class Player extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {

            Boolean prepared = false;
            try {
                player.setDataSource(strings[0]);
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        initialStage = true;
                        playPause = false;
                        startButton.setText("Play");
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                    }
                });

                player.prepare();
                prepared = true;

            } catch (Exception e) {
                Log.e("MyAudioStreamingApp", e.getMessage());
                prepared = false;
            }
            return prepared;
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }

            player.start();
            initialStage = false;


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setMessage("Buffering...");
            progressDialog.show();
        }
    }

    @Override
    public void run() {
        int currentPosition = 0;
//        int soundTotal = player.getDuration();
//        soundSeekBar.setMax(soundTotal);

        if(player != null){
            int mCurrentPosition = player.getCurrentPosition();
            soundSeekBar.setProgress(mCurrentPosition);
        }
        mHandler.postDelayed(this, 1000);

//        while (player != null && currentPosition < soundTotal) {
//            try {
//                Thread.sleep(300);
//                currentPosition = player.getCurrentPosition();
//            } catch (InterruptedException soundException) {
//                return;
//            } catch (Exception otherException) {
//                return;
//            }
//            soundSeekBar.setProgress(currentPosition);
//        }
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    class AmbilDetailAudio extends AsyncTask<String, String,
            String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DetailAudio.this);
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
                        BasicNameValuePair("id_audio",idaudio));
                JSONObject json = jsonParser.makeHttpRequest(
                        url_detail_audio, "GET", params1);
                string_json = json.getJSONArray("audio");
                runOnUiThread(new Runnable() {


                    @Override
                    public void run() {

                        TextView judul = (TextView) findViewById(R.id.judulKajian);
                        TextView nama = (TextView) findViewById(R.id.namaMasjid);
                        TextView ustadz = (TextView) findViewById(R.id.ustadzAudio);
                        TextView tanggal = (TextView) findViewById(R.id.tanggalAudio);
                        TextView deskripsi = (TextView) findViewById(R.id.penjelasanAudio);

                        try {
                            // ambil objek member pertama dari JSON Array
                            JSONObject ar =
                                    string_json.getJSONObject(0);
                            String nama_d = ar.getString(TAG_NAMA_MASJID);
                            String judul_d = ar.getString(TAG_JUDUL_AUDIO);
                            String ustadz_d = ar.getString(TAG_NAMA_USTADZ);
                            String tanggal_d = ar.getString(TAG_TGL_AUDIO);
                            String deskripsi_d = ar.getString(TAG_DES_AUDIO);
                            String url_detail_image = ar.getString(TAG_FILE_AUDIO);

                            nama.setText(nama_d);
                            judul.setText(judul_d);
                            ustadz.setText(ustadz_d);
                            tanggal.setText(tanggal_d);
                            deskripsi.setText(deskripsi_d);
                            url_audio = url_detail_image;
                            audio = Uri.parse(url_audio);
                            System.out.println(url_audio);

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }catch (Exception e){
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




}
