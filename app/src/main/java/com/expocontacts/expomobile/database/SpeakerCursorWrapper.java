package com.expocontacts.expomobile.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.expocontacts.expomobile.model.Speaker;

public class SpeakerCursorWrapper extends CursorWrapper {

    public SpeakerCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Speaker getSpeaker() {

        Integer speakerId = getInt(getColumnIndex(ExpoDbSchema.SpeakerTable.Cols.SPEAKER_ID));
        String speakerName = getString(getColumnIndex(ExpoDbSchema.SpeakerTable.Cols.SPEAKER_NAME));
        String speakerJobTitle = getString(getColumnIndex(ExpoDbSchema.SpeakerTable.Cols.SPEAKER_JOBTITLE));
        String speakerCompany = getString(getColumnIndex(ExpoDbSchema.SpeakerTable.Cols.SPEAKER_COMPANY));
        String emailAddress = getString(getColumnIndex(ExpoDbSchema.SpeakerTable.Cols.EMAIL_ADDRESS));
        String speakerBio = getString(getColumnIndex(ExpoDbSchema.SpeakerTable.Cols.SPEAKER_BIO));
        String speakerPhoto = getString(getColumnIndex(ExpoDbSchema.SpeakerTable.Cols.SPEAKER_PHOTO));

        Speaker speaker = new Speaker();
        speaker.setSpeakerId(speakerId);
        speaker.setSpeakerName(speakerName);
        speaker.setJobTitle(speakerJobTitle);
        speaker.setSpeakerCompany(speakerCompany);
        speaker.setEmailAddress(emailAddress);
        speaker.setSpeakerBio(speakerBio);
        speaker.setSpeakerPhoto(speakerPhoto);

        return speaker;
    }
}