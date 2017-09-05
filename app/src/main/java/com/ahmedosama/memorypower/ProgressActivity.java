package com.ahmedosama.memorypower;

import android.content.Intent;
import android.database.SQLException;
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
import android.widget.TextView;

import java.io.IOException;

public class ProgressActivity extends AppCompatActivity {

    DrawerLayout drawerLayoutMain;
    ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        // Opening data base
        TheDataBaseHelper dbHelper = new TheDataBaseHelper(this);
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

        // Getting references to views
        TextView txtProgress = (TextView) findViewById(R.id.txtProgress);
        TextView txtLearnedWords = (TextView) findViewById(R.id.txtLearnedWords);
        TextView txtRestWords = (TextView) findViewById(R.id.txtRestWords);
        Button btnLearn = (Button) findViewById(R.id.btnLearn);
        Button btnReview = (Button) findViewById(R.id.btnReview);

        // Setting handlers
        btnLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SetupActivity.class);
                startActivity(intent);
            }
        });

        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
                startActivity(intent);
            }
        });

        // Setting the layout
        int nLearn = dbHelper.getnReviewWords();
        int nRest = dbHelper.getnRestWords();
        int progress = (int) (((double) nLearn / ((double) nRest + (double) nLearn)) * 100.0);

        txtProgress.setText(progress + "%");
        txtLearnedWords.setText(String.valueOf(nLearn));
        txtRestWords.setText(String.valueOf(nRest));

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
        if (drawerLayoutMain.isDrawerOpen(GravityCompat.START))
        {
            drawerLayoutMain.closeDrawer(GravityCompat.START);
        }
        else
        {
            drawerLayoutMain.openDrawer(GravityCompat.START);
        }
    }



}