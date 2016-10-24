package com.expocontacts.expomobile.model_utils;

import android.content.Context;
import android.preference.PreferenceManager;

public class SharedPreferencesUtils {

    private static final String PREF_APP_DATA_VERSION = "AppDataVersion";
    private static final String PREF_USER_ID = "UserId";
    private static final String PREF_USER_FAIR_ID = "UserFairId";
    private static final String PREF_USER_EMAIL_ADDRESS = "UserEmailAddress";
    private static final String PREF_USER_NAME = "UserName";
    private static final String PREF_DB_DIRTY = "DbDirty";

    public static Integer getAppDataVersion(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(PREF_APP_DATA_VERSION, 0);
    }

    public static void setAppDataVersion(Context context, int appDataVersion) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(PREF_APP_DATA_VERSION, appDataVersion)
                .apply();
    }

    public static int getUserId(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(PREF_USER_ID, 0);
    }

    public static void setUserId(Context context, int userId) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(PREF_USER_ID, userId)
                .apply();
    }

    public static int getUserFairId(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(PREF_USER_FAIR_ID, 0);
    }

    public static void setUserFairId(Context context, int userFairId) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(PREF_USER_FAIR_ID, userFairId)
                .apply();
    }

    public static String getUserEmailAddress(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_USER_EMAIL_ADDRESS, "");
    }

    public static void setUserEmailAddress(Context context, String userEmailAddress) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_USER_EMAIL_ADDRESS, userEmailAddress)
                .apply();
    }

    public static String getUserName(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_USER_NAME, "");
    }

    public static void setUserName(Context context, String userName) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_USER_NAME, userName)
                .apply();
    }

    public static boolean getDbDirty(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_DB_DIRTY, false);
    }

    public static void setDbDirty(Context context, boolean dbDirty) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_DB_DIRTY, dbDirty)
                .apply();
    }

}
