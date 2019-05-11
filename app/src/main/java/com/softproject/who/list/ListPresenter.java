package com.softproject.who.list;

import android.widget.Toast;

import com.softproject.who.BaseContract;
import com.softproject.who.model.APIUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
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



    @Override
    public void onStop() {
        mDisposable.clear();
    }
}
