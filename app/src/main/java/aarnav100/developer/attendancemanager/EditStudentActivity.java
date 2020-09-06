package aarnav100.developer.attendancemanager;

import android.content.DialogInterface;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import aarnav100.developer.attendancemanager.Database.DBUtils.DatabaseHandler;
import aarnav100.developer.attendancemanager.Database.Tables.TableAttendance;
import aarnav100.developer.attendancemanager.Database.Tables.TableStudents;
import aarnav100.developer.attendancemanager.Models.Student;

public class EditStudentActivity extends AppCompatActivity {
    private String type,id;
    private String name,gname,grel,number,rollNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);
        final DatabaseHandler dbHandler = new DatabaseHandler(this);
        final TextInputEditText et_name,et_number,et_gname,et_rollno;
        final Spinner spinner_grelation = findViewById(R.id.grd_rel);
        ArrayList<String> relations = new ArrayList<>();
        relations.add("Father");
        relations.add("Mother");
        relations.add("Other");
        spinner_grelation.setAdapter(new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,relations));
        et_name = findViewById(R.id.stu_name);
        et_number = findViewById(R.id.contact_num);
        et_gname = findViewById(R.id.grd_name);
        et_rollno = findViewById(R.id.stu_rollno);
        type = getIntent().getStringExtra("type");
        if(type.equals("add")){
            findViewById(R.id.edit_student).setVisibility(View.GONE);
            findViewById(R.id.del_student).setVisibility(View.GONE);
        } else{
            findViewById(R.id.add_student).setVisibility(View.GONE);
            id = getIntent().getStringExtra("id");
            Student std = TableStudents.getStudent(dbHandler.getReadableDatabase(),id);
            name = std.getStudentName();
            gname = std.getGuardianName();
            grel = std.getGuardianRelation();
            number = std.getContactNumber();
            rollNo = std.getRollNo();
            et_name.setText(name);
            et_gname.setText(gname);
            et_number.setText(number);
            et_rollno.setText(rollNo);
            spinner_grelation.setSelection(relations.indexOf(grel));
        }

        View.OnClickListener ocl = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.add_student:
                        name = et_name.getText().toString();
                        gname = et_gname.getText().toString();
                        grel = (String)spinner_grelation.getSelectedItem();
                        number = et_number.getText().toString();
                        rollNo = et_rollno.getText().toString();
                        if(name.equals("")||gname.equals("")||grel.equals("")||number.equals("")||rollNo.equals("")){
                            Toast.makeText(EditStudentActivity.this, "Please fill all details", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        et_name.setText("");
                        et_gname.setText("");
                        et_number.setText("");
                        et_rollno.setText("");
                        if(TableStudents.addStudent(dbHandler.getWritableDatabase(),new Student(name,rollNo,grel,gname,number,null))){
                            Toast.makeText(EditStudentActivity.this, "Student added", Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(EditStudentActivity.this, "Error. Please try again", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.edit_student:
                        name = et_name.getText().toString();
                        gname = et_gname.getText().toString();
                        grel = (String)spinner_grelation.getSelectedItem();
                        number = et_number.getText().toString();
                        rollNo = et_rollno.getText().toString();
                        Student student = new Student(name,rollNo,grel,gname,number,id);
                        if(TableStudents.updateStudent(dbHandler.getWritableDatabase(),student))
                            Toast.makeText(EditStudentActivity.this, "Student details edited successfully", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(EditStudentActivity.this, "Error. Try again", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.del_student:
                        new AlertDialog.Builder(EditStudentActivity.this)
                                .setMessage("Are you sure ? Deleting the student will delete all the existing records of this student")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        TableStudents.delStudent(dbHandler.getWritableDatabase(),id);
                                        TableAttendance.delStudentAttendances(dbHandler.getWritableDatabase(),id);
                                        Toast.makeText(EditStudentActivity.this, "Student deleted", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }).show();
                        break;
                }
            }
        };
        findViewById(R.id.add_student).setOnClickListener(ocl);
        findViewById(R.id.edit_student).setOnClickListener(ocl);
        findViewById(R.id.del_student).setOnClickListener(ocl);
    }
}
