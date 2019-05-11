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

import java.util.Arrays;

import static com.softproject.who.model.APIUtils.FACEBOOK_ID;

public class MainPresenter implements BaseContract.BasePresenter {



    private MainActivity mActivity;
    public CallbackManager facebookCallbackManager;

    public MainPresenter(MainActivity activity) {
        mActivity = activity;
    }

    @Override
    public void onStart() {
        facebookInit();
    }

    private void facebookInit(){
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if(isLoggedIn){
            Log.d("facebook", "Logged Facebook");
            mActivity.showMessage("Logged Facebook");
            mActivity.startListActivity(FACEBOOK_ID);
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
                mActivity.startListActivity(FACEBOOK_ID);
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

    @Override
    public void onStop() {

    }
}
