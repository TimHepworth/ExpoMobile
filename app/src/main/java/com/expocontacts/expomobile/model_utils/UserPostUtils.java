package com.expocontacts.expomobile.model_utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.expocontacts.expomobile.database.ExpoDbOpenHelper;
import com.expocontacts.expomobile.database.UserPostCursorWrapper;
import com.expocontacts.expomobile.model.UserPost;
import com.expocontacts.expomobile.database.ExpoDbSchema.UserPostTable;

import java.util.ArrayList;
import java.util.List;

public class UserPostUtils {

    private static final String TAG = "UserPostUtils";

    private static UserPostUtils sUserPostUtils;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private Activity mCallingActivity;

    private UserPostUtils(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new ExpoDbOpenHelper(mContext)
                .getWritableDatabase();
    }

    public static UserPostUtils get(Context context) {
        if (sUserPostUtils == null) {
            sUserPostUtils = new UserPostUtils(context);
        }
        return sUserPostUtils;
    }

    public List<UserPost> getUserPosts() {
        List<UserPost> userPosts = new ArrayList<>();

        UserPostCursorWrapper cursor = queryUserPosts(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                userPosts.add(cursor.getUserPost());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return userPosts;
    }

    public void addUserPost(UserPost up) {
        ContentValues values = getContentValues(up);
        mDatabase.insert(UserPostTable.NAME, null, values);
    }

    public UserPost getUserPost(int id) {
        UserPostCursorWrapper cursor = queryUserPosts(
                UserPostTable.Cols.POST_ID + " = ?",
                new String[] { Integer.toString(id) }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getUserPost();
        } finally {
            cursor.close();
        }
    }

    private static ContentValues getContentValues(UserPost UserPost) {
        ContentValues values = new ContentValues();
        values.put(UserPostTable.Cols.POST_ID, UserPost.getPostId().toString());
        values.put(UserPostTable.Cols.FORENAME, UserPost.getForename());
        values.put(UserPostTable.Cols.SURNAME, UserPost.getSurname());
        values.put(UserPostTable.Cols.POST_DATE, UserPost.getPostDate());
        values.put(UserPostTable.Cols.POST_TEXT, UserPost.getPostText());
        values.put(UserPostTable.Cols.IMAGE_URL, UserPost.getImageURL());

        return values;
    }

    private UserPostCursorWrapper queryUserPosts(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                UserPostTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new UserPostCursorWrapper(cursor);
    }
}