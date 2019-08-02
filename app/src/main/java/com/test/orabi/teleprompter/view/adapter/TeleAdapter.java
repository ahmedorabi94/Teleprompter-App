package com.test.orabi.teleprompter.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.test.orabi.teleprompter.R;
import com.test.orabi.teleprompter.repository.data.Tele;

import java.util.List;

public class TeleAdapter extends ArrayAdapter<Tele> {

    private Context mContext;
    private List<Tele> teles;

    public TeleAdapter(Context context, List<Tele> teles) {
        super(context, 0, teles);
        this.mContext = context;
        this.teles = teles;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Tele tele = teles.get(position);

        @SuppressLint("ViewHolder") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_item, parent, false);

        TextView tv_title = view.findViewById(R.id.tv_title);
        TextView tv_body = view.findViewById(R.id.tv_body);
        tv_title.setText(tele.getTitle());
        tv_body.setText(tele.getBody());

        return view;
    }


}
