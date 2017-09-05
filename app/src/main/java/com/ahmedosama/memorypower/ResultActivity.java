package com.ahmedosama.memorypower;

import android.content.Intent;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    DrawerLayout drawerLayoutMain;
    ActionBarDrawerToggle mToggle;
    // Data base
    TheDataBaseHelper dbHelper;

    // Data base variables
    private int count;
    private int pos;
    private int lowestMark = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Opening data base
        dbHelper = new TheDataBaseHelper(this);
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
        // Getting references to views
        TextView txtResult = (TextView) findViewById(R.id.txtResult);
        TextView txtMark = ((TextView) findViewById(R.id.txtMark));
        Button btnResult = (Button) findViewById(R.id.btnResult);
        ListView mylstviewWrongAnswers = (ListView) findViewById(R.id.lstviewWrongAnswers);

        // Assigning database variables
        pos = dbHelper.getPos();

        // Getting information sent
        Bundle data = getIntent().getExtras();

        count = data.getInt("count");
        ArrayList<String> lstWrongAnswers = data.getStringArrayList("lstWrongAnswers");
        ArrayList<String> lstRightAnswers = data.getStringArrayList("lstRightAnswers");
        ArrayList<String> lstQuestionTitles = data.getStringArrayList("lstQuestionTitles");

        // Calculating the needed variables
        int wrongAnswers = lstWrongAnswers.size();
        int rightAnswers = count - wrongAnswers;
        int percent = (int) (((double)rightAnswers / (double)count) * 100.0);

        //Setting the layout : Shared Settings
        txtMark.setText(percent + "%");

        CustomAdapter customAdapter = new CustomAdapter(this, lstRightAnswers, lstQuestionTitles);
        mylstviewWrongAnswers.setAdapter(customAdapter);

        //Different Settings depending on the mark
        if (percent >= lowestMark) {
            //Passed the test
            dbHelper.setPos(pos + count);

            btnResult.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnContinue_onClicked(v);
                }
            });

            txtMark.setTextColor(Color.GREEN);

            txtResult.setText(getResources().getString(R.string.str_txtcontinue));
            btnResult.setText(getResources().getString(R.string.str_continue));

        } else if (percent < lowestMark) {
            ///Failed in the test
            btnResult.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnBack_onClicked(v);
                }
            });
            txtMark.setTextColor(Color.RED);

            txtResult.setText(getResources().getString(R.string.str_txtback));
            btnResult.setText(getResources().getString(R.string.str_back));
        }

        //Setting the navigation view
        drawerLayoutMain = (DrawerLayout) findViewById(R.id.drawerLayoutMain);
        mToggle = new ActionBarDrawerToggle(this, drawerLayoutMain, R.string.str_open, R.string.str_close);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbarNav);

        setSupportActionBar(mToolbar);
        drawerLayoutMain.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView nav = (NavigationView) findViewById(R.id.MainNav);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return onNavItemSelected(item);
            }
        });
    }

    private void btnContinue_onClicked(View v) {
        Intent intent = new Intent(this, SetupActivity.class);
        startActivity(intent);
        /*
        if (nRestWords < count) {
            Intent intent = new Intent(this, SetupActivity.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this, LearnActivity.class);
            intent.putExtra("count", count);
            startActivity(intent);
        }
        */


    }

    private void btnBack_onClicked(View v) {
        Intent intent = new Intent(this, LearnActivity.class);
        intent.putExtra("count", count);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean onNavItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.navMain)
        {
            Intent intentMain = new Intent(this, MainActivity.class);
            startActivity(intentMain);
        }
        else if (id == R.id.navLearn)
        {
            Intent intentLearn = new Intent(this, SetupActivity.class);
            startActivity(intentLearn);
        }
        else if (id == R.id.navReview)
        {
            Intent intentReview = new Intent(this, ReviewActivity.class);
            startActivity(intentReview);
        }
        else if (id == R.id.navProgress)
        {
            Intent intentProgress = new Intent(this, ProgressActivity.class);
            startActivity(intentProgress);
        }
        else if (id == R.id.navAbout)
        {
            Intent intentAbout = new Intent(this, AboutActivity.class);
            startActivity(intentAbout);
        }
        else if (id == R.id.navExit)
        {
            new CustomDialogClass(this).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayoutMain);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LearnActivity.class);
        intent.putExtra("count", count);
        startActivity(intent);

    }

}
