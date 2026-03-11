package com.example.tan_lab08;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitHubFollowingParser {

    private static final String BASE_URL = "https://github.com";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";

    public interface ParseCallback {
        void onSuccess(List<GitHubUser> users);
        void onError(String errorMessage);
    }

    /**
     * Асинхронный парсинг списка following с коллбэком
     */
    public static void parseFollowingAsync(String username, ParseCallback callback) {
        new Thread(() -> {
            try {
                List<GitHubUser> users = parseFollowing(username);
                if (users != null) {
                    callback.onSuccess(users);
                } else {
                    callback.onError("Пустой результат парсинга");
                }
            } catch (Exception e) {
                callback.onError("Ошибка: " + e.getMessage());
            }
        }).start();
    }

    /**
     * Синхронный метод парсинга (вызывать только из фонового потока!)
     */
    public static List<GitHubUser> parseFollowing(String username) {
        List<GitHubUser> users = new ArrayList<>();
        String url = BASE_URL + "/" + username + "?tab=following";

        try {
            Document doc = Jsoup.connect(url)
                    .userAgent(USER_AGENT)
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                    .header("Accept-Language", "en-US,en;q=0.5")
                    .header("Connection", "keep-alive")
                    .timeout(15000)
                    .ignoreHttpErrors(true)
                    .followRedirects(true)
                    .get();

            // Ищем элементы пользователей - несколько возможных селекторов
            // Структура GitHub может меняться, поэтому используем гибкий подход
            Elements userItems = doc.select(
                    "div.follow-list-item, " +
                            "li.follow-list-item, " +
                            "[data-hovercard-type='user'], " +
                            "article[data-component='UserListItem']"
            );

            // Если не нашли по стандартным селекторам, пробуем альтернативный поиск
            if (userItems.isEmpty()) {
                // Поиск по ссылкам на профили
                Elements profileLinks = doc.select("a[data-test-id='user-name-link'], a.Link--secondary[href^='/" + username + "/']");
                for (Element link : profileLinks) {
                    GitHubUser user = extractUserFromLink(link);
                    if (user != null) {
                        users.add(user);
                    }
                }
                return users;
            }

            for (Element item : userItems) {
                try {
                    GitHubUser user = extractUserFromItem(item);
                    if (user != null && user.getUsername() != null && !user.getUsername().isEmpty()) {
                        users.add(user);
                    }
                } catch (Exception e) {
                    // Пропускаем проблемные элементы, продолжаем парсинг
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return users;
    }

    private static GitHubUser extractUserFromItem(Element item) {
        try {
            // Ищем ссылку на профиль
            Element link = item.selectFirst("a[href^='/'][href*='/']:not([href*='/sponsors'])");
            if (link == null) return null;

            String profileHref = link.attr("href");
            String parsedUsername = extractUsernameFromHref(profileHref);
            if (parsedUsername == null) return null;

            String profileUrl = link.absUrl("href");

            // Ищем аватар
            Element avatar = item.selectFirst("img.avatar-user, img.avatar, img[data-component='avatar']");
            String avatarUrl = "";
            if (avatar != null) {
                avatarUrl = avatar.absUrl("src");
                // GitHub может использовать data-src для lazy loading
                if (avatarUrl.isEmpty()) {
                    avatarUrl = avatar.absUrl("data-src");
                }
            }

            // Ищем отображаемое имя
            String displayName = "";
            Element nameEl = item.selectFirst("span.Link--primary, span.color-fg-default, a span:not([class*='secondary'])");
            if (nameEl != null) {
                displayName = nameEl.text().trim();
            }

            // Если имя пустое, пробуем взять из alt аватара или заголовка ссылки
            if (displayName.isEmpty()) {
                if (avatar != null) {
                    String alt = avatar.attr("alt");
                    if (alt.startsWith("@")) {
                        displayName = alt.substring(1).trim();
                    }
                }
                if (displayName.isEmpty()) {
                    displayName = parsedUsername;
                }
            }

            return new GitHubUser(parsedUsername, displayName, avatarUrl, profileUrl);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static GitHubUser extractUserFromLink(Element link) {
        try {
            String profileHref = link.attr("href");
            String parsedUsername = extractUsernameFromHref(profileHref);
            if (parsedUsername == null) return null;

            String profileUrl = link.absUrl("href");
            String displayName = link.text().trim();
            if (displayName.isEmpty()) {
                displayName = parsedUsername;
            }

            // Ищем аватар в родительских элементах
            String avatarUrl = "";
            Element parent = link.parent();
            if (parent != null) {
                Element avatar = parent.selectFirst("img.avatar-user, img.avatar");
                if (avatar != null) {
                    avatarUrl = avatar.absUrl("src");
                }
            }

            return new GitHubUser(parsedUsername, displayName, avatarUrl, profileUrl);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Извлекает username из href вида "/username" или "https://github.com/username"
     */
    private static String extractUsernameFromHref(String href) {
        if (href == null || href.isEmpty()) return null;

        // Убираем домен если есть
        String path = href.replace(BASE_URL, "");

        // Разбиваем по / и ищем username
        String[] parts = path.split("/");
        for (String part : parts) {
            if (!part.isEmpty() && !part.equals("tab") && !part.equals("following")) {
                // Валидация: username не должен содержать специальных символов
                if (Pattern.matches("^[a-zA-Z0-9][a-zA-Z0-9-]*[a-zA-Z0-9]?$|^[a-zA-Z0-9]$", part)) {
                    return part;
                }
            }
        }
        return null;
    }
}