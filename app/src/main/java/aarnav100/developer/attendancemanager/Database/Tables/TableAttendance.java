package aarnav100.developer.attendancemanager.Database.Tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

import aarnav100.developer.attendancemanager.Models.Attendee;

import static aarnav100.developer.attendancemanager.Database.DBUtils.DBConsts.*;

public class TableAttendance {

    public static final String TABLE_NAME = "attendance";

    public static final String COL_DATE_ID = "f_date_id";
    public static final String COL_STUDENT_ID = "f_student_id";
    public static final String COL_PRESENT = "present";


    public static final String CMD_CREATE_ATTENDANCE_TABLE =
            "CREATE TABLE " + TABLE_NAME +
                    LBR +
                    COL_DATE_ID + TYPE_TEXT + TYPE_NN + COMMA +
                    COL_STUDENT_ID + TYPE_TEXT + TYPE_NN + COMMA +
                    COL_PRESENT + TYPE_INT + TYPE_NN +
                    RBR + SEMCOL;

    public static boolean setAttendance(ArrayList<Attendee> attendees,String dateId, SQLiteDatabase db){
        if(db.isReadOnly())
            return false;
        ContentValues cv = new ContentValues();
        for(Attendee attendee:attendees){
            cv.clear();
            cv.put(COL_DATE_ID,dateId);
            cv.put(COL_STUDENT_ID,attendee.getId());
            if(attendee.isPresent())
                cv.put(COL_PRESENT,1);
            else
                cv.put(COL_PRESENT,0);
            db.insert(TABLE_NAME,null,cv);
        }
        return true;
    }

    public static boolean delAttendances(SQLiteDatabase db, String dateId){
        if(db.isReadOnly())
            return false;
        String whereClause = COL_DATE_ID + " = ? ";
        db.delete(
                TABLE_NAME,
                whereClause,
                new String[]{dateId}
        );
        return true;
    }

    public static boolean delStudentAttendances(SQLiteDatabase db, String studentId){
        if(db.isReadOnly())
            return false;
        String whereClause = COL_STUDENT_ID + " = ? ";
        db.delete(
                TABLE_NAME,
                whereClause,
                new String[]{studentId}
        );
        return true;
    }

    public static HashMap<Date,String> getStudentAttendances(SQLiteDatabase db, String id){
        HashMap<Date,String> dates = new HashMap<>();
        Cursor c = db.query(
                TableAttendance.TABLE_NAME + " , " + TableDates.TABLE_NAME,
                new String[]{TableAttendance.COL_PRESENT,TableDates.COL_DATE},
                TableAttendance.COL_DATE_ID + " = " + TableDates.COL_DATE_ID + " AND " + TableAttendance.COL_STUDENT_ID + " = "+id ,
                null,null,null,null
        );
        int dateIndex = c.getColumnIndex(TableDates.COL_DATE);
        int presentIndex = c.getColumnIndex(TableAttendance.COL_PRESENT);
        for(c.moveToFirst();!c.isAfterLast();c.moveToNext()) {
            if(c.getInt(presentIndex)==0)
                dates.put(Date.valueOf(c.getString(dateIndex)),"A");
            else
                dates.put(Date.valueOf(c.getString(dateIndex)),"P");
        }
        c.close();
        return dates;
    }

    public static ArrayList<Attendee> getDateAttendances(SQLiteDatabase db, String dateId){
        ArrayList<Attendee> attendees = new ArrayList<>();
        Cursor c = db.query(
                TableAttendance.TABLE_NAME + " , " + TableStudents.TABLE_NAME,
                new String[]{TableAttendance.COL_PRESENT,TableStudents.COL_ID,TableStudents.COL_ROLL_NO,TableStudents.COL_NAME},
                TableAttendance.COL_STUDENT_ID + " = " + TableStudents.COL_ID + " AND " + TableAttendance.COL_DATE_ID + " = "+dateId,
                null,null,null,null
        );

        int presentIndex  = c.getColumnIndex(TableAttendance.COL_PRESENT);
        int idIndex = c.getColumnIndex(TableStudents.COL_ID);
        int rollNoIndex = c.getColumnIndex(TableStudents.COL_ROLL_NO);
        int nameIndex = c.getColumnIndex(TableStudents.COL_NAME);

        Boolean present;
        String id,rollNo,name;
        for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
            present = c.getInt(presentIndex)==1;
            id = c.getString(idIndex);
            rollNo = c.getString(rollNoIndex);
            name = c.getString(nameIndex);
            attendees.add(new Attendee(id,rollNo,name,present));
        }
        c.close();
        return attendees;
    }

    public static boolean addAttendance(SQLiteDatabase db,String studentId,String dateId,boolean present){
        if(db.isReadOnly())
            return false;
        ContentValues cv = new ContentValues();
        cv.put(COL_PRESENT,present);
        String whereClause = COL_DATE_ID + " = ? AND "+ COL_STUDENT_ID + " = ? ";
        db.update(TABLE_NAME, cv,whereClause,new String[]{dateId,studentId});
        return true;
    }
}
