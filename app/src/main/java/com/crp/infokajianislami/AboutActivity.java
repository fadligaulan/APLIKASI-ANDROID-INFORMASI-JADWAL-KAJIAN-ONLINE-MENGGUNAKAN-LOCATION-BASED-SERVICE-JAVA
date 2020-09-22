package com.crp.infokajianislami;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class AboutActivity extends AppCompatActivity {
    Button hub;
    ImageView home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        hub =(Button) findViewById(R.id.hubKami);
        hub.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("message/rfc822");
//                intent.putExtra(Intent.EXTRA_EMAIL, "kajianpku@gmail.com");
//                intent.putExtra(Intent.EXTRA_CC, "kajianpku@gmail.com");
//                intent.putExtra(Intent.EXTRA_SUBJECT, "Info");
//                intent.putExtra(Intent.EXTRA_TEXT, "Kepada Admin kajianpku.com : ");
//
//                startActivity(Intent.createChooser(intent, "Send Email"));
                sendEmail();
            }
        });

        home = (ImageView) findViewById(R.id.imageHome);
        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent hm = new Intent(AboutActivity.this, MainActivity.class);
                startActivity(hm);
            }
        });


    }
    protected void sendEmail() {
        Log.i("Send email", "");
        String[] TO = {"infokajianislami@gmail.com"};
        String[] CC = {"admininfokajianislami@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Info : Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Kepada admin kajianpku.com : ");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email", "");
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(AboutActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
