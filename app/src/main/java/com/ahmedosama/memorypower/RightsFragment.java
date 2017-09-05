package com.ahmedosama.memorypower;

import android.app.Fragment;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RightsFragment extends Fragment {

    // Needed Constants
    int CONST_LEFT_MARGIN;
    int SCREEN_WIDTH;

    // References to views
    TextView txtRights;

    // Other objects
    Handler handler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rights, container, false);

        //Initializing objects and variables
        txtRights = (TextView) view.findViewById(R.id.txtRights);

        CONST_LEFT_MARGIN = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {

                if (txtRights.getX() > SCREEN_WIDTH) {
                    txtRights.setX(-1 * txtRights.getWidth());
                }
                txtRights.animate().xBy(30);
                return true;
            }
        });

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        SCREEN_WIDTH = size.x;

        //Moving the logo
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    handler.sendEmptyMessage(0);
                    synchronized (this) {
                        try {
                            wait(250);
                        } catch (Exception ex) {}
                    }
                }
            }
        }).start();

        return view;
    }
}