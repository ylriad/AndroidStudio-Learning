package com.example.tan_lab07;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText etUsername;
    private Button btnSearch;
    private RecyclerView rvUsers;
    private ProgressBar progressBar;
    private GitHubApiHelper apiHelper;
    private UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = findViewById(R.id.etUsername);
        btnSearch = findViewById(R.id.btnSearch);
        rvUsers = findViewById(R.id.rvUsers);
        progressBar = findViewById(R.id.progressBar);

        apiHelper = new GitHubApiHelper();
        rvUsers.setLayoutManager(new LinearLayoutManager(this));

        // Load default: proffix4's following
        etUsername.setText("proffix4");
        loadFollowing("proffix4");

        btnSearch.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            if (!username.isEmpty()) {
                loadFollowing(username);
            }
        });
    }

    private void loadFollowing(String username) {
        showLoading(true);

        apiHelper.getFollowing(username, new GitHubApiHelper.ApiCallback() {
            @Override
            public void onSuccess(List<GitHubUser> users) {
                runOnUiThread(() -> {
                    showLoading(false);
                    adapter = new UserAdapter(users);
                    rvUsers.setAdapter(adapter);

                    if (users.isEmpty()) {
                        Toast.makeText(MainActivity.this,
                                "No followings found", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    showLoading(false);
                    Toast.makeText(MainActivity.this,
                            "Error: " + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        rvUsers.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}