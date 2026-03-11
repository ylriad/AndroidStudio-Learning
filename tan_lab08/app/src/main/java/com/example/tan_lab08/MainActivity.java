package com.example.tan_lab08;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private Button parseButton;
    private TextView resultTextView;
    private ProgressBar progressBar;

    private static final String TAG = "GitHubParser";
    private static final String DEFAULT_USERNAME = "proffix4";
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        usernameEditText = findViewById(R.id.usernameEditText);
        parseButton = findViewById(R.id.parseButton);
        resultTextView = findViewById(R.id.resultTextView);
        progressBar = findViewById(R.id.progressBar);

        usernameEditText.setText(DEFAULT_USERNAME);
    }

    private void setupClickListeners() {
        parseButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            if (username.isEmpty()) {
                Toast.makeText(this, "Введите username GitHub", Toast.LENGTH_SHORT).show();
                return;
            }
            startParsing(username);
        });
    }

    private void startParsing(String username) {
        setLoadingState(true);
        resultTextView.setText("🔄 Загрузка данных с GitHub...\nПожалуйста, подождите.");

        GitHubFollowingParser.parseFollowingAsync(username, new GitHubFollowingParser.ParseCallback() {
            @Override
            public void onSuccess(List<GitHubUser> users) {
                runOnUiThread(() -> {
                    setLoadingState(false);
                    displayResults(users, username);
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    setLoadingState(false);
                    showError(errorMessage);
                });
            }
        });
    }

    private void displayResults(List<GitHubUser> users, String username) {
        if (users == null || users.isEmpty()) {
            resultTextView.setText("⚠️ Не найдено пользователей");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("✅ Найдено: ").append(users.size()).append(" пользователей\n\n")
                .append("📋 Подписки: @").append(username).append("\n\n")
                .append("━━━━━━━━━━━━━━━━━━━━━━━\n\n");

        for (int i = 0; i < users.size(); i++) {
            GitHubUser user = users.get(i);
            sb.append((i + 1)).append(". ").append(user.getDisplayName())
                    .append("\n   @").append(user.getUsername()).append("\n\n");
        }

        resultTextView.setText(sb.toString());
        Log.d(TAG, "Успешно распарсено " + users.size() + " пользователей");
    }

    private void showError(String message) {
        Log.e(TAG, "Ошибка: " + message);
        resultTextView.setText("❌ Ошибка:\n" + message);
        Toast.makeText(this, "Ошибка парсинга", Toast.LENGTH_LONG).show();
    }

    private void setLoadingState(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        parseButton.setEnabled(!isLoading);
        usernameEditText.setEnabled(!isLoading);
        parseButton.setText(isLoading ? "Загрузка..." : "🚀 Начать парсинг");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}