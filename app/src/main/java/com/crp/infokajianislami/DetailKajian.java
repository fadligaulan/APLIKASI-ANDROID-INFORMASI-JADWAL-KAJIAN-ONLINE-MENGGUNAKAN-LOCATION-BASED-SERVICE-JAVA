package com.crp.infokajianislami;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class DetailKajian extends AppCompatActivity {
    private static Uri eventUri;
    public ImageLoader imageLoader;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    {
        imageLoader = new ImageLoader(null);
    }

    public String tanggalHari, waktuHari;
    ImageView pin, telp, tgl, wktu, ust;
    Button join;
    JSONArray string_json = null;
    String idkajian, namaMasjid;

    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    public static final String TAG_ID = "id";
    public static final String TAG_NAMA_MASJID = "nama_masjid";
    public static final String TAG_JUDUL_KAJIAN = "judul_kajian";
    public static final String TAG_ALAMAT_MASJID = "alamat_masjid";
    public static final String TAG_TGL_KAJIAN = "tgl_kajian";
    public static final String TAG_DESKRIPSI_KAJIAN = "deskripsi_kajian";
    public static final String TAG_CONTACT = "contact_masjid";
    public static final String TAG_NAMA_USTADZ = "nama_ustadz";
    public static final String TAG_WAKTU_KAJIAN = "waktu_kajian";
    public static final String TAG_JML_PESERTA = "jumlah_peserta_kajian";
    public static final String TAG_POSTER_KAJIAN = "poster_kajian";

//    private static final String url_detail_kajian = "https://onlinecashier.id/infokajianislami/web/json/DetailKajian.php";
//    private static final String url_ikuti = "https://onlinecashier.id/infokajianislami/web/json/IkutiKajian.php";

    private static final String url_detail_kajian = "https://infokajianislami.fadligaulan.com/json_detail_kajian";
    private static final String url_ikuti = "https://infokajianislami.fadligaulan.com/json_ikuti_kajian";

    private static final String TAG_IDCEK = "id";
    private static final String TAG_CEK = "cek_peserta";
    private static final String url_cek_peserta = "http://www.kajianpku.com/web/json/cekPeserta.php";
    public Peserta pKajian = new Peserta();
    public Kajian kjKajian = new Kajian();
    TextView deskripsi;
    public String value, judul_d, deskripsi_d, nama_d, tanggal_d, waktu_d, email;
    int indx;

    public static final String[] EVENT_PROJECTION = new String[] {
            CalendarContract.Calendars._ID, // 0
            CalendarContract.Calendars.ACCOUNT_NAME, // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME // 2
    };

    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kajian);

        System.out.println(pKajian.getEmail_peserta()+" email");
        pin = (ImageView) findViewById(R.id.img_pin_maps_kajian);
        telp = (ImageView) findViewById(R.id.img_tlpn_kajian);
        ust = (ImageView) findViewById(R.id.img_ustadz_kajian);
        tgl = (ImageView) findViewById(R.id.img_calender_kajian);
        wktu = (ImageView) findViewById(R.id.img_waktu_kajian);
        Intent i = getIntent();
        idkajian = i.getStringExtra(TAG_ID);
        namaMasjid = i.getStringExtra(TAG_NAMA_MASJID);
        deskripsi = (TextView) findViewById(R.id.deskripsiKajian);
//        Toast.makeText(getApplicationContext(), "nilai id kajian" + idkajian,Toast.LENGTH_LONG).show();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(DetailKajian.this);
        value = sharedPreferences.getString(AkunActivity.KEY_SHARED, null);
        SharedPreferences sharedPreferences2 = PreferenceManager.getDefaultSharedPreferences(DetailKajian.this);
        email = sharedPreferences2.getString(AkunActivity.KEY_SHARED2, null);

        tanggalHari = getCurrentDate();
        waktuHari = getCurrentTime();

        new AmbilDetailKajian().execute();

        new AmbilDataPeserta().execute();

        join = (Button) findViewById(R.id.ikutKajian);
        join.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                indx = 1;
                join.setClickable(false);
                join.setText("Sudah Daftar");
                join.setBackgroundColor(getResources().getColor(R.color.bg_screen6));
//                System.out.println(idkajian + " + " + kjKajian.getTanggalKajian() + " + " + value + " " + waktuHari + " + " + email);
                new TambahPeserta().execute();

                Toast.makeText(getApplicationContext(), " Anda telah terdaftar pada kajian " + judul_d, Toast.LENGTH_SHORT).show();

                addCal();


            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


    }
    private void updateUI(String statusLogin){
        if(statusLogin=="sudah"){
            join.setClickable(false);
            join.setText("Sudah Daftar");
            join.setBackgroundColor(getResources().getColor(R.color.bg_screen6));

        }else if(statusLogin=="out"){
            join.setClickable(false);
            join.setText("Out Date");
            join.setBackgroundColor(getResources().getColor(R.color.bg_screen6));
        }else if(statusLogin=="login"){
            join.setClickable(false);
            join.setText("Anda Belum Login");
            join.setBackgroundColor(getResources().getColor(R.color.bg_screen1));
        }else{

            join.setClickable(true);
            join.setText("Ikuti Kajian");

        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("DetailKajian Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    public void onStart() {

        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
       // client.connect();
        //AppIndex.AppIndexApi.start(client, getIndexApiAction());

    }

    public void onStop() {

        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
       // AppIndex.AppIndexApi.end(client, getIndexApiAction());
        //client.disconnect();
    }

    class TambahPeserta extends AsyncTask<String, String,
            String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DetailKajian.this);
            pDialog.setMessage("Mohon Tunggu ... ");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> params1 = new
                        ArrayList<NameValuePair>();
                params1.add(new
                        BasicNameValuePair("tanggal_ikuti", tanggalHari));
                params1.add(new
                        BasicNameValuePair("waktu_ikuti", waktuHari));
                params1.add(new
                        BasicNameValuePair("id_kajian", idkajian));
                params1.add(new
                        BasicNameValuePair("id_peserta", value));
                JSONObject json = jsonParser.makeHttpRequest(
                        url_ikuti, "GET", params1);
//                Log.e("CEK_PESERTA", "================================= : "+value+" ============"+idkajian);
                string_json = json.getJSONArray("kajian");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
        }
    }

    class AmbilDetailKajian extends AsyncTask<String, String,
            String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DetailKajian.this);
            pDialog.setMessage("Mohon Tunggu ... !");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> params1 = new ArrayList<NameValuePair>();
                params1.add(new BasicNameValuePair("id_kajian", idkajian));
                JSONObject dataDetail = jsonParser.makeHttpRequest(url_detail_kajian, "GET", params1);

                string_json = dataDetail.getJSONArray("detail_kajian");

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        ImageView thumb_image = (ImageView) findViewById(R.id.gambarkajian);
                        TextView judul = (TextView) findViewById(R.id.judulKajian);
                        TextView nama = (TextView) findViewById(R.id.namaMasjid);
                        TextView telpon = (TextView) findViewById(R.id.contactMsjd);
                        TextView ustadz = (TextView) findViewById(R.id.namaUstadz);
                        TextView tanggal = (TextView) findViewById(R.id.tanggalkajian);
                        TextView waktu = (TextView) findViewById(R.id.waktuKajian);
                        TextView des = (TextView) findViewById(R.id.deskripsiKJ);

                        try {
                            // ambil objek member pertama dari JSON Array
                            JSONObject ar =
                                    string_json.getJSONObject(0);

                            Log.i("Ini nilai", " id kajian yang dikirim --" + idkajian);
                            Log.i("Ini nilai json detail", ">" + ar);

                            nama_d = ar.getString(TAG_NAMA_MASJID);
                            judul_d = ar.getString(TAG_JUDUL_KAJIAN);
                            String telpon_d = ar.getString(TAG_CONTACT);
                            String ustadz_d = ar.getString(TAG_NAMA_USTADZ);
                            tanggal_d = ar.getString(TAG_TGL_KAJIAN);
                            waktu_d = ar.getString(TAG_WAKTU_KAJIAN);
                            deskripsi_d = ar.getString(TAG_DESKRIPSI_KAJIAN);
                            String url_detail_image = ar.getString(TAG_POSTER_KAJIAN);
                            nama.setText(nama_d);
                            judul.setText(judul_d);
                            telpon.setText(telpon_d);
                            ustadz.setText(ustadz_d);
                            tanggal.setText(tanggal_d);
                            waktu.setText(waktu_d);
                            des.setText(deskripsi_d);
                            Picasso.with(getApplicationContext())
                                    .load(url_detail_image)
                                    .error(R.drawable.no_image)
                                    .into(thumb_image);
                            kjKajian.setTanggalKajian(tanggal_d);
                            kjKajian.setWaktuKajian(waktu_d);
                            System.out.println(kjKajian.getTanggalKajian()+" "+kjKajian.getWaktuKajian()+" dalam method");
                            System.out.println(getCurrentDate()+" dalam method");

                            int tahun = Integer.parseInt(kjKajian.getTanggalKajian().charAt(6)+""+kjKajian.getTanggalKajian().charAt(7)+""+kjKajian.getTanggalKajian().charAt(8)+""+kjKajian.getTanggalKajian().charAt(9));
                            int bulan = Integer.parseInt(kjKajian.getTanggalKajian().charAt(0)+""+kjKajian.getTanggalKajian().charAt(1));
                            int hari = Integer.parseInt(kjKajian.getTanggalKajian().charAt(3)+""+kjKajian.getTanggalKajian().charAt(4));
                            int thIni =Integer.parseInt(getCurrentDate().charAt(6)+""+getCurrentDate().charAt(7)+""+getCurrentDate().charAt(8)+""+getCurrentDate().charAt(9)) ;
                            int blIni = Integer.parseInt(getCurrentDate().charAt(0)+""+getCurrentDate().charAt(1));
                            int hrIni = Integer.parseInt(getCurrentDate().charAt(3)+""+getCurrentDate().charAt(4));
                            if((hari<hrIni)&&(bulan<=blIni)&&(tahun<=thIni)){
                                updateUI("out");
                            }

                            //                        imageLoader.DisplayImage(ar.getString(TAG_POSTER),thumb_image);

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

    class AmbilDataPeserta extends AsyncTask<String, String,
            String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(String... params) {
            try{
                List<NameValuePair> params3 = new
                        ArrayList<NameValuePair>();
                params3.add(new
                        BasicNameValuePair("id_kajian", idkajian));
                params3.add(new
                        BasicNameValuePair("id_peserta", value));
                JSONObject json = jsonParser.makeHttpRequest(
                        url_cek_peserta, "GET", params3);
                string_json = json.getJSONArray("ikuti");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // ambil objek member pertama dari JSON Array
                            JSONObject ar =
                                    string_json.getJSONObject(0);
                            String idik_d = ar.getString(TAG_IDCEK);
                            String cek_d = ar.getString(TAG_CEK);
                            if(cek_d.equals("ada")){
                                updateUI("sudah");
                            }

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


    public String getCurrentDate() {
        final Calendar c = Calendar.getInstance();
        int year, month, day;
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DATE);


        return "0"+(month + 1) + "/" + day + "/" + year;
    }
    public String getCurrentTime() {
        final Calendar t = Calendar.getInstance();
        int hour, minute;
        hour = t.get(Calendar.HOUR);
        minute = t.get(Calendar.MINUTE);

        return hour + ":" + minute;
    }

    private String m_selectedCalendarId = "0";

    private void addCal(){
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(CalendarContract.Events.TITLE, judul_d);
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, nama_d);
        intent.putExtra(CalendarContract.Events.DESCRIPTION, deskripsi_d);

        int tahun = Integer.parseInt(kjKajian.getTanggalKajian().charAt(6)+""+kjKajian.getTanggalKajian().charAt(7)+""+kjKajian.getTanggalKajian().charAt(8)+""+kjKajian.getTanggalKajian().charAt(9));
        int bulan = Integer.parseInt(kjKajian.getTanggalKajian().charAt(0)+""+kjKajian.getTanggalKajian().charAt(1));
        int hari = Integer.parseInt(kjKajian.getTanggalKajian().charAt(3)+""+kjKajian.getTanggalKajian().charAt(4));
        // Setting dates
        GregorianCalendar calDate = new GregorianCalendar(tahun, bulan-1, hari);
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                calDate.getTimeInMillis()+02);
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                calDate.getTimeInMillis());

        // Make it a full day event
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false);

        // Make it a recurring Event
        intent.putExtra(CalendarContract.Events.RRULE,
                "FREQ=WEEKLY;COUNT=11;WKST=SU;BYDAY=TU,TH");

        // Making it private and shown as busy
        intent.putExtra(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PRIVATE);
        intent.putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);

        startActivity(intent);

    }

    /*add an event to calendar*/
    private void addEvent() {
        ContentValues l_event = new ContentValues();
        l_event.put("calendar_id", m_selectedCalendarId);
        l_event.put("title", judul_d);
        l_event.put("description", deskripsi_d);
        l_event.put("eventLocation", nama_d);
        l_event.put("dtstart", tanggal_d);
        l_event.put("dtend", tanggal_d + waktu_d);
        l_event.put("allDay", 0);
        //status: 0~ tentative; 1~ confirmed; 2~ canceled
        l_event.put("eventStatus", 3);
        //0~ default; 1~ confidential; 2~ private; 3~ public
        l_event.put("visibility", 0);
        //0~ opaque, no timing conflict is allowed; 1~ transparency, allow overlap of scheduling
        l_event.put("transparency", 0);
        //0~ false; 1~ true
        l_event.put("hasAlarm", 1);
        Uri l_eventUri;

        l_eventUri = Uri.parse("content://com.android.calendar/events");

        Uri l_uri = this.getContentResolver().insert(l_eventUri, l_event);
        Log.v("++++++test", l_uri.toString());

    }

    /*add an event through intent, this doesn't require any permission
     * just send intent to android calendar
     */
    private void addEvent2() {
        Intent l_intent = new Intent(Intent.ACTION_EDIT);
        l_intent.setType("vnd.android.cursor.item/event");
        //l_intent.putExtra("calendar_id", m_selectedCalendarId);  //this doesn't work
        l_intent.putExtra("title", judul_d);
        l_intent.putExtra("description", deskripsi_d);
        l_intent.putExtra("eventLocation", nama_d);
        l_intent.putExtra("beginTime", 2-9-2018);
        l_intent.putExtra("endTime", 2-9-2018 + 60);
        l_intent.putExtra("allDay", 0);
        //status: 0~ tentative; 1~ confirmed; 2~ canceled
        l_intent.putExtra("eventStatus", 1);
        //0~ default; 1~ confidential; 2~ private; 3~ public
        l_intent.putExtra("visibility", 0);
        //0~ opaque, no timing conflict is allowed; 1~ transparency, allow overlap of scheduling
        l_intent.putExtra("transparency", 0);
        //0~ false; 1~ true
        l_intent.putExtra("hasAlarm", 1);
        try {
            startActivity(l_intent);
        } catch (Exception e) {
            Toast.makeText(this.getApplicationContext(), "Sorry, no compatible calendar is found!", Toast.LENGTH_LONG).show();
        }
    }

    public static long pushAppointmentsToCalender(Activity curActivity, String title, String addInfo, String place, String tanggal_d, boolean needReminder, boolean needMailService, String email) {
        /***************** Event: note(without alert) *******************/
        String eventUriString = "content://com.android.calendar/events";
        ContentValues eventValues = new ContentValues();

        eventValues.put("calendar_id", 1);
        eventValues.put("title", title);
        eventValues.put("description", addInfo);
        eventValues.put("eventLocation", place);

        String endDate = tanggal_d + 1000 * 60 * 60; // For next 1hr

        eventValues.put("dtstart", tanggal_d);
        eventValues.put("dtend", endDate);

        eventValues.put("allDay", 0);
        eventValues.put("eventStatus", 3);
        eventValues.put("eventTimezone", "UTC/GMT +7:00");
        /*eventValues.put("visibility", 3); // visibility to default (0),
                                        // confidential (1), private
                                        // (2), or public (3):
    eventValues.put("transparency", 0); // You can control whether
                                        // an event consumes time
                                        // opaque (0) or transparent
                                        // (1).
      */
        eventValues.put("hasAlarm", 1); // 0 for false, 1 for true

        eventUri = curActivity.getApplicationContext().getContentResolver().insert(Uri.parse(eventUriString), eventValues);
        long eventID = Long.parseLong(eventUri.getLastPathSegment());

        if (needReminder) {
            String reminderUriString = "content://com.android.calendar/reminders";

            ContentValues reminderValues = new ContentValues();

            reminderValues.put("event_id", eventID);
            reminderValues.put("minutes", 5); // Default value of the
            // system. Minutes is a
            // integer
            reminderValues.put("method", 1); // Alert Methods: Default(0),

            // Alert(1), Email(2),
            // SMS(3)

            Uri reminderUri = curActivity.getApplicationContext().getContentResolver().insert(Uri.parse(reminderUriString), reminderValues);

        }
        /***************** Event: Meeting(without alert) Adding Attendies to the meeting *******************/

        if (needMailService) {
            String attendeuesesUriString = "content://com.android.calendar/attendees";

            /********
             * To add multiple attendees need to insert ContentValues multiple
             * times
             ***********/
            ContentValues attendeesValues = new ContentValues();

            attendeesValues.put("event_id", eventID);
            attendeesValues.put("attendeeName", "xxxxx"); // Attendees name
            attendeesValues.put("attendeeEmail", email);// Attendee
            // E
            // mail
            // id
            attendeesValues.put("attendeeRelationship", 0); // Relationship_Attendee(1),
            // Relationship_None(0),
            // Organizer(2),
            // Performer(3),
            // Speaker(4)
            attendeesValues.put("attendeeType", 0); // None(0), Optional(1),
            // Required(2), Resource(3)
            attendeesValues.put("attendeeStatus", 0); // NOne(0), Accepted(1),
            // Decline(2),
            // Invited(3),
            // Tentative(4)

            Uri attendeuesesUri = curActivity.getApplicationContext().getContentResolver().insert(Uri.parse(attendeuesesUriString), attendeesValues);
        }

        return eventID;


    }
}
