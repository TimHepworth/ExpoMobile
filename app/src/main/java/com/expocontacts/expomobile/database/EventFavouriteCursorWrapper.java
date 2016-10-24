package com.expocontacts.expomobile.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.expocontacts.expomobile.model.EventFavourite;
import com.expocontacts.expomobile.database.ExpoDbSchema.EventFavouritesTable;

public class EventFavouriteCursorWrapper extends CursorWrapper {

    public EventFavouriteCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public EventFavourite getEventFavourite() {

        Integer eventId = getInt(getColumnIndex(EventFavouritesTable.Cols.EVENT_ID));
        Integer userFairId = getInt(getColumnIndex(EventFavouritesTable.Cols.USER_FAIR_ID));

        EventFavourite eventFavourite = new EventFavourite();
        eventFavourite.setEventId(eventId);
        eventFavourite.setUserFairId(userFairId);

        return eventFavourite;
    }
}