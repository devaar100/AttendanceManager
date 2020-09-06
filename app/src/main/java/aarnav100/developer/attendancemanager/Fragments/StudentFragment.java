package aarnav100.developer.attendancemanager.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import aarnav100.developer.attendancemanager.Adapters.StudentAdapter;
import aarnav100.developer.attendancemanager.Database.DBUtils.DatabaseHandler;
import aarnav100.developer.attendancemanager.Database.Tables.TableStudents;
import aarnav100.developer.attendancemanager.EditStudentActivity;
import aarnav100.developer.attendancemanager.MainActivity;
import aarnav100.developer.attendancemanager.Models.Student;
import aarnav100.developer.attendancemanager.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class StudentFragment extends Fragment {
    private ArrayList<Student> students;
    private RecyclerView studentRecyclerView;
    private StudentAdapter adapter;
    private FloatingActionButton fab;
    private DatabaseHandler dbHandler;

    public StudentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_students, container, false);
        final Context mContext = getContext();
        studentRecyclerView = fragmentView.findViewById(R.id.student_recycler_view);
        adapter = new StudentAdapter(students, (MainActivity) getActivity());
        dbHandler = new DatabaseHandler(mContext);


        fab = ((MainActivity)getActivity()).findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, EditStudentActivity.class);
                i.putExtra("type","add");
                mContext.startActivity(i);
            }
        });

        studentRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        studentRecyclerView.setAdapter(adapter);

        return fragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        students = TableStudents.getStudents(dbHandler.getReadableDatabase());
        Collections.sort(students,new Student.Sort());
        adapter.setStudents(students);
    }
}
