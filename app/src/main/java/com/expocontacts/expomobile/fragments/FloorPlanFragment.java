package com.expocontacts.expomobile.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.expocontacts.expomobile.AppSettings;
import com.expocontacts.expomobile.R;
import com.expocontacts.expomobile.PinchZoomImageView;
import com.expocontacts.expomobile.model.Fair;
import com.expocontacts.expomobile.model_utils.FairUtils;

public class FloorPlanFragment extends Fragment {

    private Fair mFair;
    private PinchZoomImageView mFloorplan;

    public FloorPlanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_floor_plan, container, false);
        mFair = FairUtils.get(getContext()).getFair(AppSettings.FAIR_DATE_ID);
        mFloorplan = (PinchZoomImageView) view.findViewById(R.id.floor_plan_image_view);

        Glide.with(this)
                .load(mFair.getFloorplanURL())
                .into(mFloorplan)
        ;

        pinchZoomPan();

        return view;
    }

    private void pinchZoomPan() {
        mFloorplan.setImageUri(mFair.getFloorplanURL());
        mFloorplan.setAlpha(1.f);
        mFloorplan.setVisibility(View.VISIBLE);
    }

}
