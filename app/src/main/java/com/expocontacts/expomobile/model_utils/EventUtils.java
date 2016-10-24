package com.expocontacts.expomobile.model_utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.expocontacts.expomobile.database.EventCursorWrapper;
import com.expocontacts.expomobile.database.EventFavouriteCursorWrapper;
import com.expocontacts.expomobile.database.ExhibitorFavouriteCursorWrapper;
import com.expocontacts.expomobile.database.ExpoDbOpenHelper;
import com.expocontacts.expomobile.database.ExpoDbSchema;
import com.expocontacts.expomobile.model.EventFavourite;
import com.expocontacts.expomobile.model.ExhibitorFavourite;
import com.expocontacts.expomobile.model.FairEvent;

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

import static com.expocontacts.expomobile.database.ExpoDbSchema.EventTable.Cols.EVENT_DATE;

public class EventUtils {

    private static final String TAG = "EventUtils";

    private static EventUtils sEventUtils;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private Activity mCallingActivity;

    private EventUtils(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new ExpoDbOpenHelper(mContext)
                .getWritableDatabase();
    }

    public static EventUtils get(Context context) {
        if (sEventUtils == null) {
            sEventUtils = new EventUtils(context);
        }
        return sEventUtils;
    }

    public List<FairEvent> getEvents(String eventDate) {
        List<FairEvent> Events = new ArrayList<>();

        String[] whereArgs = new String [] { eventDate };

        EventCursorWrapper cursor = queryEvents(EVENT_DATE + " = ?", whereArgs);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Events.add(cursor.getEvent());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return Events;
    }

    public List<EventFavourite> getEventFavourites() {
        List<EventFavourite> eventFavourites = new ArrayList<>();

        Log.i(TAG, "Getting event favourites from local DB");

        EventFavouriteCursorWrapper cursor = queryEventFavourites(null, null);

        int numEventFavourites = 0;

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                eventFavourites.add(cursor.getEventFavourite());
                numEventFavourites++;
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        Log.i(TAG, numEventFavourites + " exhibitor favourites retrieved from local DB");

        return eventFavourites;
    }

    public List<FairEvent> getFavouriteEvents(int userFairId) {
        List<FairEvent> eventFavourites = new ArrayList<>();

        EventCursorWrapper cursor = queryFavouriteEvents(userFairId);

        int numEventFavourites = 0;

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                eventFavourites.add(cursor.getEvent());
                numEventFavourites++;
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        Log.i(TAG, numEventFavourites + " event favourites retrieved from local DB");

        return eventFavourites;
    }

    public List<String> getEventDays() {
        List<String> eventDays = new ArrayList<>();

        EventCursorWrapper cursor = queryEventDays();

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                eventDays.add(cursor.getEventDate());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return eventDays;
    }

    public void addEvent(FairEvent e) {
        ContentValues values = getContentValues(e);
        mDatabase.insert(ExpoDbSchema.EventTable.NAME, null, values);
    }

    public void addEventFavourite(EventFavourite ef) {
        ContentValues values = getContentValues(ef);
        mDatabase.insert(ExpoDbSchema.EventFavouritesTable.NAME, null, values);
    }

    public FairEvent getEvent(int id) {
        EventCursorWrapper cursor = queryEvents(
                ExpoDbSchema.EventTable.Cols.EVENT_ID + " = ?",
                new String[] { Integer.toString(id) }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getEvent();
        } finally {
            cursor.close();
        }
    }

    public EventFavourite getEventFavourite(int eventId, int userFairId) {
        EventFavouriteCursorWrapper cursor = queryEventFavourites(
                ExpoDbSchema.EventFavouritesTable.Cols.EVENT_ID + " = ? AND " +
                        ExpoDbSchema.EventFavouritesTable.Cols.USER_FAIR_ID + " = ?",
                new String[] { Integer.toString(eventId), Integer.toString(userFairId) }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getEventFavourite();
        } finally {
            cursor.close();
        }
    }

    public void deleteEventFavourite(int eventId, int userFairId) {
        mDatabase.delete(
                ExpoDbSchema.EventFavouritesTable.NAME,
                ExpoDbSchema.EventFavouritesTable.Cols.EVENT_ID + " = ? AND " +
                        ExpoDbSchema.EventFavouritesTable.Cols.USER_FAIR_ID + " = ?",
                new String[] { Integer.toString(eventId), Integer.toString(userFairId) }
        );

    }

    private static ContentValues getContentValues(FairEvent Event) {
        ContentValues values = new ContentValues();
        values.put(ExpoDbSchema.EventTable.Cols.EVENT_ID, Event.getFairEventId().toString());
        values.put(ExpoDbSchema.EventTable.Cols.EVENT_NAME, Event.getEventName());
        values.put(ExpoDbSchema.EventTable.Cols.EVENT_DESCRIPTION, Event.getEventDescription());
        values.put(ExpoDbSchema.EventTable.Cols.EVENT_LOCATION, Event.getEventLocation());
        values.put(ExpoDbSchema.EventTable.Cols.EVENT_PHOTO, Event.getEventPhoto());
        values.put(EVENT_DATE, Event.getEventDate());
        values.put(ExpoDbSchema.EventTable.Cols.EVENT_TIME, Event.getEventTime());
        values.put(ExpoDbSchema.EventTable.Cols.EVENT_END_DATE, Event.getEventEndDate());
        values.put(ExpoDbSchema.EventTable.Cols.EVENT_END_TIME, Event.getEventEndTime());

        return values;
    }

    private static ContentValues getContentValues(EventFavourite eventFavourite) {
        ContentValues values = new ContentValues();
        values.put(ExpoDbSchema.EventFavouritesTable.Cols.EVENT_ID, eventFavourite.getEventId().toString());
        values.put(ExpoDbSchema.EventFavouritesTable.Cols.USER_FAIR_ID, eventFavourite.getUserFairId().toString());

        return values;
    }

    private EventCursorWrapper queryEvents(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ExpoDbSchema.EventTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new EventCursorWrapper(cursor);
    }

    private EventFavouriteCursorWrapper queryEventFavourites(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ExpoDbSchema.EventFavouritesTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new EventFavouriteCursorWrapper(cursor);
    }

    private EventCursorWrapper queryFavouriteEvents(int userFairId) {

        Cursor cursor = mDatabase.rawQuery("select * from events e inner join eventfavourites ef on e.eventid = ef.eventid where userfairid = " + userFairId,null);

        return new EventCursorWrapper(cursor);
    }

    private EventCursorWrapper queryEventDays() {

        // e.g. rawQuery("SELECT id, name FROM people WHERE name = ? AND id = ?", new String[] {"David", "2"});

        Cursor cursor = mDatabase.rawQuery("select distinct eventdate from events",null);

        return new EventCursorWrapper(cursor);
    }
}
