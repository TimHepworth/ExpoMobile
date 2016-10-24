package com.expocontacts.expomobile.fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.expocontacts.expomobile.R;
import com.expocontacts.expomobile.helpers.DividerItemDecoration;
import com.expocontacts.expomobile.model.UserPost;
import com.expocontacts.expomobile.model_utils.UserPostUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UserPostsFragment extends Fragment {

    private RecyclerView mUserPostRecyclerView;
    private UserPostsFragment.UserPostAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_posts, container, false);

        mUserPostRecyclerView = (RecyclerView) view
                .findViewById(R.id.user_posts_recycler_view);
        mUserPostRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        UserPostUtils userPostUtils = UserPostUtils.get(getActivity());
        List<UserPost> userPosts = userPostUtils.getUserPosts();

        if (mAdapter == null) {
            mAdapter = new UserPostsFragment.UserPostAdapter(userPosts);
            mUserPostRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setUserPosts(userPosts);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class UserPostHolder extends RecyclerView.ViewHolder {
//    private class UserPostHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mPhotoImageView;
        private TextView mNameTextView;
        private TextView mPostDateTextView;
        private TextView mPostTextTextView;

        private UserPost mUserPost;

        public UserPostHolder(View itemView) {
            super(itemView);
//            itemView.setOnClickListener(this);

            mPhotoImageView = (ImageView) itemView.findViewById(R.id.list_item_user_post_photo);
            mNameTextView = (TextView) itemView.findViewById(R.id.list_item_user_name);
            mPostDateTextView = (TextView) itemView.findViewById(R.id.list_item_post_date);
            mPostTextTextView = (TextView) itemView.findViewById(R.id.list_item_post_text);
        }

        public void bindUserPost(UserPost userPost) {

            mUserPost = userPost;

            mNameTextView.setText(mUserPost.getForename() + " " + mUserPost.getSurname());

            DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getContext());
            Date postDate = new Date(1000L * mUserPost.getPostDate());

            SimpleDateFormat sdf=new SimpleDateFormat("hh:mm a");
            String postTime = sdf.format(postDate);

            mPostDateTextView.setText(dateFormat.format(postDate) + " " + postTime);

            if ((mUserPost.getImageURL() != null) && (mUserPost.getImageURL() != "")) {

                try {
                    Bitmap photoBitmap = BitmapFactory.decodeFile(mUserPost.getImageURL());

                    int imageWidth = photoBitmap.getWidth();
                    int imageHeight = photoBitmap.getHeight();

                    DisplayMetrics displaymetrics = new DisplayMetrics();
                    ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                    int screenHeight = displaymetrics.heightPixels;
                    int screenWidth = displaymetrics.widthPixels;

                    float scaleFactor = (float)screenWidth/(float)imageWidth;
                    int newHeight = (int)(imageHeight * scaleFactor);

                    photoBitmap = Bitmap.createScaledBitmap(photoBitmap, screenWidth, newHeight, true);

                    mPhotoImageView.setImageBitmap(photoBitmap);

                } catch (Exception e) {
                    mPhotoImageView.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }

            } else {
                mPhotoImageView.setVisibility(View.INVISIBLE);
            }

            mPostTextTextView.setText(mUserPost.getPostText());
        }

//        @Override
//        public void onClick(View v) {
//            Intent intent = UserPostPagerActivity.newIntent(getActivity(), mUserPost.getUserPostId());
//            startActivity(intent);
//        }
    }

    private class UserPostAdapter extends RecyclerView.Adapter<UserPostsFragment.UserPostHolder> {

        private List<UserPost> mUserPosts;

        public UserPostAdapter(List<UserPost> userPosts) {
            mUserPosts = userPosts;
        }

        @Override
        public UserPostsFragment.UserPostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_user_post, parent, false);
            view.setBackgroundColor(0xffffffff); // first byte is the alpha, then rgb
            return new UserPostsFragment.UserPostHolder(view);
        }

        @Override
        public void onBindViewHolder(UserPostsFragment.UserPostHolder holder, int position) {
            UserPost userPost = mUserPosts.get(position);
            holder.bindUserPost(userPost);
        }

        @Override
        public int getItemCount() {
            return mUserPosts.size();
        }

        public void setUserPosts(List<UserPost> userPosts) {
            mUserPosts = userPosts;
        }
    }
}
