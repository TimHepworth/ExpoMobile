package com.expocontacts.expomobile;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.expocontacts.expomobile.model_utils.NetworkUtils;
import com.expocontacts.expomobile.model_utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;

public class RegisterActivity extends AppCompatActivity {

    private Button mRegisterButton;
    private EditText mName;
    private EditText mEmailAddress;
    private EditText mPassword;
    private  EditText mConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle(AppSettings.FAIR_NAME + " " + getResources().getString(R.string.registration));

        mName = (EditText) findViewById(R.id.txtName);
        mEmailAddress = (EditText) findViewById(R.id.txtEmailAddress);
        mPassword = (EditText) findViewById(R.id.txtPassword);
        mConfirmPassword = (EditText) findViewById(R.id.txtConfirmPassword);
        mRegisterButton=(Button) findViewById(R.id.btnRegister);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AttemptRegister().execute(
                        mName.getText().toString(),
                        mEmailAddress.getText().toString(),
                        mPassword.getText().toString(),
                        mConfirmPassword.getText().toString());

            }
        });
    }

    private class AttemptRegister extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {

            String message = "";

            if ("".equalsIgnoreCase(args[0]) ||
                    "".equalsIgnoreCase(args[1]) ||
                    "".equalsIgnoreCase(args[2]) ||
                    "".equalsIgnoreCase(args[3])
                    ) {
                message = getResources().getString(R.string.err_all_fields_required);
            } else if (!GeneralUtils.isEmailValid(args[1])) {
                message = getResources().getString(R.string.err_invalid_email);
            } else if (!args[2].equals(args[3])) {
                message = getResources().getString(R.string.err_password_mismatch);
            } else {

                try {

                    String result;
                    result = new NetworkUtils().getUrlString("http://www.expocontacts.com/webservices/exposervice.asmx/checkEmailRegistered?sEmailAddress=" + args[1]);
                    JSONObject jsonResult = new JSONObject(result);

                    if (jsonResult.getBoolean("email_registered")) {
                        message = getResources().getString(R.string.err_email_already_registered);
                    } else {

                        //
                        //  All is OK, so go ahead and register the user with expocontacts
                        //

                        String params = "";
                        String url;

                        params += "lFairDateId=" + AppSettings.FAIR_DATE_ID;
                        params += "&sName=" + URLEncoder.encode(args[0], "UTF-8");
                        params += "&sEmailAddress=" + URLEncoder.encode(args[1], "UTF-8");
                        params += "&sPassword=" + URLEncoder.encode(args[2], "UTF-8");

                        url = "http://www.expocontacts.com/webservices/exposervice.asmx/addUser?" + params;

                        result = new NetworkUtils().getUrlString(url);
                        jsonResult = new JSONObject(result);

                        SharedPreferencesUtils.setUserId(getApplicationContext(), jsonResult.getInt("user_id"));
                        SharedPreferencesUtils.setUserFairId(getApplicationContext(), jsonResult.getInt("user_fair_id"));
                        SharedPreferencesUtils.setUserEmailAddress(getApplicationContext(), args[1]);
                        SharedPreferencesUtils.setUserName(getApplicationContext(), args[2]);
                    }

                } catch (JSONException je) {
                    message = getResources().getString(R.string.err_json_error) + je;
                } catch (IOException ioe) {
                    message = getResources().getString(R.string.err_url_error) + ioe;
                }
            }

            return message;
        }

        protected void onPostExecute(String message) {
            if (message != null) {
                Log.i("ExpoDebug", message);
                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
            } else {
            }
        }

   }

}
