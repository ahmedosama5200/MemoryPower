package com.ahmedosama.memorypower;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class CustomAdapter extends ArrayAdapter<String> {

    private ArrayList<String> arabic;

    CustomAdapter(Context context, ArrayList<String> english, ArrayList<String> arabic) {
        super(context, R.layout.list_item_wrong_answer, english);
        this.arabic = arabic;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View rootView = inflater.inflate(R.layout.list_item_wrong_answer, parent, false);

        TextView txtEnglish = (TextView) rootView.findViewById(R.id.txtEnglish);
        TextView txtArabic = (TextView) rootView.findViewById(R.id.txtArabic);

        txtEnglish.setText(getItem(position));
        txtArabic.setText(arabic.get(position));

        return rootView;
    }
}
