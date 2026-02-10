package com.example.tan_lab03;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private RadioGroup radioGroup;
    private RadioButton selectRadioButton;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);
        setContentView(R.layout.activity_main);

        editText =   findViewById(R.id.editText);
        radioGroup = findViewById(R.id.radioGroup);
        spinner =    findViewById(R.id.spinner);


        ImageView animatedImageView = findViewById(R.id.animatedImageView);

        AnimationDrawable frameAnimation = (AnimationDrawable) animatedImageView.getBackground();

        animatedImageView.post(() -> {
            if (frameAnimation != null && !frameAnimation.isRunning()) {
                frameAnimation.start();
            }
        });
    }


    public void onClickNxt(View v) {
        Intent intent = new Intent(MainActivity.this, KittenActivity.class);

        selectRadioButton = findViewById(radioGroup.getCheckedRadioButtonId());

        intent.putExtra("Your name", editText.getText().toString());
        intent.putExtra("House type", selectRadioButton.getText());
        intent.putExtra("City", spinner.getSelectedItem().toString());

        startActivity(intent);
    }

    public void onClickExit(View v) {
        finishAffinity();
    }
}