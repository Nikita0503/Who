package com.softproject.who.list;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.softproject.who.BaseContract;
import com.softproject.who.R;
import com.softproject.who.model.data.Userdata;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListActivity extends AppCompatActivity implements BaseContract.BaseView {

    private int mSocialWebId;
    private ListPresenter mPresenter;
    private UsersListAdapter mAdapter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);
        mPresenter = new ListPresenter(this);
    }

    @Override
    public void onStart(){
        super.onStart();
        mPresenter.onStart();
        fetchUsers();
    }

    private void fetchUsers(){
        mAdapter = new UsersListAdapter(getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(mAdapter);
        mPresenter.getUsers();
    }

    public void addUsers(ArrayList<Userdata> users){
        mAdapter.addUsers(users);
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
