package com.expocontacts.expomobile.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.expocontacts.expomobile.model.Fair;

public class FairCursorWrapper extends CursorWrapper {

    public FairCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Fair getFair() {

        Integer fairId = getInt(getColumnIndex(ExpoDbSchema.FairTable.Cols.FAIR_ID));
        String fairName = getString(getColumnIndex(ExpoDbSchema.FairTable.Cols.NAME));
        String generalInfo = getString(getColumnIndex(ExpoDbSchema.FairTable.Cols.GENERAL_INFO));
        Integer startDate = getInt(getColumnIndex(ExpoDbSchema.FairTable.Cols.START_DATE));
        Integer endDate = getInt(getColumnIndex(ExpoDbSchema.FairTable.Cols.END_DATE));
        String fairLogo = getString(getColumnIndex(ExpoDbSchema.FairTable.Cols.LOGO_URL));
        String floorplanURL = getString(getColumnIndex(ExpoDbSchema.FairTable.Cols.FLOORPLAN_URL));
        Integer showGeneralInfo = getInt(getColumnIndex(ExpoDbSchema.FairTable.Cols.SHOW_GENERAL_INFO));
        Integer showExhibitorList = getInt(getColumnIndex(ExpoDbSchema.FairTable.Cols.SHOW_EXHIBITOR_LIST));
        Integer showSchedule = getInt(getColumnIndex(ExpoDbSchema.FairTable.Cols.SHOW_SCHEDULE));
        Integer showNews = getInt(getColumnIndex(ExpoDbSchema.FairTable.Cols.SHOW_NEWS));
        Integer showSpeakers = getInt(getColumnIndex(ExpoDbSchema.FairTable.Cols.SHOW_SPEAKERS));
        Integer showFloorplan = getInt(getColumnIndex(ExpoDbSchema.FairTable.Cols.SHOW_FLOORPLAN));

        Fair fair = new Fair();
        fair.setFairId(fairId);
        fair.setFairName(fairName);
        fair.setGeneralInfo(generalInfo);
        fair.setStartDate(startDate);
        fair.setStartDate(endDate);
        fair.setFairLogoURL(fairLogo);
        fair.setFloorplanURL(floorplanURL);
        fair.setShowGeneralInfo(showGeneralInfo);
        fair.setShowExhibitorList(showExhibitorList);
        fair.setShowSchedule(showSchedule);
        fair.setShowNews(showNews);
        fair.setShowSpeakers(showSpeakers);
        fair.setShowFloorplan(showFloorplan);

        return fair;
    }
}