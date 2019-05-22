package com.softproject.who.list;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.softproject.who.BaseContract;
import com.softproject.who.R;
import com.softproject.who.model.data.Userdata;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListActivity extends AppCompatActivity implements BaseContract.BaseView {

    private ListPresenter mPresenter;
    private UsersListAdapter mAdapter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String socialId = intent.getStringExtra("socialId");
        mPresenter = new ListPresenter(this, socialId);
        if(mToolbar != null){
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        final EditText editTextSearch = (EditText) mToolbar.findViewById(R.id.search_edit_text);
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fetchUsers(editTextSearch.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Do nothing
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        mPresenter.onStart();
        fetchUsers();
    }

    private void fetchUsers(){
        mAdapter = new UsersListAdapter(this, mPresenter.getSocialId());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(mAdapter);
        mPresenter.getUsers("");
    }

    private void fetchUsers(String text){
        mAdapter = new UsersListAdapter(this, mPresenter.getSocialId());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(mAdapter);
        mPresenter.getUsers(text);
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
