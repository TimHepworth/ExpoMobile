package com.expocontacts.expomobile.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.expocontacts.expomobile.R;
import com.expocontacts.expomobile.RegisterActivity;
import com.expocontacts.expomobile.model.Exhibitor;
import com.expocontacts.expomobile.model.ExhibitorFavourite;
import com.expocontacts.expomobile.model_utils.ExhibitorUtils;
import com.expocontacts.expomobile.model_utils.NetworkUtils;
import com.expocontacts.expomobile.model_utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ExhibitorFragment extends Fragment {

    private static final String ARG_EXHIBITOR_ID = "exhibitor_id";

    private Exhibitor mExhibitor;
    private ExhibitorFavourite mExhibitorFavourite;
    private TextView mStand;
    private TextView mWebsite;
    private TextView mExhibitorName;
    private TextView mExhibitorProfileText;
    private ImageView mExhibitorLogo;
    private Button mToggleList;
    private boolean mIsFavourite = false;

    public static ExhibitorFragment newInstance(int exhibitorId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_EXHIBITOR_ID, exhibitorId);

        ExhibitorFragment fragment = new ExhibitorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int exhibitorId = (int) getArguments().getSerializable(ARG_EXHIBITOR_ID);
        mExhibitor = ExhibitorUtils.get(getActivity()).getExhibitor(exhibitorId);
        mExhibitorFavourite = ExhibitorUtils.get(getActivity()).getExhibitorFavourite(mExhibitor.getExhibitorFairId(), SharedPreferencesUtils.getUserFairId(getActivity()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_exhibitor, container, false);

        Bitmap logoBitmap = BitmapFactory.decodeFile(mExhibitor.getExhibitorLogo());

        mExhibitorName = (TextView) v.findViewById(R.id.exhibitor_name);
        mExhibitorName.setText(mExhibitor.getExhibitorName());
        mExhibitorLogo = (ImageView) v.findViewById(R.id.exhibitor_logo);
        mExhibitorLogo.setImageBitmap(logoBitmap);
        mStand = (TextView) v.findViewById(R.id.exhibitor_stand);
        mStand.setText(getResources().getString(R.string.location) + " " + mExhibitor.getStand());
        mWebsite = (TextView) v.findViewById(R.id.exhibitor_web_url);
        mWebsite.setText(mExhibitor.getWebsite());
        mExhibitorProfileText = (TextView) v.findViewById(R.id.exhibitor_profile_text);
        mExhibitorProfileText.setText(stripHtml(mExhibitor.getProfile()));
        mToggleList=(Button) v.findViewById(R.id.btn_toggle_list);

        if (mExhibitorFavourite != null) {
            mToggleList.setText(R.string.btn_remove_list);
            mIsFavourite = true;
        } else {
            mToggleList.setText(R.string.btn_add_list);
            mIsFavourite = false;
        }

        mWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mExhibitor.getWebsite()));
                startActivity(browserIntent);
            }
        });

        mToggleList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                // If the user hasn't registered or logged in then redirect them to the register activity

                if (SharedPreferencesUtils.getUserEmailAddress(getActivity()).equals("")) {

                    Intent i = new Intent(getActivity(), RegisterActivity.class);
                    startActivity(i);

                } else {

                    ExhibitorUtils exhibitorUtils;

                    exhibitorUtils = ExhibitorUtils.get(getActivity());

                    SharedPreferencesUtils.setDbDirty(getActivity(), true);

                    if (mIsFavourite) {

                        exhibitorUtils.deleteExhibitorFavourite(mExhibitor.getExhibitorFairId(), SharedPreferencesUtils.getUserFairId(getActivity()));
                        mToggleList.setText(R.string.btn_add_list);
                        mIsFavourite = false;

                    } else {

                        ExhibitorFavourite exhibitorFavourite = new ExhibitorFavourite();

                        exhibitorFavourite.setExhibitorFairId(mExhibitor.getExhibitorFairId());
                        exhibitorFavourite.setUserFairId(SharedPreferencesUtils.getUserFairId(getActivity()));

                        exhibitorUtils.addExhibitorFavourite(exhibitorFavourite);

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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    private String stripHtml(String html) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            return Html.fromHtml(html).toString();
        }
    }

    private class ToggleExhibitorFavourite extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {

            String message = "";

            try {

                String result;
                result = new NetworkUtils().getUrlString("http://www.expocontacts.com/webservices/exposervice.asmx/toggleExhibitorFavourite?lUserFairId=" + args[0] + "&lExhibitorFairId=" + args[1]);
                JSONObject jsonResult = new JSONObject(result);

                if (jsonResult.getBoolean("error_occurred")) {
                    message = getResources().getString(R.string.err_unexpected) + jsonResult.getString("error_message");
                } else if (jsonResult.getString("favourite_status").equals("Y")) {
                    mIsFavourite = true;
                } else {
                    mIsFavourite = false;
                }

            } catch (JSONException je) {
                message = getResources().getString(R.string.err_json_error) + je;
            } catch (IOException ioe) {
                message = getResources().getString(R.string.err_url_error) + ioe;
            }

            return message;
        }

        protected void onPostExecute(String message) {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            } else {
                if (mIsFavourite) {
                    mToggleList.setText(R.string.btn_remove_list);
                } else {
                    mToggleList.setText(R.string.btn_add_list);
                }
            }
        }

    }

}

