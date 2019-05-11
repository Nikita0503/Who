package com.softproject.who.list;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.softproject.who.BaseContract;
import com.softproject.who.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListActivity extends AppCompatActivity implements BaseContract.BaseView {

    private int mSocialWebId;
    private ListPresenter mPresenter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);
        mPresenter = new ListPresenter(this);
        Intent intent = getIntent();
        mSocialWebId = intent.getIntExtra("socialWebId", -1);
        switch (mSocialWebId){
            case 0:
                Toast.makeText(getApplicationContext(), "Facebook", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        mPresenter.onStart();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop(){
        super.onStop();
        mPresenter.onStop();
    }
}
