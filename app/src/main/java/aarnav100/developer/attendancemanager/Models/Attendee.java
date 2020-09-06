package aarnav100.developer.attendancemanager.Models;

import java.util.Comparator;

/**
 * Created by aarnavjindal on 27/01/18.
 */

public class Attendee {
    String name;
    String id,rollNo;
    boolean present;

    public Attendee(String id,String rollNo, String name, boolean present) {
        this.name = name;
        this.id = id;
        this.present = present;
        this.rollNo = rollNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public static class Sort implements Comparator<Attendee> {
        public int compare(Attendee a, Attendee b){
            return Integer.valueOf(a.getRollNo()) - Integer.valueOf(b.getRollNo());
        }
    }
}
