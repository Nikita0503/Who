package com.softproject.who.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.softproject.who.BaseContract;
import com.softproject.who.R;
import com.softproject.who.model.APIUtils;
import com.softproject.who.model.DataTransformer;
import com.softproject.who.model.data.Userdata;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.AuthHandler;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONObject;

import java.util.Arrays;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;


public class MainPresenter implements BaseContract.BasePresenter {

    public CallbackManager facebookCallbackManager;
    public TwitterAuthClient twitterAuthClient;
    private CompositeDisposable mDisposable;
    private MainActivity mActivity;
    private APIUtils mAPIUtils;
    private DataTransformer mDataTransformer;

    public MainPresenter(MainActivity activity) {
        mActivity = activity;
        mAPIUtils = new APIUtils();
        mDataTransformer = new DataTransformer(this);
    }

    @Override
    public void onStart() {
        mDisposable = new CompositeDisposable();
        if(checkIsLogged()){
            mActivity.startListActivity();
        }else{
            facebookInit();
            twitterInit();
        }
    }

    private boolean checkIsLogged(){
        SharedPreferences sp = mActivity.getSharedPreferences("Who",
                Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        if(!token.equals("")){
            return true;
        }else{
            return false;
        }

    }

    private void facebookInit(){
        LoginManager.getInstance().logOut();
        facebookCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(facebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                fetchFacebookUserData(APIUtils.FACEBOOK_ID, loginResult.getAccessToken());
                Log.d("facebook", loginResult.getAccessToken().getToken());
                mActivity.showMessage("Successfully logged in Facebook");

            }

            @Override
            public void onCancel() {
                mActivity.showMessage("denied Facebook");
            }

            @Override
            public void onError(FacebookException exception) {
                exception.printStackTrace();
            }
        });
    }

    public void facebookLogin(){
        LoginManager.getInstance().logInWithReadPermissions(mActivity, Arrays.asList("public_profile"));
    }

    private void fetchFacebookUserData(final int socialWebId, AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        mDataTransformer.transform(socialWebId, object);
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,picture,link,birthday,gender,age_range,hometown");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void twitterInit(){

        TwitterConfig config = new TwitterConfig.Builder(mActivity)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(mActivity.getResources().getString(R.string.twitter_consumer_key), mActivity.getResources().getString(R.string.twitter_consumer_secret)))
                .debug(true)
                .build();
        Twitter.initialize(config);
        twitterAuthClient = new TwitterAuthClient();
    }

    public void twitterLogin(){
        twitterAuthClient.authorize(mActivity, new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.d("twitter", result.data.getAuthToken().token);
                mActivity.showMessage("Successfully logged in Twitter");
            }

            @Override
            public void failure(TwitterException exception) {
                mActivity.showMessage("error Twitter");
                exception.printStackTrace();
            }
        });
    }

    private void fetchTwitterUserData(){
        //TODO:
    }

    public void instagramInit(){
        //TODO: для каждой сети свой класс with interface
    }

    public void instagramLogin(){
        AuthenticationDialog dialog = new AuthenticationDialog(mActivity.getApplicationContext(), this);
        dialog.setCancelable(true);
        dialog.show();
    }

    public void fetchInstagramUserData(final int socialWebId, String accessToken){
        Log.d("token", accessToken);
    }

    private void sendNewUser(final int socialWebId, Userdata data){
        Disposable sendNewUser = mAPIUtils.sendNewUser(socialWebId, data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver(){
                    @Override
                    public void onComplete() {
                        mActivity.showMessage("ok API");
                        setAccount(socialWebId);
                        mActivity.startListActivity();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mActivity.showMessage("denied API");
                        Log.d("error", e.getMessage());
                        e.printStackTrace();
                    }
                });
        mDisposable.add(sendNewUser);
    }

    private void setAccount(int socialWeb){
        SharedPreferences activityPreferences = mActivity.getSharedPreferences("Who", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = activityPreferences.edit();
        editor.putInt("socialWeb", socialWeb);
        editor.commit();
    }


    @Override
    public void onStop() {
        mDisposable.clear();
    }
}
