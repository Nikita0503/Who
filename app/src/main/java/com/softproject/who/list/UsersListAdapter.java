package com.softproject.who.list;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.softproject.who.R;
import com.softproject.who.model.APIUtils;
import com.softproject.who.model.data.Userdata;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class UsersListAdapter extends RecyclerView.Adapter {

    private static final int USER_ROW_TYPE = 0;
    private static final int ME_ROW_TYPE = 1;
    private static final int ADVERTISEMENT_ROW_TYPE = 2;


    private String mSocialId;
    private ListActivity mActivity;
    private ArrayList<Userdata> mUsers;
    private AdRequest mAdRequest;

    public UsersListAdapter(ListActivity activity, String socialId) {
        mAdRequest = new AdRequest.Builder().build();
        mUsers = new ArrayList<Userdata>();
        mActivity = activity;
        mSocialId = socialId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        if (viewType == ADVERTISEMENT_ROW_TYPE) {
            view = inflater.inflate(R.layout.advertisement, viewGroup, false);
            return new UsersListAdapter.AdvertisementViewHolder(view);
        } else if (viewType == ME_ROW_TYPE){
            view = inflater.inflate(R.layout.list_item_me, viewGroup, false);
            return new UsersListAdapter.MeViewHolder(view);
        }
        else{
            view = inflater.inflate(R.layout.list_item, viewGroup, false);
            return new UsersListAdapter.UserViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int i) {
        if(i%10==0 && i!=0){
            return ADVERTISEMENT_ROW_TYPE;
        }else{
            if(mUsers.get(i).socialId.equals(mSocialId)){
                return ME_ROW_TYPE;
            }else {
                return USER_ROW_TYPE;
            }
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        if(i%10==0 && i!=0){
            AdvertisementViewHolder holder = (AdvertisementViewHolder) viewHolder;
            holder.adView.loadAd(mAdRequest);
        }else {
            if(viewHolder instanceof  UserViewHolder) {
                final UserViewHolder holder = (UserViewHolder) viewHolder;
                //holder.textViewNumber.setText(String.valueOf(mUsers.get(i).id));
                //holder.textViewNumber.setText(String.valueOf(i+1));
                holder.textViewNumber.setText(String.valueOf(mUsers.get(i).id));
                holder.textViewUsername.setText(mUsers.get(i).name);

                if (mUsers.get(i).isNew) {
                    holder.textViewIsNew.setText(mActivity.getResources().getString(R.string.new_user));
                }
                int difference = getDifferenceTime(mUsers.get(i).authDate);
                if (difference == 0) {
                    holder.textViewDaysAgo.setText(mActivity.getResources().getString(R.string.today));
                } else if (difference == 1) {
                    holder.textViewDaysAgo.setText(difference + " " + mActivity.getResources().getString(R.string.day_ago));
                } else if (difference >= 2 && difference <= 4) {
                    holder.textViewDaysAgo.setText(difference + " " + mActivity.getResources().getString(R.string.days_ago1));
                } else {
                    holder.textViewDaysAgo.setText(difference + " " + mActivity.getResources().getString(R.string.days_ago2));
                }

                switch (mUsers.get(i).social) {
                    case APIUtils.FACEBOOK_ID:
                        Glide
                                .with(mActivity.getApplicationContext())
                                .load(R.drawable.ic_facebook)
                                .into(holder.imageViewSocialWeb);
                        break;
                    case APIUtils.TWITTER_ID:
                        Glide
                                .with(mActivity.getApplicationContext())
                                .load(R.drawable.ic_twitter)
                                .into(holder.imageViewSocialWeb);
                        break;
                    case APIUtils.INSTAGRAM_ID:
                        Glide
                                .with(mActivity.getApplicationContext())
                                .load(R.drawable.ic_instagram)
                                .into(holder.imageViewSocialWeb);
                        break;
                    case APIUtils.VK_ID:
                        Glide
                                .with(mActivity.getApplicationContext())
                                .load(R.drawable.ic_vkontakte)
                                .into(holder.imageViewSocialWeb);
                        break;
                }

                Glide
                        .with(mActivity.getApplicationContext())
                        .load(mUsers.get(i).photo)
                        .into(holder.imageViewAvatar);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!mUsers.get(i).isHidden) {
                            holder.textViewDescrption.setVisibility(View.VISIBLE);
                            String description = "";
                            if (mUsers.get(i).location != null) {
                                if (!mUsers.get(i).location.equals("")) {
                                    description += "location: " + mUsers.get(i).location + "\n";
                                }
                            }
                            if (mUsers.get(i).age != null) {
                                description += "age: " + mUsers.get(i).age + "\n";
                            } else if (mUsers.get(i).birthday != null) {
                                description += "birthday: " + mUsers.get(i).birthday + "\n";
                            }
                            if (mUsers.get(i).gender != null) {
                                description += mUsers.get(i).gender + "\n";
                            }
                            holder.textViewDescrption.setText(description);
                            mUsers.get(i).isHidden = true;
                            if (description.equals("")) {
                                holder.textViewDescrption.setVisibility(View.GONE);
                            }
                        } else {
                            holder.textViewDescrption.setVisibility(View.GONE);
                            mUsers.get(i).isHidden = false;
                        }
                    }
                });
                holder.imageViewSocialWeb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Intent facebookIntent = getOpenFacebookIntent(i);
                        //mActivity.startActivity(facebookIntent);
                        if (mUsers.get(i).social != APIUtils.FACEBOOK_ID) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUsers.get(i).url));
                            mActivity.startActivity(browserIntent);
                        }else{
                            Intent intent = new Intent(getOpenFacebookIntent(i));
                            mActivity.startActivity(intent);
                        }
                    }
                });
            } else{
                final MeViewHolder holder = (MeViewHolder) viewHolder;
                holder.textViewNumber.setText(String.valueOf(mUsers.get(i).id));
                holder.textViewUsername.setText(mUsers.get(i).name);

                if (mUsers.get(i).isNew) {
                    holder.textViewIsNew.setText(mActivity.getResources().getString(R.string.new_user));
                }
                int difference = getDifferenceTime(mUsers.get(i).authDate);
                if (difference == 0) {
                    holder.textViewDaysAgo.setText(mActivity.getResources().getString(R.string.today));
                } else if (difference == 1) {
                    holder.textViewDaysAgo.setText(difference + " " + mActivity.getResources().getString(R.string.day_ago));
                } else if (difference >= 2 && difference <= 4) {
                    holder.textViewDaysAgo.setText(difference + " " + mActivity.getResources().getString(R.string.days_ago1));
                } else {
                    holder.textViewDaysAgo.setText(difference + " " + mActivity.getResources().getString(R.string.days_ago2));
                }

                switch (mUsers.get(i).social) {
                    case APIUtils.FACEBOOK_ID:
                        Glide
                                .with(mActivity.getApplicationContext())
                                .load(R.drawable.ic_facebook)
                                .into(holder.imageViewSocialWeb);
                        break;
                    case APIUtils.TWITTER_ID:
                        Glide
                                .with(mActivity.getApplicationContext())
                                .load(R.drawable.ic_twitter)
                                .into(holder.imageViewSocialWeb);
                        break;
                    case APIUtils.INSTAGRAM_ID:
                        Glide
                                .with(mActivity.getApplicationContext())
                                .load(R.drawable.ic_instagram)
                                .into(holder.imageViewSocialWeb);
                        break;
                    case APIUtils.VK_ID:
                        Glide
                                .with(mActivity.getApplicationContext())
                                .load(R.drawable.ic_vkontakte)
                                .into(holder.imageViewSocialWeb);
                        break;
                }

                Glide
                        .with(mActivity.getApplicationContext())
                        .load(mUsers.get(i).photo)
                        .into(holder.imageViewAvatar);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!mUsers.get(i).isHidden) {
                            holder.textViewDescrption.setVisibility(View.VISIBLE);
                            String description = "";
                            if (mUsers.get(i).location != null) {
                                if (!mUsers.get(i).location.equals("")) {
                                    description += "location: " + mUsers.get(i).location + "\n";
                                }
                            }
                            if (mUsers.get(i).age != null) {
                                description += "age: " + mUsers.get(i).age + "\n";
                            } else if (mUsers.get(i).birthday != null) {
                                description += "birthday: " + mUsers.get(i).birthday + "\n";
                            }
                            if (mUsers.get(i).gender != null) {
                                description += mUsers.get(i).gender + "\n";
                            }
                            holder.textViewDescrption.setText(description);
                            mUsers.get(i).isHidden = true;
                            if (description.equals("")) {
                                holder.textViewDescrption.setVisibility(View.GONE);
                            }
                        } else {
                            holder.textViewDescrption.setVisibility(View.GONE);
                            mUsers.get(i).isHidden = false;
                        }
                    }
                });
                holder.imageViewSocialWeb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Intent facebookIntent = getOpenFacebookIntent(i);
                        if (mUsers.get(i).social != APIUtils.FACEBOOK_ID) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUsers.get(i).url));
                            mActivity.startActivity(browserIntent);
                        }else{
                            Intent intent = new Intent(getOpenFacebookIntent(i));
                            mActivity.startActivity(intent);
                        }
                    }
                });
            }
        }
    }



    public Intent getOpenFacebookIntent(int i) {
        try {
            mActivity.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/" + mUsers.get(i).socialId));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + mUsers.get(i).name));
        }
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public void addUsers(ArrayList<Userdata> users){
        mUsers.clear();
        mUsers.addAll(users);
        notifyDataSetChanged();
    }

    private int getDifferenceTime(String date) {
        Date nowDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date itemDate = null;
        try {
            itemDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long milliseconds = getDateDiff(itemDate, nowDate,TimeUnit.MILLISECONDS);
        int days = (int) (milliseconds / (24 * 60 * 60 * 1000));
        return days;
    }

    private long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

    public static class AdvertisementViewHolder extends RecyclerView.ViewHolder{

        AdView adView;

        public AdvertisementViewHolder(View itemView) {
            super(itemView);
            adView = (AdView) itemView.findViewById(R.id.adView);
        }
    }


    public static class UserViewHolder extends RecyclerView.ViewHolder implements Joap{

        TextView textViewNumber;
        TextView textViewUsername;
        TextView textViewDescrption;
        TextView textViewDaysAgo;
        TextView textViewIsNew;
        ImageView imageViewAvatar;
        ImageView imageViewSocialWeb;
        ImageView imageViewBackground;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNumber = (TextView) itemView.findViewById(R.id.textViewNumber);
            textViewUsername = (TextView) itemView.findViewById(R.id.textViewUsername);
            textViewDescrption = (TextView) itemView.findViewById(R.id.textViewDescription);
            textViewDaysAgo = (TextView) itemView.findViewById(R.id.textViewDaysAgo);
            textViewIsNew = (TextView) itemView.findViewById(R.id.textViewIsNew);
            imageViewAvatar = (ImageView) itemView.findViewById(R.id.imageViewAvatar);
            imageViewSocialWeb = (ImageView) itemView.findViewById(R.id.imageViewSocialWeb);
            imageViewBackground = (ImageView) itemView.findViewById(R.id.imageViewbackground);
        }
    }

    public static class MeViewHolder extends RecyclerView.ViewHolder implements Joap{

        TextView textViewNumber;
        TextView textViewUsername;
        TextView textViewDescrption;
        TextView textViewDaysAgo;
        TextView textViewIsNew;
        ImageView imageViewAvatar;
        ImageView imageViewSocialWeb;
        ImageView imageViewBackground;

        public MeViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNumber = (TextView) itemView.findViewById(R.id.textViewNumber);
            textViewUsername = (TextView) itemView.findViewById(R.id.textViewUsername);
            textViewDescrption = (TextView) itemView.findViewById(R.id.textViewDescription);
            textViewDaysAgo = (TextView) itemView.findViewById(R.id.textViewDaysAgo);
            textViewIsNew = (TextView) itemView.findViewById(R.id.textViewIsNew);
            imageViewAvatar = (ImageView) itemView.findViewById(R.id.imageViewAvatar);
            imageViewSocialWeb = (ImageView) itemView.findViewById(R.id.imageViewSocialWeb);
            imageViewBackground = (ImageView) itemView.findViewById(R.id.imageViewbackground);
        }
    }

    interface Joap{

    }

}
