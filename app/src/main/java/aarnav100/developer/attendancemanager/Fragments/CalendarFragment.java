package aarnav100.developer.attendancemanager.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import aarnav100.developer.attendancemanager.AttendanceActivity;
import aarnav100.developer.attendancemanager.Database.DBUtils.DatabaseHandler;
import aarnav100.developer.attendancemanager.Database.Tables.TableDates;
import aarnav100.developer.attendancemanager.Generic.EventDecorator;
import aarnav100.developer.attendancemanager.R;

public class CalendarFragment extends Fragment {
    private MaterialCalendarView attendanceCalendar;
    private HashMap<Date,String> dates;
    private Button attendaceBtn;
    private TextView selectedDate,dateStatus;
    private SimpleDateFormat sdt;
    private DatabaseHandler dbhandler;

    public CalendarFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_attendance, container, false);
        final Context mContext = getContext();
        dbhandler = new DatabaseHandler(mContext);

        attendanceCalendar = v.findViewById(R.id.attendance_calendar);
        attendaceBtn = v.findViewById(R.id.view_attendance);
        selectedDate = v.findViewById(R.id.selected_date);
        dateStatus = v.findViewById(R.id.selected_date_status);

        sdt = new SimpleDateFormat("dd-MM-yyyy");
        attendanceCalendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                if (selected) {
                    String type = dates.get(new Date(date.getDate().getTime()));
                    selectedDate.setText("Selected date : " + sdt.format(date.getDate()));
                    if (type == null) {
                        dateStatus.setText("Date status : Not in range");
                        attendaceBtn.setText("Can't edit");
                        attendaceBtn.setEnabled(false);
                    } else {
                        attendaceBtn.setEnabled(true);
                        switch (type) {
                            case "H":
                                dateStatus.setText("Date status : Holiday");
                                attendaceBtn.setText("Edit attendance");
                                break;
                            case "M":
                                dateStatus.setText("Date status : Marked");
                                attendaceBtn.setText("Edit attendance");
                                break;
                            case "U":
                                dateStatus.setText("Date status : Unmarked");
                                attendaceBtn.setText("Mark attendance");
                                break;
                        }
                    }
                }
            }
        });
        attendaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext,AttendanceActivity.class);
                Date date = new Date(attendanceCalendar.getSelectedDate().getDate().getTime());
                Log.i("TAG",date.toString());
                i.putExtra("date",date.toString());
                startActivity(i);
            }
        });
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        dates = TableDates.getDates(dbhandler.getReadableDatabase());
        attendanceCalendar.setSelectedDate(Calendar.getInstance().getTime());
        attendanceCalendar.addDecorator(new EventDecorator(getContext(),dates,R.drawable.red_cross_bg,"H"));
        attendanceCalendar.addDecorator(new EventDecorator(getContext(),dates,R.drawable.green_tick_bg,"M"));
        attendanceCalendar.addDecorator(new EventDecorator(getContext(),dates,R.drawable.blue_circle_bg,"U"));
    }
}
