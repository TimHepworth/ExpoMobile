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

import com.expocontacts.expomobile.ExhibitorPagerActivity;
import com.expocontacts.expomobile.R;
import com.expocontacts.expomobile.helpers.DividerItemDecoration;
import com.expocontacts.expomobile.model.Exhibitor;
import com.expocontacts.expomobile.model_utils.ExhibitorUtils;

import java.util.List;

public class ExhibitorListFragment extends Fragment {

    private RecyclerView mExhibitorRecyclerView;
    private ExhibitorAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exhibitor_list, container, false);

        mExhibitorRecyclerView = (RecyclerView) view.findViewById(R.id.exhibitor_list_recycler_view);
        mExhibitorRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mExhibitorRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        ExhibitorUtils exhibitorUtils = ExhibitorUtils.get(getActivity());
        List<Exhibitor> exhibitors = exhibitorUtils.getExhibitors();

        if (mAdapter == null) {
            mAdapter = new ExhibitorAdapter(exhibitors);
            mExhibitorRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setExhibitors(exhibitors);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class ExhibitorHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mLogoImageView;
        private TextView mNameTextView;

        private Exhibitor mExhibitor;

        public ExhibitorHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mLogoImageView = (ImageView) itemView.findViewById(R.id.list_item_exhibitor_logo);
            mNameTextView = (TextView) itemView.findViewById(R.id.list_item_exhibitor_name);
        }

        public void bindExhibitor(Exhibitor exhibitor) {
            mExhibitor = exhibitor;

            Bitmap logoBitmap = BitmapFactory.decodeFile(mExhibitor.getExhibitorLogo());

            mLogoImageView.setImageBitmap(logoBitmap);
            mNameTextView.setText(mExhibitor.getExhibitorName());
        }

        @Override
        public void onClick(View v) {
            Intent intent = ExhibitorPagerActivity.newIntent(getActivity(), mExhibitor.getExhibitorId());
            startActivity(intent);
        }
    }

    private class ExhibitorAdapter extends RecyclerView.Adapter<ExhibitorHolder> {

        private List<Exhibitor> mExhibitors;

        public ExhibitorAdapter(List<Exhibitor> exhibitors) {
            mExhibitors = exhibitors;
        }

        @Override
        public ExhibitorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_exhibitor, parent, false);
            view.setBackgroundColor(0xffffffff); // first byte is the alpha, then rgb
            return new ExhibitorHolder(view);
        }

        @Override
        public void onBindViewHolder(ExhibitorHolder holder, int position) {
            Exhibitor exhibitor = mExhibitors.get(position);
            holder.bindExhibitor(exhibitor);
        }

        @Override
        public int getItemCount() {
            return mExhibitors.size();
        }

        public void setExhibitors(List<Exhibitor> exhibitors) {
            mExhibitors = exhibitors;
        }
    }

}














