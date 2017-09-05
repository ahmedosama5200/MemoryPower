package com.ahmedosama.memorypower;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.Locale;

public class ReviewActivity extends AppCompatActivity {


    DrawerLayout drawerLayoutMain;
    ActionBarDrawerToggle mToggle;
    // Data base
    private TheDataBaseHelper dbHelper;

    // Data base variables
    private int curPos;
    private int reviewWords;

    // References to views
    private TextView txtEnglishWord;
    private TextView txtTranslation;
    private TextView txtExample;
    private Button btnNext;
    private Button btnPrev;

    //Another
    private TextToSpeech toSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Opening data base
        dbHelper = new TheDataBaseHelper(this);
        try {
            dbHelper.createDataBase();
        }
        catch(IOException ex) {
            throw new Error("Unable to create database");
        }

        try {
            dbHelper.openDataBase();
        }
        catch(SQLException ex){
            throw new Error("Can't open the database");
        }
        // Deciding which view to inflate
        reviewWords = dbHelper.getnReviewWords();

        if (reviewWords == 0) {
            setContentView(R.layout.activity_review2);
            findViewById(R.id.btnLearn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnLearn_onClicked();
                }
            });
            setNavigationView();
            return;
        }

        setContentView(R.layout.activity_review);

        // Getting references to views
        txtEnglishWord = (TextView) findViewById(R.id.txtEnglishWord);
        txtTranslation = (TextView) findViewById(R.id.txtTranslation);
        txtExample = (TextView) findViewById(R.id.txtExample);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnPrev = (Button) findViewById(R.id.btnPrev);

        // Setting views' handlers
        txtEnglishWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if(status != TextToSpeech.ERROR) {
                            toSpeech.setLanguage(Locale.UK);
                        }
                    }
                });
                toSpeech.speak(txtEnglishWord.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnNext_onClick();
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPrev_onClick();
            }
        });

        //Changing the direction depending on the language of the device
        if (Locale.getDefault().getDisplayLanguage().equals(Locale.ENGLISH.getDisplayCountry())) {

            RelativeLayout.LayoutParams  txtExampleLayoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            txtExampleLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            txtExampleLayoutParams.addRule(RelativeLayout.BELOW, R.id.txtTitleExample);
            txtExample.setLayoutParams(txtExampleLayoutParams);

        }
        else {

            RelativeLayout.LayoutParams  txtExampleLayoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            txtExampleLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            txtExampleLayoutParams.addRule(RelativeLayout.BELOW, R.id.txtTitleExample);
            txtExample.setLayoutParams(txtExampleLayoutParams);

        }

        // Setting the layout
        curPos = 1;
        setWordLayout(curPos);

        setNavigationView();
    }

    private void btnNext_onClick() {

        if (curPos >= 1 &&  curPos < reviewWords - 1) {
            setWordLayout(++curPos);
            btnPrev.setEnabled(true);
            btnNext.setEnabled(true);
            btnNext.setText(getResources().getString(R.string.str_next));
        }
        else if (curPos == reviewWords - 1) {
            setWordLayout(++curPos);
            btnPrev.setEnabled(true);
            btnNext.setEnabled(true);
            btnNext.setText(getResources().getString(R.string.str_finish));
        }
        else if (curPos == reviewWords) {
            setContentView(R.layout.activity_review3);
            setNavigationView();
            findViewById(R.id.btnMainMenu).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnMainMenu_onClicked();
                }
            });

        }

    }

    private void btnPrev_onClick() {
        if (curPos == 2) {
            setWordLayout(--curPos);
            btnPrev.setEnabled(false);
            btnNext.setEnabled(true);
            btnNext.setText(getResources().getString(R.string.str_next));
        }
        else if (curPos > 2 && curPos <= reviewWords) {
            setWordLayout(--curPos);
            btnPrev.setEnabled(true);
            btnNext.setEnabled(true);
            btnNext.setText(getResources().getString(R.string.str_next));
        }

    }

    private void btnLearn_onClicked() {
        Intent intent = new Intent(this, SetupActivity.class);
        startActivity(intent);
    }

    private void btnMainMenu_onClicked() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void setWordLayout(int pos) {
        txtEnglishWord.setText(dbHelper.getWord(pos));
        txtTranslation.setText(dbHelper.getTranslation(pos));
        txtExample.setText(dbHelper.getExample(pos));
    }

    private void setNavigationView() {
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
