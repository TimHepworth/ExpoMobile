package com.expocontacts.expomobile.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.expocontacts.expomobile.GeneralUtils;
import com.expocontacts.expomobile.R;
import com.expocontacts.expomobile.RegisterActivity;
import com.expocontacts.expomobile.model.EventFavourite;
import com.expocontacts.expomobile.model.ExhibitorFavourite;
import com.expocontacts.expomobile.model.FairEvent;
import com.expocontacts.expomobile.model_utils.EventUtils;
import com.expocontacts.expomobile.model_utils.ExhibitorUtils;
import com.expocontacts.expomobile.model_utils.SharedPreferencesUtils;

public class FairEventFragment extends Fragment {

    private static final String ARG_EVENT_ID = "event_id";

    private FairEvent mFairEvent;
    private EventFavourite mEventFavourite;
    private TextView mEventName;
    private TextView mEventDescription;
    private Button mToggleList;
    private boolean mIsFavourite = false;

    public static FairEventFragment newInstance(int fairEventId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_EVENT_ID, fairEventId);

        FairEventFragment fragment = new FairEventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int fairEventId = (int) getArguments().getSerializable(ARG_EVENT_ID);
        mFairEvent = EventUtils.get(getActivity()).getEvent(fairEventId);
        mEventFavourite = EventUtils.get(getActivity()).getEventFavourite(mFairEvent.getFairEventId(), SharedPreferencesUtils.getUserFairId(getActivity()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_fair_event, container, false);

        mEventName = (TextView) v.findViewById(R.id.event_name);
        mEventName.setText(mFairEvent.getEventName());
        mEventDescription = (TextView) v.findViewById(R.id.event_description);
        mEventDescription.setText(GeneralUtils.stripHtml(mFairEvent.getEventDescription()));
        mToggleList=(Button) v.findViewById(R.id.btn_toggle_event_list);

        if (mEventFavourite != null) {
            mToggleList.setText(R.string.btn_remove_list);
            mIsFavourite = true;
        } else {
            mToggleList.setText(R.string.btn_add_list);
            mIsFavourite = false;
        }

        mToggleList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                // If the user hasn't registered or logged in then redirect them to the register activity

                if (SharedPreferencesUtils.getUserEmailAddress(getActivity()).equals("")) {

                    Intent i = new Intent(getActivity(), RegisterActivity.class);
                    startActivity(i);

                } else {

                    EventUtils eventUtils;

                    eventUtils = EventUtils.get(getActivity());

                    SharedPreferencesUtils.setDbDirty(getActivity(), true);

                    if (mIsFavourite) {

                        eventUtils.deleteEventFavourite(mFairEvent.getFairEventId(), SharedPreferencesUtils.getUserFairId(getActivity()));
                        mToggleList.setText(R.string.btn_add_list);
                        mIsFavourite = false;

                    } else {

                        EventFavourite eventFavourite = new EventFavourite();

                        eventFavourite.setEventId(mFairEvent.getFairEventId());
                        eventFavourite.setUserFairId(SharedPreferencesUtils.getUserFairId(getActivity()));

                        eventUtils.addEventFavourite(eventFavourite);

                        mToggleList.setText(R.string.btn_remove_list);
                        mIsFavourite = true;
                    }

//                    new ToggleExhibitorFavourite().execute(
//                            String.valueOf(SharedPreferencesUtils.getUserFairId(getActivity())),
//                            mExhibitor.getExhibitorFairId().toString());
                }
            }
        });

        return v;
    }
}

