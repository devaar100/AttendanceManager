package aarnav100.developer.attendancemanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import aarnav100.developer.attendancemanager.Adapters.AttendeeAdapter;
import aarnav100.developer.attendancemanager.Database.DBUtils.DatabaseHandler;
import aarnav100.developer.attendancemanager.Database.Tables.TableAttendance;
import aarnav100.developer.attendancemanager.Database.Tables.TableDates;
import aarnav100.developer.attendancemanager.Database.Tables.TableStudents;
import aarnav100.developer.attendancemanager.Models.Attendee;

public class AttendanceActivity extends AppCompatActivity {
    private RecyclerView studentList;
    private ArrayList<Attendee> attendees;
    private AttendeeAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attedance);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final DatabaseHandler dbhandler = new DatabaseHandler(this);
        final String date = getIntent().getStringExtra("date");
        Log.i("TAG",date);
        final Pair<String,String> dateIdType = TableDates.getDateIdType(dbhandler.getReadableDatabase(),date);
        studentList = findViewById(R.id.attendee_list);
        Button markAttendance = findViewById(R.id.mark_attendance);
        Button markHoliday = findViewById(R.id.mark_holiday);
        View.OnClickListener ocl = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.mark_attendance:
                        if(TableAttendance.delAttendances(dbhandler.getWritableDatabase(), dateIdType.first)){
                            attendees = adapter.getList();
                            if(!TableAttendance.setAttendance(attendees,dateIdType.first,dbhandler.getWritableDatabase()))
                                Toast.makeText(AttendanceActivity.this, "Error. Please try again", Toast.LENGTH_SHORT).show();
                            else{
                                TableDates.updateDateType(dbhandler.getWritableDatabase(),dateIdType.first,"M");
                                Toast.makeText(AttendanceActivity.this, "Attendance marked successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } else{
                            Toast.makeText(AttendanceActivity.this, "Error. Please try again", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.mark_holiday:
                        if(TableAttendance.delAttendances(dbhandler.getWritableDatabase(), dateIdType.first)){
                            if(TableDates.updateDateType(dbhandler.getWritableDatabase(),dateIdType.first,"H")) {
                                Toast.makeText(AttendanceActivity.this, "Marked as holiday", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else
                                Toast.makeText(AttendanceActivity.this, "Error. Please try again", Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(AttendanceActivity.this, "Error. Please try again", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        };

        markAttendance.setOnClickListener(ocl);
        markHoliday.setOnClickListener(ocl);

        if(dateIdType.second.equals("M")) {
            attendees = TableAttendance.getDateAttendances(dbhandler.getReadableDatabase(), dateIdType.first);
            Collections.sort(attendees,new Attendee.Sort());
        }
        else {
            attendees = TableStudents.getStudentNames(dbhandler.getReadableDatabase());
            Collections.sort(attendees,new Attendee.Sort());
        }

        studentList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AttendeeAdapter(this,attendees);
        studentList.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            onBackPressed();
        }
        return true;
    }
}
