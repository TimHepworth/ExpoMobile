package com.expocontacts.expomobile.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.expocontacts.expomobile.SpeakerPagerActivity;
import com.expocontacts.expomobile.R;
import com.expocontacts.expomobile.helpers.DividerItemDecoration;
import com.expocontacts.expomobile.model.Speaker;
import com.expocontacts.expomobile.model_utils.SpeakerUtils;

import java.util.List;

public class SpeakerListFragment extends Fragment {

    private RecyclerView mSpeakerRecyclerView;
    private SpeakerListFragment.SpeakerAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_speaker_list, container, false);

        mSpeakerRecyclerView = (RecyclerView) view
                .findViewById(R.id.speaker_list_recycler_view);
        mSpeakerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mSpeakerRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        SpeakerUtils speakerUtils = SpeakerUtils.get(getActivity());
        List<Speaker> Speakers = speakerUtils.getSpeakers();

        if (mAdapter == null) {
            mAdapter = new SpeakerListFragment.SpeakerAdapter(Speakers);
            mSpeakerRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setSpeakers(Speakers);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class SpeakerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mPhotoImageView;
        private TextView mNameTextView;
        private TextView mJobTitleTextView;
        private TextView mCompanyNameTextView;

        private Speaker mSpeaker;

        public SpeakerHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mPhotoImageView = (ImageView) itemView.findViewById(R.id.list_item_speaker_photo);
            mNameTextView = (TextView) itemView.findViewById(R.id.list_item_speaker_name);
            mCompanyNameTextView = (TextView) itemView.findViewById(R.id.list_item_speaker_company);
        }

        public void bindSpeaker(Speaker speaker) {
            mSpeaker = speaker;

            Bitmap logoBitmap = BitmapFactory.decodeFile(mSpeaker.getSpeakerPhoto());

            mPhotoImageView.setImageBitmap(logoBitmap);
            mNameTextView.setText(mSpeaker.getSpeakerName());

            if (mSpeaker.getJobTitle().equals("")) {
                mCompanyNameTextView.setText(mSpeaker.getSpeakerCompany());
            } else if (mSpeaker.getSpeakerCompany().equals("")) {
                mCompanyNameTextView.setText(mSpeaker.getJobTitle());
            } else {
                mCompanyNameTextView.setText(mSpeaker.getJobTitle() + " | " + mSpeaker.getSpeakerCompany());
            }

        }

        @Override
        public void onClick(View v) {
            Intent intent = SpeakerPagerActivity.newIntent(getActivity(), mSpeaker.getSpeakerId());
            startActivity(intent);
        }
    }

    private class SpeakerAdapter extends RecyclerView.Adapter<SpeakerListFragment.SpeakerHolder> {

        private List<Speaker> mSpeakers;

        public SpeakerAdapter(List<Speaker> speakers) {
            mSpeakers = speakers;
        }

        @Override
        public SpeakerListFragment.SpeakerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_speaker, parent, false);
            view.setBackgroundColor(0xffffffff); // first byte is the alpha, then rgb
            return new SpeakerListFragment.SpeakerHolder(view);
        }

        @Override
        public void onBindViewHolder(SpeakerListFragment.SpeakerHolder holder, int position) {
            Speaker speaker = mSpeakers.get(position);
            holder.bindSpeaker(speaker);
        }

        @Override
        public int getItemCount() {
            return mSpeakers.size();
        }

        public void setSpeakers(List<Speaker> speakers) {
            mSpeakers = speakers;
        }
    }

}
