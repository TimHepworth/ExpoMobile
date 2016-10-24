package com.expocontacts.expomobile.model_utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.expocontacts.expomobile.database.ExpoDbOpenHelper;
import com.expocontacts.expomobile.database.ExpoDbSchema;
import com.expocontacts.expomobile.database.FairCursorWrapper;
import com.expocontacts.expomobile.model.Fair;

import java.util.ArrayList;
import java.util.List;

public class FairUtils {

    private static final String TAG = "FairUtils";

    private static FairUtils sFairUtils;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private Activity mCallingActivity;

    private FairUtils(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new ExpoDbOpenHelper(mContext)
                .getWritableDatabase();
    }

    public static FairUtils get(Context context) {
        if (sFairUtils == null) {
            sFairUtils = new FairUtils(context);
        }
        return sFairUtils;
    }

    public List<Fair> getFairs() {
        List<Fair> Fairs = new ArrayList<>();

        Log.i(TAG, "Getting Fairs from local DB");

        FairCursorWrapper cursor = queryFairs(null, null);

        int numFairs = 0;

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Fairs.add(cursor.getFair());
                numFairs++;
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        Log.i(TAG, numFairs + " Fairs retrieved from local DB");

        return Fairs;
    }

    public void addFair(Fair e) {
        ContentValues values = getContentValues(e);
        mDatabase.insert(ExpoDbSchema.FairTable.NAME, null, values);
    }

    public Fair getFair(int id) {
        FairCursorWrapper cursor = queryFairs(
                ExpoDbSchema.FairTable.Cols.FAIR_ID + " = ?",
                new String[] { Integer.toString(id) }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getFair();
        } finally {
            cursor.close();
        }
    }

    private static ContentValues getContentValues(Fair Fair) {
        ContentValues values = new ContentValues();
        values.put(ExpoDbSchema.FairTable.Cols.FAIR_ID, Fair.getFairId().toString());
        values.put(ExpoDbSchema.FairTable.Cols.NAME, Fair.getFairName());
        values.put(ExpoDbSchema.FairTable.Cols.GENERAL_INFO, Fair.getGeneralInfo());
        values.put(ExpoDbSchema.FairTable.Cols.UPDATE_REQUIRED, Fair.getUpdateRequired());
        values.put(ExpoDbSchema.FairTable.Cols.START_DATE, Fair.getStartDate());
        values.put(ExpoDbSchema.FairTable.Cols.END_DATE, Fair.getEndDate());
        values.put(ExpoDbSchema.FairTable.Cols.LOGO_URL, Fair.getFairLogoURL());
        values.put(ExpoDbSchema.FairTable.Cols.FLOORPLAN_URL, Fair.getFloorplanURL());
        values.put(ExpoDbSchema.FairTable.Cols.SHOW_GENERAL_INFO, Fair.getShowGeneralInfo());
        values.put(ExpoDbSchema.FairTable.Cols.SHOW_EXHIBITOR_LIST, Fair.getShowExhibitorList());
        values.put(ExpoDbSchema.FairTable.Cols.SHOW_SCHEDULE, Fair.getShowSchedule());
        values.put(ExpoDbSchema.FairTable.Cols.SHOW_NEWS, Fair.getShowNews());
        values.put(ExpoDbSchema.FairTable.Cols.SHOW_SPEAKERS, Fair.getShowSpeakers());
        values.put(ExpoDbSchema.FairTable.Cols.SHOW_FLOORPLAN, Fair.getShowFloorplan());

        return values;
    }

    private FairCursorWrapper queryFairs(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ExpoDbSchema.FairTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new FairCursorWrapper(cursor);
    }

}
