package com.softproject.who.main;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.softproject.who.R;
import com.softproject.who.model.APIUtils;

public class AuthenticationDialog extends Dialog {
    private Context mContext;
    private WebView mWebView;
    private MainPresenter mPresenter;

    private String mUrl;


    public AuthenticationDialog(@NonNull Context context, MainPresenter presenter) {
        super(context);
        mContext = context;
        mUrl = "https://api.instagram.com/oauth/authorize/?client_id=457d99f202524bac86f7c560a34f6927&redirect_uri=http://localhost/&response_type=token";
        mPresenter = presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.auth_instagram_dialog);
        initializeWebView();
    }

    private void initializeWebView(){
        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setVerticalScrollBarEnabled(true);
        mWebView.setHorizontalScrollBarEnabled(true);
        mWebView.loadUrl(mUrl);
        mWebView.setWebViewClient(new WebViewClient(){

            boolean authComplete;
            String accessToken;

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(url.contains("#access_token=") && !authComplete){
                    Uri uri = Uri.parse(url);
                    accessToken = uri.getEncodedFragment();
                    accessToken = accessToken.substring(accessToken.lastIndexOf("=")+1);
                    mPresenter.fetchInstagramUserData(accessToken);
                    dismiss();
                }
            }
        });
    }
}
