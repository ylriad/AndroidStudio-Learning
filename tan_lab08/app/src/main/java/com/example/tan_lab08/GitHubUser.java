package com.example.tan_lab08;

public class GitHubUser {
    private String username;
    private String displayName;
    private String avatarUrl;
    private String profileUrl;

    public GitHubUser(String username, String displayName, String avatarUrl, String profileUrl) {
        this.username = username;
        this.displayName = displayName;
        this.avatarUrl = avatarUrl;
        this.profileUrl = profileUrl;
    }

    // Геттеры и сеттеры
    public String getUsername() { return username; }
    public String getDisplayName() { return displayName; }
    public String getAvatarUrl() { return avatarUrl; }
    public String getProfileUrl() { return profileUrl; }

    @Override
    public String toString() {
        return displayName + " (@" + username + ")";
    }
}