package com.expocontacts.expomobile.model_utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.expocontacts.expomobile.database.ExpoDbOpenHelper;
import com.expocontacts.expomobile.database.ExpoDbSchema;
import com.expocontacts.expomobile.database.NewsArticleCursorWrapper;
import com.expocontacts.expomobile.model.NewsArticle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewsArticleUtils {

    private static final String TAG = "NewsArticleUtils";

    private static NewsArticleUtils sNewsArticleUtils;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private Activity mCallingActivity;

    private NewsArticleUtils(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new ExpoDbOpenHelper(mContext)
                .getWritableDatabase();
    }

    public static NewsArticleUtils get(Context context) {
        if (sNewsArticleUtils == null) {
            sNewsArticleUtils = new NewsArticleUtils(context);
        }
        return sNewsArticleUtils;
    }

    public List<NewsArticle> getNewsArticles() {
        List<NewsArticle> newsArticles = new ArrayList<>();

        NewsArticleCursorWrapper cursor = queryNewsArticles(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                newsArticles.add(cursor.getNewsArticle());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return newsArticles;
    }

    public void addNewsArticle(NewsArticle na) {
        ContentValues values = getContentValues(na);
        mDatabase.insert(ExpoDbSchema.NewsTable.NAME, null, values);
    }

    public void ClearDB() {
        mDatabase.execSQL("DELETE FROM " + ExpoDbSchema.ExhibitorTable.NAME);
        mDatabase.execSQL("DELETE FROM " + ExpoDbSchema.SpeakerTable.NAME);
        mDatabase.execSQL("DELETE FROM " + ExpoDbSchema.NewsTable.NAME);
    }

    public NewsArticle getNewsArticle(int id) {
        NewsArticleCursorWrapper cursor = queryNewsArticles(
                ExpoDbSchema.NewsTable.Cols.NEWS_ID + " = ?",
                new String[] { Integer.toString(id) }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getNewsArticle();
        } finally {
            cursor.close();
        }
    }

    private static ContentValues getContentValues(NewsArticle newsArticle) {
        ContentValues values = new ContentValues();
        values.put(ExpoDbSchema.NewsTable.Cols.NEWS_ID, newsArticle.getNewsId().toString());
        values.put(ExpoDbSchema.NewsTable.Cols.HEADLINE, newsArticle.getHeadline());
        values.put(ExpoDbSchema.NewsTable.Cols.ARTICLE_TEXT, newsArticle.getArticleText());
        values.put(ExpoDbSchema.NewsTable.Cols.PUBLICATION_DATE, newsArticle.getPublicationDate());
        values.put(ExpoDbSchema.NewsTable.Cols.PICTURE_URL, newsArticle.getPictureURL());

        return values;
    }

    private NewsArticleCursorWrapper queryNewsArticles(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ExpoDbSchema.NewsTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new NewsArticleCursorWrapper(cursor);
    }
}
