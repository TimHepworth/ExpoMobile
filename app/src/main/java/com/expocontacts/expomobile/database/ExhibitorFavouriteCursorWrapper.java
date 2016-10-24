package com.expocontacts.expomobile.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.expocontacts.expomobile.model.ExhibitorFavourite;
import com.expocontacts.expomobile.database.ExpoDbSchema.ExhibitorFavouritesTable;

public class ExhibitorFavouriteCursorWrapper extends CursorWrapper {

    public ExhibitorFavouriteCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public ExhibitorFavourite getExhibitorFavourite() {

        Integer exhibitorFairId = getInt(getColumnIndex(ExhibitorFavouritesTable.Cols.EXHIBITOR_FAIR_ID));
        Integer userFairId = getInt(getColumnIndex(ExhibitorFavouritesTable.Cols.USER_FAIR_ID));

        ExhibitorFavourite exhibitorFavourite = new ExhibitorFavourite();
        exhibitorFavourite.setExhibitorFairId(exhibitorFairId);
        exhibitorFavourite.setUserFairId(userFairId);

        return exhibitorFavourite;
    }
}