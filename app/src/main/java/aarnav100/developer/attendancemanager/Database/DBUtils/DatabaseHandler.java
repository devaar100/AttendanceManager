package aarnav100.developer.attendancemanager.Database.DBUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import aarnav100.developer.attendancemanager.Database.Tables.*;
import aarnav100.developer.attendancemanager.Generic.FileUtils;

/**
 * Created by aarnavjindal on 25/01/18.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    public static final String DB_NAME =  "attendance.db";
    public static final int DB_VER = 1;


    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TableAttendance.CMD_CREATE_ATTENDANCE_TABLE);
        db.execSQL(TableDates.CMD_CREATE_DATE_TABLE);
        db.execSQL(TableStudents.CMD_CREATE_STUDENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
