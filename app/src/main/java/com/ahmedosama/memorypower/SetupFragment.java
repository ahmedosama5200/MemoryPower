package com.ahmedosama.memorypower;

import android.app.Fragment;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.io.IOException;
import java.util.ArrayList;

public class SetupFragment extends Fragment {

    // Data base
    TheDataBaseHelper dbHelper;

    // References to views
    Spinner spnCount;

    // variables
    int nRestWords;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Opening data base
        dbHelper = new TheDataBaseHelper(getActivity());
        try
        {
            dbHelper.createDataBase();
        }
        catch (IOException ioe)
        {
            throw new Error("Unable to create database");
        }

        try
        {
            dbHelper.openDataBase();
        }
        catch(SQLException sqle)
        {
            throw sqle;
        }
        // Deciding which view to inflate
        nRestWords = dbHelper.getnRestWords();

        if (nRestWords == 0) {
            View rootView2 = inflater.inflate(R.layout.fragment_setup2, container, false);

            Button btnReview = (Button) rootView2.findViewById(R.id.btnReview);
            btnReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnReview_onClicked();
                }
            });

            return rootView2;
        }

        View rootView = inflater.inflate(R.layout.fragment_setup, container, false);

        // Getting References to views
        Button btnStart = (Button) rootView.findViewById(R.id.btnStart);
        spnCount = (Spinner) rootView.findViewById(R.id.spnCount);

        // Setting handlers
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnStart_onClick();
            }
        });

        // Populating the spinner
        ArrayList<Integer> lstCount = new ArrayList<>();

        if (nRestWords < 3) {
            lstCount.add(nRestWords);
        }
        else {
            for (int i = 3; i <= 15 && i <= nRestWords; i+=3) {
                lstCount.add(i);
            }
        }

        ArrayAdapter<Integer> adapterCount = new ArrayAdapter<>(
                getActivity(),
                R.layout.support_simple_spinner_dropdown_item,
                lstCount
        );
        adapterCount.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spnCount.setAdapter(adapterCount);

        return rootView;
    }

    private void btnStart_onClick() {

        int count = Integer.parseInt(spnCount.getSelectedItem().toString());

        Intent intent = new Intent(getActivity(), LearnActivity.class);
        intent.putExtra("count", count);
        startActivity(intent);

    }

    private void btnReview_onClicked() {
        Intent intent = new Intent(getActivity(), ReviewActivity.class);
        startActivity(intent);
    }

}
