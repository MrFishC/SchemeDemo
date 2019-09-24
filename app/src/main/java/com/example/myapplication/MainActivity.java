package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 核心代码在H5页面
 */
public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final WebView webView = findViewById(R.id.webview);

        String webUrl = "file:///android_asset/index.html";

        setCookie(webUrl);
        setWebView(webView);

        webView.loadUrl(webUrl);

    }

    private void setCookie(String webUrl) {

//        String userId = "123";
//        String systemName=" SYSTEM_JOINT_10006";
//        String PASS_KEY = "FH#$tr745OrtI53#‐U9^80*gE9U1IKC245gufyo_Do3";
//
//        CookieManager cookieManager = CookieManager.getInstance();
//        cookieManager.setCookie(webUrl, "userId=" + userId);
//        cookieManager.setCookie(webUrl, "systemName=" + systemName);
//        cookieManager.setCookie(webUrl, "key=" + key);
//        cookieManager.setCookie(webUrl, "app=android");

    }

    private void setWebView(final WebView webView) {

        WebSettings webSettings = webView.getSettings();

        //支持js
        webSettings.setJavaScriptEnabled(true);

        //将图片调整到适合webview的大小
//        webSettings.setUseWideViewPort(true);

        // 缩放至屏幕的大小
//        webSettings.setLoadWithOverviewMode(true);

        //其它...

        //对js交互的对话框、title以及页面加载进度条的管理

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                Log.i(TAG, "web-log-newProgress: " + newProgress);
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                //onConsoleMessage 与 h5交互信息
                Log.i(TAG, "onConsoleMessage: " + consoleMessage.message());
                return true;
            }
        });

        //对webview页面加载管理
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (urlCanLoad(url.toLowerCase())) {

                    if(url.contains(".apk")){
                        //若是apk的下载地址,则通过浏览器或者应用市场打开下载
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    }else {
                        webView.loadUrl(url);
                    }

                    return true;

                } else {

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    if (getPackageManager().resolveActivity(intent, 0) == null) {
                        Toast.makeText(MainActivity.this,"未安装",Toast.LENGTH_SHORT).show();
                        //提示去应用市场下载 可给出提示框,点击跳转下载
                        //todo
                    }else {
                        startActivity(intent);
                    }

                    return false;
                }

            }

        });

    }

    /**
     * 列举正常情况下能正常加载的网页url
     *
     * @param url
     * @return
     */
    private boolean urlCanLoad(String url) {
        return url.startsWith("http://") || url.startsWith("https://") ||
                url.startsWith("ftp://") || url.startsWith("file://");
    }

}
