package aarnav100.developer.attendancemanager.Models;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by aarnavjindal on 25/01/18.
 */

public class Student {
    String studentName;
    String guardianRelation;
    String guardianName;
    String contactNumber;
    String tableId;
    String rollNo;

    public Student() {
    }

    public Student(String studentName, String rollNo, String guardianRelation, String guardianName, String contactNumber, String tableId) {
        this.studentName = studentName;
        this.guardianRelation = guardianRelation;
        this.guardianName = guardianName;
        this.contactNumber = contactNumber;
        this.tableId = tableId;
        this.rollNo = rollNo;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getGuardianRelation() {
        return guardianRelation;
    }

    public void setGuardianRelation(String guardianRelation) {
        this.guardianRelation = guardianRelation;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public static class Sort implements Comparator<Student> {
        public int compare(Student a, Student b){
            return Integer.valueOf(a.getRollNo()) - Integer.valueOf(b.getRollNo());
        }
    }
}
