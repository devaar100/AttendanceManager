package aarnav100.developer.attendancemanager;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import aarnav100.developer.attendancemanager.Database.DBUtils.DatabaseHandler;
import aarnav100.developer.attendancemanager.Database.Tables.TableAttendance;
import aarnav100.developer.attendancemanager.Database.Tables.TableDates;
import aarnav100.developer.attendancemanager.Database.Tables.TableStudents;
import aarnav100.developer.attendancemanager.Generic.EventDecorator;
import aarnav100.developer.attendancemanager.Models.Student;

public class StudentActivity extends AppCompatActivity {
    private MaterialCalendarView mcv;
    private Button markPresent,markAbsent;
    private TextView selectedDate, dateStatus;
    private TextView grdName,grdRelation,stdName,stdContact,stdRollNo;
    private HashMap<Date,String> dates;
    private Student s;
    private TextView tvPresent,tvAbsent,tvPercentage;
    private int absent,present;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        mcv = findViewById(R.id.student_calendar);
        markPresent = findViewById(R.id.mark_present);
        markAbsent = findViewById(R.id.mark_absent);
        selectedDate = findViewById(R.id.selected_date);
        dateStatus = findViewById(R.id.selected_date_status);
        grdName = findViewById(R.id.grd_name);
        grdRelation = findViewById(R.id.grd_rel);
        stdName = findViewById(R.id.std_name);
        stdContact = findViewById(R.id.contact_num);
        stdRollNo = findViewById(R.id.std_rollno);
        String id = getIntent().getStringExtra("id");
        Log.i("TAG" ,id);
        final DatabaseHandler dbhandler = new DatabaseHandler(this);
        s = TableStudents.getStudent(dbhandler.getReadableDatabase(),id);
        stdName.setText(s.getStudentName());
        grdName.setText(s.getGuardianName());
        grdRelation.setText(s.getGuardianRelation());
        stdContact.setText(s.getContactNumber());
        stdRollNo.setText("Roll Number : " + s.getRollNo());
        final SimpleDateFormat sdt = new SimpleDateFormat("dd-MM-yyyy");

        HashMap<Date,String> tempDates = TableDates.getDates(dbhandler.getReadableDatabase());
        dates = new HashMap<>();
        for(Date date:tempDates.keySet())
            if(tempDates.get(date).equals("M"))
                dates.put(date,"M");
        mcv.addDecorator(new EventDecorator(this,tempDates,R.drawable.blue_circle_bg,"M"));

        tempDates = TableAttendance.getStudentAttendances(dbhandler.getReadableDatabase(),id);
        for(Date date:tempDates.keySet()){
            dates.remove(date);
            dates.put(date,tempDates.get(date));
        }

        present=0;
        absent=0;
        for(String type:dates.values()){
            if(type.equals("P"))
                present++;
            else
                absent++;
        }
        tvPresent = findViewById(R.id.tv_present);
        tvAbsent = findViewById(R.id.tv_absent);
        tvPercentage = findViewById(R.id.tv_percentage);
        updateAttendance();

        mcv.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                if (selected) {
                    String type = dates.get(new Date(date.getDate().getTime()));
                    String status=null;
                    if(type == null)
                        status = "Unmarked";
                    else {
                        switch (type) {
                            case "A":
                                status = "Date status : Absent";
                                break;
                            case "P":
                                status = "Date status : Present";
                                break;
                        }
                    }
                    selectedDate.setText("Selected date : " + sdt.format(date.getDate()));
                    dateStatus.setText(status);
                }
            }
        });
        mcv.setSelectedDate(Calendar.getInstance().getTime());
        View.OnClickListener ocl= new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = s.getTableId();
                if(mcv.getSelectedDate()==null)
                    return;
                Date date = new Date(mcv.getSelectedDate().getDate().getTime());
                String strdate = date.toString();
                String type = dates.get(date);
                if(type==null)
                    return;
                String dateId = TableDates.getDateIdType(dbhandler.getReadableDatabase(),strdate).first;
                switch (view.getId()){
                    case R.id.mark_present:
                        if(!type.equals("P")) {
                            if (TableAttendance.addAttendance(dbhandler.getWritableDatabase(), id, dateId, true)) {
                                present++;
                                if(type.equals("A"))
                                    absent--;
                                dates.remove(date);
                                dates.put(date,"P");
                                updateAttendance();
                            } else
                                Toast.makeText(StudentActivity.this, "Error. Please try again", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.mark_absent:
                        if(!type.equals("A")) {
                            if (TableAttendance.addAttendance(dbhandler.getWritableDatabase(), id, dateId, false)) {
                                absent++;
                                if (type.equals("P"))
                                    present--;
                                dates.remove(date);
                                dates.put(date,"A");
                                updateAttendance();
                            } else
                                Toast.makeText(StudentActivity.this, "Error. Please try again", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        };
        markAbsent.setOnClickListener(ocl);
        markPresent.setOnClickListener(ocl);
        stdContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("TAG","Here");
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                String telString = "tel:" + s.getContactNumber();
                callIntent.setData(Uri.parse(telString));
                startActivity(callIntent);
            }
        });
    }

    private void updateAttendance(){
        tvPresent.setText("P : "+present);
        tvAbsent.setText("A : "+absent);
        try {
            String num = String.valueOf(((double) present*100) / (present + absent));
            String perc = "Percentage : " + num + "%";
            tvPercentage.setText(perc);
        } catch (Exception e){
            e.printStackTrace();
            tvPercentage.setText("Percentage : 0.00%");
        }
        mcv.addDecorator(new EventDecorator(this,dates,R.drawable.red_cross_bg,"A"));
        mcv.addDecorator(new EventDecorator(this,dates,R.drawable.green_tick_bg,"P"));
    }
}
