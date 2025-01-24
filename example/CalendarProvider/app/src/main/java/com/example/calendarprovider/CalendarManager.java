package com.example.calendarprovider;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

public class CalendarManager {

    private final Context context;
    private final long calendarId;
    private static final String LOG_TAG="CALENDAR";

    private static final String[] CALENDAR_PROJECTION = new String[] {
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 0
            CalendarContract.Calendars.ACCOUNT_TYPE,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME          // 2
    };

    private static final int CALENDAR_PROJECTION_ACCOUNT_NAME_IDX=0;
    private static final int CALENDAR_PROJECTION_ACCOUNT_TYPE_IDX=1;
    private static final int CALENDAR_PROJECTION_DISPLAY_NAME_IDX=2;

    public CalendarManager(Context context, long calendarId){
        this.context=context;
        this.calendarId=calendarId;
    }

    public void calendarQuery(){
        String selection= CalendarContract.Calendars._ID+"=?";
        String [] selectionArgs=new String[]{String.valueOf(calendarId)};

        Cursor cursor=context.getContentResolver().query(
                CalendarContract.Calendars.CONTENT_URI,
                CALENDAR_PROJECTION,
                selection,
                selectionArgs,
                null
        );

        if(cursor!=null){
            cursor.moveToNext();
            Log.i(LOG_TAG, cursor.getString(CALENDAR_PROJECTION_ACCOUNT_NAME_IDX));
            Log.i(LOG_TAG, cursor.getString(CALENDAR_PROJECTION_ACCOUNT_TYPE_IDX));
            Log.i(LOG_TAG, cursor.getString(CALENDAR_PROJECTION_DISPLAY_NAME_IDX));
            cursor.close();
        }
    }

    public void calendarUpdate(){
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, "my local calendar");

        Uri uriUpdated = ContentUris.withAppendedId(
                CalendarContract.Calendars.CONTENT_URI, calendarId);
        int rows = context.getContentResolver()
                .update(uriUpdated, values, null, null);

        Log.i(LOG_TAG, "업데이트된 열: " + rows);
    }

}
