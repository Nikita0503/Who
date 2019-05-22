package com.softproject.who.list;

import android.util.Log;
import android.widget.Toast;

import com.softproject.who.BaseContract;
import com.softproject.who.model.APIUtils;
import com.softproject.who.model.data.Userdata;
import com.softproject.who.model.database.WhoDatabase;
import com.twitter.sdk.android.core.models.User;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ListPresenter implements BaseContract.BasePresenter {

    private String mSocialId;
    private APIUtils mAPIUtils;
    private WhoDatabase mDatabase;
    private CompositeDisposable mDisposable;
    private ListActivity mActivity;


    public ListPresenter(ListActivity activity, String socialId) {
        mActivity = activity;
        mAPIUtils = new APIUtils();
        mDatabase = new WhoDatabase(activity);
        mSocialId = socialId;
    }

    @Override
    public void onStart() {
        mDisposable = new CompositeDisposable();
    }

    public String getSocialId(){
        return mSocialId;
    }

    public void getUsers(String text){
        Disposable newUsers = mAPIUtils.getUsers(text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ArrayList<Userdata>>() {
                    @Override
                    public void onSuccess(ArrayList<Userdata> users) {
                        Log.d("Users", "Users count = " + users.size());
                        checkIsNewUser(users);
                        //mActivity.addUsers(users);
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
                        int counter = 0;
                        for(int i = counter; i < users.size(); i++){
                            if(users.get(i).isNew){
                                users.add(0, users.get(i));
                                users.remove(i+1);
                            }
                        }
                        mActivity.addUsers(users);
                        addNewUsers(users, list);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
        mDisposable.add(checker);
    }

    private void addNewUsers(ArrayList<Userdata> users, ArrayList<Integer> list){
        Disposable newUsers = mDatabase.addNewApiId(users, list)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Toast.makeText(mActivity.getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
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
