package com.expocontacts.expomobile.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.expocontacts.expomobile.GeneralUtils;
import com.expocontacts.expomobile.R;
import com.expocontacts.expomobile.model.Speaker;
import com.expocontacts.expomobile.model_utils.SpeakerUtils;

public class SpeakerFragment extends Fragment {

    private static final String ARG_SPEAKER_ID = "speaker_id";

    private Speaker mSpeaker;
    private TextView mSpeakerName;
    private TextView mSpeakerBio;
    private TextView mEmailAddress;
    private ImageView mSpeakerPhoto;

    public static SpeakerFragment newInstance(int speakerId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_SPEAKER_ID, speakerId);

        SpeakerFragment fragment = new SpeakerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int speakerId = (int) getArguments().getSerializable(ARG_SPEAKER_ID);
        mSpeaker = SpeakerUtils.get(getActivity()).getSpeaker(speakerId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_speaker, container, false);

        Bitmap logoBitmap = BitmapFactory.decodeFile(mSpeaker.getSpeakerPhoto());

        mSpeakerName = (TextView) v.findViewById(R.id.speaker_name);
        mSpeakerName.setText(mSpeaker.getSpeakerName());
        mSpeakerPhoto = (ImageView) v.findViewById(R.id.speaker_photo);
        mSpeakerPhoto.setImageBitmap(logoBitmap);
        mEmailAddress = (TextView) v.findViewById(R.id.email_address);
        mEmailAddress.setText(mSpeaker.getEmailAddress());
        mSpeakerBio = (TextView) v.findViewById(R.id.speaker_bio);
        mSpeakerBio.setText(GeneralUtils.stripHtml(mSpeaker.getSpeakerBio()));

        return v;
    }
}