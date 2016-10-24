package com.expocontacts.expomobile.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.expocontacts.expomobile.model.UserPost;

public class UserPostCursorWrapper extends CursorWrapper {

    public UserPostCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public UserPost getUserPost() {

        Integer userPostId = getInt(getColumnIndex(ExpoDbSchema.UserPostTable.Cols.POST_ID));
        String forename = getString(getColumnIndex(ExpoDbSchema.UserPostTable.Cols.FORENAME));
        String surname = getString(getColumnIndex(ExpoDbSchema.UserPostTable.Cols.SURNAME));
        String postText = getString(getColumnIndex(ExpoDbSchema.UserPostTable.Cols.POST_TEXT));
        Integer postDate = getInt(getColumnIndex(ExpoDbSchema.UserPostTable.Cols.POST_DATE));
        String imageURL = getString(getColumnIndex(ExpoDbSchema.UserPostTable.Cols.IMAGE_URL));

        UserPost userPost = new UserPost();
        userPost.setPostId(userPostId);
        userPost.setForename(forename);
        userPost.setSurname(surname);
        userPost.setPostText(postText);
        userPost.setPostDate(postDate);
        userPost.setImageURL(imageURL);

        return userPost;
    }
}