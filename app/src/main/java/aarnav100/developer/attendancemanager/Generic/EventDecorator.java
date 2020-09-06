package aarnav100.developer.attendancemanager.Generic;

import android.content.Context;
import android.graphics.Color;
import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;

import aarnav100.developer.attendancemanager.R;

/**
 * Created by aarnavjindal on 25/01/18.
 */
public class EventDecorator implements DayViewDecorator {

    //private final int color;
    private final Context mContext;
    private int drawable_bg;
    private HashMap<Date,String> dates;
    private String val;

    public EventDecorator(Context mContext, HashMap<Date,String> dates,int drawable_bg,String val) {
        this.mContext = mContext;
        this.dates = dates;
        this.drawable_bg = drawable_bg;
        this.val = val;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        String type = dates.get(new Date(day.getDate().getTime()));
        if(type==null)
            return false;

        return type.equals(val);
    }

    @Override
    public void decorate(DayViewFacade view) {
        //view.addSpan(new ForegroundColorSpan(Color.WHITE));
        view.setBackgroundDrawable(mContext.getResources().getDrawable(drawable_bg));
    }
}