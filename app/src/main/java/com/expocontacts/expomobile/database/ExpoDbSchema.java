package com.expocontacts.expomobile.database;

public class ExpoDbSchema {

    public static final class FairTable {
        public static final String NAME = "fair";

        public static final class Cols {
            public static final String FAIR_ID = "fairId";
            public static final String NAME = "fairName";
            public static final String GENERAL_INFO = "generalInfo";
            public static final String UPDATE_REQUIRED = "updateRequired";
            public static final String START_DATE = "startDate";
            public static final String END_DATE = "endDate";
            public static final String LOGO_URL = "logoURL";
            public static final String FLOORPLAN_URL = "floorplanURL";
            public static final String SHOW_GENERAL_INFO = "showGeneralInfo";
            public static final String SHOW_EXHIBITOR_LIST = "showExhibitorList";
            public static final String SHOW_SCHEDULE = "showSchedule";
            public static final String SHOW_NEWS = "showNews";
            public static final String SHOW_SPEAKERS = "showSpeakers";
            public static final String SHOW_FLOORPLAN = "showFloorplan";
            public static final String REQUIRES_SYNC = "requiresSync";
        }
    }

    public static final class ExhibitorTable {
        public static final String NAME = "exhibitors";

        public static final class Cols {
            public static final String EXHIBITOR_ID = "exhibitorId";
            public static final String EXHIBITOR_FAIR_ID = "exhibitorFairId";
            public static final String NAME = "exhibitorName";
            public static final String EXHIBITOR_LOGO = "exhibitorLogo";
            public static final String STAND = "stand";
            public static final String WEB_URL = "webURL";
            public static final String PROFILE = "profile";
            public static final String FAVOURITE = "favourite";
        }
    }

    public static final class EventTable {
        public static final String NAME = "events";

        public static final class Cols {
            public static final String EVENT_ID = "eventId";
            public static final String EVENT_NAME = "eventName";
            public static final String EVENT_PHOTO = "eventPhoto";
            public static final String EVENT_DESCRIPTION = "eventDescription";
            public static final String EVENT_LOCATION = "eventLocation";
            public static final String EVENT_DATE = "eventDate";
            public static final String EVENT_TIME = "eventTime";
            public static final String EVENT_END_DATE = "eventEndDate";
            public static final String EVENT_END_TIME = "eventEndTime";
        }
    }

    public static final class SpeakerTable {
        public static final String NAME = "speakers";

        public static final class Cols {
            public static final String SPEAKER_ID = "speakerId";
            public static final String SPEAKER_NAME = "speakerName";
            public static final String SPEAKER_JOBTITLE = "speakerJobTitle";
            public static final String SPEAKER_COMPANY = "speakerCompany";
            public static final String SPEAKER_BIO = "speakerBio";
            public static final String SPEAKER_PHOTO = "speakerPhoto";
            public static final String EMAIL_ADDRESS = "emailAddress";
        }
    }

    public static final class NewsTable {
        public static final String NAME = "news";

        public static final class Cols {
            public static final String NEWS_ID = "newsId";
            public static final String HEADLINE = "newsHeadline";
            public static final String ARTICLE_TEXT = "articleText";
            public static final String PUBLICATION_DATE = "articleDate";
            public static final String PICTURE_URL = "pictureURL";
        }
    }

    public static final class ExhibitorFavouritesTable {
        public static final String NAME = "exhibitorFavourites";

        public static final class Cols {
            public static final String EXHIBITOR_FAIR_ID = "exhibitorFairId";
            public static final String USER_FAIR_ID = "userFairId";
        }
    }

    public static final class EventFavouritesTable {
        public static final String NAME = "eventFavourites";

        public static final class Cols {
            public static final String EVENT_ID = "eventId";
            public static final String USER_FAIR_ID = "userFairId";
        }
    }

    public static final class UserPostTable {
        public static final String NAME = "userPosts";

        public static final class Cols {
            public static final String POST_ID = "postId";
            public static final String SURNAME = "surname";
            public static final String FORENAME = "forename";
            public static final String POST_DATE = "postDate";
            public static final String POST_TEXT = "postText";
            public static final String IMAGE_URL = "imageURL";
        }
    }

}
