package com.example.tan_lab07;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GitHubApiHelper {
    private static final String BASE_URL = "https://api.github.com/users/";
    private final OkHttpClient client = new OkHttpClient();

    public interface ApiCallback {
        void onSuccess(List<GitHubUser> users);
        void onError(String error);
    }

    public void getFollowing(String username, ApiCallback callback) {
        String url = BASE_URL + username + "/following";

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Accept", "application/vnd.github+json") // GitHub recommends this [[web_extractor]]
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onError("HTTP error: " + response.code());
                    return;
                }

                try {
                    String jsonData = response.body().string();
                    JSONArray array = new JSONArray(jsonData);
                    List<GitHubUser> users = new ArrayList<>();

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        GitHubUser user = new GitHubUser();
                        // Use reflection or manual mapping; here's manual:
                        user.setLogin(obj.optString("login"));
                        user.setAvatarUrl(obj.optString("avatar_url"));
                        user.setHtmlUrl(obj.optString("html_url"));
                        users.add(user);
                    }
                    callback.onSuccess(users);
                } catch (Exception e) {
                    callback.onError("Parse error: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("Network error: " + e.getMessage());
            }
        });
    }
}
