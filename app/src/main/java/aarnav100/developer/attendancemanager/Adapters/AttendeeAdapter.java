package aarnav100.developer.attendancemanager.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import aarnav100.developer.attendancemanager.Models.Attendee;
import aarnav100.developer.attendancemanager.R;

/**
 * Created by aarnavjindal on 26/01/18.
 */

public class AttendeeAdapter extends RecyclerView.Adapter<AttendeeAdapter.AttendeeHolder> {
    private Context mContext;
    private ArrayList<Attendee> attendees;
    private LayoutInflater li;

    public AttendeeAdapter(Context mContext, ArrayList<Attendee> attendees) {
        this.mContext = mContext;
        this.attendees = attendees;
        this.li = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public AttendeeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AttendeeHolder(li.inflate(R.layout.attendee_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(AttendeeHolder holder, int position) {
        final Attendee attendee = attendees.get(position);
        holder.tvName.setText(attendee.getName());
        String id = attendee.getRollNo();
        if(id.length()==1)
            id = " "+id;
        holder.tvId.setText(id);
        if(attendee.isPresent())
            holder.rbPresent.setChecked(true);
        else
            holder.rbAbsent.setChecked(true);
        holder.rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(radioGroup.getCheckedRadioButtonId()){
                    case R.id.present:
                        attendee.setPresent(true);
                        break;
                    case R.id.absent:
                        attendee.setPresent(false);
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return attendees.size();
    }

    class AttendeeHolder extends RecyclerView.ViewHolder{
        TextView tvName,tvId;
        RadioButton rbPresent,rbAbsent;
        RadioGroup rg;

        public AttendeeHolder(View itemView) {
            super(itemView);
            this.tvName = itemView.findViewById(R.id.attendee_name);
            this.tvId = itemView.findViewById(R.id.attendee_id);
            this.rbPresent = itemView.findViewById(R.id.present);
            this.rbAbsent = itemView.findViewById(R.id.absent);
            this.rg = itemView.findViewById(R.id.rg);
        }
    }

    public ArrayList<Attendee> getList(){
        return attendees;
    }

    public void setList(ArrayList<Attendee> attendees){
        this.attendees = attendees;
        notifyDataSetChanged();
    }
}
