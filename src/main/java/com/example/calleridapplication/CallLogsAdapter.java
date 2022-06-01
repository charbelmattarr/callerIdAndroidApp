package com.example.calleridapplication;
/*
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.CallerIdApplication.R;

import java.util.List;

public class CallLogsAdapter extends ArrayAdapter<ContactModel> {
    private Context mContext;
    private int mResource;
    List<CallLogs> contacts;
    // Used for the ViewHolder pattern
    // https://developer.android.com/training/improving-layouts/smooth-scrolling
    static class ViewHolder {
        TextView duration;
        TextView time;
        TextView contact;
    }

    public CallLogsAdapter(Context context, int resource, List<CallLogs> contacts) {
        super(context, resource, contacts);
        mContext = context;
        mResource = resource;
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ContactModel contact = getItem(position);

        ContactsAdapter.ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);

            holder = new ContactsAdapter.ViewHolder();
            holder.duration = convertView.findViewById(R.id.adapterduration);
            holder.time = convertView.findViewById(R.id.adaptertime);
            holder.contact = convertView.findViewById(R.id.adaptercontact);


            convertView.setTag(holder);
        } else {
            holder = (ContactsAdapter.ViewHolder) convertView.getTag();
        }

        holder.fullname.setText(contact.getContact_fname() + "-" +contact.getContact_lname());
        holder.company.setText(contact.getContact_company());
        holder.job.setText(contact.getContact_job());
        holder.phonenbre.setText(contact.getContact_mobilephone());
        return convertView;
    }

    // Convert Graph's DateTimeTimeZone format to
    // a LocalDateTime, then return a formatted string


}
*/

