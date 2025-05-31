package com.example.calendarprovider;

import android.os.Bundle;
import android.provider.CalendarContract;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EdgeToEdge.enable(this);

        CalendarManager manager=new CalendarManager(this, 1);
        Thread thread=new Thread(() -> {
            //calendar
            manager.queryCalendar();
            manager.updateCalendar();
            manager.queryCalendar();

            //event
            long eventId=manager.insertEvent();
            manager.updateEvent(eventId);
            manager.viewEvent(eventId);
            manager.viewSpecifiedEvent();

            //attendee
            long attendeeAId=manager
                    .insertAttendee(eventId,"A","a@mail.com",
                            CalendarContract.Attendees.RELATIONSHIP_ATTENDEE,CalendarContract.Attendees.TYPE_OPTIONAL,
                            CalendarContract.Attendees.ATTENDEE_STATUS_ACCEPTED);
            manager.insertAttendee(eventId,"b","a@mail.com",
                    CalendarContract.Attendees.RELATIONSHIP_ATTENDEE,CalendarContract.Attendees.TYPE_OPTIONAL,
                    CalendarContract.Attendees.ATTENDEE_STATUS_ACCEPTED);
            manager.updateAttendee(attendeeAId);
            manager.deleteAttendee(attendeeAId);

            //reminder
            long reminderId=manager.insertReminder(eventId);
            manager.updateReminder(reminderId);

            //instance
            manager.queryInstance(eventId);
        });

        thread.start();

    }

}