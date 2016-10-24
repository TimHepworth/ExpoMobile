package com.expocontacts.expomobile.model_utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.expocontacts.expomobile.database.EventCursorWrapper;
import com.expocontacts.expomobile.database.ExhibitorCursorWrapper;
import com.expocontacts.expomobile.database.ExhibitorFavouriteCursorWrapper;
import com.expocontacts.expomobile.database.ExpoDbOpenHelper;
import com.expocontacts.expomobile.database.ExpoDbSchema.ExhibitorTable;
import com.expocontacts.expomobile.database.ExpoDbSchema.ExhibitorFavouritesTable;
import com.expocontacts.expomobile.model.Exhibitor;
import com.expocontacts.expomobile.model.ExhibitorFavourite;

import java.util.ArrayList;
import java.util.List;

public class ExhibitorUtils {

    private static final String TAG = "ExhibitorUtils";

    private static ExhibitorUtils sExhibitorUtils;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private ExhibitorUtils(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new ExpoDbOpenHelper(mContext)
                .getWritableDatabase();
    }

    public static ExhibitorUtils get(Context context) {
        if (sExhibitorUtils == null) {
            sExhibitorUtils = new ExhibitorUtils(context);
        }
        return sExhibitorUtils;
    }

    public List<Exhibitor> getExhibitors() {
        List<Exhibitor> exhibitors = new ArrayList<>();

        Log.i(TAG, "Getting exhibitors from local DB");

        ExhibitorCursorWrapper cursor = queryExhibitors(null, null);

        int numExhibitors = 0;

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                exhibitors.add(cursor.getExhibitor());
                numExhibitors++;
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        Log.i(TAG, numExhibitors + " exhibitors retrieved from local DB");

        return exhibitors;
    }

    public List<ExhibitorFavourite> getExhibitorFavourites() {
        List<ExhibitorFavourite> exhibitorFavourites = new ArrayList<>();

        Log.i(TAG, "Getting exhibitor favourites from local DB");

        ExhibitorFavouriteCursorWrapper cursor = queryExhibitorFavourites(null, null);

        int numExhibitorFavourites = 0;

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                exhibitorFavourites.add(cursor.getExhibitorFavourite());
                numExhibitorFavourites++;
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        Log.i(TAG, numExhibitorFavourites + " exhibitor favourites retrieved from local DB");

        return exhibitorFavourites;
    }

    public List<Exhibitor> getFavouriteExhibitors(int userFairId) {
        List<Exhibitor> exhibitors = new ArrayList<>();

        Log.i(TAG, "Getting exhibitor favourites from local DB");

        ExhibitorCursorWrapper cursor = queryFavouriteExhibitors(userFairId);

        int numExhibitorFavourites = 0;

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                exhibitors.add(cursor.getExhibitor());
                numExhibitorFavourites++;
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        Log.i(TAG, numExhibitorFavourites + " exhibitor favourites retrieved from local DB");

        return exhibitors;
    }

    public void addExhibitor(Exhibitor e) {
        ContentValues values = getContentValues(e);
        mDatabase.insert(ExhibitorTable.NAME, null, values);
    }

    public void addExhibitorFavourite(ExhibitorFavourite ef) {
        ContentValues values = getContentValues(ef);
        mDatabase.insert(ExhibitorFavouritesTable.NAME, null, values);
    }

    public Exhibitor getExhibitor(int id) {
        ExhibitorCursorWrapper cursor = queryExhibitors(
                ExhibitorTable.Cols.EXHIBITOR_ID + " = ?",
                new String[] { Integer.toString(id) }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getExhibitor();
        } finally {
            cursor.close();
        }
    }

    public ExhibitorFavourite getExhibitorFavourite(int exhibitorFairId, int userFairId) {
        ExhibitorFavouriteCursorWrapper cursor = queryExhibitorFavourites(
                ExhibitorFavouritesTable.Cols.EXHIBITOR_FAIR_ID + " = ? AND " +
                ExhibitorFavouritesTable.Cols.USER_FAIR_ID + " = ?",
                new String[] { Integer.toString(exhibitorFairId), Integer.toString(userFairId) }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getExhibitorFavourite();
        } finally {
            cursor.close();
        }
    }

    public void deleteExhibitorFavourite(int exhibitorFairId, int userFairId) {
        mDatabase.delete(
                ExhibitorFavouritesTable.NAME,
                ExhibitorFavouritesTable.Cols.EXHIBITOR_FAIR_ID + " = ? AND " +
                ExhibitorFavouritesTable.Cols.USER_FAIR_ID + " = ?",
                new String[] { Integer.toString(exhibitorFairId), Integer.toString(userFairId) }
        );

    }

    private static ContentValues getContentValues(Exhibitor exhibitor) {
        ContentValues values = new ContentValues();
        values.put(ExhibitorTable.Cols.EXHIBITOR_ID, exhibitor.getExhibitorId().toString());
        values.put(ExhibitorTable.Cols.EXHIBITOR_FAIR_ID, exhibitor.getExhibitorFairId().toString());
        values.put(ExhibitorTable.Cols.NAME, exhibitor.getExhibitorName());
        values.put(ExhibitorTable.Cols.EXHIBITOR_LOGO, exhibitor.getExhibitorLogo());
        values.put(ExhibitorTable.Cols.STAND, exhibitor.getStand());
        values.put(ExhibitorTable.Cols.WEB_URL, exhibitor.getWebsite());
        values.put(ExhibitorTable.Cols.FAVOURITE, exhibitor.isFavourite() ? 1 : 0);
        values.put(ExhibitorTable.Cols.PROFILE, exhibitor.getProfile());

        return values;
    }

    private static ContentValues getContentValues(ExhibitorFavourite exhibitorFavourite) {
        ContentValues values = new ContentValues();
        values.put(ExhibitorFavouritesTable.Cols.EXHIBITOR_FAIR_ID, exhibitorFavourite.getExhibitorFairId().toString());
        values.put(ExhibitorFavouritesTable.Cols.USER_FAIR_ID, exhibitorFavourite.getUserFairId().toString());

        return values;
    }

    private ExhibitorCursorWrapper queryExhibitors(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ExhibitorTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new ExhibitorCursorWrapper(cursor);
    }

    private ExhibitorFavouriteCursorWrapper queryExhibitorFavourites(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ExhibitorFavouritesTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new ExhibitorFavouriteCursorWrapper(cursor);
    }

    private ExhibitorCursorWrapper queryFavouriteExhibitors(int userFairId) {

        Cursor cursor = mDatabase.rawQuery("select * from exhibitors e inner join exhibitorfavourites ef on e.exhibitorfairid = ef.exhibitorfairid where userfairid = " + userFairId,null);

        return new ExhibitorCursorWrapper(cursor);
    }

}
