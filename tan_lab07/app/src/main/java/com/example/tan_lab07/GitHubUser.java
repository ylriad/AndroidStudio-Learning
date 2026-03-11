package com.example.tan_lab07;

public class GitHubUser {
    private String login;
    private String avatar_url;
    private String html_url;

    // Default constructor (required)
    public GitHubUser() {
    }

    // Getters
    public String getLogin() {
        return login;
    }

    public String getAvatarUrl() {
        return avatar_url;
    }

    public String getHtmlUrl() {
        return html_url;
    }

    // Setters - THESE ARE MISSING!
    public void setLogin(String login) {
        this.login = login;
    }

    public void setAvatarUrl(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public void setHtmlUrl(String html_url) {
        this.html_url = html_url;
    }
}