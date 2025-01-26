package com.example.calendarprovider;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import java.util.Calendar;
import java.util.Objects;

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

    public void queryCalendar(){
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

    public void updateCalendar(){
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, "my local calendar");

        Uri uriUpdated = ContentUris.withAppendedId(
                CalendarContract.Calendars.CONTENT_URI, calendarId);
        int rows = context.getContentResolver()
                .update(uriUpdated, values, null, null);

        Log.i(LOG_TAG, "업데이트된 열: " + rows);
    }

    public long insertEvent(){
        long startMillis;
        Calendar beginTime=Calendar.getInstance();
        beginTime.set(2025,Calendar.APRIL,2,10,30);
        startMillis=beginTime.getTimeInMillis();

        ContentValues values=new ContentValues();
        values.put(CalendarContract.Events.CALENDAR_ID, calendarId);
        values.put(CalendarContract.Events.ORGANIZER,"organizer@mail.com");
        values.put(CalendarContract.Events.TITLE,"meetingA");
        values.put(CalendarContract.Events.EVENT_LOCATION,"A-room302");
        values.put(CalendarContract.Events.EVENT_TIMEZONE,"Asia/Seoul");
        values.put(CalendarContract.Events.DTSTART,startMillis);
        values.put(CalendarContract.Events.DURATION, "PT1H");
        values.put(CalendarContract.Events.RRULE, "FREQ=WEEKLY;UNTIL=20250702T113000Z");

        Uri uri=context.getContentResolver()
                .insert(CalendarContract.Events.CONTENT_URI,values);

        long eventId=0;
        if(uri!=null){
            eventId=Long.parseLong(Objects.requireNonNull(uri.getLastPathSegment()));
        }

        return eventId;
    }

    public void updateEvent(long eventId){
        ContentValues values=new ContentValues();
        values.put(CalendarContract.Events.EVENT_LOCATION,"A-room304");

        Uri uri=ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI,eventId);
        int row=context.getContentResolver()
                .update(uri,values,null,null);

        Log.i(LOG_TAG, "업데이트된 이벤트 열: " + row);
    }

    public void viewEvent(long eventId){
        Uri uri=ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI,eventId);
        Intent intent=new Intent(Intent.ACTION_VIEW).setData(uri);
        context.startActivity(intent);
    }

    public void viewSpecifiedEvent(){
        long startMillis;
        Calendar beginTime=Calendar.getInstance();
        beginTime.set(2025,Calendar.APRIL,2,10,30);
        startMillis=beginTime.getTimeInMillis();

        Uri.Builder builder=CalendarContract.CONTENT_URI.buildUpon();
        builder.appendPath("time");
        ContentUris.appendId(builder, startMillis);

        Intent intent=new Intent(Intent.ACTION_VIEW)
                .setData(builder.build());
        context.startActivity(intent);
    }

    public long insertAttendee(
            long eventId, String name, String email,
            int relationship, int type, int status
    ){
        ContentValues values=new ContentValues();
        values.put(CalendarContract.Attendees.EVENT_ID,eventId);
        values.put(CalendarContract.Attendees.ATTENDEE_NAME,name);
        values.put(CalendarContract.Attendees.ATTENDEE_EMAIL,email);
        values.put(CalendarContract.Attendees.ATTENDEE_RELATIONSHIP,relationship);
        values.put(CalendarContract.Attendees.ATTENDEE_TYPE,type);
        values.put(CalendarContract.Attendees.ATTENDEE_STATUS,status);

        Uri uri= context.getContentResolver()
                .insert(CalendarContract.Attendees.CONTENT_URI, values);

        long attendeeId=0;
        if(uri!=null){
            attendeeId=Long.parseLong(Objects.requireNonNull(uri.getLastPathSegment()));
        }

        return attendeeId;
    }

    public void updateAttendee(long attendeeId){
        ContentValues values=new ContentValues();
        values.put(CalendarContract.Attendees.ATTENDEE_TYPE, CalendarContract.Attendees.TYPE_REQUIRED);

        Uri uri=ContentUris.withAppendedId(CalendarContract.Attendees.CONTENT_URI,attendeeId);
        int row=context.getContentResolver().update(uri,values,null,null);

        Log.i(LOG_TAG, "업데이트된 참석자 열: " + row);
    }

    public void deleteAttendee(long attendeeId){
        Uri uri=ContentUris.withAppendedId(CalendarContract.Attendees.CONTENT_URI,attendeeId);
        int row=context.getContentResolver().delete(uri,null,null);
        Log.i(LOG_TAG, "삭제된 참석자 열: " + row);
    }

}
