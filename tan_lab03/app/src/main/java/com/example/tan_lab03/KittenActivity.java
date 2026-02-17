package com.example.tan_lab03;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class KittenActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private RadioButton selectRadioButton;
    private Spinner spinner;
    private CheckBox checkBox1, checkBox2;
    private EditText editText;

    private String userName, houseType, city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);
        setContentView(R.layout.activity_kitten);

        Intent receivedIntent = getIntent();
        userName = receivedIntent.getStringExtra("Your name");
        houseType = receivedIntent.getStringExtra("House type");
        city = receivedIntent.getStringExtra("City");


        ImageView animatedImageView = findViewById(R.id.animatedImageView);

        AnimationDrawable frameAnimation = (AnimationDrawable) animatedImageView.getBackground();

        animatedImageView.post(() -> {
            if (frameAnimation != null && !frameAnimation.isRunning()) {
                frameAnimation.start();
            }
        });
    }


    public void onClickPrv(View v) {
        setResult(RESULT_OK);
        finish();
    }


    public void onClickNxt(View v) {
        Intent intent = new Intent(KittenActivity.this, ResultActivity.class);



        intent.putExtra("Your name", userName);
        intent.putExtra("House type", houseType);
        intent.putExtra("City", city);

        selectRadioButton = findViewById(radioGroup.getCheckedRadioButtonId());

        intent.putExtra("Color", selectRadioButton.getText());
        if (checkBox1.isChecked() && checkBox2.isChecked()) {
            intent.putExtra("Personality", getString(R.string.neutral));
        }else if (checkBox1.isChecked()) {
            intent.putExtra("Personality", getString(R.string.happy));
        } else if (checkBox2.isChecked()) {
            intent.putExtra("Personality", getString(R.string.sad));
        }

        intent.putExtra("Kitten's name", editText.getText().toString());

        startActivity(intent);
    }

    public void onClickExit(View v) {
        finishAffinity();
    }
}