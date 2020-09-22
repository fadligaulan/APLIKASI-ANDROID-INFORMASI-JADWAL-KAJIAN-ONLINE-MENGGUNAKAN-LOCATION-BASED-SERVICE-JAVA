package com.crp.infokajianislami;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AkunActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    JSONParser jsonParser = new JSONParser();

    private static String url_tambah = "https://onlinecashier.id/infokajianislami/web/json/TambahPeserta.php";
    private static String url_lihat = "https://onlinecashier.id/infokajianislami/web/json/DataPeserta.php";
    public static final String TAG_ID = "id";
    public static final String TAG_NAMA_PESERTA = "nama_peserta";
    public static final String TAG_EMAIL_PESERTA = "email_peserta";
    public static final String TAG_FOTO_PESERTA = "foto_peserta";

    public static final int LOGIN_GOOGLE_NUMBER = 012;
    JSONArray string_json = null;
    private ProgressDialog pDialog;
    private Button btnSignInGoogle;
    private Button btnSignOutGoogle;
    private TextView tvName, tvEmail;
    private ImageView ivProfile;
    private ProgressDialog progressDialog;
    public Peserta pesertakajian = new Peserta();
    public String idPeserta;
    ImageView riwayat, home;
    //google attribute
    private GoogleApiClient googleApiClient;
    public static final String KEY_SHARED = "AkunActivity.KEY";
    public static final String KEY_SHARED2 = "AkunActivity.KEY2";
    int in = 0;
    TextView keterangan;
    LinearLayout linear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_akun);

        btnSignInGoogle = (Button) findViewById(R.id.btn_sign_in_google);
        tvName = (TextView) findViewById(R.id.textview_name);
        tvEmail = (TextView) findViewById(R.id.textview_email);
        ivProfile = (ImageView) findViewById(R.id.profile);
        btnSignOutGoogle = (Button) findViewById(R.id.btn_log_out_google);
        linear = (LinearLayout) findViewById(R.id.lineAtas);

        riwayat = (ImageView) findViewById(R.id.img_email);
        riwayat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(AkunActivity.this, AboutActivity.class);
                startActivity(i);
            }
        });
        home = (ImageView) findViewById(R.id.imageHomeAk);
        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(AkunActivity.this, MainActivity.class);
                startActivity(i);

            }
        });

        //set progressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Login Using Google");


        //google signin uption
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        //set google api
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this) /*activity, onConnectionFailedListener*/
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        btnSignInGoogle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                progressDialog.show();
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, LOGIN_GOOGLE_NUMBER);

            }
        });


        btnSignOutGoogle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Logout");
                progressDialog.show();

                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {

                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {
                            progressDialog.dismiss();
                            updateUI(false);
                            String vl = "";
                            String em = "";
                            cekLogin(vl, em);
                            Toast.makeText(AkunActivity.this, "Logout successful", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(AkunActivity.this, "Logout failed", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
            }
        });


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        getCache();
    }

    /**
     * this method used to get the cache if you have logged or not before.
     */
    private void getCache() {
        OptionalPendingResult<GoogleSignInResult> optionalPendingResult = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (optionalPendingResult.isDone()) {
            GoogleSignInResult result = optionalPendingResult.get();
            handleSignInResult(result);
        } else {
            optionalPendingResult.setResultCallback(new ResultCallback<GoogleSignInResult>() {

                @Override
                public void onResult(@NonNull GoogleSignInResult result) {
                    handleSignInResult(result);

                }
            });
        }

    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {


            updateUI(true);

            progressDialog.dismiss();

            //hide and show button login and logout
            keterangan.setVisibility(View.GONE);
            btnSignInGoogle.setVisibility(View.GONE);
            btnSignOutGoogle.setVisibility(View.VISIBLE);
            linear.setBackgroundColor(Color.parseColor("#ffffff"));

            GoogleSignInAccount account = result.getSignInAccount();

            //set name and photo profile

            tvName.setText("Selamat datang " + account.getDisplayName());
            tvEmail.setText(account.getEmail());
            Glide.with(this)
                    .load(account.getPhotoUrl())
                    .into(ivProfile);

            pesertakajian.setNama_peserta(account.getDisplayName());
            pesertakajian.setEmail_peserta(account.getEmail());
            //pesertakajian.setFoto_peserta(account.getPhotoUrl());

            new TambahPeserta().execute();

            new AmbilDetailPeserta().execute();

            System.out.println(pesertakajian.getId_peserta());
            cekLogin(pesertakajian.getId_peserta(), pesertakajian.getEmail_peserta());

        } else {
            progressDialog.dismiss();
        }
    }

    private void cekLogin(String value, String email) {
        //        String value = pesertakajian.getId_peserta();
//        String email = pesertakajian.getEmail_peserta();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AkunActivity.this);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(KEY_SHARED, value);
        edit.commit();
        SharedPreferences sharedPreferences2 = PreferenceManager.getDefaultSharedPreferences(AkunActivity.this);
        SharedPreferences.Editor edit2 = sharedPreferences2.edit();
        edit2.putString(KEY_SHARED2, email);
        edit2.commit();

    }

    /**
     * this method used to update visible or gone of user interface
     *
     * @param statusLogin
     */

    private void updateUI(boolean statusLogin) {
        if (statusLogin == true) {
            keterangan.setVisibility(View.GONE);
            btnSignInGoogle.setVisibility(View.GONE);
            btnSignOutGoogle.setVisibility(View.VISIBLE);
            linear.setBackgroundColor(Color.parseColor("#ffffff"));
        } else {
            in = 0;
            keterangan.setVisibility(View.VISIBLE);
            btnSignInGoogle.setVisibility(View.VISIBLE);
            btnSignOutGoogle.setVisibility(View.GONE);
            linear.setBackgroundColor(Color.parseColor("#ededed"));
            ivProfile.setVisibility(View.GONE);
            tvName.setVisibility(View.GONE);
            tvEmail.setVisibility(View.GONE);
        }

    }

    class TambahPeserta extends AsyncTask<String, String,
            String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AkunActivity.this);
            pDialog.setMessage("Mohon Tunggu ... ");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... strings) {

            try {

                List<NameValuePair> params1 = new
                        ArrayList<NameValuePair>();

                params1.add(new
                        BasicNameValuePair("nama_peserta", pesertakajian.getNama_peserta().toString()));
                params1.add(new
                        BasicNameValuePair("email_peserta", pesertakajian.getEmail_peserta().toString()));
                JSONObject json = jsonParser.makeHttpRequest(
                        url_tambah, "GET", params1);
                string_json = json.getJSONArray("");


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
        }
    }

    class AmbilDetailPeserta extends AsyncTask<String, String,
                String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            protected String doInBackground(String... strings) {
                try {
                    List<NameValuePair> paramm = new
                            ArrayList<NameValuePair>();
                    paramm.add(new
                            BasicNameValuePair("email_peserta", pesertakajian.getEmail_peserta().toString()));
                    JSONObject json = jsonParser.makeHttpRequest(
                            url_lihat, "GET", paramm);
                    string_json = json.getJSONArray("peserta");
                    runOnUiThread(new Runnable() {
                        public void run() {

                            try {
                                // ambil objek member pertama dari JSON Array
                                JSONObject ar =
                                        string_json.getJSONObject(0);
                                String nama_d = ar.getString(TAG_NAMA_PESERTA);
                                String email_d = ar.getString(TAG_EMAIL_PESERTA);
                                String id_peserta = ar.getString(TAG_ID);

                                pesertakajian.setNama_peserta(nama_d);
                                pesertakajian.setEmail_peserta(email_d);
                                pesertakajian.setId_peserta(id_peserta);
                                idPeserta = id_peserta;
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (Exception e) {
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
