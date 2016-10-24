package com.expocontacts.expomobile;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.expocontacts.expomobile.fragments.NewsArticleFragment;
import com.expocontacts.expomobile.model.NewsArticle;
import com.expocontacts.expomobile.model_utils.NewsArticleUtils;

import java.util.List;

public class NewsPagerActivity extends AppCompatActivity {

    private static final String EXTRA_NEWS_ID =
            "com.expocontacts.expomobile.news_id";

    private ViewPager mViewPager;
    private List<NewsArticle> mNewsArticles;

    public static Intent newIntent(Context packageContext, int newsId) {
        Intent intent = new Intent(packageContext, NewsPagerActivity.class);
        intent.putExtra(EXTRA_NEWS_ID, newsId);
        return intent;
    }

    @Override
    protected  void  onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_pager);

        int newsArticleId = (int) getIntent()
                .getSerializableExtra(EXTRA_NEWS_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_news_pager_view_pager);

        mNewsArticles = NewsArticleUtils.get(this).getNewsArticles();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                NewsArticle newsArticle = mNewsArticles.get(position);
                return NewsArticleFragment.newInstance(newsArticle.getNewsId());
            }

            @Override
            public int getCount() {
                return mNewsArticles.size();
            }
        });

        for (int i=0; i < mNewsArticles.size(); i++) {
            if (mNewsArticles.get(i).getNewsId().equals(newsArticleId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}