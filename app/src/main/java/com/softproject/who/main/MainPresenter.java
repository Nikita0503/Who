package com.softproject.who.main;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;


import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.softproject.who.BaseContract;
import com.softproject.who.model.APIUtils;

import java.util.Arrays;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;


public class MainPresenter implements BaseContract.BasePresenter {

    public CallbackManager facebookCallbackManager;
    private CompositeDisposable mDisposable;
    private MainActivity mActivity;
    private APIUtils mAPIUtils;

    public MainPresenter(MainActivity activity) {
        mActivity = activity;
        mAPIUtils = new APIUtils();
    }

    @Override
    public void onStart() {
        mDisposable = new CompositeDisposable();
        init();
        facebookInit();
    }

    private void init(){
        SharedPreferences sp = mActivity.getSharedPreferences("Who",
                Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        if(!token.equals("")){
            mActivity.startListActivity();
        }

    }

    private void facebookInit(){
        facebookCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(facebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("facebook", loginResult.getAccessToken().getToken());
                mActivity.showMessage("Successfully logged in Facebook");
                authorization(APIUtils.FACEBOOK_ID, loginResult.getAccessToken().getToken());
                setAccount(loginResult.getAccessToken().getToken(), APIUtils.FACEBOOK_ID);
            }

            @Override
            public void onCancel() {
                mActivity.showMessage("denied");
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

    public void authorization(final int socialWeb, String socialWebToken){
        Disposable authorization = mAPIUtils.singIn(socialWeb, socialWebToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver(){
                    @Override
                    public void onComplete() {
                        Toast.makeText(mActivity.getApplicationContext(), "ok", Toast.LENGTH_SHORT).show();
                        mActivity.startListActivity();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(mActivity.getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                });
        mDisposable.add(authorization);
    }

    private void setAccount(String token, int socialWeb){
        SharedPreferences activityPreferences = mActivity.getSharedPreferences("Who", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = activityPreferences.edit();
        editor.putString("token", token);
        editor.putInt("socialWeb", socialWeb);
        editor.commit();
    }

    @Override
    public void onStop() {
        mDisposable.clear();
    }
}
