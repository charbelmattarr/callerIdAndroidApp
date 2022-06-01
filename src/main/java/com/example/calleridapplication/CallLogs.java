package com.example.calleridapplication;

public class CallLogs {
    private String duration;
    private String time;
    private String date;
    private String phoneNbre;

    @Override
    public String toString() {
        return "CallLogs{" +
                "duration='" + duration + '\'' +
                ", time='" + time + '\'' +
                ", date='" + date + '\'' +
                ", phoneNbre='" + phoneNbre + '\'' +
                ", contactModel=" + contactModel +
                '}';
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhoneNbre() {
        return phoneNbre;
    }

    public void setPhoneNbre(String phoneNbre) {
        this.phoneNbre = phoneNbre;
    }

    public ContactModel getContactModel() {
        return contactModel;
    }

    public void setContactModel(ContactModel contactModel) {
        this.contactModel = contactModel;
    }

    public CallLogs(String duration, String time, String date, String phoneNbre, ContactModel contactModel) {
        this.duration = duration;
        this.time = time;
        this.date = date;
        this.phoneNbre = phoneNbre;
        this.contactModel = contactModel;
    }

    private ContactModel contactModel;


}
