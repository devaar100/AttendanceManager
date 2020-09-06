package aarnav100.developer.attendancemanager.Models;

/**
 * Created by aarnavjindal on 26/01/18.
 */

public class AttendanceDay {
    String date;
    String type; // H for holiday ; M for marked ; U for unmarked

    public AttendanceDay(String date, String type) {
        this.date = date;
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
