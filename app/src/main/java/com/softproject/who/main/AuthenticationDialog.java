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

public class AuthenticationDialog extends Dialog {
    private AuthenticationListener mListener;
    private Context mContext;
    private WebView mWebView;

    private String mUrl;


    public AuthenticationDialog(@NonNull Context context, AuthenticationListener listener) {
        super(context);
        mContext = context;
        mListener = listener;
        //mUrl = context.getResources().getString(R.string.base_url)
        //        +"oauth/authorize/?client_id="
        //        +context.getResources().getString(R.string.instagram_client_id)
        //        +"&redirect_uri="
        //        +context.getResources().getString(R.string.redirect_url)
        //        +"&response_type=token"
        //        +"&display=touch&scope=public_content";
        mUrl = "https://api.instagram.com/oauth/authorize/?client_id=457d99f202524bac86f7c560a34f6927&redirect_uri=http://localhost/&response_type=token";
        Log.d("insta", mUrl);
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
                    Log.d("access_token", accessToken);
                    mListener.onCodeReceived(accessToken);
                    dismiss();
                }else if(url.contains("?error")){
                    Log.d("access_token", "error");
                }
            }
        });
    }
}
