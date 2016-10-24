package com.expocontacts.expomobile;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.expocontacts.expomobile.fragments.SpeakerFragment;
import com.expocontacts.expomobile.model.Speaker;
import com.expocontacts.expomobile.model_utils.SpeakerUtils;

import java.util.List;

public class SpeakerPagerActivity extends AppCompatActivity {

    private static final String EXTRA_SPEAKER_ID = "com.expocontacts.expomobile.speaker_id";

    private ViewPager mViewPager;
    private List<Speaker> mSpeakers;

    public static Intent newIntent(Context packageContext, int speakerId) {
        Intent intent = new Intent(packageContext, SpeakerPagerActivity.class);
        intent.putExtra(EXTRA_SPEAKER_ID, speakerId);
        return intent;
    }

    @Override
    protected  void  onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaker_pager);

        int speakerId = (int) getIntent()
                .getSerializableExtra(EXTRA_SPEAKER_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_speaker_pager_view_pager);

        mSpeakers = SpeakerUtils.get(this).getSpeakers();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Speaker speaker = mSpeakers.get(position);
                return SpeakerFragment.newInstance(speaker.getSpeakerId());
            }

            @Override
            public int getCount() {
                return mSpeakers.size();
            }
        });

        for (int i=0; i < mSpeakers.size(); i++) {
            if (mSpeakers.get(i).getSpeakerId().equals(speakerId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}