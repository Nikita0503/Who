package com.softproject.who.list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.softproject.who.R;
import com.softproject.who.model.data.Userdata;
import com.softproject.who.model.APIUtils;

import java.util.ArrayList;


public class UsersListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<Userdata> mUsers;

    public UsersListAdapter(Context context) {
        mUsers = new ArrayList<Userdata>();
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item, viewGroup, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        UserViewHolder holder = (UserViewHolder) viewHolder;
        holder.textViewNumber.setText(String.valueOf(mUsers.get(i).number));
        holder.textViewUsername.setText(mUsers.get(i).username);
        switch (mUsers.get(i).socialWeb){
            case APIUtils.FACEBOOK_ID:
                Glide
                        .with(mContext)
                        .load(R.drawable.ic_facebook)
                        .into(holder.imageViewSocialWeb);
                break;
        }
        Glide
                .with(mContext)
                .load(mUsers.get(i).imageURL)
                .into(holder.imageViewSocialWeb);

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{

        TextView textViewNumber;
        TextView textViewUsername;
        TextView textViewLocation;
        TextView textViewAge;
        ImageView imageViewAvatar;
        ImageView imageViewSocialWeb;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNumber = (TextView) itemView.findViewById(R.id.textViewNumber);
            textViewUsername = (TextView) itemView.findViewById(R.id.textViewUsername);
            textViewLocation = (TextView) itemView.findViewById(R.id.textViewLocation);
            textViewAge = (TextView) itemView.findViewById(R.id.textViewAge);
            imageViewAvatar = (ImageView) itemView.findViewById(R.id.imageViewAvatar);
            imageViewSocialWeb = (ImageView) itemView.findViewById(R.id.imageViewSocialWeb);
        }
    }
}
