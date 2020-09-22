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

public class KajianAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    //    public ImageLoader ImageLoader;
    public KajianAdapter(Activity a, ArrayList<HashMap<String, String>> d)
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
            vi = inflater.inflate(R.layout.list_kajian, null);
        TextView id_kajian = (TextView) vi.findViewById(R.id.kodekajian);
        TextView judulkajian = (TextView) vi.findViewById(R.id.kajian);
        TextView ustadz = (TextView) vi.findViewById(R.id.ustadz);
        TextView tanggal = (TextView) vi.findViewById(R.id.tglkajian);
        TextView namamasjid = (TextView) vi.findViewById(R.id.masjidkj);
        ImageView thumb_image = (ImageView) vi.findViewById(R.id.gambarkj);

        HashMap<String, String> daftar_kajian = new HashMap<String, String>();
        daftar_kajian = data.get(position);

        id_kajian.setText(daftar_kajian.get(KajianActivity.TAG_ID_KAJIAN));
        judulkajian.setText(daftar_kajian.get(KajianActivity.TAG_JUDUL_KAJIAN));
        ustadz.setText(daftar_kajian.get(KajianActivity.TAG_NAMA_USTADZ));
        tanggal.setText(daftar_kajian.get(KajianActivity.TAG_TANGGAL_KAJIAN));
        namamasjid.setText(daftar_kajian.get(KajianActivity.TAG_NAMA_MASJID));


        //ImageLoader.DisplayImage(daftar_kajian.get(DetailKajian.TAG_POSTER),thumb_image);
        Picasso.with(activity.getApplicationContext())
                .load(daftar_kajian.get(KajianActivity.TAG_POSTER_KAJIAN))
                .error(R.drawable.no_image)
                .into(thumb_image);
        
        return vi;

    }
}
