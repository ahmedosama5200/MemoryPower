package com.ahmedosama.memorypower;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TheDataBaseHelper extends SQLiteOpenHelper {

    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.ahmedosama.memorypower/databases/";
    private static String DB_NAME = "maindatabase.db";

    private static final String TABLE_POS = "table_pos";
    private static final String POS_COLUMN = "pos";

    private static final String TABLE_WORDS = "table_words";
    private static final String _ID_COLUMN = "_id";
    private static final String WORD_COLUMN = "word";
    private static final String TRANSLATION_COLUMN = "translation";
    private static final String EXAMPLE_COLUMN = "example";

    private SQLiteDatabase myDataBase;
    private final Context myContext;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public TheDataBaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if(dbExist){
            //do nothing - database already exist
        }else{

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){

            //database does't exist yet.

        }

        if(checkDB != null){

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException{

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.

    String getWord(int pos) {
        Cursor c = myDataBase.rawQuery("SELECT * FROM " + TABLE_WORDS + " WHERE " + _ID_COLUMN + " = " + pos, null);
        c.moveToFirst();
        String word = c.getString(c.getColumnIndex(WORD_COLUMN));
        c.close();
        return word;
    }

    String getTranslation(int pos) {
        Cursor c = myDataBase.rawQuery("SELECT * FROM " + TABLE_WORDS + " WHERE " + _ID_COLUMN + " = " + pos, null);
        c.moveToFirst();
        String translation = c.getString(c.getColumnIndex(TRANSLATION_COLUMN));
        c.close();
        return translation;
    }

    String getExample(int pos) {
        Cursor c = myDataBase.rawQuery("SELECT * FROM " + TABLE_WORDS + " WHERE " + _ID_COLUMN + " = " + pos, null);
        c.moveToFirst();
        String example = c.getString(c.getColumnIndex(EXAMPLE_COLUMN));
        c.close();
        return example;
    }

    void setPos(int pos) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_POS + " SET " + POS_COLUMN + " = " + pos);
    }

    int getPos() {
        Cursor c = myDataBase.rawQuery("SELECT * FROM " + TABLE_POS, null);
        c.moveToFirst();
        int pos = Integer.parseInt(c.getString(c.getColumnIndex(POS_COLUMN)));
        c.close();
        return pos;
    }

    int getnAllWords() {
        Cursor c = myDataBase.rawQuery("SELECT COUNT(*) AS count FROM " + TABLE_WORDS, null);
        c.moveToFirst();
        int n = c.getInt(c.getColumnIndex("count"));
        c.close();
        return n;
    }

    int getnReviewWords() {
        int pos = getPos();
        return pos - 1;
    }

    int getnRestWords() {
        return getnAllWords() - getnReviewWords();
    }

    void setDefaultDataBase() {
        setPos(1);
    }

}