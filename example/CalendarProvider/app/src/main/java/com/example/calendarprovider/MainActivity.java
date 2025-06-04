package com.example.calendarprovider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CALENDAR_PERMISSIONS=100;
    private static final String [] CALENDAR_PERMISSIONS={
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EdgeToEdge.enable(this);

        checkAndRequestCalendarPermissions();
    }

    private void checkAndRequestCalendarPermissions() {
        boolean isReadNotGranted=
                ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CALENDAR)!=
                PackageManager.PERMISSION_GRANTED;
        boolean isWriteNotGranted=
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR)!=
                PackageManager.PERMISSION_GRANTED;

        if( isReadNotGranted || isWriteNotGranted){
            ActivityCompat.requestPermissions(
                    this,
                    CALENDAR_PERMISSIONS,
                    REQUEST_CALENDAR_PERMISSIONS
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode== REQUEST_CALENDAR_PERMISSIONS){
            if( grantResults.length>1&&
                grantResults[0]== PackageManager.PERMISSION_GRANTED&&
                grantResults[1]== PackageManager.PERMISSION_GRANTED
            ){
                useCalendar();
            }else{
                Toast.makeText(this, "권한 허용 필요",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void useCalendar(){
        CalendarManager manager=new CalendarManager(this, 1);

        Thread thread=new Thread(() -> {
            //calendar
            manager.queryAllCalendar();
            manager.queryCalendar();
            manager.updateCalendar();
            manager.queryCalendar();

            //event
            long eventId=manager.insertEvent();
            manager.updateEvent(eventId);
            manager.viewEvent(eventId);
            manager.viewSpecifiedEvent();

            //attendee
            long attendeeAId=manager.insertAttendee(
                    eventId,"A","a@mail.com",
                    CalendarContract.Attendees.RELATIONSHIP_ATTENDEE,
                    CalendarContract.Attendees.TYPE_OPTIONAL,
                    CalendarContract.Attendees.ATTENDEE_STATUS_ACCEPTED
            );
            manager.insertAttendee(
                    eventId,"b","a@mail.com",
                    CalendarContract.Attendees.RELATIONSHIP_ATTENDEE,
                    CalendarContract.Attendees.TYPE_OPTIONAL,
                    CalendarContract.Attendees.ATTENDEE_STATUS_ACCEPTED
            );
            manager.updateAttendee(attendeeAId);
            manager.deleteAttendee(attendeeAId);

            //reminder
            long reminderId=manager.insertReminder(eventId);
            manager.updateReminder(reminderId);

            //instance
            manager.queryInstance(eventId);

            //intent
            manager.insertEventUsingIntent();
        });

        thread.start();

    }

}