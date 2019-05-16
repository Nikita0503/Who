package com.softproject.who.list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.softproject.who.R;
import com.softproject.who.model.APIUtils;
import com.softproject.who.model.data.Userdata;

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
            case APIUtils.TWITTER_ID:
                Glide
                        .with(mContext)
                        .load(R.drawable.ic_twitter)
                        .into(holder.imageViewSocialWeb);
                break;
            case APIUtils.INSTAGRAM_ID:
                Glide
                        .with(mContext)
                        .load(R.drawable.ic_instagram)
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
                    holder.textViewDescrption.setVisibility(View.VISIBLE);
                    String description = "";
                    if(mUsers.get(i).location!=null){
                        if(!mUsers.get(i).location.equals("")) {
                            description += "location: " + mUsers.get(i).location + "\n";
                        }
                    }
                    if(mUsers.get(i).age!=null){
                        description += "age: " + mUsers.get(i).age + "\n";
                    }
                    if(mUsers.get(i).gender!=null){
                        description += mUsers.get(i).gender + "\n";
                    }
                    holder.textViewDescrption.setText(description);
                    mUsers.get(i).isHidden = true;
                    if(description.equals("")){
                        holder.textViewDescrption.setVisibility(View.GONE);
                    }
                }else{
                    holder.textViewDescrption.setVisibility(View.GONE);
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
        TextView textViewDescrption;
        ImageView imageViewAvatar;
        ImageView imageViewSocialWeb;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNumber = (TextView) itemView.findViewById(R.id.textViewNumber);
            textViewUsername = (TextView) itemView.findViewById(R.id.textViewUsername);
            textViewDescrption = (TextView) itemView.findViewById(R.id.textViewDescription);
            imageViewAvatar = (ImageView) itemView.findViewById(R.id.imageViewAvatar);
            imageViewSocialWeb = (ImageView) itemView.findViewById(R.id.imageViewSocialWeb);
        }
    }



}
