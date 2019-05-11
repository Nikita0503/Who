package com.softproject.who;

public interface BaseContract {

    interface BaseView{
        void showMessage(String message);
    }

    interface BasePresenter{
        void onStart();
        void onStop();
    }
}
