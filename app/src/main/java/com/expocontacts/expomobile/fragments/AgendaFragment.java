package com.expocontacts.expomobile.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.expocontacts.expomobile.EventPagerActivity;
import com.expocontacts.expomobile.R;
import com.expocontacts.expomobile.helpers.DividerItemDecoration;
import com.expocontacts.expomobile.model.FairEvent;
import com.expocontacts.expomobile.model_utils.EventUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AgendaFragment extends Fragment {

    private RecyclerView mAgendaRecyclerView;
    private AgendaFragment.AgendaDayAdapter mAdapter;
    private String mSelectedDate;

  public AgendaFragment() {
        // Required empty public constructor
    }

    public static AgendaFragment newInstance(String param1, String param2) {
        AgendaFragment fragment = new AgendaFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_agenda, container, false);
        mAgendaRecyclerView = (RecyclerView) view
                .findViewById(R.id.agenda_recycler_view);
        mAgendaRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAgendaRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));

        LinearLayout ll = (LinearLayout) view.findViewById(R.id.day_button_linearlayout);

        EventUtils eventUtils = EventUtils.get(getActivity());

        List<String> eventDates = eventUtils.getEventDays();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String eventDay;
        String eventMonth;

        for (String dayDate : eventDates) {

            Date convertedDate = new Date();

            try {
                convertedDate = dateFormat.parse(dayDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            eventDay = dayDate.substring(0,2);
            eventMonth = dayDate.substring(3,5);

            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");

            Button btnDay = new Button(getActivity());
            btnDay.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            btnDay.setText(outFormat.format(convertedDate) + " " + eventDay + "/" + eventMonth);
            btnDay.setTag(dayDate);

            btnDay.setOnClickListener(getOnClickDoSomething(btnDay));

            ll.addView(btnDay);

        }

        mSelectedDate = eventDates.get(0);

        updateUI();

        return view;
    }

    View.OnClickListener getOnClickDoSomething(final Button button)  {
        return new View.OnClickListener() {
            public void onClick(View v) {
                mSelectedDate = (String) button.getTag();
                updateUI();
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {

        EventUtils eventUtils = EventUtils.get(getActivity());
        List<FairEvent> fairEvents = eventUtils.getEvents(mSelectedDate);

        if (mAdapter == null) {
            mAdapter = new AgendaFragment.AgendaDayAdapter(fairEvents);
            mAgendaRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setFairEvents(fairEvents);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class AgendaDayHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mEventNameTextView;
        private TextView mEventDate;
        private TextView mEventTime;

        private FairEvent mFairEvent;

        public AgendaDayHolder(View itemView) {
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

    private class AgendaDayAdapter extends RecyclerView.Adapter<AgendaFragment.AgendaDayHolder> {

        private List<FairEvent> mFairEvents;

        public AgendaDayAdapter(List<FairEvent> fairEvents) {
            mFairEvents = fairEvents;
        }

        @Override
        public AgendaFragment.AgendaDayHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_fair_event, parent, false);
            view.setBackgroundColor(0xffffffff); // first byte is the alpha, then rgb
            return new AgendaFragment.AgendaDayHolder(view);
        }

        @Override
        public void onBindViewHolder(AgendaFragment.AgendaDayHolder holder, int position) {
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
}
