package com.crp.infokajianislami;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class AudioAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    //    public ImageLoader ImageLoader;
    public AudioAdapter(Activity a, ArrayList<HashMap<String, String>> d){
        activity = a;

        data = d;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.list_audio, null);
        TextView id_audio = (TextView) vi.findViewById(R.id.kodekajian);
        TextView judul_audio = (TextView) vi.findViewById(R.id.audiokajian);
        TextView nama_ustadz = (TextView) vi.findViewById(R.id.ustadz);
        TextView nama_masjid = (TextView) vi.findViewById(R.id.masjidkj);
        ImageView thumb_image = (ImageView) vi.findViewById(R.id.audiokj);

        HashMap<String, String> daftar_audio = new HashMap<String, String>();
        daftar_audio = data.get(position);

        id_audio.setText(daftar_audio.get(AudioActivity.TAG_ID_AUDIO));
        judul_audio.setText(daftar_audio.get(AudioActivity.TAG_JUDUL_AUDIO));
        nama_ustadz.setText(daftar_audio.get(AudioActivity.TAG_NAMA_USTAZD));

        nama_masjid.setText(daftar_audio.get(AudioActivity.TAG_NAMA_MASJID));

        Picasso.with(activity.getApplicationContext())
                .load(daftar_audio.get(AudioActivity.TAG_FILE_AUDIO))
                .error(R.drawable.no_image)
                .into(thumb_image);
        
        
        return vi;
    }
}
