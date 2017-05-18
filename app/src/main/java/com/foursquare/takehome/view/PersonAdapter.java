package com.foursquare.takehome.view;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.foursquare.takehome.R;
import com.foursquare.takehome.model.Person;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


final public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {
    private static final String TAG = PersonAdapter.class.getSimpleName();
    private List<Person> visitorList;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa");


    public PersonAdapter(List<Person> list) {
        visitorList = list;
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);

        final ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String visitorName = visitorList.get(position).getName();
        final int textColor = visitorName == null ? Color.LTGRAY : Color.DKGRAY;
        final String label =   visitorName == null ? "No Visitors" : visitorList.get(position).getName();

        holder.personNameTextView.setText(label);
        holder.personNameTextView.setTextColor(textColor);

        final String timeStamp = dateFormat.format(new Date(visitorList.get(position).getArriveTime()*1000)) +
                " - " + dateFormat.format(new Date(visitorList.get(position).getLeaveTime()*1000));
        holder.timeStampTextView.setText(timeStamp);
        holder.timeStampTextView.setTextColor(textColor);

        Log.e(TAG, dateFormat.format(new Date(visitorList.get(position).getArriveTime()*1000))+" - "
                + dateFormat.format(new Date(visitorList.get(position).getLeaveTime()*1000))+" : "
                + visitorList.get(position).getName()
        );
    }

    @Override
    public int getItemCount() {
        if(visitorList == null) {
            return 0;
        }
        return visitorList.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        TextView personNameTextView;
        TextView timeStampTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            personNameTextView = (TextView) itemView.findViewById(R.id.name);
            timeStampTextView = (TextView) itemView.findViewById(R.id.timeStamp);
        }

    }
}
