package com.expocontacts.expomobile.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.expocontacts.expomobile.model.FairEvent;

public class EventCursorWrapper extends CursorWrapper {

    public EventCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public FairEvent getEvent() {

        Integer eventId = getInt(getColumnIndex(ExpoDbSchema.EventTable.Cols.EVENT_ID));
        String eventName = getString(getColumnIndex(ExpoDbSchema.EventTable.Cols.EVENT_NAME));
        String eventDescription = getString(getColumnIndex(ExpoDbSchema.EventTable.Cols.EVENT_DESCRIPTION));
        String eventLocation = getString(getColumnIndex(ExpoDbSchema.EventTable.Cols.EVENT_LOCATION));
        String eventPhoto = getString(getColumnIndex(ExpoDbSchema.EventTable.Cols.EVENT_PHOTO));
        String eventDate = getString(getColumnIndex(ExpoDbSchema.EventTable.Cols.EVENT_DATE));
        String eventTime = getString(getColumnIndex(ExpoDbSchema.EventTable.Cols.EVENT_TIME));
        String eventEndDate = getString(getColumnIndex(ExpoDbSchema.EventTable.Cols.EVENT_END_DATE));
        String eventEndTime = getString(getColumnIndex(ExpoDbSchema.EventTable.Cols.EVENT_END_TIME));

        FairEvent fairEvent = new FairEvent();
        fairEvent.setEventId(eventId);
        fairEvent.setEventName(eventName);
        fairEvent.setEventDescription(eventDescription);
        fairEvent.setEventLocation(eventLocation);
        fairEvent.setEventPhoto(eventPhoto);
        fairEvent.setEventDate(eventDate);
        fairEvent.setEventTime(eventTime);
        fairEvent.setEventEndDate(eventEndDate);
        fairEvent.setEventEndTime(eventEndTime);

        return fairEvent;
    }

    public String getEventDate() {

        String eventDate = getString(getColumnIndex(ExpoDbSchema.EventTable.Cols.EVENT_DATE));

        return eventDate;
    }

}