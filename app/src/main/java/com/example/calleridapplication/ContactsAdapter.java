package com.example.calleridapplication;

import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.CallerIdApplication.R;

import java.util.List;

public class ContactsAdapter extends ArrayAdapter<ContactModel> {
    private Context mContext;
    private int mResource;
    List<ContactModel> contacts;
    // Used for the ViewHolder pattern
    // https://developer.android.com/training/improving-layouts/smooth-scrolling
    static class ViewHolder {
        TextView fullname;
        TextView company;
        TextView job;
        TextView phonenbre;
    }

    public ContactsAdapter(Context context, int resource, List<ContactModel> contacts) {
        super(context, resource, contacts);
        mContext = context;
        mResource = resource;
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ContactModel contact = getItem(position);

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);

            holder = new ViewHolder();
            holder.fullname = convertView.findViewById(R.id.fullname);
            holder.company = convertView.findViewById(R.id.company);
            holder.job = convertView.findViewById(R.id.job);
            holder.phonenbre= convertView.findViewById(R.id.phonenbre);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
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