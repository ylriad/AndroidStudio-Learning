package com.example.tan_lab06;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class MySQLite extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 7;

    static final String DATABASE_NAME = "phone_models";

    static final String TABLE_NAME = "prices";
    static final String ID = "id";
    static final String MODEL = "model";
    static final String MODEL_LC = "model_lc";
    static final String MODEL_PRICE = "model_price";
    static final String ASSETS_FILE_NAME = "phone.txt";
    static final String DATA_SEPARATOR = "|";

    private Context context;

    public MySQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY,"
                + MODEL + " TEXT,"
                + MODEL_LC + " TEXT,"
                + MODEL_PRICE + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
        System.out.println(CREATE_CONTACTS_TABLE);
        loadDataFromAsset(context, ASSETS_FILE_NAME,  db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        System.out.println("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addData(SQLiteDatabase db, String name, String phoneNumber) {
        ContentValues values = new ContentValues();
        values.put(MODEL, name);
        values.put(MODEL_LC, name.toLowerCase());
        values.put(MODEL_PRICE, phoneNumber);
        db.insert(TABLE_NAME, null, values);
    }

    public void loadDataFromAsset(Context context, String fileName, SQLiteDatabase db) {
        BufferedReader in = null;

        try {
            InputStream is = context.getAssets().open(fileName);
            in = new BufferedReader(new InputStreamReader(is));

            String str;
            while ((str = in.readLine()) != null) {
                String strTrim = str.trim();
                if (!strTrim.equals("")) {
                    StringTokenizer st = new StringTokenizer(strTrim, DATA_SEPARATOR);
                    String model = st.nextToken().trim();
                    String modelPrice = st.nextToken().trim();
                    addData(db, model, modelPrice);
                }
            }

        } catch (IOException ignored) {
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignored) {
                }
            }
        }

    }

    public String getData(String filter) {

        String selectQuery;

        if (filter.equals("")) {
            selectQuery = "SELECT  * FROM " + TABLE_NAME + " ORDER BY " + MODEL;
        } else {
            selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE (" + MODEL_LC + " LIKE '%" +
                    filter.toLowerCase() + "%'" +
                    " OR " + MODEL_PRICE + " LIKE '%" + filter + "%'" + ") ORDER BY " + MODEL;
        }
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        StringBuilder data = new StringBuilder();

        int num = 0;
        if (cursor.moveToFirst()) {
            do {
                int n = cursor.getColumnIndex(MODEL);
                int t = cursor.getColumnIndex(MODEL_PRICE);
                String name = cursor.getString(n);
                String phoneNumber = cursor.getString(t);
                data.append(String.valueOf(++num) + ") " + name + ": " + phoneNumber + "\n");
            } while (cursor.moveToNext());
        }
        return data.toString();
    }

}