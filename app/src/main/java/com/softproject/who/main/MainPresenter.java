package com.softproject.who.main;

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
        facebookInit();
    }

    private void facebookInit(){
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if(isLoggedIn){
            Log.d("facebook", "Logged Facebook");
            mActivity.showMessage("Logged Facebook");
            authorization(APIUtils.FACEBOOK_ID, accessToken.getToken());
        }else{
            Log.d("facebook", "No");
            mActivity.showMessage("No");
        }
        facebookCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(facebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("facebook", loginResult.getAccessToken().getToken());
                mActivity.showMessage("Successfully logged in Facebook");
                authorization(APIUtils.FACEBOOK_ID, loginResult.getAccessToken().getToken());
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
                        mActivity.startListActivity(socialWeb);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(mActivity.getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                });
        mDisposable.add(authorization);
    }

    @Override
    public void onStop() {
        mDisposable.clear();
    }
}
