package com.softproject.who.list;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        final UserViewHolder holder = (UserViewHolder) viewHolder;
        //holder.textViewNumber.setText(String.valueOf(mUsers.get(i).id));
        holder.textViewNumber.setText(String.valueOf(i+1));
        holder.textViewUsername.setText(mUsers.get(i).name);
        switch (mUsers.get(i).social){
            case APIUtils.FACEBOOK_ID:
                Glide
                        .with(mContext)
                        .load(R.drawable.ic_facebook)
                        .into(holder.imageViewSocialWeb);
                break;
        }

        Glide
                .with(mContext)
                .load(mUsers.get(i).photo)
                .into(holder.imageViewAvatar);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mUsers.get(i).isHidden){
                    if(mUsers.get(i).location!=null){
                        holder.textViewLocation.setVisibility(View.VISIBLE);
                        holder.textViewLocation.setText(mUsers.get(i).location);
                    }
                    if(mUsers.get(i).authDate!=null){
                        holder.textViewAge.setVisibility(View.VISIBLE);
                        holder.textViewAge.setText(String.valueOf(mUsers.get(i).age));
                    }
                    if(mUsers.get(i).gender!=null){
                        holder.textViewGender.setVisibility(View.VISIBLE);
                        holder.textViewGender.setText(String.valueOf(mUsers.get(i).gender));
                    }
                    mUsers.get(i).isHidden = true;
                }else{
                    holder.textViewLocation.setVisibility(View.GONE);
                    holder.textViewAge.setVisibility(View.GONE);
                    holder.textViewGender.setVisibility(View.GONE);
                    mUsers.get(i).isHidden = false;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public void addUsers(ArrayList<Userdata> users){
        mUsers.addAll(users);
        notifyDataSetChanged();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{

        TextView textViewNumber;
        TextView textViewUsername;
        TextView textViewLocation;
        TextView textViewAge;
        TextView textViewGender;
        ImageView imageViewAvatar;
        ImageView imageViewSocialWeb;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNumber = (TextView) itemView.findViewById(R.id.textViewNumber);
            textViewUsername = (TextView) itemView.findViewById(R.id.textViewUsername);
            textViewLocation = (TextView) itemView.findViewById(R.id.textViewLocation);
            textViewAge = (TextView) itemView.findViewById(R.id.textViewAge);
            textViewGender = (TextView) itemView.findViewById(R.id.textViewGender);
            imageViewAvatar = (ImageView) itemView.findViewById(R.id.imageViewAvatar);
            imageViewSocialWeb = (ImageView) itemView.findViewById(R.id.imageViewSocialWeb);
        }
    }



}
