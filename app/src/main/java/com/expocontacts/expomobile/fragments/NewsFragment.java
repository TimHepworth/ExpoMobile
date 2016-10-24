package com.expocontacts.expomobile.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.expocontacts.expomobile.GeneralUtils;
import com.expocontacts.expomobile.NewsPagerActivity;
import com.expocontacts.expomobile.R;
import com.expocontacts.expomobile.helpers.DividerItemDecoration;
import com.expocontacts.expomobile.model.NewsArticle;
import com.expocontacts.expomobile.model_utils.NewsArticleUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NewsFragment extends Fragment {

    private RecyclerView mNewsRecyclerView;
    private NewsArticleAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        mNewsRecyclerView = (RecyclerView) view
                .findViewById(R.id.news_recycler_view);
        mNewsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mNewsRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        NewsArticleUtils newsArticleUtils = NewsArticleUtils.get(getActivity());
        List<NewsArticle> newsArticles = newsArticleUtils.getNewsArticles();

        if (mAdapter == null) {
            mAdapter = new NewsFragment.NewsArticleAdapter(newsArticles);
            mNewsRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setNewsArticles(newsArticles);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class NewsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mHeadlineTextView;
        private TextView mPublicationDateTextView;
        private TextView mArticleTextTextView;
        private TextView mMoreInfo;

        private NewsArticle mNewsArticle;

        public NewsHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mHeadlineTextView = (TextView) itemView.findViewById(R.id.list_item_headline);
            mPublicationDateTextView = (TextView) itemView.findViewById(R.id.list_item_publication_date);
            mArticleTextTextView = (TextView) itemView.findViewById(R.id.list_item_article_text);
            mMoreInfo = (TextView) itemView.findViewById(R.id.list_item_article_text_more);
        }

        public void bindNews(NewsArticle newsArticle) {

            mNewsArticle = newsArticle;

            Date d = new Date(mNewsArticle.getPublicationDate());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

            mPublicationDateTextView.setText(dateFormat.format(d));
            mHeadlineTextView.setText(mNewsArticle.getHeadline());
            String s = GeneralUtils.stripHtml(mNewsArticle.getArticleText());

            if (s.length() > 200) {
                mMoreInfo.setVisibility(View.VISIBLE);
                s = s.substring(0, Math.min(s.length(), 200)) + " ..."; // Just take the first 200 characters (if there are 200!)


            } else {
                mMoreInfo.setVisibility(View.INVISIBLE);
            }

            mArticleTextTextView.setText(s);

        }

        @Override
        public void onClick(View v) {
            Intent intent = NewsPagerActivity.newIntent(getActivity(), mNewsArticle.getNewsId());
            startActivity(intent);
        }
    }

    private class NewsArticleAdapter extends RecyclerView.Adapter<NewsFragment.NewsHolder> {

        private List<NewsArticle> mNewsArticles;

        public NewsArticleAdapter(List<NewsArticle> newsArticles) {
            mNewsArticles = newsArticles;
        }

        @Override
        public NewsFragment.NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_news, parent, false);
            view.setBackgroundColor(0xffffffff); // first byte is the alpha, then rgb
            return new NewsFragment.NewsHolder(view);
        }

        @Override
        public void onBindViewHolder(NewsFragment.NewsHolder holder, int position) {
            NewsArticle newsArticle = mNewsArticles.get(position);
            holder.bindNews(newsArticle);
        }

        @Override
        public int getItemCount() {
            return mNewsArticles.size();
        }

        public void setNewsArticles(List<NewsArticle> newsArticles) {
            mNewsArticles = newsArticles;
        }
    }
}
