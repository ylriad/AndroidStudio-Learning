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
    private static final int DATABASE_VERSION = 7; // НОМЕР ВЕРСИИ БАЗЫ ДАННЫХ И ТАБЛИЦ !

    static final String DATABASE_NAME = "phone_models"; // Имя базы данных

    static final String TABLE_NAME = "prices"; // Имя таблицы
    static final String ID = "id"; // Поле с ID
    static final String MODEL = "model"; // Поле с наименованием модели телефона
    static final String MODEL_LC = "model_lc"; // // Поле с наименованием модели телефона в нижнем регистре
    static final String MODEL_PRICE = "model_price"; // Поле с телефонным номером

    static final String ASSETS_FILE_NAME = "phone.txt"; // Имя файла из ресурсов с данными для БД
    static final String DATA_SEPARATOR = "|"; // Разделитель данных в файле ресурсов с телефонами

    private Context context; // Контекст приложения

    public MySQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // Метод создания базы данных и таблиц в ней
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

    // Метод при обновлении структуры базы данных и/или таблиц в ней
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        System.out.println("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Добавление нового контакта в БД
    public void addData(SQLiteDatabase db, String name, String phoneNumber) {
        ContentValues values = new ContentValues();
        values.put(MODEL, name);
        values.put(MODEL_LC, name.toLowerCase());
        values.put(MODEL_PRICE, phoneNumber);
        db.insert(TABLE_NAME, null, values);
    }

    // Добавление записей в базу данных из файла ресурсов
    public void loadDataFromAsset(Context context, String fileName, SQLiteDatabase db) {
        BufferedReader in = null;

        try {
            // Открываем поток для работы с файлом с исходными данными
            InputStream is = context.getAssets().open(fileName);
            // Открываем буфер обмена для потока работы с файлом с исходными данными
            in = new BufferedReader(new InputStreamReader(is));

            String str;
            while ((str = in.readLine()) != null) { // Читаем строку из файла
                String strTrim = str.trim(); // Убираем у строки пробелы с концов
                if (!strTrim.equals("")) { // Если строка не пустая, то
                    StringTokenizer st = new StringTokenizer(strTrim, DATA_SEPARATOR); // Нарезаем ее на части
                    String model = st.nextToken().trim(); // Извлекаем из строки название модели без пробелов на концах
                    String modelPrice = st.nextToken().trim(); // Извлекаем из строки номер модели без пробелов на концах
                    addData(db, model, modelPrice); // Добавляем название и цену в базу данных
                }
            }

            // Обработчики ошибок
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

    // Получение значений данных из БД в виде строки с фильтром
    public String getData(String filter) {

        String selectQuery; // Переменная для SQL-запроса

        if (filter.equals("")) {
            selectQuery = "SELECT  * FROM " + TABLE_NAME + " ORDER BY " + MODEL;
        } else {
            selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE (" + MODEL_LC + " LIKE '%" +
                    filter.toLowerCase() + "%'" +
                    " OR " + MODEL_PRICE + " LIKE '%" + filter + "%'" + ") ORDER BY " + MODEL;
        }
        SQLiteDatabase db = this.getReadableDatabase(); // Доступ к БД
        Cursor cursor = db.rawQuery(selectQuery, null); // Выполнение SQL-запроса

        StringBuilder data = new StringBuilder(); // Переменная для формирования данных из запроса

        int num = 0;
        if (cursor.moveToFirst()) { // Если есть хоть одна запись, то
            do { // Цикл по всем записям результата запроса
                int n = cursor.getColumnIndex(MODEL);
                int t = cursor.getColumnIndex(MODEL_PRICE);
                String name = cursor.getString(n); // Чтение названия модели
                String phoneNumber = cursor.getString(t); // Чтение цены
                data.append(String.valueOf(++num) + ") " + name + ": " + phoneNumber + "\n");
            } while (cursor.moveToNext()); // Цикл пока есть следующая запись
        }
        return data.toString(); // Возвращение результата
    }

}