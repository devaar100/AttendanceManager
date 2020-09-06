package aarnav100.developer.attendancemanager.Database.Tables;

/**
 * Created by aarnavjindal on 26/01/18.
 */

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

import aarnav100.developer.attendancemanager.Models.AttendanceDay;

import static aarnav100.developer.attendancemanager.Database.DBUtils.DBConsts.*;

public class TableDates {

    public static final String TABLE_NAME = "dates";
    public static final String COL_DATE_ID = "date_id";
    public static final String COL_DATE = "attendance_date";
    public static final String COL_TYPE = "date_type"; // Holiday , Attendance Taken

    public static final String CMD_CREATE_DATE_TABLE =
            "CREATE TABLE " + TABLE_NAME +
                    LBR +
                    COL_DATE_ID + TYPE_INT + TYPE_PK + TYPE_AI + COMMA +
                    COL_DATE + TYPE_DATE + TYPE_UNIQUE + TYPE_NN + COMMA +
                    COL_TYPE + TYPE_TEXT + TYPE_NN +
                    RBR + SEMCOL;

    public static boolean addDate(SQLiteDatabase db,Date date,String type){
        if(db.isReadOnly())
            return false;
        ContentValues cv = new ContentValues();
        cv.put(COL_DATE,date.toString());
        cv.put(COL_TYPE,type);
        db.insert(TABLE_NAME,null,cv);
        return true;
    }

    public static boolean updateDateType(SQLiteDatabase db, String dateId, String type){
        if(db.isReadOnly())
            return false;
        ContentValues cv = new ContentValues();
        cv.put(COL_TYPE,type);
        String whereClause = COL_DATE_ID + " = ? ";
        db.update( TABLE_NAME, cv, whereClause, new String[]{dateId});
        return true;
    }

    public static Pair<String,String> getDateIdType(SQLiteDatabase db, String date){
        String whereClause = COL_DATE + " = ? ";
        Cursor c = db.query(
                TABLE_NAME,
                new String[]{COL_DATE_ID,COL_TYPE},
                whereClause,
                new String[]{date},
                null,null,null
        );

        c.moveToFirst();
        String id = c.getString(c.getColumnIndex(COL_DATE_ID));
        String type = c.getString(c.getColumnIndex(COL_TYPE));
        c.close();
        return new Pair<>(id,type);
    }

    public static HashMap<Date, String> getDates(SQLiteDatabase db){
        HashMap<Date,String> dates = new HashMap<>();
        Cursor c = db.query(
                TABLE_NAME,
                new String[]{COL_DATE, COL_TYPE},
                null,null,null,null,null
        );

        int dateIndex = c.getColumnIndex(COL_DATE);
        int typeIndex = c.getColumnIndex(COL_TYPE);

        String date,type;
        for(c.moveToFirst();!c.isAfterLast();c.moveToNext()) {
            date = c.getString(dateIndex);
            type = c.getString(typeIndex);
            dates.put(Date.valueOf(date),type);
        }
        c.close();
        return dates;
    }
}
