package com.expocontacts.expomobile.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.expocontacts.expomobile.model.NewsArticle;
import com.expocontacts.expomobile.database.ExpoDbSchema.NewsTable;

/**
 * Created by Tim on 22/09/2016.
 */

public class NewsArticleCursorWrapper extends CursorWrapper {

    public NewsArticleCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public NewsArticle getNewsArticle() {

        int newsId = getInt(getColumnIndex(NewsTable.Cols.NEWS_ID));
        String headline = getString(getColumnIndex(NewsTable.Cols.HEADLINE));
        long publicationDate = getLong(getColumnIndex(NewsTable.Cols.PUBLICATION_DATE));
        String articleText = getString(getColumnIndex(NewsTable.Cols.ARTICLE_TEXT));
        String pictureURL = getString(getColumnIndex(NewsTable.Cols.PICTURE_URL));

        NewsArticle newsArticle = new NewsArticle();
        newsArticle.setNewsId(newsId);
        newsArticle.setHeadline(headline);
        newsArticle.setPublicationDate(publicationDate);
        newsArticle.setArticleText(articleText);
        newsArticle.setPictureURL(pictureURL);

        return newsArticle;
    }
}