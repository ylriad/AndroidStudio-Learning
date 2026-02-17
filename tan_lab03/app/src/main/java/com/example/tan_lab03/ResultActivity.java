package com.example.tan_lab03;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;



public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String user, color, personality, catName;

        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);
        setContentView(R.layout.activity_result);

        user = getIntent().getStringExtra("Your name");
        color = getIntent().getStringExtra("Color");
        personality = getIntent().getStringExtra("Personality");
        catName = getIntent().getStringExtra("Kitten's name");

        if (user == null){
            user = "Anonymous";
        }

        if (color == null){
            color = "black";
        }

        if (personality == null){
            personality = "neutral";
        }

        if (catName == null){
            catName = "Kitty";
        }

        TextView textView2 = findViewById(R.id.textView2);
        textView2.setKeyListener(null);
        textView2.setText("Congratulations, " + user + "\n");

        TextView textView3 = findViewById(R.id.textView3);
        textView3.setKeyListener(null);
        textView3.setText("Your " + color + " " + personality + " kitten will live in your " + getIntent().getStringExtra("House type") + " in " + getIntent().getStringExtra("City")  + "\n");

        TextView textView4 = findViewById(R.id.textView4);
        textView4.setKeyListener(null);
        textView4.setText(catName + "\n");
    }


    public void onGift(View v) {
        String color, personality, kitten;

        ImageButton imageButton = findViewById(R.id.imageButton);
        ImageView imageView = findViewById(R.id.imageView);
        ImageView imageView2 = findViewById(R.id.imageView2);

        imageButton.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.VISIBLE);
        imageView2.setVisibility(View.VISIBLE);

        color = getIntent().getStringExtra("Color");
        personality = getIntent().getStringExtra("Personality");

        kitten = personality + "_" + color;


        switch (kitten) {
            case "happy_black":
                imageView2.setImageResource(R.drawable.bh);
                break;
            case "happy_white":
                imageView2.setImageResource(R.drawable.wh);
                break;
            case "happy_orange":
                imageView2.setImageResource(R.drawable.oh);
                break;
            case "happy_triple-colored":
                imageView2.setImageResource(R.drawable.th);
                break;
            case "sad_black":
                imageView2.setImageResource(R.drawable.bs);
                break;
            case "sad_white":
                imageView2.setImageResource(R.drawable.ws);
                break;
            case "sad_orange":
                imageView2.setImageResource(R.drawable.os);
                break;
            case "sad_triple-colored":
                imageView2.setImageResource(R.drawable.ts);
                break;
            case "neutral_black":
                imageView2.setImageResource(R.drawable.bn);
                break;
            case "neutral_white":
                imageView2.setImageResource(R.drawable.wn);
                break;
            case "neutral_orange":
                imageView2.setImageResource(R.drawable.on);
                break;
            case "neutral_triple-colored":
                imageView2.setImageResource(R.drawable.tn);
                break;
        }
    }


    public void onClickPrv(View v) {
        setResult(RESULT_OK);
        finish();
    }

    public void onClickExit(View v) {
        finishAffinity();
    }

}