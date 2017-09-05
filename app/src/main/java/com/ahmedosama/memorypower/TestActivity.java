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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;


public class TestActivity extends AppCompatActivity {

    ArrayList<String> TranslationsWords;
    String[] lstTemp;
    private static final String LOG_TAG = "TestActivity";

    // Data base
    private TheDataBaseHelper dbHelper;

    // Needed variables
    private int count;
    private int pos;

    private ListView lstQuestions;
    private DrawerLayout drawerLayoutMain;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // Opening data base
        dbHelper = new TheDataBaseHelper(this);
        try {
            dbHelper.createDataBase();
        } catch (IOException ex) {
            throw new Error("Unable to create database");
        }

        try {
            dbHelper.openDataBase();
        } catch (SQLException ex) {
            throw new Error("Can't open the database");
        }

        // Getting References to views
        lstQuestions = (ListView) findViewById(R.id.lstQuestions);

        // Setting handlers
        findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSubmit_onClicked(v);
            }
        });

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBack_onClicked(v);
            }
        });

        // Assigning variables
        pos = dbHelper.getPos();
        count = getIntent().getExtras().getInt("count");

        // Populating the list view
        TranslationsWords = new ArrayList<>();
        for (int i = pos; i < pos + count; i++) {
            TranslationsWords.add(dbHelper.getTranslation(i));
        }

        lstTemp = new String[TranslationsWords.size()];

        MyCustomAdapter myCustomAdapter = new MyCustomAdapter();
        lstQuestions.setAdapter(myCustomAdapter);

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

    private void btnSubmit_onClicked(View v) {

        //Checking that all entries are available
        for (int i = 0; i < lstQuestions.getChildCount(); i++) {
            if (((EditText) lstQuestions.getChildAt(i).findViewById(R.id.edtxtAnswer)).getText().toString().equals("")) {
                Toast.makeText(this, "Enter all answers", Toast.LENGTH_LONG).show();
                return;
            }
        }

        // Preparing the information for sending them
        ArrayList<String> lstWrongAnswers = new ArrayList<>();
        ArrayList<String> lstRightAnswers = new ArrayList<>();
        ArrayList<String> lstQuestionTitles = new ArrayList<>();

        int p = pos;
        for (int i = 0; i < lstQuestions.getChildCount(); i++) {
            //String entry = ((EditText) lstQuestions.getChildAt(i).findViewById(R.id.edtxtAnswer)).getText().toString();
            String entry = ((EditText) lstQuestions.getAdapter().getView(i, null, null).findViewById(R.id.edtxtAnswer)).getText().toString();
            String answer = dbHelper.getWord(p);

            Log.i(LOG_TAG, "i : " + i);
            Log.i(LOG_TAG, "p : " + p);
            Log.i(LOG_TAG, "entry : " + entry);
            Log.i(LOG_TAG, "answer : " + answer);
            Log.i(LOG_TAG, "question title : " + dbHelper.getTranslation(p));

            if (!(entry.equals(answer))) {
                lstWrongAnswers.add(entry);
                lstRightAnswers.add(answer);
                lstQuestionTitles.add(dbHelper.getTranslation(p));

            }
            p++;

        }

        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("lstWrongAnswers", lstWrongAnswers);
        intent.putExtra("lstRightAnswers", lstRightAnswers);
        intent.putExtra("lstQuestionTitles", lstQuestionTitles);
        intent.putExtra("count", count);
        startActivity(intent);
    }

    private void btnBack_onClicked(View v) {
        this.onBackPressed();
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

        if (id == R.id.navMain) {
            Intent intentMain = new Intent(this, MainActivity.class);
            startActivity(intentMain);
        } else if (id == R.id.navLearn) {
            Intent intentLearn = new Intent(this, SetupActivity.class);
            startActivity(intentLearn);
        } else if (id == R.id.navReview) {
            Intent intentReview = new Intent(this, ReviewActivity.class);
            startActivity(intentReview);
        } else if (id == R.id.navProgress) {
            Intent intentProgress = new Intent(this, ProgressActivity.class);
            startActivity(intentProgress);
        } else if (id == R.id.navAbout) {
            Intent intentAbout = new Intent(this, AboutActivity.class);
            startActivity(intentAbout);
        } else if (id == R.id.navExit) {
            new CustomDialogClass(this).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayoutMain);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class MyCustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public Object getItem(int position) {
            return TranslationsWords.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = TestActivity.this.getLayoutInflater();
                convertView = inflater.inflate(R.layout.list_item_question, null);
                holder.txtQuestion = (TextView) convertView.findViewById(R.id.txtQuestion);
                holder.edtxtAnswer = (EditText) convertView.findViewById(R.id.edtxtAnswer);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.ref = position;
            holder.txtQuestion.setText(TranslationsWords.get(position));
            holder.edtxtAnswer.setText(lstTemp[position]);
            holder.edtxtAnswer.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    lstTemp[holder.ref] = s.toString();
                }
            });

            return convertView;

        }

        private class ViewHolder {
            TextView txtQuestion;
            EditText edtxtAnswer;
            int ref;
        }


    }

}