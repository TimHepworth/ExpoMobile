package com.expocontacts.expomobile.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.expocontacts.expomobile.AppSettings;
import com.expocontacts.expomobile.R;
import com.expocontacts.expomobile.model.Fair;
import com.expocontacts.expomobile.model_utils.FairUtils;

public class GeneralInfoFragment extends Fragment {

    private Fair mFair;
    private WebView mGeneralInfoWebView;

    public GeneralInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFair = FairUtils.get(getActivity()).getFair(AppSettings.FAIR_DATE_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_general_info, container, false);

        mGeneralInfoWebView = (WebView) view.findViewById(R.id.general_info_webview);
        mGeneralInfoWebView.loadDataWithBaseURL("", mFair.getGeneralInfo(), "text/html", "UTF-8", "");

        return view;
    }

}
