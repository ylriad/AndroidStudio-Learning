package com.example.tan_lab05;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        String name = getIntent().getStringExtra("name");
        String desc = getIntent().getStringExtra("desc");
        String link = getIntent().getStringExtra("link");

        String html = "<html><body style='font-family: sans-serif; padding: 20px;'>" +
                "<h1>" + name + "</h1>" +
                "<p>" + desc + "</p>" +
                "<br><br>" +
                "<h3>Links:</h3>" +
                "<ul>" +
                "<li><a href='" + link + "'>Official Website</a></li>" +
                "<li><a href='https://yandex.ru/maps/?text=" + name + "'>Yandex Maps</a></li>" +
                "<li><a href='https://google.com/maps/search/" + name + "'>Google Maps</a></li>" +
                "</ul>" +
                "</body></html>";

        webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
    }
}