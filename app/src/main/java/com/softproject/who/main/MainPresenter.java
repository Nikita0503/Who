package com.softproject.who.main;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;


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
import com.softproject.who.model.APIInstagramUtils;
import com.softproject.who.model.APIUtils;
import com.softproject.who.model.APIVkUtils;
import com.softproject.who.model.DataTransformer;
import com.softproject.who.model.data.Userdata;
import com.softproject.who.model.data.instagram.InstagramData;
import com.softproject.who.model.data.instagram.InstagramUserdata;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.auth.VKAccessToken;
import com.vk.api.sdk.auth.VKAuthCallback;
import com.vk.api.sdk.auth.VKScope;
import com.vk.api.sdk.requests.VKRequest;
import com.vk.api.sdk.utils.VKUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;


public class MainPresenter implements BaseContract.BasePresenter {

    public CallbackManager facebookCallbackManager;
    public TwitterAuthClient twitterAuthClient;
    public VKAuthCallback vkAuthCallback;
    private CompositeDisposable mDisposable;
    private MainActivity mActivity;
    private APIUtils mAPIUtils;
    private APIInstagramUtils mAPIInstagramUtils;
    private APIVkUtils mAPIVkUtils;
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
            instagramInit();
            vkInit();
        }
    }

    private boolean checkIsLogged(){
        SharedPreferences sp = mActivity.getSharedPreferences("Who",
                Context.MODE_PRIVATE);
        int socialWeb = sp.getInt("socialWeb", 0);
        if(socialWeb != 0){
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
                        Userdata userdata = mDataTransformer.facebookTransform(object);
                        sendNewUser(userdata);
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
                fetchTwitterUserData(result.data);
                mActivity.showMessage("Successfully logged in Twitter");
            }

            @Override
            public void failure(TwitterException exception) {
                mActivity.showMessage("denied Twitter");
                exception.printStackTrace();
            }
        });
    }

    private void fetchTwitterUserData(TwitterSession twitterSession){
        TwitterCore.getInstance().getApiClient().getAccountService().verifyCredentials(true, false, true).enqueue(new Callback<User>() {
            @Override
            public void success(Result<User> userResult) {
                try {
                    User user = userResult.data;
                    Userdata userdata = new Userdata(APIUtils.TWITTER_ID);
                    userdata.name = user.name;
                    userdata.photo = user.profileImageUrl;
                    userdata.location = user.location;
                    userdata.socialId = user.idStr;
                    userdata.url = "https://twitter.com/" + user.screenName;
                    sendNewUser(userdata);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void failure(TwitterException e) {
                e.printStackTrace();
            }
        });
    }

    public void instagramInit(){
        mAPIInstagramUtils = new APIInstagramUtils();
    }

    public void instagramLogin(){
        AuthenticationDialog dialog = new AuthenticationDialog(mActivity, this);
        dialog.setCancelable(true);
        dialog.show();
    }

    public void fetchInstagramUserData(String accessToken){
        Log.d("INSTAGRAM", accessToken);
        Disposable instagramUserdata = mAPIInstagramUtils.getInstagramUserinfo(accessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<InstagramData>() {
                    @Override
                    public void onSuccess(InstagramData value) {
                        InstagramUserdata instagramUserdata= value.data;
                        Userdata userdata = mDataTransformer.instagramTransform(instagramUserdata);
                        sendNewUser(userdata);

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
        mDisposable.add(instagramUserdata);
    }

    private void vkInit(){
        VK.initialize(mActivity);
        mAPIVkUtils = new APIVkUtils();
    }

    public void vkLogin(){
        vkAuthCallback = new VKAuthCallback() {
            @Override
            public void onLogin(@NotNull VKAccessToken vkAccessToken) {
                Log.d("VK", vkAccessToken.getAccessToken());
                fetchVkUserdata(String.valueOf(vkAccessToken.getUserId()), vkAccessToken.getAccessToken());
            }

            @Override
            public void onLoginFailed(int i) {
                Log.d("VK", "fail: " + i);
            }
        };
        VK.login(mActivity);
    }

    private void fetchVkUserdata(String userId, String token){
        mAPIVkUtils.getVkUserinfo(userId, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject vkUserdata) {
                        try {
                            Userdata userdata = mDataTransformer.vkTransform(vkUserdata);
                            Log.d("VK", vkUserdata.toString(4));
                            sendNewUser(userdata);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    public void sendNewUser(final Userdata userdata){
        //setAccount(userdata.social);
        Disposable sendNewUser = mAPIUtils.sendNewUser(userdata)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver(){
                    @Override
                    public void onComplete() {
                        mActivity.showMessage("ok API");

                        mActivity.startListActivity();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mActivity.showMessage("denied API");
                        mActivity.startListActivity();
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
