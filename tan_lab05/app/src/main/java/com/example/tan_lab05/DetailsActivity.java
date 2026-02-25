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
        String ytId = getIntent().getStringExtra("yt");
        String link = getIntent().getStringExtra("link");

        String html = "<html><body style='font-family: sans-serif; padding: 20px;'>" +
                "<h1>" + name + "</h1>" +
                "<p>" + desc + "</p>" +
                "<hr>" +
                "<h3>Video Review:</h3>" +
                "<iframe width='100%' height='200' src='https://www.youtube.com/embed/" + ytId + "' frameborder='0' allowfullscreen></iframe>" +
                "<br><br>" +
                "<h3>Links:</h3>" +
                "<ul>" +
                "<li><a href='" + link + "'>Official Website</a></li>" +
                "<li><a href='https://yandex.ru/maps/?text=" + name + "'>Yandex Maps</a></li>" +
                "<li><a href='https://google.com/maps/search/" + name + "'>Google Maps</a></li>" +
                "</ul>" +
                "</body></html>";

        // Corrected for UTF-8 support
        webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
    }
}