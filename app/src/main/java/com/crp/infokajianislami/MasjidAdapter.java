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

public class MasjidAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;

    //    public ImageLoader ImageLoader;
    public MasjidAdapter(Activity a, ArrayList<HashMap<String, String>> d)
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
            vi = inflater.inflate(R.layout.list_masjid, null);

        TextView id_masjid = (TextView) vi.findViewById(R.id.kodemasjid);
        TextView nama_masjid = (TextView) vi.findViewById(R.id.nama_masjid);
        TextView alamat_masjid = (TextView) vi.findViewById(R.id.alamat_masjid);
        ImageView thumb_image = (ImageView) vi.findViewById(R.id.gambar_masjid);

        HashMap<String, String> daftar_masjid = new HashMap<String, String>();
        daftar_masjid = data.get(position);

        id_masjid.setText(daftar_masjid.get(MasjidActivity.TAG_ID_MASJID));
        nama_masjid.setText(daftar_masjid.get(MasjidActivity.TAG_NAMA_MASJID));
        alamat_masjid.setText(daftar_masjid.get(MasjidActivity.TAG_ALAMAT_MASJID));

        //       ImageLoader.DisplayImage(daftar_masjid.get(DetailMasjid.TAG_GAMBAR),thumb_image);
        Picasso.with(activity.getApplicationContext())
                .load(daftar_masjid.get(MasjidActivity.TAG_FOTO_MASJID))
                .error(R.drawable.no_image)
                .into(thumb_image);

        return vi;
    }
}
