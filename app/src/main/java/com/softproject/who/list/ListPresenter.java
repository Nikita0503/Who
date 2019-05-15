package com.softproject.who.list;

import android.util.Log;

import com.softproject.who.BaseContract;
import com.softproject.who.model.APIUtils;
import com.softproject.who.model.data.Userdata;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ListPresenter implements BaseContract.BasePresenter {

    private APIUtils mAPIUtils;
    private CompositeDisposable mDisposable;
    private ListActivity mActivity;


    public ListPresenter(ListActivity activity) {
        mActivity = activity;
        mAPIUtils = new APIUtils();
    }

    @Override
    public void onStart() {
        mDisposable = new CompositeDisposable();
    }

    public void getUsers(){
        Disposable newUsers = mAPIUtils.getUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ArrayList<Userdata>>() {
                    @Override
                    public void onSuccess(ArrayList<Userdata> users) {
                        Log.d("Users", "Users count = " + users.size());
                        mActivity.addUsers(users);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
        mDisposable.add(newUsers);
    }

    @Override
    public void onStop() {
        mDisposable.clear();
    }
}
