package com.expocontacts.expomobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.expocontacts.expomobile.fragments.ExhibitorFragment;
import com.expocontacts.expomobile.model.Exhibitor;
import com.expocontacts.expomobile.model_utils.ExhibitorUtils;

import java.util.List;

public class ExhibitorPagerActivity extends AppCompatActivity {

    private static final String EXTRA_EXHIBITOR_ID = "com.expocontacts.expomobile.exhibitor_id";

    private ViewPager mViewPager;
    private List<Exhibitor> mExhibitors;

    public static Intent newIntent(Context packageContext, int exhibitorId) {
        Intent intent = new Intent(packageContext, ExhibitorPagerActivity.class);
        intent.putExtra(EXTRA_EXHIBITOR_ID, exhibitorId);
        return intent;
    }

    @Override
    protected  void  onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibitor_pager);

        int exhibitorId = (int) getIntent()
                .getSerializableExtra(EXTRA_EXHIBITOR_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_exhibitor_pager_view_pager);

        mExhibitors = ExhibitorUtils.get(this).getExhibitors();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Exhibitor exhibitor = mExhibitors.get(position);
                return ExhibitorFragment.newInstance(exhibitor.getExhibitorId());
            }

            @Override
            public int getCount() {
                return mExhibitors.size();
            }
        });

        for (int i=0; i < mExhibitors.size(); i++) {
            if (mExhibitors.get(i).getExhibitorId().equals(exhibitorId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
