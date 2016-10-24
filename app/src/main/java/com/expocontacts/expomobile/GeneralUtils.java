package com.expocontacts.expomobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import com.expocontacts.expomobile.database.ExpoDbOpenHelper;
import com.expocontacts.expomobile.database.ExpoDbSchema;
import com.expocontacts.expomobile.helpers.BasicImageDownloader;
import com.expocontacts.expomobile.model.EventFavourite;
import com.expocontacts.expomobile.model.Exhibitor;
import com.expocontacts.expomobile.model.ExhibitorFavourite;
import com.expocontacts.expomobile.model.Fair;
import com.expocontacts.expomobile.model.FairEvent;
import com.expocontacts.expomobile.model.NewsArticle;
import com.expocontacts.expomobile.model.Speaker;
import com.expocontacts.expomobile.model.UserPost;
import com.expocontacts.expomobile.model_utils.EventUtils;
import com.expocontacts.expomobile.model_utils.ExhibitorUtils;
import com.expocontacts.expomobile.model_utils.FairUtils;
import com.expocontacts.expomobile.model_utils.NetworkUtils;
import com.expocontacts.expomobile.model_utils.NewsArticleUtils;
import com.expocontacts.expomobile.model_utils.SharedPreferencesUtils;
import com.expocontacts.expomobile.model_utils.SpeakerUtils;
import com.expocontacts.expomobile.model_utils.UserPostUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneralUtils {

    private static final String TAG = "GeneralUtils";

    private static GeneralUtils sGeneralUtils;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private Activity mCallingActivity;
    private boolean mForceRefresh;

    private GeneralUtils(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new ExpoDbOpenHelper(mContext)
                .getWritableDatabase();
    }

    public static GeneralUtils get(Context context) {
        if (sGeneralUtils == null) {
            sGeneralUtils = new GeneralUtils(context);
        }
        return sGeneralUtils;
    }

    public void LoadData(Activity callingActivity, boolean forceRefresh) {
        mCallingActivity = callingActivity;
        mForceRefresh = forceRefresh;

        if (mForceRefresh) {
            Toast.makeText(mContext, "Refreshing data ...", Toast.LENGTH_SHORT);
        }
        new GeneralUtils.GetDataTask().execute();
    }

    public void ClearDB() {
        mDatabase.execSQL("DELETE FROM " + ExpoDbSchema.FairTable.NAME);
        mDatabase.execSQL("DELETE FROM " + ExpoDbSchema.ExhibitorTable.NAME);
        mDatabase.execSQL("DELETE FROM " + ExpoDbSchema.EventTable.NAME);
        mDatabase.execSQL("DELETE FROM " + ExpoDbSchema.SpeakerTable.NAME);
        mDatabase.execSQL("DELETE FROM " + ExpoDbSchema.NewsTable.NAME);
        mDatabase.execSQL("DELETE FROM " + ExpoDbSchema.ExhibitorFavouritesTable.NAME);
        mDatabase.execSQL("DELETE FROM " + ExpoDbSchema.EventFavouritesTable.NAME);
        mDatabase.execSQL("DELETE FROM " + ExpoDbSchema.UserPostTable.NAME);
    }

    private void GetFair(File storagePath) {

        try {

            String result;
            String extension;
            String logoURL;
            String floorplanURL;
            String logoFilename;
            String floorplanFilename;
            FairUtils fairUtils;

            fairUtils = FairUtils.get(mContext);

            Log.i(TAG, "Adding fair");

            result = new NetworkUtils().getUrlString("http://www.expocontacts.com/webservices/exposervice.asmx/getFairInfo?lFairDateId=" + AppSettings.FAIR_DATE_ID);
            Log.i(TAG, "Bytes received: " + result.length());
            Log.i(TAG, "Fetched contents of URL: " + result);
            JSONObject jsonFair = new JSONObject(result);

            Fair fair = new Fair();

            fair.setFairId(jsonFair.getInt("fair_id"));
            fair.setFairName(jsonFair.getString("fair_name"));
            fair.setGeneralInfo(jsonFair.getString("general_info"));


            fair.setUpdateRequired(0);
            fair.setStartDate(jsonFair.getInt("start_date"));
            fair.setEndDate(jsonFair.getInt("end_date"));
            fair.setFairLogoURL(jsonFair.getString("fair_logo_file"));
            fair.setFloorplanURL(jsonFair.getString("floorplan_file"));
            fair.setShowGeneralInfo(jsonFair.getInt("show_general_info"));
            fair.setShowExhibitorList(jsonFair.getInt("show_exhibitor_list"));
            fair.setShowSchedule(jsonFair.getInt("show_schedule"));
            fair.setShowNews(jsonFair.getInt("show_news"));
            fair.setShowSpeakers(jsonFair.getInt("show_speakers"));
            fair.setShowFloorplan(jsonFair.getInt("show_floorplan"));

            logoURL = jsonFair.getString("fair_logo_file");

            if ((logoURL != null) && (logoURL.length() > 0)) {

                extension  = logoURL.substring(logoURL.lastIndexOf("."));

                logoFilename = storagePath + "/fair" + fair.getFairId() + extension;

                fair.setFairLogoURL(logoFilename);

                //
                //  Save the image in the local cache ...
                //

                URL url = new URL (logoURL);
                InputStream input = url.openStream();
                try {

                    OutputStream output = new FileOutputStream(logoFilename);
                    try {
                        byte[] buffer = new byte[1024];
                        int bytesRead = 0;
                        while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
                            output.write(buffer, 0, bytesRead);
                        }
                    } finally {
                        output.close();
                    }
                } finally {
                    input.close();
                }
            } else {
                fair.setFairLogoURL("");
            }

            floorplanURL = jsonFair.getString("floorplan_file");

            if ((floorplanURL != null) && (floorplanURL.length() > 0)) {

                extension  = floorplanURL.substring(floorplanURL.lastIndexOf("."));

                floorplanFilename = storagePath + "/floorplan" + fair.getFairId() + extension;

                fair.setFloorplanURL(floorplanFilename);

                //
                //  Save the image in the local cache ...
                //

                URL url = new URL (floorplanURL);
                InputStream input = url.openStream();
                try {

                    OutputStream output = new FileOutputStream(floorplanFilename);
                    try {
                        byte[] buffer = new byte[1024];
                        int bytesRead = 0;
                        while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
                            output.write(buffer, 0, bytesRead);
                        }
                    } finally {
                        output.close();
                    }
                } finally {
                    input.close();
                }
            } else {
                fair.setFloorplanURL("");
            }

            fairUtils.addFair(fair);

            Log.i(TAG, "Added fair");

        } catch (JSONException je) {
            Log.e(TAG, "Error parsing JSON: " + je);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch URL: " + ioe);
        }        String result;

    }

    private void GetExhibitors(final File storagePath) {

        try {

            String result;
            String extension;
            String logoURL;
            String logoFilename;
            final String logoPath;
            final String name;
            int i;

            ExhibitorUtils exUtils;

            exUtils = ExhibitorUtils.get(mContext);

            Log.i(TAG, "Adding exhibitors");

            result = new NetworkUtils().getUrlString("http://www.expocontacts.com/webservices/exposervice.asmx/getFairExhibitors?lFairDateId=" + AppSettings.FAIR_DATE_ID);
            Log.i(TAG, "Bytes received: " + result.length());
            Log.i(TAG, "Fetched contents of URL: " + result);
            JSONArray jsonExhibitor = new JSONArray(result);

            for (i = 0; i < jsonExhibitor.length(); i++) {

                JSONObject exhibitorJsonObject = jsonExhibitor.getJSONObject(i);

                final Exhibitor ex = new Exhibitor();

                ex.setExhibitorId(exhibitorJsonObject.getInt("id"));
                ex.setExhibitorFairId(exhibitorJsonObject.getInt("exhibitor_fair_id"));
                ex.setExhibitorName(exhibitorJsonObject.getString("exhibitor_name"));
                ex.setStand(exhibitorJsonObject.getString("stand_no"));
                ex.setWebsite(exhibitorJsonObject.getString("web_url"));
                ex.setProfile(exhibitorJsonObject.getString("exhibitor_profile"));

                logoURL = exhibitorJsonObject.getString("exhibitor_logo_file");
                ex.setExhibitorLogo(storagePath + "/logo" + ex.getExhibitorId() + ".jpeg");

//                if ((logoURL != null) && (logoURL.length() > 0)) {
//
//                    extension  = logoURL.substring(logoURL.lastIndexOf("."));
//
//                    logoFilename = storagePath + "/logo" + ex.getExhibitorId() + extension;
//
//                    ex.setExhibitorLogo(logoFilename);
//
//                    //
//                    //  Save the image in the local cache ...
//                    //
//
//                    URL url = new URL (logoURL);
//                    InputStream input = url.openStream();
//                    try {
//
//                        OutputStream output = new FileOutputStream(logoFilename);
//                        try {
//                            byte[] buffer = new byte[1024];
//                            int bytesRead = 0;
//                            while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
//                                output.write(buffer, 0, bytesRead);
//                            }
//                        } finally {
//                            output.close();
//                        }
//                    } finally {
//                        input.close();
//                    }
//                } else {
//                    ex.setExhibitorLogo("");
//                }

               if ((logoURL != null) && (logoURL.length() > 0)) {

                    final BasicImageDownloader downloader = new BasicImageDownloader(new BasicImageDownloader.OnImageLoaderListener() {
                        @Override
                        public void onError(BasicImageDownloader.ImageError error) {
                            Log.i(TAG, "Error code " + error.getErrorCode() + ": " + error.getMessage());
                            error.printStackTrace();
                        }

                        @Override
                        public void onProgressChange(int percent) {
                        }

                        @Override
                        public void onComplete(Bitmap result) {
                        /* save the image - I'm gonna use JPEG */
                            final Bitmap.CompressFormat mFormat = Bitmap.CompressFormat.JPEG;
                        /* don't forget to include the extension into the file name */
                            final File myImageFile = new File(storagePath + "/logo" + ex.getExhibitorId() + "." + mFormat.name().toLowerCase());
                            BasicImageDownloader.writeToDisk(myImageFile, result, new BasicImageDownloader.OnBitmapSaveListener() {
                                @Override
                                public void onBitmapSaved() {
                                    Log.i(TAG, "Image saved as: " + myImageFile.getAbsolutePath());
                                }

                                @Override
                                public void onBitmapSaveError(BasicImageDownloader.ImageError error) {
                                    Log.i(TAG, "Error code " + error.getErrorCode() + ": " + error.getMessage());
                                    error.printStackTrace();
                                }


                            }, mFormat, false);

                        }
                    });
                    downloader.download(logoURL, true);
                }

                exUtils.addExhibitor(ex);
            }

            Log.i(TAG, "Added " + i + " exhibitors");
        } catch (JSONException je) {
            Log.e(TAG, "Error parsing JSON: " + je);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch URL: " + ioe);
        }
    }

    private void GetNewsArticles() {

        try {

            String result;
            int i;
            NewsArticleUtils newsUtils;

            newsUtils = NewsArticleUtils.get(mContext);

            Log.i(TAG, "Adding news");

            result = new NetworkUtils().getUrlString("http://www.expocontacts.com/webservices/exposervice.asmx/getFairNews?lFairDateId=" + AppSettings.FAIR_DATE_ID);
            Log.i(TAG, "Bytes received: " + result.length());
            Log.i(TAG, "Fetched contents of URL: " + result);
            JSONArray jsonNews = new JSONArray(result);

            for (i = 0; i < jsonNews.length(); i++) {

                JSONObject newsArticleJsonObject = jsonNews.getJSONObject(i);

                NewsArticle newsArticle = new NewsArticle();

                newsArticle.setNewsId(newsArticleJsonObject.getInt("id"));
                newsArticle.setHeadline(newsArticleJsonObject.getString("headline"));
                newsArticle.setPictureURL(newsArticleJsonObject.getString("picture_url"));
                newsArticle.setArticleText(newsArticleJsonObject.getString("article_text"));
                newsArticle.setPublicationDate(newsArticleJsonObject.getLong("publication_date"));

                newsUtils.addNewsArticle(newsArticle);
            }

            Log.i(TAG, "Added " + i + " news articles");

        } catch (JSONException je) {
            Log.e(TAG, "Error parsing JSON: " + je);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch URL: " + ioe);
        }
    }

    private void GetFairEvents(File storagePath) {

        try {
            String result;
            String pictureURL;
            String pictureFilename;
            String extension;
            int i;

            EventUtils eventUtils;

            eventUtils = EventUtils.get(mContext);

            Log.i(TAG, "Adding events");

            result = new NetworkUtils().getUrlString("http://www.expocontacts.com/webservices/exposervice.asmx/getFairEvents?lFairDateId=" + AppSettings.FAIR_DATE_ID);
            Log.i(TAG, "Bytes received: " + result.length());
            Log.i(TAG, "Fetched contents of URL: " + result);
            JSONArray jsonEvents = new JSONArray(result);

            for (i = 0; i < jsonEvents.length(); i++) {

                JSONObject eventJsonObject = jsonEvents.getJSONObject(i);

                FairEvent ev = new FairEvent();

                ev.setEventId(eventJsonObject.getInt("event_id"));
                ev.setEventName(eventJsonObject.getString("event_name"));
                ev.setEventDescription(eventJsonObject.getString("event_description"));
                ev.setEventLocation(eventJsonObject.getString("event_location"));
                ev.setEventDate(eventJsonObject.getString("event_date"));
                ev.setEventTime(eventJsonObject.getString("event_time"));
                ev.setEventEndDate(eventJsonObject.getString("event_end_date"));
                ev.setEventEndTime(eventJsonObject.getString("event_end_time"));

                pictureURL = eventJsonObject.getString("event_picture");

                if ((pictureURL != null) && (pictureURL.length() > 0)) {

                    extension  = pictureURL.substring(pictureURL.lastIndexOf("."));

                    pictureFilename = storagePath + "/event" + ev.getFairEventId() + extension;

                    ev.setEventPhoto(pictureFilename);

                    //
                    //  Save the image in the local cache ...
                    //

                    URL url = new URL (pictureURL);
                    InputStream input = url.openStream();
                    try {

                        OutputStream output = new FileOutputStream(pictureFilename);
                        try {
                            byte[] buffer = new byte[1024];
                            int bytesRead = 0;
                            while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
                                output.write(buffer, 0, bytesRead);
                            }
                        } finally {
                            output.close();
                        }
                    } finally {
                        input.close();
                    }
                } else {
                    ev.setEventPhoto("");
                }

                eventUtils.addEvent(ev);
            }

            Log.i(TAG, "Added " + i + " events");

        } catch (JSONException je) {
            Log.e(TAG, "Error parsing JSON: " + je);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch URL: " + ioe);
        }
    }

    private void GetSpeakers(File storagePath) {

        try {
            String result;
            String logoURL;
            String logoFilename;
            String extension;
            int i;
            SpeakerUtils speakerUtils;

            speakerUtils = SpeakerUtils.get(mContext);

            //
            // Get speaker
            //

            Log.i(TAG, "Adding speaker");

            result = new NetworkUtils().getUrlString("http://www.expocontacts.com/webservices/exposervice.asmx/getFairSpeakers?lFairDateId=" + AppSettings.FAIR_DATE_ID);
            Log.i(TAG, "Bytes received: " + result.length());
            Log.i(TAG, "Fetched contents of URL: " + result);
            JSONArray jsonSpeakers = new JSONArray(result);

            for (i = 0; i < jsonSpeakers.length(); i++) {

                JSONObject speakerJsonObject = jsonSpeakers.getJSONObject(i);

                Speaker spk = new Speaker();

                spk.setSpeakerId(speakerJsonObject.getInt("speaker_id"));
                spk.setSpeakerName(speakerJsonObject.getString("speaker_name"));
                spk.setJobTitle(speakerJsonObject.getString("speaker_job_title"));
                spk.setSpeakerCompany(speakerJsonObject.getString("speaker_company"));
                spk.setEmailAddress(speakerJsonObject.getString("email_address"));
                spk.setSpeakerBio(speakerJsonObject.getString("speaker_bio"));

                logoURL = speakerJsonObject.getString("picture_url");

                if ((logoURL != null) && (logoURL.length() > 0)) {

                    extension  = logoURL.substring(logoURL.lastIndexOf("."));

                    logoFilename = storagePath + "/speaker" + spk.getSpeakerId() + extension;
                    Log.i(TAG, "Adding speaker picture with filename: " + logoFilename);

                    spk.setSpeakerPhoto(logoFilename);

                    //
                    //  Save the image in the local cache ...
                    //

                    URL url = new URL (logoURL);
                    InputStream input = url.openStream();
                    try {

                        OutputStream output = new FileOutputStream(logoFilename);
                        try {
                            byte[] buffer = new byte[1024];
                            int bytesRead = 0;
                            while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
                                output.write(buffer, 0, bytesRead);
                            }
                        } finally {
                            output.close();
                        }
                    } finally {
                        input.close();
                    }
                } else {
                    spk.setSpeakerPhoto("");
                }

                speakerUtils.addSpeaker(spk);
            }

            Log.i(TAG, "Added " + i + " speakers");
        } catch (JSONException je) {
            Log.e(TAG, "Error parsing JSON: " + je);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch URL: " + ioe);
        }
    }

    private void GetExhibitorFavourites(int userFairId) {

        try {

            String result;
            int i;
            ExhibitorUtils exhibitorUtils;

            exhibitorUtils = ExhibitorUtils.get(mContext);

            Log.i(TAG, "Adding exhibitor favourites");

            result = new NetworkUtils().getUrlString("http://www.expocontacts.com/webservices/exposervice.asmx/getExhibitorFavourites?lUserFairId=" + userFairId);
            Log.i(TAG, "Bytes received: " + result.length());
            Log.i(TAG, "Fetched contents of URL: " + result);
            JSONArray jsonResult = new JSONArray(result);

            for (i = 0; i < jsonResult.length(); i++) {

                JSONObject jsonFavourite = jsonResult.getJSONObject(i);

                ExhibitorFavourite exhibitorFavourite = new ExhibitorFavourite();

                exhibitorFavourite.setExhibitorFairId(jsonFavourite.getInt("exhibitor_fair_id"));
                exhibitorFavourite.setUserFairId(jsonFavourite.getInt("user_fair_id"));

                exhibitorUtils.addExhibitorFavourite(exhibitorFavourite);
            }

            Log.i(TAG, "Added " + i + " exhibitor favourites");

        } catch (JSONException je) {
            Log.e(TAG, "Error parsing JSON: " + je);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch URL: " + ioe);
        }
    }

    private void GetEventFavourites(int userFairId) {

        try {

            String result;
            int i;
            EventUtils eventUtils;

            eventUtils = EventUtils.get(mContext);

            Log.i(TAG, "Adding event favourites");

            result = new NetworkUtils().getUrlString("http://www.expocontacts.com/webservices/exposervice.asmx/getEventFavourites?lUserFairId=" + userFairId);
            Log.i(TAG, "Bytes received: " + result.length());
            Log.i(TAG, "Fetched contents of URL: " + result);
            JSONArray jsonResult = new JSONArray(result);

            for (i = 0; i < jsonResult.length(); i++) {

                JSONObject jsonFavourite = jsonResult.getJSONObject(i);

                EventFavourite eventFavourite = new EventFavourite();

                eventFavourite.setEventId(jsonFavourite.getInt("fair_event_id"));
                eventFavourite.setUserFairId(jsonFavourite.getInt("user_fair_id"));

                eventUtils.addEventFavourite(eventFavourite);
            }

            Log.i(TAG, "Added " + i + " event favourites");

        } catch (JSONException je) {
            Log.e(TAG, "Error parsing JSON: " + je);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch URL: " + ioe);
        }
    }

    private void GetUserPosts(final File storagePath) {

        try {

            String result;
            String imageURL;
            int i;
            UserPostUtils userpostUtils;

            userpostUtils = UserPostUtils.get(mContext);

            Log.i(TAG, "Adding user posts");

            result = new NetworkUtils().getUrlString("http://www.expocontacts.com/webservices/exposervice.asmx/getFairUserPosts?lFairDateId=" + AppSettings.FAIR_DATE_ID);
            Log.i(TAG, "Bytes received: " + result.length());
            Log.i(TAG, "Fetched contents of URL: " + result);
            JSONArray jsonResult = new JSONArray(result);

            for (i = 0; i < jsonResult.length(); i++) {

                JSONObject jsonPost = jsonResult.getJSONObject(i);

                final UserPost userPost = new UserPost();

                userPost.setPostId(jsonPost.getInt("id"));
                userPost.setForename(jsonPost.getString("forename"));
                userPost.setSurname(jsonPost.getString("surname"));
                userPost.setPostDate(jsonPost.getInt("post_date"));
                userPost.setPostText(jsonPost.getString("post_text"));

                if (jsonPost.getString("image_url") != "") {
                    userPost.setImageURL(storagePath + "/usr" + userPost.getPostId() + ".jpeg");
                } else {
                    userPost.setImageURL("");
                }

                imageURL = jsonPost.getString("image_url");

                if ((imageURL != null) && (imageURL.length() > 0)) {

                    final BasicImageDownloader downloader = new BasicImageDownloader(new BasicImageDownloader.OnImageLoaderListener() {
                        @Override
                        public void onError(BasicImageDownloader.ImageError error) {
                            Log.i(TAG, "Error code " + error.getErrorCode() + ": " + error.getMessage());
                            error.printStackTrace();
                        }

                        @Override
                        public void onProgressChange(int percent) {
                        }

                        @Override
                        public void onComplete(Bitmap result) {
                        /* save the image - I'm gonna use JPEG */
                            final Bitmap.CompressFormat mFormat = Bitmap.CompressFormat.JPEG;
                        /* don't forget to include the extension into the file name */
                            final File myImageFile = new File(storagePath + "/usr" + userPost.getPostId() + "." + mFormat.name().toLowerCase());
                            BasicImageDownloader.writeToDisk(myImageFile, result, new BasicImageDownloader.OnBitmapSaveListener() {
                                @Override
                                public void onBitmapSaved() {
                                    Log.i(TAG, "Image saved as: " + myImageFile.getAbsolutePath());
                                }

                                @Override
                                public void onBitmapSaveError(BasicImageDownloader.ImageError error) {
                                    Log.i(TAG, "Error code " + error.getErrorCode() + ": " + error.getMessage());
                                    error.printStackTrace();
                                }


                            }, mFormat, false);

                        }
                    });
                    downloader.download(imageURL, true);
                }

                userpostUtils.addUserPost(userPost);
            }

            Log.i(TAG, "Added " + i + " user posts");

        } catch (JSONException je) {
            Log.e(TAG, "Error parsing JSON: " + je);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch URL: " + ioe);
        }
    }

    private class GetDataTask extends AsyncTask<Void, Void, Void> {

        String errorMessage = "";

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String result;
                File storagePath = mContext.getCacheDir();

                //
                //  Get the current app data version from expocontacts. If the data we have is up to date then we don't need to do anything
                //

                result = new NetworkUtils().getUrlString("http://www.expocontacts.com/webservices/exposervice.asmx/getAppDataVersion?lFairDateId=" + AppSettings.FAIR_DATE_ID);

                JSONObject jsonVersion = new JSONObject(result);

                Log.i(TAG, "Data version (local): " + SharedPreferencesUtils.getAppDataVersion(mContext));
                Log.i(TAG, "Data version (server): " + jsonVersion.getInt("data_version"));

                if ((SharedPreferencesUtils.getAppDataVersion(mContext) < jsonVersion.getInt("data_version")) || mForceRefresh) {

                    ClearDB();

                    GetFair(storagePath);

                    GetExhibitors(storagePath);

                    GetNewsArticles();

                    GetFairEvents(storagePath);

                    GetSpeakers(storagePath);

                    GetUserPosts(storagePath);

                    if (SharedPreferencesUtils.getUserFairId(mContext) != 0) {
                        GetExhibitorFavourites(SharedPreferencesUtils.getUserFairId(mContext));
                        GetEventFavourites(SharedPreferencesUtils.getUserFairId(mContext));
                    }

                    //
                    //  Update the local data version
                    //

                    SharedPreferencesUtils.setAppDataVersion(mContext, jsonVersion.getInt("data_version"));

                } else {
                    Log.i(TAG, "No need to refresh data");
                }

            } catch (JSONException je) {
                Log.e(TAG, "Error parsing JSON: " + je);
            } catch (IOException ioe) {
                Log.e(TAG, "Failed to fetch URL: " + ioe);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {

            Intent i = new Intent(mContext, MainActivity.class);

            if (!errorMessage.equals("")) {
                Toast.makeText(mContext, errorMessage, Toast.LENGTH_SHORT);
            }

            mCallingActivity.startActivity(i);
            mCallingActivity.finish();
        }
    }

    public static int calculateInSampleSize( BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodeSampledBitmapFromResource(String imageFile, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile, options);
        //BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imageFile, options);
    }

    public static String stripHtml(String html) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            return Html.fromHtml(html).toString();
        }
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
}
