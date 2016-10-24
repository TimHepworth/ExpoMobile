package com.expocontacts.expomobile.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.expocontacts.expomobile.database.ExpoDbSchema.EventTable;
import com.expocontacts.expomobile.database.ExpoDbSchema.ExhibitorTable;
import com.expocontacts.expomobile.database.ExpoDbSchema.FairTable;
import com.expocontacts.expomobile.database.ExpoDbSchema.NewsTable;
import com.expocontacts.expomobile.database.ExpoDbSchema.SpeakerTable;
import com.expocontacts.expomobile.database.ExpoDbSchema.ExhibitorFavouritesTable;
import com.expocontacts.expomobile.database.ExpoDbSchema.EventFavouritesTable;
import com.expocontacts.expomobile.database.ExpoDbSchema.UserPostTable;

public class ExpoDbOpenHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "expoDb.db";

    public ExpoDbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + FairTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                FairTable.Cols.FAIR_ID + " integer, " +
                FairTable.Cols.NAME + ", " +
                FairTable.Cols.GENERAL_INFO + ", " +
                FairTable.Cols.UPDATE_REQUIRED + " integer, " +
                FairTable.Cols.START_DATE + " integer, " +
                FairTable.Cols.END_DATE + " integer, " +
                FairTable.Cols.LOGO_URL + ", " +
                FairTable.Cols.FLOORPLAN_URL + ", " +
                FairTable.Cols.SHOW_GENERAL_INFO + " integer, " +
                FairTable.Cols.SHOW_EXHIBITOR_LIST + " integer, " +
                FairTable.Cols.SHOW_SCHEDULE + " integer, " +
                FairTable.Cols.SHOW_NEWS + " integer, " +
                FairTable.Cols.SHOW_SPEAKERS + " integer, " +
                FairTable.Cols.SHOW_FLOORPLAN + " integer)");

        db.execSQL("create table " + ExhibitorTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                ExhibitorTable.Cols.EXHIBITOR_ID + " integer, " +
                ExhibitorTable.Cols.EXHIBITOR_FAIR_ID + " integer, " +
                ExhibitorTable.Cols.NAME + ", " +
                ExhibitorTable.Cols.EXHIBITOR_LOGO + ", " +
                ExhibitorTable.Cols.STAND + ", " +
                ExhibitorTable.Cols.WEB_URL + ", " +
                ExhibitorTable.Cols.PROFILE + ", " +
                ExhibitorTable.Cols.FAVOURITE + " boolean)");

        db.execSQL("create table " + EventTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                EventTable.Cols.EVENT_ID + " integer, " +
                EventTable.Cols.EVENT_NAME + ", " +
                EventTable.Cols.EVENT_PHOTO + ", " +
                EventTable.Cols.EVENT_DESCRIPTION + ", " +
                EventTable.Cols.EVENT_LOCATION + ", " +
                EventTable.Cols.EVENT_DATE + ", " +
                EventTable.Cols.EVENT_TIME + ", " +
                EventTable.Cols.EVENT_END_DATE + ", " +
                EventTable.Cols.EVENT_END_TIME + ")");

        db.execSQL("create table " + SpeakerTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                SpeakerTable.Cols.SPEAKER_ID + " integer, " +
                SpeakerTable.Cols.SPEAKER_NAME + ", " +
                SpeakerTable.Cols.SPEAKER_JOBTITLE + ", " +
                SpeakerTable.Cols.SPEAKER_COMPANY + ", " +
                SpeakerTable.Cols.SPEAKER_PHOTO + ", " +
                SpeakerTable.Cols.EMAIL_ADDRESS + ", " +
                SpeakerTable.Cols.SPEAKER_BIO + ")");

        db.execSQL("create table " + NewsTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                NewsTable.Cols.NEWS_ID + " integer, " +
                NewsTable.Cols.HEADLINE + ", " +
                NewsTable.Cols.ARTICLE_TEXT + ", " +
                NewsTable.Cols.PUBLICATION_DATE + ", " +
                NewsTable.Cols.PICTURE_URL + ")");

        db.execSQL("create table " + ExhibitorFavouritesTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                ExhibitorFavouritesTable.Cols.EXHIBITOR_FAIR_ID + " integer, " +
                ExhibitorFavouritesTable.Cols.USER_FAIR_ID + " integer)");

        db.execSQL("create table " + EventFavouritesTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                EventFavouritesTable.Cols.EVENT_ID + " integer, " +
                EventFavouritesTable.Cols.USER_FAIR_ID + " integer)");

        db.execSQL("create table " + UserPostTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                UserPostTable.Cols.POST_ID + " integer, " +
                UserPostTable.Cols.FORENAME + ", " +
                UserPostTable.Cols.SURNAME + ", " +
                UserPostTable.Cols.POST_DATE + " integer, " +
                UserPostTable.Cols.POST_TEXT + ", " +
                UserPostTable.Cols.IMAGE_URL + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
