package com.example.tan_lab06;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final int LARGE_FONT = 16;
    private final int SMALL_FONT = 12;
    private int fontSize = SMALL_FONT;

    MySQLite db = new MySQLite(this);

    EditText editText;
    TextView textView;
    static final String FILTER = "FILTER";
    String filter = "";

    SharedPreferences sPref;
    static final String CONFIG_FILE_NAME = "Config";
    static final String FONT_SIZE = "FontSize";

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(FILTER, filter);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);

        textView.setKeyListener(null);

        sPref = getSharedPreferences(CONFIG_FILE_NAME, MODE_PRIVATE);
        fontSize = sPref.getInt(FONT_SIZE, SMALL_FONT);

        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        textView.requestFocus();

        if (savedInstanceState != null) {
            editText.setText(savedInstanceState.getString(FILTER));
        }

        textView.setText(R.string.Загрузка_данных);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                new Thread(new Runnable() {
                    public void run() {
                        filter = editText.getText().toString().trim();
                        final String data = db.getData(filter);
                        textView.post(new Runnable() {
                            public void run() {
                                textView.setText(data);
                            }
                        });
                    }
                }).start();

            }

        });

        editText.post(new Runnable() {
            public void run() {
                editText.setText(filter);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.large_font).setChecked(fontSize == LARGE_FONT);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.email) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.myemail)});
            i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.Добавьте_еще_модель));
            i.putExtra(Intent.EXTRA_TEXT, getString(R.string.Предлагаю_такую_модель));
            try {
                startActivity(Intent.createChooser(i, getString(R.string.Посылка_письма)));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(MainActivity.this, R.string.Нет_установленного_почтового_клиента, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        if (id == R.id.large_font) {
            item.setChecked(!item.isChecked());
            int size = item.isChecked() ? LARGE_FONT : SMALL_FONT;
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            fontSize = size;
            return true;
        }
        if (id == R.id.exit) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor ed = sPref.edit();
        ed.putInt(FONT_SIZE, fontSize);
        ed.apply();
    }
}
