package aarnav100.developer.attendancemanager.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import aarnav100.developer.attendancemanager.EditStudentActivity;
import aarnav100.developer.attendancemanager.Models.Student;
import aarnav100.developer.attendancemanager.R;
import aarnav100.developer.attendancemanager.StudentActivity;

/**
 * Created by aarnavjindal on 25/01/18.
 */

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private ArrayList<Student> students;
    private Context mContext;
    private LayoutInflater li;
    private View.OnClickListener ocl,ocl2;

    public StudentAdapter(final ArrayList<Student> students, final Context mContext) {
        this.students = students;
        this.mContext = mContext;
        this.li = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ocl = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = (String)view.getTag();
                Log.i("TAG",id);
                Intent i = new Intent(mContext, StudentActivity.class);
                i.putExtra("id",id);
                mContext.startActivity(i);
            }
        };
        ocl2 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = (String)view.getTag();
                Log.i("TAG",id);
                Intent i = new Intent(mContext, EditStudentActivity.class);
                i.putExtra("type","edit");
                i.putExtra("id",id);
                mContext.startActivity(i);
            }
        };
    }

    @Override
    public StudentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = li.inflate(R.layout.student_list_view,parent,false);
        return new StudentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(StudentViewHolder holder, int position) {
        Student std = students.get(position);
        holder.studentName.setText(std.getStudentName());
        holder.studentId.setText(std.getRollNo());

        holder.itemView.setOnClickListener(ocl);
        holder.itemView.setTag(std.getTableId());
        holder.editStudent.setOnClickListener(ocl2);
        holder.editStudent.setTag(std.getTableId());
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    class StudentViewHolder extends RecyclerView.ViewHolder{
        TextView studentName,studentId;
        ImageView editStudent;

        public StudentViewHolder(View itemView) {
            super(itemView);
            this.studentName = itemView.findViewById(R.id.student_name);
            this.studentId = itemView.findViewById(R.id.student_id);
            this.editStudent = itemView.findViewById(R.id.edit_student);
        }
    }

    public void setStudents(ArrayList<Student> students){
        this.students = students;
        notifyDataSetChanged();
    }
}
