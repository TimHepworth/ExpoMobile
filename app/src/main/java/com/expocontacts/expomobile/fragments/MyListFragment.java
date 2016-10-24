package com.expocontacts.expomobile.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.expocontacts.expomobile.EventPagerActivity;
import com.expocontacts.expomobile.ExhibitorPagerActivity;
import com.expocontacts.expomobile.R;
import com.expocontacts.expomobile.helpers.DividerItemDecoration;
import com.expocontacts.expomobile.model.Exhibitor;
import com.expocontacts.expomobile.model.FairEvent;
import com.expocontacts.expomobile.model_utils.EventUtils;
import com.expocontacts.expomobile.model_utils.ExhibitorUtils;
import com.expocontacts.expomobile.model_utils.SharedPreferencesUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyListFragment extends Fragment {

    private RecyclerView mMyListRecyclerView;
    private RecyclerView mMyListRecyclerViewExhibitors;
    private MyListFragment.MyListAdapter mAdapter;
    private MyListFragment.MyListAdapterExhibitors mAdapterExhibitors;
    private String mListType = "Schedule";
    private Button mExhibitors;
    private Button mSchedule;

    public MyListFragment() {
        // Required empty public constructor
    }

    public static MyListFragment newInstance(String param1, String param2) {
        MyListFragment fragment = new MyListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_list, container, false);

        mMyListRecyclerView = (RecyclerView) view.findViewById(R.id.my_list_recycler_view);
        mMyListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMyListRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));

        mMyListRecyclerViewExhibitors = (RecyclerView) view.findViewById(R.id.my_list_recycler_view_exhibitors);
        mMyListRecyclerViewExhibitors.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMyListRecyclerViewExhibitors.addItemDecoration(new DividerItemDecoration(getActivity()));

        mExhibitors = (Button) view.findViewById(R.id.btnMyListExhibitors);
        mSchedule = (Button) view.findViewById(R.id.btnMyListAgenda);

        mExhibitors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mListType = "Exhibitors";
                updateUI();
            }
        });

        mSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mListType = "Schedule";
                updateUI();
            }
        });

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {

        if (mListType == "Schedule") {

            EventUtils eventUtils = EventUtils.get(getActivity());
            List<FairEvent> fairEvents = eventUtils.getFavouriteEvents(SharedPreferencesUtils.getUserFairId(getActivity()));

            if (mAdapter == null) {
                mAdapter = new MyListFragment.MyListAdapter(fairEvents);
                mMyListRecyclerView.setAdapter(mAdapter);
            } else {
                mAdapter.setFairEvents(fairEvents);
                mAdapter.notifyDataSetChanged();
            }

            mMyListRecyclerView.setVisibility(View.VISIBLE);
            mMyListRecyclerViewExhibitors.setVisibility(View.GONE);
        } else {

            ExhibitorUtils exhibitorUtils = ExhibitorUtils.get(getActivity());
            List<Exhibitor> exhibitors = exhibitorUtils.getFavouriteExhibitors(SharedPreferencesUtils.getUserFairId(getActivity()));

            if (mAdapterExhibitors == null) {
                mAdapterExhibitors = new MyListFragment.MyListAdapterExhibitors(exhibitors);
                mMyListRecyclerViewExhibitors.setAdapter(mAdapterExhibitors);
            } else {
                mAdapterExhibitors.setExhibitors(exhibitors);
                mAdapterExhibitors.notifyDataSetChanged();
            }

            mMyListRecyclerView.setVisibility(View.GONE);
            mMyListRecyclerViewExhibitors.setVisibility(View.VISIBLE);
        }
    }

    private class MyListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mEventNameTextView;
        private TextView mEventDate;
        private TextView mEventTime;

        private FairEvent mFairEvent;

        public MyListHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mEventNameTextView = (TextView) itemView.findViewById(R.id.list_item_event_name);
            mEventDate = (TextView) itemView.findViewById(R.id.list_item_event_date);
            mEventTime = (TextView) itemView.findViewById(R.id.list_item_event_time);
        }

        public void bindEvents(FairEvent fairEvent) {
            mFairEvent = fairEvent;

            mEventNameTextView.setText(mFairEvent.getEventName());
            mEventDate.setText(mFairEvent.getEventDate());

            String sTime;
            if (mFairEvent.getEventEndTime() == "") {
                mEventTime.setText(mFairEvent.getEventTime());
            } else {
                sTime = mFairEvent.getEventTime() + "-" + mFairEvent.getEventEndTime();
                mEventTime.setText(sTime);
            }
        }

        @Override
        public void onClick(View v) {
            Intent intent = EventPagerActivity.newIntent(getActivity(), mFairEvent.getFairEventId());
            intent.putExtra("EventDate", mFairEvent.getEventDate());
            startActivity(intent);
        }
    }

    private class MyListAdapter extends RecyclerView.Adapter<MyListFragment.MyListHolder> {

        private List<FairEvent> mFairEvents;

        public MyListAdapter(List<FairEvent> fairEvents) {
            mFairEvents = fairEvents;
        }

        @Override
        public MyListFragment.MyListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_fair_event, parent, false);
            view.setBackgroundColor(0xffffffff); // first byte is the alpha, then rgb
            return new MyListFragment.MyListHolder(view);
        }

        @Override
        public void onBindViewHolder(MyListFragment.MyListHolder holder, int position) {
            FairEvent fairEvent = mFairEvents.get(position);
            holder.bindEvents(fairEvent);
        }

        @Override
        public int getItemCount() {
            return mFairEvents.size();
        }

        public void setFairEvents(List<FairEvent> fairEvents) {
            mFairEvents = fairEvents;
        }
    }

    private class MyListHolderExhibitors extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mLogoImageView;
        private TextView mNameTextView;

        private Exhibitor mExhibitor;

        public MyListHolderExhibitors(View itemView) {
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

    private class MyListAdapterExhibitors extends RecyclerView.Adapter<MyListFragment.MyListHolderExhibitors> {

        private List<Exhibitor> mExhibitors;

        public MyListAdapterExhibitors(List<Exhibitor> exhibitors) {
            mExhibitors = exhibitors;
        }

        @Override
        public MyListFragment.MyListHolderExhibitors onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_exhibitor, parent, false);
            view.setBackgroundColor(0xffffffff); // first byte is the alpha, then rgb
            return new MyListFragment.MyListHolderExhibitors(view);
        }

        @Override
        public void onBindViewHolder(MyListFragment.MyListHolderExhibitors holder, int position) {
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