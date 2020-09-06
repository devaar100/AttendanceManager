package aarnav100.developer.attendancemanager.Database.Tables;

/**
 * Created by aarnavjindal on 26/01/18.
 */
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLData;
import java.util.ArrayList;

import aarnav100.developer.attendancemanager.Models.Attendee;
import aarnav100.developer.attendancemanager.Models.Student;

import static aarnav100.developer.attendancemanager.Database.DBUtils.DBConsts.*;

public class TableStudents {
    public static final String TABLE_NAME = "students";
    public static final String COL_ID = "student_id";
    public static final String COL_NAME = "student_name";
    public static final String COL_GUARDIAN = "student_gaurdian";
    public static final String COL_NUMBER = "student_number";
    public static final String COL_ROLL_NO = "student_rollno";


    public static final String CMD_CREATE_STUDENT_TABLE =
            "CREATE TABLE " + TABLE_NAME +
                    LBR +
                    COL_ID + TYPE_INT + TYPE_PK + TYPE_AI + COMMA +
                    COL_ROLL_NO + TYPE_TEXT + COMMA +
                    COL_NAME + TYPE_TEXT + COMMA +
                    COL_GUARDIAN + TYPE_TEXT + COMMA +
                    COL_NUMBER + TYPE_TEXT +
                    RBR + SEMCOL;

    public static boolean addStudent(SQLiteDatabase db, Student std){
        if(db.isReadOnly())
            return false;
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME,std.getStudentName());
        cv.put(COL_ROLL_NO,std.getRollNo());
        cv.put(COL_GUARDIAN,std.getGuardianRelation()+":"+std.getGuardianName());
        cv.put(COL_NUMBER,std.getContactNumber());
        db.insert(TABLE_NAME,null,cv);
        return true;
    }

    public static boolean delStudent(SQLiteDatabase db,String id){
        if(db.isReadOnly())
            return false;
        String whereClause = COL_ID + " = ? ";
        db.delete(TABLE_NAME,whereClause,new String[]{id});
        return true;
    }

    public static boolean updateStudent(SQLiteDatabase db,Student student){
        if(db.isReadOnly())
            return false;
        String whereClause = COL_ID + " = ? ";
        ContentValues cv = new ContentValues();
        cv.put(COL_ROLL_NO,student.getRollNo());
        cv.put(COL_NUMBER,student.getContactNumber());
        cv.put(COL_NAME,student.getStudentName());
        cv.put(COL_GUARDIAN,student.getGuardianRelation()+":"+student.getGuardianName());
        db.update(
                TABLE_NAME,
                cv,
                whereClause,
                new String[]{student.getTableId()}
        );
        return true;
    }

    public static ArrayList<Student> getStudents(SQLiteDatabase db){
        ArrayList<Student> students= new ArrayList<>();
        String[] projection = new String[]{COL_ID,COL_ROLL_NO,COL_NAME,COL_GUARDIAN,COL_NUMBER};
        Cursor c = db.query(
                TABLE_NAME,
                projection,
                null,null,null,null,null
        );
        int studentId = c.getColumnIndex(COL_ID);
        int nameIndex = c.getColumnIndex(COL_NAME);
        int gaurdianIndex = c.getColumnIndex(COL_GUARDIAN);
        int numberIndex = c.getColumnIndex(COL_NUMBER);
        int rollNoIndex = c.getColumnIndex(COL_ROLL_NO);

        String name,gaurdianRelation,gaurdianName,number,guardian[],id,rollNo;
        for(c.moveToFirst();!c.isAfterLast();c.moveToNext()) {
            name = c.getString(nameIndex);
            guardian = c.getString(gaurdianIndex).split(":");
            gaurdianRelation = guardian[0];
            gaurdianName = guardian[1];
            number = c.getString(numberIndex);
            id = c.getString(studentId);
            rollNo = c.getString(rollNoIndex);
            students.add(new Student(name, rollNo, gaurdianRelation, gaurdianName, number,id));
        }

        c.close();
        return students;
    }

    public static ArrayList<Attendee> getStudentNames(SQLiteDatabase db){
        ArrayList<Attendee> students= new ArrayList<>();
        String[] projection = new String[]{COL_ID,COL_ROLL_NO,COL_NAME};
        Cursor c = db.query(
                TABLE_NAME,
                projection,
                null,null,null,null,null
        );
        int nameIndex = c.getColumnIndex(COL_NAME);
        int rollNoIndex = c.getColumnIndex(COL_ROLL_NO);
        int tableId = c.getColumnIndex(COL_ID);
        String name,id,rollNo;

        for(c.moveToFirst();!c.isAfterLast();c.moveToNext()) {
            name = c.getString(nameIndex);
            id = c.getString(tableId);
            rollNo = c.getString(rollNoIndex);
            students.add(new Attendee(id,rollNo,name,false));
        }
        c.close();
        return students;
    }

    public static Student getStudent(SQLiteDatabase db,String id){
        String[] projection = new String[]{COL_ID,COL_ROLL_NO,COL_NAME,COL_GUARDIAN,COL_NUMBER};
        Cursor c = db.query(
                TABLE_NAME,
                projection,
                COL_ID + " = "+id,
                null,null,null,null
        );
        int studentId = c.getColumnIndex(COL_ID);
        int nameIndex = c.getColumnIndex(COL_NAME);
        int rollNoIndex = c.getColumnIndex(COL_ROLL_NO);
        int gaurdianIndex = c.getColumnIndex(COL_GUARDIAN);
        int numberIndex = c.getColumnIndex(COL_NUMBER);

        c.moveToFirst();
        Student s = new Student();
        s.setStudentName(c.getString(nameIndex));
        String[] guardian = c.getString(gaurdianIndex).split(":");
        s.setGuardianRelation(guardian[0]);
        s.setGuardianName(guardian[1]);
        s.setTableId(c.getString(studentId));
        s.setContactNumber(c.getString(numberIndex));
        s.setRollNo(c.getString(rollNoIndex));
        c.close();
        return s;
    }
}
