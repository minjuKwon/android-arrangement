package com.example.contentprovider_user;

import android.net.Uri;
import android.provider.BaseColumns;

public final class DataContract {

    public static final String AUTHORITY="com.example.content-provider";
    public static final String TABLE_JAVA="java_notes";
    public static final String TABLE_KOTLIN="kotlin_notes";
    public static final Uri BASE_CONTENT_URI=Uri.parse("content://"+AUTHORITY);

    public static final class JavaNotesEntry implements BaseColumns {
        static final Uri CONTENT_URI=Uri.withAppendedPath(BASE_CONTENT_URI,TABLE_JAVA);
        static final String COLUMN_TITLE="title";
        static final String COLUMN_CONTENT="content";
    }

    public static final class KotlinNotesEntry implements BaseColumns{
        static final Uri CONTENT_URI=Uri.withAppendedPath(BASE_CONTENT_URI,TABLE_KOTLIN);
        static final String COLUMN_TITLE="title";
        static final String COLUMN_CONTENT="content";
    }

}
