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

public class StreamingAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;

    //    public ImageLoader ImageLoader;
    public StreamingAdapter(Activity a, ArrayList<HashMap<String, String>> d)
    {
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //       ImageLoader = new ImageLoader(activity.getApplicationContext());
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
            vi = inflater.inflate(R.layout.list_streaming, null);
        TextView id_streaming = (TextView) vi.findViewById(R.id.kodekajian);
        TextView judul_streaming = (TextView) vi.findViewById(R.id.streamingkajian);
        TextView nama_ustadz = (TextView) vi.findViewById(R.id.ustadz);
        TextView nama_masjid = (TextView) vi.findViewById(R.id.masjidkj);
        ImageView thumb_image = (ImageView) vi.findViewById(R.id.streamingkj);

        HashMap<String, String> daftar_streaming = new HashMap<String, String>();
        daftar_streaming = data.get(position);

        id_streaming.setText(daftar_streaming.get(StreamingActivity.TAG_ID));
        judul_streaming.setText(daftar_streaming.get(StreamingActivity.TAG_JUDUL_STREAMING));
        nama_ustadz.setText(daftar_streaming.get(StreamingActivity.TAG_NAMA_USTADZ));

        nama_masjid.setText(daftar_streaming.get(VideoActivity.TAG_NAMA_MASJID));

        //thumb_image.setVideoPath(daftar_video.get(VideoActivity.TAG_POSTER));


        //digunakan untuk mengidentifikasi resource alamat url video
        //thumb_image.setMediaController(new MediaController(this));
        //menampilkan media controller video
        //thumb_image.start();
        //memulai video

//            //       ImageLoader.DisplayImage(daftar_video.get(DetailKajian.TAG_POSTER),thumb_image);
        Picasso.with(activity.getApplicationContext())
                .load(daftar_streaming.get(StreamingActivity.TAG_URL_STREAMING))
                .error(R.drawable.no_image)
                .into(thumb_image);
        return vi;
    }
}
