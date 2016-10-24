package com.expocontacts.expomobile.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.expocontacts.expomobile.GeneralUtils;
import com.expocontacts.expomobile.R;
import com.expocontacts.expomobile.model.NewsArticle;
import com.expocontacts.expomobile.model_utils.NewsArticleUtils;

public class NewsArticleFragment extends Fragment {

    private static final String ARG_NEWS_ID = "news_id";

    private NewsArticle mNewsArticle;
    private TextView mHeadline;
    private TextView mArticleText;

    public static NewsArticleFragment newInstance(int newsId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_NEWS_ID, newsId);

        NewsArticleFragment fragment = new NewsArticleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int newsId = (int) getArguments().getSerializable(ARG_NEWS_ID);
        mNewsArticle = NewsArticleUtils.get(getActivity()).getNewsArticle(newsId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_news_article, container, false);

        mHeadline = (TextView) v.findViewById(R.id.headline);
        mHeadline.setText(mNewsArticle.getHeadline());
        mArticleText = (TextView) v.findViewById(R.id.article_text);
        mArticleText.setText(GeneralUtils.stripHtml(mNewsArticle.getArticleText()));

        return v;
    }
}
