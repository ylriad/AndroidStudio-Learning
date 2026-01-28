package com.example.tan_lab02;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tan_lab02.R;

public class MainActivity extends AppCompatActivity {

    EditText editText_a;
    EditText editText_b;
    EditText editText_x;
    TextView textView_sum;
    Button buttonSum;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("x_value", editText_x.getText().toString());
        outState.putString("a_value", editText_a.getText().toString());
        outState.putString("b_value", editText_b.getText().toString());
        outState.putString("result_value", textView_sum.getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText_a = findViewById(R.id.editText_a);
        editText_b = findViewById(R.id.editText_b);
        editText_x = findViewById(R.id.editText_x);
        textView_sum = findViewById(R.id.textView_sum);
        buttonSum = findViewById(R.id.buttonSum);

        if (savedInstanceState != null) {
            editText_x.setText(savedInstanceState.getString("x_value", ""));
            editText_a.setText(savedInstanceState.getString("a_value", ""));
            editText_b.setText(savedInstanceState.getString("b_value", ""));
            textView_sum.setText(savedInstanceState.getString("result_value", ""));
        }
    }

    public void onClick(View v) {
        if (editText_x.getText().toString().trim().isEmpty() ||
                editText_a.getText().toString().trim().isEmpty() ||
                editText_b.getText().toString().trim().isEmpty()) {
            textView_sum.setText("Заполните все поля!");
            return;
        }

        double x, a, b, y;

        try {

            x = Double.parseDouble(editText_x.getText().toString().trim());
            a = Double.parseDouble(editText_a.getText().toString().trim());
            b = Double.parseDouble(editText_b.getText().toString().trim());

            if (x >= 4) {
                if (Math.pow(a, 3) - b == 0) {
                    textView_sum.setText("При использовании данных переменных получается ошибка деления на ноль.");
                } else {
                    y = 12*x/(Math.pow(a, 3) - b);
                    textView_sum.setText(String.format("y = %.2f", y));
                }
            } else{
                y = 3*Math.pow(x, 2) - a*b;
                textView_sum.setText(String.format("y = %.2f", y));
            }


        } catch (Exception e) {
            textView_sum.setText("Неверные входные данные для расчета!");
        }

    }

    public void onClickClear(View c) {
        editText_a.setText("");
        editText_b.setText("");
        editText_x.setText("");
        textView_sum.setText("");
    }
}