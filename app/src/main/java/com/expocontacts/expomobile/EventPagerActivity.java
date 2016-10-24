package com.expocontacts.expomobile;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.expocontacts.expomobile.fragments.FairEventFragment;
import com.expocontacts.expomobile.model.FairEvent;
import com.expocontacts.expomobile.model_utils.EventUtils;

import java.util.List;

public class EventPagerActivity extends AppCompatActivity {

    private static final String EXTRA_EVENT_ID = "com.expocontacts.expofair.fair_event_id";

    private ViewPager mViewPager;
    private List<FairEvent> mFairEvents;

    public static Intent newIntent(Context packageContext, int fairEventId) {
        Intent intent = new Intent(packageContext, EventPagerActivity.class);
        intent.putExtra(EXTRA_EVENT_ID, fairEventId);
        return intent;
    }

    @Override
    protected  void  onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_pager);

        int fairEventId = (int) getIntent()
                .getSerializableExtra(EXTRA_EVENT_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_event_pager_view_pager);

        String eventDate;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                eventDate= null;
            } else {
                eventDate= extras.getString("EventDate");
            }
        } else {
            eventDate= (String) savedInstanceState.getSerializable("EventDate");
        }

        mFairEvents = EventUtils.get(this).getEvents(eventDate);
        FragmentManager fragmentManager = getSupportFragmentManager();

        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                FairEvent fairEvent = mFairEvents.get(position);
                return FairEventFragment.newInstance(fairEvent.getFairEventId());
            }

            @Override
            public int getCount() {
                return mFairEvents.size();
            }
        });

        for (int i=0; i < mFairEvents.size(); i++) {
            if (mFairEvents.get(i).getFairEventId().equals(fairEventId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}