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

public class VideoAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    //    public ImageLoader ImageLoader;
    public VideoAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
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
            vi = inflater.inflate(R.layout.list_video, null);
        TextView id_video = (TextView) vi.findViewById(R.id.kodekajian);
        TextView judul_video = (TextView) vi.findViewById(R.id.videokajian);
        TextView nama_ustadz = (TextView) vi.findViewById(R.id.ustadz);
        TextView nama_masjid = (TextView) vi.findViewById(R.id.masjidkj);
        ImageView thumb_image = (ImageView) vi.findViewById(R.id.videokj);

        HashMap<String, String> daftar_video = new HashMap<String, String>();
        daftar_video = data.get(position);

        id_video.setText(daftar_video.get(VideoActivity.TAG_ID_VIDEO));
        judul_video.setText(daftar_video.get(VideoActivity.TAG_JUDUL_VIDEO));
        nama_ustadz.setText(daftar_video.get(VideoActivity.TAG_NAMA_USTADZ));

        nama_masjid.setText(daftar_video.get(VideoActivity.TAG_NAMA_MASJID));
//          thumb_image.setVideoPath(daftar_video.get(VideoActivity.TAG_POSTER));

        //digunakan untuk mengidentifikasi resource alamat url video
        //thumb_image.setMediaController(new MediaController(this));
        //menampilkan media controller video
        //thumb_image.start();
        //memulai video

        //       ImageLoader.DisplayImage(daftar_video.get(DetailKajian.TAG_POSTER),thumb_image);

        Picasso.with(activity.getApplicationContext())
                .load(daftar_video.get(VideoActivity.TAG_FILE_VIDEO))
                .error(R.drawable.no_image)
                .into(thumb_image);

        return vi;
    }
}
