package com.expocontacts.expomobile.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.expocontacts.expomobile.model.Exhibitor;
import com.expocontacts.expomobile.database.ExpoDbSchema.ExhibitorTable;

public class ExhibitorCursorWrapper extends CursorWrapper {

    public ExhibitorCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Exhibitor getExhibitor() {

        Integer exhibitorId = getInt(getColumnIndex(ExhibitorTable.Cols.EXHIBITOR_ID));
        Integer exhibitorFairId = getInt(getColumnIndex(ExhibitorTable.Cols.EXHIBITOR_FAIR_ID));
        String exhibitorName = getString(getColumnIndex(ExhibitorTable.Cols.NAME));
        String exhibitorLogo = getString(getColumnIndex(ExhibitorTable.Cols.EXHIBITOR_LOGO));
        String stand = getString(getColumnIndex(ExhibitorTable.Cols.STAND));
        String webURL = getString(getColumnIndex(ExhibitorTable.Cols.WEB_URL));
        String profile = getString(getColumnIndex(ExhibitorTable.Cols.PROFILE));

        Exhibitor exhibitor = new Exhibitor();
        exhibitor.setExhibitorId(exhibitorId);
        exhibitor.setExhibitorFairId(exhibitorFairId);
        exhibitor.setExhibitorName(exhibitorName);
        exhibitor.setExhibitorLogo(exhibitorLogo);
        exhibitor.setStand(stand);
        exhibitor.setWebsite(webURL);
        exhibitor.setProfile(profile);

        return exhibitor;
    }
}