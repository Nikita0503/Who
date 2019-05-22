package com.softproject.who.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.ads.MobileAds;
import com.softproject.who.BaseContract;
import com.softproject.who.R;
import com.softproject.who.list.ListActivity;
import com.twitter.sdk.android.core.Twitter;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.auth.VKAccessToken;
import com.vk.api.sdk.auth.VKAuthCallback;
import com.vk.api.sdk.utils.VKUtils;

import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements BaseContract.BaseView{

    private MainPresenter mPresenter;

   @OnClick(R.id.imageViewFacebook)
   void onClickFacebook(){
       mPresenter.facebookLogin();
   }

   @OnClick(R.id.imageViewTwitter)
   void onClickTwitter() {
       mPresenter.twitterLogin();
   }

   @OnClick(R.id.imageViewInstagram)
   void onClickInstagram() {
       mPresenter.instagramLogin();
   }

   @OnClick(R.id.imageViewVk)
   void onClickVk(){
       mPresenter.vkLogin();
   }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        MobileAds.initialize(this, "ca-app-pub-3433509722016307~1743026273");
        //String[] fingerprints = VKUtils.getCertificateFingerprint(this, this.getPackageName());
        //Log.d("VK", fingerprints[0]);
        mPresenter = new MainPresenter(this);
    }

    @Override
    public void onStart(){
        super.onStart();
        mPresenter.onStart();
    }

    public void startListActivity(String socailId){
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("socialId", socailId);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==64206){
            mPresenter.facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
        if(requestCode==140){
            mPresenter.twitterAuthClient.onActivityResult(requestCode, resultCode, data);
        }
        if(requestCode==282) {
            VK.onActivityResult(requestCode, resultCode, data, mPresenter.vkAuthCallback);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop(){
       super.onStop();
       mPresenter.onStop();
    }


}
