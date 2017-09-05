package com.ahmedosama.memorypower;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // Setting handlers
        view.findViewById(R.id.btnLearn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLearn_onClick();
            }
        });

        view.findViewById(R.id.btnReview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnReview_onClick();
            }
        });

        view.findViewById(R.id.btnProgress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnProgress_onClick();
            }
        });

        view.findViewById(R.id.btnAbout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAbout_onClick();
            }
        });

        view.findViewById(R.id.btnExit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnExit_onClick();
            }
        });

        return view;
    }

    private void btnLearn_onClick() {
        Intent intent = new Intent(getActivity(), SetupActivity.class);
        startActivity(intent);
    }

    private void btnReview_onClick() {
        Intent intent = new Intent(getActivity(), ReviewActivity.class);
        startActivity(intent);
    }

    private void btnProgress_onClick() {
        Intent intent = new Intent(getActivity(), ProgressActivity.class);
        startActivity(intent);
    }

    private void btnAbout_onClick() {
        Intent intent = new Intent(getActivity(), AboutActivity.class);
        startActivity(intent);
    }

    private void btnExit_onClick() {
        getActivity().onBackPressed();
    }

}
