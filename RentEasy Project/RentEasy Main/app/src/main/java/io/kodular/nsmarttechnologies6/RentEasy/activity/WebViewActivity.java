package io.kodular.nsmarttechnologies6.RentEasy.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import io.kodular.nsmarttechnologies6.RentEasy.R;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        WebView webView = findViewById(R.id.web);

        String url = getIntent().getStringExtra("url");


        webView.loadUrl(url);


        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}