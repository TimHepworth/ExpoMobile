package com.expocontacts.expomobile.model_utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.expocontacts.expomobile.database.ExpoDbOpenHelper;
import com.expocontacts.expomobile.database.ExpoDbSchema;
import com.expocontacts.expomobile.database.SpeakerCursorWrapper;
import com.expocontacts.expomobile.model.Speaker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SpeakerUtils {

    private static final String TAG = "SpeakerUtils";

    private static SpeakerUtils sSpeakerUtils;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private Activity mCallingActivity;

    private SpeakerUtils(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new ExpoDbOpenHelper(mContext)
                .getWritableDatabase();
    }

    public static SpeakerUtils get(Context context) {
        if (sSpeakerUtils == null) {
            sSpeakerUtils = new SpeakerUtils(context);
        }
        return sSpeakerUtils;
    }

    public List<Speaker> getSpeakers() {
        List<Speaker> Speakers = new ArrayList<>();

        SpeakerCursorWrapper cursor = querySpeakers(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Speakers.add(cursor.getSpeaker());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return Speakers;
    }

    public void addSpeaker(Speaker e) {
        ContentValues values = getContentValues(e);
        mDatabase.insert(ExpoDbSchema.SpeakerTable.NAME, null, values);
    }

    public Speaker getSpeaker(int id) {
        SpeakerCursorWrapper cursor = querySpeakers(
                ExpoDbSchema.SpeakerTable.Cols.SPEAKER_ID + " = ?",
                new String[] { Integer.toString(id) }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getSpeaker();
        } finally {
            cursor.close();
        }
    }

    private static ContentValues getContentValues(Speaker Speaker) {
        ContentValues values = new ContentValues();
        values.put(ExpoDbSchema.SpeakerTable.Cols.SPEAKER_ID, Speaker.getSpeakerId().toString());
        values.put(ExpoDbSchema.SpeakerTable.Cols.SPEAKER_NAME, Speaker.getSpeakerName());
        values.put(ExpoDbSchema.SpeakerTable.Cols.SPEAKER_JOBTITLE, Speaker.getJobTitle());
        values.put(ExpoDbSchema.SpeakerTable.Cols.SPEAKER_COMPANY, Speaker.getSpeakerCompany());
        values.put(ExpoDbSchema.SpeakerTable.Cols.EMAIL_ADDRESS, Speaker.getEmailAddress());
        values.put(ExpoDbSchema.SpeakerTable.Cols.SPEAKER_BIO, Speaker.getSpeakerBio());
        values.put(ExpoDbSchema.SpeakerTable.Cols.SPEAKER_PHOTO, Speaker.getSpeakerPhoto());

        return values;
    }

    private SpeakerCursorWrapper querySpeakers(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ExpoDbSchema.SpeakerTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new SpeakerCursorWrapper(cursor);
    }
}
