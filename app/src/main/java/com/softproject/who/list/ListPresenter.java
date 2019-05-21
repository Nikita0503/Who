package com.softproject.who.list;

import android.util.Log;

import com.softproject.who.BaseContract;
import com.softproject.who.model.APIUtils;
import com.softproject.who.model.data.Userdata;
import com.softproject.who.model.database.WhoDatabase;
import com.twitter.sdk.android.core.models.User;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ListPresenter implements BaseContract.BasePresenter {

    private APIUtils mAPIUtils;
    private WhoDatabase mDatabase;
    private CompositeDisposable mDisposable;
    private ListActivity mActivity;


    public ListPresenter(ListActivity activity) {
        mActivity = activity;
        mAPIUtils = new APIUtils();
        mDatabase = new WhoDatabase(activity);
    }

    @Override
    public void onStart() {
        mDisposable = new CompositeDisposable();
    }

    public void getUsers(String text){
        Disposable newUsers = mAPIUtils.getUsers(text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ArrayList<Userdata>>() {
                    @Override
                    public void onSuccess(ArrayList<Userdata> users) {
                        Log.d("Users", "Users count = " + users.size());
                        //checkIsNewUser(users);
                        mActivity.addUsers(users);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
        mDisposable.add(newUsers);
    }

    private void checkIsNewUser(final ArrayList<Userdata> users){
        Disposable checker = mDatabase.getApiIds()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ArrayList<Integer>>() {
                    @Override
                    public void onSuccess(ArrayList<Integer> list) {
                        for(int i = 0; i < users.size(); i++){
                            users.get(i).isNew = true;
                            for(int j = 0; j < list.size(); j++){

                                Log.d("DB", "true");
                                if(users.get(i).id==list.get(j)){
                                    users.get(i).isNew = false;
                                    break;
                                }
                            }
                        }
                        mActivity.addUsers(users);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
        mDisposable.add(checker);
    }

    @Override
    public void onStop() {
        mDisposable.clear();
    }
}
