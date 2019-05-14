package com.softproject.who.main;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
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
import com.softproject.who.BaseContract;
import com.softproject.who.R;
import com.softproject.who.list.ListActivity;
import com.twitter.sdk.android.core.Twitter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements BaseContract.BaseView, AuthenticationListener{

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
       AuthenticationDialog dialog = new AuthenticationDialog(this, this);
       dialog.setCancelable(true);
       dialog.show();
   }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Twitter.initialize(this);
        mPresenter = new MainPresenter(this);
    }

    @Override
    public void onStart(){
        super.onStart();
        mPresenter.onStart();
    }

    public void startListActivity(){
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        //mPresenter.twitterAuthClient.onActivityResult(requestCode, resultCode, data);
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

    @Override
    public void onCodeReceived(String authToken) {
        Log.d("token", authToken);
    }
}
